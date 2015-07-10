package org.softeg.morphinebrowser.pageviewcontrol;

import android.content.Context;
import android.os.Bundle;

import org.softeg.morphinebrowser.controls.AppWebView;


/*
 * Created by slinkin on 02.10.2014.
 */
public interface IWebViewClientListener {
    void setProgressBarIndeterminateVisibility(boolean b);



    Context getContext();

    AppWebView getWebView();
}
