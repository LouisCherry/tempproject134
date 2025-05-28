package com.epoint.sghd.auditjianguan.rlconfig.api;

import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IRlconfigSendmsg extends Serializable {

    public int insert(RlconfigSendmsg rlconfigsendmsg);

    public int deleteByGuid(String guid);

    public int update(RlconfigSendmsg rlconfigsendmsg);

    public RlconfigSendmsg find(Object primaryKey);

    public PageData<RlconfigSendmsg> getListByPage(Map<String, String> conditionMap, int first,
                                                   int pageSize, String sortField, String sortOrder);

    public List<RlconfigSendmsg> findtosendmsg();

    /**
     * 部门获取已认领数量
     *
     * @return
     */
    public int getTaJianGuanTabYrlCount(String areaCode, String ouguid);

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    public int getTaJianGuanTabWrlCount(String areaCode, String ouguid);

    public int updateSendinfo(String rowguid, String sendmsg);
}
