package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

public class layout_total extends Activity implements View.OnClickListener {
  Button button_relative1,button_frame1,button_line1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_total);
        iniView();
        iniListener();
    }
    private void iniView(){
        button_relative1=(Button)findViewById(R.id.button_relative1);
        button_frame1=(Button)findViewById(R.id.button_frame1);
        button_line1=(Button)findViewById(R.id.button_line1);
    }
    private void iniListener(){
        button_relative1.setOnClickListener(this);
        button_frame1.setOnClickListener(this);
        button_line1.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_relative1:
                Toast.makeText(layout_total.this, "button_relative1", Toast.LENGTH_SHORT).show();
                Intent  intent=new Intent(layout_total.this,layout_relative1.class);
                startActivity(intent);
                break;
            case R.id.button_frame1:
                Toast.makeText(layout_total.this,"button_frame1",Toast.LENGTH_SHORT).show();
                Intent  intent1=new Intent(layout_total.this,layout_frame1.class);
                startActivity(intent1);
                break;
            case R.id.button_line1:
                Toast.makeText(layout_total.this,"button_line1",Toast.LENGTH_SHORT).show();
                Intent  intent2=new Intent(layout_total.this,layout_line1.class);
                startActivity(intent2);
                break;
            default:
                Toast.makeText(layout_total.this,"default",Toast.LENGTH_SHORT).show();
                break;

        }
    }

}
