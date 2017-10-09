package cn.kol.pes.model.param;

public class KoAssetCheckCheckItemParams extends KoHttpParams {

	public KoAssetCheckCheckItemParams(String assetId) {
		
		setParam("uri", "/erp/assetCheckGetCheckItemList");
		
		setParam("assetId", assetId);
	}
}
