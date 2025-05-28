package com.epoint.socket.iso8583;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.epoint.socket.SocketClient;
import com.epoint.socket.iso8583.utils.ByteUtils;

public class MainTest
{

    public static void main(String[] args) throws IOException, IOException {
        // TODO Auto-generated method stub
        Iso8583Utils utils = new Iso8583Utils();

        Map<Integer, String> map = new TreeMap<>();
        // 签到

        /* 	map.put(11, "123456");
        	map.put(41, "01171229");
        	map.put(42, "103201171229395");
        	map.put(60, "00000001003");
        	map.put(63, "01");
        	String font = "0800";*/

        // 查询客户信息
        map.put(3, "360000");
        map.put(11, "123456");
        map.put(25, "92");

        map.put(41, "01171229");
        map.put(42, "103201171229395");
        // map.put(41, "00810001");
        // map.put(42, "123456789900081");
        String x = "C1V2560346100000000000000000000000000000000000000000000010247404750000001b0004zzyh8832zzyhsd754389066#";

        String zx = ByteUtils.getHexStr(x.getBytes(), false);

        //system.out.println(zx);

        // 转化为你byte
        map.put(48, "0000001300050");
        map.put(49, "156");
        map.put(60, "0000001300050");
        String font = "0100";
        map.put(64, "665834F8697CB856");
        // map.put(64, "00000001001");

        /*		String cards ="600084008260220000000008000020000000c00012007882333031303132363833303134333031353934373030303600110000000100300003303031";
        		String backCards =
        				"007B60008900846030003201060810003A00010AC0001400002217195811161009084805791032313131313631373139353830307A7A7968383833327A7A7968736437353433383930363600110000001400300080A089FF81A3A92F37C1F618BE7F5214E0C7FF03208E43BC179214D33D000000000000000005CE3C15000000";
        
        
        		//system.out.println(utils.parseIso8583Datas(ByteUtils.getByteByNoSpli(backCards)));*/

        byte[] data = null;
        try {
            data = utils.packageIso8583Datas(map, false, font);
            String backString = SocketClient.send(data);
            ByteArrayOutputStream outAr = new ByteArrayOutputStream();
            outAr.write(ByteUtils.getByteByNoSpli(backString));
            byte[] result = ByteUtils.subArray(outAr.toByteArray(), 13, -1);
            //system.out.println(utils.parseIso8583Datas(result));
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
