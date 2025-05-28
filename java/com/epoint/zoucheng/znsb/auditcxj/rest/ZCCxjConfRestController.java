package com.epoint.zoucheng.znsb.auditcxj.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbydpconf.domain.AuditZnsbYdpconf;
import com.epoint.basic.auditqueue.auditznsbydpconf.inter.IAuditZnsbYdpconfService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

/**
 * [智能化引导屏配置页]
 * 
 * @author 王杰
 * @version [版本号, 2018年8月10日]
 */
@RestController
@RequestMapping("/zccxjconf")
public class ZCCxjConfRestController
{
    @Autowired
    private IAuditZnsbYdpconfService auditZnsbYdpconfService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @RequestMapping(value = "/getarticletype", method = RequestMethod.POST)
    public String getarticletype(@RequestBody String params) {
        try {
            JSONObject jsondata = new JSONObject();
            List<CodeItems> codeItemslist = codeItemsService.listCodeItemsByCodeName("诵经典文章类型");
            List<JSONObject> list = new ArrayList<>();
            if (codeItemslist != null && !codeItemslist.isEmpty()) {
                for (CodeItems codeItems : codeItemslist) {
                    JSONObject codejson = new JSONObject();
                    codejson.put("text", codeItems.getItemText());
                    codejson.put("code", codeItems.getItemValue());
                    list.add(codejson);
                }
            }
            jsondata.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 
     * [获取新闻配置]
     * 
     * @param params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getcenternews", method = RequestMethod.POST)
    public String getcenternews(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));// token验证
            JSONObject obj = (JSONObject) json.get("params");
            String type = obj.getString("type");
            String articletype = obj.getString("articletype");
            String searchname = obj.getString("searchname");
            String page = obj.getString("page");// 当前页数
            String pagesize = obj.getString("pagesize");// 每页数
            String centerguid = obj.getString("centerguid");// 中心guid
            int pageindex = 0;
            if (StringUtil.isNotBlank(page)) {
                pageindex = Integer.parseInt(page);
            }
            int firstresult = pageindex * Integer.parseInt(pagesize);
            JSONObject jsondata = new JSONObject();
            if (StringUtil.isNotBlank(type)) {
                if (QueueConstant.YdpContentType.新闻动态.value.equals(type) || "4".equals(type)) {
                    SqlConditionUtil sql = new SqlConditionUtil();// 获取新闻列表和内容
                    sql.eq("type", type);
                    if (StringUtil.isNotBlank(articletype)) {
                        sql.eq("articletype", articletype);
                    }
                    if (StringUtil.isNotBlank(searchname)) {
                        sql.like("newstitle", searchname);
                    }
                    sql.eq("isenable", "1");
                    sql.eq("centerguid", centerguid);
                    PageData<AuditZnsbYdpconf> pageData = auditZnsbYdpconfService
                            .getFileByPage(sql.getMap(), firstresult, Integer.parseInt(pagesize), "OperateDate", "desc")
                            .getResult();
                    List<AuditZnsbYdpconf> auditZnsbYdpconflist = pageData.getList();
                    List<Record> recordlist = new ArrayList<>();
                    int total = pageData.getRowCount();
                    if (auditZnsbYdpconflist != null && !auditZnsbYdpconflist.isEmpty()) {
                        List<CodeItems> codeItemslist = codeItemsService.listCodeItemsByCodeName("诵经典文章类型");
                        for (AuditZnsbYdpconf ydpconf : auditZnsbYdpconflist) {
                            Record record = new Record();
                            record.put("newscontent", ydpconf.getConfigure());
                            record.put("newstime",
                                    EpointDateUtil.convertDate2String(ydpconf.getOperatedate(), "yyyy-MM-dd HH:mm:ss"));
                            record.put("newstitle", ydpconf.getNewsTitle());
                            record.put("newsguid", ydpconf.getRowguid());
                            if (codeItemslist != null && !codeItemslist.isEmpty()) {
                                for (CodeItems codeItems : codeItemslist) {
                                    if (ydpconf.getStr("articletype").equals(codeItems.getItemValue())) {
                                        record.put("newstype", codeItems.getItemText());
                                        break;
                                    }
                                }
                            }
                            recordlist.add(record);
                        }
                    }
                    jsondata.put("content", recordlist);
                    jsondata.put("totalcount", total);
                }

            }
            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
