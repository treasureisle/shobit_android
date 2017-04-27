package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.SellListAdapter;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class SellListActivity extends BaseActivity {
    public static final String TAG = SellListActivity.class.getSimpleName();

    private ArrayList<Purchase> purchases = new ArrayList<>();
    private RecyclerView listPurchase;
    private SellListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_list);

        listPurchase = (RecyclerView)findViewById(R.id.list_purchase_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPurchase.setLayoutManager(layoutManager);
        adapter = new SellListAdapter(this, purchases);
        listPurchase.setItemViewCacheSize(10);
        listPurchase.setAdapter(adapter);

        getSellList();
    }

    private void getSellList() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/sell", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray purchaseArray = response.getJSONArray("purchase");
                    purchases.clear();

                    for (int i = 0; i < purchaseArray.length(); i++) {
                        Purchase purchase = new Purchase(purchaseArray.getJSONObject(i));
                        purchases.add(purchase);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSellList();

    }
}
