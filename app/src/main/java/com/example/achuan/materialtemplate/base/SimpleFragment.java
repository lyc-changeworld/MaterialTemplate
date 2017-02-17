package com.example.achuan.materialtemplate.base;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by achuan on 16-10-29.
 * 无MVP的Fragment基类
 * 注意：这里使用的是android.app.Fragment,是为了和PreferenceFragment的父类保持一致
 *
 */
public abstract class SimpleFragment extends Fragment {

    private boolean isInited = false;

    //当碎片和活动建立关联的时候调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    //为碎片创建视图（加载布局）时调用
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutId(),container,false);
        return view;
    }
    //确保与碎片相关联的活动一定已经创建完毕的时候调用
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);//初始化加载布局控件
        //如果是视图第一次创建,而且没有被挡住,才初始化事件和数据
        if (savedInstanceState == null) {
            if (!isHidden()) {
                isInited = true;//已经初始化过一次了
                initEventAndData();
            }
        }
    }
    //碎片隐藏状态改变时调用该方法(用来确保非隐藏状态时完成初始化)
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isInited && !hidden) {
            isInited = true;
            initEventAndData();
        }
    }
    //当与碎片关联的视图被移除的时候调用
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /***---动态添加碎片到位置区域---
     * 用replace的效果就是：切换fragment时每次都会重新创建初始化。
     * ***/
    public void replaceFragment(int viewId, Fragment fragment) {
        //在Fragment中获取FragmentManager时,就用下面的方法,否则用getSupportFragmentManager()
        //getChildFragmentManager().beginTransaction().replace(viewId,fragment).commit();
        //getSupportFragmentManager().beginTransaction().replace(viewId,fragment).commit();
        //对于android.app.Fragment使用下面的方法,否则用上面的两种方法
        getFragmentManager().beginTransaction()//开启事务
                .replace(viewId,fragment)//kill之前的碎片,并初始化加载当前碎片
                .commit();//提交事务
    }

    /***---添加碎片到内容区域中---***/
    public void addFragment(int viewId, Fragment fragment){
        getFragmentManager().beginTransaction()//开启事务
                .add(viewId,fragment)//添加
                .commit();//提交事务
    }
    /***隐藏之前的以及显示当前的item对应的Fragment***/
    public void showFragment( Fragment hideFragment,Fragment showFragment){
        getFragmentManager().beginTransaction()//开启事务
                .hide(hideFragment)//隐藏
                .show(showFragment)//显示
                .commit();//提交事务
    }

    protected abstract int getLayoutId();//添加布局文件
    protected abstract void initEventAndData();//初始化事件及数据
}
