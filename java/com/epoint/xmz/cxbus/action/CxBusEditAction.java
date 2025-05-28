package com.epoint.xmz.cxbus.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.cxbus.api.entity.CxBus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditresource.cert.action.TARequestUtil;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.cxbus.api.ICxBusService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 车辆信息表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@RightRelation(CxBusListAction.class)
@RestController("cxbuseditaction")
@Scope("request")
public class CxBusEditAction extends BaseController {

	@Autowired
	private ICxBusService service;
	
	public static final String zwdturl= ConfigUtil.getConfigValue("zwdtparam", "zwdturl")+"/rest/jncxcltxzdj/";

	/**
	 * 车辆信息表实体对象
	 */
	private CxBus dataBean = null;

	/**
	 * 车辆种类下拉列表model
	 */
	private List<SelectItem> vehiclekindModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new CxBus();
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
	
	

	/**
	 * 保存修改
	 * 
	 */
	public void sendedit() {
	  dataBean.setOperatedate(new Date());
		
		
	  String  resulttoken = TARequestUtil.sendPostInner(zwdturl + "getaccesstoken", "{}", "", "");
	  log.info("getaccesstoken接口上传返回信息："+resulttoken);
	  JSONObject jsontoken = JSON.parseObject(resulttoken);
      String accesstoken = jsontoken.getJSONObject("custom").getString("accesstoken");
      
	  JSONObject jsonnew4 = new JSONObject();
  	  jsonnew4.put("vehicleId", dataBean.getVehicleid());
  	  jsonnew4.put("vehicleKind", dataBean.getVehiclekind());
  	  jsonnew4.put("vehicleNo", dataBean.getVehicleno());
  	  jsonnew4.put("vin", dataBean.getGcclsbdm());
  	  jsonnew4.put("model", dataBean.getModel());
  	  jsonnew4.put("length", dataBean.getcxlength());
  	  jsonnew4.put("width", dataBean.getWidth());
  	  jsonnew4.put("height", dataBean.getHeight());
  	  jsonnew4.put("weight", dataBean.getWeight());
  	  jsonnew4.put("driveImg", dataBean.getDriveimg());
  	  jsonnew4.put("axles", dataBean.getAxles());
  	  jsonnew4.put("tyles", dataBean.getTyles());
  	  jsonnew4.put("axes", dataBean.getAxes());
    	JSONObject json4 = new JSONObject();
        json4.put("detail", jsonnew4.toString());
        json4.put("accesstoken", accesstoken);
  	  String resultsign = TARequestUtil.sendPostInner(zwdturl + "updatebus", json4.toJSONString(), "", "");
  	  if (StringUtil.isNotBlank(resultsign)) {
            JSONObject results = JSONObject.parseObject(resultsign);
            JSONObject custom = results.getJSONObject("custom");
            if ("1".equals(custom.getString("code"))) {
          	  String desc = custom.getString("desc");
          	   service.update(dataBean);
               log.info("updatebus接口返回挂车编号："+desc);
               addCallbackParam("msg", "推送省平台成功！");
            }else {
            	addCallbackParam("msg", "推送省平台失败！");
            }
        }else {
        	addCallbackParam("msg", "推送省平台失败！");
        }
  	  
		
	}

	public CxBus getDataBean() {
		return dataBean;
	}

	public void setDataBean(CxBus dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getVehiclekindModel() {
		if (vehiclekindModel == null) {
			vehiclekindModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "超限车辆类型", null, false));
		}
		return this.vehiclekindModel;
	}

}
