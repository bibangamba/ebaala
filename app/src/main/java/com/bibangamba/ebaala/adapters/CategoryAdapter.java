package com.bibangamba.ebaala.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Category;
import com.bibangamba.ebaala.viewholders.CategoryViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by davy on 5/22/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private List<Category>  categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindDataToView(categories.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size():0;
    }


}
