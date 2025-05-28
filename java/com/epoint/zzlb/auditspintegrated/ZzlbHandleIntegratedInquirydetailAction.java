package com.epoint.zzlb.auditspintegrated;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.util.TARequestUtil;
import com.epoint.yyyz.auditspiyyyzmaterial.api.IAuditSpIYyyzMaterialService;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.util.SafetyUtil;
import com.epoint.yyyz.yyyzhandelproject.api.IYyyzHandleProject;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("zzlbhandleintegratedinquirydetailaction")
@Scope("request")
public class ZzlbHandleIntegratedInquirydetailAction extends BaseController {
	private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1313301881620639339L;

	//private static String prov_filedown = ConfigUtil.getConfigValue("epointframe", "prov_filedown");
	/**
	 * 子申报API
	 */
	@Autowired
	private IAuditSpISubapp iAuditSpISubapp;
	/**
	 * 材料API
	 */
	@Autowired
	private IHandleSPIMaterial iHandleSPIMaterial;

	@Autowired
	private IAuditTaskCase iAuditTaskCase;

	@Autowired
	private IAuditSpOptionService iAuditSpOptionService;

	@Autowired
	private IAuditSpISubapp auditSpISubappService;

	@Autowired
	private ICodeItemsService codeItemService;

	@Autowired
	private IConfigService configServicce;

	@Autowired
	private IAuditSpYyyzMaterialService auditSpYyyzMaterialService;

	/**
	 * 材料API
	 */
	@Autowired
	private IAuditSpIMaterial iAuditSpIMaterial;
	
	@Autowired
	private IAuditProject iAuditProject;
	
	/**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;
	/**
	 * 办件组合服务
	 */
	@Autowired
	private IHandleProject iHandleProject;
	@Autowired
	private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
	@Autowired
	private IAuditSpInstance auditSpInstanceService;
	@Autowired
	private IAuditSpITask auditSpITaskService;
	@Autowired
	private IAuditSpTask auditSpTaskService;
	@Autowired
	private IAuditProject projectService;
	@Autowired
	private ICodeItemsService codeItemsService;
	@Autowired
	private IAuditProjectSparetime sparetimeService;
	@Autowired
	private IAuditOnlineProject iAuditOnlineProject;

	@Autowired
	private IBusinessLicenseExtension iBusinessLicenseExtension;

	@Autowired
	private IBusinessLicenseBaseinfo iBusinessLicenseBaseinfo;

	@Autowired
	private IAttachService iattachService;

	@Autowired
	private IAuditSpBusiness iAuditSpBusiness;

	@Autowired
	private IAuditTaskExtension iAuditTaskExtension;
	
	/**
     * 阶段事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;

	@Autowired
	private IAuditTask iAuditTask;

	@Autowired
	private IYyyzHandleProject yyyzHandleProject;
	
	//代办接口
    @Autowired
    private IMessagesCenterService messagesCenterService;
	/**
	 * 一业一证材料API
	 */
	@Autowired
	private IAuditSpIYyyzMaterialService auditSpIYyyzMaterialService;

	private Record otherInfo = new Record();
	

	public Record getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(Record otherInfo) {
		this.otherInfo = otherInfo;
	}

	/**
	 * 项目基本信息表实体对象
	 */
	private AuditSpIntegratedCompany dataBean = null;
	/**
	 * 项目申报日期
	 */
	private Date createdate;
	/**
	 * 事项与材料情况
	 */
	private String materialdesc = "";
	/**
	 * 材料提交情况
	 */
	private String materialsubmitdesc = "";
	/**
	 * 主题实例标识
	 */
	private String biGuid = "";
	/**
	 * 主题标识
	 */
	private String businessGuid = "";
	/**
	 * 主题实例
	 */
	private AuditSpInstance spInstance;
	/**
	 * 子申报实例标识
	 */
	private String subappGuid;

	private List<AuditSpITask> spITasks = null;

	private DataGridModel<AuditSpITask> modelTask = null;
	
	

