package com.money.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivestar.models.Transaction;
import com.fivestar.models.converters.TransactionCursorConverter;
import com.money.Constants;
import com.money.R;

/**
 * Created by skuba on 24.03.2016.
 */
public class CostsRecyclerAdapter extends CursorRecyclerViewAdapter<CostsRecyclerAdapter.NewCostViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    public CostsRecyclerAdapter(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(NewCostViewHolder viewHolder, Cursor cursor, int position) {
        Transaction transaction;
        TransactionCursorConverter converter = new TransactionCursorConverter();
        converter.setCursor(cursor);
        transaction = converter.getObject();
//        viewHolder.textViewCategory.setText("" + transaction.getCategoryName());
        viewHolder.textViewDescription.setText("" + (transaction.getDescription() != null ? transaction.getDescription() : ""));
        viewHolder.textViewDate.setText("6 дней назад");
        viewHolder.textViewSum.setText(String.valueOf(transaction.getMoney()));
        viewHolder.textViewSum.setTextColor(transaction.getType().equals(context.getString(R.string.category_type_gain)) ?
                context.getResources().getColor(R.color.colorAccent) :
                context.getResources().getColor(R.color.primary));
        viewHolder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

    }

    @Override
    public NewCostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_operation_recycler, parent, false);
        NewCostViewHolder holder = new NewCostViewHolder(view);
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

    public class NewCostViewHolder extends ViewHolder {
        CardView cardView;
        ImageView icon;
        TextView textViewSum;
        TextView textViewDescription;
        TextView textViewDate;

        public NewCostViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.operation_icon);
            textViewSum = (TextView) itemView.findViewById(R.id.operation_sum);
            textViewDate = (TextView) itemView.findViewById(R.id.operation_date);
            textViewDescription = (TextView) itemView.findViewById(R.id.operation_descr);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

//    private void setItemColor(View view, int color){
//        LayerDrawable layerDrawable = (LayerDrawable) context.getResources()
//                .getDrawable(R.drawable.item_operation_view_color);
//        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
//                .findDrawableByLayerId(R.id.color_operation_layer);
//        gradientDrawable.setColor(color); // change color
//        view.setBackground(gradientDrawable);
//    }
}
