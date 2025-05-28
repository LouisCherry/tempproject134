
package com.epoint.evainstance.evainstance.action.ps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.audittask.dataassetsexport.api.IDataassetsExportService;
import com.epoint.basic.audittask.dataassetsexport.api.entity.DataassetsExport;
import com.epoint.basic.audittask.dataassetsexportdetail.api.IDataassetsExportDetailService;
import com.epoint.basic.audittask.dataassetsexportdetail.api.entity.DataassetsExportDetail;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ProjectConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.IEvainstanceService;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 评价表list页面对应的后台
 * 
 * @author lizhenjie
 * @version [版本号, 2020-09-06 14:00:35]
 */
@RestController("evainstancepslistaction")
@Scope("request")
public class EvainstancePsListAction extends BaseController {
	@Autowired
	private IEvainstanceService service;
	// 事项导出导出service
	@Autowired
	private IDataassetsExportService iDataassetsExportService;
	@Autowired
	private ISendMQMessage sendMQMessageService;
	@Autowired
	private IDataassetsExportDetailService iDataassetsExportDetailService;

	/**
	 * 评价表实体对象
	 */
	private Evainstance dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<Evainstance> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	/**
	 * 所属辖区下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;
	/**
	 * 评价渠道下拉列表model
	 */
	private List<SelectItem> pfModel = null;
	/**
	 * 满意度下拉列表model
	 */
	private List<SelectItem> satisfactionModel = null;
	/**
	 * 是否回复单选按钮组model
	 */
	private List<SelectItem> sfhfModel = null;
	/**
	 * 是否回访单选按钮组model
	 */
	private List<SelectItem> sfhfangModel = null;

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

	public DataGridModel<Evainstance> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<Evainstance>() {
				@Override
				public List<Evainstance> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					String fields = "pf,rowguid,taskCode,taskName,projectNo,acceptDate,userProp,userName,assessNumber,areacode,mobile,sfhf,sfhfang,khyj,khouname,ldps,ldmc"
							+ ",evalDetail,satisfaction";
					if (!dataBean.isEmpty()) {
						if (!dataBean.getAreacode().isEmpty()) {
							sql.eq("areacode", dataBean.getAreacode());
						}
						if (!dataBean.getPf().isEmpty()) {
							sql.eq("pf", dataBean.getPf());
						}
						if (!dataBean.getSatisfaction().isEmpty()) {
							sql.eq("satisfaction", dataBean.getSatisfaction());
						}
						if (!dataBean.getSfhf().isEmpty()) {
							sql.eq("sfhf", dataBean.getSfhf());
						}
						if (!dataBean.getSfhfang().isEmpty()) {
							sql.eq("sfhfang", dataBean.getSfhfang());
						}
						if (dataBean.get("startDate") != null && dataBean.get("startDate") != "") {
							sql.ge("assessTime", "'" + dataBean.get("startDate") + "'");
						}
						////system.out.println(dataBean.get("startDate")+"_"+EpointDateUtil.convertDate2String(dataBean.getDate("endDate"), "yyyy-MM-dd"));
						if (dataBean.get("endDate") != null && dataBean.get("endDate") != "") {
							String endDate = EpointDateUtil.convertDate2String(dataBean.getDate("endDate"), "yyyy-MM-dd")+" 59:59:59";
							////system.out.println(endDate);
							sql.le("assessTime", "'" + endDate + "'");
							addCallbackParam("startDate", dataBean.get("startDate"));
							addCallbackParam("endDate", dataBean.get("endDate"));
							addCallbackParam("type", true);
						}
					} else {
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DATE, -7);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = cal.getTime();
						sql.ge("assessTime", "'" + sdf.format(date) + "'");
						sql.le("assessTime",
								"'" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss") + "'");

					}
					if (StringUtil.isBlank(sortField)) {
						sortField = "acceptdate";
					}
					PageData<Evainstance> pageProject = service.getEvaluateservicePageData(fields, sql.getMap(), first,
							pageSize, sortField, sortOrder, UserSession.getInstance().getUserGuid()).getResult();
					List<Evainstance> list = new ArrayList<>();
					list = pageProject.getList();
					int count = pageProject.getRowCount();
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;

	}

