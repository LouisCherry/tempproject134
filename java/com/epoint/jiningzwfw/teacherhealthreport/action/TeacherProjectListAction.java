package com.epoint.jiningzwfw.teacherhealthreport.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.zip.ZipUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.teacherhealthreport.api.IMyAttachService;

/**
 * 教师资格体检报告list页面对应的后台
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@RestController("teacherprojectlistaction")
@Scope("request")
public class TeacherProjectListAction extends BaseController
{
    private Logger log = Logger.getLogger(TeacherProjectListAction.class);
    private static final long serialVersionUID = -4045631566146210366L;

    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;
    @Autowired
    private IMyAttachService iMyAttachService;
    @Autowired
    private IAttachService attachService;
    
    /**
     * 教师资格证办件统计信息
     */
    private Record dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    /**
     * 县区下拉列表model
     */
    private List<SelectItem> countyModel = null;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new Record();
        }

    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                private static final long serialVersionUID = 2239055340019994361L;

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 封装筛选办件的办件名称条件
                    List<String> pnList = new ArrayList<>();
                    pnList.add("51a74101-84c0-44f0-aaff-534e6f82675d");
                    pnList.add("b574893d-024c-4672-8aa0-827b856088c6");
                    pnList.add("5e5afa55-d1a3-48ca-88ab-e079cd1b47dd");
                    pnList.add("5d617e32-9691-4cb2-89b4-426477205e54");
                    pnList.add("a2e57190-6b85-46c6-860b-c9b2830f7a89");
                    pnList.add("e7d6f9da-cb5b-4dea-ab74-e17b7113217d");
                    pnList.add("c328ee00-4e7d-4674-8fd1-43513cdc1edc");
                    pnList.add("c73e6826-1880-437e-b820-7c2e42747c99");
                    pnList.add("42163cde-0795-47e7-82eb-1549af70d601");
                    pnList.add("8b2cfa6a-41ff-475f-aac8-51a24646994a");
                    pnList.add("02921679-9926-4c98-a04b-5a2436a51af7");
                    pnList.add("1220893a-6a10-4a5c-b61f-f954f4e70244");
                    pnList.add("ce9acd2c-0d8c-4e68-be79-363c36d893a1");
                    pnList.add("c9024160-5031-4370-be66-96405b6cbafe");
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.in("task_id", StringUtil.joinSql(pnList));
                    sqlConditionUtil.eq("status", ZwfwConstant.BANJIAN_STATUS_ZCBJ + "");
                    if (StringUtil.isNotBlank(dataBean.getStr("applyername"))) {
                        sqlConditionUtil.like("applyername", dataBean.getStr("applyername"));
                    }
                    if (StringUtil.isNotBlank(dataBean.getStr("areacode"))) {
                        sqlConditionUtil.eq("areacode", dataBean.getStr("areacode"));
                    }
                    if (StringUtil.isNotBlank(dataBean.getStr("certnum"))) {
                        sqlConditionUtil.like("certnum", dataBean.getStr("certnum"));
                    }
                    if (dataBean.getDate("applydateStart") != null) {
                        sqlConditionUtil.ge("applydate",
                                EpointDateUtil.getBeginOfDate(dataBean.getDate("applydateStart")));
                    }
                    if (dataBean.getDate("applydateEnd") != null) {
                        sqlConditionUtil.le("applydate", EpointDateUtil.getEndOfDate(dataBean.getDate("applydateEnd")));
                    }
                    PageData<AuditProject> result = iAuditProject.getAuditProjectPageDataByCondition(
                            "rowguid,flowsn,applyername,certnum,areacode", sqlConditionUtil.getMap(),first,pageSize,null,null,null).getResult();
                    
                    List<AuditProject> projects = result.getList();
                    
                    for (AuditProject project : projects) {
                    	  SqlConditionUtil sqlConditionUtilNew = new SqlConditionUtil();
                    	  sqlConditionUtilNew.eq("projectguid", project.getRowguid());
                    	List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                                .selectProjectMaterialByCondition(sqlConditionUtilNew.getMap()).getResult();
                    	 if (materialList != null && !materialList.isEmpty()) {
                    		 for (AuditProjectMaterial auditProjectMaterial : materialList) {
                    			 if ("彩色免冠一寸电子版照片1张 (必须与中国教师资格网上传照片一致）".equals(auditProjectMaterial.getTaskmaterial())) {
                    				 List<FrameAttachStorage> attachInfoList = attachService
                                             .getAttachListByGuid(auditProjectMaterial.getCliengguid());
                        			 if (!attachInfoList.isEmpty()) {
                        				 project.set("attachguid", attachInfoList.get(0).getAttachGuid()) ;
                        			 }
                    			 }
                    		 }
                    	 }
                    }
                    this.setRowCount(
                            iAuditProject.getAuditProjectCountByCondition(sqlConditionUtil.getMap()).getResult());
                    return result == null ? new ArrayList<>() : result.getList();
                }

            };
        }
        return model;
    }

    public void doZipFile() {
        String guid = getRequestParameter("guid");
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        Date nowDate = new Date();
        sqlConditionUtil.clear();
        sqlConditionUtil.eq("rowguid", guid);
        sqlConditionUtil.eq("status", ZwfwConstant.BANJIAN_STATUS_ZCBJ + "");
        AuditProject auditProject = iAuditProject.getAuditProjectByCondition(sqlConditionUtil.getMap());
        if (auditProject != null) {
            sqlConditionUtil.clear();
            sqlConditionUtil.eq("projectguid", guid);
            List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                    .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
            String filename = "";
            String path = "/opt/epoint/BigFileUpLoadStorage/temp/"
                    + EpointDateUtil.convertDate2String(nowDate, EpointDateUtil.DATE_FORMAT) + File.separator;
            String type = ".png";
            if (materialList != null && !materialList.isEmpty()) {
                for (AuditProjectMaterial auditProjectMaterial : materialList) {
                	List<FrameAttachStorage> attachInfoList = attachService
                            .getAttachListByGuid(auditProjectMaterial.getCliengguid());
                    if (attachInfoList != null && !attachInfoList.isEmpty()) {
                    	FrameAttachStorage frameAttachInfo = attachInfoList.get(0);
                    	type = frameAttachInfo.getContentType();
                    	filename = auditProject.getApplyername() + "_" + auditProject.getCertnum() + frameAttachInfo.getContentType();
                        File createFile = FileManagerUtil
                                .createFile(path+filename);
//                        frameAttachInfo.getFilePath() + frameAttachInfo.getAttachFileName()
                        if (!createFile.exists()) {
                            try {
                                createFile.createNewFile();
                                FileManagerUtil.writeContentToFileByStream(createFile, frameAttachInfo.getContent());;
                            }
                            catch (IOException e) {
                                log.error("==========TeacherHealthReportListAction在创建新文件时异常：" + e.getMessage(), e);
                            }
                        }
                    }
                }
               
                String zipFilePath = path + filename;

                byte[] contentFromSystem = FileManagerUtil.getContentFromSystem(zipFilePath);
                if (contentFromSystem != null) {
                    InputStream byteArrayInputStream = new ByteArrayInputStream(contentFromSystem);
                    this.sendRespose(byteArrayInputStream, filename, type);
                }
                FileManagerUtil.deleteFile(zipFilePath);
            }
        }
    }

    public void doZip() {
        String projectGuids = getRequestParameter("guids");
        Date nowDate = new Date();
        String zipFilePath = "/opt/epoint/BigFileUpLoadStorage/temp/"
                + EpointDateUtil.convertDate2String(nowDate, EpointDateUtil.DATE_FORMAT) + File.separator
                + "教资办件材料汇总包.zip";
        // 首先判断是否存在该附件
        String[] projectArray = projectGuids.split(";");
        List<String> projectguidList = Arrays.asList(projectArray);
        if (projectguidList != null && !projectguidList.isEmpty()) {
            List<File> fileList = new ArrayList<>();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            FrameAttachInfoNewService9 frameAttachInfoNewService9 = new FrameAttachInfoNewService9();
            File pmkFile = FileManagerUtil.createFile("/opt/epoint/BigFileUpLoadStorage/temp/"
                    + EpointDateUtil.convertDate2String(nowDate, EpointDateUtil.DATE_FORMAT) + File.separator);
            if (!pmkFile.exists()) {
                pmkFile.mkdir();
            }
            for (String projectGuid : projectguidList) {
                sqlConditionUtil.clear();
                // 根据办件标识查询办件
                sqlConditionUtil.eq("rowguid", projectGuid);
                List<AuditProject> result = iAuditProject.getAuditProjectListByCondition(sqlConditionUtil.getMap())
                        .getResult();
                if (result != null && !result.isEmpty()) {
                    AuditProject auditProject = result.get(0);
                    sqlConditionUtil.clear();
                    sqlConditionUtil.eq("projectguid", projectGuid);
                    List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                            .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                    if (materialList != null && !materialList.isEmpty()) {
                        for (AuditProjectMaterial auditProjectMaterial : materialList) {
                        	List<FrameAttachStorage> attachInfoList = attachService
                                    .getAttachListByGuid(auditProjectMaterial.getCliengguid());
                            if (attachInfoList != null && !attachInfoList.isEmpty()) {
                                // 将文件做一个汇总到一个文件中
                                for (FrameAttachStorage frameAttachInfo : attachInfoList) {
                                	String filename = auditProject.getApplyername() + "_" + auditProject.getCertnum() + frameAttachInfo.getContentType();
                                    File createFile = FileManagerUtil.createFile(filename);
                                    if (!createFile.exists()) {
                                        try {
                                            createFile.createNewFile();
                                            FileManagerUtil.writeContentToFileByStream(createFile, frameAttachInfo.getContent());;
                                        }
                                        catch (IOException e) {
                                            log.error("==========TeacherHealthReportListAction在创建新文件时异常："
                                                    + e.getMessage(), e);
                                        }
                                    }
                                    fileList.add(createFile);
                                }
                            }
                        }
                    }
                }
            }
            // 对该办件的所有材料做压缩
            File[] fileArray = new File[fileList.size()];
            ZipUtil.doZip(fileList.toArray(fileArray), zipFilePath);
            byte[] contentFromSystem = FileManagerUtil.getContentFromSystem(zipFilePath);
            if (contentFromSystem != null) {
                InputStream byteArrayInputStream = new ByteArrayInputStream(contentFromSystem);
                this.sendRespose(byteArrayInputStream, "教资办件材料汇总包.zip", ".zip");
            }
            FileManagerUtil.deleteFile(zipFilePath);

        }
        else {
            addCallbackParam("info", "请勾选需要导出附件的记录！");
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCountyModel() {
        if (countyModel == null) {
            countyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "县区名称", null, false));
        }
        return this.countyModel;
    }

    public Record getDataBean() {
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }

}
