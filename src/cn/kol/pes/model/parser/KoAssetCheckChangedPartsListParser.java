package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoChangedPartItem;
import cn.kol.pes.model.parser.adapter.KoAssetCheckChangedPartsListAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<partsList>
//<#list changedPartsList as changedPartData>
//<partItem>${changedPartData}</partItem>
//</#list>
//</partsList>
//</pes>

public class KoAssetCheckChangedPartsListParser extends DefaultBasicParser<KoAssetCheckChangedPartsListAdapter> {

	final String PARTSLIST = "partsList";
	final String PARTITEM = "partItem";
	

	public KoAssetCheckChangedPartsListParser(KoAssetCheckChangedPartsListAdapter adapter) {
		this.adapter = adapter;
	}
	

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(PARTSLIST)){
			
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(PARTITEM)){
			this.adapter.add(new KoChangedPartItem(mBuffer.toString().trim()));
		}

		super.endElement(uri, localName, qName);
	}
}
