package com.epoint.auditresource.auditrsitembaseinfo.action;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目基本信息表修改页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnauditrsitembaseinfoeditaction")
@Scope("request")
public class JnAuditRsItemBaseinfoEditAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 5821443350885492397L;
    /**
     * 项目库API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    /**
     * 项目类型下拉列表model
     */
    private List<SelectItem> itemTypeModel = null;
    /**
     * 建设性质下拉列表model
     */
    private List<SelectItem> constructionPropertyModel = null;
    /**
     * 证照类型下拉列表model
     */
    private List<SelectItem> itemLegalCertTypeModel = null;
    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> belongtIndustryModel = null;
    /**
     * 法人性质下拉列表model
     */
    private List<SelectItem> legalPropertyModel = null;
    /**
     * 资金来源下拉列表model
     */
    private List<SelectItem> fundSourcesModel = null;
    /**
     * 财政资金来源下拉列表model
     */
    private List<SelectItem> financialResourcesModel = null;
    /**
     * 量化建设规模类别下拉列表model
     */
    private List<SelectItem> quantifyConstructTypeModel = null;
    /**
     * 是否技改项目单选按钮组model
     */
    private List<SelectItem> isImprovementModel = null;

    /**
     * 土地获取方式
     */
    private List<SelectItem> tdhqfsModel = null;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        // 国标行业文本赋值
        if (StringUtil.isNotBlank(dataBean.getGbhy())) {
            String gbhyText = codeItemsService.getItemTextByCodeName("国标行业2017", dataBean.getGbhy());
            if (StringUtil.isNotBlank(gbhyText)) {
                addCallbackParam("gbhyText", gbhyText);
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setItemlegalcreditcode(dataBean.getItemlegalcertnum());

        /* 3.0新增逻辑 */
        dataBean.setItemlegalcerttype(("1").equals(dataBean.getStr("JSDWLX")) ? "16" : "22");
        /* 3.0结束逻辑 */

        if (iAuditRsItemBaseinfo.checkItemCodeRepeat(dataBean).getResult() > 0) {
            addCallbackParam("msg", "项目代码重复！");
        } else {
            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(dataBean);
            addCallbackParam("msg", "修改成功！");
        }
    }

    public AuditRsItemBaseinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemTypeModel() {
        if (itemTypeModel == null) {
            itemTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目类型", null, false));
        }
        return itemTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getConstructionPropertyModel() {
        if (constructionPropertyModel == null) {
            constructionPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设性质", null, false));
        }
        return constructionPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getItemLegalCertTypeModel() {
        if (itemLegalCertTypeModel == null) {
            itemLegalCertTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            //去除身份证等个人选择
            itemLegalCertTypeModel.removeIf(a -> Integer.parseInt(String.valueOf(a.getValue())) >= Integer
                    .parseInt(ZwfwConstant.CERT_TYPE_SFZ));
        }
        return itemLegalCertTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBelongtIndustryModel() {
        if (belongtIndustryModel == null) {
            belongtIndustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属行业", null, false));
        }
        return belongtIndustryModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLegalPropertyModel() {
        if (legalPropertyModel == null) {
            legalPropertyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "法人性质", null, false));
        }
        return legalPropertyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFundSourcesModel() {
        if (fundSourcesModel == null) {
            fundSourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "资金来源", null, false));
        }
        return fundSourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFinancialResourcesModel() {
        if (financialResourcesModel == null) {
            financialResourcesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "财政资金来源", null, false));
        }
        return financialResourcesModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getQuantifyConstructTypeModel() {
        if (quantifyConstructTypeModel == null) {
            quantifyConstructTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "量化建设规模的类别", null, false));
        }
        return quantifyConstructTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsImprovementModel() {
        if (isImprovementModel == null) {
            isImprovementModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return isImprovementModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTdhqfsModel() {
        if (tdhqfsModel == null) {
            tdhqfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "土地获取方式", null, false));
        }
        return tdhqfsModel;
    }
}
