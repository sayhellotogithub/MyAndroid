package com.iblogstreet.basecontructor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/22.
 *  监听屏幕的点亮和关闭
 */
public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Toast.makeText(context,"Screen on",Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Toast.makeText(context,"Screen off",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 对于锁屏解屏和电量变化的监听只能通过动态注册
     *动态注册广播
     //1、创建一个广播接收者对象
     ScreenReceiver screenReceiver=new ScreenReceiver();
     //2、创建 IntentFilter 对象
     IntentFilter intentFilter=new IntentFilter();
     intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
     intentFilter.addAction(Intent.ACTION_SCREEN_ON);
     //注册广播
     registerReceiver(screenReceiver,intentFilter);

     需要在onDestroy()，取消广播的注册
     @Override
     protected void onDestroy() {
     super.onDestroy();
     unregisterReceiver(screenReceiver);
     }
     * */
}
