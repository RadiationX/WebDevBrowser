package org.softeg.morphinebrowser;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;


public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminateVisibility(false);
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


}
