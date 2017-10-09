/*-----------------------------------------------------------

-- PURPOSE

--    登录返回员工信息的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<asset>
//<assetId>${assetInfo.assetId}</assetId>
//<assetNumber>${assetInfo.assetNumber}</assetNumber>
//<tagNumber>${assetInfo.tagNumber}</tagNumber>
//<description>${assetInfo.description}</description>
//<opCode>${assetInfo.opCode}</opCode>
//<opDscr>${assetInfo.opDscr}</opDscr>
//<location>${assetInfo.location}</location>
//<attribute7>${assetInfo.attribute7}</attribute7>
//</asset>
//</pes>

public class KoAssetGetAssetInfoParser extends DefaultBasicParser<KoAssetGetAssetInfoAdapter> {
	
	final String ASSETID = "assetId";
	final String ASSETNUMBER = "assetNumber";
	final String TAGNUMBER = "tagNumber";
	final String DESCRIPTION = "description";
	final String OPCODE = "opCode";
	final String OPDSCR = "opDscr";
	final String LOCATION = "location";
	final String ATTRIBUTE7 = "attribute7";
	
	final String ASSET = "asset";
	
	public KoAssetGetAssetInfoParser(KoAssetGetAssetInfoAdapter adapter){
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETID)){
			adapter.assetId = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETNUMBER)){
			adapter.assetNumber = mBuffer.toString().trim();
		}
		else if(localName.equals(TAGNUMBER)){
			adapter.tagNumber = mBuffer.toString().trim();
		}
		else if(localName.equals(DESCRIPTION)){
			adapter.description = mBuffer.toString().trim();
		}
		else if(localName.equals(OPCODE)){
			adapter.opCode = mBuffer.toString().trim();
		}
		else if(localName.equals(OPDSCR)){
			adapter.opDscr = mBuffer.toString().trim();
		}
		else if(localName.equals(LOCATION)){
			adapter.location = mBuffer.toString().trim();
		}
		else if(localName.equals(ATTRIBUTE7)){
			adapter.attribute7 = mBuffer.toString().trim();
		}
		super.endElement(uri, localName, qName);
	}
	
}
