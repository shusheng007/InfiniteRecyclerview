package com.ss007.swiprecycleview;


import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public interface AdapterLoader<T> {

    void onBindLoadMoreViewHolder(RecyclerView.ViewHolder holder, LoadState loadState);

    RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent);

    void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position);

    RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);

    /**
     * if has more data to load
     *
     * @param hasMore
     */
    void setHasMore(boolean hasMore);

    /**
     * invoke it when loading error happened
     */
    void loadMoreError();

    void setLayoutMode(LayoutMode layoutMode);

    /**
     *
     * @param data the data you want to add
     */
    void appendList(List<T> data);

    /**
     *当列表中包含不同类型view时，重写此方法
     *  instead of {@link androidx.recyclerview.widget.RecyclerView.Adapter#getItemViewType(int)}
     *
     * @param position
     * @return
     */
    int getCustomItemViewType(int position);

    /**
     * 获取当前数据源个数，不包括最后一个loading item
     *
     * instead of {@link RecyclerView.Adapter#getItemCount()}
     * @return current list size!
     */
    int getItemRealCount();

    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(View itemView, int position);
    }

    interface OnRefreshLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

}
