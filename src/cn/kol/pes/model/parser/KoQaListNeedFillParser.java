/*-----------------------------------------------------------

-- PURPOSE

--    质量收集计划数据项列表的解析器.

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoQaNeedFillItem;
import cn.kol.pes.model.parser.adapter.KoQaListNeedFillAdapter;

//<?xml version="1.0" encoding="UTF-8" ?>
//<pes code="${code}" message="${message}">

//<isLastSeq>${isLastSeq}</isLastSeq>
//<incompleteQuan>${incompleteQuan}</incompleteQuan>
//<minStartTime>${minStartTime}</minStartTime>
//<maxEndTime>${maxEndTime}</maxEndTime>

//<qaList>
//<#list qaList as qaData>
//<qaItem>
//<charId>${qaData.charId}</charId>
//<charName>${qaData.charName}</charName>
//<prompt>${qaData.prompt}</prompt>
//<resultColumnName>${qaData.resultColumnName}</resultColumnName>
//<datatypeMeaning>${qaData.datatypeMeaning}</datatypeMeaning>
//<derivedFlag>${qaData.derivedFlag}</derivedFlag>
//<mandatoryFlag>${qaData.mandatoryFlag}</mandatoryFlag>
//<readOnlyFlag>${qaData.readOnlyFlag}</readOnlyFlag>
//<qaValueList>
//<qaValue shortCode="${qaValue.shortCode}" description="${qaValue.description}"></qaValue>
//</qaValueList>
//</qaItem>
//</#list>
//</qaList>

//<qaChildPlanIdList>
//<#list qaChildPlanIdList as qaChildPlanId>
//<qaChildPlan planId="${qaChildPlan.planId}" planName="${qaChildPlan.planName}"></qaChildPlan>
//</#list>
//</qaChildPlanIdList>

//</pes>

public class KoQaListNeedFillParser extends DefaultBasicParser<KoQaListNeedFillAdapter> {
	
	final String QALIST = "qaList";
	final String QAITEM = "qaItem";
	
	final String ISLASTSEQ = "isLastSeq";
	final String INCOMPLETEQUAN = "incompleteQuan";
	final String MINSTARTTIME = "minStartTime";
	final String MAXENDTIME = "maxEndTime";
	
	final String QACHILDPLANIDLIST = "qaChildPlanIdList";
	final String QACHILDPLAN = "qaChildPlan";
	final String PLANID = "planId";
	final String PLANNAME = "planName";
	
	final String CHARID = "charId";
	final String CHARNAME = "charName";
	final String PROMPT = "prompt";
	final String RESULTCOLUMNNAME = "resultColumnName";
	final String DATATYPEMEANING = "datatypeMeaning";
	final String DERIVEDFLAG = "derivedFlag";
	final String MANDATORYFLAG = "mandatoryFlag";
	final String READONLYFLAG = "readOnlyFlag";
	
	final String QAVALUE = "qaValue";
	final String SHORTCODE = "shortCode";
	final String DESCRIPTION = "description";

	
	public KoQaListNeedFillParser(KoQaListNeedFillAdapter adapter) {
		this.adapter = adapter;
	}
	
	KoQaNeedFillItem item;
	
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals(QALIST)) {
			
		}
		else if(localName.equals(QAITEM)) {
			item  = new KoQaNeedFillItem();
		}
		else if(localName.equals(QAVALUE)) {
			item.addQaValueList(attributes.getValue(SHORTCODE), attributes.getValue(DESCRIPTION));
		}
		else if(localName.equals(QACHILDPLAN)) {
			adapter.addPlanIDToChildPlanIdList(attributes.getValue(PLANID), attributes.getValue(PLANNAME));
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(QAITEM)) {
			adapter.add(item);
			item = null;
		}
		else if(localName.equals(CHARID)){
			item.charId = mBuffer.toString().trim();
		}
		else if(localName.equals(CHARNAME)){
			item.charName = mBuffer.toString().trim();
		}
		else if(localName.equals(PROMPT)){
			item.prompt = mBuffer.toString().trim();
		}
		else if(localName.equals(RESULTCOLUMNNAME)){
			item.resultColumnName = mBuffer.toString().trim();
		}
		else if(localName.equals(DATATYPEMEANING)){
			item.datatypeMeaning = mBuffer.toString().trim();
		}
		else if(localName.equals(DERIVEDFLAG)){
			item.derivedFlag = mBuffer.toString().trim();
		}
		else if(localName.equals(MANDATORYFLAG)){
			item.mandatoryFlag = mBuffer.toString().trim();
		}
		else if(localName.equals(READONLYFLAG)){
			item.readOnlyFlag = mBuffer.toString().trim();
		}
		
		else if(localName.equals(ISLASTSEQ)){
			adapter.mIsLastSeq = "Y".equals(mBuffer.toString().trim());
		}
		else if(localName.equals(MINSTARTTIME)){
			adapter.minStartTime = mBuffer.toString().trim();
		}
		else if(localName.equals(MAXENDTIME)){
			adapter.maxEndTime = mBuffer.toString().trim();
		}
		else if(localName.equals(INCOMPLETEQUAN)){
			adapter.incompleteQuan = mBuffer.toString().trim();
		}
		
		else if(localName.equals("timeBufferOpEnd")){
			adapter.timeBufferOpEnd = mBuffer.toString().trim();
		}
		else if(localName.equals("scrapQuanTotal")){
			adapter.scrapQuanTotal = mBuffer.toString().trim();
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