	@Override
	public void pageLoad() {
		biGuid = getRequestParameter("biGuid");
		spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
		if (spInstance != null) {
			List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
			AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
			subappGuid = auditSpISubapp.getRowguid();
			businessGuid = spInstance.getBusinessguid();
			dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid())
					.getResult();
			createdate = spInstance.getCreatedate();
			AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(spInstance.getBusinessguid())
					.getResult();
			String yewuGuid = dataBean.getRowguid();
			if (business != null) {
				addCallbackParam("formid", business.getStr("formid"));
				addCallbackParam("yewuGuid", yewuGuid);
				BusinessLicenseExtension extension = iBusinessLicenseBaseinfo
						.getBusinessBaseinfoByBiguidAndBuesiness(biGuid, businessGuid);
				JSONObject commondata = new JSONObject();
				JSONArray subdatalist = new JSONArray();
				if (extension != null) {
					String formsinfo = extension.getFormsInfo();
					if (StringUtil.isNotBlank(formsinfo)) {
						JSONObject formsinfojson = JSON.parseObject(formsinfo);
						jsonLoop(formsinfojson, commondata, subdatalist);
					}
				}
				addCallbackParam("commondata", commondata.toString());
				addCallbackParam("subdatalist", subdatalist.toString());
			}
			addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
			addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
		}
		spITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
		String taskCount = "0";
		String materialCount = "0";
		// 电子材料
		int dzMaterial = 0;
		// 纸质材料
		int zzMaterial = 0;
		// 容缺材料
		int rqMaterial = 0;
		// 补正材料
		int bzMaterial = 0;
		if (spITasks != null) {
			taskCount = String.valueOf(spITasks.size());
		}
//		List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialByBIGuid(biGuid).getResult();
        List<AuditSpIYyyzMaterial> auditSpIMaterials = auditSpIYyyzMaterialService.getSpIMaterialBybiGUid(biGuid);

