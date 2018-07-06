package person.jack.plant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import person.jack.plant.R;
import person.jack.plant.ui.UIHelper;

/**
 * webview公用界面 chenle
 */
public class WebViewActivity extends BaseFragmentActivity {
    @Bind(R.id.btnBack)
    Button btnBack;

    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    @Bind(R.id.web_all_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("navigate");
        String title= bundle.getString("title");
        String url= bundle.getString("url");

        textHeadTitle.setText(title);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
