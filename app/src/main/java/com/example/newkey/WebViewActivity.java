package com.example.newkey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class WebViewActivity extends AppCompatActivity {

    ImageView web_view_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // WebView 설정
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Intent로 전달받은 URL 로드
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
        }

        web_view_back = findViewById(R.id.web_view_back);
        web_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebView에서 뒤로 갈 수 있으면 뒤로가기, 없으면 Activity 종료
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        // WebView에서 뒤로 갈 수 있으면 뒤로가기, 없으면 Activity 종료
        if (webView.canGoBack()) {
            webView.goBack(); // WebView 내 뒤로가기
        } else {
            super.onBackPressed(); // Activity 종료
        }
    }
}