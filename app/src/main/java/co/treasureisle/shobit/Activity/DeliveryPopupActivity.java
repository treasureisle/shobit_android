package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 27..
 */

public class DeliveryPopupActivity extends BaseActivity {
    final static String TAG = DeliveryPopupActivity.class.getSimpleName();

    private Purchase purchase;
    private Spinner spnrDelivery;
    private EditText textDelivery;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_delivery);

        purchase = getIntent().getParcelableExtra(IntentTag.PURCHASE);

        spnrDelivery = (Spinner)findViewById(R.id.spnr_delivery);
        textDelivery = (EditText)findViewById(R.id.text_delivery);
        btnSave = (Button)findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        final ArrayAdapter<String> spnrDeliveryAdapter = new ArrayAdapter<>(
                context, R.layout.spinner_item, Constants.delivery_corp);

        spnrDeliveryAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnrDelivery.setAdapter(spnrDeliveryAdapter);
    }

    private void save() {
        String deliveryCode = String.valueOf(spnrDelivery.getSelectedItemPosition());
        String deliveryNumber = textDelivery.getText().toString();

        HashMap<String,String> params = new HashMap<>();

        params.put("delivery_code", deliveryCode);
        params.put("delivery_number",deliveryNumber);

        MultipartRequest req = new MultipartRequest(context, com.android.volley.Request.Method.POST, "/sell/" + purchase.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.showToast(context, "저장되었습니다");
                        finish();
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
