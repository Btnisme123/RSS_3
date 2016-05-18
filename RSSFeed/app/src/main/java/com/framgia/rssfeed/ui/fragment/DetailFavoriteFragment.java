package com.framgia.rssfeed.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
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
import com.framgia.rssfeed.ui.widget.LayoutNotifyState;
import com.framgia.rssfeed.util.MonitorWorkerThreadUtil;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;
import com.framgia.rssfeed.util.WorkerThread;

public class DetailFavoriteFragment extends Fragment {
    public static final String TAG_DETAIL_FAVORITE_FRAGMENT = "detail_favorite_fragment";
    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListFavoriteAdapter;
    private LayoutNotifyState mLayoutNoData;
    private int mIndex;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            News news = mListFavoriteAdapter.getItem(position);
            if (view instanceof LinearLayout) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_NEWS, news);
                bundle.putInt(Constants.BUNDLE_TYPE, ShowDetailFragment.TYPE_FAVORITE);
                ShowDetailFragment fragment = new ShowDetailFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment, TAG_DETAIL_FAVORITE_FRAGMENT);
            } else if (view instanceof ImageView) {
                WorkerThread worker = new WorkerThread(getActivity(), WorkerThread.Work.REMOVE
                        , mListFavoriteAdapter.getItem(position), WorkerThread.WorkPriority.NORMAL);
                MonitorWorkerThreadUtil.getInstance().assign(worker);
                mListFavoriteAdapter.removeItem(position);
            }
        }
    };

    public static DetailFavoriteFragment newInstance(int category) {
        DetailFavoriteFragment fragmentDetail = new DetailFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.BUNDLE_INDEX, category);
        fragmentDetail.setArguments(args);
        return fragmentDetail;
    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
        mLayoutNoData= (LayoutNotifyState) view.findViewById(R.id.notify_state_layout);
    }

    public void getData() {
        Bundle bundle = this.getArguments();
        mIndex = bundle.getInt(Constants.BUNDLE_INDEX);
        mListFavoriteAdapter.addItems(DatabaseHandler.getInstance(getActivity()).getFavoriteNews(mIndex));
    }

    public void setupRecyclerView(Context context) {
        ListViewItemDecoration mListViewItemDecoration = new ListViewItemDecoration(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListFavoriteAdapter = new ListNewsAdapter(context, mRecyclerView.getLayoutManager());
        getData();
        mListFavoriteAdapter.setOnRecyclerViewItemClickListener(mOnRecyclerViewItemClickListener);
        mRecyclerView.setAdapter(mListFavoriteAdapter);
        if(mListFavoriteAdapter.getItemCount()>0){
            mLayoutNoData.hide(mRecyclerView);
        }else{
            mLayoutNoData.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT,mRecyclerView);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        findView(view);
        setupRecyclerView(getActivity());
        return view;
    }

    private void replaceFragment(BaseFragment fragment, String tag) {
        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_left_enter, R.animator.fragment_slide_right_exit)
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack("").commit();
    }
}
