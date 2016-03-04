package org.softeg.morphinebrowser;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import org.softeg.morphinebrowser.controls.AppWebView;


public class MainActivity extends ActionBarActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportProgressBarIndeterminateVisibility(false);
        createFragment(getIntent() != null ? getIntent().getData() : null);
    }

    private void createFragment(Uri uri) {
        Fragment newFragment = getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (newFragment == null) {
            newFragment = WebFragment.getInstance(uri);
        }

        transaction.replace(R.id.topic_fragment_container, newFragment, WebFragment.ID);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        AppWebView webView = ((WebFragment)getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container)).getWebView();
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
