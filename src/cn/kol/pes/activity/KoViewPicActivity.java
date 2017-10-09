/*-----------------------------------------------------------

-- PURPOSE

--    为拍摄的照片添加说明信息。是从点检和维修设备界面启动

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;

public class KoViewPicActivity extends BaseActivity implements OnClickListener {

	private ImageView mViewPicView;
	private EditText mDescEdit;
	
	public static final int REQ_CODE = 1009;
	
	public static final String KEY_OPERATION_SAVE = "key_operation_save";
	public static final String KEY_OPERATION_DELETE = "key_operation_delete";
	
	public static final String KEY_PIC_OPERATION = "key_pic_operation";
	public static final String KEY_PIC_FILE_PATH = "key_pic_file_path";
	public static final String KEY_PIC_FILE_DESC = "key_pic_file_desc";
	
	private String mPicPath;
	private String mPicDesc;
	
	public static void startActForRes(Activity context, String path, String desc) {
		Intent i = new Intent(context, KoViewPicActivity.class);
		i.putExtra(KEY_PIC_FILE_PATH, path);
		i.putExtra(KEY_PIC_FILE_DESC, desc);
		context.startActivityForResult(i, REQ_CODE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_view_pic_activity);
		super.onCreate(savedInstanceState);
		
		mViewPicView = (ImageView) findViewById(R.id.view_pic_img_view);
		mDescEdit = (EditText) findViewById(R.id.view_pic_desc_edit);
		
		findViewById(R.id.view_pic_add_pic_btn).setOnClickListener(this);
		findViewById(R.id.view_pic_del_btn).setOnClickListener(this);
		
		Intent i = getIntent();
		mPicDesc = i.getStringExtra(KEY_PIC_FILE_DESC);
		mPicPath = i.getStringExtra(KEY_PIC_FILE_PATH);
		
		mDescEdit.setText(mPicDesc);
		try {
			LogUtils.e(tag, "mPicPath="+mPicPath);
			BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inSampleSize=4;
            LogUtils.e(tag, "opts.inSampleSize="+opts.inSampleSize);
			mViewPicView.setImageBitmap(BitmapFactory.decodeFile(mPicPath, opts));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_pic_add_pic_btn:
			String desc = mDescEdit.getText().toString();
			Intent i1 = new Intent();
			i1.putExtra(KEY_PIC_FILE_PATH, getIntent().getStringExtra(KEY_PIC_FILE_PATH));
			i1.putExtra(KEY_PIC_FILE_DESC, desc);
			i1.putExtra(KEY_PIC_OPERATION, KEY_OPERATION_SAVE);
			setResult(RESULT_OK, i1);
			this.finish();
			break;
			
		case R.id.view_pic_del_btn:
			KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
				@Override
				public void onOkBack() {
					Intent i2 = new Intent();
					i2.putExtra(KEY_PIC_FILE_PATH, mPicPath);
					i2.putExtra(KEY_PIC_FILE_DESC, mPicDesc);
					i2.putExtra(KEY_PIC_OPERATION, KEY_OPERATION_DELETE);
					setResult(RESULT_OK, i2);
					KoViewPicActivity.this.finish();
				}
				
				@Override
				public void onCancelBack() {
				}
			}, getString(R.string.ko_title_sure_del_pic));
			
			dlg.setOkCancelBtn(true, true);
			dlg.show();
			
			break;

		default:
			break;
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack();
	}

}
