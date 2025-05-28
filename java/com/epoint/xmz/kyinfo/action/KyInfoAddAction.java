package com.epoint.xmz.kyinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.search.inteligentsearch.sdk.util.EpointDateUtil;
import com.epoint.xmz.kyinfo.api.IKyInfoService;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
import com.epoint.xmz.kyperson.api.IKyPersonService;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import com.epoint.xmz.kypoint.api.IKyPointService;
import com.epoint.xmz.kypoint.api.entity.KyPoint;

/**
 * 勘验信息表新增页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:18]
 */
@RightRelation(KyInfoListAction.class)
@RestController("kyinfoaddaction")
@Scope("request")
public class KyInfoAddAction extends BaseController
{
    @Autowired
    private IKyInfoService service;
    @Autowired
    private IKyPointService pointService;
    @Autowired
    private IKyPersonService personService;
    @Autowired
    private IAuditProject auditProjectService;
    @Autowired
    private IMessagesCenterService messageCenterService;

    /**
     * 表格控件model
     */
    private DataGridModel<KyPoint> pointmodel;
    
    /**
     * 表格控件model
     */
    private DataGridModel<KyPerson> personmodel;
    
    /**
     * 勘验信息表实体对象
     */
    private KyInfo dataBean = null;

    /**
     * 勘验城市下拉列表model
     */
    private List<SelectItem> kycityModel = null;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;
    private String projectguid;
    public void pageLoad() {
        dataBean = new KyInfo();
        projectguid = getRequestParameter("projectguid");
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        String rowguid = UUID.randomUUID().toString();
        dataBean.setRowguid(rowguid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setProjectguid(projectguid);
        dataBean.setClientguid(getViewData("cliengguid"));
        service.insert(dataBean);
        addCallbackParam("msg", l("提交成功！"));
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("rowguid", projectguid);
        AuditProject auditProject = auditProjectService.getAuditProjectByCondition(sqlConditionUtil.getMap());
        String projectname = auditProject.getProjectname();
        if ("在城市建筑物、设施上张挂、张贴宣传品审批".equals(projectname)||"延续设置电子显示屏广告".equals(projectname)||"设置其他类型户外广告".equals(projectname)||
            "设置电子显示屏".equals(projectname)||"设置任一边长大于4米或者面积大于10平方米的固定式广告".equals(projectname)||"设置车体广告".equals(projectname)) {
            this.sendInfoToCg(auditProject);
        }
        else {
            this.sendMsgAndWait(auditProject);
        }
        dataBean = null;
        new KyPoint();
    }

    //向系统中的人员发送信息和待办
    public void sendMsgAndWait(AuditProject auditProject) {
        List<KyPerson> person = personService.getPersonByProjectguid(projectguid);
        for (KyPerson kyPerson : person) {
            String name = kyPerson.getName();
            String mobile = kyPerson.getMobile();
            String userguid = kyPerson.getStr("userguid");
            //发送短信
            String strContent = "你好，您有新的勘验请求，请您尽快登录系统进行处理。";// 短信内容
            messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), strContent, new Date(), 0, null,
                    mobile, mobile, "", "", "", "", "", null,false, "短信");
            //发送待办
            String handleUrl="jiningzwfw/xmz/kyinfo/kyinfohandle.html?projectguid="+projectguid;
            messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), auditProject.getProjectname()+"(勘验请求)",
                    IMessagesCenterService.MESSAGETYPE_WAIT, userguid,
                    name, userSession.getUserGuid(), userSession.getDisplayName(), "勘验请求",
                    handleUrl, userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                    auditProject.getRowguid(), "", new Date(),
                    auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            
        }
    }
    
    
    //通过结果传递信息到城管侧
    public void sendInfoToCg(AuditProject project) {
        try {
            String apiUrl = "http://172.20.58.59:18015/free/exchange/openapi/v2/upstream/uprecreport";
            // 构建参数
            Map<String, Object> map = new HashMap<>();
            map.put("senderCode","sgxj");
            map.put("actionType","UP_REC_REPORT");
            JSONObject param = new JSONObject();
            param.put("otherTaskNum", project.getFlowsn());
            String points = pointService.getPointByProjectguid(dataBean.getProjectguid());
            String date = EpointDateUtil.convertDate2String(dataBean.getEndtime(),"yyyy-MM-dd HH:mm:ss");
            param.put("eventDesc", points+";"+date);
            param.put("address",dataBean.getKylocation());
            param.put("eventSrcID", 56);
            param.put("recTypeID", 1305);
            param.put("eventLevelID", 1);
            param.put("eventTypeCode", "6");
            if ("延续设置电子显示屏广告".equals(project.getProjectname())) {
                param.put("mainTypeCode","602");
                param.put("subTypeCode", "60201");
            }
            else if ("设置其他类型户外广告".equals(project.getProjectname())) {
                param.put("mainTypeCode","602");
                param.put("subTypeCode", "60202");
            }
            else if ("设置电子显示屏".equals(project.getProjectname())) {
                param.put("mainTypeCode","602");
                param.put("subTypeCode", "60203");
            }
            else if ("设置任一边长大于4米或者面积大于10平方米的固定式广告".equals(project.getProjectname())) {
                param.put("mainTypeCode","602");
                param.put("subTypeCode", "60204");
            }
            else if ("设置车体广告".equals(project.getProjectname())) {
                param.put("mainTypeCode","602");
                param.put("subTypeCode", "60205");
            }
            else if ("在城市建筑物、设施上张挂、张贴宣传品审批".equals(project.getProjectname())) {
                param.put("mainTypeCode","603");
                param.put("subTypeCode", "60301");
            }
            map.put("data",param.toJSONString());
            String result = HttpUtil.doPost(apiUrl, map);
            JSONObject reJson = JSONObject.parseObject(result);
            log.info(result);
            JSONObject jsonObject = reJson.getJSONObject("data");
            if (jsonObject!=null) {
                String taskNum = jsonObject.getString("taskNum");
                String recID = jsonObject.getString("recID");
                dataBean.setTaskNum(taskNum);
                dataBean.setRecID(recID);
                dataBean.setIssync("0");
                service.update(dataBean);
            }
        }
        catch (Exception e) {
            log.info("接口调用失败！错误信息：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //删除要点
    public void deletePoint() {
        List<String> select = pointgetDataGridData().getSelectKeys();
        for (String sel : select) {
             pointService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }
    
    //获取要点列表
    public DataGridModel<KyPoint> pointgetDataGridData() {
        // 获得表格对象
        if (pointmodel == null) {
            pointmodel = new DataGridModel<KyPoint>()
            {

                @Override
                public List<KyPoint> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 获取where条件Map集合
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("kyguid", projectguid);
                    PageData<KyPoint> relationpageData = pointService
                            .paginatorList(sqlConditionUtil.getMap(), first, pageSize);
                    
                    List<KyPoint> relationlist = relationpageData.getList();
                    this.setRowCount(relationpageData.getRowCount());
                    return relationlist;
                }
            };
        }
        return pointmodel;
    }
    //删除人员
    public void deletePerson() {
        List<String> select = personGetDataGridData().getSelectKeys();
        for (String sel : select) {
             personService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }
    //获取人员列表
    public DataGridModel<KyPerson> personGetDataGridData() {
        // 获得表格对象
        if (personmodel == null) {
            personmodel = new DataGridModel<KyPerson>()
            {

                @Override
                public List<KyPerson> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 获取where条件Map集合
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("kyguid",projectguid);
                    PageData<KyPerson> relationpageData = personService
                            .paginatorList(sqlConditionUtil.getMap(), first, pageSize);
                    
                    List<KyPerson> relationlist = relationpageData.getList();
                    this.setRowCount(relationpageData.getRowCount());
                    return relationlist;
                }
            };
        }
        return personmodel;
    }
    
    

    public KyInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new KyInfo();
        }
        return dataBean;
    }

    public void setDataBean(KyInfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getKycityModel() {
        if (kycityModel == null) {
            kycityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "区县市", null, false));
        }
        return this.kycityModel;
    }
    
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            if (StringUtil.isBlank(getViewData("cliengguid"))) {
                String cliengguid = UUID.randomUUID().toString();
                addViewData("cliengguid", cliengguid);
            }
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("cliengguid"), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        // 该属性设置他为只读，不能被删除
        // fileUploadModel1.setReadOnly("true");
        return fileUploadModel;
    }

}
