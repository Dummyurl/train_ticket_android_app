package ts.trainticket.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liuZOZO on 2018/4/22.
 */
public class HttpThread extends Thread {
    private String url;
    private ImageView imageView;
    private Handler handler;

    public HttpThread(String url, ImageView imageView, Handler handler) {
        this.url = url;
        this.imageView = imageView;
        this.handler = handler;
    }
    @Override
    public void run() {
        try {
            URL httpURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);//表示允许获取输入流
            InputStream in = connection.getInputStream();//获取输入流
            FileOutputStream outputStream = null;//图片下载保存到本地，需要输出流
            File downloadFile = null;
            //设置下载图片的文件名，我们以下载图片时的系统时间作为其文件名
            String fileName  = String.valueOf(System.currentTimeMillis());
            //判断sd卡是否挂载
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdPath = Environment.getExternalStorageDirectory();//获取sd卡的路径
                downloadFile = new File(sdPath, fileName);//在sd卡目录下创建一个文件，就是我们下载的图片文件
                outputStream = new FileOutputStream(downloadFile);
            }
            byte[] b = new byte[2*1024];//创建一个2K字节大小的缓冲区
            int len = 0;
            if (outputStream != null) {
                /*
                 * InputStream read(byte)方法：从输入流中读取一定数量的字节，并将其存储在缓冲区数组 b中。以整数形式返回
                 * 实际读取的字节数。如果 b 的长度为 0，则不读取任何字节并返回 0；否则，尝试读取至少一个字节。
                 * 如果因为流位于文件末尾而没有可用的字节，则返回值 -1；否则，至少读取一个字节并将其存储在 b 中
                 * */
                while ((len = in.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                }
            }
            final Bitmap bitmap = BitmapFactory.decodeFile(downloadFile.getAbsolutePath());//解析我们下载的文件
            handler.post(new Runnable() {

                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);//更新主线程中的ui

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
