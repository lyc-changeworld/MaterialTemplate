package com.example.achuan.materialtemplate.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.achuan.materialtemplate.app.App;

import butterknife.ButterKnife;

/**
 * Created by achuan on 16-10-24.
 * 功能：MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView{

    protected T mPresenter;//引入操作者

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());//设置布局文件
        ButterKnife.bind(this);//初始化控件加载
        mPresenter=createPresenter();//创建操作者实例对象
        if(mPresenter!=null){
            mPresenter.attachView(this);//建立关联(prsenter持有activity对象)
        }
        App.getInstance().addActivity(this);//存储活动对象到集合中
        initEventAndData();//初始化事件和数据操作
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detachView();//取消关联(prsenter不再持有activity对象)
        }
        App.getInstance().removeActivity(this);
    }

    //设置toolbar的方法
    protected void setToolBar(Toolbar toolbar, String title) {
        //设置标题名称
        toolbar.setTitle(title);
        //用自定义的toolbar取代原始的toolbar
        setSupportActionBar(toolbar);
        //给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //对NavigationIcon添加点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    protected abstract int getLayout();//添加布局文件
    protected abstract void initEventAndData();//初始化事件及数据
}
