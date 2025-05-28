package com.epoint.basic.auditproject.auditproject.inter;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditproject.auditproject.domain.AuditProReport;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectCK;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTJ;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskName;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectTaskType;
import com.epoint.basic.auditproject.auditproject.domain.AuditProjectUser;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 办件相关接口定义
 *
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
public interface IJNAuditProject {
    /**
     * 增加 add 实体通用方法
     *
     * @param dataBean 实体
     * @return 无
     */
    public AuditCommonResult<String> addProject(AuditProject dataBean);

    /**
     * 删除 delete 实体通用方法
     *
     * @param rowGuid  办件唯一标识
     * @param areaCode 辖区编号
     * @return 无
     */
    public AuditCommonResult<Void> deleteProjectByGuid(String rowGuid, String areaCode);

    /**
     * 删除无效办件
     *
     * @param projectStatus 办件状态
     * @param areaCode      辖区编号
     * @return 无
     */
    public AuditCommonResult<Void> deleteUselessProject(String projectStatus, String areaCode);

    /**
     * 获取需要同步的办件列表
     *
     * @param conditionMap 条件集合
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getSyncProjectList(Map<String, String> conditionMap);

    /**
     * 更新 update 实体通用方法
     *
     * @param dataBean 实体
     * @return 无
     */
    public AuditCommonResult<String> updateProject(AuditProject dataBean);

