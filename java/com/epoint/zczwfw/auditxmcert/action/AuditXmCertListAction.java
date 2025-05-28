package com.epoint.zczwfw.auditxmcert.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.zczwfw.auditxmcert.api.IAuditXmCertService;
import com.epoint.zczwfw.auditxmcert.api.entity.AuditXmCert;

/**
 * 项目证照表list页面对应的后台
 * 
 * @author 吕闯
 * @version [版本号, 2022-06-10 15:47:30]
 */
@RestController("auditxmcertlistaction")
@Scope("request")
public class AuditXmCertListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditXmCertService service;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IAttachService iAttachService;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditXmCert> model;

    String itemguid = "";

    public void pageLoad() {
        itemguid = getRequestParameter("itemguid");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditXmCert> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditXmCert>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditXmCert> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<CodeItems> codeItemsList = iCodeItemsService.listCodeItemsByCodeName("项目证照");
                    Date nowDate = new Date();
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    SQLManageUtil sqlManageUtil = new SQLManageUtil();
                    // 初始化数据
                    for (CodeItems codeItems : codeItemsList) {
                        sqlConditionUtil.clear();
                        sqlConditionUtil.eq("itemguid", itemguid);
                        sqlConditionUtil.eq("cert", codeItems.getItemValue());
                        String sql = sqlManageUtil.buildSqlComoplete(AuditXmCert.class, sqlConditionUtil.getMap());
                        AuditXmCert auditXmCert = service.find(sql, "");
                        if (auditXmCert == null) {
                            AuditXmCert dataBean = new AuditXmCert();
                            dataBean.setRowguid(UUID.randomUUID().toString());
                            dataBean.setOperatedate(nowDate);
                            dataBean.setItemguid(itemguid);
                            dataBean.setCert(codeItems.getItemValue());
                            dataBean.setClengguid(UUID.randomUUID().toString());
                            service.insert(dataBean);
                        }
                    }
                    // 生成DataGridData列表数据
                    sqlConditionUtil.clear();
                    sqlConditionUtil.eq("itemguid", itemguid);
                    sqlConditionUtil.setOrderAsc("cert");
                    String sql = sqlManageUtil.buildSqlComoplete(AuditXmCert.class, sqlConditionUtil.getMap());
                    List<AuditXmCert> list = service.findList(sql, first, pageSize, new ArrayList<>().toArray());
                    for (AuditXmCert auditXmCert : list) {
                        int num = iAttachService.getAttachCountByClientGuid(auditXmCert.getClengguid());
                        auditXmCert.put("num", num);
                    }
                    sqlConditionUtil.setSelectFields("count(*)");
                    String countSql = sqlManageUtil.buildSqlComoplete(AuditXmCert.class, sqlConditionUtil.getMap());
                    Integer count = service.countAuditXmCert(countSql);
                    this.setRowCount(count != null ? count : 0);
                    return list;
                }

            };
        }
        return model;
    }
}
