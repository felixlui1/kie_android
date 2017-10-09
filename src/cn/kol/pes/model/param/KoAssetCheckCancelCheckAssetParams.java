/*-----------------------------------------------------------

-- PURPOSE

--    取消某个设备的点检的请求的参数类.

-- History

--	  11-Aug-16  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetCheckCancelCheckAssetParams extends KoHttpParams {
	
	public KoAssetCheckCancelCheckAssetParams(String assetId){
		
		setParam("uri", "/erp/assetCheckCancelAssetCheck");
		
		setParam("assetId", assetId);
	}
}
