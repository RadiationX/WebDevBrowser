package org.softeg.morphinebrowser.other;

/**
 * Created by isanechek on 08.08.16.
 */

public class UrlItem {
    private String title;
    private String url;

    public UrlItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public UrlItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
