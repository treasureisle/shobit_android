package co.treasureisle.shobit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Activity.PurchaseActivity;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.CartViewHolder;
import co.treasureisle.shobit.ViewHolder.PurchaseViewHolder;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DetailImageAdapter.class.getSimpleName();

    private PurchaseActivity mActivity;
    private ArrayList<Order> orders;

    public PurchaseAdapter(PurchaseActivity activity, ArrayList<Order> orders) {
        this.mActivity = activity;
        this.orders = orders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.cart, parent, false);

        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PurchaseViewHolder) {
            ((PurchaseViewHolder) holder).onBind(mActivity, orders.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}