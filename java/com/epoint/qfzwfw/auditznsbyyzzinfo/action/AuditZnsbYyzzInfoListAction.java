package com.epoint.qfzwfw.auditznsbyyzzinfo.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.IAuditZnsbYyzzInfoService;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.entity.AuditZnsbYyzzInfo;

/**
 * 智能设备营业执照表list页面对应的后台
 * 
 * @author LIUCTT
 * @version [版本号, 2018-06-07 09:51:27]
 */
@RestController("auditznsbyyzzinfolistaction")
@Scope("request")
public class AuditZnsbYyzzInfoListAction extends BaseController
{
    @Autowired
    private IAuditZnsbYyzzInfoService service;
    private List<AuditZnsbYyzzInfo> needAddUserList = null;
    private List<AuditZnsbYyzzInfo> needAddUserList2 = null;
    private List<AuditZnsbYyzzInfo> needAddUserList3 = null;
    private List<AuditZnsbYyzzInfo> needAddUserList4 = null;
    /**
     * 智能设备营业执照表实体对象
     */
    private AuditZnsbYyzzInfo dataBean;
    /**
     * 是否已打印单选按钮组model
     */
    private List<SelectItem> isprintModel = null;

    private String qyname;
    private String isprint = "0";
    private DataImportModel9 dataImport;
    private DataImportModel9 dataImport2;
    private DataImportModel9 dataImport3;
    private DataImportModel9 dataImport4;
    private String tyshxydm;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbYyzzInfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    private CommonService commservice = new CommonService();

