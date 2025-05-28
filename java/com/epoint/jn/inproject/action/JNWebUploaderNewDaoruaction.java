package com.epoint.jn.inproject.action;

import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.jn.externalprojectinfo.api.IExternalProjectInfoService;
import com.epoint.jn.externalprojectinfo.api.entity.ExternalProjectInfo;
import com.epoint.jn.externalprojectinfoext.api.IExternalProjectInfoExtService;
import com.epoint.jn.externalprojectinfoext.api.entity.ExternalProjectInfoExt;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController("jnwebuploadernewdaoruaction")
@Scope("request")
public class JNWebUploaderNewDaoruaction extends BaseController {

    private static final long serialVersionUID = 1L;
    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private DataGridModel<Record> model = null;

    private Logger logger = Logger.getLogger(JNWebUploaderNewDaoruaction.class);

    @Autowired
    private IWebUploaderService service;
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IConfigService configservice;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IExternalProjectInfoService externalProjectInfoService;

    @Autowired
    private IExternalProjectInfoExtService externalProjectInfoExtService;

    public void pageLoad() {
    }

    @SuppressWarnings("deprecation")
    public DataImportModel9 getDataImportModel() {
        String userguid = userSession.getUserGuid();
        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler() {
                private static final long serialVersionUID = 1L;

                /**
                 * 此方法会将excel表格数据一行一行处理 每行数据都进入该方法，动态加载到list中
                 *
                 * @param filename
                 *            导入的excel名字
                 * @param sheetName
                 *            sheet名字
                 * @param sheet
                 *            第几个sheet
                 * @param curRow
                 *            第几行数据
                 * @param totalRows
                 *            总共多少行
                 * @param data
                 *            数据
                 * @return String 保存信息(如果失败,那么返回失败提示信息,否则返回null)
                 */
                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                                            Object[] data) {
                    Map<String, Object> map = new HashMap<>();
                    String message = null;
                    String project_giud = "";
                    String project_ext_giud = "";
                    String baseinfo_giud = "";
                    String process_giud = "";
                    String done_giud = "";
                    String process1_giud = "";
                    try {
                        EpointFrameDsManager.begin(null);
                        // lcproject project = new lcproject();
                        ExternalProjectInfo project = new ExternalProjectInfo();
                        ExternalProjectInfoExt project_ext = new ExternalProjectInfoExt();
                        project.setRowguid(UUID.randomUUID().toString());
                        project_ext.setRowguid(UUID.randomUUID().toString());
                        project_ext.setProject_guid(project.getRowguid());
                        if (totalRows > 2000) {
                            return "一次性导入的办件数量不能超过2000！";
                        }
                        String nowareacode = ZwfwUserSession.getInstance().getAreaCode();
                        // 通过辖区获取系统参数配置数量
                        String areacount = configservice
                                .getFrameConfigValue("导入_" + ZwfwUserSession.getInstance().getAreaCode());
                        int intareacount = Integer.parseInt(areacount);
                        // 查询今日已导入量
                        int incount = service.getNewInCountByAreacode(ZwfwUserSession.getInstance().getAreaCode());
                        if (incount + totalRows - 1 >= intareacount) {
                            int returncouont = intareacount - incount;
                            return "今日已导入 " + incount + " 件办件，还可导入 " + returncouont + " 件办件";
                        }
                        // 默认导入sheet1的内容
                        if (sheet == 0) {
                            if (curRow == 1) {
                                // 默认excel的第一行为datagrid的列名
                                for (int i = 0; i < data.length; i++) {
                                    struList.add(data[i].toString());
                                }
                            }
                            if (curRow > 1) {
                                String areacode = "";
                                String ouguid = "";
                                String taskname = "";
                                AuditTask task = null;

                                for (int i = 0; i < data.length; i++) {
                                    map.put(struList.get(i) + "", data[i].toString());
                                    String key = struList.get(i) + "";
                                    String value = data[i].toString();
                                    switch (key) {
                                        case "受理人":
                                            project_ext.setAccept_user(value);
                                            break;
                                        case "受理单位":
                                            FrameOu frameou = service.getFrameOuByOunameNew(value, nowareacode);
                                            if (frameou != null) {
                                                project.setAccept_ou_guid(frameou.getOuguid());
                                                project_ext.setAccept_ou(frameou.getOuname());
                                                project_ext.setAccept_ou_guid(frameou.getOuguid());
                                                ouguid = frameou.getOuguid();
                                            } else {
                                                message = "请检查部门名称是否与系统中部门名称匹配";
                                            }
                                            break;
                                        case "所属区县":
                                            areacode = iCodeItemsService.getItemTextByCodeName("办件导入辖区对应关系", value);
                                            if (StringUtil.isBlank(areacode)) {
                                                message = "请检查所属区县与系统的辖区对应关系是否匹配";
                                            }
                                            project.setAreacode(areacode);
                                            project_ext.setAreacode(areacode);

                                            break;
                                        case "事项名称":
                                            task = service.getAuditTaskByTaskname(value, areacode, ouguid);
                                            taskname = value;
                                            project_ext.setTask_name(value);
                                            if (task != null) {
                                                project.setTask_guid(task.getRowguid());
                                                project.setTask_id(task.getTask_id());
                                                // 来源（外网还是其他系统）
                                                String resource = "1";
                                                // 获取事项ID
                                                String unid = zhenggaiImpl.getunidbyTaskid(task.getTask_id());
                                                // 请求接口获取受理编码 //FIXME 111
                                                if (StringUtil.isNotBlank(unid)) {
                                                    String result = FlowsnUtil.createReceiveNum(unid, task.getRowguid());
                                                    if (!"error".equals(result)) {
                                                        log.info("========================>获取受理编码成功！" + result);
                                                        project_ext.setFlowsn(result);
                                                    } else {
                                                        log.info("========================>获取受理编码失败！");
                                                    }
                                                }
                                            } else {
                                                message = "请检查事项所属的部门、辖区、事项名称是否与系统事项库匹配";
                                            }
                                            break;

                                        case "联系人":
                                            project_ext.setLink_user(value);
                                            break;
                                        case "联系电话":
                                            project_ext.setLink_phone(value);
                                            break;
                                        case "承诺时间":
                                            if (StringUtil.isNotBlank(value)) {
                                                project_ext.setPromise_date(value);
                                            }
                                            break;
                                        case "申请人或申请单位名称":
                                            project_ext.setApply_obj(value);
                                            project_ext.setApplyer_type(20);
                                            break;
                                        case "身份证号":
                                            project_ext.setCert_type("22");
                                            project_ext.setApply_id(value);
                                            break;
                                        case "统一社会信用代码":
                                            if (StringUtil.isNotBlank(value)) {
                                                project_ext.setApplyer_type(10);
                                                project_ext.setCert_type("16");
                                                project_ext.setApply_id(value);
                                            }
                                            break;
                                        case "组织机构代码":
                                            if (StringUtil.isNotBlank(value)) {
                                                project_ext.setApplyer_type(10);
                                                project_ext.setCert_type("14");
                                                project_ext.setApply_id(value);
                                            }
                                            break;
                                        case "法定代表人姓名":
                                            project_ext.setLegal_repr(value);
                                            break;

                                        case "联系地址":
                                            project_ext.setLink_addr(value);
                                            break;
                                    }
                                }

                                project.setAccept_date(new Date());
                                project_ext.setAccept_date(new Date());
                                project_ext.setApplydate(new Date());
                                project.setComplete_date(new Date());
                                project_ext.setComplete_date(new Date());
                                project_ext.setTasktype(1);
                                project.setOperatedate(new Date());
                                project_ext.setIs_lczj("9");
                                project_ext.setStatus(90);
                                project_ext.setBanjieresult(40);
                                // service.insert(project);
                                // 按照受理时间的月份插入分表
                                int externalProjectInfoInsert = externalProjectInfoService.insert(project);

                                int externalProjectInfoExtInsert = externalProjectInfoExtService.insert(project_ext);

                                if (project != null && project_ext != null) {
                                    log.info("办件导入：" + project_ext.getFlowsn());
                                    String Orgbusno = UUID.randomUUID().toString();
                                    eajcstepbasicinfogt baseinfo = new eajcstepbasicinfogt();
                                    FrameOu frameOu = service.getFrameOuByOunameNew(project_ext.getAccept_ou(),
                                            nowareacode);
                                    baseinfo.set("rowguid", Orgbusno);
                                    baseinfo.setOrgbusno(Orgbusno);
                                    baseinfo.setProjid(project_ext.getFlowsn());
                                    baseinfo.setProjpwd(project.getStr("pwd"));
                                    baseinfo.setValidity_flag("1");
                                    baseinfo.setDataver("1");
                                    baseinfo.setStdver("1");
                                    baseinfo.setItemno(task.getStr("inner_code"));
                                    List<AuditTaskMaterial> materials = iAuditTaskMaterial
                                            .getUsableMaterialListByTaskguid(task.getRowguid()).getResult();
                                    String materialname = "";
                                    if (materials != null && materials.size() > 0) {
                                        for (AuditTaskMaterial material : materials) {
                                            materialname += "[纸质]" + material.getMaterialname() + ";";
                                        }
                                    } else {
                                        materialname = "无";
                                    }
                                    baseinfo.set("ACCEPTLIST", materialname);
                                    String shenpilb = "";
                                    if ("11".equals(task.getShenpilb())) {
                                        shenpilb = "20";
                                    } else if ("10".equals(task.getShenpilb())) {
                                        shenpilb = "10";
                                    } else if ("07".equals(task.getShenpilb())) {
                                        shenpilb = "07";
                                    } else if ("06".equals(task.getShenpilb())) {
                                        shenpilb = "08";
                                    } else if ("05".equals(task.getShenpilb())) {
                                        shenpilb = "05";
                                    } else if ("02".equals(task.getShenpilb())) {
                                        shenpilb = "02";
                                    } else if ("01".equals(task.getShenpilb())) {
                                        shenpilb = "01";
                                    } else if ("03".equals(task.getShenpilb())) {
                                        shenpilb = "03";
                                    } else if ("04".equals(task.getShenpilb())) {
                                        shenpilb = "04";
                                    } else if ("08".equals(task.getShenpilb())) {
                                        shenpilb = "09";
                                    } else if ("09".equals(task.getShenpilb())) {
                                        shenpilb = "06";
                                    }

                                    baseinfo.set("ITEMTYPE", shenpilb);
                                    baseinfo.set("CATALOGCODE", task.getStr("CATALOGCODE"));
                                    baseinfo.set("TASKCODE", task.getStr("TASKCODE"));
                                    //企业法人
                                    if (10 == project_ext.getApplyer_type()) {
                                        baseinfo.set("APPLYERTYPE", "2");
                                        baseinfo.set("ApplyerPageType","01");
                                    } else {
                                        baseinfo.set("APPLYERTYPE", "1");
                                        baseinfo.set("ApplyerPageType","111");
                                    }
                                    //申请人证件号码
                                    baseinfo.set("ApplyerPageCode",project_ext.getApply_id());
                                    baseinfo.setItemname(taskname);
                                    baseinfo.setProjectname(project_ext.getTask_name());
                                    baseinfo.setApplicant(project_ext.getApply_obj());
                                    baseinfo.setApplicantCardCode(project_ext.getApply_id());
                                    baseinfo.setApplicanttel(project_ext.getLink_phone());
                                    baseinfo.setAcceptdeptid(frameOu.getOucode());
                                    baseinfo.setRegion_id(task.getAreacode().replace("370882", "370812") + "000000");
                                    baseinfo.setAcceptdeptname(frameOu.getOuname());
                                    baseinfo.setApprovaltype("2");
                                    baseinfo.setPromisetimelimit("0");
                                    baseinfo.setPromisetimeunit("1");
                                    baseinfo.setTimelimit("0");
                                    baseinfo.setItemregionid(project.getAreacode().replace("370882", "370812"));
                                    if ("22".equals(project_ext.getCert_type())) {
                                        baseinfo.setApplicantCardtype("111");
                                    } else if ("16".equals(project_ext.getCert_type())) {
                                        baseinfo.setApplicantCardtype("01");
                                    } else if ("14".equals(project_ext.getCert_type())) {
                                        baseinfo.setApplicantCardtype("02");
                                    }
                                    baseinfo.setSubmit("1");
                                    baseinfo.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.set("Status", "0");
                                    if (StringUtil.isNotBlank(project_ext.getApply_id())) {
                                        baseinfo.setAcceptdeptcode(project_ext.getApply_id());
                                    } else {
                                        baseinfo.setAcceptdeptcode("无");
                                    }
                                    baseinfo.setAcceptdeptcode1("无");
                                    baseinfo.setAcceptdeptcode2("无");

                                    service.insertQzkBaseInfo(baseinfo);

                                    // 插入办件流程表（受理步骤）
                                    eajcstepprocgt process = new eajcstepprocgt();
                                    process.set("rowguid", UUID.randomUUID().toString());
                                    process.setOrgbusno(Orgbusno);
                                    process.setProjid(project_ext.getFlowsn());
                                    process.setValidity_flag("1");
                                    process.setDataver("1");
                                    process.setStdver("1");
                                    process.setSn("1");
                                    process.setNodename("受理");
                                    process.setNodecode("act1");
                                    process.setNodetype("1");
                                    process.setNodeprocer(UUID.randomUUID().toString());
                                    process.setNodeprocername(project_ext.getAccept_user());
                                    process.setNodeprocerarea(
                                            project.getAreacode().replace("370882", "370812") + "000000");
                                    process.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                                    process.setProcunit(frameOu.getOucode());
                                    process.setProcunitname(frameOu.getOuname());
                                    process.setNodestate("02");
                                    String accepttime = EpointDateUtil.convertDate2String(project.getAccept_date(),
                                            EpointDateUtil.DATE_TIME_FORMAT);
                                    process.setNodestarttime(accepttime);
                                    process.setNodeendtime(accepttime);
                                    process.setNoderesult("4");
                                    process.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    process.setSignstate("0");
                                    process.setItemregionid(project.getAreacode().replace("370882", "370812"));
                                    // FIXME 111
                                    service.insertQzkProcess(process);

                                    eajcstepdonegt done = new eajcstepdonegt();
                                    done.set("rowguid", UUID.randomUUID().toString());
                                    done.setOrgbusno(Orgbusno);
                                    done.setProjid(project_ext.getFlowsn());
                                    done.setValidity_flag("1");
                                    done.setStdver("1");
                                    done.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                                    done.setDataver("1");
                                    done.setDoneresult("0");
                                    done.setApprovallimit(new Date());
                                    done.setCertificatenam("111");
                                    done.setCertificateno(project_ext.getApply_id());
                                    done.setIsfee("0");
                                    done.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    done.setTransactor(userSession.getDisplayName());
                                    done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    done.setSignstate("0");
                                    done.setItemregionid(project.getAreacode().replace("370882", "370812"));
                                    // FIXME 111
                                    service.insertQzkDone(done);

                                    // 插入办件流程表（已办结）
                                    eajcstepprocgt process1 = new eajcstepprocgt();
                                    process1.set("rowguid", UUID.randomUUID().toString());
                                    process1.setOrgbusno(Orgbusno);
                                    process1.setProjid(project_ext.getFlowsn());
                                    process1.setValidity_flag("1");
                                    process1.setDataver("1");
                                    process1.setStdver("1");
                                    process1.setSn("2");
                                    process1.setNodename("办结");
                                    process1.setNodecode("act2");
                                    process1.setNodetype("3");
                                    process1.setNodeprocer(userSession.getUserGuid());
                                    process1.setNodeprocername(userSession.getDisplayName());
                                    process1.setNodeprocerarea(
                                            project.getAreacode().replace("370882", "370812") + "000000");
                                    process1.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                                    process1.setProcunit(frameOu.getOucode());
                                    process1.setProcunitname(frameOu.getOuname());
                                    process1.setNodestate("02");
                                    String banjietime = EpointDateUtil.convertDate2String(project.getComplete_date(),
                                            EpointDateUtil.DATE_TIME_FORMAT);
                                    process1.setNodestarttime(banjietime);
                                    process1.setNodeendtime(banjietime);
                                    process1.setNoderesult("1");
                                    process1.setOccurtime(EpointDateUtil.convertDate2String(project.getComplete_date(), "yyyy-MM-dd HH:mm:ss"));
                                    process1.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    process1.setSignstate("0");
                                    process1.setItemregionid(project.getStr("areacode").replace("370882", "370812"));
                                    // FIXME 111
                                    service.insertQzkProcess(process1);
                                    EpointFrameDsManager.commit();
                                }
                            }
                            // 当行数等于总行数后，插入导入记录表
                            if (curRow == totalRows) {
                                service.insertExterRecord(ZwfwUserSession.getInstance().getAreaCode(), totalRows - 1);
                            }

                        }
                        EpointFrameDsManager.commit();
                    } catch (Exception e) {
                        EpointFrameDsManager.rollback();
                        // 插入报错，将已插入的数据删除，达到回滚的效果
                        service.insertExterRecord(ZwfwUserSession.getInstance().getAreaCode(), curRow - 2);
                        e.printStackTrace();
                        log.info("同步失败 =====" + e.getMessage());
                        return "导入失败！请联系管理员！";
                    } finally {
                        EpointFrameDsManager.close();
                    }

                    return message;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    getDataGridData();
                }
            });
        }
        return dataImportModel;
    }

    /*   public void deleteInfo(String project_giud,String project_ext_giud,String baseinfo_giud,String process_giud,String done_giud,String process1_giud) {
        if(StringUtil.isNotBlank(project_giud)) {
            // 删除project
            externalProjectInfoService.deleteByGuid(project_giud);
        }
        if(StringUtil.isNotBlank(project_ext_giud)) {
            // 删除project_ext
            externalProjectInfoExtService.deleteByGuid(project_ext_giud);
        }
        if(StringUtil.isNotBlank(baseinfo_giud)) {
            // 删除QzkBaseInfo
            service.deleteQzkBaseInfo(baseinfo_giud);
        }
        if(StringUtil.isNotBlank(process_giud)) {
            // 删除QzkProcess
            service.deleteQzkProcess(process_giud);
        }
        if(StringUtil.isNotBlank(done_giud)) {
            // 删除QzkDone
            service.deleteQzkDone(done_giud);
        }
        if(StringUtil.isNotBlank(process1_giud)) {
            // 删除QzkProcess
            service.deleteQzkProcess(process1_giud);
        }
    }*/

    // 获得表格对象
    public DataGridModel<Record> getDataGridData() {
        // userSession.get
        if (model == null) {
            model = new DataGridModel<Record>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pagesize, String arg2, String arg3) {
                    String ouguid = userSession.getOuGuid();
                    String areacode = "";
                    String month = "4";
                    List<String> roles = userSession.getUserRoleList();
                    if (roles != null && !roles.isEmpty()) {
                        for (String roleid : roles) {
                            FrameRole role = iRoleService.getRoleByRoleField("roleguid", roleid);
                            if (role != null) {
                                if ("中心管理员".equals(role.getRoleName())) {
                                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                                    ouguid = "";
                                    break;
                                }
                            }
                        }
                    }
                    // List<Record> lst = service.finList(first, pagesize,
                    // ouguid, areacode);
                    List<Record> lst = externalProjectInfoExtService.finList(first, pagesize, ouguid, areacode, month);
                    Integer row = externalProjectInfoExtService.finTotal(ouguid, areacode);
                    setRowCount(row == null ? 0 : row);
                    return lst;
                }
            };
        }
        return model;
    }
}
