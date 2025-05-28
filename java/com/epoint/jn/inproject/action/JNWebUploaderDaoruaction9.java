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
import com.epoint.hcp.api.entity.lcprojectnine;
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

@RestController("jnwebuploaderdaorunineaction")
@Scope("request")
public class JNWebUploaderDaoruaction9 extends BaseController {

    private static final long serialVersionUID = 1L;
    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private DataGridModel<Record> model = null;

    private Logger logger = Logger.getLogger(JNWebUploaderDaoruaction9.class);

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
                    try {
                        EpointFrameDsManager.begin(null);
                        lcprojectnine project = new lcprojectnine();
                        project.setRowguid(UUID.randomUUID().toString());
                        if (totalRows > 2000) {
                            return "一次性导入的办件数量不能超过2000！";
                        }
                        // 通过辖区获取系统参数配置数量
                        String areacount = configservice
                                .getFrameConfigValue("导入_" + ZwfwUserSession.getInstance().getAreaCode());
                        int intareacount = Integer.parseInt(areacount);
                        // 查询今日已导入量
                        int incount = service.getInCountByAreacode(ZwfwUserSession.getInstance().getAreaCode());
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
                                            project.setAcceptusername(value);
                                            break;
                                        case "受理单位":
                                            FrameOu frameou = service.getFrameOuByOuname(value);
                                            if (frameou != null) {
                                                project.setOuguid(frameou.getOuguid());
                                                project.setOuname(frameou.getOuname());
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
                                            break;
                                        case "事项名称":
                                            task = service.getAuditTaskByTaskname(value, areacode, ouguid);
                                            taskname = value;
                                            project.setProjectname(value);
                                            if (task != null) {
                                                project.setTaskguid(task.getRowguid());
                                                project.setTaskid(task.getTask_id());

                                                // 来源（外网还是其他系统）
                                                String resource = "1";
                                                // 获取事项ID
                                                String unid = zhenggaiImpl.getunidbyTaskid(task.getTask_id());
                                                // 请求接口获取受理编码
                                                if (StringUtil.isNotBlank(unid)) {

                                                    String result = FlowsnUtil.createReceiveNum(unid, task.getRowguid());
                                                    if (!"error".equals(result)) {
                                                        log.info("========================>获取受理编码成功！" + result);
                                                        project.setFlowsn(result);
                                                    } else {
                                                        log.info("========================>获取受理编码失败！");
                                                    }
                                                }
                                            } else {
                                                message = "请检查事项所属的部门、辖区、事项名称是否与系统事项库匹配";
                                            }
                                            break;

                                        case "联系人":
                                            project.setContactperson(value);
                                            break;
                                        case "联系电话":
                                            project.setContactmobile(value);
                                            break;
                                        case "承诺时间":
                                            if (StringUtil.isNotBlank(value)) {
                                                Date enddate = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                                project.setPromiseenddate(enddate);
                                            }
                                            break;
                                        case "申请人或申请单位名称":
                                            project.setApplyername(value);
                                            project.setApplyertype(20);
                                            break;
                                        case "身份证号":
                                            project.setCerttype("22");
                                            project.setCertnum(value);
                                            break;
                                        case "统一社会信用代码":
                                            if (StringUtil.isNotBlank(value)) {
                                                project.setApplyertype(10);
                                                project.setCerttype("16");
                                                project.setCertnum(value);
                                            }
                                            break;
                                        case "组织机构代码":
                                            if (StringUtil.isNotBlank(value)) {
                                                project.setApplyertype(10);
                                                project.setCerttype("14");
                                                project.setCertnum(value);
                                            }
                                            break;
                                        case "法定代表人姓名":
                                            project.setLegal(value);
                                            break;

                                        case "联系地址":
                                            project.setAddress(value);
                                            break;
                                    }
                                }

                                project.setAcceptuserdate(new Date());
                                project.setApplydate(new Date());
                                project.setBanjiedate(new Date());
                                project.setTasktype(1);
                                project.setOperatedate(new Date());
                                project.setIs_lczj("9");
                                project.setStatus(90);
                                project.setBanjieresult(40);
                                service.insert(project);

