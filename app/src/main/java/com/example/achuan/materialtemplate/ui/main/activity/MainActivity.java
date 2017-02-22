package com.example.achuan.materialtemplate.ui.main.activity;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.app.Constants;
import com.example.achuan.materialtemplate.base.SimpleActivity;
import com.example.achuan.materialtemplate.ui.main.fragment.SettingsFragment;
import com.example.achuan.materialtemplate.ui.module0.fragment.Module0MainFragment;
import com.example.achuan.materialtemplate.ui.module1.fragment.Module1MainFragment;
import com.example.achuan.materialtemplate.ui.module2.fragment.Module2MainFragment;
import com.example.achuan.materialtemplate.util.SharedPreferenceUtil;
import com.example.achuan.materialtemplate.util.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SimpleActivity implements NavigationView.OnNavigationItemSelectedListener {

    //左侧部分打开按钮
    ActionBarDrawerToggle mDrawerToggle;
    //需要装载到主活动中的Fragment的引用变量
    Module0MainFragment mModule0MainFragment;
    Module1MainFragment mModule1MainFragment;
    Module2MainFragment mModule2MainFragment;
    SettingsFragment mSettingsFragment;


    //定义变量记录需要隐藏和显示的fragment的编号
    private int hideFragment = Constants.TYPE_MODULE_0;
    private int showFragment = Constants.TYPE_MODULE_0;

    //记录左侧navigation的item点击
    MenuItem mLastMenuItem;//历史
    int contentViewId;//内容显示区域的控件的id号,后面用来添加碎片使用


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_nav)
    NavigationView mDrawerNav;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        contentViewId = R.id.fl_main_content;
        /********************检测并打开网络****************/
        SystemUtil.checkAndShowNetSettingDialog(this);
        /***1-初始化创建模块的fragment实例对象,并装载到主activity中****/
        mModule0MainFragment = new Module0MainFragment();
        //***初始化显示第一个Fragment,用replace方法***
        replaceFragment(R.id.fl_main_content, mModule0MainFragment);
        /***2-navigation中item的初始化设置***/
        //初始化第一次显示的item为设置界面
        mLastMenuItem = mDrawerNav.getMenu().findItem(R.id.drawer_fun_0);
        mLastMenuItem.setChecked(true);
        mDrawerNav.setNavigationItemSelectedListener(this);
        SharedPreferenceUtil.setCurrentItem(showFragment);
        //初始化toolbar
        setToolBar(mToolbar, (String) mLastMenuItem.getTitle());
        /***3-因为该为主界面,需要额外设置home按钮***/
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(
                this,            /* host Activity */
                mDrawerLayout,  /* DrawerLayout object */
                mToolbar,
                R.string.navigation_drawer_open,/* "open drawer" description for accessibility */
                R.string.navigation_drawer_close);/* "close drawer" description for accessibility */
        mDrawerLayout.addDrawerListener(mDrawerToggle);//添加点击监听事件
        //将DrawerToggle中的drawer图标,设置为ActionBar中的Home-Button的Icon
        mDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        /***当系统版本小于5.0时,避免NavView不延伸到状态栏,需进行如下设置***/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }
    }

    //重写back按钮的点击事件
    @Override
    public void onBackPressed() {
        SystemUtil.showExitDialog(this);
    }

    //HomeAsUp按钮的id永远都是android.R.id.home
    @Override//Toolbar上按钮的选择监听事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                //点击按钮打开显示左边的菜单,这里设置其显示的行为和XML中定义的一致
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;*/
            default:
                break;
        }
        return true;
    }

    /***
     * navigation的item点击事件监听方法实现
     ***/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //针对不同的item实现不同的逻辑处理
            case R.id.drawer_fun_0:
                showFragment = Constants.TYPE_MODULE_0;
                if (mModule0MainFragment == null) {
                    mModule0MainFragment = new Module0MainFragment();
                    //第一次创建Fragment时将碎片添加到内容布局中
                    addFragment(contentViewId, mModule0MainFragment);
                }
                break;
            case R.id.drawer_fun_1:
                showFragment = Constants.TYPE_MODULE_1;
                if (mModule1MainFragment == null) {
                    mModule1MainFragment = new Module1MainFragment();
                    addFragment(contentViewId, mModule1MainFragment);
                }
                break;
            case R.id.drawer_fun_2:
                showFragment = Constants.TYPE_MODULE_2;
                if (mModule2MainFragment == null) {
                    mModule2MainFragment = new Module2MainFragment();
                    addFragment(contentViewId, mModule2MainFragment);
                }
                break;
            case R.id.drawer_opt_2:
                showFragment = Constants.TYPE_SETTINGS;
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsFragment();
                    addFragment(contentViewId, mSettingsFragment);
                }
                break;
            default:
                break;
        }
        /***点击item后进行显示切换处理,并记录在本地中***/
        if (mLastMenuItem != null && mLastMenuItem != item) {
            mLastMenuItem.setChecked(false);//取消历史选择
            item.setChecked(true);//设置当前item选择
            mToolbar.setTitle(item.getTitle());//改变标题栏的内容
            //记录当前显示的item
            SharedPreferenceUtil.setCurrentItem(showFragment);
            //***实现fragment的切换显示***
            showFragment(getTargetFragment(hideFragment), getTargetFragment(showFragment));
            //选择过的item变成了历史
            mLastMenuItem = item;
            //当前fragment显示完就成为历史了
            hideFragment = showFragment;
        }
        //点击左侧菜单栏中的item后将滑动菜单关闭
        mDrawerLayout.closeDrawers();
        return true;
    }

    //根据item编号获取fragment对象的方法
    private Fragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_MODULE_0:
                return mModule0MainFragment;
            case Constants.TYPE_MODULE_1:
                return mModule1MainFragment;
            case Constants.TYPE_MODULE_2:
                return mModule2MainFragment;
            case Constants.TYPE_SETTINGS:
                return mSettingsFragment;
            default:
                break;
        }
        return mModule0MainFragment;
    }

}
