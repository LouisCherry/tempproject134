package com.epoint.cert.auditcertrelation.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 证照字段关系表修改页面对应的后台
 *
 * @author miemieyang12128
 * @version [版本号, 2024-10-16 09:08:40]
 */
@RightRelation(AuditCertRelationListAction.class)
@RestController("auditcertrelationeditaction")
@Scope("request")
public class AuditCertRelationEditAction extends BaseController {

    @Autowired
    private IAuditCertRelationService service;

	@Autowired
	private ICodeItemsService codeItemsService;

	@Autowired
	private IAuditSpPhase iAuditSpPhase;

	@Autowired
	private ICertCatalog iCertCatalog;

	@Autowired
	private ICertConfigExternal certConfigExternalImpl;

    /**
     * 证照字段关系表实体对象
     */
    private AuditCertRelation dataBean = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditCertRelation();
        }
		if(!isPostback()){
			addViewData("oldrelationtype", dataBean.getRelationtype());
			addViewData("oldcertid", dataBean.getCertid());
			addViewData("oldformid", dataBean.getFormid());
			addViewData("oldphaseguid", dataBean.getPhaseguid());
		}
		addCallbackParam("certname", dataBean.getCertname());
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
		if (StringUtil.isBlank(dataBean.getCertid())||"f9root".equals(dataBean.getCertid())) {
			addCallbackParam("msg", "请选择证照名称！");
			return;
		}
		CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(dataBean.getCertid());
		if(certCatalog == null){
			addCallbackParam("msg", l("保存失败 证照编号:"+dataBean.getCertid()+"对应的证照目录不存在！"));
			return;
		}
		dataBean.setCertname(certCatalog.getCertname());
		dataBean.setCertid(certCatalog.getCertcatalogid());
		dataBean.setCertno(certCatalog.getCertcatcode());
//		addViewData("oldrelationtype", dataBean.getRelationtype());
		String oldrelationtype = getViewData("oldrelationtype");
//		addViewData("oldcertid", dataBean.getCertid());
		String oldcertid = getViewData("oldcertid");
//		addViewData("oldformid", dataBean.getFormid());
		String oldformid = getViewData("oldformid");
//		addViewData("oldphaseguid", dataBean.getPhaseguid());
		String oldphaseguid = getViewData("oldphaseguid");
		if(StringUtil.isNotBlank(oldrelationtype)&&!oldrelationtype.equals(dataBean.getRelationtype())){
			dataBean.setRelationjson("");
		}
		if(StringUtil.isNotBlank(oldcertid)&&!oldcertid.equals(dataBean.getCertid())){
			dataBean.setRelationjson("");
		}
		if(StringUtil.isNotBlank(oldformid)&&!oldformid.equals(dataBean.getFormid())){
			dataBean.setRelationjson("");
		}
		if(StringUtil.isNotBlank(oldphaseguid)&&!oldphaseguid.equals(dataBean.getPhaseguid())){
			dataBean.setRelationjson("");
		}
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

	/**
	 * 关联类型
	 *
	 * @return
	 */
	public List<SelectItem> getRelationTypeModel() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("关系类型");
		for (CodeItems codeItem : codeItems) {
			result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
		}
		return result;
	}

	/**
	 * 阶段
	 * @return
	 */
	public List<SelectItem> getPhaseModel(){
//		List<SelectItem> result = new ArrayList<SelectItem>();
//		SqlConditionUtil sql = new SqlConditionUtil();
//		//sql.eq("businedssguid",auditSpBusiness.getRowguid());
//		sql.setOrder("ordernumber", "desc");
//		List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
//		if (EpointCollectionUtils.isNotEmpty(auditSpPhases))
//			for (AuditSpPhase auditSpPhase : auditSpPhases) {
//				result.add(new SelectItem(auditSpPhase.getRowguid(), auditSpPhase.getPhasename()));
//			}
//		return result;
		List<SelectItem> result = new ArrayList<SelectItem>();
		List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("审批阶段");
		for (CodeItems codeItem : codeItems) {
			if("5".equals(codeItem.getItemValue())||"6".equals(codeItem.getItemValue())){
				continue;
			}
			result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
		}
		return result;
	}

	//证照
	public List<SelectItem> getCertModel() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		String areacode="";
		// 证照和批文要按区域划分
		if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
			areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
		}else{
			areacode = ZwfwUserSession.getInstance().getAreaCode();
		}
		List<CertCatalog> list = certConfigExternalImpl.selectCatalogByAreaCode(areacode,"",ZwfwConstant.Material_ZZ,"" , false);
		for (CertCatalog certCatalog : list) {
			if(StringUtil.isBlank(certCatalog.getCertcatalogid())){
				continue;
			}
			result.add(new SelectItem(certCatalog.getCertcatalogid(), certCatalog.getCertname()));
		}
		return result;
	}


	public AuditCertRelation getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditCertRelation dataBean) {
        this.dataBean = dataBean;
    }

}
