package com.ecity.android.contactmanchooser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ecity.android.contactmanchooser.adapter.ContactAdapter;
import com.ecity.android.contactmanchooser.model.ContactMan;
import com.yokeyword.indexablelistview.IndexEntity;
import com.yokeyword.indexablelistview.IndexableStickyListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

/**
 * 选择联系人页面
 * 
 * 打开页面用startActivityForResult 需要传入的数据包： 1、选择模式 ；2、已选人；3、title；4、实现操作类的名字
 * 
 * @author gaokai
 *
 */
public class ChooseContactManActivity extends BaseActivity {
    public static final int REQCONTACT = 101;
    public static final String KEY_TITLE = "title";
    public static final String KEY_TARGET = "target"; // 返回需要更新的UI控件ID
    public static final String KEY_CHOOSE_MODE = "mode"; // 选择模式
    public static final String KEY_CHOOSED_MEN = "choosedMen"; // 已选人
    public static final int SINGLE_CHOICE = 0x00000010; // 单选模式
    public static final int MULTIPLE_CHOICE = 0x00000011; // 多选模式
    public static final String KEY_CORE_NAME = "core"; // 反射初始化的操作类名
    public static final String KEY_FILTER_MEN = "filter"; // 初始化后需要过滤的数据，比如，选完主办人后，选协办人时，过滤上次选择的主办人
    public static final String KEY_FILTER_RULE = "filterRule"; // 过滤规则，其实就是请求服务的参数
    public static final String KEY_ONLY_MONITOR = "monitor";// 只留班长
    public static final String KEY_CHOOSE_MEN_REQUEST_TYPE = "KEY_CHOOSE_MEN_REQUEST_TYPE";//请求类型

    /**
     * 选择模式：单选还是多选
     */
    private int mode;
    /**
     * 已选人列表
     */
    private TextView tvChoosedMenBar;
    /**
     * 联系人数据源
     */
    private List<ContactMan> allContactMen;
    /**
     * 已选人数据
     */
    private List<ContactMan> hasChoosedMen;
    /**
     * 初始化后需要过滤的单选数据
     */
    private List<ContactMan> filterMen;
    /**
     * 需要自己实现的联系人操作类
     */
    private AContactManCore contactManCore;
    /**
     * 请求联系人的参数
     */
    private HashMap<String, String> params;
    /**
     * 请求类型
     */
    private String requestType;

