package com.ecity.cswatersupply.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.UserInforAdapter;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.UIHelper;

public class UserCenterActivity extends Activity {
    private CustomTitleView titleView;
    private ListView inforList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_center);
        initUI();
        loadUserInfo();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void onChangePasswordButtonClicked(View view) {
        UIHelper.startActivityWithoutExtra(ChangePasswordActivity.class);
    }

    public void onLogoutButtonClicked(View view) {
    }

    private void initUI() {
        titleView = (CustomTitleView) findViewById(R.id.view_title_user_center);
        titleView.setBtnStyle(BtnStyle.ONLY_BACK);
        titleView.setTitleText(R.string.user_center_title);
        inforList = (ListView)findViewById(R.id.user_inforlist);
    }

    private void loadUserInfo() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        List<String> userInforTitle = new ArrayList<String>();
        List<String> userInfor = new ArrayList<String>();
        userInforTitle.add(getResources().getString(R.string.user_center_user_portrait));
        userInforTitle.add(getResources().getString(R.string.user_center_user_id));
        userInforTitle.add(getResources().getString(R.string.user_center_user_login_name));
        userInforTitle.add(getResources().getString(R.string.user_center_user_true_name));
        userInforTitle.add(getResources().getString(R.string.user_center_department_name));
//        userInforTitle.add(getResources().getString(R.string.user_center_user_level));
        userInforTitle.add(getResources().getString(R.string.user_center_group_id));
        userInforTitle.add(getResources().getString(R.string.user_center_group_name));

        userInfor.add("SetForImage");
        userInfor.add(currentUser.getId());
        userInfor.add(currentUser.getLoginName());
        userInfor.add(currentUser.getTrueName());
        userInfor.add(currentUser.getCompany());
//        userInfor.add(currentUser.getGroupLev());
        userInfor.add(currentUser.getGroupCode());
        userInfor.add(currentUser.getGroupName());

        UserInforAdapter adapter = new UserInforAdapter(userInforTitle, userInfor, this);
        inforList.setAdapter(adapter);
    }
}
