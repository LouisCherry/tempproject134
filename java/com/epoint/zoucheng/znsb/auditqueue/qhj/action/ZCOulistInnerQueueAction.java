package com.epoint.zoucheng.znsb.auditqueue.qhj.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController("zcoulistinnerqueueaction")
@Scope("request")
public class ZCOulistInnerQueueAction extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 5744552639400444526L;
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
    @Autowired
    private IOuService ouService9;
    private final static int size = 16; // 每页数据量
    private List<Record> rtnValue = new ArrayList<>();

    String Centerguid;
    String hallguid;

    @Override
    public void pageLoad() {
        Centerguid = getRequestParameter("Centerguid");
        hallguid = getRequestParameter("hallguid");

        // 获取数据的分页数
        addCallbackParam("pages", pages(Centerguid, hallguid));
    }

    public int pages(String Centerguid, String hallguid) {
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("Centerguid=", Centerguid);
        if (!"all".equals(hallguid)) {
            // map.put("LOBBYTYPE=", hallguid);
            map.put("LOBBYTYPE in", "'" + hallguid.replace(",", "','") + "'");
        }
        map.put("IS_USEQUEUE=", QueueConstant.Common_yes_String);
        int num = windowservice.getSomeFieldsCount(map, "distinct(ouguid)");
        num = num % size == 0 ? num / size : num / size + 1;
        return num;
    }

    public void getDataJson() {
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("Centerguid=", Centerguid);
        String activehallguid = getRequestParameter("activehallguid");
        if (StringUtil.isNotBlank(activehallguid)) {
            if (!"all".equals(activehallguid)) {
                map.put("LOBBYTYPE=", activehallguid);
            }
        }
        else {
            if (!"all".equals(hallguid)) {
                // map.put("LOBBYTYPE=", hallguid);
                // 多大厅写法
                map.put("LOBBYTYPE in", "'" + hallguid.replace(",", "','") + "'");
            }
        }
        map.put("IS_USEQUEUE=", QueueConstant.Common_yes_String);
        List<AuditOrgaWindow> auditQueueWindowAndOus = windowservice
                .getSomeFieldsByPage(map, 0, 100, "ordernum", "desc", "distinct(ouguid)").getResult();
        for (AuditOrgaWindow auditOrgaWindow : auditQueueWindowAndOus) {
            Record record = new Record();
            if (StringUtil.isNotBlank(auditOrgaWindow.getOuguid())) {
                FrameOu ou = ouService9.getOuByOuGuid(auditOrgaWindow.getOuguid());
                if (StringUtil.isNotBlank(ou)) {
                    record.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                    record.put("ouguid", auditOrgaWindow.getOuguid());
                    record.put("ordernum", ou.getOrderNumber());
                }
                else {
                    record.put("ouguid", auditOrgaWindow.getOuguid());
                    record.put("ouname", "");
                    record.put("ordernum", ou.getOrderNumber());
                }

            }
            rtnValue.add(record);
        }

        // 按照是否有效对list进行排序
        Collections.sort(rtnValue, new Comparator<Record>()
        {
            @Override
            public int compare(Record o1, Record o2) {
                if (o1.getInt("ordernum") < o2.getInt("ordernum")) {
                    return 1;
                }
                if (o1.getInt("ordernum") == o2.getInt("ordernum")) {
                    return 0;
                }
                return -1;
            }
        });
        int firstint = Integer.parseInt(getRequestParameter("cur")) * size;
        int endint = (firstint + size) >= rtnValue.size() ? rtnValue.size() : (firstint + size);
        List<Record> rtnlist = rtnValue.subList(firstint, endint);
        addCallbackParam("html", JsonUtil.listToJson(rtnlist));
    }
}
