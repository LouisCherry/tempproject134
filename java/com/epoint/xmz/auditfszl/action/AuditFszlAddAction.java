package com.epoint.xmz.auditfszl.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;

/**
 * 放射诊疗数据新增页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:15]
 */
@RightRelation(AuditFszlListAction.class)
@RestController("auditfszladdaction")
@Scope("request")
public class AuditFszlAddAction extends BaseController {
    @Autowired
    private IAuditFszlService service;

    @Autowired
    private IAuditFszlPersonService auditFszlPersonService;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 放射诊疗数据实体对象
     */
    private AuditFszl dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditFszlPerson> gzryModel;


    public void pageLoad() {
        dataBean = new AuditFszl();
        String rowguid = getViewData("rowguid");
        if(StringUtil.isBlank(rowguid)){
            String rowGuid = UUID.randomUUID().toString();
            addCallbackParam("rowGuid",rowGuid);
            addViewData("rowguid",rowGuid);
        }

    }

    /**
     * 保存并关闭
     */
    public void add() {

        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("certno",dataBean.getCertno());
        sqlc.eq("is_history",ZwfwConstant.CONSTANT_STR_ZERO);
        AuditFszl auditFszl = service.find("select * from audit_fszl " + new SqlHelper().getSql(sqlc.getMap()),new ArrayList<>());
        if(auditFszl != null){
            addCallbackParam("err","当前放射诊疗许可证号已存在");
            return;
        }

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setRowguid(getViewData("rowguid"));
        // 新增默认设置值
        dataBean.setIs_history(ZwfwConstant.CONSTANT_STR_ZERO);
        dataBean.setVersion(ZwfwConstant.CONSTANT_INT_ONE);
        dataBean.setSendtip(ZwfwConstant.CONSTANT_STR_ZERO);
        dataBean.setVersiondate(new Date());
        dataBean.setUpdatetime(new Date());
        dataBean.setFszlid(UUID.randomUUID().toString());
        dataBean.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
        // 计算下一次校验日期
        Date jydate = dataBean.getJydate();
        String validitydate = dataBean.getValiditydate();
        dataBean.setNextjydate(getNextJydate(jydate,validitydate));
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditFszl();
    }


    /**
     * 经济类型
     * @return
     */
    public List<SelectItem> getSyzxsModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("竣工许可所有制形式");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 校验有效期
     * @return
     */
    public List<SelectItem> getJyyyqModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("校验有效期");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 许可项目下拉
     *
     * @return
     */
    public List<SelectItem> getXkxmModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可项目");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    /**
     * 许可证状态
     *
     * @return
     */
    public List<SelectItem> getXkzztModel() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("放射诊疗许可证状态");
        for (CodeItems codeItem : codeItems) {
            result.add(new SelectItem(codeItem.getItemValue(), codeItem.getItemText()));
        }
        return result;
    }

    public static Date getNextJydate(Date date,String jydate){
        if(StringUtil.isNotBlank(jydate)){
            switch (jydate){
                case "15年":
                    return EpointDateUtil.addYear(date,3);
                case "5年":
                case "长期":
                    return EpointDateUtil.addYear(date,1);
                default:
            }
        }
        return null;
    }

    public AuditFszl getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszl();
        }
        return dataBean;
    }

    public void setDataBean(AuditFszl dataBean) {
        this.dataBean = dataBean;
    }


}
