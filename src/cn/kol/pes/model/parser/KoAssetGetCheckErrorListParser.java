/*-----------------------------------------------------------

-- PURPOSE

--    设备点检列表的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetCheckErrorListAdapter;

//<pes code="0" message="">
//<assetList>
//<assetItem>
//<checkId>1001026</checkId>
//<creationDate>2014-10-05 00:00:00</creationDate>
//<createdBy>52277</createdBy>
//<createdByName>${assetData.createdByName}</createdByName>
//<lastUpdateDate>2014-10-05 00:00:00</lastUpdateDate>
//<lastUpdatedBy>52277</lastUpdatedBy>
//<assetId>52277</assetId>
//<assetName>52277</assetName>
//<assetTagNumber>SDJ-4#</assetTagNumber>

//<assetOpDscr>${assetData.assetOpDscr}</assetOpDscr>
//<checkTime>2014-10-05 00:00:00</checkTime>
//<checkResult>2</checkResult>
//<estRepairStart></estRepairStart>
//<estRepairEnd></estRepairEnd>
//<checkRemarks></checkRemarks>
//<failureCode>0</failureCode>
//<opCode>${assetData.opCode}</opCode>
//</assetItem>


public class KoAssetGetCheckErrorListParser extends DefaultBasicParser<KoAssetGetCheckErrorListAdapter> {
	
	final String ASSETLIST = "assetList";
	final String ASSETITEM = "assetItem";
	
	final String CHECKID = "checkId";
	final String CREATIONDATE = "creationDate";
	final String CREATEDBY = "createdBy";
	final String CREATEDBYNAME = "createdByName";
	final String LASTUPDATEDATE = "lastUpdateDate";
	final String LASTUPDATEDBY = "lastUpdatedBy";
	final String ASSETID = "assetId";
	final String ASSETNAME = "assetName";
	final String ASSETTAGNUMBER = "assetTagNumber";
	final String ASSETOPDSCR = "assetOpDscr";
	final String CHECKTIME = "checkTime";
	
	final String CHECKRESULT = "checkResult";
	final String ESTREPAIRSTART = "estRepairStart";
	final String ESTREPAIREND = "estRepairEnd";
	final String CHECKREMARKS = "checkRemarks";
	final String FAILURECODE = "failureCode";
	
	final String OPCODE = "opCode";
	
	public KoAssetGetCheckErrorListParser(KoAssetGetCheckErrorListAdapter adapter){
		this.adapter = adapter;
	}
	
	KoAssetCheckItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(ASSETLIST)){
			
		}else if(localName.equals(ASSETITEM)){
			item  = new KoAssetCheckItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(CHECKID)){
			item.checkId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(CREATIONDATE)){
			item.creationDate = mBuffer.toString().trim();
		}
		else if(localName.equals(CREATEDBY)){
			item.createdBy = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(CREATEDBYNAME)){
			item.createdByName = mBuffer.toString().trim();
		}
		else if(localName.equals(LASTUPDATEDATE)){
			item.lastUpdateDate = mBuffer.toString().trim();
		}
		else if(localName.equals(LASTUPDATEDBY)){
			item.lastUpdatedBy = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETID)){
			item.assetId = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETNAME)){
			item.assetName = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETTAGNUMBER)){
			item.assetTagNum = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETOPDSCR)){
			item.assetOpDscr = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKTIME)){
			item.checkTime = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKRESULT)){
			item.checkResult = mBuffer.toString().trim();
		}
		else if(localName.equals(ESTREPAIRSTART)){
			item.estRepairStart = mBuffer.toString().trim();
		}
		else if(localName.equals(ESTREPAIREND)){
			item.estRepairEnd = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKREMARKS)){
			item.checkRemarks = mBuffer.toString().trim();
		}
		else if(localName.equals(FAILURECODE)){
			item.failureCode = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(OPCODE)){
			item.opCode = mBuffer.toString().trim();
		}
		

		super.endElement(uri, localName, qName);
	}
	
}
