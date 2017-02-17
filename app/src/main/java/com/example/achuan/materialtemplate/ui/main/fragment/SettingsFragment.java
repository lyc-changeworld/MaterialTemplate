package com.example.achuan.materialtemplate.ui.main.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.example.achuan.materialtemplate.R;
import com.example.achuan.materialtemplate.app.Constants;
import com.example.achuan.materialtemplate.util.SharedPreferenceUtil;

/**
 * Created by achuan on 17-2-15.
 * 功能：实现个人喜好设置的界面功能
 * 参考官网链接：https://developer.android.com/guide/topics/ui/settings.html
 */

public  class SettingsFragment extends PreferenceFragment {

    private PreferenceManager mPreferenceManager;//全局喜好的管理者
    private CheckBoxPreference mCheckBoxPreference;
    private SwitchPreference mSwitchPreference;
    private Preference mClearCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);//加载xml

        //先拿到全部设置文件的管理者,后续可根据key值来获取设置的值
        mPreferenceManager=getPreferenceManager();
        //如果想指定保存文件名，可使用：(括号中参数为"配置文件名")
        mPreferenceManager.setSharedPreferencesName(Constants.PREFERENCES_NAME);


        /*1-通过管理者根据key值获取对应的UI控件*/
        mCheckBoxPreference= (CheckBoxPreference) mPreferenceManager
                .findPreference("save_net_mode");//这里传入xml文件中定义的key值获取控件对象
        mSwitchPreference= (SwitchPreference) mPreferenceManager
                .findPreference("light_mode");
        mClearCache=mPreferenceManager.findPreference("clear_cache");

        //2-初始化UI控件的状态
        mCheckBoxPreference.setChecked(SharedPreferenceUtil.getAppSp().
                getBoolean("save_net_mode",false));
        mSwitchPreference.setChecked(SharedPreferenceUtil.getAppSp()
                .getBoolean("light_mode",false));


        /*3-为纯文本的设置栏添加点击监听*/
        mClearCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(),
                        "清空缓存",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        /*4-为可发生变化的设置栏注册监听*/
        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(
                            SharedPreferences sharedPreferences, String key) {
                        if(key.equals("save_net_mode")){
                            Toast.makeText(getActivity(),
                                    "CheckBox状态为"+sharedPreferences.getBoolean(key,false),
                                    Toast.LENGTH_SHORT).show();

                        }else if(key.equals("light_mode")){
                            Toast.makeText(getActivity(),
                                    "Switch状态为"+sharedPreferences.getBoolean(key,false),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
