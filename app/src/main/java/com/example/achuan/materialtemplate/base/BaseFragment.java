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
 * Created by codeest on 2016/8/2.
 * 功能：MVP Fragment基类
 * 注意：SupportFragment使用的是support.v4.app.Fragment,因为在ViewPager和Fragment的联动适配器中,
 *     只能使用supportV4包中的Fragment,因此单独建了该基类,本想只在联动布局中使用,但发现其父布局也应该使
 *     用该包,因此该项目中全都使用了
 *
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView{


    protected T mPresenter;
    private boolean isInited = false;//记录是否已经初始化完毕


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
        mPresenter=createPresenter();//创建操作者实例
        return view;
    }
    //确保与碎片相关联的活动一定已经创建完毕的时候调用
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPresenter!=null){
            mPresenter.attachView(this);//添加关联（presenter和fragment）
        }
        ButterKnife.bind(this, view);//初始化加载布局控件
        //如果是视图第一次创建,而且没有被挡住,才初始化事件和数据
        if (savedInstanceState == null) {
            if (!isHidden()) {
                isInited = true;//已经初始化过一次了
                initEventAndData();
            }
        }
    }
    //碎片隐藏状态改变时调用该方法(碎片与用户交互时进行初始化数据操作)
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
        if(mPresenter!=null){
            mPresenter.detachView();//取消关联(presenter和fragment)
        }
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

    protected abstract T createPresenter();//创建操作者实例
    protected abstract int getLayoutId();//添加布局文件
    protected abstract void initEventAndData();//初始化事件及数据
}