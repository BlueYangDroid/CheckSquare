package com.hisense.checksquare.mdomain;

import com.bluelinelabs.logansquare.LoganSquare;
import com.hisense.checksquare.base.IMainConstract;
import com.hisense.checksquare.dagger.mdomain.DaggerMainDomainComponent;
import com.hisense.checksquare.dagger.mdomain.MainDomainComponent;
import com.hisense.checksquare.dagger.mdomain.MainDomainModule;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.ParseEntity;
import com.hisense.checksquare.widget.AssetUtil;
import com.hisense.checksquare.command.CheckCommandRouter;
import com.hisense.checksquare.widget.Constants;
import com.hisense.checksquare.widget.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableFromObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yanglijun.ex on 2017/2/16.
 */

public class MainDomain implements IMainConstract.IMainDomain {

    private final MainDomainComponent domainComponent;

    public MainDomain() {
        domainComponent = DaggerMainDomainComponent.builder().mainDomainModule(new MainDomainModule()).build();
        domainComponent.inject(this);
    }

    private CheckCommandRouter getCheckCommandRouter() {
        if (null != domainComponent) {
            return domainComponent.mCheckRouter();
        }
        return null;
    }

    @Override
    public Observable<List<CheckEntity>> getCheckDatas() {
        return Observable.fromCallable(new Callable<ParseEntity>() {
                    @Override
                    public ParseEntity call() throws Exception {
                        try {
                            InputStream streamFromAssets = AssetUtil.getStreamFromAssets(AssetUtil.DEFAULT_ASSET_FILENAME);
                            LogUtil.d("streamFromAssets = %s", streamFromAssets);
                            return LoganSquare.parse(streamFromAssets, ParseEntity.class);
                        } catch (IOException e) {
                            LogUtil.e("IOException e = %s", e.getMessage());
                        }
                        return null;
                    }
                })
                .map(new Function<ParseEntity, List<CheckEntity>>() {
                    @Override
                    public List<CheckEntity> apply(ParseEntity parseEntity) throws Exception {
                        LogUtil.d("ParseEntity = %s", parseEntity);
                        if (null != parseEntity) {
                            List<CheckEntity> checkEntities = parseEntity.checkEntities;
                            if (null != checkEntities & !checkEntities.isEmpty()) {
                                LogUtil.d("checkEntities.size = %d, %s", checkEntities.size(), checkEntities);
                                return checkEntities;
                            }
                        }
                        return new ArrayList<CheckEntity>();
                    }
                })
                .timeout(Constants.TIME_OUT_INIT_DATA, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     *  check property
     * @param entity
     * @return
     */
    @Override
    public Flowable<CheckEntity> toCheckProperty(CheckEntity entity) {
        final CheckEntity tEntity = entity;
        return FlowableFromObservable
                .fromCallable(new Callable<CheckEntity>() {
                    @Override
                    public CheckEntity call() throws Exception {
                        // do check
                        CheckCommandRouter router = getCheckCommandRouter();
                        if (null != router) {
                            return router.checkProp(tEntity);
                        }

                        return null;
                    }
                })
                .timeout(Constants.TIME_OUT_CHECK_PROPERTY, TimeUnit.MILLISECONDS);

    }

    /**
     * check functions
     * @param entity
     */
    public void toCheckFunctions(CheckEntity entity){

    }
}
