package com.epoint.lsyc.sharedata.portset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
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
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.api.ICodeMainServiceInternal;
import com.epoint.frame.service.metadata.mis.control.api.IControlService;
import com.epoint.frame.service.metadata.mis.tableinfo.api.ITableStructService;
import com.epoint.frame.service.metadata.mis.tableinfo.entity.TableStruct;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.DataTypeService9;
import com.epoint.lsyc.common.ZjjZwfwConstant;
import com.epoint.lsyc.common.util.LsZwfwBjsbConstant;
import com.epoint.lsyc.comprehensivewindow.meta.IAuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.IAuditRsShareMetadataSpClass;
import com.epoint.lsyc.comprehensivewindow.meta.IMetaShare;
import com.epoint.lsyc.comprehensivewindow.meta.IMetaShareRelation;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSpClass;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShare;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

/**
 * 证照元数据新增
 * 
 * @作者 Administrator
 * @version [版本号, 2017年7月6日]
 
 
 */
@RestController("lsycauditcertmetaaddaction")
@Scope("request")
public class LsycAuditCertMetaAddAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 控件显示宽度
     */
    private static final int DEFAULT_CONTROL_WIDTH = 2;
    private List<SelectItem> validationType = null;
    /**
     * 获取显示控件的service
     */
    @Autowired
    private IControlService controlservice;

    /**
     * 获取代码字典的service
     */
    @Autowired
    private ICodeMainServiceInternal codemain;

    /**
     * 获取数据类型的service
     */
    private DataTypeService9 dataTypeService = new DataTypeService9();

    /**
     * 数据类型
     */
    private List<SelectItem> dataType;

    /**
     * 字段显示类型
     */
    private List<SelectItem> displayType;
    /**
     * 时间格式
     */
    private List<SelectItem> dateformatType;
    /**
    * 获取表字段字典的service
    */
    @Autowired
    private ITableStructService tablestruct;

    /**
     * 选择代码源代码（下拉框）
     */
    private List<SelectItem> codesource;
    /**
     * 选择字段（下拉框）
     */
    private List<SelectItem> relatefield;

    /**
     * 表的sql名
     */
    private String sqltablename;

    /**
     * 表中文名
     */
    private String tablename;

    private String controlwidth;

    /**
     * 初始字段类型
     */
    private String fieldtypeOrg = "";

    private AuditRsShareMetadata databean;
    private AuditTask auditTask;

    @Autowired
    private IAuditRsShareMetadata iAuditRsShareMetadata;
    @Autowired
    private IMetaShare iMetaShare;

    @Autowired
    private IMetaShareRelation iMetaShareRelation;
    /**
     * 事项service
     */
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditSpTask auditSpTaskImpl;
    @Autowired
    private IAuditRsShareMetadataSp auditRsShareMetadataSpService;

    @Autowired
    private IAuditRsShareMetadataSpClass iAuditRsShareMetadataSpClass;
    private String dataTypeSel;

    private String portguid;
    
    // 修改前的字段英文名称，若不是修改则为空
    private String oldfiledname;
    // 修改前的字段共享字段rowguid，若不是修改则为空
    private String oldshareguid;
    
    @Override
    public void pageLoad() {
        // 获取当前字段的guid
        String guid = getRequestParameter("guid");
        String taskguid = getRequestParameter("taskGuid");
        auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        if (auditTask != null) {
            portguid = auditTask.getStr("task_id");
        }
        // 新增
        if (StringUtil.isBlank(guid)) {
            addCallbackParam("add", true);
            initTypeA();
        }
        else {// 修改
            databean = iAuditRsShareMetadata.selectShareMetaDataByGuid(guid).getResult();
            if (databean!=null) {
            	initTypeU();
                if ("projectguid".equalsIgnoreCase(databean.getFieldname())) {
                    addCallbackParam("projectguid", "1");
                }
                oldfiledname=databean.getFieldname();
                oldshareguid=databean.getStr("shareguid");
			}
            addCallbackParam("add", false);
        }
        if (databean == null) {
            init();
        }
    }

    public void init() {
        initTypeA();
        databean = new AuditRsShareMetadata();
        String parentguid = getRequestParameter("parentguid");
        // 是否必填
        databean.setNotnull(ZwfwConstant.CONSTANT_STR_ONE);
        // 是否在页面中显示
        databean.setDisplayInadd(ZwfwConstant.CONSTANT_STR_ONE);
        // 在表单中显示
        databean.setDispinforms(ZwfwConstant.CONSTANT_STR_ONE);
        // 是否关联表单字段
        databean.setIsrelatetable(ZwfwConstant.CONSTANT_STR_ZERO);
        // 控件显示宽度
        databean.setControlwidth(ZwfwConstant.CONSTANT_INT_ONE);
        // 字段类型
        databean.setFieldtype("nvarchar");
        // 显示类型
        databean.setFielddisplaytype("textbox");
        // 是否关联子表表单
        databean.setIsRelateSubTable(ZwfwConstant.CONSTANT_STR_ZERO);
        // 如果能获取到parentguid，说明是子表属性
        if (StringUtil.isNotBlank(parentguid) && !"undefined".equalsIgnoreCase(parentguid)) {
            databean.setParentguid(parentguid);
        }
    }

    /**
     * 给表添加1个字段
     * 
     * @param type
     *            类型：1 仅操作结构表 ;2 同步sql表
     * @param 无
     * @return 无
     */
    public void add() {
        databean.setOperatedate(new Date());
        databean.setRowguid(UUID.randomUUID().toString());
        databean.setMaterialguid(portguid);
        // 判断数据
        String msg = judge();
        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("msg", msg);
            return;
        }
        iAuditRsShareMetadata.addShareMetaData(databean);
        //addCallbackParam("msg", "保存成功");
        // 处理元数据共享关系
        //DealingCorrelation();
        init();
    }

    /**
     * 保存修改
     * 
     * @param type
     *            类型：1 仅操作结构表 ;2 同步sql表
     * @param 无
     * @return 无
     */
    public void update() {
        // 判断数据
        String msg = judge();
        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("msg", msg);
            return;
        }
        iAuditRsShareMetadata.updateShareMetaData(databean);
        //DealingCorrelation();
    }

    public String judge() {
        String msg = LsZwfwBjsbConstant.CONSTANT_STR_NULL;
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("materialguid", portguid);
        sql.eq("fieldchinesename", databean.getFieldchinesename());
        // 子表元数据对应的父表属性主键
        if (StringUtil.isNotBlank(databean.getParentguid())) {
            sql.eq("parentguid", databean.getParentguid());
        }
        else {
            sql.isBlank("parentguid");
        }
        List<AuditRsShareMetadata> list = iAuditRsShareMetadata.selectAuditRsShareMetadataByCondition(sql.getMap())
                .getResult();
        if (list != null && list.size() > 0) {
            if (!databean.getRowguid().equals(list.get(0).getRowguid())) {
                msg = "中文名称不允许重复";
            }
        }
        else {
            SqlConditionUtil sqlcondition = new SqlConditionUtil();
            sqlcondition.eq("materialguid", portguid);
            sqlcondition.eq("fieldname", databean.getFieldname());
            // 子表元数据对应的父表属性主键
            if (StringUtil.isNotBlank(databean.getParentguid())) {
                sqlcondition.eq("parentguid", databean.getParentguid());
            }
            else {
                sql.isBlank("parentguid");
            }
            List<AuditRsShareMetadata> list1 = iAuditRsShareMetadata
                    .selectAuditRsShareMetadataByCondition(sqlcondition.getMap()).getResult();
            if (list1 != null && list1.size() > 1) {
                msg = "英文名称不允许重复";
            }
        }
        return msg;
    }

    public void DealingCorrelation() {
        if (StringUtil.isNotBlank(databean.getStr("shareguid"))) {
            // 若关联了共享字段，处理元数据共享关系
            if (StringUtil.isNotBlank(oldshareguid)) {
                // 原来存在关联关系，更新或清除关系
                if (oldshareguid.equals(databean.getStr("shareguid"))) {
                    // 是原来已有的关系，更新关联的主题字段
                    updateAuditRsShareMetadataSp(oldshareguid);
                }
                else {
                    // 不是原来已有的关系，关联关系变更，更新关联的所有主题字段，更新已有的关联关系
                	// 获取原来与共享字段关联关系
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("metaguid", databean.getRowguid());
                    List<MetaShareRelation> relations = iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
                    Set<String> businessguids=new HashSet<String>();
                    for (MetaShareRelation metaShareRelation : relations) {
                    	AuditRsShareMetadataSp auditRsShareMetadataSp=auditRsShareMetadataSpService.getMetaSp(metaShareRelation.getMetaspguid()).getResult();
                    	if (auditRsShareMetadataSp!=null) {
                    		// 相同主题不做重复处理
                    		if (!businessguids.contains(auditRsShareMetadataSp.getBusinessGuid())) {
                    			businessguids.add(auditRsShareMetadataSp.getBusinessGuid());
                    			// 获取共享字段
                    	        MetaShare metaShare = iMetaShare.getMetaShare(databean.getStr("shareguid"));
                    			// 判断原来的关联关系是否还有其他事项的字段关联
                            	sqlConditionUtil.clear();
                            	sqlConditionUtil.eq("shareguid", oldshareguid);
                            	sqlConditionUtil.notin("metaguid", "'"+databean.getRowguid()+"'");
                            	sqlConditionUtil.in("metaspguid", "SELECT rowguid from audit_rs_share_metadata_sp where businessGuid='"+auditRsShareMetadataSp.getBusinessGuid()+"'");
                            	List<MetaShareRelation> otherrelations = iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
                            	// 若还有，新增一个主题字段
                            	if (otherrelations!=null&&!otherrelations.isEmpty()) {
                            		// 新增关联关系及对应主题字段，shareguid为空则没有关联共享字段，为局部主题字段
                                    AuditRsShareMetadataSp shareMetaSp=initAuditRsShareMetadataSp(auditRsShareMetadataSp.getBusinessGuid(), auditRsShareMetadataSp.getPhaseguid(), auditTask.getTask_id(),metaShare);
                                    auditRsShareMetadataSpService.insertRecord(shareMetaSp);
                                    // 修改旧的关联关系
                                    for (MetaShareRelation shareRelation : relations) {
                                    	shareRelation.setShareguid(databean.getStr("shareguid"));
                                    	shareRelation.setMetaspguid(shareMetaSp.getRowguid());
            	                        iMetaShareRelation.update(shareRelation);
            	                    }
            					} else {
            						// 若没有，则删除原来的主题字段
            						auditRsShareMetadataSpService.deleteShareMetaSpData(metaShareRelation.getMetaspguid());
            						// 更新原有的关联关系
            	                    for (MetaShareRelation shareRelation : relations) {
            	                        shareRelation.setShareguid(databean.getStr("shareguid"));
            	                        // 关联到主题下其他事项字段关联的相同共享主题字段
            	                        sqlConditionUtil.clear();
                                    	sqlConditionUtil.eq("shareguid", databean.getStr("shareguid"));
                                    	sqlConditionUtil.notin("metaguid", "'"+databean.getRowguid()+"'");
                                    	sqlConditionUtil.in("metaspguid", "SELECT rowguid from audit_rs_share_metadata_sp where businessGuid='"+auditRsShareMetadataSp.getBusinessGuid()+"'");
                                    	otherrelations = iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
                                    	if (otherrelations!=null&&!otherrelations.isEmpty()) {
											// 若有主题下其他事项字段关联的相同共享主题字段，则关联上去
                                    		shareRelation.setMetaspguid(otherrelations.get(0).getMetaspguid());
                	                        iMetaShareRelation.update(shareRelation);
										} else {
											// 若没有则新增
											AuditRsShareMetadataSp shareMetaSp=initAuditRsShareMetadataSp(auditRsShareMetadataSp.getBusinessGuid(), auditRsShareMetadataSp.getPhaseguid(), auditTask.getTask_id(),metaShare);
		                                    auditRsShareMetadataSpService.insertRecord(shareMetaSp);
		                                    shareRelation.setMetaspguid(otherrelations.get(0).getMetaspguid());
                	                        iMetaShareRelation.update(shareRelation);
										}
            	                    }
            					}
							}
						} else {
							// 关联关系找不到主题字段，删除关系
							iMetaShareRelation.delete(metaShareRelation);
						}
					}
                }
            }
            else {
                // 原来不存在关联关系，现在关联关系
                // 删除主题中关联的单个事项字段
            	SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("MATERIALGUID", portguid);
                sqlConditionUtil.eq("FIELDNAME", oldfiledname);
                SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditRsShareMetadataSp.class);
                List<AuditRsShareMetadataSp> sps = sqlManageUtil.getListByCondition(AuditRsShareMetadataSp.class, sqlConditionUtil.getMap());
                for (AuditRsShareMetadataSp auditRsShareMetadataSp : sps) {
                	auditRsShareMetadataSpService.deleteShareMetaDataByRowGuid(auditRsShareMetadataSp.getRowguid());
				}
                // 新增关联关系及对应主题字段，shareguid不为空则有关联共享字段，为全局主题字段
                addAuditRsShareMetadataSp(databean.getStr("shareguid"));
            }
        }
        else {
            // 新增关联关系及对应主题字段，shareguid为空则没有关联共享字段，为局部主题字段
            addAuditRsShareMetadataSp(databean.getStr("shareguid"));
        }
        //        auditRsShareMetadataSpService.initSPShareMetaFromTask(sptask.getBusinessguid(),sptask.getPhaseguid());
    }

    public void updateAuditRsShareMetadataSp(String shareguid) {
        // 获取共享字段
        MetaShare metaShare = iMetaShare.getMetaShare(shareguid);
        List<AuditRsShareMetadataSp> metadataSps = auditRsShareMetadataSpService.getMetaSpListByMetaguid(databean.getRowguid()).getResult();
        for (AuditRsShareMetadataSp auditRsShareMetadataSp : metadataSps) {
            auditRsShareMetadataSp = setAuditRsShareMetadataSp(auditRsShareMetadataSp, metaShare);
            auditRsShareMetadataSpService.updateShareMetaSpData(auditRsShareMetadataSp);
        }
    }

    public void addAuditRsShareMetadataSp(String shareguid) {
        // 获取共享字段
        MetaShare metaShare = null;
        if (StringUtil.isNotBlank(shareguid)) {
            metaShare = iMetaShare.getMetaShare(shareguid);
        }
        // 获取关联过这个事项的主题
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("taskbasicid", portguid);
        sqlConditionUtil.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
        sqlConditionUtil.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
        sqlConditionUtil.eq("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
        List<AuditTask> auditTasks = iAuditTask.getAllTask(sqlConditionUtil.getMap()).getResult();
        if (auditTasks != null && !auditTasks.isEmpty()) {
            for (AuditTask task : auditTasks) {
                SqlConditionUtil sqlConditionUtil1 = new SqlConditionUtil();
                sqlConditionUtil1.eq("taskid", task.getTask_id());
                List<AuditSpTask> auditSpTasks = auditSpTaskImpl.getAllAuditSpTask(sqlConditionUtil1.getMap()).getResult();
                if (auditSpTasks != null && !auditSpTasks.isEmpty()) {
                    for (AuditSpTask sptask : auditSpTasks) {
                    	AuditRsShareMetadataSp shareMetaSp=initAuditRsShareMetadataSp(sptask.getBusinessguid(), sptask.getPhaseguid(), sptask.getTaskid(),metaShare);
                        if (metaShare != null) {
                            int count = iMetaShareRelation.getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(sptask.getBusinessguid(), sptask.getPhaseguid(), shareguid);
                            if (count == 0) {
                                auditRsShareMetadataSpService.insertRecord(shareMetaSp);
                            }
                            else {
                                shareMetaSp = auditRsShareMetadataSpService.getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(sptask.getBusinessguid(), sptask.getPhaseguid(), shareguid);
                            }
                            // 新增关联关系
                            MetaShareRelation shareRelation = new MetaShareRelation();
                            shareRelation.setRowguid(UUID.randomUUID().toString());
                            shareRelation.setMetaspguid(shareMetaSp.getRowguid());
                            shareRelation.setShareguid(metaShare.getRowguid());
                            shareRelation.setMetaguid(databean.getRowguid());
                            iMetaShareRelation.insertRecord(shareRelation);
                            auditRsShareMetadataSpService.updateShareMetaSpData(shareMetaSp);
                        }
                        else {
                            // 若现在没有关联共享字段，清除元数据共享关系
                            // 获取与共享字段关联关系
                            sqlConditionUtil.clear();
                            sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("metaguid", databean.getRowguid());
                            List<MetaShareRelation> relations = iMetaShareRelation
                                    .getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
                            if (relations != null && !relations.isEmpty()) {
                                for (MetaShareRelation metaShareRelation : relations) {
                                    iMetaShareRelation.delete(metaShareRelation);
                                    // 判断主题字段是否还有其他关联关系，若没有，则删除
                                    //                                    sqlConditionUtil.clear();
                                    //                                    sqlConditionUtil.eq("metaspguid", metaShareRelation.getMetaspguid());
                                    //                                    List<MetaShareRelation> relations2=iMetaShareRelation.getMetaShareRelationListByCondition(sqlConditionUtil.getMap());
                                    //                                    if (relations2==null||relations2.size()==0) {
                                    //                                        auditRsShareMetadataSpService.deleteShareMetaDataByRowGuid(metaShareRelation.getMetaspguid());
                                    //                                    }
                                }
                            }
                            // 若事项添加在了某个主题中，字段需要加到主题中去
                            // 判断字段是否已经添加到了主题中，若是则更新
                            List<AuditRsShareMetadataSp> rsShareMetadataSps = auditRsShareMetadataSpService
                                    .getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(
                                            sptask.getBusinessguid(), sptask.getPhaseguid(), databean.getFieldname());
                            if (rsShareMetadataSps != null && !rsShareMetadataSps.isEmpty()) {
                                shareMetaSp.setRowguid(rsShareMetadataSps.get(0).getRowguid());
                                auditRsShareMetadataSpService.updateShareMetaSpData(shareMetaSp);
                            }
                            else {
                                auditRsShareMetadataSpService.insertRecord(shareMetaSp);
                            }
                        }
                    }
                }
            }
        }
    }

    public AuditRsShareMetadataSp initAuditRsShareMetadataSp (String businessguid,String phaseguid,String taskid,MetaShare metaShare) {
    	AuditRsShareMetadataSp shareMetaSp = new AuditRsShareMetadataSp();
        BeanUtils.copyProperties(databean, shareMetaSp, "Sql_TableName");
        shareMetaSp.setBusinessGuid(businessguid);
        shareMetaSp.setRowguid(UUID.randomUUID().toString());
        shareMetaSp.setSql_TableName("AUDIT_RS_SHARE_METADATA_SP");
        shareMetaSp.setPhaseguid(phaseguid);
        shareMetaSp.setIsshare(ZwfwConstant.CONSTANT_INT_ONE);
        shareMetaSp.setEffictiverange(ZjjZwfwConstant.EFFICTIVE_GLOBAL);
        shareMetaSp.setDisplayInadd(databean.getDispInadd());
        //【代码组2019-10-31】
        // 外网控件显示宽度
        shareMetaSp.setControlwidth(DEFAULT_CONTROL_WIDTH);
        //内网控件左侧显示宽度
        shareMetaSp.set("cncontrolwidth", 22);
        //内网控件显示宽度
        shareMetaSp.set("rncontrolwidth", 22);
        // 必填项
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(databean.getNotnull())) {
            shareMetaSp.setNotnull(databean.getNotnull());
        }
        /**[代码组2019-10-31]新增元数据分类*/
        //获取该事项分类
        AuditRsShareMetadataSpClass spClass = iAuditRsShareMetadataSpClass.selectAuditRsShareMetadataSpClassByTaskId(phaseguid,taskid);
        //如果存在该分类,获取该分类下的所有主题元数据
        if (spClass != null) {
        	shareMetaSp.setClassguid(spClass.getRowguid());
            List<AuditRsShareMetadataSp> rsShareMetadataSpList = auditRsShareMetadataSpService.selectAuditRsShareMetadataByBusinessguidAndClassguid(businessguid,phaseguid, spClass.getRowguid()).getResult();
            //如果存在元数据,按照元数据ordernum字段排序,取最大值+1作为新增字段的ordernum
            if (!rsShareMetadataSpList.isEmpty()) {
                rsShareMetadataSpList.sort((o1, o2) -> o1.getOrdernum().compareTo(o2.getOrdernum()));
                shareMetaSp.setOrdernum(rsShareMetadataSpList.get(0).getOrdernum() + 1);
            }
            //不存在则默认1
            else {
                shareMetaSp.setOrdernum(1);
            }
        }
        if (metaShare != null) {
            shareMetaSp.setFieldname(metaShare.getFieldname());
            shareMetaSp.setFieldchinesename(metaShare.getFieldchinesename());
            // 字段是否共享
            shareMetaSp.setIsshare(ZwfwConstant.CONSTANT_INT_ONE);
        } else {
        	shareMetaSp.setIsshare(ZwfwConstant.CONSTANT_INT_ZERO);
        }
        return shareMetaSp;
    }
    
    public AuditRsShareMetadataSp setAuditRsShareMetadataSp(AuditRsShareMetadataSp auditRsShareMetadataSp,MetaShare metaShare) {
        if (metaShare != null) {
            // 字段英文名
            auditRsShareMetadataSp.setFieldname(metaShare.getFieldname());
            // 字段中文名
            auditRsShareMetadataSp.setFieldchinesename(metaShare.getFieldchinesename());
            // 字段是否共享
            auditRsShareMetadataSp.setIsshare(ZwfwConstant.CONSTANT_INT_ONE);
        }
        else {
            // 字段是否共享
            auditRsShareMetadataSp.setIsshare(ZwfwConstant.CONSTANT_INT_ZERO);
        }

        //        if (auditRsShareMetadataSp.getControlwidth()<databean.getControlwidth()) {
        //            auditRsShareMetadataSp.setControlwidth(databean.getControlwidth());
        // 

        // 数据类型
        auditRsShareMetadataSp.setFieldtype(databean.getFieldtype());
        // 显示类型
        auditRsShareMetadataSp.setFielddisplaytype(databean.getFielddisplaytype());
        // 数据代码
        if (StringUtil.isBlank(databean.getDatasource_codename())
                && StringUtil.isNotBlank(auditRsShareMetadataSp.getDatasource_codename())) {
            auditRsShareMetadataSp.setDatasource_codename(null);
        }
        else if (StringUtil.isNotBlank(databean.getDatasource_codename())) {
            auditRsShareMetadataSp.setDatasource_codename(databean.getDatasource_codename());
        }
        // 必填项，判断其他事项的这个字段是否有选择必填
        List<AuditRsShareMetadata> metadatas = auditRsShareMetadataSpService
                .seletemetadataListByMetasharerelationMetaspguidAndShareguid(auditRsShareMetadataSp.getRowguid(),
                        metaShare.getRowguid());
        // 是否存在必填项
        boolean isnotnull = false;
        // 在表单中显示
        boolean isshow = false;
        if (metadatas != null && metadatas.size() > 0) {
            for (AuditRsShareMetadata auditRsShareMetadata : metadatas) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsShareMetadata.getNotnull())) {
                    isnotnull = true;
                }
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsShareMetadata.getDispinforms())) {
                    isshow = true;
                }
            }
        }
        if (isnotnull) {
            auditRsShareMetadataSp.setNotnull(ZwfwConstant.CONSTANT_STR_ONE);
        }
        else {
            auditRsShareMetadataSp.setNotnull(ZwfwConstant.CONSTANT_STR_ZERO);
        }
        if (isshow) {
            auditRsShareMetadataSp.setDispinforms(ZwfwConstant.CONSTANT_STR_ONE);
        }
        else {
            auditRsShareMetadataSp.setDispinforms(ZwfwConstant.CONSTANT_STR_ZERO);
        }
        // 在页面中显示
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(databean.getDispInadd())) {
            auditRsShareMetadataSp.setDisplayInadd(databean.getDispInadd());
        }
        // 字段长度
        if (auditRsShareMetadataSp.getMaxLength() > databean.getMaxLength()) {
            auditRsShareMetadataSp.setMaxLength(databean.getMaxLength());
        }
        // 字段校验
        if (StringUtil.isBlank(auditRsShareMetadataSp.getValidation())) {
            auditRsShareMetadataSp.setValidation(databean.getValidation());
        }
        // 时间格式
        auditRsShareMetadataSp.setDateformat(databean.getDateformat());
        // 外网控件显示宽度
        if (StringUtil.isBlank(auditRsShareMetadataSp.getControlwidth())||auditRsShareMetadataSp.getControlwidth()==0) {
        	auditRsShareMetadataSp.setControlwidth(DEFAULT_CONTROL_WIDTH);
		}
        //内网控件左侧显示宽度
        if (StringUtil.isBlank(auditRsShareMetadataSp.getInt("cncontrolwidth"))||auditRsShareMetadataSp.getInt("cncontrolwidth")==0) {
        	auditRsShareMetadataSp.set("cncontrolwidth", 22);
		}
        //内网控件显示宽度
        if (StringUtil.isBlank(auditRsShareMetadataSp.getInt("rncontrolwidth"))||auditRsShareMetadataSp.getInt("rncontrolwidth")==0) {
        	auditRsShareMetadataSp.set("rncontrolwidth", 22);
		}
        // 是否需要更新分类
        boolean boo = false;
        AuditRsShareMetadataSpClass spClass = null;
        if (StringUtil.isNotBlank(auditRsShareMetadataSp.getClassguid())) {
        	spClass = iAuditRsShareMetadataSpClass.getAuditRsShareMetadataSpClass(auditRsShareMetadataSp.getClassguid());
        	if (spClass==null || !spClass.getBusinessGuid().equals(auditRsShareMetadataSp.getBusinessGuid())) {
				boo = true;
			}
		} else {
			boo = true;
		}
        if (boo) {
        	//获取该事项分类
	        spClass = iAuditRsShareMetadataSpClass.selectAuditRsShareMetadataSpClassByTaskId(auditRsShareMetadataSp.getPhaseguid(),auditTask.getTask_id());
	        //如果存在该分类,获取该分类下的所有主题元数据
	        if (spClass != null) {
	        	auditRsShareMetadataSp.setClassguid(spClass.getRowguid());
	        }
		}
        if (StringUtil.isBlank(auditRsShareMetadataSp.getOrdernum())||auditRsShareMetadataSp.getOrdernum()==0) {
        	List<AuditRsShareMetadataSp> rsShareMetadataSpList = auditRsShareMetadataSpService
                    .selectAuditRsShareMetadataByBusinessguidAndClassguid(auditRsShareMetadataSp.getBusinessGuid(),
                    		auditRsShareMetadataSp.getPhaseguid(), spClass.getRowguid())
                    .getResult();
            //如果存在元数据,按照元数据ordernum字段排序,取最大值+1作为新增字段的ordernum
            if (!rsShareMetadataSpList.isEmpty()) {
                rsShareMetadataSpList.sort((o1, o2) -> o1.getOrdernum().compareTo(o2.getOrdernum()));
                auditRsShareMetadataSp.setOrdernum(rsShareMetadataSpList.get(0).getOrdernum() + 1);
            }
            //不存在则默认1
            else {
            	auditRsShareMetadataSp.setOrdernum(1);
            }
		}
        return auditRsShareMetadataSp;
    }

    public List<SelectItem> getDataType() {
        if (dataType == null) {
            dataType = DataUtil.convertMap2ComboBox(dataTypeService.getDataTypeSelectItem(false));
        }
        return dataType;
    }

    public String getSqltablename() {
        return sqltablename;
    }

    public void setSqltablename(String sqltablename) {
        this.sqltablename = sqltablename;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<SelectItem> getCodesource() {
        if (codesource == null) {
            codesource = DataUtil
                    .convertMap2ComboBox(ICodeMainService.changeCodeSelectItems(codemain.listCodeMain(), true));
            codesource.remove(0);
        }
        return codesource;
    }

    public List<SelectItem> getRelatefield() {
        if (relatefield == null) {
        		relatefield = DataUtil.convertMap2ComboBox(
                        (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "超限表单配置字段", null, true));
        }
        return relatefield;
    }

    public String getControlwidth() {
        return controlwidth;
    }

    public void setControlwidth(String controlwidth) {
        this.controlwidth = controlwidth;
    }

    public String getFieldtypeOrg() {
        return fieldtypeOrg;
    }

    public void setFieldtypeOrg(String fieldtypeOrg) {
        this.fieldtypeOrg = fieldtypeOrg;
    }

    public AuditRsShareMetadata getDatabean() {
        return databean;
    }

    public void setDatabean(AuditRsShareMetadata databean) {
        this.databean = databean;
    }

    public List<SelectItem> getDisplayType() {
        if (StringUtil.isNotBlank(dataTypeSel)) {
            displayType = DataUtil.convertMap2ComboBox(controlservice.getDispSelectItemByType(dataTypeSel));
            displayType.remove(0);
        }
        return displayType;
    }

    public List<SelectItem> getDateformatType() {
        if (dateformatType == null) {
            dateformatType = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "时间格式", null, false));
        }
        return dateformatType;
    }

    public void setDateformatType(List<SelectItem> dateformatType) {
        this.dateformatType = dateformatType;
    }

    public String getDataTypeSel() {
        return dataTypeSel;
    }

    public void setDataTypeSel(String dataTypeSel) {
        this.dataTypeSel = dataTypeSel;
    }

    /**
     * 初始化字段显示类型
     * 
     * @param e
     *            下拉列表的切换事件
     * @param 无
     * @return 无
     */
    public void initTypeA() {
        // 字段类型
        String fieldType = DataTypeService9.DATATYPE_NVARCHAR;
        // 根据选中的数据类型,查找对应能够显示该类型的控件集
        displayType = DataUtil.convertMap2ComboBox(controlservice.getDispSelectItemByType(fieldType));
        displayType.remove(0);
    }

    /**
     * 初始化字段显示类型
     * 
     * @param e
     *            下拉列表的切换事件
     * @param 无
     * @return 无
     */
    public void initTypeU() {
        // 字段类型
        String fieldType = databean.getFieldtype();
        // 获取该字段类型对应显示的控件列表
        displayType = DataUtil.convertMap2ComboBox(controlservice.getDispSelectItemByType(fieldType));
        displayType.remove(0);
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getValidationType() {
        if (validationType == null) {
            validationType = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "字段校验", null, false));
        }
        return validationType;
    }

	public String getOldfiledname() {
		return oldfiledname;
	}

	public void setOldfiledname(String oldfiledname) {
		this.oldfiledname = oldfiledname;
	}

	public String getOldshareguid() {
		return oldshareguid;
	}

	public void setOldshareguid(String oldshareguid) {
		this.oldshareguid = oldshareguid;
	}
}
