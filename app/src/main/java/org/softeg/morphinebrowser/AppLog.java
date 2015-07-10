package org.softeg.morphinebrowser;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

/*
 * Created by slartus on 25.10.2014.
 */
public class AppLog {
    private static final String TAG = "AppLog";

    public static void e(Context context, Throwable ex) {
        Log.e(TAG,ex.toString());
        try{
            new AlertDialog.Builder(context)
                    .setTitle("Ошибка")
                    .setMessage(ex.toString())
                    .setPositiveButton("ОК",null)
                    .create().show();
        }catch (Throwable e){
            Log.e(TAG,ex.toString());
        }

    }

    public static void e(Throwable ex) {
        Log.e(TAG,ex.toString());
    }
}
