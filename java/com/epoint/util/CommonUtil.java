package com.epoint.util;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

public class CommonUtil
{
    /**
     * 通过实体类获取数据表名
     * 
     * @param baseClass
     * @return baseClass.getAnnotation(Entity.class).table()
     */
    public static String getTablenameByClass(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        return en.table();
    }
}
