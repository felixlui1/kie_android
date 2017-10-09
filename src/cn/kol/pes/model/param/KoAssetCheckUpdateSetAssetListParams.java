package cn.kol.pes.model.param;

import java.util.List;

import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;

public class KoAssetCheckUpdateSetAssetListParams extends KoHttpParams {

	public KoAssetCheckUpdateSetAssetListParams(String date, String shift, String staffNo, List<KoAssetCheckSetAssetCheckItem> assetList) {
		
		setParam("uri", "/erp/assetCheckUpdateSetAssetCheckList");
		
		setParam("date", date);
		setParam("shift", shift);
		setParam("staffNo", staffNo);
		
		int size = assetList.size();
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<assetList.size(); i++) {
			KoAssetCheckSetAssetCheckItem checkItem = assetList.get(i);
			sb.append(checkItem.assetId).append(",");
			sb.append(checkItem.assetTagNum).append(",");
			sb.append(checkItem.assetDescription).append(",");
			sb.append(checkItem.scheduledId).append(",");;
			sb.append(checkItem.checkedId);
			if(i < size-1) {
				sb.append("@");
			}
		}
		
		setParam("assetList", sb.toString());
	}
}