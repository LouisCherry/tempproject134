package com.epoint.xmz.auditfszl.action;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.annotation.RequestSupport;
import com.epoint.core.dto.annotation.RequestType;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.util.ExcelUtil;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 放射诊疗数据list页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:16]
 */
@RestController("auditfszllistaction")
@Scope("request")
public class AuditFszlListAction extends BaseController {
    @Autowired
    private IAuditFszlService service;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditFszlPersonService auditFszlPersonService;

    @Autowired
    private IAuditFszlDeviceService auditFszlDeviceService;

    /**
     * 放射诊疗数据实体对象
     */
    private AuditFszl dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditFszl> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private DataImportModel9 dataImportModel;


    /*查询入参*/
    // 放射诊疗许可证批准日期
    private String certificateDateStart;
    private String certificateDateEnd;
    // 放射设备校验日期
    private String jydateStart;
    private String jydateEnd;
    // 下一次校验日期
    private String nextjyDateStart;
    private String nextjyDateEnd;
    // 预警类型
    private String type;

    @Autowired
    private ICertInfo iCertInfo;

    @Autowired
    private IAttachService iAttachService;

    public void pageLoad() {
        if(dataBean == null){
            dataBean = new AuditFszl();
        }
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            AuditFszl auditFszl = service.find(sel);
            if(auditFszl != null){
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.eq("fszlid",auditFszl.getFszlid());
                List<AuditFszl> list = service.findList(sqlc.getMap());
                list.forEach(fszl -> {
                    service.deleteByGuid(fszl.getRowguid());
                    auditFszlPersonService.deleteByFszlguid(fszl.getRowguid());
                    auditFszlDeviceService.deleteByFszlguid(fszl.getRowguid());
                });
            }
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<AuditFszl> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditFszl>() {

                @Override
                public List<AuditFszl> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();

                    String whereSql = getWhereSql();

                    List<AuditFszl> list = service.findList(
                            ListGenerator.generateSql("audit_fszl", whereSql, "updatetime", "desc"), first, pageSize,
                            conditionList.toArray());
                    int count = service.countAuditFszl(ListGenerator.generateSql("audit_fszl", whereSql), conditionList.toArray());
                    this.setRowCount(count);

                    list.forEach(auditFszl -> {
                        Date nextjydate = auditFszl.getNextjydate();
                        if(nextjydate != null){
                            long now = System.currentTimeMillis();
                            long l = nextjydate.getTime() - now;
                            if(l <= 0){
                                auditFszl.put("type","已超期");
                            }
                            else if(l <= 1000L * 60 * 60 * 24 * 30){
                                auditFszl.put("type","即将超期");
                            }
                            else if(l <= 1000L * 60 * 60 * 24 * 60){
                                auditFszl.put("type","临近超期");
                            }
                        }
                    });

                    return list;
                }

            };
        }
        return model;
    }


    @RequestSupport(type = {RequestType.GET })
    public void exportExcel() {

        List<Object> conditionList = new ArrayList<Object>();
        List<AuditFszl> list = service.findList(
                ListGenerator.generateSql("audit_fszl", "", "updatetime", "desc"), 0, Integer.MAX_VALUE,
                conditionList.toArray());

        ExcelUtil excelUtil = new ExcelUtil("放射诊疗数据信息");
        excelUtil.setCellStyle((short) 21,true,true);
        excelUtil.setRowData("医疗机构名称","医疗机构登记号","经营地址","经济类型","法定代表人","主要负责人","联系电话","许可项目","放射诊疗许可证批准日期",
                "放射诊疗许可证号","放射设备校验日期","校验有效期","下一次校验日期","上一年度设备状态检测报告出具时间","报告编制单位","许可证状态");
        excelUtil.addNewSheet("工作人员信息");
        excelUtil.setRowData("放射诊疗许可证号","姓名","放射工作人员证编号","执业类别","执业范围","职业分类","上一年度年度职业健康体检日期","上一季度个人剂量检测日期");
        excelUtil.addNewSheet("放射装置信息");
        excelUtil.setRowData("放射诊疗许可证号","装置名称","设备编号","型号","生产厂家","主要参数","所在场所");
        excelUtil.setCellStyle((short) 18,false,false);
        List<CodeItems> status = codeItemsService.listCodeItemsByCodeName("放射诊疗许可证状态");
        HashMap<String,String> statusMap = new HashMap<>();
        status.forEach(codeItems -> {
            statusMap.put(codeItems.getItemValue(),codeItems.getItemText());
        });


        list.forEach(auditFszl -> {
            excelUtil.movePointer(0);
            excelUtil.setRowData(
                    auditFszl.getYljgmc(),
                    auditFszl.getYljgdjh(),
                    auditFszl.getJydz(),
                    auditFszl.getSyzxs(),
                    auditFszl.getFddbr(),
                    auditFszl.getZyfzr(),
                    auditFszl.getLxdh(),
                    auditFszl.getXkxm(),
                    auditFszl.getCertificatedate(),
                    auditFszl.getCertno(),
                    EpointDateUtil.convertDate2String(auditFszl.getJydate(),EpointDateUtil.DATE_FORMAT),
                    auditFszl.getValiditydate(),
                    EpointDateUtil.convertDate2String(auditFszl.getNextjydate(),EpointDateUtil.DATE_FORMAT),
                    EpointDateUtil.convertDate2String(auditFszl.getSyndsbztjcbgcjsj(),EpointDateUtil.DATE_FORMAT),
                    auditFszl.getBaogbzdw(),
                    statusMap.get(auditFszl.getCertstatus())
            );
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("fszlguid",auditFszl.getRowguid());
            List<AuditFszlPerson> auditFszlPersonList = auditFszlPersonService.findList("select * from audit_fszl_person " + new SqlHelper().getSql(sqlc.getMap()));
            if(EpointCollectionUtils.isNotEmpty(auditFszlPersonList)){
                excelUtil.movePointer(1);
                auditFszlPersonList.forEach(auditFszlPerson -> {
                    excelUtil.setRowData(
                            auditFszl.getCertno(),
                            auditFszlPerson.getXm(),
                            auditFszlPerson.getFsgzryzbh(),
                            auditFszlPerson.getZylb(),
                            auditFszlPerson.getZyfw(),
                            auditFszlPerson.getZyfl(),
                            EpointDateUtil.convertDate2String(auditFszlPerson.getSyndndzyjktjrq(),EpointDateUtil.DATE_FORMAT),
                            EpointDateUtil.convertDate2String(auditFszlPerson.getSyjdgrjljcrq(),EpointDateUtil.DATE_FORMAT)
                    );
                });
            }
            List<AuditFszlDevice> auditFszlDeviceList = auditFszlDeviceService.findList("select * from audit_fszl_device " + new SqlHelper().getSql(sqlc.getMap()));
            if(EpointCollectionUtils.isNotEmpty(auditFszlDeviceList)){
                excelUtil.movePointer(2);
                auditFszlDeviceList.forEach(auditFszlDevice -> {
                    excelUtil.setRowData(
                            auditFszl.getCertno(),
                            auditFszlDevice.getZzmc(),
                            auditFszlDevice.getSbbh(),
                            auditFszlDevice.getXh(),
                            auditFszlDevice.getSccj(),
                            auditFszlDevice.getZycs(),
                            auditFszlDevice.getSzcs()
                    );
                });
            }
        });

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            excelUtil.write(outputStream);
            String expName = "放射诊疗数据.xlsx";
            this.sendRespose(outputStream.toByteArray(), expName,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            outputStream.close();
        }
        catch (IOException e) {
            log.info(e);
        }
    }


    /**
     * 许可项目下拉
     *
     * @return
     */
    public List<SelectItem> getXkxmModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可项目");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 许可证状态
     *
     * @return
     */
    public List<SelectItem> getXkzztModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可证状态");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 校验有效期
     * @return
     */
    public List<SelectItem> getJyyyqModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("校验有效期");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }


    private String getWhereSql(){
        SqlConditionUtil sqlc = new SqlConditionUtil();
        // 放射诊疗许可证号
        if(StringUtil.isNotBlank(dataBean.getCertno())){
            sqlc.like("certno",dataBean.getCertno());
        }
        // 医疗机构名称
        if(StringUtil.isNotBlank(dataBean.getYljgmc())){
            sqlc.like("yljgmc",dataBean.getYljgmc());
        }
        // 医疗登记号
        if(StringUtil.isNotBlank(dataBean.getYljgdjh())){
            sqlc.like("yljgdjh",dataBean.getYljgdjh());
        }
        // 法定代表人
        if(StringUtil.isNotBlank(dataBean.getFddbr())){
            sqlc.like("fddbr",dataBean.getFddbr());
        }
        // 许可项目
        if(StringUtil.isNotBlank(dataBean.getXkxm())){
            sqlc.like("xkxm",dataBean.getXkxm());
        }
        // 放射诊疗许可证批准日期
        if(StringUtil.isNotBlank(certificateDateStart)){
            sqlc.ge("CERTIFICATEDATE", EpointDateUtil.getBeginOfDateStr(certificateDateStart));
        }
        if(StringUtil.isNotBlank(certificateDateEnd)){
            sqlc.le("CERTIFICATEDATE", EpointDateUtil.getBeginOfDateStr(certificateDateEnd));
        }
        // 放射设备校验日期
        if(StringUtil.isNotBlank(jydateStart)){
            sqlc.ge("jydate", EpointDateUtil.getBeginOfDateStr(jydateStart));
        }
        if(StringUtil.isNotBlank(jydateEnd)){
            sqlc.le("jydate", EpointDateUtil.getBeginOfDateStr(jydateEnd));
        }
        // 校验有效期
        if(StringUtil.isNotBlank(dataBean.getValiditydate())){
            sqlc.ge("validitydate", dataBean.getValiditydate());
        }
        // 下一次校验日期
        if(StringUtil.isNotBlank(nextjyDateStart)){
            sqlc.ge("nextjydate", EpointDateUtil.getBeginOfDateStr(nextjyDateStart));
        }
        if(StringUtil.isNotBlank(nextjyDateEnd)){
            sqlc.le("nextjydate", EpointDateUtil.getBeginOfDateStr(nextjyDateEnd));
        }
        // 许可证状态
        if(StringUtil.isNotBlank(dataBean.getCertstatus())){
            sqlc.eq("certstatus",dataBean.getCertstatus());
        }
        // 预警类型
        if(StringUtil.isNotBlank(type)){
            Date date = new Date();
            // 临近超期 相差60天
            if("ljcq".equals(type)){
                // 30天后时间
                long l = date.getTime() + 1000L * 60 * 60 * 24 * 30;
                sqlc.gt("nextjydate",EpointDateUtil.convertDate2String(new Date(l),EpointDateUtil.DATE_TIME_FORMAT));
                // 60 天后时间
                l += 1000L * 60 * 60 * 24 * 30;;
                sqlc.le("nextjydate",EpointDateUtil.convertDate2String(new Date(l),EpointDateUtil.DATE_TIME_FORMAT));
            }
            // 即将超期
            else if("jjcq".equals(type)){
                sqlc.gt("nextjydate",EpointDateUtil.convertDate2String(date,EpointDateUtil.DATE_TIME_FORMAT));
                // 30 天后时间
                long l = date.getTime() + 1000L * 60 * 60 * 24 * 30;
                sqlc.le("nextjydate",EpointDateUtil.convertDate2String(new Date(l),EpointDateUtil.DATE_TIME_FORMAT));
            }
            // 已超期
            else if("ycq".equals(type)){
                sqlc.le("nextjydate",EpointDateUtil.convertDate2String(date,EpointDateUtil.DATE_TIME_FORMAT));
            }
        }

        // 排除历史版本
        sqlc.eq("is_history", ZwfwConstant.CONSTANT_STR_ZERO);
        // 辖区
        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());

        return new SqlHelper().getPatchSql(sqlc.getMap());
    }


    public AuditFszl getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszl();
        }
        return dataBean;
    }


    // 导入
    public DataImportModel9 getDataImportModel() {

        // 放射装置
        HashMap<String, List<AuditFszlDevice>> fszzMap = new HashMap<>();
        // 工作人员信息
        HashMap<String, List<AuditFszlPerson>> gzryMap = new HashMap<>();
        // 放射诊疗数据信息
        ArrayList<AuditFszl> fszlList = new ArrayList<>();
        // 经济类型
        List<CodeItems> jjlx = codeItemsService.listCodeItemsByCodeName("竣工许可所有制形式");
        Set<String> jjlxSet = jjlx.stream().map(CodeItems::getItemValue).collect(Collectors.toSet());
        // 许可证状态
        List<CodeItems> xkz = codeItemsService.listCodeItemsByCodeName("放射诊疗许可证状态");
        Set<String> xkzSet = xkz.stream().map(CodeItems::getItemText).collect(Collectors.toSet());
        HashMap<String, String> xkzMap = new HashMap<>();
        xkz.forEach(codeItems -> {
            xkzMap.put(codeItems.getItemText(),codeItems.getItemValue());
        });
        // 校验有效期
        List<CodeItems> jyyxq = codeItemsService.listCodeItemsByCodeName("校验有效期");
        Set<String> jyyxqSet = jyyxq.stream().map(CodeItems::getItemValue).collect(Collectors.toSet());
        // 许可项目
        List<CodeItems> xkxm = codeItemsService.listCodeItemsByCodeName("放射诊疗许可项目");
        Set<String> xkxmSet = xkxm.stream().map(CodeItems::getItemValue).collect(Collectors.toSet());

        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler() {
                private static final long serialVersionUID = 1L;

                private String msg = null;
                boolean isCorrectTemplate = true;

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
                    // 默认导入sheet1的内容
                    try {
                        if (sheet == 0) {
                            if (curRow > 0) {
                                // 先验证模板是否正确
                                if (data == null || data.length != 16) {
                                    msg = "导入文件有误，请使用正确模板";
                                    isCorrectTemplate = false;
                                    return msg;
                                }

                            }
                        }

                        if(isCorrectTemplate){
                            /*保存放射装置信息*/
                            if(sheet == 2){
                                if(curRow > 0){
                                    String certno = data[0].toString();
                                    if(StringUtil.isBlank(certno)){
                                        msg = "放射装置-放射诊疗许可证号存在空值！\n";
                                        return msg;
                                    }
                                    AuditFszlDevice auditFszlDevice = new AuditFszlDevice();
                                    auditFszlDevice.setRowguid(UUID.randomUUID().toString());
                                    auditFszlDevice.setZzmc(data[1].toString());
                                    auditFszlDevice.setSbbh(data[2].toString());
                                    auditFszlDevice.setXh(data[3].toString());
                                    auditFszlDevice.setSccj(data[4].toString());
                                    auditFszlDevice.setZycs(data[5].toString());
                                    auditFszlDevice.setSzcs(data[6].toString());

                                    List<AuditFszlDevice> auditFszlDevices = fszzMap.computeIfAbsent(certno, k -> new ArrayList<>());
                                    auditFszlDevices.add(auditFszlDevice);
                                }
                            }
                            /*保存工作人员信息*/
                            if(sheet == 1){
                                if(curRow > 0){
                                    String certno = data[0].toString();
                                    if(StringUtil.isBlank(certno)){
                                        msg = "工作人员信息-放射诊疗许可证号存在空值！\n";
                                        return msg;
                                    }
                                    AuditFszlPerson auditFszlPerson = new AuditFszlPerson();
                                    auditFszlPerson.setRowguid(UUID.randomUUID().toString());
                                    auditFszlPerson.setXm(data[1].toString());
                                    auditFszlPerson.setFsgzryzbh(data[2].toString());
                                    auditFszlPerson.setZylb(data[3].toString());
                                    auditFszlPerson.setZyfw(data[4].toString());
                                    auditFszlPerson.setZyfl(data[5].toString());

                                    String syndndzyjktjrq = data[6].toString();
                                    String message = checkDate(syndndzyjktjrq);
                                    if(StringUtil.isNotBlank(message)){
                                        return message;
                                    }
                                    else {
                                        auditFszlPerson.setSyndndzyjktjrq(EpointDateUtil.getBeginOfDateStr(syndndzyjktjrq));
                                    }

                                    String syjdgrjljcrq = data[7].toString();
                                    String message2 = checkDate(syjdgrjljcrq);
                                    if(StringUtil.isNotBlank(message2)){
                                        return message2;
                                    }
                                    else {
                                        auditFszlPerson.setSyjdgrjljcrq(EpointDateUtil.getBeginOfDateStr(syjdgrjljcrq));
                                    }

                                    List<AuditFszlPerson> list = gzryMap.computeIfAbsent(certno, k -> new ArrayList<>());
                                    list.add(auditFszlPerson);

                                }
                            }
                            /*保存放射诊疗数据*/
                            if(sheet == 0){
                                if(curRow > 0){
                                    AuditFszl auditFszl = new AuditFszl();
                                    auditFszl.setRowguid(UUID.randomUUID().toString());
                                    auditFszl.setFszlid(UUID.randomUUID().toString());
                                    auditFszl.setYljgmc(data[0].toString());
                                    // 医疗机构登记号
                                    String yljgdjh = data[1].toString();
                                    if(StringUtil.isBlank(yljgdjh)){
                                        msg = "存在依赖机构登记号为空的字段！";
                                        return msg;
                                    }
                                    auditFszl.setYljgdjh(yljgdjh);
                                    auditFszl.setJydz(data[2].toString());

                                    // 经济类型
                                    String jjlx = data[3].toString();
                                    if(StringUtil.isNotBlank(jjlx)){
                                        if(!jjlxSet.contains(jjlx)){
                                            msg = "存在经济类型字段值有不包含在代码项【竣工许可所有制形式】中的值！";
                                            return msg;
                                        }
                                        auditFszl.setSyzxs(jjlx);
                                    }
                                    auditFszl.setFddbr(data[4].toString());
                                    auditFszl.setZyfzr(data[5].toString());
                                    auditFszl.setLxdh(data[6].toString());
                                    // 许可项目
                                    String xkxm = data[7].toString();
                                    if(StringUtil.isNotBlank(xkxm)){
                                        String[] split = xkxm.split(",");
                                        for(int i=0;i<split.length;i++){
                                            if(StringUtil.isNotBlank(split[i]) && !xkxmSet.contains(split[i])){
                                                msg = "存在许可项目不在于代码项【放射诊疗许可项目】中的值！";
                                                return msg;
                                            }
                                        }
                                        auditFszl.setXkxm(xkxm);
                                    }
                                    // 放射诊疗许可证批准日期
                                    String certificateDate = data[8].toString();
                                    if(StringUtil.isNotBlank(certificateDate)){
                                        String message = checkDate(certificateDate);
                                        if(StringUtil.isNotBlank(message)){
                                            msg = "数据格式有误！";
                                            return msg;
                                        }
                                        auditFszl.setCertificatedate(certificateDate);
                                    }
                                    auditFszl.setCertno(data[9].toString());
                                    // 放射设备校验日期
                                    String jydate = data[10].toString();
                                    if(StringUtil.isNotBlank(jydate)){
                                        String message = checkDate(jydate);
                                        if(StringUtil.isNotBlank(message)){
                                            msg = "数据格式有误！";
                                            return msg;
                                        }
                                        auditFszl.setJydate(EpointDateUtil.convertString2Date(jydate,EpointDateUtil.DATE_FORMAT));
                                    }
                                    // 校验有效期
                                    String validitydate = data[11].toString();
                                    if(StringUtil.isNotBlank(validitydate)){
                                        if(!jyyxqSet.contains(validitydate)){
                                            msg = "校验有效期字段存在不在代码项【校验有效期】中的值！";
                                            return msg;
                                        }
                                        auditFszl.setValiditydate(validitydate);
                                    }
                                    // 下一次校验日期
                                    String nextjydate = data[12].toString();
                                    if(StringUtil.isNotBlank(nextjydate)){
                                        String message = checkDate(nextjydate);
                                        if(StringUtil.isNotBlank(message)){
                                            msg = "数据格式有误！";
                                            return msg;
                                        }
                                        auditFszl.setNextjydate(EpointDateUtil.getBeginOfDateStr(nextjydate));
                                    }
                                    // 上一年度设备状态检测报告出具时间
                                    String syndsbztjcbgcjsj = data[13].toString();
                                    if(StringUtil.isNotBlank(syndsbztjcbgcjsj)){
                                        String message = checkDate(syndsbztjcbgcjsj);
                                        if(StringUtil.isNotBlank(message)){
                                            msg = "数据格式有误！";
                                            return msg;
                                        }
                                        auditFszl.setSyndsbztjcbgcjsj(EpointDateUtil.getBeginOfDateStr(syndsbztjcbgcjsj));
                                    }
                                    auditFszl.setBaogbzdw(data[14].toString());
                                    // 许可证状态
                                    String certstatus = data[15].toString();
                                    if(StringUtil.isNotBlank(certstatus) && !xkzSet.contains(certstatus)){
                                        msg = "许可证状态字段存在代码项【放射诊疗许可证状态】中不存在的值！";
                                        return msg;
                                    }
                                    else {
                                        auditFszl.setCertstatus(xkzMap.get(certstatus));
                                    }
                                    fszlList.add(auditFszl);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    return msg;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    Map<String, Object> rtnMap = new HashMap<String, Object>();
                    if (StringUtil.isNotBlank(msg)) {
                        rtnMap.put("message", msg);
                    }
                    else {
                        try {
                            EpointFrameDsManager.begin(null);
                            // 保存入库
                            Date date = new Date();
                            for(AuditFszl auditFszl:fszlList){
                                String certno = auditFszl.getCertno();

                                SqlConditionUtil sqlc = new SqlConditionUtil();
                                sqlc.eq("certno",certno);
                                sqlc.eq("is_history",ZwfwConstant.CONSTANT_STR_ZERO);
                                AuditFszl fszl = service.find("select * from audit_fszl " + new SqlHelper().getSql(sqlc.getMap()),new ArrayList<>());
                                if(fszl != null){
                                    continue;
                                }

                                auditFszl.setIs_history(ZwfwConstant.CONSTANT_STR_ZERO);
                                auditFszl.setVersion(ZwfwConstant.CONSTANT_INT_ONE);
                                auditFszl.setSendtip(ZwfwConstant.CONSTANT_STR_ZERO);
                                auditFszl.setVersiondate(date);
                                auditFszl.setFszlid(UUID.randomUUID().toString());
                                auditFszl.setUpdatetime(date);
                                auditFszl.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
                                service.insert(auditFszl);

                                // 保存工作人员
                                List<AuditFszlPerson> auditFszlPersonList = gzryMap.get(certno);
                                if(EpointCollectionUtils.isNotEmpty(auditFszlPersonList)){
                                    auditFszlPersonList.forEach(auditFszlPerson -> {
                                        auditFszlPerson.setFszlguid(auditFszl.getRowguid());
                                        auditFszlPersonService.insert(auditFszlPerson);
                                    });
                                }
                                // 保存放射装置
                                List<AuditFszlDevice> auditFszlDeviceList = fszzMap.get(certno);
                                if(EpointCollectionUtils.isNotEmpty(auditFszlDeviceList)){
                                    auditFszlDeviceList.forEach(auditFszlDevice -> {
                                        auditFszlDevice.setFszlguid(auditFszl.getRowguid());
                                        auditFszlDeviceService.insert(auditFszlDevice);
                                    });
                                }
                            }
                            EpointFrameDsManager.commit();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            EpointFrameDsManager.rollback();
                        }
                        finally {
                            EpointFrameDsManager.close();
                        }
                    }
                    dataImportModel.getUploadModel().setExtraDatas(rtnMap);
                }
            });
        }
        return dataImportModel;
    }


    private String checkDate(String date){
        if(StringUtil.isBlank(date)){
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EpointDateUtil.DATE_FORMAT);
            LocalDate.parse(date, formatter);
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return "数据格式有误！";
        }
    }


    public void setDataBean(AuditFszl dataBean) {
        this.dataBean = dataBean;
    }


    public String getCertificateDateStart() {
        return certificateDateStart;
    }

    public void setCertificateDateStart(String certificateDateStart) {
        this.certificateDateStart = certificateDateStart;
    }

    public String getCertificateDateEnd() {
        return certificateDateEnd;
    }

    public void setCertificateDateEnd(String certificateDateEnd) {
        this.certificateDateEnd = certificateDateEnd;
    }

    public String getJydateStart() {
        return jydateStart;
    }

    public void setJydateStart(String jydateStart) {
        this.jydateStart = jydateStart;
    }

    public String getJydateEnd() {
        return jydateEnd;
    }

    public void setJydateEnd(String jydateEnd) {
        this.jydateEnd = jydateEnd;
    }

    public String getNextjyDateStart() {
        return nextjyDateStart;
    }

    public void setNextjyDateStart(String nextjyDateStart) {
        this.nextjyDateStart = nextjyDateStart;
    }

    public String getNextjyDateEnd() {
        return nextjyDateEnd;
    }

    public void setNextjyDateEnd(String nextjyDateEnd) {
        this.nextjyDateEnd = nextjyDateEnd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
