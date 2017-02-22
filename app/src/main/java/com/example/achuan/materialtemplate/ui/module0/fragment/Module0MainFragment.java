package com.example.achuan.materialtemplate.ui.module0.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.app.Constants;
import com.example.achuan.materialtemplate.base.SimpleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-1.
 * 功能：使用官方的BottomNavigation控件实现了主Fragment中子Fragment的切换显示
 * &&&当前的问题:当设置列表向上滑动时让Toolbar上移隐藏,此时BottomNav也上移,真正--
 * 合理的效果应该是列表上滑时Toolbar上移隐藏的同时BottomNav下移消失,反之亦然
 */

public class Module0MainFragment extends SimpleFragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    Item0Fragment mItem0Fragment;
    Item1Fragment mItem1Fragment;
    Item2Fragment mItem2Fragment;

    //定义变量记录需要隐藏和显示的fragment的编号
    private int hideFragment = Constants.TYPE_ITEM_0;
    private int showFragment = Constants.TYPE_ITEM_0;

    //记录BottomNavigation的item点击
    MenuItem mLastMenuItem;//历史
    int contentViewId;//内容显示区域的控件的id号,后面用来添加碎片使用

    @BindView(R.id.btm_nav)
    BottomNavigationView mBtmNav;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moudle0_main;
    }

    @Override
    protected void initEventAndData() {
        contentViewId = R.id.fl_moudle0_content;
        /***1-初始化底部导航栏设置***/
        //初始化第一次显示的item为设置界面
        mLastMenuItem = mBtmNav.getMenu().findItem(R.id.bottom_0);
        mLastMenuItem.setChecked(true);
        //添加点击监听事件
        mBtmNav.setOnNavigationItemSelectedListener(this);
        /***2-初始化创建模块的fragment实例对象,并装载到主布局中****/
        mItem0Fragment = new Item0Fragment();//默认先创建第一界面
        //并将第一界面碎片添加到布局容器中
        replaceFragment(R.id.fl_moudle0_content, getTargetFragment(showFragment));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_0:
                showFragment = Constants.TYPE_ITEM_0;
                //第一次加载显示时,才创建碎片对象,并添加到内容容器中
                if (mItem0Fragment == null) {
                    mItem0Fragment = new Item0Fragment();
                    addFragment(contentViewId, mItem0Fragment);
                }
                break;
            case R.id.bottom_1:
                showFragment = Constants.TYPE_ITEM_1;
                if (mItem1Fragment == null) {
                    mItem1Fragment = new Item1Fragment();
                    addFragment(contentViewId, mItem1Fragment);
                }
                break;
            case R.id.bottom_2:
                showFragment = Constants.TYPE_ITEM_2;
                if (mItem2Fragment == null) {
                    mItem2Fragment = new Item2Fragment();
                    addFragment(contentViewId, mItem2Fragment);
                }
                break;
            default:
                break;
        }
        /***点击item后进行显示切换处理,并记录在本地中***/
        if (mLastMenuItem != null && mLastMenuItem != item) {
            //实现fragment的切换显示
            showFragment(getTargetFragment(hideFragment), getTargetFragment(showFragment));
            //选择过的item变成了历史
            mLastMenuItem = item;
            //当前fragment显示完就成为历史了
            hideFragment = showFragment;
        }
        return true;
    }

    //根据item编号获取fragment对象的方法
    private Fragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_ITEM_0:
                return mItem0Fragment;
            case Constants.TYPE_ITEM_1:
                return mItem1Fragment;
            case Constants.TYPE_ITEM_2:
                return mItem2Fragment;
            default:
                break;
        }
        return mItem0Fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
