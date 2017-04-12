package co.treasureisle.shobit.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import co.treasureisle.shobit.BuildConfig;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 1. 25..
 */

public class ShobitRequest extends JsonObjectRequest {
    private static final String TAG = ShobitRequest.class.getSimpleName();

    Context context;

    public ShobitRequest(final Context context, int method, String url, JSONObject obj) {
        super(method, BuildConfig.API_URL + url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, makeDefaultErrorListener(context));

        this.context = context;
    }

    public ShobitRequest(final Context context, int method, String url, JSONObject obj, Response.Listener<JSONObject> listener) {
        super(method, BuildConfig.API_URL + url, obj, listener, makeDefaultErrorListener(context));

        this.context = context;
    }

    public ShobitRequest(final Context context, int method, String url, JSONObject obj, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, BuildConfig.API_URL + url, obj, listener, errorListener);

        this.context = context;
    }

    public ShobitRequest(Context context, int method, String url) {
        super(method, BuildConfig.API_URL + url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, makeDefaultErrorListener(context));

        this.context = context;
    }

    public ShobitRequest(Context context, int method, String url, Response.Listener<JSONObject> listener) {
        super(method, BuildConfig.API_URL + url, new JSONObject(), listener, makeDefaultErrorListener(context));

        this.context = context;
    }

    public ShobitRequest(Context context, int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, BuildConfig.API_URL + url, new JSONObject(), listener, errorListener);

        this.context = context;
    }

    private static Response.ErrorListener makeDefaultErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse");
                if (error != null) {
                    if (error.getMessage() != null) {
                        Log.d(TAG, error.getLocalizedMessage());
                    }

                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            Utils.showLostConnectionToast(context);
                        }
                    }
                }
            }
        };
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        SharedPreferences pref = Utils.getPrefs(context);
        String userId = pref.getString(PrefTag.USER_ID, null);
        String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);
        if (userId != null && accessToken != null) {
            params.put("Authorization", userId + ":" + accessToken);
            Log.d(TAG, "Authorization: " + userId + ":" + accessToken);
        } else {
            Log.d(TAG, "no auth value");
        }

        return params;
    }

}

