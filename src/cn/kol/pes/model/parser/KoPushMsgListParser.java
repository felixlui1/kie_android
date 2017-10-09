/*-----------------------------------------------------------

-- PURPOSE

--    推送消息的解析器.

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoPushMsgItem;
import cn.kol.pes.model.parser.adapter.KoPushMsgAdapter;

//<pes code="${code}" message="${message}">
//<pushMsgList>
//<#list pushMsgList as msgData>
//<msgItem>
//<title>${msgData.title}</title>
//<msg>${msgData.msg}</msg>
//</msgItem>
//</#list>
//</pushMsgList>
//</pes>

public class KoPushMsgListParser extends DefaultBasicParser<KoPushMsgAdapter> {
	
	final String PUSHMSGLIST = "pushMsgList";
	final String MSGITEM = "msgItem";
	final String TRANSID = "transId";
	final String TITLE = "title";
	final String MSG = "msg";
	
	public KoPushMsgListParser(KoPushMsgAdapter adapter){
		this.adapter = adapter;
	}
	
	KoPushMsgItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(PUSHMSGLIST)){
			
		}else if(localName.equals(MSGITEM)){
			item  = new KoPushMsgItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(MSGITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(TRANSID)){
			item.transId = mBuffer.toString().trim();
		}
		else if(localName.equals(TITLE)){
			item.title = mBuffer.toString().trim();
		}
		else if(localName.equals(MSG)){
			item.msg = (mBuffer.toString().trim());
		}

		super.endElement(uri, localName, qName);
	}
	
}
