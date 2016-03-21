package com.iblogstreet.basecontructor.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/16.
 */
public class StreamUtils {

    /**
     * 采用ByteArrayOutputStream来读取字符串，防止中文字符问题
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int result=0;
        byte[] array=new byte[1024*8];
        while((result=inputStream.read(array))!=-1){
            byteArrayOutputStream.write(array,0,result);
        }
        inputStream.close();
        return byteArrayOutputStream.toString();
    }
}
