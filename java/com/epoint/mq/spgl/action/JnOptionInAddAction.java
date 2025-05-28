package com.epoint.mq.spgl.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.mq.spgl.api.IJnOptionInService;
import com.epoint.mq.spgl.api.entity.JnOptionIn;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmqqyjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmqqyjxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 成品油零售经营企业库新增页面对应的后台
 *
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RightRelation(JnOptionInListAction.class)
@RestController("jnoptioninaddaction")
@Scope("request")
public class JnOptionInAddAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -6643486139267184886L;
    @Autowired
    private IJnOptionInService service;

    @Autowired
    private ISpglXmqqyjxxbV3 spglXmqqyjxxbV3service;
    /**
     * 成品油零售经营企业库实体对象
     */
    private JnOptionIn dataBean = null;


    public void pageLoad() {
        dataBean = new JnOptionIn();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        String xmdm = getRequestParameter("xmdm");
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setXzqhdm("370800");
        dataBean.setQqyjslbm(UUID.randomUUID().toString());
        dataBean.setSync(999);
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        dataBean.setXmdm(xmdm);
        service.insert(dataBean);

        //同步生成到3.0表里
        SpglXmqqyjxxbV3 xmqqyjxxbV3 = new SpglXmqqyjxxbV3();
        String rowguid = UUID.randomUUID().toString();
        xmqqyjxxbV3.setRowguid(rowguid);
        xmqqyjxxbV3.setOperatedate(new Date());
        xmqqyjxxbV3.setDfsjzj(rowguid);
        xmqqyjxxbV3.setOperateusername(userSession.getDisplayName());
        xmqqyjxxbV3.setXzqhdm("370800");
        xmqqyjxxbV3.setQqyjslbm(UUID.randomUUID().toString());
        xmqqyjxxbV3.setSjsczt(0);
        xmqqyjxxbV3.setSjyxbs(1);
        xmqqyjxxbV3.setXmdm(xmdm);
        xmqqyjxxbV3.setBldwmc(dataBean.getBldwmc());
        xmqqyjxxbV3.setBlr(dataBean.getBlr());
        xmqqyjxxbV3.setFksj(dataBean.getFksj());
        xmqqyjxxbV3.setQqyj(dataBean.getQqyj());
        spglXmqqyjxxbV3service.insert(xmqqyjxxbV3);

        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new JnOptionIn();
    }

    public JnOptionIn getDataBean() {
        if (dataBean == null) {
            dataBean = new JnOptionIn();
        }
        return dataBean;
    }

    public void setDataBean(JnOptionIn dataBean) {
        this.dataBean = dataBean;
    }


}
