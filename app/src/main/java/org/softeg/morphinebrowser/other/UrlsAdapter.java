package org.softeg.morphinebrowser.other;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by isanechek on 08.08.16.
 */

public class UrlsAdapter extends ArrayAdapter<UrlItem> {

    public UrlsAdapter(Context context, ArrayList<UrlItem> items) {
        super(context, android.R.layout.simple_expandable_list_item_2,items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UrlItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_expandable_list_item_2, null);
            assert item != null;
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(item.getTitle());
            ((TextView) convertView.findViewById(android.R.id.text2)).setText(item.getUrl());
        }
        return convertView;
    }
}
