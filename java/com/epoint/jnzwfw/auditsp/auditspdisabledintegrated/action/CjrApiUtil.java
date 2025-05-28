package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;

/**
 * 残联接口调用工具
 * 
 * @author ：lionzz
 * @date ：Created in 2021/4/12 15:56
 * @description：
 * @modified By：
 * @version:
 */
public class CjrApiUtil
{

    private static final transient Logger log = Logger.getLogger(BaseController.class);

    /**
     * 缓存，登录成功时存入
     */
    private static final HashMap<String, CjrApiUtil> cache = new HashMap<>();

    /**
     * 用户名
     */
    final String username;
    /**
     * 密码
     */
    final String password;
    /**
     * 验证码
     */
    final String verycode;
    /**
     * 短信验证码
     */
    final String dx_verifcode;

    final String apiSession;

    final String apikey;
    /**
     * 登录成功后的userId - 方便后续接口使用
     */
    String userId;

    UserSession userSession = UserSession.getInstance();

    /**
     * 构造方法
     * 
     * @param username
     * @param password
     * @param verycode
     * @param dx_verifcode
     */

    public CjrApiUtil(String username, String password, String verycode, String dx_verifcode) {
        this.username = username;
        this.password = password;
        this.verycode = verycode;
        this.dx_verifcode = dx_verifcode;
        this.apiSession = userSession.getUserGuid();
        this.apikey = userSession.getUserGuid();
    }

    /**
     * 获取缓存
     * 
     * @param userGuid
     * @return
     */
    public static CjrApiUtil getInstance(String userGuid) {
        return cache.get(userGuid);
    }

