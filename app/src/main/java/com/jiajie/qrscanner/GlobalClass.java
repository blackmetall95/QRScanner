package com.jiajie.qrscanner;

import android.app.Application;
import android.content.Context;

/**
 * Created by jia.jie on 6/2/2017.
 */

public class GlobalClass extends Application {
    private static Context context;

    public void  onCreate(){
        super.onCreate();
        GlobalClass.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return GlobalClass.context;
    }
}
