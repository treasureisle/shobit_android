package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.SellListAdapter;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class SellListActivity extends BaseActivity {
    public static final String TAG = SellListActivity.class.getSimpleName();

    private ArrayList<Purchase> purchases;
    private RecyclerView listPurchase;
    private SellListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listPurchase = (RecyclerView)findViewById(R.id.list_basket);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPurchase.setLayoutManager(layoutManager);
        adapter = new SellListAdapter(this, purchases);
        listPurchase.setItemViewCacheSize(10);
        listPurchase.setAdapter(adapter);
    }
}
