/*-----------------------------------------------------------

-- PURPOSE

--    升级APK的适配类

-- History

--	  17-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;


public class KoUpdateApkAdapter extends DefaultBasicAdapter {

	public String needUpdate;
	public String updateApkUrl;
	public String updateMsg;
	public boolean isForceUpdate;
	
	public boolean isNeedUpdate() {
		return "Y".equals(needUpdate);
	}
	
	public void setIsForceUpdate(String isForce) {
		isForceUpdate = "Y".equals(isForce);
	}
}
