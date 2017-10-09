/*-----------------------------------------------------------

-- PURPOSE

--    一些最基本的弹出框和提示信息的util

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.kol.pes.R;

public class DialogUtils {

	public static Dialog getDialog(Context context) {
		return getDialog(context, context.getString(R.string.ko_title_loading));
	}

	//网络请求加载时的等待滚动框
	public static Dialog getDialog(Context context, String msg) {
		LayoutInflater lf = LayoutInflater.from(context);
		View view = lf.inflate(R.layout.ko_loading_view, null);

		TextView text = (TextView) view.findViewById(R.id.loading_text);
		text.setText(msg);

		Dialog dialog = new Dialog(context, R.style.sd_loading_dialog_theme);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		dialog.setCancelable(true);
		return dialog;
	}

	public static void setShowLoadingText(Dialog dlg, String msg) {
		try {
			if (dlg != null && dlg.isShowing()) {
				TextView text = (TextView) dlg.findViewById(R.id.loading_text);
				text.setText(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//弹出信息提示的toast,显示时间稍长
	public static void showToast(Context context, String msg) {

		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_LONG);
		LayoutInflater li = LayoutInflater.from(context);
		View parent = li.inflate(R.layout.ko_toast_text_layout, null);

		TextView view = (TextView) parent.findViewById(R.id.toast);
		view.setText(Html.fromHtml(msg));
		t.setView(parent);
		t.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		t.show();
	}

	//弹出信息提示的toast,显示时间稍短
	public static void showToastShort(Context context, String msg) {
		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_SHORT);
		LayoutInflater li = LayoutInflater.from(context);
		View parent = li.inflate(R.layout.ko_toast_text_layout, null);
		TextView view = (TextView) parent.findViewById(R.id.toast);
		view.setText(Html.fromHtml(msg));
		t.setView(parent);
		t.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		t.show();
	}

	//弹出信息提示的toast,显示时间稍长
	public static void showToast(Context context, int msg) {

		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_LONG);
		LayoutInflater li = LayoutInflater.from(context);
		View parent = li.inflate(R.layout.ko_toast_text_layout, null);

		TextView view = (TextView) parent.findViewById(R.id.toast);
		view.setText(Html.fromHtml(context.getString(msg)));
		t.setView(parent);
		t.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		t.show();
	}

	//弹出信息提示的toast,显示时间稍短
	public static void showToastShort(Context context, int msg) {
		Toast t = new Toast(context);
		t.setDuration(Toast.LENGTH_SHORT);
		LayoutInflater li = LayoutInflater.from(context);
		View parent = li.inflate(R.layout.ko_toast_text_layout, null);
		TextView view = (TextView) parent.findViewById(R.id.toast);
		view.setText(Html.fromHtml(context.getString(msg)));
		t.setView(parent);
		t.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		t.show();
	}
}
