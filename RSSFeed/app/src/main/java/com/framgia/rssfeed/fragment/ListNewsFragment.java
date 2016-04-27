package com.framgia.rssfeed.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.rssfeed.GridViewItemDecoration;
import com.framgia.rssfeed.ListViewItemDecoration;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.base.BaseFragment;
import com.framgia.rssfeed.bean.News;
import com.framgia.rssfeed.utility.LoadDataUtil;
import com.framgia.rssfeed.utility.NetworkUtil;
import com.framgia.rssfeed.widget.LayoutNotifyState;

import java.util.ArrayList;

/**
 * Created by yue on 25/04/2016.
 */
public class ListNewsFragment extends BaseFragment {

    public final static String URL = "http://vnexpress.net/rss/thoi-su.rss";
    public final static String URL_2 = "https://tinhte.vn/rss/";
    private final static int NUMBER_OF_COLUMN = 2;
    private RecyclerView mListNews;
    private LayoutNotifyState mNotifyStateLayout;
    private ListNewsAdapter mAdapter;
    private ListViewItemDecoration mListViewItemDecoration;
    private GridViewItemDecoration mGridViewItemDecoration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListViewItemDecoration = new ListViewItemDecoration(getActivity());
        mGridViewItemDecoration = new GridViewItemDecoration(getActivity());
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_list_news;
    }

    @Override
    protected void onCreateContentView(View rootView) {
        findViews(rootView);
        mListNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListNews.addItemDecoration(mListViewItemDecoration);
        mAdapter = new ListNewsAdapter(getActivity(), mListNews.getLayoutManager());
        mListNews.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkUtil.getInstance(getActivity()).isNetworkAvailable()) {
            if (mNotifyStateLayout.getVisibility() == View.VISIBLE) {
                mNotifyStateLayout.hide(mListNews);
            }
            LoadDataUtil.getInstance().setOnLoadingListener(onLoadingListener);
            LoadDataUtil.getInstance().getDataFromNetwork(URL_2);
        } else {
            mNotifyStateLayout.show(LayoutNotifyState.TYPE_NETWORK_ERROR_LAYOUT, mListNews);
        }
    }

    private LoadDataUtil.OnLoadingListener onLoadingListener = new LoadDataUtil.OnLoadingListener() {
        @Override
        public void onLoading() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNotifyStateLayout.show(LayoutNotifyState.TYPE_LOADING_LAYOUT, mListNews);
                }
            });
        }

        @Override
        public void onLoadComplete(ArrayList<Object> objects) {
            if (objects != null) {
                mNotifyStateLayout.hide(mListNews);
                for (int i = 0; i < objects.size(); i++) {
                    mAdapter.addItem((News) objects.get(i));
                }
            } else {
                mNotifyStateLayout.show(LayoutNotifyState.TYPE_NO_DATA_LAYOUT, mListNews);
            }
        }
    };

    private void findViews(View rootView) {
        mListNews = (RecyclerView) rootView.findViewById(R.id.list_news);
        mNotifyStateLayout = (LayoutNotifyState) rootView.findViewById(R.id.notify_state_layout);
        rootView.findViewById(R.id.button_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListNews.getLayoutManager() instanceof GridLayoutManager) {
                    mListNews.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mListNews.removeItemDecoration(mGridViewItemDecoration);
                    mListNews.addItemDecoration(mListViewItemDecoration);
                } else {
                    mListNews.setLayoutManager(new GridLayoutManager(getActivity(), NUMBER_OF_COLUMN));
                    mListNews.removeItemDecoration(mListViewItemDecoration);
                    mListNews.addItemDecoration(mGridViewItemDecoration);
                }
                mListNews.setAdapter(mAdapter);
                mAdapter.changeLayoutManager(mListNews.getLayoutManager());
            }
        });
    }

    private static class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ItemHolder> {

        private Context mContext;
        private ArrayList<News> mNewsList;
        private RecyclerView.LayoutManager mLayoutManager;

        public ListNewsAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
            mNewsList = new ArrayList<>();
            mLayoutManager = layoutManager;
            mContext = context;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            if (mLayoutManager instanceof GridLayoutManager) {
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_news, parent, false);
            } else {
                itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_news, parent, false);
            }
            return new ItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            News news = mNewsList.get(position);
            if (TextUtils.isEmpty(news.getImageUrl())) {
                holder.imageNews.setVisibility(View.GONE);
            } else {
                holder.imageNews.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(news.getImageUrl()).centerCrop().into(holder.imageNews);
            }
            holder.newsTitle.setText(news.getTitle());
            holder.description.setText(Html.fromHtml(news.getDescription()));
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }

        public void addItem(News news) {
            mNewsList.add(news);
            notifyItemInserted(mNewsList.size() - 1);
        }

        public void addItems(ArrayList<News> newsList) {
            mNewsList.addAll(newsList);
            notifyDataSetChanged();
        }

        public void changeLayoutManager(RecyclerView.LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
            notifyDataSetChanged();
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            public TextView newsTitle;
            public ImageView imageNews;
            public TextView description;

            public ItemHolder(View itemView) {
                super(itemView);
                newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
                imageNews = (ImageView) itemView.findViewById(R.id.image_news);
                description = (TextView) itemView.findViewById(R.id.description);
                newsTitle.setSelected(true);
            }
        }

    }

}
