/*-----------------------------------------------------------

-- PURPOSE

--    更新设备维修信息请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;



public class KoAssetRepairParams extends KoHttpParams {
	
	public KoAssetRepairParams(String CHECK_ID, String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
							   String ACT_REPAIR_START, String ACT_REPAIR_END, String DOWN_TIME, 
							   String REPAIR_REMARKS, String FAILURE_CODE, String changedParts, String opCode){
		
		setParam("uri", "/erp/assetCheckRepair");
		
		setParam("checkId", CHECK_ID);
		setParam("lastUpdateDate", LAST_UPDATE_DATE);
		setParam("lastUpdatedBy", LAST_UPDATED_BY);
		setParam("actRepairStart", ACT_REPAIR_START);
		setParam("actRepairEnd", ACT_REPAIR_END);
		setParam("downTime", DOWN_TIME);
		setParam("repairRemarks", REPAIR_REMARKS);
		setParam("failureCode", FAILURE_CODE);
		setParam("changedParts", changedParts);
		setParam("opCode", opCode);
	}
}
