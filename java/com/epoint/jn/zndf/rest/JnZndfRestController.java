package com.epoint.jn.zndf.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jn.zndf.api.IJnzndf;

@RestController
@RequestMapping("/jnzndf")
public class JnZndfRestController
{
    @Autowired
    private IJnzndf iJnzndf;

    @RequestMapping(value = "/gettasknamelist", method = RequestMethod.POST)
    public String getTasknameList(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String pos = obj.getString("pos");
            List<Record> list = iJnzndf.getTaskNameByPos(pos).getResult();
            List<JSONObject> objlist = new ArrayList<JSONObject>();
            if (StringUtil.isNotBlank(list)) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("taskname", list.get(i).get("TaskName"));
                    dataJson.put("taskguid", list.get(i).get("taskguid"));
                    objlist.add(dataJson);
                }
            }
            JSONObject data = new JSONObject();
            data.put("info", objlist);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", data);

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用失败", "");
        }
    }

    @RequestMapping(value = "/getnumbymac", method = RequestMethod.POST)
    public String getNumByMac(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String num = iJnzndf.getNumByMac(macaddress).getResult();
            JSONObject dataJson = new JSONObject();
            dataJson.put("num", num);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用失败", "");
        }
    }

    @RequestMapping(value = "/getwindownamebylobby", method = RequestMethod.POST)
    public String getWindowNameByLobby(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String lobbyType = obj.getString("lobbytype");
            List<JSONObject> objlist = new ArrayList<JSONObject>();
            List<Record> list = iJnzndf.getWindowNameByLobby(lobbyType).getResult();
            JSONObject dataJson = new JSONObject();;
            if (list != null && !list.isEmpty()) {
                for(int i=0;i<list.size();i++){
                    if(StringUtil.isNotBlank(list.get(i).get("portno"))){
                        dataJson = new JSONObject();
                        dataJson.put("windowno", list.get(i).get("windowno"));
                        dataJson.put("windowname", list.get(i).get("indicating"));
                        dataJson.put("portno", list.get(i).get("portno"));
                        objlist.add(dataJson);
                    }
                }               
            }
            JSONObject data = new JSONObject();
            data.put("info", objlist);           
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", data);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口调用失败", "");
        }
    }

}
