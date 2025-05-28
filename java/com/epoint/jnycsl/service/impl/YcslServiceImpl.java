package com.epoint.jnycsl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.webservice.WebServiceUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnycsl.constant.*;
import com.epoint.jnycsl.dao.TaianBaseDao;
import com.epoint.jnycsl.dao.YcslDao;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;
import com.epoint.jnycsl.service.YcslService;
import com.epoint.jnycsl.utils.HttpUtils;
import com.epoint.jnycsl.utils.NetworkDiskUtils;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy;

import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 一窗受理service
 * 推送接件信息给部门自建系统，其他一窗系统主动推送功能
 *
 * @author 徐本能
 * @time 2018年12月11日下午6:00:55
 */
@Service
public class YcslServiceImpl implements YcslService {

    /**
     * 默认行政区划代码
     */
    private static final String DEFAULT_REGION_ID = "370800";
    private Logger logger = Logger.getLogger(YcslServiceImpl.class);

    @Autowired
    private IAuditTask auditTaskService;

    @SuppressWarnings("unused")
    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IConfigService frameconfigservice;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private DeptSelfBuildSystemService selfBuildSystemService;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial auditTaskMaterialService;

    /**
     * 区域API
     */
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @SuppressWarnings("unused")
    @Autowired
    private IAttachService attachService;
    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private YcslDao ycslDao;
    /**
     * 部门API
     */
    @Autowired
    private IOuService iOuService;
    @Autowired
    private TaianBaseDao taianBaseDao;
    /**
     * 办件推送浪潮工具类
     */
    WavePushInterfaceUtils wavePushInterfaceUtils = new WavePushInterfaceUtils();

