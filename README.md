# InfiniteRecyclerview
# 前言
  在日常从事Android开发工作时，经常会遇到下拉刷新列表页面，上拉自动加载列表的需求，
  GitHub上有很多关于这方面的功能极其强大的类库了，那此库还有存在的必要性吗？

 答案是：**有**，因为那些NB类库实在是**太重**了！很多时候我们只使用其5%的功能，却不得不忍受剩下95%“无用”的功能和复杂性。

# 效果
  ![infinite loading](https://github.com/shusheng007/InfiniteRecyclerview/blob/master/pictures/gif1.gif)

# 如何使用

1. 自定义`Adapter`,需要继承 `RefreshRecycleAdapter<T>`
2. 像使用`RecyclerView`一样

教程：

