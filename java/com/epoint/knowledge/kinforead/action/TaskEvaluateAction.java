package com.epoint.knowledge.kinforead.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.knowledge.common.domain.CnsKinfoEvl;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.kinforead.service.CnsKinfoEvlService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;

/**
 * 知识信息表详情页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("taskevaluateaction")
@Scope("request")
public class TaskEvaluateAction extends BaseController {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();
	private CnsKinfoEvlService kinfoEvlService = new CnsKinfoEvlService();
	private String evlresult = null;
	private String evlcontent = null;
	/**
	 * 知识信息表实体对象
	 */
	private AuditTask dataBean;

	public AuditTask getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditTask dataBean) {
		this.dataBean = dataBean;
	}

	@Autowired
	private IAuditTask taskService;
	/**
	 * 知识信息评价实体对象
	 */
	private CnsKinfoEvl cnskinfoEvl = null;
	private List<SelectItem> evlResultModel = null;
	/**
	 * 表格控件model
	 */
	private DataGridModel<CnsKinfoStep> model;

	private String guid;

	private String messageItemGuid;

	@Override
	public void pageLoad() {
		messageItemGuid = getRequestParameter("MessageItemGuid");
		if (StringUtil.isNotBlank(messageItemGuid)) {
			this.addCallbackParam("messageItem", true);
		}
		guid = getRequestParameter("guid");
		dataBean = taskService.getAuditTaskByGuid(guid, true).getResult();
		this.addCallbackParam("dataBean", dataBean);
		// 得到自己的评价人信息
		List<CnsKinfoEvl> cnsKinfoEvls = kinfoEvlService.getListByOneField("KGUID", guid, "EVLTIME", "DESC");
		this.addCallbackParam("cnskinfoEvlCount", cnsKinfoEvls.size());
		if (cnsKinfoEvls != null && cnsKinfoEvls.size() > 0) {
			this.addCallbackParam("cnskinfoEvlList", cnsKinfoEvls);
		} else {
			this.addCallbackParam("cnskinfoEvlList", "");
		}
		this.addCallbackParam("currentUserName", userSession.getDisplayName());
	}

	/**
	 * 
	 * [获取审核流程表格] [功能详细描述]
	 */
	public DataGridModel<CnsKinfoStep> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<CnsKinfoStep>() {
				@Override
				public List<CnsKinfoStep> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<CnsKinfoStep> list = kinfoStepService.getAllStepByKguid(guid);
					this.setRowCount(list.size());
					return list;
				}
			};
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getEvlResultModel() {
		if (evlResultModel == null) {
			evlResultModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "知识评价", null, true));
		}
		return evlResultModel;
	}

	public void setEvlResultModel(List<SelectItem> evlResultModel) {
		this.evlResultModel = evlResultModel;
	}

	public CnsKinfoEvl getCnskinfoEvl() {
		return cnskinfoEvl;
	}

	public void setCnskinfoEvl(CnsKinfoEvl cnskinfoEvl) {
		this.cnskinfoEvl = cnskinfoEvl;
	}

	public String getEvlresult() {
		return evlresult;
	}

	public void setEvlresult(String evlresult) {
		this.evlresult = evlresult;
	}

	public String getEvlcontent() {
		return evlcontent;
	}

	public void setEvlcontent(String evlcontent) {
		this.evlcontent = evlcontent;
	}

	public void addEvl() {
		cnskinfoEvl = new CnsKinfoEvl();
		if (StringUtil.isNotBlank(evlresult) && StringUtil.isNotBlank(evlcontent)) {
			cnskinfoEvl.setRowguid(UUID.randomUUID().toString());
			cnskinfoEvl.setEvltime(new Date());
			cnskinfoEvl.setKguid(guid);
			cnskinfoEvl.setEvlresult(evlresult);
			cnskinfoEvl.setEvlperson(userSession.getDisplayName());
			cnskinfoEvl.setEvlpersonguid(userSession.getUserGuid());
			cnskinfoEvl.setEvlcontent(evlcontent);
			cnskinfoEvl.setOuguid(userSession.getOuGuid());
			cnskinfoEvl.setOuname(userSession.getOuName());
			kinfoEvlService.addRecord(cnskinfoEvl);
		}
	}
}
