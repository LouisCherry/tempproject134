package com.epoint.ssocommon.sm2;

import java.io.IOException;

import com.epoint.common.sm2.SM2SignUtils;
import com.epoint.core.utils.security.api.IIrreversibleEncryption;

/**
 * sm2实现类
 * 
 * @author Zzh
 * @version [版本号, 2018年6月6日]
 */
public class SM2EncryptAndDecryptImpl implements IIrreversibleEncryption
{

    @Override
    public String encryption(String password, String loginId) {
        String sign = null;
        try {
            sign = SM2SignUtils.sign(loginId, SM2SignUtils.SM2_KEY, password);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return sign;
    }

    @Override
    public Boolean matchs(String password, String loginId, String dbpwd) {
        boolean vs = false;
        try {
            vs = SM2SignUtils.verifySign(loginId, SM2SignUtils.SM2_KEY, password, dbpwd);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return vs;
    }

    public static void main(String[] args) throws IOException {
        String loginId = "admin";
        String password = "EpointGtig_**##@12345";
        String sign = new SM2EncryptAndDecryptImpl().encryption(password, loginId);
        System.out.println("sign=" + sign);
        // 3046022100CFCE727E317075553AA17A46F5A9A97D5FFC4029DA78F3FEDE4D299D34A6FA14022100B64BA86C23209CC558FE550215B1E2575173EA877558BEF5C51725B6415EACEF
        // 3045022100BDC78E53235457C9724817FF3FCBF3D88D70F355397328C2AF574A497A179169022064DD984933D140B7EAD6D558805760BB3EB104DA65ED5F013AB36DE2ACDCC20B

        // sign =
        // "3045022100BDC78E53235457C9724817FF3FCBF3D88D70F355397328C2AF574A497A179169022064DD984933D140B7EAD6D558805760BB3EB104DA65ED5F013AB36DE2ACDCC20B";
        boolean vs = new SM2EncryptAndDecryptImpl().matchs(password, loginId, sign);
        System.out.println("vs=" + vs);
    }
}
