package com.epoint.zwfw.task.aop;

import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskjs.domain.AuditSpBasetaskJS;
import com.epoint.basic.auditsp.auditspbasetaskjs.inter.IAuditSpBasetaskJS;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.spgl.domain.SpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.xmz.thirdreporteddata.basic.audittask.meterial.inter.GxhIAuditTaskMaterial;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxclmlxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxkzxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxclmlxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxkzxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AuditTaskNewVersionAop {

    public void AfterReturning(JoinPoint point, Object returning) throws Throwable {
        IAuditTask auditTaskBasicImpl = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb = ContainerFactory.getContainInfo()
                .getComponent(ISpglDfxmsplcjdsxxxb.class);
        IAuditSpBasetask iauditspbasetask = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetask.class);
        IOuService iouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IConfigService frameConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditTaskResult iAuditTaskResult = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        GxhIAuditTaskMaterial iAuditTaskMaterial = ContainerFactory.getContainInfo()
                .getComponent(GxhIAuditTaskMaterial.class);
        ISpglsplcjdsxxxb iSpglsplcjdsxxxb = ContainerFactory.getContainInfo().getComponent(ISpglsplcjdsxxxb.class);
        ISpglspsxjbxxb iSpglspsxjbxxb = ContainerFactory.getContainInfo().getComponent(ISpglspsxjbxxb.class);
        ISpglSpsxkzxxbService iSpglSpsxkzxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpsxkzxxbService.class);
        ISpglSpsxclmlxxbService iSpglSpsxclmlxxbService = ContainerFactory.getContainInfo()
                .getComponent(ISpglSpsxclmlxxbService.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditSpBasetaskR iauditspbasetaskr = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetaskR.class);
        String isjs = ConfigUtil.getConfigValue("spgl", "isJs");
        // 处理完请求，返回内容
        @SuppressWarnings("unchecked")
        AuditCommonResult<String> result = (AuditCommonResult<String>) returning;
        if (result != null && StringUtil.isNotBlank(result.getResult())) {
            String msg = result.getResult();
            // 内网地址
            String zwfwUrl = frameConfigService.getFrameConfigValue("AS_ZWFW_ATTACH_URL");
            String OfficeWeb365_NET_URL = frameConfigService.getFrameConfigValue("OfficeWeb365_NET_URL");
            if (msg.indexOf("版本") != -1) {
                // 生成新版本了，修改数据产生的
                Object[] args = point.getArgs();
                String taskguid = (String) args[1];
                AuditTask audittask = auditTaskBasicImpl.getAuditTaskByGuid(taskguid, false).getResult();
                // 根据taskid获取在用事项版本
                audittask = auditTaskBasicImpl.selectUsableTaskByTaskID(audittask.getTask_id()).getResult();
                if (audittask != null) {
                    String is_open_v3 = frameConfigService.getFrameConfigValue("IS_OPEN_V3");
                    if (("1").equals(is_open_v3) || ("2").equals(is_open_v3)) {
                        // 3.0逻辑 Spglsplcjdsxxxb SpglSpsxjbxxb SpglSpsxkzxxb
                        // Spglsplcjdsxxxb
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("spsxbm", audittask.getItem_id());
                        String sxsjsczt = "0";
                        StringBuilder sxsbyy = new StringBuilder();// 说明
                        StringBuilder sxsbyyjbxx = new StringBuilder();// 基本信息失败原因
                        StringBuilder sxsbyykzxx = new StringBuilder();// 扩展信息失败原因
                        StringBuilder sxsbyyclml = new StringBuilder();// 材料目录失败原因
                        List<Spglsplcjdsxxxb> spglsplcjdsxxxbs = iSpglsplcjdsxxxb.getListByCondition(
                                sqlConditionUtil.getMap()).getResult();
                        for (Spglsplcjdsxxxb spglsplcjdsxxxbold : spglsplcjdsxxxbs) {
                            Spglsplcjdsxxxb spglsplcjdsxxxb = new Spglsplcjdsxxxb();
                            spglsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                            spglsplcjdsxxxb.setDfsjzj(spglsplcjdsxxxbold.getDfsjzj());
                            spglsplcjdsxxxb.setXzqhdm(spglsplcjdsxxxbold.getXzqhdm());
                            spglsplcjdsxxxb.setSplcbm(spglsplcjdsxxxbold.getSplcbm());
                            spglsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            spglsplcjdsxxxb.setSplcbbh(spglsplcjdsxxxbold.getSplcbbh());
                            spglsplcjdsxxxb.setSpjdxh(spglsplcjdsxxxbold.getSpjdxh());
                            spglsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isjs)) {
                                dealDataToJs(spglsplcjdsxxxb, audittask.getTask_id());
                            }
                            spglsplcjdsxxxb.set("sjsczt", sxsjsczt);
                            spglsplcjdsxxxb.setSbyy(sxsbyy.toString());
                            spglsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglsplcjdsxxxb.set("sync", 0);
                            iSpglsplcjdsxxxb.insert(spglsplcjdsxxxb);
                        }
                        // SpglSpsxjbxxb
                        List<SpglSpsxjbxxb> spglSpsxjbxxbs = iSpglspsxjbxxb.getListByCondition(
                                sqlConditionUtil.getMap()).getResult();
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(
                                audittask.getRowguid(), false).getResult();
                        AuditTaskResult auditTaskResult = iAuditTaskResult.getAuditResultByTaskGuid(
                                audittask.getRowguid(), false).getResult();
                        SqlConditionUtil sqlCondition = new SqlConditionUtil();
                        sqlCondition.eq("taskguid", audittask.getRowguid());
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial.getUsableMaterialListByTaskguid(
                                audittask.getRowguid()).getResult();

                        for (SpglSpsxjbxxb spglSpsxjbxxbold : spglSpsxjbxxbs) {
                            SpglSpsxjbxxb sqSpsxjbxxb = new SpglSpsxjbxxb();
                            sqSpsxjbxxb.setRowguid(UUID.randomUUID().toString());
                            sqSpsxjbxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            sqSpsxjbxxb.setDfsjzj(audittask.getRowguid());
                            sqSpsxjbxxb.setXzqhdm(audittask.getAreacode());
                            sqSpsxjbxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            sqSpsxjbxxb.setSpsxbm(audittask.getItem_id());
                            sqSpsxjbxxb.setSpsxmc(audittask.getTaskname());
                            sqSpsxjbxxb.setDybzspsxbm(spglSpsxjbxxbold.getDybzspsxbm());
                            sqSpsxjbxxb.setSxbszndz(audittask.getStr("SXBSZNDZ"));
                            sqSpsxjbxxb.setSxzxsbdz(audittask.getStr("SXZXSBDZ"));
                            sqSpsxjbxxb.setJbbm(audittask.getCatalogcode());
                            sqSpsxjbxxb.setSsbm(audittask.getTaskcode());
                            sqSpsxjbxxb.setYwblxbm(audittask.getTaskcode());
                            sqSpsxjbxxb.setYwblxkzm(audittask.getStr("YWBLXKZM"));
                            sqSpsxjbxxb.setSxlx(audittask.getStr("SXLX"));
                            if (!isInCode("事项类型", audittask.getStr("SXLX"), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("事项类型的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setSdyj(audittask.getBy_law());
                            if (StringUtil.isNotBlank(audittask.get("QLLY"))) {
                                sqSpsxjbxxb.setQlly(Integer.parseInt(audittask.get("QLLY")));
                                if (!isInCode("权利来源", audittask.getStr("QLLY"), true)) {
                                    sqSpsxjbxxb.setSjsczt(-1);
                                    sxsbyyjbxx.append("权利来源的值不在代码项之中!");
                                }
                            }

                            if (StringUtil.isNotBlank(auditTaskExtension.getUse_level())) {
                                sqSpsxjbxxb.setXscj(Integer.parseInt(auditTaskExtension.getUse_level()));
                                if (!isInCode("行使层级", auditTaskExtension.getUse_level(), true)) {
                                    sqSpsxjbxxb.setSjsczt(-1);
                                    sxsbyyjbxx.append("行使层级的值不在代码项之中!");
                                }
                            }

                            sqSpsxjbxxb.setSxzt(0 == audittask.getIs_enable() ? 2 : audittask.getIs_enable()); // 0转2
                            sqSpsxjbxxb.setSszt(audittask.getOuname());
                            sqSpsxjbxxb.setSsztxz(auditTaskExtension.getSubjectnature());
                            if (StringUtil.isNotBlank(audittask.getDeptcode())) {
                                sqSpsxjbxxb.setSsztbm(audittask.getDeptcode());
                            } else {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("实施主体编码验证失败!");
                            }
                            sqSpsxjbxxb.setSsbmssdqxzqhdm(audittask.getAreacode());
                            sqSpsxjbxxb.setWtbm(audittask.getEntrust_name());
                            sqSpsxjbxxb.setFdbjsx(audittask.getAnticipate_day());
                            sqSpsxjbxxb.setFdbjsxdw(audittask.getAnticipate_type());

                            if (!isInCode("时限单位", audittask.getAnticipate_type(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("法定办结时限单位的值不在代码项之中!");
                            }

                            sqSpsxjbxxb.setFdbjsxsm(audittask.getAnticipate_day_remark());
                            sqSpsxjbxxb.setCnbjsx(audittask.getPromise_day());
                            sqSpsxjbxxb.setCnbjsxdw(audittask.getPromise_type());

                            if (!isInCode("时限单位", audittask.getPromise_type(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("承诺办结时限单位的值不在代码项之中!");
                            }

                            sqSpsxjbxxb.setCnbjsxsm(audittask.getPromise_day_remark());
                            sqSpsxjbxxb.setSltj(audittask.getAcceptcondition());
                            if (StringUtil.isNotBlank(audittask.getStr("handle_flow"))) {
                                // 拼附件预览地址
                                List<FrameAttachInfo> attachInfo = attachService.getAttachInfoListByGuid(
                                        auditTaskExtension.getFinishproductsamples());
                                if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                    String nwattachurl = zwfwUrl
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachInfo.get(0).getAttachGuid();
                                    sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC") + ".办理流程图:"
                                            + OfficeWeb365_NET_URL + "fname=" + attachInfo.get(0).getAttachFileName()
                                            + "&furl=" + nwattachurl);
                                }

                            } else {
                                sqSpsxjbxxb.setBllc("办理流程说明:" + audittask.getStr("BLLC"));
                            }

                            sqSpsxjbxxb.setSfsf(audittask.getCharge_flag());// 质检吗
                            if (!isInCodeYesOrNo("是否", audittask.getCharge_flag(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("是否收费的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setSfyj(audittask.getCharge_basis());
                            sqSpsxjbxxb.setFwdx(audittask.getStr("fwdx"));
                            if (!isInCode("服务对象", audittask.getStr("fwdx"), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("服务对象的值不在代码项之中!");
                            }
                            sqSpsxjbxxb.setBjlx(
                                    audittask.getType() == 3 ? 4 : audittask.getType() == 4 ? 6 : audittask.getType());
                            sqSpsxjbxxb.setBlxs(audittask.getHandletype());

                            if (!isInCode("办理形式", audittask.getHandletype(), true)) {
                                sqSpsxjbxxb.setSjsczt(-1);
                                sxsbyyjbxx.append("办理形式的值不在代码项之中!");
                            }

                            if (StringUtil.isNotBlank(auditTaskExtension.getDao_xc_num())) {
                                sqSpsxjbxxb.setDbsxccs(Integer.parseInt(auditTaskExtension.getDao_xc_num()));
                            }
                            sqSpsxjbxxb.setTbcx(audittask.getSpecialprocedure());
                            sqSpsxjbxxb.setBldd(audittask.getTransact_addr());
                            sqSpsxjbxxb.setBlsj(audittask.getTransact_time());
                            sqSpsxjbxxb.setZxfs(audittask.getLink_tel());
                            sqSpsxjbxxb.setJdtsfs(audittask.getSupervise_tel());
                            sqSpsxjbxxb.setSxrq(audittask.getEffectplan());
                            sqSpsxjbxxb.setTyrq(audittask.getCancelplan());
                            sqSpsxjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            sqSpsxjbxxb.setSbyy(sxsbyyjbxx.toString());
                            sqSpsxjbxxb.setSjwxyy(sxsbyyjbxx.toString());
                            sqSpsxjbxxb.set("sync", 0);
                            iSpglspsxjbxxb.insert(sqSpsxjbxxb);

                        }
                        // SpglSpsxkzxxb
                        List<SpglSpsxkzxxb> spglSpsxkzxxbs = iSpglSpsxkzxxbService.getListByCondition(
                                sqlConditionUtil.getMap()).getResult();

                        for (SpglSpsxkzxxb spglSpsxkzxxbold : spglSpsxkzxxbs) {
                            SpglSpsxkzxxb spglSpsxkzxxb = new SpglSpsxkzxxb();
                            spglSpsxkzxxb.setRowguid(UUID.randomUUID().toString());
                            spglSpsxkzxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            spglSpsxkzxxb.setDfsjzj(spglSpsxkzxxbold.getDfsjzj());
                            spglSpsxkzxxb.setXzqhdm(audittask.getAreacode());
                            spglSpsxkzxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                            spglSpsxkzxxb.setSpsxbm(audittask.getItem_id());
                            spglSpsxkzxxb.setSfjzzhck(auditTaskExtension.get("SFJZZHCK"));
                            if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFJZZHCK"), true)) {
                                spglSpsxkzxxb.setSjsczt(-1);
                                sxsbyykzxx.append("是否进驻建设窗口的值不在代码项之中!");
                            }
                            spglSpsxkzxxb.setFwqd(auditTaskExtension.getStr("FWQD"));
                            if (!isInCode("服务渠道", auditTaskExtension.getStr("FWQD"), true)) {
                                spglSpsxkzxxb.setSjsczt(-1);
                                sxsbyykzxx.append("服务渠道的值不在代码项之中!");
                            }
                            if (StringUtil.isNotBlank(audittask.getHandlearea())) {
                                spglSpsxkzxxb.setTbfw(Integer.parseInt(audittask.getHandlearea()));
                                if (!isInCode("通办范围", audittask.getHandlearea(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("通办范围的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setLbjg(audittask.getOther_togetdept());
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKSXLX"))) {
                                spglSpsxkzxxb.setXzxksxlx(Integer.parseInt(auditTaskExtension.getStr("XZXKSXLX")));
                                if (!isInCode("行政许可类型", auditTaskExtension.getStr("XZXKSXLX"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("行政许可类型的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setZyxzxkdtj(auditTaskExtension.getStr("ZYXZXKDTJ"));
                            spglSpsxkzxxb.setGdxzxktjdyj(auditTaskExtension.getStr("GDXZXKTJDYJ"));
                            spglSpsxkzxxb.setGdsfbzdyj(auditTaskExtension.getStr("GDXZXKTJDYJ"));
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("YWZJFWSX"))) {
                                spglSpsxkzxxb.setYwzjfwsx(Integer.parseInt(auditTaskExtension.getStr("YWZJFWSX")));
                            }
                            spglSpsxkzxxb.setZjfwsxmc(auditTaskExtension.getStr("ZJFWSXMC"));
                            spglSpsxkzxxb.setSdzjfwsxdyj(auditTaskExtension.getStr("SDZJFWSXDYJ"));
                            if (auditTaskResult != null) {
                                spglSpsxkzxxb.setSpjglx(("40").equals(auditTaskResult.getResulttype().toString())
                                        ? "30"
                                        : auditTaskResult.getResulttype().toString());
                                if (!isInCodeYesOrNo("审批结果类型", auditTaskResult.getResulttype(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("审批结果类型的值不在代码项之中!");
                                }
                            }
                            spglSpsxkzxxb.setSpjgmc(auditTaskExtension.getFinishfilename());

                            List<FrameAttachInfo> attachInfo = attachService.getAttachInfoListByGuid(
                                    auditTaskExtension.getFinishproductsamples());

                            if (EpointCollectionUtils.isNotEmpty(attachInfo)) {
                                String nwattachurl = zwfwUrl
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + attachInfo.get(0).getAttachGuid();
                                spglSpsxkzxxb.setSpjgyb(
                                        OfficeWeb365_NET_URL + "fname=" + attachInfo.get(0).getAttachFileName()
                                                + "&furl=" + nwattachurl);
                            }

                            if (StringUtil.isNotBlank(auditTaskExtension.getReservationmanagement())) {
                                spglSpsxkzxxb.setSfzcyybl(
                                        Integer.parseInt(auditTaskExtension.getReservationmanagement()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getReservationmanagement(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持预约办理的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.getOnlinepayment())) {
                                spglSpsxkzxxb.setSfzcwszf(Integer.parseInt(auditTaskExtension.getOnlinepayment()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getOnlinepayment(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持网上支付的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.getIf_express())) {
                                spglSpsxkzxxb.setSfzcwlkd(Integer.parseInt(auditTaskExtension.getIf_express()));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getIf_express(), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持物流快递的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFZCZZZDBL"))) {
                                spglSpsxkzxxb.setSfzczzzdbl(auditTaskExtension.get("SFZCZZZDBL"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFZCZZZDBL"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否支持自主终端办理的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFWB"))) {
                                spglSpsxkzxxb.setSfwb(auditTaskExtension.get("SFWB"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.getStr("SFWB"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否网办的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("WSBLSD"))) {
                                spglSpsxkzxxb.setWsblsd(auditTaskExtension.get("WSBLSD"));
                                if (!isInCode("网上办理深度", auditTaskExtension.get("WSBLSD"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("网上办理深度的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFSXGZCNBL"))) {
                                spglSpsxkzxxb.setSfsxgzcnbl(auditTaskExtension.get("SFSXGZCNBL"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFSXGZCNBL"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否实行告知承诺办理的值不在代码项之中!");
                                }
                            }
                            spglSpsxkzxxb.setBxxcblyysm(auditTaskExtension.get("BXXCBLYYSM"));
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYXCKY"))) {
                                spglSpsxkzxxb.setSfxyxcky(auditTaskExtension.get("SFXYXCKY"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYXCKY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要现场勘验的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZZTZ"))) {
                                spglSpsxkzxxb.setSfxyzztz(auditTaskExtension.get("SFXYZZTZ"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZZTZ"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要组织听证的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZZTZ"))) {
                                spglSpsxkzxxb.setSfxyzbpmgpjy(auditTaskExtension.get("SFXYZZTZ"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZBPMGPJY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要招标、拍卖、挂牌交易的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYJYJCJY"))) {
                                spglSpsxkzxxb.setSfxyjyjcjy(auditTaskExtension.get("SFXYJYJCJY"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYJYJCJY"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要检验、检测、检疫的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYJD"))) {
                                spglSpsxkzxxb.setSfxyjd(auditTaskExtension.get("SFXYJD"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYJD"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要鉴定的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYZJPS"))) {
                                spglSpsxkzxxb.setSfxyzjps(auditTaskExtension.get("SFXYZJPS"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYZJPS"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要专家评审的值不在代码项之中!");
                                }
                            }
                            if (StringUtil.isNotBlank(auditTaskExtension.get("SFXYXSHGS"))) {
                                spglSpsxkzxxb.setSfxyxshgs(auditTaskExtension.get("SFXYXSHGS"));
                                if (!isInCodeYesOrNo("是否", auditTaskExtension.get("SFXYXSHGS"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("是否需要向社会公示的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setXzxkzjmc(auditTaskExtension.getStr("XZXKZJMC"));
                            spglSpsxkzxxb.setXzxkzjdyxqx(auditTaskExtension.getStr("XZXKZJDYXQX"));
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("XZXKZJDYXQXDW"))) {
                                spglSpsxkzxxb.setXzxkzjdyxqxdw(
                                        Integer.parseInt(auditTaskExtension.getStr("XZXKZJDYXQXDW")));
                                if (!isInCode("有效期限单位", auditTaskExtension.getStr("XZXKZJDYXQXDW"), true)) {
                                    spglSpsxkzxxb.setSjsczt(-1);
                                    sxsbyykzxx.append("有效期限单位的值不在代码项之中!");
                                }
                            }

                            spglSpsxkzxxb.setGdxzxkzjyxqxdyj(auditTaskExtension.getStr("GDXZXKZJYXQXDYJ"));
                            spglSpsxkzxxb.setBlxzxkzjyxsxdyq(auditTaskExtension.getStr("BLXZXKZJYXSXDYQ"));
                            spglSpsxkzxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglSpsxkzxxb.setSbyy(sxsbyykzxx.toString());
                            spglSpsxkzxxb.setSjwxyy(sxsbyykzxx.toString());
                            spglSpsxkzxxb.set("sync", "0");
                            iSpglSpsxkzxxbService.insert(spglSpsxkzxxb);
                        }
                        // SpglSpsxclmlxxb
                        if (EpointCollectionUtils.isNotEmpty(auditTaskMaterials)) {
                            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                // 事项材料目录信息
                                SpglSpsxclmlxxb spglSpsxclmlxxb = new SpglSpsxclmlxxb();
                                spglSpsxclmlxxb.setRowguid(UUID.randomUUID().toString());
                                spglSpsxclmlxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                spglSpsxclmlxxb.setDfsjzj(auditTaskMaterial.getRowguid());
                                spglSpsxclmlxxb.setXzqhdm(audittask.getAreacode());
                                spglSpsxclmlxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                                spglSpsxclmlxxb.setSpsxbm(audittask.getItem_id());
                                spglSpsxclmlxxb.setPx(auditTaskMaterial.getOrdernum());
                                spglSpsxclmlxxb.setClmlbh(
                                        StringUtil.isNotBlank(auditTaskMaterial.get("CLMLBH")) ? auditTaskMaterial.get(
                                                "CLMLBH") : auditTaskMaterial.getMaterialid());
                                spglSpsxclmlxxb.setClmc(auditTaskMaterial.getMaterialname());
                                if (StringUtil.isNotBlank(auditTaskMaterial.get("CLXS"))) {
                                    spglSpsxclmlxxb.setCllx(Integer.parseInt(auditTaskMaterial.getStr("CLXS")));
                                }
                                if (StringUtil.isNotBlank(auditTaskMaterial.getSubmittype())) {
                                    spglSpsxclmlxxb.setClxs(Integer.parseInt(auditTaskMaterial.getSubmittype()) == 10
                                            ? 2
                                            : Integer.parseInt(auditTaskMaterial.getSubmittype()) == 20 ? 1 : 3);
                                }

                                spglSpsxclmlxxb.setClbyx((auditTaskMaterial.getIs_rongque() == 1
                                        ? 3
                                        : (auditTaskMaterial.getNecessity() == 10
                                        ? 1
                                        : (auditTaskMaterial.getNecessity() == 20 ? 2 : 0))));
                                List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(
                                        auditTaskMaterial.getTemplateattachguid());
                                if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                                    String nwattachurl = zwfwUrl
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachInfos.get(0).getAttachGuid();
                                    spglSpsxclmlxxb.setKbbg(
                                            OfficeWeb365_NET_URL + "fname=" + attachInfos.get(0).getAttachFileName()
                                                    + "&furl=" + nwattachurl);

                                }
                                List<FrameAttachInfo> attachInfo1s = attachService.getAttachInfoListByGuid(
                                        auditTaskMaterial.getExampleattachguid());
                                if (EpointCollectionUtils.isNotEmpty(attachInfo1s)) {
                                    String nwattachurl1 = zwfwUrl
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachInfo1s.get(0).getAttachGuid();
                                    spglSpsxclmlxxb.setSlyb(
                                            OfficeWeb365_NET_URL + "fname=" + attachInfo1s.get(0).getAttachFileName()
                                                    + "&furl=" + nwattachurl1);
                                }
                                if (StringUtil.isNotBlank(auditTaskMaterial.getFile_source())) {
                                    spglSpsxclmlxxb.setLyqd(Integer.parseInt(auditTaskMaterial.getFile_source()));
                                }
                                spglSpsxclmlxxb.setLyqdsm(auditTaskMaterial.getStr("LYQDSM"));
                                spglSpsxclmlxxb.setZzclfs(auditTaskMaterial.getPage_num());
                                spglSpsxclmlxxb.setZzclgg(auditTaskMaterial.getStr("ZZCLGG"));
                                spglSpsxclmlxxb.setTbxz(auditTaskMaterial.getFile_explian());
                                spglSpsxclmlxxb.setSlbz(auditTaskMaterial.getStandard());
                                spglSpsxclmlxxb.setYqtgcldyj(auditTaskMaterial.getStr("YQTGCLDYJ"));
                                spglSpsxclmlxxb.setBz(auditTaskMaterial.getStr("BZ"));
                                spglSpsxclmlxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                spglSpsxclmlxxb.setSbyy(sxsbyyclml.toString());
                                spglSpsxclmlxxb.setSjwxyy(sxsbyyclml.toString());
                                spglSpsxclmlxxb.setSync(0);
                                iSpglSpsxclmlxxbService.insert(spglSpsxclmlxxb);

                            }

                        }

                    }
                    if (("1").equals(is_open_v3) || ("0").equals(is_open_v3)) {
                        List<SpglDfxmsplcjdsxxxb> list = ispgldfxmsplcjdsxxxb.getNeedAddNewVersionByItemId(
                                audittask.getItem_id()).getResult();
                        for (SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb1 : list) {
                            // 获取标准事项rowguid,根据Task_id，和businesstype
                            List<String> areacodelist = new ArrayList<String>();
                            areacodelist.add(audittask.getAreacode());
                            List<Record> result2 = iauditspbasetaskr.getBasetaskByAreacodelistandTaskidNew(
                                    audittask.getTask_id(), areacodelist, "").getResult();
                            if (!result2.isEmpty()) {
                                SpglDfxmsplcjdsxxxb spgldfxmsplcjdsxxxb = new SpglDfxmsplcjdsxxxb();
                                spgldfxmsplcjdsxxxb.setRowguid(UUID.randomUUID().toString());
                                spgldfxmsplcjdsxxxb.setDfsjzj(result2.get(0).getStr("BASETASKGUID"));
                                spgldfxmsplcjdsxxxb.setXzqhdm(spglDfxmsplcjdsxxxb1.getXzqhdm());
                                spgldfxmsplcjdsxxxb.setSplcbm(spglDfxmsplcjdsxxxb1.getSplcbm());
                                spgldfxmsplcjdsxxxb.setSplcbbh(spglDfxmsplcjdsxxxb1.getSplcbbh());
                                spgldfxmsplcjdsxxxb.setSpjdxh(spglDfxmsplcjdsxxxb1.getSpjdxh());
                                spgldfxmsplcjdsxxxb.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                                spgldfxmsplcjdsxxxb.setSpsxmc(audittask.getTaskname());
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isjs)) {
                                    dealDataToJs(spgldfxmsplcjdsxxxb, audittask.getTask_id());
                                }
                                spgldfxmsplcjdsxxxb.setSpsxbm(audittask.getItem_id());
                                AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskByrowguid(
                                        result2.get(0).getStr("BASETASKGUID")).getResult();
                                if (basetask != null) {
                                    spgldfxmsplcjdsxxxb.setDybzspsxbm(basetask.getTaskcode());
                                    spgldfxmsplcjdsxxxb.setSflcbsx(
                                            StringUtil.isNotBlank(basetask.getSflcbsx())
                                                    ? Integer.valueOf(basetask.getSflcbsx())
                                                    : ZwfwConstant.CONSTANT_INT_ZERO); // 是否里程碑事项。默认0
                                } else {
                                    spgldfxmsplcjdsxxxb.setSflcbsx(0);
                                    spgldfxmsplcjdsxxxb.setDybzspsxbm("9990");
                                }
                                spgldfxmsplcjdsxxxb.setSfsxgzcnz(ZwfwConstant.CONSTANT_INT_ZERO);// 是否实行告知承诺制 默认0

                                spgldfxmsplcjdsxxxb.setBjlx(audittask.getType());
                                if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1
                                        && audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                                    spgldfxmsplcjdsxxxb.setSqdx(3);
                                } else {
                                    if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_GR) != -1) {
                                        spgldfxmsplcjdsxxxb.setSqdx(1);
                                    } else if (audittask.getApplyertype().indexOf(ZwfwConstant.APPLAYERTYPE_QY) != -1) {
                                        spgldfxmsplcjdsxxxb.setSqdx(2);
                                    }
                                }
                                spgldfxmsplcjdsxxxb.setBljgsdfs("2"); // 办理送达方式   默认 申请对象窗口领取
                                spgldfxmsplcjdsxxxb.setSpsxblsx(audittask.getPromise_day());
                                spgldfxmsplcjdsxxxb.setSpbmbm(
                                        iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                                spgldfxmsplcjdsxxxb.setSpbmmc(audittask.getOuname());
                                spgldfxmsplcjdsxxxb.setQzspsxbm(null);
                                spgldfxmsplcjdsxxxb.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                                spgldfxmsplcjdsxxxb.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                                ispgldfxmsplcjdsxxxb.insert(spgldfxmsplcjdsxxxb);
                            }
                        }
                    }

                }

            }
        }
    }

    public void dealDataToJs(Record record, String taskid) {
        IAuditSpBasetaskJS iauditspbasetaskjs = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpBasetaskJS.class);
        AuditSpBasetaskJS auditspbasetaskjs = iauditspbasetaskjs.getAuditSpBasetaskJSByTaskid(taskid).getResult();
        if (auditspbasetaskjs != null) {
            record.set("spsxmc", auditspbasetaskjs.getTaskname());
        }
    }

    /**
     * @param codename 主项名称
     * @param value    子项值
     * @param a        是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCode(String codename, Object value, boolean a) {
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        codename = "国标_" + codename;
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

    /**
     * @param codename 主项名称
     * @param value    子项值
     * @param a        是否必须
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isInCodeYesOrNo(String codename, Object value, boolean a) {
        if (a) {
            if (value == null) {
                throw new RuntimeException(codename + "的值不能为空！");
            }
        }
        String v = value.toString();
        ICodeItemsService icodeitemsservice = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        String s = icodeitemsservice.getItemTextByCodeName(codename, v);
        return StringUtil.isNotBlank(s);
    }

}
