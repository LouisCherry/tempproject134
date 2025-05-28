package com.epoint.mq.spgl;

import com.epoint.audittaskzj.job.AuditTaskZJSynService;
import com.epoint.basic.spgl.domain.*;
import com.epoint.basic.spgl.inter.ISpglFail;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.annotation.Entity;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@DisallowConcurrentExecution
public class AuditTaskZJSyncJob implements Job {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //向前置库同步数据多张表
            syncSpglDfxmsplcjdsxxxb();
            syncSpglDfxmsplcjdxxb();
            syncSpglDfxmsplcxxb();
            syncSpglXmdwxxb();
            syncSpglXmjbxxb();
            syncSpglXmjgxxb();
            syncSpglXmqtfjxxb();
            // syncSpglXmspsxblqthjxxb();
            syncSpglXmspsxblxxb();
            syncSpglXmspsxblxxxxb();
            syncSpglXmspsxpfwjxxb();
            //syncSpglXmspsxqqyjxxb();
            syncSpglZjfwsxblxxb();
            syncspglxmspsxzqyjxxb();
            syncspglXmspsxbltbcxxxb();
            //syncSpglZjfwsxblxxxxb();
            //推送前期意见
            syncSpglXmqqyjxxb();
            EpointFrameDsManager.commit();
            //调用住建通知接口
            doNotify();

        } catch (Exception ex) {
            EpointFrameDsManager.rollback();
            ex.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    //调用住建通知接口
    private void doNotify() {
        String url = ConfigUtil.getConfigValue("zjnotifyurl");
        if (StringUtil.isNotBlank(url)) {
            log.info("开始调用通知接口！");
            String result = HttpUtil.doGet(url);
            log.info("调用通知接口调用结束！内容" + result);
        }
    }

    private void syncSpglXmqqyjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmqqyjxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmqqyjxxb> list1 = service.selectNeedsync(SpglXmqqyjxxb.class);
        if (list1 != null) {
            for (SpglXmqqyjxxb spglXmqqyjxxb : list1) {
                //去除多余字段
                String rowguid = spglXmqqyjxxb.getRowguid();
                spglXmqqyjxxb.remove("BelongXiaQuCode");
                spglXmqqyjxxb.remove("OperateUserName");
                spglXmqqyjxxb.remove("OperateDate");
                spglXmqqyjxxb.remove("ROW_ID");
                spglXmqqyjxxb.remove("YearFlag");
                spglXmqqyjxxb.remove("sync");
                try {
                    //添加记录
                    service.addRecord(SpglXmqqyjxxb.class, spglXmqqyjxxb);
                    //更新字段
                    service.updateSync(SpglXmqqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);

                } catch (Exception e) {
                    e.printStackTrace();
                    //报错处理
                    service.updateSync(SpglXmqqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息,错误信息唯一标识为rowguid
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglXmqqyjxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmqqyjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxbm") + ";" + record.getStr("spjdxh") + ";" + record.getStr("splcbbh"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //修改对应本地记录的状态
                service.updateSyncQqyjForErr(SpglXmqqyjxxb.class, "rowguid", record.getStr("rowguid"));
            }
        }
        EpointFrameDsManager.commit();
    }

    private void syncSpglDfxmsplcjdsxxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglDfxmsplcjdsxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglDfxmsplcjdsxxxb> list1 = service.selectNeedsync(SpglDfxmsplcjdsxxxb.class);
        if (list1 != null) {
            for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb : list1) {
                //去除多余字段
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                spglDfxmsplcjdsxxxb.remove("sync");
                try {
                    //添加记录
                    service.addRecord(SpglDfxmsplcjdsxxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    //更新字段
                    service.updateSync(SpglDfxmsplcjdsxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    //报错处理
                    service.updateSync(SpglDfxmsplcjdsxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息,错误信息唯一标识为rowguid
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();

    }

    private void syncSpglDfxmsplcjdxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglDfxmsplcjdxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglDfxmsplcjdxxb> list1 = service.selectNeedsync(SpglDfxmsplcjdxxb.class);
        if (list1 != null) {
            for (SpglDfxmsplcjdxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglDfxmsplcjdxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglDfxmsplcjdxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglDfxmsplcjdxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglDfxmsplcjdxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spjdbm") + ";" + record.getStr("splcbbh"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //修改对应本地记录的状态
                service.updateSyncForErr(SpglDfxmsplcjdxxb.class, "spjdbm", record.getStr("spjdbm"), "splcbbh", record.getStr("splcbbh"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglDfxmsplcxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglDfxmsplcxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglDfxmsplcxxb> list1 = service.selectNeedsync(SpglDfxmsplcxxb.class);
        if (list1 != null) {
            for (SpglDfxmsplcxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglDfxmsplcxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglDfxmsplcxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglDfxmsplcxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglDfxmsplcxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("splcbm") + ";" + record.getStr("splcbbh"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglDfxmsplcxxb.class, "splcbm", record.getStr("splcbm"), "splcbbh", record.getStr("splcbbh"));
            }
        }
        EpointFrameDsManager.commit();

    }

    private void syncSpglXmdwxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmdwxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmdwxxb> list1 = service.selectNeedsync(SpglXmdwxxb.class);
        if (list1 != null) {
            for (SpglXmdwxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmdwxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmdwxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmdwxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmdwxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("xmdm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmdwxxb.class, "xmdm", record.getStr("xmdm"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmjbxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmjbxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmjbxxb> list1 = service.selectNeedsync(SpglXmjbxxb.class);
        if (list1 != null) {
            for (SpglXmjbxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmjbxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmjbxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmjbxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }
        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmjbxxb.class);
        if (list != null) {

            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("xmdm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmjbxxb.class, "xmdm", record.getStr("xmdm"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmjgxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmjgxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmjgxxb> list1 = service.selectNeedsync(SpglXmjgxxb.class);
        if (list1 != null) {
            for (SpglXmjgxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmjgxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmjgxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmjgxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }
        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmjgxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("xmdm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmjgxxb.class, "xmdm", record.getStr("xmdm"));
            }
        }
        EpointFrameDsManager.commit();
    }

    private void syncSpglXmqtfjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmqtfjxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmqtfjxxb> list1 = service.selectNeedsync(SpglXmqtfjxxb.class);
        if (list1 != null) {
            for (SpglXmqtfjxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmqtfjxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmqtfjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmqtfjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmqtfjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("fjid"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmqtfjxxb.class, "fjid", record.getStr("fjid"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmspsxblqthjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmspsxblqthjxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxblqthjxxb> list1 = service.selectNeedsync(SpglXmspsxblqthjxxb.class);
        if (list1 != null) {
            for (SpglXmspsxblqthjxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmspsxblqthjxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmspsxblqthjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxblqthjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getSpsxslbm());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();


        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxblqthjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm") + ";" + record.getStr("qthjmc"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxblqthjxxb.class, "spsxslbm", record.getStr("spsxslbm"), "qthjmc", record.getStr("qthjmc"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmspsxblxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmspsxblxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);

        //查询需要同步的数据,同步有按期办结时间的数据
        List<SpglXmspsxblxxb> list1 = service.selectNeedAqbjsjsync(SpglXmspsxblxxb.class);
        if (list1 != null) {
            for (SpglXmspsxblxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmspsxblxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmspsxblxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxblxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }


        EpointFrameDsManager.commit();

        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxblxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxblxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }
        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmspsxblxxxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmspsxblxxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxblxxxxb> list1 = service.selectNeedsync(SpglXmspsxblxxxxb.class);
        if (list1 != null) {
            for (SpglXmspsxblxxxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");

                try {
                    service.addRecord(SpglXmspsxblxxxxb.class, spglDfxmsplcjdsxxxb);
                    EpointFrameDsManager.commit();
                    service.updateSync(SpglXmspsxblxxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxblxxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxblxxxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm") + ";" + record.getStr("blzt"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxblxxxxb.class, "spsxslbm", record.getStr("spsxslbm"), "blzt", record.getStr("blzt"));
            }

        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglXmspsxpfwjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmspsxpfwjxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxpfwjxxb> list1 = service.selectNeedsync(SpglXmspsxpfwjxxb.class);
        if (list1 != null) {
            for (SpglXmspsxpfwjxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmspsxpfwjxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglXmspsxpfwjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxpfwjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }
        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxpfwjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("fjid"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxpfwjxxb.class, "fjid", record.getStr("fjid"));
            }
        }
        EpointFrameDsManager.commit();
    }

    private void syncSpglXmspsxqqyjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglXmspsxqqyjxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxqqyjxxb> list1 = service.selectNeedsync(SpglXmspsxqqyjxxb.class);
        if (list1 != null) {
            for (SpglXmspsxqqyjxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmspsxqqyjxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglXmspsxqqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxqqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxqqyjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxqqyjxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }

        }
        EpointFrameDsManager.commit();
    }


    private void syncSpglZjfwsxblxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglZjfwsxblxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglZjfwsxblxxb> list1 = service.selectNeedsync(SpglZjfwsxblxxb.class);
        if (list1 != null) {
            for (SpglZjfwsxblxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglZjfwsxblxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglZjfwsxblxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglZjfwsxblxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }
        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglZjfwsxblxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglZjfwsxblxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }
        }
        EpointFrameDsManager.commit();

    }

    private void syncSpglZjfwsxblxxxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglZjfwsxblxxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglZjfwsxblxxxxb> list1 = service.selectNeedsync(SpglZjfwsxblxxxxb.class);
        if (list1 != null) {
            for (SpglZjfwsxblxxxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglZjfwsxblxxxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglZjfwsxblxxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglZjfwsxblxxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglZjfwsxblxxxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglZjfwsxblxxxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }
        }
        EpointFrameDsManager.commit();
    }

    private void syncspglxmspsxzqyjxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglZjfwsxblxxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxzqyjxxb> list1 = service.selectNeedsync(SpglXmspsxzqyjxxb.class);
        if (list1 != null) {
            for (SpglXmspsxzqyjxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");
                try {
                    service.addRecord(SpglXmspsxzqyjxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglXmspsxzqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxzqyjxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxzqyjxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglZjfwsxblxxxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }
        }
        EpointFrameDsManager.commit();
    }

    private void syncspglXmspsxbltbcxxxb() {
        AuditTaskZJSynService service = new AuditTaskZJSynService();
        Entity en = SpglZjfwsxblxxxxb.class.getAnnotation(Entity.class);
        ISpglFail ispglfail = ContainerFactory.getContainInfo().getComponent(ISpglFail.class);
        //查询需要同步的数据
        List<SpglXmspsxbltbcxxxb> list1 = service.selectNeedsync(SpglXmspsxbltbcxxxb.class);
        if (list1 != null) {
            for (SpglXmspsxbltbcxxxb spglDfxmsplcjdsxxxb : list1) {
                String rowguid = spglDfxmsplcjdsxxxb.getRowguid();
                spglDfxmsplcjdsxxxb.remove("BelongXiaQuCode");
                spglDfxmsplcjdsxxxb.remove("OperateUserName");
                spglDfxmsplcjdsxxxb.remove("OperateDate");
                spglDfxmsplcjdsxxxb.remove("ROW_ID");
                spglDfxmsplcjdsxxxb.remove("YearFlag");

                try {
                    service.addRecord(SpglXmspsxbltbcxxxb.class, spglDfxmsplcjdsxxxb);
                    service.updateSync(SpglXmspsxbltbcxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_ONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    service.updateSync(SpglXmspsxbltbcxxxb.class, rowguid, ZwfwConstant.CONSTANT_STR_TWO);
                    //存储错误信息
                    SpglFail sf = new SpglFail();
                    sf.setRowguid(UUID.randomUUID().toString());
                    sf.setTablename(en.table());
                    sf.setLsh(0000L);
                    sf.setNote(e.getMessage());
                    sf.setIdentifyfield(spglDfxmsplcjdsxxxb.getRowguid());
                    sf.setStatus(ZwfwConstant.CONSTANT_STR_TWO);
                    ispglfail.insert(sf);
                }
            }

        }

        EpointFrameDsManager.commit();
        //查询需要同步到错误信息表的数据
        List<Record> list = service.selectNeedsyncErr(SpglXmspsxbltbcxxxb.class);
        if (list != null) {
            for (Record record : list) {
                SpglFail sf = new SpglFail();
                sf.setRowguid(UUID.randomUUID().toString());
                sf.setTablename(en.table());
                sf.setLsh(record.getLong("lsh"));
                sf.setNote(record.getStr("sbyy"));
                sf.setIdentifyfield(record.getStr("spsxslbm"));
                sf.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                ispglfail.insert(sf);
                //更新状态
                service.updateSyncForErr(SpglXmspsxbltbcxxxb.class, "spsxslbm", record.getStr("spsxslbm"));
            }
        }
        EpointFrameDsManager.commit();
    }
}
