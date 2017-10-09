package cn.kol.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.kol.pes.KolApplication;

public class ContextSaveUtils {


	private static Context mContext = KolApplication.getApp();

	//或许app的上下文
	public static Context getAppContext() {
		return KolApplication.getApp();
	}

	public static String getSharePreString(String sharePreName, String key) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		return sp.getString(key, null);
	}

	public static int getSharePreInt(String sharePreName, String key) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		return sp.getInt(key, 0);
	}

	public static long getSharePreLong(String sharePreName, String key) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
					Activity.MODE_PRIVATE);
			return sp.getLong(key, 0L);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0L;
	}

	public static int getSharePreInt(String sharePreName, String key,
			int defaultValue) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void saveSharePre(String sharePreName, String key,
			String value) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString(key, value);
		ed.commit();
	}

	public static void saveSharePre(String sharePreName, String key, int value) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt(key, value);
		ed.commit();
	}

	public static void saveSharePre(String sharePreName, String key, long value) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putLong(key, value);
		ed.commit();
	}
	
	public static void saveSharePre(String sharePreName, String key, boolean value) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean(key, value);
		ed.commit();
	}

	public static boolean getSharePreBoolean(String sharePreName, String key,
			boolean defaultBoolen) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		return sp.getBoolean(key, defaultBoolen);
	}

	public static void deleteSharePre(String sharePreName, String key) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.remove(key);
		ed.commit();
	}

	public static boolean isContainsKeyInSharePre(String sharePreName,
			String key) {
		SharedPreferences sp = mContext.getSharedPreferences(sharePreName,
				Activity.MODE_PRIVATE);
		return sp.contains(key);
	}

}
