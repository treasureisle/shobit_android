package co.treasureisle.shobit.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.ContentType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import co.treasureisle.shobit.BuildConfig;
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 1. 25..
 */

public class AddressRequest extends JsonObjectRequest {
    private static final String TAG = ShobitRequest.class.getSimpleName();

    Context context;

    public AddressRequest(Context context, int method, String url, Response.Listener<JSONObject> listener) {
        super(method, url, new JSONObject(), listener, makeDefaultErrorListener(context));

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
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, "utf-8");
            data = data.substring(1, data.length()-1);
            Log.e(TAG, data);
            return Response.success(new JSONObject(data), getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
