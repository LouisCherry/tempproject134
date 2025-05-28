package com.epoint.znhdt.rest;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.StringUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.znhdt.service.IZnhdtService;

@RestController
@RequestMapping({"/znhdtApi" })
public class ZnhdtController
{
    @Autowired
    private IZnhdtService iZnhdtService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 办件数据统计：日均办件量、月均办件量、本年办件量、累计办件量
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getProjectCount", method = RequestMethod.POST)
    public String getProjectCount(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            Record result = iZnhdtService.getProjectCount(areacode);
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 一件事办件情况,热门一件事排名，展示一件事名称和数量TOP5数据
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getBusinessCountList", method = RequestMethod.POST)
    public String getBusinessCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String limitInt = obj.getString("limitInt");
            if (StringUtil.isBlank(limitInt)) {
                limitInt = "5";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            List<Record> result = iZnhdtService.getBusinessCountList(areacode, Integer.valueOf(limitInt));
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 大屏中间放置梁山geojson地图
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getTownMapProjectCountList", method = RequestMethod.POST)
    public String getTownMapProjectCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            List<Record> result = iZnhdtService.getTownMapProjectCountList(areacode);
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 本月窗口办件排行，展示字段为排名、窗口、申报量、办结量、办结率、平均耗时【（办结时间-受理时间）/办件量总数】
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getNowMonthWinProjectCountList", method = RequestMethod.POST)
    public String getNowMonthWinProjectCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String limitInt = obj.getString("limitInt");
            if (StringUtil.isBlank(limitInt)) {
                limitInt = "5";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            DecimalFormat towdf = new DecimalFormat("0.00");
            DecimalFormat df = new DecimalFormat("0");

            List<Record> result = iZnhdtService.getNowMonthWinProjectCountList(areacode, Integer.valueOf(limitInt));
            for (Record rcd : result) {
                int slnum = rcd.getInt("slnum");
                int bjnum = rcd.getInt("bjnum");
                int sldate = rcd.getInt("sldate");// 秒

                rcd.put("bjlv", slnum > 0 ? towdf.format(bjnum * 100 / slnum) + "%" : "0%");
                // 分钟，各窗口所有已受理的办件（办结时间-受理时间）总和/受理件总数
                rcd.put("pjhs", slnum > 0 ? df.format(sldate / 60 / slnum) : 0);

            }
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 一件事办件进度追踪管理
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getBusinessDataList", method = RequestMethod.POST)
    public String getBusinessDataList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String first = obj.getString("pagenumber");
            if (StringUtil.isBlank(first)) {
                first = "0";
            }
            String pagesize = obj.getString("pagesize");
            if (StringUtil.isBlank(pagesize)) {
                pagesize = "5";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            PageData<Record> result = iZnhdtService.getBusinessDataList(areacode, Integer.valueOf(first),
                    Integer.valueOf(pagesize));
            List<Record> list = result.getList();
            for (Record rcd : list) {
                String yjsstatus = rcd.getStr("yjsstatus");
                if (StringUtil.isNotBlank(yjsstatus)) {
                    yjsstatus = iCodeItemsService.getItemTextByCodeName("一件事办件状态", yjsstatus);
                }
                rcd.put("yjsstatus", yjsstatus);

                rcd.put("acceptuserdate", EpointDateUtil.convertDate2String(rcd.getDate("acceptuserdate"),
                        EpointDateUtil.DATE_TIME_FORMAT));

                rcd.put("banjiedate",
                        EpointDateUtil.convertDate2String(rcd.getDate("banjiedate"), EpointDateUtil.DATE_TIME_FORMAT));
            }
            dataJson.put("data", list);
            dataJson.put("totalcount", result.getRowCount());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 项目总额、项目总数、重点项目个数
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getXmCount", method = RequestMethod.POST)
    public String getXmCount(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            Record result = iZnhdtService.getXmCount(areacode);
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getXmlxCountList", method = RequestMethod.POST)
    public String getXmlxCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String limitInt = obj.getString("limitInt");
            if (StringUtil.isBlank(limitInt)) {
                limitInt = "5";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            List<Record> result = iZnhdtService.getXmlxCountList(areacode, Integer.valueOf(limitInt));
            for (Record rcd : result) {
                String itemtype = rcd.getStr("itemtype");
                if (StringUtil.isNotBlank(itemtype)) {
                    itemtype = iCodeItemsService.getItemTextByCodeName("审批流程类型", itemtype);
                }
                rcd.put("itemtype", itemtype);
            }
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 工程行业分类占比
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getXmGchyflCountList", method = RequestMethod.POST)
    public String getXmGchyflCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String limitInt = obj.getString("limitInt");
            if (StringUtil.isBlank(limitInt)) {
                limitInt = "5";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            List<Record> result = iZnhdtService.getXmGchyflCountList(areacode, Integer.valueOf(limitInt));
            for (Record rcd : result) {
                String gchyfl = rcd.getStr("gchyfl");
                if (StringUtil.isNotBlank(gchyfl)) {
                    gchyfl = iCodeItemsService.getItemTextByCodeName("工程分类", gchyfl);
                }
                rcd.put("gchyfl", gchyfl);
            }
            dataJson.put("data", result);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 立项类型
     * [一句话功能简述]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getXmLxlxCountList", method = RequestMethod.POST)
    public String getXmLxlxCountList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            List<Record> result = iZnhdtService.getXmLxlxCountList(areacode);
            Integer xmzs = 0;
            for (Record rcd : result) {
                xmzs += rcd.getInt("totalcount");
                String lxlx = rcd.getStr("lxlx");
                if (StringUtil.isNotBlank(lxlx)) {
                    lxlx = iCodeItemsService.getItemTextByCodeName("国标_立项类型", lxlx);
                }
                else {
                    // 为空默认其他
                    lxlx = "其他";
                }
                rcd.put("lxlx", lxlx);
            }
            dataJson.put("data", result);
            dataJson.put("xmzs", xmzs);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getXmTjDataList", method = RequestMethod.POST)
    public String getXmTjDataList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);

            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String first = obj.getString("pagenumber");
            if (StringUtil.isBlank(first)) {
                first = "0";
            }
            String pagesize = obj.getString("pagesize");
            if (StringUtil.isBlank(pagesize)) {
                pagesize = "10";
            }

            if (StringUtil.isBlank(areacode)) {
                areacode = "370832";
            }

            PageData<Record> result = iZnhdtService.getXmTjDataList(areacode, Integer.valueOf(first), Integer.valueOf(pagesize));
            List<Record> list = result.getList();
            for (Record rcd : list) {
                String yjsstatus = rcd.getStr("yjsstatus");
                if (StringUtil.isNotBlank(yjsstatus)) {
                    yjsstatus = iCodeItemsService.getItemTextByCodeName("一件事办件状态", yjsstatus);
                }
                rcd.put("yjsstatus", yjsstatus);

                rcd.put("acceptuserdate", EpointDateUtil.convertDate2String(rcd.getDate("acceptuserdate"), EpointDateUtil.DATE_TIME_FORMAT));

                rcd.put("banjiedate", EpointDateUtil.convertDate2String(rcd.getDate("banjiedate"), EpointDateUtil.DATE_TIME_FORMAT));
            }
            dataJson.put("data", list);
            dataJson.put("totalcount", result.getRowCount());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
