package org.softeg.morphinebrowser.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import org.softeg.morphinebrowser.App;
import org.softeg.morphinebrowser.AppLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Created by slinkin on 26.09.2014.
 */
public class FileUtils {

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (!contentUri.toString().startsWith("content://"))
            return contentUri.getPath();

        // can post image
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri,
                filePathColumn, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public static String readFileText(String filePath) {
        StringBuilder text = new StringBuilder();


        try {
            BufferedReader br = new BufferedReader(new FileReader(getRealPathFromURI(App.getInstance(),Uri.parse(filePath))));
            String line=br.readLine();

            while (line != null) {
                text.append(line);
                text.append('\n');
                line=br.readLine();
            }
        } catch (IOException e) {
            AppLog.e(App.getInstance(),e);
        }
        return text.toString();
    }


}
