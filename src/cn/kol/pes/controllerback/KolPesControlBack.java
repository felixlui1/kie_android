/*-----------------------------------------------------------

-- PURPOSE

--    KolPesControlBack是所有网络请求回调的类.由于 此应用的网络请求数量相对较少，所以我们采用一个类接受所有网络请求回调。每个界面只需复写自己需要的回调即可。
--	       回调后具体的处理需要在复写的函数中实现

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

public class KolPesControlBack implements IKolPesControlBack {

	@Override
	public void dateEnableBack(boolean isSuc, HashMap<String, KoWeekItem> weekMap, String msg) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void loginBack(boolean isSuc, KoLoginAdapter userData, String msg) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void getJobBack(boolean isSuc, List<KoJobItem> gongDanList, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getOpBack(boolean isSuc, List<KoOpItem> gongXuList, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void assetCheckAddBack(boolean isSuc, String checkId, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void assetCheckGetErrorListBack(boolean isSuc, String msg, List<KoAssetCheckItem> assetList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetGetMachineFailListBack(boolean isSuc, String msg, List<KoAssetMachineFailItem> failList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckRepairBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetInsertPicPathBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opGetMaxQuanBack(boolean isSuc, String maxQuan, List<KoAssetCheckItem> opAssetList, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opStartBack(boolean isSuc, String maxQuan, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void opEndBack(boolean isSuc, String maxQuan, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void qaListNeedFillBack(boolean isSuc, boolean mIsLastSeq, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, List<KoParamItem> childPlanIdList, String msg, String timeBuffer, String scrapQuanTotal) {
		// TODO Auto-generated method stub
	}

	@Override
	public void submitQaDataBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void opMoveAllListBack(boolean isSuc, List<KoOpStartedItem> opList, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getPushMsgListBack(boolean isSuc, List<KoPushMsgItem> msgList, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getUpdateApkBack(boolean isSuc, KoUpdateApkAdapter updateData,
			String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOpCheckBeforeStartBack(boolean isSuc, String timeBuffer, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOpDeleteBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void qaListManualAddBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd,
			List<KoQaNeedFillItem> qaList, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void opStartedListBack(boolean isSuc, List<KoOpStartedItem> opList,
			boolean isOpCompleted, String curWorkingOpCode, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void qaListByPlanIdBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd,
			List<KoQaNeedFillItem> qaList, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCheckItemListDataBack(boolean isSuc, List<KoAssetCheckCheckItem> checkList, String msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public void assetCheckGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter) {
		// TODO Auto-generated method stub
	}

	@Override
	public void assetCheckSubmitCheckBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckAssetListBack(boolean isSuc, List<KoAssetCheckAssetItem> assetList, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckGetSetAssetCheckListBack(boolean isSuc, List<KoAssetCheckSetAssetCheckItem> assetList,
			String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckUpdateSetAssetCheckListBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckGetChangedPartsBack(boolean isSuc, List<KoChangedPartItem> partList, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assetCheckCancelCheckAssetBack(boolean isSuc, String msg) {
		// TODO Auto-generated method stub
		
	}

	
}
