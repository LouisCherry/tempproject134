package com.epoint.xmz.certggxkws.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 公共许可卫生证照库list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-12 17:01:05]
 */
@RestController("certggxkwslistwithadvancedsearchaction")
@Scope("request")
public class CertGgxkwsListWithAdvancedSearchAction extends BaseController {
	@Autowired
	private ICertGgxkwsService service;

	/**
	 * 公共许可卫生证照库实体对象
	 */
	private CertGgxkws dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<CertGgxkws> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	/**
	 * 是否在用下拉列表model
	 */
	private List<SelectItem> is_enableModel = null;

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

	public DataGridModel<CertGgxkws> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<CertGgxkws>() {

				@Override
				public List<CertGgxkws> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					List<CertGgxkws> list = service.findList(
							ListGenerator.generateSql("cert_ggxkws", conditionSql, sortField, sortOrder), first,
							pageSize, conditionList.toArray());
					int count = service.countCertGgxkws(ListGenerator.generateSql("cert_ggxkws", conditionSql),
							conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public CertGgxkws getDataBean() {
		if (dataBean == null) {
			dataBean = new CertGgxkws();
		}
		return dataBean;
	}

	public void setDataBean(CertGgxkws dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel(
					"areacode,certno,certnum,certtype,creditcode,econtype,legal,manageaddress,monitorname,registeraddress",
					"所属区县,许可证号,证件号码,证件名称,社会信用代码,经济类型,法定代表人,经营地址,被监督单位,注册地址");
		}
		return exportModel;
	}

	public List<SelectItem> getIs_enableModel() {
		if (is_enableModel == null) {
			is_enableModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, true));
		}
		return this.is_enableModel;
	}

}
