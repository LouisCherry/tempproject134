package com.epoint.auditsp.auditsphandle.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.ibatis.SqlMapClientImplWrapper;
import com.epoint.auditsp.auditsphandle.api.IJNQiYeMa;
import com.epoint.basic.auditresource.auditdggtview.domain.AuditDggtView;
import com.epoint.basic.auditresource.auditdggtview.inter.IAuditDggtView;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

import cn.hutool.core.lang.UUID;

/**
 * 企业码对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@RestController("jnqiyemaaction")
@Scope("request")
public class JNQiYeMaAction extends BaseController {

	private  DataGridModel<FrameAttachInfo>  model = null;
	
	@Autowired
	private  IJNQiYeMa service;
	
	@Autowired
	private IAuditSpIMaterial iAuditSpIMaterial;
	
	@Autowired
	private IAttachService attachService;
	
	private String attachguid;
	
	private String materialguid;
	
    private List<SelectItem> materialModel;

    @Override
	public void pageLoad() {
		if(!"关联材料".equals(getRequestParameter("materialname"))) {
		    materialguid = getRequestParameter("materialname");
		}
		attachguid = getRequestParameter("attachguid");
	}

    //关联材料
    public void save() {
        String guid = getRequestParameter("guid");
        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(getRequestParameter("subappGuid"), materialguid).getResult();
        if(null != auditSpIMaterial) {
            //查询出审批材料的cliengguid
            String cliengguid = auditSpIMaterial.getCliengguid();
            if(StringUtil.isNotBlank(cliengguid)) {
                //复制info
                FrameAttachInfo attachInfo = attachService.getAttachInfoDetail(guid);
                attachInfo.setAttachGuid(UUID.randomUUID().toString());
                attachInfo.setCliengGuid(cliengguid);
                FrameAttachStorage attachStorage = attachService.getAttach(guid);
                attachService.addAttach(attachInfo, attachStorage.getContent());
                auditSpIMaterial.setStatus("20");
                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                
                addCallbackParam("msg", "保存成功!");
                addCallbackParam("materialname", auditSpIMaterial.getMaterialname());
                addCallbackParam("materialguid", auditSpIMaterial.getMaterialguid());
            }
        }
        else {
            addCallbackParam("msg", "保存失败!");
        }
    }
    
	public DataGridModel<FrameAttachInfo> getDataGridData(){
	    if(model == null){
	        model = new DataGridModel<FrameAttachInfo>(){
                private static final long serialVersionUID = 1L;

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    int count = 0;
                    String attachguids = ";";
                    if(StringUtil.isNotBlank(attachguid)){                        
                        attachguids = StringUtil.join(attachguid.split(";"), "', '");
                        count = attachguid.split(";").length;
                    }


                    List<FrameAttachInfo> list = service.findListByGuids(attachguids, first, pageSize, sortField, sortOrder);
                    this.setRowCount(count);
                    return list;
                }
	            
	        };
	    }
	    
	    return model;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getMaterialModel(){
	    if (materialModel == null) {
	        materialModel = new ArrayList<SelectItem>();
	        List<AuditSpIMaterial> list = iAuditSpIMaterial.getAllSpIMaterialBySubappGuid(getRequestParameter("subappGuid")).getResult();
	        SelectItem item = null;
	        for (AuditSpIMaterial auditSpIMaterial : list) {
	            item = new SelectItem();
	            item.setText(auditSpIMaterial.getMaterialname());
	            item.setValue(auditSpIMaterial.getMaterialguid());
	            materialModel.add(item);
	        }
        }
        return materialModel;
	}
	
	public String getMaterialguid() {
        return materialguid;
    }

    public void setMaterialguid(String materialguid) {
        this.materialguid = materialguid;
    }

    public void setMaterialModel(List<SelectItem> materialModel) {
        this.materialModel = materialModel;
    }
	
	public String getAttachguid() {
	    return attachguid;
	}

	public void setAttachguid(String attachguid) {
	   this.attachguid = attachguid;
	}
}
