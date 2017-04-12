package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.CartAdapter;
import co.treasureisle.shobit.Model.Basket;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.RecyclerItemClickListener;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class CartActivity extends BaseActivity {
    public static final String TAG = CartActivity.class.getSimpleName();

    private Button btnPurchaseAll;
    private RecyclerView listBasket;
    private CartAdapter adapter;
    private TextView textNumSelectedItems;
    private TextView textTotalPrice;
    private Button btnSelectAll;
    private Button btnDeleteItem;
    private Button btnPurchaseItem;

    int numSelectedItems = 0;
    int numTotalPrice = 0;
    boolean [] isSelecteidItem;

    private ArrayList<Basket> baskets = new ArrayList<>();

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


        btnPurchaseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "purchase touched");
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listBasket.setLayoutManager(layoutManager);
        adapter = new CartAdapter(this, baskets);
        listBasket.setAdapter(adapter);

        listBasket.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CheckBox c = (CheckBox) view.findViewById(R.id.chk_item);
                Spinner spnrColorSize = (Spinner) view.findViewById(R.id.spnr_colorsize);
                Spinner spnrAmount = (Spinner) view.findViewById(R.id.spnr_amount);

                if (c.isChecked() && !isSelecteidItem[position]) {
                    isSelecteidItem[position] = true;
                    numSelectedItems++;
                    Post selectedItem = baskets.get(position).getPost();
                    int itemPrice = selectedItem.getPurchasePrice() + selectedItem.getFee();
                    numTotalPrice+=(spnrAmount.getSelectedItemPosition() + 1) * itemPrice;


                } else if (!(c.isChecked()) && isSelecteidItem[position]){
                    isSelecteidItem[position] = false;
                    numSelectedItems--;
                    Post selectedItem = baskets.get(position).getPost();
                    int itemPrice = selectedItem.getPurchasePrice() + selectedItem.getFee();
                    numTotalPrice-=(spnrAmount.getSelectedItemPosition() + 1) * itemPrice;
                }

            }
        }));

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
}