	public void exportEvainstance() {
		try {

			EpointFrameDsManager.begin(null);
			String flowsnIndex = "dsx"
					+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_NOSPLIT_FORMAT);
			int flowsn = iDataassetsExportService.getFlowsnNumByDate(flowsnIndex);

			// 查询导出天数
			int diffDays = new Long(diffDays(dataBean.get("startDate"), dataBean.get("endDate"))).intValue();
			// 生成一个导出记录
			DataassetsExport dataassetsExport = new DataassetsExport();
			dataassetsExport.setRowguid(UUID.randomUUID().toString());
			dataassetsExport.setCreatedate(new Date());
			dataassetsExport.setExportname(EpointDateUtil.convertDate2String(dataBean.getDate("startDate"), "yyyy-MM-dd") + "至" +EpointDateUtil.convertDate2String(dataBean.getDate("endDate"), "yyyy-MM-dd")   + "好差评数据导出");
			dataassetsExport.setExportuserguid(userSession.getUserGuid());
			dataassetsExport.setExportusername(userSession.getDisplayName());
			dataassetsExport.setFlowsn("hcp" + System.currentTimeMillis());
			dataassetsExport.setType(ProjectConstant.SJZCLX_DSX);
			dataassetsExport.setStatus(ProjectConstant.SJZT_DAIDAOCHU);
			dataassetsExport.setPassword(UUID.randomUUID().toString());
			dataassetsExport.setProjecttype(ProjectConstant.DCGCLX_XMZ);
			iDataassetsExportService.insert(dataassetsExport);
		
			for (int i = 0; i < diffDays; i++) {
				// 每条导出记录，都有一个导出详情
				DataassetsExportDetail dataassetsExportDetail = new DataassetsExportDetail();
				dataassetsExportDetail.setRowguid(UUID.randomUUID().toString());
				dataassetsExportDetail.setOperatedate(new Date());
				dataassetsExportDetail.setExportguid(dataassetsExport.getRowguid());

				// String startDate = "";
				// String endDate = "";
				Date startDate = dataBean.getDate("startDate");
				Date endDate = null;

				for (i = 0; i < diffDays; i++) {
					Calendar calendar = new GregorianCalendar();
					Calendar calendar1 = new GregorianCalendar();
					calendar.setTime(startDate);
					calendar.add(calendar.DATE, 1); // 把日期往后增加一天,整数 往后推,负数往前移动
					endDate = calendar.getTime();
					String condition = "";
					if (!dataBean.getAreacode().isEmpty()) {
						condition += " and  areacode=" + dataBean.getAreacode();
					}
					if (!dataBean.getPf().isEmpty()) {
						condition += " and  pf=" + dataBean.getPf();
					}
					if (!dataBean.getSatisfaction().isEmpty()) {
						condition += " and  satisfaction=" + dataBean.getSatisfaction();
					}
					if (!dataBean.getSfhf().isEmpty()) {
						condition += " and  sfhf=" + dataBean.getSfhf();
					}
					if (!dataBean.getSfhfang().isEmpty()) {
						condition += " and  sfhfang=" + dataBean.getSfhfang();
					}
					condition += " and  assessTime <" +"'"+ EpointDateUtil.convertDate2String(endDate, "yyyy-MM-dd")+"'"
							+ " and assessTime >=" +"'"+ EpointDateUtil.convertDate2String(startDate, "yyyy-MM-dd")+"' ";
					dataassetsExportDetail.setRowguid(UUID.randomUUID().toString());
					dataassetsExportDetail.setYewguid(condition);
					dataassetsExportDetail.setOuguid(userSession.getOuGuid());
					dataassetsExportDetail.setOuname(userSession.getOuName());
					dataassetsExportDetail.setType(ProjectConstant.SJZCLX_DSX);
					dataassetsExportDetail.setStatus(ProjectConstant.SJZT_DAIDAOCHU);
					dataassetsExportDetail.setProjecttype(ProjectConstant.DCGCLX_XMZ);
					dataassetsExportDetail.setYename(EpointDateUtil.convertDate2String(startDate, "yyyy-MM-dd") + "至"
							+ EpointDateUtil.convertDate2String(endDate, "yyyy-MM-dd") + "好差评数据导出");
					iDataassetsExportDetailService.insert(dataassetsExportDetail);
					calendar1.setTime(startDate);
					calendar1.add(calendar1.DATE, 1);
					startDate = calendar1.getTime();
				}

			}
			// 插入mq队列
			sendMQMessageService.sendByExchange("exchange_handle", dataassetsExport.getRowguid(),
					"exprot." + ProjectConstant.SJZCLX_DSX + "." + dataassetsExport.getRowguid());
			addCallbackParam("msg", "评价信息导出中，请关注导出完成消息提醒");
			EpointFrameDsManager.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			addCallbackParam("msg", "导出失败，" + e.getMessage());
			EpointFrameDsManager.rollback();
		} finally {
			EpointFrameDsManager.close();
		}
	}

	public long diffDays(String dateStart, String dateStop) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// 毫秒ms
			long diff = d2.getTime() - d1.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			return diffDays;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private String getWZFlowsn(int i) throws Exception {
		if (i < 10) {
			return "00" + i;
		} else if (i < 100) {
			return "0" + i;
		} else if (i < 1000) {
			return String.valueOf(i);
		} else {
			throw new Exception("当前流水号已超过当天处理数据的最大值，无法生成新的可用流水号");
		}
	}

	public Evainstance getDataBean() {
		if (dataBean == null) {
			dataBean = new Evainstance();
		}
		return dataBean;
	}

	public void setDataBean(Evainstance dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel(
					"projectno,areacode,taskname,username,pf,satisfaction,winname,winusername,mobile,sfhf,sfhfang,nbyj,zgjg,hfjg,writingEvaluation,evaldetail",
					"办件编号,辖区编码,事项名称,评价人员,评价渠道,满意度,窗口名称,窗口编号,联系方式,是否回复,是否回访,拟办意见,整改结果,回复结果,评价理由,评价指标");
			exportModel.addColumnWidth("projectno", 5000);
			exportModel.addColumnWidth("areacode", 5000);
			exportModel.addColumnWidth("taskname", 5000);

			exportModel.addColumnWidth("username", 5000);
			exportModel.addColumnWidth("pf", 5000);
			exportModel.addColumnWidth("satisfaction", 5000);

			exportModel.addColumnWidth("winname", 5000);
			exportModel.addColumnWidth("winusername", 5000);

			exportModel.addColumnWidth("mobile", 5000);
			exportModel.addColumnWidth("sfhf", 5000);
			exportModel.addColumnWidth("sfhfang", 5000);
			exportModel.addColumnWidth("nbyj", 5000);

			exportModel.addColumnWidth("zgjg", 5000);
			exportModel.addColumnWidth("hfjg", 5000);

			exportModel.addColumnWidth("writingEvaluation", 5000);
			exportModel.addColumnWidth("evaldetail", 5000);
		}
		return exportModel;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区", null, true));
		}
		return this.areacodeModel;
	}

	public List<SelectItem> getPfModel() {
		if (pfModel == null) {
			pfModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "评价渠道", null, true));
		}
		return this.pfModel;
	}

	public List<SelectItem> getSatisfactionModel() {
		if (satisfactionModel == null) {
			satisfactionModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评满意度层级", null, true));
		}
		return this.satisfactionModel;
	}

	public List<SelectItem> getSfhfModel() {
		if (sfhfModel == null) {
			sfhfModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回复", null, false));
		}
		return this.sfhfModel;
	}

	public List<SelectItem> getSfhfangModel() {
		if (sfhfangModel == null) {
			sfhfangModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否回访", null, false));
		}
		return this.sfhfangModel;
	}

}
