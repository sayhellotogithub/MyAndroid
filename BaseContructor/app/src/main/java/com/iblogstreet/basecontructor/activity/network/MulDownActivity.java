package com.iblogstreet.basecontructor.activity.network;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iblogstreet.basecontructor.R;
import com.iblogstreet.basecontructor.utils.ConstantUtils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/18.
 * 多线程下载
 */
public class MulDownActivity extends Activity implements View.OnClickListener {
    Button button_mul_access;
    EditText et_mul_down_url, et_mul_down_num;
    LinearLayout ll_mul_down_pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mul_down);
        iniView();
        iniValues();
        iniListener();
    }

    private void iniView() {
        button_mul_access = (Button) findViewById(R.id.button_mul_access);
        et_mul_down_url = (EditText) findViewById(R.id.et_mul_down_url);
        et_mul_down_num = (EditText) findViewById(R.id.et_mul_down_num);
        ll_mul_down_pb = (LinearLayout) findViewById(R.id.ll_mul_down_pb);
    }

    private void iniValues() {
    }

    private void iniListener() {
        button_mul_access.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final  int num = Integer.valueOf(et_mul_down_num.getText().toString()).intValue();
        final String targetPath= "/mnt/sdcard/";;
       final  String path = et_mul_down_url.getText().toString()==""?ConstantUtils.DOWN_FILE_URL:et_mul_down_url.getText().toString();
        if (num < 2) {
            Toast.makeText(MulDownActivity.this, "线程数目不少于2", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < num; i++) {
            ProgressBar progressBar = (ProgressBar) View.inflate(MulDownActivity.this, R.layout.layout_mul_down_progressbar, null);
            ll_mul_down_pb.addView(progressBar);
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
               new  MultiDownloaderUtils(path,num,targetPath);
            }
        }).start();


     /*   HttpUtils httpUtils=new HttpUtils();
        httpUtils.download(path, targetPath, true, new RequestCallBack<File>() {
            @Override
            public void onStart() {
               // super.onStart();
                Log.d("tag", "onStart" + Thread.currentThread().getName());
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                //super.onLoading(total, current, isUploading);
                Log.d("tag", "onLoading" + Thread.currentThread().getName()+"Current:"+current+",Total:"+total);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(MulDownActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(MulDownActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    public class MultiDownloaderUtils {
        private String sourcePath;
        private int threadCount;
        private String targetPath;
        //未完成任务的线程
        private int threadRunning;

        public MultiDownloaderUtils(String sourcePath, int threadCount, String targetPath) {
            this.sourcePath = sourcePath;
            this.threadCount = threadCount;
            this.targetPath = targetPath;
        }
        public void download(){
            try {
                URL url=new URL(sourcePath);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(ConstantUtils.CONNECT_TIMEOUT);
                connection.setReadTimeout(ConstantUtils.READ_TIMEOUT);
                connection.connect();
                int responseCode=connection.getResponseCode();
                if(responseCode==200){
                    int totalSize=connection.getContentLength();
                    //计算每个线程下载的平均字节数
                    int avgSize=totalSize/threadCount;
                    for(int i=0;i<threadCount;i++){
                        final int startIndex=i*avgSize;
                        int endIndex=0;
                        if(i==threadCount-1){
                            endIndex=totalSize-1;
                        }
                        else {
                            endIndex=(i+1)*avgSize-1;
                        }
                        threadRunning++;
                        //开启子线程,实现下载
                        new MyDownloadThread(i,startIndex,endIndex,targetPath,sourcePath).start();
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        class MyDownloadThread extends  Thread{
            private int id;
            private int startIndex;
            private int endIndex;
            private String targetPath;
            private String sourcePath;
            public MyDownloadThread(int id,int startIndex,int endIndex,String targetPath,String sourcePath){
                this.id=id;
                this.startIndex=startIndex;
                this.endIndex=endIndex;
                this.sourcePath=sourcePath;
                this.targetPath=targetPath;
            }
            public void run(){
                try {
                    URL url=new URL(sourcePath);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    File file=new File("d://"+id+".tmp");
                    //该属性设置后，返回的状态码就不再是200，而是206
                    int currentIndex=-1;
                    if(file.exists()){
                        BufferedReader reader=new BufferedReader(new FileReader(file));
                        String readLine=reader.readLine();
                        currentIndex=Integer.valueOf(readLine);
                        reader.close();
                    }else{
                        currentIndex=startIndex;
                    }
                    //只有设置了该属性才能下载指定范围的文件
                    connection.setRequestProperty("Range","bytes="+currentIndex+"-"+endIndex);
                    connection.setConnectTimeout(ConstantUtils.CONNECT_TIMEOUT);
                    connection.setReadTimeout(ConstantUtils.READ_TIMEOUT);
                    connection.connect();
                    int responseCode=connection.getResponseCode();
                    if(responseCode==206){
                        InputStream inputStream=connection.getInputStream();
                        //支持随机读写的文件类
                        RandomAccessFile raf=new RandomAccessFile(targetPath,"rws");
                        raf.seek(currentIndex);
                        byte[] buffer=new byte[1024*16];
                        int len=-1;
                        int totalDownloaded=0;
                        ProgressBar pb=(ProgressBar)ll_mul_down_pb.getChildAt(id);
                        pb.setMax(endIndex-startIndex);
                        while((len=inputStream.read(buffer))!=-1){
                            raf.write(buffer,0,len);
                            totalDownloaded+=len;
                            //写进度
                            BufferedWriter writer=new BufferedWriter(new FileWriter(file));
                            writer.write(currentIndex+totalDownloaded+"");
                            writer.close();
                            pb.setProgress(currentIndex+totalDownloaded-startIndex);
                            System.out.println(id+"下载了："+totalDownloaded+","+(totalDownloaded+currentIndex-startIndex)*100/(endIndex-startIndex)+"%");
                        }
                        raf.close();
                        inputStream.close();
                        connection.disconnect();

                        threadRunning--;
                        if(threadRunning==0){
                            for(int i=0;i<threadCount;i++){
                                File tmpFile=new File("d://"+i+".tmp");
                                tmpFile.delete();
                            }
                        }

                    }
                    else{
                        System.out.println("返回的状态码为:"+responseCode+" 下载失败！");
                    }

                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
