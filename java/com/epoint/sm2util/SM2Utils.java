package com.epoint.sm2util;


import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle154.math.ec.ECPoint;


public class SM2Utils {

    //数据解密
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        /** 分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

        //返回解密结果
        return c2;
    }
    
    public static String getDecrypt(String privatekey, String encrypt){
        byte[] decrypt;
        try {
            decrypt = decrypt(Util.hexToByte(privatekey), Util.hexToByte(encrypt));
            String newInfo=new String(decrypt,"utf-8");
            return newInfo;
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 将字节数组转为字符串
        return "fail"; 
    }

    public static void main(String[] args) throws IOException {
		//sm2私钥
		String privatekey="641585FCDB9B5D71F3456AE9FC5A1FAA6E14D1647A46A037ECF6FCC48A2F9F77";
        //加密串 说明：加密前是"测试解密"
		String encrypt = "042485F119F453F15D22B3E7842AF0B1B6AE80450D9DAFCFE5A0D0AF67B4FB8B840960BFEF547A653B37FD344B0CC9C7ED3E673F06A31757968F0D51AEE7570462E2C18627D7EF26D016D15516D7A44A79C930E62F7A6A9F02666EF57E615A5B92DC4E733A6DD0861EC4DFBE507570D9D49486AA5155CA531837E53A4DA1076AF706D62C38D3A97EFEAB96D836D42730C33ED447624589F1163C0178FC99D0A24C096E3651536A0895F83C52E83B7949816C178350D4514890E440D417B69023EBBA45D2CDE71967F8D0C9EC8D2F00E2BA0EBA57FEFD3F8408A17A6A3053FDD75DB87EC7FDB18CE4F3465FAC44D7530F915768615529B785E1E7FF0E64FE30DD807110CF6167C74D4C5AC738B3AD4326BBA77E5F88CE6F58073F12A36225C179E22D31E8ABC853F3C7659AB9C04E328C70EC8FE3";

        // 将加密后的信息解密，私钥和加密后的数据的字节码对象需要用util里的hexToByte方法
        byte[] decrypt = decrypt(Util.hexToByte(privatekey), Util.hexToByte(encrypt));
        // 将字节数组转为字符串
        String newInfo=new String(decrypt,"utf-8");
        //system.out.println("解密后信息:"+newInfo);

    }
}