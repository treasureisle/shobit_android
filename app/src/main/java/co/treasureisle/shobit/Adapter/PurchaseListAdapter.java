package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.CartViewHolder;
import co.treasureisle.shobit.ViewHolder.PurchaseListViewHolder;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PurchaseListAdapter.class.getSimpleName();

    private Activity mActivity;
    private ArrayList<Purchase> purchases;

    public PurchaseListAdapter(Activity activity, ArrayList<Purchase> purchases) {
        this.mActivity = activity;
        this.purchases = purchases;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.purchase_list, parent, false);

        return new PurchaseListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PurchaseListViewHolder) {
            ((PurchaseListViewHolder) holder).onBind(mActivity, purchases.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

}