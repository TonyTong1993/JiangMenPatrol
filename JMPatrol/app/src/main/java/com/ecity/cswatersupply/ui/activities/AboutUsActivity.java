package com.ecity.cswatersupply.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.AppUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class AboutUsActivity extends BaseActivity {
    private CustomTitleView viewTitle;
    private TextView versionname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_us);
        initUI();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    private void initUI() {
        viewTitle = (CustomTitleView) findViewById(R.id.customTitleView1);
        viewTitle.setBtnStyle(CustomTitleView.BtnStyle.ONLY_BACK);
        viewTitle.setTitleText(R.string.app_name_cswatersupply);

        versionname = (TextView) findViewById(R.id.versionname);
        viewTitle.setTitleText(R.string.my_profile_about_us);

        String version = AppUtil.getVersion(this);
        if (StringUtil.isBlank(version)) {
            throw new RuntimeException("Application version is unknown.");
        }
        versionname.setText("v" + version);
    }
}