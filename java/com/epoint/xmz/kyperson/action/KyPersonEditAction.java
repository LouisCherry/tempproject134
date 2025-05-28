package com.epoint.xmz.kyperson.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.kyperson.api.IKyPersonService;
import com.epoint.xmz.kyperson.api.entity.KyPerson;

/**
 * 勘验人员表修改页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
@RightRelation(KyPersonListAction.class)
@RestController("kypersoneditaction")
@Scope("request")
public class KyPersonEditAction extends BaseController
{

    @Autowired
    private IKyPersonService service;

    @Autowired
    private IMessagesCenterService messageCenterService;
    /**
     * 勘验人员表实体对象
     */
    private KyPerson dataBean = null;

    /**
     * 勘验结果单选按钮组model
     */
    private List<SelectItem> resultModel = null;

    /**
     * 附件上传
     */
    private FileUploadModel9 fileUploadModel;
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new KyPerson();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        String messageItemGuid = getRequestParameter("MessageItemGuid");
        dataBean.setOperatedate(new Date());
        dataBean.setRepplytime(new Date());
        service.update(dataBean);
        messageCenterService.deleteMessage(messageItemGuid, userSession.getUserGuid());
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public KyPerson getDataBean() {
        return dataBean;
    }

    public void setDataBean(KyPerson dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getResultModel() {
        if (resultModel == null) {
            resultModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "勘验结果", null, false));
        }
        return this.resultModel;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getClientguid(), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        // 该属性设置他为只读，不能被删除
        // fileUploadModel1.setReadOnly("true");
        return fileUploadModel;
    }
}
