package com.epoint.xmz.jnyjsevaluate.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;

/**
 * 一件事评价表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@RestController("jnyjsevaluatelistaction")
@Scope("request")
public class JnYjsEvaluateListAction extends BaseController {
	@Autowired
	private IJnYjsEvaluateService service;

	/**
	 * 一件事评价表实体对象
	 */
	private JnYjsEvaluate dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<JnYjsEvaluate> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	/**
	 * 辖区编码下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;

	public void pageLoad() {
	}

	/**
	 * 删除选定
	 * 
	 */
	public void deleteSelect() {
		List<String> select = getDataGridData().getSelectKeys();
		for (String sel : select) {
			service.deleteByGuid(sel);
		}
		addCallbackParam("msg", "成功删除！");
	}

	public DataGridModel<JnYjsEvaluate> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<JnYjsEvaluate>() {

				@Override
				public List<JnYjsEvaluate> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					
					String statisfation = getRequestParameter("statisfation");
					if (StringUtil.isNotBlank(statisfation)) {
						conditionSql += " and satisfaction ='" +statisfation+ "'";
					}
					
					
					String businessguid = getRequestParameter("businessguid");
					
					if (StringUtil.isNotBlank(businessguid)) {
						conditionSql += " and businessguid ='" +businessguid+ "'";
					}
					
					List<JnYjsEvaluate> list = service.findList(
							ListGenerator.generateSql("jn_yjs_evaluate", conditionSql, sortField, sortOrder), first,
							pageSize, conditionList.toArray());
					
					int count = service.countJnYjsEvaluate(ListGenerator.generateSql("jn_yjs_evaluate", conditionSql),
							conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public JnYjsEvaluate getDataBean() {
		if (dataBean == null) {
			dataBean = new JnYjsEvaluate();
		}
		return dataBean;
	}

	public void setDataBean(JnYjsEvaluate dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("", "");
		}
		return exportModel;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评辖区", null, true));
		}
		return this.areacodeModel;
	}

}
