/*-----------------------------------------------------------

-- PURPOSE

--    开始工序的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/


package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoOpStartAdapter;

//<pes code="${code}" message="${message}">
//<transactionId>111222</transactionId>
//</pes>

public class KoOpStartParser extends DefaultBasicParser<KoOpStartAdapter> {
	
	final String TRANSACTIONID = "transactionId";
	
	public KoOpStartParser(KoOpStartAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(TRANSACTIONID)){
			adapter.transactionId = mBuffer.toString().trim();
		}
		super.endElement(uri, localName, qName);
	}
	
}
