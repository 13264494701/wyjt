package com.jxf.svc.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: rizenguo
 * Date: 2014/11/1
 * Time: 14:06
 */
public class XMLParser {
	
	private static final Logger logger = LoggerFactory.getLogger(XMLParser.class);
	
	/** 
     * @description 将xml字符串转换成map，适合单层节点的xml
     * @param xml 
     * @return Map 
     */  
	@SuppressWarnings("unchecked")
	public static Map<String, String> readStringXmlOut(String xml) {  
        Map<String,String> map = new HashMap<String,String>();  
        Document doc = null;  
        try {  
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML  
            Element rootElt = doc.getRootElement(); // 获取根节点  
            List<Element> list = rootElt.elements();//获取根节点下所有节点  
            for (Element element : list) {  //遍历节点  
                map.put(element.getName(), element.getText()); //节点的name为map的key，text为map的value  
            }  
        } catch (DocumentException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return map;  
    }  
	/** 
	 * @description 将xml字符串转换成map,适合xml格式为多层节点
	 * @param xml 
	 * @return Map 
	 */  
	public static Map<String, Object> xmlStrConvertToMap(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			map = getElement(rootElt);
		} catch (DocumentException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		return map;
	}  
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getElement(Element element) {
    	Map<String, Object> map = new HashMap<String,Object>();
		 List<Element> elements = element.elements();
		 for (Element elementChild : elements) {
			 List<Element> childElements = elementChild.elements();
			 if(childElements.size() > 0) {
				 map.put(elementChild.getName(), getElement(elementChild));
			 }else {
				 map.put(elementChild.getName(), elementChild.getText());
			 }
		 }
		 return map;
	}
    
    public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><qrytransrsp><ret>000000</ret><memo>成功</memo><trans><merdt>20190422</merdt><orderno>317735889738534912</orderno><accntno>6216600100007159070</accntno><accntnm>刘辉辉</accntnm><amt>400</amt><entseq></entseq><memo></memo><state>1</state><result>渠道资金到账已复核,交易已发送</result><reason>交易成功</reason><tpst>0</tpst><rspcd></rspcd><transStatusDesc>success</transStatusDesc></trans></qrytransrsp>";
		Map<String, Object> map = xmlStrConvertToMap(xml);
		System.out.println(map.toString());
		Map<String, Object> map2 = (Map<String, Object>) map.get("trans");
		System.out.println(map2.toString());
		System.out.println((String)map2.get("rspcd"));
    }
}
