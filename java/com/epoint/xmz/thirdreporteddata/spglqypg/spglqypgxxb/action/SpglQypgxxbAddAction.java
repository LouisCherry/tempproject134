package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.basic.spgl.service.SpglCommonService;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.BaseEntity;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgsxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgsxxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgxxbV3Service;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 区域评估信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
@RestController("spglqypgxxbaddaction")
@Scope("request")
public class SpglQypgxxbAddAction extends BaseController {
    @Autowired
    private ISpglQypgxxbService service;
    @Autowired
    private ISpglQypgsxxxbService iSpglQypgsxxxbService;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private ISpglCommon iSpglCommon;
    @Autowired
    private ISpglQypgxxbV3Service iSpglQypgxxbV3Service;
    @Autowired
    private ISpglQypgsxxxbV3Service iSpglQypgsxxxbV3Service;
    @Autowired
    private IAttachService attachService;
    /**
     * 区域评估信息表实体对象
     */
    private SpglQypgxxb dataBean = null;

    private String guid;
    private SpglQypgxxb spglQypgxxb;
    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgsxxxb> model;
    private String qypgdybm;
    private List<SelectItem> areaModel = null;

    public void pageLoad() {
        guid = getRequestParameter("guid");
        spglQypgxxb = service.find(guid);
        if (spglQypgxxb == null) {
            dataBean = new SpglQypgxxb();
        } else {
            dataBean = spglQypgxxb;
            qypgdybm = spglQypgxxb.getQypgdybm();
            // 区域评估事项信息使用该字段
            addCallbackParam("qypgdybm", qypgdybm);
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        if (spglQypgxxb == null) {
            dataBean.setRowguid(guid);
            service.insert(dataBean);
        } else {
            service.update(dataBean);
        }
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    public List<SelectItem> getAreaModel() {
        if (areaModel == null) {
            areaModel = new ArrayList<>();
            AuditOrgaArea area = iAuditOrgaArea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult();
            if (ZwfwConstant.AREA_TYPE_XQJ.equals(area.getCitylevel())) {
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(area.getXiaqucode());
                selectItem.setText(area.getXiaquname());
                areaModel.add(selectItem);
            }
            if (ZwfwConstant.AREA_TYPE_SJ.equals(area.getCitylevel())) {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.in("citylevel", "'1','2'");
                List<AuditOrgaArea> areaList = iAuditOrgaArea.selectAuditAreaList(sql.getMap()).getResult();
                for (AuditOrgaArea auditOrgaArea : areaList) {
                    SelectItem selectItem = new SelectItem();
                    selectItem.setValue(auditOrgaArea.getXiaqucode());
                    selectItem.setText(auditOrgaArea.getXiaquname());
                    areaModel.add(selectItem);
                }
            }
        }
        return areaModel;
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iSpglQypgsxxxbService.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<SpglQypgsxxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglQypgsxxxb>() {

                @Override
                public List<SpglQypgsxxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("qypgguid", guid);
                    PageData<SpglQypgsxxxb> pageData = iSpglQypgsxxxbService.getAuditSpDanitemByPage(sql.getMap(),
                            first, pageSize, sortField, sortOrder).getResult();
                    List<SpglQypgsxxxb> list = pageData.getList();
                    for (SpglQypgsxxxb spglQypgsxxxb : list) {
                        String cliengguid = spglQypgsxxxb.getCliengguid();
                        if (StringUtil.isNotBlank(cliengguid)) {
                            List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(cliengguid);
                            if (EpointCollectionUtils.isNotEmpty(attachList)) {
                                FrameAttachInfo attachInfo = attachList.get(0);
                                spglQypgsxxxb.set("fileurl", iAttachService.getAttachDownPath(attachInfo));
                                spglQypgsxxxb.set("filename", attachInfo.getAttachFileName());
                            }
                        }
                    }

                    this.setRowCount(pageData.getRowCount());
                    return list;
                }

            };
        }
        return model;
    }


