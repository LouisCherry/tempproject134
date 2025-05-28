package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.spgl.service.SpglCommonService;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.BaseEntity;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgsxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglQypgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgsxxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglQypgxxbV3Service;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 区域评估信息表新增页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
@RestController("spglqypgxxblistaction")
@Scope("request")
public class SpglQypgxxbListAction extends BaseController {
    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;

    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    @Autowired
    private ISpglQypgxxbV3Service iSpglQypgxxbV3Service;
    @Autowired
    private ISpglQypgsxxxbV3Service iSpglQypgsxxxbV3Service;
    /**
     * 区域评估信息表实体对象
     */
    private SpglQypgxxb dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgxxb> qypgModel;

    public void pageLoad() {
    }

    /**
     * 删除选定
     */
    public void deleteQypgSelect() {
        List<String> select = getQypgDatagrid().getSelectKeys();
        for (String sel : select) {
            // 删除数据时进行数据作废
            syncDataDisabled(sel);
            iSpglQypgxxbService.deleteByGuid(sel);

        }
        addCallbackParam("msg", l("成功删除！"));
    }

    /**
     * 数据作废 往前置库插一条作废数据 并把本地数据置成无效
     * [一句话功能简述]
     *
     * @param rowguid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void syncDataDisabled(String rowguid) {
        // 对区域评估信息和区域评估事项信息作废(如果还没同步就不用做处理)
        SpglQypgxxbV3 spglQypgxxbV3 = iSpglQypgxxbV3Service.getSpglQypgxxbByDfsjzj(rowguid);
        if (spglQypgxxbV3 != null) {
            // 推送无效数据
            BaseEntity record = (BaseEntity) spglQypgxxbV3.clone();
            pushToDisable(record);
            // 本地信息作废
            spglQypgxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ZERO);
            spglQypgxxbV3.setSjwxyy("数据已删除");
            iSpglQypgxxbV3Service.update(spglQypgxxbV3);
            // 查询事项信息
            List<SpglQypgsxxxbV3> listByMap = iSpglQypgsxxxbV3Service.getListByQypgbm(spglQypgxxbV3.getQypgdybm());
            if (EpointCollectionUtils.isNotEmpty(listByMap)) {
                for (SpglQypgsxxxbV3 spglQypgsxxxbV3 : listByMap) {
                    // 推送无效数据
                    BaseEntity recordiEntity = (BaseEntity) spglQypgsxxxbV3.clone();
                    pushToDisable(recordiEntity);
                    // 本地信息作废
                    spglQypgsxxxbV3.setSjyxbs(ZwfwConstant.CONSTANT_INT_ZERO);
                    spglQypgsxxxbV3.setSjwxyy("数据已删除");
                    iSpglQypgsxxxbV3Service.update(spglQypgsxxxbV3);
                }
            }
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

    public DataGridModel<SpglQypgxxb> getQypgDatagrid() {
        // 获得表格对象
        if (qypgModel == null) {
            qypgModel = new DataGridModel<SpglQypgxxb>() {
                @Override
                public List<SpglQypgxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    String areaCode = ZwfwUserSession.getInstance().getAreaCode();
                    AuditOrgaArea area = iAuditOrgaArea.getAreaByAreacode(areaCode).getResult();
                    if (ZwfwConstant.AREA_TYPE_XQJ.equals(area.getCitylevel())) {
                        sql.eq("xzqhdm", areaCode);
                    }
                    PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getAuditSpDanitemByPage(sql.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return qypgModel;
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
