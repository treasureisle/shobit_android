package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.CartAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Basket;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class CartActivity extends BaseActivity {
    public static final String TAG = CartActivity.class.getSimpleName();

    private Button btnPurchaseAll;
    private RecyclerView listBasket;
    private CartAdapter adapter;
    public TextView textNumSelectedItems;
    public TextView textTotalPrice;
    private Button btnSelectAll;
    private Button btnDeleteItem;
    private Button btnPurchaseItem;

    public int numSelectedItems = 0;
    public int numTotalPrice = 0;
    public boolean [] isSelecteidItem;

    public ArrayList<Basket> baskets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btnPurchaseAll = (Button)findViewById(R.id.btn_purchase_all);
        listBasket = (RecyclerView)findViewById(R.id.list_basket);
        textNumSelectedItems = (TextView)findViewById(R.id.text_num_items);
        textTotalPrice = (TextView)findViewById(R.id.text_total_price);
        btnSelectAll = (Button)findViewById(R.id.btn_select_all);
        btnDeleteItem = (Button)findViewById(R.id.btn_delete_item);
        btnPurchaseItem = (Button)findViewById(R.id.btn_purchase_item);

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll();
            }
        });


        btnPurchaseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasAll();
            }
        });

        btnPurchaseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase();
            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listBasket.setLayoutManager(layoutManager);
        adapter = new CartAdapter(this, baskets);
        listBasket.setItemViewCacheSize(10);
        listBasket.setAdapter(adapter);

        getBaskets();

    }

    private void getBaskets() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/basket", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray basketArray = response.getJSONArray("basket");
                    baskets.clear();
                    isSelecteidItem = null;
                    isSelecteidItem = new boolean[basketArray.length()];

                    for (int i = 0; i < basketArray.length(); i++) {
                        Basket basket = new Basket(basketArray.getJSONObject(i));
                        baskets.add(basket);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    public void recognizeTotalPrice() {
        numTotalPrice = 0;
        numSelectedItems = 0;
        for (int i=0; i<baskets.size(); i++) {
            if (isSelecteidItem[i]) {
                numSelectedItems += 1;
                Basket selectedBasket = baskets.get(i);
                Post selectedPost = selectedBasket.getPost();
                int itemPrice = (selectedPost.getPurchasePrice() + selectedPost.getFee());
                int amount = selectedBasket.getAmount();
                numTotalPrice += itemPrice * amount;
            }
        }
        textNumSelectedItems.setText(numSelectedItems + " 아이템");
        textTotalPrice.setText(Utils.numberFormat(numTotalPrice) + " 원");
    }

    private void checkAll() {
        boolean isAllSelected = true;
        for (int i=0; i<baskets.size(); i++) {
            if (!isSelecteidItem[i]) {
                isAllSelected = false;
            }
        }

        if (isAllSelected) {
            for (int i=0; i<baskets.size(); i++) { isSelecteidItem[i] = false; }
        } else {
            for (int i=0; i<baskets.size(); i++) { isSelecteidItem[i] = true; }
        }

        recognizeTotalPrice();

        listBasket.getAdapter().notifyDataSetChanged();

    }

    private void purchasAll() {
        checkAll();
        purchase();
    }

    private void purchase() {
        ArrayList<Order> orders = new ArrayList<>();
        for(int i = 0; i<isSelecteidItem.length; i++) {
            if(isSelecteidItem[i]) {
                Basket basket = baskets.get(i);
                Order order = new Order(basket.getPost(), basket.getColorSize(), basket.getAmount());
                orders.add(order);
            }
        }
        Intent i = new Intent(this, PurchaseActivity.class);
        i.putExtra(IntentTag.ORDERS, orders);
        startActivity(i);
    }

    private void delete() {

    }
}