    @Override
    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditZnsbYyzzInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbYyzzInfo>()
            {

                @Override
                public List<AuditZnsbYyzzInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    if (StringUtil.isNotBlank(qyname)) {
                        conditionSql += " and qyname like '%" + qyname + "%'";
                    }
                    if (StringUtil.isNotBlank(tyshxydm)) {
                        conditionSql += " and tyshxydm like '%" + tyshxydm + "%'";
                    }
                    if (StringUtil.isNotBlank(isprint)) {
                        conditionSql += " and isprint ='" + isprint + "'";
                    }
                    List<AuditZnsbYyzzInfo> list = service.findList(
                            ListGenerator.generateSql("Audit_ZNSB_YYZZ_Info", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    /*int count = service.findList(ListGenerator.generateSql("Audit_ZNSB_YYZZ_Info", conditionSql),
                            AuditZnsbYyzzInfo.class, conditionList.toArray()).size();*/
                    int count = getconut();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public int getconut() {
        String conditionSql = "";
        if (StringUtil.isNotBlank(qyname)) {
            conditionSql += " and qyname like '%" + qyname + "%'";
        }
        if (StringUtil.isNotBlank(tyshxydm)) {
            conditionSql += " and tyshxydm like '%" + tyshxydm + "%'";
        }
        if (StringUtil.isNotBlank(isprint)) {
            conditionSql += " and isprint ='" + isprint + "'";
        }
      
        String sql = "select * from Audit_ZNSB_YYZZ_Info where 1=1 " + conditionSql;
        int rr = commservice.findList(sql, Record.class).size();
        return rr;
    }

    public DataImportModel9 getDataImport() {
        if (dataImport == null) {
            dataImport = new DataImportModel9(new ImportExcelHandler()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    String message = null;

                    if (curRow == 0) {// 做一些初始化操作
                        if (needAddUserList == null) {
                            needAddUserList = new ArrayList<AuditZnsbYyzzInfo>();
                        }
                    }
                    else {// 第0行是标题不做考虑
                        if (StringUtil.isNotBlank(data[0])) {
                            AuditZnsbYyzzInfo user = new AuditZnsbYyzzInfo();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String TYSHXYDM = data[0].toString().trim();
                            String ZCH = data[1].toString().trim();
                            String RANGEN = data[2].toString().trim();
                            String QYNAME = data[3].toString().trim();
                            String ZCZB = data[4].toString().trim();
                            String ADDRESS = data[5].toString().trim();
                            String QYTYPE = data[6].toString().trim();
                            String LEGAL = data[7].toString().trim();

                            Date CLDATE = null;
                            Date HZDATE = null;
                            Date FROMDATE = null;
                            Date TODATE = null;

                            try {
                                CLDATE = sdf.parse(data[8].toString().trim());
                                HZDATE = sdf.parse(data[9].toString().trim());
                                FROMDATE = sdf.parse(data[10].toString().trim());
                                TODATE = sdf.parse(data[11].toString().trim());
                            }
                            catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Date day = new Date();
                            String RPRESON = data[12].toString().trim();
                            String FZRMOBILE = data[13].toString().trim();
                            String ISPRINT = "0";
                            String ZZTYPE = "1";
                            user.setOperatedate(day);
                            user.setRowguid(UUID.randomUUID().toString());
                            user.setTyshxydm(TYSHXYDM);
                            user.setZch(ZCH);
                            user.setRangen(RANGEN);
                            user.setQyname(QYNAME);
                            user.setZczb(ZCZB);
                            user.setAddress(ADDRESS);
                            user.setQytype(QYTYPE);
                            user.setLegal(LEGAL);
                            user.setCldate(CLDATE);
                            user.setHzdate(HZDATE);
                            user.setFromdate(FROMDATE);
                            user.setTodate(TODATE);
                            user.setRpreson(RPRESON);
                            user.setFzrmobile(FZRMOBILE);
                            user.setIsprint(ISPRINT);
                            user.setZztype(ZZTYPE);

                            needAddUserList.add(user);
                        }
                        else {
                            message = "导入成功！";
                            return message;
                        }
                    }
                    if (StringUtil.isNotBlank(message)) {
                        dataImport.setMessage(message);
                    }
                    return message;
                }

                @Override
                public void refreshTable() {

                    // TODO 这边执行存库操作
                    if (needAddUserList != null) {
                        int i = 0;
                        for (AuditZnsbYyzzInfo user : needAddUserList) {
                        	commservice.insert(user);
                        }
                    }

                }
            });
        }
        return dataImport;
    }

    public DataImportModel9 getDataImport2() {
        if (dataImport2 == null) {
            dataImport2 = new DataImportModel9(new ImportExcelHandler()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    String message = null;

                    if (curRow == 0) {// 做一些初始化操作
                        if (needAddUserList2 == null) {
                            needAddUserList2 = new ArrayList<AuditZnsbYyzzInfo>();
                        }
                    }
                    else {// 第0行是标题不做考虑
                        if (StringUtil.isNotBlank(data[0])) {
                            AuditZnsbYyzzInfo user = new AuditZnsbYyzzInfo();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String TYSHXYDM = data[0].toString().trim();
                            String ZCH = data[1].toString().trim();
                            String RANGEN = data[2].toString().trim();
                            String QYNAME = data[3].toString().trim();

                            String ADDRESS = data[4].toString().trim();
                            String QYTYPE = data[5].toString().trim();
                            String LEGAL = data[6].toString().trim();

                            Date CLDATE = null;
                            Date HZDATE = null;
                            Date FROMDATE = null;
                            Date TODATE = null;

                            try {
                                CLDATE = sdf.parse(data[7].toString().trim());
                                HZDATE = sdf.parse(data[8].toString().trim());
                                FROMDATE = sdf.parse(data[9].toString().trim());
                                TODATE = sdf.parse(data[10].toString().trim());
                            }
                            catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Date day = new Date();
                            String RPRESON = data[11].toString().trim();
                            String FZRMOBILE = data[12].toString().trim();
                            String ISPRINT = "0";
                            String ZZTYPE = "2";
                            user.setOperatedate(day);
                            user.setRowguid(UUID.randomUUID().toString());
                            user.setTyshxydm(TYSHXYDM);
                            user.setZch(ZCH);
                            user.setRangen(RANGEN);
                            user.setQyname(QYNAME);
                            user.setAddress(ADDRESS);
                            user.setQytype(QYTYPE);
                            user.setLegal(LEGAL);
                            user.setCldate(CLDATE);
                            user.setHzdate(HZDATE);
                            user.setFromdate(FROMDATE);
                            user.setTodate(TODATE);
                            user.setRpreson(RPRESON);
                            user.setFzrmobile(FZRMOBILE);
                            user.setIsprint(ISPRINT);
                            user.setZztype(ZZTYPE);

                            needAddUserList2.add(user);
                        }
                        else {
                            message = "导入成功！";
                            return message;
                        }
                    }
                    if (StringUtil.isNotBlank(message)) {
                        dataImport2.setMessage(message);
                    }
                    return message;
                }

                @Override
                public void refreshTable() {

                    // TODO 这边执行存库操作
                    if (needAddUserList2 != null) {
                        for (AuditZnsbYyzzInfo user : needAddUserList2) {
                        	commservice.insert(user);
                        }
                    }

                }
            });
        }
        return dataImport2;
    }
    
    public DataImportModel9 getDataImport3() {
        if (dataImport3 == null) {
            dataImport3 = new DataImportModel9(new ImportExcelHandler()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    String message = null;

                    if (curRow == 0) {// 做一些初始化操作
                        if (needAddUserList3 == null) {
                            needAddUserList3 = new ArrayList<AuditZnsbYyzzInfo>();
                        }
                    }
                    else {// 第0行是标题不做考虑
                        if (StringUtil.isNotBlank(data[0])) {
                            AuditZnsbYyzzInfo user = new AuditZnsbYyzzInfo();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String TYSHXYDM = data[0].toString().trim();
                            String ZCH = data[1].toString().trim();
                            String RANGEN = data[2].toString().trim();
                            String QYNAME = data[3].toString().trim();
                            String ZCZB = data[4].toString().trim();
                            String ADDRESS = data[5].toString().trim();
                            String QYTYPE = data[6].toString().trim();
                            String LEGAL = data[7].toString().trim();

                            Date CLDATE = null;
                            Date HZDATE = null;
                            Date FROMDATE = null;
                            Date TODATE = null;

                            try {
                                CLDATE = sdf.parse(data[8].toString().trim());
                                HZDATE = sdf.parse(data[9].toString().trim());
                                FROMDATE = sdf.parse(data[10].toString().trim());
                                TODATE = sdf.parse(data[11].toString().trim());
                            }
                            catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Date day = new Date();
                            String RPRESON = data[12].toString().trim();
                            String FZRMOBILE = data[13].toString().trim();
                            String ISPRINT = "0";
                            String ZZTYPE = "3";
                            user.setOperatedate(day);
                            user.setRowguid(UUID.randomUUID().toString());
                            user.setTyshxydm(TYSHXYDM);
                            user.setZch(ZCH);
                            user.setRangen(RANGEN);
                            user.setQyname(QYNAME);
                            user.setZczb(ZCZB);
                            user.setAddress(ADDRESS);
                            user.setQytype(QYTYPE);
                            user.setLegal(LEGAL);
                            user.setCldate(CLDATE);
                            user.setHzdate(HZDATE);
                            user.setFromdate(FROMDATE);
                            user.setTodate(TODATE);
                            user.setRpreson(RPRESON);
                            user.setFzrmobile(FZRMOBILE);
                            user.setIsprint(ISPRINT);
                            user.setZztype(ZZTYPE);

                            needAddUserList3.add(user);
                        }
                        else {
                            message = "导入成功！";
                            return message;
                        }
                    }
                    if (StringUtil.isNotBlank(message)) {
                        dataImport3.setMessage(message);
                    }
                    return message;
                }

                @Override
                public void refreshTable() {

                    // TODO 这边执行存库操作
                    if (needAddUserList3 != null) {
                        int i = 0;
                        for (AuditZnsbYyzzInfo user : needAddUserList3) {
                        	commservice.insert(user);
                        }
                    }

                }
            });
        }
        return dataImport3;
    }
    public DataImportModel9 getDataImport4() {
        if (dataImport4 == null) {
            dataImport4 = new DataImportModel9(new ImportExcelHandler()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    String message = null;

                    if (curRow == 0) {// 做一些初始化操作
                        if (needAddUserList4 == null) {
                            needAddUserList4 = new ArrayList<AuditZnsbYyzzInfo>();
                        }
                    }
                    else {// 第0行是标题不做考虑
                        if (StringUtil.isNotBlank(data[0])) {
                            AuditZnsbYyzzInfo user = new AuditZnsbYyzzInfo();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String TYSHXYDM = data[0].toString().trim();
                            String ZCH = data[1].toString().trim();
                            String RANGEN = data[2].toString().trim();
                            String QYNAME = data[3].toString().trim();

                            String ADDRESS = data[4].toString().trim();
                            String QYTYPE = data[5].toString().trim();
                            String LEGAL = data[6].toString().trim();

                            Date CLDATE = null;
                            Date HZDATE = null;
                            Date FROMDATE = null;
                            Date TODATE = null;

                            try {
                                CLDATE = sdf.parse(data[7].toString().trim());
                                HZDATE = sdf.parse(data[8].toString().trim());
                                FROMDATE = sdf.parse(data[9].toString().trim());
                                TODATE = sdf.parse(data[10].toString().trim());
                            }
                            catch (ParseException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Date day = new Date();
                            String RPRESON = data[11].toString().trim();
                            String FZRMOBILE = data[12].toString().trim();
                            String ISPRINT = "0";
                            String ZZTYPE = "4";
                            user.setOperatedate(day);
                            user.setRowguid(UUID.randomUUID().toString());
                            user.setTyshxydm(TYSHXYDM);
                            user.setZch(ZCH);
                            user.setRangen(RANGEN);
                            user.setQyname(QYNAME);
                            user.setAddress(ADDRESS);
                            user.setQytype(QYTYPE);
                            user.setLegal(LEGAL);
                            user.setCldate(CLDATE);
                            user.setHzdate(HZDATE);
                            user.setFromdate(FROMDATE);
                            user.setTodate(TODATE);
                            user.setRpreson(RPRESON);
                            user.setFzrmobile(FZRMOBILE);
                            user.setIsprint(ISPRINT);
                            user.setZztype(ZZTYPE);

                            needAddUserList4.add(user);
                        }
                        else {
                            message = "导入成功！";
                            return message;
                        }
                    }
                    if (StringUtil.isNotBlank(message)) {
                        dataImport4.setMessage(message);
                    }
                    return message;
                }

                @Override
                public void refreshTable() {

                    // TODO 这边执行存库操作
                    if (needAddUserList4 != null) {
                        for (AuditZnsbYyzzInfo user : needAddUserList4) {
                        	commservice.insert(user);
                        }
                    }

                }
            });
        }
        return dataImport4;
    }
    public AuditZnsbYyzzInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbYyzzInfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbYyzzInfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("cldate,qyname,qytype,rpreson,tyshxydm", "成立日期,企业中文名,企业类型,负责人,统一社会信用代码");
        }
        return exportModel;
    }

    public String getQyname() {
        return qyname;
    }

    public void setQyname(String qyname) {
        this.qyname = qyname;
    }

    public String getIsprint() {
        return isprint;
    }

    public void setIsprint(String isprint) {
        this.isprint = isprint;
    }

    public String getTyshxydm() {
        return tyshxydm;
    }

    public void setTyshxydm(String tyshxydm) {
        this.tyshxydm = tyshxydm;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsprintModel() {
        if (isprintModel == null) {
            isprintModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isprintModel;
    }
}
