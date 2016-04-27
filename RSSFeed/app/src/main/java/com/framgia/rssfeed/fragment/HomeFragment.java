package com.framgia.rssfeed.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.adapter.TabFragmentAdapter;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.bean.News;

import java.util.ArrayList;


/**
 * Created by yue on 21/04/2016.
 */
public class HomeFragment extends BaseFragment {

    public final static String TAG_HOME_FRAGMENT = "home fragment";
    private final static String URL_TINH_TE = "https://tinhte.vn/rss/";
    private final static String URL_VNEXPRESS = "http://vnexpress.net/rss/thoi-su.rss";
    private ArrayList<News> mNewsList;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabFragmentAdapter mTabFragmentAdapter;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        mNewsList = new ArrayList<>();
        findView(rootView);
    }

    @Override
    protected boolean enableBackButton() {
        return false;

    }

    private void findView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        mTabFragmentAdapter = new TabFragmentAdapter(getChildFragmentManager());
        mTabFragmentAdapter.addFragment(new NewsFragment(), getString(R.string.news_uppercase));
        mTabFragmentAdapter.addFragment(new HistoryFragment(), getString(R.string.history_uppercase));
        mViewPager.setAdapter(mTabFragmentAdapter);
    }

}
