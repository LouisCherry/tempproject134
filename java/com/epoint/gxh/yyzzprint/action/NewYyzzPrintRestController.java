package com.epoint.gxh.yyzzprint.action;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping("/newyyzzprint")
public class NewYyzzPrintRestController
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private static final String[] IUNIT = {"元整", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰",
            "仟" };
    private static final String[] DUNIT = {"角", "分", "厘" };

    @RequestMapping(value = "/getcompanyinfo", method = RequestMethod.POST)
    public String getcompanyinfo(@RequestBody String params, @Context HttpServletRequest request) {

        log.info("yyzzprint/getcompanyinfo开始" + new Date());
        JSONObject e = JSONObject.parseObject(params);
        String token = e.getString("token");

        JSONObject obj = e.getJSONObject("params");
        JSONObject result = new JSONObject();
        try {
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certnum = obj.getString("certnum");
                String results = getcompanyinfobycertnum(certnum);
                log.info("getcompanyinfo  backresult" + results);
                if (!isJSON(results)) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息获取异常，请重新申报", "");
                }
                /*
                 * JSONObject a = new JSONObject();
                 * a.put("data",
                 * "{\"code\":200,\"data\":\"{\\\"header\\\":{\\\"status\\\":\\\"1\\\",\\\"msg\\\":\\\"查询成功\\\"},\\\"data\\\":{\\\"alterRecoder\\\":[],\\\"baseinfo\\\":{\\\"apprdate\\\":\\\"2018-12-10\\\",\\\"uniscid\\\":\\\"91370800MA3NRCRQ84\\\",\\\"dom\\\":\\\"山东省济宁北湖省级旅游度假区荷花路京投总部大厦A座1204室\\\",\\\"empnum\\\":20,\\\"entname\\\":\\\"济宁公用快速路建设工程有限公司\\\",\\\"enttypeCn\\\":\\\"有限责任公司(非自然人投资或控股的法人独资)\\\",\\\"estdate\\\":\\\"2018-12-10\\\",\\\"industryco\\\":\\\"4813\\\",\\\"industryphy\\\":\\\"E\\\",\\\"lerep\\\":\\\"李鲁\\\",\\\"opfrom\\\":\\\"2018-12-10\\\",\\\"opscope\\\":\\\"市政道路工程、城市及道路照明工程、园林绿化工程、土石方工程、管道工程、地下综合管廊工程施工。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"opto\\\":\\\"2048-12-09\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\",\\\"regcap\\\":10000,\\\"regcapcurCn\\\":null,\\\"regno\\\":\\\"370891000000341\\\",\\\"regorg\\\":\\\"370891\\\",\\\"regorgCn\\\":\\\"济宁北湖省级旅游度假区市场监督管理局\\\",\\\"regstateCn\\\":\\\"在营（开业）企业\\\",\\\"town\\\":\\\"否\\\"},\\\"brchinfo\\\":[],\\\"cancel\\\":null,\\\"contact\\\":{\\\"lmid\\\":\\\"370891000012018120411236\\\",\\\"cerno\\\":\\\"370802196906253619\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"丁磊\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},\\\"investment\\\":[{\\\"acconam\\\":null,\\\"blicno\\\":\\\"9137080016593424X9\\\",\\\"blictypeCn\\\":\\\"企业法人营业执照(公司)\\\",\\\"condate\\\":\\\"2023-12-03\\\",\\\"conform\\\":\\\"1\\\",\\\"inv\\\":\\\"山东公用控股有限公司\\\",\\\"invid\\\":\\\"ff808081677dc0cd016787842e692477\\\",\\\"invtypeCn\\\":\\\"企业法人\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\",\\\"subconam\\\":10000,\\\"subconform\\\":\\\"1\\\"}],\\\"invPerson\\\":[],\\\"priPerson\\\":[{\\\"personid\\\":\\\"c0cd01678788e18025e1\\\",\\\"cerno\\\":\\\"37080219651204331X\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"李鲁\\\",\\\"positionCn\\\":\\\"执行董事\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},{\\\"personid\\\":\\\"ff808081677dc0cd0167878b2ba226c9\\\",\\\"cerno\\\":\\\"370802196906253619\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"丁磊\\\",\\\"positionCn\\\":\\\"监事\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"},{\\\"personid\\\":\\\"c0cd0167878e20e927b3\\\",\\\"cerno\\\":\\\"370802196509213912\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"冯中君\\\",\\\"positionCn\\\":\\\"经理\\\",\\\"pripid\\\":\\\"370891000012018120411236\\\"}],\\\"revoke\\\":null,\\\"liquidating\\\":null}}\",\"message\":\"\"}"
                 * );
                 * a.put("message" , "");
                 * a.put("code", "200");
                 * String results = a.toJSONString();
                 */
                if (results == null) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息未获取到...", "");
                }
                JSONObject res = JSONObject.parseObject(results);
                JSONObject data = res.getJSONObject("data");
                JSONObject data1 = data.getJSONObject("data");
                JSONObject data2 = data1.getJSONObject("data");
                if (data2 == null) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息核验不通过", "");
                }
                else {
                    JSONObject baseinfo = data2.getJSONObject("baseinfo");
                    JSONArray priPerson = data2.getJSONArray("priPerson");
                    result.put("baseinfo", baseinfo);
                    result.put("priPerson", priPerson);
                    String creditcode = baseinfo.getString("uniscid");//统一信用代码
                    result.put("creditcode", creditcode);
                    String dwmc = baseinfo.getString("entname");//企业名称
                    result.put("dwmc", dwmc);
                    String companytype = baseinfo.getString("enttypeCn");//企业类型
                    result.put("companytype", companytype);
                    String legalman = baseinfo.getString("lerep");//法定代表人或负责人
                    result.put("legalman", legalman);
                    String jyfw = baseinfo.getString("opscope");//经营范围
                    result.put("jyfw", jyfw);
                    String regcap = baseinfo.getString("regcap");//注册资本 单位：万
                    String cnregcap = toChinese(regcap + "0000");
                    result.put("cnregcap", cnregcap);
                    log.info("yyzzprint/getcompanyinfo结束" + new Date());
                    return JsonUtils.zwdtRestReturn("1", "获取营业执照信息成功", result.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e1) {
            e1.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用异常：" + e1.getMessage(), "");
        }

    }

    public static String toChinese(String str) {
        if (StringUtil.isBlank(str) || !str.matches("(-)?[\\d]*(.)?[\\d]*")) {

            return str;
        }
        if ("0".equals(str) || "0.00".equals(str) || "0.0".equals(str)) {
            return "零元";
        }

        boolean flag = false;
        if (str.startsWith("-")) {
            flag = true;
            str = str.replaceAll("-", "");
        }
        str = str.replaceAll(",", "");
        String integerStr;
        String decimalStr;
        if (str.indexOf("'.'") > 0) {
            integerStr = str.substring(0, str.indexOf("'.'"));
            decimalStr = str.substring(str.indexOf("'.'") + 1);
        }
        else if (str.indexOf("'.'") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        }
        else {
            integerStr = str;
            decimalStr = "";
        }
        if (integerStr.length() > IUNIT.length) {
            return str;
        }
        int[] integers = toIntArray(integerStr);
        if (integers.length > 1 && integers[0] == 0) {
            if (flag) {
                str = "-" + str;
            }
            return str;
        }
        boolean isWan = isWan5(integerStr);
        int[] decimals = toIntArray(decimalStr);
        String result = getChineseInteger(integers, isWan) + getChineseDecimal(decimals);
        if (flag) {
            return "负" + result;
        }
        else {
            return result;
        }
    }

    public static boolean isJSON(String str) {
        try {
            Object obj = JSON.parseObject(str);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getcompanyinfobycertnum(String certnum) {
        try {
            //获取token的地址
            String qytokenurl = ConfigUtil.getConfigValue("jnyyzz", "qytoken");
            //获取企业信息地址
            String qyyyzzurl = ConfigUtil.getConfigValue("jnyyzz", "qyyyzz");

            String apikey = ConfigUtil.getConfigValue("jnyyzz", "apikey");
            String accesstoken = ConfigUtil.getConfigValue("jnyyzz", "accesstoken");

            String BizId = UUID.randomUUID().toString();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Apikey", apikey);
            headers.put("Authorization", "Basic " + accesstoken);
            headers.put("X-BizId", BizId);
            String resultString = HttpUtil.doGet(qytokenurl, headers);
            /*
             * JSONObject a = new JSONObject();
             * a.put("data",
             * "{\"code\":\"200\",\"msg\":\"请求成功\",\"data\":\"{\\\"access_token\\\":\\\"b1446cf6-ac1a-4ba3-8daf-4d28ea814da4\\\",\\\"token_type\\\":\\\"bearer\\\",\\\"expires_in\\\":7194,\\\"scope\\\":\\\"all\\\"}\",\"total\":0}"
             * );
             * a.put("message", "");
             * a.put("code", "200");
             * String resultString = a.toJSONString();
             */
            log.info("getcompanyinfobycertnum token backresult" + resultString);
            if (isJSON(resultString)) {

                JSONObject res = JSONObject.parseObject(resultString);
                if ("200".equals(res.getString("code"))) {
                    JSONObject data = res.getJSONObject("data");
                    JSONObject data2 = data.getJSONObject("data");
                    String access_token = data2.getString("access_token");
                    headers.clear();
                    headers.put("Apikey", apikey);
                    headers.put("Authorization", "Bearer " + access_token);
                    headers.put("X-BizId", BizId);
                    //查询条件
                    Map<String, Object> postparams = new HashMap<String, Object>();
                    postparams.put("q", certnum);

                    return HttpUtil.doPost(qyyyzzurl, postparams, headers);
                    /*
                     * JSONObject b = new JSONObject();
                     * b.put("data",
                     * "{\"code\":\"200\",\"msg\":\"请求成功\",\"data\":\"{\\\"header\\\":{\\\"status\\\":\\\"1\\\",\\\"msg\\\":\\\"查询成功\\\"},\\\"data\\\":{\\\"alterRecoder\\\":[{\\\"altaf\\\":\\\"一般项目：物联网技术服务；信息技术咨询服务；计算机系统服务；技术服务、技术开发、技术咨询、技术交流、技术转让、技术推广；计算机软硬件及辅助设备批发；办公服务；办公设备销售。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：公章刻制。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\\\",\\\"altbe\\\":\\\"印章制作、刻章材料批发零售。打字复印、广告设计服务。（依法须经批准的项目，经相关部门批准后方可开展经营活动）\\\",\\\"altdate\\\":\\\"2021-03-02\\\",\\\"altitemCn\\\":\\\"经营范围\\\",\\\"pripid\\\":\\\"370811000022020072197044\\\"},{\\\"altaf\\\":null,\\\"altbe\\\":null,\\\"altdate\\\":\\\"2021-03-02\\\",\\\"altitemCn\\\":\\\"章程\\\",\\\"pripid\\\":\\\"370811000022020072197044\\\"}],\\\"baseinfo\\\":{\\\"apprdate\\\":\\\"2021-03-02\\\",\\\"uniscid\\\":\\\"91370811MA3TKE9Y12\\\",\\\"dom\\\":\\\"山东省济宁市任城区阜桥街道运河路2号（太白路与运河路交叉口向南100路南）\\\",\\\"empnum\\\":2,\\\"entname\\\":\\\"济宁市富卓信息服务有限公司\\\",\\\"enttypeCn\\\":\\\"有限责任公司(自然人独资)\\\",\\\"estdate\\\":\\\"2020-07-22\\\",\\\"industryco\\\":\\\"7259\\\",\\\"industryphy\\\":\\\"L\\\",\\\"lerep\\\":\\\"朱青\\\",\\\"opfrom\\\":\\\"2020-07-22\\\",\\\"opscope\\\":\\\"一般项目：物联网技术服务；信息技术咨询服务；计算机系统服务；技术服务、技术开发、技术咨询、技术交流、技术转让、技术推广；计算机软硬件及辅助设备批发；办公服务；办公设备销售。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：公章刻制。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\\\",\\\"opto\\\":null,\\\"pripid\\\":\\\"370811000022020072197044\\\",\\\"regcap\\\":20,\\\"regcapcurCn\\\":null,\\\"regno\\\":\\\"370811200452316\\\",\\\"regorg\\\":\\\"370811\\\",\\\"regorgCn\\\":\\\"济宁市任城区市场监督管理局\\\",\\\"regstateCn\\\":\\\"在营（开业）企业\\\",\\\"town\\\":\\\"否\\\"},\\\"brchinfo\\\":[],\\\"cancel\\\":null,\\\"contact\\\":{\\\"lmid\\\":\\\"370811000022020072197044\\\",\\\"cerno\\\":\\\"370828199307034729\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"朱青\\\",\\\"pripid\\\":\\\"370811000022020072197044\\\"},\\\"investment\\\":[],\\\"invPerson\\\":[{\\\"invid\\\":\\\"bc13017374360bb970af\\\",\\\"cerno\\\":\\\"370828199307034729\\\",\\\"certypeCn\\\":\\\"中华人民共和国居民身份证\\\",\\\"inv\\\":\\\"朱青\\\",\\\"liacconam\\\":null,\\\"lisubconam\\\":20,\\\"pripid\\\":\\\"370811000022020072197044\\\"}],\\\"priPerson\\\":[{\\\"personid\\\":\\\"bc13017374389f33729a\\\",\\\"cerno\\\":\\\"370828199307034729\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"朱青\\\",\\\"positionCn\\\":\\\"执行董事兼经理\\\",\\\"pripid\\\":\\\"370811000022020072197044\\\"},{\\\"personid\\\":\\\"bc1301737439612c7345\\\",\\\"cerno\\\":\\\"370829196202252941\\\",\\\"certype\\\":\\\"10\\\",\\\"name\\\":\\\"刘五妮\\\",\\\"positionCn\\\":\\\"监事\\\",\\\"pripid\\\":\\\"370811000022020072197044\\\"}],\\\"revoke\\\":null,\\\"liquidating\\\":null}}\",\"total\":0}"
                     * );
                     * b.put("message", "请求成功");
                     * b.put("code", "200");
                     * return b.toJSONString();
                     */
                }
                else {
                    return res.getString("msg");
                }

            }
            else {
                return "json解析异常";
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return "1";
        }

    }

    public String getpersonalinfobycertnum(String certnum) {
        try {
            String qytokenurl = ConfigUtil.getConfigValue("jnyyzz", "grtoken");
            String qyyyzzurl = ConfigUtil.getConfigValue("jnyyzz", "gryyzz");
            String apikey = ConfigUtil.getConfigValue("jnyyzz", "apikey");
            String accesstoken = ConfigUtil.getConfigValue("jnyyzz", "accesstoken");
            String BizId = UUID.randomUUID().toString();
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Apikey", apikey);
            headers.put("Authorization", "Basic " + accesstoken);
            headers.put("X-BizId", BizId);
            String resultString = HttpUtil.doGet(qytokenurl, headers);
            /*
             * JSONObject a = new JSONObject();
             * a.put("data",
             * "{\"code\":\"200\",\"msg\":\"请求成功\",\"data\":\"{\\\"access_token\\\":\\\"b1446cf6-ac1a-4ba3-8daf-4d28ea814da4\\\",\\\"token_type\\\":\\\"bearer\\\",\\\"expires_in\\\":7194,\\\"scope\\\":\\\"all\\\"}\",\"total\":0}"
             * );
             * a.put("message", "");
             * a.put("code", "200");
             * String resultString = a.toJSONString();
             */
            log.info("getpersonalinfobycertnum token backresult" + resultString);
            if (isJSON(resultString)) {

                JSONObject res = JSONObject.parseObject(resultString);
                if ("200".equals(res.getString("code"))) {
                    JSONObject data = res.getJSONObject("data");
                    JSONObject data2 = data.getJSONObject("data");
                    String access_token = data2.getString("access_token");
                    headers.clear();
                    headers.put("Apikey", apikey);
                    headers.put("Authorization", "Bearer " + access_token);
                    headers.put("X-BizId", BizId);
                    Map<String, Object> postparams = new HashMap<String, Object>();
                    postparams.put("q", certnum);
                    return HttpUtil.doPost(qyyyzzurl, postparams, headers);
                    /*
                     * JSONObject b = new JSONObject();
                     * b.put("data",
                     * "{\"code\":\"200\",\"msg\":\"请求成功\",\"data\":\"{\\\"header\\\":{\\\"status\\\":\\\"1\\\",\\\"msg\\\":\\\"查询成功\\\"},\\\"data\\\":{\\\"baseinfo\\\":{\\\"apprdate\\\":\\\"2022-07-11\\\",\\\"compformCn\\\":\\\"个人经营\\\",\\\"estdate\\\":\\\"2022-06-09\\\",\\\"name\\\":\\\"赵敏\\\",\\\"oploc\\\":\\\"鱼台县滨湖街道湖陵三路与观鱼大道交汇处向北88米路东锦绣华城5号楼1-107号商铺（自主申报）\\\",\\\"opscope\\\":\\\"一般项目：食品销售（仅销售预包装食品）；保健食品（预包装）销售；新鲜水果零售；玩具销售；农副产品销售；初级农产品收购；互联网销售（除销售需要许可的商品）。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：食品销售；烟草制品零售；食品生产。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\\\",\\\"pripid\\\":\\\"3708270000520220609BGGFX\\\",\\\"regno\\\":\\\"370827600441877\\\",\\\"regorg\\\":\\\"370827\\\",\\\"regorgCn\\\":\\\"鱼台县市场监督管理局\\\",\\\"regstateCn\\\":\\\"在营（开业）企业\\\",\\\"traname\\\":\\\"鱼台县栗满多炒货店\\\",\\\"uniscid\\\":\\\"92370827MABQE8RC9G\\\",\\\"empnum\\\":\\\"3\\\",\\\"industryphy\\\":\\\"批发和零售业\\\"},\\\"operator\\\":{\\\"cerno\\\":\\\"370827198012262029\\\",\\\"name\\\":\\\"赵敏\\\",\\\"personid\\\":\\\"3708270000520220609BGGFX\\\",\\\"pripid\\\":\\\"3708270000520220609BGGFX\\\"},\\\"alterRecoders\\\":[{\\\"altaf\\\":\\\"一般项目：食品销售（仅销售预包装食品）；保健食品（预包装）销售；新鲜水果零售；玩具销售；农副产品销售；初级农产品收购；互联网销售（除销售需要许可的商品）。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：食品销售；烟草制品零售；食品生产。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\\\",\\\"altbe\\\":\\\"一般项目：食品销售（仅销售预包装食品）；保健食品（预包装）销售；新鲜水果零售；玩具销售。（除依法须经批准的项目外，凭营业执照依法自主开展经营活动）许可项目：食品销售；烟草制品零售。（依法须经批准的项目，经相关部门批准后方可开展经营活动，具体经营项目以相关部门批准文件或许可证件为准）\\\",\\\"altdate\\\":\\\"2022-07-11\\\",\\\"altid\\\":\\\"eca07554eca0e0dc008a52a6a881ebd64d0181ec9a9f226b87\\\",\\\"altitemCn\\\":\\\"经营范围及方式\\\",\\\"pripid\\\":\\\"3708270000520220609BGGFX\\\"}],\\\"cancel\\\":null,\\\"revoke\\\":null}}\",\"total\":0}"
                     * );
                     * b.put("message", "请求成功");
                     * b.put("code", "200");
                     * return b.toJSONString();
                     */
                }
                else {
                    return res.getString("msg");
                }

            }
            else {
                return "json解析异常";
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return "1";
        }

    }

    private static int[] toIntArray(String number) {
        int[] array = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            array[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        return array;
    }

    public static String getChineseInteger(int[] integers, boolean isWan) {
        StringBuffer chineseInteger = new StringBuffer("");
        int length = integers.length;
        if (length == 1 && integers[0] == 0) {
            return "";
        }
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13) {
                    key = IUNIT[4];
                }
                else if ((length - i) == 9) {
                    key = IUNIT[8];
                }
                else if ((length - i) == 5 && isWan) {
                    key = IUNIT[4];
                }
                else if ((length - i) == 1) {
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            chineseInteger.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return chineseInteger.toString();
    }

    private static String getChineseDecimal(int[] decimals) {
        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) {
            if (i == 3) {
                break;
            }
            chineseDecimal.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return chineseDecimal.toString();
    }

    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            }
            else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        }
        else {
            return false;
        }
    }
}
