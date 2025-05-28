package com.epoint.knowledge.category.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.category.inter.ICnsKinfoCategory;
import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;



/**
 * 
 *  知识库类别实现接口类
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoCategoryImpl extends CnsCommonImpl<CnsKinfoCategory> implements ICnsKinfoCategory
{

    private ICommonDao commonDao;

    public CnsKinfoCategoryImpl() {
        commonDao = CommonDao.getInstance();
    }

    @Override
    public String getMaxCategoryCode(String parentGuid, boolean isRoot) {
        String maxCode = "";
        String sqlCount = "select count(1) from CNS_KInfo_Category where parentguid = ?1";
        CnsKinfoCategory parentCategory = this.getBeanByGuid(parentGuid);
        int num = commonDao.queryInt(sqlCount, parentGuid);
        //如果没有顶级节点，则查询个数，默认为001
        if (num == 0) {
            if (isRoot) {
                maxCode = "01";
            }
            else {
                maxCode = parentCategory.getCategorycode() + "01";
            }
        }
        //如果有顶级节点，则查询最大的code
        else {
            String sqlMax = "select max(cast(categorycode AS SIGNED INTEGER)) from CNS_KInfo_Category where parentguid = ?1";
            maxCode = (commonDao.queryInt(sqlMax, parentGuid) + 1) + "";
            if (maxCode.length() == 1) {
                maxCode = "0" + maxCode;
            }
            maxCode = maxCode.substring(maxCode.length() - 2, maxCode.length());
            if (!isRoot) {
                maxCode = parentCategory.getCategorycode() + maxCode;
            }
        }
        return maxCode;
    }

    @Override
    public void deleteCategoryCascade(String key) {
        String sql = "delete from CNS_KInfo_Category where CATEGORYCODE like ?1";
        commonDao.execute(sql, key + "%");
    }

    @Override
    public List<CnsKinfoCategory> getChildCategoryList(String categoryGuid) {
        CnsKinfoCategory cnsKinfoCategory = this.getSelectBeanByOneField("CATEGORYCODE", "ROWGUID", categoryGuid);
        String sql = "select * from CNS_KINFO_CATEGORY where  CATEGORYCODE like ?1";
        return commonDao.findList(sql, CnsKinfoCategory.class, cnsKinfoCategory.getCategorycode() + "%");
    }

    @Override
    public List<CnsKinfoCategory> getChildLeafCategoryList(String categoryGuid) {
        CnsKinfoCategory cnsKinfoCategory = this.getBeanByGuid(categoryGuid);
        String sql = "select * from CNS_KINFO_CATEGORY where  CATEGORYCODE like ?1 and ISLEAF='1'";
        return commonDao.findList(sql, CnsKinfoCategory.class, cnsKinfoCategory.getCategorycode() + "%");
    }

    @Override
    public List<String> getCategoryByOuNext(String deptOus) {
        String sql = "select distinct CATEGORYGUID from CNS_KINFO_CATEGORY_OU where BASEOUGUID in (" + deptOus + ")";
        return commonDao.findList(sql, String.class);
    }

    @Override
    public List<CnsKinfoCategory> getListByNameOrPY(String keyword,boolean isleaf) {
        String sql = "";
        if(isleaf)
        {
            sql= "select CATEGORYNAME,ROWGUID from CNS_KINFO_CATEGORY where isleaf='1' and (CATEGORYNAME like '%" + keyword
                    + "%' or FIRSTPY like '%" + keyword + "%' or FULLPY like '%" + keyword + "%')";
        }
        else {
            sql = "select CATEGORYNAME,ROWGUID from CNS_KINFO_CATEGORY where CATEGORYNAME like '%" + keyword
                    + "%' or FIRSTPY like '%" + keyword + "%' or FULLPY like '%" + keyword + "%'";
        }
        return commonDao.findList(sql, CnsKinfoCategory.class);
    }

    @Override
    public List<CnsKinfoCategory> getListByOuguidNameOrPY(String ouguid, String keyword) {
        String sql = "select CATEGORYNAME,ROWGUID from CNS_KINFO_CATEGORY where ouguid ='"+ouguid+"' and ( CATEGORYNAME like '%" + keyword
                + "%' or FIRSTPY like '%" + keyword + "%' or FULLPY like '%" + keyword + "%')";
        return commonDao.findList(sql, CnsKinfoCategory.class);
    }

    @Override
    public CnsKinfoCategory getOuByRowguid(String rowguid) {
        String sql = "select ouguid,ouname,isdepttop from CNS_KINFO_CATEGORY where rowguid='" + rowguid + "'";
        return commonDao.find(sql, CnsKinfoCategory.class);
    }

    @Override
    public List<CnsKinfoCategory> getListByOuguidParentguid(String ouguid, String parentguid) {
        String sql = "select * from CNS_KINFO_CATEGORY where ouguid = '" + ouguid + "' and parentguid ='" + parentguid
                + "'";
        return commonDao.findList(sql, CnsKinfoCategory.class);
    }

    @Override
    public List<String> getRootByOuguid(String ouguid) {
        String sql = "select parentguid from CNS_KINFO_CATEGORY where ouguid = '" + ouguid + "' and isdepttop='"+CnsConstValue.CNS_ONT_STR+"'";
        return commonDao.findList(sql, String.class);
    }
}
