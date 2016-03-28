package com.money.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivestar.models.Transaction;
import com.fivestar.models.converters.TransactionCursorConverter;
import com.money.R;

/**
 * Created by skuba on 24.03.2016.
 */
public class CostsRecyclerAdapter extends CursorRecyclerViewAdapter<CostsRecyclerAdapter.CostViewHolder> {

    Context context;

    public CostsRecyclerAdapter(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(CostViewHolder viewHolder, Cursor cursor, int position) {
        Transaction transaction;
        TransactionCursorConverter converter = new TransactionCursorConverter();
        converter.setCursor(cursor);
        transaction = converter.getObject();
        viewHolder.textViewCategory.setText("" + transaction.getCategoryName());
        viewHolder.textViewDescription.setText("" + (transaction.getDescription() != null ? transaction.getDescription() : ""));
        viewHolder.textViewSum.setText("" + transaction.getMoney());

    }

    @Override
    public CostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_cost_fragment, parent, false);
        CostViewHolder holder = new CostViewHolder(view);
        return holder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CostViewHolder extends ViewHolder {

        ImageView icon;
        TextView textViewSum;
        TextView textViewCategory;
        TextView textViewDescription;

        public CostViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_cost_fragment_image);
            textViewSum = (TextView) itemView.findViewById(R.id.item_cost_fragment_tv_sum);
            textViewCategory = (TextView) itemView.findViewById(R.id.item_cost_fragment_tv_category);
            textViewDescription = (TextView) itemView.findViewById(R.id.item_cost_fragment_tv_description);
        }
    }
}
