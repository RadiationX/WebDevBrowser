package org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;


/*
 * Created by slinkin on 06.10.2014.
 */
public class HtmlOut implements IHtmlOut {
    public static final String NAME = "HTMLOUT";
    private IHtmlOutListener mControl;

    public HtmlOut(IHtmlOutListener control) {

        mControl = control;
    }

    protected Context getContext() {
        return mControl.getContext();
    }

    protected FragmentActivity getActivity() {
        return mControl.getActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasRequestCode(int requestCode) {
        return false;
    }


}
