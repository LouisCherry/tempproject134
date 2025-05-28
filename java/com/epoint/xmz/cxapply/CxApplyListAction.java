package com.epoint.xmz.cxapply;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditresource.cert.action.TARequestUtil;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 车辆信息表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@RestController("cxapplylistaction")
@Scope("request")
public class CxApplyListAction extends BaseController {
	
	public static final String zwdturl= ConfigUtil.getConfigValue("zwdtparam", "zwdturl")+"/rest/jncxcltxzdj/";
	
	@Autowired
	private ICxBusService service;
	
	@Autowired
	private ICodeItemsService codeItemsService;
	
	@Autowired
	private IAuditProject iAuditProject;
	
	private String cxcode;
	
	private String flowsn;
	
	private String applyername;

	/**
	 * 车辆信息表实体对象
	 */
	private AuditProject dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditProject> model;

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

	public DataGridModel<AuditProject> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditProject>() {

				@Override
				public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
//					sortField = "applydate";
	                SqlConditionUtil sql = new SqlConditionUtil();
	                if (StringUtil.isNotBlank(cxcode)) {
	                	sql.eq("cxcode", cxcode);
	                }
	                if (StringUtil.isNotBlank(flowsn)) {
	                	sql.eq("flowsn", flowsn);
	                }
	                if (StringUtil.isNotBlank(applyername)) {
	                	sql.like("applyername", applyername);
	                }
	                
	                sql.eq("status", "90");
	                sql.isNotBlank("cxcode");
	                sql.isNotBlank("remark");
	                
                    String fields = "rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,tasktype,flowsn,applydate";
					
                    PageData<AuditProject>  pageProject = iAuditProject
                            .getAuditProjectPageData(fields,sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
					this.setRowCount(pageProject.getRowCount());
					return pageProject.getList();
				}

			};
		}
		return model;
	}
	
	public void cancel(String rowguid) {
		AuditProject project = iAuditProject.getAuditProjectByRowGuid(rowguid, null).getResult();
		if (project != null) {
			String resulttoken = TARequestUtil.sendPostInner(zwdturl + "getaccesstoken", "{}", "", "");
			JSONObject jsontoken = JSON.parseObject(resulttoken);
			String accesstoken = jsontoken.getJSONObject("custom").getString("accesstoken");
			String cxtxz = project.getStr("dataObj_baseinfo");
			String remark = project.getRemark();
			if (StringUtil.isNotBlank(cxtxz) && StringUtil.isNotBlank(remark)) {

				log.info("开始进行申请单注销，rowguid:"+rowguid);
				
				JSONObject json = JSON.parseObject(cxtxz);
				// 道路运输经营许可证号
				String dlysjyxkzh = json.getString("dlysjyxkzh");
				// 经办人姓名
				String jbrxm = json.getString("jbrxm");
				// 经办人身份证号
				String jbrsfzh = json.getString("jbrsfzh");
				// 经办人性别
				String jbrxb = json.getString("jbrxb");
				// 经办人电话
				String jbrdh = json.getString("jbrdh");
				// 行驶时间开始日期
				Date xssjksrq = EpointDateUtil.convertString2Date(json.getString("xssjksrq"), "yyyy-MM-dd");
				// 行驶时间结束时间
				Date xssjjssj = EpointDateUtil.convertString2Date(json.getString("xssjjssj"), "yyyy-MM-dd");
				// 出发地
				String cfd = json.getString("cfd");
				// 途径地
				String tjd = json.getString("tjd");
				// 目的地
				String mdd = json.getString("mdd");
				// 通行路线
				String txlx = json.getString("txlx");
				// 货物类型名称
				String hwlxmc = json.getString("hwlxmc");
				// 货物名称
				String hwmc = json.getString("hwmc");
				// 货物重量
				String hwzl = json.getString("hwzl");
				// 货物长度
				String hwcd = json.getString("hwcd");
				// 货物最大宽
				String hwzdk = json.getString("hwzdk");
				// 货物最大高
				String whzdg = json.getString("whzdg");
				// 车货最大长
				String chzdc = json.getString("chzdc");
				// 车货最大高
				String chzdg = json.getString("chzdg");
				// 车货最大宽
				String chzdk = json.getString("chzdk");
				// 轴荷分布
				String zhfb = json.getString("zhfb");
				// 轮胎数
				String lts = json.getString("lts");
				// 轴距
				String zj = json.getString("zj");
				// 车货总重量
		    	  String chzzl = json.getString("chzzl");
				// 授权委托书图片
				String wts_img = "无";
				// 经营许可证主键图片
				String org_cert_img = "无";
				// 车货总体轮廓图主键
				String total_img = "无";
				// 申请人身份证照片主键
				String agent_id_img = "无";
				String applyguid = "";
				String qlcguid = "";
				String gcguid = "";

				String[] remarks = null;
				if (remark.contains(";")) {
					remarks = remark.split(";");
					applyguid = remarks[0];
					qlcguid = remarks[1];
					gcguid = remarks[2];
				}

				JSONObject jsonnew2 = new JSONObject();
				jsonnew2.put("requestId", applyguid);
				jsonnew2.put("reqNo", project.getStr("cxcode"));
				jsonnew2.put("reqStatus", "53");
				jsonnew2.put("deptId", project.getAreacode());
				jsonnew2.put("startDate", EpointDateUtil.convertDate2String(xssjksrq, "yyyy-MM-dd"));
				jsonnew2.put("endDate", EpointDateUtil.convertDate2String(xssjjssj, "yyyy-MM-dd"));
				jsonnew2.put("beginDist", cfd);
				jsonnew2.put("passDists", cfd + "," + tjd.replace("[", "").replace("]", "").replace("\"", "") + "," + mdd);
				jsonnew2.put("endDist", mdd);
				jsonnew2.put("roads", txlx);
				jsonnew2.put("cargoCate", hwlxmc);
				jsonnew2.put("cargoType", codeItemsService.getItemTextByCodeName("货物类型", hwlxmc));
				jsonnew2.put("cargoInfo", hwmc);
				jsonnew2.put("cargoWeight", hwzl);
				jsonnew2.put("cargoLength", hwcd);
				jsonnew2.put("cargoWidth", hwzdk);
				jsonnew2.put("cargoHeight", whzdg);
				jsonnew2.put("totalWeight", chzzl);
				jsonnew2.put("totalLength", chzdc);
				jsonnew2.put("totalWidth", chzdk);
				jsonnew2.put("totalHeight", chzdg);
				jsonnew2.put("tractorNo", qlcguid);
				jsonnew2.put("trailerNo", gcguid);
				String[] strs = zhfb.split("\\+");
				int total = 0;
				for (String s : strs) {
					String count = s.substring(0, 1);
					if (s.contains("*")) {
						total += Integer.parseInt(count) * 2;
					} else {
						total += Integer.parseInt(count);
					}
				}
				jsonnew2.put("axles", total);
				jsonnew2.put("tyles", lts);
				jsonnew2.put("wheelbases", zj);
				jsonnew2.put("axlesLoad", zhfb);
				jsonnew2.put("totalImg", total_img);
				jsonnew2.put("orgCertNo", dlysjyxkzh);
				jsonnew2.put("orgCertImg", org_cert_img);
				jsonnew2.put("applicantName", project.getApplyername());
				jsonnew2.put("creditCode", project.getCertnum());
				jsonnew2.put("proxyId", applyguid);
				jsonnew2.put("agentName", jbrxm);
				if ("男".equals(jbrxb)) {
					jsonnew2.put("agentSex", 1);
				}else if("女".equals(jbrxb)){
					jsonnew2.put("agentSex", 2);
				}else {
					jsonnew2.put("agentSex", jbrxb);
				}
				
				jsonnew2.put("agentId", jbrsfzh);
				jsonnew2.put("agentIdImg", agent_id_img);
				jsonnew2.put("agentTel", jbrdh);
				jsonnew2.put("authorizeImg", wts_img);
				jsonnew2.put("category", "2");
				jsonnew2.put("planDoc", "无");

				JSONObject json4 = new JSONObject();
				json4.put("detail", jsonnew2.toString());
				json4.put("accesstoken", accesstoken);
				String resultsign = TARequestUtil.sendPostInner(zwdturl + "updateapply", json4.toJSONString(), "", "");
				log.info("超限注销详情："+resultsign);
				if (StringUtil.isNotBlank(resultsign)) {
					JSONObject results = JSONObject.parseObject(resultsign);
					JSONObject custom = results.getJSONObject("custom");
					if ("1".equals(custom.getString("code"))) {
						String desc = custom.getString("desc");
						log.info("updatebus接口返回挂车编号：" + desc);
						addCallbackParam("msg", "注销成功！");
					} else {
						addCallbackParam("msg", "注销失败！");
					}
				} else {
					addCallbackParam("msg", "注销失败！");
				}
			}
		}
	}
	
	public String getCxcode() {
		return cxcode;
	}

	public void setCxcode(String cxcode) {
		this.cxcode = cxcode;
	}

	public String getFlowsn() {
		return flowsn;
	}

	public void setFlowsn(String flowsn) {
		this.flowsn = flowsn;
	}

	public AuditProject getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditProject();
		}
		return dataBean;
	}

	public void setDataBean(AuditProject dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("", "");
		}
		return exportModel;
	}

	public String getApplyername() {
		return applyername;
	}

	public void setApplyername(String applyername) {
		this.applyername = applyername;
	}
	
	

}
