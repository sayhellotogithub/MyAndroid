package com.iblogstreet.basecontructor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/22.
 * 有序广播监听者
 */
public class OrderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg="OrderReceiver:"+intent.getAction()+"-"+intent.getStringExtra("data")+"-"+intent.getData();
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    /*注册无序广播
     <receiver android:name=".broadcast.DisorderReceiver">
            <intent-filter>
                <action android:name="com.iblogstreet.disorderbroadcast"></action>
            </intent-filter>
        </receiver>
    * */
}
