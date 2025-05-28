package com.epoint.xmz.realestateinfo.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 楼盘信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@RestController("realestateinfolistwithadvancedsearchaction")
@Scope("request")
public class RealEstateInfoListWithAdvancedSearchAction extends BaseController {
	@Autowired
	private IRealEstateInfoService service;

	/**
	 * 楼盘信息表实体对象
	 */
	private RealEstateInfo dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<RealEstateInfo> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	/**
	 * 所属县区下拉列表model
	 */
	private List<SelectItem> belong_areaModel = null;

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

	public DataGridModel<RealEstateInfo> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<RealEstateInfo>() {

				@Override
				public List<RealEstateInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					List<RealEstateInfo> list = service.findList(
							ListGenerator.generateSql("real_estate_info", conditionSql, sortField, sortOrder), first,
							pageSize, conditionList.toArray());
					int count = service.countRealEstateInfo(ListGenerator.generateSql("real_estate_info", conditionSql),
							conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public RealEstateInfo getDataBean() {
		if (dataBean == null) {
			dataBean = new RealEstateInfo();
		}
		return dataBean;
	}

	public void setDataBean(RealEstateInfo dataBean) {
		this.dataBean = dataBean;
	}



	public List<SelectItem> getBelong_areaModel() {
		if (belong_areaModel == null) {
			belong_areaModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属区县", null, true));
		}
		return this.belong_areaModel;
	}

}
