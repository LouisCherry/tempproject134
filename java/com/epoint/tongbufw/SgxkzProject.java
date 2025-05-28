package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.sgxkzproject.api.IJzgcSgxkzService;
import com.epoint.jnzwfw.sgxkzproject.api.ISpxkzBanjianProjectService;
import com.epoint.jnzwfw.sgxkzproject.api.entity.SpxkzBanjianProject;
import com.epoint.jnzwfw.sgxkzproject.api.entity.jzgcsgxkz1;

@DisallowConcurrentExecution
public class SgxkzProject implements Job
{
    transient Logger log = LogUtil.getLog(SgxkzProject.class);

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步一窗受理系统的办件信息
    private void doService() {
        ISpxkzBanjianProjectService service = ContainerFactory.getContainInfo().getComponent(ISpxkzBanjianProjectService.class);
        IJzgcSgxkzService jzgcSgxkzService = ContainerFactory.getContainInfo().getComponent(IJzgcSgxkzService.class);
        IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService= ContainerFactory.getContainInfo().getComponent(IAuditProjectFormSgxkzService.class);
        IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IAuditOrgaArea iAuditorgaAtra = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        try {
            log.info("开始同步SGXKZ报表 =====");
            List<Record> list = service.getSpxkzProject();
            //system.out.println("sgxkzlist:"+list);
            if (list != null && list.size() > 0) {
                for (Record info : list) {
                    String projectguid = info.getStr("rowguid");
                    SpxkzBanjianProject project = service.getRecordBy(projectguid);
                    //system.out.println("sgxkzproject:"+project);
                    if (project == null) {
                        SpxkzBanjianProject project1 = new SpxkzBanjianProject();
                        project1.set("status", info.getStr("status"));
                        project1.set("banjiedate", info.getStr("banjiedate"));
                        project1.set("applydate", info.getStr("applydate"));
                        project1.set("certno", info.getStr("certno"));
                        project1.set("awarddate", info.getStr("awarddate"));
                        project1.set("ouname", info.getStr("ouname"));
                        project1.set("xiangmumingchen", info.getStr("xiangmumingchen"));
                        project1.set("xiangmufenlei", info.getStr("xiangmufenlei"));
                        project1.set("lixiangpifujiguan", info.getStr("lixiangpifujiguan"));
                        project1.set("lixiangpifushijian", info.getStr("lixiangpifushijian"));
                        project1.set("jianshegongchengguihuaxukezhen", info.getStr("jianshegongchengguihuaxukezhen"));
                        project1.set("xiangmuzongjianzhumianjipingfa", info.getStr("xiangmuzongjianzhumianjipingfa"));
                        project1.set("xiangmudishangjianzhumianjipin", info.getStr("xiangmudishangjianzhumianjipin"));
                        project1.set("xiangmudixiajianzhumianjipingf", info.getStr("xiangmudixiajianzhumianjipingf"));
                        project1.set("hetongjiagewanyuan", info.getStr("hetongjiagewanyuan"));
                        project1.set("hetongkaigongriqi", info.getStr("hetongkaigongriqi"));
                        project1.set("hetongjungongriqi", info.getStr("hetongjungongriqi"));
                        project1.set("jianshedanweimingchen", info.getStr("jianshedanweimingchen"));
                        project1.set("shigongzongchengbaoqiye", info.getStr("shigongzongchengbaoqiye"));
                        project1.set("jianlidanwei", info.getStr("jianlidanwei"));
                        project1.set("shentudanwei", info.getStr("shentudanwei"));
                        project1.set("shejidanwei", info.getStr("shejidanwei"));
                        project1.set("kanchadanwei", info.getStr("kanchadanwei"));
                        project1.set("contactperson", info.getStr("contactperson"));
                        project1.set("contactphone", info.getStr("contactphone"));
                        project1.set("rowguid", info.getStr("rowguid"));
                        service.insert(project1);
                        service.updateflag(projectguid);
                        log.info("插入SGXKZ办件信息成功 =====");
                    }else {
                        project.set("status", info.getStr("status"));
                        project.set("banjiedate", info.getStr("banjiedate"));
                        project.set("applydate", info.getStr("applydate"));
                        project.set("certno", info.getStr("certno"));
                        project.set("awarddate", info.getStr("awarddate"));
                        project.set("ouname", info.getStr("ouname"));
                        project.set("xiangmumingchen", info.getStr("xiangmumingchen"));
                        project.set("xiangmufenlei", info.getStr("xiangmufenlei"));
                        project.set("lixiangpifujiguan", info.getStr("lixiangpifujiguan"));
                        project.set("lixiangpifushijian", info.getStr("lixiangpifushijian"));
                        project.set("jianshegongchengguihuaxukezhen", info.getStr("jianshegongchengguihuaxukezhen"));
                        project.set("xiangmuzongjianzhumianjipingfa", info.getStr("xiangmuzongjianzhumianjipingfa"));
                        project.set("xiangmudishangjianzhumianjipin", info.getStr("xiangmudishangjianzhumianjipin"));
                        project.set("xiangmudixiajianzhumianjipingf", info.getStr("xiangmudixiajianzhumianjipingf"));
                        project.set("hetongjiagewanyuan", info.getStr("hetongjiagewanyuan"));
                        project.set("hetongkaigongriqi", info.getStr("hetongkaigongriqi"));
                        project.set("hetongjungongriqi", info.getStr("hetongjungongriqi"));
                        project.set("jianshedanweimingchen", info.getStr("jianshedanweimingchen"));
                        project.set("shigongzongchengbaoqiye", info.getStr("shigongzongchengbaoqiye"));
                        project.set("jianlidanwei", info.getStr("jianlidanwei"));
                        project.set("shentudanwei", info.getStr("shentudanwei"));
                        project.set("shejidanwei", info.getStr("shejidanwei"));
                        project.set("kanchadanwei", info.getStr("kanchadanwei"));
                        project.set("contactperson", info.getStr("contactperson"));
                        project.set("contactphone", info.getStr("contactphone"));
                        service.update(project);
                        service.updateflag(projectguid);
                        log.info("更新SGXKZ办件信息成功 =====");
                    }
                    
                    jzgcsgxkz1 jzgcsgxkz = jzgcSgxkzService.getRecordByRowguid(projectguid);
                    AuditProjectFormSgxkz sgxkzdetail = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                    
                    if (sgxkzdetail != null) {
                    	 if (jzgcsgxkz == null) {
                         	jzgcsgxkz1 sgxkz = new jzgcsgxkz1();
                         	sgxkz.setId(projectguid);
                         	sgxkz.setRksj(new Date());
                         	if (StringUtil.isNotBlank(info.getStr("awarddate"))) {
                             	sgxkz.setLicense_number(info.getStr("awarddate"));
                         	}else {
                             	sgxkz.setLicense_number("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getDate("hetongkaigongriqi"))) {
                             	sgxkz.setValid_period_begin(sgxkzdetail.getDate("hetongkaigongriqi"));
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getDate("hetongjungongriqi"))) {
                             	sgxkz.setValid_period_end(sgxkzdetail.getDate("hetongjungongriqi"));
                         	}
                         	if (StringUtil.isNotBlank(info.getStr("certownername"))) {
                             	sgxkz.setHolder_name(info.getStr("certownername"));
                         	}else {
                             	sgxkz.setHolder_name("--");
                         	}
                         	if (StringUtil.isNotBlank(info.getStr("certownercerttype"))) {
                             	sgxkz.setCertificate_type(info.getStr("certownercerttype"));
                         	}else {
                             	sgxkz.setCertificate_type("--");
                         	}
                         	if (StringUtil.isNotBlank(info.getStr("certownerno"))) {
                             	sgxkz.setCertificate_no(info.getStr("certownerno"));
                         	}else {
                             	sgxkz.setCertificate_no("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmumingchen())) {
                             	sgxkz.setProject_name(sgxkzdetail.getXiangmumingchen());
                         	}else {
                             	sgxkz.setProject_name("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmumingchen())) {
                             	sgxkz.setGongchengmingchen(sgxkzdetail.getXiangmumingchen());
                         	}else {
                             	sgxkz.setGongchengmingchen("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmudidian())) {
                             	sgxkz.setJianshedizhi(sgxkzdetail.getXiangmudidian());
                         	}else {
                             	sgxkz.setJianshedizhi("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJiansheguimo())) {
                             	sgxkz.setJiansheguimo(sgxkzdetail.getJiansheguimo());
                         	}else {
                             	sgxkz.setJiansheguimo("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getDate("hetongjungongriqi"))) {
                             	sgxkz.setHetonggongqizhi(EpointDateUtil.convertDate2String(sgxkzdetail.getDate("hetongjungongriqi"), EpointDateUtil.DATE_FORMAT));
                         	}else {
                             	sgxkz.setHetonggongqizhi("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getDate("hetongkaigongriqi"))) {
                             	sgxkz.setJihuakaigongriqi(sgxkzdetail.getDate("hetongkaigongriqi"));
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getDate("hetongjungongriqi"))) {
                             	sgxkz.setJihuajungongriqi(sgxkzdetail.getDate("hetongjungongriqi"));
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getHetongjiagewanyuan())) {
                             	sgxkz.setHetongjiage(sgxkzdetail.getHetongjiagewanyuan());
                         	}else {
                             	sgxkz.setHetongjiage("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getStr("bz"))) {
                             	sgxkz.setZhengzhaobeizhu(sgxkzdetail.getStr("bz"));
                         	}else {
                             	sgxkz.setZhengzhaobeizhu("--");
                         	}
                         	sgxkz.setSuoshushengfen("370000");
                         	sgxkz.setSuoshudishi("370800");
                         	
                         	FrameOu frameou = iouservice.getOuByOuGuid(info.getStr("ouguid"));
                         	
                         	if (frameou != null) {
                             	sgxkz.setDept_organize_code(frameou.getOucode());
                             	sgxkz.setDept_name(frameou.getStr("ouname"));
                         	}else {
                             	sgxkz.setDept_organize_code("--");
                             	sgxkz.setDept_name("--");
                         	}
                         	
                         	AuditOrgaArea orgarea = iAuditorgaAtra.getAreaByAreacode(sgxkzdetail.getStr("Xiangmusuozaiquxian")).getResult();
                         	if(orgarea != null) {
                         		sgxkz.setDistricts_code(sgxkzdetail.getStr("Xiangmusuozaiquxian")+"000000");
                             	sgxkz.setDistricts_name(orgarea.getXiaquname());
                             	sgxkz.setSuoshuxianqu(orgarea.getXiaquname());
                         	}else {
                         		sgxkz.setSuoshuxianqu("--");
                         		sgxkz.setDistricts_code("--");
                             	sgxkz.setDistricts_name("--");
                         	}
                         
                         	sgxkz.setHolder_type("法人");
                         	sgxkz.setContact_phone("--");
                         	sgxkz.setState("insert");
                         	sgxkz.setPermit_content("--");
                         	sgxkz.setCertificate_level("A");
                         	sgxkz.setXiangmufenlei("建设项目");
                         	sgxkz.setFujianbeizhu("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmuzongjianzhumianjipingfa())) {
                             	sgxkz.setZongjianzhumianji(sgxkzdetail.getXiangmuzongjianzhumianjipingfa());
                         	}else {
                             	sgxkz.setZongjianzhumianji("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmudixiajianzhumianjipingf())) {
                             	sgxkz.setDixiajianzhumianji(sgxkzdetail.getXiangmudixiajianzhumianjipingf());
                         	}else {
                             	sgxkz.setDixiajianzhumianji("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmudishangjianzhumianjipin())) {
                             	sgxkz.setDishangjianzhumianji(sgxkzdetail.getXiangmudishangjianzhumianjipin());
                         	}else {
                             	sgxkz.setDishangjianzhumianji("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmubianhao())) {
                             	sgxkz.setGongchengxiangmubianma(sgxkzdetail.getXiangmubianhao());
                         	}else {
                             	sgxkz.setGongchengxiangmubianma("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getXiangmudaima())) {
                             	sgxkz.setGongchengxiangmudaima(sgxkzdetail.getXiangmudaima());
                         	}else {
                             	sgxkz.setGongchengxiangmudaima("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJianshedanweimingchen())) {
                             	sgxkz.setJianshedanwei(sgxkzdetail.getJianshedanweimingchen());
                         	}else {
                             	sgxkz.setJianshedanwei("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJianSheDanWeiTongYiSheHuiXinYo())) {
                             	sgxkz.setJianshedanweidaimayi(sgxkzdetail.getJianSheDanWeiTongYiSheHuiXinYo());
                         	}else {
                             	sgxkz.setJianshedanweidaimayi("--");
                         	}
                         	sgxkz.setJianshedanweidaimaleixing("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJianshedanweixiangmufuzeren())) {
                             	sgxkz.setJianshedanweixiangmufuzeren(sgxkzdetail.getJianshedanweixiangmufuzeren());
                         	}else {
                             	sgxkz.setJianshedanweixiangmufuzeren("--");
                         	}
                         	sgxkz.setJsdwxmfzrsjh_29("--");
                         	sgxkz.setJsdwxmfzrsfzjhm_30("--");
                         	sgxkz.setJsdwxmfzrsfzjhmlx("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getKanchadanwei())) {
                             	sgxkz.setKanchadanwei(sgxkzdetail.getKanchadanwei());
                         	}else {
                             	sgxkz.setKanchadanwei("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getKanChaDanWeiCode())) {
                             	sgxkz.setKanchadanweidaima(sgxkzdetail.getKanChaDanWeiCode());
                         	}else {
                             	sgxkz.setKanchadanweidaima("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getKanchadanweixiangmufuzeren())) {
                             	sgxkz.setKanchaxiangmufuzeren(sgxkzdetail.getKanchadanweixiangmufuzeren());
                         	}else {
                             	sgxkz.setKanchaxiangmufuzeren("--");
                         	}
                         	sgxkz.setKcdwxmfzrsfzjhm_35("--");
                         	sgxkz.setKcdwxmfzrsfzjhmlx("--");
                         	sgxkz.setKcdwxmfzrsjh_37("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getShejidanwei())) {
                             	sgxkz.setShejidanwei(sgxkzdetail.getShejidanwei());
                         	}else {
                             	sgxkz.setShejidanwei("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getSheJiDanWeiCode())) {
                             	sgxkz.setShejidanweidaima(sgxkzdetail.getSheJiDanWeiCode());
                         	}else {
                             	sgxkz.setShejidanweidaima("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getShejidanweixiangmufuzeren())) {
                             	sgxkz.setShejixiangmufuzeren(sgxkzdetail.getShejidanweixiangmufuzeren());
                         	}else {
                             	sgxkz.setShejixiangmufuzeren("--");
                         	}
                         	sgxkz.setSjdwxmfzrsfzjhm_41("--");
                         	sgxkz.setSjdwxmfzrsfzjhmlx("--");
                         	sgxkz.setSjdwxmfzrsjh_43("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getShigongzongchengbaoqiye())) {
                             	sgxkz.setShigongdanwei(sgxkzdetail.getShigongzongchengbaoqiye());
                         	}else {
                             	sgxkz.setShigongdanwei("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getShiGongZongChengBaoQiYeTongYiS())) {
                             	sgxkz.setShigongdanweidaima(sgxkzdetail.getShiGongZongChengBaoQiYeTongYiS());
                         	}else {
                             	sgxkz.setShigongdanweidaima("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getShigongzongchengbaoqiyexiangmu())) {
                             	sgxkz.setShigongxiangmufuzeren(sgxkzdetail.getShigongzongchengbaoqiyexiangmu());
                         	}else {
                             	sgxkz.setShigongxiangmufuzeren("--");
                         	}
                         	sgxkz.setSgdwxmfzrsfzjhm_47("--");
                         	sgxkz.setSgdwxmfzrsfzjhmlx("--");
                         	sgxkz.setSgdwxmfzrsjh_49("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJianlidanwei())) {
                             	sgxkz.setJianlidanwei(sgxkzdetail.getJianlidanwei());
                         	}else {
                             	sgxkz.setJianlidanwei("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getJianLiDanWeiCode())) {
                             	sgxkz.setJianlidanweidaima(sgxkzdetail.getJianLiDanWeiCode());
                         	}else {
                             	sgxkz.setJianlidanweidaima("--");
                         	}
                         	if (StringUtil.isNotBlank(sgxkzdetail.getZongjianligongchengshi())) {
                             	sgxkz.setZongjianligongchengshi(sgxkzdetail.getZongjianligongchengshi());
                         	}else {
                             	sgxkz.setZongjianligongchengshi("--");
                         	}
                         	sgxkz.setZjlgcssfzjhm_53("--");
                         	sgxkz.setZjlgcssfzjhmlx_54("--");
                         	sgxkz.setZjlgcssjh_55("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getGongchengzongchengbaodanwei())) {
                             	sgxkz.setGongchengzongchengbaodanwei(sgxkzdetail.getGongchengzongchengbaodanwei());
                         	}else {
                             	sgxkz.setGongchengzongchengbaodanwei("--");
                         	}
                         	sgxkz.setGczcbdwdm_57("--");
                         	if (StringUtil.isNotBlank(sgxkzdetail.getGongchengzongchengbaodanweixia())) {
                             	sgxkz.setXiangmujingli(sgxkzdetail.getGongchengzongchengbaodanweixia());
                         	}else {
                         		sgxkz.setXiangmujingli("--");
                         	}
                         	sgxkz.setGczcbdwxmfzrsfzjhm("--");
                         	sgxkz.setGczcbdwxmfzrsfzjhmlx("--");
                         	sgxkz.setGczcbdwxmfzrsjh_61("--");
                         	sgxkz.setJzgcxmmxd_62("--");
                         	jzgcSgxkzService.insert(sgxkz);
                         }
                    }
                    
                }
            }else {
                log.info("不存在需要同步的SGXKZ办件信息 =====");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("同步失败 =====");
        }
    }
    
   
}
