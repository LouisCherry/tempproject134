package com.epoint.knowledge.oumanage.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfo;



/**
 * 
 * 知识库信息接口
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public interface ICnsKinfo extends ICnsCommon<CnsKinfo>
{
    /**
     * 
     *  查询失效且仍在使用的知识
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfo> getUneffectiveKinfoByTime(String date);
    
    /**
     *  根据父节点获取其下所有可以阅读的知识
     *  [功能详细描述]
     *  @param categoryguid 父节点知识类别
     *  @param date 当前日期
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<CnsKinfo> getReadByCondition(Map<String, String> conditionMap, String date, Integer first,
            Integer pageSize);

    /**
     *  根据条件获取可阅读的知识的数量
     *  [功能详细描述]
     *  @param conditionMap 条件
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getReadCountByCondition(Map<String, String> conditionMap,String date);
}
