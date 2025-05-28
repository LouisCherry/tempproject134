package com.epoint.banjiantongjibaobiao.api;

import com.epoint.core.grammar.Record;

import java.util.Date;
import java.util.List;

/**
 * 事项统计表API
 *
 * @author Epoint
 */
public interface ISyncTaskProjectJobService {
    /**
     * 获取统计表中最新的日期信息,和数量
     *
     * @return
     */
    Record getLastDate();

    /**
     * 获取办件表中最小的日期值
     *
     * @return 最小日期
     */
    Date getMinDate();

    /**
     * 获取办件表中指定日期的部门GUID列表
     *
     * @return
     */
    List<String> getOuGuidList(Date startDate);

    /**
     * 传入部门guid，和申请日期，查询该部门当天的办件情况
     *
     * @param startDate 指定的查询日期
     * @param ouGuid    部门guid
     * @return 办件信息和办件不同状态下的总量
     */
    List<Record> getInfos(Date startDate, String ouGuid);

    /**
     * 删除指定日期指定部门的数据
     *
     * @param startDate 指定的日期
     * @param ouGuid    指定的部门GUID
     * @return 返回影响的行数
     */
    int deleteExistDate(Date startDate, String ouGuid);

    /**
     * 向统计表中插入数据
     *
     * @param value
     */
    void insertNewRecord(Record value);

    /**
     * 测试分表效果
     *
     * @param startDate
     * @param ouGuid
     * @return
     */
    List<Record> getTjInfo(Date startDate, String ouGuid);
}
