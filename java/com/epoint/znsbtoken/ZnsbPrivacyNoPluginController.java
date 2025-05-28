package com.epoint.znsbtoken;

import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.security.TokenUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

@RestController
@RequestMapping("/znsbprivacynoplugin")
public class ZnsbPrivacyNoPluginController
{
    @Autowired
    private ICodeItemsService iCodeItemsService;
    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(value = "/createToken", method = RequestMethod.POST)
    public String CreateToken(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = json.getJSONObject("params");
            if (obj != null) {
                String appkey = ConfigUtil.getConfigValue("AppKey").split(";")[1];
                String appsecret = ConfigUtil.getConfigValue("AppSecret").split(";")[1];
                HttpSession session = request.getSession();
                String sid = session.getId();
                String macaddress = obj.getString("macaddress");
                // 创建token
                JSONObject tokenjson = new JSONObject();
                String token = TokenUtils.createTokenByAppSecret(appkey, appsecret);
                // 将token和加密mac放到session中
                session.setAttribute("token." + sid, token);
                session.setAttribute("macaddress." + sid, macaddress);
                tokenjson.put("token", token);
                return JsonUtils.zwdtRestReturn("1", "创建token成功！", tokenjson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请输入参数！", "");
            }
        }
        catch (Exception e) {
            log.error("CreateToken异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "CreateToken接口出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     * [解密]
     * 
     * @param str
     * @return
     */
    public String AESDecrypt(String str) {
        try {
            String key = "9c9b76e0d3f8a2c5";
            byte[] cipherTextBytes = Base64.getDecoder().decode(str);
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] iv = new byte[16];
            System.arraycopy(cipherTextBytes, 0, iv, 0, 16);
            byte[] encrypted = new byte[cipherTextBytes.length - 16];
            System.arraycopy(cipherTextBytes, 16, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv));
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, "UTF-8");
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "接口调用发生异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getGatewayAccess", method = RequestMethod.POST)
    public String getGatewayAccess(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = json.getJSONObject("params");
            if (obj != null) {
                String appkey = obj.getString("appkey");
                String appsecret = obj.getString("appsecret");
                String idcard = obj.getString("idcard");
                String sid = obj.getString("sid");
                String mac = obj.getString("mac");
                if (StringUtil.isBlank(appkey) || StringUtil.isBlank(appsecret)) {
                    return JsonUtils.zwdtRestReturn("0", "参数不能含有空！", "");
                }
                List<CodeItems> itemlist = iCodeItemsService.listCodeItemsByCodeName("接口租户配置");
                if (itemlist != null && !itemlist.isEmpty()) {
                    for (CodeItems codeItems : itemlist) {
                        if (appkey.equals(codeItems.getItemText()) && appsecret.equals(codeItems.getItemValue())) {
                            // 授权存在
                            String ak = codeItems.getDmAbr1();
                            String as = codeItems.getDmAbr2();
                            if (StringUtil.isBlank(ak) || StringUtil.isBlank(as)) {
                                return JsonUtils.zwdtRestReturn("0", "appkey,appsecret授权参数缺失", "");
                            }
                            // 进行参数加密
                            JSONObject tokenjson = new JSONObject();
                            if (StringUtil.isNotBlank(idcard)) {
                                tokenjson.put("idcard", AESEncrypt(idcard));
                            }
                            if (StringUtil.isNotBlank(sid)) {
                                tokenjson.put("sid", AESEncrypt(sid));
                            }
                            if (StringUtil.isNotBlank(mac)) {
                                tokenjson.put("mac", AESEncrypt(mac));
                            }
                            tokenjson.put("appkey", AESEncrypt(ak));
                            tokenjson.put("appsecret", AESEncrypt(appsecret));
                            // 创建token
                            String createToken = TokenUtils.createTokenByAppSecret(ak, as);
                            tokenjson.put("token", createToken);
                            return JsonUtils.zwdtRestReturn("1", "", tokenjson);
                        }
                    }
                    return JsonUtils.zwdtRestReturn("0", "appkey,appsecret不正确", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "接口租户配置未配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请输入参数！", "");
            }
        }
        catch (Exception e) {
            log.error("CreateToken异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "CreateToken接口出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * AES加密
     * 
     * @return
     */
    public String AESEncrypt(String content) {
        try {
            String key = "9c9b76e0d3f8a2c5";
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] plaintextBytes = content.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] cipherTextBytes = cipher.doFinal(plaintextBytes);

            byte[] combinedBytes = new byte[ivBytes.length + cipherTextBytes.length];
            System.arraycopy(ivBytes, 0, combinedBytes, 0, ivBytes.length);
            System.arraycopy(cipherTextBytes, 0, combinedBytes, ivBytes.length, cipherTextBytes.length);

            return Base64.getEncoder().encodeToString(combinedBytes);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "接口调用发生异常：" + e.getMessage(), "");
        }
    }

}
