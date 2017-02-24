package com.hisense.checksquare.vpresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hisense.checksquare.R;
import com.hisense.checksquare.base.IMainConstract;
import com.hisense.checksquare.dagger.mactivity.DaggerMainComponent;
import com.hisense.checksquare.dagger.mactivity.MainModule;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.ItemClickAdapter;
import com.hisense.checksquare.widget.LogUtil;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableFromObservable;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity implements IMainConstract.IMainView , View.OnClickListener{

    @Inject MainPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private ItemClickAdapter itemClickAdapter;
    private Context mContext;
    private AtomicInteger mCheckTotal = new AtomicInteger();
    private AtomicInteger mCheckDone = new AtomicInteger();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initRView();
        initDaggerObjects();

        getDatasAsync();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CheckEntity> adapterEntities = itemClickAdapter.getData();
                if (null != adapterEntities && !adapterEntities.isEmpty()) {
                    List<CheckEntity> entities = new ArrayList<>(adapterEntities);
                    if (null != mPresenter) {
                        // one key check
                        mPresenter.onOneKeyCheckEntities(entities);
                        // show tip
                        Snackbar.make(view, "CHECK PROPERTY ITEMS", Snackbar.LENGTH_SHORT)
                                .setAction("Ok", MainActivity.this).show();
                    }
                }
            }
        });
    }

    private void initDaggerObjects() {
        DaggerMainComponent.builder().mainModule(new MainModule(this)).build().inject(this);
    }

    private void initRView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             *
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter,
             *                 can use to update the item like setData(index, data) etc.
             */
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                LogUtil.d("onSimpleOnItemClick: position = %d", position);

                Object item = adapter.getItem(position);
                if (item instanceof CheckEntity) {
                    CheckEntity entity = (CheckEntity) item;
                    String checkStatus = entity.checkStatus;
                    // call check domain
                    if (CheckEntity.CHECK_STATUS_TOCHECK.equalsIgnoreCase(checkStatus)) {
                        mPresenter.toCheckProperty(entity);
                    }
                    // toast strTip
                    String strTip = "";
                    if (CheckEntity.CHECK_STATUS_TOCHECK.equalsIgnoreCase(checkStatus) || CheckEntity.CHECK_STATUS_CHECKING.equalsIgnoreCase(checkStatus)) {
                        strTip = getString(R.string.check_status_to_do, entity.checkName)  ;
                    } else if (CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(checkStatus) || CheckEntity.CHECK_STATUS_FAIL.equalsIgnoreCase(checkStatus)) {
                        strTip = getString(R.string.check_status_done, entity.checkName)  ;
                    }
                    Snackbar.make(view, strTip, Snackbar.LENGTH_SHORT)
                            .setAction("Ok", MainActivity.this).show();
//                    Toast.makeText(MainActivity.this, strTip, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        List<CheckEntity> data = new ArrayList<>();
        /*mock data*/
//        data.add(new CheckEntity(CheckEntity.TYPE_ITEM_VIEW_HW));
//        data.add(new CheckEntity(CheckEntity.TYPE_ITEM_VIEW_FUNC));
        itemClickAdapter = new ItemClickAdapter(mContext, data);
        itemClickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(itemClickAdapter);
    }

    private void getDatasAsync() {
        LogUtil.d("getDatasAsync()");
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getCheckDatas();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.release();
            mPresenter = null;
        }
        dismCheckDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                LogUtil.d("onClick() R.id.fab");

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * implement IMainView
     */
    @Override
    public void onDatasReceived(List<CheckEntity> entities) {
        LogUtil.d("onDatasReceived(): entities = %s", entities);
        itemClickAdapter.setNewData(entities);

    }

    /**
     * implement IMainView
     */
    @Override
    public void onCheckItemStatus(String status, CheckEntity entity) {
        LogUtil.d("onCheckItemStatus(): status = %s, entity = %s", status, entity);
        if (CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(status)
                || CheckEntity.CHECK_STATUS_FAIL.equalsIgnoreCase(status)) {
            // update the counters and dialog
            int get = mCheckDone.incrementAndGet();
            showCheckDialog();
            if (get == mCheckTotal.get()) {
                initCheckCouter();
                dismCheckDialog();
            }
        }
        // update RView with any status
        int entityPosition = itemClickAdapter.getEntityPosition(entity);
        itemClickAdapter.setData(entityPosition, entity);
    }

    /**
     * implement IMainView
     */
    @Override
    public void onCheckAllResults() {
        LogUtil.d("onCheckAllResults()");
    }

    /**
     * implement IMainView
     */
    @Override
    public void onAddCheckCount(int size) {
        int checkTotal = mCheckTotal.addAndGet(size);
        showCheckDialog();
    }

    private void initCheckCouter(){
        mCheckDone.set(0);
        mCheckTotal.set(0);
    }

    private void showCheckDialog() {
        if (null == dialog || !dialog.isShowing()) {
            dialog = ProgressDialog.show(this, "Checking"
                    , new StringBuilder().append(mCheckDone.get()).append("/").append(mCheckTotal.get())
                    , true);
        } else {
            dialog.setMessage(new StringBuilder().append(mCheckDone.get()).append("/").append(mCheckTotal.get()));
        }
    }

    private void dismCheckDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
