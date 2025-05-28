package com.epoint.takan.kanyanmain.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 勘验主表详情页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RightRelation(KanyanmainListAction.class)
@RestController("liangshankanyanmaindetailaction")
@Scope("request")
public class LiangshanKanyanmainDetailAction extends BaseController
{
	private Record dataBean;

	@Autowired
	private IAuditProject iAuditProject;

	@Autowired
	private IAuditTaskExtension iAuditTaskExtension;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
		String materialcliengguid = UUID.randomUUID().toString();
		String liangshantakanurl = ConfigUtil.getConfigValue("xmzArgs", "liangshantakanurl");
		JSONObject dataJson = new JSONObject();
		JSONObject params = new JSONObject();
		params.put("rowguid", guid);
		dataJson.put("token", "Epoint_WebSerivce_**##0601");
		dataJson.put("params", params);
		String result = HttpUtil.doPostJson(liangshantakanurl+"/rest/takan/result",
				dataJson.toString());
		log.info("result返回结果：" + result);
		if(result!=null){
			JSONObject returnobject = JSONObject.parseObject(result);
			JSONObject custom = returnobject.getJSONObject("custom");
			String code = custom.getString("code");
			if("1".equals(code)){
				dataBean = new Record();
				dataBean.putAll(custom);
				addCallbackParam("applyattachguid",custom.getString("applyattachguid"));
				addCallbackParam("surveyattachguid",custom.getString("surveyattachguid"));
				addCallbackParam("formid",custom.getString("formid"));
			}
		}
    }

	public Record getDataBean()
	      {
	          return dataBean;
	      }
}