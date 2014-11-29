package cn.picksomething.shopassistant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class SharePrefUtils {

	
	// 向导
	public static void saveGuideFlagPreferece(Context context, boolean t) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(Constant.NEED_GUIDE, t);
		editor.commit();
	}

	public static boolean getGuideFlag(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(Constant.NEED_GUIDE, true);
	}
	
	public static void saveLoginFlagPreferece(Context context, boolean t) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(Constant.NEED_LOGIN, t);
		editor.commit();
	}
	
	public static boolean getLoginFlag(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(Constant.NEED_LOGIN, true);
	}

}
