package com.epoint.tongbufw.dingtalk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.esotericsoftware.minlog.Log;

public class DingTalkService
{
    public static String getAccessToken(String url, String corpid, String secret) {
        String requestUrl = url + "?corpid=" + corpid + "&corpsecret=" + secret;
        String result = HttpUtils.doGet(requestUrl);
        String accessToken = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject = JSON.parseObject(result);
        String msg = (String) jsonObject.get("errmsg");
        System.out.println("=========>>>返回错误信息"+msg);
        if ("ok".equals(msg)) {
            accessToken = (String) jsonObject.get("access_token");
        }
        return accessToken;
    }

    //获取考勤记录
    public static void getCardList(String accessToken, String workDateFrom, String workDateTo, String offset,
            String limit,String userid) {
        String recordUrl = "https://oapi.dingtalk.com/attendance/list?access_token=" + accessToken;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workDateFrom", workDateFrom);
        jsonObject.put("workDateTo", workDateTo);
        jsonObject.put("userIdList", Arrays.asList(userid));
        jsonObject.put("offset", offset);
        jsonObject.put("limit", limit);
        String result = HttpUtils.doPost(recordUrl, jsonObject, "utf-8");
        JSONObject resutJSON = JSONObject.parseObject(result);
        String msg = (String) resutJSON.get("errmsg");
        System.out.println("========>>>https://oapi.dingtalk.com/attendance/list======"+msg);
       /* String hasMore = "";
        hasMore = resutJSON.get("hasMore").toString();*/
        List<Record> list = new ArrayList<Record>();
       
        if ("ok".equals(msg)) {
            list = JSONObject.parseArray(resutJSON.get("recordresult").toString(), Record.class);
            //保存考勤记录
            saveDingList(list, accessToken);
        }
        //return hasMore;
    }

    private static void saveDingList(List<Record> lists, String accessToken) {
        //保存钉钉数据       
        for (Record list : lists) {
            String userCheckTime = list.getStr("userCheckTime");
            String userId = list.getStr("userId");
            String rowId = list.getStr("id");
            String checkType = list.getStr("checkType");
            String sourceType = list.getStr("sourceType");
            String timeResult = list.getStr("timeResult");
            getDingUser(accessToken, userId, userCheckTime, rowId, checkType, sourceType, timeResult);
        }
    }

    //根据考勤记录的userid获取人员信息并保存
    public static void getDingUser(String accessToken, String userId, String checktime, String rowId, String checkType,
            String sourceType, String timeResult) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//需要的时间的格式
        String userCheckTime = sdf.format(new Date(Long.parseLong(checktime)));
        String url = "https://oapi.dingtalk.com/user/get";
        String requestUrl = url + "?access_token=" + accessToken + "&userid=" + userId;
        String result = HttpUtils.doGet(requestUrl);

        JSONObject jsonObject = new JSONObject();
        jsonObject = JSON.parseObject(result);
        String msg = "";
        try {
            msg = (String) jsonObject.get("errmsg");
            System.out.println("=====>>>"+msg);
        }
        catch (Exception e) {
        }
        finally {
        }
        if ("ok".equals(msg)) {
            String name = "";
            String jobnumber = "";
            String mobile = "";
            String department = "";
            try {
                name = jsonObject.get("name").toString();
                jobnumber = jsonObject.get("jobnumber").toString();
                mobile = jsonObject.get("mobile").toString();
                department = jsonObject.get("department").toString();
                department = department.replace("[", "");
                department = department.replace("]", "");
            }
            catch (Exception e) {
            }
            finally {
            }
            /*            System.out.println("userId=========" + userId);
            System.out.println("userCheckTime=========" + userCheckTime);
            System.out.println("rowId=========" + rowId);
            System.out.println("checkType=========" + checkType);
            System.out.println("sourceType=========" + sourceType);
            System.out.println("timeResult=========" + timeResult);
            System.out.println("name=========" + name);
            System.out.println("jobnumber=========" + jobnumber);
            System.out.println("mobile=========" + mobile);
            System.out.println("department=========" + department);
            */
            CommonDao dao = CommonDao.getInstance();
            try {
                String sqlcheck = "select RowGuid from  KaoQinRecord where rowId='" + rowId + "'";
                List<Record> list = dao.findList(sqlcheck, Record.class);
                if (list.size() < 1) {
                    String sqlinser = "insert into KaoQinRecord(OperateDate,RowGuid,userId,userCheckTime,rowId,checkType,sourceType,timeResult,name,jobnumber,mobile,department) VALUES(NOW(),NEWID(),'"
                            + userId + "','" + userCheckTime + "','" + rowId + "','" + checkType + "','" + sourceType
                            + "','" + timeResult + "','" + name + "','" + jobnumber + "','" + mobile + "','"
                            + department + "')";
                    dao.executeSqlCommand(sqlinser);
                }
            }
            catch (Exception e) {
            }
            finally {
                dao.close();
            }

        }
    }
}
