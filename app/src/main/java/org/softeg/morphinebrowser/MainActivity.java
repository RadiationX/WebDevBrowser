package org.softeg.morphinebrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.softeg.morphinebrowser.controls.AppWebView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e("TEST", "onCreate");
        setContentView(R.layout.activity_main);
        //иначе будет падать на kit-kat и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            Log.w("WDB","векторные иконки загружены");
        // Инициализирую Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.w("WDB","тулбар инициализирован");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        clearTitle(null);
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, toolbar_title.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        String url = null;
        if (getIntent() != null) {
            url = getIntent().getDataString();
            Log.e("Main Activity", "onCreate: " + url);
        }

        Log.e("Main Activity", "onCreate: " + url);

        createFragment(url);
    }

    private void createFragment(String uri) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topic_fragment_container, WebFragment.getInstance(uri), WebFragment.ID)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
    public static long press_time;
    @Override
    public void onBackPressed() {
        AppWebView webView = ((WebFragment)getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container)).getWebView();
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        }else {
                if (press_time + 2000 > System.currentTimeMillis()){
                    super.onBackPressed();
                } else {
                    Toast.makeText(this, R.string.press_once_more, Toast.LENGTH_LONG).show();
                    press_time = System.currentTimeMillis();
                }
            }
        }

    public void showFragmentHistory(String url) {
        historyFragment = HistoryFragment.newInstance(url);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topic_fragment_container, historyFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void hideFragmentHistory(String url) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(historyFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        createFragment(url);
    }

    /**
     * Понять и простить
     */

    public void showProgress(boolean show) {
        if (show) {
            toolbar_title.setText(R.string.loading);
            Log.i("WDB","прогресс показан");
        }
        else
            clearTitle(null);
    }

    public void changeTitle(String text) {
        toolbar_title.setText(text);
    }

    public void clearTitle(String title) {
        if (title == null)
            toolbar_title.setText(R.string.app_name);
        else
            toolbar_title.setText(title);
    }

}