
package com.epoint.xmz.shuilu;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;

@RestController
@RequestMapping("/jnsl")
public class JnSlLegalController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ICertHwslysjyxkService iCertHwslysjyxkService;

    /**
     * 获取办件结果
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getSlLegalDetail", method = RequestMethod.POST)
    public String getSlLegalDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getSlLegalDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getProjectReuslt入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            String xkzbh = jsonObject.getString("xkzbh");
            String qymc = jsonObject.getString("qymc");

            CertHwslysjyxk hwslysjyxk = iCertHwslysjyxkService.getCertByCertno(xkzbh, qymc);

            if (hwslysjyxk == null) {
                return JsonUtils.zwdtRestReturn("0", "法人信息为空", "");
            }

            dataJson.put("fddbr", hwslysjyxk.getStr("fddbr"));
            dataJson.put("jjlx", hwslysjyxk.getStr("qyxz"));
            dataJson.put("nhyh", hwslysjyxk.getStr("nhyh"));
            dataJson.put("jyqx", hwslysjyxk.getStr("jyqx"));
            dataJson.put("ksrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getKsrq(), EpointDateUtil.DATE_TIME_FORMAT));
            dataJson.put("jzrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getJzrq(), EpointDateUtil.DATE_TIME_FORMAT));
            dataJson.put("xkzbh", hwslysjyxk.getStr("bh"));
            dataJson.put("jyqy", hwslysjyxk.getStr("jyqy"));
            // dataJson.put("qyzslx", hwslysjyxk.getStr("qyzslx"));
            dataJson.put("qyzwmc", hwslysjyxk.getStr("jyzmc"));
            dataJson.put("qydz", hwslysjyxk.getStr("jyrdz"));
            dataJson.put("fzjg", hwslysjyxk.getStr("fzjg"));
            dataJson.put("fzrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getFzrq(), EpointDateUtil.DATE_TIME_FORMAT));
            dataJson.put("pzjgjwh", hwslysjyxk.getStr("pzjg"));
            dataJson.put("jiany", hwslysjyxk.getStr("jyfw"));
            dataJson.put("lkys", hwslysjyxk.getStr("kyjyfw"));
            dataJson.put("hwys", hwslysjyxk.getStr("hyjyfw"));

            return JsonUtils.zwdtRestReturn("1", "获取法人信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSlLegalDetail接口参数：params【" + params + "】=======");
            log.info("=======getSlLegalDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询法人库信息结果失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getYyysCertDetail", method = RequestMethod.POST)
    public String getYyysCertDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getYyysCertDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getYyysCertDetail入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            String xkzbh = jsonObject.getString("xkzbh");

            CertCbyyysz hwslysjyxk = iCertHwslysjyxkService.getYyysCertByCertno(xkzbh);

            if (hwslysjyxk == null) {
                return JsonUtils.zwdtRestReturn("0", "法人信息为空", "");
            }

            // 本船经营范围
            dataJson.put("bcjyfw", hwslysjyxk.getStr("bcjyfw"));
            // 船舶长度
            dataJson.put("cbcd", hwslysjyxk.getStr("cbcd"));
            // 船舶材料
            dataJson.put("cbcl", hwslysjyxk.getStr("cbcl"));
            // 船舶登记号
            dataJson.put("cbdjh", hwslysjyxk.getStr("cbdjh"));
            // 船舶管理人
            dataJson.put("cbglr", hwslysjyxk.getStr("cbglr"));
            // 船舶经营人
            dataJson.put("cbjyr", hwslysjyxk.getStr("cbjyr"));
            // 船舶来源
            dataJson.put("cbly", hwslysjyxk.getStr("cbly"));
            // 船舶所有人
            dataJson.put("cbsyr", hwslysjyxk.getStr("cbsyr"));
            // 船舶型宽
            dataJson.put("cbxk", hwslysjyxk.getStr("cbxk"));
            // 船舶型深
            dataJson.put("cbxs", hwslysjyxk.getStr("cbxs"));
            // 船舶种类
            dataJson.put("cbzl", hwslysjyxk.getStr("cbzl"));
            // 船检登记号
            dataJson.put("cjdjh", hwslysjyxk.getStr("cjdjh"));
            // 船籍港
            dataJson.put("cjg", hwslysjyxk.getStr("cjg"));
            // 车位
            dataJson.put("cw", hwslysjyxk.getStr("cw"));
            // 曾用名
            dataJson.put("cym", hwslysjyxk.getStr("cym"));
            // 发证机构
            dataJson.put("fzjg", hwslysjyxk.getStr("fzjg"));
            // 发证日期
            dataJson.put("fzrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("fzrq"), EpointDateUtil.DATE_FORMAT));
            // 改建日期
            dataJson.put("gjrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("gjrq"), EpointDateUtil.DATE_FORMAT));
            // 管理许可证编号
            dataJson.put("glxkzbh", hwslysjyxk.getStr("glxkzbh"));
            // 建成日期
            dataJson.put("jcrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("jcrq"), EpointDateUtil.DATE_FORMAT));
            // 净吨
            dataJson.put("jd", hwslysjyxk.getStr("jd"));
            // 经营区域
            dataJson.put("jyfw", hwslysjyxk.getStr("jyfw"));
            // 经营许可证编号
            dataJson.put("jyxkzbh", hwslysjyxk.getStr("jyxkzbh"));
            // 客位
            dataJson.put("kw", hwslysjyxk.getStr("kw"));
            // 立方米
            dataJson.put("lfm", hwslysjyxk.getStr("lfm"));
            // 内河沿海
            dataJson.put("nhyh", hwslysjyxk.getStr("nhyh"));
            // 是否标准船型
            dataJson.put("sfbzcx", hwslysjyxk.getStr("sfbzcx"));
            // 是否自有船舶
            dataJson.put("sfzycb", hwslysjyxk.getStr("sfzycb"));
            // TEU(标箱)
            dataJson.put("teu", hwslysjyxk.getStr("teu"));
            // 相关证书号
            dataJson.put("xgzsh", hwslysjyxk.getStr("xgzsh"));
            // 许可证经营范围
            dataJson.put("xkzjyfw", hwslysjyxk.getStr("xkzjyfw"));
            // 有效标志
            dataJson.put("yxbz", hwslysjyxk.getStr("yxbz"));
            // 有效日期
            dataJson.put("yxrq", hwslysjyxk.getStr("yxrq"));
            // 营运证编号
            dataJson.put("yyzbh", hwslysjyxk.getStr("yyzbh"));
            // 主机台数
            dataJson.put("zcts", hwslysjyxk.getStr("zcts"));
            // 总吨
            dataJson.put("zd", hwslysjyxk.getStr("zd"));
            // 主机功率(千瓦)
            dataJson.put("zjgl", hwslysjyxk.getStr("zjgl"));
            // 中文船名
            dataJson.put("zwcm", hwslysjyxk.getStr("zwcm"));
            // 主运货类
            dataJson.put("zyhl", hwslysjyxk.getStr("zyhl"));
            // 载重吨
            dataJson.put("zzd", hwslysjyxk.getStr("zzd"));

            return JsonUtils.zwdtRestReturn("1", "获取法人信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYyysCertDetail接口参数：params【" + params + "】=======");
            log.info("=======getYyysCertDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询法人库信息结果失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getYyysZxCertDetail", method = RequestMethod.POST)
    public String getYyysZxCertDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getYyysZxCertDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getYyysCertDetail入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            String xkzbh = jsonObject.getString("xkzbh");

            CertCbyyysz hwslysjyxk = iCertHwslysjyxkService.getYyysZxCertByCertno(xkzbh);

            if (hwslysjyxk == null) {
                return JsonUtils.zwdtRestReturn("0", "法人信息为空", "");
            }

            // 本船经营范围
            dataJson.put("bcjyfw", hwslysjyxk.getStr("bcjyfw"));
            // 船舶长度
            dataJson.put("cbcd", hwslysjyxk.getStr("cbcd"));
            // 船舶材料
            dataJson.put("cbcl", hwslysjyxk.getStr("cbcl"));
            // 船舶登记号
            dataJson.put("cbdjh", hwslysjyxk.getStr("cbdjh"));
            // 船舶管理人
            dataJson.put("cbglr", hwslysjyxk.getStr("cbglr"));
            // 船舶经营人
            dataJson.put("cbjyr", hwslysjyxk.getStr("cbjyr"));
            // 船舶来源
            dataJson.put("cbly", hwslysjyxk.getStr("cbly"));
            // 船舶所有人
            dataJson.put("cbsyr", hwslysjyxk.getStr("cbsyr"));
            // 船舶型宽
            dataJson.put("cbxk", hwslysjyxk.getStr("cbxk"));
            // 船舶型深
            dataJson.put("cbxs", hwslysjyxk.getStr("cbxs"));
            // 船舶种类
            dataJson.put("cbzl", hwslysjyxk.getStr("cbzl"));
            // 船检登记号
            dataJson.put("cjdjh", hwslysjyxk.getStr("cjdjh"));
            // 船籍港
            dataJson.put("cjg", hwslysjyxk.getStr("cjg"));
            // 车位
            dataJson.put("cw", hwslysjyxk.getStr("cw"));
            // 曾用名
            dataJson.put("cym", hwslysjyxk.getStr("cym"));
            // 发证机构
            dataJson.put("fzjg", hwslysjyxk.getStr("fzjg"));
            // 发证日期
            dataJson.put("fzrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("fzrq"), EpointDateUtil.DATE_FORMAT));
            // 改建日期
            dataJson.put("gjrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("gjrq"), EpointDateUtil.DATE_FORMAT));
            // 管理许可证编号
            dataJson.put("glxkzbh", hwslysjyxk.getStr("glxkzbh"));
            // 建成日期
            dataJson.put("jcrq",
                    EpointDateUtil.convertDate2String(hwslysjyxk.getDate("jcrq"), EpointDateUtil.DATE_FORMAT));
            // 净吨
            dataJson.put("jd", hwslysjyxk.getStr("jd"));
            // 经营区域
            dataJson.put("jyfw", hwslysjyxk.getStr("jyfw"));
            // 经营许可证编号
            dataJson.put("jyxkzbh", hwslysjyxk.getStr("jyxkzbh"));
            // 客位
            dataJson.put("kw", hwslysjyxk.getStr("kw"));
            // 立方米
            dataJson.put("lfm", hwslysjyxk.getStr("lfm"));
            // 内河沿海
            dataJson.put("nhyh", hwslysjyxk.getStr("nhyh"));
            // 是否标准船型
            dataJson.put("sfbzcx", hwslysjyxk.getStr("sfbzcx"));
            // 是否自有船舶
            dataJson.put("sfzycb", hwslysjyxk.getStr("sfzycb"));
            // TEU(标箱)
            dataJson.put("teu", hwslysjyxk.getStr("teu"));
            // 相关证书号
            dataJson.put("xgzsh", hwslysjyxk.getStr("xgzsh"));
            // 许可证经营范围
            dataJson.put("xkzjyfw", hwslysjyxk.getStr("xkzjyfw"));
            // 有效标志
            dataJson.put("yxbz", hwslysjyxk.getStr("yxbz"));
            // 有效日期
            dataJson.put("yxrq", hwslysjyxk.getStr("yxrq"));
            // 营运证编号
            dataJson.put("yyzbh", hwslysjyxk.getStr("yyzbh"));
            // 主机台数
            dataJson.put("zcts", hwslysjyxk.getStr("zcts"));
            // 总吨
            dataJson.put("zd", hwslysjyxk.getStr("zd"));
            // 主机功率(千瓦)
            dataJson.put("zjgl", hwslysjyxk.getStr("zjgl"));
            // 中文船名
            dataJson.put("zwcm", hwslysjyxk.getStr("zwcm"));
            // 主运货类
            dataJson.put("zyhl", hwslysjyxk.getStr("zyhl"));
            // 载重吨
            dataJson.put("zzd", hwslysjyxk.getStr("zzd"));

            return JsonUtils.zwdtRestReturn("1", "获取法人信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYyysZxCertDetail接口参数：params【" + params + "】=======");
            log.info("=======getYyysZxCertDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询法人库信息结果失败：" + e.getMessage(), "");
        }
    }

    /**
     * 
     * [从本地库（港航水路运输经营许可证）中获取许可证信息渲染到表单中]
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getGhslysjyxkzCertDetail", method = RequestMethod.POST)
    public String getGhslysjyxkzCertDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getGhslysjyxkzCertDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getGhslysjyxkzCertDetail入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            String zzbh = jsonObject.getString("xkzbh");

            CertHwslysjyxk certHwslysjyxk = iCertHwslysjyxkService.getGhslysCertByCertJyxkzbh(zzbh);

            if (certHwslysjyxk == null) {
                return JsonUtils.zwdtRestReturn("0", "对应港航水路运输经营许可证信息为空", "");
            }

            // 原企业名称
            dataJson.put("yqymc", certHwslysjyxk.getStr("jyzmc"));
            // 原地址
            dataJson.put("ydz", certHwslysjyxk.getStr("jyrdz"));
            // 原法定代表人
            dataJson.put("yfddbr", certHwslysjyxk.getStr("fddbr"));
            // 原经济类型
            dataJson.put("yjjlx", certHwslysjyxk.getStr("jyfs"));
            // 原旅客运输
            dataJson.put("ylkys", certHwslysjyxk.getStr("kyjyfw"));
            // 原货物运输
            dataJson.put("yhwys", certHwslysjyxk.getStr("hyjyfw"));
            // 原兼营
            dataJson.put("yjy", certHwslysjyxk.getStr("jyfw"));

            return JsonUtils.zwdtRestReturn("1", "获取港航水路运输经营许可证信息成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getGhslysjyxkzCertDetail接口参数：params【" + params + "】=======");
            log.info("=======getGhslysjyxkzCertDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询港航水路运输经营许可证信息结果失败：" + e.getMessage(), "");
        }
    }

}
