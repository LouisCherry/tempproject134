package com.epoint.basic.auditorga.auditwindow.inter;

import java.util.List;
import java.util.Map;

import com.epoint.auditorga.auditwindow.entiy.AuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 
 * 事项基础窗口api
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 */
public interface IAuditOrgaWindowYjs
{
    /**
     * 
     * 添加窗口
     * 
     * @param auditWindow
     *            窗口实体
     * @param centerGuid
     *            中心唯一标识
     * @return
     * 
     */
    public AuditCommonResult<String> insertWindow(AuditOrgaWindow auditWindow, String centerGuid);

    /**
     * 
     * 插入窗口人员对象
     * 
     * @param auditWindowUser
     *            窗口人员实体
     * 
     * @return
     */
    public AuditCommonResult<String> insertWindowUser(AuditOrgaWindowUser auditWindowUser);

    /**
     * 插入窗口事项
     * 
     * @param auditWindowTask
     *            窗口事项实体
     * @return
     */
    public AuditCommonResult<String> insertWindowTask(AuditOrgaWindowYjs auditWindowTask);

    /**
     * 
     * 获取窗口分页
     * 
     * @param conditionMap
     *            条件集合
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序
     * @return
     * 
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder);

    /**
     * 
     * 获取字段值不同的窗口分页
     * 
     * @param conditionMap
     *            条件集合
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序
     * @param fields
     *            字段集合
     * @return
     * 
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getSomeFieldsByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder, String fields);

    /**
     * 获取窗口个数
     * 
     * @param conditionMap
     *            条件集合
     * @return
     * 
     * 
     */
    public AuditCommonResult<Integer> getWindowCount(Map<String, String> conditionMap);

    /**
     * 获取字段值不同窗口个数
     * 
     * @param conditionMap
     *            条件集合
     * @param fields
     *            字段集合
     * @return
     * 
     * 
     */
    public Integer getSomeFieldsCount(Map<String, String> conditionMap, String fields);

    /**
     * 根据指定条件获取窗口
     * 
     * @param conditionMap
     *            条件集合
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getAllWindow(Map<String, String> conditionMap);

    /**
     * 
     * 根据用户用户唯一标识获取用户窗口信息
     * 
     * @param userGuid
     *            用户唯一标识
     * @return AuditWindow 窗口对象
     * 
     * 
     */
    public AuditCommonResult<AuditOrgaWindow> getWindowByUserGuid(String userGuid);

    /**
     * 
     * 根据用户guid和条件获取用户窗口信息
     * 
     * @param userGuid
     *            用户guid
     * @param conditionMap
     *            条件集合
     * @return AuditWindow 窗口对象
     * 
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByUserGuidAndCondition(String userGuid,
            Map<String, String> conditionMap);

    /**
     * 
     * 根据用户用户唯一标识获取用户窗口信息列表
     * 
     * @param userGuid
     *            用户唯一标识
     * @return AuditWindow 窗口对象
     * 
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByUserGuid(String userGuid);

    /**
     * 
     * 根据事项版本唯一标识获取用户窗口信息列表
     * 
     * @param taskid
     *            事项版本唯一标识
     * @return AuditWindow 窗口对象
     * 
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByTaskId(String taskid);

    /**
     * 
     * 根据 窗口标识获取窗口
     * 
     * @param windowGuid
     *            窗口标识
     * @return
     * 
     * 
     */
    public AuditCommonResult<AuditOrgaWindow> getWindowByWindowGuid(String windowGuid);

    /**
     * 
     * 根据 窗口标识获取该窗口下的人员
     * 
     * @param windowGuid
     *            窗口标识
     * @return
     * 
     */
    public AuditCommonResult<List<AuditOrgaWindowUser>> getUserByWindow(String windowGuid);

    /**
     * 
     * 获取该窗口人员(分页)
     * 
     * @param conditionMap
     *            条件集合
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * 
     * @return
     * 
     */
    public AuditCommonResult<PageData<AuditOrgaWindowUser>> getUserPageData(Map<String, String> conditionMap, int first,
            int pageSize);

    /**
     * 获取窗口事项列表
     * 
     * @param windowGuid
     *            窗口唯一标识
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindowYjs>> getTaskByWindow(String windowGuid);

    /**
     * 获取窗口事项唯一标识
     * 
     * @param windowGuid
     *            窗口唯一标识
     * @return
     */
    public AuditCommonResult<List<String>> getTaskidsByWindow(String windowGuid);

