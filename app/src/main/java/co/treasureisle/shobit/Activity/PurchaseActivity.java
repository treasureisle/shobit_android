package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.PurchaseAdapter;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.R;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseActivity extends BaseActivity {
    public static final String TAG = PurchaseActivity.class.getSimpleName();

    private ArrayList<Order> orders;
    private RecyclerView listPurchase;
    private PurchaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listPurchase = (RecyclerView)findViewById(R.id.list_basket);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPurchase.setLayoutManager(layoutManager);
        adapter = new PurchaseAdapter(this, orders);
        listPurchase.setItemViewCacheSize(10);
        listPurchase.setAdapter(adapter);
    }
}
