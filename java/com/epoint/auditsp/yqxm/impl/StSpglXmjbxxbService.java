package com.epoint.auditsp.yqxm.impl;

import com.epoint.auditsp.yqxm.api.entity.StSpglXmjbxxb;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目基本信息表对应的后台service
 *
 * @author fenglin
 * @version [版本号, 2019-07-02 19:27:32]
 */
public class StSpglXmjbxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public StSpglXmjbxxbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(StSpglXmjbxxb record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(StSpglXmjbxxb.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(StSpglXmjbxxb record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public StSpglXmjbxxb find(Object primaryKey) {
        return baseDao.find(StSpglXmjbxxb.class, primaryKey);
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
    public StSpglXmjbxxb find(String sql, Object... args) {
        return baseDao.find(sql, StSpglXmjbxxb.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<StSpglXmjbxxb> findList(String sql, Object... args) {
        return baseDao.findList(sql, StSpglXmjbxxb.class, args);
    }

    public int findListCount(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
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
    public List<StSpglXmjbxxb> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, StSpglXmjbxxb.class, args);
    }

    // 城市
    public List<CodeItems> findCity(String codename) {
        String sql = "select * from code_items where CODEID=(select CODEID from code_main where codename=?) and ITEMVALUE like '37%' and itemtext!='山东省' and (itemtext LIKE '%市'or itemtext like '%区')";
        return baseDao.findList(sql, CodeItems.class, codename);
    }

    // 查询国标行业
    public String findGbhy(String gbhycode, String gbhydmfbnd) {
        String sql = "select item_name from validate_item  where item_code=? and item_year=?";
        return baseDao.queryString(sql, gbhycode, gbhydmfbnd);
    }

    // 正常项目数
    public int getCountZcxms(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from ST_SPGL_XMJBXXB where sjyxbs=1 and XMSFYQ =0 and SFYTJ='0' and xmdm in (select a.xmdm from ST_SPGL_XMJBXXB a  left JOIN st_spgl_gcjdxxb b ON a.XMDM=b.XMDM WHERE b.XMDM IS NOT NULL GROUP BY a.XMDM) ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 逾期项目数 0：正常 1：逾期项目
    public int getCountXmsfyq(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from ST_SPGL_XMJBXXB where sjyxbs=1 and XMSFYQ >=1  and xmdm in (select a.xmdm from ST_SPGL_XMJBXXB a  left JOIN st_spgl_gcjdxxb b ON a.XMDM=b.XMDM WHERE b.XMDM IS NOT NULL GROUP BY a.XMDM) ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 不予受理数
    public int getCountBysl(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from ST_SPGL_XMJBXXB where sjyxbs=1 and SFYTJ='1'  and xmdm in (select a.xmdm from ST_SPGL_XMJBXXB a  left JOIN st_spgl_xmspsxblxxb b ON a.xzqhdm=b.xzqhdm and a.XMDM=b.XMDM WHERE b.XMDM IS NOT NULL and b.sjyxbs=1 GROUP BY a.XMDM) ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    //
    public int getCountByBlztQt(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "SELECT count(*) FROM st_spgl_xmjbxxb WHERE SJYXBS=1 AND (XZQHDM,XMDM) NOT IN (SELECT a.XZQHDM,a.XMDM FROM st_spgl_xmjbxxb a LEFT JOIN st_spgl_xmspsxblxxb b ON a.XZQHDM=b.XZQHDM AND a.XMDM=b.XMDM WHERE a.SJYXBS=1 AND b.SJYXBS=1 GROUP BY a.XZQHDM,a.XMDM)";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 未校验数
    public int getCountWjyxms(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where 1=1 and SJSCZT='0' ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 校验失败数
    public int getCountJysbxms(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where 1=1 and SJSCZT='2' ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 所有办件数量
    public int getCountAllBJ(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 政府投资类项目

    public int getCountZjtzlxm(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 and  SPLCLX in ('1','2')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 社会投资类项目
    public int getCountShtzlxm(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 and  SPLCLX in ('3','4')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 带方案出让土地类项目
    public int getCountFatdlxm(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX='5'";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 工业投资项目类
    public int getCountGytzlxm(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX='6'";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 其他项目类
    public int getCountQtlxm(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX in ('5','7','8','9','10')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    // 政府投资总额
    public Record getZftztzzem(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select sum(ztze)/10000 as tzze from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX in ('1','2')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.find(sql, Record.class, params.toArray());
    }

    // 社会投资总额
    public Record getShtztzze(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select sum(ztze)/10000 as tzze from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX in ('3','4')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.find(sql, Record.class, params.toArray());
    }

    // 工业投资总额
    public Record getGytztzze(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select sum(ztze)/10000 as tzze from  ST_SPGL_XMJBXXB  where sjyxbs=1 and   SPLCLX in ('6')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.find(sql, Record.class, params.toArray());
    }

    // 其它投资总额
    public Record getQttzze(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select sum(ztze)/10000 as tzze from  ST_SPGL_XMJBXXB   where sjyxbs=1 and   SPLCLX in ('5','7','8','9','10')";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.find(sql, Record.class, params.toArray());
    }

    // 所有项目数
    public int getCountALLXM(String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select count(*) from  ST_SPGL_XMJBXXB   where sjyxbs=1 ";
        if (StringUtil.isNotBlank(xzqhdm)) {
            if ("370000".equals(xzqhdm) || "37".equals(xzqhdm)) {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 2) + "%'");
            } else {
                sql += " and xzqhdm like ? ";
                params.add("'" + xzqhdm.substring(0, 4) + "%'");
            }
        }
        return baseDao.queryInt(sql, params.toArray());
    }

    /**
     * 根据项目代码和行政区划代码查找项目信息 [功能详细描述]
     *
     * @param xmdm
     * @param xzqhdm
     * @return
     * @see [类、类#方法、类#成员]
     */
    public StSpglXmjbxxb findByXmdmAndXzqh(String xmdm, String xzqhdm) {
        List<String> params = new ArrayList<>();
        String sql = "select a.*,a.YDMJ/10000 as sydmj,a.jzmj/10000 as sjzmj from ST_SPGL_XMJBXXB a where SJYXBS=1 ";
        if (StringUtil.isNotBlank(xmdm)) {
            sql += "and xmdm = ? ";
            params.add(xmdm);
        }
        if (StringUtil.isNotBlank(xzqhdm)) {
            sql += "and xzqhdm = ? ";
            params.add(xzqhdm);
        }
        return baseDao.find(sql, StSpglXmjbxxb.class, params.toArray());
    }

    // 根据审批流程类型获取该类型项目办件用时
    public String getProjectUseTime(String splclx, String xzqhcode) {
        String sql = "select IFNULL(SUM(XMSPYS),0) from ST_SPGL_XMJBXXB where sjyxbs=1 ";
        if (StringUtil.isNotBlank(splclx)) {
            sql += " and splclx in (" + splclx + ")";
        }
        if (StringUtil.isNotBlank(xzqhcode)) {
            sql += " and xzqhdm='" + xzqhcode + "'";
        }
        return baseDao.queryString(sql);
    }

    public int getCountByDYBZSPJDXH(String DYBZSPJDXH) {
        String sql = "select count(*) from  ST_SPGL_XMJBXXB aa  where 1=1  and SJYXBS = '1' and XMSFWQBJ = '0' ";
        if (StringUtil.isNotBlank(DYBZSPJDXH)) {
            sql += " and XMDM in (select XMDM from ST_SPGL_GCJDXXB  where DYBZSPJDXH = ? )";
        }
        return baseDao.queryInt(sql, DYBZSPJDXH);
    }

}
