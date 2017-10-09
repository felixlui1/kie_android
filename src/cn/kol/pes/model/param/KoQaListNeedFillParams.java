/*-----------------------------------------------------------

-- PURPOSE

--    获取质量管理计划数据项列表的参数类.

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

import cn.kol.common.util.CacheUtils;


public class KoQaListNeedFillParams extends KoHttpParams {
	
	public KoQaListNeedFillParams(String wipId, String opCode, String organizationId){
		
		setParam("uri", "/erp/qaListNeedFill");
		setParam("wipId", wipId);
		setParam("opCode", opCode);
		setParam("organizationId", organizationId);
		
		setParam("canJump", CacheUtils.getLoginUserInfo().isCanJumpOp()?"Y":"N");
	}
}
