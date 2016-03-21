package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class PhoneDialogActivity extends Activity {
    Button button_dialog;
    EditText et_phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phone);
        iniCompontent();
        iniListener();
    }
    private void iniCompontent(){
        button_dialog=(Button)findViewById(R.id.btn_dialog);
        et_phoneNumber=(EditText)findViewById(R.id.et_phoneNumber);

    }
    private void iniListener(){
        button_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String number=et_phoneNumber.getText().toString().trim();
                Toast.makeText(PhoneDialogActivity.this,number,Toast.LENGTH_LONG);
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                Uri uri= Uri.parse("tel:"+number);
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }
}
