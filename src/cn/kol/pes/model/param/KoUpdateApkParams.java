/*-----------------------------------------------------------

-- PURPOSE

--    更新apk的参数类.

-- History

--	  17-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoUpdateApkParams extends KoHttpParams {
	
	public KoUpdateApkParams(String apkPadVersion) {
		
		setParam("uri", "/erp/updateApk");
		setParam("apkPadVersion", apkPadVersion);
	}
}