    /**
     * 获取验证码
     * http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=8a2f2e0b-4379-4491-8fc6-922671bb79b2&iw-cmd=verifycode
     * http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=8a2f2e0b-4379-4491-8fc6-922671bb79b2&iw-cmd=login&username=370828197110020674&password=l8758069*&verifcode=82SR&dx_verifcode=
     * 
     * @return
     */
    public static String getVeryCode() {
        String apikey = UserSession.getInstance().getUserGuid();
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=verifycode";
        log.info("调用残联验证码接口:" + url);
        try (InputStream in = HttpUtil.doHttp(url, null, "get", HttpUtil.RTN_TYPE_INPUTSTREAM);
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            return Base64Util.encode(out.toByteArray());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 登录
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public JSONObject login() throws UnsupportedEncodingException {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + URLEncoder.encode(apikey, "UTF-8")
                + "&iw-cmd=login&username=" + URLEncoder.encode(username, "UTF-8") + "&password="
                + URLEncoder.encode(password, "UTF-8") + "&verifycode=" + URLEncoder.encode(verycode, "UTF-8");
        log.info("登录接口:" + url);
        String result = HttpUtil.doGet(url);
        log.info("登录结果:" + result);
        JSONObject resultObj = JSON.parseObject(result);
        String rtnCode = resultObj.getString("rtnCode");
        // 1.2、如果登录成功
        if ("000000".equals(rtnCode)) {
            String userId = resultObj.getJSONObject("data").getString("userId");
            this.setUserId(userId);
            cache.put(apikey, this);
            log.info("登录存储信息为：" + cache + ",apikey：" + apikey);
            resultObj.put("issuccess", true);
        }
        return resultObj;
    }

    /**
     * 登录
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public JSONObject Smslogin(String verycode) throws UnsupportedEncodingException {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey="
                + URLEncoder.encode(UserSession.getInstance().getUserGuid(), "UTF-8")
                + "&iw-cmd=dx_login&dx_verifycode=" + URLEncoder.encode(verycode, "UTF-8");
        log.info("短信登录接口:" + url);
        String result = HttpUtil.doGet(url);
        log.info("短信登录结果:" + result);
        JSONObject resultObj = JSON.parseObject(result);
        String rtnCode = resultObj.getString("rtnCode");
        // 1.2、如果登录成功
        if ("000000".equals(rtnCode)) {
            String userId = resultObj.getJSONObject("data").getString("userId");
            this.setUserId(userId);
            cache.put(apikey, this);
            log.info("登录存储信息为：" + cache + ",apikey：" + apikey);
            resultObj.put("issuccess", "true");
        }
        return resultObj;
    }

    /**
     * 发送短信
     *
     * @return
     */
    public static JSONObject sendSms() {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + UserSession.getInstance().getUserGuid()
                + "&iw-cmd=dx_verifycode";
        log.info("发送短信接口:" + url);
        String result = HttpUtil.doGet(url);
        return JSON.parseObject(result);
    }

    /**
     * 受理提交
     *
     * @param dataBean
     * @return
     * @throws UnsupportedEncodingException
     */
    public String acceptSubmit(AuditYjsCjr dataBean) throws UnsupportedEncodingException {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        // 1、获取受理首页信息
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=new_getCreateMain";
        log.info("获取受理首页信息：" + url);
        String result = HttpUtil.doGet(url);
        log.info("获取受理首页信息结果：" + result);
        JSONObject resultObj = JSON.parseObject(result);
        if ("000000".equals(resultObj.getString("rtnCode"))) {
            JSONObject data = resultObj.getJSONObject("data");
            String version_id = data.getString("version_id");
            String dealtype = data.getString("dealtype");
            String dataId = data.getString("dataId");
            String bj_id = data.getString("bj_id");
            String save_path = data.getString("save_path");
            String tran_id = data.getString("tran_id");

            // 2、调用身份验证接口
            String checkIdUrl = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey
                    + "&iw-cmd=checkIdcardAndGetPhoto&name=" + dataBean.getName() + "&idcard="
                    + dataBean.getIdcard().toUpperCase() + "&version_id=" + version_id;
            log.info("身份验证:" + checkIdUrl);

            try {
                result = HttpUtil.doGet(checkIdUrl);
            }
            catch (Exception e) {
                log.info("调用身份验证接口失败");
                e.printStackTrace();
            }

            log.info("身份验证结果：" + result);
            JSONObject checkResult = JSON.parseObject(result);
            if ("000000".equals(checkResult.getString("rtnCode"))) {
                JSONObject data2 = checkResult.getJSONObject("data");
                if ("false".equals(data2.getString("flag"))) {
                    log.info("该申请人已经持证，apikey：" + apikey + data2.getString("msg"));
                    return data2.getString("msg");
                }

                // 3、调用照片上传接口
                IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo()
                        .getComponent(IAuditSpInstance.class);
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(dataBean.getRowguid())
                        .getResult();
                if (auditSpInstance != null) {
                    IAuditSpIMaterial auditSpIMaterial = ContainerFactory.getContainInfo()
                            .getComponent(IAuditSpIMaterial.class);
                    SqlConditionUtil sqlCondition = new SqlConditionUtil();
                    sqlCondition.eq("biguid", auditSpInstance.getRowguid());
                    sqlCondition.eq("materialname", "2寸免冠白底彩色照片");
                    List<AuditSpIMaterial> materialList = auditSpIMaterial
                            .getSpIMaterialByCondition(sqlCondition.getMap()).getResult();
                    if (materialList != null && !materialList.isEmpty()) {
                        AuditSpIMaterial spIMaterial = materialList.get(0);
                        String cliengGuid = spIMaterial.getCliengguid();
                        IAttachService attachService = ContainerFactory.getContainInfo()
                                .getComponent(IAttachService.class);
                        List<FrameAttachStorage> attachListByGuid = attachService.getAttachListByGuid(cliengGuid);
                        if (attachListByGuid != null && !attachListByGuid.isEmpty()) {
                            String uploadUrl = clUrl + "/qgclxinxihuafuwupingtai/new_zpUpload";
                            log.info("照片上传:" + uploadUrl);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("iw-apikey", apikey);
                            map.put("object_id", version_id);
                            // map.put("APISession", apikey);
                            FrameAttachStorage attachStorage = attachListByGuid.get(0);
                            try {
                                InputStream content = attachStorage.getContent();
                                byte[] bytes = IOUtils.toByteArray(content);
                                String imgBase64 = Base64Util.encode(bytes);
                                map.put("fileName", attachStorage.getAttachFileName().split("\\.")[0]);
                                map.put("imgBase64", imgBase64);
                                String json = JSON.toJSONString(map);
                                result = HttpUtil.doPostJson(uploadUrl, json);
                                log.info("照片上传结果：" + result);
                            }
                            catch (IOException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }

                /*
                 * JSONObject uploadResult = JSON.parseObject(result);
                 * if ("000000".equals(uploadResult.getString("rtnCode"))) {
                 * JSONObject uploadData = uploadResult.getJSONObject("data");
                 * save_path = uploadData.getString("save_path");
                 * }
                 * if (save_path == null) {
                 * save_path = "";
                 * }
                 */
                String source = checkResult.getJSONObject("data").getString("source");
                StringBuilder submitUrl = new StringBuilder(
                        clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=new_sltj");
                submitUrl.append("&dataId=").append(URLEncoder.encode(dataId, "UTF-8"));
                submitUrl.append("&tran_id=").append(URLEncoder.encode(tran_id, "UTF-8"));
                submitUrl.append("&bj_id=").append(URLEncoder.encode(bj_id, "UTF-8"));
                submitUrl.append("&idcard=").append(URLEncoder.encode(dataBean.getIdcard().toUpperCase(), "UTF-8"));
                submitUrl.append("&nation=").append(URLEncoder.encode(dataBean.getNation(), "UTF-8"));
                submitUrl.append("&brith_time=").append(EpointDateUtil.convertDate2String(dataBean.getBrith_time()));
                submitUrl.append("&con_phone=").append(URLEncoder.encode(dataBean.getCon_phone(), "UTF-8"));
                submitUrl.append("&con_tel=").append(URLEncoder.encode(dataBean.getCon_tel(), "UTF-8"));
                submitUrl.append("&residence_area=").append(URLEncoder.encode(dataBean.getResidence_area(), "UTF-8"));
                submitUrl.append("&guard_name=").append(URLEncoder.encode(dataBean.getGuard_name(), "UTF-8"));
                submitUrl.append("&guard_phone=").append(URLEncoder.encode(dataBean.getGuard_phone(), "UTF-8"));
                submitUrl.append("&nodeId=").append(URLEncoder.encode(dataBean.getBelong_village(), "UTF-8"));
                submitUrl.append("&education=").append(URLEncoder.encode(dataBean.getEducation(), "UTF-8"));
                submitUrl.append("&save_path=").append(save_path);
                submitUrl.append("&marital=").append(URLEncoder.encode(dataBean.getMarital(), "UTF-8"));
                submitUrl.append("&name=").append(URLEncoder.encode(dataBean.getName(), "UTF-8"));
                submitUrl.append("&residence=").append(URLEncoder.encode(dataBean.getResidence(), "UTF-8"));
                submitUrl.append("&version_id=").append(version_id);
                submitUrl.append("&dealtype=").append(dealtype);
                ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                        .getComponent(ICodeItemsService.class);
                String district = codeItemsService.getItemTextByCodeName("残联一件事-行政区域", dataBean.getBelong_district());
                String town = codeItemsService.getItemTextByCodeName("残联一件事-行政区域", dataBean.getBelong_town());
                String village = codeItemsService.getItemTextByCodeName("残联一件事-行政区域", dataBean.getBelong_village());
                String domicile_area = district + town + village;
                submitUrl.append("&domicile_area=").append(URLEncoder.encode(domicile_area, "UTF-8"));
                submitUrl.append("&sources=").append(URLEncoder.encode(source, "UTF-8"));
                submitUrl.append("&deformity_type=").append(URLEncoder.encode(dataBean.getDeformity_type(), "UTF-8"));
                submitUrl.append("&sex=").append(URLEncoder.encode(dataBean.getSex(), "UTF-8"));
                submitUrl.append("&relation=").append(URLEncoder.encode(dataBean.getRelation(), "UTF-8"));
                submitUrl.append("&guard_contelphone=")
                        .append(URLEncoder.encode(dataBean.getGuard_contelphone(), "UTF-8"));
                // submitUrl.append("&APISession=").append(apiSession);
                log.info("受理提交" + submitUrl);
                result = HttpUtil.doGet(submitUrl.toString());
                log.info("受理提交结果：" + result);
                JSONObject finalResult = JSON.parseObject(result);
                if ("000000".equals(finalResult.getString("rtnCode"))) {
                    return "success";
                }
            }
        }
        return "受理提交接口调用失败！";
    }

    public void setUserId(String userId) {
        List<JSONObject> objects = new ArrayList<>();
        objects.stream().sorted(Comparator.comparing(a -> a.getInteger("value"), Comparator.reverseOrder()))
                .collect(Collectors.toList());
        this.userId = userId;
    }

    /**
     * 获取残疾人基本信息
     *
     * @param name
     * @param idcard
     * @return
     */
    public JSONObject getCjrglInfo(String name, String idcard, String nodeId) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=cjrglList&userId=" + userId
                + "&page=1&pageNum=1&nodeId=" + nodeId + "&name=" + name + "&idcard=" + idcard.toUpperCase()
                + "&domicile_area=&state=";
        log.info("查询残疾人记录:" + url);
        String result = "";
        result = HttpUtil.doGet(url);
        if (StringUtil.isNotBlank(result)) {
            JSONObject resultObj = JSON.parseObject(result);
            if (resultObj != null && "000000".equals(resultObj.getString("rtnCode"))) {
                JSONObject data = resultObj.getJSONObject("data");
                String total = data.getString("total");
                // 正常要么没有，要么查询出来一条
                if ("1".equals(total)) {
                    JSONArray list = data.getJSONArray("list");
                    String cjrid = list.getJSONObject(0).getString("id");
                    url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=getCjrInfo&userId="
                            + userId + "&cjrId=" + cjrid;
                    log.info("查询残疾人基本信息" + url);
                    result = HttpUtil.doGet(url);
                    log.info("残疾人信息返回结果：" + result);
                    if (StringUtil.isNotBlank(result)) {
                        return JSON.parseObject(result);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断是否登录成功
     * 
     * @return
     */
    public boolean isLoginSuccess() {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String clUrl = configService.getFrameConfigValue("残联地址");
        // 1、获取受理首页信息
        String url = clUrl + "/qgclxinxihuafuwupingtai/?iw-apikey=" + apikey + "&iw-cmd=new_getCreateMain";
        log.info("获取受理首页信息" + url);
        String result = HttpUtil.doGet(url);
        log.info("获取受理首页登录结果" + result);
        JSONObject resultObj = JSON.parseObject(result);
        return "000000".equals(resultObj.getString("rtnCode"));
    }
}
