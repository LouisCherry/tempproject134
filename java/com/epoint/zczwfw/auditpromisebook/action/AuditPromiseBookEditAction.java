package com.epoint.zczwfw.auditpromisebook.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zczwfw.auditpromisebook.api.IAuditPromiseBookService;
import com.epoint.zczwfw.auditpromisebook.api.entity.AuditPromiseBook;

/**
 * 卫生许可告知书附件表修改页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@RestController("auditpromisebookeditaction")
@Scope("request")
public class AuditPromiseBookEditAction extends BaseController {

	@Autowired
	private IAuditPromiseBookService service;

	/**
	 * 卫生许可告知书附件表实体对象
	 */
	private AuditPromiseBook dataBean = null;
	
	private FileUploadModel9 fileUploadModel = null;

	/**
	 * 告知承诺类型下拉列表model
	 */
	private List<SelectItem> promisetypeModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new AuditPromiseBook();
		}
	}
	
	public FileUploadModel9 getFileUploadModel() {
		if (fileUploadModel == null) {
            // clientGuid一般是地址中获取到的，此处只做参考使用
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getPromiseattachguid(), null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        // 该属性设置他为只读，不能被删除
//         fileUploadModel.setReadOnly("true");
        return fileUploadModel;
    }

	/**
	 * 保存修改
	 * 
	 */
	public void save() {
		dataBean.setOperatedate(new Date());
		service.update(dataBean);
		addCallbackParam("msg", "修改成功！");
	}

	public AuditPromiseBook getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditPromiseBook dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getPromisetypeModel() {
		if (promisetypeModel == null) {
			promisetypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "告知承诺类型", null, false));
		}
		return this.promisetypeModel;
	}

}
