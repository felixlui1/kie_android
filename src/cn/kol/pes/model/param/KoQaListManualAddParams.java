/*-----------------------------------------------------------

-- PURPOSE

--    获取人工添加的质量收集计划的参数类.

-- History

--	  03-Dec-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoQaListManualAddParams extends KoHttpParams {
	
	public KoQaListManualAddParams(String wipId, String opCode) {
		
		setParam("uri", "/erp/qaListManual");

		setParam("wipId", wipId);
		setParam("opCode", opCode);
	}
	
}
