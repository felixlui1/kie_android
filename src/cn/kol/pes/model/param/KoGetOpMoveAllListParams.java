/*-----------------------------------------------------------

-- PURPOSE

--    获取特定工单的工序列表的参数类.

-- History

--	  15-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoGetOpMoveAllListParams extends KoHttpParams {
	
	public KoGetOpMoveAllListParams(String wipEntityId){
		
		setParam("uri", "/erp/seqMoveAllList");
		setParam("wipEntityId", wipEntityId);
	}
	
}
