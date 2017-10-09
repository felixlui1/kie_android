/*-----------------------------------------------------------

-- PURPOSE

--    获取已开启工序列表的解析器.

-- History

--	  1-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.model.parser.adapter.KoGetOpStartedListAdapter;

//<pes code="${code}" message="${message}">
//<seqStartedList>
//<#list seqList as seqData>
//<seqItem>
///public String transactionId;
//<wipEntityName>${seqData.wipEntityName}</wipEntityName>
//<wipEntityId>${seqData.wipEntityId}</wipEntityId>
//<creationDate>${seqData.creationDate}</creationDate>
//<createdBy>${seqData.createdBy}</createdBy>
//<lastUpdateDate>${seqData.lastUpdateDate}</lastUpdateDate>
//<lastUpdateBy>${seqData.lastUpdateBy}</lastUpdateBy>
//<fmOperationCode>${seqData.fmOperationCode}</fmOperationCode>
//<opDesc>${seqData.opDesc}</opDesc>
//<trxQuantity>${seqData.trxQuantity}</trxQuantity>
//<scrapQuantity>${seqData.scrapQuantity}</scrapQuantity>
//<assetDesc>${seqData.assetDesc}</assetDesc>
//<assettagNumber>${seqData.assettagNumber}</assettagNumber>
//<interfaced>${seqData.interfaced}</interfaced>
//<opStartDate>${seqData.opStartDate}</opStartDate>
//<opEndDate>${seqData.opEndDate}</opEndDate>
////<organizationId>${osJob.organizationId}</organizationId>
//</seqItem>
//</#list>
//</seqStartedList>
//</pes>

public class KoGetOpStartedListParser extends DefaultBasicParser<KoGetOpStartedListAdapter> {
	
	final String SEQSTARTEDLIST = "seqStartedList";
	final String ATTR_ISOPCOMPLETED = "isOpCompleted";
	final String ATTR_CURWORKINGOPCODE = "curWorkingOpCode";
	
	final String SEQITEM = "seqItem";
	
	final String TRANSACTIONID = "transactionId";
	final String WIPENTITYID = "wipEntityId";
	final String WIPENTITYNAME = "wipEntityName";
	final String CREATIONDATE = "creationDate";
	final String LASTUPDATEDATE = "lastUpdateDate";
	final String LASTUPDATEBY = "lastUpdateBy";
	final String FMOPERATIONCODE = "fmOperationCode";
	final String OPDESC = "opDesc";
	final String TRXQUANTITY = "trxQuantity";
	final String SCRAPQUANTITY = "scrapQuantity";
	final String ASSETDESC = "assetDesc";
	final String ASSETTAGNUMBER = "assettagNumber";
	final String INTERFACED = "interfaced";
	final String OPSTARTDATE = "opStartDate";
	final String OPENDDATE = "opEndDate";

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
	final String PCTCOMPLETE = "pctComplete";
	
	public KoGetOpStartedListParser(KoGetOpStartedListAdapter adapter){
		this.adapter = adapter;
	}
	
	KoOpStartedItem item;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(SEQSTARTEDLIST)){
			this.adapter.isOpCompleted = "Y".equals(attributes.getValue(ATTR_ISOPCOMPLETED));
			this.adapter.curWorkingOpCode = attributes.getValue(ATTR_CURWORKINGOPCODE);
		}
		else if(localName.equals(SEQITEM)){
			item  = new KoOpStartedItem();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(SEQITEM)){
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(TRANSACTIONID)){
			item.transactionId = mBuffer.toString().trim();
		}
		else if(localName.equals(WIPENTITYID)){
			item.jobObj.wipEntityId = (mBuffer.toString().trim());
		}
		else if(localName.equals(WIPENTITYNAME)){
			item.wipEntityName = mBuffer.toString().trim();
			item.jobObj.wipEntityName = item.wipEntityName;
		}
		else if(localName.equals(CREATIONDATE)){
			item.creationDate = (mBuffer.toString().trim());
		}
		else if(localName.equals(LASTUPDATEDATE)) {
			item.lastUpdateDate = (mBuffer.toString().trim());
		}
		else if(localName.equals(LASTUPDATEBY)){
			item.lastUpdateBy = (mBuffer.toString().trim());
		}
		else if(localName.equals(FMOPERATIONCODE)){
			item.fmOperationCode = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPDESC)){
			item.opDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(TRXQUANTITY)){
			item.trxQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(SCRAPQUANTITY)){
			item.scrapQuantity = (mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETDESC)){
			item.assetDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(ASSETTAGNUMBER)){
			item.assettagNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(INTERFACED)){
			item.interfaced = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPSTARTDATE)){
			item.opStartDate = (mBuffer.toString().trim());
		}
		else if(localName.equals(OPENDDATE)){
			item.opEndDate = (mBuffer.toString().trim());
		}
		
		//
		else if(localName.equals(SAITEM)){
			item.jobObj.saItem = (mBuffer.toString().trim());
		}
		else if(localName.equals(SAITEMDESC)){
			item.jobObj.saItemDesc = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFCPNNUMBER)){
			item.jobObj.dffCpnNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFCUSTOMERSPEC)){
			item.jobObj.dffCustomerspec = (mBuffer.toString().trim());
		}
		else if(localName.equals(DFFMFGSPEC)){
			item.jobObj.dffMfgSpec = (mBuffer.toString().trim());
		}
		else if(localName.equals(CUSTNUMBER)){
			item.jobObj.custNumber = (mBuffer.toString().trim());
		}
		else if(localName.equals(INCOMPLETEQUANTITY)){
			item.jobObj.incompleteQuantity = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(STARTQUANTITY)){
			item.jobObj.startQuantity = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(QUANTITYCOMPLETED)){
			item.jobObj.quantityCompleted = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(QUANTITYSCRAPPED)){
			item.jobObj.quantityScrapped = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(PRIMARYITEMID)){
			item.jobObj.primaryItemId = Integer.valueOf(mBuffer.toString().trim());
		}
		else if(localName.equals(COMMONROUTINGSEQUENCEID)){
			item.jobObj.commonRoutingSequenceId = (mBuffer.toString().trim());
		}
		else if(localName.equals(CUROPERATIONID)){
			item.jobObj.curOperationId = (mBuffer.toString().trim());
		}
		else if(localName.equals(ORGANIZATIONID)){
			item.jobObj.organizationId = (mBuffer.toString().trim());
		}
		else if(localName.equals(PCTCOMPLETE)){
			item.pctComplete = (mBuffer.toString().trim());
		}
		
		
		super.endElement(uri, localName, qName);
	}
	
}
