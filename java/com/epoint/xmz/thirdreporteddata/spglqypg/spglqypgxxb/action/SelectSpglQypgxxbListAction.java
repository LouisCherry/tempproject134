package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.action;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import com.epoint.xmz.thirdreporteddata.spglqypgxxbeditr.api.ISpglQypgxxbEditRService;
import com.epoint.xmz.thirdreporteddata.spglqypgxxbeditr.api.entity.SpglQypgxxbEditR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 区域评估信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
@RestController("selectspglqypgxxblistaction")
@Scope("request")
public class SelectSpglQypgxxbListAction extends BaseController {
    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;
    @Autowired
    private ISpglQypgsxxxbService iSpglQypgsxxxbService;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private ISpglQypgxxbEditRService iSpglQypgxxbEditRService;
    /**
     * 区域评估信息表实体对象
     */
    private SpglQypgxxb dataBean = null;

    private String preitemguid;
    private String subappguid;
    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgxxb> qypgModel;

    public void pageLoad() {
        preitemguid = getRequestParameter("preitemguid");
        subappguid = getRequestParameter("subappguid");
    }

    /**
     * 确定关联
     */
    public void confirm() {
        List<String> select = getQypgDatagrid().getSelectKeys();
        Date date = new Date();
        for (String sel : select) {
            SpglQypgxxbEditR spglQypgxxbEditR = new SpglQypgxxbEditR();
            spglQypgxxbEditR.setCreatedate(date);
            spglQypgxxbEditR.setQypgguid(sel);
            spglQypgxxbEditR.setSubappguid(subappguid);
            spglQypgxxbEditR.setPre_itemguid(preitemguid);
            spglQypgxxbEditR.setRowguid(UUID.randomUUID().toString());
            iSpglQypgxxbEditRService.insert(spglQypgxxbEditR);
        }
        addCallbackParam("msg", l("成功关联！"));
    }

    public DataGridModel<SpglQypgxxb> getQypgDatagrid() {
        // 获得表格对象
        if (qypgModel == null) {
            qypgModel = new DataGridModel<SpglQypgxxb>() {
                @Override
                public List<SpglQypgxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getNotAssociationPageData(preitemguid, first,
                            pageSize, sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return qypgModel;
    }

    public SpglQypgxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglQypgxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglQypgxxb dataBean) {
        this.dataBean = dataBean;
    }

}
