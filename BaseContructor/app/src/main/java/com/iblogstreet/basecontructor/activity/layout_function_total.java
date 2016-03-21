package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;
import com.iblogstreet.basecontructor.activity.network.MulDownActivity;
import com.iblogstreet.basecontructor.activity.network.NetSourcecodeActivity;
import com.iblogstreet.basecontructor.activity.network.NetworkNewsActivity;
import com.iblogstreet.basecontructor.activity.sendmessage.SendMessageActivity;

/**
 * Created by Administrator on 2016/3/16.
 */
public class layout_function_total extends Activity implements View.OnClickListener {
    Button button_network_watcher,button_network_news,button_mul_down;
    Button button_send_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_function_total);
        iniView();
        iniValues();
        iniListener();
    }
    private void iniView() {
        button_network_watcher=(Button)findViewById(R.id.button_network_watcher);
        button_network_news=(Button)findViewById(R.id.button_network_news);
        button_mul_down=(Button)findViewById(R.id.button_mul_down);
        button_send_message=(Button)findViewById(R.id.button_send_message);
    }

    private void iniListener() {

        button_network_watcher.setOnClickListener(this);
        button_network_news.setOnClickListener(this);
        button_mul_down.setOnClickListener(this);
        button_send_message.setOnClickListener(this);
    }

    private void iniValues() {

    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.button_network_watcher:
                intent.setClass(layout_function_total.this, NetSourcecodeActivity.class);
                Toast.makeText(layout_function_total.this, "button_network_watcher", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_network_news:
                intent.setClass(layout_function_total.this, NetworkNewsActivity.class);
                Toast.makeText(layout_function_total.this, "NetworkNewsActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_mul_down:
                intent.setClass(layout_function_total.this, MulDownActivity.class);
                Toast.makeText(layout_function_total.this, "MulDownActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.button_send_message:
                intent.setClass(layout_function_total.this, SendMessageActivity.class);
                Toast.makeText(layout_function_total.this, "SendMessageActivity", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
