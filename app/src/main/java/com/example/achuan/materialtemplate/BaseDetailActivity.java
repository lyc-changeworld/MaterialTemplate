package com.example.achuan.materialtemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.achuan.materialtemplate.base.SimpleActivity;
import com.example.achuan.materialtemplate.util.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-18.
 */

public class BaseDetailActivity extends SimpleActivity {

    public static final String TITLE_NAME = "title_name";
    @BindView(R.id.iv_detail)
    ImageView mIvDetail;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.tv_content_detail)
    TextView mTvContentDetail;
    @BindView(R.id.fab_detail)
    FloatingActionButton mFabDetail;

    @Override
    protected int getLayout() {
        return R.layout.activity_base_detail;
    }

    @Override
    protected void initEventAndData() {
        /*获取上一个活动传递过来的数据*/
        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE_NAME);//获取标题的名字
        //设置标题栏(标题+图标)
        setToolBar(mToolbar, title);
        mCollapsingToolbar.setTitle(title);
        //通过Glide加载图片作为标题的背景图片
        Glide.with(this).load(R.drawable.banana).into(mIvDetail);
        //获取文本信息,并显示在内容区域
        String content = createFruitContent(title);
        mTvContentDetail.setText(content);

        //为悬浮圆形按钮添加点击监听事件
        mFabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.showShort(v,"收藏文章成功");
            }
        });

    }

    //为内容显示区域添加文本数据
    private String createFruitContent(String titleName) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            content.append(titleName);
        }
        return content.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
