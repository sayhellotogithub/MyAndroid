package com.iblogstreet.basecontructor.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/22.
 * 手机通话监听
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 <uses-permission android:name="android.permission.RECORD_AUDIO"/>
 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 需要用到的权限
 写外存的权限
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 录音的权限
 <uses-permission android:name="android.permission.RECORD_AUDIO"/>

 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 */
public class CallListenerService extends Service {
    private TelephonyManager tm;
    public MediaRecorder recorder;
    public boolean isCalling;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取电话管理器
        tm=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //开启监听
        tm.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        /*
        * state,电话的状态
        * incomingNumber 打进来的电话号码
        * */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //接通状态
            if(TelephonyManager.CALL_STATE_OFFHOOK==state){
                //记录当前状态
                isCalling=true;
                //MediaRecorder对象
                recorder=new MediaRecorder();
                //设置声音来源
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //设置输入格式
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //格式化日期文件
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss");
                String date=format.format(new Date());
                //设置输出到的文件
                recorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + date + ".3gp");
                //设置音频编码
                recorder.setAudioSource(MediaRecorder.AudioEncoder.DEFAULT);
                try{
                    //录音准备
                    recorder.prepare();
                    //录音开始
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //如果空闭状态，并且已经开启了录音
            } else if(TelephonyManager.CALL_STATE_IDLE==state&&isCalling){
                //通知录音器
                recorder.stop();
                //释放录音资源
                recorder.release();
                //设置状态为false
                isCalling=false;
            }
        }
    }
}
