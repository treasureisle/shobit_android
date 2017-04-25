package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.SessionHelper;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 4. 12..
 */

public class SettingActivity  extends BaseActivity {
    public static final String TAG = SettingActivity.class.getSimpleName();

    private User user;

    private FrameLayout wrapperDelivary;
    private Switch alramMessage;
    private Switch alramReply;
    private Switch alramFeed;
    private Switch loginKakao;
    private Switch loginFacebook;
    private Switch loginInstagram;
    private FrameLayout wrapperTermofuse;
    private FrameLayout wrapperPrivacy;
    private FrameLayout wrapperHelp;
    private FrameLayout wrapperLogout;
    private FrameLayout wrapperSignout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        user = getIntent().getParcelableExtra(IntentTag.USER);

        wrapperDelivary = (FrameLayout)findViewById(R.id.wrapper_setting_delivery);
        alramMessage = (Switch)findViewById(R.id.switch_message_alram);
        alramReply = (Switch)findViewById(R.id.switch_reply_alram);
        alramFeed = (Switch)findViewById(R.id.switch_feed_alram);
        loginKakao = (Switch)findViewById(R.id.switch_login_kakao);
        loginFacebook = (Switch)findViewById(R.id.switch_login_facebook);
        loginInstagram = (Switch)findViewById(R.id.switch_login_instagram);
        wrapperTermofuse = (FrameLayout)findViewById(R.id.wrapper_termofuse);
        wrapperPrivacy = (FrameLayout)findViewById(R.id.wrapper_privacy);
        wrapperHelp = (FrameLayout)findViewById(R.id.wrapper_help);
        wrapperLogout = (FrameLayout)findViewById(R.id.wrapper_logout);
        wrapperSignout = (FrameLayout)findViewById(R.id.wrapper_signout);

        wrapperDelivary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, AddressActivity.class);
                i.putExtra(IntentTag.USER, user);
                startActivity(i);
            }
        });

        wrapperLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutPopup();
            }
        });
    }

    private void showLogoutPopup() {

        Intent i = new Intent(this, ShobitAlertActivity.class);
        i.putExtra(IntentTag.ALERT_TITLE, "Shobit");
        i.putExtra(IntentTag.ALERT_TEXT, "정말 로그아웃 하시겠습니까");
        i.putExtra(IntentTag.ALERT_OK_TITLE, "로그아웃");
        i.putExtra(IntentTag.ALERT_CANCLE_TITLE, "취소");

        startActivityForResult(i, RequestCode.REQ_LOGOUT);
    }

    private void logout() {
        SessionHelper sessionHelper = new SessionHelper(this);
        sessionHelper.clearToken();
        Utils.showToast(this, "로그아웃하였습니다. 첫화면으로 돌아갑니다.");
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQ_LOGOUT:
                if (resultCode == RESULT_OK) {
                    logout();
                }
                break;
        }

    }


}