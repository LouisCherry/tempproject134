package com.epoint.zwdt.zwdtrest.sghd.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.user.entity.FrameUser;

import java.io.Serializable;
import java.util.List;

public interface IJnAuditJianGuan extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int insert(T record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey);



    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange数量 （byouguid）
     * @author male
     * @date 2019年1月22日 上午11:38:52
     */
    public int getAuditProjectPermissionNumByOuguid(String areaCode, String ouguid);



    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange数量
     * @author male
     * @date 2019年1月22日 上午11:43:03
     */
    public int getAuditProjectPermissionNum(String areaCode);

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 根据areacode获取ou信息
     * @author male
     * @date 2019年1月22日 上午11:48:11
     */
    public List<Record> getOuByareaCode(String areaCode);


    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange 数量 中心省管
     * @author male
     * @date 2019年1月22日 下午1:50:52
     */
    public int getAuditProjectPermissionNum2(String areaCode);


    /**
     * @return int 返回类型
     * @throws
     * @Description: 查询 ProjectPermissionChange 数量（byouguid）中心省管
     * @author male
     * @date 2019年1月22日 下午1:51:23
     */
    public int getAuditProjectPermissionNumByOuguid2(String areaCode, String ouguid);

    /**
     * @return String 返回类型
     * @throws
     * @Description: 获取themeguid
     * @author male
     * @date 2019年1月22日 下午2:00:47
     */
    public String getThemeguidFromAuditPP(String monitorGuid);


    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取fileclientguid
     * @author male
     * @date 2019年1月22日 下午2:23:16
     */
    public List<Record> getFileclientguidByThemeguid(String themeGuid);

    /**
     * @return List<FrameAttachInfo> 返回类型
     * @throws
     * @Description: 根据cliengguid获取FrameAttachInfo信息
     * @author male
     * @date 2019年1月22日 下午2:27:13
     */
    public List<FrameAttachInfo> getAllFrameAttachInfoByCliengGuid(String cliengguid);

    /**
     * @return String 返回类型
     * @throws
     * @Description: 查询 fileclientguid（from audit_project_monitor）
     * @author male
     * @date 2019年1月22日 下午3:05:09
     */
    public String getFileclientguidFromMonitor(String rowguid);

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取部门信息，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午3:15:11
     */
    public List<Record> getChangeOpinionOuInfo(String areaCode);

    /**
     * @return List<FrameUser> 返回类型
     * @throws
     * @Description: 获取部门信息 byouguid，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午3:19:08
     */
    public List<FrameUser> getChangeOpinionFrameOu(String ouguid);

    /**
     * @return String 返回类型
     * @throws
     * @Description: 根据角色名称获取userguid
     * @author male
     * @date 2019年1月22日 下午4:03:42
     */
    public String getCenterUserGuid(String roleName);

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 获取部门信息2，ChangeOpinion类
     * @author male
     * @date 2019年1月22日 下午4:03:47
     */
    public List<Record> getChangeOpinionFrameOu2(String centerUserGuid, String ouGuid);

    /**
     * @return List<Record> 返回类型
     * @throws
     * @Description: 根据ouguid获取frame_user信息
     * @author male
     * @date 2019年1月22日 下午5:08:15
     */
    public List<Record> getChangeOpinionFrameOu2(String ouguid);

    /**
     * 部门获取已认领数量
     *
     * @return
     */
    public int getTaJianGuanTabYrlCount(String areaCode, String ouguid, String userguid);

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    public int getTaJianGuanTabWrlCount(String areaCode, String ouguid, String userguid);

    /**
     * 部门获取审批信息数量
     *
     * @return
     */
    public int getTaJianGuanTabSpxxCount(String sql);

    /**
     * 获取互动协助的数量
     *
     * @return
     */
    public int getTaJianGuanTabjgcount(String ouguid, String areaCode);

    /**
     * @return int 返回类型
     * @throws
     * @Description: 更新fileclientguid字段
     * @author male
     * @date 2019年1月22日 下午5:41:25
     */
    public int updateFileclientguid(String fileclientguid, String rowguid);

    /**
     * @return List<AuditProject> 返回类型
     * @throws
     * @Description: TaProjectWrlAction类
     * @author male
     * @date 2019年1月22日 下午5:53:06
     */
    public List<AuditProject> getTaProjectWrlInfo(String sql, int first, int pageSize);

    /**
     * @return int 返回类型
     * @throws
     * @Description: TaProjectWrlAction类
     * @author male
     * @date 2019年1月22日 下午5:53:38
     */
    public int getTaProjectWrlNum(String sql);

    /**
     * 判断是否已办结
     *
     * @param rowguid
     * @return
     */
    public Boolean judgeSign(String rowguid);

    /**
     * 获取最新回复的handleurl
     *
     * @param rowguid
     * @return
     */
    public String getHandleUrlByRowguid(String rowguid);

    /**
     * 根据rowguid获取audit_task ouname
     *
     * @param rowguid
     * @return
     */
    public String getOuNameFromAuditTask(String rowguid);

    /**
     * 根据辖区获取审批局信息
     *
     * @param areaCode
     * @return
     */
    public List<Record> getCenterOunameinfo(String areaCode);

    /**
     * 获取二级部门
     *
     * @param str
     * @return
     * @authory shibin
     * @version 2019年9月19日 上午10:05:00
     */
    public List<Record> findChildOuByParentguid(String str);

    List<String> findTaskidListByUserguidAndOuguid(String userguid, String ouguid);

}
