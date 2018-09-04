package com.ecity.cswatersupply.emergency.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.CustomGridViewAdapter;
import com.ecity.cswatersupply.adapter.ImageAdapter;
import com.ecity.cswatersupply.adapter.VideoRecordAdapter;
import com.ecity.cswatersupply.adapter.checkitem.AudiosDetailListAdapter;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemSelectValueAdapter;
import com.ecity.cswatersupply.emergency.menu.operator.EarthQuakeEmptyReportOperator;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.menu.map.ObtainDeviceOpratorXtd;
import com.ecity.cswatersupply.menu.map.ObtainLocationOpratorXtd;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.Group;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog;
import com.ecity.cswatersupply.ui.widght.DatetimePickerDialog.DatetimePickerCallback;
import com.ecity.cswatersupply.ui.widght.GridViewForScrollView;
import com.ecity.cswatersupply.ui.widght.ListViewForScrollView;
import com.ecity.cswatersupply.utils.CoordTransfer;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.ecity.cswatersupply.workorder.view.FullScreenPhotoActivity;
import com.ecity.medialibrary.activity.AlbumSelectActivity;
import com.ecity.medialibrary.activity.FileChooserActivity;
import com.ecity.medialibrary.activity.PhotoActivity;
import com.ecity.medialibrary.activity.PlayVideoActivity;
import com.ecity.medialibrary.activity.TakeVideoActivity;
import com.ecity.medialibrary.adapter.AudiosRecordAdapter;
import com.ecity.medialibrary.model.AudioModel;
import com.ecity.medialibrary.model.IAudioRecordCallback;
import com.ecity.medialibrary.model.VideoModel;
import com.ecity.medialibrary.utils.AudioRecordUtil;
import com.ecity.medialibrary.utils.FileSuffixUtils;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.ecity.medialibrary.utils.MediaHelper;
import com.ecity.medialibrary.utils.StartCameraUtil;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator;
import com.ecity.mobile.android.bdlbslibrary.BDGeoLocator.OnNotifyAddressInfo;
import com.ecity.mobile.android.bdlbslibrary.model.AddressInfo;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.service.PositionService;
import com.zzz.ecity.android.applibrary.view.ActionSheet;

/**
 * 灾情速报上报界面
 */
