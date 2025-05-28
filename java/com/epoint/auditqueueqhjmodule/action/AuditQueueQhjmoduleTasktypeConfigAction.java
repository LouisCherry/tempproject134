package com.epoint.auditqueueqhjmodule.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueueqhjmoduletasktype.api.IAuditQueueQhjmoduleTasktypeService;
import com.epoint.auditqueueqhjmoduletasktype.api.entity.AuditQueueQhjmoduleTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("auditqueueqhjmoduletasktypeconfigaction")
@Scope("request")
public class AuditQueueQhjmoduleTasktypeConfigAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditQueueQhjmoduleTasktypeService qhjmoduleTasktypeService;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private IHandleFrameOU iHandleFrameOU;

    /**
     * 取号机模块与事项分类的关联实体对象
     */
    private AuditQueueQhjmoduleTasktype dataBean = null;
    /**
     * 事项分类树结构model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 取号机模块标识
     */
    private String qhjmoduleguid = null;

    @Override
    public void pageLoad() {
        qhjmoduleguid = getRequestParameter("guid");
    }

    /***
     * 
     * 用treebean构造事项树
     * 
     * @return
     * 
     * 
     */
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllTask());
            treeModel.setSelectNode(this.getSelectTasktype(qhjmoduleguid));
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("添加该部门下事项分类");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    /**
     * 
     * 使用SimpleFetchHandler9构造树
     * 
     * @return
     * 
     * 
     */
    private SimpleFetchHandler9 loadAllTask() {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List list = new ArrayList();
                if (StringUtil.isNotBlank(conndition)) {
                    list.clear();
                    list = selectTasktypeByCondition(conndition);
                }
                return list;
            }

            /**
             * 窗口配置事项分类根据条件查询事项分类
             * 
             * @param condition
             * @return
             * 
             * 
             */
            public List<AuditQueueTasktype> selectTasktypeByCondition(String condition) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.like("tasktypename", condition);
                sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                return tasktypeservice.getAllTasktype(sql.getMap()).getResult();
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的窗口部门加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        list = iHandleFrameOU.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(), true)
                                .getResult();
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该窗口部门下的所有事项
                    }
                    else {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("Centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                        sql.eq("ouguid", treeData.getObjectGuid());
                        list = tasktypeservice.getAllTasktype(sql.getMap()).getResult();
                    }
                }
                // 点击checkbox的时候触发
                else {
                    // 如果点击的是部门的checkbox，则返回该部门下所有的事项的list
                    if ("frameou".equals(treeData.getObjectcode())) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("Centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                        sql.eq("ouguid", treeData.getObjectGuid());
                        list = tasktypeservice.getAllTasktype(sql.getMap()).getResult();
                    }
                    // 如果点击的是事项的checkbox，则返回该是事项的list
                    else if ("auditqueuetasktype".equals(treeData.getObjectcode())) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("rowguid", treeData.getObjectGuid());
                        list = tasktypeservice.getAllTasktype(sql.getMap()).getResult();
                    }
                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("Centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                sql.eq("ouguid", treeData.getObjectGuid());
                return tasktypeservice.getAllTasktype(sql.getMap()).getResult().size();

            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        // 将dataList转化为frameou的list
                        if (obj instanceof FrameOu) {
                            FrameOu frameOu = (FrameOu) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(frameOu.getOuguid());
                            treeData.setTitle(frameOu.getOuname());
                            // 没有子节点的不加载
                            if (tasktypeservice.getCountbyOUGuid(frameOu.getOuguid(),
                                    ZwfwUserSession.getInstance().getCenterGuid()).getResult() != 0) {
                                treeData.setObjectcode("frameou");
                                treeList.add(treeData);
                            }

                        }
                        // 将dataList转化为AuditTask的list
                        if (obj instanceof AuditQueueTasktype) {
                            AuditQueueTasktype tasktype = (AuditQueueTasktype) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(tasktype.getRowguid());
                            treeData.setTitle(tasktype.getTasktypename());
                            // objectcode的作用是来区分是点击了部门还是事项
                            treeData.setObjectcode("auditqueuetasktype");
                            treeList.add(treeData);
                        }
                    }
                }
                return treeList;
            }
        };
        return fetchHandler9;
    }

    /**
     * 根据qhjmoduleguid获取事项分类SelectItem，初始化事项分类信息
     * 
     * @param qhjmoduleguid
     * @return
     * 
     * 
     */
    public List<SelectItem> getSelectTasktype(String qhjmoduleguid) {
        List<SelectItem> showtasktypeList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(qhjmoduleguid)) {

            List<AuditQueueTasktype> tasktypeList = new ArrayList<AuditQueueTasktype>();
            if (StringUtil.isNotBlank(qhjmoduleguid)) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("qhjmoduleguid", qhjmoduleguid);
                List<AuditQueueQhjmoduleTasktype> qhjmoduleTasktypes = qhjmoduleTasktypeService
                        .findList("select * from audit_queue_qhjmodule_tasktype where qhjmoduleguid= ?", qhjmoduleguid);
                if (qhjmoduleTasktypes != null && !qhjmoduleTasktypes.isEmpty()) {
                    for (AuditQueueQhjmoduleTasktype qhjmoduleTasktype : qhjmoduleTasktypes) {
                        AuditQueueTasktype tasktype = tasktypeservice
                                .getAuditQueueTasktypeByRowguid(qhjmoduleTasktype.getTasktypeguid()).getResult();
                        if (tasktype != null) {
                            tasktypeList.add(tasktype);
                        }
                    }
                }
            }
            if (!tasktypeList.isEmpty()) {
                for (AuditQueueTasktype tasktype : tasktypeList) {
                    showtasktypeList.add(new SelectItem(tasktype.getRowguid(), tasktype.getTasktypename()));
                }
            }
        }
        return showtasktypeList;
    }

    /**
     * 
     * 把事项和窗口信息保存到关系表里面
     * 
     * @param guidList
     *            前台获取的事项guid 用“;”分割
     * @param qhjmoduleguid
     *            窗口guid
     * 
     * 
     */
    public void saveTasktypeToQhjmodule(String tasktypeguidList, String qhjmoduleguid) {
        // 添加新的事项关联关系
        if (StringUtil.isNotBlank(qhjmoduleguid)) {
            qhjmoduleTasktypeService.deleteAllByGuid(qhjmoduleguid);

            if (StringUtil.isNotBlank(tasktypeguidList)) {
                String[] taskGuids = tasktypeguidList.split(";");
                for (int i = 0; i < taskGuids.length; i++) {

                    AuditQueueQhjmoduleTasktype qhjmoduleTasktype = new AuditQueueQhjmoduleTasktype();
                    qhjmoduleTasktype.setOperatedate(new Date());
                    qhjmoduleTasktype.setOperateusername(UserSession.getInstance().getDisplayName());
                    qhjmoduleTasktype.setRowguid(UUID.randomUUID().toString());

                    qhjmoduleTasktype.setQhjmoduleguid(qhjmoduleguid);
                    qhjmoduleTasktype.setTasktypeguid(taskGuids[i]);
                    qhjmoduleTasktypeService.insert(qhjmoduleTasktype);
                }
            }
        }

    }

    public String getQhjmoduleguid() {
        return qhjmoduleguid;
    }

    public void setQhjmoduleguid(String qhjmoduleguid) {
        this.qhjmoduleguid = qhjmoduleguid;
    }

    public AuditQueueQhjmoduleTasktype getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditQueueQhjmoduleTasktype dataBean) {
        this.dataBean = dataBean;
    }
}
