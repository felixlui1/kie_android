/*-----------------------------------------------------------

-- PURPOSE

--    开始工序前进行检测的参数类.

-- History

--	  30-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;



public class KoOpCheckBeforeStartParams extends KoHttpParams {
	
	public KoOpCheckBeforeStartParams(String wipEntityId, String wipEntityName, String fmOperationCode, 
									  String curOperationId, String seqId){
		
		setParam("uri", "/erp/seqCheckBeforeStart");
		
		setParam("wipEntityId", wipEntityId);
		setParam("wipEntityName", wipEntityName);
		setParam("fmOperationCode", fmOperationCode);
		setParam("curOperationId", curOperationId);
		setParam("seqId", seqId);
	}
}
