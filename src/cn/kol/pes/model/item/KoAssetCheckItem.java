/*-----------------------------------------------------------

-- PURPOSE

--    KoAssetCheckItem设备点检列表的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;


public class KoAssetCheckItem extends Item {

	public int checkId;
	public String creationDate;
	public int createdBy;
	public String createdByName;
	
	public String lastUpdateDate;
	public int lastUpdatedBy;
	
	public String assetId;
	public String assetName;
	public String assetTagNum;
	public String assetOpDscr="";
	public String checkTime;
	public String checkResult;
	
	public String estRepairStart;
	public String estRepairEnd;
	public String checkRemarks;
	
	public int failureCode;
	
	public String opCode;
	
	
	public int getCheckId() {
		return checkId;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public int getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	public String getAssetId() {
		return assetId;
	}
	
	public String getCheckTime() {
		return checkTime;
	}
	
	public String getCheckResult() {
		return checkResult;
	}
	
	public String getEstRepairStart() {
		return estRepairStart;
	}
	
	public String getEstRepairEnd() {
		return estRepairEnd;
	}
	
	public String getCheckRemarks() {
		return checkRemarks;
	}

	public int getFailureCode() {
		return failureCode;
	}

}
