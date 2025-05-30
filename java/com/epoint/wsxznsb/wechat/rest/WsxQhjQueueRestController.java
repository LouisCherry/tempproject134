package com.epoint.wsxznsb.wechat.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.wsxznsb.auditqueue.inter.WsxIAuditQueueTasktype;

@RestController
@RequestMapping("/wsxqhjqueue")
public class WsxQhjQueueRestController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private WsxIAuditQueueTasktype tasktypeservice;

    @Autowired
    private IHandleQueue handlequeueservice;
    @Autowired
    private IOuService ouservice;

    /**
     * 获取取号部门列表
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getOUList", method = RequestMethod.POST)
    public String getOUList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            FrameOu ou = null;
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject ouJson = new JSONObject();

            List<String> ouguidlist = windowservice.getOUbyHall(centerguid, hallguid).getResult();

            for (String ouguid : ouguidlist) {
                ouJson = new JSONObject();
                ouJson.put("ouguid", ouguid);
                ouJson.put("ougnum",
                        StringUtil.isNotBlank(ouservice.getOuByOuGuid(ouguid).getOrderNumber())
                                ? ouservice.getOuByOuGuid(ouguid).getOrderNumber()
                                : 0);
                ou = ouservice.getOuByOuGuid(ouguid);
                if (StringUtil.isNotBlank(ou)) {
                    ouJson.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                }
                else {
                    ouJson.put("ouname", "");
                }
                list.add(ouJson);
            }
            // 根据ougnum对部门数据进行降序排序
            Collections.sort(list,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("ougnum") - l1.getIntValue("ougnum"));
            // 截取对应页面的部门list数据
            int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
            int endint = (firstint + Integer.parseInt(pageSize)) >= list.size() ? list.size()
                    : (firstint + Integer.parseInt(pageSize));
            List<JSONObject> rtnlist = list.subList(firstint, endint);

            int totalcount = list.size();

            dataJson.put("oulist", rtnlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("异常信息：" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项类别列表
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskTypeList", method = RequestMethod.POST)
    public String getTaskTypeList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String largetasktypeguid = obj.getString("largetasktypeguid");
            String hallguid = obj.getString("hallguid");
            String ouguid = obj.getString("ouguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String tasktypename = obj.getString("tasktypename");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject tasktypeJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            if (StringUtil.isNotBlank(tasktypename)) {
                sql.like("tasktypename", tasktypename);
            }
            if (StringUtil.isNotBlank(largetasktypeguid)) {
                // 大事项类别
                sql.eq("largetasktypeguid", largetasktypeguid);
            }
            else if (StringUtil.isNotBlank(ouguid)) {
                // 根据ouguid查询
                sql.eq("ouguid", ouguid);
            }
            else {
                // 获取该设备绑定的所有大厅下的事项
                if (!"all".equals(hallguid)) {
                    String[] hallguids = hallguid.split(",");
                    List<String> oulist = new ArrayList<String>();
                    for (String eachhallguid : hallguids) {
                        oulist.addAll(windowservice.getOUbyHall(centerguid, eachhallguid).getResult());
                    }

                    StringBuilder ous = new StringBuilder();
                    ous.append("'");
                    for (String ou : oulist) {
                        ous.append(ou + "','");
                    }
                    ous.append("'");
                    sql.in("ouguid", ous.toString());
                }
            }
            PageData<AuditQueueTasktype> tasktypepagedata = tasktypeservice
                    .getAuditQueueTasktypeByPage(" rowguid,tasktypename,is_face ", sql.getMap(),
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            "ordernum", "desc")
                    .getResult();

            int totalcount = tasktypepagedata.getRowCount();
            for (AuditQueueTasktype tasktype : tasktypepagedata.getList()) {
                tasktypeJson = new JSONObject();
                tasktypeJson.put("tasktypeguid", tasktype.getRowguid());
                tasktypeJson.put("tasktypename", tasktype.getTasktypename());
                tasktypeJson.put("isface", tasktype.getIs_face());
                tasktypeJson.put("taskwaitnum", StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult()));
                list.add(tasktypeJson);
            }

            dataJson.put("tasktypelist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            log.info("异常信息：" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
