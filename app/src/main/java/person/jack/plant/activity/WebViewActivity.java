package person.jack.plant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import person.jack.plant.R;
import person.jack.plant.ui.UIHelper;

/**
 * webview公用界面
 */
public class WebViewActivity extends BaseFragmentActivity {
    WebView webView;
    Button back;
    TextView title;
    SharedPreferences sharedPreference= PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        title=(TextView)findViewById(R.id.text_title);
        back=(Button)findViewById(R.id.btn_web_back);
        webView=(WebView)findViewById(R.id.web_all_view);
        String name=sharedPreference.getString("title","");
        String url=sharedPreference.getString("url","");
        title.setText(name);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showHome(WebViewActivity.this);
            }
        });
    }
}
