package cn.kol.pes.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoOpStartedItem;

public class KoSeqPctDialog extends KoCommomDialogFatherClass implements android.view.View.OnClickListener {
	
	private EditText mQtyCompleteEdit;
	private TextView mPctCompleteText;
	private EditText mScrapQtyText;
	private TextView mEstCompleteTime;
	
	private KoOpStartedItem mSeqData;

	public KoSeqPctDialog(Activity context, KoOpStartedItem seqData) {
		super(context);
		
		LayoutInflater li = LayoutInflater.from(context);
        ViewGroup parentView = (ViewGroup) li.inflate(R.layout.ko_seq_pct_dialog_layout, null);
        
        mQtyCompleteEdit = (EditText)parentView.findViewById(R.id.seq_pct_dialog_complete_qty);
        mPctCompleteText = (TextView)parentView.findViewById(R.id.seq_pct_dialog_complete_pct);
        mScrapQtyText = (EditText)parentView.findViewById(R.id.seq_pct_dialog_scrap_qty);
        mEstCompleteTime = (TextView)parentView.findViewById(R.id.seq_pct_dialog_est_complete_time);
        
        mEstCompleteTime.setOnClickListener(this);
        parentView.findViewById(R.id.seq_pct_dialog_submit_btn).setOnClickListener(this);
        
        this.setContentView(parentView);
        
        this.mSeqData = seqData;
        
        
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.seq_pct_dialog_est_complete_time:
		
			break;
			
		case R.id.seq_pct_dialog_submit_btn:
			
			break;
		}
	}

}
