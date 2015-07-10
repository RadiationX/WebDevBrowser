package org.softeg.morphinebrowser.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import org.softeg.morphinebrowser.AppLog;

/*
 * Created by slinkin on 02.10.2014.
 */
public class UrlExtensions {


    public static void showChoiceDialog(final Activity activity, final String url) {
        try {
            final int OPEN_IN_BROWSER = 0;
            final int SHARE_IT = 1;
            final int COPY = 2;
            final int OPEN_IMAGE = 3;
            final int DOWNLOAD_FILE = 4;
            CharSequence[] titles = null;
            int[] ids = null;

                titles = new CharSequence[]{"Открыть в браузере", "Поделиться", "Скопировать"};
                ids = new int[]{OPEN_IN_BROWSER, SHARE_IT, COPY};

            final int[] finalIds = ids;
            new AlertDialog.Builder(activity)
                    .setTitle(url)
                    .setSingleChoiceItems(titles, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            int id = finalIds[i];
                            try {
                                switch (id) {
                                    case OPEN_IN_BROWSER:
                                        Intent intent = IntentUtils.openLink(url);
                                        activity.startActivity(intent);
                                        break;
                                    case SHARE_IT:
                                        Intent intent2 = IntentUtils.shareText(url, url);
                                        activity.startActivity(intent2);
                                        break;
                                    case COPY:
                                        StringUtils.copyToClipboard(activity, url);
                                        Toast.makeText(activity, "Ссылка скопирована в буфер обмена", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (Throwable ex) {
                                AppLog.e(activity, ex);
                            }
                        }
                    })
                    .create().show();
        } catch (Throwable ex) {
            AppLog.e(activity, ex);
        }


    }


}
