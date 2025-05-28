package com.epoint.knowledge.abandon.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfoAbandon;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.oumanage.service.CnsKinfoAbandonService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;


/**
 * 下架知识表list页面对应的后台
 * 
 * @author wxlin
 * @version [版本号, 2017-06-07 18:50:19]
 */
@RestController("cnskinfoabandonlistaction")
@Scope("request")
public class CnsKinfoAbandonListAction extends BaseController
{

    private ResourceModelService resourceModelService = new ResourceModelService();

    /**
     * 下架知识表实体对象
     */
    private CnsKinfoAbandon dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoAbandon> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 类别标识
     */
    private String categoryguid;
    
    private List<SelectItem> rqstsourceModel;


    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    private CnsKinfoAbandonService kinfoAbandonService = new CnsKinfoAbandonService();

    private CnsKinfoCategoryService cnsKinfoCategoryService = new CnsKinfoCategoryService();

    private CnsKinfoService cnsKinfoService = new CnsKinfoService();
    
    @Override
    public void pageLoad() {
    }

    public DataGridModel<CnsKinfoAbandon> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfoAbandon>()
            {

                @Override
                public List<CnsKinfoAbandon> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(dataBean.getAbandonkinfoname())) {
                        conditionMap.put("ABANDONKINFONAMELIKE", dataBean.getAbandonkinfoname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getAbandonreasontype())) {
                        conditionMap.put("ABANDONREASONTYPE=", dataBean.getAbandonreasontype());
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
                            conditionMap.put("ABANDONKINFOCATEGORYGUIDIN", inStr);
                        }
                    }
                    else {
                        String ouguid = userSession.getOuGuid();
                        Map<String, String> conditions = new HashMap<String, String>();
                        conditions.put("OUGUID=", ouguid);
                        //conditions.put("isdepttop=", CnsConstValue.CNS_ONT_STR);
                        List<CnsKinfoCategory> categoryList = cnsKinfoCategoryService.getRecordList("*", conditions, "",
                                "");
                        String inStr = " ";
                        if (categoryList != null && categoryList.size() > 0) {
                            for (CnsKinfoCategory parentCategory : categoryList) {
                                //获取该类别下面所有类别，作为条件
                                List<CnsKinfoCategory> childCategory = kinfoCategoryService
                                        .getChildCategoryList(parentCategory.getRowguid());
                                if (childCategory != null && childCategory.size() > 0) {

                                    for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                        inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                                    }
                                    inStr = inStr.substring(0, inStr.length() - 1);
                                    conditionMap.put("ABANDONKINFOCATEGORYGUIDIN", inStr);
                                }
                            }
                        }
                    }
                    List<CnsKinfoAbandon> list = kinfoAbandonService.getRecordPage("*", conditionMap, first, pageSize,
                            "abandontime", "desc");
                    int count = kinfoAbandonService.getRecordCount(conditionMap);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public LazyTreeModal9 getCategoryOuModal() {
        LazyTreeModal9 model = null;
        //成员单位只能看到自己的
//        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {
//            String ouguid = userSession.getOuGuid();
//            model = resourceModelService.getCategoryOuModel(ouguid);
//        }
//        else {
//            model = resourceModelService.getCategoryAllModel();
//        }
        model = resourceModelService.getCategoryAllModel();
        return model;
    }

    public CnsKinfoAbandon getDataBean() {
        if (dataBean == null) {
            dataBean = new CnsKinfoAbandon();
        }
        return dataBean;
    }

    public void setDataBean(CnsKinfoAbandon dataBean) {
        this.dataBean = dataBean;
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
    
    public List<SelectItem> getRqstsourceModel() {
        rqstsourceModel = DataUtil
                .convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "知识下架原因类型", null, false));
        return rqstsourceModel;
    }

    //知识模板详情的url的拼接
    public void chooseTempletDetail(String guid) {
        dataBean=kinfoAbandonService.getBeanByGuid(guid);
        
        String url = CnsConstValue.GXHML+"/pages/knowledge/kinfoabandon/cnskinfoabandondetail";
        this.addCallbackParam("url", url+ "?guid=" +dataBean.getRowguid());

    }
    
}
