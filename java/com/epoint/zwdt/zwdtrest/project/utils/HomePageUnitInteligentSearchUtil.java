package com.epoint.zwdt.zwdtrest.project.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.search.inteligentsearch.restful.sdk.InteligentSearchRestNewSdk;
import com.epoint.search.inteligentsearch.sdk.domain.*;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomePageUnitInteligentSearchUtil {
    /**
     * 全文检索接口地址
     */
    public static final String restIpConfig = ConfigUtil.getConfigValue("inteligentSearch", "InteligentSearchUrl");
    /**
     * 事项检索分类号
     */
    public static final String task_categoryNum = ConfigUtil.getConfigValue("inteligentSearch", "task_categoryNum");
    /**
     * 办件检索分类号
     */
    public static final String project_categoryNum = ConfigUtil.getConfigValue("inteligentSearch",
            "project_categoryNum");

    public static final Integer ORDER_DESC = 0;

    public static final Integer ORDER_ASC = 1;

    private final static Logger log = LogUtil.getLog(HomePageUnitInteligentSearchUtil.class);

    /**
     * 根据关键词和条件进行办件检索
     *
     * @param re_fields      指定返回字段（以";"分隔，不传则返回所有的字段），如果此字段不传，则默认返回索引的所有字段，但无论是否指定返回字段，id是一定会返回的
     * @param condition      额外查询条件，匹配规则为and
     * @param unionCondition 额外查询条件，功能类似前面的condition参数，但是这个条件全都是or的匹配规则
     * @param times          额外时间范围条件
     * @param searchRange    根据某个字段值范围查询，没有范围查询此字段可传空
     * @param sort           排序规则，根据某个字段升序或降序排序，hashmap，key:字段，value：0：降序，1：升序，多个排序字段优先级为map前面的优先级比较高
     * @param opCondition    布尔查询条件
     * @param first          查询起始行
     * @param pageSize       一次查询返回的记录数
     * @return
     */
    public static PageData<AuditProject> getProjectInteligentPageData(String re_fields, List<SearchCondition> condition,
                                                                      List<SearchUnionCondition> unionCondition, List<SearchTimeFormat> times,
                                                                      List<searchRangeCodition> searchRange, Map<String, Object> sort, List<SearchOperatorCondition> opCondition,
                                                                      int first, int pageSize) {
        return getInteligentPageData(re_fields, condition, unionCondition, times, searchRange, sort, opCondition,
                first, pageSize, project_categoryNum, AuditProject.class);

    }

    /**
     * 根据关键词和条件进行事项检索
     *
     * @param re_fields      指定返回字段（以";"分隔，不传则返回所有的字段），如果此字段不传，则默认返回索引的所有字段，但无论是否指定返回字段，id是一定会返回的
     * @param condition      额外查询条件，匹配规则为and
     * @param unionCondition 额外查询条件，功能类似前面的condition参数，但是这个条件全都是or的匹配规则
     * @param times          额外时间范围条件
     * @param searchRange    根据某个字段值范围查询，没有范围查询此字段可传空
     * @param sort           排序规则，根据某个字段升序或降序排序，hashmap，key:字段，value：0：降序，1：升序，多个排序字段优先级为map前面的优先级比较高
     * @param opCondition    布尔查询条件
     * @param first          查询起始行
     * @param pageSize       一次查询返回的记录数
     * @return
     */
    public static PageData<AuditTask> getPassItemByInteligentPageData(String re_fields, List<SearchCondition> condition,
                                                                      List<SearchUnionCondition> unionCondition, List<SearchTimeFormat> times,
                                                                      List<searchRangeCodition> searchRange, Map<String, Object> sort, List<SearchOperatorCondition> opCondition,
                                                                      int first, int pageSize) {

        return getInteligentPageData(re_fields, condition, unionCondition, times, searchRange, sort, opCondition,
                first, pageSize, task_categoryNum, AuditTask.class);
    }

    /**
     * 根据关键词和条件进行全文检索
     *
     * @param re_fields      指定返回字段（以";"分隔，不传则返回所有的字段），如果此字段不传，则默认返回索引的所有字段，但无论是否指定返回字段，id是一定会返回的
     * @param condition      额外查询条件，匹配规则为and
     * @param unionCondition 额外查询条件，功能类似前面的condition参数，但是这个条件全都是or的匹配规则
     * @param times          额外时间范围条件
     * @param searchRange    根据某个字段值范围查询，没有范围查询此字段可传空
     * @param sort           排序规则，根据某个字段升序或降序排序，hashmap，key:字段，value：0：降序，1：升序，多个排序字段优先级为map前面的优先级比较高
     * @param opCondition    布尔查询条件
     * @param first          查询起始行
     * @param pageSize       一次查询返回的记录数
     * @param cnum           需要查询的分类号，需要查询多个分类以”;”隔开，如果不传，则默认将后台所有分类中符合条件的内容返回
     * @param clazz          结果封装类
     * @return
     */
    public static <T extends Record> PageData<T> getInteligentPageData(String re_fields,
                                                                       List<SearchCondition> condition, List<SearchUnionCondition> unionCondition, List<SearchTimeFormat> times,
                                                                       List<searchRangeCodition> searchRange, Map<String, Object> sort, List<SearchOperatorCondition> opCondition,
                                                                       int first, int pageSize, String cnum, Class<T> clazz) {

        PageData<T> pageData = new PageData<>();
        String returnStr = null;
        try {
            String token = null;
            // 查询起始行，这个参数不是页码
            String pn = String.valueOf(first);
            // 一次查询返回的记录数
            String rn = String.valueOf(pageSize);
            // 查询开始时间，默认字段infodate，格式为yyyy-MM-dd HH:mm:ss
            String sdt = null;
            // 查询结束时间，默认字段infodate，格式为yyyy-MM-dd HH:mm:ss
            String edt = null;
            // 全文检索字段范围，例如要搜索标题，这里就传title，要搜索内容就传content，标题内容都要搜索，就传title;content
            String fields = null;
            // 查询关键词
            String wd = null;
            // 包含任意关键词
            String inc_wd = null;
            // 不包含关键词
            String exc_wd = null;
            // 排序规则，根据某个字段升序或降序排序，hashmap，key:字段，value：0：降序，1：升序，多个排序字段优先级为map前面的优先级比较高
            if (sort == null) {
                sort = new LinkedHashMap<>();
            }
            if (sort.isEmpty()) {
                // 默认按infodate倒序
                sort.put("infodate", ORDER_DESC);
            }
            // 按照某个字段的匹配度排序，比如标题匹配度比较高，但是结果并不在最前面，这里可以将title传入，标题匹配度最高的在前
            String ssort = null;
            // 返回内容长度，如果传0则返回内容不截取，全部返回
            int cl = 0;
            // 终端类别，0:pc,1:移动端,2:其他
            String terminal = "0";
            // 需要高亮显示的字段（飘红），如果此字段不传，则没有高亮显示
            String highLightFields = null;
            // （不建议使用，传空即可）统计字段，不传默认按照后台配置的索引分类进行统计，如果要自定义统计字段，则这个字段必须是没有分词的，否则统计效果不理想
            String statistics = null;
            // 精确度查询，查询内容精确度（0~100整数)，此参数默认为空，关键词匹配方式为and，如果传入1~100的数字，则匹配方式为or
            int accuracy = 100;
            // 查询关键词不要分词（设置为1则启用，否则不启用），全词匹配
            String noParticiple = null;
            // 查询时没有关键词，仅靠条件就能获取数据，多数业务系统需要用到，此功能是整合了getFullTextDataForBusiness方法，一般设置为false，如果需要仅通过条件就能查询的场景，请设置为true
            boolean noWd = true;
            // 是否开启热度排序，此参数不要随意开启，确认业务场景需要热度排序是在设置为true，否则设为false，此功能需要配合inteligentsearch.properties中的heatSort参数使用
            boolean isUseClickSort = false;
            // 排除字段的截取，例如要排除标题的长度截取，这里就传title，要排除内容就传content，标题内容都要排除，就传title;content
            String cutIngore = null;
            // 经纬度空间查询（支持圆形区域、矩形区域、多边形区域）圆形区域提供圆心和半径，矩形区域需要提供矩形左上角经纬度以及后下角经纬度，多边形区域提供多个点的经纬度。支持根据某个点进行距离排序。
            List<GeoCondition> geoCondition = null;
            // 实现权重的自定义排序，例如数据中包含“城北派出所”和“城南派出所”的数据，通过设置权重比，可以优先展示“城北派出所”的数据。
            List<SearchBoostCondition> boostCondition = null;
            // 如果布尔查询为(a and b) or (c and d)则设置为1，若布尔查询为(a or b) and ( c or
            // d)则设置为0
            // 布尔查询默认0
            String opType = "0";
            // 指定全文检索的搜索引擎版本，如果是es版本则设置为1；如果是solr版本则设置为0
            String searchEngine = "1";
            // 用于接口的权限判断以及操作权限，若开启了权限认证，则需传入相应的部门guid，此功能需要配合inteligentsearch.properties中的isOpenPermission参数使用
            String ouGuid = null;
            returnStr = new InteligentSearchRestNewSdk().getFullTextDataNew(restIpConfig, token, pn, rn, sdt, edt, wd,
                    inc_wd, exc_wd, fields, re_fields, cnum, sort, ssort, cl, terminal, condition, times, searchRange,
                    highLightFields, statistics, unionCondition, accuracy, noParticiple, noWd, isUseClickSort,
                    cutIngore, geoCondition, boostCondition, opCondition, opType, searchEngine, ouGuid);
            if (log.isDebugEnabled()) {
                log.info("【全文检索】getFullTextDataNew查询索引。服务器响应值:{}" + returnStr);
            }
            JSONObject obj = JSON.parseObject(returnStr);
            if (obj.containsKey("result")) {
                JSONObject result = obj.getJSONObject("result");
                if (!result.containsKey("error")) {
                    if (result.containsKey("totalcount")) {
                        pageData.setRowCount(result.getIntValue("totalcount"));
                    }
                    if (result.containsKey("records")) {
                        pageData.setList(JsonUtil.jsonToList(result.getString("records"), clazz));
                    }
                } else {
                    log.info("【全文检索】getFullTextDataNew查询索引。服务器响应异常结果：{}" + returnStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【全文检索】getFullTextDataNew查询索引。服务器响应值解析异常：{}" + returnStr);
        }
        return pageData;

    }

}
