package com.epoint.fmgl.auditrsitembaseinfoextends.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.IAuditRsItemBaseinfoExtendsService;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwdt.tzzxjgpt.rest.TzzxjgptUtil;

/**
 * 赋码项目基本信息表修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-23 15:19:10]
 */
@RightRelation(AuditRsItemBaseinfoExtendsListAction.class)
@RestController("auditrsitembaseinfoextendseditaction")
@Scope("request")
public class AuditRsItemBaseinfoExtendsEditAction extends BaseController
{

    @Autowired
    private IAuditRsItemBaseinfoExtendsService service;

    /**
     * 赋码项目基本信息表实体对象
     */
    private AuditRsItemBaseinfoExtends dataBean = null;

    /**
     * 行业核准目录下拉列表model
     */
    private List<SelectItem> permititemcodeModel = null;
    /**
     * 建设性质下拉列表model
     */
    private List<SelectItem> constructperModel = null;
    /**
     * 产业结构调整指导目录下拉列表model
     */
    private List<SelectItem> cyjgtzzdmlModel = null;
    /**
     * 项目单位性质下拉列表model
     */
    private List<SelectItem> enterprisenatureModel = null;
    /**
     * 项目单位性质（申报单位）下拉列表model
     */
    private List<SelectItem> enterprisenaturesbModel = null;
    /**
     * 国标行业下拉列表model
     */
    private List<SelectItem> industryModel = null;
    /**
     * 申报单位项目法人证照类型下拉列表model
     */
    private List<SelectItem> lerepcerttypesbModel = null;
    /**
     * 投资项目行业分类下拉列表model
     */
    private List<SelectItem> permitindustryModel = null;
    /**
     * 建设地点下拉列表model
     */
    private List<SelectItem> placecodeModel = null;
    /**
     * 项目属性下拉列表model
     */
    private List<SelectItem> projectattributesModel = null;
    /**
     * 项目阶段下拉列表model
     */
    private List<SelectItem> projectstageModel = null;
    /**
     * 项目类型下拉列表model
     */
    private List<SelectItem> projecttypeModel = null;
    /**
     * 状态下拉列表model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 所属行业下拉列表model
     */
    private List<SelectItem> theindustryModel = null;
    /**
     * 拟向民间资本推介项目下拉列表model
     */
    private List<SelectItem> tjxmModel = null;
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfoExtends();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }
    /**
     * 提交
     * 
     */
    public void submit() {
        dataBean.setOperatedate(new Date());
        try {
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            String resultdata = TzzxjgptUtil.sendtZxmbcxmxxcs(dataBean, auditOnlineRegister);
            JSONObject jsondata = JSONObject.parseObject(resultdata);
            String code = jsondata.getString("code");
            if("1".equals(code)) {
                String investId = jsondata.getString("investId");
                String seqId = jsondata.getString("seqId");
                dataBean.setInvestid(investId);
                dataBean.setSeqid(seqId);
                dataBean.setStatus("1");
                service.update(dataBean);
                addCallbackParam("msg", "申请提交成功！");
            }else {
                String errorText = jsondata.getString("error");
                if(StringUtil.isNotBlank(errorText)) {
                    addCallbackParam("msg", "申请提交失败！"+errorText);
                }else {
                    addCallbackParam("msg", "申请提交失败！");
                }
            }
        }
        catch (Exception e) {
            addCallbackParam("msg", "申请提交失败！");
        }
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditRsItemBaseinfoExtends getDataBean() {
        return dataBean;
    }
    
    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    public void setDataBean(AuditRsItemBaseinfoExtends dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getPermititemcodeModel() {
        if (permititemcodeModel == null) {
            permititemcodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_投资项目核准目录行业分类", null, false));
        }
        return this.permititemcodeModel;
    }

    public List<SelectItem> getConstructperModel() {
        if (constructperModel == null) {
            constructperModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_建设性质", null, false));
        }
        return this.constructperModel;
    }

    public List<SelectItem> getCyjgtzzdmlModel() {
        if (cyjgtzzdmlModel == null) {
            cyjgtzzdmlModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "产业结构222", null, false));
        }
        return this.cyjgtzzdmlModel;
    }

    public List<SelectItem> getEnterprisenatureModel() {
        if (enterprisenatureModel == null) {
            enterprisenatureModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_申报单位性质", null, false));
        }
        return this.enterprisenatureModel;
    }

    public List<SelectItem> getEnterprisenaturesbModel() {
        if (enterprisenaturesbModel == null) {
            enterprisenaturesbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_申报单位性质", null, false));
        }
        return this.enterprisenaturesbModel;
    }

    public List<SelectItem> getIndustryModel() {
        if (industryModel == null) {
            industryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标行业", null, false));
        }
        return this.industryModel;
    }

    public List<SelectItem> getLerepcerttypesbModel() {
        if (lerepcerttypesbModel == null) {
            lerepcerttypesbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_证件类型", null, false));
        }
        return this.lerepcerttypesbModel;
    }

    public List<SelectItem> getPermitindustryModel() {
        if (permitindustryModel == null) {
            permitindustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_投资项目行业分类", null, false));
        }
        return this.permitindustryModel;
    }

    public List<SelectItem> getPlacecodeModel() {
        if (placecodeModel == null) {
            placecodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "济宁区县", null, false));
        }
        return this.placecodeModel;
    }

    public List<SelectItem> getProjectattributesModel() {
        if (projectattributesModel == null) {
            projectattributesModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目属性", null, false));
        }
        return this.projectattributesModel;
    }

    public List<SelectItem> getProjectstageModel() {
        if (projectstageModel == null) {
            projectstageModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目阶段", null, false));
        }
        return this.projectstageModel;
    }

    public List<SelectItem> getProjecttypeModel() {
        if (projecttypeModel == null) {
            projecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_项目类型", null, false));
        }
        return this.projecttypeModel;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_建设性质", null, false));
        }
        return this.statusModel;
    }

    public List<SelectItem> getTheindustryModel() {
        if (theindustryModel == null) {
            theindustryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "省发改_所属行业", null, false));
        }
        return this.theindustryModel;
    }

    public List<SelectItem> getTjxmModel() {
        if (tjxmModel == null) {
            tjxmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.tjxmModel;
    }

}
