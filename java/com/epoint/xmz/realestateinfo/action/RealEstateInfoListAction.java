package com.epoint.xmz.realestateinfo.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.realestateinfo.api.IRealEstateInfoService;

/**
 * 楼盘信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
@RestController("realestateinfolistaction")
@Scope("request")
public class RealEstateInfoListAction extends BaseController {
	@Autowired
	private IRealEstateInfoService service;

	/**
	 * 楼盘信息表实体对象
	 */
	private RealEstateInfo dataBean;

	@Autowired
	private IOuServiceInternal ouServiceInternal;

	/**
	 * 表格控件model
	 */
	private DataGridModel<RealEstateInfo> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;
	
	private String re_name;
	
	private String dev_unit;
	
	private String belong_area;

	private String housetype;

	/**
	 * 所属县区下拉列表model
	 */
	private List<SelectItem> belong_areaModel = null;

	/**
	 * 房屋类型下拉列表model
	 */
	private List<SelectItem> housetypeModel = null;

	public void pageLoad() {
		String fmtcliengguid = UUID.randomUUID().toString();
        String nktcliengguid =  UUID.randomUUID().toString();
        String pmtcliengguid =  UUID.randomUUID().toString();
        String qatcliengguid =  UUID.randomUUID().toString();
        String Lurccliengguid =  UUID.randomUUID().toString();
        String Permitcliengguid =  UUID.randomUUID().toString();
        String ProjectPermitcliengguid =  UUID.randomUUID().toString();
        String ProjectConPermitcliengguid =  UUID.randomUUID().toString();
        String PreSalePermitcliengguid =  UUID.randomUUID().toString();
        String Pcpvccliengguid =  UUID.randomUUID().toString();
        String Fpccliengguid =  UUID.randomUUID().toString();
        String Carccliengguid =  UUID.randomUUID().toString();
        addCallbackParam("fmtcliengguid", fmtcliengguid);
        addCallbackParam("nktcliengguid", nktcliengguid);
        addCallbackParam("pmtcliengguid", pmtcliengguid);
        addCallbackParam("qatcliengguid", qatcliengguid);
        addCallbackParam("Lurccliengguid", Lurccliengguid);
        addCallbackParam("Permitcliengguid", Permitcliengguid);
        addCallbackParam("ProjectPermitcliengguid", ProjectPermitcliengguid);
        addCallbackParam("ProjectConPermitcliengguid", ProjectConPermitcliengguid);
        addCallbackParam("PreSalePermitcliengguid", PreSalePermitcliengguid);
        addCallbackParam("Pcpvccliengguid", Pcpvccliengguid);
        addCallbackParam("Fpccliengguid", Fpccliengguid);
        addCallbackParam("Carccliengguid", Carccliengguid);
        
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
					if (StringUtil.isNotBlank(re_name)) {
						conditionSql += " and re_name like '%"+re_name+"%'";
					}
					if (StringUtil.isNotBlank(dev_unit)) {
						conditionSql += " and dev_unit like '%"+dev_unit+"%'";
					}
					String ouGuid = userSession.getOuGuid();
					FrameOuExtendInfo frameOuExtendInfo = ouServiceInternal.getFrameOuExtendInfo(ouGuid);
					if(StringUtil.isNotBlank(frameOuExtendInfo.getStr("AREACODE"))){
						belong_area = frameOuExtendInfo.getStr("AREACODE");
					}
					else {
						// 避免查询到所有数据
						belong_area = UUID.randomUUID().toString();
					}
					if (StringUtil.isNotBlank(belong_area)) {
						conditionSql += " and belong_area = '"+belong_area+"'";
					}
					if (StringUtil.isNotBlank(housetype)) {
						conditionSql += " and housetype = '"+housetype+"'";
					}
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

	public List<SelectItem> getHousetypeModel() {
		if (housetypeModel == null) {
			housetypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "房屋类型", null, false));
		}
		return this.housetypeModel;
	}

	public String getRe_name() {
		return re_name;
	}

	public void setRe_name(String re_name) {
		this.re_name = re_name;
	}

	public String getDev_unit() {
		return dev_unit;
	}

	public void setDev_unit(String dev_unit) {
		this.dev_unit = dev_unit;
	}

	public String getBelong_area() {
		return belong_area;
	}

	public void setBelong_area(String belong_area) {
		this.belong_area = belong_area;
	}

	public String getHousetype() {
		return housetype;
	}

	public void setHousetype(String housetype) {
		this.housetype = housetype;
	}
}
