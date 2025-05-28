package com.epoint.sqgl.rabbitmqhandle.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sqgl.rabbitmqhandle.api.IReportGgDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 上报工改2.0数据至3.0接口
 * [一句话功能简述]
 *
 * @author Ray
 * @version [版本号, 2024年1月30日]
 */
@RestController
@RequestMapping("/reportggdata")
public class ReportGgDataInterface {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IReportGgDataService ireportGgDataService;

    /**
     * 处理项目基本信息表
     * [一句话功能简述]
     *
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/handlexmjbxxb", method = RequestMethod.POST)
    public String handleXmjbxxb(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用handlexmjbxxb接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 2.0数据主键
                String lsh = obj.getString("lsh");
                String splcbm = obj.getString("splcbm");
                String splcbbh = obj.getString("splcbbh");

                String cd = obj.getString("cd");// 长度
                String jsdw = obj.getString("jsdw");// 建设单位
                String jsdwdm = obj.getString("jsdwdm");// 建设单位代码
                String gchyfl = obj.getString("gchyfl");// 工程行业分类
                String jsdwlx = obj.getString("jsdwlx");// 建设单位类型

                if (StringUtil.isBlank(splcbm)) {
                    return JsonUtils.zwdtRestReturn("0", "splcbm为空！", "");
                }
                if (StringUtil.isBlank(splcbbh)) {
                    return JsonUtils.zwdtRestReturn("0", "splcbbh为空！", "");
                }
                if (StringUtil.isBlank(jsdw)) {
                    return JsonUtils.zwdtRestReturn("0", "jsdw为空！", "");
                }
                if (StringUtil.isBlank(jsdwdm)) {
                    return JsonUtils.zwdtRestReturn("0", "jsdwdm为空！", "");
                }
                if (StringUtil.isBlank(gchyfl)) {
                    return JsonUtils.zwdtRestReturn("0", "gchyfl为空！", "");
                }
                if (StringUtil.isBlank(jsdwlx)) {
                    return JsonUtils.zwdtRestReturn("0", "jsdwlx为空！", "");
                }
                // 查询2.0项目基本信息表数据
                Record record = ireportGgDataService.getXmjbxxb(lsh);

                if (StringUtil.isBlank(record)) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关信息！", "");
                }

                if (!"3".equals(record.getStr("sjsczt")) || !"1".equals(record.getStr("sjyxbs"))
                        || !"1".equals(record.getStr("isinsert"))) {
                    return JsonUtils.zwdtRestReturn("0", "此条项目信息异常，请确认有效状态！！", "");
                }

                record.setSql_TableName("spgl_xmjbxxb");
                record.setPrimaryKeys("lsh");

                Date now = new Date();
                record.set("lsh", null);
                record.set("isinsert", null);
                record.set("sync", null);
                record.set("sjgxsj", null);
                record.set("scgjsj", null);

                record.set("sjsczt", "0");

                record.set("sbsj", now);
                record.set("scsj", now);
                record.set("gxsj", now);

                // 新增数据
                record.set("splcbm", splcbm);
                record.set("splcbbh", splcbbh);
                record.set("cd", cd);// 长度
                record.set("gchyfl", gchyfl);// 工程行业分类
                record.set("jsdw", jsdw);// 建设单位
                record.set("jsdwdm", jsdwdm);// 建设单位代码
                record.set("jsdwlx", jsdwlx);// 建设单位类型

                // 新增3.0项目基本信息表数据
                int i = ireportGgDataService.insertXmjbxxb(record);

                JSONObject itemJson = new JSONObject();
                itemJson.put("status", i);

                if (i != 1) {
                    return JsonUtils.zwdtRestReturn("0", "推送项目基本信息表失败！", itemJson.toString());
                }

                log.info("=======结束调用handlexmjbxxb接口=======");
                return JsonUtils.zwdtRestReturn("1", "推送项目基本信息表成功！", itemJson.toString());
            } else {
                log.info("=======结束调用handlexmjbxxb接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======handlexmjbxxb接口参数：params【" + params + "】=======");
            log.info("=======handlexmjbxxb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "推送项目基本信息表失败：" + e.getMessage(), "");
        }
    }


    /**
     * spgl_xmspsxblxxb & spgl_xmsqcljqtfjxxb
     * [一句话功能简述]
     *
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/handlexmspsxblxxb", method = RequestMethod.POST)
    public String handleXmspsxblxxb(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用handlexmspsxblxxb接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、审批事项市里编码
                String spsxslbm = obj.getString("spsxslbm");
                String gcdm = obj.getString("gcdm");
                String spsxbm = obj.getString("spsxbm");
                String spsxbbh = obj.getString("spsxbbh");

                String lxr = obj.getString("lxr");// 联系人
                String lxrsjh = obj.getString("lxrsjh");// 联系人手机号
                String ywqx = obj.getString("ywqx");// 业务情形（代码参照附录“A.14业务情形”）

                if (StringUtil.isBlank(spsxslbm)) {
                    return JsonUtils.zwdtRestReturn("0", "spsxslbm为空！", "");
                }
                if (StringUtil.isBlank(gcdm)) {
                    return JsonUtils.zwdtRestReturn("0", "gcdm为空！", "");
                }
                if (StringUtil.isBlank(spsxbm)) {
                    return JsonUtils.zwdtRestReturn("0", "spsxbm为空！", "");
                }
                if (StringUtil.isBlank(spsxbbh)) {
                    return JsonUtils.zwdtRestReturn("0", "spsxbbh为空！", "");
                }
                if (StringUtil.isBlank(lxr)) {
                    return JsonUtils.zwdtRestReturn("0", "lxr为空！", "");
                }
                if (StringUtil.isBlank(lxrsjh)) {
                    return JsonUtils.zwdtRestReturn("0", "lxrsjh为空！", "");
                }
                if (StringUtil.isBlank(ywqx)) {
                    return JsonUtils.zwdtRestReturn("0", "ywqx为空！", "");
                }

                // 查3.0已上报的项目基本信息表
                Record xmjbxxb3 = ireportGgDataService.getXmjbxxbByGcdm(gcdm);

                if (StringUtil.isBlank(xmjbxxb3)) {
                    return JsonUtils.zwdtRestReturn("0", "未在3.0种查询到此项目已上报信息！", "");
                }

                String splcbm = xmjbxxb3.getStr("splcbm");
                String splcbbh = xmjbxxb3.getStr("splcbbh");

                // 查询2.0办件信息
                Record record = ireportGgDataService.getXmspsxblxxb(spsxslbm);

                if (StringUtil.isBlank(record)) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关办件信息！", "");
                }

                if (!"3".equals(record.getStr("sjsczt")) || !"1".equals(record.getStr("sjyxbs"))
                        || !"1".equals(record.getStr("isinsert"))) {
                    return JsonUtils.zwdtRestReturn("0", "此条项目信息异常，请确认有效状态！！", "");
                }

                record.setSql_TableName("spgl_xmspsxblxxb");
                record.setPrimaryKeys("lsh");

                Date now = new Date();
                record.set("lsh", null);
                record.set("isinsert", null);
                record.set("sync", null);
                record.set("sjgxsj", null);
                record.set("scgjsj", null);

                record.set("sjsczt", "0");

                record.set("sbsj", now);
                record.set("scsj", now);
                record.set("gxsj", now);

                // 新增数据
                record.set("splcbm", splcbm);
                record.set("splcbbh", splcbbh);
                record.set("spsxbm", spsxbm);
                record.set("spsxbbh", spsxbbh);

                record.set("lxr", lxr);// 联系人
                record.set("lxrsjh", lxrsjh);// 联系人手机号
                record.set("ywqx", ywqx);// 业务情形

                // 新增3.0表数据
                int i = ireportGgDataService.insertXmjbxxb(record);

                JSONObject itemJson = new JSONObject();
                itemJson.put("status", i);

                if (i != 1) {
                    return JsonUtils.zwdtRestReturn("0", "推送信息表失败！", itemJson.toString());
                }

                // 2.0其他附件信息
                List<Record> xmqtfjxxbs = ireportGgDataService.getXmqtfjxxbs(spsxslbm);
                if (ValidateUtil.isNotBlankCollection(xmqtfjxxbs)) {
                    for (Record xmsqcljqtfjxxb : xmqtfjxxbs) {

                        xmsqcljqtfjxxb.setSql_TableName("spgl_xmsqcljqtfjxxb");
                        xmsqcljqtfjxxb.setPrimaryKeys("lsh");

                        xmsqcljqtfjxxb.set("lsh", null);
                        xmsqcljqtfjxxb.set("isinsert", null);
                        xmsqcljqtfjxxb.set("sync", null);
                        xmsqcljqtfjxxb.set("sjgxsj", null);
                        xmsqcljqtfjxxb.set("scgjsj", null);

                        xmsqcljqtfjxxb.set("sjsczt", "0");

                        xmsqcljqtfjxxb.set("sbsj", now);
                        xmsqcljqtfjxxb.set("scsj", now);
                        xmsqcljqtfjxxb.set("gxsj", now);

                        xmsqcljqtfjxxb.set("clmc", xmsqcljqtfjxxb.getStr("fjmc"));
                        xmsqcljqtfjxxb.set("cllx", xmsqcljqtfjxxb.getStr("fjlx"));

                        xmsqcljqtfjxxb.set("clmlbh", "001");//
                        xmsqcljqtfjxxb.set("clfl", "1");//
                        xmsqcljqtfjxxb.set("clid", UUID.randomUUID().toString());//
                        xmsqcljqtfjxxb.set("sqfs", "1");//
                        xmsqcljqtfjxxb.set("sqsl", "1");//

                        // 新增3.0表数据
                        ireportGgDataService.insertXmjbxxb(xmsqcljqtfjxxb);
                    }
                }

                log.info("=======结束调用handlexmspsxblxxb接口=======");
                return JsonUtils.zwdtRestReturn("1", "推送信息表成功！", itemJson.toString());
            } else {
                log.info("=======结束调用handlexmspsxblxxb接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======handlexmspsxblxxb接口参数：params【" + params + "】=======");
            log.info("=======handlexmspsxblxxb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "推送信息表失败：" + e.getMessage(), "");
        }
    }

    /**
     * spgl_xmspsxblxxxxb & spgl_xmspsxbltbcxxxb & spgl_xmspsxpfwjxxb
     * [一句话功能简述]
     *
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/handlexmspsxblxxxxb", method = RequestMethod.POST)
    public String handleXmspsxblxxxxb(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用handlexmspsxblxxxxb接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 审批事项市里编码
                String spsxslbm = obj.getString("spsxslbm");

                String sjly = obj.getString("sjly");// 数据来源
                String dwmc = obj.getString("dwmc");// 单位名称
                String dwtyshxydm = obj.getString("dwtyshxydm");// 单位统一社会信用代码

                if (StringUtil.isBlank(spsxslbm)) {
                    return JsonUtils.zwdtRestReturn("0", "spsxslbm为空！", "");
                }
                if (StringUtil.isBlank(sjly)) {
                    return JsonUtils.zwdtRestReturn("0", "sjly为空！", "");
                }
                if (StringUtil.isBlank(dwmc)) {
                    return JsonUtils.zwdtRestReturn("0", "dwmc为空！", "");
                }
                if (StringUtil.isBlank(dwtyshxydm)) {
                    return JsonUtils.zwdtRestReturn("0", "dwtyshxydm为空！", "");
                }

                // 查2.0的xxxxb表
                List<Record> xmspsxblxxxxbs = ireportGgDataService.getXmspsxblxxxxb(spsxslbm);
                if (ValidateUtil.isBlankCollection(xmspsxblxxxxbs)) {
                    return JsonUtils.zwdtRestReturn("0", "未查询到相关办件详细信息！", "");
                }
                for (Record record : xmspsxblxxxxbs) {
                    record.setSql_TableName("spgl_xmspsxblxxxxb");
                    record.setPrimaryKeys("lsh");

                    Date now = new Date();
                    record.set("lsh", null);
                    record.set("isinsert", null);
                    record.set("sync", null);
                    record.set("sjgxsj", null);
                    record.set("scgjsj", null);

                    record.set("sjsczt", "0");

                    record.set("sbsj", now);
                    record.set("scsj", now);
                    record.set("gxsj", now);

                    record.set("sjly", sjly);
                    record.set("dwmc", dwmc);
                    record.set("dwtyshxydm", dwtyshxydm);

                    // 新增3.0表数据
                    ireportGgDataService.insertXmjbxxb(record);
                }

                // 查2.0的xmspsxbltbcxxxb 表
                List<Record> xmspsxbltbcxxxbs = ireportGgDataService.getXmspsxbltbcxxxb(spsxslbm);
                if (ValidateUtil.isNotBlankCollection(xmspsxbltbcxxxbs)) {
                    for (Record record : xmspsxbltbcxxxbs) {
                        record.setSql_TableName("spgl_xmspsxbltbcxxxb");
                        record.setPrimaryKeys("lsh");

                        Date now = new Date();
                        record.set("lsh", null);
                        record.set("isinsert", null);
                        record.set("sync", null);
                        record.set("sjgxsj", null);
                        record.set("scgjsj", null);

                        record.set("sjsczt", "0");

                        record.set("sbsj", now);
                        record.set("scsj", now);
                        record.set("gxsj", now);

                        // 新增3.0表数据
                        ireportGgDataService.insertXmjbxxb(record);
                    }
                }

                // 查2.0的xmspsxpfwjxxb 表
                List<Record> xmspsxpfwjxxbs = ireportGgDataService.getXmspsxpfwjxxb(spsxslbm);
                if (ValidateUtil.isNotBlankCollection(xmspsxbltbcxxxbs)) {
                    for (Record record : xmspsxpfwjxxbs) {
                        record.setSql_TableName("spgl_xmspsxpfwjxxb");
                        record.setPrimaryKeys("lsh");

                        Date now = new Date();
                        record.set("lsh", null);
                        record.set("isinsert", null);
                        record.set("sync", null);
                        record.set("sjgxsj", null);
                        record.set("scgjsj", null);

                        record.set("sjsczt", "0");

                        record.set("sbsj", now);
                        record.set("scsj", now);
                        record.set("gxsj", now);

                        record.set("SPJGLX", "10");//10证照 20批文 30其他 99无

                        // 新增3.0表数据
                        ireportGgDataService.insertXmjbxxb(record);
                    }
                }

                JSONObject itemJson = new JSONObject();
                log.info("=======结束调用handlexmspsxblxxxxb接口=======");
                return JsonUtils.zwdtRestReturn("1", "推送信息表成功！", itemJson.toString());
            } else {
                log.info("=======结束调用handlexmspsxblxxxxb接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======handlexmspsxblxxxxb接口参数：params【" + params + "】=======");
            log.info("=======handlexmspsxblxxxxb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "推送信息表失败：" + e.getMessage(), "");
        }
    }

}
