package co.treasureisle.shobit;


import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import co.treasureisle.shobit.Model.Session;
import co.treasureisle.shobit.Request.MultipartRequest;


/**
 * Created by pgseong on 2017. 4. 28..
 */

public class ShobitFirebaseInstaceIDHelper extends FirebaseInstanceIdService {

    private static final String TAG = ShobitFirebaseInstaceIDHelper.class.getSimpleName();

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // token 저장
        if (SessionHelper.getSession(getApplicationContext()) == null) return;

        HashMap<String,String> params = new HashMap<>();

        params.put("device_token", token);

        MultipartRequest req = new MultipartRequest(getApplicationContext(), com.android.volley.Request.Method.POST, "/firebase", params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

        VolleySingleTon.getInstance(getApplicationContext()).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
