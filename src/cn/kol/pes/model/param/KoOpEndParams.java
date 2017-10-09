/*-----------------------------------------------------------

-- PURPOSE

--    完成工序的参数类.

-- History

--	  24-Oct-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

import cn.kol.common.util.CacheUtils;



public class KoOpEndParams extends KoHttpParams {
	
	public KoOpEndParams(String transactionId, String scrapQuantity, String opEnd, String lastUpdatedBy,
						 String organizationId, String wipId, String opCode) {
		
		setParam("uri", "/erp/seqEnd");
		
		setParam("transactionId", transactionId);
		setParam("lastUpdatedBy", lastUpdatedBy);
		setParam("scrapQuantity", scrapQuantity);
		setParam("opEnd", opEnd);
		
		setParam("organizationId", organizationId);
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		
		setParam("canJump", CacheUtils.getLoginUserInfo().isCanJumpOp()?"Y":"N");
	}
}
