package com.epoint.auditresource.auditrsitembaseinfo.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 项目办件信息
 */
@RestController("jnauditxiangmumaterialdetailaction")
@Scope("request")
public class JNAuditXiangmuMaterialDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 共享材料配置表实体对象
     */
    private AuditSpIMaterial dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpIMaterial> model;

    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterialImpl;
    /**
     * 项目guid
     */
    private String xiangmuguid = "";
    /**
     * 项目库接口
     */
    @Autowired
    private IAuditRsItemBaseinfo auditRsItemBaseinfoImpl;
    @Autowired
    private IAuditSpBusiness auditSpBusinessImpl;
    @Autowired
    private IAuditSpISubapp auditSpISubappImpl;

    private AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
    /**
     * 附件service
     */
    @Autowired
    private IAttachService attachService;
    @Autowired
    private IAuditSpPhase spPhaseService;

    private String materialname;
    
    @Override
    public void pageLoad() {
        xiangmuguid = getRequestParameter("guid");
        auditRsItemBaseinfo = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByRowguid(xiangmuguid).getResult();
    }

    public DataGridModel<AuditSpIMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpIMaterial>()
            {

                @Override
                public List<AuditSpIMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("BIGUID", auditRsItemBaseinfo.getBiguid());
                    sql.isBlank("flag");
                    if(StringUtil.isNotBlank(materialname)){                        
                        sql.like("materialname", materialname);
                    }
                    PageData<AuditSpIMaterial> pageData = iAuditSpIMaterialImpl.selectAuditSpIMaterialPageData(
                            AuditSpIMaterial.class, sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    for (AuditSpIMaterial rsmateriali : pageData.getList()) {
                        if (StringUtil.isNotBlank(rsmateriali.getCliengguid())) {
                            List<FrameAttachInfo> frameAttachInfos = attachService
                                    .getAttachInfoListByGuid(rsmateriali.getCliengguid());
                            if (frameAttachInfos.size() > 0) {
                                String attachname = frameAttachInfos.get(0).getAttachFileName();
                                rsmateriali.put("attachname", attachname);
                                rsmateriali.put("attachguid", frameAttachInfos.get(0).getAttachGuid());
                            }
                        }
                        else {
                            rsmateriali.put("attachname", "");
                        }
                        if (StringUtil.isNotBlank(rsmateriali.getBusinessguid())) {
                            AuditSpBusiness auditSpBusiness = auditSpBusinessImpl
                                    .getAuditSpBusinessByRowguid(rsmateriali.getBusinessguid()).getResult();
                            if(auditSpBusiness!=null && StringUtils.isNotBlank(auditSpBusiness.getBusinessname())){
                                rsmateriali.put("BUSINESSNAME", auditSpBusiness.getBusinessname());
                            }
                        }
                        else {
                            rsmateriali.put("BUSINESSNAME", "");
                        }

                        if (StringUtil.isNotBlank(rsmateriali.getBusinessguid())) {
                            AuditSpISubapp auditSpISubapp = auditSpISubappImpl
                                    .getSubappByGuid(rsmateriali.getSubappguid()).getResult();
                            if(auditSpISubapp!=null){
                                rsmateriali.put("subappname", auditSpISubapp.getSubappname());
                            }
                        }
                        else {
                            rsmateriali.put("subappname", "");
                        }
                        if (StringUtil.isNotBlank(rsmateriali.getPhaseguid())) {
                            AuditSpPhase auditSpPhase = spPhaseService
                                    .getAuditSpPhaseByRowguid(rsmateriali.getPhaseguid()).getResult();
                            String phasename = (auditSpPhase==null)?"":auditSpPhase.getPhasename();
                            rsmateriali.put("phasename", phasename);
                        }
                        else {
                            rsmateriali.put("phasename", "");
                        }
                    }
                    return pageData.getList();

                }

            };
        }
        return model;
    }

    public AuditSpIMaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpIMaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpIMaterial dataBean) {
        this.dataBean = dataBean;
    }
    
    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

}
