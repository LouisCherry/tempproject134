package com.epoint.auditsp.sqnbz.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.core.grammar.Record;

public interface ITaSqnProject extends Serializable
{
    /**
     * 
     * @Description: 查询是否有项目推送 @author male @date 2020年8月4日 下午3:02:21 @return
     * int 返回类型 @throws
     */
    public int isExsitXmdm(String itemcode);

    /**
     * 
     * @Description: 获取关联工程的guid信息 @author male @date 2020年8月4日
     * 下午6:47:30 @return Record 返回类型 @throws
     */
    public Record getContectInfo(String itemcode);

    Record getCjdaContectInfo(String itemcode);

    /**
     * 
     * @Description: 是否存在项目审批事项办理信息表信息 @author male @date 2020年8月5日
     * 上午9:17:30 @return int 返回类型 @throws
     */
    public int isExsitXmspsxblxxb(String spsxblbm);

    /**
     * 根据统一社会信用代码获取单位
     * 
     * @param creditcode
     * @return
     */
    public ParticipantsInfo getlegalByCreditcode(String creditcode);

    /**
     * 根据工程代码获取合同信息
     * 
     * @param gcdm
     * @return
     */
    public Record getHtxxByGcdm(String gcdm);

    /**
     * 新增合同信息
     * 
     * @param record
     */
    public void addHtxx(Record record);

    /**
     * 更新合同信息
     * 
     * @param record
     */
    public void updateHtxx(Record record);

    /**
     * 根据单位名称和项目代码查找完整的项目代码
     * 
     * @param gcdm
     * @param companyname
     * @return
     */
    public List<AuditRsItemBaseinfo> getItemCodeBycompanynameAnditemcode(String gcdm, String companyname);

    /**
     * 根据项目代码获取项目详情
     * 
     * @param gcdm
     * @return
     */
    public AuditRsItemBaseinfo getitemDetailByItemcode(String gcdm);

    /**
     * 根据项目代码获取项目详情
     * 
     * @param gcdm
     * @return
     */
    public List<AuditSpSpGcjsxk> getAuditSpSpGcjsxkByItemcode(String gcdm);
    /*
     * 获取招标备案的taskid的lsit
     */
    public List<String> getTaskIdList(String basetaskguid);
}
