/*-----------------------------------------------------------

-- PURPOSE

--    KoParamItem是所有键值对类型的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;


public class KoParamItem extends Item {
	
    public String name;
	public String value;
	public String key;
	
	public KoParamItem() {
	    
	}
	
	public KoParamItem(String name, String value) {
	    this.name = name;
	    this.value = value;
	}
	
	public KoParamItem(String key, String name, String value) {
	    this.key = key;
        this.name = name;
        this.value = value;
    }
	
}
