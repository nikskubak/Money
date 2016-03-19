package com.money;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fivestar.models.Category;

import java.util.ArrayList;

/**
 * Created by skuba on 19.03.2016.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    ArrayList<Category> data;

    public CategoryRecyclerAdapter(ArrayList<Category> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        holder = new CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((CategoryViewHolder)holder).textViewCategoryName.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CategoryViewHolder extends ViewHolder{

        TextView textViewCategoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textViewCategoryName = (TextView)itemView.findViewById(R.id.item_category_tv_name);
        }
    }

}
