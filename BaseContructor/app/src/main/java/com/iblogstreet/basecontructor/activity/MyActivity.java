package com.iblogstreet.basecontructor.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//    }
    private static final String LOG_TAG = "WebViewDemo";
    private WebView mWebView;
    private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        Toast.makeText(MyActivity.this,"HELLOW WORLD", Toast.LENGTH_LONG);

        mWebView = (WebView) findViewById(R.id.webView);


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);


        mWebView.setWebChromeClient(new MyWebChromeClient());

        // 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
        // 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中

        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "asdasd");
        mWebView.addJavascriptInterface(new myHellowWorld(),"my");

        mWebView.loadUrl("file:///android_asset/main.html");
    }

   final class myHellowWorld{
        myHellowWorld(){

        }
        public void show(String msg){
            Toast.makeText(MyActivity.this,msg, Toast.LENGTH_LONG).show();
           /* mHandler.post(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(MyActivity.this,msg, Toast.LENGTH_LONG).show();
                }
            });*/
        }
    }
    // 这是他定义由 addJavascriptInterface 提供的一个Object
 class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }
        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * 这不是呼吁界面线程。发表一个运行调用
         * loadUrl on the UI thread.
         * loadUrl在UI线程。
         */
        public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
            mHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MyActivity.this,"click me,I will die", Toast.LENGTH_LONG).show();
                    // 此处调用 HTML 中的javaScript 函数
                    mWebView.loadUrl("javascript:wave()");
                }
            });
        }
    }
// 线下的代码可以不看，调试用的
///////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * 从javascript中提供了一个叫“提示框” 。这是很有用的
     * debugging your javascript.
     *  调试你的javascript。
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(LOG_TAG, message);
            result.confirm();
            return true;
        }
    }

}