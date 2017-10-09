/*-----------------------------------------------------------

-- PURPOSE

--    KoHttpParams是所有网络请求参数类的父类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.kol.common.util.Constants;
import cn.kol.common.util.KoCommonUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.model.item.KoParamItem;


public abstract class KoHttpParams {
	private static final String tag = "KoHttpParams";
	protected String mURL;
	protected List<NameValuePair> mParams;
	protected boolean mIsPost;
	private StringBuilder mGetUrlBuilder = null;
	protected String mCharSet = "UTF-8";
	private boolean mIsEncode = true;
	
	public KoHttpParams(){

		mURL = Constants.getServerUrl();
		
		mParams = new ArrayList<NameValuePair>();
		mIsPost = true;
		mGetUrlBuilder = new StringBuilder();
		mGetUrlBuilder.append("?");
	}
	
	public KoHttpParams(String url, boolean isPost){
		mURL = url;
		mParams = new ArrayList<NameValuePair>();
		mIsPost = isPost;
	}
	
	//供子类设置http参数，post类型专用
	public void setParam(String key, String value){
		mIsPost = true;
		mParams.add(new BasicNameValuePair(key, value));
	}
	
	public void setIsEncode(boolean isEncode) {
		this.mIsEncode = isEncode;
	}
	
	public void setCharSet(String charSet) {
		this.mCharSet = charSet;
	}
	
	//供子类设置http参数，get类型专用
	public void setGetParam(String key, String value){
		mIsPost = false;
		mParams.add(new BasicNameValuePair(key, value));
	}
	
	public HttpUriRequest getHttpUriRequest() {
		
		HttpUriRequest ret = null;
		List<KoParamItem> headerList = new LinkedList<KoParamItem>();

		for(int i=mParams.size()-1; i>=0; i--) {//将http参数列表中名为new_main和uri的参数特殊处理，分别作为服务器地址，和http路径
			NameValuePair p = mParams.get(i);
			if(p.getName()!=null && p.getName().equals("new_main")) {
				mURL = mURL.replace(Constants.getServerUrl(), p.getValue());
				mParams.remove(i);
			}else if(p.getName()!=null && p.getName().equals("uri")) {
				mURL = mURL+p.getValue();
				mParams.remove(i);
			}
		}

		if(LogUtils.show) {
			LogUtils.e("SdHttpParams", "mURL="+mURL);
		}
		
		try {
			if(!mIsPost) {//拼出get类型的url
				for(NameValuePair p : mParams) {
					mGetUrlBuilder.append(p.getName()).append("=");
					if(this.mIsEncode) {
						mGetUrlBuilder.append(URLEncoder.encode(p.getValue(), mCharSet)).append("&");
					}else {
						mGetUrlBuilder.append(p.getValue()).append("&");
					}
				}
			}
			if(mGetUrlBuilder!=null && mGetUrlBuilder.length()>0) {
				mGetUrlBuilder.deleteCharAt(mGetUrlBuilder.length()-1);
			}

			if(LogUtils.show) {
				for(NameValuePair p : mParams) {
					LogUtils.i("KoHttpParams", "setParam():key="+p.getName()+", value="+p.getValue());
				}
			}
			
			if(mParams.size() == 0 || !mIsPost){//设置get
				if(LogUtils.show) {
					LogUtils.e("SdHttpParams", "is get !!!" + mURL+mGetUrlBuilder.toString());
				}
				
				ret = new HttpGet(mURL+mGetUrlBuilder.toString());
				
				ret.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:18.0) Gecko/20100101 Firefox/18.0");
				
				if("en".equals(KoCommonUtil.getLanguage())) {
					ret.addHeader("Accept-Language", "us-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				}else {
					ret.addHeader("Accept-Language", "zh-cn,zh;q=0.5");
				}
				
				if(headerList!=null && headerList.size()>0) {
					for(KoParamItem header : headerList) {
						ret.addHeader(header.name, header.value);
						LogUtils.e("SdHttpParams", "add get header="+header.name+","+header.value);
					}
				}
				
			}else {//设置post
				if(LogUtils.show) {
					LogUtils.e("SdHttpParams", "is post !!!");
				}
				HttpPost httpPost = new HttpPost(mURL);
				
				httpPost.addHeader("Accept-Encoding", "gzip, deflate");
				if("en".equals(KoCommonUtil.getLanguage())) {
					httpPost.addHeader("Accept-Language", "us-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				}else {
					httpPost.addHeader("Accept-Language", "zh-cn,zh;q=0.5");
				}
				httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:18.0) Gecko/20100101 Firefox/18.0");
				
				httpPost.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
				
				if(headerList!=null && headerList.size()>0) {
					for(KoParamItem header : headerList) {
						httpPost.addHeader(header.name, header.value);
					}
				}

				if(LogUtils.show) {//打印日志，方便调试
					if(mParams!=null) {
						StringBuilder sb = new StringBuilder();
						sb.append(mURL).append("?");
						for(NameValuePair nv : mParams) {
							if(nv != null) {
								sb.append(nv.getName()).append("=").append(nv.getValue()).append("&");
							}
						}
						LogUtils.e(tag, "post url="+sb.toString());
					}
					
					Header[] headers = httpPost.getAllHeaders();
					if(headers!=null) {
						for(Header h : headers) {
							if(h != null) {
								LogUtils.e(tag, "header "+h.getName()+"="+h.getValue());
							}
						}
					}
				}
				
				ret = httpPost;
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getURL(){
		return mURL;
	} 
	
	
}
