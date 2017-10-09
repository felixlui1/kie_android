/*-----------------------------------------------------------

-- PURPOSE

--    获取已开启工序列表的参数类.

-- History

--	  1-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoGetStartedOpListParams extends KoHttpParams {
	public KoGetStartedOpListParams(String lastUpdateByOrWipId){
		
		setParam("uri", "/erp/seqStartedList");
		setParam("lastUpdateByOrWipId", lastUpdateByOrWipId);
	}
}
