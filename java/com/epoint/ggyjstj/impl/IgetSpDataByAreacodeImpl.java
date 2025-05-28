package com.epoint.ggyjstj.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.ggyjstj.api.IgetSpDataByAreacode;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Service
@Component
public class IgetSpDataByAreacodeImpl implements IgetSpDataByAreacode {
    private Logger log = LogUtil.getLog(IgetSpDataByAreacodeImpl.class);

    @Override
    public Record getSpDataByAreacode(String areacode){
        CommonDao dao = new CommonDao();
        String  sql="select\n" +
                "    b.XiaQuName , -- 辖区名称\n" +
                "    c.areacode , -- 辖区名称\n" +
                "    sum(case when a.STATUS > 10 then 1 else 0 end) as sb, -- 申报量\n" +
                "    sum(case when a.STATUS = 40 then 1 else 0 end) as bj -- 办结量\n" +
                "from\n" +
                "    audit_sp_i_subapp a,\n" +
                "    audit_orga_area b,\n" +
                "    audit_sp_business c\n" +
                "where\n" +
                "    c.areacode = '"+areacode+"' "+
                "    and c.isggyjs = '1'\n" +
                "    and a.yjsbusinessguid = c.RowGuid\n" +
                "    and c.areacode = b.XiaQuCode\n" +
                "group by\n" +
                "    c.areacode\n" +
                "order by\n" +
                "    c.areacode";
      return  dao.find(sql, Record.class);


    }

    @Override
    public PageData<AuditSpInstance> getyjsDataByAreacodeAndType(String areacode, String type,String applyername,String itemname,String status, Date beginTime,Date endTime,Integer first,Integer pagesize) {
        CommonDao dao = new CommonDao();

        String sql="select\n" +
                "   a.rowguid,a.biguid,a.businessguid,a.yjsname,\n" +
                "    a.applyername, \n" +
                "    a.CREATEDATE, \n" +
                "    a.FINISHDATE,\n" +
                "    a.STATUS \n" +
                "from\n" +
                "    audit_sp_i_subapp a\n" +
                "left join audit_sp_business b on\n" +
                "    a.yjsbusinessguid = b.rowguid\n" +
                "where\n" +
                "    b.isggyjs = '1'\n" +
                "    and b.areacode =  '"+areacode+"' ";
        //type=1,申报量。type=2,办结量
        if (StringUtil.isBlank(type) || "1".equals(type)){
            sql+=" and a.status >= 10 ";
        }
        else {
            sql+=" and a.status = 40 ";
        }
        if (StringUtil.isNotBlank(applyername)){
            sql+=" and a.applyername like '%"+applyername+"%' ";
        }
        if (StringUtil.isNotBlank(itemname)){
            sql+=" and a.yjsname like '%"+itemname+"%' ";
        }
        if (StringUtil.isNotBlank(status)){
            sql+=" and b.status =  "+status +" ";
        }
        if (beginTime!=null && endTime !=null){
           sql+=" and b.createdate >= '"+ EpointDateUtil.convertDate2String(beginTime) +"' " +
                   "            and b.createdate <= '"+ EpointDateUtil.convertDate2String(endTime) +"' ";
        }
        sql+=" order by createdate desc";

        PageData<AuditSpInstance> pageData=new PageData<>();
        pageData.setList(dao.findList(sql,first,pagesize, AuditSpInstance.class));
        String sqlcount="select\n" +
                "  count(1) " +
                "from\n" +
                "    audit_sp_i_subapp a\n" +
                "left join audit_sp_business b on\n" +
                "    a.yjsbusinessguid = b.rowguid\n" +
                "where\n" +
                "    b.isggyjs = '1'\n" +
                "    and b.areacode =  '"+areacode+"' ";
        //type=1,申报量。type=2,办结量
        if (StringUtil.isBlank(type) || "1".equals(type)){
            sqlcount+=" and a.status > 10 ";
        }
        else {
            sqlcount+=" and a.status = 40 ";
        }
        if (StringUtil.isNotBlank(applyername)){
            sqlcount+=" and a.applyername like '%"+applyername+"%' ";
        }
        if (StringUtil.isNotBlank(itemname)){
            sqlcount+=" and a.yjsname like '%"+itemname+"%' ";
        }
        if (StringUtil.isNotBlank(status)){
            sqlcount+=" and b.status =  "+status +" ";
        }
        if (beginTime!=null && endTime !=null){
            sqlcount+=" and b.createdate >= '"+ EpointDateUtil.convertDate2String(beginTime) +"' " +
                    "            and b.createdate <= '"+ EpointDateUtil.convertDate2String(endTime) +"' ";
        }
        Integer i = dao.queryInt(sqlcount);
        pageData.setRowCount(i);
        return  pageData;


    }

}
