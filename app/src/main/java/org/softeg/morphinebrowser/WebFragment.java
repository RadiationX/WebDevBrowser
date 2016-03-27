package org.softeg.morphinebrowser;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.softeg.morphinebrowser.pageviewcontrol.PageFragment;

/*
 * Created by slartus on 25.10.2014.
 */
public class WebFragment extends PageFragment /*implements FileChooserDialog.FileCallback*/{
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
        } else if (id == R.id.write_url) {
            writeUrl();
            return true;
        } else if (id == R.id.cache_mode) {
            showCacheDialog();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }  else if (id == R.id.action_choose_file) {
            openHtml();
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
                .title(R.string.scr_w)
                .customView(v, false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .show();
        editText.selectAll();
        editText.requestFocus();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void showAboutDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.about_dialog, null);


        new MaterialDialog.Builder(getActivity())
                .title(R.string.about_title)
                .customView(v, true)
                .positiveText(R.string.ok)
                .show();
    }

    public static final int FILE_CHOOSER = 1; //инит
    public String lastSelectDirPath = Environment.getExternalStorageDirectory().getPath();//для kit-kat и новее
    public void openHtml() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//для lollipop и новее
            Toast.makeText(getActivity(), R.string.ex_storage_prem, Toast.LENGTH_LONG).show();
            return;
        }
        CharSequence[] items = new CharSequence[]{getString(R.string.fi_html)/*, "ЛЮБОЙ ДРУГОЙ ПУНКТ МЕНЮ"*/};
        new MaterialDialog.Builder(getContext())
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence items) {
                        switch (i) {
                            case 0://вызываю GET_CONTENT

                                try {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT);//просит систему вызвать get_content
                                    intent.setType("text/html");// uri
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) //выпоняется только при sdk >=18
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    intent.setDataAndType(Uri.parse("file://" + lastSelectDirPath), "text/html");
                                    startActivityForResult(intent, FILE_CHOOSER);

                                } catch (ActivityNotFoundException ex) {//если ни одно приложение не найдено
                                    Toast.makeText(getActivity(), R.string.ex_not_found_fm, Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {
                                    AppLog.e(getActivity(), ex);//записывается в лог проги как ошибка
                                
                                break
                        }
                    }
                })
                .show();
    }

}

