package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.Address;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Session;
import co.treasureisle.shobit.Model.UserDetail;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.AddressRequest;
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
    private ArrayList<String> listAddress = new ArrayList<>();
    ArrayList<Address> addresses = new ArrayList<>();

    private PurchaseAdapter adapter;
    private Session me;
    private UserDetail userDetail;
    private Address selectedAddr;
    private int payment = 0; //0:카드 1:계좌이체 2:모바일
    private int totalPrice = 0;
    private boolean isPaid = false;

    RecyclerView listPurchase;

    Button btnSavedAddress;
    Button btnRecentAddress;
    Button btnNewAddress;
    Button btnSearch;

    EditText textZipcode;
    EditText textAddress;
    Spinner spnrAddress;
    ArrayAdapter<String> spnrAddressAdapter;

    EditText textAddressDetail;
    EditText textName;
    EditText textPhone;
    EditText textComment;

    CheckBox chkPolicy;
    Button btnCard;
    Button btnBank;
    Button btnMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase);

        listAddress.add("먼저 검색해주세요");

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

        textZipcode = (EditText)findViewById(R.id.text_zipcode);
        btnSearch = (Button)findViewById(R.id.btn_search);

        textAddress = (EditText)findViewById(R.id.text_address);
        spnrAddress = (Spinner)findViewById(R.id.spnr_address);
        textAddressDetail = (EditText)findViewById(R.id.text_address_detail);
        textName = (EditText)findViewById(R.id.text_name);
        textPhone = (EditText)findViewById(R.id.text_phone);
        textComment = (EditText)findViewById(R.id.text_comment);

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress(1);
            }
        });

        spnrAddressAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, listAddress);

        spnrAddressAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnrAddress.setAdapter(spnrAddressAdapter);

        spnrAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (addresses.size() == 0) return;
                selectedAddr = addresses.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textZipcode.setEnabled(false);
        textAddress.setEnabled(false);
        textAddress.setVisibility(View.INVISIBLE);
        spnrAddress.setEnabled(false);
        spnrAddress.setVisibility(View.INVISIBLE);
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
                            spnrAddress.setVisibility(View.VISIBLE);
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
                            textAddress.setVisibility(View.VISIBLE);
                            textAddress.setText(userDetail.getRecentAdd1());
                            textAddressDetail.setText(userDetail.getRecentAdd2());
                            textName.setText(userDetail.getRecentName());
                            textPhone.setText(userDetail.getRecentPhone());
                        }
                    } else {
                        textZipcode.setText(String.valueOf(userDetail.getRecentZipcode()));
                        textAddress.setVisibility(View.VISIBLE);
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

    private void searchAddress(int page){
        String url = Constants.ADDRESS_API_URL;
        url = url + "?keyword=" + textZipcode.getText().toString();
        url = url + "&currentPage=" + page;
        url = url + "&confmKey=" + Constants.ADDRESS_CONFIRM_KEY;
        url = url + "&countPerPage=200";
        url = url + "&resultType=json";

        final AddressRequest req = new AddressRequest(this, Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray addressArray = response.getJSONObject("results").getJSONArray("juso");

                    listAddress.clear();
                    listAddress.add("주소를 선택해주세요");

                    for (int i = 0; i < addressArray.length(); i++) {
                        Address address = new Address(addressArray.getJSONObject(i));
                        addresses.add(address);
                        String strAddr =
                                "우편변호: " + address.getZipNo() +
                                "\n도로명주소: " + address.getRoadAddrPart1() +
                                "\n지번주소: " + address.getJibunAddr();
                        listAddress.add(strAddr);
                    }

                    spnrAddressAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, listAddress);

                    spnrAddressAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spnrAddress.setAdapter(spnrAddressAdapter);

                    spnrAddress.setEnabled(true);

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
            params.put("zipcode", selectedAddr.getZipNo() + "");
            params.put("address1", selectedAddr.getRoadAddrPart1());
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
