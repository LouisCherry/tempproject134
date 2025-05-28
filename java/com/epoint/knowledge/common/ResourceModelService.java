package com.epoint.knowledge.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;


public class ResourceModelService// extends BasicModel
{

    private List<SelectItem> selectItems = null;

    public LazyTreeModal9 treeModal = null;

    public LazyTreeModal9 categoryOuTree = null;
    
    public LazyTreeModal9 categoryAllTree = null;
    
    private CnsKinfoCategoryService cnsKinfoCategoryService = new CnsKinfoCategoryService();
    
    private ICommonDao dao = CommonDao.getInstance();
    @SuppressWarnings("unchecked")
    public List<SelectItem> getKstatusModel() {
        selectItems = DataUtil.convertMap2ComboBox(
                (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "知识库内容状态", null, true));
        return this.selectItems;
    }
    /**
     * 是否
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SelectItem> getZeroOrOneModel() {
        selectItems = DataUtil.convertMap2ComboBox(
                (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "便民是否判断", null, false));
        return selectItems;
    }
    
    
    
    public LazyTreeModal9 getCategoryOuModel(String ouGuid) {
        if (categoryOuTree == null) {
            categoryOuTree = new LazyTreeModal9(new SimpleFetchHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 2005459160380451054L;

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> search(String keyWord) {
                    List<CnsKinfoCategory> cnsKinfoCategoryList = null;
                    if (StringUtil.isNotBlank(keyWord)) {
                        cnsKinfoCategoryList = cnsKinfoCategoryService.getListByOuguidNameOrPY(ouGuid, keyWord);
                        //cnsKinfoCategoryList = dao.findList("select * from Cns_Kinfo_Category where ouguid = '"+ouGuid+"' or ouname='所有用戶'", CnsKinfoCategory.class, null);
                    }
                    return (List<T>) cnsKinfoCategoryList;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List<CnsKinfoCategory> categoryList = new ArrayList<CnsKinfoCategory>();
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        Map<String, String> conditionMap=new HashMap<String, String>();
                        conditionMap.put("OUGUID=", ouGuid);
                        conditionMap.put("isdepttop=",CnsConstValue.CNS_ONT_STR);
                        categoryList = cnsKinfoCategoryService.getRecordList("*", conditionMap, "", "");
                    }
                    else {
                        String parentGuid = treeData.getObjectGuid();
                        categoryList = cnsKinfoCategoryService.getListByOneField("parentguid", parentGuid);
                    }
                    return (List<T>) categoryList;
                }

                @Override
                public int fetchChildCount(TreeData treeData) {
                    List<CnsKinfoCategory> categoryList = new ArrayList<CnsKinfoCategory>();
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        categoryList = cnsKinfoCategoryService.getListByOneField("isdepttop",
                                CnsConstValue.CNS_ONT_STR);
                    }
                    else {
                        String parentGuid = treeData.getObjectGuid();
                        categoryList = cnsKinfoCategoryService.getListByOneField("parentguid", parentGuid);
                    }
                    return categoryList.size();
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
        categoryOuTree.setRootName("知识类别");
        return categoryOuTree;
    }
    
    
    
    public LazyTreeModal9 getCategoryAllModel() {
        if (categoryAllTree == null) {
            categoryAllTree = new LazyTreeModal9(new SimpleFetchHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 2005459160380451054L;

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> search(String keyWord) {
                    List<CnsKinfoCategory> cnsKinfoCategoryList = null;
                    if (StringUtil.isNotBlank(keyWord)) {
                        cnsKinfoCategoryList = cnsKinfoCategoryService.getListByNameOrPY(keyWord,false);
                    }
                    return (List<T>) cnsKinfoCategoryList;
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List<CnsKinfoCategory> categoryList = new ArrayList<CnsKinfoCategory>();
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        Map<String, String> conditionMap=new HashMap<String, String>();
                        conditionMap.put("parentguid=", "top");
                        categoryList = cnsKinfoCategoryService.getRecordList("*", conditionMap, "", "");
                    }
                    else {
                        String parentGuid = treeData.getObjectGuid();
                        categoryList = cnsKinfoCategoryService.getListByOneField("parentguid", parentGuid);
                    }
                    return (List<T>) categoryList;
                }

                @Override
                public int fetchChildCount(TreeData treeData) {
                    List<CnsKinfoCategory> categoryList = new ArrayList<CnsKinfoCategory>();
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        categoryList = cnsKinfoCategoryService.getListByOneField("isdepttop",
                                CnsConstValue.CNS_ONT_STR);
                    }
                    else {
                        String parentGuid = treeData.getObjectGuid();
                        categoryList = cnsKinfoCategoryService.getListByOneField("parentguid", parentGuid);
                    }
                    return categoryList.size();
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
        categoryAllTree.setRootName("知识类别");
        return categoryAllTree;
    }
}
