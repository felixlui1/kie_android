/*-----------------------------------------------------------

-- PURPOSE

--    XML解析器的父类.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.helpers.DefaultHandler;

public abstract class BasicParser extends DefaultHandler {
	final String PES = "pes";
	final String CODE_ATTR = "code";
	final String ERROR_KEY = "message";
	
	public static final int ERROR_MESSAGE = 400;
	StringBuffer mBuffer = new StringBuffer();
	
	int currentState = 0;
}
