package org.softeg.morphinebrowser;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.softeg.morphinebrowser.pageviewcontrol.PageFragment;

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
        loadUrl(mUrl);
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
        } else if (id == R.id.action_close) {
            getActivity().finish();
            return true;
        }else if (id == R.id.write_url) {
            writeUrl();
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

        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        editText.setText((seekBar.getProgress()) + "");


        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) {
                    int i = seekBar.getProgress() - 1;

                    seekBar.setProgress(i);
                    RelativeLayout.LayoutParams rl_lp = new RelativeLayout.LayoutParams(i, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    getWebView().setLayoutParams(rl_lp);
                    rl_lp = null;
                    editText.setText((i) + "");
                }
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < seekBar.getMax()) {
                    int i = seekBar.getProgress() + 1;

                    seekBar.setProgress(i);
                    RelativeLayout.LayoutParams rl_lp = new RelativeLayout.LayoutParams(i, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    getWebView().setLayoutParams(rl_lp);
                    rl_lp = null;
                    editText.setText((i) + "");
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    seekBar.setProgress(Integer.valueOf(s.toString()));
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                try {
                    RelativeLayout.LayoutParams rl_lp = new RelativeLayout.LayoutParams(i, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    getWebView().setLayoutParams(rl_lp);
                    rl_lp = null;
                    //getWebView().setLayoutParams(new RelativeLayout.LayoutParams(i, RelativeLayout.LayoutParams.MATCH_PARENT));
                    editText.setText((i) + "");
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
        new MaterialDialog.Builder(getActivity())
                .title("Ширина экрана")
                .customView(v, false)
                .positiveText("Ок")
                .negativeText("Отмена")
                .show();
        editText.selectAll();
        editText.requestFocus();
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
