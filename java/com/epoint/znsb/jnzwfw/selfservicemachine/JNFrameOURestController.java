package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditselfservice.auditselfservicerest.common.PrintRestController;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.zoucheng.znsb.auditznsbcentertask.inter.IZCAuditZnsbCentertask;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("qcwbselfserviceou")
public class JNFrameOURestController
{

    @Autowired
    private IAuditZnsbCentertask centertaskService;
    @Autowired
    private IOuService ouservice;

    transient Logger log = LogUtil.getLog(JNFrameOURestController.class);

    /**
     * 获取事项部门
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getTaskOUList", method = RequestMethod.POST)
    public String getTaskOUList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String applyertype = obj.getString("applyertype");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject ouJson = new JSONObject();
            List<String> ouguidlist = new ArrayList<String>();
//            log.info("======qcwbselfserviceou参数:========" + params);
            ouguidlist = centertaskService.getTaskOUList(centerguid, applyertype).getResult();

            FrameOu frameou;
            SqlConditionUtil tasksql = new SqlConditionUtil();
            tasksql.eq("centerguid", centerguid);
            tasksql.eq("is_enable", QueueConstant.CONSTANT_STR_ONE);
            
            for (String ouguid : ouguidlist) {

                frameou = ouservice.getOuByOuGuid(ouguid);
                if (StringUtil.isNotBlank(frameou)) {
                    ouJson = new JSONObject();
                    ouJson.put("ouguid", ouguid);
                    ouJson.put("ougnum",
                            StringUtil.isNotBlank(frameou.getOrderNumber()) ? frameou.getOrderNumber() : 0);
                    ouJson.put("ouname", StringUtil.isNotBlank(frameou.getOushortName()) ? frameou.getOushortName()
                            : frameou.getOuname());
                    if (StringUtil.isNotBlank(applyertype)) {
                        tasksql.like("applyertype", applyertype);
                    }
                    if (StringUtil.isNotBlank(ouguid)) {
                        tasksql.eq("ouguid", ouguid);
                    }
                    tasksql.eq("isqcwb","1");
                    List<AuditZnsbCentertask> result = centertaskService.getCenterTaskList(tasksql.getMap()).getResult();
                    if(result!=null&&!result.isEmpty()){
                        list.add(ouJson);
                    }
                }
            }
            //根据ougnum对部门数据进行降序排序
            Collections.sort(list,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("ougnum") - l1.getIntValue("ougnum"));
            //截取对应页面的部门list数据
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
            log.info("======qcwbselfserviceou异常:========" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
