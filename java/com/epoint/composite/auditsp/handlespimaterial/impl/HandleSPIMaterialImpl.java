package com.epoint.composite.auditsp.handlespimaterial.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.AttachUtil;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@Service
@Component
public class HandleSPIMaterialImpl implements IHandleSPIMaterial
{

    @Override
    public AuditCommonResult<Void> initProjctMaterial(String businessGuid, String subAppGuid, String taskGuid,
            String projectGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        try {

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            if (auditTaskMaterials != null) {
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    auditProjectMaterial.clear();
                    String cliengguid = UUID.randomUUID().toString();

                    auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                    auditProjectMaterial.setTaskguid(taskGuid);
                    auditProjectMaterial.setProjectguid(projectGuid);
                    auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                    auditProjectMaterial.setStatus(10);
                    auditProjectMaterial.setAuditstatus("0");
                    auditProjectMaterial.setIs_rongque(0);
                    auditProjectMaterial.setCliengguid(cliengguid);
                    auditProjectMaterial.setAttachfilefrom("1");
                    auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                    IAuditSpIMaterial spIMaterialService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditSpIMaterial.class);
                    AuditSpIMaterial auditSpIMaterial = spIMaterialService
                            .getSpIMaterialByID(businessGuid, subAppGuid, auditTaskMaterial.getMaterialid())
                            .getResult();
                    if (auditSpIMaterial != null) {
                        // 不是审批结果
                        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getResult())) {
                            // 提交电子材料状态则复制
                            if ((("20").equals(auditSpIMaterial.getStatus())
                                    || ("25").equals(auditSpIMaterial.getStatus()))
                                    && StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                                IAttachService attachService = ContainerFactory.getContainInfo()
                                        .getComponent(IAttachService.class);
                                attachService.copyAttachByClientGuid(auditSpIMaterial.getCliengguid(), null, null,
                                        cliengguid);
                            }
                            auditProjectMaterial.setStatus(Integer.parseInt(auditSpIMaterial.getStatus()));
                        }
                        auditProjectMaterial.setIs_rongque(Integer.parseInt(auditSpIMaterial.getAllowrongque()));
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getShared())) {
                            auditProjectMaterial.setSharematerialiguid(auditSpIMaterial.getMaterialguid());
                        }
                        auditProjectMaterial.setCertinfoinstanceguid(auditSpIMaterial.getCertinfoinstanceguid());
                    }
                    auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> checkMaterial(String subAppGuid, String projectGuid) {
        AuditCommonResult<List<String>> result = new AuditCommonResult<>();
        List<String> materials = new ArrayList<>();
        try {
            IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectMaterial.class);
            List<AuditProjectMaterial> projectMaterials = projectMaterialService.selectProjectMaterial(projectGuid)
                    .getResult();
            IAuditSpIMaterial spIMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpIMaterial.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            if (projectMaterials != null) {
                for (AuditProjectMaterial auditProjectMaterial : projectMaterials) {
                    AuditSpIMaterial auditSpIMaterial = null;
                    // 共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.getSharematerialiguid())) {
                        auditSpIMaterial = spIMaterialService
                                .getSpIMaterialByMaterialGuid(subAppGuid, auditProjectMaterial.getSharematerialiguid())
                                .getResult();
                    }
                    else {
                        IAuditTaskMaterial taskMaterialService = ContainerFactory.getContainInfo()
                                .getComponent(IAuditTaskMaterial.class);
                        String materialID = taskMaterialService
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getRowguid()).getResult()
                                .getMaterialid();
                        auditSpIMaterial = spIMaterialService.getSpIMaterialByMaterialGuid(subAppGuid, materialID)
                                .getResult();
                    }
                    if (auditSpIMaterial != null) {
                        // 提交状态不一致
                        if (!auditSpIMaterial.getStatus().equals(String.valueOf(auditProjectMaterial.getStatus()))) {
                            materials.add(auditProjectMaterial.getRowguid());
                        }
                        // 提交了电子件则继续比较
                        else if ("20".equals(auditSpIMaterial.getStatus())
                                || "25".equals(auditSpIMaterial.getStatus())) {
                            int cntSpIMaterial = attachService
                                    .getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                            int cntProjectMaterial = attachService
                                    .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
                            // 附件材料数不相等
                            if (cntSpIMaterial != cntProjectMaterial) {
                                materials.add(auditProjectMaterial.getRowguid());
                            }
                            else {
                                List<FrameAttachInfo> spInfos = attachService
                                        .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                List<FrameAttachInfo> projectInfos = attachService
                                        .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                spInfos.sort((a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                projectInfos
                                        .sort((a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                                for (int i = 0; i < spInfos.size(); i++) {
                                    // AttachStorageGuid不同说明附件改动过
                                    if (!spInfos.get(i).getAttachStorageGuid()
                                            .equals(projectInfos.get(i).getAttachStorageGuid())) {
                                        materials.add(auditProjectMaterial.getRowguid());
                                    }
                                }
                            }
                        }
                    }

                }
            }
            result.setResult(materials);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> initSubappMaterial(String subAppGuid, String businessGuid,
            String biGuid, String phaseGuid, String ownerGuid, String certNum) {
        AuditCommonResult<List<AuditSpIMaterial>> result = new AuditCommonResult<List<AuditSpIMaterial>>();
        List<AuditSpIMaterial> listSpIMaterial = new ArrayList<AuditSpIMaterial>();
        // 实例化各个接口
        IAuditSpITask auditTaskIService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditSpShareMaterialRelation auditMatrerialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);
        IAuditSpShareMaterial auditMatrerialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterial.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditSpISubapp auditSubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditSpOptionService iauditspoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptionService.class);
        try {
            List<AuditSpIMaterial> materialslist = auditSpIMaterialService.getAllSpIMaterialBySubappGuid(subAppGuid)
                    .getResult();
            // 记录materialguid和clingguid重新初始化使用
            Map<String, String> materialguidmap = new HashMap<>();
            Map<String, String> materialstatusmap = new HashMap<>();
            if (materialslist != null && !materialslist.isEmpty()) {
                materialguidmap = materialslist.stream()
                        .collect(Collectors.toMap(AuditSpIMaterial::getMaterialguid, AuditSpIMaterial::getCliengguid));
                materialstatusmap = materialslist.stream()
                        .collect(Collectors.toMap(AuditSpIMaterial::getMaterialguid, AuditSpIMaterial::getStatus));
                // 删除材料重新初始化
                auditSpIMaterialService.deleteSpIMaterialBySubappguid(subAppGuid);
            }
            // 第一步，获取所有的已选择办理事项,并将材料进行拼接处理
            List<AuditSpITask> listTask = auditTaskIService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            // 添加标准事项过滤
            listTask = listTask.stream().filter(distinctByKey(AuditSpITask::getTaskguid)).collect(Collectors.toList());
            AuditSpISubapp subapp = auditSubappService.getSubappByGuid(subAppGuid).getResult();

            if (listTask.isEmpty()) {
                result.setBusinessFail("未选择办理事项");
                return result;
            }
            listTask.sort((b, a) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            int ordernum = 0;
            // 获取map 存储taskguid和材料数据
            Map<String, List<AuditTaskMaterial>> mapt_m = new HashMap<>();
            // 记录所有的材料materialid
            List<String> materialids = new ArrayList<>();
            // 记录未配置在情形中的材料标识
            List<String> unmatchmaterialids = new ArrayList<>();
            // 记录记录所有材料
            List<String> allmaterialids = new ArrayList<>();

            List<String> necessity_materialids = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                        .selectTaskMaterialListByTaskGuid(auditSpiTask.getTaskguid(), false).getResult();
                for (AuditTaskMaterial auditTaskMaterial : listMaterial) {
                    if (!materialids.contains(auditTaskMaterial.getMaterialid())) {
                        materialids.add(auditTaskMaterial.getMaterialid());
                    }
                }
                mapt_m.put(auditSpiTask.getTaskguid(), listMaterial);
            }
            // 记录需要提交材料的option
            Map<String, AuditSpOption> optionmap = new HashMap<>();
            // 查找多有关联了如下材料的option
            List<AuditSpOption> listoption = iauditspoptionservice.getSpOptionListByMaterialids(materialids)
                    .getResult();
            for (AuditSpOption auditSpOption : listoption) {
                String material = auditSpOption.getMaterialids();
                if (StringUtil.isBlank(material)) {
                    continue;
                }
                String[] materials = material.split(";");
                for (String string : materials) {
                    if (!allmaterialids.contains(string)) {
                        allmaterialids.add(string);
                    }
                }
            }
            List<String> materialidscopy = new ArrayList<>();
            // 复制集合
            unmatchmaterialids.addAll(materialids);
            materialidscopy.addAll(materialids);
            // 获取配置了option的材料
            materialidscopy.retainAll(allmaterialids);
            // 移除配置在option里的材料
            unmatchmaterialids.removeAll(materialidscopy);

            // 创建guidlist
            List<String> optionGuidList = new ArrayList<>();
            // 匹配单独的option
            for (AuditSpOption auditSpOption : listoption) {
                // 匹配成功记录下optionguid
                if (iauditspoptionservice
                        .checkOptionBySubappguid(subAppGuid, subapp.getYewuguid(), auditSpOption.getEqualvalue())
                        .getResult()) {
                    optionmap.put(auditSpOption.getRowguid(), auditSpOption);
                    optionGuidList.add(auditSpOption.getRowguid());
                }
            }
            // 匹配情形以及添加情形到audit_task_csae表
            String caseguid = iauditspoptionservice.checkIsNewSpCase(optionGuidList).getResult();
            if (StringUtil.isBlank(caseguid)) {
                caseguid = iauditspoptionservice.addNewSpCase(optionGuidList).getResult();
            }
            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
            auditSpISubapp.setRowguid(subAppGuid);
            auditSpISubapp.setCaseguid(caseguid);
            auditSubappService.updateAuditSpISubapp(auditSpISubapp);

            materialids.clear();
            // 查找需要提交的选项关联的类别再关联选项
            optionmap = iauditspoptionservice.getAllOptionByOptionMap(optionmap).getResult();
            // 遍历map将其中的材料添加进materialids中
            for (AuditSpOption auditSpOption : optionmap.values()) {
                String material = auditSpOption.getMaterialids();
                if (StringUtil.isBlank(material)) {
                    continue;
                }
                String[] materials = material.split(";");
                for (String string : materials) {
                    if (!materialids.contains(string)) {
                        materialids.add(string);
                    }
                }
            }

            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = mapt_m.get(auditSpiTask.getTaskguid());
                listMaterial.sort((b, a) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                        .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
                for (AuditTaskMaterial auditMaterial : listMaterial) {
                    // 根据情形过滤,如果是非必须，并且不再上面材料的提交范围中，则不初始化这个磁疗
                    if (Integer.valueOf(ZwfwConstant.NECESSITY_SET_NO).equals(auditMaterial.getNecessity())) {
                        if (!materialids.contains(auditMaterial.getMaterialid())
                                && !unmatchmaterialids.contains(auditMaterial.getMaterialid())) {
                            continue;
                        }
                    }
                    else {
                        if (!necessity_materialids.contains(auditMaterial.getMaterialid())) {
                            necessity_materialids.add(auditMaterial.getMaterialid());
                        }
                    }

                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    // 直接添加
                    auditSpIMaterial.setEffictiverange("");
                    auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialguid(auditMaterial.getMaterialid());

                    auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                    auditSpIMaterial
                            .setAllowrongque(auditMaterial.getIs_rongque() == null ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : String.valueOf(auditMaterial.getIs_rongque()));
                    auditSpIMaterial.setMaterialtype(auditMaterial.getMaterialtype());
                    auditSpIMaterial.setBiguid(biGuid);
                    auditSpIMaterial.setBusinessguid(businessGuid);
                    auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                    auditSpIMaterial.setOperatedate(new Date());
                    auditSpIMaterial.setPhaseguid(phaseGuid);
                    auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialname(auditMaterial.getMaterialname());
                    auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    // TODO 这里需要改成常量
                    auditSpIMaterial.setSubmittype(auditMaterial.getSubmittype());

                    // 判断如果是在 情形匹配材料里面需要设置成必须
                    if (materialids.contains(auditMaterial.getMaterialid())) {
                        auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                    }
                    else {
                        auditSpIMaterial.setNecessity(String.valueOf(auditMaterial.getNecessity()));
                    }

                    auditSpIMaterial.setSubappguid(subAppGuid);

                    auditSpIMaterial.setOrdernum(ordernum);
                    ordernum += 10;

                    listSpIMaterial.add(auditSpIMaterial);
                }
            }
            // 第二步，获取所有的材料对应关系 modified 查询是否在所选的事项关系中，不存在的不加
            List<AuditSpShareMaterialRelation> listRelation = auditMatrerialRelationService
                    .selectByBusinessGuid(businessGuid).getResult();
            List<AuditTask> tasklist = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditSpiTask.getTaskguid(), false)
                        .getResult();
                tasklist.add(audittask);
            }
            List<AuditSpShareMaterialRelation> listRelationnew = new ArrayList<>();
            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listRelation) {
                for (AuditTask auditTask : tasklist) {
                    // 去除掉，不需要提交的材料的关系,结果直接加入
                    if (auditSpShareMaterialRelation.getTaskid().equals(auditTask.getTask_id())
                            && ("20".equals(auditSpShareMaterialRelation.getMaterialtype())
                                    || necessity_materialids.contains(auditSpShareMaterialRelation.getMaterialid())
                                    || materialids.contains(auditSpShareMaterialRelation.getMaterialid())
                                    || unmatchmaterialids.contains(auditSpShareMaterialRelation.getMaterialid()))) {
                        listRelationnew.add(auditSpShareMaterialRelation);
                        break;
                    }
                }
            }
            listRelation = listRelationnew;
            // 第三步，筛选材料，并进行初始化
            // 这里可以确定，第一步取出的材料数量已经是材料的上限了，不会更多了，这里的做法是遍历关系表，然后将额外是共享材料的材料进行删除
            Iterator<AuditSpShareMaterialRelation> iterator = listRelation.iterator();

            // 存在多个材料关联一个共享材料，一个材料情形匹配为必须，则这个共享材料就是必须，声明map
            List<String> necessitymaterial = new ArrayList<>();
            while (iterator.hasNext()) {
                AuditSpShareMaterialRelation relation = iterator.next();
                AuditSpShareMaterial shareMaterial = auditMatrerialService
                        .getAuditSpShareMaterialByShareMaterialGuid(relation.getSharematerialguid(), businessGuid)
                        .getResult();
                // 如果是全局共享材料，那就只要删选一下是不是还存在重复的单个材料
                listSpIMaterial.removeIf(material -> {
                    return !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                            && material.getMaterialguid().equals(relation.getSharematerialguid());
                });
                if (shareMaterial != null) {
                    // 如果是主题内的，那就先把单个的，共享材料设置是启用的
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        // 删除重复的共享材料
                        listSpIMaterial.removeIf(material -> {
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                                    && material.getMaterialguid().equals(relation.getSharematerialguid())) {
                                return true;
                            }
                            else {
                                return relation.getMaterialid().equals(material.getMaterialguid());
                            }
                        });
                        // 然后再添加上这个共享材料
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        if ("2".equals(shareMaterial.getEffictiverange())) {
                            auditSpIMaterial.setEffictiverange("20");
                        }
                        else {
                            auditSpIMaterial.setEffictiverange("10");
                        }
                        auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ONE);
                        auditSpIMaterial.setMaterialguid(relation.getSharematerialguid());

                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        auditSpIMaterial.setBiguid(biGuid);
                        auditSpIMaterial.setBusinessguid(businessGuid);
                        auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                        // 如果是必須提交的和情形匹配上需要提交的设置为必须,否则为非必须
                        if (necessity_materialids.contains(relation.getMaterialid())
                                || materialids.contains(relation.getMaterialid())
                                || necessitymaterial.contains(auditSpIMaterial.getMaterialguid())) {
                            auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                            necessitymaterial.add(auditSpIMaterial.getMaterialguid());
                        }
                        else {
                            auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        }
                        auditSpIMaterial.setPhaseguid(phaseGuid);
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());
                        // TODO 这里需要改成常量
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setSubappguid(subAppGuid);
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                        listSpIMaterial.add(auditSpIMaterial);
                    }
                }
                // 如果这个材料不是结果的，那就去掉了
                if (!"20".equals(relation.getMaterialtype())) {
                    iterator.remove();
                }
            }
            List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
            spIMaterials.addAll(listSpIMaterial);
            // 插入数据
            for (AuditSpIMaterial spiMaterial : listSpIMaterial) {
                // 普通材料，直接插入
                if (StringUtil.isBlank(spiMaterial.getEffictiverange())) {
                    if (StringUtil.isNotBlank(materialguidmap.get(spiMaterial.getMaterialguid()))) {
                        spiMaterial.setCliengguid(materialguidmap.get(spiMaterial.getMaterialguid()));
                        spiMaterial.setStatus(materialstatusmap.get(spiMaterial.getMaterialguid()));
                    }
                    auditSpIMaterialService.addSpIMaterial(spiMaterial);
                }
                // 主题内共享的
                else if ("20".equals(spiMaterial.getEffictiverange())) {
                    // 跨阶段共享
                    AuditSpIMaterial lastSpIMaterial = auditSpIMaterialService
                            .getLastSpIMaterial(biGuid, spiMaterial.getMaterialguid()).getResult();
                    if (lastSpIMaterial != null) {
                        spiMaterial.setStatus(lastSpIMaterial.getStatus());
                        if (attachService.getAttachCountByClientGuid(lastSpIMaterial.getCliengguid()) > 0) {
                            String newCliengguid = UUID.randomUUID().toString();
                            attachService.copyAttachByClientGuid(lastSpIMaterial.getCliengguid(), null, null,
                                    newCliengguid);
                            spiMaterial.setCliengguid(newCliengguid);
                        }
                    }
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    // 如果发生了变化，说明是包含结果的
                    if (length > listRelation.size()) {
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        spIMaterials.add(auditSpIMaterial);
                        spIMaterials.remove(spiMaterial);
                        if (StringUtil.isNotBlank(materialguidmap.get(auditSpIMaterial.getMaterialguid()))) {
                            spiMaterial.setCliengguid(materialguidmap.get(auditSpIMaterial.getMaterialguid()));
                            spiMaterial.setStatus(materialstatusmap.get(auditSpIMaterial.getMaterialguid()));
                        }
                        auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                    }
                    else {
                        if (StringUtil.isNotBlank(materialguidmap.get(spiMaterial.getMaterialguid()))) {
                            spiMaterial.setCliengguid(materialguidmap.get(spiMaterial.getMaterialguid()));
                            spiMaterial.setStatus(materialstatusmap.get(spiMaterial.getMaterialguid()));
                        }
                        auditSpIMaterialService.addSpIMaterial(spiMaterial);
                    }

                }
                // 全局共享的
                else {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    if (length > listRelation.size()) {
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 需要替换成主题中配置的，这里还有个判断，如果这个共享材料实际没有多个进行匹配的，那就需要用原本事项的配置
                    AuditSpShareMaterial shareMaterial = auditMatrerialService
                            .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                    businessGuid)
                            .getResult();
                    if (shareMaterial != null && !shareMaterial.isEmpty()
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        // auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());

                    }

                    if (shareMaterial != null) {
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                    }
                    // 给材料默认的关联certclientguid
                    if (shareMaterial != null) {
                        auditSpIMaterial.setCertinfoinstanceguid(UUID.randomUUID().toString());
                    }
                    spIMaterials.add(auditSpIMaterial);
                    spIMaterials.remove(spiMaterial);
                    if (StringUtil.isNotBlank(materialguidmap.get(auditSpIMaterial.getMaterialguid()))) {
                        auditSpIMaterial.setCliengguid(materialguidmap.get(auditSpIMaterial.getMaterialguid()));
                        auditSpIMaterial.setStatus(materialstatusmap.get(auditSpIMaterial.getMaterialguid()));
                    }
                    auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                }
            }
            auditSubappService.updateInitmaterial(subAppGuid);
            // 第一次显示的时候移除掉做为结果的
            spIMaterials.removeIf(material -> {
                return !ZwfwConstant.CONSTANT_STR_ZERO.equals(material.getResult());
            });
            spIMaterials.sort((a, b) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                    .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
            result.setResult(auditSpIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult());
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> getAllSubappMaterial(String subAppGuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> getPatchSubappMaterial(String subAppGuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AuditCommonResult<String> updateMaterialStatus(String miGuid, int stauts) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        try {

            auditSpIMaterialService.updateSpIMaterialStatus(miGuid, stauts);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> updateIMaterialBuzheng(String subAppGuid, String projectMaterialGuids,
            String projectGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        IAuditProjectMaterial projectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAuditSpIMaterial spIMaterialService = ContainerFactory.getContainInfo().getComponent(IAuditSpIMaterial.class);
        try {
            String[] materials = projectMaterialGuids.split(",");
            for (String material : materials) {
                if (StringUtil.isNotBlank(material)) {
                    AuditProjectMaterial auditProjectMaterial = projectMaterialService
                            .getProjectMaterialDetail(material, projectGuid).getResult();
                    AuditSpIMaterial auditSpIMaterial = null;
                    // 共享材料
                    if (StringUtil.isNotBlank(auditProjectMaterial.getSharematerialiguid())) {
                        auditSpIMaterial = spIMaterialService
                                .getSpIMaterialByMaterialGuid(subAppGuid, auditProjectMaterial.getSharematerialiguid())
                                .getResult();
                    }
                    // 普通材料
                    else {
                        IAuditTaskMaterial taskMaterialService = ContainerFactory.getContainInfo()
                                .getComponent(IAuditTaskMaterial.class);
                        String materialID = taskMaterialService
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult()
                                .getMaterialid();
                        auditSpIMaterial = spIMaterialService.getSpIMaterialByMaterialGuid(subAppGuid, materialID)
                                .getResult();
                    }
                    if (auditSpIMaterial != null) {
                        auditSpIMaterial.setIsbuzheng("1");
                        spIMaterialService.updateSpIMaterial(auditSpIMaterial);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public AuditCommonResult<Void> initSpIMaterial() {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateResultStatusAndAttach(String subAppGuid, String materialGuid, int status,
            String cliengguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        try {

            String newCliengGuid = "";
            if (StringUtil.isNotBlank(cliengguid)) {
                newCliengGuid = UUID.randomUUID().toString();
                // 3、复制材料
                attachService.copyAttachByClientGuid(cliengguid, null, null, newCliengGuid);
            }
            auditSpIMaterialService.updateSpIResultStatus(subAppGuid, materialGuid, status, newCliengGuid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> is_ExistSPCert(List<AuditSpIMaterial> listMaterial, String ownerGuid,
            String certNum) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        // ICertConfigExternal certConfigExternalImpl =
        // ContainerFactory.getContainInfo()
        // .getComponent(ICertConfigExternal.class);
        // ICertInfoExternal certInfoExternalImpl =
        // ContainerFactory.getContainInfo()
        // .getComponent(ICertInfoExternal.class);
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            if (listMaterial != null && !listMaterial.isEmpty()) {
                for (AuditSpIMaterial auditSpIMaterial : listMaterial) {

                    // Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditSpIMaterial.getMaterialguid())
                            && !"20".equals(auditSpIMaterial.getSubmittype())) {

                        // CertCatalog certCatalog = certConfigExternalImpl
                        // .getCatalogByCatalogid(auditSpIMaterial.getMaterialguid());
                        //
                        // if (certCatalog != null) {
                        // String certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                        // flag =
                        // certInfoExternalImpl.isExistCertByOwner(auditSpIMaterial.getMaterialguid(),
                        // certownertype, "", certNum, null);
                        // if (flag) {
                        // result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        // return result;
                        // } else {
                        // result.setResult("2");
                        // }
                        // }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> is_ExistSPCert(List<AuditSpIMaterial> listMaterial, String ownerGuid,
            String certNum, String areacode) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            if (listMaterial != null && !listMaterial.isEmpty()) {
                for (AuditSpIMaterial auditSpIMaterial : listMaterial) {

                    Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditSpIMaterial.getMaterialguid())
                            && !"20".equals(auditSpIMaterial.getSubmittype())) {

                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditSpIMaterial.getMaterialguid(), areacode);

                        if (certCatalog != null) {
                            String certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                            flag = certInfoExternalImpl.isExistCertByOwner(auditSpIMaterial.getMaterialguid(),
                                    certownertype, "", certNum, null);
                            if (flag) {
                                result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                                return result;
                            }
                            else {
                                result.setResult("2");
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> is_ExistSPCert(List<AuditSpIMaterial> listMaterial, String ownerGuid,
            String certNum, String areacode, String applyerType) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ICertConfigExternal certConfigExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertConfigExternal.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        // 默认不存在共享材料
        result.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
        // 遍历工作流配置材料
        try {
            if (listMaterial != null && !listMaterial.isEmpty()) {
                for (AuditSpIMaterial auditSpIMaterial : listMaterial) {

                    Boolean flag = false;
                    // 事项材料存在共享材料
                    if (StringUtil.isNotBlank(auditSpIMaterial.getMaterialguid())
                            && !"20".equals(auditSpIMaterial.getSubmittype())) {

                        CertCatalog certCatalog = certConfigExternalImpl
                                .getCatalogByCatalogid(auditSpIMaterial.getMaterialguid(), areacode);

                        if (certCatalog != null) {
                            flag = certInfoExternalImpl.isExistCertByOwner(auditSpIMaterial.getMaterialguid(),
                                    applyerType, "", certNum, null);
                            if (flag) {
                                result.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                                return result;
                            }
                            else {
                                result.setResult("2");
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateResultStatusAndAttachAndCert(String subAppGuid, String materialGuid,
            int status, String cliengguid, String certinstanceguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        try {

            String newCliengGuid = "";
            if (StringUtil.isNotBlank(cliengguid)) {
                newCliengGuid = UUID.randomUUID().toString();
                // 3、复制材料
                attachService.copyAttachByClientGuid(cliengguid, null, null, newCliengGuid);
            }
            auditSpIMaterialService.updateSpIResultStatusAndCert(subAppGuid, materialGuid, status, newCliengGuid,
                    certinstanceguid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateResultStatusAndAttach(String subAppGuid, String materialGuid, int status,
            String cliengguid, String certrowguid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        try {

            String newCliengGuid = "";
            if (StringUtil.isNotBlank(cliengguid)) {
                newCliengGuid = UUID.randomUUID().toString();
                // 3、复制材料
                attachService.copyAttachByClientGuid(cliengguid, null, null, newCliengGuid);
            }
            auditSpIMaterialService.updateSpIResultStatus(subAppGuid, materialGuid, status, newCliengGuid, certrowguid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> initProjctMaterial(List<String> materiallist, String businessGuid, String subAppGuid,
            String taskGuid, String projectGuid) {
        AuditCommonResult<Void> result = new AuditCommonResult<>();
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        IAuditSpOptionService iauditspoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptionService.class);
        try {

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            if (auditTaskMaterials != null) {
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (materiallist == null) {
                        continue;
                    }
                    // 材料非必须则判断材料是否配置在情形里
                    boolean hasoption = false;
                    if (ZwfwConstant.NECESSITY_SET_NO.equals(auditTaskMaterial.getNecessity().toString())) {
                        hasoption = iauditspoptionservice.hasOptionByMaterialid(auditTaskMaterial.getMaterialid());
                        // 材料非必须，没有配置在情形中，并且没有匹配中情形
                        if (!materiallist.contains(auditTaskMaterial.getMaterialid()) && hasoption) {
                            continue;
                        }
                    }

                    auditProjectMaterial.clear();
                    String cliengguid = UUID.randomUUID().toString();
                    auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                    auditProjectMaterial.setTaskguid(taskGuid);
                    auditProjectMaterial.setProjectguid(projectGuid);
                    auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                    auditProjectMaterial.setStatus(10);
                    auditProjectMaterial.setAuditstatus("0");
                    auditProjectMaterial.setIs_rongque(0);
                    auditProjectMaterial.setCliengguid(cliengguid);
                    auditProjectMaterial.setAttachfilefrom("1");
                    auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                    IAuditSpIMaterial spIMaterialService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditSpIMaterial.class);
                    AuditSpIMaterial auditSpIMaterial = spIMaterialService
                            .getSpIMaterialByID(businessGuid, subAppGuid, auditTaskMaterial.getMaterialid())
                            .getResult();
                    if (auditSpIMaterial != null) {
                        // 不是审批结果
                        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getResult())) {
                            // 提交电子材料状态则复制
                            if ((("20").equals(auditSpIMaterial.getStatus())
                                    || ("25").equals(auditSpIMaterial.getStatus()))
                                    && StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                                IAttachService attachService = ContainerFactory.getContainInfo()
                                        .getComponent(IAttachService.class);
                                attachService.copyAttachByClientGuid(auditSpIMaterial.getCliengguid(), null, null,
                                        cliengguid);
                            }
                            auditProjectMaterial.setStatus(Integer.parseInt(auditSpIMaterial.getStatus()));
                        }
                        auditProjectMaterial.setIs_rongque(Integer.parseInt(auditSpIMaterial.getAllowrongque()));
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getShared())) {
                            auditProjectMaterial.setSharematerialiguid(auditSpIMaterial.getMaterialguid());
                        }
                        auditProjectMaterial.setCertinfoinstanceguid(auditSpIMaterial.getCertinfoinstanceguid());
                    }
                    auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> initTcSubappMaterial(String subAppGuid, String businessGuid,
            String biGuid, String phaseGuid, String ownerGuid, String certNum) {
        AuditCommonResult<List<AuditSpIMaterial>> result = new AuditCommonResult<List<AuditSpIMaterial>>();
        List<AuditSpIMaterial> listSpIMaterial = new ArrayList<AuditSpIMaterial>();
        // 实例化各个接口
        IAuditSpITask auditTaskIService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditSpShareMaterialRelation auditMatrerialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);
        IAuditSpShareMaterial auditMatrerialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterial.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditSpISubapp auditSubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        ICertInfoExternal certInfoExternalImpl = ContainerFactory.getContainInfo()
                .getComponent(ICertInfoExternal.class);
        try {
            // 第一步，获取所有的已选择办理事项,并将材料进行拼接处理
            List<AuditSpITask> listTask = auditTaskIService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            if (listTask.isEmpty()) {
                result.setBusinessFail("未选择办理事项");
                return result;
            }
            listTask.sort((b, a) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            int ordernum = 0;
            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                        .selectTaskMaterialListByTaskGuid(auditSpiTask.getTaskguid(), false).getResult();
                listMaterial.sort((b, a) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                        .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
                for (AuditTaskMaterial auditMaterial : listMaterial) {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    // 直接添加
                    auditSpIMaterial.setEffictiverange("");
                    auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialguid(auditMaterial.getMaterialid());

                    auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                    auditSpIMaterial.setAllowrongque(String.valueOf(auditMaterial.getIs_rongque()));
                    auditSpIMaterial.setBiguid(biGuid);
                    auditSpIMaterial.setBusinessguid(businessGuid);
                    auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                    auditSpIMaterial.setOperatedate(new Date());
                    auditSpIMaterial.setPhaseguid(phaseGuid);
                    auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialname(auditMaterial.getMaterialname());
                    auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    // TODO 这里需要改成常量
                    auditSpIMaterial.setSubmittype(auditMaterial.getSubmittype());
                    auditSpIMaterial.setNecessity(String.valueOf(auditMaterial.getNecessity()));
                    auditSpIMaterial.setSubappguid(subAppGuid);

                    auditSpIMaterial.setOrdernum(ordernum);
                    ordernum += 10;

                    listSpIMaterial.add(auditSpIMaterial);
                }
            }
            // 第二步，获取所有的材料对应关系 modified 查询是否在所选的事项关系中，不存在的不加
            List<AuditSpShareMaterialRelation> listRelation = auditMatrerialRelationService
                    .selectByBusinessGuid(businessGuid).getResult();
            List<AuditTask> tasklist = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditSpiTask.getTaskguid(), false)
                        .getResult();
                tasklist.add(audittask);
            }
            List<AuditSpShareMaterialRelation> listRelationnew = new ArrayList<>();
            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listRelation) {
                for (AuditTask auditTask : tasklist) {
                    if (auditSpShareMaterialRelation.getTaskid().equals(auditTask.getTask_id())) {
                        listRelationnew.add(auditSpShareMaterialRelation);
                        break;
                    }
                }
            }
            listRelation = listRelationnew;
            // 第三步，筛选材料，并进行初始化
            // 这里可以确定，第一步取出的材料数量已经是材料的上限了，不会更多了，这里的做法是遍历关系表，然后将额外是共享材料的材料进行删除
            Iterator<AuditSpShareMaterialRelation> iterator = listRelation.iterator();
            while (iterator.hasNext()) {
                AuditSpShareMaterialRelation relation = iterator.next();
                AuditSpShareMaterial shareMaterial = auditMatrerialService
                        .getAuditSpShareMaterialByShareMaterialGuid(relation.getSharematerialguid(), businessGuid)
                        .getResult();
                // 如果是全局共享材料，那就只要删选一下是不是还存在重复的单个材料
                listSpIMaterial.removeIf(material -> {
                    return !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                            && material.getMaterialguid().equals(relation.getSharematerialguid());
                });
                if (shareMaterial != null) {
                    // 如果是主题内的，那就先把单个的，共享材料设置是启用的
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        // 删除重复的共享材料
                        listSpIMaterial.removeIf(material -> {
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                                    && material.getMaterialguid().equals(relation.getSharematerialguid())) {
                                return true;
                            }
                            else {
                                return relation.getMaterialid().equals(material.getMaterialguid());
                            }
                        });
                        // 然后再添加上这个共享材料
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        if ("2".equals(shareMaterial.getEffictiverange())) {
                            auditSpIMaterial.setEffictiverange("20");
                        }
                        else {
                            auditSpIMaterial.setEffictiverange("10");
                        }
                        auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ONE);
                        auditSpIMaterial.setMaterialguid(relation.getSharematerialguid());

                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        auditSpIMaterial.setBiguid(biGuid);
                        auditSpIMaterial.setBusinessguid(businessGuid);
                        auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                        auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setPhaseguid(phaseGuid);
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());
                        // TODO 这里需要改成常量
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setSubappguid(subAppGuid);
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                        listSpIMaterial.add(auditSpIMaterial);
                    }
                }
                // 如果这个材料不是结果的，那就去掉了
                if (!"20".equals(relation.getMaterialtype())) {
                    iterator.remove();
                }
            }
            List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
            spIMaterials.addAll(listSpIMaterial);
            // 插入数据
            for (AuditSpIMaterial spiMaterial : listSpIMaterial) {
                // 普通材料，直接插入
                if (StringUtil.isBlank(spiMaterial.getEffictiverange())) {
                    auditSpIMaterialService.addSpIMaterial(spiMaterial);
                }
                // 主题内共享的
                else if ("20".equals(spiMaterial.getEffictiverange())) {
                    // 跨阶段共享
                    AuditSpIMaterial lastSpIMaterial = auditSpIMaterialService
                            .getLastSpIMaterial(biGuid, spiMaterial.getMaterialguid()).getResult();
                    if (lastSpIMaterial != null) {
                        spiMaterial.setStatus(lastSpIMaterial.getStatus());
                        if (attachService.getAttachCountByClientGuid(lastSpIMaterial.getCliengguid()) > 0) {
                            String newCliengguid = UUID.randomUUID().toString();
                            attachService.copyAttachByClientGuid(lastSpIMaterial.getCliengguid(), null, null,
                                    newCliengguid);
                            spiMaterial.setCliengguid(newCliengguid);
                        }
                    }
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    // 如果发生了变化，说明是包含结果的
                    if (length > listRelation.size()) {
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        spIMaterials.add(auditSpIMaterial);
                        spIMaterials.remove(spiMaterial);
                        auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                    }
                    else {
                        auditSpIMaterialService.addSpIMaterial(spiMaterial);
                    }

                }
                // 全局共享的
                else {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    if (length > listRelation.size()) {
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 需要替换成主题中配置的，这里还有个判断，如果这个共享材料实际没有多个进行匹配的，那就需要用原本事项的配置
                    AuditSpShareMaterial shareMaterial = auditMatrerialService
                            .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                    businessGuid)
                            .getResult();
                    if (shareMaterial != null && !shareMaterial.isEmpty()
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());

                    }

                    if (shareMaterial != null) {
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                    }
                    // 给材料默认的关联certclientguid
                    if (shareMaterial != null) {
                        auditSpIMaterial.setCertinfoinstanceguid(UUID.randomUUID().toString());
                    }
                    spIMaterials.add(auditSpIMaterial);
                    spIMaterials.remove(spiMaterial);
                    auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                }
            }
            auditSubappService.updateInitmaterial(subAppGuid);
            // 第一次显示的时候移除掉做为结果的
            spIMaterials.removeIf(material -> {
                return !ZwfwConstant.CONSTANT_STR_ZERO.equals(material.getResult());
            });
            spIMaterials.sort((a, b) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                    .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
            result.setResult(auditSpIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> initTasklistBySelectedOptions(String selectedoptions, String Subappguid,
            String areacode, String businessGuid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        IAuditSpSelectedOptionService iAuditSpSelectedOptionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpSelectedOptionService.class);
        IAuditSpOptionService iAuditSpOptionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptionService.class);
        IAuditSpBasetaskR iauditspbasetaskr = ContainerFactory.getContainInfo().getComponent(IAuditSpBasetaskR.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        IAuditSpOptiontownshipService shipservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptiontownshipService.class);

        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditSpTask iauditSpTask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
        // 所有选项得guid列表
        List<String> opguids = new ArrayList<>();
        // 所有事项id列表
        List<String> taskidlist = new ArrayList<>();
        // 区域编码列表
        List<String> arealist = new ArrayList<String>();

        AuditSpSelectedOption auditspselectedoption = iAuditSpSelectedOptionService
                .getSelectedoptionsBySubappGuid(Subappguid).getResult();
        // 将选择得数据添加到selected option表中去
        if (auditspselectedoption == null) {
            AuditSpSelectedOption SelectedOption = new AuditSpSelectedOption();
            SelectedOption.setInsertdate(new Date());
            SelectedOption.setRowguid(UUID.randomUUID().toString());
            SelectedOption.setSubappguid(Subappguid);
            SelectedOption.setSelectedoptions(selectedoptions);
            iAuditSpSelectedOptionService.insert(SelectedOption);
        }
        else {
            auditspselectedoption.setInsertdate(new Date());
            auditspselectedoption.setSelectedoptions(selectedoptions);
            iAuditSpSelectedOptionService.update(auditspselectedoption);
        }

        // 初始化areacode列表
        arealist = iauditorgaarea.getAllAreacodeByAreacode(areacode).getResult();

        JSONObject jsonObject = JSONObject.parseObject(selectedoptions);
        List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
        for (Map<String, Object> map : jsona) {
            List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
            for (Map<String, Object> map2 : maplist) {
                opguids.add(String.valueOf(map2.get("optionguid")));
            }
        }
        // 添加必办事项的optionguid
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("businessGuid", businessGuid);
        sqlc.eq("elementguid", "root");
        List<AuditSpOption> options = iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult();
        if (options != null && !options.isEmpty()) {
            opguids.add(options.get(0).getRowguid());
        }
        // 根据选项guid
        for (String opguid : opguids) {
            AuditSpOption auditspoption = iAuditSpOptionService.find(opguid).getResult();
            if (auditspoption != null) {
                // 获取选项绑定的事项id
                String taskids = auditspoption.get("taskid");
                if (StringUtil.isNotBlank(taskids)) {

                    String[] taskid = taskids.split(";");
                    for (String id : taskid) {
                        if (!taskidlist.contains(id)) {
                            String str = opguid + "_" + id;
                            if (!taskidlist.contains(str)) {
                                taskidlist.add(str);
                            }
                        }
                    }
                }
            }
        }

        // 查询主题下所有事项目录basetaskguid
        List<AuditSpTask> allBasetask = iauditSpTask.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();
        List<String> allBasetaskGuid = new ArrayList<String>();
        for (AuditSpTask auditSpTask : allBasetask) {
            if (!allBasetaskGuid.contains(auditSpTask.getBasetaskguid())) {
                allBasetaskGuid.add(auditSpTask.getBasetaskguid());
            }
        }

        // 定义存储要素信息的List
        List<Record> elementJsonList = new ArrayList<Record>();
        if (ValidateUtil.isNotBlankCollection(taskidlist)) {
            // 存放areacode_taskid，防止数据重复
            List<String> list = new ArrayList<>();
            List<AuditSpTask> auditsptasklist = iauditSpTask.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();
            for (String id : taskidlist) {
                // 判断事项是否禁用
                AuditTask audittask = iaudittask.selectUsableTaskByTaskID(id.split("_")[1]).getResult();
                if (audittask == null) {
                    continue;
                }
                /*  Record record = new Record();
                record.set("TASKNAME", audittask.getTaskname());
                record.set("TASKID", audittask.getTask_id());
                record.set("AREACODE", audittask.getAreacode());
                elementJsonList.add(record);*/

                /*                List<Record> recordlist = iauditspbasetaskr
                        .getBasetaskByAreacodelistandTaskid(id, arealist, allBasetaskGuid).getResult();*/

                List<Record> recordlist = iauditspbasetaskr.getBasetaskByTaskid(id.split("_")[1]).getResult();
                List<String> basetaskguid = auditsptasklist.stream().map(a->a.getBasetaskguid()).collect(Collectors.toList());
                recordlist.removeIf(a -> !basetaskguid.contains(a.get("basetaskguid")));
                for (Record record : recordlist) {
                    //如果是市级事项.则判断该事项在audit_sp_optiontownship表中是否存在记录,如果有则添加
                    if(record.getStr("areacode").length() == 6) {
                        sqlc.clear();
                        sqlc.eq("optionguid", id.split("_")[0]);
                        sqlc.eq("taskid", id.split("_")[1]);
                        List<AuditSpOptiontownship> list2 = shipservice.findListByCondition(sqlc.getMap());
                        for (AuditSpOptiontownship auditspoptiontownship : list2) {
                            record = (Record) record.clone();//防止一个事项市级和乡镇都办理
                            String str = auditspoptiontownship.getTownshipcode() + record.getStr("taskid");
                            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(auditspoptiontownship.getTownshipcode()).getResult();
                            record.set("xiaquname", area.getXiaquname());
                            record.set("areacode", auditspoptiontownship.getTownshipcode());
                            if(!list.contains(str)) {
                                list.add(str);
                                elementJsonList.add(record);
                            }
                        }
                    }
                    else {//乡镇法定不存在下放功能，直接添加
                        String s = record.getStr("areacode") + record.getStr("taskid");
                        if(list.contains(s)) {
                            continue;
                        }
                        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(record.getStr("areacode")).getResult();
                        record.set("xiaquname", area.getXiaquname());
                        list.add(s);
                        elementJsonList.add(record);
                    }

                }

/*                for (Record record : recordlist) {
                    elementJsonList.add(record);
                    // elementJson.put("multiselect",
                    // auditSpElement.getMultiselect());
                }*/
            }
        }
        // 定义返回的JSON
        result.setResult(elementJsonList);
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> initIntegratedMaterial(String subAppGuid, String businessGuid,
            String biGuid, String phaseGuid, String ownerGuid, String certNum) {
        AuditCommonResult<List<AuditSpIMaterial>> result = new AuditCommonResult<List<AuditSpIMaterial>>();
        List<AuditSpIMaterial> listSpIMaterial = new ArrayList<AuditSpIMaterial>();
        // 实例化各个接口
        IAuditSpITask auditTaskIService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditSpSelectedOptionService iauditspselectedoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpSelectedOptionService.class);
        IAuditSpOptionService iAuditSpOptionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptionService.class);
        IAuditSpShareMaterialRelation auditMatrerialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);
        IAuditSpShareMaterial auditMatrerialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterial.class);
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditSpISubapp auditSubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditTaskMaterial iaudittaskmaterial = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        try {
            // 第一步，获取所有的已选择办理事项,并将材料进行拼接处理
            List<AuditSpITask> listTask = auditTaskIService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            AuditSpISubapp subapp = auditSubappService.getSubappByGuid(subAppGuid).getResult();
            // 所有选项得guid列表
            List<String> opguids = new ArrayList<>();
            // 非必须材料 根据选项筛选出来的
            List<String> optmaterialidlist = new ArrayList<>();
            // 获取所有option对应的optionguids，用来选择所有选中的材料
            AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice
                    .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
            if (auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
                JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
                List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                for (Map<String, Object> map : jsona) {
                    List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                    for (Map<String, Object> map2 : maplist) {
                        opguids.add(String.valueOf(map2.get("optionguid")));
                    }
                }
            }

            // 根据选项guid
            for (String opguid : opguids) {
                AuditSpOption auditspoption = iAuditSpOptionService.find(opguid).getResult();
                if (auditspoption != null) {
                    // 获取选项绑定的事项id
                    String materialids = auditspoption.get("materialids");
                    if (StringUtil.isNotBlank(materialids)) {
                        String[] materialid = materialids.split(";");
                        for (String id : materialid) {
                            if (!optmaterialidlist.contains(id)) {
                                optmaterialidlist.add(id);
                            }
                        }
                    }
                }
            }

            if (listTask.isEmpty()) {
                result.setBusinessFail("未选择办理事项");
                return result;
            }
            listTask.sort((b, a) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            int ordernum = 0;
            // 记录所有的材料materialid 所有的必须材料
            List<String> materialids = new ArrayList<>();
            // 记录未配置在情形中的材料标识
            List<String> unmatchmaterialids = new ArrayList<>();
            // 记录记录所有材料
            List<String> allmaterialids = new ArrayList<>();

            // 保存所有材料的record对象
            List<Record> materiallisttemp = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = iaudittaskmaterial
                        .selectTaskMaterialListByTaskGuid(auditSpiTask.getTaskguid(), false).getResult();
                for (Record auditTaskMaterial : listMaterial) {
                    if (!materialids.contains(auditTaskMaterial.get("materialid")) && Integer
                            .valueOf(ZwfwConstant.NECESSITY_SET_YES).equals(auditTaskMaterial.getInt("necessity"))) {
                        materialids.add(auditTaskMaterial.get("materialid"));
                        materiallisttemp.add(auditTaskMaterial);
                    }
                    else if (optmaterialidlist.contains(auditTaskMaterial.get("materialid"))) {
                        materialids.add(auditTaskMaterial.get("materialid"));
                        materiallisttemp.add(auditTaskMaterial);
                    }
                }
                // mapt_m.put(auditSpiTask.getTaskguid(), listMaterial);
            }
            // 记录需要提交材料的option
            List<String> materialidscopy = new ArrayList<>();
            // 复制集合
            unmatchmaterialids.addAll(materialids);
            materialidscopy.addAll(materialids);
            // 获取配置了option的材料
            materialidscopy.retainAll(allmaterialids);
            // 移除配置在option里的材料
            unmatchmaterialids.removeAll(materialidscopy);

            for (Record auditMaterial : materiallisttemp) {

                AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                // 直接添加
                auditSpIMaterial.setEffictiverange("");
                auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                auditSpIMaterial.setMaterialguid(auditMaterial.get("materialid"));

                auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                if (ZwfwConstant.NECESSITY_SET_YES.equals(auditMaterial.get("necessity"))) {
                    auditSpIMaterial
                            .setAllowrongque(auditMaterial.get("is_rongque") == null ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : auditMaterial.get("is_rongque"));
                }
                else {
                    auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                }
                auditSpIMaterial.set("materialtype", auditMaterial.get("materialtype"));
                auditSpIMaterial.setBiguid(biGuid);
                auditSpIMaterial.setBusinessguid(businessGuid);
                auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                auditSpIMaterial.setOperatedate(new Date());
                auditSpIMaterial.setPhaseguid(phaseGuid);
                auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                auditSpIMaterial.setMaterialname(auditMaterial.get("materialname"));
                auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                // TODO 这里需要改成常量
                auditSpIMaterial.setSubmittype(auditMaterial.get("submittype"));
                // 判断如果是在 情形匹配材料里面需要设置成必须
                auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);

                auditSpIMaterial.setSubappguid(subAppGuid);

                auditSpIMaterial.setOrdernum(ordernum);
                ordernum += 10;

                listSpIMaterial.add(auditSpIMaterial);
            }
            // 第二步，获取所有的材料对应关系 modified 查询是否在所选的事项关系中，不存在的不加
            List<AuditSpShareMaterialRelation> listRelation = auditMatrerialRelationService
                    .selectByBusinessGuid(businessGuid).getResult();
            List<Record> tasklist = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                Record audittask = iaudittask.getAuditTaskByGuid(auditSpiTask.getTaskguid(), false).getResult();
                tasklist.add(audittask);
            }
            List<AuditSpShareMaterialRelation> listRelationnew = new ArrayList<>();
            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listRelation) {
                for (Record auditTask : tasklist) {
                    // 去除掉，不需要提交的材料的关系,结果直接加入
                    if (auditSpShareMaterialRelation.getTaskid().equals(auditTask.get("task_id"))
                            && ("20".equals(auditSpShareMaterialRelation.getMaterialtype())
                                    || materialids.contains(auditSpShareMaterialRelation.getMaterialid())
                                    || unmatchmaterialids.contains(auditSpShareMaterialRelation.getMaterialid()))) {
                        listRelationnew.add(auditSpShareMaterialRelation);
                        break;
                    }
                }
            }
            listRelation = listRelationnew;
            // 第三步，筛选材料，并进行初始化
            // 这里可以确定，第一步取出的材料数量已经是材料的上限了，不会更多了，这里的做法是遍历关系表，然后将额外是共享材料的材料进行删除
            Iterator<AuditSpShareMaterialRelation> iterator = listRelation.iterator();
            List<String> necessitysharelist = new ArrayList<>();
            while (iterator.hasNext()) {
                AuditSpShareMaterialRelation relation = iterator.next();
                AuditSpShareMaterial shareMaterial = auditMatrerialService
                        .getAuditSpShareMaterialByShareMaterialGuid(relation.getSharematerialguid(), businessGuid)
                        .getResult();
                // 如果是全局共享材料，那就只要删选一下是不是还存在重复的单个材料
                listSpIMaterial.removeIf(material -> {
                    return !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                            && material.getMaterialguid().equals(relation.getSharematerialguid());
                });
                if (shareMaterial != null) {
                    // 如果是主题内的，那就先把单个的，共享材料设置是启用的
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        // 删除重复的共享材料
                        listSpIMaterial.removeIf(material -> {
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                                    && material.getMaterialguid().equals(relation.getSharematerialguid())) {
                                return true;
                            }
                            else {
                                return relation.getMaterialid().equals(material.getMaterialguid());
                            }
                        });
                        // 然后再添加上这个共享材料
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        if ("2".equals(shareMaterial.getEffictiverange())) {
                            auditSpIMaterial.setEffictiverange("20");
                        }
                        else {
                            auditSpIMaterial.setEffictiverange("10");
                        }
                        auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ONE);
                        auditSpIMaterial.setMaterialguid(relation.getSharematerialguid());

                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        if (ZwfwConstant.NECESSITY_SET_YES.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        auditSpIMaterial.setBiguid(biGuid);
                        auditSpIMaterial.setBusinessguid(businessGuid);
                        auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                        // 必须条件1\材料在necessitysharelist中2\情形匹配上的3\在必须材料列表中4\共享材料是必须的
                        if (necessitysharelist.contains(relation.getSharematerialguid())
                                || materialids.contains(relation.getMaterialid())
                                || shareMaterial.getNecessity().equals(ZwfwConstant.NECESSITY_SET_YES)) {
                            auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                            if (!necessitysharelist.contains(relation.getSharematerialguid())) {
                                necessitysharelist.add(relation.getSharematerialguid());
                            }
                        }
                        else {
                            auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_NO);
                        }
                        auditSpIMaterial.setPhaseguid(phaseGuid);
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());
                        // TODO 这里需要改成常量
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setSubappguid(subAppGuid);
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                        listSpIMaterial.add(auditSpIMaterial);
                    }
                }
                // 如果这个材料不是结果的，那就去掉了
                if (!"20".equals(relation.getMaterialtype())) {
                    iterator.remove();
                }
            }
            List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
            spIMaterials.addAll(listSpIMaterial);
            // 插入数据
            for (AuditSpIMaterial spiMaterial : listSpIMaterial) {
                // 普通材料，直接插入
                if (StringUtil.isBlank(spiMaterial.getEffictiverange())) {
                    auditSpIMaterialService.addSpIMaterial(spiMaterial);
                }
                // 主题内共享的
                else if ("20".equals(spiMaterial.getEffictiverange())) {
                    // 跨阶段共享
                    AuditSpIMaterial lastSpIMaterial = auditSpIMaterialService
                            .getLastSpIMaterial(biGuid, spiMaterial.getMaterialguid()).getResult();
                    if (lastSpIMaterial != null) {
                        spiMaterial.setStatus(lastSpIMaterial.getStatus());
                        if (attachService.getAttachCountByClientGuid(lastSpIMaterial.getCliengguid()) > 0) {
                            String newCliengguid = UUID.randomUUID().toString();
                            attachService.copyAttachByClientGuid(lastSpIMaterial.getCliengguid(), null, null,
                                    newCliengguid);
                            spiMaterial.setCliengguid(newCliengguid);
                        }
                    }
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    // 如果发生了变化，说明是包含结果的
                    if (length > listRelation.size()) {
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        spIMaterials.add(auditSpIMaterial);
                        spIMaterials.remove(spiMaterial);
                        auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                    }
                    else {
                        auditSpIMaterialService.addSpIMaterial(spiMaterial);
                    }

                }
                // 全局共享的
                else {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    if (length > listRelation.size()) {
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 需要替换成主题中配置的，这里还有个判断，如果这个共享材料实际没有多个进行匹配的，那就需要用原本事项的配置
                    AuditSpShareMaterial shareMaterial = auditMatrerialService
                            .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                    businessGuid)
                            .getResult();
                    if (shareMaterial != null && !shareMaterial.isEmpty()
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        // auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());

                    }

                    if (shareMaterial != null) {
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                    }
                    // 给材料默认的关联certclientguid
                    if (shareMaterial != null) {
                        auditSpIMaterial.setCertinfoinstanceguid(UUID.randomUUID().toString());
                    }
                    spIMaterials.add(auditSpIMaterial);
                    spIMaterials.remove(spiMaterial);
                    auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                }
            }
            auditSubappService.updateInitmaterial(subAppGuid);
            // 第一次显示的时候移除掉做为结果的
            spIMaterials.removeIf(material -> {
                return !ZwfwConstant.CONSTANT_STR_ZERO.equals(material.getResult());
            });
            spIMaterials.sort((a, b) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                    .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
            result.setResult(auditSpIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult());
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> initNewIntegratedMaterial(String subAppGuid, String businessGuid,
            String biGuid, String phaseGuid, String ownerGuid, String certNum) {
        AuditCommonResult<List<AuditSpIMaterial>> result = new AuditCommonResult<List<AuditSpIMaterial>>();
        List<AuditSpIMaterial> listSpIMaterial = new ArrayList<AuditSpIMaterial>();
        // 实例化各个接口
        IAuditSpITask auditTaskIService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditSpShareMaterialRelation auditMatrerialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);
        IAuditSpShareMaterial auditMatrerialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterial.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditSpISubapp auditSubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditSpSelectedOptionService iauditspselectedoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpSelectedOptionService.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);
        try {
            // 第一步，获取所有的已选择办理事项,并将材料进行拼接处理
            List<AuditSpITask> listTask = auditTaskIService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            AuditSpISubapp subapp = auditSubappService.getSubappByGuid(subAppGuid).getResult();
            // 匹配的optionguid
            List<String> opguids = new ArrayList<>();
            // 匹配的选项实体
            List<AuditTaskOption> optionlist = new ArrayList<>();
            // 记录材料事项标识和材料列表
            Map<String, List<AuditTaskMaterial>> mapt_m = new HashMap<>();
            // 记录必填材料
            List<String> necessity_materialids = new ArrayList<>();

            // 记录情形匹配上的材料
            List<String> materialids = new ArrayList<>();

            if (listTask.isEmpty()) {
                result.setBusinessFail("未选择办理事项");
                return result;
            }
            listTask.sort((b, a) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            int ordernum = 0;
            // 获取map 存储taskguid和材料数据
            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                        .getUsableMaterialListByTaskguid(auditSpiTask.getTaskguid()).getResult();
                mapt_m.put(auditSpiTask.getTaskguid(), listMaterial);
            }
            // 获取选中的optionguid
            AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice
                    .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
            if (auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
                JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
                List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                for (Map<String, Object> map : jsona) {
                    List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                    for (Map<String, Object> map2 : maplist) {
                        opguids.add(String.valueOf(map2.get("optionguid")));
                    }
                }
            }
            if (!opguids.isEmpty()) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.in("rowguid", StringUtil.joinSql(opguids));
                optionlist = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
            }
            // 遍历map将其中的材料添加进materialids中
            for (AuditTaskOption audittaskoption : optionlist) {
                String material = audittaskoption.getMaterialids();
                if (StringUtil.isBlank(material)) {
                    continue;
                }
                String[] materials = material.split(";");
                for (String string : materials) {
                    if (!materialids.contains(string)) {
                        materialids.add(string);
                    }
                }
            }

            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = mapt_m.get(auditSpiTask.getTaskguid());
                listMaterial.sort((b, a) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                        .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
                for (AuditTaskMaterial auditMaterial : listMaterial) {
                    // 根据情形过滤,如果是非必须，并且不再上面材料的提交范围中，则不初始化这个磁疗
                    if (Integer.valueOf(ZwfwConstant.NECESSITY_SET_NO).equals(auditMaterial.getNecessity())) {
                        if (!materialids.contains(auditMaterial.getMaterialid())) {
                            continue;
                        }
                    }
                    else {
                        if (!necessity_materialids.contains(auditMaterial.getMaterialid())) {
                            necessity_materialids.add(auditMaterial.getMaterialid());
                        }
                    }

                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    // 直接添加
                    auditSpIMaterial.setEffictiverange("");
                    auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialguid(auditMaterial.getMaterialid());

                    auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                    auditSpIMaterial
                            .setAllowrongque(auditMaterial.getIs_rongque() == null ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : String.valueOf(auditMaterial.getIs_rongque()));
                    auditSpIMaterial.setMaterialtype(auditMaterial.getMaterialtype());
                    auditSpIMaterial.setBiguid(biGuid);
                    auditSpIMaterial.setBusinessguid(businessGuid);
                    auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                    auditSpIMaterial.setOperatedate(new Date());
                    auditSpIMaterial.setPhaseguid(phaseGuid);
                    auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialname(auditMaterial.getMaterialname());
                    auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    // TODO 这里需要改成常量
                    auditSpIMaterial.setSubmittype(auditMaterial.getSubmittype());

                    // 判断如果是在 情形匹配材料里面需要设置成必须
                    if (materialids.contains(auditMaterial.getMaterialid())) {
                        auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                    }
                    else {
                        auditSpIMaterial.setNecessity(String.valueOf(auditMaterial.getNecessity()));
                    }

                    auditSpIMaterial.setSubappguid(subAppGuid);

                    auditSpIMaterial.setOrdernum(ordernum);
                    ordernum += 10;

                    listSpIMaterial.add(auditSpIMaterial);
                }
            }
            // 第二步，获取所有的材料对应关系 modified 查询是否在所选的事项关系中，不存在的不加
            List<AuditSpShareMaterialRelation> listRelation = auditMatrerialRelationService
                    .selectByBusinessGuid(businessGuid).getResult();
            List<AuditTask> tasklist = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditSpiTask.getTaskguid(), false)
                        .getResult();
                tasklist.add(audittask);
            }
            List<AuditSpShareMaterialRelation> listRelationnew = new ArrayList<>();
            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listRelation) {
                for (AuditTask auditTask : tasklist) {
                    // 去除掉，不需要提交的材料的关系,结果直接加入
                    if (auditSpShareMaterialRelation.getTaskid().equals(auditTask.getTask_id())
                            && ("20".equals(auditSpShareMaterialRelation.getMaterialtype())
                                    || necessity_materialids.contains(auditSpShareMaterialRelation.getMaterialid())
                                    || materialids.contains(auditSpShareMaterialRelation.getMaterialid()))) {
                        listRelationnew.add(auditSpShareMaterialRelation);
                        break;
                    }
                }
            }
            listRelation = listRelationnew;
            // 第三步，筛选材料，并进行初始化
            // 这里可以确定，第一步取出的材料数量已经是材料的上限了，不会更多了，这里的做法是遍历关系表，然后将额外是共享材料的材料进行删除
            Iterator<AuditSpShareMaterialRelation> iterator = listRelation.iterator();

            // 存在多个材料关联一个共享材料，一个材料情形匹配为必须，则这个共享材料就是必须，声明map
            List<String> necessitymaterial = new ArrayList<>();
            while (iterator.hasNext()) {
                AuditSpShareMaterialRelation relation = iterator.next();
                AuditSpShareMaterial shareMaterial = auditMatrerialService
                        .getAuditSpShareMaterialByShareMaterialGuid(relation.getSharematerialguid(), businessGuid)
                        .getResult();
                // 如果是全局共享材料，那就只要删选一下是不是还存在重复的单个材料
                listSpIMaterial.removeIf(material -> {
                    return !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                            && material.getMaterialguid().equals(relation.getSharematerialguid());
                });
                if (shareMaterial != null) {
                    // 如果是主题内的，那就先把单个的，共享材料设置是启用的
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        // 删除重复的共享材料
                        listSpIMaterial.removeIf(material -> {
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                                    && material.getMaterialguid().equals(relation.getSharematerialguid())) {
                                return true;
                            }
                            else {
                                return relation.getMaterialid().equals(material.getMaterialguid());
                            }
                        });
                        // 然后再添加上这个共享材料
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        if ("2".equals(shareMaterial.getEffictiverange())) {
                            auditSpIMaterial.setEffictiverange("20");
                        }
                        else {
                            auditSpIMaterial.setEffictiverange("10");
                        }
                        auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ONE);
                        auditSpIMaterial.setMaterialguid(relation.getSharematerialguid());

                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        auditSpIMaterial.setBiguid(biGuid);
                        auditSpIMaterial.setBusinessguid(businessGuid);
                        auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                        // 如果是必須提交的和情形匹配上需要提交的设置为必须,否则为非必须
                        if (necessity_materialids.contains(relation.getMaterialid())
                                || materialids.contains(relation.getMaterialid())
                                || necessitymaterial.contains(auditSpIMaterial.getMaterialguid())) {
                            auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                            necessitymaterial.add(auditSpIMaterial.getMaterialguid());
                        }
                        else {
                            auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        }
                        auditSpIMaterial.setPhaseguid(phaseGuid);
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());
                        // TODO 这里需要改成常量
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setSubappguid(subAppGuid);
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                        listSpIMaterial.add(auditSpIMaterial);
                    }
                }
                // 如果这个材料不是结果的，那就去掉了
                if (!"20".equals(relation.getMaterialtype())) {
                    iterator.remove();
                }
            }
            List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
            spIMaterials.addAll(listSpIMaterial);
            // 插入数据
            for (AuditSpIMaterial spiMaterial : listSpIMaterial) {
                // 普通材料，直接插入
                if (StringUtil.isBlank(spiMaterial.getEffictiverange())) {
                    auditSpIMaterialService.addSpIMaterial(spiMaterial);
                }
                // 主题内共享的
                else if ("20".equals(spiMaterial.getEffictiverange())) {
                    // 跨阶段共享
                    AuditSpIMaterial lastSpIMaterial = auditSpIMaterialService
                            .getLastSpIMaterial(biGuid, spiMaterial.getMaterialguid()).getResult();
                    if (lastSpIMaterial != null) {
                        spiMaterial.setStatus(lastSpIMaterial.getStatus());
                        if (attachService.getAttachCountByClientGuid(lastSpIMaterial.getCliengguid()) > 0) {
                            String newCliengguid = UUID.randomUUID().toString();
                            attachService.copyAttachByClientGuid(lastSpIMaterial.getCliengguid(), null, null,
                                    newCliengguid);
                            spiMaterial.setCliengguid(newCliengguid);
                        }
                    }
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    // 如果发生了变化，说明是包含结果的
                    if (length > listRelation.size()) {
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        spIMaterials.add(auditSpIMaterial);
                        spIMaterials.remove(spiMaterial);
                        auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                    }
                    else {
                        auditSpIMaterialService.addSpIMaterial(spiMaterial);
                    }

                }
                // 全局共享的
                else {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    if (length > listRelation.size()) {
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 需要替换成主题中配置的，这里还有个判断，如果这个共享材料实际没有多个进行匹配的，那就需要用原本事项的配置
                    AuditSpShareMaterial shareMaterial = auditMatrerialService
                            .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                    businessGuid)
                            .getResult();
                    if (shareMaterial != null && !shareMaterial.isEmpty()
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        // auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());

                    }

                    if (shareMaterial != null) {
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                    }
                    // 给材料默认的关联certclientguid
                    if (shareMaterial != null) {
                        auditSpIMaterial.setCertinfoinstanceguid(UUID.randomUUID().toString());
                    }
                    spIMaterials.add(auditSpIMaterial);
                    spIMaterials.remove(spiMaterial);
                    auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                }
            }
            auditSubappService.updateInitmaterial(subAppGuid);
            // 第一次显示的时候移除掉做为结果的
            spIMaterials.removeIf(material -> {
                return !ZwfwConstant.CONSTANT_STR_ZERO.equals(material.getResult());
            });
            spIMaterials.sort((a, b) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                    .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
            result.setResult(auditSpIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult());
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpIMaterial>> initIntegratedMaterial(String subAppGuid, String businessGuid,
            String biGuid, String phaseGuid, String ownerGuid, String certNum, String areaCode) {
        AuditCommonResult<List<AuditSpIMaterial>> result = new AuditCommonResult<List<AuditSpIMaterial>>();
        List<AuditSpIMaterial> listSpIMaterial = new ArrayList<AuditSpIMaterial>();
        // 实例化各个接口
        IAuditSpITask auditTaskIService = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditSpShareMaterialRelation auditMatrerialRelationService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterialRelation.class);
        IAuditSpShareMaterial auditMatrerialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpShareMaterial.class);
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditSpIMaterial auditSpIMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpIMaterial.class);
        IAuditSpISubapp auditSubappService = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IAuditSpOptionService iauditspoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpOptionService.class);
        IAuditSpSelectedOptionService iauditspselectedoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditSpSelectedOptionService.class);
        IAuditTaskOptionService iaudittaskoptionservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskOptionService.class);
        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
        ICertAttachExternal iCertAttachExternal = ContainerFactory.getContainInfo()
                .getComponent(ICertAttachExternal.class);
        ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class);
        ICertCatalog icertcatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
        try {
            // 第一步，获取所有的已选择办理事项,并将材料进行拼接处理
            List<AuditSpITask> listTask = auditTaskIService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            AuditSpISubapp subapp = auditSubappService.getSubappByGuid(subAppGuid).getResult();
            // 匹配的optionguid
            List<String> opguids = new ArrayList<>();
            // 匹配的选项实体
            List<AuditTaskOption> optionlist = new ArrayList<>();
            // 记录材料事项标识和材料列表
            Map<String, List<AuditTaskMaterial>> mapt_m = new HashMap<>();
            // 记录必填材料
            List<String> necessity_materialids = new ArrayList<>();

            // 记录情形匹配上的材料
            List<String> materialids = new ArrayList<>();

            if (listTask.isEmpty()) {
                result.setBusinessFail("未选择办理事项");
                return result;
            }
            listTask.sort((b, a) -> (b.getOrdernumber() != null ? b.getOrdernumber() : Integer.valueOf(0))
                    .compareTo(a.getOrdernumber() != null ? a.getOrdernumber() : Integer.valueOf(0)));
            int ordernum = 0;
            // 获取map 存储taskguid和材料数据
            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = auditTaskMaterialService
                        .selectTaskMaterialListByTaskGuid(auditSpiTask.getTaskguid(), false).getResult();
                mapt_m.put(auditSpiTask.getTaskguid(), listMaterial);
            }
            // 获取选中的optionguid
            AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice
                    .getSelectedoptionsBySubappGuid(subAppGuid).getResult();
            if (auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
                JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
                List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
                for (Map<String, Object> map : jsona) {
                    List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                    for (Map<String, Object> map2 : maplist) {
                        opguids.add(String.valueOf(map2.get("optionguid")));
                    }
                }
            }
            if (!opguids.isEmpty()) {
                SqlConditionUtil sqlc = new SqlConditionUtil();
                sqlc.in("rowguid", StringUtil.joinSql(opguids));
                optionlist = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
            }
            // 遍历map将其中的材料添加进materialids中
            for (AuditTaskOption audittaskoption : optionlist) {
                String material = audittaskoption.getMaterialids();
                if (StringUtil.isBlank(material)) {
                    continue;
                }
                String[] materials = material.split(";");
                for (String string : materials) {
                    if (!materialids.contains(string)) {
                        materialids.add(string);
                    }
                }
            }

            for (AuditSpITask auditSpiTask : listTask) {
                List<AuditTaskMaterial> listMaterial = mapt_m.get(auditSpiTask.getTaskguid());
                listMaterial.sort((b, a) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                        .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
                for (AuditTaskMaterial auditMaterial : listMaterial) {
                    // 根据情形过滤,如果是非必须，并且不再上面材料的提交范围中，则不初始化这个磁疗
                    if (Integer.valueOf(ZwfwConstant.NECESSITY_SET_NO).equals(auditMaterial.getNecessity())) {
                        if (!materialids.contains(auditMaterial.getMaterialid())) {
                            continue;
                        }
                    }
                    else {
                        if (!necessity_materialids.contains(auditMaterial.getMaterialid())) {
                            necessity_materialids.add(auditMaterial.getMaterialid());
                        }
                    }

                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    // 直接添加
                    auditSpIMaterial.setEffictiverange("");
                    auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialguid(auditMaterial.getMaterialid());

                    auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                    auditSpIMaterial
                            .setAllowrongque(auditMaterial.getIs_rongque() == null ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : String.valueOf(auditMaterial.getIs_rongque()));
                    auditSpIMaterial.setMaterialtype(auditMaterial.getMaterialtype());
                    auditSpIMaterial.setBiguid(biGuid);
                    auditSpIMaterial.setBusinessguid(businessGuid);
                    auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                    auditSpIMaterial.setOperatedate(new Date());
                    auditSpIMaterial.setPhaseguid(phaseGuid);
                    auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                    auditSpIMaterial.setMaterialname(auditMaterial.getMaterialname());
                    auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    // TODO 这里需要改成常量
                    auditSpIMaterial.setSubmittype(auditMaterial.getSubmittype());

                    // 判断如果是在 情形匹配材料里面需要设置成必须
                    if (materialids.contains(auditMaterial.getMaterialid())) {
                        auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                    }
                    else {
                        auditSpIMaterial.setNecessity(String.valueOf(auditMaterial.getNecessity()));
                    }

                    auditSpIMaterial.setSubappguid(subAppGuid);

                    auditSpIMaterial.setOrdernum(ordernum);
                    ordernum += 10;

                    listSpIMaterial.add(auditSpIMaterial);
                }
            }
            // 第二步，获取所有的材料对应关系 modified 查询是否在所选的事项关系中，不存在的不加
            List<AuditSpShareMaterialRelation> listRelation = auditMatrerialRelationService
                    .selectByBusinessGuid(businessGuid).getResult();
            List<AuditTask> tasklist = new ArrayList<>();
            for (AuditSpITask auditSpiTask : listTask) {
                AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditSpiTask.getTaskguid(), false)
                        .getResult();
                tasklist.add(audittask);
            }
            List<AuditSpShareMaterialRelation> listRelationnew = new ArrayList<>();
            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : listRelation) {
                for (AuditTask auditTask : tasklist) {
                    // 去除掉，不需要提交的材料的关系,结果直接加入
                    if (auditSpShareMaterialRelation.getTaskid().equals(auditTask.getTask_id())
                            && ("20".equals(auditSpShareMaterialRelation.getMaterialtype())
                                    || necessity_materialids.contains(auditSpShareMaterialRelation.getMaterialid())
                                    || materialids.contains(auditSpShareMaterialRelation.getMaterialid()))) {
                        listRelationnew.add(auditSpShareMaterialRelation);
                        break;
                    }
                }
            }
            listRelation = listRelationnew;
            // 第三步，筛选材料，并进行初始化
            // 这里可以确定，第一步取出的材料数量已经是材料的上限了，不会更多了，这里的做法是遍历关系表，然后将额外是共享材料的材料进行删除
            Iterator<AuditSpShareMaterialRelation> iterator = listRelation.iterator();

            // 存在多个材料关联一个共享材料，一个材料情形匹配为必须，则这个共享材料就是必须，声明map
            List<String> necessitymaterial = new ArrayList<>();
            while (iterator.hasNext()) {
                AuditSpShareMaterialRelation relation = iterator.next();
                AuditSpShareMaterial shareMaterial = auditMatrerialService
                        .getAuditSpShareMaterialByShareMaterialGuid(relation.getSharematerialguid(), businessGuid)
                        .getResult();
                // 如果是全局共享材料，那就只要删选一下是不是还存在重复的单个材料
                listSpIMaterial.removeIf(material -> {
                    return !ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                            && material.getMaterialguid().equals(relation.getSharematerialguid());
                });
                if (shareMaterial != null) {
                    // 如果是主题内的，那就先把单个的，共享材料设置是启用的
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        // 删除重复的共享材料
                        listSpIMaterial.removeIf(material -> {
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(material.getShared())
                                    && material.getMaterialguid().equals(relation.getSharematerialguid())) {
                                return true;
                            }
                            else {
                                return relation.getMaterialid().equals(material.getMaterialguid());
                            }
                        });
                        // 然后再添加上这个共享材料
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        if ("2".equals(shareMaterial.getEffictiverange())) {
                            auditSpIMaterial.setEffictiverange("20");
                        }
                        else {
                            auditSpIMaterial.setEffictiverange("10");
                        }
                        auditSpIMaterial.setShared(ZwfwConstant.CONSTANT_STR_ONE);
                        auditSpIMaterial.setMaterialguid(relation.getSharematerialguid());

                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        auditSpIMaterial.setBiguid(biGuid);
                        auditSpIMaterial.setBusinessguid(businessGuid);
                        auditSpIMaterial.setCliengguid(UUID.randomUUID().toString());
                        // 如果是必須提交的和情形匹配上需要提交的设置为必须,否则为非必须
                        if (necessity_materialids.contains(relation.getMaterialid())
                                || materialids.contains(relation.getMaterialid())
                                || necessitymaterial.contains(auditSpIMaterial.getMaterialguid())) {
                            auditSpIMaterial.setNecessity(ZwfwConstant.NECESSITY_SET_YES);
                            necessitymaterial.add(auditSpIMaterial.getMaterialguid());
                        }
                        else {
                            auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        }
                        auditSpIMaterial.setPhaseguid(phaseGuid);
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ZERO);
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setSubappguid(subAppGuid);
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                        listSpIMaterial.add(auditSpIMaterial);
                    }
                }
                // 如果这个材料不是结果的，那就去掉了
                if (!"20".equals(relation.getMaterialtype())) {
                    iterator.remove();
                }
            }
            List<AuditSpIMaterial> spIMaterials = new ArrayList<>();
            spIMaterials.addAll(listSpIMaterial);
            // 插入数据
            for (AuditSpIMaterial spiMaterial : listSpIMaterial) {
                // 普通材料，直接插入
                if (StringUtil.isBlank(spiMaterial.getEffictiverange())) {
                    auditSpIMaterialService.addSpIMaterial(spiMaterial);
                }
                // 主题内共享的
                else if ("20".equals(spiMaterial.getEffictiverange())) {
                    // 跨阶段共享
                    AuditSpIMaterial lastSpIMaterial = auditSpIMaterialService
                            .getLastSpIMaterial(biGuid, spiMaterial.getMaterialguid()).getResult();
                    if (lastSpIMaterial != null) {
                        spiMaterial.setStatus(lastSpIMaterial.getStatus());
                        if (attachService.getAttachCountByClientGuid(lastSpIMaterial.getCliengguid()) > 0) {
                            String newCliengguid = UUID.randomUUID().toString();
                            attachService.copyAttachByClientGuid(lastSpIMaterial.getCliengguid(), null, null,
                                    newCliengguid);
                            spiMaterial.setCliengguid(newCliengguid);
                        }
                    }
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    // 如果发生了变化，说明是包含结果的
                    if (length > listRelation.size()) {
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                        spIMaterials.add(auditSpIMaterial);
                        spIMaterials.remove(spiMaterial);
                        auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                    }
                    else {
                        auditSpIMaterialService.addSpIMaterial(spiMaterial);
                    }

                }
                // 全局共享的
                else {
                    AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                    auditSpIMaterial = (AuditSpIMaterial) spiMaterial.clone();
                    // 判断一下是不是结果
                    int length = listRelation.size();
                    listRelation.removeIf(relation -> {
                        return spiMaterial.getMaterialguid().equals(relation.getSharematerialguid());
                    });
                    if (length > listRelation.size()) {
                        auditSpIMaterial.setResult(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 需要替换成主题中配置的，这里还有个判断，如果这个共享材料实际没有多个进行匹配的，那就需要用原本事项的配置
                    AuditSpShareMaterial shareMaterial = auditMatrerialService
                            .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                    businessGuid)
                            .getResult();
                    if (shareMaterial != null && !shareMaterial.isEmpty()
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(shareMaterial.getStatus())) {
                        if (ZwfwConstant.NECESSITY_SET_NO.equals(shareMaterial.getNecessity())) {
                            auditSpIMaterial.setAllowrongque(ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        else {
                            auditSpIMaterial.setAllowrongque(shareMaterial.getRongque());
                        }
                        // auditSpIMaterial.setNecessity(shareMaterial.getNecessity());
                        auditSpIMaterial.setStatus(String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC));
                        auditSpIMaterial.setSubmittype(shareMaterial.getSubmittype());
                        auditSpIMaterial.setMaterialname(shareMaterial.getMaterialname());

                    }

                    if (shareMaterial != null) {
                        auditSpIMaterial.setOrdernum(ordernum
                                + (shareMaterial.getOrdernumber() == null ? 0 : shareMaterial.getOrdernumber()));
                    }
                    // 给材料默认的关联certclientguid
                    if (shareMaterial != null) {
                        auditSpIMaterial.setCertinfoinstanceguid(UUID.randomUUID().toString());
                    }
                    // 查找证照实例
                    List<CertInfo> certInfos = null;
                    certInfos = iCertInfoExternal.selectCertByOwner(auditSpIMaterial.getMaterialguid(), "", "", certNum,
                            false, areaCode, null);
                    CertInfo certInfo = null;
                    CertInfo certInfoB = null;
                    CertInfo certInfoC = null;
                    if (certInfos != null && !certInfos.isEmpty()) {
                        for (CertInfo mCertInfo : certInfos) {
                            // 如果查到A级，则退出循环，直接赋值A级
                            if ("A".equals(mCertInfo.getCertlevel())) {
                                certInfo = mCertInfo;
                                break;
                            }
                            else if ("B".equals(mCertInfo.getCertlevel())) {
                                certInfoB = mCertInfo;
                            }
                            else {
                                certInfoC = mCertInfo;
                            }
                        }
                        // 如果没有A级,则选择B级
                        if (certInfo == null && certInfoB != null) {
                            certInfo = certInfoB;
                        } // 如果没有B级
                        else if (certInfo == null && certInfoB == null) {
                            certInfo = certInfoC;
                        }

                        if (certInfo != null) {
                            Map<String, String> map = new HashMap<String, String>(16);
                            map.put("certcatcode=", certInfo.getCertcatcode());
                            map.put("ishistory=", ZwfwConstant.CONSTANT_STR_ZERO);
                            List<CertCatalog> cataloglist = icertcatalog.getListByCondition(map);
                            if (!cataloglist.isEmpty()) {
                                CertCatalog certcatalog = new CertCatalog();
                                certcatalog = cataloglist.get(0);
                                if (StringUtil.isNotBlank(certcatalog.getBelongtype())
                                        && (!"002".equals(certcatalog.getBelongtype()))) {

                                    List<JSONObject> attachList = iCertAttachExternal
                                            .getAttachList(certInfo.getCertcliengguid(), "");
                                    if (attachList != null && !attachList.isEmpty()) {
                                        // 设置关联
                                        attachService.deleteAttachByGuid(auditSpIMaterial.getCliengguid());
                                        if (attachList != null && !attachList.isEmpty()) {
                                            for (JSONObject json : attachList) {
                                                // 独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                                        auditSpIMaterial.getCliengguid(), json.getString("attachname"),
                                                        json.getString("contentype"), json.getString("cliengtag"),
                                                        json.getLong("size"),
                                                        iCertAttachExternal.getAttach(json.getString("attachguid"), ""),
                                                        null, null);
                                            }
                                        }
                                        auditSpIMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                                    }
                                    else {
                                        // 不存在附件
                                        // 如果材料原先有对应的附件,把原先的附件删除
                                        attachService.deleteAttachByGuid(auditSpIMaterial.getCliengguid());
                                        // 更新流程材料状态
                                        auditSpIMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                                    }
                                }
                            }
                            else {
                                auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                            }

                        }
                        else {
                            auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                        }
                    }
                    else {
                        auditSpIMaterial.setStatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    }
                    spIMaterials.add(auditSpIMaterial);
                    spIMaterials.remove(spiMaterial);
                    auditSpIMaterialService.addSpIMaterial(auditSpIMaterial);
                }
            }
            auditSubappService.updateInitmaterial(subAppGuid);
            // 第一次显示的时候移除掉做为结果的
            spIMaterials.removeIf(material -> {
                return !ZwfwConstant.CONSTANT_STR_ZERO.equals(material.getResult());
            });
            spIMaterials.sort((a, b) -> (b.getOrdernum() != null ? b.getOrdernum() : Integer.valueOf(0))
                    .compareTo(a.getOrdernum() != null ? a.getOrdernum() : Integer.valueOf(0)));
            result.setResult(auditSpIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult());
        }
        catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Void> initProjctMaterialFilterM(String businessGuid, String subAppGuid, String taskGuid,
            String projectGuid, List<String> materialids) {

        AuditCommonResult<Void> result = new AuditCommonResult<>();
        IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskMaterial.class);
        IAuditProjectMaterial auditProjectMaterialService = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectMaterial.class);
        try {

            List<AuditTaskMaterial> auditTaskMaterials = auditTaskMaterialService
                    .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
            AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
            if (auditTaskMaterials != null) {
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (ZwfwConstant.NECESSITY_SET_NO.equals(auditTaskMaterial.getNecessity().toString())) {
                        // 材料非必须，没有配置在情形中，并且没有匹配中情形
                        if (!materialids.contains(auditTaskMaterial.getMaterialid())) {
                            continue;
                        }
                    }
                    auditProjectMaterial.clear();
                    String cliengguid = UUID.randomUUID().toString();
                    auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                    auditProjectMaterial.setTaskguid(taskGuid);
                    auditProjectMaterial.setProjectguid(projectGuid);
                    auditProjectMaterial.setTaskmaterialguid(auditTaskMaterial.getRowguid());
                    auditProjectMaterial.setStatus(10);
                    auditProjectMaterial.setAuditstatus("0");
                    auditProjectMaterial.setIs_rongque(0);
                    auditProjectMaterial.setCliengguid(cliengguid);
                    auditProjectMaterial.setAttachfilefrom("1");
                    auditProjectMaterial.setTaskmaterial(auditTaskMaterial.getMaterialname());
                    IAuditSpIMaterial spIMaterialService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditSpIMaterial.class);
                    AuditSpIMaterial auditSpIMaterial = spIMaterialService
                            .getSpIMaterialByID(businessGuid, subAppGuid, auditTaskMaterial.getMaterialid())
                            .getResult();
                    if (auditSpIMaterial != null) {
                        // 不是审批结果
                        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getResult())) {
                            // 提交电子材料状态则复制
                            if ((("20").equals(auditSpIMaterial.getStatus())
                                    || ("25").equals(auditSpIMaterial.getStatus()))
                                    && StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                                IAttachService attachService = ContainerFactory.getContainInfo()
                                        .getComponent(IAttachService.class);
                                attachService.copyAttachByClientGuid(auditSpIMaterial.getCliengguid(), null, null,
                                        cliengguid);
                            }
                            auditProjectMaterial.setStatus(Integer.parseInt(auditSpIMaterial.getStatus()));
                        }
                        auditProjectMaterial.setIs_rongque(Integer.parseInt(auditSpIMaterial.getAllowrongque()));
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpIMaterial.getShared())) {
                            auditProjectMaterial.setSharematerialiguid(auditSpIMaterial.getMaterialguid());
                        }
                        auditProjectMaterial.setCertinfoinstanceguid(auditSpIMaterial.getCertinfoinstanceguid());
                    }
                    auditProjectMaterialService.addProjectMateiral(auditProjectMaterial);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
