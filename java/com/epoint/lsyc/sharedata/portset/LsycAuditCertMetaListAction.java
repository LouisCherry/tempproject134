package com.epoint.lsyc.sharedata.portset;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.basic.auditresource.auditrssharemaerial.inter.IAuditRsShareMetadata;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.IAuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.IMetaShareRelation;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

/**
 * 
 * 
 * @作者 Administrator
 * @version [版本号, 2017年6月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController("lsycauditcertmetalistaction")
@Scope("request")
public class LsycAuditCertMetaListAction extends BaseController {
	private static final long serialVersionUID = 1L;

	/**
	 * 一页modal
	 */
	private DataGridModel<AuditRsShareMetadata> model;
	
    /**
     * 事项service
     */
    @Autowired
    private IAuditTask iAuditTask;
    
	/**
	 * 元数据配置接口
	 */
	@Autowired
	private IAuditRsShareMetadata iAuditRsShareMetadata;
    @Autowired
    private IMetaShareRelation iMetaShareRelation;
    @Autowired
    private IAuditRsShareMetadataSp auditRsShareMetadataSpService;
    @Autowired
    private IAuditSpTask auditSpTaskImpl;
    
    private Integer baseID ;
    
    
    public Integer getBaseID() {
        return baseID;
    }

    public void setBaseID(Integer baseID) {
        this.baseID = baseID;
    }

    private AuditTask detaBean;
	public AuditTask getDetaBean() {
        return detaBean;
    }

    public void setDetaBean(AuditTask detaBean) {
        this.detaBean = detaBean;
    }

    /**
	 * 共享材料配置信息主键
	 */
	private String portguid;

	/**
	 * 子表元数据对应的父表属性主键
	 */
	private String parentguid;

	@Override
	public void pageLoad() {
	    String taskguid=getRequestParameter("taskGuid");
	    parentguid=getRequestParameter("parentguid");
        AuditTask auditTask=iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
        detaBean = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult(); 
        if (auditTask!=null) {
            portguid = auditTask.getTask_id();
        }
        baseID = auditTask.getRow_id();
	}
	
	public void add(){
	    String taskguid=getRequestParameter("taskGuid");
	    AuditTask auditTask=iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
	    auditTask.setRow_id(baseID);
	    iAuditTask.updateAuditTask(auditTask);
	    addCallbackParam("msg", "保存成功");
	}
	public void syncform(){
	     String taskguid=getRequestParameter("taskGuid");
	     AuditTask auditTask=iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
	     Integer Row_id = auditTask.getRow_id()==null ?0:auditTask.getRow_id();
	     if(Row_id==null || Row_id==0){
	         addCallbackParam("msg", "没有设置基础ID");
	         return;
	     }
	     SqlConditionUtil sql = new SqlConditionUtil();
	     sql.eq("Row_id", Row_id+"");
	     List<AuditTask> list = iAuditTask.getAllTask(sql.getMap()).getResult();
	     for (AuditTask task : list) {
	         sql.clear();
	         sql.eq("MATERIALGUID", task.getTask_id());
	         List<AuditRsShareMetadata> mateList = iAuditRsShareMetadata.selectAuditRsShareMetadataByCondition(sql.getMap()).getResult();
	         if(mateList!=null &&mateList.size()>0){
	             for (AuditRsShareMetadata auditRsShareMetadata : mateList) {
	                 auditRsShareMetadata.setRowguid(UUID.randomUUID().toString());
	                 auditRsShareMetadata.setMaterialguid(auditTask.getTask_id());
	                 iAuditRsShareMetadata.addShareMetaData(auditRsShareMetadata);
	             }
	             addCallbackParam("msg", "关联成功！");
	             return;
	         }
	     }
	}
	
	public void syncformByTaskName(){
        String taskguid=getRequestParameter("taskGuid");
        AuditTask auditTask=iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.clear();
        sql.eq("MATERIALGUID", auditTask.getTask_id());
        List<AuditRsShareMetadata> mateList = iAuditRsShareMetadata.selectAuditRsShareMetadataByCondition(sql.getMap()).getResult();
        sql.clear();
        sql.eq("taskname", auditTask.getTaskname());
        List<AuditTask> list = iAuditTask.getAllTask(sql.getMap()).getResult();
        if(mateList!=null &&mateList.size()>0){
            for (AuditTask task : list) {
                Integer is_editafterimport = task.getIs_editafterimport()==null?0:task.getIs_editafterimport();
                Integer is_history = task.getIs_history()==null?1:task.getIs_history();
                Integer is_enable = task.getIs_enable()==null?0:task.getIs_enable();
                if(1==is_editafterimport &&1==is_enable &&!task.getTask_id().equals(auditTask.getTask_id())){
                    if(is_history==0 ||is_history==null || StringUtil.isBlank(is_history+"")){
                        for (AuditRsShareMetadata auditRsShareMetadata : mateList) {
                            auditRsShareMetadata.setRowguid(UUID.randomUUID().toString());
                            auditRsShareMetadata.setMaterialguid(task.getTask_id());
                            iAuditRsShareMetadata.addShareMetaData(auditRsShareMetadata);
                        }
                    }
                }
            }
            addCallbackParam("msg", "关联成功！");
        }else{
            addCallbackParam("msg", "事项不存在表单");
        }

	}
	

	/**
	 * 保存修改
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void saveAll() {
		// 获得页面表格记录
		List<AuditRsShareMetadata> list = getDataGridData().getWrappedData();
		for (AuditRsShareMetadata meta : list) {
			iAuditRsShareMetadata.updateShareMetaData(meta);
		}
		addCallbackParam("msg", "保存成功");
	}

	/**
	 * 删除选定 //TODO
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void deleteSelected() {
		List<String> selectKeys = getDataGridData().getSelectKeys();
		for (String key : selectKeys) {
//			 // 获取与共享字段关联关系
//	        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
//	        sqlConditionUtil.eq("metaguid",key);
//	        List<MetaShareRelation> relations=iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
//	        if (relations!=null&&!relations.isEmpty()) {
//	            // 去除原来的关联关系
//	            for (MetaShareRelation metaShareRelation : relations) {
//	                iMetaShareRelation.delete(metaShareRelation);
//	                // 判断主题字段是否还有其他关联关系，若没有，则删除
//	                sqlConditionUtil.clear();
//	                sqlConditionUtil.eq("metaspguid", metaShareRelation.getMetaspguid());
//	                List<MetaShareRelation> relations2=iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
//	                if (relations2==null||relations2.size()==0) {
//	                    auditRsShareMetadataSpService.deleteShareMetaDataByRowGuid(metaShareRelation.getMetaspguid());
//	                }
//	            }
//            } else {
//                // 去除主题下的局部字段
//                // 获取关联过这个事项的主题
//                sqlConditionUtil.clear();
//                sqlConditionUtil.eq("task_id", portguid);
//                sqlConditionUtil.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
//                sqlConditionUtil.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
//                sqlConditionUtil.eq("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
//                List<AuditTask> auditTasks = iAuditTask.getAllTask(sqlConditionUtil.getMap()).getResult();
//                if (auditTasks != null && !auditTasks.isEmpty()) {
//                    AuditRsShareMetadata databean=iAuditRsShareMetadata.selectShareMetaDataByGuid(key).getResult();
//                    for (AuditTask task : auditTasks) {
//                        SqlConditionUtil sqlConditionUtil1 = new SqlConditionUtil();
//                        sqlConditionUtil1.eq("taskid", task.getTask_id());
//                        List<AuditSpTask> auditSpTasks = auditSpTaskImpl.getAllAuditSpTask(sqlConditionUtil1.getMap()).getResult();
//                        if (auditSpTasks != null && !auditSpTasks.isEmpty()) {
//                            for (AuditSpTask sptask : auditSpTasks) {
//                                // 判断字段是否已经添加到了主题中，若是则删除
//                                List<AuditRsShareMetadataSp> rsShareMetadataSps=auditRsShareMetadataSpService.getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(sptask.getBusinessguid(), sptask.getPhaseguid(),databean.getFieldname());
//                                if (rsShareMetadataSps!=null&&!rsShareMetadataSps.isEmpty()) {
//                                    for (AuditRsShareMetadataSp auditRsShareMetadataSp : rsShareMetadataSps) {
//                                        if (ZwfwConstant.CONSTANT_INT_ZERO==auditRsShareMetadataSp.getIsshare()) {
//                                            auditRsShareMetadataSpService.deleteShareMetaDataByRowGuid(auditRsShareMetadataSp.getRowguid());
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
	        iAuditRsShareMetadata.deleteShareMetaDataByRowGuid(key);
		}
		addCallbackParam("msg", "删除成功");
	}

	public DataGridModel<AuditRsShareMetadata> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditRsShareMetadata>() {

				@Override
				public List<AuditRsShareMetadata> fetchData(int first, int pageSize, String sortField,
						String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					// 共享材料配置信息主键
					sql.eq("MATERIALGUID", portguid);
					// 子表元数据对应的父表属性主键
					if (StringUtil.isNotBlank(parentguid)&&!"undefined".equalsIgnoreCase(parentguid)) {
						sql.eq("PARENTGUID", parentguid);
					} else {
						sql.isBlank("PARENTGUID");
					}
					sortField = "ordernum";
					PageData<AuditRsShareMetadata> pageData = iAuditRsShareMetadata
							.selectAuditRsShareMetaDataPageData(AuditRsShareMetadata.class, sql.getMap(), first,
									pageSize, sortField, sortOrder)
							.getResult();
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}

			};
		}
		return model;
	}

	public String getPortguid() {
		return portguid;
	}

	public void setPortguid(String portguid) {
		this.portguid = portguid;
	}

	public String getParentguid() {
		return parentguid;
	}

	public void setParentguid(String parentguid) {
		this.parentguid = parentguid;
	}

}
