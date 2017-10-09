/*-----------------------------------------------------------

-- PURPOSE

--    常用的功能函数

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/


package cn.kol.common.util;

import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class KoCommonUtil {

	//获取客户端语言设置
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	//判断是否连接网络
	public static boolean isConnectInternet() {
        
        ConnectivityManager conManager = (ConnectivityManager) ContextSaveUtils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
