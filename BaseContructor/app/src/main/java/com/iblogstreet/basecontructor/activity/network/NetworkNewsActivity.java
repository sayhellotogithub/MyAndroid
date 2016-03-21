package com.iblogstreet.basecontructor.activity.network;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;
import com.iblogstreet.basecontructor.domain.BaiduNews;
import com.iblogstreet.basecontructor.utils.ConstantUtils;
import com.iblogstreet.basecontructor.utils.StreamUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/3/16.
 */
public class NetworkNewsActivity extends Activity {
    ListView lv_network_news;
    List<BaiduNews> baiduNewses = null;
    //定义一个缓存线性池
    private ExecutorService executorService = Executors.newCachedThreadPool();
    // 创建一个 Handler 对象，覆写类体 方法体
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //判断 Message 的类型，根据 msg 的 what 属性去获取其类型
            switch (msg.what) {
                case RESULT_OK:
                    lv_network_news.setAdapter(new MyAdapter());
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(NetworkNewsActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_network_news);
        iniView();
        iniValues();
        iniListener();
    }

    private void iniView() {
        lv_network_news = (ListView) findViewById(R.id.lv_network_news);
    }

    private void iniListener() {

    }

    private void iniValues() {
        baiduNewses = new ArrayList<>();
        //发送网络请求
        new Thread() {
            public void run() {
                           /* Json格式
                           {
                                "errNum": 0,
                                    "errMsg": "success",
                                    "retData": [
                                {
                                    "title": "搞笑图摘：说好的宁停三分、不抢一秒呢？",
                                        "url": "http://toutiao.com/group/6261098319645982978/",
                                        "abstract": "1、三哥的技术不是你想学就能学的2、不做不会死。。。3、美女，不能再下蹲了，小心裙子4、说好的宁停三分、不抢一秒呢？5、小时候有一辆的都是土豪级别了6、美女真的就是擦个车！7、又遇到一个二逼！8、战斗民族不小心炫了个技9、这台车是怎么停进去的？",
                                        "image_url": "http://p3.pstatp.com/list/14f0024b4ab3433b420"
                                }

                                ]
                            }*/
                //1.创建一个 URL 对象，需要传入 url
                try {
                    URL url = new URL(ConstantUtils.BAIDU_NEWURL);
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
                    //apikey
                    connection.setRequestProperty("apikey", ConstantUtils.BAIDU_API_KEY);
                    // 4.开始连接网络
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        //5.以字节输入流的形式获取服务端发来的数据
                        InputStream is = connection.getInputStream();
                        //6.将字节流转化为字符串（使用了自定义的 StreamUtils 工具类）
                        String data = StreamUtils.inputStream2String(is);
                        Log.d("NetWorkNewsActivity", "data:" + data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String errNum = jsonObject.getString("errNum");
                            String errMsg = jsonObject.getString("errMsg");
                            boolean f1 = errMsg == "success";
                            boolean f2 = errMsg.equals("success");
                            if (errMsg.equals("success")) {
                                JSONArray array = jsonObject.getJSONArray("retData");
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject arrayItem = array.getJSONObject(i);
                                    //title url  abstract image_url
                                    String resultTitle = arrayItem.getString("title");
                                    String resultUrl = arrayItem.getString("url");
                                    String resultAbstract = arrayItem.getString("abstract");
                                    String resultImageUrl = arrayItem.getString("image_url");
                                    baiduNewses.add(new BaiduNews(resultTitle, resultUrl, resultAbstract, resultImageUrl));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    } else {
                        Message msg = new Message();
                        msg.what = RESULT_CANCELED;
                        msg.obj = "请求失败";
                        handler.sendMessage(msg);
                    }

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

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return baiduNewses.size();
        }

        @Override
        public Object getItem(int position) {
            return baiduNewses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(NetworkNewsActivity.this, R.layout.layout_network_news_item, null);
            } else {
                view = convertView;
            }
            // Holder holder=new Holder();
            com.loopj.android.image.SmartImageView iv_news_item_src = (com.loopj.android.image.SmartImageView) view.findViewById(R.id.iv_news_item_src);
            TextView tv_news_item_title = (TextView) view.findViewById(R.id.tv_news_item_title);
            TextView tv_news_item_abstract = (TextView) view.findViewById(R.id.tv_news_item_abstract);

            //赋值
           /* String image_url=baiduNewses.get(position).getImageUrl();
            loadImage(iv_news_item_src,image_url);*/
            iv_news_item_src.setImageUrl(baiduNewses.get(position).getImageUrl());
            tv_news_item_abstract.setText(baiduNewses.get(position).getBrief());
            tv_news_item_title.setText(baiduNewses.get(position).getTitle());

            return view;
        }
    }

    public class Holder {
        ImageView iv_news_item_src;
        TextView tv_news_item_title;
        TextView tv_news_item_abstract;
    }

    public void loadImage(final ImageView imageView, final String imageUrl) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(ConstantUtils.CONNECT_TIMEOUT);
                    connection.setReadTimeout(ConstantUtils.READ_TIMEOUT);
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    // 使用 BitmapFactory 将字节输入流转换为 Bitmap 对象
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //给 Handler 发送消息 让主线程执行 run 方法 在 run 方法中将 Bitmap 设置给 ImageView 对象
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * HttpURLPOST请求数据
     */
    public void HttpURLConnectionPostData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(ConstantUtils.BAIDU_NEWURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(ConstantUtils.CONNECT_TIMEOUT);
                    connection.setReadTimeout(ConstantUtils.READ_TIMEOUT);
                    /* 设置该参数,才能以流的形式提交数据
                    需要将要提交的数据转换为字节输出流*/
                    connection.setDoOutput(true);
                    ////将提交的参数进行 URL 编码
                    String param = "username=" + URLEncoder.encode("test") + "&password=" + "teset";
                    //设置请求属性，相当于封装 http 的请求头参数
                    //设置请求体的的长度
                    connection.setRequestProperty("Content-Length", param.length() + "");
                    //设置请求体的类型
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.connect();
                    OutputStream os = connection.getOutputStream();
                    //通过输出流将要提交的数据提交出去
                    os.write(param.getBytes());
                    os.close();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = connection.getInputStream();
                        String data = StreamUtils.inputStream2String(is);
                        handler.obtainMessage(RESULT_OK, data).sendToTarget();
                    } else {
                        handler.obtainMessage(RESULT_CANCELED).sendToTarget();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    handler.obtainMessage(RESULT_CANCELED, e.getMessage()).sendToTarget();
                }

            }
        }).start();
    }

    /*
    * HttPClient在android api23中已经丢失,原类在于apace的效果不高
    * */
    public void HttpClientGetData() {
      /*  HttpClient was deprecated in API Level 22 and removed in API Level 23.
        You can still use it in API Level 23 and onwards if you must,
        however it is best to move to supported methods to handle HTTP.
                So, if you're compiling with 23, add this in your build.gradle:
                android {
    useLibrary 'org.apache.http.legacy'
}*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://www.iblogstreet.com?UserName=test&Password=34233";
                HttpClient client = new DefaultHttpClient();
                // HttpGet request = new HttpGet(path);
                //HttpResponse response = client.execute(request);
                //StatusLine statusLine = response.getStatusLine();
                //int statusCode = statusLine.getStatusCode();
                //if (200 == statusCode) {
                //HttpEntity entity = response.getEntity();
                // InputStream inputStream = entity.getContent();
                // String data = StreamUtils.inputStream2String(inputStream);
                //handler.obtainMessage(RESULT_OK, data).sendToTarget();
                //}
            }
        }) {
        }.start();
    }

    public void HttpClientPostData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
              /*  HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(path);
                List<BasicNameValuePair> parameters =new ArrayList<BasicNameValuePair>();
                parameters.add(new BasicNameValuePair("username", username));
                parameters.add(new BasicNameValuePair("password", password));
                HttpEntity requestEntity =new UrlEncodedFormEntity(parameters, "utf-8");
                request.setEntity(requestEntity);
                HttpResponse response = client.execute(request);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (200 == statusCode) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    String data = StreamUtils.inputStream2String(inputStream);
                    handler.obtainMessage(RESULT_OK, data).sendToTarget();
                }*/
            }
        }).start();
    }

    /**
     * 使用 AsyncHttpClient 的 get 方式提交数据
     * https://github.com/loopj/android-async-http
     */
    public void AsyncHttpClientGetData() {
        String path = "http://10.0.2.2:8080/userlogin/servlet/LoginServlet?username=efef";
        // 创建一个 AsyncHttpClient
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        //执行Get方式
        asyncHttpClient.get(path, new DataAsyncHttpResponseHandler() {
            /*
            * 请求网络是在子线程中进行的，当请求成功后回调 onSuccess 方法，
            * 该方法是在主线程中被调用了，其内部是通过 Handler 实现的
            * 当请求成功后回调该方法
            * 参数 1：返回的状态码
            * 参数 2：响应头
            * 参数 3：返回的数据
            */
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(NetworkNewsActivity.this, new String(responseBody),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(NetworkNewsActivity.this, new String(responseBody),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    /*
     * 使用 AsyncHttpClient 的 post 方式提交数据
     * https://github.com/loopj/android-async-http
    * */
    public void AsyncHttpClientPostData(){
        String path = "http://10.0.2.2:8080/userlogin/servlet/LoginServlet?username=efef";
       // 创建一个 AsyncHttpClient 对象
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        //封装请求参数
        RequestParams requestParams=new RequestParams();
        requestParams.put("UserName","test");
        requestParams.put("Password", "test");
       // 执行 post 方法
        asyncHttpClient.post(path, requestParams, new DataAsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(NetworkNewsActivity.this, new String(responseBody),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(NetworkNewsActivity.this, new String(responseBody),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}




