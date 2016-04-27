package com.framgia.rssfeed.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.rssfeed.R;
import com.framgia.rssfeed.bean.Category;

import java.util.List;


/**
 * Created by VULAN on 4/23/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    private List<Category> mlistNew;
    private LayoutInflater mInflater;
    private OnItemListener mOnItemListener;

    public CategoryAdapter(Context context, List<Category> mlistNew) {
        mInflater = LayoutInflater.from(context);
        this.mlistNew = mlistNew;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_category, parent, false);
        if (view == null) {
            view = mInflater.inflate(R.layout.item_category, parent, false);
        }
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final Category category = mlistNew.get(position);
        holder.setTextNew(category.getText());
        holder.setImagetNew(category.getImage());
        holder.getImageNew().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    mOnItemListener.onClickItem(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistNew.size();
    }

    public void setmOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageNew;
        private TextView mTextNew;

        public viewHolder(View itemView) {
            super(itemView);
            mImageNew = (ImageView) itemView.findViewById(R.id.image_news);
            mTextNew = (TextView) itemView.findViewById(R.id.text_news);
        }

        public void setImagetNew(Bitmap bitmap) {
            mImageNew.setImageBitmap(bitmap);
        }

        public void setTextNew(String text) {
            mTextNew.setText(text);
        }

        public ImageView getImageNew() {
            return mImageNew;
        }

        public TextView getTextNew() {
            return mTextNew;
        }
    }

    public interface OnItemListener {
        void onClickItem(int position);
    }

}
