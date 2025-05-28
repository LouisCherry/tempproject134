package com.epoint.zwzt.xxfb.constant;

import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;

public class XxfbConstant
{
    // 开启信息发布人名义 1启用 0禁用，不配默认禁用
    public final static String ZWZT_XXFB_INFO_RELEASE_PUBLISHER__IS = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_publisher__is"))
            ? ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_publisher__is") : "0";
    // 开启是否热门字段 1启用 0禁用，不配默认禁用
    public final static String ZWZT_XXFB_INFO_RELEASE_ISHOT__IS = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_ishot__is"))
            ? ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_ishot__is") : "0";

    // 开启作者字段 1启用 0禁用，不配默认禁用
    public final static String ZWZT_XXFB_INFO_RELEASE_INFOAUTHOR__IS = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_infoauthor__is"))
            ? ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_infoauthor__is") : "0";

    // 是否启用信息截至日期下架 1启用 0禁用、不配默认禁用(1.0版本暂不使用)
    public final static String ZWZT_XXFB_INFO_RELEASE_DEADLINE_IS = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_deadline_is"))
            ? ConfigUtil.getConfigValue("xxfb", "zwzt_xxfb_info_release_deadline_is") : "0";

    /**
     * 栏目层级不配 不配默认3
     */
    public final static String ZWZT_XXFB_INFO_COLUMN_LEVEL = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb","zwzt_xxfb_info_column_level"))
            ? ConfigUtil.getConfigValue("xxfb","zwzt_xxfb_info_column_level")
            : "3";
    /**
     * #是否启用栏目类型 1启用 0禁用、不配默认启用
     */
    public final static String ZWZT_XXFB_INFO_COLUMN_TYPE_IS = StringUtil
            .isNotBlank(ConfigUtil.getConfigValue("xxfb","zwzt_xxfb_info_column_type_is"))
            ? ConfigUtil.getConfigValue("xxfb","zwzt_xxfb_info_column_type_is")
            : "1";
}
