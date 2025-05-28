package com.epoint.auditsp.basedata.participantsinfo.action;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 参建单位信息表修改页面对应的后台
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
@RightRelation(ParticipantsInfoListAction.class)
@RestController("jnparticipantsinfoeditaction")
@Scope("request")
public class JnParticipantsInfoEditAction extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IParticipantsInfoService service;
    @Autowired
    private ICodeItemsService codeItemService;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;


    /**
     * 证照类型下拉列表model
     */
    private List<SelectItem> itemLegalCertTypeModel = null;

    /**
     * 法人性质下拉列表model
     */
    private List<SelectItem> legalPropertyModel = null;


    /**
     * 参建单位信息表实体对象
     */
    private ParticipantsInfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ParticipantsInfo();
        }
        // 建设单位需要初始化建设单位类型
        if ("31".equals(dataBean.getCorptype())) {
            String itemlegalcerttype = dataBean.getItemlegalcerttype();
            if (ZwfwConstant.CERT_TYPE_SFZ.equals(itemlegalcerttype)) {
                dataBean.set("jsdwlx", GxhSpConstant.GB_ZJLX_ZRR);
            }
            else if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(itemlegalcerttype)) {
                dataBean.set("jsdwlx", GxhSpConstant.GB_ZJLX_FR);
            }
        }
    }

    /**
     * 保存修改
     *
     */
    public void save() {
        // 验证重复性
        if(service.isExistSameParticipantsInfo(dataBean.getCorpcode(),dataBean.getXmfzr_idcard(),dataBean.getSubappguid(),dataBean.getCorptype(),dataBean.getRowguid())){
            addCallbackParam("msg", "已存在相同单位与负责人，请勿重复添加！");
            return;
        }
        Boolean temp =false;
        dataBean.setOperatedate(new Date());
//        if(!service.isExistSameParticipantsInfo(dataBean.getCorpcode(),dataBean.getXmfzr_idcard(),dataBean.getSubappguid())){
        if("31".equals(dataBean.getCorptype())){
            AuditRsItemBaseinfo baseinfo = new AuditRsItemBaseinfo();
            temp = true;
            dataBean.setCorpcode(dataBean.getItemlegalcertnum());
            dataBean.setCorpname(dataBean.getItemlegaldept());
            baseinfo.setRowguid(getRequestParameter("itemguid"));
            baseinfo.setDepartname(dataBean.getCorpname());
            //        baseinfo.setLegalproperty(dataBean.get);
            //        baseinfo.setItemlegaldept(dataBean.get);
            baseinfo.setConstructionaddress(dataBean.getAddress());
            baseinfo.setItemlegalcreditcode(dataBean.getItemlegalcertnum());
//                baseinfo.setItemlegalcreditcode(dataBean.getItemlegalcertnum());
            baseinfo.setLegalperson(dataBean.getLegal());
            //            baseinfo.setContractperson(dataBean.getXmfzr());
            baseinfo.setContractidcart(dataBean.getXmfzr_idcard());
            //            baseinfo.setContractphone(dataBean.getXmfzr_phone());

            baseinfo.setContractperson(dataBean.getDanweilxr());
            baseinfo.setContractphone(dataBean.getDanweilxrlxdh());

            baseinfo.setLegalproperty(dataBean.getLegalproperty());
            baseinfo.setItemlegaldept(dataBean.getItemlegaldept());
            baseinfo.setItemlegalcerttype(dataBean.getItemlegalcerttype());
            baseinfo.setItemlegalcertnum(dataBean.getItemlegalcertnum());
            baseinfo.setLegalpersonicardnum(dataBean.getLegalpersonicardnum());
            baseinfo.setFrphone(dataBean.getFrphone());
            baseinfo.setFremail(dataBean.getFremail());
            baseinfo.setContractidcart(dataBean.getDanweilxrsfz());
            if(temp){
                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(baseinfo);
            }
            else {
                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(baseinfo);
            }
        }
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
//        }else{
//            addCallbackParam("msg", "已存在相同单位与负责人，请勿重复添加！");
//        }
    }

    public ParticipantsInfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(ParticipantsInfo dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getCbType() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemService.listCodeItemsByCodeName("单位类型");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
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

}
