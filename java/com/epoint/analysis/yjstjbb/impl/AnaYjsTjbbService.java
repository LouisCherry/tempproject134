package com.epoint.analysis.yjstjbb.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

/**
 * 
 * [一件事统计报表后台service]
 * 
 * @author 28101
 * @version [版本号, 2022年10月24日]
 */
public class AnaYjsTjbbService
{

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AnaYjsTjbbService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 
     * [分页查询申办量与办结量]
     * 
     * @param first
     * @param pageSize
     * @param startDate
     * @param endDate
     * @param applyerway
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getList(int first, int pageSize, String startDate, String endDate, String areacode,
            String applyerway) {
        String areaSql = "";
        if ("3708".equals(areacode)) {
            areaSql = "  and a.areacode like ? ";
            areacode = "3708%";
        }
        else {
            areaSql = "  and a.areacode like ? ";
            areacode = areacode + "%";
        }
        String createString = "";
        String finishString = "";
        if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
            createString = " and b.CREATEDATE between ? and ?";
            finishString = " and b.FINISHDATE between ? and ?";
        }
        
        String applyString;
        if ("00".equals(applyerway)) {
            applyString = "";
        }
        else {
            applyString = " and b.APPLYERWAY = ? ";
        }
        String sql = " select c.XiaQuName , a.areacode , sum(case when b.STATUS > 10 " + createString
                + " then 1 else 0 end) as sb, sum(case when b.STATUS =40 " + finishString
                + " then 1 else 0 end) as bj from audit_sp_instance a,audit_sp_i_subapp b,audit_orga_area c where a.businesstype = '2' and a.RowGuid = b.BIGUID and a.areacode = c.XiaQuCode"
                + areaSql + applyString +" group by a.areacode order by b.CREATEDATE desc ";
        if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
            if ("00".equals(applyerway)) {
                return baseDao.findList(sql, first, pageSize, Record.class, startDate, endDate, startDate, endDate,
                        areacode);
            }
            else {
                return baseDao.findList(sql, first, pageSize, Record.class, startDate, endDate, startDate, endDate,
                        areacode, applyerway);
            }
        }
        else {
            if ("00".equals(applyerway)) {
                return baseDao.findList(sql, first, pageSize, Record.class, areacode);
            }
            else {
                return baseDao.findList(sql, first, pageSize, Record.class, areacode, applyerway);
            }
            
        }

    }

    /**
     * 
     *  [计算数量] 
     *  @param first 开始页
     *  @param pageSize 页面数量
     *  @param startDate 开始时间
     *  @param endDate 结束时间
     *  @param areacode 区域code
     *  @param applyerway 申请方式
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int countList(int first, int pageSize, String startDate, String endDate, String areacode,
            String applyerway) {
        String areaSql = "";
        if ("3708".equals(areacode)) {
            areaSql = "  and a.areacode like ? ";
            areacode = "3708%";
        }
        else {
            areaSql = "  and a.areacode like ? ";
        }
        String applyString;
        if ("00".equals(applyerway)) {
            applyString = "";
        }
        else {
            applyString = " and b.APPLYERWAY = ? ";
        }
        String sql = " select c.XiaQuName  from audit_sp_instance a,audit_sp_i_subapp b,audit_orga_area c where a.businesstype = '2' and a.RowGuid = b.BIGUID and a.areacode = c.XiaQuCode"
                + areaSql + applyString + " group by a.areacode order by b.CREATEDATE desc ";
        String numsqlString = "select count(1) from (" + sql + ") s";

        int  num = 0 ;
        if ("00".equals(applyerway)) {
            num = baseDao.queryInt(numsqlString, areacode);
        }
        else {
            num = baseDao.queryInt(numsqlString, areacode, applyerway);
        }
         
      
        return num;
    }

    /**
     * 
     * [获取区域]
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAreaList() {
        String sql = " select itemtext,itemvalue from code_items where codeid = '1016140'";
        return baseDao.findList(sql, Record.class);
    }
}
