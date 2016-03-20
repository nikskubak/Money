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
    OnItemCLickListener listener;

    public CategoryRecyclerAdapter(ArrayList<Category> data, OnItemCLickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder;
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        holder = new CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((CategoryViewHolder) holder).setPos(position);
        ((CategoryViewHolder) holder).textViewCategoryName.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CategoryViewHolder extends ViewHolder {

        int pos;
        TextView textViewCategoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pos);
                }
            });
            textViewCategoryName = (TextView) itemView.findViewById(R.id.item_category_tv_name);
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }

    public interface OnItemCLickListener {
        void onItemClick(int position);
    }

}
