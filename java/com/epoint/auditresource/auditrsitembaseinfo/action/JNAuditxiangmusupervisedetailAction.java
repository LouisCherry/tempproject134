package com.epoint.auditresource.auditrsitembaseinfo.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.auditresource.auditrsitembaseinfo.api.IJNauditRsItemBaseinfoservice;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;

/**
 * 项目办件信息
 */
@RestController("jnauditxiangmusupervisedetailaction")
@Scope("request")
public class JNAuditxiangmusupervisedetailAction extends BaseController
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
    private IJNauditRsItemBaseinfoservice jnauditRsItemBaseinfoservice;

    
    @Override
    public void pageLoad() {
        xiangmuguid = getRequestParameter("guid");
        auditRsItemBaseinfo = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByRowguid(xiangmuguid).getResult();
        addCallbackParam("xiangmuguid",xiangmuguid);

    }
    
    
	public void init(){
          addCallbackParam("phasenamelist",phaselist());
    
    }
    public List<AuditSpPhase> phaselist(){
    	List<AuditSpPhase> list=null;
    	if(StringUtil.isNotBlank(auditRsItemBaseinfo.getBiguid())){
    	 list=jnauditRsItemBaseinfoservice.getphaseNamebybusinessguid(auditRsItemBaseinfo.getBiguid());
    	}
    	return list;
    }
    public void taskname(String phaseguid){
  	  String bigguid=auditRsItemBaseinfo.getBiguid();
  	  if(StringUtil.isNotBlank(bigguid)){
  		List<AuditSpITask> tasklist=jnauditRsItemBaseinfoservice.gettaskbybigguid(phaseguid, bigguid);
  		addCallbackParam("taskname",tasklist);

  	  }else{
          addCallbackParam("taskname",null);

  	  }
    	
 }
    
    public void tasktotal(String phaseguid){
    	  String bigguid=auditRsItemBaseinfo.getBiguid();
  	  if(StringUtil.isNotBlank(bigguid)){
    		int total=jnauditRsItemBaseinfoservice.gettaskbytotal(phaseguid, bigguid);
    		addCallbackParam("total",total);

    	  }
	}
}
