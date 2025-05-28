package com.epoint.knowledge.oumanage.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfo;

import com.epoint.knowledge.oumanage.impl.CnsKinfoImpl;
import com.epoint.knowledge.oumanage.inter.ICnsKinfo;


/**
 * 
 * 知识库类别中间层
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoService extends CnsCommonService<CnsKinfo>
{
    private ICnsKinfo kinfoImpl = new CnsKinfoImpl();

    

    @Override
    protected ICnsCommon<CnsKinfo> getICnsCommon() {
        return new CnsKinfoImpl();
    }

    /**
     * 
     *  根据类别code一位一位设置code
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CnsKinfo setCategoryCodeByLevl(CnsKinfo cnsKinfo, String categoryCode) {
        if (StringUtil.isNotBlank(categoryCode)) {
            if (categoryCode.length() >= 2) {
                cnsKinfo.setTwocode(categoryCode.substring(0, 2));
            }
            if (categoryCode.length() >= 4) {
                cnsKinfo.setFourcode(categoryCode.substring(0, 4));
            }
            if (categoryCode.length() >= 6) {
                cnsKinfo.setSixcode(categoryCode.substring(0, 6));
            }
            if (categoryCode.length() >= 8) {
                cnsKinfo.setEightcode(categoryCode.substring(0, 8));
            }
            if (categoryCode.length() >= 10) {
                cnsKinfo.setTencode(categoryCode.substring(0, 10));
            }
        }
        return cnsKinfo;
    }

    
    
    /**
     * 
     *  查询失效且仍在使用的知识
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfo> getUneffectiveKinfoByTime(String date)
    {
        return kinfoImpl.getUneffectiveKinfoByTime(date);
    }
    
    /**
     *  根据条件获取可阅读的知识
     *  [功能详细描述]
     *  @param conditionMap 条件
     *  @param first 
     *  @param pageSize
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfo> getReadByCondition(Map<String, String> conditionMap, Integer first, Integer pageSize) {
        String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        return kinfoImpl.getReadByCondition(conditionMap, date, first, pageSize);
    }
    
    /**
     *  根据条件获取可阅读的知识的数量
     *  [功能详细描述]
     *  @param conditionMap 条件
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getReadCountByCondition(Map<String, String> conditionMap) {
        String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        return kinfoImpl.getReadCountByCondition(conditionMap, date);
    }
}
