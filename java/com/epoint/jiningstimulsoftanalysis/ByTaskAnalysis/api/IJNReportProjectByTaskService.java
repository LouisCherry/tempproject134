package com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;

/**
 * 取证人员信息表对应的后台service接口
 * 
 * @author oliver
 * @version [版本号, 2021-02-22 15:55:39]
 */
public interface IJNReportProjectByTaskService extends Serializable
{
    
    /**
     * 根据申请时间范围和部门分页查找数据列表
     *  @param startdate
     *  @param enddate
     *  @param ouguid
     * @param pageSize 
     * @param first 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> findPageList(String startdate, String enddate, String ouguid, int first, int pageSize);
    
    /**
     * 根据申请时间范围和部门查找所有数据列表
     *  @param startdate
     *  @param enddate
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> findAllList(String startdate, String enddate, String ouguid);

    List<String> getAllAreacode(); 
   
    Record getTaskCountByouguid(String ouguid);
}
