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
		String privatekey="00DE243AA05CCBC75F5DDCE56294184093AED8B3C3249F29715563BDB5A1E5DFD1";
        //加密串 说明：加密前是"测试解密"
	//	String encrypt = "0429ADD3DA865C98F2655EC8FB454DD1DAC49C74CCF95E629A58744050A8A326E22DC11551796443C0D9C1086F3747373B0002DA0369C9EE0581A7B253829ECE21B87B76D7DAF48B508382C2FD8A75F77D9EA46E2D09F4E9C7BAADB1893520C266B65454B625AE8FBB5431A47CA54150DC466B02F8632AF6566684207783B7DE96867AD853758D7382A6191D2E95AFF12657CA688D1E4EE2589A30AD1897764689419A29B50057E967D30DE515DEBE6EC0DA23B077BA1969A87D62251891B5E1B46A2626CD8B60B28CBD22F5A393DA3154C1C61F73B68558D5BDFB1A90EB45B5621D24C41AEC61E80814AA2084AFE8BE8077DEAA5C04CF770971BC81CC504D241770C872AC56A05F99C7EB3210190E1C06C5894AC52CCA9131890F026E1FC118D6";
		String encrypt ="048C356ECB4EBA6E446B00F600D04E4F7BA1D476F8E8DCE86F8C31BD61451CBDBFBCD437B57B9CA9E8A694EEB8D7CEAD298854C670F994E101A7FC72C62E7EB96A4417C2DAD87C2340D38941068A70EFB0AAB0C6388AD84632EFE63820F1850C8EB81E91F1B3B5C7FA5B82886C75E82B24D787D8CDD49C6EFFBDD90B07D25C97975D6ED63C27D97CEB4626347ABDD59417EFC340FB8638CB7310832391A0B932724A50A08FF2B5CC403FEA66CBEDA5CADBD96FF41075CC9786D35DD2D61A4943F9136AB1396F807A8ADD7073057BFDC2A67726EFFAB8FA2B8F91391A38C402C22D9CD5D82F873726F9713E08445ADE127061745545A09530D257AC868511FD183067DB8AD1A52E62B3CD7802E65EF8CF9EEE6CD40F39A334479368951E84387E3320AE64";
        // 将加密后的信息解密，私钥和加密后的数据的字节码对象需要用util里的hexToByte方法
        byte[] decrypt = decrypt(Util.hexToByte(privatekey), Util.hexToByte(encrypt));
        // 将字节数组转为字符串
        String newInfo=new String(decrypt,"utf-8");
        System.out.println("解密后信息:"+newInfo);
    }
    
  
}