package com.iblogstreet.basecontructor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Administrator on 2016/3/21.
 * 短信接收器 *
 */
public class SmsListenerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        //获取短信黑名单
        String num = sp.getString("blackNum", "");
        //获取短信数据 短信数据获取是固定的写法 返回值是 Object[]，每个对象代表一个短信
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for(Object object:objects){
            //这里需要将 obj 强转为 byte[]数组
           // SmsMessage.createFromPdu((byte[])object,)
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            //获取短信发送人号码
            String address = message.getOriginatingAddress();
            //获取短信内容
            String body = message.getMessageBody();
           if (address.equals(num)){
               //如果是有序广播，就终止
               if(isOrderedBroadcast()) {
                   abortBroadcast();
                   Log.d("tag", "拦截了一条短信：" + "address=" + address + "/body=" + body);
               }
           }
        }

    }
 /*注册 BroadCastReceiver
    <receiver android:name="com.iblogstreet.smslistener.SmsListenerReceiver" >
    <intent-filter android:priority="1000">
    <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
    </intent-filter>
    </receiver>*/
}
