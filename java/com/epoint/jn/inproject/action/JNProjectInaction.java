package com.epoint.jn.inproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.zhenggai.api.ZhenggaiService;

@RestController("jnprojectinaction")
@Scope("request")
public class JNProjectInaction extends BaseController
{

    private static final long serialVersionUID = 1L;
    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private DataGridModel<AuditProject> model = null;

    @Autowired
    private IWebUploaderService service;
    @Autowired
    private ZhenggaiService zhenggaiImpl;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAuditProject iAuditProject;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deletejprojectByguid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataImportModel9 getDataImportModel() {
        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler()
            {
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
                    AuditProject project = new AuditProject();
                    project.setRowguid(UUID.randomUUID().toString());
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
                            AuditTask task = null;
                            for (int i = 0; i < data.length; i++) {
                                map.put(struList.get(i) + "", data[i].toString());
                                String key = struList.get(i) + "";
                                String value = data[i].toString();
                                switch (key) {
                                    case "申办编号":
                                        project.setFlowsn(value);
                                        break;

                                    case "业务主题":
                                        project.setProjectname(value);
                                        break;

                                    case "办件类型":
                                        String tasktype = iCodeItemsService.getItemValueByCodeID("1015625", value);
                                        project.setTasktype(Integer.parseInt(tasktype));
                                        break;

                                    case "受理时间":
                                        Date acceptdate = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                        project.setAcceptuserdate(acceptdate);
                                        project.setApplydate(acceptdate);
                                        break;

                                    case "受理人":
                                        project.setAcceptusername(value);
                                        break;

                                    case "受理单位":
                                        FrameOu frameou = service.getFrameOuByOuname(value);
                                        if (frameou != null) {
                                            project.setOuguid(frameou.getOuguid());
                                            project.setOuname(frameou.getOuname());
                                            ouguid = frameou.getOuguid();
                                        }
                                        break;

                                    case "所属区县":
                                        areacode = iCodeItemsService.getItemTextByCodeName("办件导入辖区对应关系", value);
                                        project.setAreacode(areacode);
                                        break;

                                    case "事项名称":
                                        task = service.getAuditTaskByTaskname(value, areacode, ouguid);
                                        if (task != null) {
                                            project.setTaskguid(task.getRowguid());
                                            project.setTaskid(task.getTask_id());
                                        }
                                        break;

                                    case "联系人":
                                        project.setContactperson(value);
                                        break;

                                    case "联系电话":
                                        project.setContactmobile(value);
                                        break;

                                    case "承诺时间":
                                        Date enddate = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                        project.setPromiseenddate(enddate);
                                        break;

                                    case "办结时间":
                                        Date banjiedate = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                        project.setBanjiedate(banjiedate);
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
                            // 8代表办件导入来源
                            project.set("is_lczj", "8");
                            project.setStatus(90);
                            project.setBanjieresult(40);
                            iAuditProject.addProject(project);
                        }
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
    public DataGridModel<AuditProject> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditProject> fetchData(int first, int pagesize, String arg2, String arg3) {
                    String field = "*";
                    SqlConditionUtil condition = new SqlConditionUtil();
                    condition.eq("is_lczj", "8");
                    PageData<AuditProject> pagedate = iAuditProject
                            .getAuditProjectPageData(field, condition.getMap(), first, pagesize, "applydate", "desc")
                            .getResult();
                    this.setRowCount(pagedate.getRowCount());
                    return pagedate.getList();
                }
            };
        }
        return model;
    }
}
