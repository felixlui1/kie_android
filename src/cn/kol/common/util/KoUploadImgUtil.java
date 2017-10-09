/*-----------------------------------------------------------

-- PURPOSE

--    上传图片的帮助类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


public class KoUploadImgUtil {
	
	private static final String tag = "KoUploadImgUtil";

	//上传照片的逻辑
	public static boolean uploadImg(String filePath) throws Exception {
		
		String url = Constants.getServerUrl()+"/erp/uploadImg";

		LogUtils.e(tag, "uploadImg():filePath="+filePath);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		MultipartEntity reqEntity = new MultipartEntity();
		httpPost.setEntity(reqEntity);

		FileBody bin = new FileBody(new File(filePath));
		reqEntity.addPart("myFile", bin);

		StringBody myFileFileName = new StringBody(bin.getFilename());
		reqEntity.addPart("myFileFileName", myFileFileName);
		
		System.out.println("executing:" + httpPost.getRequestLine());
		
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity responseEntity = response.getEntity();
		
		String result = inputStream2String(responseEntity.getContent());

		if (responseEntity != null){
			LogUtils.e(tag, "Response content: " + result);
		}

		httpClient.getConnectionManager().closeExpiredConnections();
		httpClient.getConnectionManager().shutdown();
		
		return (result!=null && result.contains("<savedPath>"));//如果上传成功，服务器会返回包含图片保存路径的XML，所以根据返回内容简单判断下是否含有savedPath节点即可
	}

	//打印上传结果
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

}
