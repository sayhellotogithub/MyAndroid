package com.iblogstreet.basecontructor.activity.network;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;
import com.iblogstreet.basecontructor.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Administrator on 2016/3/16.
 */
public class NetSourcecodeActivity extends Activity {
    Button button_netsourcecode_confirm;
    EditText  et_netsourcecode_url;
    TextView tv_netsourcecode_result;
    // 创建一个 Handler 对象，覆写类体 方法体
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //判断 Message 的类型，根据 msg 的 what 属性去获取其类型
            switch (msg.what) {
                case RESULT_OK:
                    tv_netsourcecode_result.setText(msg.obj.toString());
                    break;
                case RESULT_CANCELED:
                    tv_netsourcecode_result.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_netsourcecode);
        iniView();
        iniValues();
        iniListener();
    }

    private void iniView() {
        button_netsourcecode_confirm = (Button) findViewById(R.id.button_netsourcecode_confirm);
        tv_netsourcecode_result = (TextView) findViewById(R.id.tv_netsourcecode_result);
        et_netsourcecode_url = (EditText) findViewById(R.id.et_netsourcecode_url);
    }

    private void iniListener() {
        button_netsourcecode_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String path = et_netsourcecode_url.getText().toString();
                if (path.equals("")) {
                    Toast.makeText(NetSourcecodeActivity.this, getText(R.string.tip_url), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NetSourcecodeActivity.this, path, Toast.LENGTH_SHORT).show();
                    //发送网络请求
                    new Thread() {
                        public void run() {
                            //1.创建一个 URL 对象，需要传入 url
                            try {
                                URL url = new URL(path);
                                 /* 2.使用 url 对象打开一个 HttpURLConnection，
                                 由于其返回的是 HttpURLConnection 的父类，
                                 * 这里可以强制类型转换
                                  * */
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                // 3. 配置 connection 连接参数
                                //设置联网超时时长，单位毫秒
                                //设置数据读取超时， 注意：不是指读取数据总耗时超时，而是能够读取到数据流等待时长
                                //设置请求方式，默认是 GET，但是为了增加代码易读性，建议显示指明为 GET
                                connection.setConnectTimeout(3000);
                                connection.setReadTimeout(5000);
                                connection.setRequestMethod("GET");
                                // 4.开始连接网络
                                connection.connect();
                                //5.以字节输入流的形式获取服务端发来的数据
                                InputStream is = connection.getInputStream();
                                //6.将字节流转化为字符串（使用了自定义的 StreamUtils 工具类）
                                String data = StreamUtils.inputStream2String(is);
                                // 7.将获取到的数据封装到 Message 对象，然后发送给 Handler
                                //创建一个 Message 对象
                                // 给 Message 对象的 what 属性设置一个 int 类型的值。
                                // 因为消息可能会有多个，因此为了区分这些不同的消息，需要给消息设置 what 属性。
                                //给 Message 对象的 obj 属性设置一个 Object 类型的
                                // 给主线程发送消息。
                                Message msg = new Message();
                                msg.what = RESULT_OK;
                                msg.obj = data;
                                handler.sendMessage(msg);

                            } catch (IOException e) {
                                e.printStackTrace();
                                Message msg = new Message();
                                msg.what = RESULT_OK;
                                msg.obj = e.getMessage();
                                handler.sendMessage(msg);
                               // handler.sendEmptyMessage(RESULT_CANCELED);
                            }
                        }
                    }.start();
                }
            }
        });
    }

    private void iniValues() {

    }


}
