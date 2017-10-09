/*-----------------------------------------------------------

-- PURPOSE

--    缓存非持久存留的信息。用于便捷地在不同Activity间传递数据。多用于具有前后流程联系的Activity传递数据

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.util.HashMap;

import cn.kol.pes.model.NetworkManager;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;


public class CacheUtils {
    
    private static KoLoginAdapter mKoLoginAdapter;
    private static KoJobItem mSelectedJob;
    private static KoOpItem mSelectedOp;
    private static KoAssetCheckItem mAssetCheckItem;
    private static HashMap<String, KoWeekItem> mWeekMap;
    
    //清空登陆信息等
    public static void clearAllCache() {
    	mKoLoginAdapter = null;
    	mSelectedJob = null;
    	mSelectedOp = null;
    	mAssetCheckItem = null;
    	NetworkManager.clearNetInstance();
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    public static HashMap<String, KoWeekItem> getWeekMap() {
    	return mWeekMap;
    }
    
    public static void setWeekMap(HashMap<String, KoWeekItem> week) {
    	mWeekMap = week;
    }
    
    //点击点检设备列表时，保存那个item的数据，供打开的维修界面使用
    public static void setSelectedAssetCheckItem(KoAssetCheckItem item) {
    	mAssetCheckItem = item;
    }
    
    //保存选中的点检列表项数据s
    public static KoAssetCheckItem getSelectedAssetCheckItem() {
    	return mAssetCheckItem;
    }
    
    //保存登陆信息
    public static void setLoginUserInfo(KoLoginAdapter user) {
    	mKoLoginAdapter = user;
    }
    
    public static KoLoginAdapter getLoginUserInfo() {
    	return mKoLoginAdapter;
    }
    
    //点击开始工序后，保存工单信息
    public static void setSelectedJob(KoJobItem dan) {
    	mSelectedJob = dan;
    }
    
    public static KoJobItem getSelectedJob() {
    	return mSelectedJob;
    }
    
    //点击开始工序后，保存工序数据
    public static void setSelectedOp(KoOpItem op) {
    	mSelectedOp = op;
    }
    
    public static KoOpItem getSelectedOp() {
    	return mSelectedOp;
    }
}
