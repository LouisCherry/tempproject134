package com.epoint.xmz.onlinetaskconfig.action;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.crypto.MDUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.log.FrameLogUtil.Visit;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 事项基本信息list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2016-10-03 14:38:39]
 */
@RestController("jnaudittasklistaction2")
@Scope("request")
public class JNAuditTaskListAction extends BaseController {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = -1490905998750897938L;

    /**
     * 事项基本信息实体对象
     */
    private AuditTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;


    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;
    /**
     * 事项名称，搜索用
     */
    private String taskName;
    /**
     * 事项编码，搜索用
     */
    private String item_id;


    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IAuditTask auditTaskBasicImpl;


    @Autowired
    private IHandleFrameOU frameOu;

    private String configguid;

    @Override
    public void pageLoad() {

        configguid = getRequestParameter("configguid");

        if (dataBean == null) {
            dataBean = new AuditTask();
        }
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        CommonDao commonDao=CommonDao.getInstance();
        List<String> select = getDataGridData().getSelectKeys();
        String sql ="select * from audit_task where rowguid = ?";
        String sql2 = "select * from onlinetask_config_task where configguid = ? and taskguid = ? ";

        String tasknames="";
        boolean hhh = true;
        for (String taskGuid : select) {
            Record record1 = commonDao.find(sql2, Record.class, configguid, taskGuid);
            if (record1 != null) {
                AuditTask auditTask = commonDao.find(sql, AuditTask.class, taskGuid);
                tasknames += auditTask.getTaskname()+",";
                hhh=false;
            }

        }

        if (hhh){
            for (String taskGuid : select) {
                Record record=new Record();
                record.setSql_TableName("onlinetask_config_task");
                record.set("rowguid",UUID.randomUUID().toString());
                record.set("configguid",configguid);
                record.set("taskguid",taskGuid);
                commonDao.insert(record);


            }
        }

        if (hhh){
            addCallbackParam("msg", "配置成功！");
        }   else {
            addCallbackParam("msg", "配置失败！"+tasknames+"已经配置过");
        }
        commonDao.close();
    }


    /**
     * 从缓存里面获取数据，并且分页
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 5063516200361741720L;

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 是否有搜索条件，默认true无搜索条件
                    boolean flag = true;

                    sql.eq("is_enable", "1");

                    sql.eq("businesstype", "1");

                    sql.eq("IS_EDITAFTERIMPORT", "1");


                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("ouguid", leftTreeNodeGuid);
                    } else {
                        AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        if (auditOrgaArea != null) {
                            // 取辖区下窗口部门
                            List<FrameOu> frameOus = frameOu.getWindowOUList(auditOrgaArea.getXiaqucode(), true)
                                    .getResult();
                            String ouGuids = "'";
                            for (FrameOu frameOu : frameOus) {
                                ouGuids += frameOu.getOuguid() + "','";
                            }
                            ouGuids += "'";
                            sql.in("ouguid", ouGuids);
                        }
                    }
                    if (StringUtil.isNotBlank(taskName)) {
                        sql.like("taskname", taskName);
                        flag = false;
                    }
                    if (StringUtil.isNotBlank(item_id)) {
                        sql.like("item_id", item_id);
                    }

                    // 辖区编码作为过滤条件
                    // sql.eq("areacode", "320581");
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    sql.eq("areacode", areacode);
                    sql.eq("istemplate", String.valueOf(ZwfwConstant.CONSTANT_INT_ZERO));// 排除模板事项
                    PageData<AuditTask> pageData = auditTaskBasicImpl
                            .getAuditTaskPageData(sql.getMap(), first, pageSize, null, null).getResult();

                    this.setRowCount(pageData.getRowCount());
                    List<AuditTask> auditTasklists = new ArrayList<AuditTask>();
                    auditTasklists = pageData.getList();
                    return auditTasklists;
                }
            };
        }
        return model;
    }


    public AuditTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }



    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }


}
