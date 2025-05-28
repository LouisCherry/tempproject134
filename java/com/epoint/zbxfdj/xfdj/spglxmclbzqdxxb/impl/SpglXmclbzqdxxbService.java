package com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.impl;


import com.epoint.basic.spgl.domain.SpglXmjbxxb;
import com.epoint.core.dao.ICommonDao;
import com.epoint.zbxfdj.util.JDBCUtil;
import com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.api.entity.SpglXmclbzqdxxb;

import java.util.Date;
import java.util.List;

/**
 * 项目审批过程信息表对应的后台service
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
public class SpglXmclbzqdxxbService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao highgoDao;

    public SpglXmclbzqdxxbService() {
        highgoDao = JDBCUtil.getHighGoJDBC();

    }

    public int insert(SpglXmjbxxb record) {
        String sql = "insert into SPGL_XMJBXXB(lsh, gcmc, xmdm, sjsczt, sjyxbs, xzqhdm, xmmc, sbsj, gcdm, splclx, id, sjgxsj, dfsjzj, forms_json) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return highgoDao.execute(sql, record.getStr("lsh"), record.getStr("gcmc"), record.getXmdm(), record.getSjsczt(), record.getSjyxbs(), record.getXzqhdm(), record.getXmmc(), record.getSbsj(), record.getGcdm(), record.getSplclx(), record.getStr("ID"),
                record.getDate("sjgxsj"), record.getDfsjzj(), record.getStr("forms_json"));
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public void insert(String lsh, String xzqhdm, String id, int sjsczt, String sjyxbs, Date sjgxsj, String forms_json) {
        String sql = "insert into SPGL_XMCLBZXXB (lsh, xzqhdm, sjsczt, id, sjyxbs, sjgxsj, forms_json) values(?, ?, ?, ?, ?, ?, ?)";
        highgoDao.execute(sql, lsh, xzqhdm, sjsczt, id, sjyxbs, sjgxsj, forms_json);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateSjsczt(String status, String id) {
        String sql = "update 370800_SPGL_XMCLBZQDXXB set Sjsczt=" + status + " where id=?";
        return highgoDao.execute(sql, id);
    }

    /**
     * 查找所有SJSCZT=? list
     *
     * @param sjsczt
     * @return
     */
    public List<SpglXmclbzqdxxb> findListByLsh(String LSH, Integer sjsczt) {
        return highgoDao.findList(
                "select * from 370800_SPGL_XMCLBZQDXXB where LSH=? and SJSCZT=?",
                SpglXmclbzqdxxb.class, LSH, sjsczt);
    }
}
