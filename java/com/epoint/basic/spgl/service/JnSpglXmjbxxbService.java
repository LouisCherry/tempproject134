package com.epoint.basic.spgl.service;

import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.workingday.entity.FrameWorkingday;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目基本信息表对应的后台service
 *
 * @author scr
 * @version [版本号, 2019-06-15 15:34:29]
 */
public class JnSpglXmjbxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnSpglXmjbxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglXmjbxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(SpglXmjbxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglXmjbxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public SpglXmjbxxb find(Object primaryKey) {
        return baseDao.find(SpglXmjbxxb.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public SpglXmjbxxb find(String sql, Object... args) {
        return baseDao.find(sql, SpglXmjbxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmjbxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, SpglXmjbxxb.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<SpglXmjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, SpglXmjbxxb.class, args);
    }

    public List<CodeItems> findCity(String codename) {
        String sql = "select * from code_items  where   CODEID=(select CODEID from code_main  where codename=?)  and ITEMVALUE like '37%' and itemtext!='山东省'  and (itemtext LIKE '%市'or itemtext like '%区')";
        return baseDao.findList(sql, CodeItems.class, codename);
    }

    public List<String> findSpjd(String spjd) {
        String sql = "select XMDM from SPGL_XMJBXXB  sx INNER JOIN  SPGL_DFXMSPLCJDXXB  sd on  sx.SPLCBM=sd.SPLCBM and sx.SPLCBBH=sd.SPLCBBH  where sd.DYBZSPJDXH=?";
        return baseDao.findList(sql, String.class, spjd);
    }

    /**
     * 根据项目代码和工程代码和行政区划查找项目
     * [功能详细描述]
     *
     * @param xmdm
     * @param gcdm
     * @param xzqh
     * @return
     * @see [类、类#方法、类#成员]
     */
    public SpglXmjbxxb findByXmdmAndGcdmAndXzqh(String xmdm, String gcdm, String xzqh) {
        String sql = "select * from ST_SPGL_XMJBXXB where xmdm = ? and xzqhdm = ? ";
        return baseDao.find(sql, SpglXmjbxxb.class, xmdm, xzqh);
    }

    /**
     * 计算开始和结束日期间的工作日数量
     * [功能详细描述]
     *
     * @param bdate
     * @param edate
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int countWorkingDay(String bdate, String edate) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int countday = 0;
        try {
            String bdate1 = sDateFormat.format(sDateFormat.parse(bdate));
            String edate1 = sDateFormat.format(sDateFormat.parse(edate));
            String sql = "SELECT count(*) FROM FRAME_WORKINGDAY WHERE wdate "
                    + "BETWEEN '" + bdate1 + "' AND '" + edate1 + "' and isworkingday='0'";
            String sqltotal = "SELECT count(*) FROM FRAME_WORKINGDAY WHERE wdate "
                    + "BETWEEN '" + bdate1 + "' AND '" + edate1 + "'";
            String sqlselect = "SELECT isworkingday FROM FRAME_WORKINGDAY WHERE wdate =?";
            String isworkingday = baseDao.find(sqlselect, String.class, bdate1);
            if ("0".equals(isworkingday)) {
                countday = baseDao.queryInt(sqltotal) - baseDao.queryInt(sql);
            } else {
                countday = baseDao.queryInt(sqltotal) - baseDao.queryInt(sql) - 1;
            }
        } catch (Exception px) {
            px.printStackTrace();
        }

        return countday;
    }


    /**
     * 计算开始和结束日期间的工作日数量
     * [功能详细描述]
     *
     * @param bdate
     * @param edate
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Double countWorkingDayD(String bdate, String edate) {
        Double countday = 0.0;
        String sql = "SELECT count(*) FROM FRAME_WORKINGDAY WHERE wdate "
                + "BETWEEN '" + bdate + "' AND '" + edate + "' and isworkingday='1'";
        countday = Double.valueOf(baseDao.queryInt(sql));//获取之间的工作日天数

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            Date bdate1 = sDateFormat.parse(bdate);
            Date edate1 = sDateFormat.parse(edate);

            SimpleDateFormat df = new SimpleDateFormat("HH");
            String bstr = df.format(bdate1);
            int b = Integer.parseInt(bstr);
            String estr = df.format(edate1);
            int e = Integer.parseInt(estr);

            if ((b < 12 && e < 12) || (b >= 12 && e >= 12)) {
                countday = countday + 0.5;
            } else {
                countday = countday + 1;
            }

        } catch (Exception px) {
            px.printStackTrace();
        }

        return countday;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public int daysBetween(Date smdate, Date bdate) {
        long l = bdate.getTime() - smdate.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        return Integer.parseInt(String.valueOf(day)) + 1;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public Double daysBetweenD(String bdate, String edate) {
        Double countday = 0.0;
        String sql = "SELECT count(*) FROM FRAME_WORKINGDAY WHERE wdate "
                + "BETWEEN '" + bdate + "' AND '" + edate + "'  ";
        countday = Double.valueOf(baseDao.queryInt(sql));//获取之间的自然日天数

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date bdate1 = sDateFormat.parse(bdate);
            Date edate1 = sDateFormat.parse(edate);

            SimpleDateFormat df = new SimpleDateFormat("HH");
            String bstr = df.format(bdate1);
            int b = Integer.parseInt(bstr);
            String estr = df.format(edate1);
            int e = Integer.parseInt(estr);

            if ((b < 12 && e < 12) || (b >= 12 && e >= 12)) {
                countday = countday + 0.5;
            } else {
                countday = countday + 1;
            }
        } catch (Exception px) {
            px.printStackTrace();
        }

        return countday;
    }


    //判断指定工作日天数后的日期
    public String getWorkDay(Date date, int daysize) {
        String wdate = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd");
        String wtime = EpointDateUtil.convertDate2String(date, " HH:mm:ss");
        //判断指定工作日天数后的日期
        String sql = "select * from FRAME_WORKINGDAY where wdate >= ? and ISWORKINGDAY='1' ORDER BY wdate asc";
        List<FrameWorkingday> workinglist = new ArrayList<FrameWorkingday>();
        workinglist = baseDao.findList(sql, FrameWorkingday.class, wdate);
        String rdate = workinglist.get(daysize).getWdate();
        return rdate + wtime;
    }

    /**
     * 查找城市名称
     * [功能详细描述]
     *
     * @param citycode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getCityname(String citycode) {
        String sql = "select cityname from huiyuan_city where citycode = ? ";
        return baseDao.queryString(sql, citycode);
    }

    /**
     * 查找安徽省城市
     * [功能详细描述]
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Record> findAhCity() {
        String sql = "select * from huiyuan_city where citycode like '37%00' ORDER BY citycode asc";
        return baseDao.findList(sql, Record.class);
    }

    /**
     * 根据月份查找项目数量 不传值时 默认查询所有
     * [功能详细描述]
     *
     * @param xzqhdm
     * @param month
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int countXmByMonth(String xzqhdm, String month) {
        List<String> list = new ArrayList<>();
        String sql = "select count(*) from st_spgl_xmjbxxb where xzqhdm = ? ";
        list.add(xzqhdm);
        if (month != null) {
            sql += "and sbsj like ? ";
            list.add(month + "%");
        }
        return baseDao.queryInt(sql, SpglXmjbxxb.class, list.toArray());
    }

    //查询国标行业
    public String findGbhy(String gbhycode) {
        String sql = "select DISTINCT item_name from validate_item  where item_code = ? ";
        return baseDao.queryString(sql, gbhycode);
    }

}
