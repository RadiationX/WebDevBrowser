package org.softeg.morphinebrowser;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.softeg.morphinebrowser.common.FileUtils;
import org.softeg.morphinebrowser.pageviewcontrol.PageFragment;

/*
 * Created by slartus on 25.10.2014.
 */
public class WebFragment extends PageFragment /*implements FileChooserDialog.FileCallback*/{
    private static final String URL_KEY = "WebFragment.URL";
    private int progressValueWidtPage;

    public static Fragment getInstance(String url) {
        Bundle args = new Bundle();
        args.putString(URL_KEY, url == null ? "" : url);
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
            showDialogUrlsList();
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
        } else if (id == R.id.action_outline) {
            showElementsOutline();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showWidthDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.font_size_dialog, null);
        changeText(getString(R.string.scr_w));
        if (AppPreferences.isFirstStart()) {
            showWarningDialog();
        }

        assert v != null;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        int webViewWidth = getWebView().getMeasuredWidth();
        log("Web Fragment", "get Pref Page Width Size Result: " + webViewWidth);
        final DiscreteSeekBar sb = (DiscreteSeekBar) v.findViewById(R.id.value_seek_bar);

        sb.setMax(screenWidth);
        sb.setProgress(webViewWidth);

        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        editText.setText((sb.getProgress()) + "");


        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.getProgress() > 0) {
                    int i = sb.getProgress() - 1;

                    sb.setProgress(i);
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
                if (sb.getProgress() < sb.getMax()) {
                    int i = sb.getProgress() + 1;

                    sb.setProgress(i);
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
                    sb.setProgress(Integer.valueOf(s.toString()));
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sb.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                try {
                    RelativeLayout.LayoutParams rl_lp = new RelativeLayout.LayoutParams(value, RelativeLayout.LayoutParams.MATCH_PARENT);
                    rl_lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    getWebView().setLayoutParams(rl_lp);
                    progressValueWidtPage = value;
                    rl_lp = null;
                    editText.setText((value) + "");
                } catch (Throwable ex) {
                    AppLog.e(getActivity(), ex);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(v);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                clearText();
                AppPreferences.setPageWidthSize(progressValueWidtPage);
                log("Web Fragment", "Save Page Width Size: " + progressValueWidtPage);
            }
        });
        editText.selectAll();
        editText.requestFocus();
    }

    private void showAboutDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.about_dialog, null);

        new MaterialDialog.Builder(getActivity())
                .title(R.string.about_title)
                .iconRes(R.drawable.ic_info_black_24dp)
                .customView(v, true)
                .positiveText(R.string.ok)
                .show();
    }

    public static final int FILE_CHOOSER = 1; //инит
    public String lastSelectDirPath = Environment.getExternalStorageDirectory().getPath();//для kit-kat и новее
    public void openHtml() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE).whenPermissionsGranted(new PermissionsGrantedListener() {
                @Override
                public void onPermissionsGranted(String[] permissions) throws SecurityException {
                    openHtmlHelper();
                }
            }).whenPermissionsRefused(new PermissionsRefusedListener() {
                @Override
                public void onPermissionsRefused(String[] permissions) {
                    Toast.makeText(getActivity(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
                }
            }).execute(getActivity());
        } else {
            openHtmlHelper();
        }
    }
    public void openHtmlHelper() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//для lollipop и новее
            Toast.makeText(getActivity(), R.string.ex_storage_prem, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT/*, FileProvider.getUriForFile(this, AUTHORITY, f)*/);//просит систему вызвать get_content
//            intent.setType("text/html");// uri
            intent.setType("*/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) //выпоняется только при sdk >=18
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setDataAndType(Uri.parse/*loadUrl*/("content://" + lastSelectDirPath), "text/html");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, FILE_CHOOSER);

        } catch (ActivityNotFoundException ex) {//если ни одно приложение не найдено
            Toast.makeText(getActivity(), R.string.ex_not_found_fm, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            AppLog.e(getActivity(), ex);//записывается в лог проги как ошибка
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER && resultCode == Activity.RESULT_OK) {
            String attachFilePath = FileUtils.getRealPathFromURI(getContext(), data.getData());
            if (attachFilePath.endsWith(".html")) {
                loadUrl("file:///" + attachFilePath);
            } else {
                String cssData = FileUtils.readFileText(attachFilePath)
                        .replace("\\", "\\\\")
                        .replace("'", "\\'").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
                mWebView.evalJs("$('#dev-less-file-path')[0].value='" + attachFilePath + "';");
                mWebView.evalJs("window['HtmlInParseLessContent']('" + cssData + "');");
            }

        }
    }
}

