/*-----------------------------------------------------------

-- PURPOSE

--    工序列表的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.parser.adapter.KoGetOpListAdapter;

//<pes code="0" message="">
//<seqList>
//<seqItem>
//<routingSeqId>1586</routingSeqId>
//<operationSequenceId>1399705</operationSequenceId>
//standardSequenceId
//<operationSeqNum>12</operationSeqNum>
//<standardOperationCode>T000</standardOperationCode>
//<departmentCode>WKShop_TF</departmentCode>
//<departmentId>16</departmentId>
//<operationDescription>operationDescription</operationDescription>
//</seqItem>

public class KoGetOpListParser extends DefaultBasicParser<KoGetOpListAdapter> {
	
	final String SEQLIST = "seqList";
	final String SEQITEM = "seqItem";
	final String ROUTINGSEQID = "routingSeqId";
	final String OPERATIONSEQUENCEID = "operationSequenceId";
	final String STANDARDSEQUENCEID = "standardSequenceId";
	final String OPERATIONSEQNUM = "operationSeqNum";
	final String STANDARDOPERATIONCODE = "standardOperationCode";
	final String DEPARTMENTCODE = "departmentCode";
	final String DEPARTMENTID = "departmentId";
	final String OPERATIONDESCRIPTION = "operationDescription";
	
	public KoGetOpListParser(KoGetOpListAdapter adapter){
		this.adapter = adapter;
	}
	
	KoOpItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SEQLIST)){
			
		}else if(localName.equals(SEQITEM)){
			item  = new KoOpItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(ROUTINGSEQID)){
			item.routingSeqId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATIONSEQUENCEID)){
			item.operationSequenceId = (mBuffer.toString().trim());
		}
		else if(localName.equals(STANDARDSEQUENCEID)) {
			item.standardSequenceId = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATIONSEQNUM)){
			item.operationSeqNum = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(STANDARDOPERATIONCODE)){
			item.standardOperationCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(DEPARTMENTCODE)){
			item.departmentCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(DEPARTMENTID)){
			item.departmentId = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPERATIONDESCRIPTION)){
			item.operationDescription = (mBuffer.toString().trim());
		}
		super.endElement(uri, localName, qName);
	}
	
}
