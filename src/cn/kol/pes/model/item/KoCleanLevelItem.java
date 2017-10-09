/*-----------------------------------------------------------

-- PURPOSE

--    KoParamItem是所有键值对类型的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;


public class KoCleanLevelItem extends Item {
	
    public String name;
	public int level;
	
	public KoCleanLevelItem() {
	    
	}
	
	public KoCleanLevelItem(String name, int level) {
	    this.name = name;
	    this.level = level;
	}
}
