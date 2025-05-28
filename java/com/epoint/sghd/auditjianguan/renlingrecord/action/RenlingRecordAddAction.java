package com.epoint.sghd.auditjianguan.renlingrecord.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.sghd.auditjianguan.renlingrecord.api.IRenlingRecordService;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 认领记录表新增页面对应的后台
 *
 * @author lizhenjie
 * @version [版本号, 2022-11-04 19:24:23]
 */
@RightRelation(RenlingRecordListAction.class)
@RestController("renlingrecordaddaction")
@Scope("request")
public class RenlingRecordAddAction extends BaseController {
    @Autowired
    private IRenlingRecordService service;
    /**
     * 认领记录表实体对象
     */
    private RenlingRecord dataBean = null;


    public void pageLoad() {
        dataBean = new RenlingRecord();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new RenlingRecord();
    }

    public RenlingRecord getDataBean() {
        if (dataBean == null) {
            dataBean = new RenlingRecord();
        }
        return dataBean;
    }

    public void setDataBean(RenlingRecord dataBean) {
        this.dataBean = dataBean;
    }


}
