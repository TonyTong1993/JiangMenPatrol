package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.activities.WorkOrderDetailFragmentActivity;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.z3app.android.util.StringUtil;

public class WorkOrderCodeInspectItemViewXtd extends ABaseInspectItemView {

    @Override
    protected void setup(View view) {
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        setText(tvContent, mInspectItem.getValue(), mInspectItem.getDefaultValue());
        view.findViewById(R.id.rl_body).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkOrderDetailFragmentActivity.class);
                Bundle bundle = new Bundle();
                // TODO:怎样获取当前工单，不从CustomViewInflater拿？
                bundle.putSerializable(WorkOrder.KEY_SERIAL, mCustomInflater.getCurrentWorkOrder());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.item_code;
    }

    private void setText(TextView v, String value, String defaultValue) {
        if (!StringUtil.isBlank(value)) {
            v.setText(value);
        } else {
            v.setText(defaultValue);
        }
    }
}
