package com.ecity.cswatersupply.emergency.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.emergency.fragment.ReportQueryInfoFragment;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.project.activity.ProjectSafeSearchActivity;
import com.ecity.cswatersupply.project.network.request.SearchProjectParameter;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ReportSearchActivity extends BaseActivity {
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
    private ArrayList<String> areaStrList;
    private TextView tv_view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quike_report_searth);
        initView();
    }

    public void onActionButtonClicked(View view) {
        int intExtra = getIntent().getIntExtra(ReportQueryInfoFragment.TAB_POSITION, -1);
        searchParameter.setProName(etName.getText().toString());
        searchParameter.setProCode(tv_view1.getText().toString());
        searchParameter.setProTypeid(intExtra);

        UIEvent uievent = new UIEvent(UIEventStatus.EMERGENCY_REPORT_QUERY_SEARCH);
        uievent.setData(searchParameter);
        EventBusUtil.post(uievent);
        finish();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void initView() {
        searchParameter = new SearchProjectParameter();

        etName = (EditText) findViewById(R.id.et_search_name);

        btn_start_value1 = (Button) findViewById(R.id.btn_start_value1);
        btn_end_value1 = (Button) findViewById(R.id.btn_end_value1);
        titleView = (CustomTitleView) findViewById(R.id.customTitleView1);
        titleView.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        titleView.setTitleText(R.string.title_search);
        titleView.setRightActionBtnText(R.string.ok);
        btn_start_value1.setOnClickListener(new MyDateClickListener());
        btn_end_value1.setOnClickListener(new MyDateClickListener());

        LinearLayout ll_view1 = (LinearLayout) findViewById(R.id.ll_view1);
        tv_view1 = (TextView) findViewById(R.id.tv_view1);

        areaStrList = new ArrayList<>();
        for (EarthQuakeInfoModel model : SessionManager.quakeInfoList) {
            if (!areaStrList.contains(model.getRegion()) && !StringUtil.isBlank(model.getRegion())) {
                areaStrList.add(model.getRegion());
            }
        }

        areaStrList.add(0, "全部");

        new CustomSpinner(this, ll_view1, tv_view1, new TypeSpinnerListener(), areaStrList, 0);
//        if (tv_view1.getText().toString().isEmpty()){
//            tv_view1.setText("请选择项目类型");
//        }
    }

    class TypeSpinnerListener implements CustomSpinner.OnSpinnerListener {
        @Override
        public void back(int position) {
            searchParameter.setProCode(areaStrList.get(position));
        }
    }

    class MyDateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DatetimePickerDialog dialog;
            switch (v.getId()) {
                case R.id.btn_start_value1:
                    dialog = new DatetimePickerDialog(ReportSearchActivity.this, new DatetimePickerDialog.DatetimePickerCallback() {

                        @Override
                        public void OnOK(String input) {
                            btn_start_value1.setText(input);
                        }
                    }, DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR_MONTH_DATE, null);
                    dialog.show();
                    break;
                case R.id.btn_end_value1:
                    dialog = new DatetimePickerDialog(ReportSearchActivity.this, new DatetimePickerDialog.DatetimePickerCallback() {

                        @Override
                        public void OnOK(String input) {
                            btn_end_value1.setText(input);
                        }
                    }, DatetimePickerDialog.EPickerDialogStyle.OK_CANCEL, DatetimePickerDialog.EPickerType.YEAR_MONTH_DATE, null);
                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    }
}
