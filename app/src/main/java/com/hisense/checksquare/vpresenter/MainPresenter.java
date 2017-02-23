package com.hisense.checksquare.vpresenter;

import com.hisense.checksquare.base.IMainConstract;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckEntityGroup;
import com.hisense.checksquare.entity.ParseEntity;
import com.hisense.checksquare.widget.StringUtil;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.hisense.checksquare.widget.LogUtil;

import org.reactivestreams.Publisher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.flowable.FlowableFromObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by yanglijun.ex on 2017/2/15.
 */

public class MainPresenter implements MainPresenterInteract{

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    IMainConstract.IMainView view;
    IMainConstract.IMainDomain domain;
    private CopyOnWriteArrayList<CheckEntity> viewDatas;

    public MainPresenter(IMainConstract.IMainView view, IMainConstract.IMainDomain domain) {
        this.view = view;
        this.domain = domain;
        init();
    }

    private void init() {
        // 注册
        Bus.getDefault().register(this);
    }

    public void release(){
        view = null;
        // 取消bus注册
        Bus.getDefault().unregister(this);
        // 取消rx
        unSubscribe();
        if (null != viewDatas && !viewDatas.isEmpty()) {
            viewDatas.clear();
        }
    }

    private void unSubscribe() {
        mCompositeDisposable.clear();
    }

    private void register(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    /**
     * get init datas
     */
    void getCheckDatas(){
        LogUtil.d("getCheckDatas() enter ---->");

            register(domain.getCheckDatas()
                    .subscribeWith(new DisposableObserver<List<CheckEntity>>() {

                        @Override
                        public void onNext(List<CheckEntity> entities) {
                            LogUtil.d("onNext: accept <---- entities = %s", entities);
                            if (null != entities & !entities.isEmpty()) {
                                view.onDatasReceived(entities);
                                updateViewDatas(entities);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e("onError() accept <---- e = %s", e.toString());
                        }

                        @Override
                        public void onComplete() {
                            LogUtil.d("onComplete() accept <---- ");
                        }
                    })

            );
    }

    /**
     * cache the adapter datas in presenter
     * @param entities
     */
    private void updateViewDatas(List<CheckEntity> entities) {
        if (null != viewDatas) {
            viewDatas.clear();
            viewDatas.addAll(entities);
        } else {
            viewDatas = new CopyOnWriteArrayList<>(entities);
        }
    }

    /**
     * one key check, need to filter the undo ones
     */
    void onOneKeyCheckEntities(List<CheckEntity> adapterEntities){
        LogUtil.d("onOneKeyCheckEntities() enter  adapterEntities = %s", adapterEntities);
        if (null != adapterEntities && !adapterEntities.isEmpty()) {
            onFilteOneKeyEntities(adapterEntities);
        }
    }

    private void onFilteOneKeyEntities(final List<CheckEntity> adapterEntities){
//        new Thread(new FilteTask(this, adapterEntities)).start();

        final List<CheckEntity> toCheckProps = new ArrayList<>();
        final List<CheckEntity> toCheckfuncs = new ArrayList<>();

        register(FlowableFromObservable
                .fromIterable(adapterEntities)
                .filter(new Predicate<CheckEntity>() {
                    @Override
                    public boolean test(CheckEntity entity) throws Exception {
                        LogUtil.d("onFilteOneKeyEntities(): filter() ----> %s", entity.checkStatus);
                        boolean b = CheckEntity.CHECK_STATUS_TOCHECK
                                .equalsIgnoreCase(entity.checkStatus);
                        return b;
                    }
                })
                .map(new Function<CheckEntity, CheckEntity>() {
                    @Override
                    public CheckEntity apply(CheckEntity entity) throws Exception {
                        LogUtil.d("onFilteOneKeyEntities(): map() ----> %s", entity.type);
                        // group the entity
                        if (CheckEntity.TYPE_ITEM_VIEW_HW == entity.type) {
                            toCheckProps.add(entity);
                        } else if (CheckEntity.TYPE_ITEM_VIEW_FUNC == entity.type) {
                            toCheckfuncs.add(entity);
                        }
                        return entity;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CheckEntity>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        LogUtil.d("onFilteOneKeyEntities(): onStart() ---->");
                    }

                    @Override
                    public void onNext(CheckEntity entity) {
                        LogUtil.d("onFilteOneKeyEntities(): onNext():");

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.d("onFilteOneKeyEntities(): onError():");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d("onFilteOneKeyEntities(): onComplete() <----: toCheckProps.size = %d, toCheckfuncs.size = %d", toCheckProps.size(), toCheckfuncs.size());
                        onFilteEntitiesDone(new CheckEntityGroup(toCheckProps, toCheckfuncs));
                    }
                })
        );
    }

    void onFilteEntitiesDone(CheckEntityGroup checkEntityGroup) {
        // TODO: 2017/2/21 only check prop items now
        if (null != view) {
            List<CheckEntity> propEntities = checkEntityGroup.propEntities;
            if (propEntities != null && !propEntities.isEmpty()) {
                // notify dialog to add wait count
                notifyAddCheckCount(propEntities.size());

                // do checks, prop items first
                toCheckProperties(propEntities);
            }
        }
    }

    /**
     * check property items, drive by one key
     * @param entities
     */
    private void toCheckProperties(List<CheckEntity> entities) {
        if (null == entities) {
            return;
        }

        int count = entities.size();
        LogUtil.d("toCheckProperties(): enter count = %d", count);

        for (CheckEntity entity : entities) {
//            toCheckProperty(entity, true);
            if (entity.type == CheckEntity.TYPE_ITEM_VIEW_HW) {
                toCheckProperty(entity, true);
            } else {
                addFuncQueue(entity);
            }
        }
    }


    /**
     * do check a hw entity: properties
     * @param entity
     */
    public void toCheckProperty(CheckEntity entity) {
        toCheckProperty(entity, false);
    }

    /**
     * do check a entity property
     * @param entity
     * @param auto if check by auto, like one key click
     */
    public void toCheckProperty(CheckEntity entity, final boolean auto) {
        final CheckEntity tempEn = entity;
        register(domain.toCheckProperty(entity)
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CheckEntity>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        LogUtil.d("toCheckProperty onStart(): ----> checkName = %s", tempEn.checkName);
                        // update the checking status
                        if (null != view) {
                            tempEn.checkStatus = CheckEntity.CHECK_STATUS_CHECKING;
                            notifyItemStatus(tempEn);
                        }

                        if (!auto) {
                            // notify the dialog
                            notifyAddCheckCount(1);
                        }
                    }

                    @Override
                    public void onNext(CheckEntity entity) {
                        LogUtil.d("toCheckProperty onNext(): entity = %s", entity);
                        // update the ok/fail status
                        if (null != view) {
                            String checkResult = entity.checkResult;
                            if (!StringUtil.isEmpty(checkResult)) {
                                entity.checkStatus = checkResult;
                            } else {
                                entity.checkStatus = CheckEntity.CHECK_STATUS_FAIL;
                            }
                            notifyItemStatus(entity);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.d("toCheckProperty onError(): checkName = %s, t = %s", tempEn.checkName, t.toString());
                        // update the fail status
                        if (null != view) {
                            tempEn.checkStatus = CheckEntity.CHECK_STATUS_FAIL;
                            notifyItemStatus(tempEn);
                        }
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d("toCheckProperty onComplete() <----: checkName = %s", tempEn.checkName);
                    }
                })
        );
    }

