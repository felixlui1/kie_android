package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.model.parser.adapter.KoAssetCheckSetAssetCheckAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<setAssetCheckList>
//<#list assetList as assetData>
//<assetItem>
//<assetId>${assetData.assetId?c}</assetId>
//<assetTagNum>${assetData.assetTagNum}</assetTagNum>
//<assetDescription>${assetData.assetDescription}</assetDescription>
//<scheduledId>${assetData.scheduledId?c}</scheduledId>
//<checkedId>${assetData.checkedId?c}</checkedId>
//</assetItem>
//</#list>
//</setAssetCheckList>
//</pes>

public class KoAssetCheckSetAssetCheckItemParser extends DefaultBasicParser<KoAssetCheckSetAssetCheckAdapter> {

	final String SETASSETCHECKLIST = "setAssetCheckList";
	final String ASSETITEM = "assetItem";
	
	final String ASSETID = "assetId";
	final String ASSETTAGNUM = "assetTagNum";
	final String ASSETDESCRIPTION = "assetDescription";
	final String SCHEDULEDID = "scheduledId";
	final String CHECKEDID = "checkedId";
	
	public KoAssetCheckSetAssetCheckItemParser(KoAssetCheckSetAssetCheckAdapter adapter) {
		this.adapter = adapter;
	}
	
	private KoAssetCheckSetAssetCheckItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SETASSETCHECKLIST)){
			
		}else if(localName.equals(ASSETITEM)){
			item  = new KoAssetCheckSetAssetCheckItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETITEM)){
			this.adapter.add(item);
			item = null;
		}
		else if(localName.equals(ASSETID)){
			item.assetId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETTAGNUM)){
			item.assetTagNum = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETDESCRIPTION)){
			item.assetDescription = mBuffer.toString().trim();
		}
		else if(localName.equals(SCHEDULEDID)){
			item.scheduledId = mBuffer.toString().trim();
		}
		else if(localName.equals(CHECKEDID)) {
			item.checkedId = mBuffer.toString().trim();
		}

		super.endElement(uri, localName, qName);
	}
}
