package com.ecity.cswatersupply.ui.inpsectitem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dtr.zxing.activity.CaptureActivity;
import com.ecity.cswatersupply.R;
import com.z3app.android.util.StringUtil;

public class QRCodeInspectItemViewXtd extends ABaseInspectItemView {
    private TextView tvQRCode;

    @Override
    protected void setup(View contentView) {
        tvQRCode = (TextView) contentView.findViewById(R.id.tv_view_value);
        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            tvQRCode.setText(mInspectItem.getValue());
        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
            tvQRCode.setText(mInspectItem.getDefaultValue());
        }
        ImageButton imgBtnZXing = (ImageButton) contentView.findViewById(R.id.imgbtn_zxing);
        imgBtnZXing.setOnClickListener(new MyQrCodeOnClickListener());
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_qrcode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CaptureActivity.REQUEST_CODE_SCAN_BARCODE) {
            String result = parseScanResult(data);
            mInspectItem.setValue(result);
            tvQRCode.setText(result);
        }
    }

    private class MyQrCodeOnClickListener implements OnClickListener {

        public MyQrCodeOnClickListener() {

        }

        @Override
        public void onClick(View v) {
            // Intent intent = new Intent(CustomViewInflater.mCurrentActivity,
            // CaptureActivity.class);
            // CustomViewInflater.mCurrentActivity.startActivityForResult(intent,
            // CaptureActivity.REQUEST_CODE_SCAN_BARCODE);
        }
    }

    private String parseScanResult(Intent data) {
        String result = "";
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                result = bundle.getString(CaptureActivity.INTENT_KEY_BARCODE);
            }
        }

        return result;
    }
}
