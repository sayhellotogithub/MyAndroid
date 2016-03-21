package com.iblogstreet.basecontructor.activity.sendmessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ContentActivity extends Activity implements AdapterView.OnItemClickListener {
    private String[] objects;
    ListView lv_send_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sendmessage_list);
        iniView();
        iniValues();
        iniListener();

    }
    private void iniView(){
        lv_send_message=(ListView)findViewById(R.id.ll_send_message);
    }
    private void iniListener(){
        lv_send_message.setOnItemClickListener(this);
    }
    private void iniValues(){
        objects = new String[50];
        for(int i=0;i<30;i++){
            objects[i]="我是短信5550"+i;
        }
        lv_send_message.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,objects));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("ContentActivity",objects[position]);

        Toast.makeText(this,objects[position],Toast.LENGTH_SHORT).show();
        String data=objects[position];
        Intent intent = new Intent();
        intent.putExtra("sms",data);
        setResult(RESULT_OK,intent);
        finish();
    }
}
