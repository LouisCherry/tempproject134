package com.epoint.zwdt.zwdtrest.newindex;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

/**
 * 
 *  用户信息设置相关接口
 *  
 * @作者 WST
 * @version [F9.3, 2018年1月16日]
 */
@RestController
@RequestMapping("/jnnewindex")
public class JnNewIndexController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 代码项API
     */
    @Autowired
    private IAuditProject iAuditProject;
    
    @Autowired
    private ICodeItemsService iCodeItemsService;
    
    @Autowired
    private IAuditTask iAuditTask;

 


    /**
     * 获取办件数量接口
     * 
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/getprojectstatistics", method = RequestMethod.POST)
    public String getProjectStatistics(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getprojectstatistics接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
            	 JSONObject dataJson = new JSONObject();
            	 SqlConditionUtil sql = new SqlConditionUtil();
            	 sql.gt("status", "90");
            	 sql.eq("substring(BANJIEDATE,1,10)", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd"));
                 dataJson.put("dayconclusion", iAuditProject.getAuditProjectCountByCondition(sql.getMap()).getResult());
                 sql.clear();
                 sql.gt("status", "90");
                 sql.gt("BANJIEDATE", "'"+EpointDateUtil.convertDate2String(EpointDateUtil.getMondayOfWeek(), "yyyy-MM-dd 00:00:00")+"'");
                 dataJson.put("weekconclusion", iAuditProject.getAuditProjectCountByCondition(sql.getMap()).getResult());
                 sql.clear();
                 sql.gt("status", "90");
                 Calendar calendar=Calendar.getInstance();
                 calendar.set(Calendar.DAY_OF_MONTH, 1);
                 sql.gt("BANJIEDATE", "'"+EpointDateUtil.convertDate2String(calendar.getTime(),"yyyy-MM-dd 00:00:00")+"'");
                 dataJson.put("monthconclusion", iAuditProject.getAuditProjectCountByCondition(sql.getMap()).getResult());
                 sql.clear();
                 sql.gt("status", "90");
                 Calendar calendar1=Calendar.getInstance();
                 calendar1.set(Calendar.DAY_OF_YEAR, 1);
                 sql.gt("BANJIEDATE", "'"+EpointDateUtil.convertDate2String(calendar1.getTime(),"yyyy-MM-dd 00:00:00")+"'");
                 dataJson.put("yearconclusion", iAuditProject.getAuditProjectCountByCondition(sql.getMap()).getResult());
                 return JsonUtils.zwdtRestReturn("1", "获取办件数量成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取办件数量异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 获取证明名称列表接口
     * 
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/getProof", method = RequestMethod.POST)
    public String getProof(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProof接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                List<CodeItems> list = iCodeItemsService.listCodeItemsByCodeName("证明开具名称");
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                return JsonUtils.zwdtRestReturn("1", "获取证明名称列表", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取证明名称列表异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 获取网厅主题事项列表
     * 
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/getService", method = RequestMethod.POST)
    public String getService(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getService接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject dataJson = new JSONObject();
                JSONArray grArray = new JSONArray();
                JSONArray frArray = new JSONArray();
                List<CodeItems> list = iCodeItemsService.listCodeItemsByCodeName("网厅主题");
                List<CodeItems> grList = new ArrayList<>();
                List<CodeItems> frList = new ArrayList<>();
                for (CodeItems codeItem : list) {
                    if(StringUtil.isNotBlank(codeItem.getItemValue())&&codeItem.getItemValue().length()==4&&"01".equals(codeItem.getItemValue().substring(0, 2))) {
                        if(grList.size()>=6) {
                            continue;
                        }
                        grList.add(codeItem);
                    }else if(StringUtil.isNotBlank(codeItem.getItemValue())&&codeItem.getItemValue().length()==4&&"02".equals(codeItem.getItemValue().substring(0, 2))){
                        if(frList.size()>=6) {
                            continue;
                        }
                        frList.add(codeItem);
                    }
                }
                int index = 0;
                if(ValidateUtil.isNotBlankCollection(grList)) {
                    for (CodeItems codeItem : grList) {
                        
                        JSONObject dataJson1 = new JSONObject();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("is_history", "0");
                        sql.eq("IS_EDITAFTERIMPORT", "1");
                        sql.eq("IS_ENABLE", "1");
                        sql.eq("wtzt", codeItem.getItemValue());
                        sql.setSelectFields("taskname,rowguid,task_id");
                        sql.setSelectCounts(3);
                        sql.setOrderDesc("operatedate");
                        List<AuditTask> result = iAuditTask.getAllTask(sql.getMap()).getResult();
                        String imgUrl = "";
                        if(index==0) {
                            imgUrl = "./images/service_icon1.png";
                        }else if(index==1) {
                            imgUrl = "./images/service_icon2.png";
                        }else if(index==2) {
                            imgUrl = "./images/service_icon3.png";
                        }else if(index==3) {
                            imgUrl = "./images/service_icon4.png";
                        }else if(index==4) {
                            imgUrl = "./images/service_icon5.png";
                        }else if(index==5) {
                            imgUrl = "./images/service_icon6.png";
                        }
                        index++;
                        dataJson1.put("imgUrl", imgUrl);
                        dataJson1.put("name", codeItem.getItemText());
                        dataJson1.put("list", result);
                        grArray.add(dataJson1);
                    }
                }
                index = 0;
                if(ValidateUtil.isNotBlankCollection(frList)) {
                    for (CodeItems codeItem : frList) {
                        JSONObject dataJson1 = new JSONObject();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.setSelectFields("taskname,rowguid,task_id");
                        sql.eq("is_history", "0");
                        sql.eq("IS_EDITAFTERIMPORT", "1");
                        sql.eq("IS_ENABLE", "1");
                        sql.eq("wtzt", codeItem.getItemValue());
                        sql.setSelectCounts(3);
                        sql.setOrderDesc("operatedate");
                        List<AuditTask> result = iAuditTask.getAllTask(sql.getMap()).getResult();
//                        for (AuditTask auditTask : result) {
//                            AuditTaskExtension auditTaskExtension = iAuditTaskExtension
//                                    .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
//                            auditTask.set("usescope", auditTaskExtension.get("usescope"));
//                        }
                        String imgUrl = "";
                        if(index==0) {
                            imgUrl = "./images/service_icon1.png";
                        }else if(index==1) {
                            imgUrl = "./images/service_icon2.png";
                        }else if(index==2) {
                            imgUrl = "./images/service_icon3.png";
                        }else if(index==3) {
                            imgUrl = "./images/service_icon4.png";
                        }else if(index==4) {
                            imgUrl = "./images/service_icon5.png";
                        }else if(index==5) {
                            imgUrl = "./images/service_icon6.png";
                        }
                        index++;
                        dataJson1.put("imgUrl", imgUrl);
                        dataJson1.put("name", codeItem.getItemText());
                        dataJson1.put("list", result);
                        frArray.add(dataJson1);
                    }
                }
                dataJson.put("individual", grArray);
                dataJson.put("legal", frArray);
                return JsonUtils.zwdtRestReturn("1", "获取证明名称列表", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取证明名称列表异常：" + e.getMessage(), "");
        }
    }

}
