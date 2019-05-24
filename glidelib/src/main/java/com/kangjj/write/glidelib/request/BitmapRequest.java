package com.kangjj.write.glidelib.request;

import android.content.Context;
import android.widget.ImageView;

import com.kangjj.write.glidelib.listener.RequestListener;
import com.kangjj.write.glidelib.util.MD5Utils;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    private String url;
    private SoftReference<ImageView> softReference;
    //这个主要和imageview做绑定，防止图片错位
    private String urlMD5;
    //正在等待的图片
    private int loadingResId;
    private Context context;
    private RequestListener requestListener;
    public BitmapRequest(Context context){
        this.context = context;
    }


    public BitmapRequest loading(int loadingResId){
        this.loadingResId = loadingResId;
        return this;
    }

    public  BitmapRequest listener(RequestListener requestListener){
        this.requestListener = requestListener;
        return this;
    }

    public BitmapRequest load(String url){
        this.url = url;
        this.urlMD5 = MD5Utils.toMD5(url);
        return this;
    }

    public void into(ImageView imageView){
        this.softReference = new SoftReference<>(imageView);
        imageView.setTag(urlMD5);
        RequestManager.getInstance().addBitmapRequest(this);

    }

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return softReference.get();
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public int getLoadingResId() {
        return loadingResId;
    }

    public Context getContext() {
        return context;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }
}
