package com.framgia.rssfeed.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.data.bean.News;
import com.framgia.rssfeed.data.local.DatabaseHandler;
import com.framgia.rssfeed.ui.adapter.ListNewsAdapter;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.base.Constants;
import com.framgia.rssfeed.ui.decoration.ListViewItemDecoration;
import com.framgia.rssfeed.util.MonitorWorkerThreadUtil;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;
import com.framgia.rssfeed.util.UrlCacheUtil;
import com.framgia.rssfeed.util.WorkerThread;

public class DetailFavoriteFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListFavoriteAdapter;
    private int mIndex;
    public static final String TAG_DETAIL_FAVORITE_FRAGMENT = "detail_favorite_fragment";

    public static DetailFavoriteFragment newInstance(int category) {
        DetailFavoriteFragment fragmentDetail = new DetailFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.BUNDLE_INDEX, category);
        fragmentDetail.setArguments(args);
        return fragmentDetail;
    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
    }

    public void getData() {
        Bundle bundle = this.getArguments();
        mIndex = bundle.getInt(Constants.BUNDLE_INDEX);
        mListFavoriteAdapter.addItems(DatabaseHandler.getInstance(getContext()).getFavoriteNews(mIndex));
    }

    public void setupRecyclerView(Context context) {
        ListViewItemDecoration mListViewItemDecoration = new ListViewItemDecoration(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListFavoriteAdapter = new ListNewsAdapter(context, mRecyclerView.getLayoutManager());
        getData();
        mListFavoriteAdapter.setOnRecyclerViewItemClickListener(mOnRecyclerViewItemClickListener);
        mRecyclerView.setAdapter(mListFavoriteAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        findView(view);
        setupRecyclerView(getActivity());
        return view;
    }

    private void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack("")
                .commit();
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            News news = mListFavoriteAdapter.getItem(position);
            if (view instanceof LinearLayout) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_NEWS, news);
                bundle.putInt(Constants.BUNDLE_INDEX, mIndex);
                ShowDetailFragment fragment = new ShowDetailFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, TAG_DETAIL_FAVORITE_FRAGMENT);
            } else if (view instanceof ImageView) {
                if (!news.isFavorite()) {
                    UrlCacheUtil.getInstance().cache(news);
                    int arraySize = mListFavoriteAdapter.getItemCount();
                    for (int i = 0; i < arraySize; i++) {
                        WorkerThread worker = new WorkerThread(getActivity(), WorkerThread.WORK_CACHE, mListFavoriteAdapter.getItem(i));
                        MonitorWorkerThreadUtil.getInstance().assign(worker);
                    }
                } else {
                    UrlCacheUtil.getInstance().remove(news);
                    mListFavoriteAdapter.removeItem(position);
                }
                news.setFavorite(!news.isFavorite());
                mListFavoriteAdapter.notifyItemChanged(position);
            }
        }
    };
}