    /**
     * 通过窗口唯一标识获取当前窗口的所有事项唯一标识
     * 
     * @param windowGuid
     *            窗口唯一标识
     * @return
     * 
     * 
     */
    public AuditCommonResult<List<String>> getTaskGuidsByWindow(String windowGuid);

    /**
     * 获取窗口事项列表
     * 
     * @param taskguid
     *            事项唯一标识
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindowTask>> getTaskByTaskguid(String taskguid);

    /**
     * 获取窗口事项列表
     * 
     * @param windowGuid
     *            窗口唯一标识
     * @param taskId
     *            事项版本唯一标识
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindowTask>> getTaskByWindowAndTaskId(String windowGuid, String taskId);

    /**
     * 更新窗口
     * 
     * @param auditwindow
     *            窗口实体
     * @param preWindowNo
     *            修改前的窗口编号
     * @param preMAC
     *            原先的mac地址
     * @param centerGuid
     *            中心标志
     * @return
     */
    public AuditCommonResult<String> updateAuditWindow(AuditOrgaWindow auditwindow, String preWindowNo, String preMAC,
            String centerGuid);

    /**
     * 
     * 根据窗口唯一标识删除该窗口下的事项信息
     * 
     * @param windowGuid
     *            事项唯一标识
     * @return
     */
    public AuditCommonResult<String> deleteWindowTaskByWindowGuid(String windowGuid);

    /**
     * 
     * 根据事项标识删除该窗口下的事项信息
     * 
     * @param taskGuid
     *            事项唯一标识
     * @return
     */
    public AuditCommonResult<String> deleteWindowTaskByTaskGuid(String taskGuid);

    /**
     * 根据窗口guid删除窗口信息，如果是这种情况，需要删除所有的对应关系
     * 
     * @param windowGuid
     *            窗口唯一标识
     * @return
     */
    public AuditCommonResult<Void> deleteWindowByWindowGuid(String windowGuid);

    /**
     * 根据窗口guid删除窗口信息，如果是这种情况，需要删除所有的对应关系
     * 
     * @param windowGuid
     *            窗口唯一标识
     * 
     * @param centerGuid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<Void> deleteWindowByWindowGuid(String windowGuid, String centerGuid);

    /**
     * 根据指定条件获取窗口人员
     * 
     * @param conditionMap
     *            条件集合
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindowUser>> getWindowUser(Map<String, String> conditionMap);

    /**
     * 
     * 更新窗口事项
     * 
     * @param auditWindowTask
     *            窗口事项实体
     * @return
     * 
     */
    public AuditCommonResult<String> updateWindowTask(AuditOrgaWindowTask auditWindowTask);

    /**
     * 
     * 查询窗口事项
     * 
     * @param rowguid
     *            唯一标识
     * @return
     * 
     */
    public AuditCommonResult<AuditOrgaWindowTask> findWindowTask(String rowguid);

    /**
     * 通过事项业务标识更新窗口事项
     * 
     * @param taskid
     *            事项业务唯一标识
     * @param newtaskguid
     *            新事项唯一标识
     * @return
     * 
     * 
     */
    public AuditCommonResult<String> updateWindowTaskByTaskId(String taskid, String newtaskguid);

