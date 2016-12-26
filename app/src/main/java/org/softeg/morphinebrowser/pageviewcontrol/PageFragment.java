package org.softeg.morphinebrowser.pageviewcontrol;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.AppPreferences;
import org.softeg.morphinebrowser.R;
import org.softeg.morphinebrowser.common.UrlExtensions;
import org.softeg.morphinebrowser.other.UrlItem;
import org.softeg.morphinebrowser.other.UrlsAdapter;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.Developer;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.HtmlOutManager;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.IHtmlOutListener;

import java.io.File;
import java.util.ArrayList;

/*
 * Created by slinkin on 01.10.2014.
 */
public abstract class PageFragment extends PageViewFragment implements
        IHtmlOutListener {
    public static final String ID = "org.softeg.slartus.car72.topic.PageFragment";
    public static final String SCROLL_Y_KEY = "PageFragment.SCROLL_Y_KEY";
    private static final String TAG = "PageFragment";

    protected HtmlOutManager mHtmlOutManager;
    protected int mScrollY = 0;
    private EditText editText;

    protected void initHtmlOutManager() {
        mHtmlOutManager = new HtmlOutManager(this);
        mHtmlOutManager.registerInterfaces(mWebView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initHtmlOutManager();
        if (savedInstanceState != null && savedInstanceState.containsKey(SCROLL_Y_KEY)) {
            mScrollY = savedInstanceState.getInt(SCROLL_Y_KEY);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
        switch (hitTestResult.getType()) {
            case WebView.HitTestResult.UNKNOWN_TYPE:
            case WebView.HitTestResult.EDIT_TEXT_TYPE:
                break;
            default: {
                UrlExtensions.showChoiceDialog(getActivity(), hitTestResult.getExtra());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_Y_KEY, mWebView.getScrollY());
    }


    protected void setLoading(boolean loading) {
        try {
            if (getActivity() == null) return;


        } catch (Throwable ignore) {

            android.util.Log.e("TAG", ignore.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHtmlOutManager.onActivityResult(requestCode, resultCode, data);
    }


    public void saveHtml() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE).whenPermissionsGranted(new PermissionsGrantedListener() {
                @Override
                public void onPermissionsGranted(String[] permissions) throws SecurityException {
                    saveHtmlHelper();
                }
            }).whenPermissionsRefused(new PermissionsRefusedListener() {
                @Override
                public void onPermissionsRefused(String[] permissions) {
                    Toast.makeText(getActivity(), "Wow!!! Permission not granted!", Toast.LENGTH_SHORT).show();
                }
            }).execute(getActivity());
        } else {
            saveHtmlHelper();
        }
    }

    private void saveHtmlHelper() {
        try {
            mWebView.evalJs("window." + Developer.NAME + ".saveHtml('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        } catch (Throwable ex) {
            AppLog.e(getActivity(), ex);
        }
    }

    public void showElementsOutline() {
        try {
            mWebView.evalJs("if (!document.querySelector('.show-outline-elements')) {\n" +
                    " var o = '<style class=\"show-outline-elements\">*,:before,:after{outline:1px solid rgba(255,100,0,0.6)}</style>';\n" +
                    " document.body.insertAdjacentHTML(\"beforeEnd\",o);\n" +
                    " } else document.body.removeChild(document.querySelector('.show-outline-elements'));");
        } catch (Throwable throwable) {
            AppLog.e(getActivity(), throwable);
        }

    }
    protected void showFontSizeDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.font_size_dialog, null);
        changeText(getString(R.string.fontsize));

        Log.e(TAG, "showFontSizeDialog: ");
        int size = getWebView().getSettings().getDefaultFontSize();
        Log.e(TAG, "Default Font Size: " + size);

        if (AppPreferences.isFirstStart()) {
            showWarningDialog();
        }



        assert v != null;
        final DiscreteSeekBar sb = (DiscreteSeekBar) v.findViewById(R.id.value_seek_bar);
        sb.setProgress(AppPreferences.getWebViewFontSize());
        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        editText.setText((sb.getProgress()) + "");

        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.getProgress() > 0) {
                    int i = sb.getProgress() - 1;

                    sb.setProgress(i);
                    getWebView().getSettings().setDefaultFontSize(i);
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
                    getWebView().getSettings().setDefaultFontSize(i);
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
                getWebView().getSettings().setDefaultFontSize(value);
                editText.setText((value) + "");
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
                AppPreferences.setWebViewFontSize(sb.getProgress());
            }
        });

        editText.selectAll();
        editText.requestFocus();

