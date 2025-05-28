package com.epoint.power.rest;


import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.power.util.PowerDataUtil;
import com.epoint.xmz.auditelectricdata.api.IAuditElectricDataService;
import com.epoint.xmz.auditelectricdata.api.entity.AuditElectricData;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RequestMapping("power")
@RestController
public class PowerDataDockingRest {

    private static final Logger logger = Logger.getLogger(PowerDataDockingRest.class);

    @Autowired
    private IAuditElectricDataService electricDataService;

    @Autowired
    private IHandleFlowSn handleFlowSn;

    @Autowired
    private IAuditOrgaServiceCenter serviceCenter;

    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private IAuditTask auditTaskService;

    @RequestMapping("projectRegister")
    public String projectRegister(@RequestBody String params) {
        try {
            logger.info("*************开始调用办件登记接口*************");
            JSONObject requestJson = PowerDataUtil.dealPowerRequestParam(params);

            JSONObject outerItem = requestJson.getJSONObject("item");

            JSONObject middleItem = outerItem.getJSONObject("DATALIST").getJSONObject("item");

            JSONObject innerItem = middleItem.getJSONObject("TEXT").getJSONObject("item");


            String itemId = innerItem.getString("SPSXBH");
            String flowSn = "";
            String numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
            AuditTask task = electricDataService.getTaskByItemId(itemId);
            if (task != null) {
                AuditOrgaServiceCenter center = serviceCenter.getAuditServiceCenterByBelongXiaqu(task.getAreacode());
                if (center != null) {
                    numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE, center.getRowguid()).getResult();
                }
            }
            flowSn = handleFlowSn.getFlowsn("办件编号", numberFlag).getResult();
            AuditElectricData electricData = new AuditElectricData();
            electricData.setRowguid(UUID.randomUUID().toString());
            electricData.setOperatedate(new Date());
            electricData.setUpdatetime(new Date());
            electricData.setFlowsn(flowSn);
            electricData.set("dlFLowSn",innerItem.getString("YWLSH"));
            electricData.setParams(params);
            electricData.setType("登记");

            String responseXml = buildXml("1", "业务处理成功，返回值{{LSH=" + innerItem.getString("YWLSH") + "}}", "");
            String result = buildResponse("调用成功", true, responseXml, "1");
            electricData.setResult(result);
            electricDataService.insert(electricData);
            return result;
        }
        catch (Exception e) {
            logger.info("*************调用办件登记接口出错*************");
            e.printStackTrace();
            return buildResponse("调用出错", false, "", "1");
        }
    }

    /**
     * 校验必填项
     *
     * @param requiredFields 必填字段
     * @param info           入参json
     * @return 返回缺少的字段，参数为空时返回“fail”
     */
    private String dataRequiredCheck(String requiredFields, JSONObject info) {
        if (info == null) {
            return requiredFields;
        }
        StringBuilder missField = new StringBuilder();
        String[] split = requiredFields.split(",");
        for (String field : split) {
            if (StringUtil.isBlank(info.getString(field))) {
                missField.append(field).append(",");
            }
        }
        return missField.toString();
    }

    /**
     * 构建返回结果
     *
     * @param msg     消息
     * @param status  状态
     * @param data    xml数据
     * @param errCode 错误代码
     * @return 结果json
     */
    private String buildResponse(String msg, boolean status, String data, String errCode) {
        JSONObject rtnJson = new JSONObject();
        rtnJson.put("msg", msg);
        rtnJson.put("SUCCESS", status);
        rtnJson.put("data", data);
        rtnJson.put("errCode", errCode);

        return rtnJson.toJSONString();
    }

    /**
     * 构建xml返回结果
     *
     * @param code   状态
     * @param msg    说明
     * @param params 参数
     * @return xml
     */
    private String buildXml(String code, String msg, String params) {
        Document document = DocumentHelper.createDocument();
        Element item = document.addElement("item");
        document.setRootElement(item);
        Element codeElement = item.addElement("CODE");
        codeElement.setText(code);
        Element msgElement = item.addElement("MSG");
        msgElement.setText(msg);
        Element paramElement = item.addElement("PARAMS");
        paramElement.setText(params);
        return item.asXML();
    }

}
