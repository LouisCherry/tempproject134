package com.epoint.xmz.thirdreporteddata.common;

import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.task.commonapi.domain.YwxxRelationMapping;

import java.util.*;

public class GxhSpConstant {
    // 国标_证件类型
    public static final String GB_ZJLX_FR = "1";
    public static final String GB_ZJLX_ZRR = "2";

    public static final Integer SPGL_SJSCZT_ERR = -1; // 异常，质检错误
    public static final Integer SPGL_SJSCZT = 0;  // 正常


    public static final List<String> SX_LIST = new ArrayList<>(Arrays.asList(
            "0090001",
            "0090002",
            "0090003",
            "0090004",
            "0120001",
            "0120002",
            "0120003",
            "0100000",
            "0140000",
            "0150000",
            "0180000"
    ));

    public static final Map<String, YwxxRelationMapping> objectMap = new HashMap<String, YwxxRelationMapping>() {
        {
            // 建设用地规划许可信息
            put("0060001", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            put("0060002", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            put("0060003", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            put("0060004", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            put("0060005", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            put("0060006", new YwxxRelationMapping("spgljsydghxkxxbv3edit", SpglJsydghxkxxbV3.class, "建设用地规划许可信息"));
            // 建设工程规划许可信息
            put("0070001", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            put("0070002", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            put("0070003", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            put("0070004", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            put("0070005", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            put("0070006", new YwxxRelationMapping("spgljsgcghxkxxbv3edit", SpglJsgcghxkxxbV3.class, "建设工程规划许可信息"));
            // 施工图设计文件审查信息
            put("0090001", new YwxxRelationMapping("spglsgtsjwjscxxbv3edit", SpglSgtsjwjscxxbV3.class, "施工图设计文件审查信息"));
            put("0090002", new YwxxRelationMapping("spglsgtsjwjscxxbv3edit", SpglSgtsjwjscxxbV3.class, "施工图设计文件审查信息"));
            put("0090003", new YwxxRelationMapping("spglsgtsjwjscxxbv3edit", SpglSgtsjwjscxxbV3.class, "施工图设计文件审查信息"));
            put("0090004", new YwxxRelationMapping("spglsgtsjwjscxxbv3edit", SpglSgtsjwjscxxbV3.class, "施工图设计文件审查信息"));
            // 建筑工程施工许可信息
            put("0120001", new YwxxRelationMapping("spgljzgcsgxkxxbv3edit", SpglJzgcsgxkxxbV3.class, "建筑工程施工许可信息"));
            put("0120002", new YwxxRelationMapping("spgljzgcsgxkxxbv3edit", SpglJzgcsgxkxxbV3.class, "建筑工程施工许可信息"));
            put("0120003", new YwxxRelationMapping("spgljzgcsgxkxxbv3edit", SpglJzgcsgxkxxbV3.class, "建筑工程施工许可信息"));
            // 建设工程消防设计审查信息
            put("0100000", new YwxxRelationMapping("spgljsgcxfsjscxxbv3edit", SpglJsgcxfsjscxxbV3.class, "建设工程消防设计审查信息"));
            // 建设工程消防验收信息
            put("0140000", new YwxxRelationMapping("spgljsgcxfysxxbv3edit", SpglJsgcxfysxxbV3.class, "建设工程消防验收信息"));
            // 建设工程消防验收备案信息
            put("0150000", new YwxxRelationMapping("spgljsgcxfysbaxxbv3edit", SpglJsgcxfysbaxxbV3.class, "建设工程消防验收备案信息"));
            // 建设工程竣工验收备案
            put("0180000", new YwxxRelationMapping("spgljsgcjgysbaxxbv3edit", SpglJsgcjgysbaxxbV3.class, "建设工程竣工验收备案信息"));
        }
    };


}
