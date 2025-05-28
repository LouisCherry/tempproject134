package com.epoint.xmz.thirdreporteddata.spgl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.spgl.impl.SpglCommonImpl;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 住建部_中介服务事项办理详细信息表对应的后台service实现类
 *
 * @author zhpengsy
 * @version [版本号, 2018-11-16 15:09:23]
 */
@Component
@Service
@Primary
public class GxhSpglCommonImpl extends SpglCommonImpl {
    /**
     *
     */
    private static final long serialVersionUID = 1515327597495159593L;


    @Override
    public void editToPushData(BaseEntity oldrecord, BaseEntity newrecord, boolean initnewRecord) {
        if (initnewRecord) {
            // rowguid 重置
            newrecord.set("rowguid", UUID.randomUUID().toString());
            // lsh置空
            newrecord.set("lsh", null);
            // 置为有效数据
            newrecord.set("sjyxbs", ZwfwConstant.CONSTANT_INT_ONE);
            // 数据无效原因置空
            newrecord.set("sjwxyy", null);
            // 同步状态 -> 未同步 0
            newrecord.set("sync", ZwfwConstant.CONSTANT_INT_ZERO);

            /* 3.0新增逻辑  切面质检组件的规则，质检过后不用重新设置 */
            // 数据上传状态 -> 新增数据 0
            //newrecord.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
            // 失败原因置空
            //newrecord.set("sbyy",null);
            /* 3.0结束逻辑 */

        }
        editToPushData(oldrecord, newrecord);
    }
}
