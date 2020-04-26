package com.ss007.swiprecycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshRecycleView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    @Nullable
    private RecyclerView.LayoutManager manager;
    private RefreshRecycleAdapter adapter;

    @Nullable
    private SpanSizeCallBack spanSizeCallBack;
    private OnScrollListener scrollListener;

    public SwipeRefreshRecycleView(Context context) {
        this(context, null);
    }

    public SwipeRefreshRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_recycler, this, false);
        mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycle_view);
        mRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.container);
        mRecyclerView.setVerticalScrollBarEnabled(false);
        mRefreshLayout.setOnRefreshListener(this);
//        mRecyclerView.setItemAnimator(new DefaultAnimator());
        mRecyclerView.addOnScrollListener(new OnScrollToBottomListener());

        mEmptyView = LayoutInflater.from(context).inflate(R.layout.view_mepty, this, false);
        mEmptyView.setVisibility(GONE);
        addView(mEmptyView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(inflate, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public void setLayoutManager(final RecyclerView.LayoutManager manager) {
        this.manager = manager;
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getItemViewType(position)) {
                        case RefreshRecycleAdapter.TYPE_BOTTOM:
                            return ((GridLayoutManager) manager).getSpanCount();
                        default:
                            return (spanSizeCallBack != null ? spanSizeCallBack.getSpanSize(position) : 0) == 0 ? 1 : spanSizeCallBack.getSpanSize(position);
                    }
                }
            });
        }
        mRecyclerView.setLayoutManager(manager);
    }


    public void setAdapter(RefreshRecycleAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        if (adapter.getLayoutMode() == LayoutMode.INFINITE_LOAD || adapter.getLayoutMode() == LayoutMode.RECYCLER_VIEW) {
            mRefreshLayout.setEnabled(false);
        }
        this.adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
        });
    }

    public void setRefresh(final boolean refresh) {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(refresh);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (adapter != null && adapter.getLoadMoreListener() != null) {
            adapter.getLoadMoreListener().onRefresh();
        }
    }

    /**
     * 获取内部的 RecyclerView
     *
     * @return
     */
    public RecyclerView getRealRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 获取内部的 SwipeRefreshLayout
     *
     * @return
     */
    public SwipeRefreshLayout getRealSwipeRefreshLayout() {
        return mRefreshLayout;
    }

    private void checkEmpty(){
        mEmptyView.setVisibility(this.adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


    private class OnScrollToBottomListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //用户自定义scrollListener
            if (null != scrollListener) {
                scrollListener.onScrolled(SwipeRefreshRecycleView.this, dx, dy);
            }
            if (null == manager) {
                throw new IllegalStateException("call setLayoutManager() first!!");
            }
            if (null == adapter) {
                throw new IllegalStateException("call setAdapter() first!!");
            }
            if (manager instanceof LinearLayoutManager) {
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();

                if (adapter.getItemCount() > 1 && lastCompletelyVisibleItemPosition >= adapter.getItemCount() - 1) {
                    adapter.loadingMore();
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int[] itemPositions = new int[2];
                ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(itemPositions);
                int lastVisibleItemPosition = (itemPositions[1] != 0) ? ++itemPositions[1] : ++itemPositions[0];

                if (lastVisibleItemPosition >= adapter.getItemCount()) {
                    adapter.loadingMore();
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    }


    public void setOnScrollListener(OnScrollListener listener) {
        this.scrollListener = listener;
    }

    /**
     * you should call this method when you want to specified SpanSize.
     *
     * @param spanSizeCallBack
     */
    public void setSpanSizeCallBack(@NonNull SpanSizeCallBack spanSizeCallBack) {
        this.spanSizeCallBack = spanSizeCallBack;
    }

    public interface SpanSizeCallBack {
        int getSpanSize(int position);
    }

    public interface OnScrollListener {
        void onScrolled(SwipeRefreshRecycleView recyclerView, int dx, int dy);
    }
}
