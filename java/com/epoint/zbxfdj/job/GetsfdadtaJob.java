package com.epoint.zbxfdj.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.api.entity.SpglXmclbzqdxxb;
import com.epoint.zbxfdj.xfdj.spglxmclbzqdxxb.impl.SpglXmclbzqdxxbService;
import com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.api.entity.SpglXmspgcpfwjxxb;
import com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.impl.SpglXmspgcpfwjxxbService;
import com.epoint.zbxfdj.xfdj.spglxmspgcxxb.api.entity.SpglXmspgcxxb;
import com.epoint.zbxfdj.xfdj.spglxmspgcxxb.impl.SpglXmspgcxxbService;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * 工改与省消防平台对接 从前置库遍历读取到数据，将文件相关数据落到附件表
 *
 * @author lgb
 * @description
 * @date 2022年12月22日09:57:51
 */
@DisallowConcurrentExecution
public class GetsfdadtaJob implements Job {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            log.info("================与消防平台对接从前置库获取信息服务启动=====================");
            doService();
            log.info("=================与消防平台对接从前置库获取信息服务结束====================");
            EpointFrameDsManager.commit();
        } catch (DocumentException e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() throws DocumentException {
        // 一些service
        IAuditProjectUnusual iAuditProjectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

        // 调接收省平台返回的办件状态和结果接口 changeProjectStatusAndResult
        String url = iConfigService.getFrameConfigValue("djxfggurl");

        InputStream in = null;
        try {
            // 项目审批过程批复文件信息表
            log.info("开始遍历项目审批过程批复文件信息表");
            SpglXmspgcpfwjxxbService pwfjService = new SpglXmspgcpfwjxxbService();
            List<SpglXmspgcpfwjxxb> pwfjListBySjsczt = pwfjService.findListBySjsczt(0);
            if (ValidateUtil.isNotBlankCollection(pwfjListBySjsczt)) {
                for (SpglXmspgcpfwjxxb spglXmspgcpfwjxxb : pwfjListBySjsczt) {
                    try {
                        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                        frameAttachInfo.setAttachGuid(UUID.randomUUID().toString());
                        // 地方数据主键
                        frameAttachInfo.setCliengGuid(spglXmspgcpfwjxxb.getDfsjzj());
                        // 附件名称
                        frameAttachInfo.setAttachFileName(spglXmspgcpfwjxxb.getFjmc());
                        // 附件类型
                        frameAttachInfo.setContentType(spglXmspgcpfwjxxb.getFjlx());
                        //消防附件url(多存储)
                        frameAttachInfo.set("xffjurl", spglXmspgcpfwjxxb.getFjurl());
                        // 批复文号和日期
                        if (StringUtil.isNotBlank(spglXmspgcpfwjxxb.getPfwh())) {
                            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid("*", spglXmspgcpfwjxxb.getDfsjzj(), null).getResult();
                            if (auditProject != null) {
                                auditProject.setCertificatedate(spglXmspgcpfwjxxb.getPfrq());
                                auditProject.setWenhao(spglXmspgcpfwjxxb.getPfwh());
                            }
                        }

                        byte[] fileArray = getByteArrayFromUrl(spglXmspgcpfwjxxb.getStr("fjzwwurl"));
                        InputStream inputStream = null;
                        try {
                            inputStream = new ByteArrayInputStream(fileArray);
                            iAttachService.addAttach(frameAttachInfo, inputStream);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                        pwfjService.updateSjsczt("4", spglXmspgcpfwjxxb.getId());
                    } catch (Exception e) {
                        log.info("url连接失败：" + spglXmspgcpfwjxxb.getStr("fjzwwurl"));
                        e.printStackTrace();
                    }
                }
            }
            log.info("结束遍历项目审批过程批复文件信息表");

            // 项目审批过程信息表
            log.info("开始遍历项目审批过程信息表");
            SpglXmspgcxxbService spgcservice = new SpglXmspgcxxbService();
            SpglXmclbzqdxxbService spglxmclbzqdxxbservice = new SpglXmclbzqdxxbService();
            List<SpglXmspgcxxb> spgcListBySjsczt = spgcservice.findListBySjsczt(0);
            if (ValidateUtil.isNotBlankCollection(spgcListBySjsczt)) {
                for (SpglXmspgcxxb spglXmspgc : spgcListBySjsczt) {
                    // 调用工改接收省平台返回的办件状态和结果接口
                    JSONObject jsonSend = new JSONObject();
                    JSONObject jsonParams = new JSONObject();
                    // 办件唯一标识 f629d8cb-0157-4344-923c-0fb10c3d9167
                    jsonParams.put("projectguid", spglXmspgc.getDfsjzj());
                    // 办理科室
                    jsonParams.put("blks", spglXmspgc.getSpbmmc());
                    // 办理人
                    jsonParams.put("blr", spglXmspgc.getSprxm());
                    // "办理状态"
                    jsonParams.put("blzt", spglXmspgc.getSpjg());
                    // "特别程序值"
                    jsonParams.put("tbcx", "");
                    // 办理意见
                    jsonParams.put("blyj", spglXmspgc.getSpyj());
                    // 办理时间
                    jsonParams.put("blsj", EpointDateUtil.convertDate2String(spglXmspgc.getSpsj(), EpointDateUtil.DATE_TIME_FORMAT));
                    // 特别程序名称，当特别程序为8时必填
                    jsonParams.put("tbcxmc", "");
                    //系统来源:消防前置库
                    jsonParams.put("source","xfqzk");

                    JSONArray resultlistArray = new JSONArray();
                    // 办理结果
                    jsonParams.put("resultlist", resultlistArray);

                    SpglXmclbzqdxxb spglxmclbzqdxxb = new SpglXmclbzqdxxb();
                    // 补正材料
                    if (spglXmspgc.getSpjg() == 6) {
                        List<SpglXmclbzqdxxb> spglxmclbzqdxxblist = spglxmclbzqdxxbservice.findListByLsh(spglXmspgc.getLsh(), 0);
                        if (!spglxmclbzqdxxblist.isEmpty()) {
                            spglxmclbzqdxxb = spglxmclbzqdxxblist.get(0);
                            jsonParams.put("bzlist", spglxmclbzqdxxb.getStr("FORMS_JSON"));
                        }
                    }

                    jsonSend.put("token", ZwdtConstant.SysValidateData);
                    jsonSend.put("params", jsonParams);

                    String result = HttpUtil.doPostJson(url + "/rest/spproject/changeProjectStatusAndResult",
                            jsonSend.toJSONString());
                    //log.info("调用工改接口结果：" + result);
                    JSONObject resultObject = JSON.parseObject(result);
                    int code = resultObject.getJSONObject("custom").getInteger("code");
                    if (code == 1) {
                        log.info("调用成功！准备更新spglXmspgc");
                        // 调用成功！更新Sjsczt=4
                        spgcservice.updateSjsczt("4", spglXmspgc.getId());
                        if (spglXmspgc.getSpjg() == 6) {
                            spglxmclbzqdxxbservice.updateSjsczt("4", spglxmclbzqdxxb.getId());
                        }
                    } else {
                        // 调用失败！更新Sjsczt=6
                        spgcservice.updateSjsczt("6", spglXmspgc.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.info("与消防平台对接从前置库获取信息服务异常");
            e.printStackTrace();
        } finally {
            log.info("与消防平台对接从前置库获取信息服务成功！");
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据url获取字节数组
     *
     * @param urlStr
     * @return
     */
    private byte[] getByteArrayFromUrl(String urlStr) {
        InputStream inputStream = null;
        ByteArrayOutputStream os = null;
        try {
            //url解码
            URL url = new URL(java.net.URLDecoder.decode(urlStr, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(10 * 1000);
            //得到输入流
            inputStream = conn.getURL().openStream();

            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            // 从输入流中读取字节并将它们存储在缓冲区中
            while ((len = inputStream.read(buffer)) != -1) {
                // 将缓冲区中的字节写入输出流
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            log.info("附件下载失败：" + urlStr);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }
        if (os == null) {
            return new byte[0];
        }
        return os.toByteArray();
    }
}
