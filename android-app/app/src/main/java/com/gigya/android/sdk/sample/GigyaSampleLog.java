package com.gigya.android.sdk.sample;

import android.util.Log;

public class GigyaSampleLog {

    private static final String TAG = "GigyaSample";

    public static void d(String classTAG, String message){
        Log.d(TAG, buildMessage(classTAG, message));
    }

    public static void i(String classTAG, String message){
        Log.i(TAG, buildMessage(classTAG, message));
    }

    public static void v(String classTAG, String message){
        Log.v(TAG, buildMessage(classTAG, message));
    }

    public static void e(String classTAG, String message){
        Log.e(TAG, buildMessage(classTAG, message));
    }

    public static void w(String classTAG, String message){
        Log.w(TAG, buildMessage(classTAG, message));
    }


    private static String buildMessage(String classTAG, String message){
        return "<<<" + classTAG + " *** " + message + ">>>";
    }
}
