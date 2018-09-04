package com.ecity.cswatersupply.ui.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.MemoryListExpdListAdapter;
import com.ecity.cswatersupply.adapter.MemoryListExpdListAdapter.IExpdCheckBoxCallBack;
import com.ecity.cswatersupply.model.MemoryInfoModel;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.ClearUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.z3app.android.util.FileUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * 2017-2-24
 * @author Gxx
 *
 */
public class MemoryClearActivity extends BaseActivity implements IExpdCheckBoxCallBack {
    private CustomTitleView customTitleView;
    private ExpandableListView expandableListView;
    private List<String> memoryTypes;
    private Map<String, List<MemoryInfoModel>> memoryInfos;
    private MemoryListExpdListAdapter adapter;
    private List<MemoryInfoModel> selectedModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_clear);

        initUI();
        initData();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void onActionButtonClicked(View v) {
        LoadingDialogUtil.show(MemoryClearActivity.this, R.string.system_settings_clearing);
        Map<String, List<MemoryInfoModel>> tempMemoryInfos = new HashMap<String, List<MemoryInfoModel>>();
        deleteSelectedModels(selectedModels);

        Iterator iter = memoryInfos.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            List<MemoryInfoModel> models = (List<MemoryInfoModel>) entry.getValue();
            List<MemoryInfoModel> commonModels = getCommonInfoModels(models, selectedModels);
            models.removeAll(commonModels);
            tempMemoryInfos.put(key, models);
        }
        selectedModels.clear();
        memoryInfos = tempMemoryInfos;
        adapter.setData(memoryTypes,memoryInfos);
        adapter.notifyDataSetChanged();
        SessionManager.isActivityMapNeedReload = true;
        SessionManager.isFragmentMapNeedReload = true;
        LoadingDialogUtil.dismiss();
    }

    private List<MemoryInfoModel> getCommonInfoModels(List<MemoryInfoModel> models1, List<MemoryInfoModel> models2) {
        List<MemoryInfoModel> commonModels = new ArrayList<MemoryInfoModel>();
        for(MemoryInfoModel out : models1) {
            for(MemoryInfoModel inner : models2) {
                if(out.getMemoryPath().equals(inner.getMemoryPath())) {
                    commonModels.add(inner);
                }
            }
        }
        return commonModels;
    }

    private void deleteSelectedModels(List<MemoryInfoModel> selectedModels) {
        List<String> paths = new ArrayList<String>();
        for(MemoryInfoModel model : selectedModels) {
            paths.add(model.getMemoryPath());
        }
        ClearUtil.deleteFiles(paths);
    }

    private void initUI() {
        customTitleView = (CustomTitleView) this.findViewById(R.id.customTitleView);
        expandableListView = (ExpandableListView) this.findViewById(R.id.expandableListView);
        customTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        customTitleView.setTitleText(R.string.system_settings_clear_memory);
        customTitleView.setRightActionBtnText(R.string.clear);
    }

    private void initData() {
        memoryTypes = Arrays.asList(getResources().getStringArray(R.array.clear_memeory_types));
        memoryInfos = new HashMap<String, List<MemoryInfoModel>>();
        initMemoInfos();
        adapter = new MemoryListExpdListAdapter(this, memoryTypes, memoryInfos, this);
        expandableListView.setAdapter(adapter);
        selectedModels = new ArrayList<MemoryInfoModel>();
    }

    private void initMemoInfos() {
        List<MemoryInfoModel> infoModels1 = findAllDirs(FileUtil.getInstance(null).getMediaPath());
        List<MemoryInfoModel> infoModels2 = findAllDirs(FileUtil.getInstance(null).getLocalMapDocPath());
        memoryInfos.put(memoryTypes.get(0).toString(), infoModels1);
        memoryInfos.put(memoryTypes.get(1).toString(), infoModels2);
    }

    private List<MemoryInfoModel> findAllDirs(String path) {
        List<MemoryInfoModel> infoModels = new ArrayList<MemoryInfoModel>();
        List<File> dirs = ClearUtil.listFilesInDir(path, false);
        for (File file : dirs) {
            if (ClearUtil.isDir(file)) {
                MemoryInfoModel model = new MemoryInfoModel();
                model.setMemoryName(file.getName());
                model.setMemoryPath(file.getPath());
                model.setCheck(false);
                infoModels.add(model);
            }
        }
        return infoModels;
    }

    @Override
    public void onChlidCheckBoxClick(MemoryInfoModel child) {
        if (child.isCheck()) {
            if(!selectedModels.contains(child)) {
                selectedModels.add(child);
            }
        } else {
            if (selectedModels.contains(child)) {
                selectedModels.remove(child);
            }
        }
    }

}
