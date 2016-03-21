package com.iblogstreet.basecontructor.utils;
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
 * Created by Administrator on 2016/3/17.
 * 多线程下载原理
 *多线程下载就是将同一个网络上的原始文件根据线程个数分成均等份，然后每个单独的线程下载对应
 的一部分，然后再将下载好的文件按照原始文件的顺序“拼接”起来就构成了完整的文件了。这样就大大
 提高了文件的下载效率。
 多线程下载大致可分为以下几个步骤：
 一、获取服务器上的目标文件的大小
 显然这一步是需要先访问一下网络，只需要获取到目标文件的总大小即可。目的是为了计算每个线程
 应该分配的下载任务。
 二、计算每个线程下载的起始位置和结束位置
 我们可以把原始文件当成一个字节数组，每个线程只下载该“数组”的指定起始位置和指定结束位置
 之间的部分。在第一步中我们已经知道了“数组”的总长度。因此只要再知道总共开启的线程的个数就好
 计算每个线程要下载的范围了。
 每个线程需要下载的字节个数（blockSize）=总字节数（totalSize）/线程数（threadCount）。
 假设给线程按照 0,1,2,3...n 的方式依次进行编号，那么第 n 个线程下载文件的范围为：
 起始脚标 startIndex=n*blockSize。
 结束脚标 endIndex=(n-1)*blockSize-1。
 考虑到 totalSize/threadCount 不一定能整除，因此对已最后一个线程应该特殊处理，最后一个线程的起
 始脚标计算公式不变，但是结束脚标 endIndex=totalSize-1;即可。
 三、在本地创建一个跟原始文件同样大小的文件
 在本地可以通过 RandomAccessFile 创建一个跟目标文件同样大小的文件，该 api 支持文件任意位置的
 读写操作。这样就给多线程下载提供了方便，每个线程只需在指定起始和结束脚标范围内写数据即可。
 四、开启多个子线程开始下载
 五、记录下载进度
 为每一个单独的线程创建一个临时文件，用于记录该线程下载的进度。对于单独的一个线程，每下载
 一部分数据就在本地文件中记录下当前下载的字节数。这样子如果下载任务异常终止了，那么下次重新开
 始下载时就可以接着上次的进度下载。
 六、删除临时文件
 当多个线程都下载完成之后，最后一个下载完的线程将所有的临时文件删除。
 */
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
                    while((len=inputStream.read(buffer))!=-1){
                        raf.write(buffer,0,len);
                        totalDownloaded+=len;
                        //写进度
                        BufferedWriter writer=new BufferedWriter(new FileWriter(file));
                        writer.write(currentIndex+totalDownloaded+"");
                        writer.close();
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
