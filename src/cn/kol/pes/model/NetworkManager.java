/*-----------------------------------------------------------

-- PURPOSE

--    NetworkManager是所有网络具体的管理类.它创建一个httpClient,顺序请求封装为DownLoadTask的若干网络任务，并为每个请求分配一个id，达到多线程的目的

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import cn.kol.common.util.ContextSaveUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.param.KoHttpParams;

public class NetworkManager {
	private final String tag = "NetworkManager";
	
	private static NetworkManager mInstance = null;
	private static int mDownLoadID;
	private int mCancelID; // because download task id increase, no need clear
//	private int					mState = 0; // 0:stop, 1:starting, 2:running
	private static List<XMLTask> mRunningTasks;
	
	public static NetworkManager instance() {
		if (mInstance == null) {
			mInstance = new NetworkManager();
			mRunningTasks = new LinkedList<XMLTask>();
		}
		return mInstance;
	}
	
	public static void clearNetInstance() {
		mInstance = null;
		mRunningTasks = null;
	}
	
	public static void initDownLoadService(Context app) {
		instance();
	}
    
	public static DownLoadTask getTask(KoHttpParams params, OnHttpDownloadListener listener, OnDataParseListener dataParse) {
		return new DownLoadTask(params, listener, dataParse);
	}
	
	private NetworkManager() {
		mDownLoadID = 0;
		mCancelID = -1;
	}
	
	public int getXML(DownLoadTask task) {
	    if(isConnectInternet()) {
	        task.mId = ++mDownLoadID;
	        XMLTask dl = new XMLTask(task);
	        mRunningTasks.add(dl);
	        dl.myExecute();
	    	LogUtils.e(tag, "getXML():task.mId="+task.mId);
	        return task.mId;
	    }
	    else {
	        DialogUtils.showToastShort(ContextSaveUtils.getAppContext(), R.string.ko_tips_connect_net_fail);
	    }
	    return -1;
	}
	
	private String doDownLoad(DownLoadTask task, DefaultHttpClient client, HttpUriRequest request) {
		
		LogUtils.e(tag, "doDownLoad()");
		
		InputStream is = null;
		String result = OnHttpDownloadListener.SUCCESS;
		HttpResponse httpResponse = null;

		try {
			request.setParams(getParams());
			
			NetworkInfo netInfor = getActiveNetworkInfo();
			if(netInfor==null || !netInfor.isConnected()) {
				return OnHttpDownloadListener.ERROR_NO_CONNECT;
			}
			
			if(isConnectionWithCmwap(getActiveNetworkInfo())) {
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, getCmwapProxy());
				if(LogUtils.show) {
					LogUtils.e(tag, "getCmwapProxy()="+getCmwapProxy());
				}
			}

			URI uri = request.getURI();

			if(uri!=null) {
				if(LogUtils.show) {
				    LogUtils.e(tag, "uri.getRawPath()="+uri.toString());
				}
			}
			
			httpResponse = client.execute(request);
			int ret = httpResponse.getStatusLine().getStatusCode();
			if(LogUtils.show) {
				LogUtils.e("NWM", "doDownLoad():ret="+ret);
			}
			if (ret == HttpStatus.SC_OK || ret==HttpStatus.SC_UNAUTHORIZED) {

				Header h = httpResponse.getFirstHeader("Content-Encoding");
				
				if(h != null && "gzip".equals(h.getValue())){
					is = new GZIPInputStream(httpResponse.getEntity().getContent());
				}else{
					is = httpResponse.getEntity().getContent();
				}
				
				try {
					ByteArrayOutputStream os=new ByteArrayOutputStream();
					byte[] b = new byte[4096];  
					for (int n; (n = is.read(b)) != -1;) {
					    os.write(b, 0, n);
					}
					is = new ByteArrayInputStream(os.toByteArray());
				} catch (IOException e1) {
					
					e1.printStackTrace();
				} 
				
				if(LogUtils.show) {
					is = LogUtils.xml(tag, is, ContextSaveUtils.getAppContext());
					if(is!=null) {
						LogUtils.e(tag, "is!=null");
					}
					if(h!=null) {
						LogUtils.e(tag, "h.getValue()="+h.getValue());
					}
				}
				client.getConnectionManager().shutdown();
				return parseIs(task, is);
				
			} else {
				return "NetWorkManager:Http Err:" + Integer.valueOf(ret);
			}
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			result =  getExceptionMessage(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result =  getExceptionMessage(e);
		} catch (IOException e) {
			e.printStackTrace();
			result =  getExceptionMessage(e);
		} catch(IllegalStateException e){
			e.printStackTrace();
			result =  getExceptionMessage(e);
		}catch(Exception e){
			e.printStackTrace();
			result =  getExceptionMessage(e);
	
		}
		finally{
			client.getConnectionManager().shutdown();
		}
		
		return result;
	}

	private String parseIs(DownLoadTask task,InputStream is){
		String result;
		try{
			result = onParseData(task, is);
			is.close();
		}catch (Exception e){
			e.printStackTrace();
			result =  getExceptionMessage(e);
		}
		return result;
	}
	
	private String onParseData(DownLoadTask task, InputStream is){
		
		if(mCancelID == task.mId){
			mCancelID = -1;
			return null;
		} else {
			if (task.getmParseListener() != null) {
				String ret = task.getmParseListener().onDataParse(task.mId, is);
					
				if(!OnDataParseListener.SUCCESS.equals(ret)){
					return ""/*TIP_PARSE_ERROR*/;
				}
				return OnDataParseListener.SUCCESS;
			}
			else{
				return null;
			}
		}
	}
	
	private HttpParams getParams(){
		
		BasicHttpParams  httpparams = new BasicHttpParams ();
		HttpConnectionParams.setConnectionTimeout(httpparams, 19000);
		
		return httpparams;
	}
	
	private HttpUriRequest getRequest(DownLoadTask task)  {
		
		return task.mParams.getHttpUriRequest();
	}
	
	private String getExceptionMessage(Exception e){
		return null;
	}
	
	private boolean isConnectInternet() {
		ConnectivityManager conManager = (ConnectivityManager) ContextSaveUtils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}
	
	private NetworkInfo getActiveNetworkInfo(){
		ConnectivityManager conManager = (ConnectivityManager) ContextSaveUtils.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return conManager.getActiveNetworkInfo();
	}
	
	private boolean isConnectionWithCmwap(NetworkInfo activeNetworkInfo) {
		if (activeNetworkInfo != null &&
			activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE &&
		    ("cmwap".equalsIgnoreCase(activeNetworkInfo.getExtraInfo())||"3gwap".equalsIgnoreCase(activeNetworkInfo.getExtraInfo()))) {
			return true;
		}
		return false;
	}
	
	private HttpHost getCmwapProxy(){
		
		return new HttpHost( "10.0.0.172", 80);
	}
	
	public interface OnHttpDownloadListener {
		public static final String SUCCESS = "Success";
		public static final String ERROR_NO_CONNECT = "error_no_connect";
		public void onHttpDownLoadDone(int taskId, String result);
	}
	public interface OnDataParseListener{
		public static final String SUCCESS = "Success";
		String onDataParse(int id, InputStream is);
	}
	public interface OnHttpClientListener{
		public static final String SUCCESS = "Success";
		void onHttpRequested(DefaultHttpClient client);
	}
	
	public static class DownLoadTask{
		private int mId;
		private KoHttpParams mParams;
		private OnHttpDownloadListener mHttpListener;
		private OnDataParseListener mParseListener;
		private OnHttpClientListener mClientBack;
		
		private int debug;
		private String file;
		
		public DownLoadTask(KoHttpParams mParams,
							OnHttpDownloadListener mHttpListener,
							OnDataParseListener mParseListener) {
			super();
			this.mParams = mParams;
			this.mHttpListener = mHttpListener;
			this.mParseListener = mParseListener;
			debug = 0; 
		}
		
		public DownLoadTask(KoHttpParams mParams,
							OnHttpDownloadListener mHttpListener,
							OnDataParseListener mParseListener, OnHttpClientListener clientBack) {
			super();
			this.mParams = mParams;
			this.mHttpListener = mHttpListener;
			this.mParseListener = mParseListener;
			debug = 0; 
			this.mClientBack = clientBack;
		}
		
		public int getmId() {
			return mId;
		}
		public void setmId(int mId) {
			this.mId = mId;
		}
		public OnHttpDownloadListener getmHttpListener() {
			return mHttpListener;
		}
		public void setmHttpListener(OnHttpDownloadListener mHttpListener) {
			this.mHttpListener = mHttpListener;
		}
		public OnDataParseListener getmParseListener() {
			return mParseListener;
		}
		public void setmParseListener(OnDataParseListener mParseListener) {
			this.mParseListener = mParseListener;
		}
		public KoHttpParams getmParams() {
			return mParams;
		}
		public void setmParams(KoHttpParams mParams) {
			this.mParams = mParams;
		}
		public void setDebug(int i){
			debug = i;
		}
		public int getDebug(){
			return 0;
		}
		public void setDebugFile(String filename){
			if (debug != 0 ){
				this.file = filename;
			} else {
				throw new RuntimeException("Now,Loader is not in debug status,can't setFile()");
			}
		}
		public String getDebugFile(){
			return file;
		}
		
		public OnHttpClientListener getHttpClientListener() {
			return this.mClientBack;
		}
	}
	
	private class XMLTask extends AsyncTask<DownLoadTask, Void, String> {
		private DownLoadTask mTask;
		private DefaultHttpClient mClient;// = getNewHttpClientInstance();
		HttpUriRequest mRequest;
		
		public XMLTask(DownLoadTask task) {
			mTask = task;
			
			HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	        HttpProtocolParams.setUseExpectContinue(params, true);

	        SchemeRegistry schReg = new SchemeRegistry();
	        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

	        //ThreadSafeClientConnManager connMgr = new ThreadSafeClientConnManager(params, schReg);
	        mClient = getHttpClient();//new DefaultHttpClient(connMgr, params);
	        
	        mRequest = getRequest(task);

	        LogUtils.e(tag, "XMLTask inited");
		}
		
		@Override
		protected String doInBackground(DownLoadTask... tasks) {
			try {
				if(isCancelled()) {
					mRequest.abort();
					mClient.getConnectionManager().shutdown();
					return null;
				}
				return doDownLoad(mTask, mClient, mRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try{
				if(mTask.getmHttpListener()!= null) {
					mTask.getmHttpListener().onHttpDownLoadDone(mTask.mId, result);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			if(isCancelled()) {
				return;
			}
		}
		
		@Override
		protected void onCancelled() {
			LogUtils.e(tag, "onCancelled()");
			try{
				if(mTask.getmHttpListener()!= null) {
					mTask.getmHttpListener().onHttpDownLoadDone(mTask.mId, "calceled");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			super.onCancelled();
		}
		
		public void myCancel() {
			mRequest.abort();
			mClient.getConnectionManager().shutdown();
			this.cancel(true);
		}

		public void myExecute(){
			execute();
		}
	}
	
	public void stopRunningTask() {
		if(mRunningTasks!=null && mRunningTasks.size()>0) {
			for(XMLTask t : mRunningTasks) {
				if(t!=null && t.mTask!=null && !t.isCancelled()) {
					t.myCancel();
					if(LogUtils.show) {
						LogUtils.e(tag, "stopRunningTask(): cancel task");
					}
				}
			}
		}
	}
    
    private static final int DEFAULT_MAX_CONNECTIONS = 30;
    private static final int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	private static synchronized DefaultHttpClient getHttpClient() {
		
		final HttpParams httpParams = new BasicHttpParams();  
		
		// timeout: get connections from connection pool
        ConnManagerParams.setTimeout(httpParams, 21000);  
        // timeout: connect to the server
        HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
        // timeout: transfer data from server
        HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT); 
        
        // set max connections per host
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(8));  
        // set max total connections
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        
        // use expect-continue handshake
        HttpProtocolParams.setUseExpectContinue(httpParams, true);
        // disable stale check
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
        
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);  
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8); 
          
        HttpClientParams.setRedirecting(httpParams, false);
        
        // set user agent
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(httpParams, userAgent); 	
        
        // disable Nagle algorithm
        HttpConnectionParams.setTcpNoDelay(httpParams, true); 
        
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);  
        
        // scheme: http and https
        SchemeRegistry schemeRegistry = new SchemeRegistry();  
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);  
        return new DefaultHttpClient(manager, httpParams); 
	}
}
