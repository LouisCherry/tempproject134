package com.epoint.auditqueue.qhj.action;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.code128c.TestCode128C;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

@RestController("jnqnoprintqueueaction")
@Scope("request")
public class JnQnoPrintQueueAction extends BaseController
{

    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditQueue auditqueueservice;
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    @Autowired
    private IHandleQueue handlequeueservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditOrgaServiceCenter centerservice;
    TestCode128C code128c = new TestCode128C();
    private String Centerguid = "";

    @Override
    public void pageLoad() {
        Centerguid = getRequestParameter("Centerguid");

    }

    public void rtnValue(String qno, String MacAddress) throws IOException {
        // 减纸
        equipmentservice.descLeftPiece(MacAddress);
        //总打印+1
     //   equipmentservice.ascAllPiece(MacAddress);
        String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
        AuditQueue auditqueue = auditqueueservice.getQNODetailByQNO(fieldstr, qno, Centerguid).getResult();

        if (auditqueue == null) {
            try {
                Thread.sleep(1500);
                auditqueue = auditqueueservice.getQNODetailByQNO(fieldstr, qno, Centerguid).getResult();
                log.info("小票打印 auditqueue 后：" + auditqueue);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (StringUtil.isNotBlank(auditqueue)) {
            AuditQueueTasktype auditqueuetasktype = tasktypeservice
                    .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
            if (StringUtil.isNotBlank(auditqueuetasktype)) {
                addCallbackParam("qno", auditqueue.getQno());
                addCallbackParam("PaiDuiPrint",
                        centerservice.findAuditServiceCenterByGuid(Centerguid).getResult().getCentername());
                addCallbackParam("taskname", auditqueuetasktype.getTasktypename());
                addCallbackParam("waitnum", StringUtil.getNotNullString(
                        handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                addCallbackParam("windowno", auditqueue.getHandlewindowno());
                addCallbackParam("time", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                String barCode = code128c.getCode(auditqueue.getFlowno(), "");
                addCallbackParam("flowno", code128c.kiCode128C(barCode, 40, auditqueue.getFlowno() + ".jpg"));
                addCallbackParam("flownonum", auditqueue.getFlowno());
                addCallbackParam("identitycardnum", auditqueue.getIdentitycardnum());
            }
        }
    }

}
