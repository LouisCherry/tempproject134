package com.epoint.cert.util;

public class CertCheckUtilsNew {

    // 可用
    public Character[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 计算证照校验码(ISO/IEC 7064
     * MOD37,36)注：参考https://www.doc88.com/p-69699870786494.html
     *
     * @param XXy
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @author: fjian
     * @date: 2023-02-16 15:28:10
     * @see [类、类#方法、类#成员]
     */
    public String getXy(String XXy) {
        // 1.2.156.3005.2.11100000000013338W050.91610800064834709T.2023000001.001.X
        // 去除头1.2.156.3005.2.
        // 使用11100000000013338W050.91610800064834709T.2023000001.001 获取校验位

        // 去除所有.
        String Xy = XXy.replace(".", "");
        // 字符0~9对应值分别为0~9,字符A~Z对应值分别为10~35,总计36
        int M = 36;
        int c = M;
        for (int i = 0; i < Xy.length(); i++) {
            // 从M开始，M+下一字符值
            c = c + getXyzh(Xy.charAt(i));
            // 求余
            c = c % M;
            if (c == 0) {
                c = M;
            }
            c = c * 2;
            c = c % (M + 1);
        }
        int result = 0;
        if (c % M != 1) {
            result = (M + 1 - c) % (M);
        }
        return getXyint(result);
    }

    /**
     * 获取在字符串中的位置序号，从右计算
     *
     * @param c
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @author: fjian
     * @date: 2023-02-16 15:32:33
     * @see [类、类#方法、类#成员]
     */
    private int getXyzh(char c) {
        for (int i = 0; i < a.length; i++) {
            if ((c + "").equals(a[i] + "")) {
                return i;
            }
        }
        return 0;
    }

    private String getXyint(int i) {
        return a[i] + "";
    }

}