		if (auditSpIMaterials != null) {
			materialCount = String.valueOf(auditSpIMaterials.size());
			for (AuditSpIYyyzMaterial auditSpIMaterial : auditSpIMaterials) {
				if ("15".equals(auditSpIMaterial.getStatus())) {
					zzMaterial++;
				} else if ("20".equals(auditSpIMaterial.getStatus())) {
					dzMaterial++;
				} else if ("25".equals(auditSpIMaterial.getStatus())) {
					zzMaterial++;
					dzMaterial++;
				}
				if ("1".equals(auditSpIMaterial.getAllowrongque())) {
					rqMaterial++;
				}
				if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
					bzMaterial++;
				}
			}
		}
		materialdesc = "共" + taskCount + "个事项，" + materialCount + "份材料";
		materialsubmitdesc = "提交电子材料" + String.valueOf(dzMaterial) + "份 提交纸质材料" + String.valueOf(zzMaterial)
				+ "份 容缺（暂无）" + String.valueOf(rqMaterial) + "份 需整改" + String.valueOf(bzMaterial) + "份";
	}

	/**
	 * 
	 * 预审通过
	 * 
	 */
	public void passyes() {

		BusinessLicenseExtension extension = iBusinessLicenseBaseinfo.getBusinessBaseinfoByBiguidAndBuesiness(biGuid,
				businessGuid);

		AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(subappGuid).getResult();

		AuditTaskCase auditTaskCase = iAuditTaskCase.getAuditTaskCaseByRowguid(auditSpISubapp.getCaseguid())
				.getResult();

		List<AuditSpTask> listTask = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();

		String selectoptions = "";
		if (auditTaskCase != null) {
			selectoptions = auditTaskCase.getSelectedoptions();
		}
		List<String> materiallist = new ArrayList<>();
		if (StringUtil.isNotBlank(selectoptions)) {
			JSONObject jsons = JSONObject.parseObject(selectoptions);
			List<String> optionlist = (List<String>) jsons.get("optionlist");
			Map<String, AuditSpOption> optionmap = new HashMap<>();
			if (optionlist != null && optionlist.size() > 0) {
				for (String guid : optionlist) {
					AuditSpOption auditSpOption = iAuditSpOptionService.find(guid).getResult();
					optionmap.put(guid, auditSpOption);
				}
				optionmap = iAuditSpOptionService.getAllOptionByOptionMap(optionmap).getResult();
				for (AuditSpOption auditSpOption : optionmap.values()) {
					if (StringUtil.isBlank(auditSpOption.getMaterialids())) {
						continue;
					}
					String materials = auditSpOption.getMaterialids();
					String[] mater = materials.split(";");
					for (int i = 0; i < mater.length; i++) {
						if (StringUtil.isNotBlank(mater[i])) {
							materiallist.add(mater[i]);
						}
					}
				}
			}
		}
		String phaseGuid = "";
        List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
		String itemName = "";
        if(iBusinessLicenseExtension.getItemNameByBiGuid(biGuid) != null) {
            itemName = iBusinessLicenseExtension.getItemNameByBiGuid(biGuid).getStr("itemname");
        }
        String title = "【受理】"+itemName;
        String openUrl =WebUtil.getRequestRootUrl(request)+"/epointzwfw/auditbusiness/auditproject/auditprojectinfo?";
        //获取办件相关信息
        JSONArray projectInfoList = new JSONArray();
		for (AuditSpTask task : listTask) {
			AuditTask taskinfo = iAuditTask.getUseTaskAndExtByTaskid(task.getTaskid()).getResult();
			if (taskinfo != null && extension != null) {
			    iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, taskinfo.getRowguid(),
			            taskinfo.getTaskname(), subappGuid, taskinfo.getOrdernum() == null ? 0 : taskinfo.getOrdernum(),taskinfo.getAreacode()).getResult();
				String formsinfo = extension.getSelectItem();
				if (StringUtil.isNotBlank(formsinfo)) {
					JSONArray formsinfoarray = JSON.parseArray(formsinfo);
					AuditTaskExtension taskextension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskinfo.getRowguid(), false).getResult();
					if (taskextension != null && formsinfoarray != null && formsinfoarray.size() > 0) {
						for (int i = 0; i < formsinfoarray.size(); i++) {
							JSONObject jsonobject = formsinfoarray.getJSONObject(i);
							String lctasktype = jsonobject.getString("itemCode");
							if (lctasktype.equals(taskextension.get("zzlbitemcode"))) {
								// 先生成办件，再初始化材料
								String projectGuid = yyyzHandleProject.InitIntegratedProject(taskinfo.getRowguid(), "",
										userSession.getDisplayName(), userSession.getUserGuid(), "", "",
										ZwfwUserSession.getInstance().getCenterGuid(), biGuid, subappGuid, businessGuid,
										ZwfwConstant.APPLY_WAY_CKDJ).getResult();
								String field = "flowsn,rowguid,areacode";
								AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(field, projectGuid, taskinfo.getAreacode()).getResult();
					            if(StringUtil.isNotBlank(taskextension.getStr("yyyzSendUrl")) && StringUtil.isNotBlank(auditProject.getFlowsn())) {
				                    JSONObject projectInfoObject = new JSONObject();
				                    projectInfoObject.put("token", "Epoint_WebSerivce_**##0601");
				                    projectInfoObject.put("flowsn", auditProject.getFlowsn());
				                    projectInfoObject.put("newItemCode", taskinfo.getStr("new_item_code") == null ? taskinfo.getTask_id():taskinfo.getStr("new_item_code"));
				                    projectInfoObject.put("areacode", auditProject.getAreacode());
				                    projectInfoObject.put("taskId", taskinfo.getTask_id());
				                    projectInfoList.add(projectInfoObject);
				                    auditProject.setStatus(33); //此处添加任意非办件状态，保证该办件不会再审批系统窗口人员中显示
				                    iAuditProject.updateProject(auditProject);
					            }
					            else {
					                //生成办件消息
					                String taskGuid = taskinfo.getRowguid();
					                String sendUrl = openUrl +"taskguid="+taskGuid+"&projectguid="+projectGuid+"&biguid="+biGuid;
					                List<Record> windowInfoList = iBusinessLicenseExtension.getWindowGuidByTaskGuid(taskGuid);
					                List<String> windowGuidList = new ArrayList<String>();
					                if(StringUtil.isNotBlank(windowInfoList) && windowInfoList.size()>0) {
					                    for (Record windowInfo : windowInfoList) {
					                        windowGuidList.add(windowInfo.getStr("windowguid"));
					                    }
					                }
					                List<Record> userGuidList = iBusinessLicenseExtension.getUserGuidByWindowGuidList(windowGuidList);
					                if(StringUtil.isNotBlank(userGuidList) && userGuidList.size()>0) {
					                    for (Record userGuid : userGuidList) {
					                        String acceptUserGuid = userGuid.getStr("userguid");
					                        messagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title, IMessagesCenterService.MESSAGETYPE_WAIT, acceptUserGuid, "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
					                                "", sendUrl, UserSession.getInstance().getOuGuid(),  UserSession.getInstance().getBaseOUGuid(), 1, null, "", projectGuid, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
					                    }
					                }
					            }
					            List<AuditSpITask> spiTaskList = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                                for(AuditSpITask spiTask : spiTaskList) {
                                    if(taskinfo.getRowguid().equals(spiTask.getTaskguid())) {
                                        spiTask.setProjectguid(projectGuid);
                                        iAuditSpITask.updateAuditSpITask(spiTask);
                                    }
                                }
							}
						}
					}
				}

			}
		}
		//判断材料是否已经初始化
        List<AuditSpIYyyzMaterial> auditSpIYyyzMaterialList = auditSpIYyyzMaterialService.getSpIMaterialBybiGUid(biGuid);
        if(StringUtil.isBlank(auditSpIYyyzMaterialList) || auditSpIYyyzMaterialList.isEmpty()) {
            // 再进行材料初始化
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList = auditSpYyyzMaterialService
                    .findListBybusinessGuid(businessGuid);
            auditSpIYyyzMaterialService.initTcSubappMaterial(subappGuid, businessGuid, biGuid, "",
                    auditSpYyyzMaterialList);
            
        }
		iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);

		// 更新外网申办的状态
		Map<String, String> updateFieldMap = new HashMap<String, String>(16);
		updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTG));// 外网申报预审通过
		SqlConditionUtil conditionsql = new SqlConditionUtil();
		conditionsql.eq("sourceguid", biGuid);
		iAuditOnlineProject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16), conditionsql.getMap());
		iHandleProject.delZwfwMessage("", ZwfwUserSession.getInstance().getAreaCode(), "统一收件人员", subappGuid);
		addCallbackParam("msg", "预审通过！");
		addCallbackParam("info",projectInfoList.toString());
	}

	/**
	 * 
	 * 预审不通过
	 * 
	 */
	public void passno() {
		iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSTH, null);
		// 更新外网申办的状态
		Map<String, String> updateFieldMap = new HashMap<String, String>(16);
		// 预审不通过，状态改为10
		updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));
		SqlConditionUtil conditionsql = new SqlConditionUtil();
		conditionsql.eq("sourceguid", biGuid);
		iAuditOnlineProject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16), conditionsql.getMap());
		iHandleProject.delZwfwMessage("", ZwfwUserSession.getInstance().getAreaCode(), "统一收件人员", subappGuid);
		addCallbackParam("msg", "预审不通过！");
	}

	/**
	 * 
	 * 初始化材料列表
	 * 
	 * @param subappGuid
	 * @param biGuid
	 * @param phaseGuid
	 * @return
	 */
	@SuppressWarnings("unused")
    public String modelAuditMaterial(String subappguid, String biguid, String phaseguid) {
        // 这里需要定义几个变量
        int submitedMaterialCount = 0;
        int shouldPaperCount = 0;
        int submitedPaperCount = 0;
        int shouldAttachCount = 0;
        int submitedAttachCount = 0;
        int rongqueCount = 0;
        int buRongqueCount = 0;

        if (subappguid != null) {
            subappGuid = subappguid;
        }

        if (subappguid != null) {
            biGuid = biguid;
        }

        String appKey = configServicce.getFrameConfigValue("AS_YYYZ_AppKey");
        String encryptKey = configServicce.getFrameConfigValue("AS_YYYZ_EncryptKey");
        String tokenUrl = configServicce.getFrameConfigValue("AS_YYYZ_TokenUrl");
        String prov_filedown = configServicce.getFrameConfigValue("prov_filedown");
        // 获取token
        String token;
        String tokenJson = SafetyUtil.doHttpPost(tokenUrl, appKey);
        JSONObject tokenObject = JSONObject.parseObject(tokenJson);
        token = tokenObject.getString("token");
        log.info("下载附件获取的token:" + token);

        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(spInstance.getBusinessguid())
                .getResult();
        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
        String subappGuid = auditSpISubapp.getRowguid();
        String phaseGuid = auditSpISubapp.getPhaseguid();
        String initMaterial = auditSpISubapp.getInitmaterial(); // 是否初始化材料
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {
            listMaterial = iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "")
                    .getResult();
        } else {
            listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
        }

        JSONObject jsonMaterials = new JSONObject();

        // 获取一页一证材料数据
        BusinessLicenseExtension extension = iBusinessLicenseBaseinfo.getBusinessBaseinfoByBiguidAndBuesiness(biGuid,
                businessGuid);
        //判断材料是否已经初始化
        List<AuditSpIYyyzMaterial> auditSpIYyyzMaterialList = auditSpIYyyzMaterialService.getSpIMaterialBybiGUid(biGuid);
        List<AuditSpIYyyzMaterial> listYyyzMaterialList = null;
        if(StringUtil.isNotBlank(auditSpIYyyzMaterialList) && !auditSpIYyyzMaterialList.isEmpty()) {
            listYyyzMaterialList = auditSpIYyyzMaterialService.getSpIMaterialBybiGUid(biGuid);
        }
        else{
         // 再进行材料初始化
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList = auditSpYyyzMaterialService
                    .findListBybusinessGuid(businessGuid);
            listYyyzMaterialList = auditSpIYyyzMaterialService.initTcSubappMaterial(subappGuid, businessGuid, biGuid, "",
                    auditSpYyyzMaterialList);
        }
        if (extension != null && "6".equals(business.getBusinesstype())) {
            String materialsinfo = extension.getMaterialsInfo();
            if (StringUtil.isNotBlank(materialsinfo)) {
                JSONArray array = JSON.parseArray(materialsinfo);
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        submitedMaterialCount++;
                        String cliengguid = UUID.randomUUID().toString();
                        JSONObject jsonobject = array.getJSONObject(i);
                        JSONObject jsonMaterial = new JSONObject();
                        jsonMaterial.put("materialname", jsonobject.getString("resourceName"));
                        String resourceCode = jsonobject.getString("resourceCode");
                        //判断材料是否已经获取过，获取过则不需要重复获取
                        boolean haveMaterial = false;
                        for(AuditSpIYyyzMaterial auditSpIyyyzMaterial : listYyyzMaterialList) {
                            if(resourceCode.equals(auditSpIyyyzMaterial.getStr("yyyztype"))) {
                                if(StringUtil.isNotBlank(auditSpIyyyzMaterial.getCliengguid())) {
                                    int num = iattachService.getAttachCountByClientGuid(auditSpIyyyzMaterial.getCliengguid());
                                    if(num>0) {
                                        cliengguid = auditSpIyyyzMaterial.getCliengguid();
                                        haveMaterial = true;
                                    }
                                    else {
                                        auditSpIyyyzMaterial.setCliengguid(cliengguid);  
                                        auditSpIYyyzMaterialService.update(auditSpIyyyzMaterial);
                                    }
                                }
                            }
                        }
                        jsonMaterial.put("necessity", "1");
                        jsonMaterial.put("is_rongque", "0");
                        jsonMaterial.put("submittype", "10");
                        jsonMaterial.put("rowguid", UUID.randomUUID().toString());
                        jsonMaterial.put("resourceCode", jsonobject.getString("resourceCode"));
                        jsonMaterial.put("cliengguid", cliengguid);
                        String resourceType = jsonobject.getString("resourceType");
                        if ("ITEM".equals(resourceType)) {
                            resourceType = "2";
                        } else if ("PERMIT".equals(resourceType)) {
                            resourceType = "1";
                        } else {
                            resourceType = "3";
                        }
                        jsonMaterial.put("resourceType", resourceType);
                        if(!haveMaterial) {
                            JSONArray files = jsonobject.getJSONArray("files");
                            if (files != null && files.size() > 0) {
                                for (int j = 0; j < files.size(); j++) {
                                    JSONObject materbject = files.getJSONObject(j);
                                    String fileid = materbject.getString("documentId");
                                    String filename = materbject.getString("documentName");
                                    String submitTime = materbject.getString("submitTime");
                                    String fileType = materbject.getString("fileType");
                                    if (StringUtil.isNotBlank(fileid)) {
//                                  FrameAttachInfo attachinfod = iBusinessLicenseBaseinfo
//                                          .getAttachByYyyzCliengguid(extension.getBaseinfoGuid(), resourceCode);
//                                  if (attachinfod == null) {
                                        FrameAttachInfo attachinfo = new FrameAttachInfo();
                                        attachinfo.setAttachGuid(UUID.randomUUID().toString());
                                        attachinfo.setCliengGuid(cliengguid);
                                        attachinfo.setCliengTag("浪潮三联办材料");
                                        attachinfo.set("yyyztype", resourceCode);
                                        attachinfo.set("yyyzguid", extension.getBaseinfoGuid());
                                        attachinfo.setAttachFileName(filename+"."+fileType);
                                        attachinfo.setContentType("."+fileType);
                                        log.info("attachinfo.getContentType======="+attachinfo.getContentType());
                                        log.info("attachinfo.getAttachFileName======="+attachinfo.getAttachFileName());
                                        attachinfo.setUploadDateTime(new Date());
                                        try {
                                            Map<String, Object> map = new HashMap<>();
                                            map = downloadFile(prov_filedown + "&token=" + token + "&docId=" + fileid);
//                                            String downloadUrl = "http://172.20.246.59/yyyz/bfDownload?method=downloadFile" + "&token=" + token + "&docId=" + fileid;
//                                            map = downloadFile(downloadUrl);
                                            if (map != null) {
                                                attachinfo.setAttachLength((Long) map.get("length"));
                                                iattachService.addAttach(attachinfo, (InputStream) map.get("stream"));
                                            }
                                        } catch (Exception e) {
                                            log.info("获取浪潮材料信息出错" + prov_filedown + "&token=" + token + "&docId="
                                                    + fileid);
                                            e.printStackTrace();
                                        }
                                    }
                                    
                                }
                            }
                        }
                        jsonMaterial.put("status", "20");
                        materialList.add(jsonMaterial);
                    }
                }
            }
        }

        try {
            if (!"6".equals(business.getBusinesstype())) {
                jsonMaterials.put("total", listMaterial.size());

                for (int i = 0; i < listMaterial.size(); i++) {
                    JSONObject jsonMaterial = new JSONObject();
                    int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                    String submittype = listMaterial.get(i).get("SUBMITTYPE");
                    jsonMaterial.put("status", listMaterial.get(i).getStatus());
                    jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                    jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                    jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));
                    jsonMaterial.put("is_rongque", listMaterial.get(i).getAllowrongque());
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).getAllowrongque())) {
                        rongqueCount++;
                    }
                    jsonMaterial.put("submittype", submittype);
                    jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                    materialList.add(jsonMaterial);
                    // 这里对材料信息进行判断
                    // 材料的提交情况
                    if (materialstatus > 10) {
                        submitedMaterialCount++;
                        if (materialstatus == 20) {
                            submitedAttachCount++;
                        } else if (materialstatus == 15) {
                            submitedPaperCount++;
                        } else {
                            submitedAttachCount++;
                            submitedPaperCount++;
                        }
                    }
                    // 材料的应提交情况
                    if (!"20".equals(submittype)) {
                        shouldAttachCount++;
                    }
                    if (!"10".equals(submittype)) {
                        shouldPaperCount++;
                    }
                }
            }

            jsonMaterials.put("isAllPaper", shouldPaperCount > submitedPaperCount ? 0 : 1);
            jsonMaterials.put("data", materialList);
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");

        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
        return jsonMaterials.toString();
    }

	/**
	 * 
	 * 操作纸质
	 * 
	 * @param dataMaterials
	 */
	public void paperOperate(Map<String, String> dataMaterials) {
		Map<String, String> params = getRequestParametersMap();
		String param = params.get("params");
		JSONArray jsonParam = JSONObject.parseArray(param);
		jsonParam.forEach(jsonPram -> {
			Map<String, Object> map = JsonUtil.jsonToMap(jsonPram.toString());
			String operateType = String.valueOf(map.get("operateType"));
			String materialInstanceGuid = String.valueOf(map.get("materialInstanceGuid"));
			if ("submit".equals(operateType)) {
				iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, 5);
			} else {
				iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, -5);
			}
		});

		// 处理返回数据
		Record rtnInfo = new Record();
		rtnInfo.put("customer", "success");
		sendRespose(JsonUtil.objectToJson(rtnInfo));
	}

	/**
	 * 
	 *  [通过获取生成办件时需要发送的申报流水号，去调取网厅推送申报流水号接口] 
	 *  @param list    
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void sendFlowsn(String info) {
	    if(StringUtil.isNotBlank(info) ) {
	        JSONObject jsonObject = JSONObject.parseObject(info);
	        JSONObject paramObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("token", jsonObject.get("token"));
            paramObject.put("flowsn", jsonObject.get("flowsn"));
            paramObject.put("newItemCode",jsonObject.get("newItemCode"));
            paramObject.put("areacode", jsonObject.get("areacode"));
            paramObject.put("taskId", jsonObject.get("taskId"));
            jsonObject1.put("params", paramObject);
            String logs=TARequestUtil.sendPost(configServicce.getFrameConfigValue("yyyzSendFlowsnUrl"), jsonObject1.toString(), "", "");
            log.info("===============调用post的结果是："+logs+"=======");
	    }
	    else {
	        log.info("===============调用sendFlowsn失败，info值是空=======");
	    }
	}
	
	public void searchCert() {
		String areacode = "";
		if (ZwfwUserSession.getInstance().getCitylevel() != null
				&& Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
						.parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
			areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
		} else {
			areacode = ZwfwUserSession.getInstance().getAreaCode();
		}
		List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
		listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
		// 关联共享材料
		String flag = iHandleSPIMaterial
				.is_ExistSPCert(listMaterial, dataBean.getCompanyname(), dataBean.getZzjgdmz(), areacode).getResult();
		addCallbackParam("flag", flag);
		addCallbackParam("applyerType", ZwfwConstant.CERTOWNERTYPE_FR);
		addCallbackParam("certnum", dataBean.getZzjgdmz());
		addCallbackParam("subappGuid", subappGuid);
	}

	public DataGridModel<AuditSpITask> getDataGridTask() {
		// 获得表格对象
		if (modelTask == null) {
			modelTask = new DataGridModel<AuditSpITask>() {
				@Override
				public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<AuditSpITask> auditSpITasks = new ArrayList<>();
					if (spITasks != null && spITasks.size() > 0) {
						for (AuditSpITask auditSpITask : spITasks) {
							if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
								String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause ";
								AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields,
										auditSpITask.getProjectguid(), ZwfwUserSession.getInstance().getAreaCode())
										.getResult();
								if (auditProject != null) {
									int status = auditProject.getStatus();
									auditSpITask.put("status",
											codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));

									String sparetime = "--";
									if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
											&& status < ZwfwConstant.BANJIAN_STATUS_ZCBJ && !ZwfwConstant.ITEMTYPE_JBJ
													.equals(auditProject.getTasktype().toString())) {
										if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
											sparetime = "暂停计时";// 办件处于暂停计时状态
										} else {
											AuditProjectSparetime auditprojectsparetime = sparetimeService
													.getSparetimeByProjectGuid(auditSpITask.getProjectguid())
													.getResult();
											if (auditprojectsparetime != null) {
												sparetime = CommonUtil
														.getSpareTimes(auditprojectsparetime.getSpareminutes());
											}
										}
									}
									auditSpITask.put("sparetime", sparetime);
									auditSpITask.put("businessname", spInstance.getItemname());
								}
								auditSpITasks.add(auditSpITask);
							}
						}
					}
					this.setRowCount(auditSpITasks.size());
					return auditSpITasks;
				}
			};
		}
		return modelTask;
	}

	public AuditSpIntegratedCompany getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditSpIntegratedCompany();
		}
		return dataBean;
	}

	public void setDataBean(AuditSpIntegratedCompany dataBean) {
		this.dataBean = dataBean;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getMaterialdesc() {
		return materialdesc;
	}

	public void setMaterialdesc(String materialdesc) {
		this.materialdesc = materialdesc;
	}

	public String getMaterialsubmitdesc() {
		return materialsubmitdesc;
	}

	public void setMaterialsubmitdesc(String materialsubmitdesc) {
		this.materialsubmitdesc = materialsubmitdesc;
	}

	public void jsonLoop(Object object, JSONObject oooo, JSONArray bbbb) {

		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				String key = entry.getKey();
				if (key.contains("_LIST") || "DC_YYYZ_QSXK_XSGC".equals(key) || "DC_YYYZ_FSYZL_TWS_MF".equals(key) || "DC_YYYZ_XQJY_JXSS".equals(key)
						|| "DC_YYYZ_XQJY_JZG".equals(key) || "DC_RES_SAFE_HR".equals(key) || "DC_YYYZ_YSGC".equals(key) || "DC_SUB".equals(key)
						|| "DC_YYYZ_QSXK_SJGC".equals(key) || "DC_YYYZ_YSJYXK_SB".equals(key) || "DC_YYYZ_FSYZL_TWS".equals(key) || "DC_YYYZ_QSXK_TSGC".equals(key) || "DC_YYYZ_YLJGXK_YQSB".equals(key) || "DC_YYYZ_J_JZSGQYAQFZR".equals(key)
						|| "DC_YYYZ_QSXK_XSGC".equals(key) || "DC_YYYZ_QSXK_XSGC".equals(key) || "DC_YYYZ_QSXK_TSGC".equals(key) || "DC_YYYZ_JZSGQYAZZAQ".equals(key) || "DC_YYYZ_JZSGQYTZZY".equals(key) || "DC_YYYZ_GYCPSCXK".equals(key)
						|| "DC_YYYZ_JZYQYZZRY".equals(key) || "DC_YYYZ_SXRSGXKLB".equals(key) || "DC_YYYZ_JGWWSCXK".equals(key) || "DC_YYYZ_JYJCJGSCDZ".equals(key) || "DC_YYYZ_JYXXCSXX".equals(key)
				        ) {
					String formid = codeItemService.getItemValueByCodeID("1016199", key); //系统1016199
					JSONArray array = jsonObject.getJSONArray(key);
					if(StringUtil.isNotBlank(formid)) {
					    String [] formids = formid.split(",");
					    for (String id : formids) {
				            JSONArray parentdata = new JSONArray();
				            JSONObject parent = new JSONObject();
				            for (int i = 0; i < array.size(); i++) {
				                JSONObject sondata = array.getJSONObject(i);
				                JSONObject newdata = new JSONObject();
				                for (Map.Entry<String, Object> entry2 : sondata.entrySet()) {
				                    newdata.put(entry2.getKey(), entry2.getValue().toString());
				                }
				                parentdata.add(newdata);
				            }
				            parent.put("subtableid", id);
				            parent.put("datalist", parentdata);
				            bbbb.add(parent);
                        }
					}
				} else {
					JSONObject json = jsonObject.getJSONObject(key);
					for (Map.Entry<String, Object> entry1 : json.entrySet()) {
						oooo.put(entry1.getKey(), entry1.getValue().toString());
					}
				}
			}
		}
	}

	/**
	 * 山东附件库
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> downloadFile(String url) throws Exception {
		Map<String, Object> map = new HashMap<>();
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		GetMethod getMethod = new GetMethod(url);
		client.executeMethod(getMethod);
		long len = getMethod.getResponseContentLength();
		InputStream inputStream = getMethod.getResponseBodyAsStream();
		map.put("length", len);
		map.put("stream", inputStream);
		return map;
	}

	private String getStrFlowSn(String numberName, String numberFlag, int snLength) {
        String flowSn = "";
        Object[] args = new Object[3];
        args[0] = numberName;
        args[1] = numberFlag;
        args[2] = Integer.valueOf(snLength);

        try {
            flowSn = CommonDao.getInstance("common")
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn2", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }

    private String getStdFlowSn(String numberName, String numberFlag, int theYearLength, int snLength) {
        String flowSn = "";
        Object[] args = new Object[7];

        args[0] = numberName;

        args[1] = numberFlag;

        args[2] = Integer.valueOf(EpointDateUtil.getYearOfDate(new Date()));

        args[3] = Integer.valueOf(theYearLength);

        args[4] = Integer.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);

        args[5] = Integer.valueOf(EpointDateUtil.getDayOfDate(new Date()));

        args[6] = Integer.valueOf(snLength);

        try {
            flowSn = CommonDao.getInstance("common")
                    .executeProcudureWithResult(args.length + 1, 12, "Common_Getflowsn", args).toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flowSn;
    }
}
