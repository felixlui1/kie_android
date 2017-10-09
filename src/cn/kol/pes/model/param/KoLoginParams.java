/*-----------------------------------------------------------

-- PURPOSE

--    登录请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoLoginParams extends KoHttpParams {
	
	public KoLoginParams(String userId){
		
		setParam("uri", "/erp/login");
		setParam("userId", userId);
	}
}
