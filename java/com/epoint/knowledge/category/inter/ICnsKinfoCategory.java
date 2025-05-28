package com.epoint.knowledge.category.inter;

import java.util.List;

import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;


/**
 * 
 *  知识库类别接口类
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ICnsKinfoCategory extends ICnsCommon<CnsKinfoCategory>
{
    /**
     * 
     *  根据父节点获取最大根节点的code
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getMaxCategoryCode(String parentGuid,boolean isRoot);

    /**
     * 
     * 根据类别编码级联删除该类别下所有类别
     *  @param key    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteCategoryCascade(String key);

    /**
     * 
     *  根据类别guid获取所有子类别
     *  @param categoryGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getChildCategoryList(String categoryGuid);

    /**
     * 
     *  获取某一类别下所有叶子节点
     *  @param categoryGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getChildLeafCategoryList(String categoryGuid);

    /**
     * 
     * 获取类别授权表里面，所有包含此部门的类别
     *  @param deptOus    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getCategoryByOuNext(String deptOus);
    /**
     * 汉语与拼音综合查询
     *  @param keyword
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByNameOrPY(String keyword,boolean isleaf);
    
    /**
     * 
     *  根据部门名或首字母拼音或者全拼查询满足条件的部门
     *  @param keyword
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByOuguidNameOrPY(String ouguid,String keyword);
    
    /**
     * 
     *  根据rowguid活动知识类别的相关部门
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CnsKinfoCategory getOuByRowguid(String rowguid);
    
    /**
     * 
     *  根据ouguid和parentguid获得知识类别
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfoCategory> getListByOuguidParentguid(String ouguid,String parentguid);

    /**
     * 
     *  根据ouguid查询部门根节点的父节点
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getRootByOuguid(String ouguid);
}
