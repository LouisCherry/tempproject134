package com.epoint.xmz.zjzcssp.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjzcssp.api.entity.ZjZcssp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.xmz.zjzcssp.api.IZjZcsspService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 邹城随手拍表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@RightRelation(ZjZcsspListAction.class)
@RestController("zjzcsspdetailaction")
@Scope("request")
public class ZjZcsspDetailAction extends BaseController {
	@Autowired
	private IZjZcsspService service;

	@Autowired
	private IAttachService attachService;

	private FileUploadModel9 fileUploadModel1;

	private String ItemResultPhoto = "";

	private String ssxzcliengguid = "";

	/**
	 * 邹城随手拍表实体对象
	 */
	private ZjZcssp dataBean = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		ssxzcliengguid = dataBean.getStr("videoattachguid");
		if (dataBean == null) {
			dataBean = new ZjZcssp();
		} else {
			if (StringUtil.isBlank(ssxzcliengguid)) {
				ssxzcliengguid = UUID.randomUUID().toString();
				dataBean.set("videoattachguid", ssxzcliengguid);
			}
		}
		ItemResultPhoto = dataBean.getAttachguid();
		List<FrameAttachStorage> storges = attachService.getAttachListByGuid(ItemResultPhoto);
		if (storges != null && storges.size() > 0) {
			for (int i = 0; i < storges.size(); i++) {
				FrameAttachStorage attach = storges.get(i);
				String sign = "signPicture" + i;
				String dataurl = "";
				if (attach != null) {
					dataurl = "data:" + attach.getContentType() + ";base64,"
							+ Base64Util.encode(FileManagerUtil.getContentFromInputStream(attach.getContent()));
				}
				addCallbackParam(sign, dataurl);
			}
		} else {
			ItemResultPhoto = UUID.randomUUID().toString();
		}
		
	}

	public ZjZcssp getDataBean() {
		return dataBean;
	}

	public FileUploadModel9 getFileUploadModel1() {
		if (fileUploadModel1 == null) {
			fileUploadModel1 = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(ssxzcliengguid, null, null, null,
					userSession.getUserGuid(), userSession.getDisplayName()));
		}
		return fileUploadModel1;
	}

	public String getSsxzcliengguid() {
		return ssxzcliengguid;
	}

	public void setSsxzcliengguid(String ssxzcliengguid) {
		this.ssxzcliengguid = ssxzcliengguid;
	}
}