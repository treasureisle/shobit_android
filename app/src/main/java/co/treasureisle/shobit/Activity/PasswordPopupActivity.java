package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 13..
 */

public class PasswordPopupActivity extends BaseActivity {
    public static final String TAG = PasswordPopupActivity.class.getSimpleName();

    private User user;
    private EditText textOldPassword;
    private EditText textNewPassword;
    private EditText textConfirm;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_password);

        user = getIntent().getParcelableExtra(IntentTag.USER);

        textOldPassword = (EditText)findViewById(R.id.text_old_password);
        textNewPassword = (EditText)findViewById(R.id.text_new_password);
        textConfirm = (EditText)findViewById(R.id.text_confirm);
        btnChangePassword = (Button)findViewById(R.id.btn_change_password);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

    }

    private void changePassword(){
        HashMap<String,String> params = new HashMap<>();

        if(isEmpty(textOldPassword) || isEmpty(textNewPassword) || isEmpty(textConfirm)) {
            Utils.showToast(this, "완료되지 않은 항목이 존재합니다");
            return;
        }

        if(!(textNewPassword.getText().equals(textConfirm.getText()))){
            Utils.showToast(this, "비밀번호가 일치하지 않습니다");
        }

        params.put("password", textOldPassword.getText().toString());
        params.put("new_password", textNewPassword.getText().toString());

        MultipartRequest req = new MultipartRequest(this, com.android.volley.Request.Method.POST, "/password/" + user.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.showToast(PasswordPopupActivity.this, "비밀번호가 변경되었습니다.");
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null) {
                        Log.e(TAG, error.networkResponse.toString());
                    }
                }
            }
        });


        VolleySingleTon.getInstance(this).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private boolean isEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }
}