public class EarthQuakeBaseReportActivity extends BaseActivity {
    //标题tag
    public static final String REPORT_TITLE = "REPORT_TITLE";
    //event标识
    public static final String REPORT_COMFROM = "REPORT_COMFROM";
    //表单数据标识
    public static final String REPORT_CHILD_ITEMS = "REPORT_CHILD_ITEMS";
    //标题
    private String mTitle;
    private CustomTitleView mViewTitle;
    //容器
    private LinearLayout mLlContainer;
    //百度定位
    private BDGeoLocator mBDGeoLocator;
    private PatrolPosition mPatrolPosition;
    private EditText etLocationValue;
    private EditText etAddressValue;
    private CountDownHandler mCountDownHandler = new CountDownHandler();
    //媒体文件处理
    private CustomGridViewAdapter mImageAdapter;
    private VideoRecordAdapter mVideoAdapter;
    private AudiosRecordAdapter mAudioAdapter;
    private AudioRecordUtil audioRecordUtil;
    //启动相机拍照结果保存的文件名。
    private static String currentPhotoName;
    // 所有选择中的Images的路径（放在SD卡中的原始路径）
    private List<String> mImagesSdPaths = new ArrayList<String>();
    private List<String> mVideosSdPaths = new ArrayList<String>();
    // 存储所有的音频，视频，图片，附件等的路径（放在Z3SDK中）
    private List<String> mImagesZ3Paths = new ArrayList<String>();
    private List<String> mVideosZ3Paths = new ArrayList<String>();
    private static String mMediaVideoPath;
    private String mMediaImagePath;
    // 录音文件
    private ArrayList<AudioModel> mAudiomodels = new ArrayList<AudioModel>();
    private ArrayList<VideoModel> mVideomodels = new ArrayList<VideoModel>();
    private final CommonReportHandler commonReportHandler = new CommonReportHandler(this);
    //基础操作类
    private ACommonReportOperator1 mCommonReportOperator;
    //表单数据
    protected static List<InspectItem> mInspectItems;
    private List<InspectItem> allInspects;
    //所有的组的集合，String = name，boolean 是否显示
    private List<Group> allGroups = new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_ff_report);
        this.setTheme(android.R.style.Theme_Holo_Light);
        initData();
        initUI();
    }

    private void initUI() {
        initTitle();
        initInspectItems();
    }

    private void initInspectItems() {
        if (ListUtil.isEmpty(mInspectItems)) {
            return;
        }
        fillDatas(mInspectItems);
    }

    private void initTitle() {
        mViewTitle = (CustomTitleView) findViewById(R.id.view_title_report_event);
        mViewTitle.setTitleText(mTitle);
    }

    private void initData() {
        initData(getIntent());
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
        if (bundle.containsKey(REPORT_TITLE)) {
            titleResId = bundle.getInt(REPORT_TITLE);
            mTitle = getString(titleResId);
        }
        if (bundle.containsKey(REPORT_COMFROM)) {
            classReportName = bundle.getString(REPORT_COMFROM);
        }
        if (StringUtil.isBlank(classReportName)) {
            mCommonReportOperator = new EarthQuakeEmptyReportOperator();
        } else {
            try {
                mCommonReportOperator = (ACommonReportOperator1) Class.forName(classReportName).newInstance();
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
        setCustomReportActivity(this);

        if (bundle.containsKey(REPORT_CHILD_ITEMS)) {
            mInspectItems = (List<InspectItem>) bundle.getSerializable(REPORT_CHILD_ITEMS);
        } else {
            mInspectItems = mCommonReportOperator.getDataSource();
        }
        mMediaImagePath = FileUtil.getInstance(getApplicationContext()).getMediaPathforImage();
    }

    private void setCustomReportActivity(EarthQuakeBaseReportActivity earthQuakeBaseReportActivity) {
        if (null != mCommonReportOperator) {
//            mCommonReportOperator.setCustomActivity(earthQuakeBaseReportActivity);
        }
    }

    /*
     * 初始化表单数据
     */
    @SuppressLint("InflateParams")
    private void fillDatas(List<InspectItem> inspectItems) {
        if (null != mLlContainer) {
            mLlContainer.removeAllViews();
        }
        if (ListUtil.isEmpty(inspectItems)) {
            return;
        }
        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
        View view = null;
        boolean isTextNormal = false;
        boolean isDdlNormal = false;
        TextView tvCommomTitle = null;
        TextView tvStar = null;
        TextView tvTitle = null;
        TextView tvGroupLeft;
        TextView tvGroupRight;
        TextView tvGetInfo;
        TextView tvGetAddress;
        EditText etSpnValue;
        RadioGroup rgGroup = null;
        LinearLayout llCbContainer;
        Button btnSelect;
        ImageButton imgBtnZXing;
        ImageView ivSpnValue;
        Spinner spnSelect;
        Iterator<InspectItem> iterator = inspectItems.iterator();
        while (iterator.hasNext()) {
            InspectItem item = iterator.next();
            if (!item.isVisible()) {
                continue;
            }
            switch (item.getType()) {
                case TEXT:
                    isTextNormal = true;
                case TEXTEXT:
                    isTextNormal = isTextNormal;
                    if (isTextNormal) {
                        view = getLayoutInflater().inflate(R.layout.custom_form_item_text, null);
                    } else {
                        view = getLayoutInflater().inflate(R.layout.custom_form_item_text_long, null);
                    }
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    EditText etValue = (EditText) view.findViewById(R.id.et_item_value);
                    if (!StringUtil.isBlank(item.getValue())) {
                        etValue.setText(item.getValue());
                    } else if (!StringUtil.isBlank(item.getDefaultValue())) {
                        etValue.setText(item.getDefaultValue());
                    }

                    etValue.addTextChangedListener(new MyEditTextListener(item));
                    if (!item.isEdit()) {
                        etValue.setEnabled(false);
                    }
                    break;
                case DATE:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_select, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    btnSelect = (Button) view.findViewById(R.id.view_value);
                    if (!StringUtil.isBlank(item.getValue())) {
                        btnSelect.setText(item.getValue());
                    } else if (!StringUtil.isBlank(item.getDefaultValue())) {
                        btnSelect.setText(item.getDefaultValue());
                    }
                    btnSelect.setOnClickListener(new MyBtnSelectOnClickListener(item, EInspectItemType.DATE, btnSelect));
                    break;
                case GEOMETRY:
                    if (null == mBDGeoLocator) {
                        mBDGeoLocator = new BDGeoLocator(this.getApplication());
                    }
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_geometry_select, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    tvGetInfo = (TextView) view.findViewById(R.id.tv_getMapLocationInfo);
                    tvGetAddress = (TextView) view.findViewById(R.id.tv_getCurrentLocationInfo);
                    etLocationValue = (EditText) view.findViewById(R.id.et_location_value);
                    etAddressValue = (EditText) view.findViewById(R.id.et_address_value);
                    tvGetInfo.setOnClickListener(new MyBtnGetInfoOnClickListener());
                    tvGetAddress.setOnClickListener(new MyBtnGetInfoOnClickListener());
                    tvGetAddress.addTextChangedListener(new EditChangedListener(item));
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    if (!StringUtil.isBlank(item.getValue())) {
                        String value = item.getValue();
                        if (value.contains(";")) {
                            String[] xyAddress = value.split(";");
                            etLocationValue.setText(xyAddress[0]);
                            etAddressValue.setText(xyAddress[1]);
                        } else {
                            etLocationValue.setText(value);
                        }
                    } else if (!StringUtil.isBlank(item.getDefaultValue())) {
                        etLocationValue.setText(item.getDefaultValue());
                    }
                    if (null != mPatrolPosition) {
                        getMoreAddressInfoByBaiDu(mPatrolPosition);
                    }
                    item.setValue(etLocationValue.getText().toString());
                    break;
                case IMAGE:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_image_select, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }

                    ViewStub imgStub = (ViewStub) view.findViewById(R.id.viewStub_galley);
                    imgStub.inflate();
                    Gallery imgGallery = (Gallery) view.findViewById(R.id.gallery1);
                    GridViewForScrollView gridViewImage = (GridViewForScrollView) view.findViewById(R.id.gridviewImage);
                    inflateImage(gridViewImage, item, imgGallery);
                    break;
                case VIDEO:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_video_select, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    ViewStub videoStub = (ViewStub) view.findViewById(R.id.viewStub_galley);
                    videoStub.inflate();
                    Gallery videoGallery = (Gallery) view.findViewById(R.id.gallery1);
                    GridViewForScrollView gridViewVideo = (GridViewForScrollView) view.findViewById(R.id.gridviewVideo);
                    inflateVideo(gridViewVideo, item, videoGallery);
                    break;
                case AUDIO:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_audio_select, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    ListViewForScrollView lvAudio = (ListViewForScrollView) view.findViewById(R.id.lv_audios);
                    inflateAudio(lvAudio, item);
                    break;
                case RADIO:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_radiogroup, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    rgGroup = (RadioGroup) view.findViewById(R.id.rg_custom);
                    rgGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener(item, rgGroup));
                    break;
                case RADIOTXT:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_radiogroup_text, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    rgGroup = (RadioGroup) view.findViewById(R.id.rg_custom);
                    rgGroup.setOnCheckedChangeListener(new MyRadioGroupOnClickListener(item, rgGroup));
                    EditText etRadioValue = (EditText) view.findViewById(R.id.et_radio_text);
                    if (!StringUtil.isBlank(item.getValue())) {
                        etRadioValue.setText(getOnlyEtValue(item));
                    }
                    etRadioValue.addTextChangedListener(new MyEditTextListener(item));
                    break;
                case CHECKBOX:
                    view = getLayoutInflater().inflate(R.layout.custom_form_item_checkbox, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    llCbContainer = (LinearLayout) view.findViewById(R.id.ll_cb_container);

                    JSONArray jsonObj = null;
                    try {
                        jsonObj = new JSONArray(item.getSelectValues());
                    } catch (JSONException e) {
                        LogUtil.e(this, e);
                    }
                    List<InspectItemSelectValue> selectLists = InspectItemSelectValueAdapter.adapt(jsonObj);
                    for (int i = 0; i < selectLists.size(); i++) {
                        CheckBox checkBox = new CheckBox(EarthQuakeBaseReportActivity.this);
                        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        checkBox.setTextColor(getResources().getColor(R.color.txt_black_normal));
                        checkBox.setText(selectLists.get(i).name);
                        checkBox.setTag(selectLists.get(i).gid);
                        if (!StringUtil.isBlank(item.getValue()) && item.getValue().contains(selectLists.get(i).gid)) {
                            checkBox.setChecked(true);
                        }
                        checkBox.setOnCheckedChangeListener(new MyCheckBoxOnClickListener(item, checkBox));
                        llCbContainer.addView(checkBox);
                    }

                    break;
                case DROPDOWNLIST:
                    isDdlNormal = true;
                case DROPDOWNLISTEXT:
                    isDdlNormal = isDdlNormal;

                    view = getLayoutInflater().inflate(R.layout.custom_form_item_spinner, null);
                    tvStar = (TextView) view.findViewById(R.id.tv_item_star);
                    if (!item.isRequired()) {
                        tvStar.setVisibility(View.GONE);
                    }
                    tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
                    if (!StringUtil.isBlank(item.getAlias())) {
                        tvTitle.setText(item.getAlias());
                    } else {
                        tvTitle.setText(item.getName());
                    }
                    spnSelect = (Spinner) view.findViewById(R.id.spn_value);
                    etSpnValue = (EditText) view.findViewById(R.id.et_spn_value);
                    etSpnValue.setVisibility(View.GONE);
                    ivSpnValue = (ImageView) view.findViewById(R.id.iv_spn_value);
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(item.getSelectValues());
                    } catch (JSONException e) {
                        LogUtil.e(this, e);
                    }

                    List<InspectItemSelectValue> selectValueLists = InspectItemSelectValueAdapter.adapt(jsonArray);
                    String[] spnDatas = {};

                    if (!isDdlNormal) {
                        if (!ListUtil.isEmpty(selectValueLists)) {
                            spnDatas = new String[selectValueLists.size() + 1];
                            for (int i = 0; i < selectValueLists.size(); i++) {
                                spnDatas[i] = selectValueLists.get(i).name;
                            }
                            spnDatas[selectValueLists.size()] = getString(R.string.event_report_other);
                            if (!item.getValue().isEmpty()) {
                                etSpnValue.setText(item.getValue());
                                etSpnValue.setVisibility(View.VISIBLE);
                                spnSelect.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (!ListUtil.isEmpty(selectValueLists)) {
                            spnDatas = new String[selectValueLists.size()];
                            for (int i = 0; i < selectValueLists.size(); i++) {
                                spnDatas[i] = selectValueLists.get(i).name;
                            }
                            if (!item.getValue().isEmpty()) {
                                for (int i = 1; i < selectValueLists.size(); i++) {
                                    if (selectValueLists.get(i).gid.equals(item.getValue())) {
                                        spnSelect.setSelection(i);
                                    }
                                }
                            }
                        }
                    }
                    ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(EarthQuakeBaseReportActivity.this, R.layout.custom_form_item_spinner_textview, spnDatas);
                    spnAdapter.setDropDownViewResource(R.layout.custom_form_item_spinner_dropdown);
                    spnSelect.setPrompt(getString(R.string.please_select));
                    spnSelect.setAdapter(spnAdapter);
                    spnSelect.setOnItemSelectedListener(new MySpinnerOnItemSelectedClick(item, spnSelect, etSpnValue, isDdlNormal));
                    ivSpnValue.setOnClickListener(new MyImgSpnViewOnClickListener(spnSelect));
                    etSpnValue.addTextChangedListener(new MyEditTextListener(item));
                    break;
                    
                default:
                    break;
            }
        }
    }

    /**
     * 输入框的监听
     * @author ml
     *
     */
    private class MyEditTextListener implements TextWatcher {
        private InspectItem item;

        public MyEditTextListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no logic to do.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newValue = s.toString();
            updateRadioTextValue(item, newValue, false);
        }
    }

    private void updateRadioTextValue(InspectItem item, String newValue, boolean replaceFirstPart) {
        if (item.getValue() == null) {
            item.setValue("");
        }
        if (item.getType() == EInspectItemType.RADIOTXT) {
            String oldValue = item.getValue();
            int index = oldValue.indexOf("&");
            String firstPart = null;
            String secondPart = null;
            if (index == -1) {
                firstPart = oldValue;
                secondPart = "";
            } else {
                firstPart = oldValue.substring(0, index);
                secondPart = oldValue.substring(index + 1);
            }
            if (replaceFirstPart) {
                item.setValue(newValue + "&" + secondPart);
            } else {
                item.setValue(firstPart + "&" + newValue);
            }
        } else {
            item.setValue(newValue);
        }
    }

    private class MyBtnSelectOnClickListener implements OnClickListener {
        private Button btn;
        private InspectItem item;
        private EInspectItemType type;
        private DatetimePickerDialog timeDialog;

        public MyBtnSelectOnClickListener(InspectItem item, EInspectItemType type, Button btn) {
            this.item = item;
            this.type = type;
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case DATE:
                    timeDialog = new DatetimePickerDialog(EarthQuakeBaseReportActivity.this, AlertDialog.THEME_HOLO_LIGHT, new MyDateTimeCallBack(item, btn));
                    LayoutParams attributes = timeDialog.getWindow().getAttributes();
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    attributes.width = (int) (metrics.widthPixels * 0.9);
                    attributes.height = (int) (metrics.heightPixels * 0.6);
                    attributes.flags = LayoutParams.FLAG_DIM_BEHIND;
                    attributes.dimAmount = 0.5f;
                    timeDialog.getWindow().setAttributes(attributes);

                    timeDialog.show();
                    break;
                default:
                    break;
            }
        }
    }

    public class MyDateTimeCallBack implements DatetimePickerCallback {
        private InspectItem item;
        private Button btn;

        public MyDateTimeCallBack(InspectItem item, Button btn) {
            this.item = item;
            this.btn = btn;
        }

        @Override
        public void OnOK(String input) {
            item.setValue(input);
            btn.setText(item.getValue());
        }
    }

    /**
     * 获取定位信息
     * @author xuxu
     *
     */
    private class MyBtnGetInfoOnClickListener implements OnClickListener {
        Intent intent;
        Bundle bundle;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_getMapLocationInfo:
                    intent = new Intent(EarthQuakeBaseReportActivity.this, MapActivity.class);
                    bundle = new Bundle();
                    bundle.putString(MapActivity.MAP_OPERATOR, ObtainLocationOpratorXtd.class.getName());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RequestCode.REQUEST_POSITION);
                    break;
                case R.id.tv_getCurrentLocationInfo:
                    if (null != PositionService.getLastLocation()) {
                        PatrolPosition mPatrolPosition = new PatrolPosition(true, SessionManager.currentLocation.getLatitude(), SessionManager.currentLocation.getLongitude(), "");
                        getMoreAddressInfoByBaiDu(mPatrolPosition);
                    } else {
                        Toast.makeText(EarthQuakeBaseReportActivity.this, R.string.event_reprot_hint_address_no_current_location, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tv_getDeviceInfo:
                    intent = new Intent(EarthQuakeBaseReportActivity.this, MapActivity.class);
                    bundle = new Bundle();
                    bundle.putString(MapActivity.DEVICE_OPERATOR, ObtainDeviceOpratorXtd.class.getName());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RequestCode.REQUEST_DEVICE);
                    break;
                default:
                    break;
            }
        }
    }

    private void getMoreAddressInfoByBaiDu(PatrolPosition mPatrolPosition) {
        etAddressValue.setEnabled(true);
        if (null == mBDGeoLocator) {
            return;
        }
        LoadingDialogUtil.show(this, R.string.event_report_hint_address_loading);
        if (null != mPatrolPosition) {
            try {
                if (mPatrolPosition.isLatLon) {
                    double[] mercatorxy = null;
                    mercatorxy = CoordTransfer.transToLocal(mPatrolPosition.x, mPatrolPosition.y);
                    if (null != mercatorxy && 2 == mercatorxy.length) {
                        etLocationValue.setText(mercatorxy[0] + "," + mercatorxy[1]);
                        etAddressValue.setText("");
                    }
                    if (!StringUtil.isBlank(mPatrolPosition.placeName)) {
                        etAddressValue.setText(mPatrolPosition.placeName);
                    } else {
                        mBDGeoLocator.startGetAddressInfoByXY(mPatrolPosition.y, mPatrolPosition.x, new MyOnNotifyAddressInfoCallback());
                        mCountDownHandler.postDelayed(new CountDownThread(), 10000);
                    }
                } else {
                    double[] lngLat = null;
                    etLocationValue.setText(mPatrolPosition.x + "," + mPatrolPosition.y);
                    etAddressValue.setText("");
                    lngLat = CoordTransfer.transToLatlon(mPatrolPosition.x, mPatrolPosition.y);
                    if (null != lngLat && 2 == lngLat.length) {
                        if (!StringUtil.isBlank(mPatrolPosition.placeName)) {
                            etAddressValue.setText(mPatrolPosition.placeName);
                        } else {
                            mBDGeoLocator.startGetAddressInfoByXY(lngLat[0], lngLat[1], new MyOnNotifyAddressInfoCallback());
                            mCountDownHandler.postDelayed(new CountDownThread(), 10000);
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.e(this, e);
            }
        }
    }

    private static class CountDownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class CountDownThread implements Runnable {

        @Override
        public void run() {
            LoadingDialogUtil.dismiss();
            if (StringUtil.isBlank(etAddressValue.getText().toString())) {
                Toast.makeText(EarthQuakeBaseReportActivity.this, R.string.event_report_hint_address_loading_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyOnNotifyAddressInfoCallback implements OnNotifyAddressInfo {

        @Override
        public void getAddressInfo(boolean arg0, int arg1, String arg2, List<AddressInfo> arg3) {
            LoadingDialogUtil.dismiss();
            if (null != etAddressValue && null != etLocationValue && !ListUtil.isEmpty(arg3)) {
                setLocationAddressValue(etLocationValue.getText().toString(), arg3.get(0).getAddrStr());
            } else {
                ToastUtil.showMessage(EarthQuakeBaseReportActivity.this, R.string.event_report_hint_address_loading_fail, 0);
            }
        }
    }

    private void setLocationAddressValue(String location, String address) {
        if (StringUtil.isBlank(location)) {
            return;
        }
        if (!StringUtil.isBlank(address)) {
            setLocationValue(location + ";" + address);
            etLocationValue.setText(location);
            etAddressValue.setText(address);
        } else {
            setLocationValue(location);
            etLocationValue.setText(location);
        }
    }

    private void setLocationValue(String location) {
        Iterator<InspectItem> iterator = mInspectItems.iterator();
        while (iterator.hasNext()) {
            InspectItem item = iterator.next();
            if (item.getType() == EInspectItemType.GEOMETRY || item.getType() == EInspectItemType.GEOMETRY_AREA) {
                item.setValue(location);
                break;
            } else {
                // TODO: no logic to do.
            }
        }
    }

    private class EditChangedListener implements TextWatcher {
        private InspectItem inspectItem;

        public EditChangedListener(InspectItem inspectItem) {
            super();
            this.inspectItem = inspectItem;
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            String value = inspectItem.getValue();
            String[] array = value.split(";");
            value = array[0] + ";" + arg0.toString();
            setLocationValue(value);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }

    /**
     * 处理图片的显示
     * @param gridViewImage
     * @param item
     * @param gallery
     */
    private void inflateImage(GridViewForScrollView gridViewImage, InspectItem item, Gallery gallery) {
        if (item.isEdit()) {
            gridViewImage.setVisibility(View.VISIBLE);
            mImageAdapter = new CustomGridViewAdapter(this);
            gridViewImage.setAdapter(mImageAdapter);
            gridViewImage.setOnItemClickListener(new MyGridViewItemClickListener(EInspectItemType.IMAGE));
            // 填充上次缓存的照片
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                List<String> imagesPaths = Arrays.asList(strs);
                MediaCacheManager.imgdrr.addAll(imagesPaths);
                updateImageFile();
            }
        } else {
            gridViewImage.setVisibility(View.GONE);
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(Constants.IMAGE_SPLIT_STRING);
                final List<String> imagesPaths = new ArrayList<String>(Arrays.asList(strs));
                // 服务返回的是图片名字，本地组装成完整路径
                for (int i = 0; i < imagesPaths.size(); i++) {
                    String path = ServiceUrlManager.getInstance().getImageUrl() + imagesPaths.get(i);
                    imagesPaths.set(i, path);
                }
                ImageAdapter adapter = new ImageAdapter(this, imagesPaths, true);
                gallery.setAdapter(adapter);
                gallery.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(FullScreenPhotoActivity.KEY_PATH, (ArrayList<String>) imagesPaths);
                        bundle.putInt(FullScreenPhotoActivity.KEY_POSITION, position);
                        UIHelper.startActivityWithExtra(FullScreenPhotoActivity.class, bundle);
                    }
                });
            }
        }
    }

    private class MyGridViewItemClickListener implements OnItemClickListener {
        private EInspectItemType type;
        private String[] menus;

        public MyGridViewItemClickListener(EInspectItemType type) {
            this.type = type;
            switch (type) {
                case IMAGE:
                    menus = getResources().getStringArray(R.array.custom_action_sheet_image_menus);
                    break;
                case VIDEO:
                    menus = getResources().getStringArray(R.array.custom_action_sheet_video_menus);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (type == EInspectItemType.IMAGE) {
                showImageMenu(position);
            }
            if (type == EInspectItemType.VIDEO) {
                showVideoMenu(position);
            }
        }

        private void showImageMenu(int position) {
            if (position == MediaCacheManager.imgbmp.size()) {
                ActionSheet.show(getApplicationContext(), getString(R.string.action_menu_title), menus, new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                takePhoto();
                                ActionSheet.dismiss();
                                break;
                            case 1:
                                selectPicture();
                                ActionSheet.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                Intent intent = new Intent(EarthQuakeBaseReportActivity.this, PhotoActivity.class);
                intent.putExtra("ID", position);
                startActivityForResult(intent, RequestCode.EDIT_PHOTO);
            }
        }

        private void showVideoMenu(int position) {
            if (position == mVideomodels.size()) {
                ActionSheet.show(getApplicationContext(), getResources().getString(R.string.action_menu_title), menus, new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Intent takeVideoIntent = new Intent(EarthQuakeBaseReportActivity.this, TakeVideoActivity.class);
                                startActivityForResult(takeVideoIntent, RequestCode.TAKE_VIDEOS_CUSTOMEPORTACTIVITY);
                                ActionSheet.dismiss();
                                break;
                            case 1:
                                FileSuffixUtils.mFileFileterBySuffixs.acceptSuffixs("mp4");
                                Intent selectVideoIntent = new Intent(EarthQuakeBaseReportActivity.this, FileChooserActivity.class);
                                startActivityForResult(selectVideoIntent, RequestCode.SELECT_VIDEOS_CUSTOMEPORTACTIVITY);
                                ActionSheet.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            } else {
                VideoModel temp = (VideoModel) mVideoAdapter.getItem(position);
                String path = temp.getPath();
                int startIndex = path.lastIndexOf("/");
                int endIndex = path.lastIndexOf(".");
                String videoPath = MediaHelper.getVideoPath() + "/" + path.substring(startIndex + 1, endIndex) + ".mp4";
                Intent intent = new Intent(EarthQuakeBaseReportActivity.this, PlayVideoActivity.class);
                intent.putExtra("INTENT_KEY_VIDEOPATH", videoPath);
                startActivity(intent);
            }
        }
    }

    private void takePhoto() {
        currentPhotoName = System.currentTimeMillis() + ".jpg";
        StartCameraUtil.launchCamera(this, StartCameraUtil.CODE_TAKE_PHOTO, MediaHelper.getImagePath(), currentPhotoName);
    }

    private void selectPicture() {
        if (null != MediaCacheManager.imgdrr) {
            MediaCacheManager.imgdrr.clear();
        }
        if (null != mImagesZ3Paths) {
            mImagesZ3Paths.clear();
        }
        if (null != mImagesSdPaths) {
            int size = mImagesSdPaths.size();
            for (int i = 0; i < size; i++) {
                if (!MediaCacheManager.imgdrr.contains(mImagesSdPaths.get(i))) {
                    MediaCacheManager.imgdrr.add(mImagesSdPaths.get(i));
                }
            }
        }

        Intent intent = new Intent(EarthQuakeBaseReportActivity.this, AlbumSelectActivity.class);
        startActivityForResult(intent, RequestCode.SELECT_PHOTO);
    }

    private void updateImageFile() {
        removeDuplicateWithOrder(MediaCacheManager.imgdrr);
        try {
            for (int i = 0; i < MediaCacheManager.imgdrr.size(); i++) {
                String originalPath = MediaCacheManager.imgdrr.get(i);
                if (!mImagesSdPaths.contains(originalPath)) {
                    mImagesSdPaths.add(originalPath);
                    Bitmap bm = MediaCacheManager.revitionImageSize(originalPath);
                    MediaCacheManager.imgbmp.add(bm);
                    String newStr = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.lastIndexOf("."));
                    FileUtil.getInstance(getApplicationContext()).saveBitmap(bm, "" + newStr);
                }
            }

            for (int i = 0; i < mImagesSdPaths.size(); i++) {
                String originalPath = mImagesSdPaths.get(i);
                String newStr = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.lastIndexOf("."));
                MediaCacheManager.maxImg += 1;
                String newFilePath = mMediaImagePath + newStr + ".JPEG";
                if (!mImagesZ3Paths.contains(newFilePath)) {
                    mImagesZ3Paths.add(newFilePath);
                }
            }

        } catch (IOException e) {
            LogUtil.e(this, e);
        }

        // 将图片路径信息设置到InspectItem中的Value中
        setImagesValue();
        Message msg = commonReportHandler.obtainMessage();
        msg.what = 1;
        commonReportHandler.sendMessage(msg);
    }

    private static class CommonReportHandler extends Handler {
        private final WeakReference<EarthQuakeBaseReportActivity> mActivity;

        public CommonReportHandler(EarthQuakeBaseReportActivity activity) {
            mActivity = new WeakReference<EarthQuakeBaseReportActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EarthQuakeBaseReportActivity earthQuakeBaseReportActivity = mActivity.get();
            if (null != earthQuakeBaseReportActivity) {
                switch (msg.what) {
                    case 1:
                        if (null != earthQuakeBaseReportActivity.mImageAdapter || null != earthQuakeBaseReportActivity.mVideoAdapter) {
                            earthQuakeBaseReportActivity.mImageAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 2:
                        if (null != earthQuakeBaseReportActivity.mImageAdapter || null != earthQuakeBaseReportActivity.mVideoAdapter) {
                            earthQuakeBaseReportActivity.mVideoAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 3:
                        setAudiosValue(earthQuakeBaseReportActivity.mAudioAdapter);
                        if (null != earthQuakeBaseReportActivity.mAudioAdapter || null != earthQuakeBaseReportActivity.mVideoAdapter) {
                            earthQuakeBaseReportActivity.mAudioAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 将图片路径信息设置到InspectItem中的Value中
     */
    private void setImagesValue() {
        if (null != mImagesZ3Paths && mImagesZ3Paths.size() > 0) {
            StringBuilder sbPaths = new StringBuilder();
            for (int i = 0; i < mImagesZ3Paths.size(); i++) {
                sbPaths.append(mImagesZ3Paths.get(i)).append(",");
                if (i == mImagesZ3Paths.size() - 1) {
                    sbPaths.deleteCharAt(sbPaths.length() - 1);
                }
            }

            List<InspectItem> allChildItems = getAllInspectItems(mInspectItems);
            Iterator<InspectItem> iterator = allChildItems.iterator();
            while (iterator.hasNext()) {
                InspectItem item = iterator.next();
                if (item.getType() == EInspectItemType.IMAGE) {
                    item.setValue(sbPaths.toString());
                    break;
                } else {
                    // TODO: no logic to do.
                }
            }
        }
    }

    private void removeDuplicateWithOrder(List<String> list) {
        Set<String> setTemp = new HashSet<String>();
        List<String> listTemp = new ArrayList<String>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            if (setTemp.add(temp)) {
                listTemp.add(temp);
            }
        }

        list.clear();
        list.addAll(listTemp);
    }

    private static void setAudiosValue(AudiosRecordAdapter mAudioAdapter) {
        if (null != mAudioAdapter) {
            ArrayList<AudioModel> audios = (ArrayList<AudioModel>) mAudioAdapter.getAudios();
            StringBuilder sbPaths = new StringBuilder();
            for (int i = 0; i < audios.size(); i++) {
                sbPaths.append(audios.get(i).getFilePath()).append(",");
                if (i == audios.size() - 1) {
                    sbPaths.deleteCharAt(sbPaths.length() - 1);
                }
            }

            Iterator<InspectItem> iterator = mInspectItems.iterator();
            while (iterator.hasNext()) {
                InspectItem item = iterator.next();
                if (item.getType() == EInspectItemType.AUDIO) {
                    item.setValue(sbPaths.toString());
                    break;
                } else {
                    // TODO: no logic to do.
                }
            }
        }
    }

    public List<InspectItem> getAllInspectItems(List<InspectItem> inspectItem) {
        allInspects = new ArrayList<InspectItem>();
        getInspectItems(inspectItem);
        return allInspects;
    }

    private void getInspectItems(List<InspectItem> data) {
        for (int i = 0; i < data.size(); i++) {
            InspectItem item = data.get(i);
            if (item.getType() == EInspectItemType.GROUP) {
                //得到所有的分组，设置为显示
                Group group = new Group(item.getName(), item.getAlias(), true, new ArrayList<Group>());
                if (!isGroupItems(group)) {
                    allGroups.add(group);
                }
                getInspectItems(item.getChilds());
            } else {
                allInspects.add(item);
            }
        }
    }

    private boolean isGroupItems(Group group) {
        if (allGroups.size() == 0) {
            return false;
        }
        for (int j = 0; j < allGroups.size(); j++) {
            Group group_n = allGroups.get(j);
            if (group.getName().contains(group_n.getName())) {
                group_n.getGroups().add(group);
                return true;
            }
        }
        return false;
    }

    private void inflateVideo(GridViewForScrollView gridViewVideo, InspectItem item, Gallery gallery) {
        if (item.isEdit()) {
            gridViewVideo.setVisibility(View.VISIBLE);
            mVideoAdapter = new VideoRecordAdapter(this, mVideomodels);
            gridViewVideo.setAdapter(mVideoAdapter);
            gridViewVideo.setOnItemClickListener(new MyGridViewItemClickListener(EInspectItemType.VIDEO));
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(",");
                List<String> videosPaths = Arrays.asList(strs);
                MediaCacheManager.vdodrr.addAll(videosPaths);
                updateVideoFile();
            }
        } else {
            gridViewVideo.setVisibility(View.GONE);
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(",");
                final List<String> imagesPaths = new ArrayList<String>(Arrays.asList(strs));
                for (int i = 0; i < imagesPaths.size(); i++) {
                    String path = ServiceUrlManager.getInstance().getImageUrl() + imagesPaths.get(i);
                    imagesPaths.set(i, path);
                }
                ImageAdapter adapter = new ImageAdapter(this, imagesPaths, false);
                gallery.setAdapter(adapter);
                gallery.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String videoPath = imagesPaths.get(position);
                        Intent intent = new Intent(EarthQuakeBaseReportActivity.this, PlayVideoActivity.class);
                        intent.putExtra("INTENT_KEY_VIDEOPATH", videoPath);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void updateVideoFile() {
        removeDuplicateWithOrder(MediaCacheManager.vdodrr);
        try {
            for (int i = 0; i < MediaCacheManager.vdodrr.size(); i++) {
                String originalPath = MediaCacheManager.vdodrr.get(i);
                if (!mVideosSdPaths.contains(originalPath)) {
                    mVideosSdPaths.add(originalPath);
                    Bitmap bm = MediaCacheManager.revitionImageSize(originalPath);
                    MediaCacheManager.vdobmp.add(bm);
                }
            }

            for (int i = 0; i < mVideosSdPaths.size(); i++) {
                String originalPath = mVideosSdPaths.get(i);
                String newStr = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.lastIndexOf("."));
                MediaCacheManager.maxVdo += 1;
                String newFilePath = "";
                newFilePath = mMediaVideoPath + newStr + ".MP4";

                if (!mVideosZ3Paths.contains(newFilePath)) {
                    mVideosZ3Paths.add(newFilePath);
                }
            }

        } catch (IOException e) {
            LogUtil.e(this, e);
        }

        setVideosValue();
        Message msg = commonReportHandler.obtainMessage();
        msg.what = 2;
        commonReportHandler.sendMessage(msg);
    }

    private void setVideosValue() {
        if (null != mVideosZ3Paths && mVideosZ3Paths.size() > 0) {
            StringBuilder sbPaths = new StringBuilder();
            for (int i = 0; i < mVideosZ3Paths.size(); i++) {
                sbPaths.append(mVideosZ3Paths.get(i)).append(",");
                if (i == mVideosZ3Paths.size() - 1) {
                    sbPaths.deleteCharAt(sbPaths.length() - 1);
                }
            }

            List<InspectItem> allChildItems = getAllInspectItems(mInspectItems);
            Iterator<InspectItem> iterator = allChildItems.iterator();
            while (iterator.hasNext()) {
                InspectItem item = iterator.next();
                if (item.getType() == EInspectItemType.VIDEO) {
                    item.setValue(sbPaths.toString());
                    break;
                } else {
                    // TODO: no logic to do.
                }
            }
        }
    }
    private void inflateAudio(ListViewForScrollView lvAudio, InspectItem item) {
        if (item.isEdit()) {
            ArrayList<InspectItem> mInspectItems = new ArrayList<InspectItem>();
            mInspectItems.add(item);
            audioRecordUtil = new AudioRecordUtil(this, new AudioRecordCallBack());
            mAudioAdapter = audioRecordUtil.getAudiosRecordAdapter(mAudiomodels);
            mAudioAdapter.setCallback(new AudioRecordCallBack());
            lvAudio.setAdapter(mAudioAdapter);
            lvAudio.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (arg2 == mAudiomodels.size()) {
                        audioRecordUtil.showRecordingAudioView(arg1);
                    }
                }
            });
            lvAudio.setAdapter(mAudioAdapter);
            setAudiosValue(mAudioAdapter);
        } else {
            if (!item.getValue().isEmpty()) {
                String[] strs = item.getValue().split(",");
                List<String> names = new ArrayList<String>(Arrays.asList(strs));
                // 服务返回的是录音文件名
                for (int i = 0; i < names.size(); i++) {
                    AudioModel audioModel = new AudioModel();
                    audioModel.setName(names.get(i));
                    mAudiomodels.add(audioModel);
                }
                AudiosDetailListAdapter adapter = new AudiosDetailListAdapter(this, mAudiomodels);
                lvAudio.setAdapter(adapter);
            }
        }
    }
    private class AudioRecordCallBack implements IAudioRecordCallback {

        @Override
        public void onRecordFinish(List<AudioModel> audios) {
            Message msg = commonReportHandler.obtainMessage();
            msg.what = 3;
            commonReportHandler.sendMessage(msg);
        }

        @Override
        public void onAudioDeleted(List<AudioModel> audios) {
            Toast.makeText(getApplicationContext(), String.valueOf(audios), Toast.LENGTH_SHORT).show();
        }
    }
    
    private class MyRadioGroupOnClickListener implements OnCheckedChangeListener {
        private InspectItem item;

        public MyRadioGroupOnClickListener(InspectItem item, RadioGroup radioGroup) {
            this.item = item;
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(item.getSelectValues());
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
            List<InspectItemSelectValue> selectValueLists = InspectItemSelectValueAdapter.adapt(jsonArray);
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params_rb.setMargins(100, 0, 100, 0);
            for (int i = 0; i < selectValueLists.size(); i++) {
                RadioButton tempButton = new RadioButton(EarthQuakeBaseReportActivity.this);
                tempButton.setLayoutParams(params_rb);
                tempButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tempButton.setTextColor(EarthQuakeBaseReportActivity.this.getResources().getColor(R.color.txt_black_normal));
                tempButton.setText(selectValueLists.get(i).name);
                tempButton.setTag(selectValueLists.get(i).gid);
                radioGroup.addView(tempButton);
                if (item.getValue().equalsIgnoreCase(selectValueLists.get(i).gid)) {
                    radioGroup.check(tempButton.getId());
                }
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton tempButton = (RadioButton) findViewById(checkedId);
            if (tempButton.isChecked()) {
                String newValue = String.valueOf(tempButton.getTag());
                updateRadioTextValue(item, newValue, true);
            }
        }
    }
    private String getOnlyEtValue(InspectItem item) {
        String etValue = item.getValue();
        int index = item.getValue().indexOf("&");
        if (index == -1) {
            etValue = "";
        } else {
            etValue = item.getValue().substring(index + 1);
        }

        return etValue;
    }
    private class MyCheckBoxOnClickListener implements android.widget.CompoundButton.OnCheckedChangeListener {
        InspectItem item;
        CheckBox checkBox;

        public MyCheckBoxOnClickListener(InspectItem item, CheckBox checkBox) {
            this.item = item;
            this.checkBox = checkBox;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String valueTemp = item.getValue();
            String cbTag = String.valueOf(checkBox.getTag());
            if (isChecked) {
                if (!StringUtil.isBlank(valueTemp) && valueTemp.contains(cbTag)) {
                    return;
                } else {
                    if (StringUtil.isBlank(valueTemp)) {
                        item.setValue(valueTemp + cbTag + ",");
                    } else {
                        item.setValue(valueTemp + "," + cbTag + ",");
                    }
                }
            } else {
                if (!StringUtil.isBlank(valueTemp) && valueTemp.contains(cbTag)) {
                    item.setValue(valueTemp.replace(cbTag + ",", ""));
                }
            }
            if (item.getValue().endsWith(",")) {
                item.setValue(item.getValue().substring(0, item.getValue().length() - 1));
            }
        }
    }
    private class MySpinnerOnItemSelectedClick implements OnItemSelectedListener {
        private InspectItem item;
        private String[] gids;
        private String[] names;
        private Spinner spinner;
        private EditText etText;
        private boolean isDdlNormal;

        public MySpinnerOnItemSelectedClick(InspectItem item, Spinner spinner, EditText editText, boolean isDdlNormal) {
            this.item = item;
            this.spinner = spinner;
            this.etText = editText;
            this.isDdlNormal = isDdlNormal;

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(item.getSelectValues());
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }
            List<InspectItemSelectValue> selectValueLists = InspectItemSelectValueAdapter.adapt(jsonArray);
            names = new String[selectValueLists.size()];
            gids = new String[selectValueLists.size()];
            for (int i = 0; i < selectValueLists.size(); i++) {
                gids[i] = selectValueLists.get(i).gid;
                names[i] = selectValueLists.get(i).name;
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!isDdlNormal) {
                if (position == gids.length) {
                    spinner.setVisibility(View.GONE);
                    etText.setVisibility(View.VISIBLE);
                } else {
                    etText.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    item.setValue(gids[position]);
                }
            } else {
                item.setValue(gids[position]);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    }
    private class MyImgSpnViewOnClickListener implements OnClickListener {
        private Spinner spinner;

        public MyImgSpnViewOnClickListener(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void onClick(View v) {
            if (null != spinner) {
                spinner.setVisibility(View.VISIBLE);
                spinner.performClick();
            }
        }
    }
}
