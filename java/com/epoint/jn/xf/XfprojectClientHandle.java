package com.epoint.jn.xf;

import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zbxfdj.controller.TsProjectDataRest;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * 消费者客户端
 *
 * @author WindowCC
 * @version [版本号, 2018年4月28日]
 */
public class XfprojectClientHandle extends AuditClientMessageListener {

    public static String[] taskname = new String[]{"其他建设工程消防验收备案抽查", "建设工程消防设计审查", "特殊建设工程消防验收", "建设工程消防设计审查（设区的市级权限）（新设）",
            "建设工程消防验收（设区的市级权限）（新设）", "建设工程消防设计审查（县级权限）（新设）", "建设工程消防验收备案", "建设工程消防验收（县级权限）（新设）"};
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * @param proMessage 参数
     * @return
     * @exception/throws
     */
    @Override
    public void handleMessage(AuditMqMessage proMessage) throws Exception {

        log.info("消防mq实现类进入=====");

        IAuditProject iauditproject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        TsProjectDataRest tsprojectdatarest = ContainerFactory.getContainInfo().getComponent(TsProjectDataRest.class);

        // 解析mq消息内容
        String[] messageContent = proMessage.getSendmessage().split("@")[1].split("\\.");
        if (messageContent == null || messageContent.length < 3) {
            return;
        }
        // 主题阶段唯一标识
        String subappguid = messageContent[0];

        String projects = messageContent[5];

        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("subappguid", subappguid);

        if(StringUtil.isNotBlank(projects)){
            log.info("projects:"+projects);
            if(projects.startsWith("'")){
                projects = projects.replace("'","");
            }
            String[] rowguids = projects.split(",");
            sqlc.in("rowguid", StringUtil.joinSql(rowguids));
        }else{
            log.info("rowguid:"+proMessage.getSendmessage());
        }
        sqlc.setSelectFields("rowguid,projectname");
        List<AuditProject> listproject = iauditproject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
        log.info("消防对接的list:" + listproject);
        if (listproject == null) {
            return;
        }
        for (AuditProject auditproject : listproject) {
            if (isxf(auditproject.getProjectname())) {
                // todo 调用消防接口
                log.info("需要上上报的消防事项" + auditproject.getRowguid());
                tsprojectdatarest.tsprojectdata(auditproject.getRowguid());
            }
        }
    }

    public static boolean isxf(String projectname) {
        boolean flag = false;
        for (int i = 0; i < taskname.length; i++) {
            if (projectname.indexOf(taskname[i]) != -1) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}
