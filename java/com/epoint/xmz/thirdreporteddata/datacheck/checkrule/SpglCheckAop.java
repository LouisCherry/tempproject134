package com.epoint.xmz.thirdreporteddata.datacheck.checkrule;

import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.zwbg.datacheck.api.DataCheckAPI;
import com.epoint.zwbg.datacheck.model.CheckDetail;
import com.epoint.zwbg.datacheck.model.CheckFieldResult;
import com.epoint.zwbg.datacheck.model.CheckResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class SpglCheckAop {
    @Pointcut("execution(* com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.*.insert(..))")
    public void spglInsertPoint() {

    }

    @Pointcut("execution(* com.epoint.basic.spgl.impl.SpglCommonImpl.editToPushData(..))")
    public void spglEditPoint() {

    }

    @Before("spglInsertPoint()")
    public void before(JoinPoint jp) {
        BaseEntity baseentity = (BaseEntity) jp.getArgs()[0];
        // 调用质检组件对记录进行检测
        checkEntity(baseentity);
    }

    @Before("spglEditPoint()")
    public void beforeEdit(JoinPoint jp) {
        BaseEntity baseentity = (BaseEntity) jp.getArgs()[1];

        // 质检前重置规则
        baseentity.set("sbyy", "");
        baseentity.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);

        // 调用质检组件对记录进行检测
        checkEntity(baseentity);
    }


    private void checkEntity(BaseEntity baseentity) {
        Entity entity = baseentity.getClass().getAnnotation(Entity.class);
        CheckResult check = new DataCheckAPI(baseentity, entity.table()).check();
        if (!check.getRtnstatus()) {
            List<CheckFieldResult> fieldCheckResultList = check.getFieldCheckResultList();
            // 校验失败，更新数据表状态
            if (fieldCheckResultList != null && !fieldCheckResultList.isEmpty()) {
                baseentity.set("sjsczt", "-1");
                baseentity.set("sbyy", dealErrText(fieldCheckResultList));
            }
        }
    }

    private String dealErrText(List<CheckFieldResult> fieldCheckResultList) {
        StringBuilder errortext = new StringBuilder("规则校验异常：");
        for (CheckFieldResult checkFieldResult : fieldCheckResultList) {
            List<CheckDetail> failList = checkFieldResult.getFailList();
            for (CheckDetail checkDetail : failList) {
                errortext.append(checkDetail.getRuleName() + ";");
            }
            errortext.append("异常编码：" + checkFieldResult.getErrorCode() + ";");
        }
        return errortext.toString();
    }


}
