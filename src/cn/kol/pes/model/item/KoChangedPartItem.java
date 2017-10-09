/*-----------------------------------------------------------

-- PURPOSE

--    KoParamItem是所有键值对类型的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;


public class KoChangedPartItem extends Item {
	
    public String name;
	
	public KoChangedPartItem() {
	    
	}
	
	public KoChangedPartItem(String name) {
	    this.name = name;
	}
}
