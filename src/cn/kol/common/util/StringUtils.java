package cn.kol.common.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtils {


	public static boolean isValidURL(String s) {
		String lowerCaseS = s.toLowerCase();
		return lowerCaseS.startsWith("http://")
				|| lowerCaseS.startsWith("https://");
	}

	public static String URLEncode(String s) {
		if (s == null) {
			return null;
		}
		return URLEncoder.encode(s);
	}

	public static String getCacheDirString() {
		return ContextSaveUtils.getAppContext().getFilesDir().getAbsolutePath()
				+ "/";
	}

	public static String formatDate(int year, int month, int day, int hour,
			int min, int seds) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("%4d-%02d-%02d %02d:%02d:%02d", year, month, day,
				hour, min, seds);
	}
	
	public static String formatDateForFileName(int year, int month, int day, int hour, int min, int seds) {
		return String.format("%4d%02d%02d%02d%02d%02d", year, month, day, hour, min, seds);
	}

	public static String formatDateWithoutTime(int year, int month, int day) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("%4d-%02d-%02d", year, month + 1, day);
	}

	public static String newFormatDateTime(Calendar cal) {
		Date date = cal.getTime();
		// Wed Jan 08 2014 00:00:00 GMT+0800
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH);
		return formatter.format(date) + " 00:00:00 GMT+0800";
	}

	public static String formatDateWithoutTime(Calendar cal) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDateWithoutTime(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}

	public static String formatDateTime(Calendar cal) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}

	public static String formatDateOnlyWithTime(Calendar cal) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE));
	}
	
	public static String mFormatDateTime(Calendar cal) {
		Date date = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA);
		return formatter.format(date);
	}
	
	public static String formatDateTimeForFileName(Calendar cal) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDateForFileName(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}

}
