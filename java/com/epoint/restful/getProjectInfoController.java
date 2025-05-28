package com.epoint.restful;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.service.JiNingGetProjectInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yangjia 集中生产
 * @create 2021-06-29 10:29
 */

@RestController
@RequestMapping("/taskApi")
public class getProjectInfoController {

    private Logger logger = Logger.getLogger(getProjectInfoController.class);


    @Autowired
    private IConfigService iConfigService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    
    @Autowired
    private IAttachService frameAttachInfoService;


    /**
     * 查询办理信息接口
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/getTaskKindsByThemes", method = RequestMethod.POST)
    public String getTaskKindsByThemes(@RequestBody String params) {
        try {
            logger.info("开始调用查询项目办理信息接口:getTaskKindsByThemes");
            JSONObject json = JSON.parseObject(params);
            if (StringUtil.isBlank(JsonUtils.checkUserAuth(json.getString("token")))){
            	
            	JiNingGetProjectInfoService jngetProjectInfoService = new JiNingGetProjectInfoService();
                //定义返回结果的Json
                JSONObject dataJson = new JSONObject();
                JSONObject obj = (JSONObject) json.get("params");
                String itemName = obj.getString("itemName");
                String itemCode = obj.getString("itemCode");
                String pageNo = obj.getString("pageNo");
                String pageSize = obj.getString("pageSize");
                //1.判断参数是否必填  页码 每页数量必填
                if (StringUtil.isNotBlank(pageNo) && StringUtil.isNotBlank(pageSize)){
                    //2.获取配置的系统参数
                    String businessGuid = iConfigService.getFrameConfigValue("SP_API_BUSINESSGUIDS");
                    String taskIDs = iConfigService.getFrameConfigValue("SP_API_TASKIDS");
                    StringBuilder stringBuilder = new StringBuilder();

                    //拼接传入的taskids
                    String[] taskidsArray = taskIDs.split(",");
                    for (String s : taskidsArray) {
                        if (StringUtil.isBlank(stringBuilder.toString())){
                            stringBuilder.append("'").append(s).append("'");
                        }
                        else {
                            stringBuilder.append(",").append("'").append(s).append("'");
                        }
                    }
                    JSONArray resultArray = new JSONArray();
                    List<Record> recordList = jngetProjectInfoService.getProjectInfo(businessGuid,stringBuilder.toString(),itemName,itemCode,pageNo,pageSize);
                    if (ValidateUtil.isNotBlankCollection(recordList)){
                        dataJson.put("totalNum",recordList.size());
                        //处理代码项
                        for (Record record : recordList) {
                            //项目类型
                            if (StringUtil.isNotBlank(record.getStr("ITEMTYPE"))){
                                record.put("itemtype",iCodeItemsService.getItemTextByCodeName("项目类型",record.getStr("ITEMTYPE")));
                            }

                            //建设性质
                            if (StringUtil.isNotBlank(record.getStr("constructionproperty"))){
                                record.put("constructionproperty",iCodeItemsService.getItemTextByCodeName("建设性质",record.getStr("constructionproperty")));
                            }

                            //项目投资来源
                            if (StringUtil.isNotBlank(record.getStr("xmtzly"))){
                                record.put("xmtzly",iCodeItemsService.getItemTextByCodeName("项目投资来源",record.getStr("xmtzly")));
                            }

                            //单位证照类型
                            if (StringUtil.isNotBlank(record.getStr("itemlegalcerttype"))){
                                record.put("itemlegalcerttype",iCodeItemsService.getItemTextByCodeName("单位证照类型",record.getStr("itemlegalcerttype")));
                            }

                            //所属行业
                            if (StringUtil.isNotBlank(record.getStr("belongtindustry"))){
                                record.put("belongtindustry",iCodeItemsService.getItemTextByCodeName("所属行业",record.getStr("belongtindustry")));
                            }

                            //法人性质
                            if (StringUtil.isNotBlank(record.getStr("legalproperty"))){
                                record.put("legalproperty",iCodeItemsService.getItemTextByCodeName("法人性质",record.getStr("legalproperty")));
                            }

                            //土地获取方式
                            if (StringUtil.isNotBlank(record.getStr("tdhqfs"))){
                                record.put("tdhqfs",iCodeItemsService.getItemTextByCodeName("土地获取方式",record.getStr("tdhqfs")));
                            }

                            //是否
                            if (StringUtil.isNotBlank(record.getStr("isimprovement"))){
                                record.put("isimprovement",iCodeItemsService.getItemTextByCodeName("是否",record.getStr("isimprovement")));
                            }

                            //是否
                            if (StringUtil.isNotBlank(record.getStr("tdsfdsjfa"))){
                                record.put("tdsfdsjfa",iCodeItemsService.getItemTextByCodeName("是否",record.getStr("tdsfdsjfa")));
                            }

                            //是否
                            if (StringUtil.isNotBlank(record.getStr("sfwcqypg"))){
                                record.put("sfwcqypg",iCodeItemsService.getItemTextByCodeName("是否",record.getStr("sfwcqypg")));
                            }

                            //国际行业
                            if (StringUtil.isNotBlank(record.getStr("GBHY"))){
                                record.put("gbhy",iCodeItemsService.getItemTextByCodeName("国标行业",record.getStr("GBHY")));
                            }

                            //项目资金属性
                            if (StringUtil.isNotBlank(record.getStr("xmzjsx"))){
                                record.put("xmzjsx",iCodeItemsService.getItemTextByCodeName("项目资金属性",record.getStr("xmzjsx")));
                            }

                            String lxpfcliengguid = record.getStr("lxpfcliengguid");
                            
                            List<FrameAttachInfo> lxpfattachs = frameAttachInfoService.getAttachInfoListByGuid(lxpfcliengguid);
                            
                            JSONArray lxpfArray = new JSONArray();
                            for (FrameAttachInfo attachinfo : lxpfattachs) {
                            	Record record1 = new Record();
                            	record1.set("filename", attachinfo.getAttachFileName());
                            	record1.set("downloadurl", "http://112.6.110.176:25001/jnzwdt/rest/jnattachcontent/readAttach?attachguid="+attachinfo.getAttachGuid());
                            	lxpfArray.add(record1);
                            }
                            record.set("lxpffiles", lxpfArray.toJSONString());
                            
                            
                            String jsgcfliengguid = record.getStr("jsgcfliengguid");
                            
                            List<FrameAttachInfo> jsgcattachs = frameAttachInfoService.getAttachInfoListByGuid(jsgcfliengguid);
                            
                            JSONArray jsgcArray = new JSONArray();
                            for (FrameAttachInfo attachinfo : jsgcattachs) {
                            	Record record1 = new Record();
                            	record1.set("filename", attachinfo.getAttachFileName());
                            	record1.set("downloadurl", "http://112.6.110.176:25001/jnzwdt/rest/jnattachcontent/readAttach?attachguid="+attachinfo.getAttachGuid());
                            	jsgcArray.add(record1);
                            }
                            record.set("jsgcfiles", jsgcArray.toJSONString());
                            
                        }
                        resultArray.addAll(recordList);
                        dataJson.put("list", resultArray);
                    }
                    else{
                        dataJson.put("list",recordList);
                        dataJson.put("totalNum",jngetProjectInfoService.getCountProjectInfo(businessGuid, taskIDs, itemName, itemCode));
                    }
                    logger.info("结束调用查询项目办理信息接口:getTaskKindsByThemes");
                    return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson);
                

                }
                else{
                    logger.info("调用查询项目办理信息接口出现异常:getTaskKindsByThemes 未传入必填参数pageNO和pageSize");
                    return JsonUtils.zwdtRestReturn("0", "出现异常：未传入必填参数pageNO和pageSize" , "");
                }

            }
            else{
                logger.info("调用查询项目办理信息接口出现异常:getTaskKindsByThemes 身份验证未通过");
                return JsonUtils.zwdtRestReturn("0", "出现异常：身份验证未通过" , "");
            }


        } 
        catch (Exception e) {
            logger.info(e);
            logger.info("调用查询项目办理信息接口出现异常:getTaskKindsByThemes");
            logger.info("传入参数为【" + params + "】");
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


}
