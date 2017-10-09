/*-----------------------------------------------------------

-- PURPOSE

--    升级APK的解析器.

-- History

--	  17-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">
//<needUpdate>${needUpdate}</needUpdate>
//<updateApkUrl>${updateApkUrl}</updateApkUrl>
//</pes>

public class KoUpdateApkParser extends DefaultBasicParser<KoUpdateApkAdapter> {
	
	final String NEEDUPDATE = "needUpdate";
	final String UPDATEAPKURL = "updateApkUrl";
	final String UPDATEMSG = "updateMsg";
	final String ISFORCEUPDATE = "isForceUpdate";
	
	public KoUpdateApkParser(KoUpdateApkAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(NEEDUPDATE)){
			adapter.needUpdate = mBuffer.toString().trim();
		}
		else if(localName.equals(UPDATEAPKURL)){
			adapter.updateApkUrl = mBuffer.toString().trim();
		}
		else if(localName.equals(UPDATEMSG)){
			adapter.updateMsg = mBuffer.toString().trim();
		}
		else if(localName.equals(ISFORCEUPDATE)){
			adapter.setIsForceUpdate(mBuffer.toString().trim());
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
