package com.epoint.zwdt.zwdtrest.qzqd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.zwdt.zwdtrest.service.ZwdtService;

import static com.epoint.core.utils.string.StringUtil.*;

@RestController
@RequestMapping("/zwdtqzqd")
public class QzqdController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private ZwdtService service = new ZwdtService();

    /**
     * 获取咨询部门，获取部门电话
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getOuList", method = RequestMethod.POST)
    public String getOuList(@RequestBody String params) {
        try {
            log.info("=======开始获取getConsultList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域
                String areacode = isNotBlank(obj.getString("areacode")) ? obj.getString("areacode") : "";
                ;
                List<FrameOu> list = service.getOuList(areacode);
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", list);
                return JsonUtils.zwdtRestReturn("1", "获取部门数据成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultList接口参数：params【" + params + "】=======");
            log.info("=======getConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门数据异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取权责清单
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getPersonList", method = RequestMethod.POST)
    public String getPersonList(@RequestBody String params) {
        try {
            log.info("=======开始获取getPersonList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域
                String leftcode = isNotBlank(obj.getString("leftcode")) ? obj.getString("leftcode") : "";
                ;
                String coldetype = isNotBlank(obj.getString("coldetype")) ? obj.getString("coldetype") : "";
                ;
                String pageIndex = isNotBlank(obj.getString("pageIndex")) ? obj.getString("pageIndex") : "0";
                ;
                String pageSize = isNotBlank(obj.getString("pageSize")) ? obj.getString("pageSize") : "10";
                ;
                String areacode = isNotBlank(obj.getString("areacode")) ? obj.getString("areacode") : "";
                ;

                //搜索条件
                String bycode = isNotBlank(obj.getString("bycode")) ? obj.getString("bycode") : "";
                ;
                String byname = isNotBlank(obj.getString("byname")) ? obj.getString("byname") : "";
                ;

                int cur = Integer.parseInt(pageIndex) + 1;

                String url = "http://221.214.94.36:81/sxkcs/main/power/getPowerDutyListByPage";
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("page", cur);
                param.put("rows", pageSize);
                param.put("regionCode", areacode + "000000");
                if (isNotBlank(leftcode) && !"ALL".equals(leftcode)) {
                    param.put("orgCode", leftcode);
                }
                if (isNotBlank(byname) || isNotBlank(bycode) || isNotBlank(coldetype)) {
                    StringBuffer wherestr = new StringBuffer();
                    StringBuffer paramValuestr = new StringBuffer();
                    if (isNotBlank(byname)) {
                        wherestr.append(" and name like ?");
                        if (paramValuestr.length() > 0) {
                            paramValuestr.append(",");
                        }
                        paramValuestr.append("'%" + byname + "%'");
                    }
                    if (isNotBlank(bycode)) {
                        wherestr.append(" and INNER_CODE like ?");
                        if (paramValuestr.length() > 0) {
                            paramValuestr.append(",");
                        }
                        paramValuestr.append("'%" + bycode + "%'");
                    }
                    if (isNotBlank(coldetype)) {
                        wherestr.append(" and type in (?)");
                        if (paramValuestr.length() > 0) {
                            paramValuestr.append(",");
                        }
                        paramValuestr.append("'" + coldetype + "'");
                    }
                    param.put("whereValue", wherestr.toString());
                    param.put("paramValue", paramValuestr.toString());
                }

                param.put("order", "order by decode(type,?,1,?,2,?,3,?,4,?,5,?,6,?,7,?,8,?,9,?,10,?,11),inner_code,code");


                if ("370800".equals(areacode) && isNotBlank(param.get("whereValue")) && StringUtils.isNotBlank(coldetype) && coldetype.length() != 2) {
                    param.put("whereValue", param.get("whereValue") + " and auth_level != '4' and IS_CONTAIN_CITY_LIST= '1'");
                }

                if (isNotBlank(param.get("paramValue"))) {
                    param.put("paramValue", "[" + param.get("paramValue") + ",'XK','CF','QZ','ZS','JF','CJ','JL','JD','QR','QT','GG']");
                } else {
                    if ("370800".equals(areacode)) {
                        param.put("whereValue", "and type in ('XK','CF','QZ','ZS','JF','CJ','JL','QR','QT','GG','JD') and auth_level != '4' and IS_CONTAIN_CITY_LIST= '1' ");
                    } else {
                        param.put("whereValue", "and type in ('XK','CF','QZ','ZS','JF','CJ','JL','QR','QT','GG','JD') and IS_CITY_CONTAIN_COUNTY= ('1') ");
                    }
                    param.put("paramValue", "['XK','CF','QZ','ZS','JF','CJ','JL','JD','QR','QT','GG']");
                }
                log.info("url==========" + url);
                log.info("param======" + param);
                JSONObject resultJson = (JSONObject) getInfoByPost(url, param);
                if (resultJson != null) {
                    JSONArray laws = resultJson.getJSONArray("pageList");
                    if (laws != null && laws.size() > 0) {
                        for (Object object : laws) {
                            Map entry = (Map) object;
                            Object object2 = entry.get("columns");
                            Map<String, String> entry2 = (Map) object2;
                            String type = typeChange(entry2.get("TYPE"));
                            entry2.put("TYPE", type);
                        }
                    }
                }

                System.out.println(resultJson.get("totlaRow"));

                return JsonUtils.zwdtRestReturn("1", "获取权责清单成功", resultJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取权责清单失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPersonList接口参数：params【" + params + "】=======");
            log.info("=======getPersonList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取权责清单异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取详情
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public String getDetail(@RequestBody String params) {
        try {
            log.info("=======开始获取getDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域
                String id = isNotBlank(obj.getString("id")) ? obj.getString("id") : "";
                ;
                // 1.2、获取areacode
                String areacode = isNotBlank(obj.getString("areacode")) ? obj.getString("areacode") : "";
                ;

                String level = "4";
                if ("370800".equals(areacode)) {
                    level = "3";
                }

                JSONObject jsonobj = new JSONObject();

                String url = "http://221.214.94.36:81/sxkcs/main/power/getPowerDutyInfoByID?itemId=" + id;

                String resultString = HttpUtil.doPostJson(url, jsonobj.toString());

                JSONObject resultJson = JSONObject.parseObject(StringEscapeUtils.unescapeHtml4(resultString));


                JSONArray item = resultJson.getJSONArray("ItemInfo");
                for (Object object : item) {
                    Map entry = (Map) object;
                    String type = typeChange(entry.get("TYPE").toString());
                    entry.put("TYPE", type);
                }

                JSONArray laws = resultJson.getJSONArray("law");

                List<Object> yjlist = new ArrayList<>();
                List<Object> zrlist = new ArrayList<>();
                List<Object> sslist = new ArrayList<>();
                List<Object> zdlist = new ArrayList<>();
                for (Object object : laws) {
                    Map entry = (Map) object;
                    String tab = entry.get("LAW_TAB").toString();
                    String content = (String) entry.get("CONTENT");
                    if (isBlank(entry.get("NAME"))) {
                        continue;
                    }
                    String name = entry.get("NAME").toString();
                    System.out.println("name:" + name);
                    if (isBlank(content)) {
                        entry.put("CONTENT2", name);
                    } else {
                        entry.put("CONTENT2", content);
                    }
                    if (content != null && content.length() > 100) {
                        content = content.substring(0, 100) + "...";
                    }
                    entry.put("CONTENT", content);
                    if ("1".equals(tab)) {
                        yjlist.add(object);
                    } else {
                        zrlist.add(object);
                    }
                }

                JSONArray array = resultJson.getJSONArray("dutyitem");
                for (Object object : array) {
                    Map entry = (Map) object;
                    String tab = entry.get("DUTY_TYPE").toString();
                    String authlevel = entry.get("AUTH_LEVEL").toString();
                    if (level.equals(authlevel)) {
                        if ("直接实施责任".equals(tab)) {
                            sslist.add(object);
                        } else {
                            zdlist.add(object);
                        }
                    }
                }

                JSONObject dataJson = new JSONObject();
                dataJson.put("ItemInfo", resultJson.get("ItemInfo"));
                dataJson.put("yjlist", yjlist);
                dataJson.put("zrlist", zrlist);
                dataJson.put("sslist", sslist);
                dataJson.put("zdlist", zdlist);

                return JsonUtils.zwdtRestReturn("1", "获取详情成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取详情失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDetail接口参数：params【" + params + "】=======");
            log.info("=======getDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取详情异常：" + e.getMessage(), "");
        }
    }

    public String typeChange(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("XK", "行政许可");
        map.put("CF", "行政处罚");
        map.put("QZ", "行政强制");
        map.put("ZS", "行政征收");
        map.put("JF", "行政给付");
        map.put("CJ", "行政裁决");
        map.put("JL", "行政奖励");
        map.put("QR", "行政确认");
        map.put("QT", "其他行政权力");
        map.put("GG", "公共服务");
        map.put("JD", "行政检查");

        for (String key : map.keySet()) {
            if (type.equals(key)) {
                type = map.get(key);
            }
        }
        return type;
    }

    public static Object getInfoByPost(String url, Map<String, Object> params) {
        Object rtnjson = new Object();
        try {
            HttpClient client = new HttpClient();
            PostMethod postMethod = new PostMethod(url);
            client.getParams().setContentCharset("UTF-8");
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                postMethod.setParameter(key, params.get(key).toString());
            }
            client.executeMethod(postMethod);
            InputStream inputStream = postMethod.getResponseBodyAsStream();
            rtnjson = JSON.parse(inputStream2String(inputStream));
            return rtnjson;
        } catch (Exception e) {
        }
        return null;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString("UTF-8");
    }
}
