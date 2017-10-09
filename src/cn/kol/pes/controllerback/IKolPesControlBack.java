/*-----------------------------------------------------------

-- PURPOSE

--    IKolPesControlBack是所有网络请求回调的父类.由于 此应用的网络请求数量相对较少，所以我们采用一个类接受所有网络请求回调。每个界面只需复写自己需要的回调即可。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.controllerback;

import java.util.HashMap;
import java.util.List;

import cn.kol.pes.model.item.KoAssetCheckAssetItem;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.model.item.KoAssetMachineFailItem;
import cn.kol.pes.model.item.KoChangedPartItem;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.model.item.KoParamItem;
import cn.kol.pes.model.item.KoPushMsgItem;
import cn.kol.pes.model.item.KoQaNeedFillItem;
import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;


public interface IKolPesControlBack extends IControlBack {
	
	public void dateEnableBack(boolean isSuc, HashMap<String, KoWeekItem> weekMap, String msg);
    
	public void loginBack(boolean isSuc, KoLoginAdapter userData, String msg);
	
	public void getJobBack(boolean isSuc, List<KoJobItem> gongDanList, String msg);
	
	public void getOpBack(boolean isSuc, List<KoOpItem> gongXuList, String msg);
	
	public void assetCheckAddBack(boolean isSuc, String checkId, String msg);
	
	public void assetInsertPicPathBack(boolean isSuc, String msg);
	
	public void assetCheckGetErrorListBack(boolean isSuc, String msg, List<KoAssetCheckItem> assetList);
	
	public void assetGetMachineFailListBack(boolean isSuc, String msg, List<KoAssetMachineFailItem> failList);
	
	public void assetCheckRepairBack(boolean isSuc, String msg);
	
	public void assetGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter);
	
	public void opGetMaxQuanBack(boolean isSuc, String maxQuan, List<KoAssetCheckItem> opAssetList, String msg);
	
	public void opStartBack(boolean isSuc, String maxQuan, String msg);
	
	public void opEndBack(boolean isSuc, String maxQuan, String msg);
	
	public void opStartedListBack(boolean isSuc, List<KoOpStartedItem> opList, boolean isOpCompleted, String curWorkingOpCode, String msg);
	public void opMoveAllListBack(boolean isSuc, List<KoOpStartedItem> opList, String msg);
	
	public void qaListNeedFillBack(boolean isSuc, boolean mIsLastSeq, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, List<KoParamItem> childPlanIdList, String msg, String timeBuffer, String scrapQuanTotal);
	
	public void qaListByPlanIdBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, String msg);
	
	public void qaListManualAddBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, String msg);
	
	public void submitQaDataBack(boolean isSuc, String msg);
	
	public void getPushMsgListBack(boolean isSuc, List<KoPushMsgItem> msgList, String msg);
	
	public void getUpdateApkBack(boolean isSuc, KoUpdateApkAdapter updateData, String msg);
	
	public void getOpCheckBeforeStartBack(boolean isSuc, String timeBuffer, String msg);
	public void getOpDeleteBack(boolean isSuc, String msg);
	
	public void getCheckItemListDataBack(boolean isSuc, List<KoAssetCheckCheckItem> checkList, String msg);
	
	public void assetCheckGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter);
	
	public void assetCheckSubmitCheckBack(boolean isSuc, String msg);
	
	public void assetCheckAssetListBack(boolean isSuc, List<KoAssetCheckAssetItem> assetList, String msg);
	
	public void assetCheckGetSetAssetCheckListBack(boolean isSuc, List<KoAssetCheckSetAssetCheckItem> assetList, String msg);
	
	public void assetCheckUpdateSetAssetCheckListBack(boolean isSuc, String msg);
	
	public void assetCheckGetChangedPartsBack(boolean isSuc, List<KoChangedPartItem> partList, String msg);
	
	public void assetCheckCancelCheckAssetBack(boolean isSuc, String msg);
}
