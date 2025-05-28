package com.epoint.zoucheng.znsb.auditqueue.auditznsbydpconf.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbydpconf.domain.AuditZnsbYdpconf;
import com.epoint.basic.auditqueue.auditznsbydpconf.inter.IAuditZnsbYdpconfService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 引导屏页面配置表新增页面对应的后台
 * 
 * @author 王杰
 * @version [版本号, 2018-08-10 09:34:25]
 */
@RightRelation(ZCAuditZnsbYdjconfListAction.class)
@RestController("zcauditznsbydjconfaddaction")
@Scope("request")
public class ZCAuditZnsbYdjconfAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditZnsbYdpconfService Ydpconfservice;
    /**
     * 引导屏页面配置表实体对象
     */
    private AuditZnsbYdpconf dataBean = null;

    /**
     * 模块类型下拉列表model
     */
    private List<SelectItem> typeModel = null;
    /**
     * 文章类型下拉列表model
     */
    private List<SelectItem> articletypeModel = null;

    /**
     * 是否启用单选按钮组model
     */
    private List<SelectItem> isenableModel = null;

    public void pageLoad() {
        dataBean = new AuditZnsbYdpconf();
        dataBean.setType("1");// 默认中心新闻
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        if (Ydpconfservice.Isexist(dataBean.getType(), ZwfwUserSession.getInstance().getCenterGuid()).getResult()
                && ("0".equals(dataBean.getType()) || "3".equals(dataBean.getType()))) {
            addCallbackParam("msg", "中心简介已存在，请直接修改！");
            dataBean = null;
        }
        else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            Ydpconfservice.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
        }

    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditZnsbYdpconf();
    }

    public AuditZnsbYdpconf getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbYdpconf();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbYdpconf dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "引导屏页面配置", null, false));
        }
        return this.typeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getArticletypeModel() {
        if (articletypeModel == null) {
            articletypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "诵经典文章类型", null, false));
        }
        return this.articletypeModel;
    }

    @SuppressWarnings({"unchecked" })
    public List<SelectItem> getIsenableModel() {
        if (isenableModel == null) {
            isenableModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isenableModel;
    }

}
