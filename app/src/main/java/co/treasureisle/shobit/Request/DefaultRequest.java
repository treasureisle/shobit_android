package co.treasureisle.shobit.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import co.treasureisle.shobit.BuildConfig;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 1. 25..
 */

public class DefaultRequest extends StringRequest {
    private static final String TAG = DefaultRequest.class.getSimpleName();

    Context context;

    public DefaultRequest(final Context context, int method, String url, Response.Listener<String> onResponseListener) {
        super(method, BuildConfig.API_URL + url, onResponseListener, makeDefaultErrorListener(context));
        this.context = context;
    }

    public DefaultRequest(final Context context, int method, String url, Response.Listener<String> onResponseListener, Response.ErrorListener errorListener) {
        super(method, BuildConfig.API_URL + url, onResponseListener, errorListener);
        this.context = context;
    }

    private static Response.ErrorListener makeDefaultErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse");
                if (error != null) {
                    error.printStackTrace();

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
        String uuid = pref.getString(PrefTag.USER_ID, null);
        String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);
        if (uuid != null && accessToken != null) {
            params.put("Authorization", uuid + ":" + accessToken);
        } else {
            Log.d(TAG, "no auth value");
        }

        return params;
    }
}
