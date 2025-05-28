package com.epoint.jn.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

public class JnBeanUtil
{
    public JnBeanUtil(String propertiesname) throws IOException {
        this.init(propertiesname);
    }

    // <表名,<表字段，对应值>>
    public Map<String, Map<String, String>> fieldmap = new HashMap<>();
    // <表名，数据>
    Map<String, Record> data = new HashMap<>();

    public String defaultkey = "default";

    public void init(String propertiesname) throws  IOException {
        Properties p = new Properties();
        InputStream in = JnBeanUtil.class.getClassLoader().getResourceAsStream(propertiesname);
        p.load(in);
        for (Entry<Object, Object> entry : p.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            String[] split = key.split("\\.");
            // key中的名称转换成map，没有设置默认值
            if (split.length == 1) {
                split = new String[] {defaultkey, split[0] };
            }
            Map<String, String> map = fieldmap.get(split[0]);
            if (map == null) {
                map = new HashMap<>();
                fieldmap.put(split[0], map);
            }
            map.put(split[1], value);
        }
        in.close();
    }
    // 根据实体获取实
    public BaseEntity coverBean(BaseEntity qzkbean, boolean dealdate) {
        Entity en = qzkbean.getClass().getAnnotation(Entity.class);
        String tableName = en.table();
        // 表字段对应
        coverData(qzkbean, tableName, dealdate);
        return qzkbean;
    }
    // 兼容没有表名配置
    public Record coverBean(Record qzkbean, boolean dealdate) {
        String tableName = defaultkey;
        // 表字段对应
        coverData(qzkbean, tableName, dealdate);
        return qzkbean;
    }

    public void coverData(Record qzkbean, String tableName, boolean dealdate) {
        Map<String, String> fild = fieldmap.get(tableName);
        for (Entry<String, String> entry : fild.entrySet()) {
            // 数据来源字段
            String value = entry.getValue(); // 来源字段名称
            String field = entry.getKey();// 目标名称
            String[] values = value.split("\\.");  //表名.字段名称
            Record baseEntity;
            String filed;
            if (values.length == 1) { // 没有.分割标识,从default中获取目标数据
                baseEntity = data.get(defaultkey);
                filed=value;
            }
            else {
                // 根据表名获取目标数据
                baseEntity = data.get(values[0]);
                filed = values[1];
            }
            if (baseEntity.get(filed) instanceof Date && dealdate) {
                baseEntity.set(filed,
                        EpointDateUtil.convertDate2String(baseEntity.get(filed), EpointDateUtil.DATE_FORMAT));
            }
            // 设置目标值
            qzkbean.put(field, baseEntity.get(filed));
        }
    }

    /**
     * 
     * 将实体信息放入到map中
     * 
     * @param entity
     *           ProjectCode=audit_rs_item_baseinfo.itemcode 情况使用
     */
    public void dataPut(BaseEntity entity) {
        data.put(getTableName(entity), entity);
    }

    /**
     *
     * 将实体信息放入到map中
     *
     * @param entity
     *            ProjectCode=itemcode 情况使用
     */
    public void dataPut(Record entity) {
        data.put(defaultkey, entity);
    }



    /**
     * 
     * 将实体信息去除到map中
     * 
     * @param baseClass
     *            map
     */
    @SuppressWarnings("unchecked")
    public <T> T dataGet(Class<? extends BaseEntity> baseClass) {
        return (T) data.get(getTableName(baseClass));
    }

    public String getTableName(BaseEntity bean) {
        return StringUtil.toLowerCase(bean.getClass().getAnnotation(Entity.class).table());
    }

    public String getTableName(Class<? extends BaseEntity> bean) {
        return StringUtil.toLowerCase(bean.getAnnotation(Entity.class).table());
    }

}
