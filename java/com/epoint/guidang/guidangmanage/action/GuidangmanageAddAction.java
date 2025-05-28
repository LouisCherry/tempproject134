package com.epoint.guidang.guidangmanage.action;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
 * 归档管理新增页面对应的后台
 * 
 * @author chengninghua
 * @version [版本号, 2017-12-15 15:11:49]
 */
@RightRelation(GuidangmanageListAction.class)
@RestController("guidangmanageaddaction")
@Scope("request")
public class GuidangmanageAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4740236922704254724L;
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
        dataBean = new Guidangmanage();
        dao = CommonDao.getInstance();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        if (CheckIsExist()) {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setAreacode(xiaqucode);
            dataBean.setOucode(oucode);
            dataBean.setTaskid(taskid);
            service.insert(dataBean);
            addCallbackParam("status", "1");
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
        }
        else {
            addCallbackParam("status", "2");
            addCallbackParam("msg", "该事项已维护，请至修改页面进行修改！");
        }
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        if (CheckIsExist()) {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setAreacode(xiaqucode);
            dataBean.setOucode(oucode);
            dataBean.setTaskid(taskid);
            service.insert(dataBean);
            addCallbackParam("status", "1");
            addCallbackParam("msg", "保存成功！");
            dataBean = new Guidangmanage();
        }
        else {
            addCallbackParam("status", "2");
            addCallbackParam("msg", "该事项已维护，请至修改页面进行修改！");
        }
    }

    protected boolean CheckIsExist() {
        String sql = "select taskid from guidangmanage where taskid=?1";
        Record rd = dao.find(sql, Record.class, taskid);
        if (rd == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public Guidangmanage getDataBean() {
        if (dataBean == null) {
            dataBean = new Guidangmanage();
        }
        return dataBean;
    }

    public void setDataBean(Guidangmanage dataBean) {
        this.dataBean = dataBean;
    }

    public List<Record> getXiaQuModel() {
        if (XiaQuModel == null) {
            String sql = "select XiaQuName,XiaQuCode from audit_orga_area where length(XiaQuCode) = 6 ORDER BY CityLevel ASC";
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
