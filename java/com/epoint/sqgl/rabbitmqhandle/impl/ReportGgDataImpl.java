package com.epoint.sqgl.rabbitmqhandle.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.sqgl.rabbitmqhandle.api.IReportGgDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class ReportGgDataImpl implements IReportGgDataService {

    /**
     *
     */
    private static final long serialVersionUID = -5642142024218988732L;

    /**
     * 查询2.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param lsh
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getXmjbxxb(String lsh) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmjbxxb(lsh);
    }

    /**
     * 新增3.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param record
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int insertXmjbxxb(Record record) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().insertXmjbxxb(record);
    }

    /**
     * 查3.0已上报的项目基本信息表
     * [一句话功能简述]
     *
     * @param gcdm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getXmjbxxbByGcdm(String gcdm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmjbxxbByGcdm(gcdm);
    }

    /**
     * 查询2.0办件信息
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getXmspsxblxxb(String spsxslbm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmspsxblxxb(spsxslbm);
    }

    /**
     * 2.0其他附件信息
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getXmqtfjxxbs(String spsxslbm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmqtfjxxbs(spsxslbm);
    }

    /**
     * 查2.0的xxxxb表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getXmspsxblxxxxb(String spsxslbm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmspsxblxxxxb(spsxslbm);
    }

    /**
     * 查2.0的xmspsxbltbcxxxb 表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getXmspsxbltbcxxxb(String spsxslbm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmspsxbltbcxxxb(spsxslbm);
    }

    /**
     * 查2.0的xmspsxpfwjxxb 表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Record> getXmspsxpfwjxxb(String spsxslbm) {
        // TODO Auto-generated method stub
        return new ReportGgDataService().getXmspsxpfwjxxb(spsxslbm);
    }

}
