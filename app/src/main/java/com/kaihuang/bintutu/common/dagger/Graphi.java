package com.kaihuang.bintutu.common.dagger;


import com.kaihuang.bintutu.common.BaseActivity;
import com.kaihuang.bintutu.common.BaseFragment;
import com.kaihuang.bintutu.common.BasePresenter;
import com.kaihuang.bintutu.utils.Pager;

/**
 * Dagger2的图接口
 * <p/>
 * Created by stefan on 16/11/2.
 */
public interface Graphi {

    void inject(BaseActivity mainActivity); // 注入BaseActivity

    void inject(BaseFragment baseFragment); // 注入BaseFragment

    void inject(Pager pager); // 注入Pager

    void inject(BasePresenter pager); // 注入BasePresenter
}