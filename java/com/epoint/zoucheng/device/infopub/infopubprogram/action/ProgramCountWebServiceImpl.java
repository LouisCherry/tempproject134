package com.epoint.zoucheng.device.infopub.infopubprogram.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.dao.CommonDao;

@WebService(endpointInterface = "com.epoint.device.infopub.infopubprogram.action.ProgramCountWebService", serviceName = "ProgramCountWebService")
public class ProgramCountWebServiceImpl implements ProgramCountWebService
{
    private CommonDao commondao = CommonDao.getInstance();

    //方法重写
    public String getCountInfo() {
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(new Date());
        String todaySql = "select count(1) from programstatistic where datetime like '%" + sdf.format(new Date())
                + "%' and programform = ?";
        String allSql = "select count(1) from programstatistic where programform = ?";
        JSONObject imagejson = new JSONObject();
        imagejson.put("type", "图片");
        imagejson.put("time", sdf.format(new Date()));
        imagejson.put("todaycount", commondao.queryInt(todaySql, "1"));
        imagejson.put("Allcount", commondao.queryInt(allSql, "1"));
        infolist.add(imagejson);
        JSONObject videojson = new JSONObject();
        videojson.put("type", "视频");
        videojson.put("time", sdf.format(new Date()));
        videojson.put("todaycount", commondao.queryInt(todaySql, "2"));
        videojson.put("Allcount", commondao.queryInt(allSql, "2"));
        infolist.add(videojson);
        JSONObject textjson = new JSONObject();
        textjson.put("type", "文本");
        textjson.put("time", sdf.format(new Date()));
        textjson.put("todaycount", commondao.queryInt(todaySql, "3"));
        textjson.put("Allcount", commondao.queryInt(allSql, "3"));
        infolist.add(textjson);
        return JSON.toJSONString(infolist);
    }
}
