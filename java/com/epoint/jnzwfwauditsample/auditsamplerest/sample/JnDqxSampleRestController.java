package com.epoint.jnzwfwauditsample.auditsamplerest.sample;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@RestController
@RequestMapping("/jndqxsample")
public class JnDqxSampleRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IJnDqxAuditZnsbCentertask centertaskservice;

    /**
     * 获取样单事项
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSampleTaskList", method = RequestMethod.POST)
    public String getSampleTaskList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String ouguid = obj.getString("ouguid");
            String taskname = obj.getString("taskname");

            String centerguid = "";
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject taskJson = new JSONObject();
            PageData<Record> taskpagedata = null;
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {

                    // 常用事项
                    if (("common".equals(ouguid))) {
                        taskpagedata = centertaskservice.getCommonSampleTaskPageData(centerguid, taskname,
                                Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize))
                                .getResult();

                    }
                    else {
                        taskpagedata = centertaskservice.getSampleTaskPageData(centerguid, ouguid, taskname,
                                Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize))
                                .getResult();
                    }
                    
                    int totalcount = taskpagedata.getRowCount();
                    for (Record task : taskpagedata.getList()) {
                        taskJson = new JSONObject();
                        
                        taskJson.put("taskid", task.get("task_id"));
                        taskJson.put("taskguid", task.get("taskguid"));
                        taskJson.put("taskname", task.get("taskname"));

                        list.add(taskJson);
                    }

                    dataJson.put("tasklist", list);
                    dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
