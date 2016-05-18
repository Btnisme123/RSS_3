package com.framgia.rssfeed.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.framgia.rssfeed.ui.widget.DeletingDialog;
import com.framgia.rssfeed.ui.widget.NothingDialog;
import com.framgia.rssfeed.util.MonitorWorkerThreadUtil;
import com.framgia.rssfeed.util.OnRecyclerViewItemClickListener;
import com.framgia.rssfeed.util.WorkerThread;

public class HistoryFragment extends Fragment implements OnRecyclerViewItemClickListener {

    public final static String TAG_HISTORY_FRAGMENT = "history fragment";
    private RecyclerView mRecyclerView;
    private ListNewsAdapter mListHistoryAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_delete);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (DatabaseHandler.getInstance(getActivity()).isAnyItemInHistory()) {
                    final DeletingDialog deletingDialog = new DeletingDialog(getActivity());
                    deletingDialog.setOnDialogItemClickListener(new DeletingDialog.onDeletingDialogItemClickListener() {
                        @Override
                        public void deleteItem(int code) {
                            if (code == DeletingDialog.OK) {
                                DatabaseHandler.getInstance(getActivity()).deleteHistory();
                                mListHistoryAdapter.removeAllItemsIfExist();
                            }
                            deletingDialog.dismiss();
                        }
                    });
                    deletingDialog.show();
                } else {
                    NothingDialog nothingDialog = new NothingDialog(getActivity());
                    nothingDialog.show();
                }

                return true;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        News news = mListHistoryAdapter.getItem(position);
        if (view instanceof LinearLayout) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_NEWS, news);
            bundle.putInt(Constants.BUNDLE_TYPE, ShowDetailFragment.TYPE_HISTORY);
            ShowDetailFragment fragment = new ShowDetailFragment();
            fragment.setArguments(bundle);
            replaceFragment(fragment, TAG_HISTORY_FRAGMENT);
        } else if (view instanceof ImageView) {
            WorkerThread.Work work;
            if (!news.isFavorite()) {
                work = WorkerThread.Work.CACHE;
            } else {
                work = WorkerThread.Work.REMOVE;
            }
            WorkerThread worker = new WorkerThread(getActivity(), work, news, WorkerThread.WorkPriority.NORMAL);
            MonitorWorkerThreadUtil.getInstance().assign(worker);
            news.setFavorite(!news.isFavorite());
            mListHistoryAdapter.notifyDataSetChanged();
        }

    }

    public void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_news);
    }

    public void setupRecyclerView(Context context) {
        ListViewItemDecoration mListViewItemDecoration = new ListViewItemDecoration(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(mListViewItemDecoration);
        mListHistoryAdapter = new ListNewsAdapter(context, mRecyclerView.getLayoutManager());
        mListHistoryAdapter.addItems(DatabaseHandler.getInstance(context).getHistoryNewsList());
        for (int i = 0; i < mListHistoryAdapter.getItemCount(); i++) {
            mListHistoryAdapter.getItem(i).setFavorite(
                    DatabaseHandler.getInstance(getActivity())
                                   .isFavorite(mListHistoryAdapter.getItem(i).getLink()));
        }
        mListHistoryAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.setAdapter(mListHistoryAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        findView(view);
        setupRecyclerView(getActivity());
        setHasOptionsMenu(true);
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
