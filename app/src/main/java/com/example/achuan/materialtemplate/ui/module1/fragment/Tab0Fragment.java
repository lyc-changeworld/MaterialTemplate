package com.example.achuan.materialtemplate.ui.module1.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.base.SimpleFragment;
import com.example.achuan.materialtemplate.model.bean.MyBean;
import com.example.achuan.materialtemplate.ui.module1.adapter.Tab0Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-4.
 */

public class Tab0Fragment extends SimpleFragment {


    Context mContent;
    List<MyBean> mList;
    Tab0Adapter mAdatper;

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.sw_rf)
    SwipeRefreshLayout mSwRf;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moudle1_tab0;
    }

    @Override
    protected void initEventAndData() {
        mContent = getActivity();
        initData();
        mAdatper = new Tab0Adapter(mContent, mList);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContent);
        //设置方向(默认是垂直,下面的是水平设置)
        //linearlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(linearlayoutManager);//为列表添加布局
        mRv.setAdapter(mAdatper);//为列表添加适配器
        //mRv.addItemDecoration(new RyItemDivider(mContent,R.drawable.di_item));//添加分割线
    }

    private void initData() {
        mList = new ArrayList<MyBean>();
        for (int i = 0; i < 20; i++) {
            mList.add(new MyBean("这里是标题" + i, "这里是内容区域"));
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
