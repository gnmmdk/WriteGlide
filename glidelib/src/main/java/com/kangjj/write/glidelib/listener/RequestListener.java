package com.kangjj.write.glidelib.listener;

import android.graphics.Bitmap;

public interface RequestListener {
    public boolean onException();
    public boolean onResourceReady(Bitmap resource);
}
