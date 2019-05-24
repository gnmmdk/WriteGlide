package com.kangjj.write.glidelib.request;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {
    private static RequestManager instance;

    private RequestManager (){
        start();
    }

    public static RequestManager getInstance(){
        if(instance == null){
            synchronized (RequestManager.class){
                if(instance == null){
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }
    //阻塞队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();

    public void addBitmapRequest(BitmapRequest request){
        if(!requestQueue.contains(request)){
            requestQueue.add(request);
        }else{
            Log.i("err","任务已存在，不用再添加");
        }
    }
    //转发器管理
    private BitmapDispatcher[] dispatchers;

    public void start(){
        stop();
        int threadCount = Runtime.getRuntime().availableProcessors();
        dispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < dispatchers.length; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher((requestQueue));
            bitmapDispatcher.start();
            dispatchers[i] = bitmapDispatcher;
        }
    }

    public void stop(){
        if(dispatchers !=null && dispatchers.length >0){
            for (BitmapDispatcher dispatcher : dispatchers) {
                if(!dispatcher.isInterrupted()){
                    dispatcher.interrupt();
                }
            }
        }
    }
}
