package com.ss007.swiprecycleview;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/12/30 15:01
 * @description
 */
public enum LayoutMode {
    PULL_AND_LOAD,// 可下拉，可加载
    INFINITE_LOAD,//只可加载更多，不可下拉刷新
    PULL_REFRESH,//只可下拉刷新，不可加载更多
    RECYCLER_VIEW//不可下拉，不可加载，就是一个普通的recycleView
}
