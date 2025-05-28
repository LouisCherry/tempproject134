package com.epoint.zwfw.dj.controller;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.*;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handleproject.service.TAHandleProjectService;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.sghd.auditjianguan.impl.GxhAuditJianguanService;
import com.epoint.zwfw.dj.api.IJnDjService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auditchaoxiancontroller")
public class AuditProjectChaoXianController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditProject projectService;
    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;
    @Autowired
    private IAuditOrgaServiceCenter serviceCenter;
    @Autowired
    private IAuditOrgaWindow windowService;
    @Autowired
    private IConfigService iconfigservice;
    @Autowired
    private IUserService iuserservice;
    GxhAuditJianguanService gxhauditjianguanservice = new GxhAuditJianguanService();

    /**
     * 市畜局办件基本信息对接
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/receiveProjectInfo", method = RequestMethod.POST)
    public String ReceiveProjectInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用receiveProjectInfo接口======");

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            //校验token
            if (!(ZwdtConstant.SysValidateData.equals(token))) {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
            //获取参数
            JSONObject obj = JSONObject.parseObject(jsonObject.getString("params"));
            //时间日期的判断
            String[] dateary = {"applydate", "receivedate", "acceptuserdate", "banwandate", "banjiedate"};
            for (String datefield : dateary) {
                //入参
                Date datestr;
                if (obj.containsKey(datefield)) {
                    try {
                        datestr = EpointDateUtil.convertString2Date(obj.getString(datefield), EpointDateUtil.DATE_TIME_FORMAT);
                    } catch (Exception e) {
                        return JsonUtils.zwdtRestReturn("0", datefield + "字段不满足时间格式：yyyy-MM-dd HH:mm:ss！", "");
                    }
                }
            }
            //非空的判断
            String[] isnullary = {"applydate", "applytype", "applyername", "certnum", "banjiedate", "banjieusername"};
            for (String isnullfield : isnullary) {
                String str = obj.getString(isnullfield);
                //入参
                if (StringUtil.isBlank(str)) {
                    return JsonUtils.zwdtRestReturn("0", isnullfield + "字段不允许为空！", "");
                }
            }
            String chaoxiantaskid = iconfigservice.getFrameConfigValue("chaoxiantaskid");
            AuditTask auditTask = auditTaskService.getUseTaskAndExtByTaskid(chaoxiantaskid).getResult();
            if (auditTask == null) {
                log.info("chaoxiantaskid:" + chaoxiantaskid);
                return JsonUtils.zwdtRestReturn("0", "事项未找到!", "");
            }
            AuditProject auditProject = new AuditProject();
            String projectGuid = UUID.randomUUID().toString();
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditProject.setOperateusername("超限办件生成接口：auditchaoxiancontroller");
            auditProject.setOperatedate(new Date());
            auditProject.setRowguid(projectGuid);
            //生成个性化流水号
            String numberFlag = "CX";
            auditProject.setFlowsn(getStrFlowSn("办件编号", numberFlag, 4, 4));
            if (auditTask != null) {
                auditProject.setTask_id(auditTask.getTask_id());
                auditProject.setTaskguid(auditTask.getRowguid());
                auditProject.setOuguid(auditTask.getOuguid());
                auditProject.setOuname(auditTask.getOuname());
                auditProject.setProjectname(auditTask.getTaskname());
                auditProject.setIs_charge(auditTask.getCharge_flag());
                auditProject.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                // 中心
                //@author fryu 2023年8月22日 修改查找数据方法
                AuditOrgaServiceCenter center = null;
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("belongxiaqu", auditTask.getAreacode());
                List<AuditOrgaServiceCenter> centerList = serviceCenter.getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
                if (centerList != null && !centerList.isEmpty()) {
                    center = centerList.get(0);
                }
                AuditOrgaWindow auditorgawindow = null;
                // 窗口
                if (center != null) {
                    auditProject.setCenterguid(center.getRowguid());
                    List<AuditOrgaWindow> windows = windowService.getWindowListByTaskId(auditTask.getTask_id()).getResult();
                    if (CollectionUtils.isNotEmpty(windows)) {
                        auditorgawindow = windows.get(0);
                        auditProject.setWindowguid(auditorgawindow.getRowguid());
                        auditProject.setWindowname(auditorgawindow.getWindowname());
                    }
                }
                auditProject.setAreacode(auditTask.getAreacode());
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                if (auditTaskExtension != null) {
                    auditProject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditProject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());// 进驻大厅
                    auditProject.setIf_express(auditTaskExtension.getIf_express());
                }
                auditProject.setTasktype(auditTask.getType());
                auditProject.setPromise_day(auditTask.getPromise_day());
//                // 企业类型
//                if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditTask.getApplyertype())) {
//                    auditProject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));
//                    auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);// 申请人证照类型：组织机构代码
//                } else {
//                    // 个人类型
//                    auditProject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));
//                    auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
//                }
            }
            auditProject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            //申报方式
            auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));// 办件申请方式：网上直接申报
            //申报时间
            auditProject.setApplydate(obj.getDate("applydate"));// 办件申请时间
            //申请人类型 10:法人 20个人
            String applytype = obj.getString("applytype");
            auditProject.setApplyertype(Integer.valueOf(applytype));
            //申请人
            auditProject.setApplyername(obj.getString("applyername"));
            //申请人证照编号
            auditProject.setCertnum(obj.getString("certnum"));
            //申请人证照类型  代码项：申请人用来唯一标识的证照类型
            auditProject.setCerttype(obj.getString("certtype"));
            //地址
            auditProject.setAddress(obj.getString("address"));
            //法人代表身份证
            auditProject.setLegalid(obj.getString("legalid"));
            //法人代表
            //法人代表身份证
            auditProject.setLegal(obj.getString("legal"));
            //联系人身份证
            auditProject.setContactcertnum(obj.getString("contactcertnum"));
            //联系人
            auditProject.setContactperson(obj.getString("contactperson"));
            //联系电话
            auditProject.setContactphone(obj.getString("contactphone"));
            //手机
            auditProject.setContactmobile(obj.getString("contactmobile"));
            //邮编
            auditProject.setContactpostcode(obj.getString("contactpostcode"));
            //传真
            auditProject.setContactfax(obj.getString("contactfax"));
            //电子邮件
            auditProject.setContactemail(obj.getString("contactemail"));
            //备注
            auditProject.setRemark(obj.getString("remark"));
            //跳转三方的预览地址
            auditProject.set("chaoxiandetailurl", obj.getString("chaoxiandetailurl"));
            //进行接件、受理、审核、办结的数据
            //接件
            auditProject.setReceivedate(obj.getDate("receivedate"));
            auditProject.setReceiveusername(obj.getString("receiveusername"));
//            if(StringUtil.isNotBlank(obj.getString("receiveusername"))){
//                FrameUser frameuser = iuserservice.getUserByUserField("displayName", obj.getString("receiveusername"));
//                if (frameuser != null) {
//                    auditProject.setReceiveuserguid(frameuser.getUserGuid());
//                }
//            }
            //受理
            auditProject.setAcceptuserdate(obj.getDate("acceptuserdate"));
            auditProject.setAcceptusername(obj.getString("acceptusername"));
            //审核
            auditProject.setBanwandate(obj.getDate("banwandate"));
            //办结
            auditProject.setBanjiedate(obj.getDate("banjiedate"));
            auditProject.setBanjieusername(obj.getString("banjieusername"));
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);// 办件状态：正常办结

            //增加推送省办件归集逻辑
            auditProject.set("is_lczj","2");
            auditProject.set("zjsync",0);

            projectService.addProject(auditProject).getResult();
            //插入监管信息
            Record record = new Record();
            record.set("rowguid", UUID.randomUUID().toString());
            record.set("projectguid", auditProject.getRowguid());
            record.set("flowsn", auditProject.getFlowsn());
            record.set("projectname", auditProject.getProjectname());
            record.set("ouname", auditProject.getOuname());
            record.set("applyername", auditProject.getApplyername());
            record.set("banjiedate", auditProject.getBanjiedate());
            record.set("renlingtype", "0");
            record.setSql_TableName("audit_jianguan");
            record.setPrimaryKeys("rowguid");
            gxhauditjianguanservice.insertauditjianguan(record);
            return JsonUtils.zwdtRestReturn("1", "办件数据接收成功！", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("======调用receiveProjectInfo接口失败！======");
            return JsonUtils.zwdtRestReturn("0", "办件数据接收失败！", "");
        } finally {
            log.info("======调用receiveProjectInfo接口结束======");
        }

    }

    private String getStrFlowSn(String numberName, String numberFlag, int theYearLength, int snLength) {
        String flowSn = "";
        Object[] args = new Object[7];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));

        args[3] = Integer.valueOf(theYearLength);

        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);

        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));

        args[6] = Integer.valueOf(snLength);

        try {
            flowSn = CommonDao.getInstance("common")
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn", args).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }
}
