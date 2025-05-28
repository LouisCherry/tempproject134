package com.epoint.zczwfw.auditpromisebook.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zczwfw.auditpromisebook.api.IAuditPromiseBookService;
import com.epoint.zczwfw.auditpromisebook.api.entity.AuditPromiseBook;

/**
 * 卫生许可告知书附件表新增页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@RestController("auditpromisebookaddaction")
@Scope("request")
public class AuditPromiseBookAddAction extends BaseController {
	@Autowired
	private IAuditPromiseBookService service;
	/**
	 * 卫生许可告知书附件表实体对象
	 */
	private AuditPromiseBook dataBean = null;

	/**
	 * 告知承诺类型下拉列表model
	 */
	private List<SelectItem> promisetypeModel = null;
	
	private String smallCliengguid;
	
	/**
	 * 图标的后台
	 */
	private FileUploadModel9 fileUploadModel = null;

	public void pageLoad() {
		dataBean = new AuditPromiseBook();
		smallCliengguid = getRequestParameter("cliengguid");
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		dataBean.setRowguid(UUID.randomUUID().toString());
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		dataBean.setPromiseattachguid((getRequestParameter("cliengguid")));
		service.insert(dataBean);
		addCallbackParam("msg", "保存成功！");
		dataBean = null;
	}
	
	public FileUploadModel9 getFileUploadModel() {
		if (fileUploadModel == null) {
			AttachHandler9 handler = new AttachHandler9() {
				private static final long serialVersionUID = 1L;

				@Override
				public void afterSaveAttachToDB(Object arg0) {
				}

				@Override
				public boolean beforeSaveAttachToDB(AttachStorage attach) {
					return true;
				}
			};

			// DefaultFileUploadHandlerImpl9参数为：clientGuid，clientTag，clientInfo，AttachHandler9，attachHandler，userGuid，userName
			// clientGuid一般是地址中获取到
			fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getRequestParameter("cliengguid"),
					null, null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
		}
		return fileUploadModel;
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditPromiseBook();
	}

	public AuditPromiseBook getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditPromiseBook();
		}
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
