package com.epoint.powersupply.util;

import com.epoint.core.utils.date.EpointDateUtil;

import java.util.Date;

public class NationUtils
{

    /**
     * 
     * [反参拼接]
     * 
     * @param status
     * @param data
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String rtnXml(String status, String data) {
        String rtnXmlString = "";
        /*rtnXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" 
                + "<Code>"+ status +"</Code>\r\n" + "<MSG>"+ data +"</MSG>\r\n" + "<Time>"
                + EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss") + "</Time>\r\n" + "<PARAMS></PARAMS>\r\n" ;*/
//        rtnXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
//                + "<item>\r\n"
//                + "\t<Code>"+ status +"</Code>\r\n" + "\t<MSG>"+ data +"</MSG>\r\n" + "\t<Time>"
//                + EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss") + "</Time>\r\n" + "\t<PARAMS></PARAMS>\r\n"
//                + "</item>";

        rtnXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><item><CODE>" + status + "</CODE><MSG>" + data + "</MSG><PARAMS></PARAMS></item>";
        return rtnXmlString;
    }
}
