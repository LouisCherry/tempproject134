package com.epoint.jnrestforevaluat.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jnrestforevaluat.api.IJnEvaluatService;

@RestController
@RequestMapping("/jnevaluat")
public class JnEvaluatRest
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IJnEvaluatService service;

    @Autowired
    private IAuditOnlineEvaluat evaluateservice;

    /**
     * 
     *  获取用户信息接口
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                String ouguid = jsonparams.getString("ouguid");
                String areacode = jsonparams.getString("areacode");
                String userguid = jsonparams.getString("userguid");
                Integer pagesize = jsonparams.getInteger("pagesize");
                Integer index = jsonparams.getInteger("index");
                PageData<FrameUser> userlist = service.getUserinfo(ouguid, areacode, userguid, pagesize, index);
                JSONObject dataJson = new JSONObject();
                dataJson.put("data", userlist);
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
            }
        }
        catch (Exception e) {
            log.info("【济宁用户信息结束调用getUserInfo异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }

    /**
     * 
     *  获取部门信息接口
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getOuInfo", method = RequestMethod.POST)
    public String getOuInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                String ouguid = jsonparams.getString("ouguid");
                String areacode = jsonparams.getString("areacode");
                List<FrameOu> oulist = service.getOuinfo(ouguid, areacode);
                JSONObject dataJson = new JSONObject();
                dataJson.put("data", oulist);
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
            }
        }
        catch (Exception e) {
            log.info("【济宁评价接口部门信息结束调用getOuInfo异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }

    /**
     * 
     *  获取业务信息接口
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getProjectInfo", method = RequestMethod.POST)
    public String getProjectInfo(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                String ouguid = jsonparams.getString("ouguid");
                String areacode = jsonparams.getString("areacode");
                String startdate = jsonparams.getString("startdate");
                String enddate = jsonparams.getString("enddate");
                String flowsn = jsonparams.getString("flowsn");
                Integer pagesize = jsonparams.getInteger("pagesize");
                Integer index = jsonparams.getInteger("index");
                PageData<AuditProject> projectlist = service.getProjectinfo(ouguid, areacode, startdate, enddate,
                        flowsn, pagesize, index);
                JSONObject dataJson = new JSONObject();
                dataJson.put("data", projectlist);
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
            }
        }
        catch (Exception e) {
            log.info("【济宁评价办件信息结束调用getProjectInfo异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }

    /**
     * 
     *  获取业务信息接口
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/evaluat", method = RequestMethod.POST)
    public String evaluat(@RequestBody String params) {
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                String projectguid = jsonparams.getString("projectguid");
                String satisfied = jsonparams.getString("satisfied");// 满意度
                String Evaluatecontent = jsonparams.getString("evaluatecontent");// 评价内容
                // 先判断是否已评价
                if(service.isExistProject(projectguid)>0){
                    if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {
                        AuditOnlineEvaluat evaluat = new AuditOnlineEvaluat();
                        evaluat.setRowguid(UUID.randomUUID().toString());
                        evaluat.setEvaluatetype(ZwfwConstant.Evaluate_type_WWSB);
                        evaluat.setClientidentifier(projectguid);
                        evaluat.setClienttype(ZwfwConstant.Evaluate_clienttype_Project);
                        evaluat.setEvaluatedate(new Date());
                        evaluat.setSatisfied(satisfied);
                        evaluat.setEvaluatecontent(Evaluatecontent);
                        evaluateservice.addAuditOnineEvaluat(evaluat);
                    }
                    else {
                        AuditOnlineEvaluat evaluat = evaluateservice.selectEvaluatByClientIdentifier(projectguid)
                                .getResult();
                        if (evaluat != null) {
                            evaluat.setEvaluatetype(ZwfwConstant.Evaluate_type_WWSB);
                            evaluat.setClientidentifier(projectguid);
                            evaluat.setClienttype(ZwfwConstant.Evaluate_clienttype_Project);
                            evaluat.setEvaluatedate(new Date());
                            evaluat.setSatisfied(satisfied);
                            evaluat.setEvaluatecontent(Evaluatecontent);
                            evaluateservice.updateAuditOnineEvaluat(evaluat);
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
                }else{
                    return JsonUtils.zwdtRestReturn("0", "办件不存在："+projectguid, "");
                }
            }
        }
        catch (Exception e) {
            log.info("【济宁评价信息结束调用evaluat异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params + "rtnjson：" + rtnjson.toJSONString());
    }
}
