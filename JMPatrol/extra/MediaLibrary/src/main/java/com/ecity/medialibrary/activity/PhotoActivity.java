package com.ecity.medialibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.utils.MediaCacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.forward.androids.utils.ImageUtils;
import cn.hzw.graffiti.GraffitiActivity;
import cn.hzw.graffiti.GraffitiParams;

import static cn.hzw.graffiti.GraffitiActivity.KEY_IMAGE_PATH;

/***
 * 
 * Description:<br/>
 *
 * @version V1.0
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月12日
 * @email
 */
public class PhotoActivity extends Activity {
	public static List<String> imagePaths;
	public static List<Bitmap> bitmaps;

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;

	private List<Bitmap> bmp = new ArrayList<Bitmap>();
	private List<String> drr = new ArrayList<String>();

	LinearLayout photo_linearLayout;
	//涂鸦
	private int selectItem;
	private Bitmap oldSelectBmp;
	public static final int REQ_CODE_GRAFFITI = 101;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		setContentView(R.layout.activity_ui_photo);

		photo_linearLayout = (LinearLayout) findViewById(R.id.photo_linearLayout);
		photo_linearLayout.setBackgroundColor(0x70000000);

		cacheDataSource();

		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(null != oldSelectBmp){
					MediaCacheManager.imgbmp.set(selectItem,oldSelectBmp);
					MediaCacheManager.imgdrr.set(selectItem,drr.get(selectItem));
					saveOldBitmap(oldSelectBmp,drr.get(selectItem));
					oldSelectBmp.recycle();
				}
				saveResult();
				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			    int currentItem = pager.getCurrentItem();
			    if(currentItem < bmp.size()){
					String newStr = String.valueOf(drr.get(currentItem));
					bmp.remove(currentItem);
					drr.remove(currentItem);
					pager.removeAllViews();
					listViews.remove(currentItem);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			}
		});
		Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		photo_bt_enter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				saveResult();
				finish();
			}
		});
		Button btn_graffiti= (Button) findViewById(R.id.photo_graffiti);
		btn_graffiti.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 涂鸦参数
				GraffitiParams params = new GraffitiParams();
				// 图片路径
				selectItem=pager.getCurrentItem();
				oldSelectBmp=bmp.get(selectItem);
				params.mImagePath = drr.get(selectItem);
				params.mPaintSize=2f;
				GraffitiActivity.startActivityForResult(PhotoActivity.this, params, REQ_CODE_GRAFFITI);
			}
		});

		pager = (ViewPager) findViewById(R.id.viewpager);
		for (int i = 0; i < bmp.size(); i++) {
			initListViews(bmp.get(i));//
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);//
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	class MyPageAdapter extends PagerAdapter {
		private ArrayList<View> listViews;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
		}
		
		@Override
		public int getCount() {
            return listViews == null ? 0 : listViews.size();
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
		    if(getCount()>0){
		        ((ViewPager) arg0).removeView(listViews.get(arg1 % getCount()));
		    }
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
		    if(getCount()<1){
		        return null;
		    }
		    
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % getCount()), 0);
			} catch (Exception e) {
			}
			
			return listViews.get(arg1 % getCount());
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	private void cacheDataSource() {
		List<Bitmap> tmpBitmaps = (imagePaths == null) ? MediaCacheManager.imgbmp : bitmaps;
		List<String> tmpPaths = (imagePaths == null) ? MediaCacheManager.imgdrr : imagePaths;

		for (Bitmap bitmap : tmpBitmaps) {
			bmp.add(bitmap);
		}
		for (String path : tmpPaths) {
			drr.add(path);
		}
	}

	private void saveResult() {
		if(imagePaths == null) {
			MediaCacheManager.imgbmp = bmp;
			MediaCacheManager.imgdrr = drr;
		} else {
			imagePaths = drr;
			bitmaps = bmp;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != oldSelectBmp){
			oldSelectBmp.recycle();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_GRAFFITI) {
			if (data == null) {
				oldSelectBmp = null;
				return;
			}
			if (resultCode == GraffitiActivity.RESULT_OK) {
				String path = data.getStringExtra(KEY_IMAGE_PATH);
				if (TextUtils.isEmpty(path)) {
					return;
				}
				//重新设置图片
				resetImageInfo(path);

			} else if (resultCode == GraffitiActivity.RESULT_ERROR) {
				Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			saveResult();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void resetImageInfo(String path) {
		bmp.set(selectItem, ImageUtils.createBitmapFromPath(path, this));
		drr.set(selectItem, path);

		MediaCacheManager.imgbmp.clear();
		MediaCacheManager.imgbmp.addAll(bmp);
		MediaCacheManager.imgdrr.clear();
		MediaCacheManager.imgdrr.addAll(drr);
		ImageView img = new ImageView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bmp.get(selectItem));
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listViews.set(selectItem,img);
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setCurrentItem(selectItem);
	}

	/**
	 * 点击X,取消使用涂鸦图片，则保存原先图片替换涂鸦图片
	 * @param mBitmap
	 * @param filePath
	 */
	public void saveOldBitmap(Bitmap mBitmap,String filePath)  {
		File f = new File(filePath);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
