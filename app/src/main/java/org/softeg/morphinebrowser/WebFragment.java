package org.softeg.morphinebrowser;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.softeg.morphinebrowser.pageviewcontrol.PageFragment;

import java.net.URL;

/*
 * Created by slartus on 25.10.2014.
 */
public class WebFragment extends PageFragment {
    private static final String URL_KEY = "WebFragment.URL";

    public static Fragment getInstance(Uri url) {
        Bundle args = new Bundle();
        args.putString(URL_KEY, url == null ? "" : url.toString());
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mUrl;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().containsKey(URL_KEY))
            mUrl = getArguments().getString(URL_KEY);
        else if (savedInstanceState != null && savedInstanceState.containsKey(URL_KEY))
            mUrl = savedInstanceState.getString(URL_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrlEdit.setText(mUrl);
        loadUrl();
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            loadUrl();
            return true;
        } else if (id == R.id.action_save) {
            saveHtml();
            return true;
        } else if (id == R.id.action_fontsize) {
            showFontSizeDialog();
            return true;
        } else if (id == R.id.action_width) {
            showWidthDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showWidthDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.font_size_dialog, null);

        assert v != null;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        int webViewWidth = getWebView().getMeasuredWidth();
        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.value_seekbar);

        seekBar.setMax(screenWidth);
        seekBar.setProgress(webViewWidth);

        final TextView textView = (TextView) v.findViewById(R.id.value_textview);
        textView.setText((seekBar.getProgress()) + "px");

        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) {
                    int i=seekBar.getProgress() - 1;

                    seekBar.setProgress(i);
                    getWebView().setLayoutParams(new LinearLayout.LayoutParams(i, LinearLayout.LayoutParams.MATCH_PARENT));
                    textView.setText((i) + "px");
                }
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=seekBar.getProgress() + 1;

                seekBar.setProgress(i);
                getWebView().setLayoutParams(new LinearLayout.LayoutParams(i, LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setText((i) + "px");
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    getWebView().setLayoutParams(new LinearLayout.LayoutParams(i, LinearLayout.LayoutParams.MATCH_PARENT));
                    textView.setText((i) + "px");
                } catch (Throwable ex) {
                    AppLog.e(getActivity(), ex);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new AlertDialog.Builder(getActivity())
                .setTitle("Ширина экрана")
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }
}
