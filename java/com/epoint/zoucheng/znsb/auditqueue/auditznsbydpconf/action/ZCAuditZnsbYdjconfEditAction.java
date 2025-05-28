package com.epoint.zoucheng.znsb.auditqueue.auditznsbydpconf.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbydpconf.domain.AuditZnsbYdpconf;
import com.epoint.basic.auditqueue.auditznsbydpconf.inter.IAuditZnsbYdpconfService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

@RightRelation(ZCAuditZnsbYdjconfListAction.class)
@RestController("zcauditznsbydjconfeditaction")
@Scope("request")
public class ZCAuditZnsbYdjconfEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditZnsbYdpconfService service;

    /**
     * 引导屏页面配置表实体对象
     */
    private AuditZnsbYdpconf dataBean = null;

    /**
     * 是否启用单选按钮组model
     */
    private List<SelectItem> isenableModel = null;
    /**
     * 文章类型下拉列表model
     */
    private List<SelectItem> articletypeModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.getYdpconfByRowguid(guid).getResult();
        if (ZwfwConstant.CONSTANT_STR_ZERO.equals(dataBean.getType())) {
            addCallbackParam("type", "中心简介");
        }
        else if (ZwfwConstant.CONSTANT_STR_THREE.equals(dataBean.getType())) {
            addCallbackParam("type", "中心简介(横版)");
        }
        else if (ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getType())) {
            addCallbackParam("type", "新闻动态");
        }
        else if ("4".equals(dataBean.getType())) {
            addCallbackParam("type", "诵经典");
        }
        else {
            addCallbackParam("type", "配套服务");
        }

        if (dataBean == null) {
            dataBean = new AuditZnsbYdpconf();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getType())) {
            if (StringUtil.isNotBlank(dataBean.getNewsTitle())) {
                service.updateAuditZnsbYdpconf(dataBean);
                addCallbackParam("msg", "修改成功！");
            }
            else {
                addCallbackParam("msg", "标题不能为空！");
            }
        }
        else {
            service.updateAuditZnsbYdpconf(dataBean);
            addCallbackParam("msg", "修改成功！");
        }

    }

    public AuditZnsbYdpconf getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbYdpconf dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsenableModel() {
        if (isenableModel == null) {
            isenableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isenableModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getArticletypeModel() {
        if (articletypeModel == null) {
            articletypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "诵经典文章类型", null, false));
        }
        return this.articletypeModel;
    }

}
