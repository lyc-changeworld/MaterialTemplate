package com.example.achuan.materialtemplate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by achuan on 17-2-16.
 * 功能：ViewPager和Fragment顶部导航界面滑动显示的适配器类
 * 注意事项：使用FragmentPagerAdapter时是默认调用v4包中的相关类,此实现的联动也是必须使用的--
 *     android.support.v4.app.Fragment,但是由于前面使用了PreferenceFragment,而它是继承于--
 *    android.app.Fragment,这里为了保证一致使用性,需要另外导入v13的包,然后调用v13包下的Adapter--
 *   这样就可以在联动布局中使用android.app.Fragment了
 */

public class BaseVf_V13Adapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTitles;


    public BaseVf_V13Adapter(FragmentManager fm,List<Fragment> mFragmentList,List<String> mTitles) {
        super(fm);
        this.mFragmentList=mFragmentList;
        this.mTitles=mTitles;
    }

    //获取Fragment对象
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    //获取总个数
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    //获取当前导航滑动到的位置的方法
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    //设置tab的标签
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
    /***
     * FragmentPagerAdapter默认会保存的三个item也就是当前的一个，前一个和后一个
     * 滑动过程中适配器默认会把前一个之前的item destroy掉，
     * 所以当滑动回来时就依然会重新加载
     * 分析其原因就是适配器销毁了之前的item，自然解决办法就是不让他销毁
     * 具体方法就是重写FragmentPagerAdapter的destroyItem方法
     * 注释掉super.destroyItem(Container, position, object);就行了
     * ***/
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    /*当前bug:第二次显示联动Fragment的父布局时,所有的Fragment都被销毁了*/

}
