package com.epoint.jn.util;

import java.io.FileNotFoundException;
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
    public JnBeanUtil(String propertiesname) throws FileNotFoundException, IOException {
        this.init(propertiesname);
    }

    public Map<String, Map<String, String>> fieldmap = new HashMap<>();

    Map<String, BaseEntity> data = new HashMap<>();

    public String defaultkey = "default";

    public void init(String propertiesname) throws FileNotFoundException, IOException {
        Properties p = new Properties();
        InputStream in = JnBeanUtil.class.getClassLoader().getResourceAsStream(propertiesname);
        p.load(in);
        for (Entry<Object, Object> entry : p.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            String[] split = key.split("\\.");
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

    public BaseEntity coverBean(BaseEntity qzkbean, boolean dealdate) {
        Entity en = qzkbean.getClass().getAnnotation(Entity.class);
        String tableName = en.table();
        // 表字段对应
        coverData(qzkbean, tableName, dealdate);
        return qzkbean;
    }

    public Record coverBean(Record qzkbean, boolean dealdate) {
        String tableName = defaultkey;
        // 表字段对应
        coverData(qzkbean, tableName, dealdate);
        return qzkbean;
    }

    private void coverData(Record qzkbean, String tableName, boolean dealdate) {
        Map<String, String> fild = fieldmap.get(tableName);
        for (Entry<String, String> entry : fild.entrySet()) {
            // 数据来源字段
            String value = entry.getValue();
            String qzkfield = entry.getKey();
            String[] valuearr = value.split("\\.");
            if (valuearr.length == 1) { // 没有.分割标识默认值
                qzkbean.set(qzkfield, value);
            }
            else {// 去对应实体中的默认值
                String tablename = valuearr[0];
                BaseEntity baseEntity = data.get(tablename);
                String filed = valuearr[1];
                if(baseEntity!=null) {
                    if (baseEntity.get(filed) instanceof Date && dealdate) {
                        baseEntity.set(filed,
                                EpointDateUtil.convertDate2String(baseEntity.get(filed), EpointDateUtil.DATE_FORMAT));
                    }
                    qzkbean.put(qzkfield, baseEntity.get(filed));
                }
            }
        }
    }

    /**
     * 
     * 将实体信息放入到map中
     * 
     * @param data
     *            map
     * @param entity
     *            数据
     */
    public void dataPut(BaseEntity entity) {
        data.put(getTableName(entity), entity);
    }

    /**
     * 
     * 将实体信息放入到map中
     * 
     * @param data
     *            map
     * @param entity
     *            数据
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
