package com.epoint.jnzwfw.auditkaoqinchecktime;

import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditorga.auditdepartment.inter.IAuditOrgaDepartment;
import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.auditorga.audittakeoff.domain.AuditOrgaTakeoff;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 中心每日考勤统计表
 *
 * @author TYX
 * @version [版本号, 2017-03-21 16:44:52]
 */
@RestController("auditkaoqintjdayaction1")
@Scope("request")
public class AuditKaoQinTJDayAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 3822691805245064160L;
    /**
     * 代码项service
     */
    @Autowired
    private IAuditOrgaMember memberService;
    @Autowired
    private ZhenggaiService zhenggaiImpl;
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
        String month = kaoqinMonth;
        if (kaoqinMonth.length() == 1) {
            month = "0" + kaoqinMonth;
        }
        Integer daysofmonth = getDaysByYearMonth(Integer.parseInt(kaoqinYear), Integer.parseInt(kaoqinMonth));
        Map<String, Integer> map = null;
        List<Map<String, Integer>> list = new ArrayList<>();
        for (int i = 1; i <= daysofmonth; i++) {
            map = new HashMap<String, Integer>(16);
            map.put("day", i);
            list.add(map);
        }
        addCallbackParam("day", list);
        addCallbackParam("week", kaoqingWeek(daysofmonth));
        addCallbackParam("ouuser", ouuser(daysofmonth, month));
    }

    // 获取部门用户信息
    public List<Map<String, Object>> ouuser(Integer daysofmonth, String month) {
        SqlConditionUtil sql = new SqlConditionUtil();
        String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        sql.setOrderAsc("gonghao");
        if (kaoqinDept.length() > 0) {
            sql.eq("ouguid", kaoqinDept);
        } else {
            sql.eq("centerguid", centerguid);
        }
        sql.eq("Is_KaoQin", "1");
        List<AuditOrgaMember> member = memberService.getAuditMemberList(sql.getMap()).getResult();
        List<Record> kaoqindaylist = new KaoqinCheckInfoService().getKaoqinInfoAll(kaoqinYear, month);
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listss = new ArrayList<Map<String, Object>>();

        if (kaoqindaylist != null) {
            for (AuditOrgaMember members : member) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ouname", members.getOuname());
                map.put("username", members.getUsername());
                map.put("gonghao", members.getGonghao());
                String userid = zhenggaiImpl.getuseridbyid(members.getUserguid());

                List<Record> kaoqinday = new KaoqinCheckInfoService().getKaoqinInfoByGh(kaoqinYear, month, userid);
                map.put("kaoqingdata", kaoqinData(kaoqinday, daysofmonth));
                lists.add(map);
                listss.add(map);
            }
        }
        return listss;
    }

    // 获取考勤记录
    public List<Map<String, String>> kaoqinData(List<Record> kaoqinday, Integer daysofmonth) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> mappm = null;
        for (int j = 1; j <= daysofmonth; j++) {
            mappm = new HashMap<String, String>(16);
            String strKQDate = kaoqinYear + "-" + kaoqinMonth + "-" + j;
            String showDate = "";
            try {
                showDate = fmt.format(fmt.parse(strKQDate));
            } catch (Exception e) {
            }
            List<Record> kqp = new ArrayList<Record>();
            for (Record kq : kaoqinday) {
                String showDate2 = "";
                try {
                    showDate2 = fmt.format(fmt.parse(kq.getStr("userchecktime")));
                } catch (Exception e) {
                }
                if (showDate.equals(showDate2)) {
                    kqp.add(kq);
                }
            }
            String status = "";
            if (kqp.size() != 0) {
                String time = "";
                for (Record kq : kqp) {
                    if (!"NotSigned".equals(kq.getStr("timeresult"))) {
                        time = kq.getStr("userchecktime").substring(11, 16) + "\n";
                        status += time;
                    }
                }
            }
            mappm.put("time", status);
            status = "";
            list.add(mappm);
        }
        return list;
    }

    /**
     * 初始化数据信息
     */
    public void initData() {

    }

    // 获取星期数据
    public List<Map<String, String>> kaoqingWeek(Integer daysofmonth) {
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> map = null;
        List<Map<String, String>> list = new ArrayList<>();
        String riqi = "";
        for (int i = 1; i <= daysofmonth; i++) {
            riqi = kaoqinYear + "-" + kaoqinMonth + "-";
            riqi = riqi + String.valueOf(i);
            try {
                date = fmt.parse(riqi);
            } catch (Exception e) {
            }
            map = new HashMap<String, String>(16);
            map.put("week", getWeekOfDate(date));
            list.add(map);
        }
        return list;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     *
     * @param year
     * @param month
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
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
            //system.out.println("======>>>部门"+userSession.getOuGuid());
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
