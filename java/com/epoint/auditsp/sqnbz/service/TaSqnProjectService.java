package com.epoint.auditsp.sqnbz.service;

import java.util.ArrayList;
import java.util.List;

import com.epoint.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class TaSqnProjectService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public TaSqnProjectService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 
     * @Description: 查询是否有项目推送 @author male @date 2020年8月4日 下午3:02:21 @return
     *               int 返回类型 @throws
     */
    public int isExsitXmdm(String itemcode) {
        String sql = "select count(1) from spgl_xmjbxxb where gcdm = ? and  SJYXBS = 1";
        return baseDao.queryInt(sql, itemcode);
    }

    /**
     * 
     * @Description: 获取关联工程的guid信息 @author male @date 2020年8月4日
     *               下午6:47:30 @return Record 返回类型 @throws
     */
    public Record getContectInfo(String itemcode) {
        String sql = "select a.RowGuid baseguid,c.RowGuid insguid,b.RowGuid subguid,a.ITEMCODE,a.ITEMNAME,b.businessguid,b.phaseguid  from audit_rs_item_baseinfo a inner join audit_sp_i_subapp b on a.RowGuid = b.YEWUGUID "
                + "inner join audit_sp_instance c on b.BIGUID = c.RowGuid inner join audit_sp_phase d on b.PHASEGUID = d.RowGuid "
                + " where ITEMCODE  like ? and d.PHASEID in ('2','3','4') ORDER BY b.CREATEDATE desc limit 1 ";
        return baseDao.find(sql, Record.class, itemcode + "%");
    }

    public Record getCjdaContectInfo(String itemcode) {
        String sql = "select a.RowGuid baseguid,c.RowGuid insguid,b.RowGuid subguid,a.ITEMCODE,a.ITEMNAME,b.businessguid,b.phaseguid  from audit_rs_item_baseinfo a inner join audit_sp_i_subapp b on a.RowGuid = b.YEWUGUID "
                + "inner join audit_sp_instance c on b.BIGUID = c.RowGuid inner join audit_sp_phase d on b.PHASEGUID = d.RowGuid "
                + " where ITEMCODE  like ?  ORDER BY b.CREATEDATE desc limit 1 ";
        return baseDao.find(sql, Record.class, itemcode + "%");
    }

    /**
     * 
     * @Description: 是否存在项目审批事项办理信息表信息 @author male @date 2020年8月5日
     *               上午9:17:30 @return int 返回类型 @throws
     */
    public int isExsitXmspsxblxxb(String spsxblbm) {
        String sql = "select count(1) from spgl_xmspsxblxxb where SPSXSLBM = ? and SJYXBS = 1 ";
        return baseDao.queryInt(sql, spsxblbm);
    }

    public ParticipantsInfo getlegalByCreditcode(String creditcode) {
        String sql = "select * from Participants_Info where corpcode =? and IFNULL(LEGAL,'') != '' and ifnull(legalpersonicardnum,'') != '' limit 0,1";
        return baseDao.find(sql, ParticipantsInfo.class, creditcode);
    }

    public Record getHtxxByGcdm(String gcdm) {
        String sql = "select * from ggzydjinfo where gcdm = ?";
        return baseDao.find(sql, Record.class, gcdm);
    }

    public void addHtxx(Record record) {
        baseDao.insert(record);
    }

    public void updateHtxx(Record record) {
        baseDao.update(record);
    }

    public List<AuditRsItemBaseinfo> getItemCodeBycompanynameAnditemcode(String gcdm, String companyname) {
        String sql = "select rowguid,itemcode,itemname from audit_rs_item_baseinfo where ifnull(parentid, '') = '' and"
                + " itemcode like '%" + gcdm + "%' and ITEMLEGALDEPT like '%" + companyname + "%'";
        return baseDao.findList(sql, AuditRsItemBaseinfo.class);
    }

    public AuditRsItemBaseinfo getitemDetailByItemcode(String gcdm) {
        String sql = "select * from audit_rs_item_baseinfo where itemcode = ?";
        return baseDao.find(sql, AuditRsItemBaseinfo.class, gcdm);
    }

    public List<AuditSpSpGcjsxk> getAuditSpSpGcjsxkByItemcode(String gcdm) {
        String sql = "select * from audit_sp_sp_gcjsxk where itemcode like '" + gcdm +"%'";
        return baseDao.findList(sql, AuditSpSpGcjsxk.class);
    }

    public List<String> getTaskIdList(String basetaskguid) {
        String sql = "select taskid from audit_sp_basetask_r where basetaskguid = ?";
        List<AuditSpBasetaskR> auditSpBasetaskRList = null;
        auditSpBasetaskRList = baseDao.findList(sql, AuditSpBasetaskR.class, basetaskguid);
        List<String> list = new ArrayList<>();
        for (AuditSpBasetaskR auditSpBasetaskR : auditSpBasetaskRList) {
            list.add(auditSpBasetaskR.getTaskid());
        }
        return list;
    }


}
