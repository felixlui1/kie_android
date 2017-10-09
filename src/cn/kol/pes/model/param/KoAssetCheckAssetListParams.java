package cn.kol.pes.model.param;

public class KoAssetCheckAssetListParams extends KoHttpParams {

	public KoAssetCheckAssetListParams(String startDate, String endDate) {
		
		setParam("uri", "/erp/assetCheckGetAssetList");
		
		setParam("startDate", startDate);
		setParam("endDate", endDate);
	}
}
