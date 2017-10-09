/*-----------------------------------------------------------

-- PURPOSE

--   插入图片路径表的参数类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;


public class KoAssetInsertPicPathParams extends KoHttpParams {
	
	public KoAssetInsertPicPathParams(String checkId, String picPathDescList, String isEnd, String isSeq){
		
		setParam("uri", "/erp/assetInsertPicPath");
		
		setParam("checkId", checkId.trim());
		setParam("picPathDescList", picPathDescList);
		setParam("isEnd", isEnd);
		setParam("isSeq", isSeq);
	}
}
