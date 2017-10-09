/*-----------------------------------------------------------

-- PURPOSE

--    设备故障列表的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoAssetMachineFailItem;
import cn.kol.pes.model.parser.adapter.KoAssetMachineFailListAdapter;

//<pes code="${code}" message="${message}">
//<machineFailList>
//<#list machineFailList as machineFail>
//<failItem>
//<lookupCode>${machineFail.lookupCode}</lookupCode>
//<tag>${machineFail.tag}</tag>
//<meaning>${machineFail.meaning}</meaning>
//</failItem>
//</#list>
//</machineFailList>
//</pes>


public class KoAssetMachineFailListParser extends DefaultBasicParser<KoAssetMachineFailListAdapter> {
	
	final String MACHINEFAILLIST = "machineFailList";
	final String FAILITEM = "failItem";
	
	final String LOOKUPCODE = "lookupCode";
	final String TAG = "tag";
	final String MEANING = "meaning";
	
	
	public KoAssetMachineFailListParser(KoAssetMachineFailListAdapter adapter){
		this.adapter = adapter;
	}
	
	KoAssetMachineFailItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(MACHINEFAILLIST)){
			
		}else if(localName.equals(FAILITEM)){
			item  = new KoAssetMachineFailItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(FAILITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(LOOKUPCODE)){
			item.lookupCode = mBuffer.toString().trim();
		}
		else if(localName.equals(TAG)){
			item.tag = mBuffer.toString().trim();
		}
		else if(localName.equals(MEANING)){
			item.meaning = mBuffer.toString().trim();
		}

		super.endElement(uri, localName, qName);
	}
	
}
