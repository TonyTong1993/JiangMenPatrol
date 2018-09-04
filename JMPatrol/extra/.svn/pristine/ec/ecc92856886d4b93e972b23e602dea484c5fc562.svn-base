package com.enrique.bluetooth4falcon;

import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.z3pipe.mobile.android.corssdk.model.CorsParams;
import com.z3pipe.mobile.android.corssdk.net.NetWorkService;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;
import com.zzz.ecity.android.applibrary.view.TitleView;
import com.zzz.ecity.android.applibrary.view.TitleView.BtnStyle;

/**
 * @author zhangfusheng
 * @date 2011-09-21
 */
public class CORSParamsActivity extends Activity {

    public static final String PREFS_CORS = "Cors_PrefsFiles";
    public static final String PREFS_MOUNTPOINTS_LIST = "MP_List_PrefsFiles";
    public static final int MESSAGE_CORS_PARAMS_READ = 0;
    private static final String PARAMS_LIST = "paramslist";

    private int files_count = 0;// cors参数文件数
    CorsParams cpc;

    private SharedPreferences sharedPreferences;
    private Editor editor;

    private Set<String> set;
    private ArrayAdapter<String> mArrayAdapter;// 源列表节点string容器

    private String IP_VALUE1 = "";
    private String PORT_VALUE = "";
    private String MOUNTPOINT_VALUE = "";
    private String USER_ID = "";
    private String PASSWORD = "";

    ImageButton mClear;
    private EditText mIPEditText1;
    private EditText mPortEditText, mUserIDEditText, mPwdEditText;
    private Spinner mSpinner;

    private String mIPAdrress = "61.155.214.238", mPort = "6602",
            mMountpoint = "FJZLRTCM32", mUserID = "fjzl", mPWD = "fjzl";

    private NetWorkService mNetWorkService = null;
    private String[] params = new String[5];
    private Button connectcors, serverlistupdate;

    enum Answer {
        YES, NO, ERROR
    }

    private TitleView customTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        setContentView(R.layout.activity_cors_param);

        // 防止用户点击"返回键"
        setResult(Activity.RESULT_CANCELED);
        initView();
        bindListener();
        // 加载前次参数记录
        sharedPreferences = this.getSharedPreferences("SP", MODE_PRIVATE);
        loadSharedPreferencesFiles2UI(sharedPreferences);

