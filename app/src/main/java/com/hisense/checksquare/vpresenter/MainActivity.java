package com.hisense.checksquare.vpresenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hisense.checksquare.R;
import com.hisense.checksquare.base.IMainConstract;
import com.hisense.checksquare.dagger.mactivity.DaggerMainComponent;
import com.hisense.checksquare.dagger.mactivity.MainModule;
import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.widget.ItemClickAdapter;
import com.hisense.checksquare.widget.SnackbarUtil;
import com.hisense.checksquare.widget.log.LogUtil;
import com.hisense.checksquare.widget.log.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements IMainConstract.IMainView , View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
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
        LogUtil.tag(TAG).d("displayed time --> onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.tag(TAG).d("displayed time --> onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.tag(TAG).d("displayed time --> onResume()");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOneKeyCheck(view);
            }
        });
    }

    private void onOneKeyCheck(View view) {
        List<CheckEntity> adapterEntities = itemClickAdapter.getData();
        if (null != adapterEntities && !adapterEntities.isEmpty()) {
            List<CheckEntity> entities = new ArrayList<>(adapterEntities);
            if (null != mPresenter) {
                // one key check
                mPresenter.onOneKeyCheckEntities(entities);
                // show tip App.getAppContext().getCurActivity().getWindow().getDecorView()
                SnackbarUtil.make("CHECK PROPERTY ITEMS", Snackbar.LENGTH_SHORT)
                        .setAction("Ok", MainActivity.this).show();
            }
        }
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
                    if (CheckEntity.CHECK_STATUS_TOCHECK.equalsIgnoreCase(checkStatus) || CheckEntity.CHECK_STATUS_FAIL.equalsIgnoreCase(checkStatus) || StringUtil.isEmpty(checkStatus)) {
                        mPresenter.toCheckProperty(entity);
                    }
                    // toast strTip
                    String strTip = "";
                     if (CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(checkStatus)) {
                        strTip = getString(R.string.check_status_done, entity.checkName)  ;
                    } else {
                        strTip = getString(R.string.check_status_to_do, entity.checkName)  ;
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
        LogUtil.d("getDatasAsync() begin ---->");
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getCheckDatas();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("onStop()");

    }

    private void testWindowManager() {
    /* // MainActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView
    new AlertDialog.Builder(this)
            // 设置对话框标题
            .setTitle("简单对话框")
            // 设置图标
            .setIcon(R.mipmap.ic_launcher)
            //设置内容，可代替
            .setMessage("对话框的测试内容\n第二行内容")
            .setPositiveButton("确定", null)
            .setNegativeButton("取消",null)
            .create()
            .show();*/
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mDesktopLayout = inflater.inflate(R.layout.item_view_hw, null);
        mDesktopLayout.setClickable(true);
        mDesktopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "悬浮窗 on Click", Toast.LENGTH_SHORT).show();
            }
        });
        // 取得系统窗体
        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        // 窗体的布局样式
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();

        // 关键----设置窗体显示类型――TYPE_SYSTEM_ALERT(系统提示)
        // type=2002; 这里的2002表示系统级窗口，也可以试试2003
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;

        // 关键----设置窗体焦点及触摸：
        // flags=40; FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        // 40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 设置显示的模式
        mLayoutParams.format = PixelFormat.RGBA_8888;
        // 设置对齐的方法
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        // 设置窗体宽度和高度
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 设置窗体显示的位置，否则在屏幕中心显示
        mLayoutParams.x = 50;
        mLayoutParams.y = 50;
        mWindowManager.addView(mDesktopLayout, mLayoutParams);
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
        if (id == R.id.action_one_key_check) {
            onOneKeyCheck(item.getActionView());
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
        if (CheckEntity.CHECK_STATUS_OK.equalsIgnoreCase(status) || CheckEntity.CHECK_STATUS_FAIL.equalsIgnoreCase(status) || StringUtil.isEmpty(status)) {
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