    /**
     * 对办件信息根据条件查询，并且分页，条件必须带有areacode
     *
     * @param fieldStr     查询字段
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 分页数据集
     */
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldStr,
                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder);

    /**
     * 对办件信息根据条件查询，并且分页，条件必须带有areacode
     *
     * @param fieldStr       查询字段
     * @param conditionMap   条件map， key为字段名称，value为值
     * @param firstResult    起始记录数
     * @param maxResults     最大记录数
     * @param sortField      排序值
     * @param sortOrder      排序字段
     * @param handleareacode 处理辖区
     * @return 分页数据集
     */
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByHandleareacode(String fieldStr,
                                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                             String sortOrder, String handleareacode);

    /**
     * 对办件信息根据条件查询
     *
     * @param fields       查询字段
     * @param conditionMap 条件集合
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(String fields,
                                                                                Map<String, String> conditionMap);

    /**
     * 查找 通过主键获取实体
     *
     * @param fieldStr 查询字段
     * @param rowGuid  主键
     * @param areaCode 区域编码
     * @return 办件基本信息实体
     */
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String fieldStr, String rowGuid, String areaCode);

    /**
     * 查找 通过物流流水号获取实体
     *
     * @param fieldStr 查询字段
     * @param flowsn   物流流水号
     * @param areaCode 区域编码
     * @return 办件基本信息实体
     */
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String fieldStr, String flowsn, String areaCode);

    /**
     * 查找 通过pviguid获取实体
     *
     * @param fieldStr 查询字段
     * @param pviGuid  流程主键
     * @param areaCode 区域编码
     * @return 办件基本信息实体
     */
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String fieldStr, String pviGuid, String areaCode);

    /**
     * 取文书文号
     *
     * @param orgWord  单位代字
     * @param rowGuid  流程主键
     * @param task_id  事项id
     * @param areaCode 辖区编码
     * @return int
     */
    public AuditCommonResult<Integer> getMaxOrgNumberinfo(String orgWord, String rowGuid, String areaCode,
                                                          String task_id);

    /**
     * 查找 通过windowGuid
     *
     * @param fieldStr       查询的字段（,分割）
     * @param groupOrSortStr 分组的sql语句（group by开头）
     * @param windowGuid     窗口guid
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getFieldList(String fieldStr, String groupOrSortStr,
                                                              String windowGuid);

    /**
     * 查找办件实体
     *
     * @param fieldstr       查询的字段（,分割）
     * @param grouporsortstr 分组的sql语句（group by开头）
     * @param whereStr       查询条件
     * @return 办件集
     */
    public AuditCommonResult<AuditProject> getField(String fieldstr, String grouporsortstr, String whereStr);

    /**
     * 获取办件列表 带分页
     *
     * @param fieldStr    查询字段
     * @param taskIdList  事项id集合
     * @param projectType 办件类型
     * @param windowGuid  窗口guid
     * @param first       分页起始值
     * @param pageSize    每页数量
     * @param areaCode    辖区编码
     * @param applyerName 申请人姓名
     * @param dateStart   开始日期
     * @param dateEnd     结束日期
     * @return 分页数据
     */
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(String fieldStr, List<String> taskIdList,
                                                                          String projectType, String windowGuid, int first, int pageSize, String areaCode, String applyerName,
                                                                          Date dateStart, Date dateEnd);

    /**
     * 获取办件列表 带分页
     *
     * @param fieldStr     查询字段
     * @param taskGuidList 事项id集合
     * @param projectType  办件类型
     * @param windowGuid   窗口guid
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getBanJianList(String fieldStr, List<String> taskGuidList,
                                                                String projectType, String windowGuid);

    /**
     * 获取不进驻中心办件
     *
     * @param fieldStr    查询字段
     * @param ouGuidList  部门guid集合
     * @param first       分页起始值
     * @param pageSize    每页数量
     * @param areaCode    辖区编码
     * @param applyerName 申请人姓名
     * @return 分页数据
     */
    public AuditCommonResult<PageData<AuditProject>> getJZListByPage(String fieldStr, List<String> ouGuidList,
                                                                     int first, int pageSize, String areaCode, String applyerName);

    /**
     * 获取不进驻中心办件
     *
     * @param fieldStr   查询字段
     * @param ouGuidList 部门guid集合
     * @param areaCode   辖区编码
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getJZList(String fieldStr, List<String> ouGuidList, String areaCode);

    /**
     * 根据指定条件更新指定字段
     *
     * @param updateFieldMap 更新字段集合
     * @param conditionMap   条件集合
     * @return 无
     */
    public AuditCommonResult<Void> updateProject(Map<String, String> updateFieldMap, Map<String, String> conditionMap);

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     *
     * @param taskidList 事项id集合
     * @param windowGuid 窗口guid
     * @param areaCode   辖区编码
     * @return 不同状态的办件数量Map集合
     */
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguid(List<String> taskidList,
                                                                              String windowGuid, String areaCode);

    public AuditCommonResult<Map<String, Integer>> getCountStatusByCenterguid(List<String> taskidList,
                                                                              String windowGuid, String centerGuid, String areaCode);

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     *
     * @param taskidList 事项id集合
     * @param windowGuid 窗口标识
     * @param centerGuid 中心标识
     * @param areaCode   辖区编码
     * @return 不同状态的办件数量Map集合
     */
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguid(List<String> taskidList,
                                                                                           String windowGuid, String areaCode, String centerGuid);

    // 政务大厅接口

    /**
     * 获取当前用户办件列表 带分页
     *
     * @param fieldStr    查询字段
     * @param projectType 办件类型
     * @param accountGuid 账户guid
     * @param first       分页起始值
     * @param pageSize    分页数量
     * @param sortField   排序值
     * @param sortOrder   排序规则
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String fieldStr, String projectType,
                                                                          String accountGuid, int first, int pageSize, String sortField, String sortOrder);

    /**
     * 获取办件量多的taskid
     *
     * @return 记录集
     */
    public AuditCommonResult<List<Record>> selectHotTaskId();

    /**
     * 监督监控页面的办件分页，条件必须带有areacode
     *
     * @param fieldStr     查询字段
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param isMaterial   是否上传材料
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 分页数据
     */
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldStr,
                                                                                    Map<String, String> conditionMap, Integer isMaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder);

    /**
     * 获取窗口预警（超时+即将超时）的办件量
     *
     * @param windowGuid 窗口guid
     * @param areaCode   辖区编码
     * @return 办件量
     */
    public AuditCommonResult<Integer> getCSCount(String windowGuid, String areaCode);

    // 南京压力测试临时修改

    /**
     * 对办件信息根据条件查询，并且分页，条件必须带有areacode
     *
     * @param fields       查询字段
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @param keyWord      搜索条件，主要是用来区别or,不用or不用传递
     * @return 分页数据
     */
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(String fields,
                                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                                        String sortOrder, String keyWord);

    /**
     * 报表统计sql
     *
     * @param ouGuid   部门guid
     * @param areaCode 辖区编码
     * @return 分页数据
     */
    public AuditCommonResult<Map<String, String>> getReportCount(String ouGuid, String areaCode);

    /**
     * 报表统计sql
     *
     * @param ouGuid    部门guid
     * @param areaCode  辖区编码
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return 分页数据
     */
    public AuditCommonResult<Map<String, String>> getReportCount(String ouGuid, String areaCode, String startDate,
                                                                 String endDate);

    /**
     * 报表统计sql
     *
     * @param areaCode 辖区编码
     * @return 记录集
     */
    public AuditCommonResult<List<Record>> getReportOu(String areaCode);


    /**
     * (过期方法) 对办件信息根据条件查询，并且分页，条件必须带有areacode
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 分页数据
     */
    @Deprecated
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> conditionMap,
                                                                             Integer firstResult, Integer maxResults, String sortField, String sortOrder);

    /**
     * (过期方法) 对办件信息根据条件查询
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @return 办件集
     */
    public AuditCommonResult<List<AuditProject>> getAuditProjectListByCondition(Map<String, String> conditionMap);

    /**
     * 分组获取满意度
     *
     * @param conditionMap 条件集合
     * @return
     */
    public AuditCommonResult<List<Record>> getAuditProjectSatisfiedList(Map<String, String> conditionMap);

    /**
     * (过期方法) 查找 通过主键获取实体
     *
     * @param rowGuid  主键
     * @param areaCode 辖区编码
     * @return 办件集
     */
    @Deprecated
    public AuditCommonResult<AuditProject> getAuditProjectByRowGuid(String rowGuid, String areaCode);

    /**
     * （过期方法） 查找 通过物流流水号获取实体
     *
     * @param flowsn   物流流水号
     * @param areaCode 辖区编码
     * @return 办件实体
     */
    @Deprecated
    public AuditCommonResult<AuditProject> getAuditProjectByFlowsn(String flowsn, String areaCode);

    /**
     * （过期方法） 查找 通过pviguid获取实体
     *
     * @param pviGuid  流程主键
     * @param areaCode 辖区编码
     * @return 办件实体
     */
    @Deprecated
    public AuditCommonResult<AuditProject> getAuditProjectByPVIGuid(String pviGuid, String areaCode);

    /**
     * （过期方法） 获取办件列表 带分页
     *
     * @param taskGuidList 事项guid集合
     * @param projectType  办件类型
     * @param windowGuid   窗口guid
     * @param first        分页起始值
     * @param pageSize     每页数量
     * @param areaCode     辖区编码
     * @param applyerName  申请人姓名
     * @return 分页数据
     */
    @Deprecated
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByPage(List<String> taskGuidList, String projectType,
                                                                          String windowGuid, int first, int pageSize, String areaCode, String applyerName);

    /**
     * （过期方法） 获取办件列表 带分页
     *
     * @param taskGuidList 事项guid集合
     * @param projectType  办件类型
     * @param windowGuid   窗口guid
     * @return 办件集
     */
    @Deprecated
    public AuditCommonResult<List<AuditProject>> getBanJianList(List<String> taskGuidList, String projectType,
                                                                String windowGuid);

    /**
     * （过期方法） 获取当前用户办件列表 带分页
     *
     * @param projectType 办件类型
     * @param accountGuid 账户guid
     * @param first       分页起始值
     * @param pageSize    每页数量
     * @param sortField   排序值
     * @param sortOrder   排序规则
     * @return 办件集
     */
    @Deprecated
    public AuditCommonResult<List<AuditProject>> getZwdtBanJianListByPage(String projectType, String accountGuid,
                                                                          int first, int pageSize, String sortField, String sortOrder);

    /**
     * （过期方法） 监督监控页面的办件分页，条件必须带有areacode
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param ismaterial   是否上传材料
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 分页数据
     */
    @Deprecated
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(Map<String, String> conditionMap,
                                                                                    Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder);

    /**
     * (过期方法) 对办件信息根据条件查询，并且分页，条件必须带有areacode
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 分页数据
     */
    @Deprecated
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataByCondition(
            Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
            String sortOrder);

    /**
     * 删除办件
     *
     * @param projectGuid 办件guid
     * @param flowsn      物流流水号
     * @param pviguid     过程guid
     * @return 无
     */
    public AuditCommonResult<String> deleteProject(String projectGuid, String flowsn, String pviguid);

    /**
     * 统计办件类型
     *
     * @param p_beginDate 起始时间
     * @param p_endDate   结束时间
     * @param p_areacode  辖区编码
     * @return 办件按事项类型统计实体集合
     */
    public List<AuditProjectTaskType> ASP_BJ_TASKTYPE(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException;

    /**
     * 统计办件名称
     *
     * @param p_beginDate 起始时间
     * @param p_endDate   结束时间
     * @param p_areacode  辖区编码
     * @return 办件按事项名称统计实体集合
     */
    public List<AuditProjectTaskName> ASP_BJ_TASKNAME(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException;

    /**
     * 统计办件人员
     *
     * @param p_beginDate 起始时间
     * @param p_endDate   结束时间
     * @param p_areacode  辖区编码
     * @return 办件按人员统计实体集合
     */
    public List<AuditProjectUser> ASP_BJ_USER(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException;

    /**
     * 统计办件窗口
     *
     * @param p_beginDate 起始时间
     * @param p_endDate   结束时间
     * @param p_areacode  辖区编码
     * @return 办件按窗口统计实体集合
     */
    public List<AuditProjectCK> ASP_BJ_WINDOW(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException;

    /**
     * 统计办件部门
     *
     * @param p_beginDate 起始时间
     * @param p_endDate   结束时间
     * @param p_areacode  辖区编码
     * @return 办件按部门统计实体集合
     */
    public List<AuditProjectTJ> ASP_BJ_OU(String p_beginDate, String p_endDate, String p_areacode)
            throws ParseException;

    /**
     * 通过部门获取事项
     *
     * @param p_beginDate 起始日期
     * @param p_endDate   结束日期
     * @param p_areaCode  辖区编码
     * @return AuditProReport集合
     */
    public List<AuditProReport> getTaskByOu(String p_beginDate, String p_endDate, String p_areaCode) throws ParseException;

    /**
     * 对办件数量根据条件查询
     *
     * @param conditionMap 条件集合
     * @return 办件集
     */
    public AuditCommonResult<Integer> getAuditProjectCountByCondition(Map<String, String> conditionMap);

    /**
     * 中心办件前三热门
     *
     * @param taskGuidList 事项编号列表
     * @param centerguid   中心唯一标识
     * @return
     */
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid);


    /**
     * 中心办件前三热门
     *
     * @param taskGuidList 事项编号列表
     * @param centerguid   中心唯一标识
     * @param areacode     辖区编码
     * @return
     */
    public AuditCommonResult<List<Record>> getHotBanJianList(List<String> taskGuidList, String centerguid, String areacode);

    /**
     * 查询即办件承诺件数量
     *
     * @param conditionMap 条件集合
     * @return
     */
    public AuditCommonResult<List<Record>> getAuditProjectStatusCountByCondition(Map<String, String> conditionMap);

    /**
     * 根据条件查询窗口人员当天办件
     *
     * @param conditionMap 条件集合
     * @return
     */
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap);

    /**
     * 根据条件查询窗口人员当天办件
     *
     * @param conditionMap 条件集合
     * @param field        时间
     * @return
     */
    public AuditCommonResult<Integer> getTodayHandleProjectCount(String conditionMap, String field);

    /**
     * 获取受理数量超过的人数
     *
     * @param conditionMap 条件集合
     * @param todayHandle  今日办理数量
     * @return
     */
    public AuditCommonResult<Integer> getOverUserCount(Map<String, String> conditionMap, int todayHandle);

    /**
     * 获取办结数量超过的人数
     *
     * @param conditionMap 条件集合
     * @param todayFinish  今日办结数量
     * @return
     */
    public AuditCommonResult<Integer> getOverFinishCount(Map<String, String> conditionMap, int todayFinish);


    /**
     * 通过中心获取办件
     *
     * @param fieldstr    查询字段
     * @param taskidList  事项标识列表
     * @param projectType 办件类型
     * @param windowguid  窗口唯一标识
     * @param first       起始页
     * @param pageSize    分页大小
     * @param areaCode    辖区编码
     * @param centerGuid  中心唯一标识
     * @param applyerName 申请人姓名
     * @param datestart   起始日期
     * @param dateend     结束日期
     * @return
     */
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuid(String fieldstr, List<String> taskidList,
                                                                                String projectType, String windowguid, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
                                                                                Date datestart, Date dateend);

    /**
     * 对办件数量根据条件查询
     *
     * @param conditionMap 条件集合
     * @param applyway     申请类型
     * @return
     */
    public AuditCommonResult<Integer> getAuditProjectCountByApplyway(Map<String, String> conditionMap, String applyway);


    /**
     * 获取部门办件数据
     *
     * @param ouguid 部门唯一标识
     * @param status 状态
     * @param date   日期
     * @return
     */
    public AuditCommonResult<List<Record>> getWeekLineList(List<String> ouguid, String status, Date date);

    /**
     * 获取部门办件数据
     *
     * @param ouGuidList 部门唯一标识集合
     * @param status     集合
     * @param date       日期
     * @return
     */
    public AuditCommonResult<List<Record>> getWeekLineListByOu(List<String> ouGuidList, String status, Date date);

    /**
     * 获取部门办件数据
     *
     * @param centerguid 中心唯一标识
     * @param areacode   辖区编码
     * @param status     状态
     * @param date       日期
     * @return
     */
    public AuditCommonResult<List<Record>> getWeekLineList(String centerguid, String areacode, String status, Date date);

    /**
     * 根据条件获取办件数据
     *
     * @param fieldstr     查询字段
     * @param taskidList   事项版本唯一标识列表
     * @param projectType  办件类型
     * @param windowguid   窗口唯一标识
     * @param first        起始页
     * @param pageSize     分页大小
     * @param baseAreaCode 辖区编码（6位）
     * @param centerGuid   中心唯一标识
     * @param applyerName  申请人姓名
     * @param datestart    起始时间
     * @param dateend      结束时间
     * @param areacode     辖区编码
     * @return
     */
    public AuditCommonResult<PageData<AuditProject>> getBanJianListByCenterGuidAndAreacode(String fieldstr,
                                                                                           List<String> taskidList, String projectType, String windowguid, int first, int pageSize, String baseAreaCode,
            String centerGuid, String applyerName, Date datestart, Date dateend, String areacode, String projectname,
            String commitnum, String sortField, String sortOrder);

    /**
     * 获取办件计数
     *
     * @param taskidList   事项唯一标识集合
     * @param windowguid   窗口唯一标识
     * @param areaCode     辖区编码
     * @param centerGuid   中心唯一标识
     * @param baseareacode 辖区编码（6位）
     * @return
     */
    public AuditCommonResult<Map<String, Integer>> getCountStatusByWindowguidAndCenterguidAndAreacode(List<String> taskidList,
                                                                                                      String windowguid, String areaCode, String centerGuid, String baseareacode);

    /**
     * 根据条件查询办件办结平均耗时
     *
     * @param map 条件集合
     * @return
     */
    public AuditCommonResult<Integer> getAvgSpendtime(Map<String, String> map);

    /**
     * 连查sparetime表获取超时办件
     *
     * @param fieldStr     查询字段
     * @param conditionMap 条件集合
     * @param firstResult  起始页
     * @param maxResults   分页大小
     * @param sortField    排序字段
     * @param sortOrder    排序条件
     */
    public AuditCommonResult<PageData<AuditProject>> getPagaBySpareTime(String fieldStr,
                                                                        Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                        String sortOrder);

    /**
     * 连查sparetime表获取超时办件数
     *
     * @param conditionMap 条件集合
     */
    public AuditCommonResult<Integer> getPagaBySpareTimeCount(Map<String, String> conditionMap);

    /**
     * 获取补正操作的办件
     *
     * @param fieldstr  查询字段
     * @param map       查询条件集合
     * @param first     起始页
     * @param pageSize  分页大小
     * @param sortField 排序字段
     * @param sortOrder 排序方式
     * @return
     */
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectListByBZ(String fieldstr, Map<String, String> map,
                                                                             int first, int pageSize, String sortField, String sortOrder);

    /**
     * 过滤池未上报的办件JS
     *
     * @param fieldstr    查询字段
     * @param projectname 办件名称
     * @param flowsn      流水号
     * @param first       起始页
     * @param pageSize    分页大小
     * @param sortField   排序字段
     * @param sortOrder   排序方式
     * @return
     */
    public AuditCommonResult<PageData<AuditProject>> selectRecordListBTS(String fieldstr, String projectname, String flowsn, int first,
                                                                         int pageSize, String sortField, String sortOrder);

    /**
     * 根据条件获取办件JS
     *
     * @param fieldstr          查询字段
     * @param projectname       办件名称
     * @param flowsn            流水号
     * @param handleProjectGuid 办件编号
     * @return
     */
    public AuditCommonResult<List<AuditProject>> selectRecordListBTSAN(String fieldstr, String projectname, String flowsn, String handleProjectGuid);

    /**
     * 根据条件获取数量
     *
     * @param groupfield   分组字段
     * @param recordnum    查询记录的数量
     * @param conditionmap 条件集合
     * @return 办件数量和事项guid
     */
    public AuditCommonResult<List<Record>> getProjectnumGroupByFiled(String groupfield, Integer recordnum, Map<String, String> conditionmap);

    public List<WorkflowWorkItem> getWorkItemListByPVIGuidAndStatus(String pviguid);

    public Record getMaxZjNum(String name, String year);

    public void UpdateMaxZjNum(String maxnum, String name, String year);

    public int updateProjectCertRowguid(String certrowguid, String rowguid);

    public AuditTask getAuditTaskByUnid(String unid);

    public int getJzListCount(List<String> OUGuids, String areaCode);

    /**
     * 获取未进驻大厅的数量
     * @param ouGuidList
     * @param areaCode
     * @return
     */
    public int getNotJZCount(List<String> ouGuidList, String areaCode);

    Record getMaxZjNumNew(String name);

    void UpdateMaxZjNumNew(String valueOf, String name);

}
