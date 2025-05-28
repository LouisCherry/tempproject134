package com.epoint.jnzwfw.auditselfservice.auditselfservicerest.common;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 套餐相关接口
 * 
 * @作者 xli
 * @version [F9.3, 2017年10月28日]
 */
@RestController
@RequestMapping("/jnznsbBusiness")
public class JnAuditZnsbBusinessController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;

    /**
     * 获取套餐列表接口(套餐列表页面调用)
     * 
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessList", method = RequestMethod.POST)
    public String getBusinessList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 1、接口的入参转化为JSON对象
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取辖区编码
            String areaCode = obj.getString("areacode");
            // 1.2、获取套餐类型
            String businessType = obj.getString("businesstype");
            // 1.3、获取当前页
            String currentPage = obj.getString("currentpage");
            // 1.4、获取分页数量
            String pageSize = obj.getString("pagesize");
            // 1.5、获取搜索条件
            String businessname = obj.getString("businessname");
            // 2、查询辖区下启用的主题
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("BUSINESSTYPE", "2"); // 主题分类：一般并联审批（套餐）
            sqlConditionUtil.eq("DEL", "0"); // 主题状态：启用
            sqlConditionUtil.eq("AREACODE", areaCode); // 辖区编码
            sqlConditionUtil.setOrderDesc("ORDERNUMBER"); // 按排序字段降序
            sqlConditionUtil.eq("isshowytj", "1"); // 是否一体机展示
            if (StringUtil.isNotBlank(businessType)) {
                sqlConditionUtil.eq("BUSINESSKIND", businessType); // 辖区编码
            }
            if (StringUtil.isNotBlank(businessname)) {
                sqlConditionUtil.like("businessname", businessname);
            }
            int count = 0;
            List<AuditSpBusiness> auditSpBusinesses = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap())
                    .getResult();
            // 3、定义返回的主题数据
            List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
            for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                List<AuditSpTask> auditSpTaskList = iAuditSpTask
                        .getAllAuditSpTaskByBusinessGuid(auditSpBusiness.getRowguid()).getResult();
                if (!auditSpTaskList.isEmpty()) {
                    JSONObject bussinessJson = new JSONObject();
                    bussinessJson.put("businessguid", auditSpBusiness.getRowguid()); // 主题标识
                    bussinessJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                    bussinessJson.put("note", auditSpBusiness.getNote()); // 主题说明
                    bussinessJson.put("iswssb", auditSpBusiness.getIswssb());// 是否允许网上申报
                    // 套餐背景图
                    bussinessJson.put("imgclass", auditSpBusiness.getStr("backgroundimg"));// 图标
                    bussinessJson.put("formid", auditSpBusiness.getStr("yjsformid"));// 图标
                    businessJsonList.add(bussinessJson);
                }
            }
            // 6、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            // dataJson.put("totalcount", count);
            // 截取对应页面的部门list数据
            int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
            int endint = (firstint + Integer.parseInt(pageSize)) >= businessJsonList.size() ? businessJsonList.size()
                    : (firstint + Integer.parseInt(pageSize));
            List<JSONObject> rtnlist = businessJsonList.subList(firstint, endint);

            dataJson.put("totalcount", businessJsonList.size());
            dataJson.put("businessList", rtnlist);
            log.info("=======结束调用getBusinessList接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取套餐式申报列表成功", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取套餐式申报列表失败：" + e.getMessage(), "");
        }
    }
}
