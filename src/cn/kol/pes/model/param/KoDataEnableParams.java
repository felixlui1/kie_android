/*-----------------------------------------------------------

-- PURPOSE

--    获取服务器是否可用标志的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoDataEnableParams extends KoHttpParams {
	
	public KoDataEnableParams(){
		
		setParam("uri", "/erp/dataEnable");
	}
}
