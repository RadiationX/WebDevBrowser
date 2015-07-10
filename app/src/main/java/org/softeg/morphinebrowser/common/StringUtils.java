package org.softeg.morphinebrowser.common;

import android.content.Context;

/*
 * Created by slinkin on 06.10.2014.
 */
public class StringUtils {
    public static void copyToClipboard(Context context, String link) {


        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("url", link);
        clipboard.setPrimaryClip(clip);

    }

}
