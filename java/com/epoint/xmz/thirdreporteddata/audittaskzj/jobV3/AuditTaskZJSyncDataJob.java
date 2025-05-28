package com.epoint.xmz.thirdreporteddata.audittaskzj.jobV3;

import com.epoint.auditspitem.job.spglxmbqxxbv3.api.entity.SpglXmbqxxb;
import com.epoint.basic.spgl.domain.SpglDfxmsplcxxb;
import com.epoint.basic.spgl.domain.SpglFail;
import com.epoint.basic.spgl.inter.ISpglFail;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.annotation.Entity;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.*;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxb.api.entity.SpglGcspxtjbxxb;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@DisallowConcurrentExecution
public class AuditTaskZJSyncDataJob implements Job {
    public boolean issend = false;

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    AuditTaskZJSynDataService service = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        service = new AuditTaskZJSynDataService();
        issend = false;
        try {
            EpointFrameDsManager.begin(null);
            // 向前置库同步数据多张表
            // 3.0
            syncCommonTable(SpglQypgxxbV3.class);
            syncCommonTable(SpglQypgsxxxbV3.class);
            syncCommonTable(Spglsplcxxb.class);
            syncCommonTable(Spglspjdxxb.class);
            syncCommonTable(SpglSpsxjbxxb.class);
            syncCommonTable(SpglSpsxkzxxb.class);
            syncCommonTable(SpglSpsxclmlxxb.class);
            syncCommonTable(Spglsplcjdsxxxb.class);
            syncCommonTable(SpglXmjbxxbV3.class);
            syncCommonTable(SpglXmydhxjzxxbV3.class);
            syncCommonTable(SpglXmqqyjxxbV3.class);
            syncCommonTable(SpglXmspsxblxxbV3.class);
            syncCommonTable(SpglXmspsxblxxxxbV3.class);
            syncCommonTable(SpglXmspsxzqyjxxbV3.class);
            syncCommonTable(SpglXmspsxbltbcxxxbV3.class);
            syncCommonTable(SpglXmspsxpfwjxxbV3.class);
            syncCommonTable(SpglsqcljqtfjxxbV3.class);
            syncCommonTable(SpglGzcnzbjjgxxbV3.class);
            syncCommonTable(SpglJsgcjgysbaxxbV3.class);
            syncCommonTable(SpglJsgcxfysbaxxbV3.class);
            syncCommonTable(SpglJsgcxfsjscxxbV3.class);
            syncCommonTable(SpglJsgcxfysxxbV3.class);
            syncCommonTable(SpglJzgcsgxkxxbV3.class);
            syncCommonTable(SpglKcsjryxxbV3.class);
            syncCommonTable(SpglSgtsjwjscxxbV3.class);
            syncCommonTable(SpglSgtsjwjscxxxxbV3.class);
            syncCommonTable(SpglZjfwsxblgcxxbV3.class);
            syncCommonTable(SpglZjfwsxblxxbV3.class);
            syncCommonTable(SpglXmjgxxbV3.class);
            syncCommonTable(SpglDfghkzxxxbV3.class);
            syncCommonTable(SpglJsydghxkxxbV3.class);
            syncCommonTable(SpglJsgcghxkxxbV3.class);
            syncCommonTable(SpglGcspxtjbxxb.class);
            //商品房预售信息许可表
            syncCommonTable(SpglSpfysxkxxb.class);
            //商品房现售备案信息表
            syncCommonTable(SpglSpfxsbaxxb.class);

            //责任主体
            syncCommonTable(SpglZrztxxbV3.class);
            syncCommonTable(SpglXmdtxxbV3.class);

            //标签信息
            syncCommonTable(SpglXmbqxxb.class);
            //附件信息
            syncCommonTable(SpglXmfjxxb.class);

            EpointFrameDsManager.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
//            service.closeDao();
            EpointFrameDsManager.close();
        }
        // 调用住建通知接口
        if (issend) {
            doNotify();
        }
    }

    // 调用住建通知接口
    private void doNotify() {
        String url = ConfigUtil.getConfigValue("zjnotifyurl");
        if (StringUtil.isNotBlank(url)) {
            log.info("开始调用通知接口！");
            String result = HttpUtil.doGet(url);
            log.info("调用通知接口调用结束！内容" + result);
        }
    }

    private void syncCommonTable(Class<? extends BaseEntity> baseClass) {
        log.info("-------------开始进行前置库同步数据-----------");
        Entity en = baseClass.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        // 查询需要同步的数据
        List<Record> list1 = service.selectNeedsync(baseClass);
        if (ValidateUtil.isNotBlankCollection(list1)) {
            Date date = new Date();
            for (Record item : list1) {
                String rowguid = item.getStr("rowguid");
                // clone一份出来
                Record itemClone = (Record) item.clone();
                try {
                    // 同步本地记录到前置库
                    service.insertRecord(baseClass, item);
                    issend = true;
                    // 设置本地记录为已同步状态
                    itemClone.set("sync", ZwfwConstant.CONSTANT_INT_ONE);
                    // 更新本地记录的流水号
                    //itemClone.set("Lsh", Long.parseLong(lsh));
                    // 同步时间
                    itemClone.set("operatedate", date);
                    service.updateRecord(baseClass, itemClone);
                    EpointFrameDsManager.commit();
                } catch (Exception e) {
                    EpointFrameDsManager.rollback();
                    e.printStackTrace();
                    // 更新同步状态为'同步失败'
                    itemClone.set("sync", ZwfwConstant.CONSTANT_INT_TWO);
                    // 同步失败信息
                    itemClone.set("Sbyy", "同步国家前置库失败！");
                    service.updateRecord(baseClass, itemClone);
                    // 存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setOperatedate(new Date());
                    sf.setTablename(en.table());
                    String lsh = String.valueOf(item.getStr("lsh"));
                    if (StringUtil.isNotBlank(lsh)) {
                        sf.setLsh(Long.valueOf(lsh));
                    } else {
                        sf.setLsh(null);
                    }
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(rowguid);
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                } finally {
                    EpointFrameDsManager.close();
                    service.closeDao();
                }
            }
        }
        try {
            // 针对已同步过的有效的lsh为空的列表，根据约束查询前置库找到唯一数据的lsh，修改本地lsh的值和数据上传状态
            List<Record> localList = service.selectAfterSyncAndLshIsNullInfo(baseClass);
            String indexfield = ConfigUtil.getConfigValue("spgl", "tablename." + en.table());
            if (ValidateUtil.isNotBlankCollection(localList)) {
                for (Record localInfo : localList) {
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    if (StringUtils.isNotBlank(indexfield)) {
                        List<String> fields = Arrays.asList(indexfield.split(","));
                        for (String index : fields) {
                            sUtil.eq(index, localInfo.get(index));
                        }
                    }
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    List<Record> qzkList = service.selectListByConditions(baseClass, sUtil.getMap(),0,50);
                    if (ValidateUtil.isNotBlankCollection(qzkList)) {
                        Record qzkInfo = qzkList.get(0);
                        // 修改为前置库的lsh
                        localInfo.set("Lsh", qzkInfo.get("Lsh"));
                        // 顺便把 0/1状态的数据对比前置库，保持数据上传状态一致性
                        Integer localSjsczt=0;
                        if(localInfo.get("sjsczt") instanceof Integer) {
                            localSjsczt = localInfo.get("sjsczt");
                        }
                        else if(localInfo.get("sjsczt") instanceof String) {
                            localSjsczt = Integer.parseInt(localInfo.getStr("sjsczt"));
                        }
                        Integer qzkSjsczt=0;
                        if(qzkInfo.get("sjsczt") instanceof Integer) {
                            qzkSjsczt = qzkInfo.get("sjsczt");
                        }
                        else if(qzkInfo.get("sjsczt") instanceof String) {
                            qzkSjsczt = Integer.parseInt(qzkInfo.getStr("sjsczt"));
                        }
                        String sbyy = qzkInfo.get("sbyy");
                        // 修改本地该数据的上传状态为前置库查询到的；如果相同，不做操作
                        if ((localSjsczt == 0 || localSjsczt == 1) && qzkSjsczt != null
                                && !qzkSjsczt.equals(localSjsczt)) {
                            // 如果返回的是校验失败，把失败原因也写入本地库
                            localInfo.set("sjsczt", qzkSjsczt);
                            if (qzkSjsczt == 2) {
                                localInfo.set("sbyy", sbyy);
                            }
                        }
                        service.updateRecord(baseClass, localInfo);
                    }
                }
            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
            service.closeDao();
        }

        try {
            // 新增数据状态(0)->前置库的反馈
            List<Record> zeroList = service.selectSynced(baseClass, ZwfwConstant.CONSTANT_STR_ZERO);
            if (ValidateUtil.isNotBlankCollection(zeroList)) {
                for (Record record : zeroList) {
                    String lsh = record.getStr("lsh");
                    String sjsczt = record.getStr("sjsczt");
                    // 通过流水号查询该数据在前置库的当前数据上传状态
                    Record info = service.selectAfterSyncInfoByLsh(baseClass, lsh);
                    if (info == null) {
                        continue;
                    }
                    String qzkSjsczt = info.getStr("sjsczt");
                    String sbyy = info.getStr("sbyy");
                    // 修改本地该数据的上传状态为前置库查询到的；如果相同，不做操作
                    if (StringUtil.isNotBlank(qzkSjsczt) && !qzkSjsczt.equals(sjsczt)) {
                        // 如果返回的是校验失败，把失败原因也写入本地库
                        record.set("sjsczt", qzkSjsczt);
                        if (ZwfwConstant.CONSTANT_STR_TWO.equals(qzkSjsczt)) {
                            record.set("sbyy", sbyy);
                        }
                        service.updateRecord(baseClass, record);
                    }
                }
            }
            // 校验成功状态(1)->前置库的反馈
            List<Record> oneList = service.selectSynced(SpglDfxmsplcxxb.class, ZwfwConstant.CONSTANT_STR_ONE);
            if (ValidateUtil.isNotBlankCollection(oneList)) {
                for (Record record : oneList) {
                    String lsh = record.getStr("lsh");
                    String sjsczt = record.getStr("sjsczt");
                    // 通过流水号查询该数据在前置库的当前数据上传状态
                    Record info = service.selectAfterSyncInfoByLsh(baseClass, lsh);
                    if (info == null) {
                        continue;
                    }
                    String qzkSjsczt = info.getStr("sjsczt");
                    // 修改本地该数据的上传状态为前置库查询到的；如果相同，不做操作
                    if (StringUtil.isNotBlank(qzkSjsczt) && !qzkSjsczt.equals(sjsczt)) {
                        record.set("sjsczt", qzkSjsczt);
                        service.updateRecord(baseClass, record);
                    }
                }
            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
            service.closeDao();
        }
    }

}
