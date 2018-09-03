package com.zzz.ecity.android.applibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zzz.ecity.android.applibrary.R;
import com.zzz.ecity.android.applibrary.adapter.BaseSelectAdapter;
import com.zzz.ecity.android.applibrary.adapter.MultipleSelectAdapter;
import com.zzz.ecity.android.applibrary.adapter.SingleSelectAdapter;
import com.zzz.ecity.android.applibrary.model.SimpleSelectModel;

import java.util.ArrayList;
import java.util.List;

public class DialogSimpleSelect extends Dialog {
    public interface OnCustomDialogListener {
        public void back(boolean result, List<SimpleSelectModel> dataSource);
    }

    public enum SelectStyle {
        SINGLE, MULTIPLE;
    }

    private ListView listView;
    private Button btn_ok;
    private Button btn_cancel;
    private TextView tv_title;
    private EditText et_filer;
    private List<SimpleSelectModel> dataSource;
    private List<SimpleSelectModel> dispDataSource;
    private String title;
    private OnCustomDialogListener customDialogListener;
    private SelectStyle style;
    private Context context;
    private BaseSelectAdapter adapter;

    public DialogSimpleSelect(Context context, String title,
                              List<SimpleSelectModel> dataSource,
                              OnCustomDialogListener customDialogListener, SelectStyle style) {
        super(context, R.style.CustomProgressDialog);
        this.context = context;
        this.title = title;
        this.dataSource = dataSource;
        dispDataSource = new ArrayList<SimpleSelectModel>();
        dispDataSource.addAll(dataSource);
        this.customDialogListener = customDialogListener;
        this.style = style;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_simpleselect);

        listView = ((ListView) findViewById(R.id.ltv_selectList));
        btn_ok = ((Button) findViewById(R.id.btn_ok));
        btn_cancel = ((Button) findViewById(R.id.btn_cancel));
        tv_title = ((TextView) findViewById(R.id.tv_title));
        et_filer = ((EditText) findViewById(R.id.et_filer));
        tv_title.setText(title);

        if (style == SelectStyle.SINGLE) {
            adapter = new SingleSelectAdapter(context, dispDataSource);
        } else if (style == SelectStyle.MULTIPLE) {
            adapter = new MultipleSelectAdapter(context, dispDataSource);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        listView.setSelection(getSelectedItemPosition());
        /***
         * 超过20条选择项就显示筛选框
         */
        if (null != dataSource && dataSource.size() > 20) {
            et_filer.addTextChangedListener(new MyEditTextListener());
            et_filer.setVisibility(View.VISIBLE);
        } else {
            et_filer.setVisibility(View.GONE);
        }
        btn_ok.setOnClickListener(clickOkListener);
        btn_cancel.setOnClickListener(clickCancelListener);
    }

    @Override
    public void show() {
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();

        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            lp.width = height / 10 * 8;
        } else {
            lp.width = width / 10 * 8;
        }
        mWindow.setAttributes(lp);

        super.show();
    }

    private View.OnClickListener clickOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (style == SelectStyle.MULTIPLE) {
                customDialogListener.back(true, dataSource);
            } else {
                customDialogListener.back(true, dispDataSource);
            }
            DialogSimpleSelect.this.dismiss();
        }
    };
    private View.OnClickListener clickCancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            customDialogListener.back(false, dispDataSource);
            DialogSimpleSelect.this.dismiss();
        }
    };

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            adapter.clickAtPosition(arg2);
        }
    };

    // 返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            customDialogListener.back(false, dataSource);
            DialogSimpleSelect.this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyEditTextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // no logic to do.

        }

        @Override
        public void afterTextChanged(Editable s) {
            dispDataSource.clear();
            for (SimpleSelectModel simpleSelect : dataSource) {
                if (simpleSelect.getName().contains(s.toString())) {
                    dispDataSource.add(simpleSelect);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    private int getSelectedItemPosition() {
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).getSelected()) {
                return i;
            }
        }

        return 0;
    }
}
