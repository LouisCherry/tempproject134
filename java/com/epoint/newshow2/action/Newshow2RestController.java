package com.epoint.newshow2.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnzwfw.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;
import com.epoint.newshow2.api.Newshow2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/nowshow")
public class Newshow2RestController {
    @Autowired
    private Newshow2Service newshow2Service;

    @Autowired
    private IJNAuditProjectRestService iJNAuditProjectRestService;

    @Autowired
    private IAuditOrgaArea area;

    @Autowired
    private ICodeItemsService iCodeItemsService;


    @Autowired
    private ISpglDfxmsplcjdsxxxb iSpglDfxmsplcjdsxxxb;


    /***
     *
     *  [取号情况]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTakeNum", method = RequestMethod.POST)
    public String getTakeNum(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String centerGuid = result.getString("centerGuid");
            JSONObject dataJson = new JSONObject();
            JSONObject dataJson1 = new JSONObject();
            Record record = newshow2Service.getDaycount(centerGuid);
            int yearcount = newshow2Service.getYearcount(centerGuid);
            dataJson1.put("total", yearcount);
            dataJson1.put("progress", record.get("nowcount"));
            dataJson1.put("today", record.get("daycount"));
            dataJson1.put("waiting", record.get("waitcount"));
            dataJson.put("data", dataJson1);

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [办件满意度]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSatisfy", method = RequestMethod.POST)
    public String getSatisfy(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject dataJson1 = new JSONObject();
        JSONObject result = JSON.parseObject(params);
        List<String> record = new ArrayList<String>();
        String areaCode = result.getString("areaCode");
        try {
            AuditProject auditProject = iJNAuditProjectRestService.getSatisfiedCount(areaCode);
            if (auditProject != null) {


                DecimalFormat df = new DecimalFormat("0.0");
//                String verymanyidu = verySatisfiedCount + " 件";
                String verymanyidu = iCodeItemsService.getItemValueByCodeID("1016273", "好差评非常满意") + " 件";
                String manyidu = iCodeItemsService.getItemValueByCodeID("1016273", "好差评满意") + " 件";
                String jbmanyidu = iCodeItemsService.getItemValueByCodeID("1016273", "好差评基本满意") + " 件";
                String dismanyidu = iCodeItemsService.getItemValueByCodeID("1016273", "好差评不满意") + " 件";
                String verydismanyidu = iCodeItemsService.getItemValueByCodeID("1016273", "好差评非常不满意") + " 件";
                String totalmanyiPercent1 = df.format((float) 1 / 1 * 100);
                record.add(verymanyidu);
                record.add(manyidu);
                record.add(jbmanyidu);
                record.add(dismanyidu);
                record.add(verydismanyidu);
                dataJson1.put("detail", record);
                dataJson1.put("city", totalmanyiPercent1);
                dataJson.put("satisfy", dataJson1);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            return JsonUtils.zwdtRestReturn("0", "出现异常", "");
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getLsSatisfy", method = RequestMethod.POST)
    public String getLsSatisfy(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject dataJson1 = new JSONObject();
        List<String> record = new ArrayList<String>();
        try {
            AuditProject auditProject = iJNAuditProjectRestService.getLsSatisfiedCount();
            if (auditProject != null) {
                DecimalFormat df = new DecimalFormat("0.0");
                String verymanyidu = auditProject.getStr("verySatisfiedCount") + " 件";
                String manyidu = auditProject.getStr("SatisfiedCount") + " 件";
                String jbmanyidu = auditProject.getStr("basicSatisfiedCount") + " 件";
                String dismanyidu = auditProject.getStr("disSatisfiedCount") + " 件";
                String verydismanyidu = auditProject.getStr("verydisSatisfiedCount") + " 件";
                String totalmanyiPercent1 = df.format((float) 1 / 1 * 100);
                record.add(verymanyidu);
                record.add(manyidu);
                record.add(jbmanyidu);
                record.add(dismanyidu);
                record.add(verydismanyidu);
                dataJson1.put("detail", record);
                dataJson1.put("city", totalmanyiPercent1);
                dataJson.put("satisfy", dataJson1);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            return JsonUtils.zwdtRestReturn("0", "出现异常", "");
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /***
     *
     *  [部门业务量top10]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getDepartmentVolumeTop10", method = RequestMethod.POST)
    public String getDepartmentVolumeTop10(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject jsonData = JSONObject.parseObject(iCodeItemsService.getItemValueByCodeID("1016179", "部门业务量top10"));
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "", jsonData.toString());


//            JSONObject dataJson = new JSONObject();
//            List<Record> list = newshow2Service.geteventType();
//            List<Record> list2 = new ArrayList<>();
//            if(list!=null&&list.size()>0){
//                for (Record record : list) {
//                     String areacode = record.getStr("areacode");
//                     if ("370812".equals(areacode)) {
//                         record.set("name", "兖州区");
//                         list2.add(record); 
//                     }else{
//                         AuditOrgaArea orgaArea = area.getAreaByAreacode(areacode).getResult();
//                         if(orgaArea!=null){
//                             if(StringUtil.isNotBlank(orgaArea.getXiaquname())){
//                                 String name = orgaArea.getXiaquname();
//                                 record.set("name", name);
//                                 list2.add(record); 
//                             }
//                         }
//                     }
//                 }
//             }
//            dataJson.put("data", list2);
//            //system.out.println(dataJson.toString());
//            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [部门业务量top10]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getLsDepartmentVolumeTop10", method = RequestMethod.POST)
    public String getLsDepartmentVolumeTop10(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            JSONObject result = JSON.parseObject(params);
            String centerGuid = result.getString("centerGuid");
            //String weekBegin = getWeekBegin();
            //修改为本月除当天
            String weekBegin = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-01");
            List<Record> list2 = new ArrayList<>();

            List<Record> list = newshow2Service.getWindowNumByCenterguid(centerGuid);
            String name = "";
            int value = 0;
            for (Record record : list) {
                Record rr = new Record();
                name = record.getStr("name");
                value = record.getInt("value");
                String ouguid = record.getStr("ouguid");
                rr.set("name", name);
                rr.set("value", value);
                Record rec = newshow2Service.getQueueNumByOuguid(weekBegin, centerGuid, ouguid);
                if (rec != null) {
                    rr.set("money", rec.getInt("total"));
                }
                list2.add(rr);
            }
            dataJson.put("data", list2);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [地图数据]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getMapData", method = RequestMethod.POST)
    public String getMapData(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            JSONObject dataJson1 = new JSONObject();
            List<Record> list = newshow2Service.getmapData();
            for (Record rec : list) {
                String areacode = rec.getStr("name");
                if (StringUtil.isNotBlank(areacode)) {
                    rec.set("name", iCodeItemsService.getItemTextByCodeName("所属区县", areacode));
                }

                if ("370892".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "经开区年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370891".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "太白湖年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370890".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "高新区年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370883".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "邹城区年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370882".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "兖州区年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370881".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "曲阜市年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370832".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "梁山县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370831".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "泗水县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370830".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "汶上县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370829".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "嘉祥县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370828".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "金乡县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370827".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "鱼台县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370826".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "微山县年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370811".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "任城区年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                } else if ("370800".equals(areacode)) {
                    String itemValue = iCodeItemsService.getItemValueByCodeID("1016273", "济宁市年度办件");
                    int parsedValue = StringUtil.isNotBlank(itemValue)? Integer.parseInt(itemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedValue);
                }

            }

            // 以下是针对value进行排序列表的具体实现
            // 创建一个新的列表，用于存储需要排序的Record对象，这里只是为了不直接修改原列表，可根据实际需求决定是否需要这样做
            List<Record> sortedList = new ArrayList<>(list);

            // 使用Collections.sort方法对新列表进行排序，通过自定义的Comparator来指定按照Record的"value"属性进行排序
            Collections.sort(sortedList, new Comparator<Record>() {
                @Override
                public int compare(Record r1, Record r2) {
                    // 按照value倒序排列，大的数在前面，所以交换比较的顺序
                    return Integer.compare(r2.getInt("value"), r1.getInt("value"));
                }
            });

            // 将排序后的列表重新放入dataJson1的"scatter"属性中，覆盖之前未排序的列表
            dataJson1.put("scatter", sortedList);

            List<Record> list1 = newshow2Service.getmapbanjian();
            for (Record rec : list1) {
                String areacode = rec.getStr("name");
                if (StringUtil.isNotBlank(areacode)) {
                    rec.set("name", iCodeItemsService.getItemTextByCodeName("所属区县", areacode));
                }
                if ("370892".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "经开区年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "经开区月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370891".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "太白湖年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "太白湖月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370890".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "高新区年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "高新区月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370883".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "邹城区年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "邹城区月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370882".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "兖州区年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "兖州区月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370881".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "曲阜市年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "曲阜市月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370832".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "梁山县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "梁山县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370831".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "泗水县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "泗水县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370830".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "汶上县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "汶上县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370829".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "嘉祥县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "嘉祥县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370828".equals(areacode)) {
                    // 处理年度办件值

                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "金乡县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件品
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "金乡县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370827".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "鱼台县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "鱼台县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370826".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "微山县年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "微山县月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370811".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "任城区年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "任城区月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                } else if ("370800".equals(areacode)) {
                    // 处理年度办件值
                    String annualItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "济宁市年度办件");
                    int parsedAnnualValue = StringUtil.isNotBlank(annualItemValue)? Integer.parseInt(annualItemValue) : 0;
                    rec.set("value", rec.getInt("value") + parsedAnnualValue);

                    // 处理月度办件值
                    String monthlyItemValue = iCodeItemsService.getItemValueByCodeID("1016273", "济宁市月度办件");
                    int parsedMonthlyValue = StringUtil.isNotBlank(monthlyItemValue)? Integer.parseInt(monthlyItemValue) : 0;
                    rec.set("MONTH", rec.getInt("MONTH") + parsedMonthlyValue);
                }

            }

            // 以下是针对value进行排序列表的具体实现
            // 创建一个新的列表，用于存储需要排序的Record对象，这里只是为了不直接修改原列表，可根据实际需求决定是否需要这样做
            List<Record> sortedList1 = new ArrayList<>(list1);

            // 使用Collections.sort方法对新列表进行排序，通过自定义的Comparator来指定按照Record的"value"属性进行排序
            Collections.sort(sortedList1, new Comparator<Record>() {
                @Override
                public int compare(Record r1, Record r2) {
                    // 按照value倒序排列，大的数在前面，所以交换比较的顺序
                    return Integer.compare(r2.getInt("value"), r1.getInt("value"));
                }
            });

            dataJson1.put("map", sortedList1);
            dataJson.put("mapData", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }



    /***
     *
     *  [地图数据]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getLsMapData", method = RequestMethod.POST)
    public String getLsMapData(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            JSONArray dataJson1 = new JSONArray();
            List<Record> list1 = newshow2Service.getLsmapbanjian();
            for (Record rec : list1) {
                String areacode = rec.getStr("name");
                if ("370832".equals(areacode)) {
                    continue;
                }
                if (StringUtil.isNotBlank(areacode)) {
                    rec.set("name", iCodeItemsService.getItemTextByCodeName("梁山所属街道", areacode));
                }

                dataJson1.add(rec);
            }
            dataJson.put("mapData", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [本年、月、日实时办件量]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getHandleEvent", method = RequestMethod.POST)
    public String getHandleEvent(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            JSONObject dataJson = new JSONObject();
            Record record = newshow2Service.gethandleEvent(areaCode);

            if ("370883".equals(areaCode)) {
                record.set("year", record.getInt("year") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城区月度办件")));
            } else if ("370832".equals(areaCode)) {
                record.set("year", record.getInt("year"));
                record.set("month", record.getInt("month"));
            } else {
                record.set("year", record.getInt("year") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市月度办件")));
            }


            long nowdate = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            String year = sdf.format(date);
            String firstdate = year + "-01-01 00-00-00";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            long time = simpleDateFormat.parse(firstdate).getTime();
            long days = (nowdate - time) / (1000 * 60 * 60 * 24L);
            record.set("day", record.getInt("year") / days);
            dataJson.put("handleEvent", record);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [市直办件总量]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getCityData", method = RequestMethod.POST)
    public String getCityData(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            String areaCode = result.getString("areaCode");
            String centerGuid = result.getString("centerGuid");
            Record record = newshow2Service.getcityDatabyid(areaCode);
            if ("370892".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "经开区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "经开区月度办件")));
            } else if ("370891".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "太白湖年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "太白湖月度办件")));
            } else if ("370890".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "高新区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "高新区月度办件")));
            } else if ("370883".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城区月度办件")));
            } else if ("370882".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "兖州区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "兖州区月度办件")));
            } else if ("370881".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "曲阜市年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "曲阜市月度办件")));
            } else if ("370832".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山县月度办件")));
            } else if ("370831".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "泗水县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "泗水县月度办件")));
            } else if ("370830".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "汶上县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "汶上县月度办件")));
            } else if ("370829".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "嘉祥县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "嘉祥县月度办件")));
            } else if ("370828".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "金乡县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "金乡县月度办件")));
            } else if ("370827".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "鱼台县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "鱼台县月度办件")));
            } else if ("370826".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "微山县年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "微山县月度办件")));
            } else if ("370811".equals(areaCode)) {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "任城区年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "任城区月度办件")));
            } else {
                record.set("total", record.getInt("total") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "济宁市年度办件")));
                record.set("month", record.getInt("month") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "济宁市月度办件")));
            }


            String day = newshow2Service.getDayQueue(centerGuid);
            record.set("day", day);
            dataJson.put("cityData", record);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [服务对象画像]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getServiceObj", method = RequestMethod.POST)
    public String getServiceObj(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            List<String> source = new ArrayList<String>();
            JSONObject dataJson = new JSONObject();
            JSONObject sexObject = new JSONObject();
            JSONArray ageArray = new JSONArray();
            JSONObject dataJson1 = new JSONObject();

            if ("370832".equals(areaCode)) {
                List<Record> applytypes = newshow2Service.getapplyerTypeByAreacode(areaCode);
                Double person = 0.0;
                Double company = 0.0;

                for (Record applytype : applytypes) {
                    if ("10".equals(applytype.getStr("APPLYERTYPE"))) {
                        company = applytype.getDouble("num");
                    } else if ("20".equals(applytype.getStr("APPLYERTYPE"))) {
                        person = applytype.getDouble("num");
                    }
                }


                Double applytotal = person + company;

                sexObject.put("male", Math.round((person / applytotal) * 10) / 10.0 * 100);
                sexObject.put("female", Math.round((company / applytotal) * 10) / 10.0 * 100);
            } else {
                sexObject.put("male", "58.3");
                sexObject.put("female", "41.7");
            }


            dataJson1.put("sex", sexObject);
            for (int i = 0; i < 6; i++) {
                JSONObject ageObject = new JSONObject();
                if (i == 0) {
                    ageObject.put("name", "0-18岁");
                    ageObject.put("value", "5");
                    ageObject.put("ratio", "5%");
                } else if (i == 1) {
                    ageObject.put("name", "19-30岁");
                    ageObject.put("value", "20");
                    ageObject.put("ratio", "20%");
                } else if (i == 2) {
                    ageObject.put("name", "31-40岁");
                    ageObject.put("value", "40");
                    ageObject.put("ratio", "40%");
                } else if (i == 3) {
                    ageObject.put("name", "41-50岁");
                    ageObject.put("value", "20");
                    ageObject.put("ratio", "20%");
                } else if (i == 4) {
                    ageObject.put("name", "51-60岁");
                    ageObject.put("value", "10");
                    ageObject.put("ratio", "10%");
                } else if (i == 5) {
                    ageObject.put("name", "60岁以上");
                    ageObject.put("value", "5");
                    ageObject.put("ratio", "5%");
                }
                ageArray.add(ageObject);
            }
            Record record = newshow2Service.getsource(areaCode);
            int wx = record.getInt("wx");
            int ck = record.getInt("chuangkou");
            int ww = record.getInt("waiwang");
            int zz = record.getInt("zz");
            int total = wx + ck + ww + zz;
            DecimalFormat df = new DecimalFormat("0.0");
            float wxnum = (float) wx * 100 / total;
            float cknum = (float) ck * 100 / total;
            float wwnum = (float) ww * 100 / total;
            float zznum = (float) zz * 100 / total;
            String wxnumstr = df.format(wxnum) + "%";
            String cknumstr = df.format(cknum) + "%";
            String wwnumstr = df.format(wwnum) + "%";
            String zznumstr = df.format(zznum) + "%";
            source.add("74.3%");
            source.add("13.7%");
            source.add("8.4%");
            source.add("3.6%");
            dataJson1.put("age", ageArray);
            dataJson1.put("source", source);
            dataJson.put("serviceObj", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [热门办理事项TOP5]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getEventTop5", method = RequestMethod.POST)
    public String getEventTop5(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject jsonData = JSONObject.parseObject(iCodeItemsService.getItemValueByCodeID("1016179", "热门办理事项TOP5"));
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "", jsonData.toString());

//            JSONObject result = JSON.parseObject(params);
//            String areaCode = result.getString("areaCode");
//            JSONObject dataJson = new JSONObject();
//            List<Record> list = newshow2Service.geteventTop5(areaCode);
//            dataJson.put("eventTop5", list);
//            //system.out.println(dataJson);
//            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }


    @RequestMapping(value = "/getLsEventTop5", method = RequestMethod.POST)
    public String getLsEventTop5(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject jsonData = JSONObject.parseObject(iCodeItemsService.getItemValueByCodeID("1016179", "梁山热门办理事项TOP5"));
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "", jsonData.toString());

//            JSONObject result = JSON.parseObject(params);
//            String areaCode = result.getString("areaCode");
//            JSONObject dataJson = new JSONObject();
//            List<Record> list = newshow2Service.geteventTop5(areaCode);
//            dataJson.put("eventTop5", list);
//            //system.out.println(dataJson);
//            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     *
     *  [全市办结量趋势]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTrend", method = RequestMethod.POST)
    public String getTrend(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            JSONObject dataJson = new JSONObject();
            List<Record> list = newshow2Service.gettrend(areaCode);
            for (Record record : list) {
                String name = record.getStr("name");

                if ("370883".equals(areaCode)) {
                    if ("1月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城一月办件")));
                    } else if ("2月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城二月办件")));
                    } else if ("3月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城三月办件")));
                    } else if ("4月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城四月办件")));
                    } else if ("5月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城五月办件")));
                    } else if ("6月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城六月办件")));
                    } else if ("7月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城七月办件")));
                    } else if ("8月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城八月办件")));
                    } else if ("9月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城九月办件")));
                    } else if ("10月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城十月办件")));
                    } else if ("11月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城十一月办件")));
                    } else if ("12月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "邹城十二月办件")));
                    }
                } else if ("370832".equals(areaCode)) {
                    if ("1月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山一月办件")));
                    } else if ("2月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山二月办件")));
                    } else if ("3月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山三月办件")));
                    } else if ("4月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山四月办件")));
                    } else if ("5月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山五月办件")));
                    } else if ("6月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山六月办件")));
                    } else if ("7月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山七月办件")));
                    } else if ("8月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山八月办件")));
                    } else if ("9月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山九月办件")));
                    } else if ("10月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山十月办件")));
                    } else if ("11月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山十一月办件")));
                    } else if ("12月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "梁山十二月办件")));
                    }
                } else {
                    if ("1月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市一月办件")));
                    } else if ("2月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市二月办件")));
                    } else if ("3月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市三月办件")));
                    } else if ("4月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市四月办件")));
                    } else if ("5月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市五月办件")));
                    } else if ("6月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市六月办件")));
                    } else if ("7月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市七月办件")));
                    } else if ("8月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市八月办件")));
                    } else if ("9月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市九月办件")));
                    } else if ("10月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市十月办件")));
                    } else if ("11月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市十一月办件")));
                    } else if ("12月".equals(name)) {
                        record.set("value", record.getInt("value") + Integer.parseInt(iCodeItemsService.getItemValueByCodeID("1016273", "全市十二月办件")));
                    }
                }

            }

            dataJson.put("trend", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }


    public String getWeekBegin() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }

        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);
        return weekBegin;
    }
}
