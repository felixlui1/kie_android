package cn.kol.pes.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;

public class KoAssetCheckChangedPartItemView extends LinearLayout implements OnClickListener {

	private EditText mEdit;
	private Button mBtn;

	private IKoAssetCheckChangePartsCallBack mCallBack;
	private Activity mContext;

	public KoAssetCheckChangedPartItemView(Activity context, IKoAssetCheckChangePartsCallBack callBack) {
		
		super(context, null);
		this.mContext = context;
		this.mCallBack = callBack;

		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_asset_check_repair_changed_part_layout, this);
		
		mEdit = (EditText) rootView.findViewById(R.id.asset_repair_changed_parts_edit);
		mBtn = (Button) rootView.findViewById(R.id.asset_repair_changed_parts_btn);
		mBtn.setOnClickListener(this);
		
		rootView.findViewById(R.id.asset_repair_changed_parts_del_btn).setOnClickListener(this);
	}
	
	public void setValue(String v) {
		this.mEdit.setText(v);
	}
	
	public String getValue() {
		return mEdit.getText().toString();
	}
	
	public boolean isDataOk() {
		return KoDataUtil.isStringNotNull(mEdit.getText().toString());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_repair_changed_parts_btn:
			this.mCallBack.changedPartViewClicked(this);
			break;

		case R.id.asset_repair_changed_parts_del_btn:
			this.mCallBack.changedPartViewDeleteClicked(this);
			break;
		}
	}
	
	
	public interface IKoAssetCheckChangePartsCallBack {
		
		public void changedPartViewClicked(KoAssetCheckChangedPartItemView v);
		
		public void changedPartViewDeleteClicked(KoAssetCheckChangedPartItemView v);
	}
}
