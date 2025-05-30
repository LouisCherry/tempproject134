package com.epoint.zndbtask.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zndbtask.api.IWZndbTaskService;
import com.epoint.zndbtask.api.entity.WZndbTask;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/zndbtaskrestcontroller")
public class ZndbTaskRestController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IWZndbTaskService service;

    /**
     *
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getProcessData", method = RequestMethod.POST)
    public String getProcessData(@RequestBody String params) {
        try {
            log.info("事项流程图getProcessDatas========"+params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String jsxz = obj.getString("jsxz");
            String tdqdfs = obj.getString("tdqdfs");
            String nzhnyxf = obj.getString("nzhnyxf");
            String zjzyly = obj.getString("zjzyly");
            String xmlx = obj.getString("xmlx");
            String ggfwjr = obj.getString("ggfwjr");
            String ggzyzy = obj.getString("ggzyzy");
            String tdbhquyx = obj.getString("tdbhquyx");
            String tsxfgc = obj.getString("tsxfgc");
            String str="";
            if (StringUtil.isNotBlank(jsxz)){
                str +=jsxz+",";
//                str +=jsxz+",f13,";
            }
            if (StringUtil.isNotBlank(tdqdfs)){
                str +=tdqdfs+",";
            }
            if (StringUtil.isNotBlank(nzhnyxf)){
                str +=nzhnyxf+",";
            }
            if (StringUtil.isNotBlank(zjzyly)){
                str +=zjzyly+",";
            }
            if (StringUtil.isNotBlank(xmlx)){
                str +=xmlx+",";
            }
            if (StringUtil.isNotBlank(ggfwjr)){
                str +=ggfwjr+",";
            }
            if (StringUtil.isNotBlank(ggzyzy)){
                str +=ggzyzy+",";
            }
            if (StringUtil.isNotBlank(tdbhquyx)){
                str +=tdbhquyx+",";
            }
            if (StringUtil.isNotBlank(tsxfgc)){
                str +=tsxfgc+",";
            }
            String[] arr = str.split(",");

            String token = jsonObject.getString("token");
            JSONObject result = new JSONObject();

            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject mainevent = new JSONObject();
                JSONObject task ;
                //获取列表
                List<WZndbTask> mainlist = service.findMainList();

                List<JSONObject> planlist=new ArrayList<>();
                List<JSONObject> constructionlist=new ArrayList<>();
                List<JSONObject> buildlist=new ArrayList<>();
                List<JSONObject> acceptlist=new ArrayList<>();

                List<JSONObject> paralleleventlist=new ArrayList<>();

                if (mainlist!=null){
                    for (WZndbTask zndbTask : mainlist) {
                        task = new JSONObject();
                        task.put("guid",zndbTask.getRowguid());
                        task.put("name",zndbTask.getName());
                        task.put("deptname",zndbTask.getDeptname());
                        task.put("type",zndbTask.getType());
                        task.put("finshtime",zndbTask.getFinshtime());
                        task.put("result",zndbTask.getResult());
                        task.put("guide",zndbTask.getGuide());
                        task.put("index",zndbTask.getStr("sxindex"));

                        //判断事项阶段
                        if ("1".equals(zndbTask.getPhase())){
                            //判断事项是否显示
                            if (checkshow(zndbTask,arr)){
                                if ("0".equals(zndbTask.getTasktype())){
                                    planlist.add(task);
                                }else {
                                    paralleleventlist.add(task);
                                }

                            }

                        }else if ("2".equals(zndbTask.getPhase())){
                            if (checkshow(zndbTask,arr)){
                                if ("0".equals(zndbTask.getTasktype())){
                                    constructionlist.add(task);
                                }else {
                                    paralleleventlist.add(task);
                                }

                            }

                        }else if ("3".equals(zndbTask.getPhase())){
                            if (checkshow(zndbTask,arr)){
                                if ("0".equals(zndbTask.getTasktype())){
                                    buildlist.add(task);
                                }else {
                                    paralleleventlist.add(task);
                                }

                            }
                        }else if ("4".equals(zndbTask.getPhase())){
                            if (checkshow(zndbTask,arr)){
                                if ("0".equals(zndbTask.getTasktype())){
                                    acceptlist.add(task);
                                }else {
                                    paralleleventlist.add(task);
                                }
                            }


                        }else {
                            if ("1".equals(zndbTask.getTasktype())){
                                paralleleventlist.add(task);
                            }
                        }
                    }
                }
                mainevent.put("plan",planlist);
                mainevent.put("construction",constructionlist);
                mainevent.put("build",buildlist);
                mainevent.put("accept",acceptlist);
                result.put("mainevent",mainevent);
                result.put("parallelevent",paralleleventlist);
                log.info("getProcessData="+result.toJSONString());
                return JsonUtils.zwdtRestReturn("1", "获取成功", result.toJSONString());
            }
            return JsonUtils.zwdtRestReturn("0", "token验证失败", result.toJSONString());
        } catch (Exception e) {
            log.debug("=======异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败" + e.getMessage(), "");
        }
    }


    /**
     *
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getListData", method = RequestMethod.POST)
    public String getListData(@RequestBody String params) {
        try {
            log.info("getListData========"+params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String jsxz = obj.getString("jsxz");
            String tdqdfs = obj.getString("tdqdfs");
            String nzhnyxf = obj.getString("nzhnyxf");
            String zjzyly = obj.getString("zjzyly");
            String xmlx = obj.getString("xmlx");
            String ggfwjr = obj.getString("ggfwjr");
            String ggzyzy = obj.getString("ggzyzy");
            String tdbhquyx = obj.getString("tdbhquyx");
            String tsxfgc = obj.getString("tsxfgc");
            String str="";
            if (StringUtil.isNotBlank(jsxz)){
                str +=jsxz+",";
            }
            if (StringUtil.isNotBlank(tdqdfs)){
                str +=tdqdfs+",";
            }
            if (StringUtil.isNotBlank(nzhnyxf)){
                str +=nzhnyxf+",";
            }
            if (StringUtil.isNotBlank(zjzyly)){
                str +=zjzyly+",";
            }
            if (StringUtil.isNotBlank(xmlx)){
                str +=xmlx+",";
            }
            if (StringUtil.isNotBlank(ggfwjr)){
                str +=ggfwjr+",";
            }
            if (StringUtil.isNotBlank(ggzyzy)){
                str +=ggzyzy+",";
            }
            if (StringUtil.isNotBlank(tdbhquyx)){
                str +=tdbhquyx+",";
            }
            if (StringUtil.isNotBlank(tsxfgc)){
                str +=tsxfgc+",";
            }
            String[] arr = str.split(",");

            String token = jsonObject.getString("token");
            JSONObject result = new JSONObject();

            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject mainevent = new JSONObject();
                JSONObject task ;
                List<WZndbTask> zndbTasklist = service.findMainList();

                List<JSONObject> mainlist=new ArrayList<>();
                List<JSONObject> paralleleventlist=new ArrayList<>();

                if (zndbTasklist!=null){
                    for (WZndbTask zndbTask : zndbTasklist) {
                        task = new JSONObject();
                        task.put("guid",zndbTask.getRowguid());
                        task.put("name",zndbTask.getName());
                        task.put("deptname",zndbTask.getDeptname());
                        task.put("type",zndbTask.getType());
                        task.put("finshtime",zndbTask.getFinshtime());
                        task.put("result",zndbTask.getResult());
                        task.put("guide",zndbTask.getGuide());
                        task.put("index",zndbTask.getPhase()+"-"+zndbTask.getStr("sxindex"));
                        if (checkshow(zndbTask,arr)){
                            if ("0".equals(zndbTask.getTasktype())){
                                mainlist.add(task);
                            }else {
                                paralleleventlist.add(task);
                            }
                        }
                    }
                }

                result.put("mainevent",mainlist);
                result.put("parallelevent",paralleleventlist);
                log.info("getListData="+result.toJSONString());
                return JsonUtils.zwdtRestReturn("1", "获取成功", result.toJSONString());
            }
            return JsonUtils.zwdtRestReturn("0", "token验证失败", result.toJSONString());
        } catch (Exception e) {
            log.debug("=======异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败" + e.getMessage(), "");
        }
    }

    private boolean checkshow(WZndbTask zndbTask,String[] arr) {
        for (String s : arr) {
            if (StringUtil.isBlank(s)){
                continue;
            }
            if (zndbTask.getStr("priority1").contains(s)){
                return  false;
            }else {
                if (zndbTask.getStr("priority2").contains(s)){
                    return  true;
                }
            }
        }
        if ("1".equals(zndbTask.getStr("priority3"))){
            return  true;
        }
        return  false;
    }


}
