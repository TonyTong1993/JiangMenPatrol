package com.ecity.cswatersupply.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.checkitem.AudiosDetailListAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.menu.EmptyReportOperator1;
import com.ecity.cswatersupply.menu.LinePartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.menu.PointPartInPlaceFeedbackOperator;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.Group;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskLinePart;
import com.ecity.cswatersupply.model.planningTask.Z3PlanTaskPointPart;
import com.ecity.cswatersupply.project.FuZhouPatrolModuleConfig;
import com.ecity.cswatersupply.ui.activities.planningtask.InspectSelectActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.esri.core.map.Graphic;
import com.z3app.android.util.PreferencesUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * @author SunShan'ai 通用上报主要逻辑界面
 */
public class CustomReportActivity1 extends BaseActivity {
    /**
     * 是否是从巡检任务或养护任务来的
     */
    public static final String INTENT_KEY_FROM_TASK_FUNC = "INTENT_KEY_FROM_TASK_FUNC";

    protected InspectItem mInspectItemParent;
    protected List<InspectItem> mInspectItems;
    protected int mMulltiChildId;
    protected ACommonReportOperator1 mCommonReportOperator;
    protected List<InspectItem> mergedInspectItems;
    /**
     * 筛选后的结果
     */
    protected ArrayList<InspectItem> selectedItems;

    protected LinearLayout layoutContainerView;
    private CustomTitleView mViewTitle;
    private String mTitle;
    private Button btnFilterInspectItems;
    private int mBottomSingBtnTxtResId = R.string.event_report_submit; // 默认是上报
    private int bottomBtnMode = CustomViewInflater.SINGLE_BTN;
    private Graphic currentSelectDevice;
    private String clickedGeometryId;
    //所有的组的集合，String = name，boolean 是否显示
    private List<Group> allGroups = new ArrayList<Group>();

