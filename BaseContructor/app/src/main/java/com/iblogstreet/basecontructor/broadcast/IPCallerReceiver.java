package com.iblogstreet.basecontructor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Administrator on 2016/3/21.
 * IP 拨号器
 */
public class IPCallerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String data=getResultData();
        SharedPreferences sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String prefix=sp.getString("ipprefix","17951");
        //加拨号前辍
        if(!TextUtils.isEmpty(prefix))
            data=prefix+data;
        //将修改后的数据设置出去
        setResultData(data);
    }

   /* 注册广播接收者
    <receiver android:name="com.iblogstreet.basecontructor.broadcast.IPCallerReceiver">
           <intent-filter >
             <action android:name="android.intent.action.NEW_OUTGOING_CALL"></action>
            </intent-filter>
            </receiver>
   * 添加权限
   * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
   */
}
