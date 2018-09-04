package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerDialogStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerType;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class ProjectSearchActivity extends BaseActivity {
    public static final int REQUEST_EDIT_FITLER = 1000;
    public static final int RESULT_CODE_OK = 1;
    public static final int RESULT_CODE_CANCEL = 0;
    public static final String INTENT_KEY_FILTER_COMMON = "INTENT_KEY_FILTER_COMMON";
    public static final String INTENT_KEY_FILTER_START_TIME = "INTENT_KEY_FILTER_START_TIME";
    public static final String INTENT_KEY_FILTER_END_TIME = "INTENT_KEY_FILTER_END_TIME";
    private Button btn_start_value1;
    private Button btn_end_value1;
    private CustomTitleView titleView;
    private EditText tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_project_list_search);
        initView();
    }

    public void onActionButtonClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_KEY_FILTER_COMMON, tv_search.getText().toString());
        bundle.putString(INTENT_KEY_FILTER_START_TIME, btn_start_value1.getText().toString());
        bundle.putString(INTENT_KEY_FILTER_END_TIME, btn_end_value1.getText().toString());
        setResult(RESULT_CODE_OK, new Intent().putExtras(bundle));
        finish();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void initView() {
        tv_search = (EditText) findViewById(R.id.tv_search);
        btn_start_value1 = (Button) findViewById(R.id.btn_start_value1);
        btn_end_value1 = (Button) findViewById(R.id.btn_end_value1);
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        titleView.setTitleText(R.string.title_search);
        titleView.setRightActionBtnText(R.string.ok);
        btn_start_value1.setOnClickListener(new MyDateClickListener());
        btn_end_value1.setOnClickListener(new MyDateClickListener());
    }

    class MyDateClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            DatetimePickerDialog dialog;
            switch (v.getId()) {
                case R.id.btn_start_value1:
                    dialog = new DatetimePickerDialog(ProjectSearchActivity.this, new DatetimePickerCallback() {

                        @Override
                        public void OnOK(String input) {
                            btn_start_value1.setText(input);
                        }
                    }, EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR_MONTH_DATE, null);
                    dialog.show();
                    break;
                case R.id.btn_end_value1:
                    dialog = new DatetimePickerDialog(ProjectSearchActivity.this, new DatetimePickerCallback() {

                        @Override
                        public void OnOK(String input) {
                            btn_end_value1.setText(input);
                        }
                    }, EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR_MONTH_DATE, null);
                    dialog.show();
                    break;

                default:
                    break;
            }
        }
    }
}
