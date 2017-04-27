package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.PurchaseListAdapter;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseListActivity extends BaseActivity {
    public static final String TAG = PurchaseListActivity.class.getSimpleName();

    private ArrayList<Purchase> purchases = new ArrayList<>();
    private RecyclerView listPurchase;
    private PurchaseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);

        listPurchase = (RecyclerView)findViewById(R.id.list_purchase_list);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPurchase.setLayoutManager(layoutManager);
        adapter = new PurchaseListAdapter(this, purchases);
        listPurchase.setItemViewCacheSize(10);
        listPurchase.setAdapter(adapter);

        getPurchaseList();
    }

    private void getPurchaseList() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/purchase", new Response.Listener<JSONObject>() {
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
}
