package com.epoint.union.auditunionproject.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;

/**
 * 异地通办办件信息表对应的后台service实现类
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@Component
@Service
public class AuditUnionProjectServiceImpl implements IAuditUnionProjectService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 插入数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int insert(AuditUnionProject record) {
		return new AuditUnionProjectService().insert(record);
	}

	/**
	 * 删除数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int deleteByGuid(String guid) {
		return new AuditUnionProjectService().deleteByGuid(guid);
	}

	/**
	 * 更新数据
	 * 
	 * @param record BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int update(AuditUnionProject record) {
		return new AuditUnionProjectService().update(record);
	}

	/**
	 * 根据ID查找单个实体
	 * 
	 * @param clazz      类<必须继承BaseEntity>
	 * @param primaryKey 主键
	 * @return T extends BaseEntity
	 */
	public AuditUnionProject find(Object primaryKey) {
		return new AuditUnionProjectService().find(primaryKey);
	}

	/**
	 * 查找单条记录
	 * 
	 * @param sql   查询语句
	 * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
	 *              ;String.class;Integer.class;Long.class]
	 * @param args  参数值数组
	 * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
	 */
	public AuditUnionProject find(String sql, Object... args) {
		return new AuditUnionProjectService().find(sql, args);
	}

	/**
	 * 查找一个list
	 * 
	 * @param sql   查询语句
	 * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
	 * @param args  参数值数组
	 * @return T extends BaseEntity
	 */
	public List<AuditUnionProject> findList(String sql, Object... args) {
		return new AuditUnionProjectService().findList(sql, args);
	}

	/**
	 * 分页查找一个list
	 * 
	 * @param sql        查询语句
	 * @param pageNumber 记录行的偏移量
	 * @param pageSize   记录行的最大数目
	 * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
	 * @param args       参数值数组
	 * @return T extends BaseEntity
	 */
	public List<AuditUnionProject> findList(String sql, int pageNumber, int pageSize, Object... args) {
		return new AuditUnionProjectService().findList(sql, pageNumber, pageSize, args);
	}

	/**
	 * 查询数量
	 * 
	 * @param sql  执行语句
	 * @param args 参数
	 * @return Integer
	 */
	@Override
	public Integer countAuditUnionProject(String sql, Object... args) {
		return new AuditUnionProjectService().countAuditUnionProject(sql, args);
	}

	
    @Override
    public AuditCommonResult<String> InitProject(String taskGuid, String projectGuid, String operateUserName,
            String operateUserGuid, String windowGuid, String windowName, String centerGuid, String certNum, String qno,
            String acceptareacode, String cityLevel, String delegatetype) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            AuditProject auditproject = new AuditProject();
            String rowGuid = StringUtil.isBlank(projectGuid) ? UUID.randomUUID().toString() : projectGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.setOperateusername(operateUserName);
            auditproject.setOperatedate(new Date());
            auditproject.setRowguid(rowGuid);
            auditproject.setWindowguid(windowGuid);
            auditproject.setWindowname(windowName);
            IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
            if (auditTask != null) {
                auditproject.setTask_id(auditTask.getTask_id());
                auditproject.setTaskguid(auditTask.getRowguid());
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYTJ);// 办件状态：待预审
                auditproject.setOuguid(auditTask.getOuguid());
                auditproject.setOuname(auditTask.getOuname());
                auditproject.setProjectname(auditTask.getTaskname());
                auditproject.setIs_charge(auditTask.getCharge_flag());
                auditproject.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                auditproject.setCenterguid(centerGuid);
                auditproject.setAreacode(auditTask.getAreacode());
                IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditTaskExtension.class);
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                if (auditTaskExtension != null) {
                    auditproject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());// 进驻大厅
                    auditproject.setIf_express(auditTaskExtension.getIf_express());
                }
                auditproject.setTasktype(auditTask.getType());
                auditproject.setPromise_day(auditTask.getPromise_day());
                // 企业类型
                if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditTask.getApplyertype())) {
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);// 申请人证照类型：组织机构代码
                }
                else {
                    // 个人类型
                    auditproject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));
                    auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
                }
            }
            auditproject.set("is_union", ZwfwConstant.CONSTANT_STR_ONE);//异地通办办件标示
            auditproject.setApplydate(new Date());// 办件申请时间
            auditproject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
            // auditproject.setIssendsms(StringUtil.isBlank(asissendmessage)
            // ? ZwfwConstant.CONSTANT_STR_ZERO
            // : ZwfwConstant.CONSTANT_STR_ONE);// 是否发送短信
            auditproject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.setIs_delay(20);// 是否延期

            IAuditUnionProjectService auditUnionProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditUnionProjectService.class);
            AuditUnionProject unionproject = auditUnionProjectService.find(projectGuid);
            if(unionproject != null) {
            	auditproject.setCertnum(unionproject.getCertnum());
            	auditproject.setApplyertype(Integer.valueOf(unionproject.getApplyertype()));
            	auditproject.setCerttype(unionproject.getCerttype());
            	auditproject.setApplyername(unionproject.getApplyername());
            	auditproject.setContactphone(unionproject.getContactphone());
            	auditproject.setAddress(unionproject.getAddress());
            	auditproject.setContactperson(unionproject.getApplyername());
            }
            // TODO这个地方需要调整

            // 排队叫号系统传递过来的参数处理
            if (StringUtil.isNotBlank(qno)) {
                auditproject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                auditproject.setCertnum(certNum);
                IAuditQueueUserinfo userinfoservice = ContainerFactory.getContainInfo()
                        .getComponent(IAuditQueueUserinfo.class);
                Record userinfo = userinfoservice.getUserByIdentityCardNum(certNum).getResult();
                if (userinfo != null) {
                    auditproject.setApplyername(userinfo.get("DisplayName"));
                    auditproject.setAddress(userinfo.get("address"));
                    auditproject.setContactmobile(userinfo.get("Mobile"));
                }
                //更新排队叫号与办件关联关系 
                IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
                queueservice.updateQNOProject(rowGuid, qno, centerGuid);
            }

            // 如果是镇村接件或是部门进驻事项，ACCEPTAREACODE字段设置为当前登记办件窗口所在的辖区编码
            if (Integer.parseInt(cityLevel) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)
                    || ZwfwConstant.TASKDELEGATE_TYPE_ZCBS.equals(delegatetype)) {
                auditproject.setAcceptareacode(acceptareacode);
            }
            auditproject.setCurrentareacode(acceptareacode);
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.addProject(auditproject);

            // 直接赋值给相关数据ProjectGuid
            // wfcontextvalueservice.createOrUpdateContext(processVersionInstanceGuid,
            // "ProjectGuid",
            // auditproject.getRowguid(),
            // WorkflowKeyNames9.ParameterType_T_string, 2);

            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(auditTask == null ? "" : auditTask.getRowguid(), true)
                    .getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            IConfigService configService = ContainerFactory.getContainInfo()
                    .getComponent(IConfigService.class);
            IAttachService attachService = ContainerFactory.getContainInfo()
                    .getComponent(IAttachService.class);
            String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                auditProjectMaterial.clear();
                JSONObject param = new JSONObject();
                JSONObject params = new JSONObject();
                params.put("projectguid", rowGuid);
                params.put("taskmaterialguid", auditTaskMaterial.getRowguid());
				param.put("params", params);
				//同步异地通办附件材料
				String rtn = HttpUtil.doPostJson(url+"rest/unionProject/getMaterialDetail", param.toJSONString());
				JSONObject rtnjson = JSON.parseObject(rtn);
				String cliengguid = UUID.randomUUID().toString();
				FrameAttachInfo attachInfo = new FrameAttachInfo();
				auditProjectMaterial.setStatus(10);
				if(rtnjson != null ) {
					JSONObject custom = rtnjson.getJSONObject("custom");
					if(custom!=null) {
						JSONArray filelist = custom.getJSONArray("filelist");
						String fileurl ="rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
						if(filelist != null && filelist.size()>0) {
							for (Object file : filelist) {
								attachInfo.clear();
								JSONObject filejson = (JSONObject) file;
								attachInfo.setAttachFileName(filejson.getString("filename"));
								attachInfo.setAttachLength(filejson.getLong("length"));
								attachInfo.setCliengGuid(cliengguid);
								attachInfo.setCliengTag("异地通办附件材料");
								InputStream inputstream =  HttpUtil.get(url + fileurl + filejson.getString("attachguid"));
								attachService.addAttach(attachInfo, inputstream);
							}
							auditProjectMaterial.setStatus(20);
						}
					}
				}
				auditProjectMaterial.setOperatedate(new Date());
                auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                auditProjectMaterial.setTaskguid(auditTask.getRowguid());
                auditProjectMaterial.setProjectguid(auditproject.getRowguid());
                auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                auditProjectMaterial.setIs_rongque(0);
                auditProjectMaterial.setCliengguid(cliengguid);
                auditProjectMaterial.setAttachfilefrom("1");
                auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());

                auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
            }
            result.setResult(rowGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
    
    public void dowloadFile(String attachgudi) {
    	
    }

}
