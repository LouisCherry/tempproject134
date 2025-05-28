package com.epoint.auditsp.yqxm.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.epoint.auditsp.yqxm.api.entity.StSpglXmspsxblxxxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 项目审批事项办理详细信息表对应的后台service
 * 
 * @author fenglin
 * @version [版本号, 2019-07-18 10:39:18]
 */
public class StSpglXmspsxblxxxxbService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public static final int NUMBER_OF_CYCLES = 4;

    /**
     * DAO
     */
    public StSpglXmspsxblxxxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(StSpglXmspsxblxxxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @param guid guid
     * @return 返回值
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(StSpglXmspsxblxxxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(StSpglXmspsxblxxxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public StSpglXmspsxblxxxxb find(Object primaryKey) {
        return baseDao.find(StSpglXmspsxblxxxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public StSpglXmspsxblxxxxb find(String sql, Object... args) {
        return baseDao.find(sql, StSpglXmspsxblxxxxb.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<StSpglXmspsxblxxxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, StSpglXmspsxblxxxxb.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<StSpglXmspsxblxxxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, StSpglXmspsxblxxxxb.class, args);
    }

    /**
     * 查找所有办件数据
     * @param xzqhdm 行政区划代码
     * @param gcdm 工程代码
     * @param spsxslbm 审批事项实例编码
     * @return 返回值
     */
    public List<StSpglXmspsxblxxxxb> findAllSpsxslbm(String xzqhdm, String gcdm, String spsxslbm) {
        String sql = "select * from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and gcdm = ? "
                + " and spsxslbm = ? order by blzt asc,blsj asc";
        return baseDao.findList(sql, StSpglXmspsxblxxxxb.class, xzqhdm, gcdm, spsxslbm);
    }

    /**
     * 查找正常办件数据
     * @param xzqhdm 行政区划代码
     * @param gcdm 工程代码
     * @param spsxslbm 审批事项实例编码
     * @return 返回值
     */
    public List<StSpglXmspsxblxxxxb> findAcceptBySpsxslbm(String xzqhdm, String gcdm, String spsxslbm) {
        String sql = "select * from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and gcdm = ? "
                + " and spsxslbm = ? order by blzt desc,blsj desc";
        return baseDao.findList(sql, StSpglXmspsxblxxxxb.class, xzqhdm, gcdm, spsxslbm);
    }

    /**
     * 查找历史退件数据
     * @param xzqhdm 行政区划代码
     * @param gcdm 工程代码
     * @param spsxslbm 审批事项实例编码
     * @return 返回值
     */
    public List<StSpglXmspsxblxxxxb> findRefuseBySpsxslbm(String xzqhdm, String gcdm, String spsxslbm) {
        //不受理，不予受理
        String sql = "select * from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and gcdm = ? "
                + " and spsxslbm = ? and blzt in('4','5') order by blsj desc";
        return baseDao.findList(sql, StSpglXmspsxblxxxxb.class, xzqhdm, gcdm, spsxslbm);
    }

    /**
     * 计算历史退件的数量
     * @param xzqhdm 行政区划代码
     * @param gcdm 工程代码
     * @param spsxslbm 审批事项实例编码
     * @return 返回值
     */
    public int countRefuseSpsx(String xzqhdm, String gcdm, String spsxslbm) {
        String sql = "select count(*) from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and gcdm = ? "
                + " and spsxslbm = ? and blzt in('4','5') ";
        return baseDao.queryInt(sql, xzqhdm, gcdm, spsxslbm);
    }

    /**
     * 计算辖区内历史退件的数量
     * @param xzqhdm 行政区划代码
     * @return 返回值
     */
    public int countXiaquRefuseSpsx(String xzqhdm) {
        String sql = "select count(*) from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and blzt in('4','5') ";
        return baseDao.queryInt(sql, xzqhdm);
    }

    /**
     * 查找指定城市指定月份的办理事项个数
     * @param xzqhdm 行政区划代码
     * @param month 月份
     * @param flag 状态
     * @return 返回值
     */
    public int countSpsxByMonth(String xzqhdm, String month, String flag) {
        List<Object> params = new ArrayList<Object>();
        params.add(xzqhdm);
        String sql = "select count(*) from (select spsxslbm,max(blzt) from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? ";
        if (StringUtil.isNotBlank(month)) {
            params.add("%" + month + "%");
            sql += " and blsj like ? ";
        }
        if ("1".equals(flag)) {
            sql += " and blzt in('11','12','13')";
        }
        else {
            sql += " and blzt not in('11','12','13')";
        }
        sql += " group by spsxslbm) countblxx";
        return baseDao.queryInt(sql, StSpglXmspsxblxxxxb.class, params.toArray());
    }

    /**
     * 获取项目审批数量在办状态统计
     * @param xzqhdm 行政区划代码
     * @param month 月份
     * @param flag 状态
     * @return 返回值
     */
    public int countBlztByMonth(String xzqhdm, String month, String flag) {
        List<Object> params = new ArrayList<Object>();
        params.add(xzqhdm);
        params.add("%" + month + "%");
        //状态4、5分类为退件flag=1，2、14、15为其他flag=3，11、12、13为办结 , 1、3/6/7/8/9/10为常规办理flag=3
        String sql = "select count(*) from ST_SPGL_XMSPSXBLXXXXB where sjyxbs='1' and xzqhdm = ? and blsj like ? ";
        if ("1".equals(flag)) {
            sql += " and blzt in('4','5')";
        }
        else if ("2".equals(flag)) {
            sql += " and blzt in('2','14','15')";
        }
        else if ("3".equals(flag)) {
            sql += " and blzt in('1','3','6','7','8','9','10')";
        }
        return baseDao.queryInt(sql, StSpglXmspsxblxxxxb.class, params.toArray());
    }

    /**
     * 查找非工作时间办理事项情况
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @param xmmc 项目名称
     * @param spsxmc 审批事项名称
     * @return 返回值
     */
    public PageData<Record> findNonworking(int pageNumber, int pageSize, String xmmc, String spsxmc) {
        List<Object> params = new ArrayList<Object>();
        PageData<Record> result = new PageData<Record>();
        String sql = "select b.xmdm,b.xmmc,b.xzqhdm,a.blsj,a.blzt,b.spsxmc,b.gcdm,a.spsxslbm from ("
                + "select xzqhdm,gcdm,spsxslbm,blsj,blzt from  st_spgl_xmspsxblxxxxb where sjyxbs='1' and date(blsj) in (select wdate from FRAME_WORKINGDAY where isworkingday='0') "
                + "or date(blsj) in (select wdate from FRAME_WORKINGDAY where isworkingday='1') and time(blsj) not between '08:00:00' and '17:30:00') a "
                + "left join (select * from st_spgl_xmspsxblxxb where sjyxbs='1') b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1 ";
        String sql2 = "select count(*) from ("
                + "select xzqhdm,gcdm,spsxslbm,blsj,blzt from  st_spgl_xmspsxblxxxxb where sjyxbs='1' and date(blsj) in (select wdate from FRAME_WORKINGDAY where isworkingday='0') "
                + "or date(blsj) in (select wdate from FRAME_WORKINGDAY where isworkingday='1') and time(blsj) not between '08:00:00' and '17:30:00') a "
                + "left join (select * from st_spgl_xmspsxblxxb where sjyxbs='1') b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1 ";
        if (StringUtil.isNotBlank(xmmc)) {
            params.add("%" + xmmc + "%");
            sql += " and b.xmmc like ? ";
            sql2 += " and b.xmmc like ? ";
        }
        if (StringUtil.isNotBlank(spsxmc)) {
            params.add("%" + spsxmc + "%");
            sql += " and b.spsxmc like ? ";
            sql2 += " and b.spsxmc like ? ";
        }
        List<Record> list = baseDao.findList(sql, pageNumber, pageSize, Record.class, params.toArray());
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, params.toArray()));
        return result;
    }

    /**
     * 查找多次受理办结的办件
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @param xmmc 项目名称
     * @param spsxmc 审批事项名称
     * @return 返回值
     */
    /*public PageData<Record> findManyAcceptEnd(int pageNumber, int pageSize, String xmmc, String spsxmc) {
        List<Object> params = new ArrayList<Object>();
        PageData<Record> result = new PageData<Record>();
        String sql = "select a.sumsl,a.sumbj,b.xmdm,b.xmmc,b.xzqhdm,b.spsxmc,a.gcdm,b.spsxslbm from (select * from ("
                + "select sum(blzt='3') as sumsl,sum(blzt in('11,12,13')) as sumbj,xzqhdm,gcdm,SPSXslbm from st_spgl_xmspsxblxxxxb "
                + "where sjyxbs='1' GROUP BY xzqhdm,gcdm,SPSXslbm) c where sumsl>1 or sumbj>1) a inner join ("
                + "select * from st_spgl_xmspsxblxxb where sjyxbs='1') b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1";
        String sql2 = "select count(*) from (select * from ("
                + "select sum(blzt='3') as sumsl,sum(blzt in('11,12,13')) as sumbj,xzqhdm,gcdm,SPSXslbm from st_spgl_xmspsxblxxxxb "
                + "where sjyxbs='1' GROUP BY xzqhdm,gcdm,SPSXslbm) c where sumsl>1 or sumbj>1) a inner join ("
                + "select * from st_spgl_xmspsxblxxb where sjyxbs='1') b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1";
        if (StringUtil.isNotBlank(xmmc)) {
            params.add("%" + xmmc + "%");
            sql += " and b.xmmc like ? ";
            sql2 += " and b.xmmc like ? ";
        }
        if (StringUtil.isNotBlank(spsxmc)) {
            params.add("%" + spsxmc + "%");
            sql += " and b.spsxmc like ? ";
            sql2 += " and b.spsxmc like ? ";
        }
        List<Record> list = baseDao.findList(sql, pageNumber, pageSize, Record.class, params.toArray());
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, params.toArray()));
        return result;
    }*/

    /**
     * 查找办理状态异常的办件(特别开始未结束，特别结束未开始，补正开始未结束，补正结束未开始)
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @param xmmc 项目名称
     * @param spsxmc 审批事项名称
     * @return 返回值
     */
    /*public PageData<Record> findHandAbnormal(int pageNumber, int pageSize, String xmmc, String spsxmc) {
        List<Object> params = new ArrayList<Object>();
        PageData<Record> result = new PageData<Record>();
        String sql = "select * from (select sum(blzt='6') as bzks ,sum(blzt='7') as bzjs ,sum(blzt='9') as tbks,sum(blzt='10') as tbjs,"
                + "b.xzqhdm,b.gcdm,b.spsxslbm,a.xmdm,a.xmmc,a.spsxmc from (select xzqhdm,gcdm,spsxslbm,xmdm,xmmc,spsxmc from st_spgl_xmspsxblxxb "
                + "where sjyxbs='1' and BJSFWQBJ='1') a right join (select * from st_spgl_xmspsxblxxxxb where sjyxbs='1') b on a.xzqhdm=b.xzqhdm "
                + "and a.gcdm=b.gcdm and a.spsxslbm=b.spsxslbm GROUP BY b.xzqhdm,b.gcdm,b.spsxslbm,a.xmdm,a.xmmc,a.spsxmc) a where xmdm is not null "
                + "and ((bzks>0 and bzjs=0) or (bzks=0 and bzjs>0) or (tbks>0 and tbjs=0) or (tbks=0 and tbjs>0))";
        String sql2 = "select count(*) from (select sum(blzt='6') as bzks ,sum(blzt='7') as bzjs ,sum(blzt='9') as tbks,sum(blzt='10') as tbjs,"
                + "b.xzqhdm,b.gcdm,b.spsxslbm,a.xmdm,a.xmmc,a.spsxmc from (select xzqhdm,gcdm,spsxslbm,xmdm,xmmc,spsxmc from st_spgl_xmspsxblxxb "
                + "where sjyxbs='1' and BJSFWQBJ='1') a right join (select * from st_spgl_xmspsxblxxxxb where sjyxbs='1') b on a.xzqhdm=b.xzqhdm "
                + "and a.gcdm=b.gcdm and a.spsxslbm=b.spsxslbm GROUP BY b.xzqhdm,b.gcdm,b.spsxslbm,a.xmdm,a.xmmc,a.spsxmc) a where xmdm is not null "
                + "and ((bzks>0 and bzjs=0) or (bzks=0 and bzjs>0) or (tbks>0 and tbjs=0) or (tbks=0 and tbjs>0))";
        if (StringUtil.isNotBlank(xmmc)) {
            params.add("%" + xmmc + "%");
            sql += " and xmmc like ? ";
            sql2 += " and xmmc like ? ";
        }
        if (StringUtil.isNotBlank(spsxmc)) {
            params.add("%" + spsxmc + "%");
            sql += " and spsxmc like ? ";
            sql2 += " and spsxmc like ? ";
        }
        List<Record> list = baseDao.findList(sql, pageNumber, pageSize, Record.class, params.toArray());
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, params.toArray()));
        return result;
    }*/

    /**
     * 获取该项目第一个办理时间
     * @param xmdm 项目代码
     * @return 返回值
     */
    public Date getFirstBlsjByXmdm(String xmdm) {
        String sql = "select xx.blsj from ST_SPGL_XMSPSXBLXXB bl INNER JOIN st_spgl_xmspsxblxxxxb xx on "
                + "bl.XZQHDM=xx.XZQHDM AND bl.GCDM=xx.GCDM AND bl.SPSXSLBM = xx.SPSXSLBM AND bl.SJYXBS=xx.SJYXBS "
                + "where bl.xmdm = ?  and bl.SJYXBS='1' and xx.SJYXBS='1' ORDER BY xx.BLSJ LIMIT 1";
        return baseDao.find(sql, Date.class, xmdm);
    }

    /**
     * 查询退件列表
     * @param first first
     * @param pageSize pageSize
     * @param xmdm 项目代码
     * @return 返回值
     */
    public PageData<Record> findTJList(int first, int pageSize, String xmdm) {
        PageData<Record> result = new PageData<>();
        String sql = "select b.SPSXMC as sxmc,a.spsxslbm,a.XZQHDM,a.gcdm from ST_SPGL_XMSPSXBLXXXXB a,ST_SPGL_XMSPSXBLXXB b  where a.gcdm=b.gcdm and a.spsxslbm=b.spsxslbm and a.BLZT in (4,5) and a.sjyxbs=1 and b.XMDM = ? ";
        String sql2 = "select count(*) from (  select b.SPSXMC,a.spsxslbm,a.XZQHDM,a.gcdm from ST_SPGL_XMSPSXBLXXXXB a,ST_SPGL_XMSPSXBLXXB b  where a.gcdm=b.gcdm and a.spsxslbm=b.spsxslbm and a.BLZT in (4,5) and a.sjyxbs=1 and b.XMDM = ? ) a";
        List<Record> list = baseDao.findList(sql, first, pageSize, Record.class, xmdm);
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, xmdm));
        return result;
    }

    /**
     *  查找多次受理办结的办件
     *  @param first first
     *  @param pageSize pageSize
     *  @param xmmc 项目名称
     *  @param spsxmc 审批事项名称
     *  @param xzqhdm 行政区划代码
     *  @return 返回值
     */
    public PageData<Record> findManyAcceptEnd(int first, int pageSize, String xmmc, String spsxmc, String xzqhdm) {
        List<Object> params = new ArrayList<Object>();
        PageData<Record> result = new PageData<Record>();
        String sql = "select a.sumsl,a.sumbj,b.xmdm,b.xmmc,b.xzqhdm,b.spsxmc,a.gcdm,b.spsxslbm,b.slsj from (select * from ("
                + "select sum(blzt='3') as sumsl,sum(blzt in('11,12,13')) as sumbj,xzqhdm,gcdm,SPSXslbm from st_spgl_xmspsxblxxxxb "
                + "where sjyxbs='1'";
        String sql2 = "select count(*) from (select * from ("
                + "select sum(blzt='3') as sumsl,sum(blzt in('11,12,13')) as sumbj,xzqhdm,gcdm,SPSXslbm from st_spgl_xmspsxblxxxxb "
                + "where sjyxbs='1'";
        if(StringUtil.isNotBlank(xzqhdm)){
            params.add(xzqhdm);
            sql += " AND XZQHDM=?";
            sql2 += " AND XZQHDM=?";
        }
        sql += " AND BLZT IN ('3','11','12','13') GROUP BY xzqhdm,gcdm,SPSXslbm) c where sumsl>1 or sumbj>1) a inner join ("
                + "select * from st_spgl_xmspsxblxxb where sjyxbs='1'";
        sql2 += " AND BLZT IN ('3','11','12','13') GROUP BY xzqhdm,gcdm,SPSXslbm) c where sumsl>1 or sumbj>1) a inner join ("
                + "select * from st_spgl_xmspsxblxxb where sjyxbs='1'";
        if(StringUtil.isNotBlank(xzqhdm)){
            params.add(xzqhdm);
            sql += " AND XZQHDM=?";
            sql2 += " AND XZQHDM=?";
        }
        sql += " ) b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1";
        sql2 += " ) b on a.xzqhdm = b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm = b.spsxslbm where 1=1";

        /*if (StringUtil.isNotBlank(xzqhdm)) {
            params.add("%" + xzqhdm.substring(0, NUMBER_OF_CYCLES) + "%");
            sql += " AND b.xzqhdm like ? ";
            sql2 += " AND b.xzqhdm like ? ";
        }*/
        if (StringUtil.isNotBlank(xmmc)) {
            params.add("%" + xmmc + "%");
            sql += " and b.xmmc like ? ";
            sql2 += " and b.xmmc like ? ";
        }
        if (StringUtil.isNotBlank(spsxmc)) {
            params.add("%" + spsxmc + "%");
            sql += " and b.spsxmc like ? ";
            sql2 += " and b.spsxmc like ? ";
        }
        List<Record> list = baseDao.findList(sql, first, pageSize, Record.class, params.toArray());
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, params.toArray()));
        return result;
    }

    /**
     * 查找特别程序异常的办件
     * @param first first
     * @param pageSize pageSize
     * @param xmmc 项目名称
     * @param spsxmc 审批事项名称
     * @param xzqhdm 行政区划代码
     * @param xmdm 项目代码
     * @return 返回值
     */
    public PageData<Record> findHandAbnormalWithXzqhdm(int first, int pageSize, String xmmc, String spsxmc,
            String xzqhdm, String xmdm) {
        List<Object> params = new ArrayList<Object>();
        PageData<Record> result = new PageData<Record>();
        String sql = "select * from (select a.xzqhdm,a.xmdm,a.xmmc,a.gcdm,a.spsxslbm,a.spsxmc,a.bjsfwqbj,"
                + "b.bzks,b.bzjs,b.tbks,b.tbjs from (select * from st_spgl_xmspsxblxxb where sjyxbs='1' ";

        if (StringUtil.isNotBlank(xzqhdm)) {
            params.add("%" + xzqhdm.substring(0, NUMBER_OF_CYCLES) + "%");
            sql += " AND xzqhdm like ? ";
        }
        if (StringUtil.isNotBlank(xmmc)) {
            params.add("%" + xmmc + "%");
            sql += " and xmmc like ? ";
        }
        if (StringUtil.isNotBlank(xmdm)) {
            params.add("%" + xmdm + "%");
            sql += " and xmdm like ? ";
        }
        if (StringUtil.isNotBlank(spsxmc)) {
            params.add("%" + spsxmc + "%");
            sql += " and spsxmc like ? ";
        }

        sql += " ) a INNER JOIN (select sum(blzt='6') as bzks ,sum(blzt='7') as bzjs ,sum(blzt='9') as tbks,sum(blzt='10') as tbjs,"
                + "xzqhdm,gcdm,spsxslbm from st_spgl_xmspsxblxxxxb where sjyxbs='1'";
        if (StringUtil.isNotBlank(xzqhdm)) {
            params.add("%" + xzqhdm.substring(0, NUMBER_OF_CYCLES) + "%");
            sql += " AND xzqhdm like ? ";
        }
        sql += " AND BLZT IN ('6','7','9','10') "
                + "GROUP BY xzqhdm,gcdm,SPSXslbm) b on a.xzqhdm=b.xzqhdm and a.gcdm=b.gcdm and a.spsxslbm=b.spsxslbm) s "
                + "where IFNULL(xmdm,'')<>'' and ((bzks-bzjs>0 and bjsfwqbj=1) or (bzks-bzjs>1) or (bzjs-bzks>0) or "
                + "(tbks-tbjs>0 and bjsfwqbj=1) or (tbks-tbjs>1) or (tbjs-tbks>0))";

        
        String sql2 = "select count(*) from (" + sql + ") h";
        List<Record> list = baseDao.findList(sql, first, pageSize, Record.class, params.toArray());
        result.setList(list);
        result.setRowCount(baseDao.queryInt(sql2, params.toArray()));
        return result;
    }

}
