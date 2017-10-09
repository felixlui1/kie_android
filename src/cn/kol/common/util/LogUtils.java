/*-----------------------------------------------------------

-- PURPOSE

--    具有开关功能的日志工具类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class LogUtils {

	public static final boolean show = Constants.IS_OPEN_LOG;
	public static final boolean show2 = false;

	public static void e(String tag, String msg) {
		if (show) {
			Log.e(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (show) {
			Log.i(tag, msg);
		}
	}

	//由于输入流被读出进行打印后，会变空，所以我们一般打印，一边吧数据放在一个输出流返回去
	public static InputStream xml(String tag, InputStream is, Context contex) {
		if (show) {
			try {
				StringBuilder sb = new StringBuilder();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] b = new byte[4096];
				for (int n; (n = is.read(b)) != -1; Log.d("XML", "n=" + n)) {
					String temp = new String(b, 0, n);
					sb.append(temp);
					Log.d(tag + "_XML", "" + temp);
					os.write(b, 0, n);
				}

				InputStream is_temp = new ByteArrayInputStream(os.toByteArray());
				is = is_temp;

				return is;
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		return is;
	}

}
