/*-----------------------------------------------------------

-- PURPOSE

--    获取最大可投入数的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.parser.adapter.KoOpMaxQuanAdapter;

//<pes code="${code}" message="${message}">
//<maxQuan>111222</maxQuan>
//<opAssetList>
//<#list opAssetList as opAsset>
//<opAssetItem assetId="${opAsset.assetId}" tagNumber="${opAsset.tagNumber}" assetDesc="${opAsset.assetDesc}"></opAssetItem>
//</#list>
//</opAssetList>
//</pes>

public class KoOpMaxQuanParser extends DefaultBasicParser<KoOpMaxQuanAdapter> {
	
	final String MAXQUAN = "maxQuan";
	final String OPASSETITEM = "opAssetItem";
	
	final String ASSETID = "assetId";
	final String TAGNUMBER = "tagNumber";
	final String ASSETDESC = "assetDesc";
	
	public KoOpMaxQuanParser(KoOpMaxQuanAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals(OPASSETITEM)) {
			KoAssetCheckItem asset = new KoAssetCheckItem();
			asset.assetId = attributes.getValue(ASSETID);
			asset.assetTagNum = attributes.getValue(TAGNUMBER);
			asset.assetOpDscr = attributes.getValue(ASSETDESC);
			adapter.addOpAsset(asset);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(MAXQUAN)){
			adapter.maxQuan = mBuffer.toString().trim();
		}
		super.endElement(uri, localName, qName);
	}
	
}
