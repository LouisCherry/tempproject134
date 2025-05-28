package com.epoint.auditproject.windowproject.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.cache.util.ZwfwCommonCacheUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;

/**
 * 办理工作台页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017年5月22日]
 * 
 * 
 */
@RestController("jnwindowworkpaneltabaction")
@Scope("request")
public class JnWindowWorkPanelTabAction extends BaseController
{
    private static final long serialVersionUID = 2714262579121582382L;

    private String tab = "";

    private Integer jzcount = 0;
    private static ZwfwCommonCacheUtil cacheUtil = new ZwfwCommonCacheUtil(1800);
    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaArea;

    @Autowired
    private IAuditTask taskService;

    @Autowired
    private IHandleConfig config;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IJNAuditProject iJNAuditProject;

    @Override
    public void pageLoad() {
        tab = getRequestParameter("Tab");
    }

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     * 
     * @param windowguid
     *            窗口guid
     * @return 不同状态的办件数量Map集合
     */
    public Map<String, Integer> getCountStatusByWindowguid(String windowguid) {
        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(windowguid).getResult();
        if (taskidList != null && !taskidList.isEmpty()) {
            List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
            if (taskidZJList != null && !taskidZJList.isEmpty()) {
                for (String zjTaskid : taskidZJList) {
                    taskidList.remove(zjTaskid);
                }
            }
        }
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        return iAuditProject.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList, windowguid,
                ZwfwUserSession.getInstance().getAreaCode(), ZwfwUserSession.getInstance().getCenterGuid(), area)
                .getResult();

    }

    /**
     * 生成人员库tab的json（基本信息，证照信息，申报历史）
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initTabData() {
        String cacheKey = this.getClass().getSimpleName() + "-initTabData-"
                + ZwfwUserSession.getInstance().getWindowGuid();
        String notcache = config.getFrameConfig("AS_NO_INDEXCACHE", "").getResult();
        String cacheStr = "";
        // 获取用户角色
        boolean existUserRoleGuid = iRoleService.isExistUserRoleName(UserSession.getInstance().getUserGuid(), "批量办理");

        // 先判断缓存有无数据 - 非第一次加载
        cacheStr = ZwfwConstant.CONSTANT_STR_ONE.equals(notcache) ? "" : cacheUtil.getCacheByKey(cacheKey);
        if (StringUtil.isNotBlank(cacheStr)) {
            String[] values = cacheStr.split(";");
            if (values != null && values.length > 0) {
                String json = values[0];
                List<Record> listrecord = JsonUtil.jsonToList(json, Record.class);
                for (Record record : listrecord) {
                    if (!existUserRoleGuid) {
                        String geturl = record.getStr("url");
                        // 显示批量办理
                        if (0 > (geturl.indexOf('?'))) {
                            record.set("url", record.get("url") + "?showPLBL=0");
                        }
                        else if (0 > (geturl.indexOf("showPLBL"))) {
                            record.set("url", record.get("url") + "&showPLBL=0");
                        }
                    }
                    else {
                        // 防止重启才能生效
                        record.set("url", record.getStr("url").replace("?showPLBL=0&", "?").replace("?showPLBL=0", "")
                                .replace("&showPLBL=0", ""));
                    }

                    record.put("selected", record.getStr("code").equalsIgnoreCase(tab));
                }
                sendRespose(JsonUtil.listToJson(listrecord));
            }
        }
        else {
            Map<String, Integer> map = getCountStatusByWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            List<Record> recordList = new ArrayList<Record>();
            // 待预审
            Record dys = new Record();
            dys.put("name", "待预审");
            dys.put("code", "dys");
            dys.put("count", "(" + (map == null ? "0" : map.get("DYS")) + ")");
            dys.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldys?type=DYS");
            dys.put("selected", "DYS".equals(tab));
            if (!existUserRoleGuid) {
                // 不显示批量办理
                dys.set("url", dys.get("url") + "&showPLBL=0");
            }
            recordList.add(dys);
            // 预审通过
            Record ystg = new Record();
            ystg.put("name", "预审通过");
            ystg.put("code", "ystg");
            ystg.put("count", "(" + (map == null ? "0" : map.get("YSTG")) + ")");
            ystg.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpanelystg?type=YSTG");
            ystg.put("selected", "YSTG".equals(tab));
            if (!existUserRoleGuid) {
                // 不显示批量办理
                ystg.set("url", ystg.get("url") + "&showPLBL=0");
            }
            recordList.add(ystg);

            // 预审退回
            Record ystu = new Record();
            ystu.put("name", "预审退回");
            ystu.put("code", "ystu");
            ystu.put("url",
                    "jiningzwfw/individuation/overall/auditbusiness/auditwindowbusiness/project/windowworkpanelysth?type=YSTU");
            ystu.put("selected", "YSTU".equals(tab));
            recordList.add(ystu);
            // 待接件
            Record djj = new Record();
            djj.put("name", "待接件");
            djj.put("code", "djj");
            djj.put("count", "(" + (map == null ? "0" : map.get("DJJ")) + ")");
            djj.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldjj?type=DJJ");
            djj.put("selected", "DJJ".equals(tab));
            if (!existUserRoleGuid) {
                // 不显示批量办理
                djj.set("url", djj.get("url") + "&showPLBL=0");
            }
            recordList.add(djj);
            // 待受理
            Record dsl = new Record();
            dsl.put("name", "待受理");
            dsl.put("code", "dsl");
            dsl.put("count", "(" + (map == null ? "0" : map.get("DSL")) + ")");
            dsl.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldsl?type=DSL");
            dsl.put("selected", "DSL".equals(tab));
            if (!existUserRoleGuid) {
                // 不显示批量办理
                dsl.set("url", dsl.get("url") + "&showPLBL=0");
            }
            recordList.add(dsl);
            // 待补办
            Record dbb = new Record();
            dbb.put("name", "待补正");
            dbb.put("code", "dbb");
            dbb.put("count", "(" + (map == null ? "0" : map.get("DBB")) + ")");
            dbb.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldbb?type=DBB");
            dbb.put("selected", "DBB".equals(tab));
            recordList.add(dbb);
            // 已暂停
            Record yzt = new Record();
            yzt.put("name", "已暂停");
            yzt.put("code", "yzt");
            yzt.put("count", "(" + (map == null ? "0" : map.get("YZT")) + ")");
            yzt.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpanelyzt?type=YZT");
            yzt.put("selected", "YZT".equals(tab));
            recordList.add(yzt);
            // 待审核 阳佳
            Record dsh = new Record();
            dsh.put("name", "待审核");
            dsh.put("code", "dsh");
            dsh.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldsh");
            if (!existUserRoleGuid) {
                // 不显示批量办理
                dsh.set("url", dsh.get("url") + "?showPLBL=0");
            }
            recordList.add(dsh);
            // 待办结
            Record dbj = new Record();
            dbj.put("name", "待办结");
            dbj.put("code", "dbj");
            dbj.put("count", "(" + (map == null ? "0" : map.get("DBJ")) + ")");
            dbj.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/windowworkpaneldbj?type=DBJ");
            dbj.put("selected", "DBJ".equals(tab));
            if (!existUserRoleGuid) {
                // 不显示批量办理
                dbj.set("url", dbj.get("url") + "&showPLBL=0");
            }
            recordList.add(dbj);
            // 待办事宜
            Record waitneedhandle = new Record();
            waitneedhandle.put("name", "待办事宜");
            waitneedhandle.put("code", "waitneedhandle");
            waitneedhandle.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/waithandlelist");
            recordList.add(waitneedhandle);

            // 自建系统办件
            Record projectzijian = new Record();
            projectzijian.put("name", "自建系统办件");
            projectzijian.put("code", "projectzijian");
            projectzijian.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/auditprojectzijianlist");
            recordList.add(projectzijian);

            // RPA办件
            Record projectrpa = new Record();
            projectrpa.put("name", "RPA办件");
            projectrpa.put("code", "projectrpa");
            projectrpa.put("url", "epointzwfw/auditbusiness/auditwindowbusiness/project/auditprojectrpalist");
            recordList.add(projectrpa);

            sendRespose(JsonUtil.listToJson(recordList));
            String value = JsonUtil.listToJson(recordList);
            cacheUtil.putCacheByKey(cacheKey, value);
        }

    }

    /**
     * 刷新tabnav数值
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void refreshTabNav() {
        Map<String, Integer> map = getCountStatusByWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
        jzcount = getJZList(ZwfwUserSession.getInstance().getAreaCode());
        // 待接件
        addCallbackParam("djj", map == null ? "0" : map.get("DJJ"));
        // 待受理
        addCallbackParam("dsl", map == null ? "0" : map.get("DSL"));
        // 待补办
        addCallbackParam("dbb", map == null ? "0" : map.get("DBB"));
        // 已暂停
        addCallbackParam("yzt", map == null ? "0" : map.get("YZT"));
        // 已办结
        addCallbackParam("dbj", map == null ? "0" : map.get("DBJ"));
        // 待预审
        addCallbackParam("dys", map == null ? "0" : map.get("DYS"));
        // 审核通过
        addCallbackParam("ystg", map == null ? "0" : map.get("YSTG"));
        // 预审退回
        addCallbackParam("ystu", map == null ? "0" : map.get("YSTU"));
        // 不进驻中心办件
        addCallbackParam("jz", jzcount);
    }

    /**
     * 获取不进驻中心办件
     * 
     * @param fieldstr
     * @param first
     * @param pageSize
     * @param areaCode
     * @param applyerName
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getJZList(String areaCode) {
        List<String> ouguidList = new ArrayList<>();
        AuditOrgaArea area = getAuditOrgaAreaByCode();
        int total = 0;
        if (area != null) {
            List<FrameOu> frameoulist = ouService.listDependOuByParentGuid(area.getOuguid(), "", 2);
            for (FrameOu ouguid : frameoulist) {
                ouguidList.add(ouguid.getOuguid());
            }
            total = iJNAuditProject.getJzListCount(ouguidList, areaCode);

        }
        return total;
    }

    /**
     * 根据areacode获取ouguid
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditOrgaArea getAuditOrgaAreaByCode() {
        Map<String, String> map = new HashMap<String, String>(16);
        ZwfwUserSession instance = ZwfwUserSession.getInstance();
        map.put("xiaqucode=", instance.getAreaCode());
        return auditOrgaArea.getAuditArea(map).getResult();
    }

}
