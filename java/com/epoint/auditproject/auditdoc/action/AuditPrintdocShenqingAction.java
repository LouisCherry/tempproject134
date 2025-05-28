package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShenqing;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocShenqing;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 申请通知书新增页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-10-26 14:14:32]
 */
@RestController("auditprintdocshenqingaction")
@Scope("request")
public class AuditPrintdocShenqingAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -8764467453058944827L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,taskguid,projectname,applydate,orgwn,legal,ouguid,certtype,certnum,biguid,applyername,contactphone,contactperson,contactmobile,address,contactemail,contactpostcode,task_id,applyertype,legalid,contactcertnum";
    /**
     * 办件材料service
     */
    @Autowired
    private IHandleMaterial materialservice;

    /**
     * service
     */
    @Autowired
    private IAuditPrintdocShenqing iAuditPrintdocShenqing;

    /**
     * 通用通知书service
     */
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IOuService iOUService;
    /**
     * 事项材料
     */
    @Autowired
    private IAuditTaskMaterial auditTaskMaterial;

    /**
     * 办件操作service
     */
    //TODO 暂缓
    //    private AuditProjectOperationService auditprojectoperationservice = new AuditProjectOperationService();
    /**
     * 申请通知书实体对象
     */
    private AuditPrintdocShenqing auditPrintdocShenqing = null;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 流程版本实例guid
     */
    private String processVersionInstanceGuid;

    /**
     * 办件实体bean
     */
    private AuditProject auditproject = null;

    /**
     * 事项实体bean
     */
    private AuditTask audittask = null;

    /**
     * 打印日期
     */
    private String dyrq;

    /**
     * 法定代表人(负责人)
     */
    private String legalrepresentative;

    /**
     * 部门名称
     */
    private String govname;

    private String govname1;

    /**
     * 身份证号码
     */
    private String IDNo;

    /**
     * 办理事项名称
     */
    private String sbsx;

    /**
     * 办号
     */
    private String txtbh = "";

    /**
     * 文号
     */
    private String wn;

    /**
     * 申请材料列表
     */
    private String txtmaterial;

    /**
     * 事项类别
     */
    private String tasktype;

    /**
     * 辖区标号
     */
    private String areacode;

    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private ICodeItemsService codeItemsService;

    @SuppressWarnings("unused")
    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        //processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        // 办件信息
        auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, null).getResult();
        // 事项信息
        audittask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());

        // 申请文书信息
        auditPrintdocShenqing = iAuditPrintdocShenqing.getDocShenqingByProjectGuid(projectguid).getResult();
        if (auditPrintdocShenqing == null) {
            auditPrintdocShenqing = new AuditPrintdocShenqing();
            auditPrintdocShenqing.setOperatedate(new Date());
            auditPrintdocShenqing.setOperateusername(userSession.getDisplayName());
            auditPrintdocShenqing.setContactphone(auditproject.getContactphone());
            auditPrintdocShenqing.setContactperson(auditproject.getContactperson());
            auditPrintdocShenqing.setContactmobile(auditproject.getContactmobile());
            auditPrintdocShenqing.setAddress(auditproject.getAddress());
            auditPrintdocShenqing.setContactemail(auditproject.getContactemail());
            auditPrintdocShenqing.setContactpostcode(auditproject.getContactpostcode());
            // 身份证号
            IDNo = StringUtil.isBlank(auditPrintdocShenqing.getRowguid())
                    ? (ZwfwConstant.CERT_TYPE_SFZ.equals(auditproject.getCerttype()) ? auditproject.getCertnum() : null)
                    : auditPrintdocShenqing.getIdno();
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditproject.getApplyertype().toString())) {
                String id = "";
                id = auditproject.getContactcertnum();
                IDNo = StringUtil.isBlank(auditPrintdocShenqing.getRowguid()) ? id : auditPrintdocShenqing.getIdno();
            }
            auditPrintdocShenqing.setIdno(IDNo);
        }

        if (!isPostback()) {
            // 日期
            dyrq = EpointDateUtil.convertDate2String(
                    auditPrintdocShenqing.getDyrq() == null ? new Date() : auditPrintdocShenqing.getDyrq(),
                    "yyyy年MM月dd日");

            // 法定代表人(负责人)
            legalrepresentative = StringUtil.isBlank(auditPrintdocShenqing.getLegal()) ? auditproject.getLegal()
                    : auditPrintdocShenqing.getLegal();
            FrameOu ou = iOUService.getOuByOuGuid(auditproject.getOuguid());
            String oushortname = "";
            if (ou != null) {
                oushortname = ou.getOuname();
            }
            if (StringUtil.isNotBlank(oushortname)) {
                govname = oushortname;
                govname1 = oushortname;
            } else {
                govname = audittask.getOuname();
                govname1 = audittask.getOuname();
            }
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditproject.getApplyertype().toString())) {
                addCallbackParam("cardtype", "联系人身份证：");
            }
            // 身份证号
            IDNo = StringUtil.isBlank(auditPrintdocShenqing.getRowguid())
                    ? (ZwfwConstant.CERT_TYPE_SFZ.equals(auditproject.getCerttype()) ? auditproject.getCertnum() : null)
                    : auditPrintdocShenqing.getIdno();
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditproject.getApplyertype().toString())) {
                String id = "";
                id = auditproject.getContactcertnum();
                IDNo = StringUtil.isBlank(auditPrintdocShenqing.getRowguid()) ? id : auditPrintdocShenqing.getIdno();
            }
            auditPrintdocShenqing.setIdno(IDNo);


            // 办件名称
            sbsx = StringUtil.isBlank(auditPrintdocShenqing.getRowguid()) ? auditproject.getProjectname()
                    : auditPrintdocShenqing.getTaskname();
            // 获取时间年份 ZJG201405140002 2014
            // 办件编号的前缀 ZJG
            String AS_FLOWSN_PRE = handleConfigService
                    .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String fyear = AS_FLOWSN_PRE + year; // LS2014

            // 文号 〔2015〕2号
            String orgwn = auditproject.getOrgwn();
            if (StringUtil.isBlank(orgwn)) {
                // 获取新文书号
                //TODO 暂缓
                int newbhinfo = auditProjectService
                        .getMaxOrgNumberinfo(null, projectguid, areacode, auditproject.getTask_id()).getResult();
                txtbh = String.valueOf(newbhinfo);
                String orgwnnew = "〔" + year + "〕" + String.valueOf(txtbh) + "号";
                auditproject.setOrgnumber(Integer.parseInt(txtbh));
                auditproject.setOrgwn(orgwnnew);
                auditProjectService.updateProject(auditproject);
            }

            // 获取更新文书号后办件信息
            String wninfo = auditproject.getOrgwn();// 〔2015〕2号
            String whnumber = wninfo.replace("〕", "〕第");// 〔2015〕第2号
            int numberbh = whnumber.indexOf("第") + 1;
            String numberwn = whnumber.substring(numberbh);// 2号
            int dwz = numberwn.indexOf("号");
            String numberwh = numberwn.substring(0, dwz);// 2
            wn = whnumber.substring(0, numberbh);// 〔2015〕第

            // 办号
            txtbh = StringUtil.isBlank(auditPrintdocShenqing.getRowguid()) ? numberwh : auditPrintdocShenqing.getBh();
            // 找出所有的材料
            String material = "";
            List<Record> projectMaterial = materialservice
                    .getProjectMaterial(auditproject.getRowguid(), auditproject.getBiguid()).getResult();
            if (projectMaterial != null) {
                projectMaterial.sort((c,
                                      d) -> (StringUtil.isNotBlank(getMaterialOrderNum(d.get("Taskmaterialguid"))) ? getMaterialOrderNum(d.get("Taskmaterialguid"))
                        : Integer.valueOf(0))
                        .compareTo(StringUtil.isNotBlank(getMaterialOrderNum(c.get("Taskmaterialguid")))
                                ? getMaterialOrderNum(c.get("Taskmaterialguid")) : 0));
            }
            if (projectMaterial != null && projectMaterial.size() > 0) {
                for (int i = 0; i < projectMaterial.size(); i++) {
                    int a = i + 1;
                    material += "&nbsp;&nbsp;&nbsp;&nbsp;" + a + "、" + projectMaterial.get(i).get("MATERIALNAME");
                    material += "；" + "<br>";
                }
            }

            txtmaterial = StringUtil.isBlank(auditPrintdocShenqing.getRowguid()) ? material
                    : auditPrintdocShenqing.getMaterials();
            //TODO 暂缓
            //            AuditProjectOperation auditprojectoperation = auditprojectoperationservice
            //                    .selectOperationByProjectGuidAndType(projectguid, ZwfwConstant.BANJIAN_STATUS_YJJ);
            //            if (auditprojectoperation != null) {
            //                updateSave();
            //            }

            //改为申请时间
            if (StringUtil.isNotBlank(auditproject.getApplydate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            }
            addViewData("txtmaterial", txtmaterial);// 材料
            addViewData("dyrq", dyrq);// 打印日期
        } else {
            if (StringUtil.isNotBlank(auditproject.getApplydate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            } else {
                dyrq = getViewData("dyrq");
            }
            txtmaterial = getViewData("txtmaterial");

        }
    }

    /**
     * 获取材料排序
     * [一句话功能简述]
     * [功能详细描述]
     *
     * @param Taskmaterialguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getMaterialOrderNum(String Taskmaterialguid) {
        AuditTaskMaterial taskMaterial = auditTaskMaterial.getAuditTaskMaterialByRowguid(Taskmaterialguid).getResult();
        return taskMaterial.getOrdernum();
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "pauditprintdocshenqing?projectguid=" + projectguid + "&processVersionInstanceGuid="
                + processVersionInstanceGuid + "&taskguid=" + audittask.getRowguid();
        addCallbackParam("msg", printaddress);

    }

    /**
     * 保存文书信息
     */
    protected void updateSave() {
        String wninfo = auditproject.getOrgwn();// auditproject.getOrgwn();// 〔2015〕2号
        int n = wninfo.indexOf("〔");
        String wnumber = wninfo.substring(n);// 〔2015〕2号
        int numberbh = wnumber.indexOf("〕") + 1;//
        String orgwnnew;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";// 〔2015〕2号
        // 身份证号
        IDNo = StringUtil.isBlank(auditPrintdocShenqing.getRowguid())
                ? (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_SFZ) ? auditproject.getCertnum() : null)
                : auditPrintdocShenqing.getIdno();
        // 根据ProjectGuid判断是否存在申请通知书
        if (StringUtil.isNotBlank(auditPrintdocShenqing.getRowguid())) {
            auditPrintdocShenqing.setTaskname(sbsx);
            auditPrintdocShenqing.setLegal(auditproject.getLegal());
            auditPrintdocShenqing.setMaterials(txtmaterial);
            String str = dyrq.replace("年", "-");
            str = str.replace("月", "-");
            str = str.replace("日", "");
            auditPrintdocShenqing.setDyrq(EpointDateUtil.convertString2Date(str, "yyyy-MM-dd"));
            auditPrintdocShenqing.setBh(txtbh);
            auditPrintdocShenqing.setOperatedate(new Date());
            auditPrintdocShenqing.setOperateusername(userSession.getDisplayName());
            iAuditPrintdocShenqing.update(auditPrintdocShenqing);
        } else {
            auditPrintdocShenqing.setRowguid(UUID.randomUUID().toString());
            auditPrintdocShenqing.setProjectguid(projectguid);
            auditPrintdocShenqing.setTaskname(sbsx);
            auditPrintdocShenqing.setLegal(auditproject.getLegal());
            auditPrintdocShenqing.setMaterials(txtmaterial);
            String str = dyrq.replace("年", "-");
            str = str.replace("月", "-");
            str = str.replace("日", "");
            auditPrintdocShenqing.setDyrq(EpointDateUtil.convertString2Date(str, "yyyy-MM-dd"));
            auditPrintdocShenqing.setBh(txtbh);
            iAuditPrintdocShenqing.add(auditPrintdocShenqing);

        }
        // auditproject.setOrgnumber(Integer.parseInt(txtbh));
        auditproject.setOrgwn(orgwnnew);
        auditProjectService.updateProject(auditproject);
    }

    public AuditPrintdocShenqing getAuditprintdocShenqing() {
        return auditPrintdocShenqing;
    }

    public void setAuditprintdocShenqing(AuditPrintdocShenqing auditprintdocShenqing) {
        this.auditPrintdocShenqing = auditprintdocShenqing;
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public String getGovname() {
        return govname;
    }

    public void setGovname(String govname) {
        this.govname = govname;
    }

    public String getTxtbh() {
        return txtbh;
    }

    public void setTxtbh(String txtbh) {
        this.txtbh = txtbh;
    }

    public String getWn() {
        return wn;
    }

    public void setWn(String wn) {
        this.wn = wn;
    }

    public String getGovname1() {
        return govname1;
    }

    public void setGovname1(String govname1) {
        this.govname1 = govname1;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
    }

    public String getTxtmaterial() {
        return txtmaterial;
    }

    public void setTxtmaterial(String txtmaterial) {
        this.txtmaterial = txtmaterial;
    }

    public String getLegalrepresentative() {
        return legalrepresentative;
    }

    public void setLegalrepresentative(String legalrepresentative) {
        this.legalrepresentative = legalrepresentative;
    }

    public String getIDNo() {
        return IDNo;
    }

    public void setIDNo(String iDNo) {
        IDNo = iDNo;
    }

    public String getProjectguid() {
        return projectguid;
    }

    public void setProjectguid(String projectguid) {
        this.projectguid = projectguid;
    }

    public String getDyrq() {
        return dyrq;
    }

    public void setDyrq(String dyrq) {
        this.dyrq = dyrq;
    }

    public String getProcessVersionInstanceGuid() {
        return processVersionInstanceGuid;
    }

    public void setProcessVersionInstanceGuid(String processVersionInstanceGuid) {
        this.processVersionInstanceGuid = processVersionInstanceGuid;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

}
