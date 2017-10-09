/*-----------------------------------------------------------

-- PURPOSE

--    已开启的工序的数据封装类

-- History

--	  31-Oct-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

public class KoOpStartedItem extends Item {

	public String transactionId;
	public String wipEntityName;
	public String creationDate;
	public String createdBy;
	public String lastUpdateDate;
	public String lastUpdateBy;
	public String fmOperationCode;
	public String opDesc;
	public String trxQuantity;
	public String scrapQuantity;
	public String assetDesc;
	public String assettagNumber;
	public String interfaced;
	public String opStartDate;
	public String opEndDate;
	public String pctComplete;
	
	//job values
	public KoJobItem jobObj = new KoJobItem();
	
	
	public String getWipEntityName() {
		return wipEntityName;
	}
	
	public String getCreationDate() {
		return creationDate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	
	public String getFmOperationCode() {
		return fmOperationCode;
	}
	
	public String getOpDesc() {
		return opDesc;
	}
	
	public String getTrxQuantity() {
		return trxQuantity;
	}
	
	public String getAssetDesc() {
		return assetDesc;
	}
	
	public String getAssettagNumber() {
		return assettagNumber;
	}
	
	public String getOpStartDate() {
		return opStartDate;
	}
}
