package com.epoint.xmz.cxbus.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.cxbus.api.entity.CxBus;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 车辆信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@RestController("cxbuslistaction")
@Scope("request")
public class CxBusListAction extends BaseController {
	@Autowired
	private ICxBusService service;

	/**
	 * 车辆信息表实体对象
	 */
	private CxBus dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<CxBus> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

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

	public DataGridModel<CxBus> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<CxBus>() {

				@Override
				public List<CxBus> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					List<CxBus> list = service.findList(
							ListGenerator.generateSql("cx_bus", conditionSql, sortField, sortOrder), first, pageSize,
							conditionList.toArray());
					int count = service.countCxBus(ListGenerator.generateSql("cx_bus", conditionSql),
							conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public CxBus getDataBean() {
		if (dataBean == null) {
			dataBean = new CxBus();
		}
		return dataBean;
	}

	public void setDataBean(CxBus dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("", "");
		}
		return exportModel;
	}

}
