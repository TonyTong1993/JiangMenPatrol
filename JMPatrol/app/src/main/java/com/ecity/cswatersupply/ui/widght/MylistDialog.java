package com.ecity.cswatersupply.ui.widght;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.MyDialogListAdapter;

public class MylistDialog extends Dialog {

    private ListView listview = null;
    private MyDialogListAdapter adapter = null;
    private ArrayList<String> items = new ArrayList<String>();

    public interface ChoiceCallBack {
        void onSelected(int position, String value);
    }

    public MylistDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_listdialog_content);
        adapter = new MyDialogListAdapter(context);
        adapter.setList(items);
        listview = (ListView) this.findViewById(R.id.list);
        listview.setAdapter(adapter);

    }

    public MylistDialog setListItems(ArrayList<String> items) {
        this.items = items;
        adapter.setList(items);
        return this;
    }

    public MylistDialog setInputCallback(final ChoiceCallBack inputCallback) {

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                String value = ((MyDialogListAdapter) parent.getAdapter())
                        .getList().get(position);
                inputCallback.onSelected(position, value);
            }
        });
        return this;
    }
}
