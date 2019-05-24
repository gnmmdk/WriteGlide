package com.kangjj.write.glidelib.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.UrlQuerySanitizer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.kangjj.write.glidelib.cache.DoubleLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

//生产者 ---  消费者
public class BitmapDispatcher extends Thread{

    private BlockingQueue<BitmapRequest> requestQueue;

    private Handler handler = new Handler(Looper.getMainLooper());

    public BitmapDispatcher(BlockingQueue<BitmapRequest> requestQueue){
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while(!isInterrupted()){
            BitmapRequest request = null;
            try {
                request = requestQueue.take();
                //显示loading，需要子线程切换到UI线程
                showLoadingImage(request);
                //网络加载图片
                Bitmap bitmap= findBitmap(request);
                //显示UI
                deliveryUIThread(request,bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void deliveryUIThread(final BitmapRequest request, final Bitmap bitmap) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = request.getImageView();
                if(imageView != null && bitmap !=null && imageView.getTag().equals(request.getUrlMD5())){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
        /**
         * 监听图片网络请求是否成功
         */
        if(request.getRequestListener() !=null){
            if(bitmap!=null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        request.getRequestListener().onResourceReady(bitmap);
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        request.getRequestListener().onException();
                    }
                });
            }
        }
    }

    private Bitmap findBitmap(BitmapRequest request) {
        Bitmap bitmap = DoubleLruCache.getInstance().get(request);
        if(bitmap!=null){
            return bitmap;
        }

        bitmap = downloadImage(request.getUrl());
        if(bitmap!=null){
            DoubleLruCache.getInstance().put(request,bitmap);
        }
        return bitmap;
    }

    private Bitmap downloadImage(String uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url  = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is =conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private void showLoadingImage(BitmapRequest request) {
        if(request.getLoadingResId()>0){
            final ImageView imageView = request.getImageView();
            final int resId = request.getLoadingResId();
            if(imageView!=null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(resId);
                    }
                });
            }
        }
    }
}
