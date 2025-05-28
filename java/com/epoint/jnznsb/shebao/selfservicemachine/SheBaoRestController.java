package com.epoint.jnznsb.shebao.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/shebao")
public class SheBaoRestController {

    transient Logger log = LogUtil.getLog(SheBaoRestController.class);
    public static final String licenseKey = "3f8a85b7-551d-4922-9482-eaf7120f0885";

    /**
     * 0 社保参保缴费证明
     */
    @RequestMapping(value = "/shebaocanbaozm", method = RequestMethod.POST)
    public String sheBaoCanBaoZm(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("Sfzhm");
            String xm = obj.getString("xm");

            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");
            String jym = obj.getString("jym");
            String dybm = obj.getString("dybm");
            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "printYlcbzm";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s Sfzhm=\"" + sfzhm + "\"/><s xm=\"" + xm
                    + "\"/><s xzbz=\"101,102,109,201,401\"/><s qsny=\"" + qsny + "\"/><s zzny=\"" + zzny
                    + "\"/><s jym=\"" + jym + "\"/><s dybm=\"" + dybm + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");
            int textindex = getErrFlagIndex(s, "text");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                dataJson.put("_lesb__text", s.getJSONObject(textindex).get("text"));
            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 0 社保参保缴费证明
     */
    @RequestMapping(value = "/shebaocanbaozmJson", method = RequestMethod.POST)
    public String shebaocanbaozmJson(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("Sfzhm");
            String xm = obj.getString("xm");

            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");
            String jym = obj.getString("jym");
            String dybm = obj.getString("dybm");
            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "printYlcbzm";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s Sfzhm=\"" + sfzhm + "\"/><s xm=\"" + xm
                    + "\"/><s xzbz=\"101,102,109,201,401\"/><s qsny=\"" + qsny + "\"/><s zzny=\"" + zzny
                    + "\"/><s jym=\"" + jym + "\"/><s dybm=\"" + dybm + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");
            int textindex = getErrFlagIndex(s, "text");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                // 解析text
                JSONObject _lesb__text = new JSONObject();
                String content = s.getJSONObject(textindex).getString("text");
                String contentsplit[] = content.split("~n~zt10 ~h1.5~n");
                String head = contentsplit[1];

                String titlesplit[] = head.split("~n");

                String shbzhmsplit[] = titlesplit[1].split("查询年月");
                _lesb__text.put("shbzhm", shbzhmsplit[0].replace("社会保障号码：", ""));

                String zglbsplit[] = titlesplit[3].split("职工类别：");
                _lesb__text.put("zglb", zglbsplit[1]);
                _lesb__text.put("xdwmc", zglbsplit[0].replace("现单位名称：", ""));

                String dwmcsplit[] = titlesplit[2].split("险种：");
                _lesb__text.put("xz", dwmcsplit[1]);

                String ssjbjg[] = titlesplit[4].split("打印日期：");
                _lesb__text.put("ssjbjg", ssjbjg[0].replace("所属经办机构：", ""));

                int llcbqkrow = 0;// 历年参保情况
                for (int i = 0; i < titlesplit.length; i++) {
                    if (titlesplit[i].indexOf("历年参保情况") != -1) {
                        llcbqkrow = i;
                    }
                }

                String yanglao[] = titlesplit[llcbqkrow + 4].split("│");

                String sy[] = titlesplit[llcbqkrow + 6].split("│");
                String gs[] = titlesplit[llcbqkrow + 8].split("│");
                _lesb__text.put("ylqs1", "".equals(yanglao[2].trim()) ? "无数据" : yanglao[2]);
                _lesb__text.put("ylzz1", "".equals(yanglao[3].trim()) ? "无数据" : yanglao[3]);
                _lesb__text.put("zdqk1", "".equals(yanglao[4].trim()) ? "无数据" : yanglao[4]);
                _lesb__text.put("ljjfys1", "".equals(yanglao[5].trim()) ? "无数据" : yanglao[5]);
                _lesb__text.put("ljdwjf1", "".equals(yanglao[6].trim()) ? "无数据" : yanglao[6]);
                _lesb__text.put("ljgrjf1", "".equals(yanglao[7].trim()) ? "无数据" : yanglao[7]);

                _lesb__text.put("ylqs3", "".equals(sy[2].trim()) ? "无数据" : sy[2]);
                _lesb__text.put("ylzz3", "".equals(sy[3].trim()) ? "无数据" : sy[3]);
                _lesb__text.put("zdqk3", "".equals(sy[4].trim()) ? "无数据" : sy[4]);
                _lesb__text.put("ljjfys3", "".equals(sy[5].trim()) ? "无数据" : sy[5]);
                _lesb__text.put("ljdwjf3", "".equals(sy[6].trim()) ? "无数据" : sy[6]);
                _lesb__text.put("ljgrjf3", "".equals(sy[7].trim()) ? "无数据" : sy[7]);

                _lesb__text.put("ylqs4", "".equals(gs[2].trim()) ? "无数据" : gs[2]);
                _lesb__text.put("ylzz4", "".equals(gs[3].trim()) ? "无数据" : gs[3]);
                _lesb__text.put("zdqk4", "".equals(gs[4].trim()) ? "无数据" : gs[4]);
                _lesb__text.put("ljjfys4", "".equals(gs[5].trim()) ? "无数据" : gs[5]);
                _lesb__text.put("ljdwjf4", "".equals(gs[6].trim()) ? "无数据" : gs[6]);
                _lesb__text.put("ljgrjf4", "".equals(gs[7].trim()) ? "无数据" : gs[7]);

                dataJson.put("_lesb__text", _lesb__text);
            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 1 职工getUrlBack汇总信息
     */
    @RequestMapping(value = "/zhigongyaolaohuizong", method = RequestMethod.POST)
    public String zhiGongYaoLaoHuiZong(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayAgedTotal";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm
                    + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");

            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");

                org.json.JSONObject r = d.getJSONObject("r");

                dataJson.put("xzbz", r.get("xzbz"));
                dataJson.put("maxny", r.get("maxny"));
                dataJson.put("stjfys", r.get("stjfys"));
                dataJson.put("jfys", r.get("jfys"));
                dataJson.put("sjjfys", r.get("sjjfys"));
                dataJson.put("xzbzmc", r.get("xzbzmc"));
                dataJson.put("minny", r.get("minny"));
                dataJson.put("dwjfe", r.get("dwjfe"));
                dataJson.put("grjfe", r.get("grjfe"));
                dataJson.put("jmjfe", r.get("jmjfe"));

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2 职工养老缴费明细信息
     */
    @RequestMapping(value = "/zhigongyanglaojiaofeimx", method = RequestMethod.POST)
    public String zhiGongYangLaoJiaoFeiMx(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayAged";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s qsny=\""
                    + qsny + "\"/><s zzny=\"" + zzny + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);
            // String xmlresult = "<?xml version='1.0' encoding='GBK'?><p><s
            // _lesb__errcode_='0' /><s minjfny='202306' /><s maxjfny='202306'
            // /><s jfys='1' /><s
            // jfxx='累计缴费0年1月,共中断0个月,最早缴费年月202306,最晚缴费年月202306' /><s zdys='0'
            // /><s
            // typelistfords_aged='ny:s,dwbh:s,dwmc:s,dwxz:s,dwxzmc:s,xzbz:s,xzbzmc:s,jflslb:s,jflslbmc:s,dwjfe:n,grjfe:n,dwjfjs:n,grjfjs:n,fsyy:s,fsyymc:s,jbrq:d,fsrq:s,jbjgid:s,jbjgmc:s,jfny:s,jfrq:s,tyshxydm:s'
            // /><s errflag='0' /><d k='ds_aged' ><r grjfjs='4378.00000000'
            // dwmc='上海外服（山东）人力资源服务有限公司济宁分公司' dwxz='10' dwbh='3708115249'
            // xzbz='101' grjfe='350.24000000' jflslb='4' fsyy='1'
            // jfrq='20230625' ny='202306' dwxzmc='企业' fsyymc='单位应缴缴费'
            // jbjgid='37089301' jfny='202306' fsrq='20230625' xzbzmc='企业养老'
            // jbrq='20230626004928' dwjfjs='4378.00000000' jflslbmc='实行账户后缴费'
            // dwjfe='700.48000000' jbjgmc='济宁市任城区企业'
            // tyshxydm='91370800MA3C5KYU7E' /></d></p>";

            log.info("xmlresult     " + xmlresult);
            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");
            int jfxxindex = getErrFlagIndex(s, "jfxx");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                Object value = d.get("r");
                if (value instanceof org.json.JSONObject) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(value);
                    d.put("r", jsonArray);
                }
                JSONArray r = new JSONArray();
                try {
                    r = d.getJSONArray("r");
                } catch (Exception a) {
                }
                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < r.length(); i++) {
                    dataJsondata = new JSONObject();

                    dataJsondata.put("xxbz", r.getJSONObject(i).get("xzbz"));
                    dataJsondata.put("dwxzmc", r.getJSONObject(i).get("dwxzmc"));
                    dataJsondata.put("jflslbmc", r.getJSONObject(i).get("jflslbmc"));
                    dataJsondata.put("dwmc", r.getJSONObject(i).get("dwmc"));
                    dataJsondata.put("jbrq", r.getJSONObject(i).get("jbrq"));
                    dataJsondata.put("jflslb", r.getJSONObject(i).get("jflslb"));
                    dataJsondata.put("jfrq", r.getJSONObject(i).get("jfrq"));
                    dataJsondata.put("grjfjs", r.getJSONObject(i).get("grjfjs"));
                    dataJsondata.put("ny", r.getJSONObject(i).get("ny"));
                    dataJsondata.put("dwjfe", r.getJSONObject(i).get("dwjfe"));
                    dataJsondata.put("dwbh", r.getJSONObject(i).get("dwbh"));
                    dataJsondata.put("jfny", r.getJSONObject(i).get("jfny"));
                    dataJsondata.put("fsyymc", r.getJSONObject(i).get("fsyymc"));
                    dataJsondata.put("dwxz", r.getJSONObject(i).get("dwxz"));
                    dataJsondata.put("xzbzmc", r.getJSONObject(i).get("xzbzmc"));
                    dataJsondata.put("dwjfjs", r.getJSONObject(i).get("dwjfjs"));
                    dataJsondata.put("fsrq", r.getJSONObject(i).get("fsrq"));
                    dataJsondata.put("fsyy", r.getJSONObject(i).get("fsyy"));
                    dataJsondata.put("grjfe", r.getJSONObject(i).get("grjfe"));

                    list.add(dataJsondata);
                }
                dataJson.put("jfxx", s.getJSONObject(jfxxindex).get("jfxx").toString());
                dataJson.put("_lesb__text", list);

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 3 职工养老账户清单信息
     */
    @RequestMapping(value = "/zhigongyanglaocountlist", method = RequestMethod.POST)
    public String zhiGongYaoLaoCountList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String nf = obj.getString("nf");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getAccAgedList";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm
                    + "\"/><s rsxtid=\"3758\"/><s nf=\"" + nf + "\"/></p>";
            // //system.out.println(xmlPara);
            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                JSONArray d = p.getJSONArray("d");

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < d.length(); i++) {

                    if (d.getJSONObject(i).get("k").equals("r_account")) {
                        // 该元素r为数组
                        JSONArray r = d.getJSONObject(i).getJSONArray("r");
                        for (int a = 0; a < r.length(); a++) {
                            dataJsondata = new JSONObject();
                            dataJsondata.put("jfsj", r.getJSONObject(a).get("jfsj"));
                            dataJsondata.put("grlx", r.getJSONObject(a).get("grlx"));
                            dataJsondata.put("grjze", r.getJSONObject(a).get("grjze"));
                            dataJsondata.put("grjfjs", r.getJSONObject(a).get("grjfjs"));
                            dataJsondata.put("dwjfe", r.getJSONObject(a).get("dwjfe"));
                            dataJsondata.put("dwlx", r.getJSONObject(a).get("dwlx"));
                            dataJsondata.put("zsjze", r.getJSONObject(a).get("zsjze"));
                            dataJsondata.put("zdlsh", r.getJSONObject(a).get("zdlsh"));
                            dataJsondata.put("jfjs", r.getJSONObject(a).get("jfjs"));
                            dataJsondata.put("lxsum", r.getJSONObject(a).get("lxsum"));
                            dataJsondata.put("jfny", r.getJSONObject(a).get("jfny"));
                            dataJsondata.put("nf", r.getJSONObject(a).get("nf"));
                            dataJsondata.put("dwjze", r.getJSONObject(a).get("dwjze"));
                            dataJsondata.put("dwjfjs", r.getJSONObject(a).get("dwjfjs"));
                            dataJsondata.put("zslx", r.getJSONObject(a).get("zslx"));
                            dataJsondata.put("fsyy", r.getJSONObject(a).get("fsyy"));
                            dataJsondata.put("grjzze", r.getJSONObject(a).get("grjzze"));
                            list.add(dataJsondata);
                        }

                        dataJson.put("list", list);
                    } else if (d.getJSONObject(i).get("k").equals("r_sum")) {
                        // 该元素为JSONObjcect对象
                        org.json.JSONObject r = d.getJSONObject(i).getJSONObject("r");

                        dataJson.put("sndwlx", r.get("sndwlx"));
                        dataJson.put("h_dwjfe", r.get("h_dwjfe"));
                        dataJson.put("h_grjfjs", r.get("h_grjfjs"));
                        dataJson.put("h_dwjfjs", r.get("h_dwjfjs"));
                        dataJson.put("dnjfys", r.get("dnjfys"));
                        dataJson.put("dwlxhj", r.get("dwlxhj"));
                        dataJson.put("grlxhj", r.get("grlxhj"));
                        dataJson.put("snmgc", r.get("snmgc"));
                        dataJson.put("bnmgrjze", r.get("bnmgrjze"));
                        dataJson.put("dnjzebxdw", r.get("dnjzebxdw"));
                        dataJson.put("snmzsjze", r.get("snmzsjze"));
                        dataJson.put("snjfys", r.get("snjfys"));
                        dataJson.put("zrnjfys", r.get("zrnjfys"));
                        dataJson.put("dnbx", r.get("dnbx"));
                        dataJson.put("dnjzebx", r.get("dnjzebx"));
                        dataJson.put("snhjlx", r.get("snhjlx"));
                        dataJson.put("h_dwjze", r.get("h_dwjze"));
                        dataJson.put("dngr", r.get("dngr"));
                        dataJson.put("zsdnbzl", r.get("zsdnbzl"));
                        dataJson.put("grjfjshj", r.get("grjfjshj"));
                        dataJson.put("snzslx", r.get("snzslx"));
                        dataJson.put("dnll", r.get("dnll"));
                        dataJson.put("bnmgc", r.get("bnmgc"));
                        dataJson.put("h_lxsum", r.get("h_lxsum"));
                        dataJson.put("jfys", r.get("jfys"));
                        dataJson.put("sngrlx", r.get("sngrlx"));
                        dataJson.put("dwjzehj", r.get("dwjzehj"));
                        dataJson.put("dnzs", r.get("dnzs"));

                        dataJson.put("snmgrjze", r.get("snmgrjze"));
                        dataJson.put("zsdnll", r.get("zsdnll"));
                        dataJson.put("zslxhj", r.get("zslxhj"));
                        dataJson.put("dnjzebxgr", r.get("dnjzebxgr"));

                        dataJson.put("dndw", r.get("dndw"));
                        dataJson.put("h_zsjze", r.get("h_zsjze"));
                        dataJson.put("grjzehj", r.get("grjzehj"));
                        dataJson.put("h_grjze", r.get("h_grjze"));

                        dataJson.put("dwjfjshj", r.get("dwjfjshj"));
                        dataJson.put("bnmzsjze", r.get("bnmzsjze"));
                        dataJson.put("bnmdwjze", r.get("bnmdwjze"));
                        dataJson.put("h_grjzze", r.get("h_grjzze"));

                        dataJson.put("nf", r.get("nf"));
                        dataJson.put("zsjzehj", r.get("zsjzehj"));
                        dataJson.put("dwjfehj", r.get("dwjfehj"));
                        dataJson.put("snmdwjze", r.get("snmdwjze"));
                        dataJson.put("dnbzl", r.get("dnbzl"));

                    }

                }

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 4 职工工伤缴费汇总信息
     */
    @RequestMapping(value = "/zhigonggongshangjiaofeihz", method = RequestMethod.POST)
    public String zhiGongGongShangJiaoFeiHz(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayHarmTotal";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm
                    + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                org.json.JSONObject r = d.getJSONObject("r");

                dataJson.put("xzbz", r.get("xzbz"));
                dataJson.put("maxny", r.get("maxny"));
                dataJson.put("stjfys", r.get("stjfys"));
                dataJson.put("jfys", r.get("jfys"));
                dataJson.put("sjjfys", r.get("sjjfys"));
                dataJson.put("xzbzmc", r.get("xzbzmc"));
                dataJson.put("minny", r.get("minny"));
                dataJson.put("dwjfe", r.get("dwjfe"));
                dataJson.put("grjfe", r.get("grjfe"));
                dataJson.put("jmjfe", r.get("jmjfe"));

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 5 职工工伤缴费明细信息
     */
    @RequestMapping(value = "/zhigonggongshangjiaofeimx", method = RequestMethod.POST)
    public String zhiGongGongShangJiaoFeiMx(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");
            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayHarm";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s qsny=\""
                    + qsny + "\"/><s zzny=\"" + zzny + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                JSONArray r = new JSONArray();
                try {
                    r = d.getJSONArray("r");
                } catch (Exception a) {
                }

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < r.length(); i++) {
                    dataJsondata = new JSONObject();

                    dataJsondata.put("xzbz", r.getJSONObject(i).get("xzbz"));
                    dataJsondata.put("jflslbmc", r.getJSONObject(i).get("jflslbmc"));
                    dataJsondata.put("dwmc", r.getJSONObject(i).get("dwmc"));
                    dataJsondata.put("jflslb", r.getJSONObject(i).get("jflslb"));
                    dataJsondata.put("jfrq", r.getJSONObject(i).get("jfrq"));
                    dataJsondata.put("dwjfe", r.getJSONObject(i).get("dwjfe"));
                    dataJsondata.put("jfjs", r.getJSONObject(i).get("jfjs"));
                    dataJsondata.put("dwbh", r.getJSONObject(i).get("dwbh"));
                    dataJsondata.put("jfny", r.getJSONObject(i).get("jfny"));
                    dataJsondata.put("fsyymc", r.getJSONObject(i).get("fsyymc"));
                    dataJsondata.put("xzbzmc", r.getJSONObject(i).get("xzbzmc"));
                    dataJsondata.put("fsrq", r.getJSONObject(i).get("fsrq"));
                    dataJsondata.put("fsyy", r.getJSONObject(i).get("fsyy"));
                    dataJsondata.put("grjfe", r.getJSONObject(i).get("grjfe"));

                    list.add(dataJsondata);
                }

                dataJson.put("_lesb__text", list);

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }
            log.info("身份证：" + sfzhm + "返回值：" + dataJson);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 6、职工失业缴费汇总信息
     */
    @RequestMapping(value = "/zhigongshiyejiaofeihz", method = RequestMethod.POST)
    public String zhiGongShiYeJiaoFeiHz(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayLostTotal";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm
                    + "\"/><s rsxtid=\"3758\"/></p>";
            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                org.json.JSONObject r = d.getJSONObject("r");

                dataJson.put("xzbz", r.get("xzbz"));
                dataJson.put("maxny", r.get("maxny"));
                dataJson.put("stjfys", r.get("stjfys"));
                dataJson.put("jfys", r.get("jfys"));
                dataJson.put("sjjfys", r.get("sjjfys"));
                dataJson.put("xzbzmc", r.get("xzbzmc"));
                dataJson.put("minny", r.get("minny"));
                dataJson.put("dwjfe", r.get("dwjfe"));
                dataJson.put("grjfe", r.get("grjfe"));
                dataJson.put("jmjfe", r.get("jmjfe"));

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 7 职工失业缴费明细信息
     */
    @RequestMapping(value = "/zhigongshiyejiaofeimx", method = RequestMethod.POST)
    public String zhiGongShiYeJiaoFeiMx(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String qsny = obj.getString("qsny");
            String zzny = obj.getString("zzny");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getPayLost";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s qsny=\""
                    + qsny + "\"/><s zzny=\"" + zzny + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                JSONArray r = new JSONArray();
                try {
                    r = d.getJSONArray("r");
                } catch (Exception a) {

                }

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < r.length(); i++) {
                    dataJsondata = new JSONObject();

                    dataJsondata.put("xxbz", r.getJSONObject(i).get("xzbz"));
                    dataJsondata.put("jflslbmc", r.getJSONObject(i).get("jflslbmc"));
                    dataJsondata.put("dwmc", r.getJSONObject(i).get("dwmc"));
                    dataJsondata.put("jflslb", r.getJSONObject(i).get("jflslb"));
                    dataJsondata.put("jfrq", r.getJSONObject(i).get("jfrq"));
                    dataJsondata.put("dwjfe", r.getJSONObject(i).get("dwjfe"));
                    dataJsondata.put("jfjs", r.getJSONObject(i).get("jfjs"));
                    dataJsondata.put("dwbh", r.getJSONObject(i).get("dwbh"));
                    dataJsondata.put("jfny", r.getJSONObject(i).get("jfny"));
                    dataJsondata.put("fsyymc", r.getJSONObject(i).get("fsyymc"));
                    dataJsondata.put("xzbzmc", r.getJSONObject(i).get("xzbzmc"));
                    dataJsondata.put("fsrq", r.getJSONObject(i).get("fsrq"));
                    dataJsondata.put("fsyy", r.getJSONObject(i).get("fsyy"));
                    dataJsondata.put("grjfe", r.getJSONObject(i).get("grjfe"));

                    list.add(dataJsondata);
                }

                dataJson.put("_lesb__text", list);

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 8 职工养老待遇查询
     */
    @RequestMapping(value = "/zhigongyanglaodaiyu", method = RequestMethod.POST)
    public String zhiGongYangLaoDaiYu(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String ny = obj.getString("ny");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiServiceAged";
            String operationName = "getGivAged";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s ny=\"" + ny
                    + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");

            JSONArray s = p.getJSONArray("s");
            // system.out.println(s);
            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");
            int textindex = getErrFlagIndex(s, "ffje");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                dataJson.put("_lesb__text", s.getJSONObject(textindex).get("ffje"));
            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2-1 居民养老缴费明细信息
     */
    @RequestMapping(value = "/jumingyanglaojiaofeimx", method = RequestMethod.POST)
    public String juMingYangLaoJiaoFeiMx(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");

            String qsnf = obj.getString("qsnf");
            String zznf = obj.getString("zznf");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiUrService";
            String operationName = "getPayHisTotal";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s qsnf=\""
                    + qsnf + "\"/><s zznf=\"" + zznf + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "errflag");
            int errtextindex = getErrFlagIndex(s, "errtext");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("errflag").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject d = p.getJSONObject("d");
                JSONArray r = new JSONArray();
                try {
                    r = d.getJSONArray("r");
                } catch (Exception a) {

                }

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < r.length(); i++) {
                    dataJsondata = new JSONObject();

                    dataJsondata.put("jfxmbh", r.getJSONObject(i).get("jfxmbh"));
                    dataJsondata.put("jfxmmc", r.getJSONObject(i).get("jfxmmc"));
                    dataJsondata.put("zznf", r.getJSONObject(i).get("zznf"));
                    dataJsondata.put("jfrq", r.getJSONObject(i).get("jfrq"));
                    dataJsondata.put("jfxmje", r.getJSONObject(i).get("jfxmje"));
                    dataJsondata.put("qsnf", r.getJSONObject(i).get("qsnf"));
                    list.add(dataJsondata);
                }

                dataJson.put("_lesb__text", list);
            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("errtext"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2-2 居民养老账户信息
     */
    @RequestMapping(value = "/jumingyanglaocount", method = RequestMethod.POST)
    public String juMingYangLaoCount(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");
            String nd = obj.getString("nd");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiUrService";
            String operationName = "getAccAged";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s nd=\"" + nd
                    + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "_lesb__errcode_");
            int errtextindex = getErrFlagIndex(s, "_lesb__errcode_text");
            if (Integer.parseInt(s.getJSONObject(errflagindex).get("_lesb__errcode_").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                JSONArray d = p.getJSONArray("d");

                int rindex = getErrFlagIndex(d, "r");
                org.json.JSONObject r = d.getJSONObject(rindex);
                JSONArray sd = new JSONArray();
                try {
                    sd = r.getJSONArray("r");
                } catch (Exception a) {

                }

                JSONObject dataJsondata = new JSONObject();
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < sd.length(); i++) {
                    dataJsondata = new JSONObject();
                    dataJsondata.put("jfxmbh", sd.getJSONObject(i).get("jfxmbh"));
                    dataJsondata.put("jfxmmc", sd.getJSONObject(i).get("jfxmmc"));
                    dataJsondata.put("zhsslb", sd.getJSONObject(i).get("zhsslb"));
                    dataJsondata.put("je", sd.getJSONObject(i).get("je"));
                    dataJsondata.put("fsrq", sd.getJSONObject(i).get("fsrq"));
                    dataJsondata.put("lx", sd.getJSONObject(i).get("lx"));
                    list.add(dataJsondata);
                }

                dataJson.put("_lesb__text", list);
            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("_lesb__errcode_text"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 2-3 居民养老发放信息
     */
    @RequestMapping(value = "/jumingyanglaofafang", method = RequestMethod.POST)
    public String juMingYangLaoFaFang(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String sfzhm = obj.getString("sfzhm");
            String ny = obj.getString("ny");

            DwPspProxy.getInstance().setDwPspServerUrl("http://10.156.80.1:8080/dwpspserver");

            String serviceName = "SiUrService";
            String operationName = "getGivAged";
            String xmlPara = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>" + "<s sfzhm=\"" + sfzhm + "\"/><s ny=\"" + ny
                    + "\"/><s rsxtid=\"3758\"/></p>";

            String xmlresult = DwPspProxy.getInstance().invokeService(licenseKey, serviceName, operationName, xmlPara);

            JSONObject dataJson = new JSONObject();
            org.json.JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
            org.json.JSONObject p = xmlJSONObj.getJSONObject("p");
            JSONArray s = p.getJSONArray("s");

            // 先判断当前接口是否有问题
            int errflagindex = getErrFlagIndex(s, "_lesb__errcode_");

            int errtextindex = getErrFlagIndex(s, "_lesb__errcode_text");

            if (Integer.parseInt(s.getJSONObject(errflagindex).get("_lesb__errcode_").toString()) == 0) {
                // 无问题
                dataJson.put("errflag", 0);
                org.json.JSONObject r = p.getJSONObject("d");
                org.json.JSONObject rr = null;

                try {
                    rr = r.getJSONObject("r");
                    dataJson.put("btje", rr.get("btje").toString());
                    dataJson.put("btmc", rr.get("btmc").toString());
                    dataJson.put("btbh", rr.get("btbh").toString());
                } catch (Exception a) {
                    dataJson.put("btje", "");
                    dataJson.put("btmc", "");
                    dataJson.put("btbh", "");
                }

            } else {
                dataJson.put("errflag", 1);
                dataJson.put("_lesb__text", s.getJSONObject(errtextindex).get("_lesb__errcode_text"));
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    // 根据属性名称找Index
    int getErrFlagIndex(JSONArray data, String key) {

        for (int i = 0; i < data.length(); i++) {

            if (data.getJSONObject(i).keys().next().equals(key)) {
                return i;
            }
        }
        return 0;
    }

    public static org.json.JSONObject convert(String xmlString) {
        org.json.JSONObject jsonObject = XML.toJSONObject(xmlString);
        convertToJSONArray(jsonObject.getJSONObject("p"), "d");
        return jsonObject;
    }

    private static void convertToJSONArray(org.json.JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            Object value = jsonObject.get(key);
            if (value instanceof org.json.JSONObject) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(value);
                jsonObject.put(key, jsonArray);
            } else if (value instanceof JSONArray) {
                // Leave it as it is
            }
        }
    }

}
