package com.epoint.xmz.kyperson.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.StringUtil;
import com.epoint.xmz.kyperson.api.IKyPersonService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 勘验人员表详情页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
@RightRelation(KyPersonListAction.class)
@RestController("kypersondetailaction")
@Scope("request")
public class KyPersonDetailAction extends BaseController
{
    @Autowired
    private IKyPersonService service;
    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;
    
    private String kyguid;
    /**
     * 勘验人员表实体对象
     */
    private KyPerson dataBean = null;

    public void pageLoad() {
        kyguid = getRequestParameter("projectguid");
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        addViewData("clientguid", dataBean.getClientguid());
        if (StringUtil.isBlank(getViewData("clientguid"))) {
            addViewData("clientguid", UUID.randomUUID().toString());
        }
        if (dataBean == null) {
            dataBean = new KyPerson();
        }
    }

    public KyPerson getDataBean() {
        return dataBean;
    }
    
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            String clientGuid = getViewData("clientguid");
            if (StringUtil.isBlank(clientGuid)) {
                addViewData("clientguid", UUID.randomUUID().toString());
            }
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("clientguid"), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        // 该属性设置他为只读，不能被删除
        fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }
}
