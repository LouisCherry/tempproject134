package com.epoint.ggyjstj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.*;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;

import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.ggyjstj.api.IgetSpDataByAreacode;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglJsgcjgysbaxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglJsgcjgysbaxxbV3Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@RestController("projecttasklistviewaction")
@Scope("request")
public class ProjectTaskListViewAction extends BaseController {
    private static final long serialVersionUID = 8713148757403132395L;
    private AuditSpBusiness dataBean;
    private DataGridModel<AuditProject> model;

    private String projectname;

    private String areacode;
    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IAuditSpPhase iauditspphase;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ISpglJsgcjgysbaxxbV3Service spglJsgcjgysbaxxbV3Service;

    private List<SelectItem> belongToAreaModel;


    private Date beginTime;
    private Date endTime;
    private String taskId="";
    private String formid="";
    private AuditTask auditTask;
    private AuditTaskExtension auditTaskExtension;
    public ProjectTaskListViewAction() {
    }

    public void pageLoad() {
        String taskGuid = getRequestParameter("taskGuid");
        if (StringUtil.isBlank(taskGuid)){
            addCallbackParam("errmsg","必填参数为空,请联系管理员处理！");
            return;
        }
        auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
        if (auditTask==null){
            addCallbackParam("errmsg","当前事项不存在，请刷新后再重试！");
            return;
        }
         taskId = auditTask.getTask_id();
         auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskGuid,false).getResult();
        if (auditTaskExtension!=null){
            formid=auditTaskExtension.getStr("formid");
        }
    }





    public DataGridModel<AuditProject> getDataGridData() {
        if (this.model == null) {
            this.model = new DataGridModel<AuditProject>() {
                private static final long serialVersionUID = 2196016255341333981L;

                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlConditionUtil=new SqlConditionUtil();
                    sqlConditionUtil.eq("TASK_ID",taskId);
                    sqlConditionUtil.isNotBlank("BIGUID");
                    if (StringUtil.isNotBlank(projectname)) {
                        sqlConditionUtil.like("projectname", projectname);
                    }
                    if (StringUtil.isNotBlank(areacode)) {
                        sqlConditionUtil.eq("areacode", areacode);
                    }
                    if (beginTime!=null && endTime!=null){
                        sqlConditionUtil.between("applydate",beginTime,endTime);
                    }
                    sqlConditionUtil.setOrderDesc("applydate");
                    PageData<AuditProject> pageData = iAuditProject.getAuditProjectPageData("*", sqlConditionUtil.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    List<AuditProject> list = pageData.getList();
                    if (EpointCollectionUtils.isNotEmpty(list)){
                        for (AuditProject au:list ) {
                            if (StringUtil.isNotBlank(au.getAreacode())){
                                AuditOrgaArea result = iAuditOrgaArea.getAreaByAreacode(au.getAreacode()).getResult();
                                if (result!=null){
                                    au.set("areaname",result.getXiaquname());
                                }
                            }
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }

        return this.model;
    }


    /**
     * 导出
     * type=1,导出第一页；type=2,导出选择记录，type=3,导出全部
     */
    public void exportAllData( String type) {

        if (StringUtil.isBlank(type)) {
            type = "1";
        }
        StringBuilder nameBuilder = new StringBuilder(auditTask.getTaskname() + "办件统计");
        nameBuilder.append("_").append(".xls");
        String defaultFileName = nameBuilder.toString();
        StringBuilder pathBuilder = new StringBuilder();
        String newFile = request.getServletContext().getRealPath("epointtemp");
        pathBuilder.append(newFile).append(File.separator).append(defaultFileName);
        log.info(org.apache.commons.lang3.StringUtils.center("开始导出办件数据", 30, "="));
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            //开始构建表头
            HSSFSheet sheet = wb.createSheet(auditTask.getTaskname() + "办件统计");//创建sheet页
            HSSFFont curFont = wb.createFont();                    //设置字体
            curFont.setCharSet(HSSFFont.DEFAULT_CHARSET);                //设置中文字体，那必须还要再对单元格进行编码设置
            curFont.setFontHeightInPoints((short) 12);                //字体大小

            HSSFFont greenFont = wb.createFont();
            BeanUtils.copyProperties(curFont, greenFont);
            greenFont.setColor(IndexedColors.GREEN.getIndex());
            HSSFFont redFont = wb.createFont();
            BeanUtils.copyProperties(curFont, redFont);
            redFont.setColor(IndexedColors.RED.getIndex());

            HSSFRow hssfRowTopHead = sheet.createRow(0);//第一行  表头
            String[] headers = new String[]{"项目名称", "项目代码", "项目类型", "建设性质", "拟开工时间", "拟建成时间",
                    "总投资（万元）", "所属行业", "是否技改项目", "土地获取方式","土地是否带设计方案","是否完成区域评估","立项类型","项目经纬度坐标","工程范围",
                    "项目投资来源","工程分类","用地面积","建筑面积","新增用地面积","农用地面积","资金来源","财政资金来源","量化建设规模的类别","量化建设规模的数值","量化建设规模的单位",
                    "建设地点","建设地点详情","建设地点行政区划","建设规模及内容","是否线性工程","长度"
            };//固定列

            List<String> allHeaders = new ArrayList<>(Arrays.asList(headers));
            int areaStartIndex = 32;//辖区表头开始索引值(从第9列开始)
            if (!"房屋建筑和市政基础设施工程竣工验收备案".equals(auditTask.getTaskname())){
                String[] projectheaders = new String[]{"申报时间", "申请人类型", "申请人", "申请人证照编号", "申请人证照类型", "地址",
                        "法人代表身份证", "法人代表", "联系人身份证", "联系人","联系电话","手机","备注"
                };//固定列
                allHeaders.addAll(Arrays.asList(projectheaders));
                areaStartIndex=45;
            }
            else {
                String[] projectheaders = new String[]{"工程代码", "行政区划代码", "审批事项实例编码", "建设单位", "建设单位代码", "建设单位类型",
                        "建设单位项目负责人", "建设单位项目负责人身份证件号码", "建设单位项目负责人身份证件类型", "建设单位项目负责人联系电话","施工许可证编号","竣工验收备案编号","备案机关",
                        "备案机关统一社会信用代码","备案日期","工程名称","备案范围","是否实行联合验收","建设地址","所属县区","项目经纬度坐标","建设规模","开工日期","竣工日期","实际造价","总建筑面积",
                        "其中，地上建筑面积（㎡）","其中，地下建筑面积（㎡）","联系人/代理人","联系人手机号","档案验收意见","档案移交状态","数据上传状态"
                };//固定列
                allHeaders.addAll(Arrays.asList(projectheaders));
                areaStartIndex=65;
            }
            List<String> formheaders=new ArrayList<>();
            Map<String,String> map=new HashMap<>();
            if (StringUtil.isNotBlank(formid)){
                //添加电子表单字段表头
                String epointsformurl = configService.getFrameConfigValue("epointsformurl");
                if (StringUtil.isNotBlank(epointsformurl) && !epointsformurl.endsWith("/")) {
                    epointsformurl = epointsformurl + "/";
                }
                    //查询表单
                    JSONObject param = new JSONObject();
                    JSONObject taskparam = new JSONObject();
                    taskparam.put("formId", formid);
                    param.put("params", taskparam);
                    String result = HttpUtil.doPost(epointsformurl + "rest/sform/getEpointSformInfo", param);
                    if(StringUtil.isNotBlank(result)){
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        int intValue = jsonObject.getJSONObject("status").getIntValue("code");
                        if(intValue==1){
                            JSONArray jsonObjectStructList = jsonObject.getJSONObject("custom").getJSONObject("formData").getJSONArray("structList");
                            if(EpointCollectionUtils.isEmpty(jsonObjectStructList)){

                            }

                            // 遍历 jsonObjectStructList
                            for (int i = 0; i < jsonObjectStructList.size(); i++) {
                                JSONObject item = jsonObjectStructList.getJSONObject(i);
                                String fieldchinesename = item.getString("fieldchinesename"); //中文名称
                                String fieldname = item.getString("fieldname");  //英文名称
                                allHeaders.add(fieldchinesename);
                                formheaders.add(fieldchinesename);
                                map.put(fieldchinesename,fieldname);
                            }
                        }
                        else{

                        }
                    }

            }

            //创建第一行表头的单元格
            HSSFFont curFontBold = wb.createFont();
            BeanUtils.copyProperties(curFont, curFontBold);
            curFontBold.setBold(true);//加粗
            for (int i = 0; i < allHeaders.size(); i++) {
                HSSFCell box = hssfRowTopHead.createCell(i);
                box.setCellValue(allHeaders.get(i));
                setDefaultStyleOfBox(box, curFontBold);
            }
            sheet.setDefaultColumnWidth((short) 20);

            HSSFCellStyle nonePowerStyle = wb.createCellStyle();

            nonePowerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//灰色背景  无事权
            nonePowerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            nonePowerStyle.setAlignment(HorizontalAlignment.CENTER);
            nonePowerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle hasPowerStyle = wb.createCellStyle();
            hasPowerStyle.cloneStyleFrom(nonePowerStyle);
            hasPowerStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());//白色背景 有事权


            int rowCount = 1;//行号
            List<AuditProject> list = new ArrayList<>();
            //TODO 查询数据
            if (StringUtil.isBlank(type) || "1".equals(type)){
                list=exportSlectData();
            }
            else if ("2".equals(type)){
                list=exportFirstPageData();
            }
            else {
                list=exportAllDatalist();
            }

            if (EpointCollectionUtils.isNotEmpty(list)) {
                for (AuditProject auditProject : list) {
                    HSSFRow currentRow = sheet.createRow(rowCount);
                    //项目信息

                    String biguid = auditProject.getBiguid();
                    if (StringUtil.isNotBlank(biguid)){
                        AuditSpInstance spInstance = auditSpInstanceService.getDetailByBIGuid(biguid).getResult();
                        if (spInstance != null) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(spInstance.getYewuguid()).getResult();
                            if (auditRsItemBaseinfo!=null){
                                exportitemData( currentRow,auditRsItemBaseinfo,curFont);
                            }
                        }
                    }
                    if (!"房屋建筑和市政基础设施工程竣工验收备案".equals(auditTask.getTaskname())){
                        exportProjectData( currentRow,auditProject,curFont);
                    }
                    else {
                        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
                        if (auditSpISubapp != null) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo = rsItemBaseinfoService
                                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                            if (auditRsItemBaseinfo != null) {
                                String xzqhdm = "370800";
                                String gcdm = auditRsItemBaseinfo.getItemcode();
                                String spsxslbm = auditProject.getFlowsn();
                                SpglJsgcjgysbaxxbV3 spgljsgcjgysbaxxbv3 = spglJsgcjgysbaxxbV3Service.findDominByCondition(xzqhdm, gcdm, spsxslbm);
                                if(spgljsgcjgysbaxxbv3!=null){
                                    exportgxhformData( currentRow,spgljsgcjgysbaxxbv3,curFont);
                                }
                            }
                        }

                    }

                    //开始处理电子表单数据的单元格
                    if (EpointCollectionUtils.isNotEmpty(formheaders)){
                        //调用获取电子表单业务信息接口
                        String epointsformurl = configService.getFrameConfigValue("epointsformurl");
                        if (StringUtil.isNotBlank(epointsformurl) && !epointsformurl.endsWith("/")) {
                            epointsformurl = epointsformurl + "/";
                        }
                        String getPageDataurl = epointsformurl + "rest/sform/getPageData";
                        String formguid=auditProject.getRowguid();
                        if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
                            addCallbackParam("subappguid", auditProject.getSubappguid());
                            String formids = auditTaskExtension.getStr("formid");
                            if (StringUtil.isNotBlank(formids)) {
                                AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
                                AuditSpPhase auditSpPhase = iauditspphase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
                                if (StringUtil.isNotBlank(auditSpPhase.getStr("formid"))) {
                                    formguid= subapp.getRowguid();

                                }
                            }
                        }
//                        formguid="02f03877-8ea4-4f44-b453-0f144ffd4122";
                        Map<String, Object> param = new HashMap<>();
                        JSONObject object = new JSONObject();
                        object.put("formId", formid);
                        object.put("rowGuid", formguid);
                        param.put("params", object);
                        String result = HttpUtil.doPost(getPageDataurl, param);
                        if (StringUtil.isNotBlank(result)) {
                            JSONObject obj1 = JSONObject.parseObject(result);
                            JSONObject recordData = obj1.getJSONObject("custom").getJSONObject("recordData");
                            JSONArray mainList = recordData.getJSONArray("mainList");
                            for (int k = areaStartIndex; k < allHeaders.size(); k++) {
                                String header = allHeaders.get(k);
                                HSSFCell catalogLevelCelli = currentRow.createCell(k);//
                                String name = map.get(header);
                               ouputloop: for (int i = 0; i<mainList.size();i++) {
                                    JSONObject jsonObject = mainList.getJSONObject(i);
                                    JSONArray rowList = jsonObject.getJSONArray("rowList");
                                    for (int j = 0; j<rowList.size();j++) {
                                        JSONObject rowObject = rowList.getJSONObject(j);
                                        if (name.equals(rowObject.getString("FieldName"))){
                                            catalogLevelCelli.setCellValue(StringUtil.isNotBlank(rowObject.getString("text"))?rowObject.getString("text"):rowObject.getString("value"));
                                            break ouputloop;
                                        }
                                    }
                                }

                                setDefaultStyleOfBox(catalogLevelCelli, curFont);
                            }

                        }


                    }
                    rowCount++;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            wb = null;
        }
        finally {
            log.info(StringUtils.center("导出数据完成", 30, "="));
            if (wb != null) {
                try {
                    wb.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (wb == null) {
            addCallbackParam("msg", "导出失败，请联系管理员！");
            return;
        }

        //保存到附件
        File file = FileManagerUtil.createFile(pathBuilder.toString());
        OutputStream outputStream1 = null;
        FileInputStream fileInputStream = null;

        try {
            // 写excel表格
            outputStream1 = new FileOutputStream(pathBuilder.toString());
            wb.write(outputStream1);
            // 存入附件表中
            fileInputStream = new FileInputStream(file);
            String fileTyle = file.getName().substring(file.getName().lastIndexOf('.'));
            String attachFileName = file.getName();
            FrameAttachInfo attachInfo = new FrameAttachInfo();
            attachInfo.setAttachFileName(attachFileName);
            attachInfo.setAttachGuid(UUID.randomUUID().toString());
            attachInfo.setContentType(fileTyle);
            attachInfo.setUploadDateTime(new Date());
            IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            iAttachService.addAttach(attachInfo, fileInputStream);
            // 附件下载地址
            String attachDownPageUrl = iAttachService.getAttachDownPath(attachInfo);
            HttpServletRequest request = getRequestContext().getReq();
            attachDownPageUrl = WebUtil.getRequestCompleteUrl(request) + "/" + attachDownPageUrl;
            addCallbackParam("msg", "导出成功！");
            addCallbackParam("path", attachDownPageUrl);
        }
        catch (IOException e) {
            log.error("证照导出异常!", e);
            addCallbackParam("msg", "导出失败，请联系管理员！");
            e.printStackTrace();
        }
        finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream1 != null) {
                    outputStream1.close();

                }
                if (wb != null) {
                    wb.close();
                }
                // 文件删除
                if (file.exists()) {
                    boolean delete = file.delete();
                    if (delete) {
                        log.info("文件 " + file.getName() + "删除成功");
                    }
                    else {
                        log.info("删除失败");
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 导出项目信息
     */
    public void exportitemData(HSSFRow currentRow,AuditRsItemBaseinfo auditRsItemBaseinfo,HSSFFont curFont){
        HSSFCell catalogNameCell = currentRow.createCell(0);
        catalogNameCell.setCellValue(auditRsItemBaseinfo.getItemname());
        setDefaultStyleOfBox(catalogNameCell, curFont);
        HSSFCell catalogNameCell1 = currentRow.createCell(1);
        catalogNameCell1.setCellValue(auditRsItemBaseinfo.getItemcode());
        setDefaultStyleOfBox(catalogNameCell1, curFont);
        HSSFCell catalogNameCell2 = currentRow.createCell(2);
        catalogNameCell2.setCellValue(codeItemsService.getItemTextByCodeName("审批流程类型",auditRsItemBaseinfo.getItemtype()));
        setDefaultStyleOfBox(catalogNameCell2, curFont);
        HSSFCell catalogNameCell3 = currentRow.createCell(3);
        catalogNameCell3.setCellValue(codeItemsService.getItemTextByCodeName("建设性质",auditRsItemBaseinfo.getConstructionproperty()));
        setDefaultStyleOfBox(catalogNameCell3, curFont);
        HSSFCell catalogNameCell4 = currentRow.createCell(4);
        catalogNameCell4.setCellValue(auditRsItemBaseinfo.getItemstartdate());
        setDefaultStyleOfBox(catalogNameCell4, curFont);
        HSSFCell catalogNameCell5 = currentRow.createCell(5);
        catalogNameCell5.setCellValue(auditRsItemBaseinfo.getItemfinishdate());
        setDefaultStyleOfBox(catalogNameCell5, curFont);
        HSSFCell catalogNameCell6 = currentRow.createCell(6);
        catalogNameCell6.setCellValue(auditRsItemBaseinfo.getTotalinvest());
        setDefaultStyleOfBox(catalogNameCell6, curFont);
        HSSFCell catalogNameCell7 = currentRow.createCell(7);
        catalogNameCell7.setCellValue(codeItemsService.getItemTextByCodeName("所属行业",auditRsItemBaseinfo.getBelongtindustry()));
        setDefaultStyleOfBox(catalogNameCell7, curFont);
        HSSFCell catalogNameCell8 = currentRow.createCell(8);
        catalogNameCell8.setCellValue(codeItemsService.getItemTextByCodeName("是否",auditRsItemBaseinfo.getIsimprovement()));
        setDefaultStyleOfBox(catalogNameCell8, curFont);
        HSSFCell catalogNameCell9 = currentRow.createCell(9);
        catalogNameCell9.setCellValue(codeItemsService.getItemTextByCodeName("土地获取方式",String.valueOf(auditRsItemBaseinfo.getTdhqfs())));
        setDefaultStyleOfBox(catalogNameCell9, curFont);
        HSSFCell catalogNameCell10 = currentRow.createCell(10);
        catalogNameCell10.setCellValue(codeItemsService.getItemTextByCodeName("是否",String.valueOf(auditRsItemBaseinfo.getTdsfdsjfa())));
        setDefaultStyleOfBox(catalogNameCell10, curFont);
        HSSFCell catalogNameCell11 = currentRow.createCell(11);
        catalogNameCell11.setCellValue(codeItemsService.getItemTextByCodeName("是否",String.valueOf(auditRsItemBaseinfo.getSfwcqypg())));
        setDefaultStyleOfBox(catalogNameCell11, curFont);
        HSSFCell catalogNameCell12 = currentRow.createCell(12);
        catalogNameCell12.setCellValue(codeItemsService.getItemTextByCodeName("国标_立项类型",auditRsItemBaseinfo.getStr("lxlx")));
        setDefaultStyleOfBox(catalogNameCell12, curFont);
        HSSFCell catalogNameCell13 = currentRow.createCell(13);
        catalogNameCell13.setCellValue(auditRsItemBaseinfo.getStr("xmjwdzb"));
        setDefaultStyleOfBox(catalogNameCell13, curFont);
        HSSFCell catalogNameCell14 = currentRow.createCell(14);
        catalogNameCell14.setCellValue(auditRsItemBaseinfo.getStr("gcfw"));
        setDefaultStyleOfBox(catalogNameCell14, curFont);
        HSSFCell catalogNameCell15 = currentRow.createCell(15);
        catalogNameCell15.setCellValue(codeItemsService.getItemTextByCodeName("项目投资来源",auditRsItemBaseinfo.getStr("xmtzly")));
        setDefaultStyleOfBox(catalogNameCell15, curFont);
        HSSFCell catalogNameCell16 = currentRow.createCell(16);
        catalogNameCell16.setCellValue(codeItemsService.getItemTextByCodeName("工程分类",auditRsItemBaseinfo.getStr("gcfl")));
        setDefaultStyleOfBox(catalogNameCell16, curFont);
        HSSFCell catalogNameCell17 = currentRow.createCell(17);
        catalogNameCell17.setCellValue(auditRsItemBaseinfo.getStr("landarea"));
        setDefaultStyleOfBox(catalogNameCell17, curFont);
        HSSFCell catalogNameCell18 = currentRow.createCell(18);
        catalogNameCell18.setCellValue(auditRsItemBaseinfo.getStr("jzmj"));
        setDefaultStyleOfBox(catalogNameCell18, curFont);
        HSSFCell catalogNameCell19 = currentRow.createCell(19);
        catalogNameCell19.setCellValue(auditRsItemBaseinfo.getStr("newlandarea"));
        setDefaultStyleOfBox(catalogNameCell19, curFont);
        HSSFCell catalogNameCell20 = currentRow.createCell(20);
        catalogNameCell20.setCellValue(auditRsItemBaseinfo.getStr("agriculturallandarea"));
        setDefaultStyleOfBox(catalogNameCell20, curFont);
        HSSFCell catalogNameCell21 = currentRow.createCell(21);
        catalogNameCell21.setCellValue(codeItemsService.getItemTextByCodeName("资金来源",auditRsItemBaseinfo.getStr("fundsources")));
        setDefaultStyleOfBox(catalogNameCell21, curFont);
        HSSFCell catalogNameCell22 = currentRow.createCell(22);
        catalogNameCell22.setCellValue(codeItemsService.getItemTextByCodeName("财政资金来源",auditRsItemBaseinfo.getStr("financialresources")));
        setDefaultStyleOfBox(catalogNameCell22, curFont);
        HSSFCell catalogNameCell23 = currentRow.createCell(23);
        catalogNameCell23.setCellValue(auditRsItemBaseinfo.getStr("quantifyconstructtype"));
        setDefaultStyleOfBox(catalogNameCell23, curFont);
        HSSFCell catalogNameCell24 = currentRow.createCell(24);
        catalogNameCell24.setCellValue(auditRsItemBaseinfo.getStr("quantifyconstructcount"));
        setDefaultStyleOfBox(catalogNameCell24, curFont);
        HSSFCell catalogNameCell25 = currentRow.createCell(25);
        catalogNameCell25.setCellValue(auditRsItemBaseinfo.getStr("quantifyconstructdept"));
        setDefaultStyleOfBox(catalogNameCell25, curFont);
        HSSFCell catalogNameCell26 = currentRow.createCell(26);
        catalogNameCell26.setCellValue(auditRsItemBaseinfo.getStr("constructionsite"));
        setDefaultStyleOfBox(catalogNameCell26, curFont);
        HSSFCell catalogNameCell27 = currentRow.createCell(27);
        catalogNameCell27.setCellValue(auditRsItemBaseinfo.getStr("constructionsitedesc"));
        setDefaultStyleOfBox(catalogNameCell27, curFont);
        HSSFCell catalogNameCell28 = currentRow.createCell(28);
        catalogNameCell28.setCellValue(codeItemsService.getItemTextByCodeName("行政区划",auditRsItemBaseinfo.getStr("jsddxzqh")));
        setDefaultStyleOfBox(catalogNameCell28, curFont);
        HSSFCell catalogNameCell29 = currentRow.createCell(29);
        catalogNameCell29.setCellValue(auditRsItemBaseinfo.getStr("constructionscaleanddesc"));
        setDefaultStyleOfBox(catalogNameCell29, curFont);
        HSSFCell catalogNameCell30 = currentRow.createCell(30);
        catalogNameCell30.setCellValue(codeItemsService.getItemTextByCodeName("是否",auditRsItemBaseinfo.getStr("SFXXGC")));
        setDefaultStyleOfBox(catalogNameCell30, curFont);
        HSSFCell catalogNameCell31 = currentRow.createCell(31);
        catalogNameCell31.setCellValue(auditRsItemBaseinfo.getStr("cd"));
        setDefaultStyleOfBox(catalogNameCell31, curFont);
    }

    private void setCellValueIfNotNull(HSSFCell cell, Object value, HSSFFont curFont) {
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Date) {
                cell.setCellValue(EpointDateUtil.convertDate2String((Date) value));
            } else {
                cell.setCellValue(String.valueOf(value));
            }
            setDefaultStyleOfBox(cell, curFont);
        }
    }
    /**
     * 导出项目信息
     */
    public void exportgxhformData(HSSFRow currentRow, SpglJsgcjgysbaxxbV3 spgljsgcjgysbaxxbv3, HSSFFont curFont){
        if(currentRow==null || spgljsgcjgysbaxxbv3==null || curFont==null){
            log.info("含有空值");
            log.info(currentRow.toString());
            log.info(spgljsgcjgysbaxxbv3.toString());
            log.info(curFont.toString());
            return;
        }
        HSSFCell catalogNameCell32 = currentRow.createCell(32);
        setCellValueIfNotNull(catalogNameCell32, spgljsgcjgysbaxxbv3.getGcdm(), curFont);

        HSSFCell catalogNameCell33 = currentRow.createCell(33);
        setCellValueIfNotNull(catalogNameCell33, spgljsgcjgysbaxxbv3.getXzqhdm(), curFont);

        HSSFCell catalogNameCell34 = currentRow.createCell(34);
        setCellValueIfNotNull(catalogNameCell34, spgljsgcjgysbaxxbv3.getSpsxslbm(), curFont);

        HSSFCell catalogNameCell35 = currentRow.createCell(35);
        setCellValueIfNotNull(catalogNameCell35, spgljsgcjgysbaxxbv3.getJsdw(), curFont);

        HSSFCell catalogNameCell36 = currentRow.createCell(36);
        setCellValueIfNotNull(catalogNameCell36, spgljsgcjgysbaxxbv3.getJsdwdm(), curFont);

        HSSFCell catalogNameCell37 = currentRow.createCell(37);
        Object jsdwlxText = null;
        if (spgljsgcjgysbaxxbv3.getJsdwlx() != null) {
            jsdwlxText = codeItemsService.getItemTextByCodeName("国标_建设单位类型", String.valueOf(spgljsgcjgysbaxxbv3.getJsdwlx()));
        }
        setCellValueIfNotNull(catalogNameCell37, jsdwlxText, curFont);

        HSSFCell catalogNameCell38 = currentRow.createCell(38);
        setCellValueIfNotNull(catalogNameCell38, spgljsgcjgysbaxxbv3.getJsdwxmfzr(), curFont);

        HSSFCell catalogNameCell39 = currentRow.createCell(39);
        setCellValueIfNotNull(catalogNameCell39, spgljsgcjgysbaxxbv3.getJsfzrzjhm(), curFont);

        HSSFCell catalogNameCell40 = currentRow.createCell(40);
        Object jsfzrzjlxText = null;
        if (spgljsgcjgysbaxxbv3.getJsfzrzjlx() != null) {
            jsfzrzjlxText = codeItemsService.getItemTextByCodeName("国标_证件类型", String.valueOf(spgljsgcjgysbaxxbv3.getJsfzrzjlx()));
        }
        setCellValueIfNotNull(catalogNameCell40, jsfzrzjlxText, curFont);

        HSSFCell catalogNameCell41 = currentRow.createCell(41);
        setCellValueIfNotNull(catalogNameCell41, spgljsgcjgysbaxxbv3.getJsfzrlxdh(), curFont);

        HSSFCell catalogNameCell42 = currentRow.createCell(42);
        setCellValueIfNotNull(catalogNameCell42, spgljsgcjgysbaxxbv3.getSgxkzbh(), curFont);

        HSSFCell catalogNameCell43 = currentRow.createCell(43);
        setCellValueIfNotNull(catalogNameCell43, spgljsgcjgysbaxxbv3.getJgysbabh(), curFont);

        HSSFCell catalogNameCell44 = currentRow.createCell(44);
        setCellValueIfNotNull(catalogNameCell44, spgljsgcjgysbaxxbv3.getBajg(), curFont);

        HSSFCell catalogNameCell45 = currentRow.createCell(45);
        setCellValueIfNotNull(catalogNameCell45, spgljsgcjgysbaxxbv3.getBajgxydm(), curFont);

        HSSFCell catalogNameCell46 = currentRow.createCell(46);
        setCellValueIfNotNull(catalogNameCell46, spgljsgcjgysbaxxbv3.getBarq(), curFont);

        HSSFCell catalogNameCell47 = currentRow.createCell(47);
        setCellValueIfNotNull(catalogNameCell47, spgljsgcjgysbaxxbv3.getGcmc(), curFont);

        HSSFCell catalogNameCell48 = currentRow.createCell(48);
        setCellValueIfNotNull(catalogNameCell48, spgljsgcjgysbaxxbv3.getBafw(), curFont);

        HSSFCell catalogNameCell49 = currentRow.createCell(49);
        Object sfsxlhysText = null;
        if (spgljsgcjgysbaxxbv3.getSfsxlhys() != null) {
            sfsxlhysText = codeItemsService.getItemTextByCodeName("是否", String.valueOf(spgljsgcjgysbaxxbv3.getSfsxlhys()));
        }
        setCellValueIfNotNull(catalogNameCell49, sfsxlhysText, curFont);

        HSSFCell catalogNameCell50 = currentRow.createCell(50);
        setCellValueIfNotNull(catalogNameCell50, spgljsgcjgysbaxxbv3.getJsdz(), curFont);

        HSSFCell catalogNameCell51 = currentRow.createCell(51);
        Object ssqxText = null;
        if (spgljsgcjgysbaxxbv3.getSsqx() != null) {
            ssqxText = codeItemsService.getItemTextByCodeName("行政区划", spgljsgcjgysbaxxbv3.getSsqx());
        }
        setCellValueIfNotNull(catalogNameCell51, ssqxText, curFont);

        HSSFCell catalogNameCell52 = currentRow.createCell(52);
        setCellValueIfNotNull(catalogNameCell52, spgljsgcjgysbaxxbv3.getXmjwdzb(), curFont);

        HSSFCell catalogNameCell53 = currentRow.createCell(53);
        setCellValueIfNotNull(catalogNameCell53, spgljsgcjgysbaxxbv3.getJsgm(), curFont);

        HSSFCell catalogNameCell54 = currentRow.createCell(54);
        setCellValueIfNotNull(catalogNameCell54, spgljsgcjgysbaxxbv3.getKgrq(), curFont);

        HSSFCell catalogNameCell55 = currentRow.createCell(55);
        setCellValueIfNotNull(catalogNameCell55, spgljsgcjgysbaxxbv3.getJgrq(), curFont);

        HSSFCell catalogNameCell56 = currentRow.createCell(56);
        setCellValueIfNotNull(catalogNameCell56, spgljsgcjgysbaxxbv3.getSjzj(), curFont);

        HSSFCell catalogNameCell57 = currentRow.createCell(57);
        setCellValueIfNotNull(catalogNameCell57, spgljsgcjgysbaxxbv3.getZjzmj(), curFont);

        HSSFCell catalogNameCell58 = currentRow.createCell(58);
        setCellValueIfNotNull(catalogNameCell58, spgljsgcjgysbaxxbv3.getDsjzmj(), curFont);

        HSSFCell catalogNameCell59 = currentRow.createCell(59);
        setCellValueIfNotNull(catalogNameCell59, spgljsgcjgysbaxxbv3.getDxjzmj(), curFont);

        HSSFCell catalogNameCell60 = currentRow.createCell(60);
        setCellValueIfNotNull(catalogNameCell60, spgljsgcjgysbaxxbv3.getLxr(), curFont);

        HSSFCell catalogNameCell61 = currentRow.createCell(61);
        setCellValueIfNotNull(catalogNameCell61, spgljsgcjgysbaxxbv3.getLxrsjh(), curFont);

        HSSFCell catalogNameCell62 = currentRow.createCell(62);
        setCellValueIfNotNull(catalogNameCell62, spgljsgcjgysbaxxbv3.getDaysyj(), curFont);

        HSSFCell catalogNameCell63 = currentRow.createCell(63);
        Object daysztText = null;
        if (spgljsgcjgysbaxxbv3.getDayszt() != null) {
            daysztText = codeItemsService.getItemTextByCodeName("国标_档案移交状态", String.valueOf(spgljsgcjgysbaxxbv3.getDayszt()));
        }
        setCellValueIfNotNull(catalogNameCell63, daysztText, curFont);

        HSSFCell catalogNameCell64 = currentRow.createCell(64);
        Object sjscztText = null;
        if (spgljsgcjgysbaxxbv3.getSjsczt() != null) {
            sjscztText = codeItemsService.getItemTextByCodeName("国标_数据上传状态", String.valueOf(spgljsgcjgysbaxxbv3.getSjsczt()));
        }
        setCellValueIfNotNull(catalogNameCell64, sjscztText, curFont);
        setDefaultStyleOfBox(catalogNameCell64, curFont);

    }
    /**
     * 导出项目信息
     */
    public void exportProjectData(HSSFRow currentRow,AuditProject auditProject,HSSFFont curFont){

        HSSFCell catalogNameCell32 = currentRow.createCell(32);
        if (auditProject.getApplydate()!=null){
            catalogNameCell32.setCellValue(EpointDateUtil.convertDate2String(auditProject.getApplydate()));
        }
        else {
            catalogNameCell32.setCellValue("");
        }
        setDefaultStyleOfBox(catalogNameCell32, curFont);
        HSSFCell catalogNameCell33 = currentRow.createCell(33);
        catalogNameCell33.setCellValue(codeItemsService.getItemTextByCodeName("申请人类型",String.valueOf(auditProject.getApplyertype())));
        setDefaultStyleOfBox(catalogNameCell33, curFont);
        HSSFCell catalogNameCell34 = currentRow.createCell(34);
        catalogNameCell34.setCellValue(auditProject.getApplyername());
        setDefaultStyleOfBox(catalogNameCell34, curFont);
        HSSFCell catalogNameCell35 = currentRow.createCell(35);
        catalogNameCell35.setCellValue(auditProject.getCertnum());
        setDefaultStyleOfBox(catalogNameCell35, curFont);
        HSSFCell catalogNameCell36 = currentRow.createCell(36);
        if (ZwfwConstant.CERT_TYPE_SFZ.equals(auditProject.getCerttype())) {
            catalogNameCell36.setCellValue( "身份证");
        }
        else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(auditProject.getCerttype())) {
            catalogNameCell36.setCellValue( "工商营业执照");
        }
        else if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(auditProject.getCerttype())) {
            catalogNameCell36.setCellValue("统一社会信用代码");
        }
        else if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditProject.getCerttype())) {
            catalogNameCell36.setCellValue( "组织机构代码证");
        }
        setDefaultStyleOfBox(catalogNameCell36, curFont);
        HSSFCell catalogNameCell37 = currentRow.createCell(37);
        catalogNameCell37.setCellValue(auditProject.getAddress());
        setDefaultStyleOfBox(catalogNameCell37, curFont);
        HSSFCell catalogNameCell38 = currentRow.createCell(38);
        catalogNameCell38.setCellValue(auditProject.getLegalid());
        setDefaultStyleOfBox(catalogNameCell38, curFont);
        HSSFCell catalogNameCell39 = currentRow.createCell(39);
        catalogNameCell39.setCellValue(auditProject.getLegal());
        setDefaultStyleOfBox(catalogNameCell39, curFont);
        HSSFCell catalogNameCell40 = currentRow.createCell(40);
        catalogNameCell40.setCellValue(auditProject.getContactcertnum());
        setDefaultStyleOfBox(catalogNameCell40, curFont);
        HSSFCell catalogNameCell41 = currentRow.createCell(41);
        catalogNameCell41.setCellValue(auditProject.getContactperson());
        setDefaultStyleOfBox(catalogNameCell41, curFont);
        HSSFCell catalogNameCell42 = currentRow.createCell(42);
        catalogNameCell42.setCellValue(auditProject.getContactphone());
        setDefaultStyleOfBox(catalogNameCell42, curFont);
        HSSFCell catalogNameCell43 = currentRow.createCell(43);
        catalogNameCell43.setCellValue(auditProject.getContactmobile());
        setDefaultStyleOfBox(catalogNameCell43, curFont);
        HSSFCell catalogNameCell44 = currentRow.createCell(44);
        catalogNameCell44.setCellValue(auditProject.getRemark());
        setDefaultStyleOfBox(catalogNameCell44, curFont);

    }
    /**
     * 查询选择的数据
     * @return
     */
    public List<AuditProject> exportSlectData() {
        List<String> selectKeys = getDataGridData().getSelectKeys();
        List<AuditProject> list = new ArrayList<>();
        if (EpointCollectionUtils.isNotEmpty(selectKeys)) {
            for (String key : selectKeys) {
                AuditProject result = iAuditProject.getAuditProjectByRowGuid("*", key, "").getResult();
                if (result != null) {
                    list.add(result);
                }

            }
        }
        return list;

    }

    public List<AuditProject> exportFirstPageData() {
        return getDataGridData().getWrappedData();

    }
    public List<AuditProject> exportAllDatalist() {
        SqlConditionUtil sqlConditionUtil=new SqlConditionUtil();
        sqlConditionUtil.eq("TASK_ID",taskId);
        sqlConditionUtil.isNotBlank("BIGUID");
        if (StringUtil.isNotBlank(projectname)) {
            sqlConditionUtil.like("projectname", projectname);
        }
        if (StringUtil.isNotBlank(areacode)) {
            sqlConditionUtil.eq("areacode", areacode);
        }
        if (beginTime!=null && endTime!=null){
            sqlConditionUtil.between("applydate",beginTime,endTime);
        }
        PageData<AuditProject> pageData = iAuditProject.getAuditProjectPageData("*", sqlConditionUtil.getMap(), 0, 10000, "", "").getResult();
        return pageData.getList();
    }
    private void setDefaultStyleOfBox(HSSFCell cell, HSSFFont font) {
        CellUtil.setFont(cell, font);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);//垂直居中
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_LEFT, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_TOP, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_RIGHT, BorderStyle.THIN);
        CellUtil.setCellStyleProperty(cell, CellUtil.BORDER_BOTTOM, BorderStyle.THIN);
    }


    public AuditSpBusiness getDataBean() {
        if (this.dataBean == null) {
            this.dataBean = new AuditSpBusiness();
        }

        return this.dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }


    public String getProjectname() {
        return this.projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
    public String getAreacode() {
        return this.areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }
    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public List<SelectItem> getBelongToAreaModel() {
        if (belongToAreaModel == null) {
            belongToAreaModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "所属区县", null, false));
        }
        return this.belongToAreaModel;
    }
}
