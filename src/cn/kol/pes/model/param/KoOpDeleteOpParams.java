/*-----------------------------------------------------------

-- PURPOSE

--    删除工序的参数类.

-- History

--	  30-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;



public class KoOpDeleteOpParams extends KoHttpParams {
	
	public KoOpDeleteOpParams(String transactionId){
		
		setParam("uri", "/erp/seqDeleteOp");
		
		setParam("transactionId", transactionId);
	}
}
