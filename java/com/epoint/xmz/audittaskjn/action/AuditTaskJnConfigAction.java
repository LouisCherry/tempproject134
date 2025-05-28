package com.epoint.xmz.audittaskjn.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 事项个性化表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-11-01 14:01:22]
 */
@RestController("audittaskjnconfigaction")
@Scope("request")
public class AuditTaskJnConfigAction extends BaseController {
    @Autowired
    private IAuditTaskJnService service;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IUserService userService;

    /**
     * 事项个性化表实体对象
     */
    private AuditTaskJn dataBean = null;

    /**
     * 是否划转单选按钮组model
     */
    private List<SelectItem> is_hzModel = null;

    private String taskguid = "";

    private AuditTask auditTask;

    public void pageLoad() {
        taskguid = getRequestParameter("taskguid");
        auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {
            String sql = "select is_hz,GROUP_CONCAT(jg_username) jg_username,GROUP_CONCAT(jg_userguid) jg_userguid from audit_task_jn where task_id = ? ";
            dataBean = service.find(sql, auditTask.getTask_id());
            if (dataBean != null) {
                addCallbackParam("jg_usernames", dataBean.getJg_username());
                addCallbackParam("jg_userguids", dataBean.getJg_userguid());
            }
        } else {
            dataBean = new AuditTaskJn();
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        //划转的时候判断
        if ("1".equals(dataBean.getIs_hz())) {
            if (StringUtil.isBlank(dataBean.getJg_username())) {
                addCallbackParam("msg", "人员未选择！");
                return;
            }
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            //每次全部删除在新增
            service.deleteAllTaskJnByTaskid(auditTask.getTask_id());
            if (auditTask != null) {
                for (String userguid : dataBean.getJg_userguid().split(",")) {
                    AuditTaskJn newJn = new AuditTaskJn();
                    newJn.setOperatedate(new Date());
                    newJn.setOperateusername(userSession.getDisplayName());
                    newJn.setRowguid(UUID.randomUUID().toString());
                    newJn.setTask_id(auditTask.getTask_id());
                    newJn.setItem_id(auditTask.getItem_id());
                    newJn.setIs_hz(dataBean.getIs_hz());
                    newJn.setOuguid(auditTask.getOuguid());
                    FrameUser frameUser = userService.getUserByUserField("userguid", userguid);
                    if (frameUser != null) {
                        FrameOu frameOu = ouService.getOuByOuGuid(frameUser.getOuGuid());
                        if (frameOu != null) {
                            newJn.setJg_ouguid(frameOu.getOuguid());
                            newJn.setJg_ouname(frameOu.getOuname());
                        }
                        newJn.setJg_userguid(frameUser.getUserGuid());
                        newJn.setJg_username(frameUser.getDisplayName());
                        service.insert(newJn);
                    }
                }
                addCallbackParam("msg", l("保存成功！"));
                dataBean = null;
            } else {
                addCallbackParam("msg", l("当前事项不存在！"));
            }
        } else {
            //不划转全部删除
            service.deleteAllTaskJnByTaskid(auditTask.getTask_id());
            addCallbackParam("msg", l("保存成功！"));
        }
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditTaskJn();
    }

    public AuditTaskJn getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskJn();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskJn dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIs_hzModel() {
        if (is_hzModel == null) {
            is_hzModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_hzModel;
    }

}
