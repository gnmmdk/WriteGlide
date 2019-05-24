package com.kangjj.write.glide;

import android.app.Application;

import com.kangjj.write.glidelib.cache.DoubleLruCache;

public class GlideApplication extends Application {
    private static GlideApplication instance;

    public static GlideApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DoubleLruCache.getInstance(this);

    }
}
