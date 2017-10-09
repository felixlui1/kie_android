/*-----------------------------------------------------------

-- PURPOSE

--    获取工序列表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoGetOpParams extends KoHttpParams {
	public KoGetOpParams(String seqId){
		
		setParam("uri", "/erp/seqList");
		setParam("seqId", seqId);
	}
}
