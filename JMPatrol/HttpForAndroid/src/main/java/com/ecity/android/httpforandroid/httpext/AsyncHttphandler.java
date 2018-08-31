package com.ecity.android.httpforandroid.httpext;

public interface AsyncHttphandler {
	public void onSuccess(String content);
	public void onFailure(String reason);
	public void onStart();
}
