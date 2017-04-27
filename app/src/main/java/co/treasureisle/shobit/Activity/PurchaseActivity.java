package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.treasureisle.shobit.Adapter.PurchaseAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Session;
import co.treasureisle.shobit.Model.UserDetail;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.SessionHelper;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseActivity extends BaseActivity {
    public static final String TAG = PurchaseActivity.class.getSimpleName();

    private ArrayList<Order> orders;

    private PurchaseAdapter adapter;
    private Session me;
    private UserDetail userDetail;
    private int payment = 0; //0:카드 1:계좌이체 2:모바일
    private int totalPrice = 0;
    private boolean isPaid = false;

    RecyclerView listPurchase;

    Button btnSavedAddress;
    Button btnRecentAddress;
    Button btnNewAddress;

    TextView textZipcode;
    TextView textAddress;
    TextView textAddressDetail;
    TextView textName;
    TextView textPhone;
    TextView textComment;

    CheckBox chkPolicy;
    Button btnCard;
    Button btnBank;
    Button btnMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase);

        me = SessionHelper.getSession(context);

        orders = getIntent().getParcelableArrayListExtra(IntentTag.ORDERS);

        for (int i=0; i<orders.size(); i++){
            Order order = orders.get(i);
            Post post = order.getPost();
            int price = (post.getPurchasePrice() + post.getFee()) * order.getAmount();
            totalPrice+=price;
        }

        listPurchase = (RecyclerView)findViewById(R.id.list_order);

        btnSavedAddress = (Button)findViewById(R.id.btn_saved_addr);
        btnRecentAddress = (Button)findViewById(R.id.btn_recent_addr);
        btnNewAddress = (Button)findViewById(R.id.btn_new_addr);

        textZipcode = (TextView)findViewById(R.id.text_zipcode);
        textAddress = (TextView)findViewById(R.id.text_address);
        textAddressDetail = (TextView)findViewById(R.id.text_address_detail);
        textName = (TextView)findViewById(R.id.text_name);
        textPhone = (TextView)findViewById(R.id.text_phone);
        textComment = (TextView)findViewById(R.id.text_comment);

        chkPolicy = (CheckBox)findViewById(R.id.chk_policy);
        btnCard = (Button)findViewById(R.id.btn_card);
        btnBank = (Button)findViewById(R.id.btn_bank_trans);
        btnMobile = (Button)findViewById(R.id.btn_mobile);

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCard();
            }
        });

        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBank();
            }
        });

        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payMobile();
            }
        });

        textZipcode.setEnabled(false);
        textAddress.setEnabled(false);
        textAddressDetail.setEnabled(false);
        textName.setEnabled(false);
        textPhone.setEnabled(false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        listPurchase.setLayoutManager(layoutManager);
        adapter = new PurchaseAdapter(this, orders);
        listPurchase.setItemViewCacheSize(10);
        listPurchase.setAdapter(adapter);

        fetchAdress();
    }

    private void payCard(){
        Utils.showToast(context, "준비중입니다.");
//        Intent i = new Intent(this, PayCardActivity.class);
//        i.putExtra(IntentTag.PRICE, totalPrice);
//        startActivity(i);
    }

    private void payBank(){
        purchase();
    }

    private void payMobile(){
        Utils.showToast(context, "준비중입니다.");
//        Intent i = new Intent(this, PayMobileActivity.class);
//        i.putExtra(IntentTag.PRICE, totalPrice);
//        startActivity(i);
    }

    private void fetchAdress() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/user_detail/" + me.getUuid(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject userDetailJson = response.getJSONObject("user_detail");
                    userDetail = new UserDetail(userDetailJson);

                    if(Utils.isNullString(userDetail.getAddress1())){
                        btnSavedAddress.setEnabled(false);
                        if(Utils.isNullString(userDetail.getRecentAdd1())){
                            btnRecentAddress.setEnabled(false);
                            textZipcode.setEnabled(true);
                            textAddress.setEnabled(true);
                            textAddressDetail.setEnabled(true);
                            textName.setEnabled(true);
                            textPhone.setEnabled(true);
                            textZipcode.setText("");
                            textAddress.setText("");
                            textAddressDetail.setText("");
                            textName.setText("");
                            textPhone.setText("");
                        } else {
                            textZipcode.setText(String.valueOf(userDetail.getRecentZipcode()));
                            textAddress.setText(userDetail.getRecentAdd1());
                            textAddressDetail.setText(userDetail.getRecentAdd2());
                            textName.setText(userDetail.getRecentName());
                            textPhone.setText(userDetail.getRecentPhone());
                        }
                    } else {
                        textZipcode.setText(String.valueOf(userDetail.getRecentZipcode()));
                        textAddress.setText(userDetail.getRecentAdd1());
                        textAddressDetail.setText(userDetail.getRecentAdd2());
                        textName.setText(userDetail.getRecentName());
                        textPhone.setText(userDetail.getRecentPhone());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    private void purchase(){
        for(int i=0; i<orders.size(); i++) {
            Order order = orders.get(i);
            Post post = order.getPost();
            int price = (post.getPurchasePrice() + post.getFee()) * order.getAmount();

            HashMap<String,String> params = new HashMap<>();

            params.put("color_size_id", String.valueOf(order.getColorSize().getId()));
            params.put("amount", String.valueOf(order.getAmount()));
            params.put("price", String.valueOf(price));
            params.put("payment", String.valueOf(payment));
            params.put("name", textName.getText().toString());
            params.put("zipcode", textZipcode.getText().toString());
            params.put("address1", textAddress.getText().toString());
            params.put("address2", textAddressDetail.getText().toString());
            params.put("phone", textPhone.getText().toString());
            params.put("comment", textComment.getText().toString());
            params.put("is_paid", isPaid?"1":"0");

            MultipartRequest req = new MultipartRequest(context, com.android.volley.Request.Method.POST, "/purchase/" + post.getId(), params, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.showToast(context, "주문이 성공적으로 완료되었습니다.");
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 500) {
                                Log.d(TAG, error.networkResponse.toString());
                            } else {
                                Log.e(TAG, error.networkResponse.toString());
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(context).addToRequestQueue(req, new DefaultRetryPolicy(
                    5 * 60 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQ_CARD:
                if (resultCode == RESULT_OK) {
                    payment=0;
                    isPaid=true;
                    purchase();
                } else {

                }
                break;
            case RequestCode.REQ_MOBILE:
                if (resultCode == RESULT_OK) {
                    payment=2;
                    isPaid=true;
                    purchase();
                } else {

                }
                break;
        }
    }
}
