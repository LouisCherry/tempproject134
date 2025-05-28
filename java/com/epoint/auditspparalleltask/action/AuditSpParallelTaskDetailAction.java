package com.epoint.auditspparalleltask.action;

import com.epoint.auditspparalleltask.api.IAuditSpParallelTaskService;
import com.epoint.auditspparalleltask.api.entity.AuditSpParallelTask;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 并行阶段阶段事项配置表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:18:40]
 */
@RightRelation(AuditSpParallelTaskListAction.class)
@RestController("auditspparalleltaskdetailaction")
@Scope("request")
public class AuditSpParallelTaskDetailAction extends BaseController
{
    @Autowired
    private IAuditSpParallelTaskService service;

    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    /**
     * 并行阶段阶段事项配置表实体对象
     */
    private AuditSpParallelTask dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpParallelTask();
        }

        String[] phaseguids = dataBean.getPhaseguid().split(",");
        List<String> arrList = Arrays.asList(phaseguids);
        List<String> phasename = new ArrayList<>();
        if (!arrList.isEmpty()) {
            for (String string : arrList) {
                AuditSpPhaseBaseinfo find = iAuditSpPhaseBaseinfoService.find(string);
                if (find != null) {
                    phasename.add(find.getPhasename());
                }
            }
        }
        dataBean.setPhaseguid(StringUtil.join(phasename, ","));

    }

    public AuditSpParallelTask getDataBean() {
        return dataBean;
    }
}
