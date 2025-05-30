package com.epoint.xmz.participatsinfo.api;

import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.io.Serializable;

/**
 * 参建单位信息表对应的后台service接口
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
public interface GxhIParticipantsInfoService extends Serializable {

    /**
     * 查询事项单位列表
     *
     * @param first
     * @param pagesize
     * @param taskguid
     * @param subappguid
     * @param corpname
     * @param corpcode
     * @param legal
     * @param keyword
     * @return
     */
    public PageData<ParticipantsInfo> getListByTaskguidAndSubappguid(int first, int pagesize, String taskguid,
                                                                     String subappguid, String corpname, String corpcode, String legal, String keyword);
}
