/*-----------------------------------------------------------

-- PURPOSE

--    数据处理相关的公用函数

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.io.File;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import cn.kol.pes.KolApplication;
import cn.kol.pes.model.item.KoParamItem;

public class KoDataUtil {
	
	private static final String KEY_IS_USE_TEST = "key_is_use_test";
	
	public static boolean isStringNotNull(String s) {
		return s!=null && s.trim().length()>0;
	}
	
	public static Boolean isUseTestUrl() {
		return ContextSaveUtils.getSharePreBoolean(KEY_IS_USE_TEST, KEY_IS_USE_TEST+KolApplication.VERSION, Constants.IS_USE_TEST);
	}
	
	public static void toggleSetIsUseTestUrl() {
		boolean isTest = isUseTestUrl();
		ContextSaveUtils.saveSharePre(KEY_IS_USE_TEST, KEY_IS_USE_TEST+KolApplication.VERSION, !isTest);
	}
	
	//获取星期，三位字母
	public static String getWeekInEnglish() {
		String date = new SimpleDateFormat("EEE",Locale.US).format(new Date());
		return date.toUpperCase();
	}
	
	//根据字符串解析出时间对象
	public static Calendar convertStringToCal(String calString) {
		try {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = df.parse(calString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Calendar.getInstance();
	}
	
	//根据字符串解析出时间对象
	public static Calendar convertDateStringToCal(String calString) {
		try {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date = df.parse(calString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Calendar.getInstance();
	}
	
	public static boolean isCanConvertStringToCal(String calString) {
		try {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.parse(calString);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//根据当前时间生成一个拍照照片文件的文件名
	public static String picPathDescStringToStringGetFileName(String pathListString) {
		List<KoParamItem> picListData = picPathDescStringToList(pathListString);
		for(KoParamItem p : picListData) {
			if(p != null && p.name!=null) {
				p.name = getFileNameFromPath(p.name);
			}
		}
		
		return picPathDescListToString(picListData);
	}
	
	//将N个照片文件的文件路径名和描述信息组合成一个字符串，便于http参数上传
	public static String picPathDescListToString(List<KoParamItem> picListData) {
		StringBuilder picPathDescSb = new StringBuilder();
		if(picListData != null) {
			for(KoParamItem p : picListData) {
				if(p!=null && p.name!=null) {
	
					if(p.value==null) {
						p.value="none";
					}
					
					picPathDescSb.append(p.name).append(",").append(p.value);
					
					if(picListData.indexOf(p)<(picListData.size()-1)) {
						picPathDescSb.append("#");
					}
				}
			}
		}
		
		return picPathDescSb.toString();
	}
	
	public static List<KoParamItem> picPathDescStringToList(String pathListString) {
		
		List<KoParamItem> picListData = new ArrayList<KoParamItem>();
		
		if (pathListString != null && pathListString.length() > 1) {
			
			String[] prarmArr = pathListString.split("#");
			for(String item : prarmArr) {
				String[] itemArr = item.split(",");
				picListData.add(new KoParamItem(itemArr[0], itemArr[1]));
			}
		}
		
		return picListData;
	}
	
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	public static String getFileNameFromPath(String filePath) {
		
		if(filePath!=null && filePath.contains(File.separator)) {
			String[] arr = filePath.split(File.separator);
			if(arr!=null && arr.length>0) {
				return arr[arr.length-1];
			}
		}
		
		return "";
	}
	
	public static String formatDate(int year, int month, int day, int hour, int min, int seds) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("%4d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, min, seds);
	}
	
	public static String formatDateTime(Calendar cal) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}
	
	public static boolean isValidNumber(String pswd) {
		String p_s = "^[0-9]+$";
		Pattern pat_s = Pattern.compile(p_s);
		
		if(pswd.startsWith("0") && pswd.length()>1) {
			return false;
		}
		
//		if(pswd.contains("-") || pswd.contains("+") || pswd.contains("*")|| pswd.contains("/")) {
//			return false;
//		}
		
		String baseString = "1234567890";
		char[] arr = pswd.toCharArray();
		
		for (int i=0; i<arr.length; i++) {
			if(!baseString.contains(String.valueOf(arr[i]))) {
				return false;
			}
		}

		return pat_s.matcher(pswd).matches();
	}
	
	public static boolean isValidFloatNumber(String pswd) {
		if(pswd == null) {
			return false;
		}
		
//		String p_s = "^(-[0-9][0-9]*)+(.[0-9]{1,9})?$";
//		Pattern pat_s = Pattern.compile(p_s);
//		
//		if(pat_s.matcher(pswd).matches()) {
//
			if(pswd.length()>1 && pswd.startsWith("0") && !pswd.startsWith("0.")) {
				return false;	
			}
			
			if(pswd.length()>1 && pswd.startsWith("-0") && !pswd.startsWith("-0.")) {
				return false;	
			}
			
			if(pswd.length()>1 && (pswd.startsWith("-.") || pswd.startsWith(".")  || pswd.startsWith("+."))) {
				return false;	
			}
//
//			if(pswd.endsWith(".") || pswd.contains("*")) {
//				return false;
//			}
//			
//			if(pswd.contains("-") || pswd.contains("+") || pswd.contains("*")|| pswd.contains("/")) {
//				return false;
//			}
//			
//			String baseString = "-1234567890.";
//			char[] arr = pswd.toCharArray();
//			
//			for (int i=0; i<arr.length; i++) {
//				if(!baseString.contains(String.valueOf(arr[i]))) {
//					return false;
//				}
//			}
//			
//			return true;
//		}
//		
//		return false;
		
		try{
			float value = Float.valueOf(pswd);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static long revertDaysToMills(int days) {
        return days*24*60*60*1000L;
    }
    
    public static int revertMillsToDays(long mills) {
        if(mills<0L) {
            return -1;
        }else {
            return (int) (mills/24/60/60/1000);
        }
    }
    
    public static double revertMillsToDaysDouble(long mills) {
        if(mills<0L) {
            return -1;
        }else {
            return (double) (mills/24d/60d/60d/1000d);
        }
    }
    
    public final static String hexMD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
