package ts.trainticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by liuZOZO on 2018/3/29.
 */
public class MoreActivity extends AppCompatActivity {
    private WebView webView;
    private String more12306Info = "https://kyfw.12306.cn/otn/gonggao/help.html";

    // head
    private Button head_back_btn = null;
    private TextView title_head_tv= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_more);
        // 初始化组件
        initView();
        initWebView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        head_back_btn  = (Button) findViewById(R.id.common_head_back_btn);
        title_head_tv = (TextView) findViewById(R.id.title_head_tv);
        title_head_tv.setText("常见问题");
        head_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转
                finish();
            }
        });
    }

    private void initWebView() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(more12306Info);
    }
}
