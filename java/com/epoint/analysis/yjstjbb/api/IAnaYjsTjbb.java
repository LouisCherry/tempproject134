package com.epoint.analysis.yjstjbb.api;

import java.util.List;

import com.epoint.core.grammar.Record;
/**
 * 
 *  [一件事统计报表后台service接口] 
 * @author 28101
 * @version [版本号, 2022年10月24日]
 */
public interface IAnaYjsTjbb
{

    /**
     * 
     *  [分页查询申办量与办结量] 
     *  @param first
     *  @param pageSize
     *  @param startDate
     *  @param endDate
     *  @param applyerway
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getList(int first, int pageSize, String startDate, String endDate,String areacode, String applyerway);
    
    /**
     * 
     *  [获取区域] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAreaList();
    
    /**
     * 
     *  [计算数量] 
     *  @param first
     *  @param pageSize
     *  @param startDate
     *  @param endDate
     *  @param areacode
     *  @param applyerway
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int countList(int first, int pageSize, String startDate, String endDate, String areacode,
            String applyerway);
}
