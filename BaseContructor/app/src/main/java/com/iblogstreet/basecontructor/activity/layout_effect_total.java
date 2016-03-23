package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;
import com.iblogstreet.basecontructor.Service.CallListenerService;
import com.iblogstreet.basecontructor.activity.network.MulDownActivity;
import com.iblogstreet.basecontructor.activity.network.NetSourcecodeActivity;
import com.iblogstreet.basecontructor.activity.network.NetworkNewsActivity;
import com.iblogstreet.basecontructor.activity.sendmessage.SendMessageActivity;

/**
 * Created by Administrator on 2016/3/16.
 */
public class layout_effect_total extends Activity implements View.OnClickListener {
    Button button_three_union_animation, button_network_news, button_mul_down;
    Button button_send_message;
    Button button_send_broadcast, button_send_broadcast_order;
    Button button_phone_watch,button_phone_watch_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_effect_total);
        iniView();
        iniValues();
        iniListener();
    }

    private void iniView() {
        button_three_union_animation = (Button) findViewById(R.id.button_three_union_animation);
        button_network_news = (Button) findViewById(R.id.button_network_news);
        button_mul_down = (Button) findViewById(R.id.button_mul_down);
        button_send_message = (Button) findViewById(R.id.button_send_message);
        button_send_broadcast = (Button) findViewById(R.id.button_send_broadcast);
        button_send_broadcast_order = (Button) findViewById(R.id.button_send_broadcast_order);
        button_phone_watch=(Button)findViewById(R.id.button_phone_watch);
        button_phone_watch_close=(Button)findViewById(R.id.button_phone_watch_close);
    }

    private void iniListener() {

        button_three_union_animation.setOnClickListener(this);
        button_network_news.setOnClickListener(this);
        button_mul_down.setOnClickListener(this);
        button_send_message.setOnClickListener(this);
        button_send_broadcast.setOnClickListener(this);
        button_send_broadcast_order.setOnClickListener(this);
        button_phone_watch.setOnClickListener(this);
        button_phone_watch_close.setOnClickListener(this);
    }

    private void iniValues() {

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_three_union_animation:
                intent.setClass(layout_effect_total.this, NetSourcecodeActivity.class);
                Toast.makeText(layout_effect_total.this, "button_network_watcher", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_network_news:
                intent.setClass(layout_effect_total.this, NetworkNewsActivity.class);
                Toast.makeText(layout_effect_total.this, "NetworkNewsActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_mul_down:
                intent.setClass(layout_effect_total.this, MulDownActivity.class);
                Toast.makeText(layout_effect_total.this, "MulDownActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_send_message:
                intent.setClass(layout_effect_total.this, SendMessageActivity.class);
                Toast.makeText(layout_effect_total.this, "SendMessageActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_send_broadcast://发送无序广播
                intent.putExtra("data", "无序广播");
                intent.setAction("com.iblogstreet.disorderbroadcast");
                sendBroadcast(intent);
                break;
            case R.id.button_send_broadcast_order://发送有序广播
                intent.putExtra("data", "有序广播");
                intent.setAction("com.iblogstreet.orderbroadcast");
                /*在 sendOrderedBroadcast 的时候我们指定 OrderReceiver 为
                最终广播接收者。那么就算是之前的 MyReceiver3 将该广播终止了，OrderReceiver 依然可以接收到该广播。
                这就是最终广播的特点*/
                // sendOrderedBroadcast(intent,null,new OrderReceiver(),null,0,"我得到幸运",null);
                sendOrderedBroadcast(intent, null, null, null, 0, "我得到幸运", null);
                break;
            case R.id.button_phone_watch://开启电话监听器服务
                //设置Context和服务的字节码
                intent.setClass(this, CallListenerService.class);
                //开启服务
                startService(intent);
                Toast.makeText(this,"开启电话监听器服务",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_phone_watch_close://关掉电话监听器服务

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
