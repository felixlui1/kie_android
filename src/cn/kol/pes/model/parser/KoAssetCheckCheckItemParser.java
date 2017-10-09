package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.parser.adapter.KoAssetCheckCheckItemAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<assetLastCheckTime>
//<checkList>
//<#list checkItemList as checkItemData>
//<checkItem>
//<itemSetId>${checkItemData.itemSetId?c}</itemSetId>
//<itemSeq>${checkItemData.itemSeq}</itemSeq>
//<queryText>${checkItemData.queryText}</queryText>
//<queryType>${checkItemData.queryType}</queryType>
//checkResult
//lastChkTime
//</checkItem>
//</#list>07-03 22:44:24.186: D/NetworkManager_XML(5538): <assetLastCheckTime>assetLastCheckTime</assetLastCheckTime>

//</checkList>
//</pes>

public class KoAssetCheckCheckItemParser extends DefaultBasicParser<KoAssetCheckCheckItemAdapter> {

	final String ASSETLASTCHECKTIME = "assetLastCheckTime";
	
	final String CHECKLIST = "checkList";
	final String CHECKITEM = "checkItem";
	
	final String ITEMSETID = "itemSetId";
	final String ITEMSEQ = "itemSeq";
	final String QUERYTEXT = "queryText";
	final String QUERYTYPE = "queryType";
	final String CHKCYCLE = "chkCycle";
	final String CHECKRESULT = "checkResult";
	final String LASTCHKTIME = "lastChkTime";
	
	public KoAssetCheckCheckItemParser(KoAssetCheckCheckItemAdapter adapter) {
		this.adapter = adapter;
	}
	
	private KoAssetCheckCheckItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(CHECKLIST)) {
			
		}else if(localName.equals(CHECKITEM)) {
			item  = new KoAssetCheckCheckItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETLASTCHECKTIME)) {
			this.adapter.assetLastCheckTime = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKITEM)) {
			this.adapter.add(item);
			item = null;
		}
		else if(localName.equals(ITEMSETID)) {
			item.itemSetId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ITEMSEQ)) {
			item.itemSeq = mBuffer.toString().trim();
		}
		else if(localName.equals(QUERYTEXT)) {
			item.queryText = mBuffer.toString().trim();
		}
		else if(localName.equals(QUERYTYPE)) {
			item.queryType = mBuffer.toString().trim();
		}
		else if(localName.equals(CHKCYCLE)) {
			item.chkCycle = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKRESULT)) {
			item.checkResult = mBuffer.toString().trim();
		}
		else if(localName.equals(LASTCHKTIME)) {
			item.lastChkTime = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
}
