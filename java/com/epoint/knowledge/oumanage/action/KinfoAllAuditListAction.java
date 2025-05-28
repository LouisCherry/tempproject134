package com.epoint.knowledge.oumanage.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.bizlogic.sysconf.code.service.CodeItemsService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoAbandon;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.oumanage.service.CnsKinfoAbandonService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;

/**
 * 知识信息表list页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-13 10:28:44]
 */
@RestController("kinfoallauditlistaction")
@Scope("request")
public class KinfoAllAuditListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ResourceModelService commonModelService = new ResourceModelService();
    private CnsKinfoService cnsKinfoService = new CnsKinfoService();
    private CommonService service = new CommonService();
    /**
     * 知识库类型service
     */
    
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    private CodeItemsService9 codeItemsService9 = new CodeItemsService9();
    private CnsKinfoAbandonService cnsKinfoAbandonService = new CnsKinfoAbandonService();
    /**
     * 知识信息表实体对象
     */
    private CnsKinfo cnsKinfo;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 类别标识
     */
    private String categoryguid;

    /**
     * 审核状态
     */
    String status;
    private ICommonDao dao = CommonDao.getInstance();
    private ResourceModelService resourceModelService = new ResourceModelService();

    @Override
    public void pageLoad() {
        if (cnsKinfo == null) {
            cnsKinfo = new CnsKinfo();
        }
    }

    public DataGridModel<CnsKinfo> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfo>()
            {

                @Override
                public List<CnsKinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //查看未被删除的数据
                    conditionMap.put("ISDEL=", CnsConstValue.CNS_ZERO_STR);
                    conditionMap.put("KSTATUSIN ", " '"+CnsConstValue.KinfoStatus.PASS_AUDIT+"','"+CnsConstValue.KinfoStatus.BACK_AUDIT+"'");
                    if (StringUtil.isNotBlank(cnsKinfo.getKname())) {
                        conditionMap.put("KNAMELIKE", cnsKinfo.getKname());
                    }
                    if (StringUtil.isNotBlank(cnsKinfo.getIsenable())) {
                        conditionMap.put("ISENABLE=", cnsKinfo.getIsenable().toString());
                    }
                    if (StringUtil.isBlank(sortField)) {
                        sortField = "CREATDATE";
                    }
                    if (StringUtil.isBlank(sortOrder)) {
                        sortOrder = "DESC";
                    }
                    if (StringUtil.isNotBlank(categoryguid)) {
                        //获取该类别下面所有类别，作为条件
                        List<CnsKinfoCategory> childCategory = kinfoCategoryService.getChildCategoryList(categoryguid);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";
                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                            }
                            inStr = inStr.substring(0, inStr.length() - 1);
                            conditionMap.put("CATEGORYGUIDIN", inStr);
                        }
                    }else{
                        String sql = "";
                        String ouGuid = UserSession.getInstance().getOuGuid();
                        sql = "select * from Cns_Kinfo_Category where 1=1 ";
                        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {
                            
                            sql+=" and ouguid = '"+ouGuid+"'";
                        }
                        
                        List<CnsKinfoCategory> childCategory = dao.findList(sql, CnsKinfoCategory.class, null);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";
                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                    inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                            }
                            if(StringUtil.isBlank(inStr.trim())){
                                return new ArrayList<CnsKinfo>();
                            }
                            inStr = inStr.substring(0, inStr.length() - 1);
                            conditionMap.put("CATEGORYGUIDIN", inStr);
                        }else{
                            return new ArrayList<CnsKinfo>();
                        }
                    }
                    List<CnsKinfo> list = cnsKinfoService.getRecordPage("*", conditionMap, first, pageSize, sortField,
                            sortOrder);
                    
                    int count = cnsKinfoService.getRecordCount(conditionMap);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 
     *  [停用]
     */
    public void onstart(String rowguid) {
        CnsKinfo kinfo = new CnsKinfo();
        kinfo = cnsKinfoService.getBeanByGuid(rowguid);
        kinfo.setIsenable("0");
        service.update(kinfo);
    }

    /**
     *  [启用]
     */
    public void onstop(String rowguid) {
        CnsKinfo kinfo = new CnsKinfo();
        kinfo = cnsKinfoService.getBeanByGuid(rowguid);
        kinfo.setIsenable("1");
        service.update(kinfo);
    }

    public LazyTreeModal9 getCategoryOuModal() {
        LazyTreeModal9 model = null;
        //成员单位只能看到自己的
        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {
            String ouguid = userSession.getOuGuid();
            model = resourceModelService.getCategoryOuModel(ouguid);
        }
        else {
            model = resourceModelService.getCategoryAllModel();
        }
        return model;
    }

    public List<SelectItem> getIsenablemodel() {
        return commonModelService.getZeroOrOneModel();
    }

    public List<SelectItem> getKstatusModel() {
        return commonModelService.getKstatusModel();
    }

    public CnsKinfo getCnsKinfo() {
        return cnsKinfo;
    }

    public void setCnsKinfo(CnsKinfo cnsKinfo) {
        this.cnsKinfo = cnsKinfo;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getCategoryguid() {
        return categoryguid;
    }

    public void setCategoryguid(String categoryguid) {
        this.categoryguid = categoryguid;
    }

    
    /**
     * 下架知识
     * 
     */
    public void abandonKinfo(String guid) {
        CnsKinfo dataBean = cnsKinfoService.getBeanByGuid(guid);
        dataBean.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
        cnsKinfoService.updateRecord(dataBean);
        CnsKinfoAbandon cnsKinfoAbandon = new CnsKinfoAbandon();
        cnsKinfoAbandon.setAbandonkinfocontent(dataBean.getKcontent());
        cnsKinfoAbandon.setAbandonkinfoname(dataBean.getKname());
        cnsKinfoAbandon.setAbandonreasontype(CnsConstValue.CNS_Kinfo_Abandon.Abandon_HAND);
        cnsKinfoAbandon.setAbandonreasoncontent(
                codeItemsService9.getItemTextByCodeName("知识下架原因类型", CnsConstValue.CNS_Kinfo_Abandon.Abandon_HAND));
        cnsKinfoAbandon.setAbandontime(new Date());
        cnsKinfoAbandon.setKguid(dataBean.getRowguid());
        cnsKinfoAbandon.setRowguid(UUID.randomUUID().toString());
        cnsKinfoAbandon.setAbandonkinfocategoryguid(dataBean.getCategoryguid());
        cnsKinfoAbandon.setAbandonkinfocategoryname(dataBean.getCategoryname());
        cnsKinfoAbandonService.addRecord(cnsKinfoAbandon);
        cnsKinfoAbandon = null;
        this.addCallbackParam("msg", "知识成功下架");
    }

    public List<SelectItem> getRqstsourceModel() {
        return DataUtil.convertMap2ComboBox(
                (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "知识启用状态", null, false));

    }
    
}
