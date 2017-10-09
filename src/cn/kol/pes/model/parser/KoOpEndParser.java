/*-----------------------------------------------------------

-- PURPOSE

--    完成工序的解析器.

-- History

--	  24-Oct-14  LiZheng  Created.

------------------------------------------------------------*/


package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoOpEndAdapter;

//<pes code="${code}" message="${message}">
//<transactionId>111222</transactionId>
//</pes>

public class KoOpEndParser extends DefaultBasicParser<KoOpEndAdapter> {
	
	final String TRANSACTIONID = "transactionId";
	
	public KoOpEndParser(KoOpEndAdapter adapter) {
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
