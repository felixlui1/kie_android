/*-----------------------------------------------------------

-- PURPOSE

--    添加点检设备的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoAssetCheckAddAdapter;

//<pes code="${code}" message="${message}">
//<checkId>111222</checkId>
//</pes>

public class KoAssetCheckAddParser extends DefaultBasicParser<KoAssetCheckAddAdapter> {
	
	final String CHECK_ID = "checkId";
	
	public KoAssetCheckAddParser(KoAssetCheckAddAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(CHECK_ID)){
			adapter.checkId = mBuffer.toString();
		}
		super.endElement(uri, localName, qName);
	}
	
}
