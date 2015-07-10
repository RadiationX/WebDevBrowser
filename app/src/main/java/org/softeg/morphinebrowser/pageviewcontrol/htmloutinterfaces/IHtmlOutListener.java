package org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.softeg.morphinebrowser.controls.AppWebView;

/*
 * Created by slinkin on 02.10.2014.
 */
public interface IHtmlOutListener{
    Context getContext();

    FragmentActivity getActivity();

    Fragment getFragment();

    AppWebView getWebView();


}
