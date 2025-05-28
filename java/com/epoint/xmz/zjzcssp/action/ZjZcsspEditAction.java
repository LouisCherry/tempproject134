package com.epoint.xmz.zjzcssp.action;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjzcssp.api.entity.ZjZcssp;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.zjzcssp.api.IZjZcsspService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 邹城随手拍表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@RightRelation(ZjZcsspListAction.class)
@RestController("zjzcsspeditaction")
@Scope("request")
public class ZjZcsspEditAction extends BaseController {

	@Autowired
	private IZjZcsspService service;

	@Autowired
	private IAttachService attachService;

	/**
	 * 附件上传model
	 */
	private FileUploadModel9 fileUploadModel = null;

	private FileUploadModel9 fileUploadModel1;

	/**
	 * 邹城随手拍表实体对象
	 */
	private ZjZcssp dataBean = null;

	private String ItemResultPhoto = "";

	private String ssxzcliengguid = "";

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

	/**
	 * 保存修改
	 * 
	 */
	public void save() {
		dataBean.setOperatedate(new Date());
		service.update(dataBean);
		addCallbackParam("msg", "修改成功！");
	}

	public ZjZcssp getDataBean() {
		return dataBean;
	}

	public void setDataBean(ZjZcssp dataBean) {
		this.dataBean = dataBean;
	}

	/**
	 * 上传现场效果图片
	 * 
	 * @return
	 */
	public FileUploadModel9 getFileUploadModel() {
		if (fileUploadModel == null) {
			AttachHandler9 handler = new AttachHandler9() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean beforeSaveAttachToDB(AttachStorage storage) {
					byte[] picContent = FileManagerUtil.getContentFromInputStream(storage.getIn());
					String picContentType = storage.getContentType();
					String base64Str = Base64Util.encode(picContent);
					fileUploadModel.getExtraDatas().put("lblPicture",
							"data:" + picContentType + ";base64," + base64Str);

					storage.setIn(new ByteArrayInputStream(picContent));

					FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
					frameAttachInfo.setAttachGuid(UUID.randomUUID().toString());
					frameAttachInfo.setAttachFileName(storage.getAttachFileName());
					frameAttachInfo.setCliengGuid(ItemResultPhoto);
					frameAttachInfo.setCliengTag("ItemResultPhoto");
					frameAttachInfo.setUploadDateTime(new Date());
					frameAttachInfo.setContentType(storage.getContentType());
					frameAttachInfo.setAttachLength(Long.valueOf((long) storage.getSize()));

					attachService.addAttach(frameAttachInfo, storage.getIn());

					return false;
				}

				@Override
				public void afterSaveAttachToDB(Object attach) {

				}
			};
			fileUploadModel = new FileUploadModel9(
					new DefaultFileUploadHandlerImpl9(ItemResultPhoto, "ItemResultPhoto", null, handler,
							UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName()));
		}
		return fileUploadModel;
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
