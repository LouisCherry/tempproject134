package com.epoint.guidang.guidangmanage.action;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.guidang.guidangmanage.api.IGuidangmanageService;
import com.epoint.guidang.guidangmanage.entity.Guidangmanage;

/**
 * 归档管理修改页面对应的后台
 * 
 * @author chengninghua
 * @version [版本号, 2017-12-15 15:11:49]
 */
@RightRelation(GuidangmanageListAction.class)
@RestController("guidangmanageeditaction")
@Scope("request")
public class GuidangmanageEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 8547525172492818975L;

    @Autowired
    private IGuidangmanageService service;

    /**
     * 归档管理实体对象
     */
    private Guidangmanage dataBean = null;

    private List<Record> ouModel = null;

    private List<Record> taskModel = null;

    private List<Record> XiaQuModel = null;

    protected ICommonDao dao;

    public String xiaqucode;
    public String oucode;
    public String taskid;

    public String getXiaqucode() {
        return xiaqucode;
    }

    public void setXiaqucode(String xiaqucode) {
        this.xiaqucode = xiaqucode;
    }

    public String getOucode() {
        return oucode;
    }

    public void setOucode(String oucode) {
        this.oucode = oucode;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    @Override
    public void pageLoad() {
        dao = CommonDao.getInstance();
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new Guidangmanage();
        }
        else {
            xiaqucode = dataBean.getAreacode();
            oucode = dataBean.getOucode();
            addCallbackParam("xiaqucode", dataBean.getAreacode());
            addCallbackParam("oucode", dataBean.getOucode());
            addCallbackParam("taskid", dataBean.getTaskid());
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        if (CheckIsExist(getRequestParameter("guid"))) {
            dataBean.setOperatedate(new Date());
            dataBean.setOucode(oucode);
            dataBean.setAreacode(xiaqucode);
            dataBean.setTaskid(taskid);
            service.update(dataBean);
            addCallbackParam("status", "1");
            addCallbackParam("msg", "修改成功！");
        }
        else {
            addCallbackParam("status", "2");
            addCallbackParam("msg", "该事项已被维护，请确认事项！");
        }
    }

    public Guidangmanage getDataBean() {
        return dataBean;
    }

    public void setDataBean(Guidangmanage dataBean) {
        this.dataBean = dataBean;
    }

    protected boolean CheckIsExist(String rowguid) {
        String sql = "select taskid from guidangmanage where taskid=?1 and rowguid!=?2";
        Record rd = dao.find(sql, Record.class, taskid, rowguid);
        if (rd == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public List<Record> getXiaQuModel() {
        if (XiaQuModel == null) {
            String sql = "select XiaQuName,XiaQuCode from audit_orga_area  ORDER BY CityLevel ASC";
            XiaQuModel = dao.findList(sql, Record.class);
        }
        return this.XiaQuModel;
    }

    public List<Record> getouModel() {
        if (xiaqucode != null) {
            String sql = "select ouname,oucode from frame_ou a inner join frame_ou_extendinfo b on "
                    + "a.OUGUID=b.OUGUID where IFNULL(oucode,'')!='' and areacode=?1 order by ORDERNUMBER desc";
            ouModel = dao.findList(sql, Record.class, xiaqucode);
        }
        return this.ouModel;
    }

    public List<Record> gettaskModel() {
        if (oucode != null) {
            String sql = "select taskname,task_id as taskid from audit_task a inner join frame_ou b on a.ouguid=b.ouguid "
                    + "and b.oucode=?1 and IFNULL(is_history,0)=0 and is_editafterimport=1 and IFNULL(is_enable,0)=1  "
                    + "order by a.ORDERNUM desc";
            taskModel = dao.findList(sql, Record.class, oucode);
        }
        return this.taskModel;
    }
}
