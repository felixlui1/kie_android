package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetCheckAssetItem;
import cn.kol.pes.model.parser.adapter.KoAssetCheckAssetItemAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<assetCheckInfoList>
//<#list assetItemList as assetItemData>
//<assetItem>
//<checkDate>${assetItemData.checkDate}</checkDate>
//<assetTotalNum>${assetItemData.assetTotalNum?c}</assetTotalNum>
//<assetNeedCheckNumLight>${assetItemData.assetNeedCheckNumLight?c}</assetNeedCheckNumLight>
//<assetNeedCheckNumNight>${assetItemData.assetNeedCheckNumNight?c}</assetNeedCheckNumNight>
//<assetCheckedNumLight>${assetItemData.assetCheckedNumLight?c}</assetCheckedNumLight>
//<assetCheckedNumNight>${assetItemData.assetCheckedNumNight?c}</assetCheckedNumNight>
//</assetItem>
//</#list>
//</assetCheckInfoList>
//</pes>

public class KoAssetCheckAssetItemParser extends DefaultBasicParser<KoAssetCheckAssetItemAdapter> {

	final String ASSETCHECKINFOLIST = "assetCheckInfoList";
	final String ASSETITEM = "assetItem";
	
	final String CHECKDATE = "checkDate";
	final String ASSETTOTALNUM = "assetTotalNum";
	final String ASSETNEEDCHECKNUMLIGHT = "assetNeedCheckNumLight";
	final String ASSETNEEDCHECKNUMNIGHT = "assetNeedCheckNumNight";
	final String ASSETCHECKEDNUMLIGHT = "assetCheckedNumLight";
	final String ASSETCHECKEDNUMNIGHT = "assetCheckedNumNight";
	
	public KoAssetCheckAssetItemParser(KoAssetCheckAssetItemAdapter adapter) {
		this.adapter = adapter;
	}
	
	private KoAssetCheckAssetItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(ASSETCHECKINFOLIST)){
			
		}else if(localName.equals(ASSETITEM)){
			item  = new KoAssetCheckAssetItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(ASSETITEM)){
			this.adapter.add(item);
			item = null;
		}
		else if(localName.equals(CHECKDATE)){
			item.checkDate = mBuffer.toString().trim();
		}
		else if(localName.equals(ASSETTOTALNUM)){
			item.assetTotalNum = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETNEEDCHECKNUMLIGHT)){
			item.assetNeedCheckNumLight = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETNEEDCHECKNUMNIGHT)){
			item.assetNeedCheckNumNight = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETCHECKEDNUMLIGHT)) {
			item.assetCheckedNumLight = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETCHECKEDNUMNIGHT)) {
			item.assetCheckedNumNight = Integer.valueOf(mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
}
