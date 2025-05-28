package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.IAuditQypgTaskService;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.entity.AuditQypgTask;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 区域评估事项信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 14:21:45]
 */
@RestController("spglqypgsxxxbaddaction")
@Scope("request")
public class SpglQypgsxxxbAddAction extends BaseController {
    @Autowired
    private ISpglQypgsxxxbService service;
    @Autowired
    private IAuditQypgTaskService iAuditQypgTaskService;
    /**
     * 区域评估事项信息表实体对象
     */
    private SpglQypgsxxxb dataBean = null;
    private FileUploadModel9 fileUploadModel;
    private String cliengguid;
    private String qypgdybm = "";
    private String qypgguid = "";

    public void pageLoad() {
        qypgdybm = getRequestParameter("qypgdybm");
        qypgguid = getRequestParameter("qypgguid");
        dataBean = new SpglQypgsxxxb();
        cliengguid = dataBean.getCliengguid();
        if (StringUtil.isBlank(getViewData("cliengguid"))) {
            if (StringUtil.isBlank(cliengguid)) {
                cliengguid = UUID.randomUUID().toString();
            }
            addViewData("cliengguid", cliengguid);
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        // todo qypgsxbm 和 taskguid存储的都是代码项值

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCliengguid(getViewData("cliengguid"));
        dataBean.setQypgdybm(qypgdybm);
        dataBean.setQypgguid(qypgguid);
        AuditQypgTask auditQypgTask = iAuditQypgTaskService.findByTaskcode(dataBean.getQypgsxbm());
        dataBean.setTaskguid(auditQypgTask.getRowguid());
        dataBean.setDybzspsxbm(auditQypgTask.get("basetaskcode"));
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        addViewData("cliengguid", UUID.randomUUID().toString());
        dataBean = new SpglQypgsxxxb();
    }

    public List<SelectItem> taskSelectModel() {
        return iAuditQypgTaskService.findAllList().stream().map(a -> {
            return new SelectItem(a.getTaskcode(), a.getTaskname());
        }).collect(Collectors.toList());
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(getViewData("cliengguid"), null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    public SpglQypgsxxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglQypgsxxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglQypgsxxxb dataBean) {
        this.dataBean = dataBean;
    }

}