    private void notifyAddCheckCount(int count) {
        if (null != view) {
            view.onAddCheckCount(count);
        }
    }

    private void notifyItemStatus(CheckEntity en) {
        view.onCheckItemStatus(en.checkStatus, en);
    }

    /**
     * do check a func entity: interfaces
     * @param entity
     */
    private void addFuncQueue(CheckEntity entity) {

    }

    /**
     * check funcs items, poll from queue
     * @param entities
     */
    void toCheckFunctions(List<CheckEntity> entities){
        int count = entities != null ? entities.size() : 0;
        LogUtil.d("toCheckFunctions() enter ----> count = %d", count);
    }

    /**
     * Bus 事件
     * @param parseEntity
     */
    @BusReceiver
    public void onParseEntityEvent(ParseEntity parseEntity) {
        LogUtil.d(parseEntity);
//        Bus.getDefault().post(new Object());
    }



    /**
     * MainPresenterInteract
     */
    @Override
    public void onCheckPropertiesDone() {

    }

    /**
     * after one key ,
     * filter the adapter datas to check
     */
    private static class FilteTask implements Runnable {

        private WeakReference<MainPresenter> reference;
        private List<CheckEntity> adapterEntities;

        public FilteTask(MainPresenter reference, List<CheckEntity> adapterEntities) {
            this.reference = new WeakReference<MainPresenter>(reference);
            this.adapterEntities = new ArrayList<>(adapterEntities);
        }

        @Override

        public void run() {
            List<CheckEntity> toCheckProps = new ArrayList<>();
            List<CheckEntity> toCheckfuncs = new ArrayList<>();
            for (CheckEntity entity : adapterEntities) {
                if (CheckEntity.CHECK_STATUS_TOCHECK
                        .equalsIgnoreCase(entity.checkStatus)) {

                        if (CheckEntity.TYPE_ITEM_VIEW_HW == entity.type) {
                            toCheckProps.add(entity);
                        } else if (CheckEntity.TYPE_ITEM_VIEW_FUNC == entity.type) {
                            toCheckfuncs.add(entity);
                        }
                    }
                }
                MainPresenter mainPresenter = reference.get();
                if (mainPresenter != null) {
                    if (!toCheckfuncs.isEmpty() || !toCheckProps.isEmpty()) {
                        CheckEntityGroup checkEntityGroup = new CheckEntityGroup(toCheckProps, toCheckfuncs);
                        mainPresenter.onFilteEntitiesDone(checkEntityGroup);
                    }
                }
            }

    }

}
