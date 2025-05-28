package com.epoint.xmz.zjknowledge.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjknowledge.api.entity.ZjKnowledge;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.zjknowledge.api.IZjKnowledgeService;

/**
 * 自建系统知识库表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-25 15:12:09]
 */
@RestController("zjknowledgelistaction")
@Scope("request")
public class ZjKnowledgeListAction extends BaseController {
	@Autowired
	private IZjKnowledgeService service;

	/**
	 * 自建系统知识库表实体对象
	 */
	private ZjKnowledge dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<ZjKnowledge> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	/**
	 * 问题类型下拉列表model
	 */
	private List<SelectItem> typeModel = null;

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

	public DataGridModel<ZjKnowledge> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<ZjKnowledge>() {

				@Override
				public List<ZjKnowledge> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					List<ZjKnowledge> list = service.findList(
							ListGenerator.generateSql("zj_knowledge", conditionSql, sortField, sortOrder), first,
							pageSize, conditionList.toArray());
					int count = service.countZjKnowledge(ListGenerator.generateSql("zj_knowledge", conditionSql),
							conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public ZjKnowledge getDataBean() {
		if (dataBean == null) {
			dataBean = new ZjKnowledge();
		}
		return dataBean;
	}

	public void setDataBean(ZjKnowledge dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("keyword,ouname,taskname,title,type", "关键字,所属部门,关联事项名称,问题标题,问题类型");
		}
		return exportModel;
	}

	public List<SelectItem> getTypeModel() {
		if (typeModel == null) {
			typeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "自建问题类型", null, true));
		}
		return this.typeModel;
	}

}
