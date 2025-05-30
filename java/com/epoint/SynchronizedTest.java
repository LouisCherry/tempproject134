package com.epoint;

import com.epoint.common.util.OfficeWebUrlEncryptUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SynchronizedTest {

    /**
     * synchronized为锁关键字
     */
    public static synchronized void update(Integer i) {
        // TODO 内部统一时间只能进入一个线程操作

        // 验证代码
        System.out.println(Thread.currentThread().getName() + "  " + i + "  " + LocalDateTime.now());
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        // 多线程执行遍历list
        list.parallelStream().forEach(SynchronizedTest::update);

//        String baseUrl = "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=000000e2-72ee-40fe-b85a-3a38cabca02c";
//        String url = OfficeWebUrlEncryptUtil.getEncryptUrl(baseUrl, ".pdf");
//        String result = "http://112.6.110.176:28080/office365/?fname=202211040179.pdf&" + url;
//        System.out.println(result);
    }
}
