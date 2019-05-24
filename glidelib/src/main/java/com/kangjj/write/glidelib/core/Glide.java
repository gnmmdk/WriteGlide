package com.kangjj.write.glidelib.core;

import android.content.Context;

import com.kangjj.write.glidelib.request.BitmapRequest;

public class Glide {
    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}
