package com.epoint.xmgj.auditrsitembaseinfo.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmgj.auditrsitembaseinfo.api.IAuditRsItemBaseinfoService;
import com.epoint.xmgj.auditrsitembaseinfo.api.entity.AuditRsItemBaseinfo;

import cn.hutool.core.lang.UUID;

/**
 * 项目表详情页面对应的后台
 * 
 * @author pansh
 * @version [版本号, 2025-02-12 17:31:53]
 */
@RightRelation(AuditRsItemBaseinfoGjListAction.class)
@RestController("auditrsitembaseinfogjdetailaction")
@Scope("request")
public class AuditRsItemBaseinfoGjDetailAction extends BaseController
{
    @Autowired
    private IAuditRsItemBaseinfoService service;

    /**
     * 项目表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;

    /**
     * 资金到位说明文件
     */
    private FileUploadModel9 fundsreceivedUploadModel;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        if (StringUtil.isBlank(dataBean.get("fundsreceivedclientguid"))) {
            dataBean.set("fundsreceivedclientguid", UUID.randomUUID().toString());
        }
        List<String> projectnameList = service.getProjectnamesByProjectid(dataBean.getBiguid());
        if (projectnameList != null) {
            dataBean.set("ybyw", StringUtil.join(projectnameList));
        }
    }

    public AuditRsItemBaseinfo getDataBean() {
        return dataBean;
    }

    public FileUploadModel9 getFundsreceivedUploadModel() {
        if (fundsreceivedUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fundsreceivedUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    return true;
                }
            };
            fundsreceivedUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(dataBean.get("fundsreceivedclientguid"), null, null, handler,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fundsreceivedUploadModel;
    }
}
