package com.epoint.jnzwfw.auditkaoqinchecktime;

import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditorga.auditdepartment.inter.IAuditOrgaDepartment;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.auditorga.audittakeoff.domain.AuditOrgaTakeoff;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 中心每日考勤统计表
 *
 * @author TYX
 * @version [版本号, 2017-03-21 16:44:52]
 */
@RestController("auditkaoqintjmonthaction2")
@Scope("request")
public class AuditKaoQinTJMonthAction2 extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 3822691805245064160L;
    /**
     * 代码项service
     */
    @Autowired
    private IAuditOrgaMember memberService;

    /**
     * 中心请假记录表实体对象
     */
    private AuditOrgaTakeoff dataBean = null;

    /**
     * 部门
     */
    @Autowired
    private IAuditOrgaDepartment auditDepartment;

    /**
     * 年份列表
     */
    private List<SelectItem> auditYearModal;

    /**
     * 月份列表
     */
    private List<SelectItem> auditMonthModal;

    /**
     * 部门列表
     */
    private List<SelectItem> auditDeptModal;

    private String kaoqinYear;
    private String kaoqinMonth;
    private String kaoqinDept;

    @Override
    public void pageLoad() {
        dataBean = new AuditOrgaTakeoff();
        addCallbackParam("ouguid", userSession.getOuGuid());
        addCallbackParam("ouname", userSession.getOuName());

    }

    public AuditOrgaTakeoff getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaTakeoff();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaTakeoff dataBean) {
        this.dataBean = dataBean;
    }

    // 返回表数据
    public void getData() {
        if ("".equals(kaoqinYear) || kaoqinYear == null) {
            kaoqinYear = String.valueOf(EpointDateUtil.getYearOfDate(new Date()));
        }
        if ("".equals(kaoqinMonth) || kaoqinMonth == null) {
            kaoqinMonth = String.valueOf(EpointDateUtil.getMonthOfDate(new Date()) + 1);
        }
        addCallbackParam("ouuser", ouuser());
    }

    // 获取部门用户信息
    public List<Map<String, Object>> ouuser() {
        CommonDao dao = CommonDao.getInstance();
        String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        String sql = "";
        if (kaoqinDept.length() > 0) {
            sql = "select * from Audit_DDKQTJ_Month where year='" + kaoqinYear + "' and month='" + kaoqinMonth
                    + "' and ouguid='" + kaoqinDept
                    + "' and userguid in (select userguid from audit_orga_member where Is_KaoQin='1') order by ouguid asc,gonghao asc";
        } else {
            sql = "select * from Audit_DDKQTJ_Month where year='" + kaoqinYear + "' and month='" + kaoqinMonth
                    + "' and userguid in (select userguid from audit_orga_member where Is_KaoQin='1' and centerguid='" + centerguid + "')  order by ouguid asc,gonghao asc";
        }
        List<Record> list = dao.findList(sql, Record.class);
        List<Map<String, Object>> listss = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ouname", list.get(i).getStr("ouname"));
            map.put("username", list.get(i).getStr("username"));
            map.put("gonghao", list.get(i).getStr("gonghao"));
            map.put("workday", list.get(i).getStr("workday"));
            map.put("takeoffday", list.get(i).getStr("takeoffday"));
            map.put("ztcount", list.get(i).getStr("ztcount"));
            map.put("zttime", gethour(list.get(i).getStr("zttime")));
            map.put("cdcount", list.get(i).getStr("cdcount"));
            map.put("cdtime", gethour(list.get(i).getStr("cdtime")));
            map.put("yzcdcount", list.get(i).getStr("yzcdcount"));
            map.put("yzcdtime", gethour(list.get(i).getStr("yzcdtime")));
            map.put("kgcdcount", list.get(i).getStr("kgcdcount"));
            map.put("kgcdtime", gethour(list.get(i).getStr("kgcdtime")));
            map.put("gcdays", list.get(i).getStr("gcdays"));
            map.put("day1", list.get(i).getStr("day1"));
            map.put("day2", list.get(i).getStr("day2"));
            map.put("day3", list.get(i).getStr("day3"));
            map.put("day4", list.get(i).getStr("day4"));
            map.put("day5", list.get(i).getStr("day5"));
            map.put("day6", list.get(i).getStr("day6"));
            map.put("day7", list.get(i).getStr("day7"));
            map.put("day8", list.get(i).getStr("day8"));
            map.put("day9", list.get(i).getStr("day9"));
            map.put("day10", list.get(i).getStr("day10"));
            map.put("day11", list.get(i).getStr("day11"));
            map.put("day12", list.get(i).getStr("day12"));
            map.put("day13", list.get(i).getStr("day13"));
            map.put("day14", list.get(i).getStr("day14"));
            map.put("day15", list.get(i).getStr("day15"));
            map.put("day16", list.get(i).getStr("day16"));
            map.put("day17", list.get(i).getStr("day17"));
            map.put("day18", list.get(i).getStr("day18"));
            map.put("day19", list.get(i).getStr("day19"));
            map.put("day20", list.get(i).getStr("day20"));
            map.put("day21", list.get(i).getStr("day21"));
            map.put("day22", list.get(i).getStr("day22"));
            map.put("day23", list.get(i).getStr("day23"));
            map.put("day24", list.get(i).getStr("day24"));
            map.put("day25", list.get(i).getStr("day25"));
            map.put("day26", list.get(i).getStr("day26"));
            map.put("day27", list.get(i).getStr("day27"));
            map.put("day28", list.get(i).getStr("day28"));
            map.put("day29", list.get(i).getStr("day29"));
            map.put("day30", list.get(i).getStr("day30"));
            map.put("day31", list.get(i).getStr("day31"));
            listss.add(map);
        }
        return listss;
    }

    public String gethour(String time1) {
        int time = Integer.parseInt(time1);
        int hours = (int) Math.floor(time / 60);
        int minute = time % 60;
        if (hours > 0) {
            return hours + "小时" + minute + "分钟";
        } else {
            if (minute > 0) {
                return minute + "分钟";
            } else {
                return "";
            }
        }
    }

    /**
     * 初始化数据信息
     */
    public void initData() {

    }

    public List<SelectItem> getYearModal() {
        if (auditYearModal == null) {
            auditYearModal = new ArrayList<>();
            Date dt = new Date();
            for (int i = -10; i < 10; i++) {
                auditYearModal.add(new SelectItem(String.valueOf(EpointDateUtil.getYearOfDate(dt) + i),
                        String.valueOf(EpointDateUtil.getYearOfDate(dt) + i)));
            }
        }
        return this.auditYearModal;
    }

    public List<SelectItem> getMonthModal() {
        if (auditMonthModal == null) {
            auditMonthModal = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                String monthValue = String.valueOf(i);
                if (monthValue.length() == 1) {
                    monthValue = "0" + monthValue;
                }
                auditMonthModal.add(new SelectItem(i, monthValue));
            }
        }
        return this.auditMonthModal;
    }

    public List<SelectItem> getDeptModal() {
        if (auditDeptModal == null) {
            auditDeptModal = new ArrayList<>();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
            if ("4e398b61-0dd0-476f-83cd-f653bea67425".equals(userSession.getOuGuid()) || "37e207d7-bcb2-4a6a-81c7-46026ccbea34".equals(userSession.getOuGuid())) {
                auditDeptModal.add(new SelectItem("", "全部"));
            } else {
                sql.eq("ouguid", userSession.getOuGuid());
            }
            List<AuditOrgaDepartment> ouList = auditDepartment.getAuditDepartmentList(sql.getMap()).getResult();
            for (AuditOrgaDepartment ou : ouList) {
                auditDeptModal.add(new SelectItem(ou.getOuguid(), ou.getOuname()));
            }
        }
        return this.auditDeptModal;
    }

    public String getKaoqinYear() {
        return kaoqinYear;
    }

    public void setKaoqinYear(String kaoqinYear) {
        this.kaoqinYear = kaoqinYear;
    }

    public String getKaoqinMonth() {
        return kaoqinMonth;
    }

    public void setKaoqinMonth(String kaoqinMonth) {
        this.kaoqinMonth = kaoqinMonth;
    }

    public String getKaoqinDept() {
        return kaoqinDept;
    }

    public void setKaoqinDept(String kaoqinDept) {
        this.kaoqinDept = kaoqinDept;
    }

}
