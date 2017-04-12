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
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;

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
    }
}