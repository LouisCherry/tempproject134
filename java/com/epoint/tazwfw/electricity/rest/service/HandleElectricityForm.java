package com.epoint.tazwfw.electricity.rest.service;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;

public class HandleElectricityForm
{
    transient Logger log = Logger.getLogger(HandleElectricityForm.class);
    /**
     * 电子表单service
     */
    private HandleElectricityFormService handleFormService;

    public HandleElectricityForm() {
        handleFormService = new HandleElectricityFormService();
    }

    /**
     * 处理表单数据
     * 
     * @param projectGuid
     * @param itemId
     * @param dataInfoObj
     * @throws Exception
     */
    public void handleForm(String ywGuid, String biGuid, String projectGuid, String itemId, JSONObject dataInfoObj,
            String flowId) throws Exception {
        // "1"、根据itemID查询对应的表单（事项表中查表单ID根据表单ID查询表）
        String tableName = "";
        // 根据itemid查询taskid
        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        String taskId = "";
        List<AuditTask> auditTaskList = iAuditTask.selectUsableTaskItemListByItemId(itemId, "").getResult();
        if (auditTaskList != null && auditTaskList.size() > 0) {
            taskId = auditTaskList.get(0).getTask_id();
        }
        if (StringUtil.isNotBlank(taskId)) {
            // 获取tableName
            tableName = handleFormService.getTableName(taskId);
            // tableName = "table20190327114845";
            if (StringUtil.isBlank(tableName)) {
                throw new Exception("==========未找到事项对应表单！==========taskId=" + taskId);
            }
        }
        else {
            throw new Exception("==========未找到相应事项！==========itemId=" + itemId);
        }
        // 2、解析dataInfoObj将数据保存到表单中
        switch (tableName) {

            /*****************************************泰安市******************************************/
            case ElectricityConstant.GHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.LDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.ZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.LSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.SLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;

            /*****************************************新泰市******************************************/
            //新泰市电子表单
            case ElectricityConstant.XTGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.XTLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.XTZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.XTLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.XTDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.XTSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;

            /*****************************************泰山区******************************************/
            //泰山区电子表单
            case ElectricityConstant.TSGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.TSLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.TSZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.TSLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.TSDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.TSSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;

            /*****************************************岱岳区******************************************/
            //岱岳区电子表单
            case ElectricityConstant.DYGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DYLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DYZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DYLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DYDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DYSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            /*****************************************宁阳县******************************************/
            //宁阳县电子表单
            case ElectricityConstant.NYGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.NYLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.NYZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.NYLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.NYDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.NYSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;

            /*****************************************东平县******************************************/
            //东平县电子表单
            case ElectricityConstant.DPGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DPLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DPZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DPLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DPDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.DPSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;

            /*****************************************肥城市******************************************/
            //肥城市电子表单
            case ElectricityConstant.FCGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.FCLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.FCZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.FCLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.FCDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.FCSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            /*****************************************泰安高新区******************************************/
            //泰安高新区电子表单
            case ElectricityConstant.GXGHXKTABLE:
                // 2.2、处理规划许可申请内容
                handleGhxkInfo(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.GXLDZYTABLE:
                // 2.3、处理绿地占用申请表
                handleLdzyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.GXZLXKTABLE:
                // 2.4、占路许可申请表
                handleZlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.GXLSZYTABLE:
                // 2.5、临时占用城市道路许可申请表
                handleLszyApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.GXDLWJTABLE:
                // 2.6、挖掘城市道路许可申请表
                handleDlwjApply(projectGuid, dataInfoObj, flowId);
                break;
            case ElectricityConstant.GXSLXKTABLE:
                // 2.7、涉路许可申请表
                handleSlxkApply(projectGuid, dataInfoObj, flowId);
                break;
            default:
                break;
        }
    }

    /**
     * 处理基本信息
     * 
     * @param biGuid
     * @param dataInfoObj
     */
    public void handleBasic(String biGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理基本信息==========");
        String serviceObj = dataInfoObj.getString("dataInfoObj");
        String consName = dataInfoObj.getString("consName");
        String consNo = dataInfoObj.getString("consNo");
        String certType = dataInfoObj.getString("certType");
        String certNo = dataInfoObj.getString("certNo");

        log.info("==========flowId==========" + flowId);
        String serviceObj1 = "";
        String consName1 = "";
        String consNo1 = "";
        String certType1 = "";
        String certNo1 = "";
        String serviceObj2 = "";
        String consName2 = "";
        String consNo2 = "";
        String certType2 = "";
        String certNo2 = "";

        if ("1".equals(serviceObj)) {
            // 个人
            serviceObj1 = "用电个人";
            consName1 = consName;
            consNo1 = consNo;
            certType1 = certType;
            certNo1 = certNo;
        }
        else {
            // 企业
            serviceObj2 = "用电企业";
            consName2 = consName;
            consNo2 = consNo;
            certType2 = certType;
            certNo2 = certNo;
        }
        // 2.1.1、基本信息主表
        String prowGuid = biGuid;
        // 2.1.2、基本信息扩展信息(用电类别)
        JSONArray capList = dataInfoObj.getJSONArray("capList");
        JSONObject elecTypeObj1 = new JSONObject();
        JSONObject elecTypeObj2 = new JSONObject();
        String tCap1 = "";
        String orgnCap1 = "";
        String appCap1 = "";
        String tCap2 = "";
        String orgnCap2 = "";
        String appCap2 = "";
        if (capList != null && capList.size() > 0) {
            if (capList.size() == 1) {
                elecTypeObj1 = capList.getJSONObject(0);
            }
            else {
                elecTypeObj1 = capList.getJSONObject(0);
                elecTypeObj2 = capList.getJSONObject(1);
            }
            tCap1 = elecTypeObj1.getString("tCap");
            orgnCap1 = elecTypeObj1.getString("orgnCap");
            appCap1 = elecTypeObj1.getString("appCap");
            tCap2 = elecTypeObj2.getString("tCap");
            orgnCap2 = elecTypeObj2.getString("orgnCap");
            appCap2 = elecTypeObj2.getString("appCap");

            /*
             * for (int i = 0; i < capList.size(); i++) { String rowGuid =
             * UUID.randomUUID().toString(); JSONObject elecTypeObj =
             * capList.getJSONObject(i); String[] elecTypeValue = { rowGuid,
             * prowGuid, elecTypeObj.getString("tCap"),
             * elecTypeObj.getString("orgnCap"), elecTypeObj.getString("appCap")
             * }; // 插入用电类别数据
             * handleFormService.insertData(ElectricityConstant.ELECTYPETABLE,
             * ElectricityConstant.elecTypeColumn, elecTypeValue); }
             */
        }
        String mobile = dataInfoObj.getString("mobile");
        String corporateRep = dataInfoObj.getString("corporateRep");
        String corporateCer = dataInfoObj.getString("corporateCer");
        String corporateMobile = dataInfoObj.getString("corporateMobile");
        String agentName = dataInfoObj.getString("agentName");
        String agentCer = dataInfoObj.getString("agentCer");
        String agentMobile = dataInfoObj.getString("agentMobile");
        String elecAddr = dataInfoObj.getString("elecAddr");
        String busiTypeCode = dataInfoObj.getString("busiTypeCode");
        String elecType = dataInfoObj.getString("elecType");
        String tCapBf = dataInfoObj.getString("tCapBf");
        String vat = dataInfoObj.getString("vat");
        String appCapBf = dataInfoObj.getString("appCapBf");
        String handleName = dataInfoObj.getString("handleName");
        String appNo = dataInfoObj.getString("appNo");
        String acceptTime = dataInfoObj.getString("acceptTime");

        String[] basicInfoValue = {prowGuid, prowGuid, serviceObj1, serviceObj2, consName1, consNo1, mobile, certType1,
                certNo1, consName2, consNo2, certType2, certNo2, corporateRep, corporateCer, corporateMobile, agentName,
                agentCer, agentMobile, elecAddr, busiTypeCode, elecType, tCap1, orgnCap1, appCap1, tCap2, orgnCap2,
                appCap2, tCapBf, vat, appCapBf, handleName, appNo, acceptTime };

        String table = "";
        switch (flowId) {
            case "370900000000":
                table = ElectricityConstant.BASICTABLE;
                break;
            case "370982000000":
                table = ElectricityConstant.XTBASICTABLE;
                break;
            case "370902000000":
                table = ElectricityConstant.TSBASICTABLE;
                break;
            case "370911000000":
                table = ElectricityConstant.DYBASICTABLE;
                break;
            case "370921000000":
                table = ElectricityConstant.NYBASICTABLE;
                break;
            case "370923000000":
                table = ElectricityConstant.DPBASICTABLE;
                break;
            case "370983000000":
                table = ElectricityConstant.FCBASICTABLE;
                break;
            case "370990000000":
                table = ElectricityConstant.GXBASICTABLE;
                break;
            default:
                break;
        }

        // 插入基本信息主表数据

        handleFormService.insertData(table, ElectricityConstant.basicInfoColumn, basicInfoValue);

        /* handleFormService.insertData(ElectricityConstant.BASICTABLE, ElectricityConstant.basicInfoColumn,
                basicInfoValue);*/
    }

    /**
     * 处理规划许可申请内容
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleGhxkInfo(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理规划许可申请内容==========");

        log.info("==========flowId==========" + flowId);
        String tableXK = "";
        String tableLine = "";
        String tableBuil = "";
        switch (flowId) {
            case "370900000000":
                tableXK = ElectricityConstant.GHXKTABLE;
                tableLine = ElectricityConstant.PROLINETABLE;
                tableBuil = ElectricityConstant.PROBUILTABLE;
                break;
            case "370982000000":
                tableXK = ElectricityConstant.XTGHXKTABLE;
                tableLine = ElectricityConstant.XTPROLINETABLE;
                tableBuil = ElectricityConstant.XTPROBUILTABLE;
                break;
            case "370902000000":
                tableXK = ElectricityConstant.TSGHXKTABLE;
                tableLine = ElectricityConstant.TSPROLINETABLE;
                tableBuil = ElectricityConstant.TSPROBUILTABLE;
                break;
            case "370911000000":
                tableXK = ElectricityConstant.DYGHXKTABLE;
                tableLine = ElectricityConstant.DYPROLINETABLE;
                tableBuil = ElectricityConstant.DYPROBUILTABLE;
                break;
            case "370921000000":
                tableXK = ElectricityConstant.NYGHXKTABLE;
                tableLine = ElectricityConstant.NYPROLINETABLE;
                tableBuil = ElectricityConstant.NYPROBUILTABLE;
                break;
            case "370923000000":
                tableXK = ElectricityConstant.DPGHXKTABLE;
                tableLine = ElectricityConstant.DPPROLINETABLE;
                tableBuil = ElectricityConstant.DPPROBUILTABLE;
                break;
            case "370983000000":
                tableXK = ElectricityConstant.FCGHXKTABLE;
                tableLine = ElectricityConstant.FCPROLINETABLE;
                tableBuil = ElectricityConstant.FCPROBUILTABLE;
                break;
            case "370990000000":
                tableXK = ElectricityConstant.GXGHXKTABLE;
                tableLine = ElectricityConstant.GXPROLINETABLE;
                tableBuil = ElectricityConstant.GXPROBUILTABLE;
                break;
            default:
                break;
        }

        String prowGuid = projectGuid;
        String proRemark = dataInfoObj.getString("proRemark");
        String[] ghxkInfoValue = {prowGuid, prowGuid, dataInfoObj.getString("proOrg"), dataInfoObj.getString("proUsci"),
                dataInfoObj.getString("proEntName"), dataInfoObj.getString("proEntAddr"),
                dataInfoObj.getString("proLenth"), dataInfoObj.getString("proRa"), dataInfoObj.getString("proVol"),
                dataInfoObj.getString("proMat"), dataInfoObj.getString("proLineType"),
                dataInfoObj.getString("proConName"), dataInfoObj.getString("proConAddr"),
                dataInfoObj.getString("proConPer"), dataInfoObj.getString("proConMob"), proRemark };
        // 插入数据
        handleFormService.insertData(tableXK, ElectricityConstant.ghxkColumn, ghxkInfoValue);
        // 线路走径图及说明

        JSONArray proLineList = dataInfoObj.getJSONArray("proLineList");
        if (proLineList != null && proLineList.size() > 0) {
            for (int i = 0; i < proLineList.size(); i++) {
                String rowGuid = UUID.randomUUID().toString();
                JSONObject proLine = proLineList.getJSONObject(i);
                String[] proLineValue = {rowGuid, prowGuid, proLine.getString("proLineNo"),
                        proLine.getString("proLineName"), proLine.getString("proLineAddr"),
                        proLine.getString("proLineSt"), proLine.getString("proLineLen"), proLine.getString("proLineDe"),
                        proLine.getString("proLineRa"), proLine.getString("proLineTi"), proLine.getString("proLineHi"),
                        proLine.getString("proLineZ") };
                // 插入数据
                handleFormService.insertData(tableLine, ElectricityConstant.proLineColumn, proLineValue);
            }
        }

        JSONArray proBuilist = dataInfoObj.getJSONArray("proBuilist");
        if (proBuilist != null && proBuilist.size() > 0) {
            for (int i = 0; i < proBuilist.size(); i++) {
                String rowGuid = UUID.randomUUID().toString();
                JSONObject proBuil = proBuilist.getJSONObject(i);
                String[] proBuilValue = {rowGuid, prowGuid, proBuil.getString("proBuiNo"),
                        proBuil.getString("proBuiName"), proBuil.getString("proBuiAddr"),
                        proBuil.getString("proBuiArea"), proBuil.getString("proBuiArch"), proBuil.getString("proBuiP"),
                        proBuil.getString("proBuiLen"), proBuil.getString("proBuiWi"), proBuil.getString("proBuiHi"),
                        proBuil.getString("proBuiZ") };
                // 插入数据
                handleFormService.insertData(tableBuil, ElectricityConstant.proBuilColumn, proBuilValue);
            }
        }

    }

    /**
     * 处理绿地占用申请表
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleLdzyApply(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理绿地占用申请表==========");

        log.info("==========flowId==========" + flowId);
        String tableLDZY = "";
        String tableGreen = "";
        switch (flowId) {
            case "370900000000":
                tableLDZY = ElectricityConstant.LDZYTABLE;
                tableGreen = ElectricityConstant.GREENINFOTABLE;
                break;
            case "370982000000":
                tableLDZY = ElectricityConstant.XTLDZYTABLE;
                tableGreen = ElectricityConstant.XTGREENINFOTABLE;
                break;
            case "370911000000":
                tableLDZY = ElectricityConstant.DYLDZYTABLE;
                tableGreen = ElectricityConstant.DYGREENINFOTABLE;
                break;
            case "370921000000":
                tableLDZY = ElectricityConstant.NYLDZYTABLE;
                tableGreen = ElectricityConstant.NYGREENINFOTABLE;
                break;
            case "370923000000":
                tableLDZY = ElectricityConstant.DPLDZYTABLE;
                tableGreen = ElectricityConstant.DPGREENINFOTABLE;
                break;
            case "370983000000":
                tableLDZY = ElectricityConstant.FCLDZYTABLE;
                tableGreen = ElectricityConstant.FCGREENINFOTABLE;
                break;
            case "370990000000":
                tableLDZY = ElectricityConstant.GXLDZYTABLE;
                tableGreen = ElectricityConstant.GXGREENINFOTABLE;
                break;
            case "370902000000":
                tableLDZY = ElectricityConstant.TSLDZYTABLE;
                tableGreen = ElectricityConstant.TSGREENINFOTABLE;
                break;
            default:
                break;
        }

        String prowGuid = projectGuid;
        String[] ldzyValue = {prowGuid, prowGuid, dataInfoObj.getString("greeConName"),
                dataInfoObj.getString("greeUsci"), dataInfoObj.getString("greeConPer"),
                dataInfoObj.getString("greeMol"), dataInfoObj.getString("greeOff"), dataInfoObj.getString("greeSite"),
                dataInfoObj.getString("greeArea"), dataInfoObj.getString("greeTime"), dataInfoObj.getString("greeRea"),
                dataInfoObj.getString("greeConRemark") };
        // 插入数据
        handleFormService.insertData(tableLDZY, ElectricityConstant.ldzyColumn, ldzyValue);
        // 施工起止时间

        JSONArray greenInfoList = dataInfoObj.getJSONArray("greenInfoList");
        if (greenInfoList != null && greenInfoList.size() > 0) {
            for (int i = 0; i < greenInfoList.size(); i++) {
                String rowGuid = UUID.randomUUID().toString();
                JSONObject greenInfo = greenInfoList.getJSONObject(i);
                String[] greenInfoValue = {rowGuid, prowGuid, greenInfo.getString("greenInfoType"),
                        greenInfo.getString("greenInfoRa"), greenInfo.getString("greenInfoNu"),
                        greenInfo.getString("greenInfoAddr") };
                // 插入数据
                handleFormService.insertData(tableGreen, ElectricityConstant.greenInfoColumn, greenInfoValue);
            }
        }

    }

    /**
     * 处理占路许可申请表
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleZlxkApply(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理占路许可申请表==========");

        log.info("==========flowId==========" + flowId);
        String tableZLXK = "";
        switch (flowId) {
            case "370900000000":
                tableZLXK = ElectricityConstant.ZLXKTABLE;
                break;
            case "370982000000":
                tableZLXK = ElectricityConstant.XTZLXKTABLE;
                break;
            case "370902000000":
                tableZLXK = ElectricityConstant.TSZLXKTABLE;
                break;
            case "370911000000":
                tableZLXK = ElectricityConstant.DYZLXKTABLE;
                break;
            case "370921000000":
                tableZLXK = ElectricityConstant.NYZLXKTABLE;
                break;
            case "370923000000":
                tableZLXK = ElectricityConstant.DPZLXKTABLE;
                break;
            case "370983000000":
                tableZLXK = ElectricityConstant.FCZLXKTABLE;
                break;
            case "370990000000":
                tableZLXK = ElectricityConstant.GXZLXKTABLE;
                break;
            default:
                break;
        }

        String rowGuid = projectGuid;

        String[] zlxkValue = {rowGuid, rowGuid, dataInfoObj.getString("roadConPer"), dataInfoObj.getString("roadMol"),
                dataInfoObj.getString("roadOff"), dataInfoObj.getString("roadSite"), dataInfoObj.getString("roadTime"),
                dataInfoObj.getString("roadLen01"), dataInfoObj.getString("roadWi01"),
                dataInfoObj.getString("roadArea01"), dataInfoObj.getString("roadLen02"),
                dataInfoObj.getString("roadWi02"), dataInfoObj.getString("roadArea02"),
                dataInfoObj.getString("roadLen03"), dataInfoObj.getString("roadWi03"),
                dataInfoObj.getString("roadArea03"), dataInfoObj.getString("roadLen04"),
                dataInfoObj.getString("roadWi04"), dataInfoObj.getString("roadArea04"),
                dataInfoObj.getString("roadConApt"), dataInfoObj.getString("roadRea"),
                dataInfoObj.getString("roadRemark") };
        // 插入数据
        handleFormService.insertData(tableZLXK, ElectricityConstant.zlxkColumn, zlxkValue);

    }

    /**
     * 处理临时占用城市道路许可申请表
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleLszyApply(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理临时占用城市道路许可申请表==========");

        log.info("==========flowId==========" + flowId);
        String tableLSZY = "";
        switch (flowId) {
            case "370900000000":
                tableLSZY = ElectricityConstant.LSZYTABLE;
                break;
            case "370982000000":
                tableLSZY = ElectricityConstant.XTLSZYTABLE;
                break;
            case "370902000000":
                tableLSZY = ElectricityConstant.TSLSZYTABLE;
                break;
            case "370911000000":
                tableLSZY = ElectricityConstant.DYLSZYTABLE;
                break;
            case "370921000000":
                tableLSZY = ElectricityConstant.NYLSZYTABLE;
                break;
            case "370923000000":
                tableLSZY = ElectricityConstant.DPLSZYTABLE;
                break;
            case "370983000000":
                tableLSZY = ElectricityConstant.FCLSZYTABLE;
                break;
            case "370990000000":
                tableLSZY = ElectricityConstant.GXLSZYTABLE;
                break;
            default:
                break;
        }

        String rowGuid = projectGuid;
        String[] lszyValue = {rowGuid, rowGuid, dataInfoObj.getString("tempConName"),
                dataInfoObj.getString("tempConPer"), dataInfoObj.getString("tempMol"), dataInfoObj.getString("tempOff"),
                dataInfoObj.getString("tempSite"), dataInfoObj.getString("tempArea"), dataInfoObj.getString("tempTime"),
                dataInfoObj.getString("tempLen01"), dataInfoObj.getString("tempWi01"),
                dataInfoObj.getString("tempArea01"), dataInfoObj.getString("tempLen02"),
                dataInfoObj.getString("tempWi02"), dataInfoObj.getString("tempArea02"),
                dataInfoObj.getString("tempLen03"), dataInfoObj.getString("tempWi03"),
                dataInfoObj.getString("tempArea03"), dataInfoObj.getString("tempLen04"),
                dataInfoObj.getString("tempWi04"), dataInfoObj.getString("tempArea04"),
                dataInfoObj.getString("tempRea"), dataInfoObj.getString("tempConRemark") };
        // 插入数据
        handleFormService.insertData(tableLSZY, ElectricityConstant.lszyColumn, lszyValue);
    }

    /**
     * 处理挖掘城市道路许可申请表
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleDlwjApply(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理挖掘城市道路许可申请表==========");

        log.info("==========flowId==========" + flowId);
        String tableDLWJ = "";
        switch (flowId) {
            case "370900000000":
                tableDLWJ = ElectricityConstant.DLWJTABLE;
                break;
            case "370982000000":
                tableDLWJ = ElectricityConstant.XTDLWJTABLE;
                break;
            case "370902000000":
                tableDLWJ = ElectricityConstant.TSDLWJTABLE;
                break;
            case "370911000000":
                tableDLWJ = ElectricityConstant.DYDLWJTABLE;
                break;
            case "370921000000":
                tableDLWJ = ElectricityConstant.NYDLWJTABLE;
                break;
            case "370923000000":
                tableDLWJ = ElectricityConstant.DPDLWJTABLE;
                break;
            case "370983000000":
                tableDLWJ = ElectricityConstant.FCDLWJTABLE;
                break;
            case "370990000000":
                tableDLWJ = ElectricityConstant.GXDLWJTABLE;
                break;
            default:
                break;
        }

        String rowGuid = projectGuid;
        String[] dlwjValue = {rowGuid, rowGuid, dataInfoObj.getString("excaConName"),
                dataInfoObj.getString("excaConPer"), dataInfoObj.getString("excaMol"), dataInfoObj.getString("excaOff"),
                dataInfoObj.getString("excaSite"), dataInfoObj.getString("excaArea"), dataInfoObj.getString("excaTime"),
                dataInfoObj.getString("excaLen01"), dataInfoObj.getString("excaWi01"),
                dataInfoObj.getString("excaArea01"), dataInfoObj.getString("excaLen02"),
                dataInfoObj.getString("excaWi02"), dataInfoObj.getString("excaArea02"),
                dataInfoObj.getString("excaLen03"), dataInfoObj.getString("excaWi03"),
                dataInfoObj.getString("excaArea03"), dataInfoObj.getString("excaLen04"),
                dataInfoObj.getString("excaWi04"), dataInfoObj.getString("excaArea04"),
                dataInfoObj.getString("excaRea"), dataInfoObj.getString("excaConRemark") };
        // 插入数据
        handleFormService.insertData(tableDLWJ, ElectricityConstant.dlwjColumn, dlwjValue);

    }

    /**
     * 处理涉路许可申请表
     * 
     * @param projectGuid
     * @param dataInfoObj
     */
    private void handleSlxkApply(String projectGuid, JSONObject dataInfoObj, String flowId) {
        log.info("==========处理涉路许可申请表==========");

        log.info("==========flowId==========" + flowId);
        String tableSLXK = "";
        String tableInvoroad = "";
        switch (flowId) {
            case "370900000000":
                tableSLXK = ElectricityConstant.SLXKTABLE;
                tableInvoroad = ElectricityConstant.INVOROADTABLE;
                break;
            case "370982000000":
                tableSLXK = ElectricityConstant.XTSLXKTABLE;
                tableInvoroad = ElectricityConstant.XTINVOROADTABLE;
                break;
            case "370902000000":
                tableSLXK = ElectricityConstant.TSSLXKTABLE;
                tableInvoroad = ElectricityConstant.TSINVOROADTABLE;
                break;
            case "370911000000":
                tableSLXK = ElectricityConstant.DYSLXKTABLE;
                tableInvoroad = ElectricityConstant.DYINVOROADTABLE;
                break;
            case "370921000000":
                tableSLXK = ElectricityConstant.NYSLXKTABLE;
                tableInvoroad = ElectricityConstant.NYINVOROADTABLE;
                break;
            case "370923000000":
                tableSLXK = ElectricityConstant.DPSLXKTABLE;
                tableInvoroad = ElectricityConstant.DPINVOROADTABLE;
                break;
            case "370983000000":
                tableSLXK = ElectricityConstant.FCSLXKTABLE;
                tableInvoroad = ElectricityConstant.FCINVOROADTABLE;
                break;
            case "370990000000":
                tableSLXK = ElectricityConstant.GXSLXKTABLE;
                tableInvoroad = ElectricityConstant.GXINVOROADTABLE;
                break;
            default:
                break;
        }

        String prowGuid = projectGuid;
        // 修复、改建公路措施/补偿数额
        String invoComp = dataInfoObj.getString("invoComp");
        String[] slxkValue = {prowGuid, prowGuid, dataInfoObj.getString("invoHand"),
                dataInfoObj.getString("invoEemark"), dataInfoObj.getString("invoTime"),
                dataInfoObj.getString("invoAddr"), dataInfoObj.getString("invoIntro"),
                dataInfoObj.getString("invoSafe"), invoComp };
        // 插入数据
        handleFormService.insertData(tableSLXK, ElectricityConstant.slxkColumn, slxkValue);

        JSONArray invoRoadList = dataInfoObj.getJSONArray("invoRoadList");
        if (invoRoadList != null && invoRoadList.size() > 0) {
            for (int i = 0; i < invoRoadList.size(); i++) {
                String rowGuid = UUID.randomUUID().toString();
                JSONObject invoRoad = invoRoadList.getJSONObject(i);
                String[] invoRoadValue = {rowGuid, prowGuid, invoRoad.getString("invoRoadNo"),
                        invoRoad.getString("invoRoadNumb"), invoRoad.getString("invoRoadAddr"),
                        invoRoad.getString("invoRoadLev"), invoRoad.getString("invoRoadWi"),
                        invoRoad.getString("invoRoadAng"), invoRoad.getString("invoRoadRa"),
                        invoRoad.getString("invoRoadHi"), invoRoad.getString("invoRoadDis"),
                        invoRoad.getString("invoRoadDist"), invoRoad.getString("invoRoadWid"),
                        invoRoad.getString("invoRoadForm"), invoRoad.getString("invoRoadOrg") };
                // 插入数据
                handleFormService.insertData(tableInvoroad, ElectricityConstant.invoRoadColumn, invoRoadValue);
            }
        }

    }
}
