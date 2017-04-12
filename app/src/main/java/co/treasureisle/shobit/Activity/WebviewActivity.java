package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.R;

/**
 * Created by pgseong on 2017. 4. 12..
 */

public class WebviewActivity extends BaseActivity {
    private static final String TAG = WebviewActivity.class.getSimpleName();

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra(IntentTag.URL);

        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }
}
