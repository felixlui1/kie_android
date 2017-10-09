/*-----------------------------------------------------------

-- PURPOSE

--    开始工序的参数类.

-- History

--	  23-Oct-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

import java.util.List;



public class KoOpStartParams extends KoHttpParams {
	
	public KoOpStartParams(String createdBy, String lastUpdatedBy, 
							String wipEntityId, String wipEntityName, String fmOperationCode, String curOperationId,
							String trxQuantity,
							List<String> assetIdList, String opStart, String seqId, String canJump){
		
		setParam("uri", "/erp/seqStart");
		
		setParam("createdBy", createdBy);
		setParam("lastUpdatedBy", lastUpdatedBy);
		setParam("wipEntityId", wipEntityId);
		setParam("wipEntityName", wipEntityName);
		setParam("fmOperationCode", fmOperationCode);
		setParam("curOperationId", curOperationId);
		setParam("trxQuantity", trxQuantity);
		if(assetIdList!=null && assetIdList.size()>0) {
			for(int i=0; i<assetIdList.size(); i++) {
				String assetId = assetIdList.get(i);
				if(i==0) {
					setParam("assetId1", assetId);
				}
				else if(i==1) {
					setParam("assetId2", assetId);
				}
				else if(i==2) {
					setParam("assetId3", assetId);
				}
			}
		}
		setParam("opStart", opStart);
		setParam("seqId", seqId);
		setParam("canJump", canJump);
	}
}
