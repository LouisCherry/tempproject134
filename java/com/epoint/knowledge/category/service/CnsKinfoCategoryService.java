package com.epoint.knowledge.category.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.knowledge.category.impl.CnsKinfoCategoryImpl;
import com.epoint.knowledge.category.inter.ICnsKinfoCategory;
import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;


/**
 * 
 * 知识库类别中间层
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoCategoryService extends CnsCommonService<CnsKinfoCategory>
{
    private ICnsKinfoCategory kinfoCategoryImpl = new CnsKinfoCategoryImpl();

    private FrameOuService9 frameOuService = new FrameOuService9();

   
//    protected ICnsCommon<CnsKinfoCategory> getICnsCommon() {
//        return kinfoCategoryImpl;
//    }

    /**
     * 
     * 根据父节点获取最大根节点的code
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getMaxCategoryCode(String parentGuid) {
        if ("top".equals(parentGuid)) {
            return kinfoCategoryImpl.getMaxCategoryCode(parentGuid, true);
        }
        else {
            return kinfoCategoryImpl.getMaxCategoryCode(parentGuid, false);
        }
    }

    /**
     * 
     * 把父节点更新为非叶子节点,最上面的节点不需要更新
     *  @param parentguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateParentLeaf(String parentguid) {
        if (!"top".equals(parentguid)) {
            CnsKinfoCategory parent = kinfoCategoryImpl.getBeanByGuid(parentguid);
            if (parent != null) {
                if ("1".equals(parent.getIsleaf())) {
                    parent.setIsleaf("0");
                    kinfoCategoryImpl.updateRecord(parent);
                }
            }
        }
    }

    /**
     * 
     * 根据类别guid获取所有的子类别
     *  @param categoryGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getChildCategoryList(String categoryGuid) {
        return kinfoCategoryImpl.getChildCategoryList(categoryGuid);
    }

    /**
     * 
     *  根据rowgui删除该类别下面的所有类别，同时删除和部门相关的关联关系
     *  @param key    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteCategory(String categoryGuid) {
        List<CnsKinfoCategory> childCategory = kinfoCategoryImpl.getChildCategoryList(categoryGuid);
        if (childCategory != null && childCategory.size() > 0) {
            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                kinfoCategoryImpl.deleteCategoryCascade(cnsKinfoCategory.getCategorycode());
            }
        }
    }

    /**
     * 
     *  获取所有上级类别，可以选择包含或者不包含自己
     *  @param categoryGuid
     *  @param isHaveMe
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getParentCategoryList(String categoryGuid, Boolean isHaveMe) {
        List<CnsKinfoCategory> cnsKinfoCategoryList = new ArrayList<CnsKinfoCategory>();
        CnsKinfoCategory category = kinfoCategoryImpl.getBeanByGuid(categoryGuid);
        //包括自己的时候才加入list
        if (isHaveMe) {
            cnsKinfoCategoryList.add(category);
        }
        if (category != null) {
            String parentGuid = category.getParentguid();
            while (true) {
                CnsKinfoCategory parent = null;
                parent = kinfoCategoryImpl.getBeanByGuid(parentGuid);
                //如果父节点为空，则说明已经遍历到了顶级节点
                if (parent == null) {
                    break;
                }
                else {
                    cnsKinfoCategoryList.add(parent);
                    parentGuid = parent.getParentguid();
                }
            }
        }
        return cnsKinfoCategoryList;
    }

    /**
     * 
     *  获取某一节点下的所有叶子节点
     *  @param parentGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getLeafByParent(String categoryGuid) {
        return kinfoCategoryImpl.getChildLeafCategoryList(categoryGuid);
    }

    /**
     * 
     * 通过授权部门获取该部门以及子部门的拥有的类别
     *  @param deptOus
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Map<String, CnsKinfoCategory> getCategoryByOuNext(String ouguid) {
        Map<String, CnsKinfoCategory> map = new HashMap<String, CnsKinfoCategory>();
        String deptOus = "";
        //获取该部门下的所有部门包括自己
        List<FrameOu> frameOuList = frameOuService.listOUByGuid(ouguid, 4);
        if (frameOuList != null && frameOuList.size() > 0) {
            for (FrameOu frameOu : frameOuList) {
                deptOus += "'" + frameOu.getOuguid() + "',";
            }
            if (deptOus.length() > 0) {
                deptOus = deptOus.substring(0, deptOus.length() - 1);
            }
        }
        List<String> categoryGuids = kinfoCategoryImpl.getCategoryByOuNext(deptOus);
        if (categoryGuids != null && categoryGuids.size() > 0) {
            for (String guid : categoryGuids) {
                List<CnsKinfoCategory> parentCategoryList = this.getParentCategoryList(guid, true);
                for (CnsKinfoCategory parent : parentCategoryList) {
                    map.put(parent.getRowguid(), parent);
                }
            }
        }
        return map;
    }

    /**
     * 
     *  根据部门名或首字母拼音或者全拼查询满足条件的部门
     *  @param keyword
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByNameOrPY(String keyword,boolean isleaf) {
        List<CnsKinfoCategory> categories = null;
        if (StringUtil.isNotBlank(keyword)) {
            categories = kinfoCategoryImpl.getListByNameOrPY(keyword.toLowerCase(),isleaf);
        }
        return categories;
    }

    /**
     * 
     *  根据部门名或首字母拼音或者全拼查询满足条件的部门
     *  @param keyword
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByOuguidNameOrPY(String ouguid, String keyword) {
        List<CnsKinfoCategory> categories = null;
        if (StringUtil.isNotBlank(keyword)) {
            categories = kinfoCategoryImpl.getListByOuguidNameOrPY(ouguid, keyword.toLowerCase());
        }
        return categories;
    }

    /**
     * 
     *  根据rowguid活动知识类别的相关部门
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CnsKinfoCategory getOuByRowguid(String rowguid) {
        return kinfoCategoryImpl.getOuByRowguid(rowguid);
    }

    /**
     * 
     *  根据ouguid和parentguid获得知识类别
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByOuguidParentguid(String ouguid, String parentguid) {
        return kinfoCategoryImpl.getListByOuguidParentguid(ouguid, parentguid);
    }

    /**
     * 
     *  根据ouguid查询部门根节点的父节点
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getRootByOuguid(String ouguid) {
        return kinfoCategoryImpl.getRootByOuguid(ouguid);
    }

    @Override
    protected ICnsCommon<CnsKinfoCategory> getICnsCommon() {
        return new CnsKinfoCategoryImpl();
    }
    
    //获取本辖区部门树
    public String getAreaou(String ouguid){
        String sql ="select areacode from frame_ou_extendinfo where ouguid='"+ouguid+"'";
        String s = CommonDao.getInstance().find(sql, String.class, null);
        return s;
    }

}
