/*-----------------------------------------------------------

-- PURPOSE

--    获取点检列表请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetCheckGetAssetInfoParams extends KoHttpParams {
	
	public KoAssetCheckGetAssetInfoParams(String assetId){
		
		setParam("uri", "/erp/assetCheckGetAssetInfo");
		
		setParam("assetId", assetId);
	}
}
