package com.kangjj.write.glidelib.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.kangjj.write.glidelib.request.BitmapRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryLruCache implements BitmapCache{
    private LruCache<String,Bitmap> lruCache;
    private HashMap<String,Integer> activityCache;

    private static volatile  MemoryLruCache instance;

    private static final byte[] lock = new byte[0];

    public static MemoryLruCache getInstance(){
        if(instance == null){
            synchronized (lock){
                if(instance == null){
                    instance = new MemoryLruCache();
                }
            }
        }
        return instance;
    }

    private MemoryLruCache(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        int cacheSize = (int) (maxMemory/8);
        lruCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = value.getRowBytes()*value.getHeight();
                Log.d("cache","sizeof="+size);
                return size;
            }
        };
        activityCache = new HashMap<>();
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if(bitmap!=null){
            lruCache.put(request.getUrlMD5(),bitmap);
            activityCache.put(request.getUrlMD5(),request.getContext().hashCode());
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return lruCache.get(request.getUrlMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        lruCache.remove(request.getUrlMD5());
    }

    @Override
    public void remove(int activityCode) {
        List<String> tempUrlMd5List = new ArrayList<>();
        for (String urlMd5 : activityCache.keySet()) {
            if(activityCache.get(urlMd5).intValue() == activityCode){
                tempUrlMd5List.add(urlMd5);
            }
        }
        for (String urlMd5 : tempUrlMd5List) {
            activityCache.remove(urlMd5);
            Bitmap bitmap = lruCache.get(urlMd5);
            if(bitmap!=null && ! bitmap.isRecycled()){
                bitmap.recycle();
            }
            lruCache.remove(urlMd5);
            bitmap = null;
        }
        if(!tempUrlMd5List.isEmpty()){
            System.gc();
        }
    }
}
