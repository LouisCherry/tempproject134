package com.epoint.jiningzwfw.projectstatistics.evaInstance.service;

import com.epoint.core.grammar.Record;

import java.util.List;
import java.util.Map;

/**
 * @author jiem
 */
public interface IEvaInstanceStatisticsService {


    /**
     * 查询分页的统计数据
     * @param map
     * @param month 用于分页
     * @param first
     * @param pageSize
     * @return
     */
    List<Record> findStatisticsPage(Map<String, String> map, String month, int first, int pageSize);


    /**
     * 计算评价列表总数
     * @param map
     * @param month 用于分页
     * @return
     */
    int count(Map<String, String> map, String month);

    /**
     * 计算统计数据总数
     * @param map
     * @param month 用于分页
     * @return
     */
    int countStatistics(Map<String, String> map, String month);

    /**
     * 查询分页的评级数据
     * @param map
     * @param month
     * @param first
     * @param pageSize
     * @return
     */
    List<Record> findPage(Map<String, String> map, String month, int first, int pageSize);

    /**
     * 查询所有的评级数据
     * @param map
     * @param month
     * @return
     */
    List<Record> findDetailList(Map<String, String> map, String month);
}
