package com.ecity.cswatersupply.workorder.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.workorder.model.MaterialBrief;
import com.ecity.cswatersupply.workorder.model.MaterialDetail;

public class WorkOrderChooseMaterialAdapter extends AExpandableListAdapter<MaterialBrief, MaterialDetail> {
    private Context mContext;
    private LayoutInflater mInflater;
    private Drawable mDrawableRight;
    private Drawable mDrawableDown;

    public WorkOrderChooseMaterialAdapter(Context context) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mDrawableRight = context.getResources().getDrawable(R.drawable.arrow_right);
        mDrawableRight.setBounds(0, 0, mDrawableRight.getMinimumWidth(), mDrawableRight.getMinimumHeight());
        mDrawableDown = context.getResources().getDrawable(R.drawable.arrow_down);
        mDrawableDown.setBounds(0, 0, mDrawableDown.getMinimumWidth(), mDrawableDown.getMinimumHeight());
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup viewHolderGroup = null;
        MaterialBrief materialBrief = getGroup(groupPosition);
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_workorder_material_expandedlistview_group, null);
            viewHolderGroup = new ViewHolderGroup(convertView);
            convertView.setTag(viewHolderGroup);
        } else {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }

        viewHolderGroup.tvMaterialName.setText(materialBrief.getName());
        if (isExpanded) {
            viewHolderGroup.tvMaterialName.setCompoundDrawables(null, null, mDrawableDown, null);
            viewHolderGroup.tvMaterialName.setTextColor(mContext.getResources().getColor(R.color.blue_normal));
        } else {
            viewHolderGroup.tvMaterialName.setCompoundDrawables(null, null, mDrawableRight, null);
            viewHolderGroup.tvMaterialName.setTextColor(mContext.getResources().getColor(R.color.black_text));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild viewHolderChild = null;
        final MaterialDetail materialDetail = getChild(groupPosition, childPosition);

        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_workorder_material_expandedlistview_children, null);
            viewHolderChild = new ViewHolderChild(convertView);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        viewHolderChild.tvMaterialSpec.setText(materialDetail.getDiameter() + materialDetail.getUnit());
        viewHolderChild.cbMaterialChoose.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    materialDetail.setSelected(true);
                } else {
                    materialDetail.setSelected(false);
                }
            }
        });

        viewHolderChild.cbMaterialChoose.setChecked(materialDetail.isSelected());

        return convertView;
    }

    static class ViewHolderGroup {
        private TextView tvMaterialName;

        public ViewHolderGroup(View convertView) {
            tvMaterialName = (TextView) convertView.findViewById(R.id.tv_material_name);
        }
    }

    static class ViewHolderChild {
        private TextView tvMaterialSpec;
        private CheckBox cbMaterialChoose;

        public ViewHolderChild(View convertView) {
            tvMaterialSpec = (TextView) convertView.findViewById(R.id.tv_material_spec);
            cbMaterialChoose = (CheckBox) convertView.findViewById(R.id.cb_material_choose);
        }
    }

}
