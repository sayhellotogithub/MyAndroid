package com.iblogstreet.basecontructor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/22.
 * 监听应用的安装和卸载
 */
public class PackageStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName=intent.getDataString();
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)){
            Log.d("PackageStateReceiver","安装了:"+packageName);
            Toast.makeText(context,"安装了:"+packageName,Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
            Log.d("PackageStateReceiver","卸载了"+packageName);
            Toast.makeText(context,"卸载了:"+packageName,Toast.LENGTH_SHORT).show();
        }
    }
    /*
    * 注册 BroadCastReceiver
    * <receiver android:name=".broadcast.PackageStateReceiver">
            <intent-filter>
                <data android:scheme="package"/>
                <action android:name="android.intent.action.PACKAGE_REMOVED"/>
                <action android:name="android.intent.action.PACKAGE_ADDED"/>
            </intent-filter>
        </receiver>
        监 听 应用 的 安 装 和 卸载 不 需 要 额外 声 明 权 限。 因 此 只 需 要在 AndroidManifest.xml 中 注 册
BroadCastReceiver 即可。
    * */
}
