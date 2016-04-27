package com.framgia.rssfeed.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.rssfeed.R;
import com.framgia.rssfeed.bean.News;

import java.util.ArrayList;

/**
 * Created by VULAN on 4/27/2016.
 */
public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ItemHolder> {

    private Context mContext;
    private ArrayList<News> mNewsList;
    private RecyclerView.LayoutManager mLayoutManager;
    private onClickNewsListener mOnClickNewsListener;

    public ListNewsAdapter(Context context, RecyclerView.LayoutManager layoutManager) {
        mNewsList = new ArrayList<>();
        mLayoutManager = layoutManager;
        mContext = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (mLayoutManager instanceof GridLayoutManager) {
            itemView = LayoutInflater.from(mContext).inflate(com.framgia.rssfeed.R.layout.item_grid_news, parent, false);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(com.framgia.rssfeed.R.layout.item_list_news, parent, false);
        }
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final News news = mNewsList.get(position);
        if (TextUtils.isEmpty(news.getImageUrl())) {
            holder.imageNews.setVisibility(View.GONE);
        } else {
            holder.imageNews.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(news.getImageUrl()).centerCrop().into(holder.imageNews);
        }
        holder.newsTitle.setText(news.getTitle());
        holder.description.setText(Html.fromHtml(news.getDescription()));
        holder.layoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickNewsListener != null) {
                    mOnClickNewsListener.onClickNews(news);
                }
            }
        });
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

    public void setOnClickNewsListener(onClickNewsListener mOnClickNewsListener) {
        this.mOnClickNewsListener = mOnClickNewsListener;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        public TextView newsTitle;
        public ImageView imageNews;
        public TextView description;
        public LinearLayout layoutNews;

        public ItemHolder(View itemView) {
            super(itemView);
            layoutNews = (LinearLayout) itemView.findViewById(R.id.item_news_layout);
            newsTitle = (TextView) itemView.findViewById(com.framgia.rssfeed.R.id.newsTitle);
            imageNews = (ImageView) itemView.findViewById(com.framgia.rssfeed.R.id.image_news);
            description = (TextView) itemView.findViewById(com.framgia.rssfeed.R.id.description);
            newsTitle.setSelected(true);
        }
    }

    public interface onClickNewsListener {
        void onClickNews(News item);
    }

}
