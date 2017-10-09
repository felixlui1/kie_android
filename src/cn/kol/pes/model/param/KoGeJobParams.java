/*-----------------------------------------------------------

-- PURPOSE

--    获取工单信息的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoGeJobParams extends KoHttpParams {
	public KoGeJobParams(String wipName){
		
		setParam("uri", "/erp/osJob");
		setParam("wipName", wipName);
	}
}
