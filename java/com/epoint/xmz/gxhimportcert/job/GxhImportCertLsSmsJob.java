package com.epoint.xmz.gxhimportcert.job;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertLsService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCertLs;

import cn.hutool.core.lang.UUID;

@DisallowConcurrentExecution
public class GxhImportCertLsSmsJob implements Job
{

    @Autowired
    private IGxhImportCertLsService service;

    private IMessagesCenterService messagesCenterService;

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        SqlConditionUtil sql = new SqlConditionUtil();
        sql.in("DATE(yxqenddate)", " DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY) ");
        sql.eq("smstatus", "10");
        List<GxhImportCertLs> list = service.findListByCondition(sql.getMap());

        if (list != null && !list.isEmpty()) {
            for (GxhImportCertLs gxhImportCertLs : list) {
                try {
                    // 开启事务
                    EpointFrameDsManager.begin(null);

                    if (StringUtil.isNotBlank(gxhImportCertLs.getMobile())) {

                        String content = "您好，您的证照【" + gxhImportCertLs.getCertname() + "】将于" + EpointDateUtil.convertDate2String(gxhImportCertLs.getYxqenddate(), "yyyy-MM-dd") + "到期，请及时办理延续业务。";
                        messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null, gxhImportCertLs.getMobile(), UUID.randomUUID().toString(), gxhImportCertLs.getApplyname(), "a5e97867-e6a7-4d88-b237-c064ba214d31", "梁山县中心管理员", "", "", null, false, "370832");

                        gxhImportCertLs.setSmstatus("20");
                        gxhImportCertLs.setSmsdate(new Date());
                        service.update(gxhImportCertLs);
                    }
                    else {
                        gxhImportCertLs.setSmstatus("40"); // 未获取手机号
                        service.update(gxhImportCertLs);
                    }

                    // 提交事务
                    EpointFrameDsManager.commit();

                }
                catch (Exception e) {
                    // 回滚事务
                    EpointFrameDsManager.rollback();
                    e.printStackTrace();

                    EpointFrameDsManager.begin(null);

                    gxhImportCertLs.setSmstatus("30");
                    service.update(gxhImportCertLs);

                    EpointFrameDsManager.commit();

                }
                finally {
                    // 关闭数据源
                    EpointFrameDsManager.close();
                }

            }
        }

    }
}
