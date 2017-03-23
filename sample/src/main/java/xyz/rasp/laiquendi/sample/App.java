package xyz.rasp.laiquendi.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by twiceYuan on 2017/3/21.
 */

public class App extends Application {

    static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        sApp = this;
    }

    public static App getApp() {
        return sApp;
    }
}
