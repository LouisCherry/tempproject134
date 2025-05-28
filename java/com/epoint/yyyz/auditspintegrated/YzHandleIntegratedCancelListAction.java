package com.epoint.yyyz.auditspintegrated;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 申报查询列表页面对应的后台
 * @author Administrator
 *
 */
@RestController("yzhandleintegratedcancellistaction")
@Scope("request")
public class YzHandleIntegratedCancelListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;
    private AuditSpBusiness dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpInstance> model;
    /**
     * 建设单位
     */
    private String applyername;
    /**
     * 申报名称
     */
    private String itemname;
    @Autowired
    private IAuditSpInstance spInstanceService;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 系统参数接口
     */
    @Autowired
    private IConfigService configService;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    @Override
    public void pageLoad() {

    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> guids = getDataGridData().getSelectKeys();
        for (String guid : guids) {
            spInstanceService.deleteAuditSpInstanceByGuid(guid);
        }
        addCallbackParam("msg", "删除成功！");
    }

    /**
     * 
     *  检测是否所有的办件是否已办结
     * 
     *  @param biGuid   
     */
    public void checkAllProjectStatus(String biGuid) {
        int count = 0;
        SqlConditionUtil conditionsql = new SqlConditionUtil();
        conditionsql.eq("biguid", biGuid);
        conditionsql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
        List<AuditProject> auditProjects = iAuditProject.getAuditProjectListByCondition(conditionsql.getMap())
                .getResult();
        if (auditProjects != null && auditProjects.size() > 0) {
            for (AuditProject auditProject : auditProjects) {
                if (auditProject.getStatus() < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                    count++;
                }
            }
        }
        addCallbackParam("msg", count);
    }

    /**
     * 
     *  套餐办结
     *  
     *  @param biGuid  
     */
    public void cancelIntegrate(String biGuid) {
        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
            for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                iAuditSpISubapp.updateSubapp(auditSpISubapp.getRowguid(), ZwfwConstant.LHSP_Status_YBJ, new Date());
                // 同时更新外网onlineproject，正常办结
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);//要更新的时间类型字段
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                updateFieldMap.put("is_evaluat=", String.valueOf(ZwfwConstant.CONSTANT_STR_ZERO));
                updateDateFieldMap.put("banjiedate=",
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                conditionsql.eq("sourceguid", auditSpISubapp.getBiguid());
                conditionsql.eq("taskguid", auditSpISubapp.getBusinessguid());
                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionsql.getMap());  
            }
        }
    }

    public DataGridModel<AuditSpInstance> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpInstance>()
            {
                @Override
                public List<AuditSpInstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                        sql.like("b.applyername", applyername);
                    }
                    if (StringUtil.isNotBlank(itemname)) {
                        sql.like("itemname", itemname);
                    }
                    sql.eq("businesstype", "3");
                    sql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode()+"000000");
                    sql.in("b.status", ZwfwConstant.LHSP_Status_YSJ + "," + ZwfwConstant.LHSP_Status_DBJ);
                    sortField = "createdate";
                    sortOrder = "desc";
                    PageData<AuditSpInstance> pageData = spInstanceService
                            .getAuditSpInstanceYSByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }
    
    public void getCertcatalogid(){
        IHandleConfig handleConfigService = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        String catalogid = handleConfigService.getFrameConfig("AS_CENTER_CERT", centerGuid).getResult();
    	addCallbackParam("catalogid", catalogid);
    }

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
