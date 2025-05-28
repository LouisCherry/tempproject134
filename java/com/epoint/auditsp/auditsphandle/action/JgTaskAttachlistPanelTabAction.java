package com.epoint.auditsp.auditsphandle.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.xmz.jgtaskmaterialrelation.api.IJgTaskmaterialrelationService;
import com.epoint.xmz.jgtaskmaterialrelation.api.entity.JgTaskmaterialrelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 办理工作台页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017年5月22日]
 */
@RestController("jgtaskattachlistpaneltabaction")
@Scope("request")
public class JgTaskAttachlistPanelTabAction extends BaseController {
    private static final long serialVersionUID = 2714262579121582382L;

    @Autowired
    private IJgTaskmaterialrelationService service;

    private String tab = "";

    private String itemguid = "";

    private String gccliengguid = "";

    private String jzcliengguid = "";

    private String rfcliengguid = "";

    @Override
    public void pageLoad() {
        tab = getRequestParameter("Tab");
        itemguid = getRequestParameter("itemguid");

        JgTaskmaterialrelation jgTaskmaterialrelation = service.getRelationByItemguid(itemguid);
        if (jgTaskmaterialrelation != null) {
            gccliengguid = jgTaskmaterialrelation.getGccliengguid();
            jzcliengguid = jgTaskmaterialrelation.getJzcliengguid();
            rfcliengguid = jgTaskmaterialrelation.getRfcliengguid();
        }
    }

    /**
     * 生成tab的json
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        // 工程规划批后监管
        Record gc = new Record();
        gc.put("name", "工程规划批后监管");
        gc.put("code", "gc");
        gc.put("url", "jiningzwfw/jgtaskmaterial/attachlist.html?cliengGuid=" + gccliengguid + "&yewtype=detail");
        gc.put("width","80px");
        gc.put("selected", "gc".equals(tab));
        recordList.add(gc);
        // 建筑工程质量安全监督
        Record jz = new Record();
        jz.put("name", "建筑工程质量安全监督");
        jz.put("code", "jz");
        jz.put("url", "jiningzwfw/jgtaskmaterial/attachlist.html?cliengGuid=" + jzcliengguid + "&yewtype=detail");
        jz.put("selected", "jz".equals(tab));
        recordList.add(jz);
        // 人防工程质量安全监督
        Record rf = new Record();
        rf.put("name", "人防工程质量安全监督");
        rf.put("code", "rf");
        rf.put("url", "jiningzwfw/jgtaskmaterial/attachlist.html?cliengGuid=" + rfcliengguid + "&yewtype=detail");
        rf.put("selected", "rf".equals(tab));
        recordList.add(rf);
        sendRespose(JsonUtil.listToJson(recordList));
    }

    /**
     * 刷新tabnav数值
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void refreshTabNav() {

    }

}
