package com.ecity.medialibrary.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.adapter.ImageGridAdapter;
import com.ecity.medialibrary.adapter.ImageGridAdapter.TextCallback;
import com.ecity.medialibrary.model.ImageItem;
import com.ecity.medialibrary.utils.AlbumHelper;
import com.ecity.medialibrary.utils.MediaCacheManager;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
    private static int MAX_SELECT_PHOTO_COUNT = 9;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt,btcancel;
    private static IBtnConfirmOnClickCallback mIBtnConfirmOnClickCallback;
    private ArrayList<String> takePhotoImageNames = new ArrayList();
    private int selectedImageCount = 0;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, getResources().getString(R.string.str_selambulance_limit), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
            finish();
            return;  
        }
		setContentView(R.layout.activity_ui_image_grid);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
        takePhotoImageNames = getIntent().getStringArrayListExtra(AlbumSelectActivity.INTENT_KEY_TAKE_PHOTO_IMAGE_NAMES);
        selectedImageCount = MediaCacheManager.imgdrr.size();

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				if (MediaCacheManager.act_bool) {
					//Intent intent = new Intent(ImageGridActivity.this,ConstructionReportAcivity.class);
					//startActivity(intent);
					MediaCacheManager.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (MediaCacheManager.imgdrr.size() < 9) {
						MediaCacheManager.imgdrr.add(list.get(i));
					}
				}
				mIBtnConfirmOnClickCallback.OnBtnConfirmClicked();
				finish();
			}

        });
        if (selectedImageCount != 0) {
            adapter.setSelectTotal(selectedImageCount);
            bt.setText(getResources().getString(R.string.str_down) + "(" + selectedImageCount + "/" + MAX_SELECT_PHOTO_COUNT + ")");
        }

        btcancel = (Button) findViewById(R.id.btcancel);
        if (null == btcancel)
            return;
        btcancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTakePhotoImageNames(takePhotoImageNames);
        adapter.setTextCallback(new TextCallback() {
            public void onListen(int count) {
                bt.setText(getResources().getString(R.string.str_down) + "(" + count + "/" + MAX_SELECT_PHOTO_COUNT + ")");
            }
        });

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}

    public static void setIBtnConfirmOnClickCallback(IBtnConfirmOnClickCallback iOnClickBtnConfirmCallback) {
        mIBtnConfirmOnClickCallback = iOnClickBtnConfirmCallback;
    }

    public interface IBtnConfirmOnClickCallback {
        void OnBtnConfirmClicked();
    }
}
