package com.ecity.android.httpforandroid.http;

//Task�����ӿ�
public abstract interface HttpBaseServiceListener<T> {
	  public static final short ON_OK = 1;
	  public static final short ON_CANCEL = 0;
	  public static final short ON_ERROR = -1;

	  public abstract void onCompletion(short completFlg, T localObject1);
	  public abstract void onError(Throwable paramThrowable);
}
