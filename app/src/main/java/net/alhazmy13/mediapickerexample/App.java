package net.alhazmy13.mediapickerexample;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Alhazmy13 on 2/11/16.
 * MediaPicker
 */
public class App extends Application{



    private RefWatcher refWatcher;

        @Override public void onCreate() {
            super.onCreate();
            LeakCanary.install(this);
        }
    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }
}
