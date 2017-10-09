/*-----------------------------------------------------------

-- PURPOSE

--    提交质量管理计划数据项列表的参数类.

-- History

--	  10-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoQaInsertDataParams extends KoHttpParams {
	
	public KoQaInsertDataParams(String createStaffNo, String wipEntityId, String opCode, String transactionId, String qaNvList, String qaChildNvList, boolean isManualAddedQa, String childPlanId){
		
		setParam("uri", "/erp/qaInsertData");
		
		setParam("createStaffNo", createStaffNo);
		setParam("wipEntityId", wipEntityId);
		setParam("opCode", opCode);
		if(childPlanId!=null) {
			setParam("childPlanId", childPlanId);
		}
		setParam("transactionId", transactionId);
		setParam("qaNvList", qaNvList!=null?qaNvList:"");
		setParam("qaChildNvList", qaChildNvList!=null?qaChildNvList:"");
		setParam("isManualAddedQa", isManualAddedQa?"Y":"N");
	}
	
}