//        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void showSelectStyleDialog() {
//        try {
//            Context context = getActivity();
//            ArrayList<CharSequence> newStyleNames = new ArrayList<CharSequence>();
//            newStyleNames.add("Стандартный");
//            newStyleNames.add("Режим разработчика");
//            final ArrayList<CharSequence> newstyleValues = new ArrayList<CharSequence>();
//            newstyleValues.add(AppAssetsManager.getAssetsPath()+"forum/css/style.css");
//            newstyleValues.add(TopicApi.CSS_DEVELOPER);
//            File file = new File(context.getExternalFilesDir(null).getPath() + File.separator + "css");
//            getStylesList(newStyleNames, newstyleValues, file, ".css");
//
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Выбор стиля")
//                    .setSingleChoiceItems(
//                            newStyleNames.toArray(new CharSequence[newStyleNames.size()]),
//                            newstyleValues.indexOf(AppPreferences.Topic.getStyleCssPath()), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    AppPreferences.Topic.setStyleCssPath(newstyleValues.get(i).toString());
//                                    refreshPage();
//                                }
//                            })
//                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).create().show();
//        } catch (Throwable ex) {
//            AppLog.e(ex);
//        }
    }

    protected void showDialogUrlsList() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_urls, null);
        assert  v != null;
        editText = (EditText) v.findViewById(R.id.editText);
        editText.setText(getGlobalUrl());

        final ListView listView = (ListView) v.findViewById(R.id.list_urls);

        final ArrayList<UrlItem> urls = tinyDB.getListObject("SaveUrl");
        ArrayAdapter<UrlItem> adapter = new UrlsAdapter(getContext(), urls);
        listView.setAdapter(adapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(v);
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText("");
                UrlItem item = urls.get(i);
                loadUrl(item.getUrl());
                dialog.dismiss();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean state = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||(actionId == EditorInfo.IME_ACTION_GO)) {
                    String link = editText.getText().toString();
                    if (link != null) {
                        loadUrl(link);
                        dialog.dismiss();
                        state = true;
                    }
                }
                return state;
            }
        });

        final Button clear_history = (Button) v.findViewById(R.id.clear_history);
        clear_history.setVisibility(urls.size() != 0 ? View.VISIBLE : View.GONE);
        clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinyDB.remove("SaveUrl");
                urls.clear();
                listView.setAdapter(null);
                clear_history.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Усе чисто", Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }



    private static void getStylesList(ArrayList<CharSequence> newStyleNames, ArrayList<CharSequence> newstyleValues,
                                      File file, String ext) {

        if (file.exists()) {
            File[] cssFiles = file.listFiles();
            assert cssFiles != null;
            for (File cssFile : cssFiles) {
                if (cssFile.isDirectory()) {
                    getStylesList(newStyleNames, newstyleValues, cssFile, ext);
                    continue;
                }
                String cssPath = cssFile.getPath();
                if (!cssPath.toLowerCase().endsWith(ext)) continue;

                newStyleNames.add(cssFile.getName());
                newstyleValues.add(cssPath);

            }
        }
    }

    protected void showCacheDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.cache_settings)
                .iconRes(R.drawable.ic_sd_card_black_24dp)
                .items(new String[]{getString(R.string.c_s), getString(R.string.off_c), getString(R.string.only_c)})
                .itemsCallbackSingleChoice(AppPreferences.getCacheMode() - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        AppPreferences.setCacheMode(i + 1);
                        return false;
                    }
                })
                .show();
    }

    protected void showWarningDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.warning)
                        .iconRes(R.drawable.ic_warning_black_24dp)
                        .content(R.string.warning_swipe)
                        .positiveText(R.string.user_agree)
                        .canceledOnTouchOutside(false)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                AppPreferences.firstStartDone();
                            }
                        }).show();
            }
        }, 500);
    }
}