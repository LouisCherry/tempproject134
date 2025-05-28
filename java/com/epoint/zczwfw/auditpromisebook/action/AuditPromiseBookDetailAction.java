package com.epoint.zczwfw.auditpromisebook.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.zczwfw.auditpromisebook.api.IAuditPromiseBookService;
import com.epoint.zczwfw.auditpromisebook.api.entity.AuditPromiseBook;

/**
 * 卫生许可告知书附件表详情页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@RestController("auditpromisebookdetailaction")
@Scope("request")
public class AuditPromiseBookDetailAction extends BaseController {
	@Autowired
	private IAuditPromiseBookService service;

	/**
	 * 卫生许可告知书附件表实体对象
	 */
	private AuditPromiseBook dataBean = null;
	
	private FileUploadModel9 fileUploadModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new AuditPromiseBook();
		}
	}
	
	public FileUploadModel9 getFileUploadModel() {
		if (fileUploadModel == null) {
            // DefaultFileUploadHandlerImpl9具体详情可以去查基础api
            // DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName

            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getPromiseattachguid(), null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        // 该属性设置他为只读，不能被删除
         fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }

	public AuditPromiseBook getDataBean() {
		return dataBean;
	}
}