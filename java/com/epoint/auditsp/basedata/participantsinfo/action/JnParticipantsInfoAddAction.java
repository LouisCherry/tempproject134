package com.epoint.auditsp.basedata.participantsinfo.action;

import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 参建单位信息表新增页面对应的后台
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
@RestController("jnparticipantsinfoaddaction")
@Scope("request")
public class JnParticipantsInfoAddAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IParticipantsInfoService service;
    /**
     * 参建单位信息表实体对象
     */
    private ParticipantsInfo dataBean = null;
    private String subappguid = "";
    private String spitaskguid = "";
    private String corptype = "";

    public void pageLoad() {
        corptype = getRequestParameter("corptype");

        if (dataBean == null) {
            dataBean = new ParticipantsInfo();
        }

        if (StringUtil.isNotBlank(getViewData("rowguid"))) {
            dataBean.setRowguid(getViewData("rowguid"));
        } else {
            dataBean.setRowguid(UUID.randomUUID().toString());
        }
        subappguid = getRequestParameter("subappguid");
        spitaskguid = getRequestParameter("spitaskguid");

        // 将单位证照类型默认为统一社会信用代码
        dataBean.setItemlegalcerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM);
    }

    /**
     * 保存并关闭
     */
    public void add() {
        if (!service.isExistSameParticipantsInfo(dataBean.getCorpcode(), dataBean.getXmfzr_idcard(),
                dataBean.getSubappguid(), corptype)) {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            if (StringUtil.isBlank(corptype)) {
                corptype = dataBean.getCorptype();
            }
            dataBean.setCorptype(corptype);
            dataBean.setSubappguid(subappguid);
            service.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
        } else {
            addCallbackParam("err", "已存在相同单位与负责人，请勿重复添加！");
        }
    }

    /**
     * 保存并新建
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

}
