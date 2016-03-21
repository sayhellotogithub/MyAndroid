package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.iblogstreet.basecontructor.R;

/**
 * Created by Administrator on 2016/3/9.
 */
public class layout_relative1 extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_relative1);
        iniView();
        iniListener();
    }
    private void iniView(){

    }
    private void iniListener(){

    }
    @Override
    public void onClick(View v) {

    }
}
