package cn.kol.pes.utils;

/*-----------------------------------------------------------

-- PURPOSE

--    上传点检和维修照片的帮助类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import cn.kol.common.util.KoUploadImgUtil;
import cn.kol.pes.model.item.KoParamItem;

public class KoAssetUploadImgHelper {

	private List<KoParamItem> mPicListData = new ArrayList<KoParamItem>();
	
	public static KoAssetUploadImgHelper getInstance() {
		return new KoAssetUploadImgHelper();
	}
	
	public void startUpload(List<KoParamItem> picListData) {//开始上传图片数据
		if(picListData!=null) {
			mPicListData.clear();
			mPicListData.addAll(picListData);
			
			mAsy.execute();
		}
	}
	
	//上传图片数据的线程
	AsyncTask<KoParamItem, Void, String> mAsy = new AsyncTask<KoParamItem, Void, String>() {

		@Override
		protected String doInBackground(KoParamItem... params) {
			for(KoParamItem p : mPicListData) {
				if(p != null) {
					try {
						KoUploadImgUtil.uploadImg(p.name);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			StringBuilder sb = new StringBuilder();
			for(KoParamItem p : mPicListData) {
				if(p!=null && p.value!=null) {
					sb.append(p.value);
				}
			}
			
			super.onPostExecute(result);
		}

	};
	
}
