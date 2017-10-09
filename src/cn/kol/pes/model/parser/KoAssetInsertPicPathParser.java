/*-----------------------------------------------------------

-- PURPOSE

--    插入图片路径的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoAssetInsertPicPathAdapter;

//<pes code="${code}" message="${message}">
//</pes>

public class KoAssetInsertPicPathParser extends DefaultBasicParser<KoAssetInsertPicPathAdapter> {
	
	
	public KoAssetInsertPicPathParser(KoAssetInsertPicPathAdapter adapter) {
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
