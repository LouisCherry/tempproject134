package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditexpress.api.IAuditExpressService;
import com.epoint.auditexpress.api.entity.AuditExpress;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;

/***
 *  网上申报涉及的接口
 * @作者 shibin
 * @version [版本号, 2018年12月28日]
 */
@RestController
@RequestMapping("/tazwdtProject")
public class TAAuditOnlineProjectController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnAppRestService iJnAppRestService;

    /**
     * 事项基本信息API
     */
    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 事项扩展信息API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    /**
     * 事项多情形API
     */
    @Autowired
    private IAuditTaskCase iAuditTaskCase;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    /**
     * 网上用户注册API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;

    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;

    /**
     * 快递信息实体
     */
    private AuditExpress auditExpress;
    /**
     * 快递信息API
     */
    @Autowired
    private IAuditExpressService iAuditExpressService;

    /**
     * 事项情形与材料关系API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;

    /**
     *  申报须知接口(办件申报须知调用)
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/declareTaProjectNotice", method = RequestMethod.POST)
    public String declareTaProjectNotice(@RequestBody String params) {
        try {
            log.info("=======开始调用declareTaProjectNotice接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskGuid = obj.getString("taskguid");
                
                // 1.1、获取事项标识
                String itemid = obj.getString("itemcode");
                String inner_code = obj.getString("inner_code");
                
                
                //个性化
                String requestUrl = jsonObject.getString("qurl");

                // 2、获取事项基本信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                
                
                if (StringUtil.isNotBlank(inner_code)) {
                     taskGuid = iJnAppRestService.getTaskguidByInnercode(inner_code).getResult();
                     log.info("输出对应的事项信息："+taskGuid);
                     auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                     
                }
                
                if (auditTask != null) {
                    // 3、获取事项扩展信息
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 5、设置需要返回的事项基本信息
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id()); // 事项业务唯一标识
                    dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                    //// 是否禁用
                    dataJson.put("is_enable", auditTask.getIs_enable());
                    //// 是否网厅展示事项
                    dataJson.put("iswtshow", auditTask.get("iswtshow"));
                    
                    String zjurl = auditTask.getStr("zj_url");
                    if (StringUtil.isNotBlank(zjurl)) {
                        dataJson.put("zjurl", zjurl);
                    }else {
                        dataJson.put("zjurl", "0");
                    }
                    String isturn = auditTask.getStr("is_turn");
                    if (StringUtil.isNotBlank(isturn)) {
                        dataJson.put("isturn", isturn);
                    }else {
                        dataJson.put("isturn", "0");
                    }
                    
                    if ("1".equals(auditTask.getStr("ywztcode"))) {
                        dataJson.put("isywzt", "1");
                    }
//                    if (StringUtil.isNotBlank(auditTask.getStr("ywztcode"))) {
//                        String[] yeztcode = auditTask.getStr("ywztcode").split(";");
//                        if(yeztcode.length>=2){
//                            dataJson.put("ywztcode", yeztcode[0]);
//                            dataJson.put("newItemCode",yeztcode[1]);
//                        }else if(yeztcode.length>=1){
//                            dataJson.put("ywztcode", yeztcode[0]);
//                        }
//
//                        dataJson.put("isywzt", "1");
//                    }else {
//                    	dataJson.put("isywzt", "0");
//                    }
                    
                    
                    dataJson.put("taskicon",
                            requestUrl + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码图标内容
                    JSONObject taskElementJson = new JSONObject();
                    String applytype = String.valueOf(auditTaskExtension.getWebapplytype());
                    taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(applytype) ? "0" : "1");// 是否可以网上申报
                    taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                    dataJson.put("taskelement", taskElementJson);

                    List<JSONObject> materialJsonList1 = new ArrayList<JSONObject>();
                    String spcondition = auditTask.getAcceptcondition();

                    if (spcondition.contains("<b>1</b>&nbsp;&nbsp;&nbsp;<b>")) {
                        // 受理条件  针对含有 <b>1</b>&nbsp;&nbsp;&nbsp;<b>
                        String regex = "<b>[0-9]</b>&nbsp;&nbsp;&nbsp;<b>";
                        String[] arr = spcondition.split(regex);
                        String[] arrNew = new String[arr.length];

                        for (int i = 0; i < arr.length; i++) {
                            if (StringUtil.isNotBlank(arr[i])) {
                                String newStr = arr[i].replace("</b><br>", "");
                                arrNew[i] = newStr;
                            }
                        }
                        for (int i = 0; i < arrNew.length; i++) {
                            if (StringUtil.isNotBlank(arrNew[i])) {
                                JSONObject materialJson1 = new JSONObject();
                                materialJson1.put("spcondition", arrNew[i]);// 材料名称
                                materialJsonList1.add(materialJson1);
                            }
                        }
                    }
                    else {
                        // 对于不含 页面元素 的条件
                        String regex2 = "[0-9]、";
                        String[] arr = spcondition.split(regex2);

                        for (int i = 0; i < arr.length; i++) {
                            if (StringUtil.isNotBlank(arr[i])) {
                                JSONObject materialJson1 = new JSONObject();
                                materialJson1.put("spcondition", arr[i]);// 材料名称
                                materialJsonList1.add(materialJson1);
                            }
                        }
                    }

                    dataJson.put("spconditionlist", materialJsonList1); // 审批条件
                    //dataJson.put("spcondition", auditTask.getAcceptcondition()); // 审批条件

                    // 6、获取事项多情形数据
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseJsonList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && auditTaskCases.size() > 0) {
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            JSONObject caseJson = new JSONObject();
                            caseJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                            caseJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                            caseJsonList.add(caseJson);
                        }
                    }
                    dataJson.put("taskcasecount", caseJsonList.size());
                    dataJson.put("casecondition", caseJsonList);
                    if (caseJsonList.size() == 0) {
                        // 7、没有多情形的情况下需要返回事项配置的材料，多情形情况下会调用下面接口返回事项材料数据
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 7.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>()
                        {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 8.1.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 8.1.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? Integer.valueOf(0)
                                            : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? Integer.valueOf(0)
                                            : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            materialJsonList.add(materialJson);
                        }
                        dataJson.put("taskmateriallist", materialJsonList);
                        //事项申请类型
                        dataJson.put("applyertype",auditTask.getApplyertype());
                    }
                    log.info("=======结束调用declareTaProjectNotice接口=======");
                    return JsonUtils.zwdtRestReturn("1", "申报须知信息获取成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareTaProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareTaProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取快递单初始化信息（办件申报信息调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getEmsInit", method = RequestMethod.POST)
    public String getEmsInit(@RequestBody String params) {
        try {
            log.info("=======开始调用getEmsInit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域标识
                String areaCode = obj.getString("areacode");
                // 1.4、获取用户基本信息

                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);
                //个性化 获取地址
                String requestUrl = jsonObject.getString("qurl");

                if (auditOnlineRegister != null) {
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    JSONObject applyerJson = new JSONObject();
                    // 5、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {

                        CommonDao dao = new CommonDao();
                        String sql = "select OUSHORTNAME from frame_ou f INNER JOIN audit_task t ON f.OUGUID = t.OUGUID WHERE f.OUGUID = ? ";
                        String ouname = dao.queryString(sql, auditTask.getOuguid());
                        String sqlArea = "select XiaQuName from audit_orga_area WHERE XiaQuCode = ? ";
                        String areaName = dao.queryString(sqlArea, auditTask.getAreacode());

                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("department", ouname);// 部门名称
                        dataJson.put("ouname", auditTask.getOuname());// 部门名称
                        dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        dataJson.put("taskaddress", auditTask.getTransact_addr());// 办公地址
                        dataJson.put("areaName", areaName);// 辖区名
                        dataJson.put("taskicon", requestUrl
                                + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码内容
                        areaCode = auditTask.getAreacode();
                        dataJson.put("areacode", areaCode);// 区域编码
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                            dataJson.put("promiseday", "1");// 即办件显示1天
                        }
                        else {
                            dataJson.put("promiseday", auditTask.getPromise_day());
                        }
                        JSONObject taskElementJson = new JSONObject();
                        taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype())) ? "0" : "1");// 是否可以网上申报
                        taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                        dataJson.put("taskelement", taskElementJson);

                        // 6、获取用户个人信息
                        AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                        // 7、获取申请信息
                        applyerJson.put("applyername", auditOnlineIndividual.getClientname());//申报人
                        if (StringUtil.isNotBlank(projectGuid)) {
                            String fields = " rowguid,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,legal,certtype,applyername,windowname ";
                            AuditProject auditProject = iAuditProject
                                    .getAuditProjectByRowGuid(fields, projectGuid, areaCode).getResult();
                            if (auditProject != null) {
                                applyerJson.put("contactperson", auditProject.getContactperson());// 联系人
                                applyerJson.put("linkphone", auditProject.getContactmobile());// 联系人手机
                                applyerJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                                applyerJson.put("postcode", auditProject.getContactpostcode()); // 邮编
                                applyerJson.put("address", auditProject.getAddress()); // 地址
                            }
                        }
                    }
                    dataJson.put("applerinfo", applyerJson);
                    log.info("=======结束调用getEmsInit接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件基本信息成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getEmsInit接口参数：params【" + params + "】=======");
            log.info("=======getEmsInit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件申报获取办件基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     *  保存EMS信息的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/saveEmsInfo", method = RequestMethod.POST)
    public String saveEmsInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用saveEmsInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String taskGuid = obj.getString("taskguid");
                String projectGuid = obj.getString("projectguid");
                // 1.2、寄件人
                String sender = obj.getString("sender");
                // 1.3、寄件人手机号
                String sendphone = obj.getString("sendphone");
                // 1.4、寄件人单位
                String sendunits = obj.getString("sendunits");
                //寄件人地址
                String sendcmbProvince = obj.getString("sendcmbProvince");
                String sendcmbCity = obj.getString("sendcmbCity");
                String sendcmbArea = obj.getString("sendcmbArea");
                // 1.8、地址
                String sendaddress = obj.getString("sendaddress");
                // 1.9、邮编
                String sendmail = obj.getString("sendmail");
                // 1.9、收件人
                String receiver = obj.getString("receiver");
                // 1.10、收件人手机号
                String receivephone = obj.getString("receivephone");
                // 1.11、收件人单位
                String receiveunits = obj.getString("receiveunits");

                // 收件人地址
                String cmbProvince = obj.getString("cmbProvince");
                String cmbCity = obj.getString("cmbCity");
                String cmbArea = obj.getString("cmbArea");
                // 1.12、地址
                String receiveaddress = obj.getString("receiveaddress");
                // 1.14、邮编
                String receivemail = obj.getString("receivemail");
                // 1.15、快递类型
                String value = obj.getString("value");
                //寄件人物流编号
                String billno = obj.getString("billno");

                // 1.16、 寄送范围
                String mySelect = obj.getString("mySelect");

                // 删除原有的记录,保存新的快递记录
                if (StringUtil.isNotBlank(projectGuid)) {
                    iAuditExpressService.deleteByProjectguid(projectGuid);
                }

                // 保存EMS快递信息
                if (StringUtil.isNotBlank(value)) {

                    if (value.equals("1")) {
                        //获取物流编号  此条记录为待寄件

                        // 保存两条记录
                        // 1.寄件人 sender    个人寄件到中心  
                        auditExpress = new AuditExpress();
                        //寄件保存物流编号
                        if (StringUtil.isNotBlank(billno)) {
                            auditExpress.setTrackingnumber(billno);
                        }
                        auditExpress.setRowguid(UUID.randomUUID().toString());
                        auditExpress.setOperatedate(new Date());
                        //寄件人地址为 省市区加地址
                        auditExpress.setSendertype("20");
                        auditExpress.setSendername(sender);
                        auditExpress.setSenderphone(sendphone);
                        auditExpress.setSenderpostcode(sendmail);
                        auditExpress.setSenderunit(sendunits);
                        auditExpress.setSenderaddress(sendaddress);
                        //个人所在省市区
                        auditExpress.setProvince(sendcmbProvince);
                        auditExpress.setCity(sendcmbCity);
                        auditExpress.setRegion(sendcmbArea);
                        // 收件人
                        auditExpress.setConsigneename(receiver);
                        auditExpress.setConsigneephone(receivephone);
                        auditExpress.setConsigneeaddress(cmbProvince + cmbCity + cmbArea + receiveaddress);
                        auditExpress.setConsigneepostcode(receivemail);
                        auditExpress.setConsigneeunit(receiveunits);

                        // 其他信息
                        CommonDao dao = new CommonDao();
                        AuditTask auditTask = dao.find(AuditTask.class, taskGuid);
                        auditExpress.setOuname(auditTask.getOuname());
                        auditExpress.setOucode(auditTask.getOuguid());
                        auditExpress.setTaskguid(taskGuid);
                        auditExpress.setProjectguid(projectGuid);
                        auditExpress.setTaskname(auditTask.getTaskname());
                        auditExpress.setWaithandledate(new Date());
                        auditExpress.setStatus("10");
                        auditExpress.setDystatus("10");
                        auditExpress.setPayway("0");
                        auditExpress.setSendarea(mySelect);
                        auditExpress.setEMSSources("20");

                        int a = iAuditExpressService.insert(auditExpress);
                        if (a > 0) {
                            iAuditExpressService.updateExpressAdress(sendcmbCity, sendcmbCity, sendcmbArea, "",
                                    auditExpress.getRowguid());
                        }

                        // 2.寄件人 receiver 中心寄个人
                        AuditExpress auditExpressRe = new AuditExpress();

                        auditExpressRe.setRowguid(UUID.randomUUID().toString());
                        auditExpressRe.setOperatedate(new Date());
                        auditExpressRe.setSendertype("10");
                        auditExpressRe.setSendername(receiver);
                        auditExpressRe.setSenderaddress(receiveaddress);
                        auditExpressRe.setSenderphone(receivephone);
                        auditExpressRe.setSenderpostcode(receivemail);
                        auditExpressRe.setSenderunit(receiveunits);
                        //中心省  市 区
                        auditExpressRe.setProvince(cmbProvince);
                        auditExpressRe.setCity(cmbCity);
                        auditExpressRe.setRegion(cmbArea);

                        // 收件人
                        auditExpressRe.setConsigneename(sender);
                        auditExpressRe.setConsigneephone(sendphone);
                        auditExpressRe.setConsigneeaddress(sendcmbProvince + sendcmbCity + sendcmbArea + sendaddress);
                        auditExpressRe.setConsigneepostcode(sendmail);
                        auditExpressRe.setConsigneeunit(sendunits);

                        // 其他信息
                        auditExpressRe.setOuname(auditTask.getOuname());
                        auditExpressRe.setOucode(auditTask.getOuguid());
                        auditExpressRe.setTaskguid(taskGuid);
                        auditExpressRe.setProjectguid(projectGuid);
                        auditExpressRe.setTaskname(auditTask.getTaskname());
                        auditExpressRe.setWaithandledate(new Date());
                        auditExpressRe.setStatus("10");
                        auditExpressRe.setDystatus("10");
                        auditExpressRe.setPayway("0");
                        auditExpressRe.setSendarea(mySelect);
                        auditExpressRe.setEMSSources("20");

                        int b = iAuditExpressService.insert(auditExpressRe);

                        if (b > 0) {
                            iAuditExpressService.updateExpressAdress(cmbProvince, cmbCity, cmbArea, "",
                                    auditExpressRe.getRowguid());
                        }

                        return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

                    }
                    else {
                        // 保存一条记录
                        // 寄件人
                        auditExpress = new AuditExpress();
                        auditExpress.setRowguid(UUID.randomUUID().toString());
                        auditExpress.setOperatedate(new Date());

                        // 申请材料快递上门取件，证照或结果文书现场领取
                        if (value.equals("2")) {
                            // 20 群众
                            auditExpress.setSendertype("20");
                            auditExpress.setTrackingnumber(billno);
                            auditExpress.setProvince(sendcmbProvince);
                            auditExpress.setCity(sendcmbCity);
                            auditExpress.setRegion(sendcmbArea);
                        }
                        else {
                            // 10 中心
                            auditExpress.setSendertype("10");
                            //中心send需要物流编号
                            auditExpress.setTrackingnumber(billno);
                            auditExpress.setProvince(cmbProvince);
                            auditExpress.setCity(cmbCity);
                            auditExpress.setRegion(cmbArea);
                        }
                        auditExpress.setSendername(sender);
                        auditExpress.setSenderaddress(sendaddress);
                        auditExpress.setSenderphone(sendphone);
                        auditExpress.setSenderpostcode(sendmail);
                        auditExpress.setSenderunit(sendunits);
                        // 收件人
                        auditExpress.setConsigneename(receiver);
                        auditExpress.setConsigneephone(receivephone);

                        auditExpress.setConsigneeaddress(receiveaddress);
                        auditExpress.setConsigneeaddress(receiveaddress);
                        auditExpress.setConsigneepostcode(receivemail);
                        auditExpress.setConsigneeunit(receiveunits);

                        // 其他信息
                        CommonDao dao = new CommonDao();
                        AuditTask auditTask = dao.find(AuditTask.class, taskGuid);

                        auditExpress.setOuname(auditTask.getOuname());
                        auditExpress.setOucode(auditTask.getOuguid());
                        auditExpress.setTaskguid(taskGuid);
                        auditExpress.setProjectguid(projectGuid);
                        auditExpress.setTaskname(auditTask.getTaskname());
                        auditExpress.setWaithandledate(new Date());
                        auditExpress.setStatus("10");
                        auditExpress.setDystatus("10");
                        auditExpress.setPayway("0");
                        auditExpress.setSendarea(mySelect);
                        auditExpress.setEMSSources("20");

                        iAuditExpressService.insert(auditExpress);

                        return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

                    }

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveEmsInfo接口参数：params【" + params + "】=======");
            log.info("=======saveEmsInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "EMS信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     *  更改EMS状态
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     */
    @RequestMapping(value = "/submitEmsStatus", method = RequestMethod.POST)
    public void submitEmsStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitEmsStatus接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");

                // 1. 快递类型
                String value = obj.getString("value");
                String projectguid = obj.getString("projectguid");
                String EMSSources = "";
                if (obj.containsKey("EMSSources")) {
                    EMSSources = obj.getString("EMSSources");
                }

                if (!value.equals("0")) {
                    // 保存EMS快递信息
                    if (StringUtil.isNotBlank(value)) {

                        List<AuditExpress> list = iAuditExpressService.listByProjectguid(projectguid);

                        for (AuditExpress auditExpress : list) {
                            if (value.equals("1")) {
                                // 更新记录
                                // 1.寄件人 sender
                                // 10 中心           20 群众
                                if (auditExpress.getSendertype().equals("20")) {
                                    // 待收件显示
                                    auditExpress.setZwdtExpressStatus("40");
                                }
                                else {
                                    // 待寄件不显示
                                    auditExpress.setZwdtExpressStatus("10");
                                }
                                iAuditExpressService.update(auditExpress);
                            }
                            else {
                                // 更新记录
                                // 申请材料快递上门取件，证照或结果文书现场领取
                                if (value.equals("2")) {
                                    // 待收件显示
                                    auditExpress.setZwdtExpressStatus("40");
                                }
                                else {
                                    //业务来源
                                    if (StringUtil.isNotBlank(EMSSources)) {
                                        auditExpress.setEMSSources(EMSSources);
                                    }
                                    // 待寄件不显示
                                    auditExpress.setZwdtExpressStatus("10");
                                }
                                iAuditExpressService.update(auditExpress);
                            }
                        }
                    }
                }
                else {
                    // 删除部分数据，选择快递后，又取消的
                    iAuditExpressService.deleteByProjectguid(projectguid);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitEmsStatus接口参数：params【" + params + "】=======");
            log.info("=======submitEmsStatus异常信息：" + e.getMessage() + "=======");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkMaterialIsSubmit", method = RequestMethod.POST)
    public String checkMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                // 1.3、获取多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                int noSubmitNum = 0;// 必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(materialGuid).getResult();
                        if (auditTaskMaterial != null) {
                            // 4、根据材料的提交状态及材料设置的必要性判断材料是否已提交（材料类型为附件的才需要判断）
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 5、材料必要性默认为事项材料配置的必要性
                                int necessity = auditTaskMaterial.getNecessity();
                                // 6、如果事项配置了多情形，需要考虑材料在多情形中的必要性
                                if (StringUtil.isNotBlank(taskCaseGuid)) {
                                    AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                            .selectTaskMaterialCaseByGuid(taskCaseGuid, auditTaskMaterial.getRowguid())
                                            .getResult();
                                    necessity = auditTaskMaterialCase == null ? necessity
                                            : auditTaskMaterialCase.getNecessity();
                                }
                                // 7、如果材料设置了必填，则需要通过材料的提交方式进行判断（外网纸质材料不予判断）
                                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                    int submitType = Integer.parseInt(auditTaskMaterial.getSubmittype()); // 材料提交方式
                                    int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
                                    if (submitType == WorkflowKeyNames9.SubmitType_Submit
                                            || submitType == WorkflowKeyNames9.SubmitType_Submit_Or_PaperSubmit
                                            || submitType == WorkflowKeyNames9.SubmitType_PaperSubmit) {
                                        // 7.1、提交方式为电子，则状态不是电子已提交或者电子和纸质都提交，说明材料未提交
                                        if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                                && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                            noSubmitNum++;
                                        }
                                    }
                                    else if (submitType == WorkflowKeyNames9.SubmitType_Submit_And_PaperSubmit) {
                                        // 7.2、提交方式为电子和纸质，则状态不是电子已提交或者电子和纸质都提交，说明材料未提交
                                        if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                                && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                            noSubmitNum++;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     *  提交办件的接口（办件申报提交调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/submitProjectByTaskguid", method = RequestMethod.POST)
    public String submitProjectByTaskguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitProjectByTaskguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域编码
                // String areaCode = obj.getString("areacode");
                // 1.4、获取法人代表
                String legal = obj.getString("legal");
                // 1.5、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.6、获取申请人证照编号
                String certNum = obj.getString("idcard");
                // 1.7、获取证照类型
                String certType = obj.getString("certtype");
                // 1.8、获取申请人类型
                // String applyerType = obj.getString("applyertype");
                // 1.9、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.10、获取联系人手机号
                String contactMobile = obj.getString("contactmobile");
                // 1.11、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.12、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.13、获取邮编
                String postCode = obj.getString("postcode");
                // 1.14、获取通讯地址
                String address = obj.getString("address");
                // 1.15、获取备注
                String remark = obj.getString("remark");
                // 1.16、获取电子邮箱
                String email = obj.getString("email");
                // 1.17、获取传真
                String fax = obj.getString("fax");
                // 1.18、获取多情形标识
                // String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.19、获取是否需要快递
                String ifExpress = obj.getString("if_express");
                // 1.20、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取用户申报的事项
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByApplyerGuid(projectGuid, auditOnlineIndividual.getApplyerguid())
                            .getResult();
                    int oldstatus = 0; //预审打回时使用 
                    if (auditOnlineProject != null) {
                        oldstatus = Integer.parseInt(auditOnlineProject.getStatus());
                    }
                    // 4、获取事项扩展信息
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid,null).getResult();
                    if (auditProject != null) {
                        if ("12".equals(auditProject.getStatus())) {
                            return JsonUtils.zwdtRestReturn("0", "不允许重复提交办件", "");
                        }
                    }else {
                        auditProject = new AuditProject();
                    }
                    // 5、获取办件状态，如网上申报类型是 网上申报后直接办理，则办件状态更新为待接件
                    int status = ZwfwConstant.BANJIAN_STATUS_WWYTJ;// 默认办件状态：外网申报已提交
                    if (ZwfwConstant.WEB_APPLY_TYPE_SL.equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                        status = ZwfwConstant.BANJIAN_STATUS_DJJ;
                        System.out.println(status);
                    }
                    // 6、更新AUDIT_ONLINE_PROJECT表数据
                    Map<String, String> updateFieldMap = new HashMap<>(16);
                    updateFieldMap.put("applyername=", applyerName);
                    updateFieldMap.put("status=", String.valueOf(status));
                    Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                    if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { //判断是否是预审退回
                        updateDateFieldMap.put("applydate=",
                                EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("sourceguid", projectGuid);
                    sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                    iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                            sqlConditionUtil.getMap());
                    // 8.1、设置办件证照类型
                    if (StringUtil.isBlank(certType)) {
                        // 8.1.1、如果证照类型为空，需要根据申请人类型设置默认值
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 8.1.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 8.1.1.2、若申请人类型为企业，则默认设置证照类型为组织机构代码证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                        }
                    }
                    else {
                        // 8.1.2、如果证照类型不为空，则获取传递的证照类型
                        auditProject.setCerttype(certType);
                    }
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();

                    auditProject.setRowguid(projectGuid);
                    if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { //判断是否是预审退回
                        if(auditProject.getApplydate()==null){
                            auditProject.setApplydate(new Date());
                        }
                    }
                    auditProject.setCertnum(certNum);
                    auditProject.setApplyername(applyerName);
                    auditProject.setContactperson(contactName);
                    auditProject.setContactmobile(contactMobile);
                    auditProject.setContactphone(contactPhone);
                    auditProject.setContactpostcode(postCode);
                    auditProject.setContactcertnum(contactIdnum);
                    auditProject.setRemark(remark);
                    auditProject.setAddress(address);
                    auditProject.setLegal(legal);
                    auditProject.setContactemail(email);
                    auditProject.setContactfax(fax);
                    auditProject.setIf_express(ifExpress);
                    auditProject.setStatus(status);
                    auditProject.setAreacode(auditTask.getAreacode());
                    iAuditProject.updateProject(auditProject);
                    log.info("=======结束调用submitProjectByTaskguid接口=======");
                    return JsonUtils.zwdtRestReturn("1", "提交办件成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitProject接口参数：params【" + params + "】=======");
            log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
        }
        return auditOnlineRegister;
    }

}
