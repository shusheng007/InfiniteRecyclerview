package top.ss007.infiniterecyclerview.ui.beautyList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss007.swiprecycleview.RefreshRecycleAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import top.ss007.infiniterecyclerview.R;
import top.ss007.infiniterecyclerview.databinding.ItemBeautyBinding;
import top.ss007.infiniterecyclerview.ui.main.Beauty;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2020/4/26 14:25
 * @description
 */
public class BeautyListAdapter extends RefreshRecycleAdapter<Beauty> {
    public BeautyListAdapter(List<Beauty> dataSource) {
        super(dataSource);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beauty,parent,false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeautyViewHolder beautyViewHolder= (BeautyViewHolder) holder;
        final Beauty beauty=getList().get(position);
        beautyViewHolder.binding.ivBeautyHead.setImageResource(beauty.getPhotoId());
        beautyViewHolder.binding.tvBeautyTitle.setText(beauty.getName());
        beautyViewHolder.binding.tvBeautyIntroduction.setText(beauty.getIntroduction());
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder{
        private final ItemBeautyBinding binding;
        public BeautyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= ItemBeautyBinding.bind(itemView);
        }
    }
}
