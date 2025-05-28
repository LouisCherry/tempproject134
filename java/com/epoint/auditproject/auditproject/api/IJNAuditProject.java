package com.epoint.auditproject.auditproject.api;

import com.epoint.auditproject.zjxt.entity.AuditProjectProcessZjxt;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.EvainstanceCk;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IJNAuditProject extends Serializable {

    /**
     * @return int    返回类型
     * @throws
     * @Description: 获取AuditExpress数量
     * @author male
     * @date 2019年1月23日 下午4:16:15
     */
    public int getAuditExpressCount(String projectguid);

    /**
     * @return String    返回类型
     * @throws
     * @Description: 根据configName获取attachconnect
     * @author male
     * @date 2019年1月24日 上午9:18:02
     */
    public String getAttachConnect(String configName);

    /**
     * @return String    返回类型
     * @throws
     * @Description: 判断该材料是否存在
     * @author male
     * @date 2019年1月24日 上午9:20:45
     */
    public String getMaterialStatus(String cliengguid);

    /**
     * @return int    返回类型
     * @throws
     * @Description: 更新材料状态
     * @author male
     * @date 2019年1月24日 上午9:22:38
     */
    public int updateMaterialStatus(String cliengguid);

    /**
     * @return String    返回类型
     * @throws
     * @Description: 获取ReaderType
     * @author male
     * @date 2019年1月24日 上午9:34:12
     */
    public String getReaderType(String windowguid);

    /**
     * 根据task_id获取电子表单的id
     *
     * @param taskId
     * @return
     */
    public String getFormtableidByTaskid(String taskId);

    /**
     * 根据task_id获取事项信息
     *
     * @param taskId
     * @return
     */
    public AuditTask getTaskinfoByTaskid(String taskId);

    AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String var1, Map<String, String> var2,
                                                                      Integer var3, Integer var4, String var5, String var6, String var7);

    public List<Record> getEpointsformTableStruct(String tableid, String fielddisplaytype);

    public List<Record> getEpointsformTableStructbyradioandcheck(String tableid);

    public List<Record> getEpointsformTableStructbyDatepicker(String tableid);

    public Record getTableId(String sql_tablename);

    /**
     * 更新是否容缺字段
     *
     * @param rowguid
     * @param str
     * @return
     * @authory shibin
     * @version 2019年10月23日 下午5:54:52
     */
    public int updateProjectIsrongque(String rowguid, String str);

    /**
     * 获取办件信息
     *
     * @param projectGuid
     * @return
     * @authory shibin
     * @version 2019年10月23日 下午5:54:41
     */
    public AuditProject getProjectByprojectguid(String projectGuid);

    /**
     * 更新承诺文件标识
     *
     * @param projectGuid
     * @param viewData
     * @return
     * @authory shibin
     * @version 2019年10月23日 下午5:55:12
     */
    public int updatePromisefileguid(String projectGuid, String viewData);

    /**
     * 是否已发送
     *
     * @param projectguid
     * @param string
     * @authory shibin
     * @version 2019年10月24日 下午8:10:08
     */
    public int updateProjectIsreminded(String projectguid, String string);

    /**
     * 获取日期差
     *
     * @param projectguid2
     * @return
     */
    public int getBanjieAndProminseDays(String projectguid2);

    /**
     * 获取申请人信息
     *
     * @param projectguid2
     * @return
     */
    public Record getApplyerinfo(String projectguid2);

    /**
     * @description
     * @author shibin
     * @date 2020年6月8日 下午3:33:11
     */
    public String getScancodeByguid(String projectGuid);


    /**
     * @description
     * @author shibin
     * @date 2020年6月8日 下午3:33:11
     */
    public String getJstScancodeByguid(String projectGuid);

    /**
     * @description
     * @author shibin
     * @date 2020年6月9日 下午5:08:45
     */
    public int updateFrameCliengguid(String qRcodeAttachguid, String cliengguid);

    /**
     * @param proguid
     * @return int    返回类型
     * @throws
     * @Description: 更新材料状态
     * @author male
     * @date 2019年1月24日 上午9:22:38
     */
    public int updateMaterialStatus(String cliengguid, String proguid);

    /**
     * [获取电子证照二维码]
     *
     * @param projectGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getScanLegalcodeByguid(String projectGuid);

    public List<AuditProjectProcessZjxt> findProcessList(String string);

    public AuditProjectZjxt getAuditProjectZjxtByRowGuid(String requestParameter);

    /**
     * 获取办件量统计
     *
     * @param dataBean
     * @param applydateStart
     * @param applydateEnd
     * @param finishdateStart
     * @param finishdateEnd
     * @param areacode
     * @return
     */
    List<Record> findDataList(AuditProject dataBean, String applydateStart, String applydateEnd, String finishdateStart
            , String finishdateEnd, String areacode, List<String> searchOulist, int first, int pagesize);
    
    List<Record> findEvaluateDataList(EvainstanceCk dataBean, String applydateStart, String applydateEnd, String finishdateStart
    		, String finishdateEnd, String areacode, List<String> searchOulist, int first, int pagesize);

    Integer getEvaluateDataCount(String areacode, List<String> searchOulist);
    
    Integer getAuditDataCount(String areacode, List<String> searchOulist);

    int findTotalDataCount(AuditProject dataBean, String applydateStart, String applydateEnd, String finishdateStart
            , String finishdateEnd, String areacode, List<String> searchOulist);
    
    int findEvaluateTotalDataCount(EvainstanceCk dataBean, String applydateStart, String applydateEnd, String finishdateStart
    		, String finishdateEnd, String areacode, List<String> searchOulist);

    /**
     * 获取办件量详情列表
     * @param dataBean
     * @param applyDateStart
     * @param applyDateEnd
     * @param finishDateStart
     * @param finishDateEnd
     * @param areacode
     * @param ouguids
     * @param first
     * @param pageSize
     * @return
     */
    List<AuditProject> getAuditProjectDataPageData(AuditProject dataBean, String applyDateStart, String applyDateEnd
            , String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids, int first
            , int pageSize, String sortField, String sortOrder);

    /**
     * 获取办件量详情数量
     * @param dataBean
     * @param applyDateStart
     * @param applyDateEnd
     * @param finishDateStart
     * @param finishDateEnd
     * @param areacode
     * @param ouguids
     * @return
     */
    int getAuditProjectDataCount(AuditProject dataBean, String applyDateStart, String applyDateEnd
            , String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids);

    /**
     * 查询下一级部门（包括自己）
     * @param areacode
     * @param leftTreeNodeGuid
     * @return
     */
    List<String> findOUGuidList(String areacode, String leftTreeNodeGuid);
    
    void updateCrtInfo(String certrowguid);

	List<Record> getSpglDantiInfoBySubappguid(String projectguid);
	
	AuditProject getAuditProjectByCertrowguid(String certrowguid);


    /**
     * @description: 条件获取导入2表数量
     * @param: map
     * @return: java.lang.Integer
     */
    public Integer getLcprojectTwoListCount(Map<String,String> map);
    /**
     * @description: 条件获取导入10表数量
     * @param: map
     * @return: java.lang.Integer
     */
    public Integer getLcprojectTenListCount(Map<String,String> map);

    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField, String sortOrder);
}
