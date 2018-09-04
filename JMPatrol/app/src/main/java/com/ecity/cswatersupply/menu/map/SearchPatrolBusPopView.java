package com.ecity.cswatersupply.menu.map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.PatrolBusSelectAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchPatrolBusPopView {

    private List<String> sourceList = new ArrayList<>();
    private OnPopViewDismissListener onPopViewDismissListener;
    private Activity activity;
    private boolean isShowing = false;
    private PopupWindow popupWindow = null;
    private EditText mSearchTxt;
    private ListView mListView;
    private PatrolBusSelectAdapter mAdapter;
    private String mSelectValue;
    private DisplayMetrics mDisplayMetrics;
    private List<String> tempFilterList = new ArrayList<>();
    private TextView mNoPatrolBusTV;

    public interface OnPopViewDismissListener {
        void onPopViewDismissed(String userName);
    }

    public SearchPatrolBusPopView(IMapOperationContext fragment, List<String> dataSource, OnPopViewDismissListener onPopViewDismissListener) {
        this.sourceList = dataSource;
        this.activity = fragment.getContext();
        this.onPopViewDismissListener = onPopViewDismissListener;
        mDisplayMetrics = new DisplayMetrics();
        fragment.getContext().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mAdapter = new PatrolBusSelectAdapter(activity);
    }

    public boolean showSearchPopView() {
        if (isShowing) {
            popupWindow.dismiss();
        } else {
            showWindow(mDisplayMetrics.widthPixels);
            initData();
            bindEvent();
        }
        return true;
    }

    private void showWindow(int screenWidth) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.popview_search_pop, null);
        mListView = (ListView) linearLayout.findViewById(R.id.listview);
        mSearchTxt = (EditText) linearLayout.findViewById(R.id.edit_search);
        mNoPatrolBusTV = (TextView) linearLayout.findViewById(R.id.tv_no_patrol_bus_tips);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(linearLayout, screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.layercontrolwin_anim_style);
        popupWindow.setOnDismissListener(onDismissListener);
        popupWindow.showAsDropDown(activity.findViewById(R.id.view_title_mapactivity), 0, 0);
        popupWindow.update();
        isShowing = true;

    }

    private void initData() {
        mSelectValue = null;
        if (sourceList.size() != 0) {
            mNoPatrolBusTV.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter.setList(sourceList);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mListView.setVisibility(View.GONE);
            mNoPatrolBusTV.setVisibility(View.VISIBLE);
        }
    }

    PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            isShowing = false;
            onPopViewDismissListener.onPopViewDismissed(mSelectValue);
        }
    };

    private void bindEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (tempFilterList.size() == 0) {
                    mSelectValue = sourceList.get(position);
                } else {
                    mSelectValue = tempFilterList.get(position);
                }
                mSearchTxt.setText("");
                popupWindow.dismiss();
            }
        });

        mSearchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString();
                List<String> filteredUserList = filterSelectUser(sourceList, key);
                if (null == filteredUserList) {
                    return;
                }
                updateListView(filteredUserList);
            }
        });
    }

    private List<String> filterSelectUser(List<String> allSelectList, String key) {
        if (allSelectList == null) {
            return null;
        }
        if (StringUtil.isBlank(key)) {
            return allSelectList;
        }

        List<String> filterResultList = new ArrayList<>();
        for (int i = 0; i < allSelectList.size(); i++) {
            if (allSelectList.get(i).contains(key)) {
                filterResultList.add(allSelectList.get(i));
            }
        }
        return filterResultList;
    }

    private void updateListView(List<String> filterList) {
        tempFilterList = filterList;
        mAdapter.setList(filterList);
    }
}
