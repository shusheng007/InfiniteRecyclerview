package com.ss007.swiprecycleview;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ss007.swiprecycleview.hoders.LoadMoreViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.RecyclerView;

import static com.ss007.swiprecycleview.LoadState.STATE_ERROR;
import static com.ss007.swiprecycleview.LoadState.STATE_LASTED;
import static com.ss007.swiprecycleview.LoadState.STATE_LOADING;

public abstract class RefreshRecycleAdapter<T> extends RecyclerView.Adapter implements AdapterLoader<T> {
    public static final int TYPE_BOTTOM = 400;

    private LoadState mLoadState = STATE_LOADING;
    private boolean hasMore = true;

    private LayoutMode mLayoutMode = LayoutMode.PULL_AND_LOAD;

    private final List<T> list;

    public RefreshRecycleAdapter(List<T> dataSource) {
        list = dataSource;
    }

    public OnRefreshLoadMoreListener getLoadMoreListener() {
        return refreshLoadMoreListener;
    }

    /**
     * return the data source
     * @return
     */
    public List<T> getList() {
        return list;
    }

    @Override
    public final void appendList(List<T> data) {
        int positionStart = list.size();
        list.addAll(data);
        int itemCount = list.size() - positionStart;

        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(positionStart + 1, itemCount);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent) {
        View loadMore = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_footer_new, parent, false);
        return new LoadMoreViewHolder(loadMore);
    }

    @Override
    public void onBindLoadMoreViewHolder(RecyclerView.ViewHolder holder, final LoadState loadState) {
        if (holder instanceof LoadMoreViewHolder) {
            final LoadMoreViewHolder lHolder = (LoadMoreViewHolder) holder;
            switch (loadState) {
                case STATE_LASTED:
                    lHolder.container.setVisibility(View.VISIBLE);
                    lHolder.container.setOnClickListener(null);
                    lHolder.pb.setVisibility(View.GONE);
                    lHolder.content.setText("---  没有更多了  ---");
                    break;
                case STATE_LOADING:
                    lHolder.container.setVisibility(View.VISIBLE);
                    lHolder.content.setText("加载更多！！");
                    lHolder.container.setOnClickListener(null);
                    lHolder.pb.setVisibility(View.VISIBLE);
                    break;
                case STATE_ERROR:
                    lHolder.container.setVisibility(View.VISIBLE);
                    lHolder.pb.setVisibility(View.GONE);
                    lHolder.content.setText("--- 加载更多失败,点击重试 ---");
                    lHolder.container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (refreshLoadMoreListener != null) {
                                RefreshRecycleAdapter.this.mLoadState = STATE_LOADING;
                                refreshLoadMoreListener.onLoadMore();
                            }
                            lHolder.content.setText("加载更多！！");
                            lHolder.pb.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BOTTOM:
                return onCreateLoadMoreViewHolder(parent);
            default:
                return onCreateCustomViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BOTTOM) {
            onBindLoadMoreViewHolder(holder, mLoadState);
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemLongClickListener != null) {
                        mItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                    }
                    return true;
                }
            });
            onBindCustomViewHolder(holder, position);
        }
    }

    public LayoutMode getLayoutMode() {
        return mLayoutMode;
    }

    public void setLayoutMode(LayoutMode layoutMode) {
        mLayoutMode = layoutMode;
    }

    /**
     * 外部不应该调用此方法
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
    public final void loadingMore() {
        if (hasMore && refreshLoadMoreListener != null) {
            if (mLoadState != STATE_LOADING) {
                mLoadState = STATE_LOADING;
            }
            refreshLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public final int getItemCount() {
        if (mLayoutMode == LayoutMode.PULL_REFRESH || mLayoutMode == LayoutMode.RECYCLER_VIEW) {
            return list.size();
        } else {
            return list.size() == 0 ? 0 : list.size() + 1;
        }
    }

    @Override
    public int getItemRealCount() {
        return list.size();
    }

    @Override
    public final int getItemViewType(int position) {
        if (list.size() > 0 && position < list.size()) {
            return getCustomItemViewType(position);
        } else {
            return TYPE_BOTTOM;
        }
    }


    @Override
    public int getCustomItemViewType(int position) {
        return 0;
    }


    @Override
    public void setHasMore(boolean more) {
        if (mLayoutMode != LayoutMode.INFINITE_LOAD && mLayoutMode != LayoutMode.PULL_AND_LOAD) {
            throw new IllegalStateException("当LayoutMode非INFINITE_LOAD或PULL_AND_LOAD模式时，不可调用此方法");
        }
        hasMore = more;
        if (!hasMore) {
            mLoadState = STATE_LASTED;
            notifyItemRangeChanged(getItemRealCount(), 1);
        }
    }

    @Override
    public final void loadMoreError() {
        mLoadState = STATE_ERROR;
        notifyItemRangeChanged(getItemRealCount(), 1);
    }


    @Nullable
    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Nullable
    private OnItemLongClickListener mItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }


    private OnRefreshLoadMoreListener refreshLoadMoreListener;

    public void setOnRefreshLoadMoreListener(OnRefreshLoadMoreListener listener) {
        this.refreshLoadMoreListener = listener;
    }
}
