package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import co.treasureisle.shobit.Model.Session;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.SessionHelper;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class LoginPopupActivity extends BaseActivity {
    final static String TAG = LoginPopupActivity.class.getSimpleName();

    private EditText inputId;
    private EditText inputPassword;
    private Button btnLogin;
    private Button btnSignIn;
    private LinearLayout blurview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_login);

        inputId = (EditText)findViewById(R.id.input_id);
        inputPassword = (EditText)findViewById(R.id.input_password);
        blurview = (LinearLayout)findViewById(R.id.blurview);

        blurview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLogin = (Button)findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnSignIn = (Button)findViewById(R.id.btn_signin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void login() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", inputId.getText().toString());
            obj.put("password", inputPassword.getText().toString());

            final ShobitRequest req = new ShobitRequest(this, Request.Method.POST, "/session/email", obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Session session = new Session(response.getJSONObject("session"));

                        SessionHelper sessionHelper = new SessionHelper(LoginPopupActivity.this);
                        sessionHelper.saveToken(session);

                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            VolleySingleTon.getInstance(this).addToRequestQueue(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signIn(){
        Intent i = new Intent(this, SigninActivity.class);
        startActivity(i);
    }
}
