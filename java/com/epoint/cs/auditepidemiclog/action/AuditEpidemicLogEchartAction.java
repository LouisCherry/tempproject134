package com.epoint.cs.auditepidemiclog.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;


import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.auditepidemiclog.api.IAuditEpidemicLogService;


/**
 * 访客登记list页面对应的后台
 * 
 * @author Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@RestController("auditepidemiclogechartaction")
@Scope("request")
public class AuditEpidemicLogEchartAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8573218025168165582L;
    @Autowired
    private IAuditEpidemicLogService service;


    public void pageLoad() {
    }



    

    @SuppressWarnings("static-access")
    public JSONObject getEchartsData() {
        Date tomorrow=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(tomorrow);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        tomorrow=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowString = formatter.format(tomorrow);


        String[] arr = getBeforeSevenDay();
        List<String> resultList = new ArrayList<>(arr.length);
        for (String s : arr) {
            resultList.add(s);
        }
        int count = 0;
        JSONObject Jsondata = new JSONObject();
        JSONArray handletypeJson = new JSONArray();

        for (int i = 0; i < resultList.size(); i++) {
            JSONObject handleJson = new JSONObject();
            handleJson.put("name", resultList.get(i));
            SqlConditionUtil sql = new SqlConditionUtil();
            if (i!=resultList.size()-1) {
                sql.ge("entrytime", resultList.get(i));
                sql.lt("entrytime", resultList.get(i+1)); 
            }            
            else {
                sql.gt("entrytime", resultList.get(i));
                sql.lt("entrytime", tomorrowString); 
            }
            count = service.getAuditEpidemicLogPageData(sql.getMap(), 0, 0, "", "").getResult().getRowCount();
            handleJson.put("value", count);
            handletypeJson.add(handleJson);
            Jsondata.put("handletype", handletypeJson);
        }

        return Jsondata;

    }

    

    public static String[] getBeforeSevenDay() {
        String[] arr = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = null;
        for (int i = 0; i < 7; i++) {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -i );
            arr[6 - i] = sdf.format(c.getTime());

        }
        return arr;
    }

}
