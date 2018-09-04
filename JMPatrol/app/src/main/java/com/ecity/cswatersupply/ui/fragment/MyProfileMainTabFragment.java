package com.ecity.cswatersupply.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.adapter.MyProfileMainTabAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.ListViewMenuItem;
import com.ecity.cswatersupply.model.SignInStateBean;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.service.PatrolService;
import com.ecity.cswatersupply.service.TaskArrivalService;
import com.ecity.cswatersupply.service.UserService;
import com.ecity.cswatersupply.ui.activities.AboutUsActivity;
import com.ecity.cswatersupply.ui.activities.DeviceUsageActivity;
import com.ecity.cswatersupply.ui.activities.GpsStateActivity;
import com.ecity.cswatersupply.ui.activities.LoginActivity;
import com.ecity.cswatersupply.ui.activities.SignInActivity;
import com.ecity.cswatersupply.ui.activities.SystemSettingsActivity;
import com.ecity.cswatersupply.ui.activities.UserCenterActivity;
import com.ecity.cswatersupply.utils.DateUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.xg.model.Notification;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

public class MyProfileMainTabFragment extends ABaseListViewFragment<ListViewMenuItem> {
    private Button btnExit;
    private View userInfoView;
    final String KEY_SIGN_IN = "KEY_SIGN_IN";
    private ListViewMenuItem signItem;
    List<ListViewMenuItem> menus = new ArrayList<ListViewMenuItem>();
    private User currentUser = HostApplication.getApplication().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        EventBusUtil.register(this);
        initUI(view);
        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        if (signItem != null) {
            setSignState(false);
        }
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (signItem != null) {
            if (isVisibleToUser) {
                UserService.getInstance().getWatchState(currentUser);
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * @param isSwitchFragment true: 通过切换tab页来到当前的fragment。false：停在当前fragment，锁屏，再解锁。
     */
    private void setSignState(boolean isSwitchFragment) {
        boolean hasSigned = !StringUtil.isBlank(currentUser.getSignOutTime());

        if (isSwitchFragment && hasSigned) {
            if (currentUser.getSignOutTime().compareTo(DateUtil.getCurrentTime()) < 0) {
                refreshSignItemTitle(R.string.my_profile_sign_in);
            }
        } else {
            if (!hasSigned) {
                refreshSignItemTitle(R.string.my_profile_sign_in);
            }
        }
    }

    private void refreshSignItemTitle(int titleId) {
        signItem.title = getString(titleId);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int prepareLayoutResource() {
        return R.layout.fragment_my_profile;
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    @Override
    protected ArrayListAdapter<ListViewMenuItem> prepareListViewAdapter() {
        return new MyProfileMainTabAdapter(getActivity());
    }

    @Override
    protected List<ListViewMenuItem> prepareDataSource() {
        ListViewMenuItem menu = new ListViewMenuItem(getString(R.string.my_profile_gps_state), null, R.drawable.my_icon_gpsstate);
        menus.add(menu);
        menu = new ListViewMenuItem(getString(R.string.my_profile_device_usage), null, R.drawable.my_icon_phone);
        menus.add(menu);
        menu = new ListViewMenuItem(getString(R.string.my_profile_system_settings), null, R.drawable.my_icon_setting);
        menus.add(menu);
        menu = new ListViewMenuItem(getString(R.string.my_profile_about_us), null, R.drawable.my_icon_aboutus);
        menus.add(menu);
        //如果是班长 && 正在值班 则显示值班签退
        if (currentUser.isCanSign()) {
            int titleId = currentUser.isWatch() ? R.string.my_profile_sign_out : R.string.my_profile_sign_in;
            signItem = new ListViewMenuItem(getString(titleId), null, R.drawable.my_icon_aboutus);
            menus.add(signItem);
        } else {
            //no logic to do
        }

        return menus;
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewMenuItem item = getDataSource().get(position);
                String title = item.getTitle();
                if (getString(R.string.my_profile_gps_state).equals(title)) {
                    UIHelper.startActivityWithoutExtra(GpsStateActivity.class);
                } else if (getString(R.string.my_profile_device_usage).equals(title)) {
                    UIHelper.startActivityWithoutExtra(DeviceUsageActivity.class);
                } else if (getString(R.string.my_profile_system_settings).equals(title)) {
                    UIHelper.startActivityWithoutExtra(SystemSettingsActivity.class);
                } else if (getString(R.string.my_profile_about_us).equals(title)) {
                    UIHelper.startActivityWithoutExtra(AboutUsActivity.class);
                } else if (getString(R.string.my_profile_sign_in).equals(title)) {
                    UserService.getInstance().getIsWatchState();
                } else if (getString(R.string.my_profile_sign_out).equals(title)) {
                    setAlertDialog(view, ResourceUtil.getStringById(R.string.admin_sign_out_confirm), false);
                }
            }
        };
    }

    /**
     * 退出登录与值班签退弹出框
     * @param view
     * @param info     
     * @param isLogOut  是否是退出登录功能
     */
    private void setAlertDialog(View view, String info, boolean isLogOut) {
        TextView tv_title;
        AlertDialog dialog = new AlertDialog.Builder(view.getContext()).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.view_alertdialog);
        dialog.getWindow().findViewById(R.id.rl_edit).setVisibility(View.GONE);
        tv_title = (TextView) dialog.getWindow().findViewById(R.id.dialog_title);
        tv_title.setText(info);
        setButtonClick(dialog, isLogOut);
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return null;
    }

    protected boolean isPullLoadEnabled() {
        return false;
    }

    protected boolean isPullRefreshEnabled() {
        return false;
    }

    private void initUI(View view) {
        btnExit = (Button) view.findViewById(R.id.btn_exit);
        userInfoView = view.findViewById(R.id.layout_user_summary);
        userInfoView.findViewById(R.id.iv_icon).setBackgroundResource(R.drawable.defaultphoto);
        ((TextView) userInfoView.findViewById(R.id.tv_title)).setText(currentUser.getTrueName());
        ((TextView) userInfoView.findViewById(R.id.tv_detail)).setText(currentUser.getGroupName());
        ImageView imgView = (ImageView) userInfoView.findViewById(R.id.iv_icon);
        imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    private void setListeners() {
        userInfoView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.startActivityWithoutExtra(UserCenterActivity.class);
            }
        });

        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setAlertDialog(v, ResourceUtil.getStringById(R.string.exit_logout), true);
            }
        });
    }

    protected void setButtonClick(AlertDialog dialog, boolean isLogOut) {
        TextView positive = (TextView) dialog.getWindow().findViewById(R.id.punish_dialog_positive);
        TextView negative = (TextView) dialog.getWindow().findViewById(R.id.punish_dialog_negative);
        ButtonClick lisButtonClick = new ButtonClick(dialog, isLogOut);
        positive.setOnClickListener(lisButtonClick);
        negative.setOnClickListener(lisButtonClick);
    }

    protected void doOtherthingBeforeExist() {
        EventBusUtil.unregister(this);
        HostApplication.getApplication().doOtherthingBeforeExist(getActivity());
    }

    private class ButtonClick implements OnClickListener {
        private Dialog dialog;
        private boolean isLogOut;

        public ButtonClick(Dialog dialog, boolean isLogOut) {
            this.dialog = dialog;
            this.isLogOut = isLogOut;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.punish_dialog_positive:
                    if (isLogOut) {
                        dialog.dismiss();
//                        updatePatrolManState();
                        //SettingsManager.getInstance().setRememberPassword(false);
                        SettingsManager.getInstance().setAutoLogin(false);
                        UIHelper.startActivityWithoutExtra(LoginActivity.class);
                        doOtherthingBeforeExist();
                        TaskArrivalService.getInstance().stop();
                        getActivity().finish();
                        break;
                    } else {//值班签退
                        dialog.dismiss();
                        UserService.getInstance().signOut(currentUser);
                        break;
                    }
                case R.id.punish_dialog_negative:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    /***
     * 更新人员在线状态
     */
    private void updatePatrolManState() {
        PatrolService.getInstance().updatePatrolManState(false);
    }

    @Override
    protected String getTitle() {
        return getActivity().getResources().getString(R.string.fragment_Profile_title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SignInActivity.SIGN_IN_REQUEST_CODE) {
            if (resultCode == SignInActivity.SIGN_IN_RESULT_CODE) {
                String endSignTime = data.getExtras().getString(SignInActivity.SIGN_IN_RESULT_STRING);
                HostApplication.getApplication().getCurrentUser().setSignOutTime(endSignTime);
                refreshSignItemTitle(R.string.my_profile_sign_out);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.SIGN_OUT_EVENT:
                String msgResult = getResources().getString(R.string.success_submit_sign_out);
                ToastUtil.showShort(msgResult);
                refreshSignItemTitle(R.string.my_profile_sign_in);
                break;
            case ResponseEventStatus.WatchState:
                handleHasSignIn(event);
                break;
            case ResponseEventStatus.WatchEvent:
                handleWatch(event);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.NOTIFICATION_SIGN_OUT:
                handleSignNotification(event);
                break;
            default:
                break;
        }
    }

    private void handleSignNotification(UIEvent event) {
        refreshSignItemTitle(R.string.my_profile_sign_in);
        Notification notification = event.getData();
        AlertView dialog = new AlertView(getActivity(), notification.getTitle(), notification.getContent(), new OnAlertViewListener() {
            @Override
            public void back(boolean result) {
                if (result) {
                  //no logic to do
                }
            }
        }, AlertView.AlertStyle.OK);
        dialog.show();
    }

    private void handleWatch(ResponseEvent event) {
        Map<String, String> result = new HashMap<String, String>();
        result = event.getData();
        currentUser.setWatch(result.get("isWatch").equals("true"));
        currentUser.setCanSign(result.get("canSign").equals("true"));
        currentUser.setSignOutTime("");
        if (result.get("isWatch").equals("true")) {
            currentUser.setSignOutTime(result.get("signOutTime"));
        }
        setSignState(true);
    }

    private void handleHasSignIn(ResponseEvent event) {
        SignInStateBean signInResult = event.getData();
        if (signInResult.isHasSignIn()) {
            showDialog(signInResult);
        } else {
            signIn();
        }
    }

    private void showDialog(SignInStateBean signInResult) {
        AlertView dialog = new AlertView(getActivity(), null, signInResult.getMsg(), new OnAlertViewListener() {
            @Override
            public void back(boolean ok) {
                if (ok) {
                    signIn();
                }
            }
        }, AlertView.AlertStyle.YESNO);
        dialog.show();
    }

    private void signIn() {
        Intent intent = new Intent(HostApplication.getApplication().getApplicationContext(), SignInActivity.class);
        MyProfileMainTabFragment.this.startActivityForResult(intent, SignInActivity.SIGN_IN_REQUEST_CODE);
    }
}
