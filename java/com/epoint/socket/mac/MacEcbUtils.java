package com.epoint.socket.mac;

/**
 * 银联标准Mac 算法
 */
public class MacEcbUtils {

    public static void main(String[] args) {
       // byte[] key = new byte[]{0x5C, (byte) 0xBE, 0x7E, 0x38, (byte) 0xA1, 0x46, (byte) 0xFD, 0x5C};
        byte[] key =  "4d64918836c06184573e61cf54c1aff9ae440c5f41647f053c1df89b000000000000000013ab7c2300000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes();
        byte[] block1 =  "360000".getBytes();
        byte[] block2 =  "791540".getBytes();
        byte[] block3 =  "92".getBytes();
        byte[] block4 =  "01171229".getBytes();
        byte[] block5 =  "103201171229395".getBytes();
        byte[] block6 =  "8f4f2a84c846c99b1f5c723d6a9c6235fdec0f99a18d7bb0055820ec99a18ac29b3fadea6f9ccb5abba3e04119af02f48656ab9aa7196e5810020dd84bfe567059f95a8b2bdb767a78e456e8c320dd110793a90845901c3bf94e6a73b9de307a338cac4179f809a5b13d185a031594cf13f710143d23dd1f8266c4d2652f4802238edae4736c4c2e21b95d6ae0844dc5ae64505c31d8d379a40a12882584bd51e12ca764caaf9ba3098f6dc314cf4b2bc8a282043187bb40817bb30a6cdab2980cee352c5876725b3c54e3588c924eef".getBytes();
        byte[] block7 =  "156".getBytes();
        byte[] block8 =  "0000001300050".getBytes();
        //加密1
        byte[] back1 = getMac(key,block1,false,new byte[8]);
        byte[] back2 = getMac(key,block2,false,back1);
        byte[] back3 = getMac(key,block3,false,back2);
        byte[] back4 = getMac(key,block4,false,back3);
        byte[] back5 = getMac(key,block5,false,back4);
        byte[] back6 = getMac(key,block6,false,back5);
        byte[] back7 = getMac(key,block7,false,back6);
        byte[] back8 = getMac(key,block8,true,back7);

        //system.out.println(Utils.bcd2Str(back8));
    }

    /**
     * mac计算
     *
     * @param key   mac秘钥
     * @param Input 待加密数据
     * @return
     */
    public static byte[] getMac(byte[] key, byte[] Input,boolean isflg,byte[] behind8) {
        int length = Input.length;
        int x = length % 8;
        // 需要补位的长度
        int addLen = 0;
        if (x != 0) {
            addLen = 8 - length % 8;
        }
       int pos = 0;
        // 原始数据补位后的数据
        byte[] data = new byte[length + addLen];
        System.arraycopy(Input, 0, data, 0, length);
        byte[] oper1 = new byte[8];
        System.arraycopy(data, pos, oper1, 0, 8);
        pos += 8;
        // 8字节异或
        for (int i = 1; i < data.length / 8; i++) {
            byte[] oper2 = new byte[8];
            System.arraycopy(data, pos, oper2, 0, 8);
            byte[] t = bytesXOR(oper1, oper2);
            oper1 = t;
            pos += 8;
        }
        // 将异或运算后的最后8个字节（RESULT BLOCK）转换成16个HEXDECIMAL：
        byte[] resultBlock = bytesToHexString(oper1).getBytes();
        // 取前8个字节MAK加密
        byte[] front8 = new byte[8];
        System.arraycopy(resultBlock, 0, front8, 0, 8);
       /* byte[] behind8 = new byte[8];*/
        System.arraycopy(resultBlock, 8, behind8, 0, 8);
        byte[] desfront8 = DesUtils.encrypt(front8, key);
        // 将加密后的结果与后8 个字节异或：
        byte[] resultXOR = bytesXOR(desfront8, behind8);
        // 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算
        byte[] buff = DesUtils.encrypt(resultXOR, key);


        if(isflg){
            // 将运算后的结果（ENC BLOCK2）转换成16 个HEXDECIMAL asc
            byte[] retBuf = new byte[8];
            // 取8个长度字节就是mac值
            System.arraycopy(bytesToHexString(buff).getBytes(), 0, retBuf, 0, 8);
            return retBuf;
        }

        return  buff;

    }

    /**
     * 单字节异或
     *
     * @param src1
     * @param src2
     * @return
     */
    public static byte byteXOR(byte src1, byte src2) {
        return (byte) ((src1 & 0xFF) ^ (src2 & 0xFF));
    }

    /**
     * 字节数组异或
     *
     * @param src1
     * @param src2
     * @return
     */
    public static byte[] bytesXOR(byte[] src1, byte[] src2) {
        int length = src1.length;
        if (length != src2.length) {
            return null;
        }
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = byteXOR(src1[i], src2[i]);
        }
        return result;
    }

    /**
     * 字节数组转HEXDECIMAL
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}