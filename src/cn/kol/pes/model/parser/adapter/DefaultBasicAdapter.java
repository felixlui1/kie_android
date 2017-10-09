/*-----------------------------------------------------------

-- PURPOSE

--    XML数据有的是列表，有的是单节点数据。而且数据封装类中没有封装XML的code和message信息.所以通过一个适配类，来适配解析器和数据封装类。
--	  DefaultBasicAdapter是所有适配类的父类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

public abstract class DefaultBasicAdapter {
	private String code = "1";
	private String message = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return "0".equals(code);
	}
}
