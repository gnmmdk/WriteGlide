package com.kangjj.write.glide;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangjj.write.glidelib.core.Glide;
import com.kangjj.write.glidelib.listener.RequestListener;

public class MainActivity extends AppCompatActivity {
    private static final String[] IMAGE_URL = {
            "http://tvax3.sinaimg.cn/crop.0.0.996.996.1024/0064fRDgly8fzr91ctqlpj30ro0roq8h.jpg",
    "http://image20.it168.com/picshow/900x675/20111124/2011112416144308103.jpg",
    "http://b-ssl.duitang.com/uploads/item/201205/09/20120509151143_eLPMS.jpeg",
    "http://img4.duitang.com/uploads/item/201405/21/20140521075427_WKVhr.thumb.600_0.jpeg",
    "http://inews.gtimg.com/newsapp_bt/0/8271837903/1000",
    "http://img5.imgtn.bdimg.com/it/u=896229575,1965206160&fm=26&gp=0.jpg"};


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private LinearLayout scroollLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scroollLine = findViewById(R.id.scrooll_line);
        verifyStoragePermissions(this);
    }

    private void verifyStoragePermissions(Activity context) {
        try {
            int permission = ActivityCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void single(View view) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scroollLine.addView(imageView);
        Glide.with(this).loading(R.drawable.loading).load(IMAGE_URL[0])
                .listener(new RequestListener() {
                    @Override
                    public boolean onException() {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource) {
                        Toast.makeText(MainActivity.this, "自定义处理图片（比如设置圆角）"
                                , Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .into(imageView);
    }

    public void more(View view) {
        for (int i = 0; i < 100; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            scroollLine.addView(imageView);
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(i+"");
            scroollLine.addView(textView);
            //设置占位图片
            Glide.with(this)
                    .loading(R.drawable.loading).load(IMAGE_URL[i%IMAGE_URL.length]).loading(R.drawable.loading)
                    .into(imageView);
        }
    }
}
