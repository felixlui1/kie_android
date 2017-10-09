/*-----------------------------------------------------------

-- PURPOSE

--    获取设备故障列表请求的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetMachineFailListParams extends KoHttpParams {
	
	public KoAssetMachineFailListParams(String tagNum){
		
		setParam("uri", "/erp/assetMachineFailList");
		setParam("tag", tagNum);
	}
}