    @Override
    public boolean pushReceiveInfo2DeptSelfBuildSystem(AuditProject project, AuditTask auditTask,
                                                       AuditTaskExtension auditTaskExtension) {
        DeptSelfBuildSystem deptSelfBuildSystem = selfBuildSystemService.findDeptSelfBuildSystemByTaskGuid(project.getTaskguid());
        boolean pass = checkObject(project, auditTask, auditTaskExtension, deptSelfBuildSystem);
        if (!pass) {
            return false;
        }
        ;
        BusinessType businessType = BusinessType.RECEIVE_BUSINESS;
        Integer selfBuildSystemMode = auditTaskExtension.getZijian_mode();
        if (selfBuildSystemMode != null) {
            if (SelfBuildSystemMode.ACCEPT_MODE.toString().equals(selfBuildSystemMode.toString())) {
                businessType = BusinessType.ACCEPT_BUSINESS;
            } else {
                businessType = BusinessType.RECEIVE_BUSINESS;
            }
        }
        // 业务参数accessToken xmlStr
        String accessToken = requestAccessToken(deptSelfBuildSystem);
        String xmlStr = generateReceiveXmlStr(project, businessType, auditTaskExtension);
        Object[] params = {accessToken, xmlStr};
        String result = "";
        String receiveApiType = deptSelfBuildSystem.getReceiveProjectApiType();
        String receiveApiUrl = deptSelfBuildSystem.getReceiveProjectApiUrl();
        if (ApiType.WEB_SERVICE.getValue().equals(receiveApiType)) {
            try {
                result = new DspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy().sendApplyNO(accessToken, xmlStr);
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            result = WebServiceUtil.invokeService(receiveApiUrl, "http://loushang.ws", "sendApplyNO", params);
        } else {
            Map<String, String> requestBodyMap = new HashMap<>();
            requestBodyMap.put("accessToken", accessToken);
            requestBodyMap.put("xmlStr", xmlStr);
            result = HttpUtils.postHttp(receiveApiUrl, requestBodyMap);
        }
        if (StringUtil.isNotBlank(result)) {
            try {
                Document document = DocumentHelper.parseText(result);
                Element root = document.getRootElement();
                String status = root.elementText("STATUS");
                if (ResponseStatus.OK.getValue().equals(status)) {
                    ycslDao.updateProjectPushStatus(PushStatus.SUCCESS, "信息推送成功", project.getRowguid());
                    return true;
                } else {
                    ycslDao.updateProjectPushStatus(PushStatus.FAILED, root.elementText("DESC"),
                            project.getRowguid());
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ycslDao.updateProjectPushStatus(PushStatus.FAILED, e.getMessage(), project.getRowguid());
                return false;
            }
        } else {
            ycslDao.updateProjectPushStatus(PushStatus.FAILED, "接口返回结果为空", project.getRowguid());
            return false;
        }
    }

    /**
     * 检查数据是否满足推送条件
     *
     * @param project
     * @param auditTask
     * @param auditTaskExtension
     * @param deptSelfBuildSystem
     * @return
     */
    private boolean checkObject(AuditProject project, AuditTask auditTask, AuditTaskExtension auditTaskExtension,
                                DeptSelfBuildSystem deptSelfBuildSystem) {
        if (auditTaskExtension == null) {
            return false;
        }
        if (StringUtil.isBlank(getQssxjhm(auditTaskExtension))) {
            return false;
        }
        if (deptSelfBuildSystem == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取事项扩展信息对象里的全省事项交换码
     *
     * @param auditTaskExtension
     * @return
     */
    private String getQssxjhm(AuditTaskExtension auditTaskExtension) {
        if (StringUtil.isNotBlank(auditTaskExtension.getStr("qssxjhm"))) {
            return auditTaskExtension.getStr("qssxjhm");
        } else {
            String sql = "select qssxjhm from audit_task_extension where rowguid = ?";
            String qssxjhm = CommonDao.getInstance().queryString(sql, auditTaskExtension.getRowguid());
            auditTaskExtension.set("qssxjhm", qssxjhm);
            return qssxjhm;
        }
    }

    public String requestAccessToken(DeptSelfBuildSystem deptSelfBuildSystem) {
        return "";
    }

    /**
     * 获取接件/受理 输入参数
     *
     * @return
     */
    private String generateReceiveXmlStr(AuditProject project, BusinessType businessType,
                                         AuditTaskExtension auditTaskExtension) {
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(project.getTaskguid(), false).getResult();
        String regionId = StringUtil.isNotBlank(project.getAreacode()) ? project.getAreacode() : DEFAULT_REGION_ID;
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("APPROVEDATAINFO");
        root.addElement("PROJID").addText(project.getFlowsn());
        root.addElement("ITEMNO").addText(auditTask.getFullid());
        root.addElement("ITEMREGION").addText(auditTask.getAreacode()); // 事项区划(必填)
        root.addElement("EXCHANGENO").addText(auditTaskExtension.getStr("qssxjhm")); // 全省事项交换码:事项数据交换编
        root.addElement("REGION_ID").addText(regionId); // 办理区划ID
        root.addElement("EXPRESSTYPE").addText(businessType.toString()); // EXPRESSTYPE 0:收件业务 1：受理业务
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        root.addElement("ACCEPTDEPTID").addText(project.getAcceptuserguid()); // EXPRESSTYPE 0:收件业务 1：受理业务
        root.addElement("ACCEPTUSERID").addText(project.getAcceptusername()); // EXPRESSTYPE 0:收件业务 1：受理业务
        root.addElement("ACCEPTTIME").addText(sdf.format(project.getAcceptuserdate())); // EXPRESSTYPE 0:收件业务 1：受理业务
        root.addElement("ACCEPTRESULT").addText("同意受理"); // EXPRESSTYPE 0:收件业务 1：受理业务
        return document.asXML();
    }

    @Override
    public void uploadFileToNetworkDisk(FrameAttachStorage storage, String projectMaterialGuid) {
        AuditProjectMaterial projectMaterial = taianBaseDao.findEntity(AuditProjectMaterial.class, projectMaterialGuid);
        String fileId = "";
        if (projectMaterial != null) {
            fileId = projectMaterial.getStr("networkDiskPath");
        }
        String newFileId = NetworkDiskUtils.updateFile(storage.getContent(), storage.getAttachFileName());
        fileId += newFileId;
        ycslDao.updateNetworkDiskPathOfProjectMaterial(fileId, projectMaterialGuid);
    }

    @Override
    public boolean useYcslByProjectGuid(String projectGuid) {
        Object isZiJianXiTong = ycslDao.getFiledOfTaskExtensionByProjectGuid("iszijianxitong", projectGuid);
        return isZiJianXiTong == null ? false : ("1".equals(isZiJianXiTong.toString()));
    }

    @Override
    public boolean useYcslByTaskExtensionObj(AuditTaskExtension auditTaskExtension) {
        Integer a = auditTaskExtension.getIszijianxitong();
        if (a == null) {
            return false;
        } else {
            if (auditTaskExtension.getZijian_mode() != null) {
                if (SelfBuildSystemMode.ACCEPT_MODE.toString().equals(auditTaskExtension.getZijian_mode().toString())) {
                    return false;
                } else {
                    return (a == 1);
                }
            } else {
                return false;
            }

        }

    }

    @Override
    public boolean useYcslByTaskExtensionObj1(AuditTaskExtension auditTaskExtension) {
        Integer a = auditTaskExtension.getIszijianxitong();
        if (a == null) {

            return false;
        } else {
            if (auditTaskExtension.getZijian_mode() != null) {
                if (SelfBuildSystemMode.ACCEPT_MODE.toString().equals(auditTaskExtension.getZijian_mode().toString())) {
                    return (a == 1);
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }

    }

    @Override
    public boolean ycsllcbyprojectandtask(AuditTask auditTask, AuditProject auditProject) {
        try {
            // 来源（外网还是其他系统）
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            // 获取事项ID
            String sql = "select unid from audit_task where rowguid=? ";
            String unid = CommonDao.getInstance().queryString(sql, new Object[]{auditProject.getTaskguid()});
            AuditProject project = CommonDao.getInstance().find(AuditProject.class, auditProject.getRowguid());

            // 构造请求申报数据对接接口
            JSONObject acceptMap = new JSONObject();
            // 审批业务受理编号

            acceptMap.put("receiveNum", auditProject.getFlowsn());
            // sp： 外网首次提交 bqbz:二次提交”
            acceptMap.put("state", "sp");
            // “是否企业设立” 企业设立：1是 ；0否
            acceptMap.put("isSetUp", "0");
            // 事项ID
            acceptMap.put("itemId", unid);
            // 事项CODE
            acceptMap.put("itemCode", auditTask.getItem_id());
            // 单位code
            FrameOu frameOu = iOuService.getOuByOuGuid(auditTask.getOuguid());
            String orgCode = frameOu.getOucode();
            // 事项名称
            acceptMap.put("itemName", auditTask.getTaskname());
            // 事项所属单位
            acceptMap.put("orgCode", orgCode);
            // 事项单位名称
            acceptMap.put("orgName", auditTask.getOuname());
            // 事项所属区划
            String regionId = auditTask.getAreacode();
            regionId = StringUtil.isNotBlank(regionId) ? regionId.replace("370882", "370812") : DEFAULT_REGION_ID;
            if (regionId.length() == 6) {
                regionId += "000000";
            } else if (regionId.length() == 9) {
                regionId += "000";
            }

            acceptMap.put("regionCode", regionId);
            // 事项所属区划名称
            AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(auditTask.getAreacode())
                    .getResult();
            acceptMap.put("regionName", auditOrgaArea.getXiaquname());
            // 办件类型
            String assort = "";
            if (auditTask.getType() == 1) {
                assort = "2";
            } else if (auditTask.getType() == 2) {
                assort = "1";
            } else if (auditTask.getType() == 3) {
                assort = "3";
            } else {
                assort = "4";
            }
            String username = UserSession.getInstance().getDisplayName();
            acceptMap.put("assort", assort);
            acceptMap.put("RECEIVE_NAME", username);
            String dataid = project.get("dataid");
            String formId = project.get("formid");

            acceptMap.put("formId", formId);
            acceptMap.put("dataId", dataid);
            // 有密码则按传过来的为准，没密码按审批系统自己生成的为准
            acceptMap.put("password", auditProject.get("pwd"));
            // 服务对象类型（1：人员；2：项目 3：企业）
            int objectType = 2;
            if (auditProject.getApplyertype() == 10) {
                objectType = 3;
            } else if (auditProject.getApplyertype() == 20) {
                objectType = 1;
            }
            acceptMap.put("objectType", Integer.toString(objectType));
            // 材料相关的数据
            JSONArray materialArray = new JSONArray();
            // 办件材料列表
            String projectguid = auditProject.getRowguid();
            IAuditProjectMaterial auditprojectmaterial = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            List<AuditProjectMaterial> projectmaterialList = auditprojectmaterial
                    .selectProjectMaterial(projectguid).getResult();

            if (projectmaterialList != null && projectmaterialList.size() > 0) {
                for (AuditProjectMaterial auditProjectMaterial : projectmaterialList) {
                    AuditTaskMaterial auditTaskMaterial = auditTaskMaterialService
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                            .getResult();
                    JSONObject materialJsonObj1 = new JSONObject();

                    List<FrameAttachInfo> frameAttachInfo = attachService
                            .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                    if (frameAttachInfo != null && frameAttachInfo.size() > 0) {
                        for (FrameAttachInfo info : frameAttachInfo) {
                            // 操作人ID
                            JSONObject materialJsonObj = new JSONObject();

                            materialJsonObj.put("OPERATOR_ID", "");
                            // 操作人
                            materialJsonObj.put("OPERATOR_NAME", "统一接件人员");
                            // 材料ID
                            materialJsonObj.put("DOCUMENT_ID", auditTaskMaterial.getMaterialid());
                            // 材料名称
                            materialJsonObj.put("DOCUMENT_NAME", auditTaskMaterial.getMaterialname());
                            Map<String, String> params = new HashMap<String, String>();
                            String jnuserid = "";
                            String langchaouid = frameconfigservice.getFrameConfigValue("AS_IS_LANGCHAOUID");
                            if (StringUtil.isNotBlank(langchaouid)) {
                                String[] uidlist = langchaouid.split(";");
                                int length = uidlist.length - 1;
                                Random random = new SecureRandom();
                                int rand = random.nextInt(length);
                                jnuserid = uidlist[rand];
                                params.put("uid", jnuserid);
                            } else {
                                params.put("uid", "jnuserid");
                            }
                            params.put("type", "doc");
                            params.put("folder_name", "jiningycsl");
                            String file = info.getFilePath()
                                    + info.getAttachFileName();
                            //上传浪潮网盘
                            String materialresult = wavePushInterfaceUtils.startUploadService(params, file,
                                    "http://59.206.96.197:8080/WebDiskServerDemo/upload");
                            JSONObject jsonObject = JSON.parseObject(materialresult);
                            String msg = jsonObject.getString("code");
                            if ("0000".equals(msg)) {
                                materialJsonObj.put("TYPE", "1");
                                materialJsonObj.put("FILE_NAME", info.getAttachFileName());
                                String id = jsonObject.getString("docid");
                                materialJsonObj.put("FILE_PATH", jsonObject.getString("docid"));
                            } else {
                                materialJsonObj.put("TYPE", "0");
                                materialJsonObj.put("FILE_NAME", auditTaskMaterial.getMaterialname());
                                materialJsonObj.put("FILE_PATH", "");
                            }
                            materialArray.add(materialJsonObj);
                        }
                    } else {
                        // 操作人ID
                        materialJsonObj1.put("OPERATOR_ID", "");
                        // 操作人
                        materialJsonObj1.put("OPERATOR_NAME", "统一接件人员");
                        // 材料ID
                        materialJsonObj1.put("DOCUMENT_ID", auditTaskMaterial.getMaterialid());
                        // 材料名称
                        materialJsonObj1.put("DOCUMENT_NAME", auditTaskMaterial.getMaterialname());
                        materialJsonObj1.put("TYPE", "0");
                        materialJsonObj1.put("FILE_NAME", auditTaskMaterial.getMaterialname());
                        materialJsonObj1.put("FILE_PATH", "");
                        materialArray.add(materialJsonObj1);

                    }
                }
            }
            acceptMap.put("metail", materialArray);
            JSONObject personInfoJson = new JSONObject();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            JSONObject formInfo = new JSONObject();
            //推送浪潮新增加字段
            formInfo.put("DianZiYouXiang", "");
            formInfo.put("ShouLiRiQi", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

            String object = auditProject.getStr("dataObj_baseinfo");
            Log.info("获取办件对应的表单信息：" + object);
            if (StringUtil.isNotBlank(object)) {
                JSONObject json = JSONObject.parseObject(object);
                String beizhu = json.getString("ssxq");
                if (StringUtil.isNotBlank(beizhu)) {
                    formInfo.put("BeiZhu", beizhu);
                } else {
                    formInfo.put("BeiZhu", "");
                }
            }
            formInfo.put("ShenFenZhengHaoSheHuiTongYiXin", auditProject.getCertnum());
            formInfo.put("ShenQingRenXingMingDanWeiMingC", auditProject.getApplyername());
            formInfo.put("JieShouDuanXinShouJiHaoMa", auditProject.getContactmobile());
            formInfo.put("ShenQingRenDanWeiDiZhi", auditProject.getAddress());
            formInfo.put("SuoShuXiaQu", "济宁市");
            formInfo.put("LianXiDianHua", auditProject.getContactphone());
            if (objectType == 1) {
                AuditRsIndividualBaseinfo info = null;
                String sqlindivi = "select * from audit_rs_individual_baseinfo where projectguid=?";
                info = CommonDao.getInstance().find(sqlindivi, AuditRsIndividualBaseinfo.class, auditProject.getRowguid());
                if (info == null) {
                    String sqlindivibyid = "select * from audit_rs_individual_baseinfo where idnumber=?";
                    info = CommonDao.getInstance().find(sqlindivibyid, AuditRsIndividualBaseinfo.class, auditProject.getCertnum());
                }
                String certType = auditProject.getCerttype();
                Log.info("推送浪潮certType:" + certType);
                String tocertType = "";
                if ("16".equals(certType)) {
                    tocertType = "01";
                } else if ("14".equals(certType)) {
                    tocertType = "02";
                } else if ("30".equals(certType)) {
                    tocertType = "03";
                } else if ("22".equals(certType)) {
                    tocertType = "111";
                } else if ("26".equals(certType)) {
                    tocertType = "414";
                } else {
                    tocertType = "01";
                }
                if (info != null && info.size() > 0) {

                    // 个人信息参数构造
                    // 证件类型
                    personInfoJson.put("identityType", tocertType);
                    // 证件号
                    personInfoJson.put("idcardNo", info.getIdnumber());
                    // 姓名
                    personInfoJson.put("name", auditProject.getApplyername());
                    // 性别
                    personInfoJson.put("sex", info.getSex());
                    // 民族
                    personInfoJson.put("nation", info.getNation());
                    // 政治面貌
                    personInfoJson.put("politicalStatus", info.get("POLITICALSTATUS"));
                    // 学历
                    personInfoJson.put("education", info.get("EDUCATION"));
                    // 籍贯
                    personInfoJson.put("nativePlace", info.get("NATIVEPLACE"));
                    // 出生日期
                    if (StringUtil.isNotBlank(info.get("BIRTHDAY"))) {
                        personInfoJson.put("birthday", formatter.format(info.get("BIRTHDAY")));
                    } else {
                        personInfoJson.put("birthday", "");
                    }
                    // 国籍
                    personInfoJson.put("country", info.get("COUNTRY"));
                    // 居住地址
                    personInfoJson.put("homeAddress", info.get("HOMEADDRESS"));
                    // 联系电话
                    personInfoJson.put("linkPhone", info.getContactmobile());
                    // 联系地址
                    personInfoJson.put("linkAddress", auditProject.getAddress());
                    // 邮政编码
                    personInfoJson.put("postCode", info.getContactpostcode());
                    // 省
                    personInfoJson.put("province", info.get("PROVINCE"));
                    // 市
                    personInfoJson.put("city", info.get("CITY"));
                    // 县
                    personInfoJson.put("county", info.get("COUNTY"));
                } else {
                    // 个人信息参数构造
                    // 证件类型

                    personInfoJson.put("identityType", tocertType);
                    // 证件号
                    personInfoJson.put("idcardNo", auditProject.getCertnum());
                    // 姓名
                    personInfoJson.put("name", auditProject.getApplyername());
                    // 性别
                    personInfoJson.put("sex", "");
                    // 民族
                    personInfoJson.put("nation", "");
                    // 政治面貌
                    personInfoJson.put("politicalStatus", "");
                    // 学历
                    personInfoJson.put("education", "");
                    // 籍贯
                    personInfoJson.put("nativePlace", "");
                    // 出生日期
                    personInfoJson.put("birthday", "");
                    // 国籍
                    personInfoJson.put("country", "");
                    // 居住地址
                    personInfoJson.put("homeAddress", "");
                    // 联系电话
                    personInfoJson.put("linkPhone", auditProject.getContactmobile());
                    // 联系地址
                    personInfoJson.put("linkAddress", auditProject.getAddress());
                    // 邮政编码
                    personInfoJson.put("postCode", auditProject.getContactpostcode());
                    // 省
                    personInfoJson.put("province", "");
                    // 市
                    personInfoJson.put("city", "");
                    // 县
                    personInfoJson.put("county", "");
                    // 邮箱
                }
                acceptMap.put("info", personInfoJson);
            } else {
                JSONObject orgInfoJson = new JSONObject();
                String companysql = "select * from Audit_Rs_Company_Baseinfo where creditcode=? ORDER BY VERSION DESC LIMIT 1";
                AuditRsCompanyBaseinfo company = CommonDao.getInstance().find(companysql, AuditRsCompanyBaseinfo.class, auditProject.getCertnum());
                if (company != null && company.size() > 0) {
                    orgInfoJson.put("orgName", company.getOrganname());
                    if ("14".equals(auditProject.getCerttype())) {
                        orgInfoJson.put("orgCode", company.getCreditcode());
                    } else {
                        orgInfoJson.put("orgCode", "");
                    }
                    if ("16".equals(auditProject.getCerttype())) {
                        orgInfoJson.put("creditCode", auditProject.getCertnum());
                    } else {
                        orgInfoJson.put("creditCode", "");
                    }
                    orgInfoJson.put("orgCodeAwardDate", "");
                    orgInfoJson.put("orgCodeAwardOrg", "");
                    orgInfoJson.put("orgCodeValidPeriodStart", "");
                    orgInfoJson.put("orgCodeValidPeriodEnd_str", "");
                    orgInfoJson.put("orgCodeValidPeriodEnd", "");
                    orgInfoJson.put("orgEnglishName", "");
                    orgInfoJson.put("orgType", company.getOrgantype());// 必填
                    orgInfoJson.put("enterpriseSortCode", "");
                    orgInfoJson.put("enterpriseSortName", "");
                    orgInfoJson.put("enterpriseTypeCode", "");
                    orgInfoJson.put("enterpriseTypeName", "");
                    orgInfoJson.put("legalPerson", company.getOrganlegal());
                    orgInfoJson.put("legalPersonType", company.get("LEGALPERSONTYPE"));
                    orgInfoJson.put("certificateName", company.get("CERTIFICATENAME"));
                    orgInfoJson.put("certificateNo", auditProject.getContactcertnum());
                    orgInfoJson.put("responsiblePerson", company.get("responsiblePerson"));
                    orgInfoJson.put("responsiblePersonId", company.get("responsiblePersonId"));
                    orgInfoJson.put("inAreaCode", company.get("inAreaCode"));
                    orgInfoJson.put("inArea", company.get("inArea"));
                    orgInfoJson.put("chargeDepartment", company.get("chargeDepartment"));
                    orgInfoJson.put("registerAddress", auditProject.getAddress());
                    orgInfoJson.put("produceAddress", company.get("produceAddress"));
                    orgInfoJson.put("mailingAddress", company.get("mailingAddress"));
                    orgInfoJson.put("postalCode", company.get("postalCode"));
                    orgInfoJson.put("linkMan", auditProject.getContactperson());
                    orgInfoJson.put("contactPhone", auditProject.getContactmobile());
                    orgInfoJson.put("fax", auditProject.getContactfax());
                    orgInfoJson.put("linkManEmail", auditProject.getContactemail());
                    orgInfoJson.put("bank", company.get("bank"));
                    orgInfoJson.put("bankAccount", "");
                    orgInfoJson.put("organizationKind", company.get("organizationKind"));
                    orgInfoJson.put("employeeNum", company.get("employeeNum"));
                    orgInfoJson.put("registerCapital", "");
                    orgInfoJson.put("currencyKind", "");
                    orgInfoJson.put("groundArea", "");
                    orgInfoJson.put("businessScope", company.get("businessScope"));// 必填
                    orgInfoJson.put("businessScopePart", "");
                    orgInfoJson.put("mainProduct", "");
                    orgInfoJson.put("operatingMode", "");
                    orgInfoJson.put("registerDate", "");// 必填
                    orgInfoJson.put("orgFoundDate", "");
                    orgInfoJson.put("businessLicense", "");
                    orgInfoJson.put("aicAwardDate", "");
                    orgInfoJson.put("aicValidPeriodStart", "");
                    orgInfoJson.put("aicValidPeriodEnd", "");
                    orgInfoJson.put("aicCertAwardOrg", "");
                    orgInfoJson.put("taxManager", "");
                    orgInfoJson.put("nationTaxRegisterNo", "");
                    orgInfoJson.put("nationTaxAwardDate", "");
                    orgInfoJson.put("locationTaxRegisterNo", "");
                    orgInfoJson.put("locationTaxAwardDate", "");
                    orgInfoJson.put("nationInvestRate", "");
                    orgInfoJson.put("corporationInvestRate", "");
                    orgInfoJson.put("foreignInvestRate", "");
                    orgInfoJson.put("naturalManInvestRate", "");
                    orgInfoJson.put("bankLoanRate", "");
                    orgInfoJson.put("remark", company.get("remark"));
                    orgInfoJson.put("HANDLERNAME", company.get("HANDLERNAME"));// 必填
                    orgInfoJson.put("HANDLERPHONE", company.get("HANDLERPHONE"));// 必填
                    orgInfoJson.put("HANDLERIDTYPE", company.get("HANDLERIDTYPE"));// 必填
                    orgInfoJson.put("HANDLERID", company.get("HANDLERID"));// 必填
                    orgInfoJson.put("ORGACTUALITY", company.get("ORGACTUALITY"));// 必填
                } else {
                    orgInfoJson.put("orgName", auditProject.getApplyername());
                    if ("14".equals(auditProject.getCerttype())) {
                        orgInfoJson.put("orgCode", auditProject.getCertnum());
                    } else {
                        orgInfoJson.put("orgCode", "");
                    }
                    if ("16".equals(auditProject.getCerttype())) {
                        orgInfoJson.put("creditCode", auditProject.getCertnum());
                    } else {
                        orgInfoJson.put("creditCode", "");
                    }
                    orgInfoJson.put("orgCodeAwardDate", "");
                    orgInfoJson.put("orgCodeAwardOrg", "");
                    orgInfoJson.put("orgCodeValidPeriodStart", "");
                    orgInfoJson.put("orgCodeValidPeriodEnd_str", "");
                    orgInfoJson.put("orgCodeValidPeriodEnd", "");
                    orgInfoJson.put("orgEnglishName", "");
                    orgInfoJson.put("orgType", "Enterprise");// 必填
                    orgInfoJson.put("enterpriseSortCode", "");
                    orgInfoJson.put("enterpriseSortName", "");
                    orgInfoJson.put("enterpriseTypeCode", "");
                    orgInfoJson.put("enterpriseTypeName", "");
                    orgInfoJson.put("legalPerson", auditProject.getLegal());
                    orgInfoJson.put("legalPersonType", "legal_person");
                    orgInfoJson.put("certificateName", "身份证");
                    orgInfoJson.put("certificateNo", auditProject.getContactcertnum());
                    orgInfoJson.put("responsiblePerson", "");
                    orgInfoJson.put("responsiblePersonId", "");
                    orgInfoJson.put("inAreaCode", "");
                    orgInfoJson.put("inArea", "");
                    orgInfoJson.put("chargeDepartment", "");
                    orgInfoJson.put("registerAddress", auditProject.getAddress());
                    orgInfoJson.put("produceAddress", "");
                    orgInfoJson.put("mailingAddress", "");
                    orgInfoJson.put("postalCode", "");
                    orgInfoJson.put("linkMan", auditProject.getContactperson());
                    orgInfoJson.put("contactPhone", auditProject.getContactmobile());
                    orgInfoJson.put("fax", auditProject.getContactfax());
                    orgInfoJson.put("linkManEmail", auditProject.getContactemail());
                    orgInfoJson.put("bank", "");
                    orgInfoJson.put("bankAccount", "");
                    orgInfoJson.put("organizationKind", "");
                    orgInfoJson.put("employeeNum", "");
                    orgInfoJson.put("registerCapital", "");
                    orgInfoJson.put("currencyKind", "");
                    orgInfoJson.put("groundArea", "");
                    orgInfoJson.put("businessScope", "");// 必填
                    orgInfoJson.put("businessScopePart", "");
                    orgInfoJson.put("mainProduct", "");
                    orgInfoJson.put("operatingMode", "");
                    orgInfoJson.put("registerDate", "");// 必填
                    orgInfoJson.put("orgFoundDate", "");
                    orgInfoJson.put("businessLicense", "");
                    orgInfoJson.put("aicAwardDate", "");
                    orgInfoJson.put("aicValidPeriodStart", "");
                    orgInfoJson.put("aicValidPeriodEnd", "");
                    orgInfoJson.put("aicCertAwardOrg", "");
                    orgInfoJson.put("taxManager", "");
                    orgInfoJson.put("nationTaxRegisterNo", "");
                    orgInfoJson.put("nationTaxAwardDate", "");
                    orgInfoJson.put("locationTaxRegisterNo", "");
                    orgInfoJson.put("locationTaxAwardDate", "");
                    orgInfoJson.put("nationInvestRate", "");
                    orgInfoJson.put("corporationInvestRate", "");
                    orgInfoJson.put("foreignInvestRate", "");
                    orgInfoJson.put("naturalManInvestRate", "");
                    orgInfoJson.put("bankLoanRate", "");
                    orgInfoJson.put("remark", "");
                    orgInfoJson.put("orgActuality", "Change");// 必填
                    //项目信息
                    orgInfoJson.put("projectName", auditProject.getStr("xiangmuname"));
                    orgInfoJson.put("projectCode", auditProject.getStr("xiangmubm"));
                    orgInfoJson.put("location", auditProject.getAddress());
                    orgInfoJson.put("linkMan", auditProject.getContactperson());
                    orgInfoJson.put("linkPhone", auditProject.getContactphone());
                    orgInfoJson.put("projectContent", auditProject.getStr("PROJECTCONTENT"));
                    orgInfoJson.put("areaAll", auditProject.getStr("AREAALL"));
                    orgInfoJson.put("areaBuild", auditProject.getStr("AREABUILD"));
                    orgInfoJson.put("investment", auditProject.getStr("INVESTMENT"));
                    orgInfoJson.put("projectAllowedNo", auditProject.getStr("PROJECTALLOWEDNO"));
                }
                acceptMap.put("info", orgInfoJson);
            }

            if (auditProject.getProjectname().contains("在设区的市范围内跨区、县进行公路超限运输许可")||auditProject.getProjectname().contains("超限运输车辆行驶公路许可新办")) {
                String data_object = auditProject.getStr("dataObj_baseinfo");
                if (StringUtil.isNotBlank(data_object)) {
                    JSONObject otherInfoJson = JSONObject.parseObject(data_object);
                    formInfo.put("qylx", otherInfoJson.getString("qylx"));
                    formInfo.put("hwcd", otherInfoJson.getString("hwcd"));
                    formInfo.put("chzzl", otherInfoJson.getString("chzzl"));
                    formInfo.put("regaddress", otherInfoJson.getString("regaddress"));
                    formInfo.put("xssjksrq", otherInfoJson.getString("xssjksrq"));
                    formInfo.put("hwlxmc", otherInfoJson.getString("hwlxmc"));
                    formInfo.put("gcc", otherInfoJson.getString("gcc"));
                    formInfo.put("gcg", otherInfoJson.getString("gcg"));
                    formInfo.put("qyclsbm", otherInfoJson.getString("qyclsbm"));
                    formInfo.put("gck", otherInfoJson.getString("gck"));
                    formInfo.put("xkzyxqq", otherInfoJson.getString("xkzyxqq"));
                    formInfo.put("cydwmc", otherInfoJson.getString("cydwmc"));
                    formInfo.put("xkzyxqz", otherInfoJson.getString("xkzyxqz"));
                    formInfo.put("gclcpxh", otherInfoJson.getString("gclcpxh"));
                    formInfo.put("jbrxb", otherInfoJson.getString("jbrxb"));
                    formInfo.put("qyck", otherInfoJson.getString("qyck"));
                    formInfo.put("gczs", otherInfoJson.getString("gczs"));
                    formInfo.put("txlx", otherInfoJson.getString("txlx"));
                    formInfo.put("gczx", otherInfoJson.getString("gczx"));
                    formInfo.put("jbrxm", otherInfoJson.getString("jbrxm"));

                    formInfo.put("mdd", iCodeItemsService.getItemTextByCodeName("地域分类", otherInfoJson.getString("mdd")));
                    formInfo.put("cfd", iCodeItemsService.getItemTextByCodeName("地域分类", otherInfoJson.getString("cfd")));


                    String tjd = otherInfoJson.getString("tjd").replace("[", "").replace("]", "");
                    String resulttjd = "";
                    if (StringUtil.isNotBlank(tjd)) {
                        if (tjd.contains(",")) {
                            String[] tjds = tjd.split(",");
                            for (String jdd : tjds) {
                                String itemtext = iCodeItemsService.getItemTextByCodeID("1015852", jdd.replace("\"", ""));
                                if (StringUtil.isNotBlank(itemtext)) {
                                    resulttjd += itemtext + ',';
                                }
                            }
                        } else {
                            resulttjd = iCodeItemsService.getItemTextByCodeID("1015852", tjd.replace("\"", ""));
                        }

                        if (resulttjd.endsWith(",")) {
                            resulttjd = resulttjd.replace('\"', '\'').substring(0, resulttjd.length() - 1);
                        } else {
                            resulttjd = resulttjd.replace('\"', '\'');
                        }
                    }
                    formInfo.put("tjd", resulttjd);
                    formInfo.put("hwzl", otherInfoJson.getString("hwzl"));
                    formInfo.put("zj", otherInfoJson.getString("zj"));
                    formInfo.put("whzdg", otherInfoJson.getString("whzdg"));
                    formInfo.put("qycg", otherInfoJson.getString("qycg"));
                    formInfo.put("zs", otherInfoJson.getString("zs"));
                    formInfo.put("jbrdh", otherInfoJson.getString("jbrdh"));
                    formInfo.put("qyclzbzl", otherInfoJson.getString("qyclzbzl"));
                    formInfo.put("qycc", otherInfoJson.getString("qycc"));
                    formInfo.put("mastercs", otherInfoJson.getString("mastercs"));
                    formInfo.put("gclhp", otherInfoJson.getString("gclhp"));
                    formInfo.put("gcllx", otherInfoJson.getString("gcllx"));
                    formInfo.put("xssjjssj", otherInfoJson.getString("xssjjssj"));
                    formInfo.put("chzdc", otherInfoJson.getString("chzdc"));
                    formInfo.put("zhfb", otherInfoJson.getString("zhfb"));
                    formInfo.put("hwmc", otherInfoJson.getString("hwmc"));
                    formInfo.put("gclts", otherInfoJson.getString("gclts"));
                    formInfo.put("gclzbzl", otherInfoJson.getString("gclzbzl"));
                    formInfo.put("chzdk", otherInfoJson.getString("chzdk"));
                    formInfo.put("chzdg", otherInfoJson.getString("chzdg"));

                    formInfo.put("hwzdk", otherInfoJson.getString("hwzdk"));
                    formInfo.put("gcclsbdm", otherInfoJson.getString("gcclsbdm"));
                    formInfo.put("qyclcpxh", otherInfoJson.getString("qyclcpxh"));
                    formInfo.put("qycczs", otherInfoJson.getString("qycczs"));
                    formInfo.put("lts", otherInfoJson.getString("lts"));
                    formInfo.put("tyshxydm", otherInfoJson.getString("tyshxydm"));
                    formInfo.put("jbrsfzh", otherInfoJson.getString("jbrsfzh"));
                    formInfo.put("masterId", otherInfoJson.getString("masterId"));
                    formInfo.put("qyclhp", otherInfoJson.getString("qyclhp"));
                    formInfo.put("qycllx", otherInfoJson.getString("qycllx"));
                    formInfo.put("qyclts", otherInfoJson.getString("qyclts"));
                    formInfo.put("dlysjyxkzh", otherInfoJson.getString("dlysjyxkzh"));
                }
            }

            acceptMap.put("formInfo", formInfo);
            Map<String, Object> acceptJsonRequest = new HashMap<String, Object>();
            acceptJsonRequest.put("postdata", acceptMap);
            // 请求申报数据对接接口
            JSONObject responseAcceptJson = null;
            if ("11".equals(auditTask.getShenpilb())) {
                responseAcceptJson = wavePushInterfaceUtils.acceptDataInfogg(acceptJsonRequest);
            } else if ("01".equals(auditTask.getShenpilb())) {
                responseAcceptJson = wavePushInterfaceUtils.acceptDataInfoXK(acceptJsonRequest);
            } else {
                responseAcceptJson = wavePushInterfaceUtils.acceptDataInfo(acceptJsonRequest);

            }
            if (responseAcceptJson != null && "200".equals(responseAcceptJson.getString("state"))) {
                auditProject.set("ISSYNACWAVE", "1");
                iAuditProject.updateProject(auditProject);
            } else {
                auditProject.set("ISSYNACWAVE", "2");
                iAuditProject.updateProject(auditProject);
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
