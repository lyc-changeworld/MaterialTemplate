package com.example.achuan.materialtemplate.ui.module0.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.RyItemDivider;
import com.example.achuan.materialtemplate.RyItemTouchHelperCallback;
import com.example.achuan.materialtemplate.base.SimpleFragment;
import com.example.achuan.materialtemplate.model.bean.MyBean;
import com.example.achuan.materialtemplate.ui.module0.adapter.Module0_RyAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-4.
 * 功能：该界面使用的是非Card子项的列表,因此使用了自定义的分割线
 */

public class Item_0Fragment extends SimpleFragment {


    @BindView(R.id.ry_view)
    RecyclerView mRyView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    Context mContent;
    List<MyBean> mList;
    Module0_RyAdapter mAdatper;

    boolean isPositive=true;//列表是否正序显示

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_0_item0;
    }

    @Override
    protected void initEventAndData() {
        mContent=getActivity();
        mList=new ArrayList<MyBean>();
        initData();
        mAdatper=new Module0_RyAdapter(mContent,mList);
        LinearLayoutManager linearlayoutManager=new LinearLayoutManager(mContent);
        //设置方向(默认是垂直,下面的是水平设置)
        //linearlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRyView.setLayoutManager(linearlayoutManager);//为列表添加布局
        mRyView.setAdapter(mAdatper);//为列表添加适配器
        //添加自定义的分割线
        mRyView.addItemDecoration(new RyItemDivider(mContent,R.drawable.item_divider));
        /*添加刷新控件的下拉刷新事件监听接口*/
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);//刷新条的颜色
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrehData();
            }
        });
        /***------------------实现item的拖拽和滑动----------------***/
        //先创建回复对象
        RyItemTouchHelperCallback ryItemTouchHelperCallback=new RyItemTouchHelperCallback();
        //添加监听事件,并实现接口回调中的方法
        ryItemTouchHelperCallback.setOnItemTouchCallbackListener(new RyItemTouchHelperCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据源移除，并刷新这个Item。
                if (mList != null) {
                    mList.remove(adapterPosition);
                    mAdatper.notifyItemRemoved(adapterPosition);
                }
            }
            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mList != null) {
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mList, srcPosition, targetPosition);
                    // 更新UI中的Item的位置,主要是给用户看到交互效果
                    mAdatper.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        ryItemTouchHelperCallback.setDragEnable(true);//设置是否拖拽
        ryItemTouchHelperCallback.setSwipeEnable(true);//设置是否可以滑动
        //将回复对象交给总触碰对象,并绑定事件到控件中
        new ItemTouchHelper(ryItemTouchHelperCallback).attachToRecyclerView(mRyView);
    }

    //模拟刷新数据显示
    private void refrehData() {
        //开启一个子线程来进行刷新工作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //将线程切换回主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isPositive=!isPositive;//反序设置
                        initData();//重新初始化数据集合
                        mAdatper.notifyDataSetChanged();//刷新列表显示
                        mSwipeRefresh.setRefreshing(false);//隐藏刷新进度条
                    }
                });
            }
        }).start();

    }
    //模拟数据抓取初始化
    private void initData() {
        mList.clear();//清空数据集合
        if(isPositive){
            for (int i = 0; i <20 ; i++) {
                mList.add(new MyBean("这里是标题"+i,"这里是内容区域"));
            }
        }else {
            for (int i = 19; i >=0 ; i--) {
                mList.add(new MyBean("这里是标题"+i,"这里是内容区域"));
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
