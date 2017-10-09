/*-----------------------------------------------------------

-- PURPOSE

--    获取最大可投入数的参数类.

-- History

--	  23-Oct-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;



public class KoOpMaxQuanParams extends KoHttpParams {
	
	public KoOpMaxQuanParams(String wipEntityName, String wipEntityId, String seqId, String fmOperationCode, String curOperationId, String canJump){
		
		setParam("uri", "/erp/seqMaxQuan");
		
		setParam("wipEntityName", wipEntityName);
		setParam("wipEntityId", wipEntityId);
		setParam("seqId", seqId);
		setParam("fmOperationCode", fmOperationCode);
		setParam("curOperationId", curOperationId);
		setParam("canJump", canJump);
		
	}
}
