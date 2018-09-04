package com.ecity.mobile.android.qrcodelibrary.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.ecity.mobile.android.qrcodelibrary.R;
import com.ecity.mobile.android.qrcodelibrary.camera.CameraManager;
import com.ecity.mobile.android.qrcodelibrary.executor.ResultHandler;

public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private TextView common_title_TV_left;
	private TextView common_title_TV_right;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private TitleView title;
	//private Button from_gallery;
	private final int from_photo = 010;
	
	private static final int PARSE_BARCODE_SUC = 3035;
	private static final int PARSE_BARCODE_FAIL = 3036;
	
	
	String photoPath;
	ProgressDialog mProgress;

	// Dialog dialog;

	enum IntentSource {
		ZXING_LINK, NONE
	}

	Handler barHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				//viewfinderView.setRun(false);
				showScanResult((String) msg.obj);
				break;
			case PARSE_BARCODE_FAIL:
				//showDialog((String) msg.obj);
				if (mProgress != null && mProgress.isShowing()) {
					mProgress.dismiss();
				}
				new AlertDialog.Builder(CaptureActivity.this).setTitle(getResources().getString(R.string.barcode_memo)).setMessage(getResources().getString(R.string.barcode_msg_fail)).setPositiveButton(getResources().getString(R.string.barcode_confirm), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Window window = getWindow();
		// window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_qrcode_scanning);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);
		statusView = (TextView) findViewById(R.id.status_view);
		common_title_TV_left = (TextView) findViewById(R.id.common_title_TV_left);
		common_title_TV_right = (TextView) findViewById(R.id.common_title_TV_right);
		setListeners();
	}

	public void showScanResult(final String msg) {
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.dismiss();
		}
		Intent intent = new Intent();
		intent.putExtra("data", msg);
		setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
		finish();//此处一定要调用finish()方法
		
//		String sp = "http://ecity:";
//		if(msg.startsWith(sp)){
//			String chestcardid = msg.substring(sp.length());
//			if(isNumeric(chestcardid))
//			{
//				
//			}else{
//				AlertDialog alertDialog = new AlertDialog.Builder(this). 
//		                 setTitle(""). 
//		                 setMessage(""+msg). 
//		                 setIcon(R.drawable.info).setPositiveButton("",null).create(); 
//		         alertDialog.show(); 
//			}
//		}
//		else{
//			AlertDialog alertDialog = new AlertDialog.Builder(this). 
//	                 setTitle(""). 
//	                 setMessage(""+msg). 
//	                 setIcon(R.drawable.info).setPositiveButton("",null).create(); 
//	         alertDialog.show(); 
//		}

	}
	public static boolean isNumeric(String str){
		   for (int i = str.length();--i>=0;){   
		    if (!Character.isDigit(str.charAt(i))){
		     return false;
		    }
		   }
		   return true;
		 }
	public void setListeners() {
		common_title_TV_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});

		common_title_TV_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringUtils.showPictures(CaptureActivity.this, from_photo);
			}
		});
	}

	public String parsLocalPic(String path) {
		String parseOk = null;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; 
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + "   " + h);
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader2 = new QRCodeReader();
		Result result;
		try {
			result = reader2.decode(bitmap1, hints);
			android.util.Log.i("steven", "result:" + result);
			parseOk = result.getText();

		} catch (NotFoundException e) {
			parseOk = null;
		} catch (ChecksumException e) {
			parseOk = null;
		} catch (FormatException e) {
			parseOk = null;
		}
		return parseOk;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		android.util.Log.i("steven", "data.getData()" + data);
		if (data != null) {
			mProgress = new ProgressDialog(CaptureActivity.this);
			mProgress.setMessage(getResources().getString(R.string.barcode_msg_scaning));
			mProgress.setCancelable(false);
			mProgress.show();
			final ContentResolver resolver = getContentResolver();
			if (requestCode == from_photo) {
				if (resultCode == RESULT_OK) {
					Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
					if (cursor.moveToFirst()) {
						photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							String result = parsLocalPic(photoPath);
							if (result != null) {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_SUC;
								m.obj = result;
								barHandler.sendMessage(m);
							} else {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_FAIL;
								m.obj = "Failed";
								barHandler.sendMessage(m);
							}
							Looper.loop();
						}
					}).start();
				}

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler = null;
		lastResult = null;
		resetStatusView();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		inactivityTimer.onResume();
		source = IntentSource.NONE;
		decodeFormats = null;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		if (mProgress!= null) {
			mProgress.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK) && lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// �����ʼ�����棬���ó�ʼ�����
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	private static ParsedResult parseResult(Result rawResult) {
		return ResultParser.parseResult(rawResult);
	}

	// ������ά��
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;

		ResultHandler resultHandler = new ResultHandler(parseResult(rawResult));
		boolean fromLiveScan = barcode != null;
		if (barcode == null) {
			android.util.Log.i("zzz", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
			android.util.Log.i("zzz", "resultHandler.getType().toString():" + resultHandler.getType().toString());
			android.util.Log.i("zzz", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());
		} else {
			showScanResult(resultHandler.getDisplayContents().toString());
		}
	}

	// ��ʼ���������CaptureActivityHandler����
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.barcode_action_title));
		builder.setMessage(getString(R.string.barcode_msg_camera_framework_bug));
		builder.setPositiveButton(R.string.barcode_confirm, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		statusView.setText(R.string.barcode_msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}