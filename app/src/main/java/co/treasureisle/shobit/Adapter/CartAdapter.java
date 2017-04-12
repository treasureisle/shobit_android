package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Basket;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.CartViewHolder;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DetailImageAdapter.class.getSimpleName();

    private Activity mActivity;
    private ArrayList<Basket> baskets;

    public CartAdapter(Activity activity, ArrayList<Basket> baskets) {
        this.mActivity = activity;
        this.baskets = baskets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.cart, parent, false);

        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartViewHolder) {
            ((CartViewHolder) holder).onBind(mActivity, baskets.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return baskets.size();
    }

}
