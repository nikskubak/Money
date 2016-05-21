package com.money.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivestar.models.Recommendation;
import com.money.R;

import java.util.ArrayList;

/**
 * Created by skuba on 21.05.2016.
 */
public class RecommendRecyclerAdapter extends RecyclerView.Adapter<RecommendRecyclerAdapter.ViewHolder> {

    ArrayList<Recommendation> data;
    Context context;

    public RecommendRecyclerAdapter(ArrayList<Recommendation> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        RecommendViewHolder holder = new RecommendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecommendViewHolder recommendViewHolder = (RecommendViewHolder)holder;
        recommendViewHolder.description.setText(data.get(position).getDescription());
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

    public class RecommendViewHolder extends ViewHolder {

        TextView description;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.rec_description_text_view);
        }
    }
}
