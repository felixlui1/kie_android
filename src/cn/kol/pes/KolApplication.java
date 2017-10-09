package cn.kol.pes;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class KolApplication extends Application{

	public static String VERSION = "0";
	public static int VERSION_CODE = 1;

	public static KolApplication mApp = null;

	@Override
	public void onCreate() {
		super.onCreate();

		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			VERSION = info.versionName;
			VERSION_CODE = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mApp = this;
	}
	
	public static KolApplication getApp() {
		return mApp;
	}

}
