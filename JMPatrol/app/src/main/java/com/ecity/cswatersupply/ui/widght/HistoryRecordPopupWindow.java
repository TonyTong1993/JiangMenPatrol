package com.ecity.cswatersupply.ui.widght;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.HistoryRecordListAdapter;

public class HistoryRecordPopupWindow {

    private PopupWindow popupWindow = null;
    private LayoutInflater mInflater;
    private List<String> actionItems;
    private Activity activity;
    private ListView historyList;
    private Button clearBtn;
    private SharedPreferences historypreference;
    private static HistoryRecordListAdapter adapter;
    private OnHistoryListItemClickListener listener;
    private boolean isShowing = false;

    public interface OnHistoryListItemClickListener {
        void onClick(String itemStr);
    }

    public HistoryRecordPopupWindow(Activity activity) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        historypreference = HostApplication.getApplication().getSharedPreferences("SP", Context.MODE_PRIVATE);
    }

    public void dismiss() {
        if (null == popupWindow) {
            return;
        }
        isShowing = false;
        popupWindow.dismiss();
    }

    public void initPopup(List<String> actionItems) {
        this.actionItems = actionItems;
        adapter = new HistoryRecordListAdapter(activity, actionItems);
        if(null == popupWindow) {
            showPopupWindow();
        } else {
            updateList(actionItems);
        }
    }

    public void updateList(List<String> actionItems) {
        this.actionItems = actionItems;
        adapter.setItems(actionItems);
        adapter.notifyDataSetChanged();
    }

    private void showPopupWindow() {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        View rootView = mInflater.inflate(R.layout.panel_list_historyrecord_popup, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(rootView, screenWidth - 40, LayoutParams.WRAP_CONTENT);
        }

        historyList = (ListView) rootView.findViewById(R.id.listView_history);
        clearBtn = (Button) rootView.findViewById(R.id.clear_history_btn);
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (null != actionItems) {
                    String selectText = actionItems.get(arg2);
                    String oldText = historypreference.getString(Constants.KEY_SEARCH_HISTORY_KEYWORD, "");
                    if (!TextUtils.isEmpty(selectText) && !oldText.contains(selectText)) {
                        historypreference.edit().putString(Constants.KEY_SEARCH_HISTORY_KEYWORD, selectText + "," + oldText).commit();
                    }
                    popupWindow.dismiss();
                    if(null != listener) {
                        listener.onClick(selectText);
                    }
                }
            }
        });

        clearBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                historypreference.edit().clear().commit();
                actionItems.clear();
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        EditText view = (EditText) activity.findViewById(R.id.search_text);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(view);
        isShowing = true;
    }

    public void setOnHistoryListItemClickListener(OnHistoryListItemClickListener listener) {
        this.listener = listener;
    }

    public boolean isShowing() {
        return isShowing;
    }

}
