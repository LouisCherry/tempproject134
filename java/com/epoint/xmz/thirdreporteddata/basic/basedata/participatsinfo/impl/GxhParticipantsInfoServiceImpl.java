package com.epoint.xmz.thirdreporteddata.basic.basedata.participatsinfo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.basedata.participatsinfo.api.GxhIParticipantsInfoService;
import org.springframework.stereotype.Component;

/**
 * 参建单位信息表对应的后台service实现类
 * 
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
@Component
@Service
public class GxhParticipantsInfoServiceImpl implements GxhIParticipantsInfoService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
    @Override
    public PageData<ParticipantsInfo> getListByTaskguidAndSubappguid(int first, int pagesize, String taskguid,
            String subappguid, String corpname, String corpcode, String legal, String keyword) {
        return new GxhParticipantsInfoService().getListByTaskguidAndSubappguid(first, pagesize, taskguid, subappguid,
                corpname, corpcode, legal, keyword);
    }
}
