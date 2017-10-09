package cn.kol.pes.model.param;

public class KoAssetCheckGetSetAssetListParams extends KoHttpParams {

	public KoAssetCheckGetSetAssetListParams(String date, String shift, String staffNo) {
		
		setParam("uri", "/erp/assetCheckGetSetAssetCheckList");
		
		setParam("date", date);
		setParam("shift", shift);
		setParam("staffNo", staffNo);
	}
}