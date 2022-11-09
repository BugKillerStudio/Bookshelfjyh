package com.com.jnu.recycleview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);//布局解析器
        WebView webView=rootView.findViewById(R.id.webview_main);
        WebSettings webSettings=webView.getSettings();//保存配置项
        webSettings.setJavaScriptEnabled(true);       //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webView.loadUrl("https://news.sina.com.cn/");//https
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });       //重载WebClient
        return rootView;
    }
}