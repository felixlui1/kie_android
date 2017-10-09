/*-----------------------------------------------------------

-- PURPOSE

--    质量管理计划中需要固定填写的值的数据封装类

-- History

--	  02-Jan-15  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

public class KoQaValueItem extends Item {

	public String shortCode;
	public String description;
	
	public KoQaValueItem() {
		
	}
	
	public KoQaValueItem(String shortCode, String description) {
		this.shortCode = shortCode;
		this.description = description;
	}
	
	public String getShortCode() {
		return shortCode;
	}
	
	public String getDescription() {
		return description;
	}
}
