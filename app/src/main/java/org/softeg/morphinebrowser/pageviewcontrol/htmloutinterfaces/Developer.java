package org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces;/*
 * Created by slinkin on 09.07.2014.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.softeg.morphinebrowser.App;
import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.common.FileUtils;

import java.io.File;
import java.io.FileWriter;

public class Developer implements IHtmlOut {
    public static final String NAME = "DEVOUT";
    private IHtmlOutListener control;

    public Developer(IHtmlOutListener control) {
        this.control = control;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasRequestCode(int requestCode) {
        return requestCode == FILECHOOSER_RESULTCODE;
    }

    private Context getContext() {
        return control.getContext();
    }

    private Activity getActivity() {
        return control.getActivity();
    }

    private final static int FILECHOOSER_RESULTCODE = App.getInstance().getUniqueIntValue();

    @JavascriptInterface
    public void showChooseCssDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");

                    // intent.setDataAndType(Uri.parse("file://" + lastSelectDirPath), "file/*");
                    control.getFragment().startActivityForResult(intent, FILECHOOSER_RESULTCODE);

                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Ни одно приложение не установлено для выбора файла!", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    AppLog.e(getActivity(), ex);
                }
            }
        });
    }

    @JavascriptInterface
    public void acceptStyle(final String attachFilePath) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String cssData = FileUtils.readFileText(attachFilePath)
                            .replace("\\", "\\\\")
                            .replace("'", "\\'").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
                    control.getWebView().evalJs("window['HtmlInParseLessContent']('" + cssData + "');");
                } catch (Exception ex) {
                    AppLog.e(getActivity(), ex);
                }
            }
        });
    }

   @JavascriptInterface
    public void saveHtml(final String html) {
        new saveHtml(getActivity(),html,"Topic");
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == FILECHOOSER_RESULTCODE) {
            String attachFilePath = FileUtils.getRealPathFromURI(getContext(), data.getData());
            String cssData = FileUtils.readFileText(attachFilePath)
                    .replace("\\", "\\\\")
                    .replace("'", "\\'").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");


            control.getWebView().evalJs("$('#dev-less-file-path')[0].value='" + attachFilePath + "';");
            control.getWebView().evalJs("window['HtmlInParseLessContent']('" + cssData + "');");

        }
    }


}

