package cn.kol.common.util;

public class Constants {
	
	public static final boolean IS_USE_TEST = true;//android
    
    public static final boolean IS_OPEN_LOG = false;//日志开android
    
    private static final String KO_SERVER_URL_RELEASE = "http://192.168.38.50:8080";

    private static final String KO_SERVER_URL_TEST = "http://192.168.57.109:8080";//服务器地址 local
    //private static final String KO_SERVER_URL_TEST = "http://192.168.38.49:8080";//服务器地址 test
    //private static final String KO_SERVER_URL_TEST = "http://192.168.1.102:8080";//服务器地址
    
    public static String getServerUrl() {
    	return KoDataUtil.isUseTestUrl() ? KO_SERVER_URL_TEST : KO_SERVER_URL_RELEASE;
    }
}
