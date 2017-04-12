package co.treasureisle.shobit.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

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
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 1. 25..
 */

public class MultipartRequest extends Request<JSONObject> {
    public static final String TAG = MultipartRequest.class.getSimpleName();

    private MultipartEntityBuilder builder = MultipartEntityBuilder.create();

    private Context context;
    private Response.Listener<JSONObject> listener;
    private Map<String, File> files;
    private Map<String, String> params;
    private String boundary = "---------------"+ UUID.randomUUID().toString();

    public MultipartRequest(Context context, int method, String url, Map<String, String> params, Map<String, File> files, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, BuildConfig.API_URL + url, errorListener);

        this.context = context;
        this.listener = listener;
        this.files = files;
        this.params = params;

        buildMultipartEntitiy();
    }

    private void buildMultipartEntitiy() {
        Charset chars = Charset.forName("UTF-8");
//        builder.setCharset(chars);
        //add textpart

        builder.setBoundary(boundary);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null) {
                    Log.d(TAG, entry.getKey() + " is null");
                }
                builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", chars));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //add file part
        try {
            if (files == null) return;
            for (Map.Entry<String, File> entry : files.entrySet()) {
                if (entry.getValue() == null) {
                    Log.d(TAG, entry.getKey() + " is null");
                }
                String filename = entry.getKey() + ".jpg";
                builder.addPart(entry.getKey(), new FileBody(entry.getValue(), ContentType.create("image/jpg", chars), filename));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBodyContentType() {

        return "multipart/form-data; charset=utf8; boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            builder.build().writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new JSONObject(new String(response.data, "utf-8")), getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences pref = Utils.getPrefs(context);
        String uuid = pref.getString(PrefTag.USER_ID, null);
        String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);
        if (uuid != null && accessToken != null) {
            params.put("Authorization", uuid + ":" + accessToken);
        }

        return params;
    }
}

