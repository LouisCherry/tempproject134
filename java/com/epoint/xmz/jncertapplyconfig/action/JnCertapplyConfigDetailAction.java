package com.epoint.xmz.jncertapplyconfig.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.jncertapplyconfig.api.IJnCertapplyConfigService;
import com.epoint.xmz.jncertapplyconfig.api.entity.JnCertapplyConfig;

/**
 * 证照申办配置表详情页面对应的后台
 * 
 * @author future
 * @version [版本号, 2023-03-21 15:06:54]
 */
@RestController("jncertapplyconfigdetailaction")
@Scope("request")
public class JnCertapplyConfigDetailAction extends BaseController
{
    @Autowired
    private IJnCertapplyConfigService service;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;

    /**
     * 证照申办配置表实体对象
     */
    private JnCertapplyConfig dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new JnCertapplyConfig();
        }
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            if (StringUtil.isNotBlank(dataBean.getCert_picture())) {
                fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getCert_picture(),
                        null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        // 该属性设置他为只读，不能被删除
        //fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }

    public JnCertapplyConfig getDataBean() {
        return dataBean;
    }
}