                                if (project != null) {
                                    String Orgbusno = UUID.randomUUID().toString();
                                    eajcstepbasicinfogt baseinfo = new eajcstepbasicinfogt();
                                    FrameOu frameOu = service.getFrameOuByOuname(project.getOuname());
                                    baseinfo.set("rowguid", Orgbusno);
                                    baseinfo.setOrgbusno(Orgbusno);
                                    baseinfo.setProjid(project.getFlowsn());
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
                                    if (10 == project.getApplyertype()) {
                                        baseinfo.set("APPLYERTYPE", "2");
                                        baseinfo.set("ApplyerPageType", "01");
                                    } else {
                                        baseinfo.set("APPLYERTYPE", "1");
                                        baseinfo.set("ApplyerPageType", "111");
                                    }
                                    //申请人证件号码
                                    baseinfo.set("ApplyerPageCode", project.getCertnum());
                                    baseinfo.setItemname(taskname);
                                    baseinfo.setProjectname(project.getProjectname());
                                    baseinfo.setApplicant(project.getApplyername());
                                    baseinfo.setApplicantCardCode(project.getCertnum());
                                    baseinfo.setApplicanttel(project.getContactmobile());
                                    baseinfo.setAcceptdeptid(frameOu.getOucode());
                                    baseinfo.setRegion_id(task.getAreacode().replace("370882", "370812") + "000000");
                                    baseinfo.setAcceptdeptname(frameOu.getOuname());
                                    baseinfo.setApprovaltype("2");
                                    baseinfo.setPromisetimelimit("0");
                                    baseinfo.setPromisetimeunit("1");
                                    baseinfo.setTimelimit("0");
                                    baseinfo.setItemregionid(project.getAreacode().replace("370882", "370812"));
                                    if ("22".equals(project.getCerttype())) {
                                        baseinfo.setApplicantCardtype("111");
                                    } else if ("16".equals(project.getCerttype())) {
                                        baseinfo.setApplicantCardtype("01");
                                    } else if ("14".equals(project.getCerttype())) {
                                        baseinfo.setApplicantCardtype("02");
                                    }
                                    baseinfo.setSubmit("1");
                                    baseinfo.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.set("Status", "0");
                                    if (StringUtil.isNotBlank(project.getCertnum())) {
                                        baseinfo.setAcceptdeptcode(project.getCertnum());
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
                                    process.setProjid(project.getFlowsn());
                                    process.setValidity_flag("1");
                                    process.setDataver("1");
                                    process.setStdver("1");
                                    process.setSn("1");
                                    process.setNodename("受理");
                                    process.setNodecode("act1");
                                    process.setNodetype("1");
                                    process.setNodeprocer(UUID.randomUUID().toString());
                                    process.setNodeprocername(project.getAcceptusername());
                                    process.setNodeprocerarea(
                                            project.getAreacode().replace("370882", "370812") + "000000");
                                    process.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                                    process.setProcunit(frameOu.getOucode());
                                    process.setProcunitname(frameOu.getOuname());
                                    process.setNodestate("02");
                                    String accepttime = EpointDateUtil.convertDate2String(project.getAcceptuserdate(),
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
                                    done.setProjid(project.getFlowsn());
                                    done.setValidity_flag("1");
                                    done.setStdver("1");
                                    done.setRegion_id(project.getAreacode().replace("370882", "370812") + "000000");
                                    done.setDataver("1");
                                    done.setDoneresult("0");
                                    done.setApprovallimit(new Date());
                                    done.setCertificatenam("111");
                                    done.setCertificateno(project.getCertnum());
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
                                    process1.setProjid(project.getFlowsn());
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
                                    String banjietime = EpointDateUtil.convertDate2String(project.getBanjiedate(),
                                            EpointDateUtil.DATE_TIME_FORMAT);
                                    process1.setNodestarttime(banjietime);
                                    process1.setNodeendtime(banjietime);
                                    process1.setNoderesult("1");
                                    process1.setOccurtime(EpointDateUtil.convertDate2String(project.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
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
                                service.insertLcprojectRecord(ZwfwUserSession.getInstance().getAreaCode(),
                                        totalRows - 1);
                            }

                        }
                        EpointFrameDsManager.commit();
                    } catch (Exception e) {
                        EpointFrameDsManager.rollback();
                        service.insertLcprojectRecord(ZwfwUserSession.getInstance().getAreaCode(), curRow - 2);
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
                    List<Record> lst = service.finList(first, pagesize, ouguid, areacode);
                    Integer row = service.finTotal(ouguid, areacode);
                    setRowCount(row == null ? 0 : row);
                    return lst;
                }
            };
        }
        return model;
    }
}
