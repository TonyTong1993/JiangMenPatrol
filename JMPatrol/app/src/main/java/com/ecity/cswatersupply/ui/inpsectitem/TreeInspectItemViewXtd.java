package com.ecity.cswatersupply.ui.inpsectitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.treeview.util.TreeViewHelper;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.WorkOrderFinishHandleTypeActivity;
import com.ecity.cswatersupply.workorder.model.TreeViewLocalData;
import com.z3app.android.util.StringUtil;

public class TreeInspectItemViewXtd extends ABaseInspectItemView {
    private Map<String, TextView> mHashMapButtons = new HashMap<String, TextView>();
    private List<TreeViewLocalData> mHandleTypes;

    @Override
    protected void setup(View contentView) {
        Button btnSelect = (Button) contentView.findViewById(R.id.view_value);
        mHashMapButtons.put(mInspectItem.getName(), btnSelect);
        mHandleTypes = TreeViewHelper.convertInspectItemToHandleType(mInspectItem);
        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            mHandleTypes = TreeViewHelper.convertInspectItemToHandleType(mInspectItem);
            setTreeViewTypeValue(mHandleTypes);
        }
        btnSelect.setOnClickListener(new MyBtnSelectOnClickListener(mInspectItem, EInspectItemType.TREE, btnSelect));
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_select_tree;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SELECT_HANDLE_TYPE) {
            if (null != data) {
                @SuppressWarnings("unchecked")
                List<TreeViewLocalData> types = (List<TreeViewLocalData>) data.getSerializableExtra(WorkOrderFinishHandleTypeActivity.TREE_DATA_SOURCE);
                setTreeViewTypeValue(types);
                UIEvent event = new UIEvent(UIEventStatus.INSPECT_ITEM_VALUE_CHANGED, mInspectItem);
                EventBusUtil.post(event);
            }
        }
    }

    private void setTreeViewTypeValue(List<TreeViewLocalData> types) {
        if (ListUtil.isEmpty(types)) {
            return;
        }

        StringBuilder sbAlias = new StringBuilder("");
        StringBuilder sbName = new StringBuilder("");
        for (TreeViewLocalData type : types) {
            if (type.isSelected() || type.isHasSelected()) {
                sbAlias.append(type.getAlias() + " ");
                sbName.append(type.getName() + ",");
            }
        }

        if (StringUtil.isBlank(sbAlias.toString())) {
            mHashMapButtons.get(mInspectItem.getName()).setText("");
        } else {
            mHashMapButtons.get(mInspectItem.getName()).setText(sbAlias.toString());
        }

        if (StringUtil.isBlank(sbName.toString())) {
            mInspectItem.setValue("");
        } else {
            sbName.deleteCharAt(sbName.length() - 1);// 删掉最后一个","
            mInspectItem.setValue(sbName.toString());
        }
    }

    private class MyBtnSelectOnClickListener implements OnClickListener {
        private InspectItem item;
        private EInspectItemType type;

        public MyBtnSelectOnClickListener(InspectItem item, EInspectItemType type, Button btn) {
            this.item = item;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case TREE:
                    Intent intent = new Intent(context, WorkOrderFinishHandleTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(WorkOrderFinishHandleTypeActivity.TREE_DATA_SOURCE, this.item);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RequestCode.SELECT_HANDLE_TYPE);
                    break;
                default:
                    break;
            }
        }
    }
}
