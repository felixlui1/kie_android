/*-----------------------------------------------------------

-- PURPOSE

--    取消点检设备的解析器.

-- History

--	  11-Aug-16  LiZheng  Created.

------------------------------------------------------------*/


package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoAssetCheckCancelCheckAssetAdapter;

//<pes code="${code}" message="${message}">
//</pes>

public class KoCheckAssetCancelCheckAssetParser extends DefaultBasicParser<KoAssetCheckCancelCheckAssetAdapter> {
	
	public KoCheckAssetCancelCheckAssetParser(KoAssetCheckCancelCheckAssetAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		super.endElement(uri, localName, qName);
	}
	
}
