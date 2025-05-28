package com.epoint.knowledge.collect.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;


/**
 * 收藏的知识表示层
 * @作者 ASUS
 * @version [版本号, 2017年3月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController("kinfocollectlistaction")
@Scope("request")
public class KinfoCollectListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CnsKinfoService cnsKinfoService = new CnsKinfoService();

    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    /**
     * 知识收藏service
     */
    private CnsKinfoCollectService kinfoCollectService = new CnsKinfoCollectService();

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo cnsKinfo;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfo> model;

    /**
     * 知识库类型树
     */
    private LazyTreeModal9 kinfoCategoryTree;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 类别标识
     */
    private String categoryguid;

    @Override
    public void pageLoad() {
        if (cnsKinfo == null) {
            cnsKinfo = new CnsKinfo();
        }
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            kinfoCollectService.deleteRecordByKguidAndUserguid(sel, userSession.getUserGuid());
        }
        addCallbackParam("msg", "取消收藏成功！");
    }

    public DataGridModel<CnsKinfo> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfo>()
            {

                @Override
                public List<CnsKinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(cnsKinfo.getKname())) {
                        conditionMap.put("KNAMELIKE", cnsKinfo.getKname());
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
                    }
                    List<CnsKinfoCollect> collectes = kinfoCollectService.getListByOneField("USERGUID",
                            userSession.getUserGuid());
                    if (collectes != null && collectes.size() > 0) {
                        String inStr = " ";
                        for (CnsKinfoCollect collect : collectes) {
                            inStr += "'" + collect.getKguid() + "',";
                        }
                        inStr = inStr.substring(0, inStr.length() - 1);
                        conditionMap.put("ROWGUIDIN", inStr);
                    }else{
                        conditionMap.put("ROWGUID=", " ");
                    }
                    List<CnsKinfo> list = cnsKinfoService.getRecordPage("*", conditionMap, first, pageSize,
                            sortField, sortOrder);
                    int count = cnsKinfoService.getRecordCount(conditionMap);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public LazyTreeModal9 getCategoryModal() {
        if (kinfoCategoryTree == null) {
            kinfoCategoryTree = new LazyTreeModal9(new SimpleFetchHandler9()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public <T> List<T> search(String keyword) {
                    return null;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List<CnsKinfoCategory> dataList = new ArrayList<CnsKinfoCategory>();

                    //如果treeData的guid不为空，则说明不是第一次加载,根据父节点获取子节点
                    if (treeData != null && StringUtil.isNotBlank(treeData.getObjectGuid())) {
                        dataList = kinfoCategoryService.getListByOneField("parentguid", treeData.getObjectGuid());
                    }
                    else {
                        //获取顶级部门，parentguid为top
                        dataList = kinfoCategoryService.getListByOneField("parentguid", "top");
                    }
                    return (List<T>) dataList;
                }

                @Override
                public int fetchChildCount(TreeData treeData) {
                    List<CnsKinfoCategory> dataList = new ArrayList<CnsKinfoCategory>();

                    //如果treeData的guid不为空，则说明不是第一次加载,根据父节点获取子节点
                    if (treeData != null && StringUtil.isNotBlank(treeData.getObjectGuid())) {
                        dataList = kinfoCategoryService.getListByOneField("parentguid", treeData.getObjectGuid());
                    }
                    else {
                        //获取顶级部门，parentguid为top
                        dataList = kinfoCategoryService.getListByOneField("parentguid", "top");
                    }
                    return dataList.size();
                }

                @Override
                public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                    List<TreeData> treeDataList = new ArrayList<TreeData>();
                    if (dataList != null) {
                        for (Object obj : dataList) {
                            if (obj instanceof CnsKinfoCategory) {
                                CnsKinfoCategory cnsKinfoCategory = (CnsKinfoCategory) obj;
                                TreeData treeData = new TreeData();
                                treeData.setObjectcode(cnsKinfoCategory.getCategorycode());
                                treeData.setObjectGuid(cnsKinfoCategory.getRowguid());
                                treeData.setTitle(cnsKinfoCategory.getCategoryname());
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }

            });
        }
        kinfoCategoryTree.setRootName("知识类别");
        return kinfoCategoryTree;
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

}