    /**
     * 
     * 获取窗口分页
     * 
     * @param conditionMap
     *            条件集合
     * 
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            顺序
     * @return
     * 
     * 
     */
    public AuditCommonResult<PageData<AuditOrgaWindowTask>> getWindowTaskPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);

    /**
     * 
     * 获取窗口部门
     * 
     * @return
     */
    public AuditCommonResult<List<FrameOu>> selectFrameOu();

    /**
     * 获取中心事项
     * 
     * @param centerguid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<List<String>> getTaskIdListByCenter(String centerguid);

    /**
     * 获取enable的中心事项
     * 
     * @param centerguid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<Integer> getEnableTaskListByCenter(String centerguid);

    /**
     * 获取enable的中心事项
     * 
     * @param centerguid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<Integer> getEnableAndChargeTaskListByCenterCount(String centerguid);

    /**
     * 根据TaskId判断是否存在
     * 
     * @param taskid
     *            事项业务标识
     * @param centerguid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<Boolean> isExistbyTaskId(String taskid, String centerguid);

    /**
     * 获取窗口个数
     * 
     * @param userguid
     *            用户唯一标识
     * @return
     * 
     */
    public AuditCommonResult<Integer> getWindowCountByUserGuid(String userguid);

    /**
     * 获取窗口个数
     * 
     * @param Mac
     *            mac地址
     * @param userguid
     *            用户唯一标识
     * @return
     * 
     */

    public AuditCommonResult<String> getWindowByMacandUserGuid(String Mac, String userguid);

    /**
     * 
     * 获取事项所在中心标识
     * 
     * @param taskId
     *            事项业务唯一标识
     * @return
     */
    public AuditCommonResult<List<Record>> selectCenterGuidsByTaskId(String taskId);

    /**
     * 获取大厅部门
     * 
     * @param centerguid
     *            中心唯一标识
     * @param hallguid
     *            大厅标识
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * @return
     */
    public AuditCommonResult<PageData<String>> getOUPageDatabyHall(String centerguid, String hallguid, int first,
            int pageSize);

    /**
     * 获取大厅部门
     * 
     * @param centerguid
     *            中心唯一标识
     * @param hallguid
     *            大厅标识
     * 
     * @return
     */
    public AuditCommonResult<List<String>> getOUbyHall(String centerguid, String hallguid);

    /**
     * 获取大厅部门
     * 
     * @param conditionMap
     *            条件集合
     * 
     * @param first
     *            页码
     * @param pageSize
     *            每页数量
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            顺序
     * @return
     */
    public AuditCommonResult<PageData<AuditOrgaWindow>> getWindowPageData(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder);

    /**
     * 根据事项获取办理人列表
     * 
     * @param taskID
     *            事项唯一标识
     * 
     * @return
     */
    public AuditCommonResult<List<FrameUser>> getFrameUserByTaskID(String taskID);

    /**
     * 
     * 获取中心下所有窗口人员数量
     * 
     * @param centerGuid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<Integer> getWindowUserByCenterGuid(String centerGuid);

    /**
     * 根据部门标识查询窗口
     * 
     * @param ouguid
     *            部门标识
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByOU(String ouguid);

    /**
     * 根据中心标识和条件查询窗口
     * 
     * @param windowname
     *            窗口名称
     * @param centerguid
     *            中心唯一标识
     * @return
     */
    public AuditCommonResult<List<AuditOrgaWindow>> selectByCenter(String windowname, String centerguid);

    /**
     * 根据窗口标识查询其部门标识
     * 
     * @param windowguid
     *            窗口唯一标识
     * @return
     */
    public AuditCommonResult<String> getOuguidByWindowGuid(String windowguid);

    /**
     * 根据中心标识获取部门标识列表
     * 
     * @param centerGuid
     *            中心标识
     */
    public AuditCommonResult<List<String>> getoulistBycenterguid(String centerGuid);

    /**
     * 根据中心标识获取所有窗口
     * 
     * @param centerGuid
     *            中心标识
     */
    public AuditCommonResult<List<AuditOrgaWindow>> getAllByCenter(String centerGuid);

    /**
     * 添加窗口
     * 
     * @param auditWindow
     *            窗口实体
     * @return
     */
    public AuditCommonResult<String> insertWindow(AuditOrgaWindowYjs auditWindow);

    /**
     * 根据窗口人员名称模糊查询窗口标识
     * 
     * @param windowpeople
     *            窗口人员名称
     * @return
     */
    public AuditCommonResult<List<String>> getWindowGuidByUserName(String windowpeople);

    /**
     * 根据窗口事项名称模糊查询窗口标识
     * 
     * @param windowtask
     *            窗口事项名称
     * @return
     */
    public AuditCommonResult<List<String>> getWindowGuidByTaskName(String windowtask);

    /**
     * 根据窗口guid查询窗口人员
     * 
     * @param windowGuid
     *            窗口标识
     * @return
     */
    public AuditCommonResult<List<FrameUser>> getFrameUserByWindowGuid(String windowGuid);

    /**
     * 
     * 根据窗口对应中心唯一标识查询对应的第一个窗口(排序降序)
     * 
     * @param rowguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditOrgaWindow getFirstWindowByCenterGuid(String centerguid);

    /**
     * 
     *  根据中心编码删除配置在该中心下的窗口 
     *  @param centerguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int deleteWindowByCenterguid(String centerguid);

	public List<AuditSpBusiness> getBusinessByAreacode(String areacode, String businessname);

	public List<AuditOrgaArea> getAreacodeList(String areacode);

	public List<AuditSpBusiness> getBusinessDetailByAreacode(String areacode);

	public List<AuditSpBusiness> getBusinessDetailByRowguid(String rowguid);

	void insert(AuditOrgaWindowYjs record);
	
	 public int deleteByGuid(String guid);

}
