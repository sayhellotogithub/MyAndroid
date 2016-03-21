package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;

public class MainActivity extends Activity implements View.OnClickListener {

    Button button_function,button_layout,button_roll_game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniView();
        iniListener();
    }
    private void iniView(){
        button_function=(Button)findViewById(R.id.button_function);
        button_layout=(Button)findViewById(R.id.button_layout);
        button_roll_game=(Button)findViewById(R.id.button_roll_game);
    }


    private void iniListener(){
        button_function.setOnClickListener(this);
        button_layout.setOnClickListener(this);
        button_roll_game.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
     //  Button button=(Button)v;
        Intent intent=new Intent();
        switch(v.getId()){
            case R.id.button_function:
                Toast.makeText(MainActivity.this,"button_function",Toast.LENGTH_LONG).show();
                intent.setClass(MainActivity.this,layout_function_total.class);
                startActivity(intent);
                break;
            case R.id.button_layout:
                Toast.makeText(MainActivity.this,"button_layout",Toast.LENGTH_LONG).show();
                intent.setClass(MainActivity.this, layout_total.class);
                startActivity(intent);
                break;
            case R.id.button_roll_game:
                Toast.makeText(MainActivity.this,"button_roll_game",Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(MainActivity.this,ListViewActivity.class);
                startActivity(intent1);
            default:
                Toast.makeText(MainActivity.this,"default",Toast.LENGTH_LONG).show();
                break;

        }
    }
}
