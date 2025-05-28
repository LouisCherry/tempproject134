package com.epoint.knowledge.category.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;



/**
 * 
 * 知识库类别action
 * 
 * @作者 Administrator
 * @version [版本号, 2017年2月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */

@RestController("categorylistaction")
@Scope("request")
public class CategoryListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 7270076561889138153L;
    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoCategory> model;
    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    /**
     * 知识库类型树
     */
    private LazyTreeModal9 kinfoCategoryTree;

    private String categoryguid;
    /**
     * 知识库类型
     */
    private CnsKinfoCategory cnsKinfoCategory;
    private ResourceModelService resourceModelService = new ResourceModelService();
    @Override
    public void pageLoad() {
        if (cnsKinfoCategory == null) {
            cnsKinfoCategory = new CnsKinfoCategory();
        }
    }

    public DataGridModel<CnsKinfoCategory> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CnsKinfoCategory>()
            {
                @Override
                public List<CnsKinfoCategory> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    Map<String, String> conditionMap = new HashMap<String, String>();
                    if (StringUtil.isNotBlank(categoryguid)) {
                        conditionMap.put("PARENTGUID=", categoryguid);
                        
                    }else if(CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)){
                        conditionMap.put("ouguid=", UserSession.getInstance().getOuGuid());
                        conditionMap.put("PARENTGUID=", "top");
                        conditionMap.put("isdepttop=",CnsConstValue.CNS_ONT_STR);
                    }else {
                        
                    }
                    if (StringUtil.isNotBlank(cnsKinfoCategory.getCategoryname())) {
                        conditionMap.put("CATEGORYNAMELIKE", cnsKinfoCategory.getCategoryname());
                    }
                    if (StringUtil.isNotBlank(cnsKinfoCategory.getCategorycode())) {
                        conditionMap.put("CATEGORYCODELIKE", cnsKinfoCategory.getCategorycode());
                    }
                    PageData<CnsKinfoCategory> pageData = kinfoCategoryService.getRecordPageBean("*", conditionMap,
                            first, pageSize, "CATEGORYCODE", "ASC");
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }
    public LazyTreeModal9 getCategoryModal() {
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



    /**
     * 
     * 删除按钮操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public void deleteSelect() {
        // 获取选中的主键
        List<String> keyList = this.getDataGridData().getSelectKeys();
        if (keyList != null && keyList.size() > 0) {
            for (String key : keyList) {
                // 得到上层节点
                CnsKinfoCategory cns = kinfoCategoryService
                        .getBeanByGuid(kinfoCategoryService.getBeanByGuid(key).getParentguid());
                if (cns != null) {
                    // 设置为子节点
                    cns.setIsleaf("1");
                    kinfoCategoryService.updateRecord(cns);
                }

                // 删除下级部门（包括自己）和部门的关联关系
                kinfoCategoryService.deleteCategory(key);

            }
        }
        this.addCallbackParam("msg", "删除成功！");

    }

    /**
     * 
     * 页面保存
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveAll() {
        List<CnsKinfoCategory> categories = this.getDataGridData().getWrappedData();
        for (CnsKinfoCategory cnsKinfoCategory : categories) {
            cnsKinfoCategory.setFirstpy(ChineseToPinyin.getPinYinHeadChar(cnsKinfoCategory.getCategoryname()));
            cnsKinfoCategory.setFulltpy(ChineseToPinyin.getPingYin(cnsKinfoCategory.getCategoryname()));
            kinfoCategoryService.updateRecord(cnsKinfoCategory);
        }
        this.addCallbackParam("msg", "修改成功！");
    }

    public LazyTreeModal9 getKinfoCategoryTree() {
        return kinfoCategoryTree;
    }

    public void setKinfoCategoryTree(LazyTreeModal9 kinfoCategoryTree) {
        this.kinfoCategoryTree = kinfoCategoryTree;
    }

    public String getCategoryguid() {
        return categoryguid;
    }

    public void setCategoryguid(String categoryguid) {
        this.categoryguid = categoryguid;
    }

    public CnsKinfoCategory getCnsKinfoCategory() {
        return cnsKinfoCategory;
    }

    public void setCnsKinfoCategory(CnsKinfoCategory cnsKinfoCategory) {
        this.cnsKinfoCategory = cnsKinfoCategory;
    }

}
