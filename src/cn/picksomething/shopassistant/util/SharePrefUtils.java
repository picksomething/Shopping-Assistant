package cn.picksomething.shopassistant.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class SharePrefUtils {
	public final static String NEED_WIZARD = "need_wizard";
	
	
	// 向导
	public static void saveWizardFlagPreferece(Context context, boolean t) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(Constant.NEED_WIZARD, t);
		editor.commit();
	}

	public static boolean getWizardFlag(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(Constant.NEED_WIZARD, true);
	}
}
