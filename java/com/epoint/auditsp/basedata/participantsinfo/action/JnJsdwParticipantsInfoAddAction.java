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
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 参建单位信息表新增页面对应的后台
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
@RightRelation(ParticipantsInfoListAction.class)
@RestController("jnjsdwparticipantsinfoaddaction")
@Scope("request")
public class JnJsdwParticipantsInfoAddAction extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IParticipantsInfoService service;
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
//    @Autowired
//    private IDjgZhanghaoService zhService;
//    @Autowired
//    private IDjgDanweiinfoTempService tpService;
//    private DjgDanweiinfoTemp djgDanweiinfoTemp;
    /**
     * 参建单位信息表实体对象
     */
    private ParticipantsInfo dataBean = null;

    public void pageLoad() {
        dataBean = new ParticipantsInfo();
        //获取注册单位的信息
//        String userguid=userSession.getUserGuid();
//        String danweiguid = "";
//        if(StringUtil.isNotBlank(userguid)){
//            danweiguid=zhService.findDanWeiGuidByUserGuid(userguid);
//        }
//
//
//        if(StringUtil.isNotBlank(danweiguid)){
//            djgDanweiinfoTemp=tpService.findDanWeiByDanweiGuid(danweiguid);
//        }
//        if(djgDanweiinfoTemp!=null){
//            if(StringUtil.isNotBlank(djgDanweiinfoTemp.getDanweiname())){
//                dataBean.setCorpname(djgDanweiinfoTemp.getDanweiname());
//            }
//            if(StringUtil.isNotBlank(djgDanweiinfoTemp.getSocialcode())){
//                dataBean.setCorpcode(djgDanweiinfoTemp.getSocialcode());
//            }else if(StringUtil.isNotBlank(djgDanweiinfoTemp.getUnitorgcode()))
//            {
//                dataBean.setCorpcode(djgDanweiinfoTemp.getUnitorgcode());
//            }
//        }



    }

    /**
     * 保存并关闭
     *
     */
    public void add() {
        Boolean temp =true;
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setItemguid(getRequestParameter("itemguid"));
        dataBean.setCorptype("31");
        dataBean.setSubappguid("");
        dataBean.setCorpcode(dataBean.getItemlegalcertnum());
        dataBean.setCorpname(dataBean.getItemlegaldept());
        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(getRequestParameter("itemguid")).getResult();
        if (baseinfo == null) {
            temp = false;
            baseinfo = new AuditRsItemBaseinfo();
        }

        baseinfo.setDepartname(dataBean.getCorpname());
//        baseinfo.setLegalproperty(dataBean.get);
//        baseinfo.setItemlegaldept(dataBean.get);
        baseinfo.setRowguid(getRequestParameter("itemguid"));
        baseinfo.setConstructionaddress(dataBean.getAddress());
        baseinfo.setItemlegalcreditcode(dataBean.getCorpcode());
        baseinfo.setLegalperson(dataBean.getLegal());
//        baseinfo.setContractperson(dataBean.getXmfzr());
        baseinfo.setContractperson(dataBean.getDanweilxr());
        baseinfo.setContractphone(dataBean.getDanweilxrlxdh());

        baseinfo.setContractidcart(dataBean.getXmfzr_idcard());

        baseinfo.setLegalproperty(dataBean.getLegalproperty());
        //隐藏
        baseinfo.setItemlegaldept(dataBean.getItemlegaldept());
        baseinfo.setItemlegalcerttype(dataBean.getItemlegalcerttype());
        baseinfo.setItemlegalcertnum(dataBean.getItemlegalcertnum());
        baseinfo.setLegalpersonicardnum(dataBean.getLegalpersonicardnum());
        baseinfo.setFrphone(dataBean.getFrphone());
        baseinfo.setFremail(dataBean.getFremail());
        baseinfo.setContractidcart(dataBean.getDanweilxrsfz());
        /* 3.0新增逻辑 */
        baseinfo.set("jsdwlx", dataBean.getStr("jsdwlx"));
        /* 3.0结束逻辑 */
        if(temp){
            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(baseinfo);
        }
        else {
            iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(baseinfo);
        }
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     *
     */
    public void addNew() {
        add();
        dataBean = new ParticipantsInfo();
    }

    public ParticipantsInfo getDataBean() {
        if (dataBean == null) {
            dataBean = new ParticipantsInfo();
        }
        return dataBean;
    }

    public void setDataBean(ParticipantsInfo dataBean) {
        this.dataBean = dataBean;
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