    /**
     * 是否是从巡检任务或养护任务来的
     */
    protected boolean isFromTaskFunction;
    protected boolean isFromEarthquakeFunc = false;
    protected boolean isHasGroupInspectItem = false;
    protected CustomViewInflater mCustomViewInflater;
    private WorkOrder currentWorkOrder;
    private LinearLayout container, tableTitleLayout;
    private ListView listView;
    private boolean isFromPumpDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_report);
        this.setTheme(android.R.style.Theme_Holo_Light);
        EventBusUtil.register(this);
        initData();
        initUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        initializeGlobal();
        releaseResources();
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void initScreenButton() {
        btnFilterInspectItems = (Button) findViewById(R.id.btn_screen);
        if (isFromTaskFunction ) {
            if(mInspectItems.size() <= 1 || !InspectItemUtil.hasGroupItem(mInspectItems)) {
                if(null != btnFilterInspectItems) {
                    btnFilterInspectItems.setVisibility(View.GONE);
                }
            } else {
                btnFilterInspectItems.setVisibility(View.VISIBLE);
                btnFilterInspectItems.setOnClickListener(new ScreenGroupOnClickListener());
            }
        }
    }

    private void initBottomToolBar() {
        switch (bottomBtnMode) {
            case CustomViewInflater.NO_BTN:
                break;
            case CustomViewInflater.SINGLE_BTN:
                initSingleToolbar(R.drawable.flow_report, mBottomSingBtnTxtResId);
                break;
            case CustomViewInflater.TWO_BTNS:
                initTwoBtnToolBar();
                break;
        }
    }

    private void initTwoBtnToolBar() {
        RelativeLayout rlBtns = (RelativeLayout) findViewById(R.id.include_bottom_btns);
        rlBtns.setVisibility(View.VISIBLE);
        // 如果不传，按默认值
        String leftBtnText = getIntent().getExtras().getString(CustomViewInflater.BOTTOM_TWOBTN_NEGATIVE_TXT);
        String rightBtnText = getIntent().getExtras().getString(CustomViewInflater.BOTTOM_TWOBTN_POSITIVE_TXT);
        if (leftBtnText != null) {
            ((TextView) findViewById(R.id.bt_negative)).setText(leftBtnText);
        }
        if (rightBtnText != null) {
            ((TextView) findViewById(R.id.bt_positive)).setText(rightBtnText);
        }
    }

    /**
     * 界面下方的工具栏，用户可继承修改
     *
     * @param imgResId
     * @param strResId
     */
    protected void initSingleToolbar(int imgResId, int strResId) {
        //        mRlToolbarBottom = (RelativeLayout) findViewById(R.id.toolbar_bottom);
        //        if (-1 == imgResId || -1 == strResId) {
        //            mRlToolbarBottom.setVisibility(View.GONE);
        //            return;
        //        } else {
        //            mRlToolbarBottom.setVisibility(View.VISIBLE);
        //        }
        //        mTvActions = (TextView) findViewById(R.id.tv_actions);
        //        mIvActions = (ImageView) findViewById(R.id.iv_actions);
        //        mTvActions.setText(strResId);
        //        mIvActions.setBackgroundResource(imgResId);

        mViewTitle.setBtnStyle(BtnStyle.RIGHT_ACTION);
        mViewTitle.setRightActionBtnText(strResId);
    }

    public void onActionButtonClicked(View view) {
        submitInfo(view);
    }

    public void onBackButtonClicked(View view) {
        if (null != currentWorkOrder) {
            EventBusUtil.post(new UIEvent(UIEventStatus.WORKORDER_OPERATE_BACK, null));
        }
        notifyBackEvent(0);
    }

    public void onCancelButtonClicked(View view) {
        if ((null != mCommonReportOperator) && !ListUtil.isEmpty(mInspectItems)) {
            mCommonReportOperator.submit2Server(mInspectItems, false);
        }
    }

    public void onOkButtonClicked(View view) {
        if (!isInspectItemContentValid()) {
            return;
        }

        if (null != mCommonReportOperator) {
            mCommonReportOperator.submit2Server(mInspectItems, true);
        }
    }

    public void submitInfo(View view) {
        if (isFromEarthquakeFunc) {
            submitInfo(mInspectItems);
        } else {
            if (!ListUtil.isEmpty(selectedItems)) {
                submitInfo(selectedItems);
            } else {
                submitInfo(mergedInspectItems);
            }
        }
    }

    protected void submitInfo(List<InspectItem> items) {
        if (InspectItemUtil.hasEmptyItem(items)) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isInspectItemContentValid()) {
            return;
        }

        if (!isGeometryInspectItemValid()) {
            Toast.makeText(this, R.string.event_no_position, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isCorsInspectItemValid()) {
            Toast.makeText(this, R.string.event_no_location, Toast.LENGTH_SHORT).show();
            return;
        }

        if (null != mCommonReportOperator) {
            SessionManager.currentInspectItems = items;
            mCommonReportOperator.submit2Server(items);
        }
    }

    private boolean isCorsInspectItemValid() {
        for (InspectItem item : mInspectItems) {
            if (item.getType() == EInspectItemType.CORS) {
                try {
                    String value = item.getValue();
                    JSONObject myJsonObject = new JSONObject(value);
                    String lon = myJsonObject.getString("lon");
                    String lat = myJsonObject.getString("lat");
                    if ("".equals(lon) || "".equals(lat)) {
                        return false;
                    }
                } catch (JSONException e) {
                }
            }
        }
        return true;
    }

    private boolean isGeometryInspectItemValid() {
        for (InspectItem item : mInspectItems) {
            if (item.getType() == EInspectItemType.GEOMETRY) {
                String value = item.getValue();
                String strArray[] = value.split(";");
                if (strArray.length > 1) {
                    if ("".equals(strArray[0]) || "".equals(strArray[1])) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }
        notifyBackEvent(keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CustomViewInflater.GROUPS) {
            if (null != data) {
                //得到所有的组  包括被选择的和没有被选择的
                @SuppressWarnings("unchecked") ArrayList<Group> selectedAllGroups = (ArrayList<Group>) data.getExtras().getSerializable(CustomViewInflater.INTENT_GROUPS);
                showFilteredInspectItems(selectedAllGroups);
            }
        } else {
            mCustomViewInflater.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initializeGlobal() {
        MediaCacheManager.clearImg();
        MediaCacheManager.clearVdo();
    }

    private void releaseResources() {
        if (mCustomViewInflater != null) {
            mCustomViewInflater.stopPlay();
        }
        CustomViewInflater.releaseResources();
    }

    protected void setCustomReportActivity(CustomReportActivity1 activity) {
        if (null != mCommonReportOperator) {
            mCommonReportOperator.setCustomActivity(activity);
        }
    }

    private void notifyBackEvent(int keyCode) {
        SessionManager.currentPointPartIntMapOpretor = null;
        if (null != mCommonReportOperator) {
            if (keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN) {
                mCommonReportOperator.notifyBackEvent(this);
            }
        } else {
            finish();
        }
    }

    @SuppressWarnings("unchecked")
    private void initData(Intent intent) {
        if (intent == null) {
            return;
        }
        String classReportName = null;
        int titleResId = R.string.info_report;
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            return;
        }

        isFromPumpDetail = getIntent().getExtras().getBoolean(PumpDetailActivity.INTENT_KEY_FROM_PUMP_DETAIL_FUNC);
        currentWorkOrder = (WorkOrder) bundle.getSerializable(WorkOrder.KEY_SERIAL);
        if (bundle.containsKey(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY)) {
            mMulltiChildId = bundle.getInt(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY, -1);
        } else {
            mMulltiChildId = -1;
        }
        if (bundle.containsKey(CustomViewInflater.REPORT_TITLE)) {
            try {
                titleResId = bundle.getInt(CustomViewInflater.REPORT_TITLE);
                mTitle = getString(titleResId);
            } catch (Exception e) {
                mTitle = bundle.getString(CustomViewInflater.REPORT_TITLE);
            }
        }
        if (bundle.containsKey(CustomViewInflater.BOTTOM_TOOLBAR_MODE)) {
            bottomBtnMode = bundle.getInt(CustomViewInflater.BOTTOM_TOOLBAR_MODE);
        }
        if (bundle.containsKey(CustomViewInflater.BOTTOM_SINLEBTN_TXT)) {
            mBottomSingBtnTxtResId = bundle.getInt(CustomViewInflater.BOTTOM_SINLEBTN_TXT);
        }
        if (bundle.containsKey(CustomViewInflater.REPORT_COORDINATE)) {
            CustomViewInflater.mPatrolPosition = (PatrolPosition) bundle.getSerializable(CustomViewInflater.REPORT_COORDINATE);
        }
        if (bundle.containsKey(CustomViewInflater.REPORT_TITLE_PARENT)) {
            mInspectItemParent = (InspectItem) bundle.getSerializable(CustomViewInflater.REPORT_TITLE_PARENT);
            if (null != mInspectItemParent) {
                if (!StringUtil.isEmpty(mInspectItemParent.getAlias())) {
                    mTitle = mInspectItemParent.getAlias();
                } else {
                    mTitle = mInspectItemParent.getName();
                }
            }
        }
        if (bundle.containsKey(CustomViewInflater.REPORT_COMFROM)) {
            classReportName = bundle.getString(CustomViewInflater.REPORT_COMFROM);
        }
        if (bundle.containsKey(CustomViewInflater.EVENT_LEAK_CURRENT_SELECT_DEVICE)) {
            currentSelectDevice = (Graphic) bundle.getSerializable(CustomViewInflater.EVENT_LEAK_CURRENT_SELECT_DEVICE);
        }

        if (bundle.containsKey(CustomViewInflater.INTENT_KEY_POINT_PART)) {
            clickedGeometryId = ((Z3PlanTaskPointPart) getIntent().getSerializableExtra(PointPartInPlaceFeedbackOperator.INTENT_KEY_POINT_PART)).getGid() + "";
        } else if (bundle.containsKey(CustomViewInflater.INTENT_KEY_LINE_PART)) {
            clickedGeometryId = ((Z3PlanTaskLinePart) getIntent().getSerializableExtra(LinePartInPlaceFeedbackOperator.INTENT_KEY_LINE_PART)).getGid() + "";
        }

        if (StringUtil.isBlank(classReportName)) {
            mCommonReportOperator = new EmptyReportOperator1();
        } else {
            try {
                mCommonReportOperator = (ACommonReportOperator1) Class.forName(classReportName).newInstance();
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }

        setCustomReportActivity(this);

        if (bundle.containsKey(CustomViewInflater.REPORT_CHILD_ITEMS)) {
            mInspectItems = (List<InspectItem>) bundle.getSerializable(CustomViewInflater.REPORT_CHILD_ITEMS);
        } else {
            mInspectItems = mCommonReportOperator.getDataSource();
        }

        isFromTaskFunction = bundle.getBoolean(INTENT_KEY_FROM_TASK_FUNC, false);
    }

    private void initData() {
        initData(getIntent());
    }

    private void initUI() {
        initTitle();
        initBottomToolBar();
        initInspectItems();
    }

    protected void initTitle() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_report_event);
        mViewTitle.setTitleText(mTitle);
    }

    public LinearLayout getLinearLayout() {
        container = (LinearLayout) findViewById(R.id.ll_container_listview);
        return container;
    }

    public LinearLayout getTableTitleLayout() {
        tableTitleLayout = (LinearLayout) findViewById(R.id.item_table_unfinish_title);
        return tableTitleLayout;
    }

    public ListView getListView() {
        listView = (ListView) findViewById(R.id.lv_table);
        return listView;
    }

    public String getCustomReportActivityTitle() {
        return mTitle;
    }

    public void fillDatas(List<InspectItem> inspectItems) {
        this.mInspectItems = inspectItems;
        initInspectItems();
    }

    public void fillDatas4Earthquake(List<InspectItem> inspectItems) {
        this.mInspectItems = inspectItems;
        this.isFromEarthquakeFunc = true;
        internalFillDatas(inspectItems);
    }

    protected void internalFillDatas(List<InspectItem> inspectItems) {
        if (null != layoutContainerView) {
            layoutContainerView.removeAllViews();
        }
        if (null == inspectItems) {
            return;
        }

        mCustomViewInflater = new CustomViewInflater(this);
        layoutContainerView = (LinearLayout) findViewById(R.id.ll_container);
        for (InspectItem item : inspectItems) {
            if (!item.isVisible() && (!item.getType().equals(EInspectItemType.GROUP))) {
                continue;
            }

            if (isFromTaskFunction) {
                addGroupTitleView(getLayoutInflater(), layoutContainerView, item);
            }

            if (!StringUtil.isBlank(clickedGeometryId) && !clickedGeometryId.equalsIgnoreCase(SessionManager.currentPointId)) {
                item.setValue("");
            }

            mCustomViewInflater.setCurrentWorkOrder(currentWorkOrder);
            if ((Constants.RELATIVE_DEVICE).equals(item.getName()) && (currentSelectDevice != null)) {
                setDeviceItemValue(inspectItems, currentSelectDevice);
            }

            if (isFromPumpDetail) {
                if (item.getType() == EInspectItemType.TEXT) {
                    addGroupTitleViewForPump(getLayoutInflater(), layoutContainerView, item);
                    continue;
                }
            }

            layoutContainerView.addView(mCustomViewInflater.inflate(item));
        }

        if (!StringUtil.isBlank(clickedGeometryId)) {
            SessionManager.currentPointId = clickedGeometryId;
            clickedGeometryId = null;
        }
    }

    public void onEventMainThread(ResponseEvent event) {

        switch (event.getId()) {
            case ResponseEventStatus.FILE_OPERATION_DOWNLOAD_FINISH:
                LoadingDialogUtil.dismiss();
                String path = event.getData() + "";
                String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                PreferencesUtil.putString(getApplicationContext(), fileName, path);
                int position = getIndex(mCustomViewInflater.getAudiomodels(), fileName);
                AudiosDetailListAdapter.getAudioRecorder2Mp3().Play(mCustomViewInflater.getAudiomodels().get(position), position);
                break;
            default:
                break;
        }
    }

    public int getIndex(List<AudioModel> audioModels, String fileName) {

        int position = -1;
        for (int i = 0; i < audioModels.size(); i++) {
            if (fileName.equals(audioModels.get(i).getName())) {
                position = i;
            }
        }
        return position;
    }

    protected boolean isInspectItemContentValid() {
        if (ListUtil.isEmpty(mInspectItems)) {
            return false;
        }

        if (InspectItemUtil.hasEmptyItem(mInspectItems)) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected void initInspectItems() {
        if (ListUtil.isEmpty(mInspectItems)) {
            return;
        }

        initScreenButton();
        isHasGroupInspectItem = getIntent().getExtras().getBoolean(CustomViewInflater.KEY_IS_EARTHQUAKE_INSPECTITEMS);
        if (!isHasGroupInspectItem) {
            mergedInspectItems = mergeInspectItems(mInspectItems);
            internalFillDatas(mergedInspectItems);
        } else {
            internalFillDatas(mInspectItems);
        }
    }

    private List<InspectItem> mergeInspectItems(List<InspectItem> inspectItem) {
        List<InspectItem> allInspectItems = new ArrayList<InspectItem>();
        mergeInspectItems(inspectItem, allInspectItems);

        return allInspectItems;
    }

    private void mergeInspectItems(List<InspectItem> items, List<InspectItem> allInspectItems) {
        for (InspectItem item : items) {
            if (item.getType() == EInspectItemType.GROUP) {
                if (isFromPumpDetail) {
                    item.setType(EInspectItemType.TEXT);
                    allInspectItems.add(item);
                }

                //得到所有的分组，设置为显示
                Group group = new Group(item.getName(), item.getAlias(), true, new ArrayList<Group>());
                if (!isGroupItems(group)) {
                    allGroups.add(group);
                }
                mergeInspectItems(item.getChilds(), allInspectItems);
            } else {
                allInspectItems.add(item);
            }
        }
    }

    private boolean isGroupItems(Group group) {
        if (allGroups.size() == 0) {
            return false;
        }

        for (Group tmpGroup : allGroups) {
            if (group.getName().contains(tmpGroup.getName())) {
                tmpGroup.getGroups().add(group);
                return true;
            }
        }

        return false;
    }


    private class ScreenGroupOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (ListUtil.isEmpty(allGroups)) {
                ToastUtil.showLong(R.string.event_report_no_inspectitem);
                return;
            }
            Intent intent = new Intent(CustomReportActivity1.this, InspectSelectActivity.class);
            Bundle attrBundle = new Bundle();
            attrBundle.putSerializable("groups", (Serializable) allGroups);
            intent.putExtras(attrBundle);
            startActivityForResult(intent, CustomViewInflater.GROUPS);
        }
    }

    public void showFilteredInspectItems(List<Group> selectedAllGroups) {
        if (!ListUtil.isEmpty(selectedAllGroups)) {
            selectedItems = new ArrayList<InspectItem>();
            getInspectsContainGroup(mInspectItems, selectedAllGroups);
            internalFillDatas(selectedItems);
        } else {
            internalFillDatas(null);
        }
    }

    //得到筛选之后的可以显示的inspect item
    private void getInspectsContainGroup(List<InspectItem> inspects, List<Group> groups) {
        for (int i = 0; i < inspects.size(); i++) {
            InspectItem inspect = inspects.get(i);
            if (inspect.getType() == EInspectItemType.GROUP) {
                for (int j = 0; j < groups.size(); j++) {
                    if (inspect.getName().equalsIgnoreCase(groups.get(j).getName()) && groups.get(j).isVisible()) {
                        if (!ListUtil.isEmpty(inspect.getChilds())) {
                            //包含2级
                            for (int w = 0; w < inspect.getChilds().size(); w++) {
                                InspectItem inspectItem = inspect.getChilds().get(w);
                                if (inspectItem.getType() == EInspectItemType.GROUP) {
                                    if (!ListUtil.isEmpty(inspectItem.getChilds())) {
                                        for (int k = 0; k < inspectItem.getChilds().size(); k++) {
                                            if (groups.get(j).getGroups().get(w).isVisible()) {
                                                selectedItems.add(inspectItem.getChilds().get(k));
                                            }
                                        }
                                    }
                                } else {
                                    //只包含一级的分类
                                    selectedItems.add(inspectItem);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    protected void addGroupTitleView(LayoutInflater inflater, LinearLayout containerView, InspectItem item) {
        View view = inflater.inflate(R.layout.custom_form_common_title, null);
        TextView tvCommomTitle = (TextView) view.findViewById(R.id.tv_item_common_title);
        String firstGroupName = getFirstGroupName(item, mInspectItems);
        tvCommomTitle.setText(firstGroupName);
        containerView.addView(view);
    }

    private void addGroupTitleViewForPump(LayoutInflater inflater, LinearLayout containerView, InspectItem item) {
        View view = inflater.inflate(R.layout.custom_form_common_title, null);
        TextView tvCommomTitle = (TextView) view.findViewById(R.id.tv_item_common_title);
        tvCommomTitle.setText(item.getAlias());
        containerView.addView(view);
    }

    protected String getFirstGroupName(InspectItem item, List<InspectItem> allInspectItems) {
        for (int i = 0; i < allInspectItems.size(); i++) {
            if (EInspectItemType.GROUP == allInspectItems.get(i).getType()) {
                List<InspectItem> secondInspectItems = allInspectItems.get(i).getChilds();
                for (int j = 0; j < secondInspectItems.size(); j++) {
                    if (EInspectItemType.GROUP == secondInspectItems.get(j).getType()) {
                        List<InspectItem> items = secondInspectItems.get(j).getChilds();
                        for (int k = 0; k < items.size(); k++) {
                            if (items.get(k).getAlias().equalsIgnoreCase(item.getAlias())) {
                                return allInspectItems.get(i).getAlias();
                            }
                        }
                    } else {
                        if (secondInspectItems.get(j).getAlias().equalsIgnoreCase(item.getAlias())) {
                            return allInspectItems.get(i).getAlias();
                        }
                    }
                }
            } else {
                if (allInspectItems.get(i).getAlias().equalsIgnoreCase(item.getAlias())) {
                    return allInspectItems.get(i).getAlias();
                }
            }
        }
        return null;
    }

    private void setDeviceItemValue(List<InspectItem> inspectItems, Graphic deviceGraphic) {
        for (InspectItem item : inspectItems) {
            if ((item.getType() != EInspectItemType.DEVICE) && (item.getType() != EInspectItemType.SELECTVALVE)) {
                continue;
            }

            String hiddenAttrPrefix = HostApplication.getApplication().getString(R.string.map_device_hidden_attr_prefix);
            Map<String, Object> attributes = deviceGraphic.getAttributes();
            JSONObject deviceAttrJson = new JSONObject();
            try {
                String key = "layerId";
                int layerId = (Integer) attributes.get(hiddenAttrPrefix + key);
                deviceAttrJson.put(key, layerId);
                String dname = QueryLayerIDs.getDnamebyLayerId(layerId);
                deviceAttrJson.put("dname", dname);
                key = "layerName";
                deviceAttrJson.put(key, attributes.get(hiddenAttrPrefix + key));
                key = "queryUrl";
                deviceAttrJson.put(key, attributes.get(hiddenAttrPrefix + key));
                deviceAttrJson.put("gid", attributes.get("gid"));
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }

            item.setValue(deviceAttrJson.toString());
        }
    }
}
