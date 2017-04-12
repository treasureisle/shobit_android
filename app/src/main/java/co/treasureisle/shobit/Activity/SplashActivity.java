package co.treasureisle.shobit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import co.treasureisle.shobit.BuildConfig;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;


public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private boolean isCheckingInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Log.d(TAG, "API_URL is " + BuildConfig.API_URL);

        checkInternetAndDoLogin();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkInternetAndDoLogin() {
        if (!isCheckingInternet()) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isOnline()) {
                        doLogin();
                    } else {

                        RelativeLayout allWrapper = (RelativeLayout) findViewById(R.id.commentHeaderAllWrapper);
                        allWrapper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setCheckingInternet(true);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        setCheckingInternet(false);
                                        checkInternetAndDoLogin();
                                    }
                                }, 1000);
                            }
                        });
                    }
                }
            });

        }
    }

    private void doLogin() {
        final SharedPreferences pref = Utils.getPrefs(this);
        final String userId = pref.getString(PrefTag.USER_ID, null);
        final String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);

        if (userId == null || accessToken == null) {
            Log.d(TAG, "sharedpref is null");
            // 맨처음 화면 보여주기
            jumpToHomeActivity();
            finish();
        } else {
            Log.d(TAG, "trying log in");
            // 로그인하기
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", userId);
                obj.put("access_token", accessToken);

                ShobitRequest req = new ShobitRequest(this, Request.Method.POST, "/session", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "login success");

                        jumpToHomeActivity();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "login failed.");
                        if (error.getLocalizedMessage() != null) {
                            Log.d(TAG, error.getLocalizedMessage());
                        }

                        jumpToHomeActivity();
                    }
                });
                req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                VolleySingleTon.getInstance(this).addToRequestQueue(req);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isCheckingInternet() {
        return isCheckingInternet;
    }

    public void setCheckingInternet(boolean isCheckingInternet) {
        this.isCheckingInternet = isCheckingInternet;
    }

    public void jumpToHomeActivity(){
        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
        i.putExtra(IntentTag.FROM_PUSH, (getIntent() != null && getIntent().getBooleanExtra(IntentTag.FROM_PUSH, false)));
        startActivity(i);
        finish();
    }

}
