package cn.hzw.graffiti;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.forward.androids.utils.ImageUtils;
import cn.forward.androids.utils.LogUtil;
import cn.forward.androids.utils.StatusBarUtil;
import cn.forward.androids.utils.ThreadUtil;
import cn.forward.androids.views.STextView;

/**
 * 涂鸦界面，根据GraffitiView的接口，提供页面交互 （这边代码和ui比较粗糙，主要目的是告诉大家GraffitiView的接口具体能实现什么功能，实际需求中的ui和交互需另提别论） Created by huangziwei(154330138@qq.com) on
 * 2016/9/3.
 */
public class GraffitiActivity extends Activity {

	public static final String TAG = "Graffiti";

	public static final int   RESULT_ERROR       = -111; // 出现错误
	public static final Float DEFAULT_PAINT_SIZE = 15f; // 默认画笔大小

	/**
	 * 启动涂鸦界面
	 *
	 * @param params      涂鸦参数
	 * @param requestCode startActivityForResult的请求码
	 * @see GraffitiParams
	 */
	public static void startActivityForResult(Activity activity, GraffitiParams params, int requestCode) {
		Intent intent = new Intent(activity, GraffitiActivity.class);
		intent.putExtra(GraffitiActivity.KEY_PARAMS, params);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动涂鸦界面
	 *
	 * @param imagePath   　图片路径
	 * @param savePath    　保存路径
	 * @param isDir       　保存路径是否为目录
	 * @param requestCode 　startActivityForResult的请求码
	 */
	@Deprecated
	public static void startActivityForResult(Activity activity, String imagePath, String savePath, boolean isDir, int requestCode) {
		GraffitiParams params = new GraffitiParams();
		params.mImagePath = imagePath;
		params.mSavePath = savePath;
		params.mSavePathIsDir = isDir;
		startActivityForResult(activity, params, requestCode);
	}

	/**
	 * {@link GraffitiActivity#startActivityForResult(Activity, String, String, boolean, int)}
	 */
	@Deprecated
	public static void startActivityForResult(Activity activity, String imagePath, int requestCode) {
		GraffitiParams params = new GraffitiParams();
		params.mImagePath = imagePath;
		startActivityForResult(activity, params, requestCode);
	}

	public static final String KEY_PARAMS     = "key_graffiti_params";
	public static final String KEY_IMAGE_PATH = "key_image_path";

	private String mImagePath;
	private Bitmap mBitmap;

	private FrameLayout  mFrameLayout;
	private GraffitiView mGraffitiView;

	private View.OnClickListener mOnClickListener;
	//将画笔颜色以及大小放置在dialog中设置
	private Dialog               paintDialog;
	private EditText             etPaintSize;
	private View                 mBtnColor;
	private int                  selectColor = Color.RED;
	private Drawable             selectColorDrawable;
	private STextView            tvNegative;
	private STextView            tvPositive;

	private Runnable mUpdateScale;

	private int mTouchMode;
	private boolean mIsMovingPic = false;

	// 手势操作相关
	private float mOldScale, mOldDist, mNewDist, mToucheCentreXOnGraffiti, mToucheCentreYOnGraffiti, mTouchCentreX, mTouchCentreY;// 双指距离

	private float mTouchLastX, mTouchLastY;

	private       boolean mIsScaling = false;
	private       float   mScale     = 1;
	private final float   mMaxScale  = 3.5f; // 最大缩放倍数
	private final float   mMinScale  = 0.25f; // 最小缩放倍数
	private final int     TIME_SPAN  = 40;
	private View mBtnMovePic, mBtnHidePanel, mSettingsPanel;
	private View mSelectedTextEditContainer;
	private View mEditContainer;

	private int mTouchSlop;

	private AlphaAnimation mViewShowAnimation, mViewHideAnimation; // view隐藏和显示时用到的渐变动画

	// 当前屏幕中心点对应在GraffitiView中的点的坐标
	float mCenterXOnGraffiti;
	float mCenterYOnGraffiti;

	private GraffitiParams mGraffitiParams;

	// 触摸屏幕超过一定时间才判断为需要隐藏设置面板
	private Runnable mHideDelayRunnable;
	//触摸屏幕超过一定时间才判断为需要隐藏设置面板
	private Runnable mShowDelayRunnable;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_PARAMS, mGraffitiParams);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onRestoreInstanceState(savedInstanceState, persistentState);
		mGraffitiParams = savedInstanceState.getParcelable(KEY_PARAMS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusBarUtil.setStatusBarTranslucent(this, true, false);
		if (mGraffitiParams == null) {
			mGraffitiParams = getIntent().getExtras().getParcelable(KEY_PARAMS);
		}
		if (mGraffitiParams == null) {
			LogUtil.e("TAG", "mGraffitiParams is null!");
			this.finish();
			return;
		}

		mImagePath = mGraffitiParams.mImagePath;
		if (mImagePath == null) {
			LogUtil.e("TAG", "mImagePath is null!");
			this.finish();
			return;
		}
		LogUtil.d("TAG", mImagePath);
		if (mGraffitiParams.mIsFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}/*else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }*/
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBitmap = ImageUtils.createBitmapFromPath(mImagePath, this);
		if (mBitmap == null) {
			LogUtil.e("TAG", "bitmap is null!");
			this.finish();
			return;
		}

		setContentView(R.layout.layout_graffiti);
		mFrameLayout = (FrameLayout) findViewById(R.id.graffiti_container);

		// /storage/emulated/0/DCIM/Graffiti/1479369280029.jpg
		mGraffitiView = new GraffitiView(this, mBitmap, mGraffitiParams.mEraserPath, mGraffitiParams.mEraserImageIsResizeable,
										 new GraffitiListener() {
											 @Override
											 public void onSaved(Bitmap bitmap, Bitmap bitmapEraser) { // 保存图片
												 if (bitmapEraser != null) {
													 bitmapEraser.recycle(); // 回收图片，不再涂鸦，避免内存溢出
												 }
												 File file = new File(mImagePath);
												 FileOutputStream outputStream = null;
												 try {
													 outputStream = new FileOutputStream(file);
													 bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
													 ImageUtils.addImage(getContentResolver(), file.getAbsolutePath());
													 Intent intent = new Intent();
													 intent.putExtra(KEY_IMAGE_PATH, file.getAbsolutePath());
													 setResult(Activity.RESULT_OK, intent);
													 finish();
												 } catch (Exception e) {
													 e.printStackTrace();
													 onError(GraffitiView.ERROR_SAVE, e.getMessage());
												 } finally {
													 if (outputStream != null) {
														 try {
															 outputStream.close();
														 } catch (IOException e) {
														 }
													 }
												 }
											 }

											 @Override
											 public void onError(int i, String msg) {
												 setResult(RESULT_ERROR);
												 finish();
											 }

											 @Override
											 public void onReady() {
												 mGraffitiView.setPaintSize(mGraffitiParams.mPaintSize > 0 ? mGraffitiParams.mPaintSize :
																			mGraffitiView.getPaintSize());
												 if (mGraffitiView.getPen() == GraffitiView.Pen.TEXT) {
													 etPaintSize.setText(String.valueOf(mGraffitiView.getTextSize() + 0.5f));
												 } else {
													 etPaintSize.setText(String.valueOf(mGraffitiView.getPaintSize() + 0.5f));
												 }
												 findViewById(R.id.iv_hand).performClick();
											 }

											 @Override
											 public void onSelectedText(boolean selected) {
												 if (selected) {
													 mSelectedTextEditContainer.setVisibility(View.VISIBLE);
													 if (mGraffitiView.getSelectedTextColor().getType() == GraffitiColor.Type.BITMAP) {
														 mBtnColor.setBackgroundDrawable(
															 new BitmapDrawable(mGraffitiView.getSelectedTextColor().getBitmap()));
													 } else {
														 mBtnColor.setBackgroundColor(mGraffitiView.getSelectedTextColor().getColor());
													 }
													 //                            etPaintSize.setText(String.valueOf(mGraffitiView.getSelectedTextSize()));
												 } else {
													 mSelectedTextEditContainer.setVisibility(View.GONE);
													 mEditContainer.setVisibility(View.VISIBLE);
													 if (mGraffitiView.getColor().getType() == GraffitiColor.Type.BITMAP) {
														 mBtnColor.setBackgroundDrawable(
															 new BitmapDrawable(mGraffitiView.getColor().getBitmap()));
													 } else {
														 mBtnColor.setBackgroundColor(mGraffitiView.getColor().getColor());
													 }
													 //                            etPaintSize.setText(String.valueOf(mGraffitiView.getTextSize()));
												 }
											 }

											 @Override
											 public void onEditText(boolean showDialog, String string) {
												 mSettingsPanel.removeCallbacks(mHideDelayRunnable);
											 }
										 });
		mGraffitiView.setIsDrawableOutside(mGraffitiParams.mIsDrawableOutside);
		mFrameLayout.addView(mGraffitiView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mOnClickListener = new GraffitiOnClickListener();
		mTouchSlop = ViewConfiguration.get(getApplicationContext()).getScaledTouchSlop();
		initView();
	}

	private void initView() {
		findViewById(R.id.iv_hand).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_text).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_arrow).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_circle).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_rect).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_clear).setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_undo).setOnClickListener(mOnClickListener);
		findViewById(R.id.graffiti_text_edit).setOnClickListener(mOnClickListener);
		findViewById(R.id.graffiti_text_remove).setOnClickListener(mOnClickListener);
		mSelectedTextEditContainer = findViewById(R.id.graffiti_text_edit_container);
		mEditContainer = findViewById(R.id.graffiti_edit_container);
		mBtnHidePanel = findViewById(R.id.graffiti_btn_hide_panel);
		mBtnHidePanel.setOnClickListener(mOnClickListener);
		findViewById(R.id.graffiti_btn_finish).setOnClickListener(mOnClickListener);
		findViewById(R.id.graffiti_btn_back).setOnClickListener(mOnClickListener);
		findViewById(R.id.btn_centre_pic).setOnClickListener(mOnClickListener);
		mBtnMovePic = findViewById(R.id.btn_move_pic);
		mBtnMovePic.setOnClickListener(mOnClickListener);
		findViewById(R.id.iv_set).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPaintSetDialog();
			}
		});

		initPaintSetDialog();
		mSettingsPanel = findViewById(R.id.graffiti_panel);
		if (mGraffitiView.getGraffitiColor().getType() == GraffitiColor.Type.COLOR) {
			mBtnColor.setBackgroundColor(mGraffitiView.getGraffitiColor().getColor());
		} else if (mGraffitiView.getGraffitiColor().getType() == GraffitiColor.Type.BITMAP) {
			mBtnColor.setBackgroundDrawable(new BitmapDrawable(mGraffitiView.getGraffitiColor().getBitmap()));
		}

		ScaleOnTouchListener onTouchListener = new ScaleOnTouchListener();
		findViewById(R.id.btn_amplifier).setOnTouchListener(onTouchListener);
		findViewById(R.id.btn_reduce).setOnTouchListener(onTouchListener);

		// 添加涂鸦的触摸监听器，移动图片位置
		mGraffitiView.setOnTouchListener(new View.OnTouchListener() {

			boolean mIsBusy = false; // 避免双指滑动，手指抬起时处理单指事件。

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏设置面板
				if (!mBtnHidePanel.isSelected()  // 设置面板没有被隐藏
					&& mGraffitiParams.mChangePanelVisibilityDelay > 0) {
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
						case MotionEvent.ACTION_DOWN:
							mSettingsPanel.removeCallbacks(mHideDelayRunnable);
							mSettingsPanel.removeCallbacks(mShowDelayRunnable);
							mSettingsPanel.postDelayed(mHideDelayRunnable,
													   mGraffitiParams.mChangePanelVisibilityDelay); //触摸屏幕超过一定时间才判断为需要隐藏设置面板
							break;
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_UP:
							mSettingsPanel.removeCallbacks(mHideDelayRunnable);
							mSettingsPanel.removeCallbacks(mShowDelayRunnable);
							mSettingsPanel.postDelayed(mShowDelayRunnable,
													   mGraffitiParams.mChangePanelVisibilityDelay); //离开屏幕超过一定时间才判断为需要显示设置面板
							break;
					}
				} else if (mBtnHidePanel.isSelected() && mGraffitiView.getAmplifierScale() > 0) {
					mGraffitiView.setAmplifierScale(-1);
				}

				if (!mIsMovingPic) {
					return false;  // 交给下一层的涂鸦处理
				}
				mScale = mGraffitiView.getScale();
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						mTouchMode = 1;
						mTouchLastX = event.getX();
						mTouchLastY = event.getY();
						return true;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						mTouchMode = 0;
						return true;
					case MotionEvent.ACTION_MOVE:
						if (mTouchMode < 2) { // 单点滑动
							if (mIsBusy) { // 从多点触摸变为单点触摸，忽略该次事件，避免从双指缩放变为单指移动时图片瞬间移动
								mIsBusy = false;
								mTouchLastX = event.getX();
								mTouchLastY = event.getY();
								return true;
							}
							float tranX = event.getX() - mTouchLastX;
							float tranY = event.getY() - mTouchLastY;
							mGraffitiView.setTrans(mGraffitiView.getTransX() + tranX, mGraffitiView.getTransY() + tranY);
							mTouchLastX = event.getX();
							mTouchLastY = event.getY();
						} else { // 多点
							mNewDist = spacing(event);// 两点滑动时的距离
							if (Math.abs(mNewDist - mOldDist) >= mTouchSlop) {
								float scale = mNewDist / mOldDist;
								mScale = mOldScale * scale;

								if (mScale > mMaxScale) {
									mScale = mMaxScale;
								}
								if (mScale < mMinScale) { // 最小倍数
									mScale = mMinScale;
								}
								// 围绕坐标(0,0)缩放图片
								mGraffitiView.setScale(mScale);
								// 缩放后，偏移图片，以产生围绕某个点缩放的效果
								float transX = mGraffitiView.toTransX(mTouchCentreX, mToucheCentreXOnGraffiti);
								float transY = mGraffitiView.toTransY(mTouchCentreY, mToucheCentreYOnGraffiti);
								mGraffitiView.setTrans(transX, transY);
							}
						}
						return true;
					case MotionEvent.ACTION_POINTER_UP:
						mTouchMode -= 1;
						return true;
					case MotionEvent.ACTION_POINTER_DOWN:
						mTouchMode += 1;
						mOldScale = mGraffitiView.getScale();
						mOldDist = spacing(event);// 两点按下时的距离
						mTouchCentreX = (event.getX(0) + event.getX(1)) / 2;// 不用减trans
						mTouchCentreY = (event.getY(0) + event.getY(1)) / 2;
						mToucheCentreXOnGraffiti = mGraffitiView.toX(mTouchCentreX);
						mToucheCentreYOnGraffiti = mGraffitiView.toY(mTouchCentreY);
						mIsBusy = true; // 标志位多点触摸
						return true;
				}
				return true;
			}
		});

		findViewById(R.id.graffiti_txt_title).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) { // 长按标题栏显示原图
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						mGraffitiView.setJustDrawOriginal(true);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						mGraffitiView.setJustDrawOriginal(false);
						break;
				}
				return true;
			}
		});

		mViewShowAnimation = new AlphaAnimation(0, 1);
		mViewShowAnimation.setDuration(500);
		mViewHideAnimation = new AlphaAnimation(1, 0);
		mViewHideAnimation.setDuration(500);
		mHideDelayRunnable = new Runnable() {
			public void run() {
				hideView(mSettingsPanel);
			}

		};
		mShowDelayRunnable = new Runnable() {
			public void run() {
				showView(mSettingsPanel);
			}
		};

		findViewById(R.id.graffiti_btn_rotate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGraffitiView.rotate(mGraffitiView.getRotateDegree() + 90);
			}
		});
	}

	/**
	 * 初始化画笔颜色大小dialog
	 */
	private void initPaintSetDialog() {
		boolean fullScreen = (getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
		if (fullScreen) {
			paintDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		} else {
			paintDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		}
		//		paintDialog=new Dialog(this,R.style.myDialog);
		View paintSetView = LayoutInflater.from(GraffitiActivity.this).inflate(R.layout.graffiti_set_dialog, null);
		paintDialog.setContentView(paintSetView);
		etPaintSize = (EditText) paintSetView.findViewById(R.id.et_paint_size);
		mBtnColor = paintSetView.findViewById(R.id.btn_set_color);
		tvNegative = (STextView) paintSetView.findViewById(R.id.dialog_enter_btn_01);
		tvPositive = (STextView) paintSetView.findViewById(R.id.dialog_enter_btn_02);
		etPaintSize.setText(String.valueOf(DEFAULT_PAINT_SIZE));
		mBtnColor.setOnClickListener(mOnClickListener);
		tvNegative.setOnClickListener(dialogOnclickListener);
		tvPositive.setOnClickListener(dialogOnclickListener);
		paintDialog.setCanceledOnTouchOutside(false);
	}

	private View.OnClickListener dialogOnclickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.dialog_enter_btn_01) {
				dismissPaintSetDialog();
			}
			if (v.getId() == R.id.dialog_enter_btn_02) {
				setPaintSize();
				setPaintColor();
				dismissPaintSetDialog();
			}

		}
	};

	private void setPaintSize() {
		String inputPaintSize = etPaintSize.getText().toString().trim();
		if ((null == inputPaintSize) || (inputPaintSize.length() == 0)) {
			inputPaintSize = "15";//采取默认值15
		}
		float paintSize = Float.parseFloat(inputPaintSize);
		setPaintSize(paintSize);
	}

	private void setPaintSize(float paintSize) {
		if (mGraffitiView.isSelectedText()) {
			mGraffitiView.setSelectedTextSize(paintSize);
		} else if (mGraffitiView.getPen() == GraffitiView.Pen.TEXT) {
			mGraffitiView.setTextSize(paintSize);
		} else {
			mGraffitiView.setPaintSize(paintSize);
		}
	}

	private void setPaintColor() {
		if (selectColor != -1) {
			if (mGraffitiView.isSelectedText()) {
				mGraffitiView.setSelectedTextColor(selectColor);
			} else {
				mGraffitiView.setColor(selectColor);
			}
		}
		if (null != selectColorDrawable) {
			if (mGraffitiView.isSelectedText()) {
				mGraffitiView.setSelectedTextColor(ImageUtils.getBitmapFromDrawable(selectColorDrawable));
			} else {
				mGraffitiView.setColor(ImageUtils.getBitmapFromDrawable(selectColorDrawable));
			}
		}

	}

	private void showPaintSetDialog() {
		if (null != paintDialog) {
			paintDialog.show();
		}
	}

	private void dismissPaintSetDialog() {
		if (null != paintDialog) {
			paintDialog.dismiss();
		}
	}

	/**
	 * 计算两指间的距离
	 */

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private class GraffitiOnClickListener implements View.OnClickListener {

		private View mLastPenView, mLastShapeView;
		private boolean mDone = false;

		@Override
		public void onClick(View v) {
			mDone = false;
			if (v.getId() == R.id.iv_hand) {
				mGraffitiView.setPen(GraffitiView.Pen.HAND);
				mDone = true;
				mGraffitiView.setShape(GraffitiView.Shape.HAND_WRITE);
				etPaintSize.setText("15");
				setPaintSize(15f);
			} else if (v.getId() == R.id.iv_arrow) {
				mGraffitiView.setPen(GraffitiView.Pen.HAND);
				mDone = true;
				mGraffitiView.setShape(GraffitiView.Shape.ARROW);
				etPaintSize.setText("30");
				setPaintSize(30f);
			} else if (v.getId() == R.id.iv_circle) {
				mGraffitiView.setPen(GraffitiView.Pen.HAND);
				mDone = true;
				mGraffitiView.setShape(GraffitiView.Shape.HOLLOW_CIRCLE);
				etPaintSize.setText("5");
				setPaintSize(5f);
			} else if (v.getId() == R.id.iv_rect) {
				mGraffitiView.setPen(GraffitiView.Pen.HAND);
				mDone = true;
				mGraffitiView.setShape(GraffitiView.Shape.HOLLOW_RECT);
				etPaintSize.setText("5");
				setPaintSize(5f);
			} else if (v.getId() == R.id.iv_text) {
				mGraffitiView.setPen(GraffitiView.Pen.TEXT);
				mDone = true;
			}
			if (mDone) {
				if (mLastPenView != null) {
					mLastPenView.setSelected(false);
				}
				v.setSelected(true);
				mLastPenView = v;
				return;
			}

			if (v.getId() == R.id.iv_clear) {
				if (!(GraffitiParams.getDialogInterceptor() != null && GraffitiParams.getDialogInterceptor()
																					 .onShow(GraffitiActivity.this, mGraffitiView,
																							 GraffitiParams.DialogType.CLEAR_ALL))) {
					DialogController.showEnterCancelDialog(GraffitiActivity.this, getString(R.string.graffiti_clear_screen),
														   getString(R.string.graffiti_cant_undo_after_clearing),
														   new View.OnClickListener() {
															   @Override
															   public void onClick(View v) {
																   mGraffitiView.clear();
															   }
														   }, null);
				}
				mDone = true;
			} else if (v.getId() == R.id.iv_undo) {
				mGraffitiView.undo();
				mDone = true;
			} else if (v.getId() == R.id.btn_set_color) {
				if (!(GraffitiParams.getDialogInterceptor() != null && GraffitiParams.getDialogInterceptor()
																					 .onShow(GraffitiActivity.this, mGraffitiView,
																							 GraffitiParams.DialogType.COLOR_PICKER))) {
					new ColorPickerDialog(GraffitiActivity.this, mGraffitiView.getGraffitiColor().getColor(), "画笔颜色",
										  new ColorPickerDialog.OnColorChangedListener() {
											  public void colorChanged(int color) {
												  mBtnColor.setBackgroundColor(color);
												  selectColor = color;
											  }

											  @Override
											  public void colorChanged(Drawable color) {
												  mBtnColor.setBackgroundDrawable(color);
												  selectColorDrawable = color;
											  }
										  }).show();
				}
				mDone = true;
			}
			if (mDone) {
				return;
			}

			if (v.getId() == R.id.graffiti_btn_hide_panel) {
				mSettingsPanel.removeCallbacks(mHideDelayRunnable);
				mSettingsPanel.removeCallbacks(mShowDelayRunnable);
				v.setSelected(!v.isSelected());
				if (!mBtnHidePanel.isSelected()) {
					showView(mSettingsPanel);
				} else {
					hideView(mSettingsPanel);
				}
				mDone = true;
			} else if (v.getId() == R.id.graffiti_btn_finish) {
				mGraffitiView.save();
				mDone = true;
			} else if (v.getId() == R.id.graffiti_btn_back) {
				finish();
				return;
				//				if (!mGraffitiView.isModified()) {
				//					finish();
				//					return;
				//				}
				//				if (!(GraffitiParams.getDialogInterceptor() != null &&
				//					  GraffitiParams.getDialogInterceptor().onShow(GraffitiActivity.this, mGraffitiView, GraffitiParams.DialogType.SAVE))) {
				//					DialogController.showEnterCancelDialog(GraffitiActivity.this, getString(R.string.graffiti_saving_picture), null,
				//														   new View.OnClickListener() {
				//															   @Override
				//															   public void onClick(View v) {
				//																   mGraffitiView.save();
				//															   }
				//														   }, new View.OnClickListener() {
				//							@Override
				//							public void onClick(View v) {
				//								finish();
				//							}
				//						});
				//				}
				//				mDone = true;
			} else if (v.getId() == R.id.btn_centre_pic) {
				mGraffitiView.centrePic();
				mDone = true;
			} else if (v.getId() == R.id.btn_move_pic) {
				v.setSelected(!v.isSelected());
				mIsMovingPic = v.isSelected();
				if (mIsMovingPic) {
					Toast.makeText(getApplicationContext(), R.string.graffiti_moving_pic, Toast.LENGTH_SHORT).show();
				}
				mDone = true;
			}
			if (mDone) {
				return;
			}
			if (v.getId() == R.id.graffiti_text_edit) {
				mGraffitiView.editSelectedText();
				mDone = true;
			} else if (v.getId() == R.id.graffiti_text_remove) {
				mGraffitiView.removeSelectedText();
				mDone = true;
			}
			if (mDone) {
				return;
			}

			if (mLastShapeView != null) {
				mLastShapeView.setSelected(false);
			}
			v.setSelected(true);
			mLastShapeView = v;
		}
	}

	@Override
	public void onBackPressed() {

		if (mBtnMovePic.isSelected()) {
			mBtnMovePic.performClick();
			return;
		} else {
			findViewById(R.id.graffiti_btn_back).performClick();
		}

	}

	/**
	 * 放大缩小
	 */
	private class ScaleOnTouchListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					scalePic(v);
					v.setSelected(true);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					mIsScaling = false;
					v.setSelected(false);
					break;
			}
			return true;
		}
	}

	/**
	 * 缩放
	 */
	public void scalePic(View v) {
		if (mIsScaling) {
			return;
		}
		mIsScaling = true;
		mScale = mGraffitiView.getScale();

		// 确定当前屏幕中心点对应在GraffitiView中的点的坐标，之后将围绕这个点缩放
		mCenterXOnGraffiti = mGraffitiView.toX(mGraffitiView.getWidth() / 2);
		mCenterYOnGraffiti = mGraffitiView.toY(mGraffitiView.getHeight() / 2);

		if (v.getId() == R.id.btn_amplifier) { // 放大
			ThreadUtil.getInstance().runOnAsyncThread(new Runnable() {
				public void run() {
					do {
						mScale += 0.05f;
						if (mScale > mMaxScale) {
							mScale = mMaxScale;
							mIsScaling = false;
						}
						updateScale();
						try {
							Thread.sleep(TIME_SPAN);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (mIsScaling);

				}
			});
		} else if (v.getId() == R.id.btn_reduce) { // 缩小
			ThreadUtil.getInstance().runOnAsyncThread(new Runnable() {
				public void run() {
					do {
						mScale -= 0.05f;
						if (mScale < mMinScale) {
							mScale = mMinScale;
							mIsScaling = false;
						}
						updateScale();
						try {
							Thread.sleep(TIME_SPAN);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (mIsScaling);
				}
			});
		}
	}

	private void updateScale() {
		if (mUpdateScale == null) {

			mUpdateScale = new Runnable() {
				public void run() {
					// 围绕坐标(0,0)缩放图片
					mGraffitiView.setScale(mScale);
					// 缩放后，偏移图片，以产生围绕某个点缩放的效果
					float transX = mGraffitiView.toTransX(mGraffitiView.getWidth() / 2, mCenterXOnGraffiti);
					float transY = mGraffitiView.toTransY(mGraffitiView.getHeight() / 2, mCenterYOnGraffiti);
					mGraffitiView.setTrans(transX, transY);
				}
			};
		}
		ThreadUtil.getInstance().runOnMainThread(mUpdateScale);
	}

	private void showView(View view) {
		if (view.getVisibility() == View.VISIBLE) {
			return;
		}

		view.clearAnimation();
		view.startAnimation(mViewShowAnimation);
		view.setVisibility(View.VISIBLE);
		if (view == mSettingsPanel || mBtnHidePanel.isSelected()) {
			mGraffitiView.setAmplifierScale(-1);
		}
	}

	private void hideView(View view) {
		if (view.getVisibility() != View.VISIBLE) {
			if (view == mSettingsPanel && mGraffitiView.getAmplifierScale() > 0) {
				mGraffitiView.setAmplifierScale(-1);
			}
			return;
		}
		view.clearAnimation();
		view.startAnimation(mViewHideAnimation);
		view.setVisibility(View.GONE);
		//不使用放大器功能
		mGraffitiView.setAmplifierScale(-1);
	}

}
