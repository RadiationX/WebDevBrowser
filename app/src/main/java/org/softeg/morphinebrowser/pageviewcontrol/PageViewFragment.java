package org.softeg.morphinebrowser.pageviewcontrol;


import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.R;
import org.softeg.morphinebrowser.common.FileUtils;
import org.softeg.morphinebrowser.controls.AppWebView;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.AppWebChromeClient;
import org.w3c.dom.Text;


/*
 * Created by slinkin on 07.10.2014.
 */
public class PageViewFragment extends Fragment implements View.OnClickListener, IWebViewClientListener {
    protected AppWebView mWebView;
    protected EditText mUrlEdit;

    protected int getViewResourceId() {
        return R.layout.webview_fragment;
    }

    protected Boolean autoHideActionBar(){
        return false;
    }

    protected void loadUrl(){
        try{
            String url=mUrlEdit.getText().toString();
            if (!TextUtils.isEmpty(url) && !url.contains("://")) {
                url = "http://" + url;
            }
            mWebView.setWebViewClient(null);
//            if(!TextUtils.isEmpty(url) &&url.startsWith("file://")){
//                mWebView.loadData(FileUtils.readFileText(url),"text/html", "windows-1251");
//            }else
            {
                mWebView.loadUrl(url);
            }
            mWebView.setWebViewClient(new AppWebViewClient(this));
        }catch (Throwable ex){
            AppLog.e(getActivity(),ex);
        }
    }

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                          Bundle savedInstanceState) {
        View v = inflater.inflate(getViewResourceId(), container, false);
        assert v != null;
        mUrlEdit=(EditText) v.findViewById(R.id.editText);
        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUrl();
            }
        });
        mWebView = (AppWebView) v.findViewById(R.id.webView);
        mWebView.initTopicPageWebView();
        if(autoHideActionBar())
            setHideActionBar();
        mWebView.setWebChromeClient(new AppWebChromeClient());
        mWebView.setWebViewClient(new AppWebViewClient(this));
        if (getActivity() != null && getActivity().getActionBar() != null)
            mWebView.setActionBarheight(getActivity().getActionBar().getHeight());
        registerForContextMenu(mWebView);

        return v;
    }

    public AppWebView getWebView() {
        return mWebView;
    }


    public Context getContext() {
        return getActivity();
    }


    public Fragment getFragment() {
        return this;
    }

    public void setProgressBarIndeterminateVisibility(boolean b) {
        if (getActivity() != null)
            getActivity().setProgressBarIndeterminateVisibility(b);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void reloadData() {

    }

    @Override
    public void onClick(View view) {

    }

    public void setHideActionBar() {
        if(!autoHideActionBar())return;
        if (getWebView() == null)
            return;
        if (getActivity() == null)
            return;
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar == null) return;
        setHideActionBar(getWebView(), actionBar);
    }

    public static void setHideActionBar(AppWebView advWebView, final ActionBar actionBar) {
        advWebView.setOnScrollChangedCallback(new AppWebView.OnScrollChangedCallback() {
            @Override
            public void onScrollDown(Boolean inTouch) {
                if (!inTouch)
                    return;
                if (actionBar.isShowing()) {
                    actionBar.hide();
                }
            }

            @Override
            public void onScrollUp(Boolean inTouch) {
                if (!inTouch)
                    return;
                if (!actionBar.isShowing()) {

                    actionBar.show();
                }
            }

            @Override
            public void onTouch() {
                actionBar.show();
            }
        });

    }


}
