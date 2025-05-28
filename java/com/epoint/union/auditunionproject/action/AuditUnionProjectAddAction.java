package com.epoint.union.auditunionproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.epoint.union.audituniontask.api.entity.AuditUnionTask;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyRegister;
import com.epoint.basic.auditresource.individual.domain.AuditRsIndividualBaseinfo;
import com.epoint.basic.auditresource.individual.inter.IAuditIndividual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;

/**
 * 异地通办办件信息表新增页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@RightRelation(AuditUnionProjectListAction.class)
@RestController("auditunionprojectaddaction")
@Scope("request")
public class AuditUnionProjectAddAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditUnionProjectService service;
	@Autowired
	private IAuditUnionProjectMaterialService auditUnionProjectMaterialService;
    @Autowired
    private IAuditRsCompanyBaseinfo companyService;
    @Autowired
    private IAuditIndividual individualService;
    @Autowired
    private IAuditRsCompanyLegal iAuditRsCompanyLegal;

    @Autowired
    private IAuditRsCompanyRegister rsCompanyRegisterService;
	/**
	 * 异地通办办件信息表实体对象
	 */
	private AuditUnionProject dataBean = null;
	
	private AuditTask audittask = null;

	/**
	 * 证件类型下拉列表model
	 */
	private List<SelectItem> certtypeModel = null;
	
	private List<SelectItem> applyertypeModel = null;
	
    /**
     * 表格控件model
     */
    private DataGridModel<AuditUnionProjectMaterial> model;
    
	@Autowired
    private IAuditUnionProjectMaterialService materialservice;

	public void pageLoad() {
		String projectguid = getRequestParameter("guid");
		if(dataBean == null) {
			dataBean = new AuditUnionProject();
			if(StringUtil.isNotBlank(projectguid)) {
				dataBean = service.find(projectguid);
				initTask(dataBean.getTask_id());
			}else {
				if(!isPostback()) {
					initProject();
				}
			}
		}
	}
	
    public DataGridModel<AuditUnionProjectMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditUnionProjectMaterial>()
            {
				private static final long serialVersionUID = 1L;
				@Override
                public List<AuditUnionProjectMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                	List<AuditUnionProjectMaterial> list = new ArrayList<AuditUnionProjectMaterial>();
                	int count =0;
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    if(dataBean!=null && StringUtil.isNotBlank(dataBean.getRowguid())) {
                    	conditionSql += " and unionprojectguid='"+dataBean.getRowguid()+"'";
                    	list = materialservice.findList(
                    			ListGenerator.generateSql("audit_union_project_material", conditionSql, sortField, sortOrder), first, pageSize,
                    			conditionList.toArray());
                    	count = materialservice.countAuditUnionProjectMaterial(ListGenerator.generateSql("audit_union_project_material", conditionSql), conditionList.toArray());
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    public void initTask(String taskid) {
		String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
		JSONObject params = new JSONObject();
		params.put("taskid", taskid);
		params.put("is_needall", 1);
		JSONObject param = new JSONObject();
		param.put("params", params);
		String rtn = HttpUtil.doPostJson(url+"rest/unionProject/getTaskDetail", param.toJSONString());
		JSONObject rtnjson = JSON.parseObject(rtn);
		if(rtnjson!=null) {
			JSONObject custom = rtnjson.getJSONObject("custom");
			if(custom != null && custom.getIntValue("code")==1) {
				JSONObject audittaskjson = custom.getJSONObject("audittask");
				if(audittaskjson != null) {
					if(audittask == null) {
						audittask = new AuditTask();
						audittask.setRowguid(audittaskjson.getString("rowguid"));
						audittask.setTaskname(audittaskjson.getString("taskname"));
						audittask.setTask_id(audittaskjson.getString("task_id"));
						audittask.setItem_id(audittaskjson.getString("item_id"));
						audittask.setType(audittaskjson.getInteger("type"));
						audittask.setOuname(audittaskjson.getString("ouname"));
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setPromise_day(audittaskjson.getInteger("promise_day"));
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setApplyertype(audittaskjson.getString("applyertype"));
						audittask.setAcceptcondition(audittaskjson.getString("acceptcondition"));
					}
					
				}
			}
		}
    }
	
	public void initProject() {
		String taskid = this.getRequestParameter("taskid");
		String is_needall = this.getRequestParameter("is_needall");
		String is_init = this.getRequestParameter("is_init");
		String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
		JSONObject params = new JSONObject();
		params.put("taskid", taskid);
		if(ZwfwConstant.CONSTANT_STR_ONE.equals(is_needall)) {
			params.put("is_needall", is_needall);
		}
		JSONObject param = new JSONObject();
		param.put("params", params);
		String rtn = HttpUtil.doPostJson(url+"rest/unionProject/getTaskDetail", param.toJSONString());
		JSONObject rtnjson = JSON.parseObject(rtn);
		if(rtnjson!=null) {
			JSONObject custom = rtnjson.getJSONObject("custom");
			if(custom != null && custom.getIntValue("code")==1) {
				String taskguid = custom.getString("taskguid");
				if(StringUtil.isNotBlank(taskguid)) {
					addCallbackParam("IS_ENABLE", "1");
					if(!ZwfwConstant.CONSTANT_STR_ONE.equals(is_needall)) {
						return;
					}
				}else {
					return;
				}
				JSONObject audittaskjson = custom.getJSONObject("audittask");
				if(audittaskjson != null) {
					if(audittask == null) {
						audittask = new AuditTask();
						audittask.setRowguid(audittaskjson.getString("rowguid"));
						audittask.setTaskname(audittaskjson.getString("taskname"));
						audittask.setTask_id(audittaskjson.getString("task_id"));
						audittask.setItem_id(audittaskjson.getString("item_id"));
						audittask.setType(audittaskjson.getInteger("type"));
						audittask.setOuname(audittaskjson.getString("ouname"));
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setPromise_day(audittaskjson.getInteger("promise_day"));
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setApplyertype(audittaskjson.getString("applyertype"));
						audittask.setAcceptcondition(audittaskjson.getString("acceptcondition"));
					}
					if(dataBean!=null) {
						dataBean.setRowguid(UUID.randomUUID().toString());
						dataBean.setProjectname(audittaskjson.getString("taskname"));
						dataBean.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);
						dataBean.setOperatedate(new Date());
						dataBean.setApplyertype(audittask.getApplyertype().split(",")[0]);
						dataBean.setApplydate(new Date());
						dataBean.setTask_id(audittaskjson.getString("task_id"));
						dataBean.setOperateusername(userSession.getDisplayName());
						dataBean.setIs_submit(ZwfwConstant.CONSTANT_STR_ZERO);
						dataBean.setRemark(userSession.getOuName()+" "+StringUtil.getNotNullString(ZwfwUserSession.getInstance().getWindowName()));
						if(ZwfwConstant.CONSTANT_STR_ONE.equals(is_init)) {
							service.insert(dataBean);
						}
						addCallbackParam("rowguid", dataBean.getRowguid());
					}
				}
				JSONArray materialarray = custom.getJSONArray("materiallist");
				if(dataBean!=null && materialarray!=null) {
					for (Object object : materialarray) {
						AuditUnionProjectMaterial unionmaterial = new AuditUnionProjectMaterial();
						JSONObject json = (JSONObject) object;
						unionmaterial.setRowguid(UUID.randomUUID().toString());
						unionmaterial.setCliengguid(UUID.randomUUID().toString());
						unionmaterial.setUnionprojectguid(dataBean.getRowguid());
						unionmaterial.setTaskmaterialguid(json.getString("rowguid"));
						unionmaterial.setIs_rongque(json.getString("is_rongque"));
						unionmaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
						unionmaterial.setAuditstatus(json.getString("submittype"));
						unionmaterial.setTaskguid(json.getString("taskguid"));
						unionmaterial.setTaskmaterial(json.getString("materialname"));
						unionmaterial.setNecessity(json.getInteger("necessity"));
						auditUnionProjectMaterialService.insert(unionmaterial);
					}
				}
			}
		}
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		String is_init = this.getRequestParameter("is_init");
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		dataBean.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);
		service.update(dataBean);
		addCallbackParam("msg", "保存成功！");
		dataBean = null;
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void saveAndSubmit(String projectguid) {
		add();
		String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
		JSONObject params = new JSONObject();
		if(dataBean == null) {
			if(StringUtil.isBlank(projectguid)) {
				projectguid = getRequestParameter("guid");
			}
			dataBean = new AuditUnionProject();
			if(StringUtil.isNotBlank(projectguid)) {
				dataBean = service.find(projectguid);
			}
		}
		params.put("project", dataBean);
		JSONObject param = new JSONObject();
		param.put("params", params);
		//默认本地都初始化数据用于后续统计，调试代码不初始化都时候不掉接口
		if(StringUtil.isNotBlank(url)) {
//			String rtn = HttpUtil.doPostJson(url+"rest/unionProject/initUnionProject", param.toJSONString());
//			JSONObject rtnjson = JSON.parseObject(rtn);
//			if(rtnjson!=null) {
//				JSONObject custom = rtnjson.getJSONObject("custom");
//				if(custom != null && custom.getIntValue("code")==1) {
//					String rowguid = custom.getString("rowguid");
//					if(StringUtil.isNotBlank(rowguid)) {
//						addCallbackParam("msg", "已提交归属地审核！");
//						dataBean.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
//						service.update(dataBean);
//						dataBean = null;
//						return;
//					}
//				}
//			}
			addCallbackParam("url", url+"rest/unionProject/initUnionProject");
			addCallbackParam("params", param.toJSONString());
		}else {
			addCallbackParam("msg", "异地通办系统参数未配置：AS_UNION_POJRCTURL");
			return;
		}
	}

	public AuditUnionProject getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditUnionProject();
		}
		return dataBean;
	}

	public void setDataBean(AuditUnionProject dataBean) {
		this.dataBean = dataBean;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCerttypeModel() {
		if (certtypeModel == null) {
			List<SelectItem> silist = new ArrayList<SelectItem>();
			certtypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证件类型", null, false));
			for (SelectItem selectitem : certtypeModel) {
                if (ZwfwConstant.APPLAYERTYPE_GR.equals(dataBean.getApplyertype().toString())) {
                    // 申请人类型：个人 只显示身份证 2开头为个人，1开头为企业
                    if ("2".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                }
                else if (ZwfwConstant.APPLAYERTYPE_QY.equals(dataBean.getApplyertype().toString())) {
                    // 申请人类型：企业 不显示身份证
                    if ("1".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                }else if("30".equals(dataBean.getApplyertype().toString())){
                    // 申请人类型：企业 不显示身份证
                    if ("3".equals(selectitem.getValue().toString().substring(0, 1))) {
                        silist.add(selectitem);
                    }
                }
            }
			certtypeModel = silist;
		}
		return this.certtypeModel;
	}
	
	@SuppressWarnings("unchecked")
    public List<SelectItem> getapplyertypeModel() {
        if (applyertypeModel == null) {
            applyertypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "申请人类型", null, false));
            if (audittask != null && StringUtil.isNotBlank(audittask.getApplyertype())) {
                applyertypeModel.removeIf(a -> !audittask.getApplyertype().contains(a.getValue().toString()));
            }
            if(dataBean !=null &&  StringUtil.isNotBlank(dataBean.getApplyertype())) {
            	applyertypeModel.removeIf(a -> !audittask.getApplyertype().contains(a.getValue().toString()));
            }
            
        }
        return this.applyertypeModel;
    }
	
    /**
     * 根据证照编号获取申请人列表
     * 
     * @param query
     *            输入的证照号
     * @return
     */
    public List<SelectItem> searchHistory(String query) {
        if (dataBean == null){
            return null;
        }
        List<SelectItem> list = new ArrayList<SelectItem>();
        String applyerType = dataBean.getApplyertype();
        String certType = dataBean.getCerttype();
        if (StringUtil.isNotBlank(query)) {
            if (StringUtil.isNotBlank(applyerType)) {
                if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                    List<AuditRsIndividualBaseinfo> individuallist = individualService
                            .selectIndividualByLikeIDNumber(query).getResult();
                    for (AuditRsIndividualBaseinfo auditIndividual : individuallist) {
                        String str = auditIndividual.getIdnumber() + "（" + auditIndividual.getClientname() + "）";
                        SelectItem selectItem = new SelectItem();
                        selectItem.setText(str);
                        selectItem.setValue(auditIndividual.getIdnumber());
                        list.add(selectItem);
                    }
                }
                // 申请人类型：企业
                else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                    List<AuditRsCompanyBaseinfo> companylist = new ArrayList<AuditRsCompanyBaseinfo>();
                    // 组织机构代码证
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                        companylist = companyService.selectCompanyByLikeOrganCode(query).getResult();
                        for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                            String str = auditCompany.getOrgancode() + "（" + auditCompany.getOrganname() + "）";
                            SelectItem selectItem = new SelectItem();
                            selectItem.setText(str);
                            selectItem.setValue(auditCompany.getOrgancode());
                            list.add(selectItem);
                        }
                    }
                    // 统一社会信用代码
                    else if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(certType)) {
                        companylist = companyService.selectCompanyByLikeCreditcode(query).getResult();
                        for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                            String str = auditCompany.getCreditcode() + "（" + auditCompany.getOrganname() + "）";
                            SelectItem selectItem = new SelectItem();
                            selectItem.setText(str);
                            selectItem.setValue(auditCompany.getCreditcode());
                            list.add(selectItem);
                        }
                    }
                    // 工商营业执照
                    else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certType)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.like("businesslicense", query);
                        sql.eq("is_history", "0");
                        sql.setSelectCounts(10);
                        companylist = companyService.selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
                        if (companylist != null) {
                            for (AuditRsCompanyBaseinfo auditCompany : companylist) {
                                String str = auditCompany.getBusinesslicense() + "（" + auditCompany.getOrganname()
                                        + "）";
                                SelectItem selectItem = new SelectItem();
                                selectItem.setText(str);
                                selectItem.setValue(auditCompany.getBusinesslicense());
                                list.add(selectItem);
                            }
                        }
                    }

                }
            }
        }
        return list;

    }
	/**
     * 根据证照编号获取申请人详细信息
     * 
     * @param certnum
     */
    public void selectApplyer(String certnum,String certType) {
        // 申请人类型：个人
        StringBuilder ownerguid = new StringBuilder(""); // 证照所有人标识
        String applyerType = dataBean.getApplyertype();
        if(StringUtil.isBlank(certType)){
        	 certType = dataBean.getCerttype();
        }     
        if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
            if (StringUtil.isNotBlank(certnum)) {

                AuditRsIndividualBaseinfo auditIndividual = individualService
                        .getAuditRsIndividualBaseinfoByIDNumber(certnum).getResult();
                if (auditIndividual != null) {
                    ownerguid.append(auditIndividual.getPersonid());
                    // 设置办件信息
                    HashMap<String, String> map = new HashMap<String, String>(16);
                    map.put("applyername", auditIndividual.getClientname());
                    map.put("address", auditIndividual.getDeptaddress());
                    map.put("contactperson", auditIndividual.getContactperson());
                    map.put("contactphone", auditIndividual.getContactphone());
                    map.put("contactmobile", auditIndividual.getContactmobile());
                    map.put("contactfax", auditIndividual.getContactfax());
                    map.put("contactpostcode", auditIndividual.getContactpostcode());
                    map.put("contactemail", auditIndividual.getContactemail());
                    map.put("certnum", auditIndividual.getIdnumber());
                    map.put("contactcertnum", auditIndividual.getContactcertnum());
                    addCallbackParam("msg", map);
                }
            }
        }
        // 申请人类型：企业
        else {
            AuditRsCompanyBaseinfo auditCompany = null;
            HashMap<String, String> map = new HashMap<String, String>(16);
            // 组织机构代码证
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                auditCompany = companyService.getCompanyByOneField("organcode", certnum).getResult();
                if (auditCompany != null) {
                    map.put("certnum", auditCompany.getOrgancode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                }
                else {
                    auditCompany = companyService.getCompanyByOneField("creditcode", certnum).getResult();
                    map.put("certnum", auditCompany.getCreditcode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_TYSHXYDM);
                }
            }
            // 营业执照号
            else if (ZwfwConstant.CERT_TYPE_GSYYZZ.equals(certType)) {
                auditCompany = companyService.getCompanyByOneField("businesslicense", certnum).getResult();
                map.put("certnum", auditCompany.getBusinesslicense());
            }
            //统一社会信用代码
            else {
                auditCompany = companyService.getCompanyByOneField("creditcode", certnum).getResult();
                if (auditCompany != null) {
                    map.put("certnum", auditCompany.getCreditcode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_TYSHXYDM);
                }
                else {
                    auditCompany = companyService.getCompanyByOneField("organcode", certnum).getResult();
                    map.put("certnum", auditCompany.getOrgancode());
                    map.put("certtype", ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                }
            }
            ownerguid.append(auditCompany.getCompanyid());
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("companyid", auditCompany.getCompanyid());
            sql.eq("is_history", "0");

            map.put("applyername", auditCompany.getOrganname());
            map.put("legal", auditCompany.getOrganlegal());
            map.put("contactcertnum", auditCompany.getContactcertnum());
            map.put("legalid", auditCompany.getOrgalegal_idnumber());
            List<AuditRsCompanyRegister> auditRsCompanyRegisters = rsCompanyRegisterService
                    .selectAuditRsCompanyRegisterByCondition(sql.getMap()).getResult();
            if (auditRsCompanyRegisters != null && auditRsCompanyRegisters.size() > 0) {
                // 设置办件信息
                map.put("address", auditRsCompanyRegisters.get(0).getBusinessaddress());
                map.put("contactphone", auditRsCompanyRegisters.get(0).getContactphone());
            }
            // 返回法人信息
            AuditRsCompanyLegal auditRsCompanyLegal = new AuditRsCompanyLegal();
            List<AuditRsCompanyLegal> list2 = iAuditRsCompanyLegal.selectAuditRsCompanyLegalByCondition(sql.getMap())
                    .getResult();
            if (list2 != null && list2.size() > 0) {
                auditRsCompanyLegal = list2.get(0);
            }
            if (!auditRsCompanyLegal.isEmpty()) {
                map.put("contactperson", auditRsCompanyLegal.getContactperson());
                map.put("contactmobile", auditRsCompanyLegal.getContactmobile());
                map.put("contactfax", auditRsCompanyLegal.getContactfax());
                map.put("contactpostcode", auditRsCompanyLegal.getContactpostcode());
                map.put("contactemail", auditRsCompanyLegal.getContactemail());
            }

            addCallbackParam("msg", map);
        }
    }

	public AuditTask getAudittask() {
		if (audittask == null) {
			audittask = new AuditTask();
		}
		return audittask;
	}

	public void setAudittask(AuditTask audittask) {
		this.audittask = audittask;
	}

}
