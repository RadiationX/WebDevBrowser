package org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by slinkin on 02.10.2014.
 */
public class HtmlOutManager {
    protected List<IHtmlOut> interfaces;

    public HtmlOutManager(IHtmlOutListener htmlOutCallBack) {

        interfaces = new ArrayList<IHtmlOut>();
        fillInterfaces(htmlOutCallBack);
    }

    protected void fillInterfaces(IHtmlOutListener htmlOutCallBack){
        interfaces.add(new Developer(htmlOutCallBack));
        interfaces.add(new HtmlOut(htmlOutCallBack));
    }

    @SuppressLint("JavascriptInterface")
    public void registerInterfaces(WebView webView) {
        for (IHtmlOut i : interfaces) {
            webView.addJavascriptInterface(i, i.getName());
        }
    }

    public Boolean onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        for (IHtmlOut i : interfaces) {
            if(!i.hasRequestCode(requestCode))
                continue;
            i.onActivityResult(requestCode,resultCode,data);
        }

        return false;
    }
}
