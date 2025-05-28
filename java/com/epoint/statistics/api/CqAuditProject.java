package com.epoint.statistics.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;

import java.io.Serializable;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName CqAuditProject.java
 * @Description 超期办件统计列表页面
 * @createTime 2022年01月06日 12:36:00
 */
public class CqAuditProject extends AuditProject implements Cloneable, Serializable {

    /**
     * 未办结
     */
    private String wbjauditproject;
    /**
     * 超期件
     */
    private String cqauditproject;
    /**
     * 已办结
     */
    private String ybjauditproject;
    /**
     * 办结率
     */
    private Long bjlauditproject;

    /**
     * @Author: yuchenglin
     * @Description: get方法
     * @Date: 2022/1/6 19:14
     * @return: java.lang.String
     **/
    public String getWbjauditproject() {
        return wbjauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:15
     * @param wbjauditproject: wbjauditproject
     * @return: void
     **/
    public void setWbjauditproject(String wbjauditproject) {
        this.wbjauditproject = wbjauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:15
     * @return: java.lang.String
     **/
    public String getCqauditproject() {
        return cqauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:15
     * @param cqauditproject:
     * @return: void
     **/
    public void setCqauditproject(String cqauditproject) {
        this.cqauditproject = cqauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:15
     * @return: java.lang.String
     **/
    public String getYbjauditproject() {
        return ybjauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:15
     * @param ybjauditproject: ybjauditproject
     * @return: void
     **/
    public void setYbjauditproject(String ybjauditproject) {
        this.ybjauditproject = ybjauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:16
     * @return: java.lang.Long
     **/
    public Long getBjlauditproject() {
        return bjlauditproject;
    }

    /***
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:16
     * @param bjlauditproject: bjlauditproject
     * @return: void
     **/
    public void setBjlauditproject(Long bjlauditproject) {
        this.bjlauditproject = bjlauditproject;
    }
}
