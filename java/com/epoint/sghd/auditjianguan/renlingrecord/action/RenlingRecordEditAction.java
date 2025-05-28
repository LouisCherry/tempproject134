package com.epoint.sghd.auditjianguan.renlingrecord.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.sghd.auditjianguan.renlingrecord.api.IRenlingRecordService;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 认领记录表修改页面对应的后台
 *
 * @author lizhenjie
 * @version [版本号, 2022-11-04 19:24:23]
 */
@RightRelation(RenlingRecordListAction.class)
@RestController("renlingrecordeditaction")
@Scope("request")
public class RenlingRecordEditAction extends BaseController {

    @Autowired
    private IRenlingRecordService service;

    /**
     * 认领记录表实体对象
     */
    private RenlingRecord dataBean = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new RenlingRecord();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setOpiniontime(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public RenlingRecord getDataBean() {
        return dataBean;
    }

    public void setDataBean(RenlingRecord dataBean) {
        this.dataBean = dataBean;
    }

}
