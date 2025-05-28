package com.epoint.cert.auditcertrelation.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;

/**
 * 证照字段关系表新增页面对应的后台
 * 
 * @author miemieyang12128
 * @version [版本号, 2024-10-16 09:08:40]
 */
@RightRelation(AuditCertRelationListAction.class)
@RestController("auditcertrelationaddaction")
@Scope("request")
public class AuditCertRelationAddAction  extends BaseController
{
	@Autowired
	private IAuditCertRelationService service;
    /**
     * 证照字段关系表实体对象
     */
  	private AuditCertRelation dataBean=null;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    @Autowired
    private ICertCatalog iCertCatalog;

    private TreeModel treeModel;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IUserService userservice;

    @Autowired
    private ICertConfigExternal certConfigExternalImpl;

    public void pageLoad()
    {
        dataBean=new AuditCertRelation();
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setBelongxiaqucode(ZwfwUserSession.getInstance().getAreaCode());
        dataBean.setCreate_by(userSession.getDisplayName());
        dataBean.setCreate_time(new Date());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
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
	    service.insert(dataBean);
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new AuditCertRelation();
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



    /**
     * 阶段
     * @return
     */
    public List<SelectItem> getPhaseModel(){
       /* List<SelectItem> result = new ArrayList<SelectItem>();
        SqlConditionUtil sql = new SqlConditionUtil();
        //sql.eq("businedssguid",auditSpBusiness.getRowguid());
        sql.setOrder("ordernumber", "desc");
        List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
        if (EpointCollectionUtils.isNotEmpty(auditSpPhases))
        for (AuditSpPhase auditSpPhase : auditSpPhases) {
            result.add(new SelectItem(auditSpPhase.getRowguid(), auditSpPhase.getPhasename()));
        }
        return result;*/
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

    public AuditCertRelation getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditCertRelation();
        }
        return dataBean;
    }

    public void setDataBean(AuditCertRelation dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
