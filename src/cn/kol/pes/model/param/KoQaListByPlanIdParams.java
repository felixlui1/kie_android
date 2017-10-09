/*-----------------------------------------------------------

-- PURPOSE

--    获取质量管理计划数据项列表的参数类.

-- History

--	  06-Mar-15  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoQaListByPlanIdParams extends KoHttpParams {
	public KoQaListByPlanIdParams(String planId, String wipId, String opCode){
		
		setParam("uri", "/erp/qaListByPlanId");
		setParam("planId", planId);
		setParam("wipId", wipId);
		setParam("opCode", opCode);
	}
}
