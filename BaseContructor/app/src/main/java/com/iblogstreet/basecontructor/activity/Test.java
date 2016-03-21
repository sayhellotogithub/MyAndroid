package com.iblogstreet.basecontructor.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/10.
 */
public class Test extends Activity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * 调用父类的方法获取 sp 对象
        * 第一个参数：sp 文件的名字，没有则创建
        * 第二个参数：文件权限
         */
        sp = getSharedPreferences("info", MODE_PRIVATE);
        // 从 sp 中获取数据
        String name = sp.getString("name", "");
        //设置数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", "www");
        editor.commit();

    }

    public void deleteData() {
        //删除数据
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

    }

    public void getExternal() {

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //SD卡没有挂载好
            return;
        }
        File file = Environment.getExternalStorageDirectory();
        //获取总字节数
        long totalSpace = file.getTotalSpace();
        //获取可用字节数
        long availableSpace = file.getFreeSpace();
        //格式化
        String totalString = Formatter.formatFileSize(this, totalSpace);
        String availableString = Formatter.formatFileSize(this, availableSpace);

    }

    public void getExternalStorage() {
        //StatFs 类是专门用于统计文件系统文件空间信息的工具类。
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        // 获取总 block 个数
        int blockCount = statFs.getBlockCount();
        // 获取可用的块个数
        int availableBlocks = statFs.getAvailableBlocks();
        // 获取 block 大小
        int blockSize = statFs.getBlockSize();
        // 计算总空间=块个数*块大小
        long totalSpace = blockCount * blockSize;
        // 计算可用空间==可用块个数*块大小
        long freeSpace = availableBlocks * blockSize;
    }

    /**
     * 此种方法有个问题
     * 比如如果短信内容包含“</>”
     * 符号，那么 xml 解析器就无法完成正确的解析。因此使用的前提是你确定数据内容没有特殊字符。
     */
    public void createXml1(View view) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>");
        sb.append("<smses>");
        for (int i = 0; i < 20; i++) {
            sb.append("<sms>");
            sb.append("<address>");
            sb.append(66666 + i);
            sb.append("</address>");
            sb.append("<body>");
            sb.append("短信内容" + i);
            sb.append("</body>");
            sb.append("<time>");
            sb.append(new Date().getTime());
            sb.append("</time>");
            sb.append("</sms>");
        }
        sb.append("</smses>");
        FileOutputStream fos = this.openFileOutput("info.xml", MODE_PRIVATE);
        fos.write(sb.toString().getBytes());
        fos.close();
    }

    public void createXml2(View v) throws IOException {
        FileOutputStream fos = openFileOutput("info2.xml", MODE_PRIVATE);
        //通过 Xml 类创建一个 Xml 序列化器
        XmlSerializer serializer = Xml.newSerializer();
        //给序列化器设置输出流和输出流编码
        serializer.setOutput(fos, "utf-8");
        /*
        *让序列化器开发写入 xml 的头信息，其本质是写入内容
        * "<?xml version='1.0' encoding='utf-8' standalone='yes' ?>"
        */
        serializer.startDocument("utf-8", true);
        //第一个参数是该标签的命名空间， 这里不需要
        serializer.startTag(null, "smses");
        for (int i = 0; i < 20; i++) {
            serializer.startTag(null, "sms");
            serializer.startTag(null, "address");
            serializer.text("" + (8888 + i));
            serializer.endTag(null, "address");
            serializer.startTag(null, "body");
            serializer.text("短信内容<>" + i);
            serializer.endTag(null, "body");
            serializer.startTag(null, "time");
            serializer.text(new Date().getTime() + "");
            serializer.endTag(null, "time");
            serializer.endTag(null, "sms");
        }
        serializer.endTag(null, "smses");
        serializer.endDocument();
        fos.close();
    }
    /**
     *通过XmlPullParse来解析xml
     * */
    public void readXml(View v) throws IOException, XmlPullParserException {
        ArrayList<Sms> smses = null;
        Sms sms = null;
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("info2.xml");
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream,"utf-8");
        //获取事件类型
        int event = parser.next();
        while(event!=XmlPullParser.END_DOCUMENT){
            String tagName = parser.getName();
            if (event==XmlPullParser.START_TAG) {
                if ("smses".equals(tagName)) {
                    smses = new ArrayList<>();
                }
                else if(("sms").equals(tagName)){
                    sms=new Sms();
                }
                else if(("address").equals(tagName)){
                    sms.setAddress(parser.nextText());
                }
                else if(("body").equals(tagName)){
                    sms.setBody(parser.nextText());
                }
                else if(("time").equals(tagName)){
                    sms.setTime(parser.nextText());
                }
            }
            else if(event==XmlPullParser.END_TAG){
                if("sms".equals(tagName)){
                    smses.add(sms);
                }
            }
            event=parser.next();
        }
        inputStream.close();
    }
}
class Sms{
    private String address;
    private String body;
    private String time;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}



