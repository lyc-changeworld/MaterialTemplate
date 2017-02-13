package com.example.achuan.materialtemplate.ui.module1.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.materialtemplate.BaseVfAdapter;
import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.base.SimpleFragment;
import com.example.achuan.materialtemplate.ui.module0.fragment.Item_0Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 17-2-1.
 * 功能：该界面是使用了ViewPager和Fragment联动的效果,使用了TabLayout,
 *      在这里添加了四个子Fragment到主Fragment中实现滑动切换界面显示
 */

public class Module_1MainFragment extends SimpleFragment {


    //定义一个fragment集合实例来存储对应要滑动显示的多个fragment实例
    List<Fragment> fragments;
    //顶部导航标签文字
    List<String> titles;

    //FragmentPagerAdapter引用变量
    BaseVfAdapter mBaseVfAdapter;


    @BindView(R.id.tab_module1_main)
    TabLayout mTabModule1Main;
    @BindView(R.id.vp_module1_main)
    ViewPager mVpModule1Main;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_1;
    }

    @Override
    protected void initEventAndData() {
        //创建集合体实例
        fragments = new ArrayList<Fragment>();
        titles=new ArrayList<String>();
        /***装载fragment实例和tab的标签到集合体中***/
        titles.add(getString(R.string.tab_vf_title_0));
        titles.add(getString(R.string.tab_vf_title_1));
        titles.add(getString(R.string.tab_vf_title_2));
        titles.add(getString(R.string.tab_vf_title_3));
        fragments.add(new Tab_0Fragment());
        fragments.add(new Item_0Fragment());
        fragments.add(new Tab_0Fragment());
        fragments.add(new Item_0Fragment());
        //实例化适配器
        mBaseVfAdapter=new BaseVfAdapter(getChildFragmentManager(),fragments,titles);
        //为viewpager添加适配器
        mVpModule1Main.setAdapter(mBaseVfAdapter);
        //将TabLayout和ViewPager关联起来
        mTabModule1Main.setupWithViewPager(mVpModule1Main);
        //当使用很多tab时,使用滑动模式
        //mTabModule1Main.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab均分,适合少的tab
        //mTabModule1Main.setTabMode(TabLayout.MODE_FIXED);
        //tab均分,适合少的tab,TabLayout.GRAVITY_CENTER
        //mTabModule1Main.setTabGravity(TabLayout.GRAVITY_FILL);//充满,仅适用于MODE_FIXED模式
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
