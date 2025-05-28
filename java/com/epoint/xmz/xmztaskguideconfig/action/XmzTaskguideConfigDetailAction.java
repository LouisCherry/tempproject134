package com.epoint.xmz.xmztaskguideconfig.action;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.StringUtil;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;

/**
 * 事项指南配置表详情页面对应的后台
 * 
 * @author xczheng0314
 * @version [版本号, 2023-03-21 11:38:55]
 */
@RightRelation(XmzTaskguideConfigListAction.class)
@RestController("xmztaskguideconfigdetailaction")
@Scope("request")
public class XmzTaskguideConfigDetailAction extends BaseController
{
    @Autowired
    private IXmzTaskguideConfigService service;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    /**
     * 事项指南配置表实体对象
     */
    private XmzTaskguideConfig dataBean = null;
    /**
     * 背景图片
     */
    private FileUploadModel9 fileUploadModel;
    private String guidecliengguid = "";
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new XmzTaskguideConfig();
        }
        String xiaquname = iAuditOrgaArea.getAreaByAreacode(dataBean.getAreacode()).getResult().getXiaquname();
        dataBean.setAreacode(xiaquname);
        addViewData("guidecliengguid", dataBean.getGuidecliengguid());

    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            guidecliengguid = getViewData("guidecliengguid");
            // clientGuid一般是地址中获取到的，此处只做参考使用
            if (StringUtil.isBlank(guidecliengguid)) {
                guidecliengguid = UUID.randomUUID().toString();
                addViewData("guidecliengguid", guidecliengguid);
            }
            fileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(guidecliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        // 该属性设置他为只读，不能被删除
        fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }
    public XmzTaskguideConfig getDataBean() {
        return dataBean;
    }
}
