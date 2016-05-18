package com.framgia.rssfeed.ui.base;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.rssfeed.R;

/**
 * Created by yue on 21/04/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setSupportActionBar(mToolbar);
        if (savedInstanceState == null) addFragment();
        onCreateContentView();
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getCurrentFragment().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void addFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, getFragment()).commit();
    }

    public void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_left_enter, R.animator.fragment_slide_right_exit)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack("").commit();
    }

    public void popFragment() {
        getFragmentManager().popBackStack();
    }

    public void popToSpecifyFragment(String tag) {
        getFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    protected abstract BaseFragment getFragment();

    protected abstract void onCreateContentView();

    protected BaseFragment getCurrentFragment() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            return (BaseFragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        }
        return null;
    }

    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
