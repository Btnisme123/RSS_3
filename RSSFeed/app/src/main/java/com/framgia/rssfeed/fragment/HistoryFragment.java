package com.framgia.rssfeed.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.rssfeed.ListViewItemDecoration;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.adapter.ListNewsAdapter;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.data.local.DatabaseHandler;


public class HistoryFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListNewsAdapter;
    private ListViewItemDecoration mListViewItemDecoration;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findView(rootView);
    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
        setupRecyclerView();
    }

    public void setupRecyclerView() {
        mListViewItemDecoration = new ListViewItemDecoration(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListNewsAdapter = new ListNewsAdapter(getActivity(), mRecyclerView.getLayoutManager());
        mListNewsAdapter.addItems(DatabaseHandler.getInstance(getActivity()).getHistoryNews());
        mRecyclerView.setAdapter(mListNewsAdapter);
    }

}
