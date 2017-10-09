/*-----------------------------------------------------------

-- PURPOSE

--    判断服务器是否可用的解析器.

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.parser.adapter.KoDataEnableAdapter;

//<pes code="${code}" message="${message}">
//<weekList>
//<#list weekList as weekData>
//<weekItem week="${weekData.week}">
//<week>${weekData.week}</week>
//<startEnd>${weekData.startEnd}</startEnd>
//<hourMin>${weekData.hourMin}</hourMin>
//</weekItem>
//</#list>
//</weekList>
//</pes>

public class KoDataEnableParser extends DefaultBasicParser<KoDataEnableAdapter> {
	
	private static final String WEEKLIST = "weekList";
	private static final String WEEKITEM = "weekItem";
	private static final String WEEK = "week";
	private static final String STARTEND = "startEnd";
	private static final String HOURMIN = "hourMin";
	
	private KoWeekItem mWeekData;
	
	public KoDataEnableParser(KoDataEnableAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals(WEEKITEM)){
			if(adapter.getWeekMap().get(attributes.getValue(WEEK)) == null) {
				mWeekData = new KoWeekItem();
			}
			else {
				mWeekData = adapter.getWeekMap().get(attributes.getValue(WEEK));
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(localName.equals(WEEKITEM)) {
			adapter.addWeek(mWeekData.week, mWeekData);
		}
		else if(localName.equals(WEEK)) {
			mWeekData.week = mBuffer.toString().trim();
		}
		else if(localName.equals(STARTEND)) {
			mWeekData.startEnd = mBuffer.toString().trim();
		}
		else if(localName.equals(HOURMIN)) {
			if("BACKUP_SCHEDULE_S".equals(mWeekData.startEnd)) {
				mWeekData.setStart( mBuffer.toString().trim());
			}
			else if("BACKUP_SCHEDULE_E".equals(mWeekData.startEnd)) {
				mWeekData.setEnd( mBuffer.toString().trim());
			}
		}
		
		super.endElement(uri, localName, qName);
	}
	
}
