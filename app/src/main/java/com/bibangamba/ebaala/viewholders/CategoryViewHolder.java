package com.bibangamba.ebaala.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Category;
import com.bibangamba.ebaala.model.Event;

/**
 * Created by bibangamba on 10/18/2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView eventNameTV;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        eventNameTV = (TextView) itemView.findViewById(R.id.list_header);
    }

    public void bindDataToView(Category category) {
        eventNameTV.setText(category.getName());
    }
}
