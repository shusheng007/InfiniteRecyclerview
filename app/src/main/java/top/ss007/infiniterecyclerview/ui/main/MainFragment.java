package top.ss007.infiniterecyclerview.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ss007.swiprecycleview.AdapterLoader;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import top.ss007.infiniterecyclerview.databinding.MainFragmentBinding;
import top.ss007.infiniterecyclerview.ui.beautyList.BeautyListAdapter;

public class MainFragment extends Fragment {
    private static final int PAGE_SIZE = 5;

    private MainViewModel mViewModel;
    private MainFragmentBinding mBinding;

    private BeautyListAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = MainFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initBeautyList(getActivity());

        mViewModel.getBeautyListByPage(0, PAGE_SIZE);
    }

    private void initBeautyList(Context context) {
        mAdapter = new BeautyListAdapter(mViewModel.dataSource);
        mAdapter.setHasMore(true);
        mBinding.pullRefreshList.setLayoutManager(new LinearLayoutManager(context));
        mBinding.pullRefreshList.setAdapter(mAdapter);
        mAdapter.setOnRefreshLoadMoreListener(new AdapterLoader.OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(context, "finish refreshing", Toast.LENGTH_SHORT).show();
                mBinding.pullRefreshList.setRefresh(false);
            }

            @Override
            public void onLoadMore() {
                mViewModel.getBeautyListByPage(mViewModel.dataSource.size(), PAGE_SIZE);
            }
        });
        mViewModel.getBeautyLd().observe(getViewLifecycleOwner(), beauties -> {
            if (beauties == null || beauties.isEmpty()) {
                mAdapter.setHasMore(false);
                return;
            }
            //模拟延时
            new Handler().postDelayed(() -> {
                mViewModel.dataSource.addAll(beauties);
                mAdapter.notifyDataSetChanged();
            }, mViewModel.dataSource.isEmpty() ? 0 : 1000 * 1);

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}