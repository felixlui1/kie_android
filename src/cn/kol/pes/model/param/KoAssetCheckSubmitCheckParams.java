package cn.kol.pes.model.param;

import java.util.List;

import cn.kol.pes.model.item.KoAssetCheckCheckItem;

public class KoAssetCheckSubmitCheckParams extends KoHttpParams {

	public KoAssetCheckSubmitCheckParams(String userId,String assetId, String checkId, List<KoAssetCheckCheckItem> checkValueList){
		
		setParam("uri", "/erp/assetCheckSubmitCheck");
		
		setParam("userId", userId);
		setParam("assetId", assetId);
		setParam("checkId", checkId);
		
		int size = checkValueList.size();
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<checkValueList.size(); i++) {
			KoAssetCheckCheckItem checkItem = checkValueList.get(i);
			sb.append(checkItem.itemSetId).append("~");
			sb.append(checkItem.itemSeq).append("~");
			sb.append(checkItem.queryText).append("~");
			sb.append(checkItem.queryType).append("~");
			sb.append(checkItem.checkResult).append("~");
			sb.append(checkItem.chkCycle);
			if(i < size-1) {
				sb.append("@");
			}
		}
		
		setParam("checkValueListString", sb.toString());
	}
}
