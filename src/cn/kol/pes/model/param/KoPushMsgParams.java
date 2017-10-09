/*-----------------------------------------------------------

-- PURPOSE

--    获取推送信息的参数类.

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoPushMsgParams extends KoHttpParams {
	
	public KoPushMsgParams(String staffNo, String transId, boolean isNotice){
		
		setParam("uri", "/erp/pushMsg");
		setParam("staffNo", staffNo);
		setParam("transId", transId);
		setParam("isNotice", isNotice?"Y":"N");
	}
}
