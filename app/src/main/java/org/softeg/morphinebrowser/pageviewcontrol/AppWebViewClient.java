package org.softeg.morphinebrowser.pageviewcontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.common.IntentUtils;


/*
 * Created by slinkin on 02.10.2014.
 */
public class AppWebViewClient extends WebViewClient {
    private IWebViewClientListener listener;

    public AppWebViewClient(IWebViewClientListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        listener.setSupportProgressBarIndeterminateVisibility(true);
        //ThemeActivity.this.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        listener.setSupportProgressBarIndeterminateVisibility(false);
    }
/*
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final String url) {

        try {
            Intent intent = IntentUtils.openLink(url);
            listener.getContext().startActivity(intent);
        } catch (Throwable ex) {
            AppLog.e(ex);
        }
        return true;
    }
    */

}
