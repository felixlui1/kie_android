/*-----------------------------------------------------------

-- PURPOSE

--    登录返回员工信息的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoLoginAdapter;

//<pes code="${code}" message="${message}">
//<staff>
//<staffNo>${loginData.staffNo}</staffNo>
//<staffName>${loginData.staffName}</staffName>
//<cardNo>${loginData.cardNo}</cardNo>
//<levelCode>${loginData.levelCode}</levelCode>
//</staff>
//</pes>

public class KoLoginParser extends DefaultBasicParser<KoLoginAdapter> {
	
	final String STAFF_NO = "staffNo";
	final String STAFF_NAME = "staffName";
	final String CARD_NO = "cardNo";
	final String LEVEL_CODE = "levelCode";
	
	public KoLoginParser(KoLoginAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(STAFF_NO)){
			adapter.staffNo = mBuffer.toString().trim();
		}
		else if(localName.equals(STAFF_NAME)){
			adapter.staffName = mBuffer.toString().trim();
		}
		else if(localName.equals(CARD_NO)){
			adapter.cardNo = mBuffer.toString().trim();
		}
		else if(localName.equals(LEVEL_CODE)){
			adapter.levelCode = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
