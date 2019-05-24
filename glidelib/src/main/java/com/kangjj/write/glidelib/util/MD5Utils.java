package com.kangjj.write.glidelib.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String TAG = MD5Utils.class.getSimpleName();

    private static MessageDigest digest;

    static{
        try{
            digest = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            Log.d(TAG,"md5 算法不支持！");
        }
    }

    public static String toMD5(String key){
        if(digest == null){
            return String.valueOf(key.hashCode());
        }
        digest.update(key.getBytes());
        return convert2HexString(digest.digest());
    }

    private static String convert2HexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if(hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
