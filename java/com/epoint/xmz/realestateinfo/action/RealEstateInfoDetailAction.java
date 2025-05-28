package com.epoint.xmz.realestateinfo.action;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 楼盘信息表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@RightRelation(RealEstateInfoListAction.class)
@RestController("realestateinfodetailaction")
@Scope("request")
public class RealEstateInfoDetailAction  extends BaseController
{
	  @Autowired
      private IRealEstateInfoService service; 
    
    /**
     * 楼盘信息表实体对象
     */
  	private RealEstateInfo dataBean=null;

	private FileUploadModel9 fileUploadFirstFloorModel;
	private FileUploadModel9 fileUploadStandFloorModel;
	private FileUploadModel9 fileUploadTopFloorModel;

	private FileUploadModel9 fileUploadHshgzModel;
  
    public void pageLoad() {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new RealEstateInfo();  
		  }
    }

	public FileUploadModel9 getFileUploadFirstFloorModel() {
		if (fileUploadFirstFloorModel == null) {
			if(StringUtil.isBlank(dataBean.getStr("firstfloor_pic"))){
				dataBean.put("firstfloor_pic", UUID.randomUUID().toString());
			}
			fileUploadFirstFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getStr("firstfloor_pic"), null, null,
					null, userSession.getUserGuid(), userSession.getDisplayName()));
		}
		fileUploadFirstFloorModel.setReadOnly("true");
		return fileUploadFirstFloorModel;
	}
	public FileUploadModel9 getFileUploadStandFloorModel() {
		if (fileUploadStandFloorModel == null) {
			if(StringUtil.isBlank(dataBean.getStr("standfloor_pic"))){
				dataBean.put("standfloor_pic", UUID.randomUUID().toString());
			}
			fileUploadStandFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getStr("standfloor_pic"), null, null,
					null, userSession.getUserGuid(), userSession.getDisplayName()));
		}
		fileUploadStandFloorModel.setReadOnly("true");
		return fileUploadStandFloorModel;
	}
	public FileUploadModel9 getFileUploadTopFloorModel() {
		if (fileUploadTopFloorModel == null) {
			if(StringUtil.isBlank(dataBean.getStr("topfloor_pic"))){
				dataBean.put("topfloor_pic", UUID.randomUUID().toString());
			}
			fileUploadTopFloorModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getStr("topfloor_pic"), null, null,
					null, userSession.getUserGuid(), userSession.getDisplayName()));
		}
		fileUploadTopFloorModel.setReadOnly("true");
		return fileUploadTopFloorModel;
	}
	public FileUploadModel9 getFileUploadHshgzModel() {
		if (fileUploadHshgzModel == null) {
			if(StringUtil.isBlank(dataBean.getStr("hshgz"))){
				dataBean.put("hshgz", UUID.randomUUID().toString());
			}
			fileUploadHshgzModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getStr("hshgz"), null, null,
					null, userSession.getUserGuid(), userSession.getDisplayName()));
		}
		fileUploadHshgzModel.setReadOnly("true");
		return fileUploadHshgzModel;
	}

	      public RealEstateInfo getDataBean() {
	          return dataBean;
	      }
}