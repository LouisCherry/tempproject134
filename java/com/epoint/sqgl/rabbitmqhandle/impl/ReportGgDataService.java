package com.epoint.sqgl.rabbitmqhandle.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

import java.util.List;

public class ReportGgDataService {

    private static final String twoqzkURL = ConfigUtil.getConfigValue("reporteddatajdbc", "twoqzkurl");
    private static final String twoqzkNAME = ConfigUtil.getConfigValue("reporteddatajdbc", "twoqzkname");
    private static final String twoqzkPASSWORD = ConfigUtil.getConfigValue("reporteddatajdbc", "twoqzkpassword");
    private DataSourceConfig twoqzkdataSourceConfig = new DataSourceConfig(twoqzkURL, twoqzkNAME, twoqzkPASSWORD);

    private static final String threeqzkURL = ConfigUtil.getConfigValue("reporteddatajdbc", "threeqzkurl");
    private static final String threeqzkNAME = ConfigUtil.getConfigValue("reporteddatajdbc", "threeqzkname");
    private static final String threeqzkPASSWORD = ConfigUtil.getConfigValue("reporteddatajdbc", "threeqzkpassword");
    private DataSourceConfig threeqzkdataSourceConfig = new DataSourceConfig(threeqzkURL, threeqzkNAME,
            threeqzkPASSWORD);

    /**
     * 数据增删改查组件
     */
    // protected ICommonDao baseDao;
    protected ICommonDao twoqzkDao;
    protected ICommonDao threeqzkDao;

    public ReportGgDataService() {
        // baseDao = CommonDao.getInstance();
        twoqzkDao = CommonDao.getInstance(twoqzkdataSourceConfig);
        threeqzkDao = CommonDao.getInstance(threeqzkdataSourceConfig);
    }

    /**
     * 查询2.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param lsh
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getXmjbxxb(String lsh) {
        String sql = "select * from spgl_xmjbxxb where lsh=?";
        return twoqzkDao.find(sql, Record.class, lsh);
    }

    /**
     * 新增3.0项目基本信息表数据
     * [一句话功能简述]
     *
     * @param record
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int insertXmjbxxb(Record record) {
        // TODO Auto-generated method stub
        return threeqzkDao.insert(record);
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
    public Record getXmjbxxbByGcdm(String gcdm) {
        String sql = "select * from spgl_xmjbxxb where gcdm=? and sjyxbs=1 and SJSCZT =3 limit 1";
        return threeqzkDao.find(sql, Record.class, gcdm);
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
    public Record getXmspsxblxxb(String spsxslbm) {
        String sql = "select * from spgl_xmspsxblxxb where spsxslbm=? and sjyxbs=1 and SJSCZT =3 order by scsj desc limit 1";
        return twoqzkDao.find(sql, Record.class, spsxslbm);
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
    public List<Record> getXmqtfjxxbs(String spsxslbm) {
        String sql = "select * from spgl_xmqtfjxxb where spsxslbm=? and sjyxbs=1 and SJSCZT =3 order by scsj desc";
        return twoqzkDao.findList(sql, Record.class, spsxslbm);
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
    public List<Record> getXmspsxblxxxxb(String spsxslbm) {
        String sql = "select * from spgl_xmspsxblxxxxb where spsxslbm=? and sjyxbs=1 and SJSCZT =3 order by scsj desc";
        return twoqzkDao.findList(sql, Record.class, spsxslbm);
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
    public List<Record> getXmspsxbltbcxxxb(String spsxslbm) {
        String sql = "select * from spgl_xmspsxbltbcxxxb where spsxslbm=? and sjyxbs=1 and SJSCZT =3 order by scsj desc";
        return twoqzkDao.findList(sql, Record.class, spsxslbm);
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
    public List<Record> getXmspsxpfwjxxb(String spsxslbm) {
        String sql = "select * from spgl_xmspsxpfwjxxb where spsxslbm=? and sjyxbs=1 and SJSCZT =3 order by scsj desc";
        return twoqzkDao.findList(sql, Record.class, spsxslbm);
    }

}
