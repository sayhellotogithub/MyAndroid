package com.iblogstreet.basecontructor.activity.sendmessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

import java.util.ArrayList;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * Created by Administrator on 2016/3/21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private static final int REQUESTNUM = 1;
    private static final int REQUESTSMS = 2;
    Button button_send_message, button_fast_send_message, button_choose_phone;
    EditText et_phoneNumber, et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sendmessage);
        iniView();
        iniListener();
    }

    private void iniView() {
        button_send_message = (Button) findViewById(R.id.button_send_message);
        button_fast_send_message = (Button) findViewById(R.id.button_fast_send_message);
        button_choose_phone = (Button) findViewById(R.id.button_choose_phone);

        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    private void iniListener() {
        button_send_message.setOnClickListener(this);
        button_fast_send_message.setOnClickListener(this);
        button_choose_phone.setOnClickListener(this);
    }

    private void iniValues() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_send_message:
                Toast.makeText(SendMessageActivity.this, "发送信息", Toast.LENGTH_SHORT);
                if(TextUtils.isEmpty(et_content.getText())|| TextUtils.isEmpty(et_phoneNumber.getText())){
                    Toast.makeText(SendMessageActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                SmsManager smsManager=SmsManager.getDefault();
                ArrayList<String> arrayList=smsManager.divideMessage(et_content.getText().toString());
                smsManager.sendMultipartTextMessage(et_phoneNumber.getText().toString(),null,arrayList,null,null);

                break;
            case R.id.button_fast_send_message:
                Toast.makeText(this, "快速发送信息", Toast.LENGTH_SHORT).show();
                intent.setClass(SendMessageActivity.this, ContentActivity.class);
                startActivityForResult(intent, REQUESTSMS);
                break;
            case R.id.button_choose_phone:
                Intent intent1=new Intent();
                intent1.setClass(SendMessageActivity.this, ContactsActivity.class);
                startActivityForResult(intent1, REQUESTNUM);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SendMessageActivity",requestCode+","+resultCode);
        Toast.makeText(this,requestCode+","+resultCode,Toast.LENGTH_SHORT).show();
        if (data == null)
            return;
        switch (requestCode) {
            case REQUESTNUM:
                et_phoneNumber.setText(data.getStringExtra("number"));
                break;
            case REQUESTSMS:
                et_content.setText(data.getStringExtra("sms"));
                break;
            default:
                break;
        }
    }
}
