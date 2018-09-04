package com.ecity.cswatersupply.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerDialogStyle;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.EPickerType;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;

public class ProjectSafeSearchActivity extends BaseActivity {
    public static final int REQUEST_EDIT_FITLER = 1000;
    public static final int RESULT_CODE_OK = 1;
    public static final int RESULT_CODE_CANCEL = 0;
    public static final String SEARCH_RESULT_DATA = "SEARCH_RESULT_DATA";
    private Button btn_start_value1;
    private Button btn_end_value1;
    private CustomTitleView titleView;
    private EditText tv_search;
    private SearchProjectParameter searchParameter;
    private EditText etName, etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_search);
        initView();
    }

    public void onActionButtonClicked(View view) {
        searchParameter.setProName(etName.getText().toString());
        searchParameter.setProCode(etCode.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_RESULT_DATA, searchParameter);
        setResult(RESULT_CODE_OK, new Intent().putExtras(bundle));
        finish();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void initView() {
        searchParameter = new SearchProjectParameter();

        etName = (EditText) findViewById(R.id.et_search_name);
        etCode = (EditText) findViewById(R.id.et_search_code);

        btn_start_value1 = (Button) findViewById(R.id.btn_start_value1);
        btn_end_value1 = (Button) findViewById(R.id.btn_end_value1);
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        titleView.setTitleText(R.string.title_search);
        titleView.setRightActionBtnText(R.string.ok);
        btn_start_value1.setOnClickListener(new MyDateClickListener());
        btn_end_value1.setOnClickListener(new MyDateClickListener());

        LinearLayout ll_view1 = (LinearLayout) findViewById(R.id.ll_view1);
        TextView tv_view1 = (TextView) findViewById(R.id.tv_view1);
        String[] typeArray = getResources().getStringArray(R.array.project_type);
        ArrayList<String> sgdwList = new ArrayList<>();
        for (String str : typeArray) {
            sgdwList.add(str);
        }
        new CustomSpinner(this, ll_view1, tv_view1, new TypeSpinnerListener(), sgdwList, -1);
//        if (tv_view1.getText().toString().isEmpty()){
//            tv_view1.setText("请选择项目类型");
//        }
    }

    class TypeSpinnerListener implements CustomSpinner.OnSpinnerListener {
        @Override
        public void back(int position) {
            searchParameter.setProTypeid(position);
        }
    }

    class MyDateClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            DatetimePickerDialog dialog;
            switch (v.getId()) {
                case R.id.btn_start_value1:
                    dialog = new DatetimePickerDialog(ProjectSafeSearchActivity.this, new DatetimePickerCallback() {

                        @Override
                        public void OnOK(String input) {
                            btn_start_value1.setText(input);
                        }
                    }, EPickerDialogStyle.OK_CANCEL, EPickerType.YEAR_MONTH_DATE, null);
                    dialog.show();
                    break;
                case R.id.btn_end_value1:
                    dialog = new DatetimePickerDialog(ProjectSafeSearchActivity.this, new DatetimePickerCallback() {

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
