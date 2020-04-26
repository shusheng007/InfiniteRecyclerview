package com.ss007.swiprecycleview.hoders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ss007.swiprecycleview.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ben.Wang
 *
 * @author Ben.Wang
 * @modifier
 * @createDate 2019/12/30 13:43
 * @description
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout container;
    public final ProgressBar pb;
    public final TextView content;

    public LoadMoreViewHolder(@NonNull View itemView) {
        super(itemView);
        container = (LinearLayout) itemView.findViewById(R.id.footer_container);
        pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        content = (TextView) itemView.findViewById(R.id.content);
    }
}
