package com.epoint.jiningzwfw.projectstatistics.evaInstance.service.impl;

import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.projectstatistics.evaInstance.service.IEvaInstanceStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EvaInstanceStatisticsServiceImpl implements IEvaInstanceStatisticsService {
    /**
     * 查询分页的统计数据
     *
     * @param map
     * @param month    用于分页
     * @param first
     * @param pageSize
     * @return
     */
    @Override
    public List<Record> findStatisticsPage(Map<String, String> map, String month, int first, int pageSize) {
        return new EvaInstanceStatisticsService().findStatisticsPage(map,month,first,pageSize);
    }

    /**
     * 计算总数
     *
     * @param map
     * @param month 用于分页
     * @return
     */
    @Override
    public int count(Map<String, String> map, String month) {
        return new EvaInstanceStatisticsService().count(map,month);
    }

    /**
     * 计算统计数据总数
     *
     * @param map
     * @param month 用于分页
     * @return
     */
    @Override
    public int countStatistics(Map<String, String> map, String month) {
        return new EvaInstanceStatisticsService().countStatistics(map,month);
    }

    /**
     * 查询分页的评级数据
     *
     * @param map
     * @param month
     * @param first
     * @param pageSize
     * @return
     */
    @Override
    public List<Record> findPage(Map<String, String> map, String month, int first, int pageSize) {
        return new EvaInstanceStatisticsService().findPage(map,month,first,pageSize);
    }

    /**
     * 查询所有的评级数据
     *
     * @param map
     * @param month
     * @return
     */
    @Override
    public List<Record> findDetailList(Map<String, String> map, String month) {
        return new EvaInstanceStatisticsService().findDetailList(map,month);
    }

}