    /**
     * 同步
     */
    public void syncQypgPush(String rowguid) {
        // 事务控制
        String msg = "同步成功！";
        try {
            EpointFrameDsManager.begin(null);

            SpglQypgxxb sqQypgxxbedit = service.find(guid);
            if (sqQypgxxbedit != null) {
                SpglQypgxxbV3 spQypgxxbV3 = new SpglQypgxxbV3();
                BeanUtils.copyProperties(sqQypgxxbedit, spQypgxxbV3);
                spQypgxxbV3.setRowguid(UUID.randomUUID().toString());
                spQypgxxbV3.setDfsjzj(guid);
                spQypgxxbV3.setSync(ZwfwConstant.CONSTANT_INT_ZERO);
                // 查询是否有同步过的数据
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("dfsjzj", guid);
                sqlConditionUtil.eq("sjyxbs", "1");
                List<SpglQypgxxbV3> list = iSpglQypgxxbV3Service.getAuditSpDanitemByPage(sqlConditionUtil.getMap(), -1, -1, null, null).getResult().getList();
                if (EpointCollectionUtils.isNotEmpty(list)) {
                    //若存在则说明【同步过】
                    spQypgxxbV3.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
                    iSpglCommon.editToPushData(list.get(0), spQypgxxbV3, true);
                } else {
                    //不存在则说明【未同步过】则直接将数据插入至【区域评估信息上报表(SPGL_QYPGXXB_V3)】
                    spQypgxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                    spQypgxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                    iSpglQypgxxbV3Service.insert(spQypgxxbV3);
                }
                sqQypgxxbedit.setSync(ZwfwConstant.CONSTANT_INT_ONE);
                service.update(sqQypgxxbedit);

                // 处理区域评估事项信息
                sqlConditionUtil.clear();
                sqlConditionUtil.eq("qypgdybm", sqQypgxxbedit.getQypgdybm());
                List<SpglQypgsxxxb> listByMap = iSpglQypgsxxxbService.getListByMap(sqlConditionUtil.getMap());
                if (EpointCollectionUtils.isNotEmpty(listByMap)) {
                    for (SpglQypgsxxxb spglQypgsxxxb : listByMap) {
                        SpglQypgsxxxbV3 spglQypgsxxxbV3 = new SpglQypgsxxxbV3();
                        spglQypgsxxxbV3.setRowguid(UUID.randomUUID().toString());
                        spglQypgsxxxbV3.setDfsjzj(spglQypgsxxxb.getRowguid());
                        spglQypgsxxxbV3.setXzqhdm(sqQypgxxbedit.getXzqhdm());
                        spglQypgsxxxbV3.setQypgdybm(spglQypgsxxxb.getQypgdybm());
                        spglQypgsxxxbV3.setQypgsxbm(spglQypgsxxxb.getQypgsxbm());
                        spglQypgsxxxbV3.setQypgsxmc(spglQypgsxxxb.getQypgsxmc());
                        spglQypgsxxxbV3.setDybzspsxbm(spglQypgsxxxb.getDybzspsxbm());
                        spglQypgsxxxbV3.setQypgcgsxrq(spglQypgsxxxb.getQypgcgsxrq());
                        spglQypgsxxxbV3.setQypgcgjzrq(spglQypgsxxxb.getQypgcgjzrq());
                        spglQypgsxxxbV3.setJhspdfs(spglQypgsxxxb.getJhspdfs());
                        // 查询附件
                        List<FrameAttachInfo> attachInfos = attachService
                                .getAttachInfoListByGuid(spglQypgsxxxb.getStr("cliengguid"));
                        if (EpointCollectionUtils.isNotEmpty(attachInfos)) {
                            spglQypgsxxxbV3.setQypgcgcllx(attachInfos.get(0).getContentType());
                            spglQypgsxxxbV3.setQypgcgclmc(attachInfos.get(0).getAttachFileName());
                            spglQypgsxxxbV3.setQypgcgfjid(attachInfos.get(0).getAttachGuid());
                        }
                        spglQypgsxxxbV3.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                        // 查询是否有同步过的数据
                        SpglQypgsxxxbV3 spglQypgsxxxbV3edit = iSpglQypgsxxxbV3Service.getSpglQypgsxxxbByDfsjzj(spglQypgsxxxb.getRowguid());
                        if (spglQypgsxxxbV3edit != null) {
                            // 若存在则说明【同步过】
                            iSpglCommon.editToPushData(spglQypgsxxxbV3edit, spglQypgsxxxbV3);
                        } else {
                            // 不存在则说明【未同步过】则直接将数据插入至【区域评估事项信息上报表(SPGL_QYPGSXXXB_V3)】
                            spglQypgsxxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                            spglQypgsxxxbV3.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                            iSpglQypgsxxxbV3Service.insert(spglQypgsxxxbV3);
                        }
                        spglQypgsxxxb.setSync(ZwfwConstant.CONSTANT_STR_ONE);
                        iSpglQypgsxxxbService.update(spglQypgsxxxb);

                    }
                }

                // 判断有没有本地删除了前置库还存在的数据
                List<SpglQypgsxxxbV3> spglQypgsxxxbV3s = iSpglQypgsxxxbV3Service
                        .getDataByQypgdybm(sqQypgxxbedit.getQypgdybm());
                if (EpointCollectionUtils.isNotEmpty(spglQypgsxxxbV3s)) {
                    for (SpglQypgsxxxbV3 spglQypgsxxxbV3 : spglQypgsxxxbV3s) {
                        // 推送无效数据
                        BaseEntity record = (BaseEntity) spglQypgsxxxbV3.clone();
                        pushToDisable(record);
                        // 本地信息作废
                        spglQypgsxxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ZERO);
                        spglQypgsxxxbV3.setSjwxyy("数据已删除");
                        iSpglQypgsxxxbV3Service.update(spglQypgsxxxbV3);
                    }
                }

            }
            EpointFrameDsManager.commit();
        } catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } finally {
            addCallbackParam("msg", msg);
        }

    }

    /**
     * 推送无效数据
     *
     * @param record
     */
    public void pushToDisable(BaseEntity record) {
        SpglCommonService service = new SpglCommonService();
        record.set("row_id", null);
        // rowguid 重置
        record.set("rowguid", UUID.randomUUID().toString());
        // lsh置空
        record.set("lsh", null);
        // 置为无效数据
        record.set("sjyxbs", ZwfwConstant.CONSTANT_INT_ZERO);
        // 数据无效原因：重新上报产生的一条无效、新增、未同步的数据，用于通知前置库与该数据约束一致的记录更新为无效，等待约束一致的一条有效、新增、未同步的数据插入...
        record.set("sjwxyy", "重新上报新增的无效未同步数据，用于通知前置库与该数据约束一致的记录数据上传状态更新为无效，等待约束一致的一条有效数据的插入...");
        // 数据上传状态 -> 新增数据 0
        record.set("sjsczt", ZwfwConstant.CONSTANT_INT_ZERO);
        // 同步状态 -> 未同步 0
        record.set("sync", ZwfwConstant.CONSTANT_INT_ZERO);
        // 失败原因置空
        record.set("sbyy", null);
        service.insert(record);
    }

    public SpglQypgxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglQypgxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglQypgxxb dataBean) {
        this.dataBean = dataBean;
    }

}
