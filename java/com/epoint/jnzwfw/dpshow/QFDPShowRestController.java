package com.epoint.jnzwfw.dpshow;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

@RestController
@RequestMapping("/qfdpshow")
public class QFDPShowRestController
{
    /* select count(OUGUID) as sum1,
    (select OUSHORTNAME from frame_ou where frame_ou.OUGUID=audit_project.OUGUID) as OUNAME1
     from audit_project where status>=26 and TO_DAYS(operatedate) - TO_DAYS('2018-07-01')>=0 group by OUNAME1 order by sum1 desc*/

    @RequestMapping(value = "/getQNOSum", method = RequestMethod.POST)
    public String getQNOSum(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String macaddress = obj.getString("macaddress");
            int qnosum = getqnosum();
            int qnosumnew = getqnosumnew();
            dataJson.put("qnosumnew", qnosumnew);
            dataJson.put("qnosum", qnosum);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getmyd", method = RequestMethod.POST)
    public String getmyd(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            int myd0 = getmyd("0");
            int myd = getmyd("1");
            int myd6 = getmyd("4");
            int myd5 = getmyd("5");
           
            int myd1 = myd0+myd;
            int myd2 = getmyd("3");
            int myd3 = getmyd("2");
            int myd4 = myd5+myd6;
            int all=myd1+myd2+myd3+myd4;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(1);
            String zhmyd = numberFormat.format((float) (all-myd4) / (float) (all) * 100);

            dataJson.put("myd1", myd1);
            dataJson.put("myd2", myd2);
            dataJson.put("myd3", myd3);
            dataJson.put("myd4", myd4);
            dataJson.put("zhmyd", zhmyd + "%");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    public int getqnosum2() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from  dia";
        String sql2 = "select RowGuid from audit_queue";
        int sum = dao.findList(sql, Record.class).size();
        sum = sum + dao.findList(sql2, Record.class).size();
        dao.close();
        return sum;
    }

    @RequestMapping(value = "/getage", method = RequestMethod.POST)
    public String getage(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String macaddress = obj.getString("macaddress");
            String sql1 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>=18 and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))<=30";
            int age1 = getage(sql1);
            String sql2 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>70";
            int age2 = getage(sql2);
            String sql3 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>=61 and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))<=70";
            int age3 = getage(sql3);
            String sql4 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>=51 and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))<=60";
            int age4 = getage(sql4);
            String sql5 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>=41 and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))<=50";
            int age5 = getage(sql5);
            String sql6 = "SELECT (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4)) AS age from audit_queue_history where IDENTITYCARDNUM!='' and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))>=31 and (DATE_FORMAT(NOW(), '%Y') - SUBSTRING(IDENTITYCARDNUM,7,4))<=40";
            int age6 = getage(sql6);

            String sql7 = "SELECT RowGuid from audit_queue_history where IDENTITYCARDNUM!='' AND IF (MOD(SUBSTRING(IDENTITYCARDNUM,17,1),2),'男','女')='男'";
            int sex1 = getage(sql7);
            String sql8 = "SELECT RowGuid from audit_queue_history where IDENTITYCARDNUM!='' AND IF (MOD(SUBSTRING(IDENTITYCARDNUM,17,1),2),'男','女')='女'";
            int sex2 = getage(sql8);
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(1);
            String sex11 = numberFormat.format((float) sex1 / (float) (sex1 + sex2) * 100);
            String sex22 = numberFormat.format((float) sex2 / (float) (sex1 + sex2) * 100);
            int max = age1;
            max = max > age2 ? max : age2;
            max = max > age3 ? max : age3;
            max = max > age4 ? max : age4;
            max = max > age5 ? max : age5;
            max = max > age6 ? max : age6;

            dataJson.put("age1", age1);
            dataJson.put("age2", age2);
            dataJson.put("age3", age3);
            dataJson.put("age4", age4);
            dataJson.put("age5", age5);
            dataJson.put("age6", age6);
            dataJson.put("max", max + 100);
            dataJson.put("sex1", sex11);
            dataJson.put("sex2", sex22);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getzfx", method = RequestMethod.POST)
    public String getzfx(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String taskname = getztaskname();
            String WINDOWNO = getzwindowname();
            String JD = getzjd();
            String mont = getzmonth();
            String day = getzwddkday();
            String hour = getzhour();
            dataJson.put("taskname", taskname);
            dataJson.put("windowname", WINDOWNO + "窗口");
            dataJson.put("JD", JD);
            dataJson.put("month", mont + "月份");
            dataJson.put("day", day);
            dataJson.put("hour", hour);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/gettasktype", method = RequestMethod.POST)
    public String gettasktype(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();

            int xk = gettasksum("01");
            int fw = gettasksum("11");
            int qr = gettasksum("07");
            int jl = gettasksum("06");
            int jf = gettasksum("05");
            int qt = gettasksum("10");

            int all = xk + fw + qr + jl + jf + qt;
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(1);

            dataJson.put("xk", numberFormat.format((float) xk / (float) all * 100));
            dataJson.put("fw", numberFormat.format((float) fw / (float) all * 100));
            dataJson.put("qr", numberFormat.format((float) qr / (float) all * 100));
            dataJson.put("jl", numberFormat.format((float) jl / (float) all * 100));
            dataJson.put("jf", numberFormat.format((float) jf / (float) all * 100));
            dataJson.put("qt", numberFormat.format((float) qt / (float) all * 100));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getouqno", method = RequestMethod.POST)
    public String getouqno(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();

            List<JSONObject> dataJsonList = new ArrayList<JSONObject>();

            List<Record> oulist= getounamebyqno();
            for(Record record : oulist){
                 JSONObject dataJson1 = new JSONObject();
            	 dataJson1.put("ouname", record.getStr("OUSHORTNAME"));
            	 String ouguid=record.getStr("ouguid");
            	 dataJson1.put("ouguid", ouguid);
                 int sw = getqnobyou1(ouguid);
                 int sw2 = getqnobyou(ouguid);
                 dataJson1.put("sw", sw);
                 dataJson1.put("sw2", sw2);
                 dataJsonList.add(dataJson1);
             }
            
            dataJson.put("dataJsonList", dataJsonList);

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    public int getqnobywindow1(String windowname) {
        CommonDao dao = CommonDao.getInstance();
        String sql2 = "select RowGuid from audit_queue where HANDLEWINDOWGUID in (select RowGuid from audit_orga_window where WINDOWNAME like '%"
                + windowname + "%')";
        int all = dao.findList(sql2, Record.class).size();
        dao.close();
        return all;
    }

    public int getqnobywindow(String windowname) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from audit_queue_history where HANDLEWINDOWGUID in (select RowGuid from audit_orga_window where WINDOWNAME like '%"
                + windowname + "%')";
        String sql2 = "select RowGuid from audit_queue where HANDLEWINDOWGUID in (select RowGuid from audit_orga_window where WINDOWNAME like '%"
                + windowname + "%')";
        int all = dao.findList(sql, Record.class).size();
        all = all + dao.findList(sql2, Record.class).size();
        dao.close();
        return all;
    }

    public int getqnobyou(String ouguid) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select rowguid from audit_queue_history where HANDLEWINDOWGUID in (select rowguid from audit_orga_window where OUGUID='"
                + ouguid + "')";
        String sql2 = "select rowguid from audit_queue where HANDLEWINDOWGUID in (select rowguid from audit_orga_window where OUGUID='"
                + ouguid + "')";
        int all = dao.findList(sql, Record.class).size();
        all = all + dao.findList(sql2, Record.class).size();
        dao.close();
        return all;
    }

    public int getqnobyou1(String ouguid) {
        CommonDao dao = CommonDao.getInstance();
        String sql2 = "select rowguid from audit_queue where HANDLEWINDOWGUID in (select rowguid from audit_orga_window where OUGUID='"
                + ouguid + "')";
        int all = dao.findList(sql2, Record.class).size();
        dao.close();
        return all;
    }

    public int gettasksum(String shenpilb) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from audit_task where IFNULL(IS_HISTORY,'0')='0' and is_editafterimport=1 and SHENPILB='"
                + shenpilb + "' ";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();
        return list.size();
    }

    public String getzhour() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT HOUR(operatedate) as HOU,count(RowGuid) as sum11 FROM audit_queue_history group by HOU order by sum11 desc  LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {
            String HOU = list.get(0).getStr("HOU");
            String h = "";
            switch (HOU) {
                case "8":
                    h = "8:00~9:00";
                    break;
                case "9":
                    h = "9:00~10:00";
                    break;
                case "10":
                    h = "10:00~11:00";
                    break;
                case "11":
                    h = "11:00~12:00";
                    break;
                case "12":
                    h = "12:00~13:00";
                    break;
                case "13":
                    h = "13:00~14:00";
                    break;
                case "14":
                    h = "14:00~15:00";
                    break;
                case "15":
                    h = "15:00~16:00";
                    break;
                case "16":
                    h = "16:00~17:00";
                    break;
                case "17":
                    h = "17:00~18:00";
                    break;
                case "18":
                    h = "18:00~19:00";
                    break;
            }
            return h;
        }
        else {
            return "";
        }
    }

    public String getzwddkday() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT DAYOFWEEK(operatedate) as WEEKD,count(RowGuid) as sum11 FROM audit_queue_history group by WEEKD order by sum11 desc  LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {
            String WEEKD = list.get(0).getStr("WEEKD");
            String ww = "";
            switch (WEEKD) {
                case "1":
                    ww = "星期日";
                    break;
                case "2":
                    ww = "星期一";
                    break;
                case "3":
                    ww = "星期二";
                    break;
                case "4":
                    ww = "星期三";
                    break;
                case "5":
                    ww = "星期四";
                    break;
                case "6":
                    ww = "星期五";
                    break;
                case "7":
                    ww = "星期六";
                    break;
            }
            return ww;
        }
        else {
            return "";
        }
    }

    public String getzmonth() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT month(operatedate) as mon,count(RowGuid) as sum11 FROM audit_queue_history group by mon order by sum11 desc  LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {
            String mon = list.get(0).getStr("mon");
            return mon;
        }
        else {
            return "";
        }
    }

    public String getzjd() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT quarter(operatedate) as quar,count(RowGuid) as sum11 FROM audit_queue_history group by quar order by sum11 desc  LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {

            String quar = list.get(0).getStr("quar");
            String jd = "";
            if ("1".equals(quar)) {
                jd = "第一季度";
            }
            if ("2".equals(quar)) {
                jd = "第二季度";
            }
            if ("3".equals(quar)) {
                jd = "第三季度";
            }
            if ("4".equals(quar)) {
                jd = "第四季度";
            }
            return jd;
        }
        else {
            return "";
        }
    }

    public String getzwindowname() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select count(RowGuid) as sum1,HANDLEWINDOWGUID, (select WINDOWNO from audit_orga_window where audit_orga_window.RowGuid=audit_queue_history.HANDLEWINDOWGUID) as WINDOWNO from audit_queue_history where HANDLEWINDOWGUID!='' group by HANDLEWINDOWGUID order by sum1 DESC LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {
            String taskname = list.get(0).getStr("WINDOWNO");
            return taskname;
        }
        else {
            return "";
        }
    }

    public String getztaskname() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT  count(1) as summ,(select tasktypename from audit_queue_tasktype where audit_queue_tasktype.rowguid=audit_queue_history.TASKGUID) as taskname from audit_queue_history  where TASKGUID!='' and EXISTS (select 'x' from audit_queue_tasktype where audit_queue_tasktype.rowguid=audit_queue_history.TASKGUID) group by TASKGUID order by summ desc LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();

        if (list.size() > 0) {
            String taskname = list.get(0).getStr("taskname");
            return taskname;
        }
        else {
            return "";
        }
    }

    public int getage(String sql) {
        CommonDao dao = CommonDao.getInstance();
        int sum = dao.findList(sql, Record.class).size();
        dao.close();
        return sum;
    }

    public int getqnosumnew() {
        CommonDao dao = CommonDao.getInstance();
        String sql2 = "select RowGuid from audit_queue";
        int sum = dao.findList(sql2, Record.class).size();
        dao.close();
        return sum;
    }

    public int getqnosum() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from audit_queue_history";
        String sql2 = "select RowGuid from audit_queue";
        int sum = dao.findList(sql, Record.class).size();
        sum = sum + dao.findList(sql2, Record.class).size();
        dao.close();
        return sum;
    }

    public int getmyd(String status) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from audit_online_evaluat where satisfied='" + status + "'";
        int sum = dao.findList(sql, Record.class).size();
        dao.close();
        return sum;
    }
    public List<Record> getounamebyqno(){
    	 CommonDao dao = CommonDao.getInstance();
         String sql = "SELECT b.OUGUID,c.OUSHORTNAME,COUNT(1) AS num FROM audit_queue_history a LEFT JOIN audit_orga_window b ON a.HANDLEWINDOWGUID = b.RowGuid LEFT JOIN frame_ou c ON c.OUGUID=b.OUGUID WHERE a.HANDLEWINDOWGUID <> '' and c.OUGUID <> '4e398b61-0dd0-476f-83cd-f653bea67425' GROUP BY b.OUGUID ORDER BY num DESC LIMIT 10";
         List<Record> list = dao.findList(sql, Record.class);
         dao.close();
         return list;
    }
    
    
}
