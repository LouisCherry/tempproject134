package com.epoint.zbxfdj.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.impl.SpglXmclbzqdxxbService;
import com.epoint.zbxfdj.xfdj.spglxmjbxxb.api.IGxhSpglXmjbxxbService;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 工改与省消防平台对接
 *
 * @author lgb
 * @description
 * @date 2022年12月22日09:57:51
 */
@RestController
@RequestMapping("/tsprojectdata")
public class TsProjectDataRest {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final String E_FORM_URL = ConfigUtil.getConfigValue("datasyncjdbc", "eformUrl");

    @Autowired
    IAuditProject iAuditProject;

    @Autowired
    IAuditSpInstance iAuditSpInstance;

    @Autowired
    IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IParticipantsInfoService participantsInfoService;

    @Autowired
    private IOuService ouService;

    /**
     * 材料api
     */
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IGxhSpglXmjbxxbService iGxhSpglXmjbxxbService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String tsprojectdata(String projectGuid) {
        try {
            // 逻辑处理
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("rowguid", projectGuid);
            // 拿到办件信息
            List<AuditProject> auditProjects = iAuditProject.getAuditProjectListByCondition(sqlConditionUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(auditProjects)) {
                AuditProject auditProject = auditProjects.get(0);
                String subAppGuid = auditProject.getSubappguid();
                // 区域代码
                String xzqhdm = auditProject.getAreacode();
                xzqhdm = StringUtil.substring(xzqhdm, 0, 6);
                // 流水号
                String lsh = auditProject.getFlowsn();

                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditProject.getBiguid()).getResult();
                String yewuguidxm = auditSpInstance.getYewuguid();
                AuditRsItemBaseinfo auditRsItemBaseinfoXm = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguidxm)
                        .getResult();

                // 项目代码
                String xmdm = auditRsItemBaseinfoXm.getItemcode();
                // 项目名称
                String xmmc = auditRsItemBaseinfoXm.getItemname();
                // 建设单位统一信用代码
                String ussc = auditRsItemBaseinfoXm.getItemlegalcertnum();

                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
                String yewuguid = auditSpISubapp.getYewuguid();
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(yewuguid)
                        .getResult();
                // 工程代码
                String gcdm = auditRsItemBaseinfo.getItemcode();
                // 工程名称
                String gcmc = auditRsItemBaseinfo.getItemname();
                // 审批流程类型  只对接【建设工程消防设计审查事项】
                String splclx = "";
                // 如果是消防事项，调用接口
                String taskguid = auditProject.getTaskguid();
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                if ("建设工程消防设计审查".equals(auditTask.getTaskname()) || "建设工程消防设计审查（设区的市级权限）（新设）".equals(auditTask.getTaskname()) ||
                        "建设工程消防设计审查（县级权限）（新设）".equals(auditTask.getTaskname())) {
                    splclx = "1";
                } else if ("特殊建设工程消防验收".equals(auditTask.getTaskname()) || "建设工程消防验收（设区的市级权限）（新设）".equals(auditTask.getTaskname()) ||
                        "建设工程消防验收（县级权限）（新设）".equals(auditTask.getTaskname())) {
                    splclx = "2";
                } else if ("其他建设工程消防验收备案抽查".equals(auditTask.getTaskname()) || "建设工程消防验收备案".equals(auditTask.getTaskname())) {
                    splclx = "3";
                }

                // 入前置库
                SpglXmjbxxb spglXmjbxxb = new SpglXmjbxxb();
                //yangjia 2023/06/15 判断该项目是否是复查的
                SqlConditionUtil sql = new SqlConditionUtil();
                //工程代码
                sql.eq("xmdm", gcdm);
                //工程名称
                sql.eq("xmmc", gcmc);
                //推送状态
                sql.eq("sjsczt", "4");
                //事项类型
                sql.eq("splclx", splclx);
                sql.setOrderDesc("sbsj");
                // 流水号
                spglXmjbxxb.put("lsh", lsh);
                // 地方数据主键
                spglXmjbxxb.setDfsjzj(projectGuid);
                // 行政区划代码
                spglXmjbxxb.setXzqhdm(xzqhdm);
                // 项目代码
                spglXmjbxxb.setXmdm(xmdm);
                // 项目名称
                spglXmjbxxb.setXmmc(xmmc);
                // 工程代码
                spglXmjbxxb.setGcdm(gcdm);
                // 工程名称
                spglXmjbxxb.set("gcmc", gcmc);
                // 审批流程类型
                int splclxInt = Integer.valueOf(splclx);
                spglXmjbxxb.setSplclx(splclxInt);
                // 申报时间
                spglXmjbxxb.setSbsj(new Date());

                JSONObject forms = new JSONObject();
                // 消防设计审查
                if (splclxInt == 1) {
                    forms = projectDto(subAppGuid, "879", projectGuid, "1", auditTask, ussc, yewuguidxm);
                }
                // 消防验收
                else if (splclxInt == 2) {
                    forms = projectDto(subAppGuid, "771", projectGuid, "2", auditTask, ussc, yewuguidxm);
                }
                // 消防验收备案
                else if (splclxInt == 3) {
                    forms = projectDto(subAppGuid, "774", projectGuid, "3", auditTask, ussc, yewuguidxm);
                }

                // 申请表单json
                spglXmjbxxb.set("forms_json", forms);

                // 数据有效标识
                spglXmjbxxb.setSjyxbs(1);
                // 数据更新时间
                Date date = EpointDateUtil
                        .convertString2Date(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                spglXmjbxxb.set("sjgxsj", date);

                // 数据状态标识
                spglXmjbxxb.setSjsczt(0);
                spglXmjbxxb.setSql_TableName("SPGL_XMJBXXB");
                spglXmjbxxb.set("ID", UUID.randomUUID().toString());
                spglXmjbxxb.setRowguid(UUID.randomUUID().toString());
                //iGxhSpglXmjbxxbService.insert(spglXmjbxxb);
                iGxhSpglXmjbxxbService.insertHighGo(spglXmjbxxb);
                XfqzktsService xfqzktsService = new XfqzktsService();
                // 消防设计审查
                if (splclxInt == 1) {
                    //查电子表单信息推送
                    xfqzktsService.insertxfsc(subAppGuid, auditProject, xmdm, gcdm, gcmc, lsh);
                }
                // 消防验收
                else if (splclxInt == 2) {
                    xfqzktsService.insertxfys(subAppGuid, auditProject, xmdm, gcdm, gcmc, lsh);
                }
                // 消防验收备案
                else if (splclxInt == 3) {
                    xfqzktsService.insertxfba(subAppGuid, auditProject, xmdm, gcdm, gcmc, lsh);
                }
                return JsonUtils.zwdtRestReturn("1", "推送成功", "");
            } else {
                log.info("未查询到办件");
                return JsonUtils.zwdtRestReturn("0", "未找到办件！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "推送失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/sendBZlist", method = RequestMethod.POST)
    public String sendBZlist(String projectguid) {
        try {
            SpglXmclbzqdxxbService spglxmclbzqdxxbservice = new SpglXmclbzqdxxbService();
            // 逻辑处理
            com.epoint.common.util.SqlConditionUtil auditProjectSql = new com.epoint.common.util.SqlConditionUtil();
            auditProjectSql.eq("rowguid", projectguid);
            // 拿到办件信息
            AuditProject auditProject = iAuditProject.getAuditProjectByCondition(auditProjectSql.getMap());
            // 区域代码
            String xzqhdm = auditProject.getAreacode();
            xzqhdm = StringUtil.substring(xzqhdm, 0, 6);
            // 流水号
            String lsh = auditProject.getFlowsn();

            String taskguid = auditProject.getTaskguid();
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
            String splclx = "";
            if ("建设工程消防设计审查".equals(auditTask.getTaskname()) || "建设工程消防设计审查（设区的市级权限）（新设）".equals(auditTask.getTaskname())) {
                splclx = "1";
            } else if ("特殊建设工程消防验收".equals(auditTask.getTaskname()) || "建设工程消防验收（设区的市级权限）（新设）".equals(auditTask.getTaskname())) {
                splclx = "2";
            } else if ("其他建设工程消防验收备案抽查".equals(auditTask.getTaskname())) {
                splclx = "3";
            }

            // 审批流程类型
            int splclxInt = Integer.valueOf(splclx);
            List fireFileDtos = new ArrayList();
            JSONObject forms = new JSONObject();
            // 消防设计审查
            if (splclxInt == 1) {
                fireFileDtos = fireFileDtos(auditProject.getRowguid(), "1");
                // 申请表单json
                forms.put("FireFileDtos", fireFileDtos);
            }
            // 消防验收
            else if (splclxInt == 2) {
                // FireFileDtos
                fireFileDtos = fireFileDtos(auditProject.getRowguid(), "2");
                // 申请表单json
                forms.put("FireFileDtos", fireFileDtos);
            }
            // 消防验收备案
            else if (splclxInt == 3) {
                // FireFileDtos
                fireFileDtos = fireFileDtos(auditProject.getRowguid(), "3");
                // 申请表单json
                forms.put("FireFileDtos", fireFileDtos);
            }
            Date date = EpointDateUtil.convertString2Date(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            spglxmclbzqdxxbservice.insert(lsh, xzqhdm, UUID.randomUUID().toString(), 0, "1", date, forms.toString());
            return JsonUtils.zwdtRestReturn("1", "补正推送成功", "");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======接口入参为=======" + projectguid);
            log.info("=======异常信息为：=======" + e.getStackTrace());
            return JsonUtils.zwdtRestReturn("0", "推送失败", e.getMessage());
        }
    }

    private Record exchangeStringtoboolean(Record record, String[] filenames) {
        if (filenames.length > 0) {
            for (String filename : filenames) {
                if (StringUtil.isNotBlank(record.getStr(filename))) {
                    if (record.getStr(filename).equals(ZwfwConstant.CONSTANT_INT_ZERO + "")) {
                        record.set(filename, false);
                    } else {
                        record.set(filename, true);
                    }
                }
            }
        }
        return record;
    }

    private String getContent(String url, Map<String, String> stringMap) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        try {
            // 设置提交方式
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            // 添加参数
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (stringMap.size() != 0) {
                Set keySet = stringMap.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String k = it.next().toString();// key
                    String v = stringMap.get(k);// value
                    nameValuePairs.add(new BasicNameValuePair(k, v));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 获得http响应体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 响应的结果
                String content = EntityUtils.toString(entity, "UTF-8");
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取数据错误";
    }


    /**
     * 消防设计审查文件名包含转换成类型
     *
     * @param materialName
     * @return
     */
    public int convertMaterialName2TypeCodeOne(String materialName) {
        if (materialName.contains("消防设计审查申请表")) {
            return 1;
        } else if (materialName.contains("建设工程规划许可")) {
            return 2;
        } else if (materialName.contains("临时性建筑批准")) {
            return 3;
        } else if (materialName.contains("消防设计")) {
            return 4;
        } else if (materialName.contains("山东省施工图设计文件审查合格书")) {
            return 5;
        } else if (materialName.contains("消防设计审查技术审查意见")) {
            return 6;
        } else if (materialName.contains("特殊消防设计技术资料")) {
            return 8;
        } else {
            //其他
            return 7;
        }
    }

    /**
     * 消防验收文件名包含转换成类型
     *
     * @param materialName
     * @return
     */
    public int convertMaterialName2TypeCodeTwo(String materialName) {
        // 文件类型（消防验收）
        if (materialName.contains("消防验收申请表")) {
            return 11;
        } else if (materialName.contains("工程竣工验收报告")) {
            return 12;
        } else if (materialName.contains("建设工程竣工图纸")) {
            return 13;
        } else if (materialName.equals("建设工程规划许可证")) {
            return 14;
        } else {
            //其他
            return 19;
        }

    }

    /**
     * 消防验收备案文件名包含转换成类型
     *
     * @param materialName
     * @return
     */
    public int convertMaterialName2TypeCodeThree(String materialName) {
        // 6.17文件类型（消防验收备案）
        if (materialName.contains("建设工程消防验收备案告知承诺书")) {
            return 20;
        } else if (materialName.contains("消防验收备案")) {
            return 21;
        } else if (materialName.contains("工程竣工验收报告")) {
            return 22;
        } else if (materialName.contains("建设工程竣工图纸")) {
            return 23;
        } else if (materialName.equals("建设工程规划许可证")) {
            return 24;
        } else {
            //其他
            return 29;
        }
    }

    /**
     * 文件名包含转换成类型
     *
     * @param materialName
     * @return
     */
    public int convertMaterialName2TypeCode(String materialName, String typeCode) {

        if ("1".equals(typeCode)) {
            return convertMaterialName2TypeCodeOne(materialName);
        }
        if ("2".equals(typeCode)) {
            return convertMaterialName2TypeCodeTwo(materialName);
        }
        if ("3".equals(typeCode)) {
            return convertMaterialName2TypeCodeThree(materialName);
        }
        return 0;
    }

    /**
     * @param subAppGuid
     * @param formId      表单id
     * @param projectGuid
     * @param typeCode    事项类型
     * @param auditTask
     * @param ussc        建设单位统一信用代码
     * @param ywGuid      用于查询建设单位
     * @return
     */
    public JSONObject projectDto(String subAppGuid, String formId, String projectGuid,
                                 String typeCode, AuditTask auditTask, String ussc, String ywGuid) {
        // 从电子表单获取数据
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> paramMap = new HashMap<>();
        JSONObject reqParam = new JSONObject();
        reqParam.put("formId", formId);
        reqParam.put("rowGuid", subAppGuid);
        paramMap.put("params", reqParam.toJSONString());


        Record projectRecord = new Record();

        List<Record> fireMonomerDtoList = new ArrayList<>();
        List<Record> companyDtoList = new ArrayList<>();
        List<Record> companyOpinionDtoList = new ArrayList<>();

        String result = getContent(E_FORM_URL + "sform/getPageData", paramMap);
        // 解析表单json
        if (StringUtil.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject recordData = jsonObject.getJSONObject("custom").getJSONObject("recordData");
            // 主表 基本信息
            JSONArray mainList = recordData.getJSONArray("mainList");
            log.info("====TsProjectDataRest====主表信息：" + mainList);
            if (CollectionUtils.isNotEmpty(mainList)) {
                JSONArray rowList = mainList.getJSONObject(0).getJSONArray("rowList");
                // 解析rowList
                if (CollectionUtils.isNotEmpty(rowList)) {
                    rowList.forEach(e -> {
                        JSONObject row = (JSONObject) e;
                        projectRecord.set(row.getString("FieldName"), row.getString("value") == null ? "" : row.getString("value"));
                    });
                }
            }
            // 子表信息
            JSONArray subList = recordData.getJSONArray("subList");
            log.info("====TsProjectDataRest====子表信息：" + subList);
            if (CollectionUtils.isNotEmpty(subList)) {
                // 循环子表
                subList.forEach(e -> {
                    JSONObject subTable = (JSONObject) e;
                    // 主表记录只有一条
                    log.info("====TsProjectDataRest====subTable信息：" + subTable);
                    JSONObject subRecord = subTable.getJSONArray("mainRecordList").getJSONObject(0);
                    String subTableName = subRecord.getString("subTableName");
                    if (StringUtil.isNotBlank(subTableName) && subTableName.contains("dwxxzb")) {
                        getSubTableList(companyDtoList, subRecord);
                    } else if (StringUtil.isNotBlank(subTableName) && subTableName.contains("gcqk")) {
                        getSubTableList(fireMonomerDtoList, subRecord);
                    }
                });
            }
        }

        // 办件信息
        Record projectDto = new Record();

        // 项目代码
        projectDto.set("ProjectCode", StringUtil.isNotBlank(projectRecord.getStr("xmdm")) ? projectRecord.getStr("xmdm") : projectRecord.getStr("ProjectCode"));
        // 工程名称
        projectDto.set("ProjectName", StringUtil.isNotBlank(projectRecord.getStr("gcmc")) ? projectRecord.getStr("gcmc") : projectRecord.getStr("ProjectName"));
        // 建设单位名称
        projectDto.set("ConstructionName", StringUtil.isNotBlank(projectRecord.getStr("jsdw")) ? projectRecord.getStr("jsdw") : projectRecord.getStr("ConstructionName"));
        // 建设单位统一信用代码
        projectDto.set("ConstructionUscc", ussc);
        // 建设单位法定代表人
        projectDto.set("ConstructionLegalRepresentative", projectRecord.getStr("fddbr"));
        if ("1".equals(typeCode)) {
            projectDto.set("ConstructionLegalRepresentative", projectRecord.getStr("ConstructionLegalRepresentative"));
        }
        // 建设单位法定代表人身份证号
        projectDto.set("ConstructionLegalRepresentativeIdCard", projectRecord.getStr("ConstructionLegalRepresentativeIdCard"));
        // 查询建设单位
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("corptype", "31");
        sqlConditionUtil.eq("itemguid", ywGuid);
        List<ParticipantsInfo> sjList = participantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
        if (CollectionUtils.isNotEmpty(sjList)) {
            if (StringUtil.isNotBlank(sjList.get(0).getStr("legalpersonicardnum"))) {
                projectDto.set("ConstructionLegalRepresentativeIdCard", sjList.get(0).getStr("legalpersonicardnum"));
            }
        }

        // 建设单位法定代表人联系电话
        projectDto.set("ConstructionLegalContact", projectRecord.getStr("fddbrlxdh"));
        // 项目负责人
        projectDto.set("Contact", StringUtil.isNotBlank(projectRecord.getStr("Contact")) ? projectRecord.getStr("Contact") : projectRecord.getStr("xiangmufzrmc"));
        // 项目负责人身份证号
        projectDto.set("ContactIdCard", projectRecord.getStr("ContactIdCard"));
        // 项目负责人联系电话
        projectDto.set("ContactNo", StringUtil.isNotBlank(projectRecord.getStr("ContactNo")) ? projectRecord.getStr("ContactNo") : projectRecord.getStr("lianxdh"));
        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("areacode", projectGuid, null).getResult();
        if (auditProject != null) {
            // 项目所在区域代码
            projectDto.set("AreaCode", auditProject.getAreacode());
        }
        //  事项特有字段
        if ("1".equals(typeCode)) {
            projectDto.set("IsDrawingReview", "1".equals(projectRecord.getStr("sfywcjsgcsgtsjwjsc")));
            projectDto.set("ConstructionName", StringUtil.isNotBlank(projectRecord.getStr("jiansdw")) ? projectRecord.getStr("jiansdw") : projectRecord.getStr("ConstructionName"));
        }

        // 工程地址
        projectDto.set("ConstructionSite", projectRecord.getStr("gcdz"));
        // 经纬度

        String jwd = projectRecord.getStr("jwd");
        if (StringUtil.isNotBlank(jwd) && jwd.split(" ").length > 1) {
            projectDto.set("Longitude", jwd.split(" ")[0]);
            projectDto.set("Latitude", jwd.split(" ")[1]);
        }
        // 类别
        projectDto.set("BuildNatureCode", StringUtil.isNotBlank(projectRecord.getStr("BuildNatureCode")) ? projectRecord.getStr("BuildNatureCode") : projectRecord.getStr("lb"));
        // 项目类型
        projectDto.set("ProjectType", projectRecord.getStr("jzgcsysx"));
        // 审查合格日期
        if ("2".equals(typeCode)) {
            // 《特殊建设工程消防设计审查意见书》文号（审查意见为合格的）
            projectDto.set("DesignReviewNo", StringUtil.isNotBlank(projectRecord.getStr("tsjsycxfsjscyjs")) ? projectRecord.getStr("tsjsycxfsjscyjs") : projectRecord.getStr("DesignReviewNo"));
            Date designReviewTime = projectRecord.getDate("schgrq") != null ? projectRecord.getDate("schgrq") : projectRecord.getDate("DesignReviewTime");
            projectDto.set("DesignReviewTime", EpointDateUtil.convertDate2String(designReviewTime, "yyyy-MM-dd"));
        }
        // 特殊消防设计
        projectDto.set("IsSpecialDesign", "1".equals(projectRecord.getStr("tsxfsj")));
        // 建筑工程施工许可证号、批准开工报告编号或证明文件编号（依法需办理的）
        projectDto.set("ConstructionPermit", StringUtil.isNotBlank(projectRecord.getStr("ConstructionPermit")) ? projectRecord.getStr("ConstructionPermit") : projectRecord.getStr("jzgcsgxkzh"));
        // 制证日期
        projectDto.set("ConstructionDate", EpointDateUtil.convertDate2String(projectRecord.getDate("zzrq"), "yyyy-MM-dd"));
        // 审图编号
        projectDto.set("DrawingNumber", projectRecord.getStr("DrawingNumber"));
        // 备案事项特有字段
        if ("3".equals(typeCode)) {
            projectDto.set("DrawingNumber", projectRecord.getStr("stbh"));
            // 是否复查
            projectDto.set("IsReview", "1".equals(projectRecord.getStr("IsReview")));
            // 备案凭证文号
            projectDto.set("CompletionCertificateNo", projectRecord.getStr("CompletionCertificateNo"));
            // 备案表编号
            projectDto.set("CompletionFormCode", projectRecord.getStr("CompletionFormCode"));
            // 建设工程消防验收备案抽（复）查结果通知书文号
            projectDto.set("CompletionNoticeNo", projectRecord.getStr("CompletionNoticeNo"));
            // 存在问题整改情况
            projectDto.set("CompletionRectification", projectRecord.getStr("CompletionRectification"));
            // 其他需要说明情况
            projectDto.set("CompletionRemark", projectRecord.getStr("CompletionRemark"));
        }
        if ("1".equals(typeCode)) {
            // 建筑高度大于250m的建筑采取加强性消防设计措施
            if ("1".equals(projectRecord.getStr("jzgddy250mdjzcqjqxxfsjcs"))) {
                projectDto.set("IsStrengthen", true);
            } else if ("0".equals(projectRecord.getStr("jzgddy250mdjzcqjqxxfsjcs"))) {
                projectDto.set("IsStrengthen", false);
            }
            // 建设工程规划许可证件（依法需办理的）
            projectDto.set("BuildingPermit", projectRecord.getStr("BuildingPermit"));
        }
        // 临时性建筑批准文件依法需办理的
        projectDto.set("TempApprovePermit", projectRecord.getStr("lsxjzpzwjyfxbld"));

        // 工程投资额(万元)
        projectDto.set("TotalInvestment", StringUtil.isNotBlank(projectRecord.getStr("TotalInvestment")) ? projectRecord.getStr("TotalInvestment") : projectRecord.getStr("gctze"));
        // 总建筑面积
        projectDto.set("TotalBuildArea", projectRecord.getStr("TotalBuildArea"));
        if ("3".equals(typeCode)) {
            // 是否承诺制
            projectDto.set("IsPromise", "1".equals(projectRecord.getStr("sfcn")));
        }

        if (auditTask != null) {
            FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(auditTask.getOuguid());
            if (frameOuExtendInfo != null) {
                // 住建单位统一社会信用代码
                projectDto.set("CompanyUscc", frameOuExtendInfo.getStr("ORGCODE"));
            }
            // 住建单位名称
            projectDto.set("CompanyName", auditTask.getOuname());
        }
        // 特殊建设工程情形
        String str = projectRecord.getStr("jzzmjdyewpfmd") + "," +
                projectRecord.getStr("jzzmjdyywwqpfmd") + "," +
                projectRecord.getStr("jzzmjdyywpfmd") + "," +
                projectRecord.getStr("jzzmjdyeqwbpfmd") + "," +
                projectRecord.getStr("jzzmjdyyqpfmd") + "," +
                projectRecord.getStr("jzzmjdywbpfmd") + "," +
                projectRecord.getStr("gjgcjsxfjsbzgddylgczzjz") + "," +
                projectRecord.getStr("csjtfdgc") + "," +
                projectRecord.getStr("yrybwxwp") + "," +
                projectRecord.getStr("gjjgbgl") + "," +
                projectRecord.getStr("sybtdyxzdlxslqxdjsgc") + "," +
                projectRecord.getStr("btdsxdsyxgdywddtjzmjdyswpfmhzjzgdcgwsmdggjz");
        projectDto.set("SpecialSituation", str);
        // 装修部位
        projectDto.set("DecorationPart", StringUtil.isNotBlank(projectRecord.getStr("zxbw")) ? projectRecord.getStr("zxbw") : projectRecord.getStr("DecorationPart"));
        // 装修面积（㎡）
        projectDto.set("DecorationArea", projectRecord.getStr("zxmj"));
        // 装修所在层数
        projectDto.set("DecorationFloor", projectRecord.getStr("zxszcs"));
        // 使用性质
        projectDto.set("Nature", projectRecord.getStr("syxz"));
        // 原有用途
        projectDto.set("OriginalPurpose", projectRecord.getStr("yyyt"));
        if ("1".equals(typeCode)) {
            // 消防设计审查字段

            // 材料类别
            projectDto.set("MaterialsCategory", projectRecord.getStr("cllb"));
            // 保温所在层数
            projectDto.set("InsulationFloor", projectRecord.getStr("bwszcs"));
            // 保温部位
            projectDto.set("InsulationPart", projectRecord.getStr("bwbw"));
            // 保温材料
            projectDto.set("InsulationMaterials", projectRecord.getStr("bwcl"));
            // 消防设施及其他
            projectDto.set("FireFacilitiesCategory", projectRecord.getStr("xfssjqt"));
            // 工程简要说明
            projectDto.set("Brief", projectRecord.getStr("gcjysm"));
            // 备注
            projectDto.set("Remark", projectRecord.getStr("beiz"));
        } else if ("2".equals(typeCode) || "3".equals(typeCode)) {
            // 特殊建设工程消防验收

            // 外墙保温材料
            projectDto.set("InsulationMaterialsOut", projectRecord.getStr("bwszcs"));
            // 外墙保温材料类别
            projectDto.set("MaterialsCategoryOut", projectRecord.getStr("cllb"));
            // 屋面保温材料
            projectDto.set("InsulationMaterialsTop", projectRecord.getStr("bwcl"));
            // 屋面保温材料类别
            projectDto.set("MaterialsCategoryTop", projectRecord.getStr("caillb"));
            if ("3".equals(typeCode)) {
                projectDto.set("MaterialsCategoryTop", projectRecord.getStr("cllb2"));
            }
            // 其他保温材料、材料类别
            projectDto.set("MaterialsOther", projectRecord.getStr("qtbwclcllb"));
            // 备注
            projectDto.set("Remark", projectRecord.getStr("Remark"));
            if ("3".equals(typeCode)) {
                projectDto.set("MaterialsCategoryTop", projectRecord.getStr("beiz"));
                // 竣工验收合格日期
                projectDto.set("CompletionAcceptanceDate", EpointDateUtil.convertDate2String(projectRecord.getDate("jungyshgrq"), "yyyy-MM-dd"));
            }
            // 单位意见

            Record opinionTypeOne = new Record();
            opinionTypeOne.set("companyName", projectRecord.getStr("jsfwjg"));
            opinionTypeOne.set("OpinionType", "1");
            opinionTypeOne.set("Principal", projectRecord.getStr("xmfzrmc"));
            opinionTypeOne.set("Opinion", projectRecord.getStr("sggczxfssjcqk"));
            companyOpinionDtoList.add(opinionTypeOne);

            Record opinionTypeTwo = new Record();
            opinionTypeTwo.set("companyName", projectRecord.getStr("jiansdw"));
            opinionTypeTwo.set("OpinionType", "2");
            opinionTypeTwo.set("Principal", projectRecord.getStr("xiangmfzrmc"));
            opinionTypeTwo.set("Opinion", projectRecord.getStr("jsgcjgysxfcyqkjyj"));
            companyOpinionDtoList.add(opinionTypeTwo);

            Record opinionTypeThree = new Record();
            opinionTypeThree.set("companyName", projectRecord.getStr("sjdw"));
            opinionTypeThree.set("OpinionType", "3");
            opinionTypeThree.set("Principal", projectRecord.getStr("xiangmfzrmc1"));
            opinionTypeThree.set("Opinion", projectRecord.getStr("jschgdxfsjwjssqk"));
            companyOpinionDtoList.add(opinionTypeThree);

            Record opinionTypeFour = new Record();
            opinionTypeFour.set("companyName", projectRecord.getStr("jldw"));
            opinionTypeFour.set("OpinionType", "4");
            opinionTypeFour.set("Principal", projectRecord.getStr("xmzjlgcsmc"));
            opinionTypeFour.set("Opinion", projectRecord.getStr("gcjlqk"));
            companyOpinionDtoList.add(opinionTypeFour);

            Record opinionTypeFive = new Record();
            opinionTypeFive.set("companyName", projectRecord.getStr("sgdw"));
            opinionTypeFive.set("OpinionType", "5");
            opinionTypeFive.set("Principal", projectRecord.getStr("xmjlmc"));
            opinionTypeFive.set("Opinion", projectRecord.getStr("gcsgqk"));
            companyOpinionDtoList.add(opinionTypeFive);

            Record opinionTypeSix = new Record();
            opinionTypeSix.set("companyName", projectRecord.getStr("jisfwjg"));
            opinionTypeSix.set("OpinionType", "6");
            opinionTypeSix.set("Principal", projectRecord.getStr("xiangmfzrmc2"));
            opinionTypeSix.set("Opinion", projectRecord.getStr("xfssxnxtgnldlsqk"));
            companyOpinionDtoList.add(opinionTypeSix);
        }


        // 转换建筑信息字段
        List<Record> fireMonomerDtoResultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fireMonomerDtoList)) {
            fireMonomerDtoList.forEach(e -> {
                Record fireMonomerDto = new Record();
                // 建筑名称
                fireMonomerDto.set("EngineeringName", e.getStr("EngineeringName"));
                // 结构类型
                fireMonomerDto.set("StructuralSystemCode", e.getStr("StructuralSystemCode"));
                // 使用性质
                fireMonomerDto.set("Nature", e.getStr("Nature"));
                // 耐火等级（地上）
                fireMonomerDto.set("FireResisRateCode", e.getStr("FireResisRateCode"));
                // 耐火等级（地下）
                fireMonomerDto.set("FireResisRateCodeDown", e.getStr("FireResisRateCodeDown"));
                // 地上层数
                fireMonomerDto.set("LandUpLayerNumber", e.getStr("LandUpLayerNumber"));
                // 地下层数
                fireMonomerDto.set("LandDownLayerNumber", e.getStr("LandDownLayerNumber"));
                // 高度
                fireMonomerDto.set("BuildingHeight", e.getStr("BuildingHeight"));
                // 长度
                fireMonomerDto.set("BuildingLength", e.getStr("BuildingLength"));
                // 占地面积
                fireMonomerDto.set("FootprintArea", e.getStr("FootprintArea"));
                // 建筑面积（地上）
                fireMonomerDto.set("BuildingUpArea", e.getStr("BuildingUpArea"));
                // 建筑面积（地下）
                fireMonomerDto.set("BuildingDownArea", e.getStr("BuildingDownArea"));
                // 火宅危险性类别
                fireMonomerDto.set("FireDangerCategoryCode", e.getStr("FireDangerCategoryCode"));
                fireMonomerDtoResultList.add(fireMonomerDto);
            });

        }

        // 获取单位信息
        List<Record> companyDtoResultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(companyDtoList)) {
            companyDtoList.forEach(e -> {
                Record companyDto = new Record();
                if ("1".equals(typeCode)) {
                    // 单位名称
                    companyDto.set("CompanyName", e.getStr("CompanyName"));
                    // 单位类型
                    companyDto.set("CompanyType", e.getStr("CompanyType"));
                    // 单位统一社会信用代码
                    companyDto.set("Uscc", e.getStr("Uscc"));
                    // 设计专业
                    companyDto.set("DesignMajor", e.getStr("DesignMajor"));
                    // 是否总包
                    companyDto.set("IsGeneral", "1".equals(e.getStr("IsGeneral")));
                    // 资质等级
                    companyDto.set("QualificationLevel", e.getStr("zzdj"));
                    // 法定代表人
                    companyDto.set("LegalRepresentative", e.getStr("LegalRepresentative"));
                    // 法定代表人身份证号
                    companyDto.set("LegalRepresentativeIdCard", e.getStr("LegalRepresentativeIdCard"));
                    // 项目负责人
                    companyDto.set("Principal", e.getStr("Principal"));
                    // 项目负责人身份证号
                    companyDto.set("PrincipalIdCard", e.getStr("PrincipalIdCard"));
                    // 联系电话(移动电话和座机)
                    companyDto.set("ContactNo", e.getStr("ContactNo"));
                } else {
                    // 单位名称
                    companyDto.set("CompanyName", e.getStr("dwmc"));
                    // 单位类型
                    companyDto.set("CompanyType", e.getStr("dwlb"));
                    // 单位统一社会信用代码
                    companyDto.set("Uscc", e.getStr("Uscc"));
                    // 设计专业
                    companyDto.set("DesignMajor", e.getStr("sjzy"));
                    // 是否总包
                    companyDto.set("IsGeneral", "1".equals(e.getStr("IsGeneral")));
                    // 资质等级
                    companyDto.set("QualificationLevel", e.getStr("zzdj"));
                    // 法定代表人
                    companyDto.set("LegalRepresentative", e.getStr("LegalRepresentative"));
                    // 法定代表人身份证号
                    companyDto.set("LegalRepresentativeIdCard", e.getStr("fddbr"));
                    // 项目负责人
                    companyDto.set("Principal", e.getStr("Principal"));
                    // 项目负责人身份证号
                    companyDto.set("PrincipalIdCard", e.getStr("xmfzr"));
                    // 联系电话(移动电话和座机)
                    companyDto.set("ContactNo", e.getStr("lxdh"));
                }
                companyDtoResultList.add(companyDto);
            });
        }

        // ----单位意见信息---
        Record opinionDto = new Record();
        // 单位名称
        opinionDto.set("CompanyName", projectRecord.getStr(""));
        // 意见类型
        opinionDto.set("OpinionType", projectRecord.getStr(""));
        // 项目负责人名称/项目总监理工程师名称/项目经理名称
        opinionDto.set("Principal", projectRecord.getStr(""));
        // 意见
        opinionDto.set("Opinion", projectRecord.getStr(""));


        // ---- 附件信息----


        JSONObject formJson = new JSONObject();
        formJson.put("ProjectDto", projectDto);
        formJson.put("FireMonomerDtos", fireMonomerDtoResultList);
        formJson.put("CompanyDtos", companyDtoResultList);
        formJson.put("FireFileDtos", fireFileDtos(projectGuid, typeCode));
        if ("2".equals(typeCode) || "3".equals(typeCode)) {
            formJson.put("CompanyOpinionDtos", companyOpinionDtoList);
        }

        return formJson;
    }


    private List<Record> getSubTableList(List<Record> recordList, JSONObject mainRecord) {
        JSONArray subRecordList = mainRecord.getJSONArray("subRecordList");
        if (CollectionUtils.isNotEmpty(subRecordList)) {
            subRecordList.forEach(f -> {
                Record record = new Record();
                JSONObject subRecord = (JSONObject) f;
                JSONArray rowList = subRecord.getJSONArray("rowList");
                if (CollectionUtils.isNotEmpty(rowList)) {
                    rowList.forEach(g -> {
                        JSONObject row = (JSONObject) g;
                        record.set(row.getString("FieldName"), row.getString("value") == null ? "" : row.getString("value"));
                    });
                    recordList.add(record);
                }
            });
        }
        return recordList;
    }


    /**
     * 获取FireFileDtos信息
     *
     * @param projectguid
     * @return
     */
    public List<Record> fireFileDtos(String projectguid, String typeCode) {
        // FireFileDtos
        String internetip = iConfigService.getFrameConfigValue("internetip");
        SqlConditionUtil auditProjectMaterialSql = new SqlConditionUtil();
        auditProjectMaterialSql.eq("projectguid", projectguid);

        List<AuditProjectMaterial> auditProjectMaterialList = iAuditProjectMaterial
                .selectProjectMaterialByCondition(auditProjectMaterialSql.getMap()).getResult();
        List<Record> recordList = new ArrayList<>();
        for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {
            // 拿到附件信息
            List<FrameAttachInfo> frameAttachInfos = iAttachService
                    .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
            if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                    Record record = new Record();
                    // 文件名包括文件名后缀
                    record.put("Name", frameAttachInfo.getAttachFileName());
                    // 文件URL
                    record.put("Code", internetip + frameAttachInfo.getAttachGuid());
                    // 文件类型
                    record.put("DirType",
                            convertMaterialName2TypeCode(auditProjectMaterial.getTaskmaterial(), typeCode));
                    recordList.add(record);
                }
            }
        }
        return recordList;
    }
}
