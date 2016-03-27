package org.softeg.morphinebrowser.classes.io;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.softeg.morphinebrowser.App;
import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.R;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by Snow Volf on 06.02.16.
 */
public class saveHtml {
    public saveHtml(final Activity activity, final String html, final String defaultFileName){
        final String[] fileName = {defaultFileName};
        new MaterialDialog.Builder(activity)
                .title(R.string.export_html)
                .input(defaultFileName, defaultFileName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
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

                            File file = new File(App.getInstance().getExternalFilesDir(null), fileName[0]+".html");
                            FileWriter out = new FileWriter(file);
                            out.write(html);
                            out.close();
                            Uri uri = Uri.fromFile(file);

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "text/html");
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            AppLog.e(activity, e);
                        }
                    }
                })
                .show();

    }
}