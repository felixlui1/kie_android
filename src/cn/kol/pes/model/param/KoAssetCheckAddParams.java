/*-----------------------------------------------------------

-- PURPOSE

--    添加点检记录请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetCheckAddParams extends KoHttpParams {
	
	public KoAssetCheckAddParams(String CREATION_DATE, String CREATED_BY, 
			 String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
			 String ASSET_ID, String CHECK_TIME, String CHECK_RESULT, 
			 String EST_REPAIR_START, String EST_REPAIR_END, 
			 String CHECK_REMARKS){
		
		setParam("uri", "/erp/assetCheckAdd");
		
		setParam("creationDate", CREATION_DATE);
		setParam("createdBy", CREATED_BY);
		setParam("lastUpdateDate", LAST_UPDATE_DATE);
		setParam("lastUpdatedBy", LAST_UPDATED_BY);
		setParam("assetId", ASSET_ID);
		setParam("checkTime", CHECK_TIME);
		setParam("checkResult", CHECK_RESULT);
		setParam("estRepairStart", EST_REPAIR_START);
		setParam("estRepairEnd", EST_REPAIR_END);
		setParam("checkRemarks", CHECK_REMARKS);
	}
}
