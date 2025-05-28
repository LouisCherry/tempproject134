package com.epoint.sghd.auditmember.action;

import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.sghd.auditmember.api.IJnAuditOrgaMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 中心人事人员表修改页面对应的后台
 *
 * @author zp
 * @version [版本号, 2017-03-15 09:24:09]
 */
@RestController("jnauditorgamembereditaction")
@Scope("request")
public class JnAuditOrgaMemberEditAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 2010029874731920870L;
    @Autowired
    private IAuditOrgaMember iAuditOrgaMember;

    /**
     * 中心人事人员表实体对象
     */
    private AuditOrgaMember dataBean = null;

    @Autowired
    private IJnAuditOrgaMember jnAuditOrgaMemberService;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = iAuditOrgaMember.getMemberByRowguid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditOrgaMember();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("gonghao", dataBean.getGonghao());
        sql.nq("rowguid", dataBean.getRowguid());
        AuditOrgaMember member = iAuditOrgaMember.getAuditMember(sql.getMap()).getResult();
        if (member != null) {
            addCallbackParam("same", "工号已存在");
            return;
        }
        dataBean.setOperatedate(new Date());
        iAuditOrgaMember.updateMember(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditOrgaMember getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaMember dataBean) {
        this.dataBean = dataBean;
    }


}
