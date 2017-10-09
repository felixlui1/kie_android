/*-----------------------------------------------------------

-- PURPOSE

--    XML解析器的父类，实现了对XML中状态码code，和状态信息message的解析

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import cn.kol.pes.model.parser.adapter.DefaultBasicAdapter;


public abstract class DefaultBasicParser<E extends DefaultBasicAdapter> extends BasicParser {
	
	E adapter;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		super.startElement(uri, localName, qName, attributes);
		
		if(localName.equals(PES)){
			adapter.setCode(attributes.getValue(CODE_ATTR));
			adapter.setMessage(attributes.getValue(ERROR_KEY));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		super.endElement(uri, localName, qName);
		
		mBuffer.delete(0, mBuffer.length());
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
		super.characters(ch, start, length);
		
		mBuffer.append(ch, start, length);
	}
	
}
