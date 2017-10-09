/*-----------------------------------------------------------

-- PURPOSE

--    由于开始工序和结束工序两个界面的基本工单工序信息是大致相同的，所以，将相同部分分离一个View出来。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;

public class KoJobInfoView extends LinearLayout {

	private TextView gongXuHao;
	private TextView gongDanBianHao;
	private TextView zuZhuangLiaoHao;
	private TextView zuZhuangLiaoHaoDesc;
	private TextView soBianHao;
	private TextView cpn;
	private TextView otherReq;
	private TextView gongXuReq;
	private TextView customer;
	private TextView remain;
	private TextView remainLable;
	private ViewGroup remainParent;
	
	public KoJobInfoView(Context context) {
		this(context, null);
	}
	
	public KoJobInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_job_info_layout, this);
		
		gongXuHao = (TextView) rootView.findViewById(R.id.g_d_info_gongxuhao);
		gongDanBianHao = (TextView) rootView.findViewById(R.id.g_d_info_gongdanbianhao);
		zuZhuangLiaoHao = (TextView) rootView.findViewById(R.id.g_d_info_zuzhuangliaohao);
		zuZhuangLiaoHaoDesc = (TextView) rootView.findViewById(R.id.g_d_info_zuzhuangliaohao_desc);
		soBianHao = (TextView) rootView.findViewById(R.id.g_d_info_so_bianhao);
		cpn = (TextView) rootView.findViewById(R.id.g_d_info_cpn);
		otherReq = (TextView) rootView.findViewById(R.id.g_d_info_other_request);
		gongXuReq = (TextView) rootView.findViewById(R.id.g_d_info_gongxu_request);
		customer = (TextView) rootView.findViewById(R.id.g_d_info_customer);
	    remain = (TextView) rootView.findViewById(R.id.g_d_info_remine);
	    remainLable = (TextView) rootView.findViewById(R.id.g_d_info_remine_lable);
	    remainParent = (ViewGroup) rootView.findViewById(R.id.g_d_info_remine_layout);
	    
	}
	
	public void setData(Context context, KoJobItem gdData, String opCpde, String opDesc, String trxQuan) {
		if(gdData!=null && opCpde!=null) {
			gongXuHao.setText(opCpde +" "+ opDesc);
			gongDanBianHao.setText(gdData.wipEntityId+" "+gdData.wipEntityName);
			zuZhuangLiaoHao.setText(gdData.saItem);
			zuZhuangLiaoHaoDesc.setText(gdData.saItemDesc);
			soBianHao.setText("");
			cpn.setText(gdData.dffCpnNumber);
			otherReq.setText(gdData.dffCustomerspec);
			gongXuReq.setText(gdData.dffMfgSpec);
			customer.setText(gdData.custNumber);

			remainLable.setText(R.string.ko_title_input_quan);
			
			if(trxQuan != null) {
				remain.setText(trxQuan.trim());
				remainParent.setVisibility(View.VISIBLE);
			}else {
				remainParent.setVisibility(View.GONE);
			}
		}
	}
	
	public void setData(Context context, KoJobItem gdData, KoOpItem gxData, String maxQuan) {

		if(gdData!=null && gxData!=null) {
			gongXuHao.setText(gxData.standardOperationCode +" "+ gxData.operationDescription);
			gongDanBianHao.setText(gdData.wipEntityId+" "+gdData.wipEntityName);
			zuZhuangLiaoHao.setText(gdData.saItem);
			zuZhuangLiaoHaoDesc.setText(gdData.saItemDesc);
			soBianHao.setText("");
			cpn.setText(gdData.dffCpnNumber);
			otherReq.setText(gdData.dffCustomerspec);
			gongXuReq.setText(gdData.dffMfgSpec);
			customer.setText(gdData.custNumber);
			
			remainLable.setText(R.string.ko_title_left);
			
			if(maxQuan !=null) {
				remain.setText(maxQuan.trim());
				remainParent.setVisibility(View.VISIBLE);
			}else {
				remainParent.setVisibility(View.GONE);
			}
		}
	}	
	
}
