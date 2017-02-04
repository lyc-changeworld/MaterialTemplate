package com.example.achuan.materialtemplate.ui.main.activity;

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
import com.example.achuan.materialtemplate.ui.module0.fragment.Module_0MainFragment;
import com.example.achuan.materialtemplate.ui.module1.fragment.Module_1MainFragment;
import com.example.achuan.materialtemplate.ui.module2.fragment.Module_2MainFragment;
import com.example.achuan.materialtemplate.util.SharedPreferenceUtil;
import com.example.achuan.materialtemplate.util.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SimpleActivity implements NavigationView.OnNavigationItemSelectedListener {

    //左侧部分打开按钮
    ActionBarDrawerToggle mDrawerToggle;
    //需要装载到主活动中的Fragment的引用变量
    Module_0MainFragment mModuleFragment_0;
    Module_1MainFragment mModuleFragment_1;
    Module_2MainFragment mModuleFragment_2;


    //定义变量记录需要隐藏和显示的fragment的编号
    private int hideFragment = Constants.TYPE_MODULE_0;
    private int showFragment = Constants.TYPE_MODULE_0;

    //记录左侧navigation的item点击
    MenuItem mLastMenuItem;//历史


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }
    @Override
    protected void initEventAndData() {
        /********************检测并打开网络****************/
        SystemUtil.checkAndShowNetSettingDialog(this);
        /***1-初始化创建各模块的fragment实例对象,并装载到主activity中****/
        mModuleFragment_0=new Module_0MainFragment();
        mModuleFragment_1=new Module_1MainFragment();
        mModuleFragment_2=new Module_2MainFragment();
        //越靠前添加的fragment越在上面显示
        loadMultipleRootFragment(R.id.fl_main_content, 0,//容器id和目标位置
                mModuleFragment_0, mModuleFragment_1,
                mModuleFragment_2);//添加进去的fragment实例对象
        /***2-navigation中item的初始化设置***/
        //初始化第一次显示的item为设置界面
        mLastMenuItem = mNavView.getMenu().findItem(R.id.drawer_nav_fun_0);
        mLastMenuItem.setChecked(true);
        mNavView.setNavigationItemSelectedListener(this);
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }
    }

    //重写back按钮的点击事件
    @Override
    public void onBackPressedSupport() {
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
            case R.id.drawer_nav_fun_0:
                showFragment = Constants.TYPE_MODULE_0;
                break;
            case R.id.drawer_nav_fun_1:
                showFragment = Constants.TYPE_MODULE_1;
                break;
            case R.id.drawer_nav_fun_2:
                showFragment = Constants.TYPE_MODULE_2;
                break;
            default:break;
        }
        /***点击item后进行显示切换处理,并记录在本地中***/
        if (mLastMenuItem!=null&&mLastMenuItem!= item) {
            mLastMenuItem.setChecked(false);//取消历史选择
            item.setChecked(true);//设置当前item选择
            mToolbar.setTitle(item.getTitle());//改变标题栏的内容
            //记录当前显示的item
            SharedPreferenceUtil.setCurrentItem(showFragment);
            //实现fragment的切换显示
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
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
    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_MODULE_0:
                return mModuleFragment_0;
            case Constants.TYPE_MODULE_1:
                return mModuleFragment_1;
            case Constants.TYPE_MODULE_2:
                return mModuleFragment_2;
            default:break;
        }
        return mModuleFragment_0;
    }

}
