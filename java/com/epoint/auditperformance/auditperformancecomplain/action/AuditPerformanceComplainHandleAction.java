package com.epoint.auditperformance.auditperformancecomplain.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancecomplain.api.IAuditPerformanceComplain;
import com.epoint.basic.auditperformance.auditperformancecomplain.domain.AuditPerformanceComplain;
import com.epoint.basic.auditperformance.auditperformancecomplainr.api.IAuditPerformanceComplainR;
import com.epoint.basic.auditperformance.auditperformancecomplainr.domain.AuditPerformanceComplainR;
import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancedetail.inter.IAuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 考评申诉记录新增页面对应的后台
 *
 * @version [版本号, 2018-01-17 09:15:19]
 */
@RestController("auditperformancecomplainhandleaction")
@Scope("request")
public class AuditPerformanceComplainHandleAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -6044446141952609672L;
    @Autowired
    private IAuditPerformanceComplain complainService;
    /**
     * 考评申诉记录实体对象
     */
    private AuditPerformanceComplain dataBean = null;

    private AuditPerformanceAccount performanceAccount;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceDetail> model;

    @Autowired
    private IAuditPerformanceAccount performanceAccountService;

    @Autowired
    private IAuditPerformanceDetail performanceDetailService;

    @Autowired
    private IAuditPerformanceComplainR relationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    IMessagesCenterService messageCenterService;

    @Autowired
    IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    IAuditPerformanceRecord performanceRecordService;
    
    @Autowired
    IAuditPerformanceRecordObject objectService;

    private String complainguid;


    /**
     * 材料model
     */
    private FileUploadModel9 templateAttachUploadModel;
    
    @Override
    public void pageLoad() {
        String accoutguid = getRequestParameter("accountguid");
        complainguid = getRequestParameter("complainguid");
        if (StringUtil.isBlank(complainguid) && StringUtil.isNotBlank(getViewData("complainguid"))) {
            complainguid = getViewData("complainguids");
        }
        performanceAccount = performanceAccountService.findDetailByRowguid(accoutguid).getResult();
        if (performanceAccount == null) {
            performanceAccount = new AuditPerformanceAccount();
        }
        if (StringUtil.isNotBlank(complainguid)) {
            dataBean = complainService.getPerformanceComplainByGuid(complainguid).getResult();
        }
        if (dataBean == null) {
            dataBean = new AuditPerformanceComplain();
            if (StringUtil.isBlank(getViewData("Complaincliengguid"))) {
                addViewData("Complaincliengguid", UUID.randomUUID().toString());
            }
            dataBean.setComplaincliengguid(getViewData("Complaincliengguid"));
        }
        if (StringUtil.isBlank(dataBean.getComplainusername())) {
            dataBean.setComplainusername(userSession.getDisplayName());
        }
        if (StringUtil.isNotBlank(performanceAccount.getRecordrowguid())) {
            AuditPerformanceRecord performanceRecord = performanceRecordService
                    .getPerformanceRecordByGuid(performanceAccount.getRecordrowguid()).getResult();
            if (performanceRecord != null && "3".equals(performanceRecord.getStatus())) {
                addCallbackParam("error", "考评记录已归档，不能进行申诉处理！");
            }
        }
        addCallbackParam("processstatus", dataBean.getProcessstatus());
    }

    public DataGridModel<AuditPerformanceDetail> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceDetail>()
            {

                @Override
                public List<AuditPerformanceDetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    if (ZwfwConstant.COMPLAINSTATUS_SSZ.equals(dataBean.getProcessstatus())
                            || ZwfwConstant.COMPLAINSTATUS_YCL.equals(dataBean.getProcessstatus())) {
                        List<AuditPerformanceDetail> auditPerformanceDetails = performanceDetailService
                                .selectDetailByComplainguid(dataBean.getRowguid()).getResult();
                        if (ZwfwConstant.COMPLAINSTATUS_YCL.equals(dataBean.getProcessstatus())) {
                            for (AuditPerformanceDetail auditPerformanceDetail : auditPerformanceDetails) {
                                if (ZwfwConstant.CONSTANT_STR_ONE
                                        .equals(auditPerformanceDetail.get("complainresult"))) {
                                    auditPerformanceDetail.set("ispass", "是");
                                }
                                else if ("2".equals(auditPerformanceDetail.get("complainresult"))) {
                                    auditPerformanceDetail.set("ispass", "否");
                                }
                            }
                        }
                        this.setRowCount(auditPerformanceDetails.size());
                        return auditPerformanceDetails;
                    }
                    else {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("a.recordrowguid", performanceAccount.getRecordrowguid());
                        sql.eq("a.recorddetailrulerowguid", performanceAccount.getRecorddetailrulerowguid());
                        sql.eq("a.objectguid", performanceAccount.getObjectguid());
                        sql.eq("usable", ZwfwConstant.CONSTANT_STR_ONE);
                        PageData<AuditPerformanceDetail> pagedata = performanceDetailService
                                .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                        if (StringUtil.isNotBlank(complainguid)) {
                            List<AuditPerformanceComplainR> complainRS = relationService
                                    .selectRelationByComplainGuid(complainguid).getResult();
                            if (complainRS != null && complainRS.size() > 0) {
                                Set<String> detailRowguids = new HashSet<>();
                                for (AuditPerformanceComplainR complainR : complainRS) {
                                    detailRowguids.add(complainR.getDetailrowguid());
                                }
                                for (AuditPerformanceDetail auditPerformanceDetail : pagedata.getList()) {
                                    if (detailRowguids.contains(auditPerformanceDetail.getRowguid())) {
                                        auditPerformanceDetail.set("checked", "true");
                                    }
                                }
                            }
                        }
                        this.setRowCount(pagedata.getRowCount());
                        return pagedata.getList();
                    }
                }
            };
        }
        return model;
    }

    public void save(String sub) {
        dataBean.setComplaintime(new Date());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        if (StringUtil.isBlank(dataBean.getRowguid())) {
            //新增
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setAccountrowguid(performanceAccount.getRowguid());
            dataBean.setComplainuserguid(userSession.getUserGuid());
            dataBean.setComplainusername(userSession.getDisplayName());
            dataBean.setComplainouguid(userSession.getOuGuid());
            dataBean.setComplainouname(userSession.getOuName());
            if (StringUtil.isBlank(dataBean.getProcessstatus())) {
                dataBean.setProcessstatus(ZwfwConstant.COMPLAINSTATUS_CG);
            }
            dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            complainService.insertPerformanceComplain(dataBean);
            addViewData("complainguid", dataBean.getRowguid());
        }
        else {
            //更新
            complainService.updatePerformanceComplain(dataBean);
        }

        relationService.deleteRelationByAccountGuid(dataBean.getRowguid());
        List<String> select = getDataGridData().getSelectKeys();
        AuditPerformanceDetail detail;
        if (select != null && select.size() > 0) {
            for (String sel : getDataGridData().getSelectKeys()) {
                AuditPerformanceComplainR auditPerformanceComplainR = new AuditPerformanceComplainR();
                auditPerformanceComplainR.setRowguid(UUID.randomUUID().toString());
                auditPerformanceComplainR.setComplainaccountrowguid(dataBean.getRowguid());
                auditPerformanceComplainR.setDetailrowguid(sel);
                auditPerformanceComplainR.setOperatedate(new Date());
                auditPerformanceComplainR.setOperateusername(userSession.getDisplayName());
                relationService.insertPerformanceComplainR(auditPerformanceComplainR);
                //如果点击提交申诉则添加日志
                if("sub".equals(sub)){                    
                    detail = performanceDetailService.findDetailByRowguid(sel).getResult();
                    SimpleDateFormat sfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sfg.format(new Date());
                    String updaterecord = "【" + userSession.getDisplayName() + "】于" + date + "对记录进行了申诉" + "<br/>";
                    detail.setUpdaterecord(
                            (detail.getUpdaterecord() == null ? "" : detail.getUpdaterecord()) + updaterecord);
                    performanceDetailService.updata(detail);
                }
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

 
    public void submit() {
        if (dataBean.getInt("frequence") == null) {
            dataBean.setFrequence(ZwfwConstant.CONSTANT_INT_ONE);
        }
        else {
            dataBean.setFrequence(dataBean.getFrequence() + 1);
        }
        dataBean.setProcessstatus(ZwfwConstant.COMPLAINSTATUS_SSZ);
        save("sub");

        // 1、获取角色标识
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "考核管理员");
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            String ouGuid = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getOuguid();
            // 2、获取该角色的对应的人员
            List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false, 3);
            if (listUser != null && listUser.size() > 0) {
                // 3、发送待办给审核人员
                for (FrameUser frameUser : listUser) {
                    // 待办名称
                    String title = "【" + userSession.getDisplayName() + "】" + "考评申诉";
                    // 处理页面
                    String handleurl = "epointperformance/auditperformancecomplain/auditperformancecomplainhandle?accountguid="
                            + performanceAccount.getRowguid() + "&complainguid=" + dataBean.getRowguid();
                    String messageItemGuid = UUID.randomUUID().toString();
                    messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                            IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                            frameUser.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "考评申诉",
                            handleurl, frameUser.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                            dataBean.getRowguid(), "", new Date(), "", frameUser.getUserGuid(), "", "");
                }
            }
        }
        addCallbackParam("msg", "申诉提交成功！");
    }

    public void handleComplain() {
        DataGridModel<AuditPerformanceDetail> gridModel = getDataGridData();
        List<AuditPerformanceDetail> auditPerformanceDetails = gridModel.getWrappedData();
        if (auditPerformanceDetails != null && auditPerformanceDetails.size() > 0) {
            List<String> select = getDataGridData().getSelectKeys();
            if (select == null) {
                select = new ArrayList<>();
            }
            String status;
            for (AuditPerformanceDetail auditPerformanceDetail : auditPerformanceDetails) {
                if (select.contains(auditPerformanceDetail.getRowguid())) {
                    //更新关系表状态为审核通过
                    relationService.updateComplainresult(dataBean.getRowguid(), auditPerformanceDetail.getRowguid(),
                            true);
                    //更新明细为不可用
                    auditPerformanceDetail.setUsable(ZwfwConstant.CONSTANT_STR_ZERO);
                    performanceDetailService.updata(auditPerformanceDetail);
                    //计算汇总分
                    status = "通过申诉";
                    //重新结算个人成绩
                    String accoutguid = getRequestParameter("accountguid");
                    AuditPerformanceAccount account = performanceAccountService.findDetailByRowguid(accoutguid).getResult();
                    performanceAccountService.doAccountByObject(account.getRecordrowguid(),objectService.getDetailByObjectGuid(account.getObjectguid()).getResult());        
                }
                else {
                    //更新关系表状态为审核不通过
                    status = "拒绝申诉";
                    relationService.updateComplainresult(dataBean.getRowguid(), auditPerformanceDetail.getRowguid(),
                            false);
                }
                //添加日志
                SimpleDateFormat sfg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sfg.format(new Date());
                String updaterecord = "【" + userSession.getDisplayName() + "】于" + date + status + "<br/>";
                auditPerformanceDetail.setUpdaterecord(
                        (auditPerformanceDetail.getUpdaterecord() == null ? "" : auditPerformanceDetail.getUpdaterecord()) + updaterecord);
                performanceDetailService.updata(auditPerformanceDetail);
            }
        }

        dataBean.setHandleuserguid(userSession.getUserGuid());
        dataBean.setHandleusername(userSession.getDisplayName());
        dataBean.setHandleouguid(userSession.getOuGuid());
        dataBean.setHandleouname(userSession.getOuName());
        dataBean.setHandletime(new Date());
        dataBean.setProcessstatus(ZwfwConstant.COMPLAINSTATUS_YCL);
        complainService.updatePerformanceComplain(dataBean);
        
 
        // 1、获取角色标识
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "考核管理员");
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            String ouGuid = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getOuguid();
            // 2、获取该角色的对应的人员
            List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false, 3);
            if (listUser != null && listUser.size() > 0) {
                // 3、删除待办
                for (FrameUser frameUser : listUser) {
                    messageCenterService.deleteMessageByIdentifier(dataBean.getRowguid(), frameUser.getUserGuid());
                }
            }
        }

        addCallbackParam("msg", "处理成功！");
    }

    /**
     * 保存并关闭
     */
    public void add() {

    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditPerformanceComplain();
    }


    public FileUploadModel9 getComplaincAttachModel() {
        if (templateAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    templateAttachUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {

                    return true;
                }

            };
            templateAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getComplaincliengguid(), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return templateAttachUploadModel;
    }
    
    public AuditPerformanceComplain getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceComplain();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceComplain dataBean) {
        this.dataBean = dataBean;
    }

    public AuditPerformanceAccount getPerformanceAccount() {
        return performanceAccount;
    }

    public void setPerformanceAccount(AuditPerformanceAccount performanceAccount) {
        this.performanceAccount = performanceAccount;
    }
}