    private int titleResId;
    private ContactAdapter adapter;
    private IndexableStickyListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contact_man);
        handleIntent();
        initTitle();
        initView();
        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackButtonClicked(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackButtonClicked(View view) {
        if (hasChoosedMen != null && hasChoosedMen.size() > 0) {
            String msg = getResources().getString(R.string.save_or_not);
            AlertView dialog = new AlertView(this, null, msg, new OnAlertViewListener() {
                @Override
                public void back(boolean ok) {
                    // 确定：保存已填信息
                    // 取消：删除缓存
                    if (ok) {
                        onOkButtonClicked(null);
                    } else {
                        finishActivity();
                    }
                }
            }, AlertView.AlertStyle.OK_CANCEL_HIGHLIGHT_CANCEL);
            dialog.show();
        } else {
            finishActivity();
        }
    }

    public void onErrorButtonClicked(View view) {
        initData();
    }

    public void onOkButtonClicked(View view) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bundle.putSerializable(KEY_CHOOSED_MEN, (Serializable) hasChoosedMen);
        intent.putExtras(bundle);
        setResult(REQCONTACT, intent);
        finishActivity();
    }

    /**
     * 显示错误布局
     */
    public void setErrorLayout() {
        TextView tvError = (TextView) findViewById(R.id.tv_error);
        tvError.setText(getResources().getString(R.string.click_me_later));
        tvError.setVisibility(View.VISIBLE);
        tvError.setClickable(true);
    }

    /**
     * 显示空布局
     */
    public void setEmptyLayout() {
        TextView tvError = (TextView) findViewById(R.id.tv_error);
        tvError.setClickable(false); // 联系人为空，就不用再重新点击刷新了
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(getResources().getString(R.string.no_contact_men));
    }

    /**
     * 自定义实现获取数据后，要手动调用该方法
     */
    public void updateContactMenList(List<ContactMan> allContactMen) {
        filterMen(allContactMen);
        onlySaveMonitor(allContactMen);
        this.allContactMen = allContactMen;
        if (allContactMen.size() == 0) {// 成功获取联系人，但数量为0，显示空布局
            setEmptyLayout();
        } else {
            findViewById(R.id.tv_error).setVisibility(View.GONE);
            changeChooseStatus();
            mListView.bindDatas(allContactMen);
        }
    }

    /**
     * 过滤指定人员
     */
    private void filterMen(List<ContactMan> allContactMen) {
        if (allContactMen == null || allContactMen.size() == 0 || filterMen == null || filterMen.size() == 0) {
            return;
        }
        filterChoosedMen(allContactMen, filterMen);
    }

    /**
     * 过滤方法
     */
    private void filterChoosedMen(List<ContactMan> allContactMen, List<ContactMan> filterMen) {
        if (allContactMen == null || filterMen == null) {
            return;
        }

        List<ContactMan> filteredContacts = new ArrayList<ContactMan>();
        for (int i = 0; i < allContactMen.size(); i++) {
            ContactMan sourceContact = allContactMen.get(i);
            for (int j = 0; j < filterMen.size(); j++) {
                ContactMan filterContact = filterMen.get(j);
                String filterContactName = filterContact.getName();
                if ((sourceContact.getUserid() == filterContact.getUserid()) || ((filterContactName != null) && filterContactName.equals(sourceContact.getName()))) {
                    filteredContacts.add(filterContact);
                }
            }
        }

        allContactMen.removeAll(filteredContacts);
    }

    /**
     * 根据intent指令决定是否只保留班长
     */
    private void onlySaveMonitor(List<ContactMan> allContactMen) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(KEY_ONLY_MONITOR)) {
            boolean onlyMonitor = bundle.getBoolean(KEY_ONLY_MONITOR, false);
            if (onlyMonitor) {
                onlyMonitor(allContactMen);
            }
        }
    }

    /**
     * 过滤不是班长的人
     */
    private void onlyMonitor(List<ContactMan> allContactMen) {
        for (int i = 0; i < allContactMen.size(); i++) {
            if (!allContactMen.get(i).isLeader()) {
                allContactMen.remove(i);
                i--;
            }
        }
    }

    private void handleChoosedItem(IndexEntity indexEntity) {
        ContactMan contactMan = (ContactMan) indexEntity;
        contactMan.setHasChoosed(!contactMan.isHasChoosed());
        addChoosedList(contactMan, mode);
        if (mode == SINGLE_CHOICE) {
            onOkButtonClicked(null);
        } else {
            updateListView(contactMan);
            updateChoosedBar(contactMan);
        }
    }

    /**
     * 更新底部，已选择人员条目
     */
    private void updateChoosedBar(ContactMan contactMan) {
        StringBuilder nameList = new StringBuilder();
        String singleName = contactMan.getName() + "、";
        String displayTxt = tvChoosedMenBar.getText().toString();
        if (displayTxt.contains(singleName)) {// 如果已选，把名字去掉
            displayTxt = displayTxt.replaceAll(singleName, "");
        } else {
            displayTxt += contactMan.getName() + "、";
        }
        nameList.append(displayTxt);
        tvChoosedMenBar.setText(nameList);
    }

    private void initView() {
        this.mListView = (IndexableStickyListView) findViewById(R.id.indexListView);
        EditText EditText = (EditText) findViewById(R.id.edit_search);
        EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mListView.searchTextChange(s.toString());
            }
        });
        this.tvChoosedMenBar = (TextView) findViewById(R.id.tv_choosedManList);
        tvChoosedMenBar.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.adapter = new ContactAdapter(this);
        mListView.setAdapter(adapter);

        mListView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {
            @Override
            public void onItemClick(View v, IndexEntity indexEntity) { // 没有position因为，title也算如position，所以返回position并没有什么用
                handleChoosedItem(indexEntity);
            }
        });
        // 刚进来时，ListView和底部显示上次选择的名单
        if (hasChoosedMen != null) {
            for (ContactMan contactMan : hasChoosedMen) {
                updateChoosedBar(contactMan);
            }
        }
    }

    private void addChoosedList(ContactMan contactMan, int mode) {
        if (hasChoosedMen == null) {
            hasChoosedMen = new ArrayList<ContactMan>();
        }
        if (hasChoosedMen.contains(contactMan)) {// 如果已选，把它从列表里去掉
            hasChoosedMen.remove(contactMan);
        } else if (mode == SINGLE_CHOICE) {
            hasChoosedMen.clear();
            hasChoosedMen.add(contactMan);
        } else {
            hasChoosedMen.add(contactMan);
        }
    }

    private void updateListView(ContactMan contactMan) {
        allContactMen.set(allContactMen.indexOf(contactMan), contactMan);
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据上次已选名单，改变联系人列表中的选择状态
     */
    private void changeChooseStatus() {
        if (this.hasChoosedMen == null || this.allContactMen == null) {
            return;
        }
        for (ContactMan contactMan : hasChoosedMen) {
            contactMan.setHasChoosed(true);
            if (allContactMen.contains(contactMan)) {
                allContactMen.set(allContactMen.indexOf(contactMan), contactMan);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey(KEY_FILTER_MEN)) {
            this.filterMen = (List<ContactMan>) bundle.getSerializable(KEY_FILTER_MEN);
        }
        if (bundle.containsKey(KEY_TITLE)) {
            this.titleResId = bundle.getInt(KEY_TITLE) == 0 ? -1 : bundle.getInt(KEY_TITLE);
        }
        if (bundle.containsKey(KEY_CHOOSED_MEN)) {
            this.hasChoosedMen = (List<ContactMan>) bundle.getSerializable(KEY_CHOOSED_MEN);
        }
        if (bundle.containsKey(KEY_CHOOSE_MODE)) {
            this.mode = bundle.getInt(KEY_CHOOSE_MODE) == 0 ? MULTIPLE_CHOICE : bundle.getInt(KEY_CHOOSE_MODE); // 默认多选
        }
        if (bundle.containsKey(KEY_FILTER_RULE)) {
            this.params = (HashMap<String, String>) bundle.getSerializable(KEY_FILTER_RULE);
        }
        if (bundle.containsKey(KEY_CORE_NAME)) {
            String name = bundle.getString(KEY_CORE_NAME);
            try {
                this.contactManCore = (AContactManCore) Class.forName(name).newInstance();
                contactManCore.register(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bundle.containsKey(KEY_CHOOSE_MEN_REQUEST_TYPE)) {
        	this.requestType = KEY_CHOOSE_MEN_REQUEST_TYPE;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        this.allContactMen = contactManCore.getContactMen(params,requestType);
        if (allContactMen != null) {
            updateContactMenList(allContactMen);
        }
    }

    private void initTitle() {
        if (titleResId == -1) {
            titleResId = R.string.title_contact_man;
        }
        ((TextView) findViewById(R.id.tv_title)).setText(getResources().getString(titleResId));
    }

    private void finishActivity() {
        contactManCore.beforeFinishActivity();
        finish();
    }
}
