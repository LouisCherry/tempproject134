package com.epoint.auditselfservice.auditselfservicerest.common;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

/**
 * 事项相关接口
 * 
 * @作者 WST
 * @version [F9.3, 2017年11月9日]
 */

public class SyncbanlididiansjService
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 同步办事流程
     * 
     * @param OUT_FLOW_INFO
     * @param taskId
     */
    @SuppressWarnings("unchecked")
    public static List<Record> syncbanlididiansj(String banlididiansj) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(banlididiansj);

            if (banlididiansj != null && StringUtil.isNotBlank(banlididiansj)) {
                if (document != null) {
                    Element root = document.getRootElement();
                    Element data1 = root.element("ACCEPT_ADDRESSS");
                    List<Element> datas = data1.elements("ACCEPT_ADDRESS");
                    for (Element data : datas) {

                        Element OFFICE_HOUR = data.element("OFFICE_HOUR");

                        Record record = new Record();

                        if (StringUtil.isNotBlank(OFFICE_HOUR)) {
                            record.set("officehour", OFFICE_HOUR.getStringValue());
                        }

                        list.add(record);
                    }
                }
            }
            return list;
        }
        catch (Exception e) {
            e.printStackTrace();
            return list;
        }

    }

}
