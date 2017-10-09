/*-----------------------------------------------------------

-- PURPOSE

--    获取最大可投入数的适配类

-- History

--	  23-Oct-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.kol.pes.model.item.KoAssetCheckItem;


public class KoOpMaxQuanAdapter extends DefaultBasicAdapter {

	public String maxQuan;
	private List<KoAssetCheckItem> opAssetList;
	
	public List<KoAssetCheckItem> getOpAssetList() {
		return opAssetList;
	}
	
	public void addOpAsset(KoAssetCheckItem asset) {
		if(opAssetList == null) {
			opAssetList = new ArrayList<KoAssetCheckItem>();
		}
		opAssetList.add(asset);
	}
	
}