        refreshFirstPoint();
    }

    private void initView() {
        mIPEditText1 = (EditText) findViewById(R.id.ip1);
        customTitleView = (TitleView) findViewById(R.id.customTitleView);
        mPortEditText = (EditText) findViewById(R.id.port);
        mSpinner = (Spinner) findViewById(R.id.mp_spinner);
        mUserIDEditText = (EditText) findViewById(R.id.userID);
        mPwdEditText = (EditText) findViewById(R.id.password);

        connectcors = (Button) findViewById(R.id.connectcors);
        serverlistupdate = (Button) findViewById(R.id.serverlistupdate);

        customTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        customTitleView
                .setRightActionBtnText(getString(R.string.str_cors_saveparams));
        customTitleView.setTitleText(getString(R.string.str_cors_set_params));

    }

    private void bindListener() {
        mPortEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!v.hasFocus()) {
                    mPort = mPortEditText.getText().toString();
                }
            }
        });

        connectcors.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mIPAdrress = mIPEditText1.getText().toString();

                // 利用正则表达式验证ip地址合法性
                if (!IPChecking(mIPAdrress)) {
                    Toast.makeText(getApplicationContext(), "亲，IP不合法，细心一点喽！", Toast.LENGTH_SHORT).show();
                    return;
                }

                mPort = mPortEditText.getText().toString();
                if (mPort == null || mPort.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "亲，不会连端口都忘了吧！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                mUserID = mUserIDEditText.getText().toString();
                mPWD = mPwdEditText.getText().toString();

                if (mUserID == null || mUserID.equals("") || mPWD == null
                        || mPWD.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "用户名或密码不能为空！", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.b, 0, 0);
                    toast.show();
                    return;
                }

                Object mountpoint = mSpinner.getSelectedItem();
                if (null == mountpoint) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.str_cors_get_mountpointslist_fail), Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                mMountpoint = mountpoint.toString();
                if (mMountpoint == null || mMountpoint.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.str_cors_select_mountpoint), Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                params[0] = mIPAdrress;
                params[1] = mPort;
                params[2] = mUserID;
                params[3] = mPWD;
                params[4] = mMountpoint;

                if (!isNetworkAvailable()) {
                    setNetworkMethod(CORSParamsActivity.this);
                    return;
                }

                // 将参数值传回主界面，并在主界面启动连接线程
                Intent intent = new Intent();
                intent.putExtra(PARAMS_LIST, params);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        serverlistupdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.str_cors_networkerror), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                mIPAdrress = mIPEditText1.getText().toString();
                if (!IPChecking(mIPAdrress)) {
                    Toast.makeText(getApplicationContext(), "亲，IP不合法，请确认！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mPort = mPortEditText.getText().toString();
                if (mNetWorkService != null) {
                    mNetWorkService = null;
                }

                try {
                    mNetWorkService = new NetWorkService(mIPAdrress, mPort,
                            mHandler);
                    mNetWorkService.connect2Server();
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                }

            }
        });
    }

    private void refreshFirstPoint() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.str_cors_networkerror), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        mIPAdrress = mIPEditText1.getText().toString();
        if (!IPChecking(mIPAdrress)) {
            Toast.makeText(getApplicationContext(), "亲，IP不合法，请确认！",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mPort = mPortEditText.getText().toString();
        if (mNetWorkService != null) {
            mNetWorkService = null;
        }
        mNetWorkService = new NetWorkService(mIPAdrress, mPort,
                mHandler);
        mNetWorkService.connect2Server();
    }

    public void onBackButtonClicked(View view) {
        if (mNetWorkService != null) {
            mNetWorkService.cancleAll();
            mNetWorkService = null;
        }

        finish();
    }

    public void onActionButtonClicked(View view) {
        try {
            editor = sharedPreferences.edit();
            editor.putString("ip", mIPEditText1.getText().toString());
            editor.putString("port", mPortEditText.getText().toString());
            editor.putStringSet(PREFS_MOUNTPOINTS_LIST, set);
            Object mountpoint = mSpinner.getSelectedItem();
            if (null != mountpoint) {
                editor.putString("mountpoint", mountpoint.toString());
            }
            editor.putString("userId", mUserIDEditText.getText().toString());
            editor.putString("psword", mPwdEditText.getText().toString());

            editor.commit();
            Toast.makeText(getApplicationContext(), getString(R.string.str_cors_paramsaved),
                    Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            // TODO: handle exception
            throw e;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*
     * 利用正则表达式验证IP字符串的合法性
     */
    public boolean IPChecking(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    private static class MsgHandler extends Handler {
        private WeakReference<Activity> mActivity;

        MsgHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (activity != null) {
                CORSParamsActivity mActivity = (CORSParamsActivity) activity;
                mActivity.handleMessage(msg);
            }
        }
    }

    private MsgHandler mHandler = new MsgHandler(this);

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case NetWorkService.MESSAGE_SOURCE_TABLE_OK:
                if (msg.obj != null) {
                    @SuppressWarnings("unchecked")
                    ArrayList<String> srcTable = (ArrayList<String>) msg.obj;

                    if (null == srcTable || srcTable.isEmpty()) {
                        return;
                    }

                    // 保存最近一次的挂载点列表
                    set = new HashSet<String>(srcTable);

                    mArrayAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.source_spinner_item_style, srcTable);
                    mSpinner.setAdapter(mArrayAdapter);
                    int count = srcTable.size();
                    for (int i = 0; i < count; i++) {
                        String item = String.valueOf(srcTable.get(i));
                        if (null != mMountpoint && mMountpoint.equalsIgnoreCase(item)) {
                            mSpinner.setSelection(i, true);
                        }
                    }

                    Toast.makeText(getApplicationContext(),
                            getString(R.string.str_cors_getsourcelist_success), Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case NetWorkService.MESSAGE_SOURCE_TABLE_ERR:
                if (null != msg.obj) {
                    String errMsg = (String) msg.obj;

                    Toast.makeText(getApplicationContext(), errMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case MESSAGE_CORS_PARAMS_READ:
                mIPEditText1.setText(IP_VALUE1);

                mPortEditText.setText(PORT_VALUE);
                mUserIDEditText.setText(USER_ID);
                mPwdEditText.setText(PASSWORD);

                SharedPreferences spf = getSharedPreferences(
                        PREFS_MOUNTPOINTS_LIST, 0);
                Set<String> set = spf.getStringSet("list", null);
                if (set != null) {
                    ArrayList<String> arrayList = new ArrayList<String>(set);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_item, arrayList);
                    mSpinner.setAdapter(adapter);

                    int position = 0;
                    for (String string : arrayList) {
                        if (string.equals(MOUNTPOINT_VALUE)) {
                            mSpinner.setSelection(position);
                            return;
                        }
                        position++;
                    }
                }
            default:
                break;
        }
    }

    /*
     * 验证当前网络是否连接
     */
    private boolean isNetworkAvailable() {
        boolean flag = false;
        ConnectivityManager conManager = (ConnectivityManager) this
                .getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netService = conManager.getActiveNetworkInfo();
        if (netService != null) {
            flag = netService.isAvailable();
        }
        return flag;
    }

    /*
     * 打开设置网络界面
     */
    public static void setNetworkMethod(final Activity activity) {

        AlertView alert = new AlertView(activity, "网络设置提示", "网络连接不可用,是否进行设置?", new OnAlertViewListener() {

            @Override
            public void back(boolean result) {
                if (result) {
                    Intent intent = null;
                    intent = new Intent(
                            android.provider.Settings.ACTION_SETTINGS);
                    activity.startActivity(intent);
                }
            }
        }, AlertView.AlertStyle.OK_CANCEL);
        alert.setOkText("设置");
        alert.show();
    }

    /*
     * 获取当前UI控件中各项参数
     */
    @SuppressWarnings("unused")
    private CorsParams getParamsFromUI() {
        try {
            IP_VALUE1 = mIPEditText1.getText().toString();

            PORT_VALUE = mPortEditText.getText().toString();
            MOUNTPOINT_VALUE = mSpinner.getSelectedItem().toString();
            USER_ID = mUserIDEditText.getText().toString();
            PASSWORD = mPwdEditText.getText().toString();

            if (null == cpc) {
                cpc = new CorsParams();
            }
            cpc.setIp(IP_VALUE1);
            cpc.setPort(PORT_VALUE);
            cpc.setMountpoint(MOUNTPOINT_VALUE);
            cpc.setUserId(USER_ID);
            cpc.setPsw(PASSWORD);

            return cpc;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    /*
     * 将保存的参数加载至UI对应控件
     */
    private void loadSharedPreferencesFiles2UI(
            SharedPreferences sharedPreferences) {
        try {
            if (null == sharedPreferences) {
                Toast.makeText(this, "当前无历史保存记录参数", Toast.LENGTH_SHORT).show();
                return;
            }

            String ip = sharedPreferences.getString("ip", "61.155.214.238");
            String port = sharedPreferences.getString("port", "6602");
            this.set = sharedPreferences.getStringSet(PREFS_MOUNTPOINTS_LIST, null);
            String userId = sharedPreferences.getString("userId", "fjzl");
            String psWord = sharedPreferences.getString("psword", "fjzl");
            mMountpoint = sharedPreferences.getString("mountpoint", "FJZLRTCM32");

            // 设置ip
            mIPEditText1.setText(ip);
            // 设置port
            mPortEditText.setText(String.valueOf(port));
            // 设置userId
            mUserIDEditText.setText(userId);
            // 设置psWord
            mPwdEditText.setText(psWord);

            // 设置源列表
            if (null != set) {
                ArrayList<String> arrayList = new ArrayList<String>(set);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        R.layout.source_spinner_item_style, arrayList);
                mSpinner.setAdapter(arrayAdapter);

                int count = arrayList.size();
                for (int i = 0; i < count; i++) {
                    String item = String.valueOf(arrayList.get(i));
                    if (null != mMountpoint && mMountpoint.equalsIgnoreCase(item)) {
                        mSpinner.setSelection(i, true);
                    }
                }
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
            // Toast.makeText(this, null, Toast.LENGTH_SHORT).show();
            return;
        } catch (PatternSyntaxException e) {
            // TODO: handle exception
            Toast.makeText(this,
                    "loadSharedPreferencesFiles2UI:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private String WriteToXml(CorsParams cpc) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "project");
            serializer.attribute("", "date", "2015-04-04");

            serializer.startTag("", "id");
            serializer.text(String.valueOf(files_count + 1));
            serializer.endTag("", "id");

            serializer.startTag("", "ip");
            serializer.text(cpc.getIp());
            serializer.endTag("", "ip");

            serializer.startTag("", "port");
            serializer.text(cpc.getPort());
            serializer.endTag("", "port");

            serializer.startTag("", "mountpoint");
            serializer.text(cpc.getMountpoint());
            serializer.endTag("", "mountpoint");

            serializer.startTag("", "userId");
            serializer.text(cpc.getUserId());
            serializer.endTag("", "userId");

            serializer.startTag("", "psw");
            serializer.text(cpc.getPsw());
            serializer.endTag("", "psw");

            serializer.endTag("", "project");
            serializer.endDocument();

            return writer.toString();
        } catch (Exception e) {
            // TODO: handle exception
            return "error";
        }
    }
}
