package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.R;

/**
 * Created by pgseong on 2017. 4. 6..
 */

public class ShobitAlertActivity extends BaseActivity {
    final static String TAG = ShobitAlertActivity.class.getSimpleName();

    private String title;
    private String text;
    private String okTitle;
    private String cancleTitle;

    private Button btnCancle;
    private Button btnOk;
    private TextView textTitle;
    private TextView textText;

    public ShobitAlertActivity() {
        title = "Shobit";
        text = "알 수 없는 오류가 발생하였습니다";
        okTitle = "Ok";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        title  = getIntent().getStringExtra(IntentTag.ALERT_TITLE);
        text = getIntent().getStringExtra(IntentTag.ALERT_TEXT);
        okTitle = getIntent().getStringExtra(IntentTag.ALERT_OK_TITLE);
        cancleTitle = getIntent().getStringExtra(IntentTag.ALERT_CANCLE_TITLE);

        if (title == null) title = "Shobit";
        if (text == null) text = "알 수 없는 오류가 발생하였습니다";
        if (okTitle == null) okTitle = "Ok";
        if (cancleTitle == null) cancleTitle = "Cancle";

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_shobit);

        btnCancle = (Button)findViewById(R.id.btn_cancle);
        btnOk = (Button)findViewById(R.id.btn_ok);
        textText = (TextView)findViewById(R.id.text_text);
        textTitle = (TextView)findViewById(R.id.text_title);

        textText.setText(text);
        textTitle.setText(title);
        btnCancle.setText(cancleTitle);
        btnOk.setText(okTitle);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancle();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk();
            }
        });

    }

    public void onOk(){
        setResult(0);
        finish();
    }

    public void onCancle(){
        setResult(1);
        finish();
    }


}