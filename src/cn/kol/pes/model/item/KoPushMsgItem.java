/*-----------------------------------------------------------

-- PURPOSE

--    推送消息的数据封装类

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

public class KoPushMsgItem extends Item {

	public String transId;
	public String title;
	public String msg;
	
	public String getTitle() {
		return title;
	}
	
	public String getMsg() {
		return msg;
	}
	
}
