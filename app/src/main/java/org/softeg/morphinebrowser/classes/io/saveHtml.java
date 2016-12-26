package org.softeg.morphinebrowser.classes.io;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.R;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by Snow Volf on 06.02.16.
 */
public class saveHtml {
    public saveHtml(final Context activity, final String html, final String defaultFileName){
        final String[] fileName = {defaultFileName};
        new MaterialDialog.Builder(activity)
                .title(R.string.export_html)
                .input(defaultFileName, defaultFileName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog materialDialog, CharSequence charSequence) {
                        fileName[0] = charSequence.toString();
                    }
                })
                .alwaysCallInputCallback()
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        try {
                            String state = Environment.getExternalStorageState();
                            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                                Toast.makeText(activity, R.string.ex_sd, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String rootPath = Environment.getExternalStorageDirectory().toString();
                            File folder = new File(rootPath + "/WDB"+ "/pages");
                            folder.mkdirs();
                            File file = new File(folder, fileName[0]+".html");
                            FileWriter out = new FileWriter(file);
                            out.write(html);
                            out.close();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                try {
                                    Uri uri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(uri, "text/html");
                                    activity.startActivity(intent);
                                } catch (Exception e){
                                    Log.e("WDB", "ошибка провайдера");
                                    e.printStackTrace();
                                    Toast.makeText(activity, "Work in progress...", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //устарело с API 24
                                Uri uri = Uri.fromFile(file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "text/html");
                                activity.startActivity(intent);
                            }
                        } catch (Exception e) {
                            AppLog.e(activity, e);
                        }
                    }
                })
                .show();

    }
}