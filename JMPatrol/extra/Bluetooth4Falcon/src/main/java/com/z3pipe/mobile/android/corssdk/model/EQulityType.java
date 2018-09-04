package com.z3pipe.mobile.android.corssdk.model;

import android.content.res.Resources;
import com.enrique.bluetooth4falcon.R;

public enum EQulityType {
	NOTPOSITIONED("RECEIVENEWLOCATION"), /* 未定位 */
	SINGLEPOINT("SINGLEPOINT"), /* 单点 */
	PSEUDORANGE("PSEUDORANGE"), /* 伪距 */
	FLOATINGPOINT("FLOATINGPOINT"), /* 浮点 */
	FIXED("FIXED");/* 固定 */

	private final String value;

	public String getValue() {
		return value;
	}

	EQulityType(String value) {
		this.value = value.toUpperCase();
	}

	public static EQulityType getTypeByValue(String type) {
		for (EQulityType e : values()) {
			if (e.getValue().equalsIgnoreCase(type)) {
				return e;
			}
		}
		return null;
	}

	public static String getStrByValue(EQulityType type) {
		String equlity = "";
		switch (type) {
			case SINGLEPOINT:
				equlity = Resources.getSystem().getString(R.string.str_cors_equlity_singlepoint);
				break;
			case PSEUDORANGE:
				equlity = Resources.getSystem().getString(R.string.str_cors_equlity_pseudorange);
				break;
			case FLOATINGPOINT:
				equlity = Resources.getSystem().getString(R.string.str_cors_equlity_floatingpoint);
				break;
			case FIXED:
				equlity = Resources.getSystem().getString(R.string.str_cors_equlity_fixed);
				break;
			case NOTPOSITIONED:
				equlity = Resources.getSystem().getString(R.string.str_cors_equlity_notposition);
			default:
				break;
		}
		return equlity;
	}
}