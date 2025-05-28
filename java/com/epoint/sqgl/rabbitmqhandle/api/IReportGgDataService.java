package com.epoint.sqgl.rabbitmqhandle.api;

import com.epoint.core.grammar.Record;

import java.io.Serializable;
import java.util.List;

public interface IReportGgDataService extends Serializable {

    /**
     * 查询2.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param lsh
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    Record getXmjbxxb(String lsh);

    /**
     * 新增3.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param record
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    int insertXmjbxxb(Record record);

    /**
     * 查3.0已上报的项目基本信息表
     * [一句话功能简述]
     *
     * @param gcdm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    Record getXmjbxxbByGcdm(String gcdm);

    /**
     * 查询2.0办件信息
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    Record getXmspsxblxxb(String spsxslbm);

    /**
     * 2.0其他附件信息
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> getXmqtfjxxbs(String spsxslbm);

    /**
     * 查2.0的xxxxb表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> getXmspsxblxxxxb(String spsxslbm);

    /**
     * 查2.0的xmspsxbltbcxxxb 表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> getXmspsxbltbcxxxb(String spsxslbm);

    /**
     * 查2.0的xmspsxpfwjxxb 表
     * [一句话功能简述]
     *
     * @param spsxslbm
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<Record> getXmspsxpfwjxxb(String spsxslbm);

}
