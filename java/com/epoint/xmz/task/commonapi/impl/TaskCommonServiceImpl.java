package com.epoint.xmz.task.commonapi.impl;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.xmz.task.commonapi.inter.ITaskCommonService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TaskCommonServiceImpl implements ITaskCommonService {

    @Override
    public AuditCommonResult<String> getCropInfo(String taskguid, String subappguid, String pahseguid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            ICommonDao dao = CommonDao.getInstance();
            String sql = "select asp.basetaskguid from audit_sp_task asp \r\n"
                    + "left join audit_sp_basetask_r asbr on asp.basetaskguid = asbr.basetaskguid \r\n"
                    + "left join audit_task at on at.task_id = asbr.taskid\r\n"
                    + "left join audit_sp_i_task asit on asit.taskguid = at.rowguid\r\n"
                    + "where 1=1 and at.rowguid = ? and asit.subappguid = ? and asp.phaseguid = ?  and at.IS_ENABLE='1' and at.is_history ='0' and at.is_editafterimport = 1";

            // dao.execute(sql);
            result.setResult(dao.find(sql, String.class, taskguid, subappguid, pahseguid));
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getCropName(String baseTaskguid) {
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            ICommonDao dao = CommonDao.getInstance();
            String sql = "select corptype from audit_sp_basetask where rowguid = ?";
            // dao.execute(sql);
            result.setResult(dao.find(sql, String.class, baseTaskguid));
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpBasetask>> getAuditSpTask(String businessguid, String task_id) {
        AuditCommonResult<List<AuditSpBasetask>> result = new AuditCommonResult<>();
        try {
            ICommonDao dao = CommonDao.getInstance();
            String sql = "select * from audit_sp_basetask asb inner join audit_sp_basetask_r asbr on asbr.basetaskguid = asb.RowGuid inner join audit_sp_task ast on ast.basetaskguid  = asb.RowGuid and ast.BUSINESSGUID = ? and asbr.taskid =?";
            result.setResult(dao.findList(sql, AuditSpBasetask.class, businessguid, task_id));
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getYwsjCommonByCondition(Class<? extends BaseEntity> class1, String itemcode,
                                                               String flowsn) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            ICommonDao dao = CommonDao.getInstance();
            String sql = "";
            if (("spgl_sgtsjwjscxxb_v3").equals(class1.getAnnotation(Entity.class).table())) {
                sql = "select count(1) from spgl_sgtsjwjscxxb_v3 where gcdm = ?";
            } else {
                sql = "select count(1) from " + class1.getAnnotation(Entity.class).table() + " where gcdm = ?  and spsxslbm=?";
            }

            result.setResult(dao.queryInt(sql, itemcode, flowsn));
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public AuditCommonResult<List<ParticipantsInfo>> getJsdwInfor(String gcdm) {
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IParticipantsInfoService iParticipantsInfoService = ContainerFactory.getContainInfo()
                .getComponent(IParticipantsInfoService.class);
        AuditRsItemBaseinfo result = iauditrsitembaseinfo.getAuditRsItemBaseinfoByItemcode(gcdm).getResult();
        AuditCommonResult<List<ParticipantsInfo>> resultList = new AuditCommonResult<>();
        String itemguid = "";
        if (result != null) {
            // 获取建设单位信息
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("rowguid", result.getParentid());
            List<AuditRsItemBaseinfo> result2 = iauditrsitembaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
            if (result2 != null && !result2.isEmpty()) {
                itemguid = result2.get(0).getRowguid();
            } else {
                itemguid = result.getRowguid();
            }
            sqlConditionUtil.clear();
            sqlConditionUtil.eq("itemguid", itemguid);
            sqlConditionUtil.eq("corptype", "31");
            List<ParticipantsInfo> list = iParticipantsInfoService.getParticipantsInfoListByCondition(sqlConditionUtil.getMap());
            if (list != null && !list.isEmpty()) {
                for (ParticipantsInfo participantsInfo : list) {
                    // 设置项目信息
                    participantsInfo.set("GCMC", result.getItemname());
                    participantsInfo.set("JSDZ", result.getConstructionsite());
                    participantsInfo.set("SSQX", result.get("areacode"));
                    participantsInfo.set("XMJWDZB", result.get("XMJWDZB"));
                    participantsInfo.set("JSGM", result.getConstructionscaleanddesc());
                    participantsInfo.set("JHKGRQ", result.getItemstartdate());
                    participantsInfo.set("JHJGRQ", result.getItemfinishdate());
                    participantsInfo.set("GCHYFL", result.get("GCHYFL"));
                    participantsInfo.set("Xmdm", result.getItemcode());
                    participantsInfo.set("JSXZ", result.getConstructionproperty());
                }
                resultList.setResult(list);
            }
        }
        return resultList;
    }

}
