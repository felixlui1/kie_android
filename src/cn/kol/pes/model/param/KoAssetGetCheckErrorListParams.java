/*-----------------------------------------------------------

-- PURPOSE

--    获取点检列表请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetGetCheckErrorListParams extends KoHttpParams {
	
	public KoAssetGetCheckErrorListParams(){
		
		setParam("uri", "/erp/assetGetCheckList");
	}
}
