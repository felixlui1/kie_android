/*-----------------------------------------------------------

-- PURPOSE

--    拍照界面

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;

public class KoCameraActivity extends Activity implements OnClickListener, SurfaceHolder.Callback{

	private String tag = "KoCameraActivity";
	
	private Camera mCam;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	
	public static final int KEY_REQ_CODE_CAMERA = 1019;
	public static final String KEY_RES_INTENT = "res";
	
	private static final String KEY_ID = "check_or_trans_id";
	
	private View mShotBtn;
	private View mCancelBtn;
	private View mSaveBtn;
	
	public static void startActForRes(Activity context, String checkIdOrTransId) {
		Intent i = new Intent(context, KoCameraActivity.class);
		i.putExtra(KEY_ID, checkIdOrTransId!=null ? checkIdOrTransId:"kol");
		context.startActivityForResult(i, KEY_REQ_CODE_CAMERA);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_camera_activity);
		super.onCreate(savedInstanceState);
		
		mCam = Camera.open();
		Camera.Parameters params = mCam.getParameters();
		params.setRotation(90);
		params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		params.setJpegQuality(68);

		mCam.setParameters(params);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.camera_sur_view);
		mHolder = mSurfaceView.getHolder();
		
		mCancelBtn = findViewById(R.id.camera_cancel);
		mSaveBtn = findViewById(R.id.camera_save);
		
		mShotBtn = findViewById(R.id.camera_shot_btn);
		
		mShotBtn.setOnClickListener(this);
		findViewById(R.id.camera_shot_parent).setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mShotBtn.setVisibility(View.VISIBLE);
		mSaveBtn.setVisibility(View.GONE);
		mCancelBtn.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_shot_btn:
			try {
				mCam.cancelAutoFocus();
				mCam.autoFocus(new AutoFocusCallback() {  
				    @Override  
				    public void onAutoFocus(boolean success, Camera camera) {  
				    	LogUtils.e(tag, "onAutoFocus()"+success);
				    	mCam.takePicture(shutterCallback, rawCallback, jpegCallback);
				    }  
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			break;
			
		case R.id.camera_shot_parent:
			try {
				mCam.cancelAutoFocus();
				mCam.autoFocus(new AutoFocusCallback() {  
				    @Override  
				    public void onAutoFocus(boolean success, Camera camera) {  
				    	LogUtils.e(tag, "onAutoFocus()");
				    }  
				});
			} catch (Exception e) {
				e.printStackTrace();
			} 
			break;
			
		case R.id.camera_cancel:
			mShotBtn.setVisibility(View.VISIBLE);
			mSaveBtn.setVisibility(View.GONE);
			mCancelBtn.setVisibility(View.GONE);
			
			try {
	            mCam.setPreviewDisplay(mHolder);
	            mCam.startPreview();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			break;
			
		case R.id.camera_save:
			new SaveImageTask().execute(mPicData);
			break;

		default:
			break;
		}
	}
	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {

		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

		}
	};

	private byte[] mPicData = null;
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			mPicData = data;
			
			mShotBtn.setVisibility(View.GONE);
			mSaveBtn.setVisibility(View.VISIBLE);
			mCancelBtn.setVisibility(View.VISIBLE);
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
		LogUtils.e(tag, "surfaceChanged():width="+width+", height="+height);
		
		if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCam.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCam.setPreviewDisplay(mHolder);
            mCam.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
            if (mCam != null) {
            	mCam.setPreviewDisplay(holder);
            	mCam.startPreview();
            }
        } catch (Exception exception) {
           exception.printStackTrace();
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if(mCam != null) {
				mCam.stopPreview();
				mCam.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initCamera()  
    {  
        Parameters parameters=mCam.getParameters();  
        parameters.setPictureFormat(PixelFormat.JPEG);  
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。  
        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);     
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦  
        setDispaly(parameters,mCam);  
        mCam.setParameters(parameters);  
        mCam.startPreview();  
        mCam.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上  
          
    } 
	
	// 控制图像的正确显示方向
	private void setDispaly(Camera.Parameters parameters, Camera camera) {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			parameters.setRotation(90);
		}

	}  
	
	//实现的图像的正确显示  
    private void setDisplayOrientation(Camera camera, int i) {  
        Method downPolymorphic;  
       try{  
              downPolymorphic=camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});  
              if(downPolymorphic!=null) {  
                  downPolymorphic.invoke(camera, new Object[]{i});  
              }  
          }  
          catch(Exception e){  
              Log.e("Came_e", "图像出错");  
          }  
    }  
	
	private class SaveImageTask extends AsyncTask<byte[], Void, String> {

		@Override
		protected String doInBackground(byte[]... data) {
			FileOutputStream outStream = null;

			// Write to SD Card
			try {
	            File sdCard = Environment.getExternalStorageDirectory();
	            File dir = new File (sdCard.getAbsolutePath() + "/kol_pes_pics");
	            dir.mkdirs();				
				
				String fileName = String.format("kol_%s_"+getIntent().getStringExtra(KEY_ID)+".jpg", StringUtils.formatDateTimeForFileName(Calendar.getInstance()));
				File outFile = new File(dir, fileName);
				
				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();
				
				return dir.getAbsolutePath()+"/"+fileName;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			LogUtils.e(tag, "SaveImageTask:"+result);
			Intent i = new Intent();
			i.putExtra("res", result);
			setResult(RESULT_OK, i);
			KoCameraActivity.this.finish();
		}
	}
}
