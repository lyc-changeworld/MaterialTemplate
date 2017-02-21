package com.example.achuan.materialtemplate.ui.module0.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.materialtemplate.BaseDetailActivity;
import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.RyItemDivider;
import com.example.achuan.materialtemplate.RyItemTouchHelperCallback;
import com.example.achuan.materialtemplate.app.Constants;
import com.example.achuan.materialtemplate.base.SimpleFragment;
import com.example.achuan.materialtemplate.model.bean.MyBean;
import com.example.achuan.materialtemplate.ui.module0.adapter.Item0Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-4.
 * 功能：该界面使用的是非Card子项的列表,因此使用了自定义的分割线
 */

public class Item0Fragment extends SimpleFragment {


    Context mContent;
    List<MyBean> mMyBeanList;
    Item0Adapter mItem0Adapter;
    boolean isPositive = true;//列表是否正序显示

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.sw_rf)
    SwipeRefreshLayout mSwRf;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moudle0_item0;
    }

    @Override
    protected void initEventAndData() {
        mContent = getActivity();
        mMyBeanList = new ArrayList<MyBean>();
        initData();
        mItem0Adapter = new Item0Adapter(mContent, mMyBeanList);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContent);
        //设置方向(默认是垂直,下面的是水平设置)
        //linearlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(linearlayoutManager);//为列表添加布局
        mRv.setAdapter(mItem0Adapter);//为列表添加适配器
        //添加自定义的分割线
        mRv.addItemDecoration(new RyItemDivider(mContent, R.drawable.di_item));
        /*添加刷新控件的下拉刷新事件监听接口*/
        mSwRf.setColorSchemeResources(R.color.colorAccent);//刷新条的颜色
        mSwRf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrehData();
            }
        });
        /***------------------实现item的拖拽和滑动----------------***/
        //先创建回复对象
        RyItemTouchHelperCallback ryItemTouchHelperCallback = new RyItemTouchHelperCallback();
        //添加监听事件,并实现接口回调中的方法
        ryItemTouchHelperCallback.setOnItemTouchCallbackListener(new RyItemTouchHelperCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据源移除，并刷新这个Item。
                if (mMyBeanList != null) {
                    mMyBeanList.remove(adapterPosition);
                    mItem0Adapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (mMyBeanList != null) {
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mMyBeanList, srcPosition, targetPosition);
                    // 更新UI中的Item的位置,主要是给用户看到交互效果
                    mItem0Adapter.notifyItemMoved(srcPosition, targetPosition);
                    return true;
                }
                return false;
            }
        });
        ryItemTouchHelperCallback.setDragEnable(true);//设置是否拖拽
        ryItemTouchHelperCallback.setSwipeEnable(true);//设置是否可以滑动
        //将回复对象交给总触碰对象,并绑定事件到控件中
        new ItemTouchHelper(ryItemTouchHelperCallback).attachToRecyclerView(mRv);

        /*添加item的点击事件监听*/
        mItem0Adapter.setOnClickListener(new Item0Adapter.OnClickListener() {
            @Override
            public void onClick(View view, int postion) {
                Intent intent = new Intent(mContent, BaseDetailActivity.class);
                /*传递消息*/
                intent.putExtra(Constants.TITLE_NAME,
                        mMyBeanList.get(postion).getTitle());//传递点击item的标题
                mContent.startActivity(intent);//开始跳转activity
            }
        });
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
                        isPositive = !isPositive;//反序设置
                        initData();//重新初始化数据集合
                        mItem0Adapter.notifyDataSetChanged();//刷新列表显示
                        mSwRf.setRefreshing(false);//隐藏刷新进度条
                    }
                });
            }
        }).start();

    }

    //模拟数据抓取初始化
    private void initData() {
        mMyBeanList.clear();//清空数据集合
        if (isPositive) {
            for (int i = 0; i < 20; i++) {
                mMyBeanList.add(new MyBean("这里是标题" + i, "这里是内容区域"));
            }
        } else {
            for (int i = 19; i >= 0; i--) {
                mMyBeanList.add(new MyBean("这里是标题" + i, "这里是内容区域"));
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
