package com.epoint.common.zwdt.login.dhrsautil;


/**
 * 
 * @Description:RSA解密工具类
 * @date 2019年12月19日下午2:56:08
 *
 */
public class Rsa {
    public static void main(String[] args) throws Exception {
        // 网关返回的密文
        String secret =
            "BdHg3ZZi11HR0tEi+7xMjL7QYoMHg/POEtbzrm0ZPrqP4voNTTQRxkRTdkSq+CwoN7JTuCP4s0hs2ZZu2biXHLO7xZkFPYf4pqB0+kjDNxY9mb2pFSvT79g05PVHdC39QroxX49q2CTafd9EUlZbq0GO+/3xo5ZKLSKRwRZV908=";
        // rsa私钥（在平台应用详情处复制）
        String privateKey =
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL1xPmciI6W2grfir5lWykikigo0Tw+PdbwIcPyluxfg9EcCeii3XzUhCpbEtSp7Jy6hqwTPge8bywSg5BAGvON02uBY5DN6y2Pqfu4dsIMCGwPHI2FQlu/88EAJBv6esYFFIsdr45uBG9Wya/je+vpMJ3E4A9tgP76v6/1bK2zLAgMBAAECgYAu5Px8jS/j0oUTPfMX8ysJxmlBU2eKw4lybWiCsgfZRl9RwKQ6tgHEZhR38+Ogy3GMkoqCG1fft7KOx8EM0o4pFvhpHohDXDmEKHYDJvRkQUoGurmN6Qdatt03MT4EKMBI087O/CMplc9lmPPJNbQqyWZ884LSpSH6He75yc1nUQJBAN56EBwJEBLE5d4ZnNbfAdlmftTqobqT2ZlmQz5/ucGMOCW1WcoSFeVlYRogiIZE6srIOEDxbpRIm8IXhClk+cMCQQDZ/OSEGNMD1vTvkBp1C56D1KGnb3q6dbfIrK/bKEREk7SVY6HRUGnuMgXQNpM2dmVjJhfXwIouD2sDhq6dC4hZAkAR4Fj/B7Nk7rFRwVka4txxLY0vapMIPO0VKGRq1zSD2tKAJSxL0lw0DIta4BZYQ51iIehzP3MVMyhg1ibmdJilAkBI+EC+f1jTpjcjokdY+uS7GIhsdgLNO+6jfDr5z8badd8zSsC2QobTN2d+xWYOCx/xSqUckdUvifW2nnUBGQUxAkB/x4A4JUBQCzVVcDVqsqgDIrsWqvHJRQOCUDA1rYvKPVy0lx2LTkhOL4/WfEr2nPOz7Yh+WgDlOZSq9Eanxv/N";
        String content = getContent(secret, privateKey);
        System.out.println(content);
    }

    /**
     * 
     * @param privateKey
     * @param secret
     * @Description:解密网关返回的密文
     * @date 2019年12月19日下午1:50:00
     *
     * @return
     * @throws Exception
     */
    public static String getContent(String secret, String privateKey) throws Exception {
        String decrypt = RsatureUtil.decrypt(secret, RsatureUtil.getPrivateKey(privateKey));
        return decrypt;
    }
}
