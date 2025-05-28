
package com.epoint.evainstance.evainstance.action.cp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.audittask.dataassetsexport.api.IDataassetsExportService;
import com.epoint.basic.audittask.dataassetsexportdetail.api.IDataassetsExportDetailService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
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
@RestController("evainstancecplistaction")
@Scope("request")
public class EvainstanceCpListAction extends BaseController {
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
					String fields = "pf,rowguid,taskCode,taskName,projectNo,assessTime,orgName,acceptDate,userProp,userName,serviceNumber,areacode,mobile,pjqd,winName,winUserName,sfhf,sfhfang,khyj,khouname,ldps,ldmc"
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
						}else{
							sql.in("satisfaction","1,2");
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
						sql.in("satisfaction","1,2");

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



	/**
	 *  表格导出数据
	 *  @return
	 */
	public List<Evainstance> getExcelInfo() {
//		if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
//			List<String> oucode = monitorService.getOucodeByOuguid(leftTreeNodeGuid);
//			String oucodes = "'";
//			for (String code : oucode) {
//				if (StringUtil.isNotBlank(code)) {
//					oucodes += code + "','";
//				}
//			}
//			oucodes += "'";
//			param.set("oucodes", oucodes);
//		}

		//List<Record> pageData = monitorService.findProvinceTaskPage(param);
		List  pageData = service.getPageDate(dataBean);
		return pageData;
	}


	public void exportExcel(String serviceUrl){
		String filename = "";
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("Sheet1");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 11000);
		sheet.setColumnWidth(2, 9000);
		sheet.setColumnWidth(3, 9000);
		sheet.setColumnWidth(4, 9000);
		sheet.setColumnWidth(5, 9000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 5000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 5000);
		sheet.setColumnWidth(13, 5000);
		sheet.setColumnWidth(14, 5000);
		sheet.setColumnWidth(15, 5000);
		sheet.setColumnWidth(16, 5000);
		sheet.setColumnWidth(17, 5000);
		sheet.setColumnWidth(18, 5000);
		// 第三步，创建单元格，并设置值表头 设置表头居中
		// 设置字体 统一12号
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		// 样式2 只设置了字体
		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setFont(font);
		// 样式 折行，可编辑，字体
		style2.setBorderLeft(BorderStyle.THIN); // 设置单元格的边框为粗体
		style2.setLeftBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色.
		style2.setBorderRight(BorderStyle.THIN); // 设置单元格的边框为粗体
		style2.setRightBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style2.setBorderTop(BorderStyle.THIN); // 设置单元格的边框为粗体
		style2.setTopBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style2.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
		style2.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style2.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格的背景颜色．
		style2.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

		// 第四步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		// 第0行
		HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 30);
		font2.setBold(true);
		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setFont(font2);
		style4.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
		style4.setVerticalAlignment(VerticalAlignment.CENTER);
		style4.setBorderLeft(BorderStyle.THIN); // 设置单元格的边框为粗体
		style4.setLeftBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色.
		style4.setBorderRight(BorderStyle.THIN); // 设置单元格的边框为粗体
		style4.setRightBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style4.setBorderTop(BorderStyle.THIN); // 设置单元格的边框为粗体
		style4.setTopBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style4.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
		style4.setBottomBorderColor(IndexedColors.BLACK.index); // 设置单元格的边框颜色．
		style4.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格的背景颜色．

		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("好差评差评详情表");
		cell.setCellStyle(style4);
		row.setHeight((short) (20 * 60));
		/**
		 * 合并单元格
		 * 第一个参数：第一个单元格的行数（从0开始）
		 * 第二个参数：第二个单元格的行数（从0开始）
		 * 第三个参数：第一个单元格的列数（从0开始）
		 * 第四个参数：第二个单元格的列数（从0开始）
		 */
		CellRangeAddress range = new CellRangeAddress(0, 0, 0, 8);
		sheet.addMergedRegion(range);
		cell = row.createCell(1);
		cell.setCellStyle(style4);
		cell = row.createCell(2);
		cell.setCellStyle(style4);
		cell = row.createCell(3);
		cell.setCellStyle(style4);
		cell = row.createCell(4);
		cell.setCellStyle(style4);
		cell = row.createCell(5);
		cell.setCellStyle(style4);
		cell = row.createCell(6);
		cell.setCellStyle(style4);
		cell = row.createCell(7);
		cell.setCellStyle(style4);
		cell = row.createCell(8);
		cell.setCellStyle(style4);
		cell = row.createCell(9);
		cell.setCellStyle(style4);
		cell = row.createCell(10);
		cell.setCellStyle(style4);
		cell = row.createCell(11);
		cell.setCellStyle(style4);
		cell = row.createCell(12);
		cell.setCellStyle(style4);
		cell = row.createCell(13);
		cell.setCellStyle(style4);
		cell = row.createCell(14);
		cell.setCellStyle(style4);
		cell = row.createCell(15);
		cell.setCellStyle(style4);
		cell = row.createCell(16);
		cell.setCellStyle(style4);

		cell = row.createCell(17);
		cell.setCellStyle(style4);
		cell = row.createCell(18);
		cell.setCellStyle(style4);

		//第一行
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("序号");
		cell.setCellStyle(style2);
		cell = row.createCell(1);
		cell.setCellValue("姓名");
		cell.setCellStyle(style2);
		cell = row.createCell(2);
		cell.setCellValue("办件编号");
		cell.setCellStyle(style2);
		cell = row.createCell(3);
		cell.setCellValue("事项名称");
		cell.setCellStyle(style2);
		cell = row.createCell(4);
		cell.setCellValue("评价渠道");
		cell.setCellStyle(style2);
		cell = row.createCell(5);
		cell.setCellValue("满意度");
		cell.setCellStyle(style2);
		cell = row.createCell(6);
		cell.setCellValue("辖区编码");
		cell.setCellStyle(style2);
		cell = row.createCell(7);
		cell.setCellValue("联系方式");
		cell.setCellStyle(style2);
		cell = row.createCell(8);
		cell.setCellValue("评价窗口名称");
		cell.setCellStyle(style2);
		cell = row.createCell(9);
		cell.setCellValue("评价窗口编号");
		cell.setCellStyle(style2);
		cell = row.createCell(10);
		cell.setCellValue("是否回复");
		cell.setCellStyle(style2);
		cell = row.createCell(11);
		cell.setCellValue("是否回访");
		cell.setCellStyle(style2);
		cell = row.createCell(12);
		cell.setCellValue("拟办意见");
		cell.setCellStyle(style2);
		cell = row.createCell(13);
		cell.setCellValue("整改结果");
		cell.setCellStyle(style2);
		cell = row.createCell(14);
		cell.setCellValue("回复结果");
		cell.setCellStyle(style2);
		cell = row.createCell(15);
		cell.setCellValue("评价指标");
		cell.setCellStyle(style2);
		cell = row.createCell(16);
		cell.setCellValue("评价理由");
		cell.setCellStyle(style2);

		cell = row.createCell(17);
		cell.setCellValue("领导批示");
		cell.setCellStyle(style2);
		cell = row.createCell(18);
		cell.setCellValue("回访信息");
		cell.setCellStyle(style2);

		int i = 1;
		List<Evainstance> excelList = getExcelInfo();
		for (Evainstance item : excelList) {
			i++;
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(i - 1);
			cell.setCellStyle(style2);
			cell = row.createCell(1);
			cell.setCellValue(item.getUsername());
			cell.setCellStyle(style2);

			cell = row.createCell(2);
			cell.setCellValue(item.getProjectno());
			cell.setCellStyle(style2);

			cell = row.createCell(3);
			cell.setCellValue(item.getTaskname());
			cell.setCellStyle(style2);
			cell = row.createCell(4);
			String pf = item.getPf();
			switch (pf) {
				case "1":
					cell.setCellValue("书面表格");
					break;
				case "2":
					cell.setCellValue("山东政务服务网");
					break;
				case "3":
					cell.setCellValue("大厅二维码");
					break;
				case "4":
					cell.setCellValue("窗口评价器");
					break;
				case "7":
					cell.setCellValue("短信");
					break;
				case "5":
					cell.setCellValue("政务大厅其它终端");
					break;
				case "6":
					cell.setCellValue("电话");
					break;
				default:
					cell.setCellValue("小程序");
					break;
			}
			cell.setCellStyle(style2);
			cell = row.createCell(5);
			String satisfaction = item.getSatisfaction();
			switch (satisfaction) {
				case "5":
					cell.setCellValue("非常满意");
					break;
				case "4":
					cell.setCellValue("满意");
					break;
				case "3":
					cell.setCellValue("脚本不满意");
				case "2":
					cell.setCellValue("不满意");
					break;
				default:
					cell.setCellValue("非常不满意");
					break;
			}
			cell.setCellStyle(style2);

			cell = row.createCell(6);
			String areacode = item.getAreacode();
		//	cell.setCellValue(item.getAreacode());
			switch (areacode) {
				case "370900000000":
					cell.setCellValue("泰安市");
					break;
				case "370982000000":
					cell.setCellValue("新泰市");
					break;
				case "370911000000":
					cell.setCellValue("岱岳区");
					break;
				case "370983000000":
					cell.setCellValue("肥城市");
					break;
				case "370990000000":
					cell.setCellValue("高新区");
					break;
				case "370902000000":
					cell.setCellValue("泰山区");
					break;
				case "370921000000":
					cell.setCellValue("宁阳县");
					break;
				case "370923000000":
					cell.setCellValue("东平县");
					break;
				default:
					cell.setCellValue("粥店街道");
					break;
			}
			cell.setCellStyle(style2);
			cell = row.createCell(7);
			cell.setCellValue(item.getMobile());
			cell.setCellStyle(style2);
			cell = row.createCell(8);
			cell.setCellValue(item.getWinusername());
			cell.setCellStyle(style2);
			cell = row.createCell(9);
			cell.setCellValue(item.getWinname());
			cell.setCellStyle(style2);

			cell = row.createCell(10);
			String sfhf = item.getInt("sfhf")+"";
			if (StringUtil.isBlank(sfhf)) {
				cell.setCellValue("无");
			} else {
				switch (sfhf) {
					case "0":
						cell.setCellValue("未回复");
						break;
					case "1":
						cell.setCellValue("已回复");
						break;
					default:
						cell.setCellValue("无");
						break;
				}
			}
			cell.setCellStyle(style2);


			cell = row.createCell(11);
			String sfhfang = item.getInt("sfhfang")+"";
			if (StringUtil.isBlank(sfhfang)) {
				cell.setCellValue("无");
			} else {
				switch (sfhf) {
					case "0":
						cell.setCellValue("未回复");
						break;
					case "1":
						cell.setCellValue("已回复");
						break;
					default:
						cell.setCellValue("无");
						break;
				}
			}
			cell.setCellStyle(style2);


			cell = row.createCell(12);
			cell.setCellValue(item.getNbyj());
			cell.setCellStyle(style2);

			cell = row.createCell(13);
			cell.setCellValue(item.getZgjg());
			cell.setCellStyle(style2);

			cell = row.createCell(14);
			cell.setCellValue(item.getHfjg());
			cell.setCellStyle(style2);


			cell = row.createCell(15);
			String evaldetail = item.getEvaldetail();
			String evaldetailArr = "";
			if (StringUtil.isNotBlank(evaldetail)) {
				String[] strs = evaldetail.split(",");
				String evDatail = "";
				for (int k = 0, len = strs.length; k < len; k++) {

					Record evRecord = iDataassetsExportDetailService.getEvaDetail(strs[k].toString());
					if (StringUtil.isNotBlank(evRecord)) {
						evDatail = evRecord.getStr("itemtext");
						evaldetailArr = String.join(",", evDatail, evaldetailArr);
					}
				}
				if (StringUtil.isNotBlank(evaldetailArr)) {
					evaldetailArr = evaldetailArr.substring(0, evaldetailArr.length() - 1);
				}
				cell.setCellValue(evaldetailArr);
			} else {
				cell.setCellValue("无");
			}
			cell.setCellStyle(style2);

			cell = row.createCell(16);
			cell.setCellValue(item.getWritingevaluation());
			cell.setCellStyle(style2);

			cell = row.createCell(17);
			cell.setCellValue(item.getLdps());
			cell.setCellStyle(style2);
			cell = row.createCell(18);
			cell.setCellValue(item.getHfxx());
			cell.setCellStyle(style2);
		}

		FileOutputStream fout = null;

		try {
			String path = "";
			String FILE_SEPARATOR = System.getProperty("file.separator");
			String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String projectName = request.getServletContext().getContextPath();
			int num = t.indexOf(projectName);
			String filePath = ClassPathUtil.getDeployWarPath() +"export/";
			//获取项目路径
			File directory = new File("");// 参数为空
			String courseFile = directory.getCanonicalPath();
			if (serviceUrl.contains("localhost")) {
				//本地环境获取项目物理路径
				//path = t.substring(1, num).replace('/', '\\') + "\\nxzwfw\\src\\main\\webapp\\export";
				path = courseFile.replace('/', '\\') + "\\src\\main\\webapp\\export";
				////system.out.println(path);
			}
			else {
				//服务器环境获取项目物理路径
				path = (courseFile + projectName + FILE_SEPARATOR + "export").replace("/", FILE_SEPARATOR).replace("\\",
						FILE_SEPARATOR);

				path = (t.substring(0, num) + projectName + FILE_SEPARATOR + "export").replace("/", FILE_SEPARATOR)
						.replace("\\", FILE_SEPARATOR);
			}

			if (StringUtil.isNotBlank(filePath)) {
				File f = new File(filePath);
				// 创建文件夹
				if (!f.exists()) {
					f.mkdirs();
				}
				filename = "hcp" + EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss") + ".xls";
				fout = new FileOutputStream(filePath + FILE_SEPARATOR + filename);
				wb.write(fout);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				fout.close();
				wb.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		addCallbackParam("filename", filename);
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
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评差评满意度", null, true));
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
