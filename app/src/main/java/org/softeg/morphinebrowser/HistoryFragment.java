package org.softeg.morphinebrowser;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.softeg.morphinebrowser.other.TinyDB;
import org.softeg.morphinebrowser.other.UrlItem;
import org.softeg.morphinebrowser.other.UrlsAdapter;

import java.util.ArrayList;

/**
 * Created by isanechek on 02.10.16.
 */

public class HistoryFragment extends Fragment {

    private static final String GLOBAL_URL = "url";
    private String globalUrl;
    private EditText editText;
    private TinyDB tinyDB;


    public static HistoryFragment newInstance(String url) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(GLOBAL_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(getActivity());
        globalUrl = getActivity().getIntent().getStringExtra(GLOBAL_URL);
        if (globalUrl == null) {
            globalUrl = " ";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_urls, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(globalUrl);

        final ListView listView = (ListView) view.findViewById(R.id.list_urls);

        final ArrayList<UrlItem> urls = tinyDB.getListObject("SaveUrl");
        ArrayAdapter<UrlItem> adapter = new UrlsAdapter(getContext(), urls);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText("");
                UrlItem item = urls.get(i);
//                loadUrl(item.getUrl());
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean state = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String link = editText.getText().toString();
                    if (link != null) {
//                        loadUrl(link);
                        state = true;
                    }
                }
                return state;
            }
        });

        final Button clear_history = (Button) view.findViewById(R.id.clear_history);
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

        view.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

    }
}
