package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoAssetCheckUpdateSetAssetCheckAdapter;

public class KoAssetCheckUpdateSetAssetCheckParser extends DefaultBasicParser<KoAssetCheckUpdateSetAssetCheckAdapter> {

	final String CHECK_ID = "checkId";
	
	public KoAssetCheckUpdateSetAssetCheckParser(KoAssetCheckUpdateSetAssetCheckAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(CHECK_ID)){
			//adapter.checkId = mBuffer.toString();
		}
		
		super.endElement(uri, localName, qName);
	}
}
