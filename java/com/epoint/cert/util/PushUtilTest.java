package com.epoint.cert.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PushUtilTest
{
//    private static final Logger logger = Logger.getLogger(EncryDecryUtil3.class);

    // 可以删除掉，仅测试使用
    public static void main(String[] args) {
        String accept = "{\"AcceptData\":[{\"MonomerList\":[{\"DSJZMJ\":11.0,\"DFSJZJ\":\"b1e25233-478a-4439-be85-66412e8e322b\",\"JGTX\":\"01\",\"DXCS\":11,\"DXJZMJ\":11.0,\"DTMC\":\"1\",\"ZDMJ\":11.0,\"GMZB\":\"11\",\"JZMJ\":11.0,\"DTJWDZB\":\"空\",\"JZGCGD\":111.0,\"GCYT\":\"0101\",\"DTBM\":\"370800120241111118\",\"JZFS\":\"9\",\"DSCS\":11}],\"DFSJZJ\":\"b7d70341-361b-4010-8201-57b6ae229881\",\"SFBGWFZT\":\"0\",\"LXR\":\"测试\",\"Zzcertid\":\"1.2.156.3005.2.11100000000013338W087.11370800MB28559184.37080001202411010001.001.3\",\"JSGM\":\"1\",\"BAFW\":\"备案范围\",\"JGYSRQ\":\"2024-12-10\",\"JSDWLX\":\"1\",\"GCMC\":\"测试赋码\",\"JSDWYJ\":\"建设单位意见\",\"DAYSZT\":\"2\",\"XMDM\":\"3708272000062-01-1122-06\",\"JSFZRLXDH\":\"18264724439\",\"JGYSBABH\":\"37080001202411010001\",\"JSDWDM\":\"370827200006253513\",\"BAJGXYDM\":\"11370800MB28559184\",\"SFSXLHYS\":1,\"BARQ\":\"2024-11-01\",\"SSQX\":\"370800\",\"SJZJ\":11.0,\"BAJG\":\"济宁市行政审批服务局\",\"GCGHXKZH\":\"工程规划许可证号\",\"JSDWDD\":\"11\",\"LXRSJH\":\"18264724439\",\"XZQHDM\":\"370800\",\"CorpInfoList\":[{\"FRZJHM\":\"370830199801181718\",\"FZRLXDH\":\"18264724439\",\"DFSJZJ\":\"92cd12a1-70ac-4163-9d95-f47af515f2a4\",\"DWTYSHXYDM\":\"370830199801181718\",\"FZRZJHM\":\"370830199801181718\",\"FZRXM\":\"11\",\"FRZJLX\":\"1\",\"DWMC\":\"11\",\"ZZDJ\":\"101\",\"FDDBR\":\"11\",\"DWLX\":\"2\",\"FZRZJLX\":\"1\"}],\"JDJGMC\":\"程质量监督机构名称：\",\"BAWJML\":\"1.竣工验收意见表;\\n2.工程竣工验收报告;\\n3.规划、消防验收等部门出具的认可文件或者准许使用的\\n文件;\\n4.施工单位签署的工程质量保修书;             5.住宅工程的《住宅质量保证书》和《住宅使用说明书》:\\n6.法规、规章规定必须提供的其他文件。\",\"XMJWDZB\":\"1,1;1,1;1,1\",\"JSDWXMFZR\":\"测试\",\"JSFZRZJLX\":\"1\",\"JGYSYJYLDZ\":\"http://jizwfw.sd.gov.cn/jnzwdt/rest/attachAction/getContent?isCommondto=true&attachGuid=83db6424-742c-4779-ac2c-f9d59b9a6f65\",\"DAYSYJ\":\"111\",\"SGXKZBH\":\"11\",\"JSFZRZJHM\":\"370827200006253513\",\"KGRQ\":\"2024-12-10\",\"SCHGSBH\":\"施工图审查合格书编号\",\"GCDD\":\"1\",\"JSDZ\":\"1\",\"JSDW\":\"袁智勇\"}]}";
        String data = sm4Encryption(accept, "2fkgapjad4xzq8ff");
        System.out.println("data：" +data);
    }

    public static String sm4Encryption(String str, String key) {
        SM4 sm4 = SmUtil.sm4(key.getBytes());
        String base64Pass = sm4.encryptBase64(str);
        return base64Pass;
    }
    

}
