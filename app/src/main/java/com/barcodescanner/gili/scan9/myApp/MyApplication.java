package com.barcodescanner.gili.scan9.myApp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gili on 6/24/2015.
 */
public class MyApplication extends Application{

    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        PrintHashKey();
        sInstance=this;
    }

    public void PrintHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.barcodescanner.gili.scan9",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext(){
        return  sInstance.getApplicationContext();
    }
}
