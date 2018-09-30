package com.liyu.websocketjsbridge;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        SocketServer.getDefault().listen(this, 8124);

        webView.loadUrl("file:///android_asset/demo-websocket.html");
    }

    public void sendMsgToJs(View v) {
        SocketServer.getDefault().send("Hello~I am from Java");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketServer.getDefault().stop();
    }
}
