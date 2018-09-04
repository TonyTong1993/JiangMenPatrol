package com.dtr.zxing.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.dtr.zxing.R;
import com.dtr.zxing.utils.BitmapUtils;
import com.google.zxing.WriterException;

public class GenerateBarActivity extends Activity {
    public static final String INTENT_KEY_BARCODE_TEXT = "INTENT_KEY_BARCODE_TEXT";
    protected int mScreenWidth;
    ImageView imgViewBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bar);
        initUI();

        String barcodeText = getIntent().getStringExtra(INTENT_KEY_BARCODE_TEXT);
        Bitmap bitmap = generateBarcode(barcodeText, mScreenWidth);
        if (bitmap != null) {
            imgViewBarcode.setImageBitmap(bitmap);
        }
    }

    private void initUI() {
        imgViewBarcode = (ImageView) this.findViewById(R.id.iv_generate_bar);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    private Bitmap generateBarcode(String barcodeText, int width) {
        if (TextUtils.isEmpty(barcodeText)) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = BitmapUtils.createQRCode(barcodeText, width);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
