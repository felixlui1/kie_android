/*-----------------------------------------------------------

-- PURPOSE

--    KoAssetMachineFailItem设备故障类型的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;


public class KoAssetMachineFailItem extends Item {

	public String lookupCode;
	public String tag;
	public String meaning;
	
	public String getLookupCode() {
		return lookupCode;
	}
	
	public String getTag() {
		return tag;
	}
	
	public String getMeaning() {
		return meaning;
	}
}
