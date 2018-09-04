package com.ecity.cswatersupply.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;

public class InitPatrolDataHandler extends Handler{
	//开始获取任务有关数据
	public static final int GET_INSPECT_DATA_BEGIN = 0;
	public static final int GET_INSPECT_DATA_ERROR = 1;
	public static final int GET_INSPECT_DATA_END = 2;
	public static final int GET_PLANTASK_DATA_BEGIN = 3;
	public static final int GET_PLANTASK_DATA_ERROR = 4;
	public static final int GET_PLANTASK_DATA_END = 5;
	public static final int MAINACTIVITY_INIT_BEGIN= 6;
	
	private static InitPatrolDataHandler instance;
    private Activity mactivity;
    static {
        instance = new InitPatrolDataHandler();
    }

    private InitPatrolDataHandler() {
    }

    public static InitPatrolDataHandler getInstance() {
        return instance;
    }

    public void init(Activity activity) {
    	mactivity = activity;
    }
    
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case MAINACTIVITY_INIT_BEGIN:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_initdata_brgin);
			break;
		case GET_INSPECT_DATA_BEGIN:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_inspect_begin);
			break;
		case GET_INSPECT_DATA_ERROR:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_inspect_error);
			break;
		case GET_INSPECT_DATA_END:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_inspect_end);
			break;
		case GET_PLANTASK_DATA_BEGIN:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_plantask_begin);
			break;
		case GET_PLANTASK_DATA_ERROR:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_plantask_error);
			break;
		case GET_PLANTASK_DATA_END:
			LoadingDialogUtil.show(mactivity, R.string.mainactivity_getdata_plantask_end);
			LoadingDialogUtil.dismiss();
			break;
		default:
			break;
		}
	}
}
