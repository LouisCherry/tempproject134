package com.epoint.common.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.authentication.UserSession;
import com.epoint.core.utils.code.DESEncrypt;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

public class AppletOfficeWebUrlEncryptUtil
{
    public static String getEncryptUrl(String downloadUrl) {
        Object userDispName = UserSession.getInstance().getDisplayName();
        Object ouName = UserSession.getInstance().getOuName();
        // 是否开启附件预览furl地址加密功能，若要使用水印功能必须开启加密，否则无效,可选值 true/false, 默认true
        String enableEncryptStr = ConfigUtil.getConfigValue("uploadpreview.encrypt.enable");
        if ("false".equals(enableEncryptStr)) {
            downloadUrl="furl="+downloadUrl;
            return downloadUrl;
        }
        // 配置附件预览加密的key值，向量IV值 默认88888888/66666666
        String desKey = ConfigUtil.getConfigValue("uploadpreview.encrypt.key");
        String desIv = ConfigUtil.getConfigValue("uploadpreview.encrypt.iv");
        if (StringUtil.isBlank(desKey)) {
            desKey = "88888888";
        }
        if (StringUtil.isBlank(desIv)) {
            desIv = "66666666";
        }
        // 预览带的token有效期设置，默认300
        int timeOutExt = 300;
        String timeOutExtStr = ConfigUtil.getConfigValue("uploadpreview.token.timeoutext");
        if (StringUtil.isNotBlank(timeOutExtStr)) {
            timeOutExt = Integer.parseInt(timeOutExtStr);
        }
        DESEncrypt tools = null;
        String result = "";
        try {
            tools = new DESEncrypt(desKey, desIv);
            // 配置附件预览水印的模板序号，需要提前在web365服务端配置好，序号是数字;配置为none为不开启水印， 如果为空默认序号为1
            String tpNo = ConfigUtil.getConfigValue("uploadpreview.tp.no");
            // 如果none表示不开启水印
            if (!EpointKeyNames9.NONE_CHAR.equalsIgnoreCase(tpNo)) {
                tpNo = StringUtil.isBlank(tpNo) ? "1" : tpNo;
                // 配置附件预览水印的提示附加文字，默认将 用户姓名和其所在部门也加入水印文字
                String tpText = ConfigUtil.getConfigValue("uploadpreview.tp.text");
                // TODO 这里需要进行改造，提示文字要能个性化
                if (StringUtil.isBlank(tpText)) {
                    tpText = "涉密文件请勿泄漏";
                }
                tpText = tools.encode(tpNo + ";" + userDispName + "," + ouName + "," + tpText);
                // 如果开启水印，tp是必须带的，否则不让预览
                result += "tp=" + tpText + "&";
            }
            String furl = downloadUrl;
            byte[] DESkey = desKey.getBytes();// 设置密钥，略去
            byte[] DESIV = desIv.getBytes() ;// 设置向量，略去
            //加密算法的参数接口，IvParameterSpec是它的一个实现
            AlgorithmParameterSpec iv = null;
            Key key = null;
            // 设置密钥参数
            DESKeySpec keySpec = new DESKeySpec(DESkey);
            // 设置向量
            iv = new IvParameterSpec(DESIV);
            // 获得密钥工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);// 得到密钥对象
            Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // 设置工作模式为加密模式，给出密钥和向量
            enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] pasByte = enCipher.doFinal(downloadUrl.getBytes("utf-8"));
            furl = Base64.encodeBase64String(pasByte).replaceAll("\\+", "_").replaceAll("\\/", "@");
            result += "furl=" + furl;
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
