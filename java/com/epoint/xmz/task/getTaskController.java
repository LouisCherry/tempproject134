
package com.epoint.xmz.task;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping("/task")
public class getTaskController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 获取办件结果
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskByItemId", method = RequestMethod.POST)
    public String getTaskByItemId(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskByItemId接口=======" + System.currentTimeMillis());
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getTaskByItemId入参：" + jsonObject);
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            //拿到item_id
            JSONObject obj = JSONObject.parseObject(jsonObject.getString("params"));
            String itemId = obj.getString("item_id");
            //查询事项taskis和taskguid
            SqlConditionUtil sql =  new SqlConditionUtil();
            //根据itemid查询在用的事项
            sql.setSelectFields("rowguid,task_id");
            sql.eq("item_id", itemId);
            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
            sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
            sql.eq("is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
            sql.setOrderDesc("OperateDate");
            List<AuditTask> auditTaskList = iAuditTask.getAllTask(sql.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(auditTaskList)) {
                dataJson.put("taskguid", auditTaskList.get(0).getRowguid());
                dataJson.put("taskid", auditTaskList.get(0).getTask_id());
                return JsonUtils.zwdtRestReturn("1", "获取事项信息成功！", dataJson.toString());
            }
            else{
                return JsonUtils.zwdtRestReturn("1", "未查询到事项！", dataJson.toString());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskByItemId接口参数：params【" + params + "】=======");
            log.info("=======getTaskByItemId异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项信息失败：" + e.getMessage(), "");
        }
        finally {
            log.info("=======结束调用getTaskByItemId接口=======" +  System.currentTimeMillis());
        }
    }

}
