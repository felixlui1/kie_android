/*-----------------------------------------------------------

-- PURPOSE

--    工单信息的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.parser.adapter.KoGetJobListAdapter;


//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="0" message="">
//<osJobList>
//<osJob>
//<wipEntityId>1076851</wipEntityId>
//<wipEntityName>PS1401-001</wipEntityName>
//<saItem>DA40A0AAAD0</saItem>
//<saItemDesc>40±1KHZ/-20+70/2000±15%pF/14*9MM铝壳</saItemDesc>
//<dffCpnNumber></dffCpnNumber>
//<dffCustomerspec></dffCustomerspec>
//<dffMfgSpec></dffMfgSpec>
//<custNumber></custNumber>
//<incompleteQuantity>3439</incompleteQuantity>
//<startQuantity>3439</startQuantity>
//<quantityCompleted>0</quantityCompleted>
//<quantityScrapped>0</quantityScrapped>
//<primaryItemId>679650</primaryItemId>
//<commonRoutingSequenceId>697713</commonRoutingSequenceId>
//<curOperationId>curOperationId</curOperationId>
//<organizationId>${osJob.organizationId}</organizationId>
//</osJob>
//</osJobList>
//</pes>

public class KoGetJobListParser extends DefaultBasicParser<KoGetJobListAdapter> {
	
	final String OSJOBLIST = "osJobList";
	final String OSJOB = "osJob";
	final String WIPENTITYID = "wipEntityId";
	final String WIPENTITYNAME = "wipEntityName";
	final String SAITEM = "saItem";
	final String SAITEMDESC = "saItemDesc";
	final String DFFCPNNUMBER = "dffCpnNumber";
	final String DFFCUSTOMERSPEC = "dffCustomerspec";
	final String DFFMFGSPEC = "dffMfgSpec";
	final String CUSTNUMBER = "custNumber";
	final String INCOMPLETEQUANTITY = "incompleteQuantity";
	final String STARTQUANTITY = "startQuantity";
	final String QUANTITYCOMPLETED = "quantityCompleted";
	final String QUANTITYSCRAPPED = "quantityScrapped";
	final String PRIMARYITEMID = "primaryItemId";
	final String COMMONROUTINGSEQUENCEID = "commonRoutingSequenceId";
	final String CUROPERATIONID = "curOperationId";
	final String ORGANIZATIONID = "organizationId";
	
	public KoGetJobListParser(KoGetJobListAdapter adapter){
		this.adapter = adapter;
	}
	
	KoJobItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(OSJOBLIST)){
			
		}else if(localName.equals(OSJOB)){
			item  = new KoJobItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(OSJOB)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(WIPENTITYID)){
			item.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIPENTITYNAME)){
			item.wipEntityName = (mBuffer.toString().trim());
		}
		else if(localName.equals(SAITEM)){
			item.saItem = (mBuffer.toString().trim());
		}
		else if(localName.equals(SAITEMDESC)){
			item.saItemDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFCPNNUMBER)){
			item.dffCpnNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFCUSTOMERSPEC)){
			item.dffCustomerspec = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFMFGSPEC)){
			item.dffMfgSpec = (mBuffer.toString().trim());
		}
		else if(localName.equals(CUSTNUMBER)){
			item.custNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(INCOMPLETEQUANTITY)){
			item.incompleteQuantity = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(STARTQUANTITY)){
			item.startQuantity = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(QUANTITYCOMPLETED)){
			item.quantityCompleted = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(QUANTITYSCRAPPED)){
			item.quantityScrapped = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(PRIMARYITEMID)){
			item.primaryItemId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(COMMONROUTINGSEQUENCEID)){
			item.commonRoutingSequenceId = (mBuffer.toString().trim());
		}
		else if(localName.equals(CUROPERATIONID)){
			item.curOperationId = (mBuffer.toString().trim());
		}
		else if(localName.equals(ORGANIZATIONID)){
			item.organizationId = (mBuffer.toString().trim());
		}
		
		
		super.endElement(uri, localName, qName);
	}
	
}
