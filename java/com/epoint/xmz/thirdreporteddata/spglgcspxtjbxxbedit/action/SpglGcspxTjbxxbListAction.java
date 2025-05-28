package com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxb.api.ISpglGcspxtjbxxbService;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxb.api.entity.SpglGcspxtjbxxb;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.ISpglGcspxtjbxxbEditService;
import com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.entity.SpglGcspxtjbxxbEdit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 工改维护list页面对应的后台
 *
 * @author lzm
 * @version [版本号, 2023-09-01 10:29:56]
 */
@RestController("spglgcspxtjbxxblistaction")
@Scope("request")
public class SpglGcspxTjbxxbListAction extends BaseController {
    @Autowired
    private ISpglGcspxtjbxxbEditService service;
    @Autowired
    private ISpglGcspxtjbxxbService iSpglGcspxtjbxxbService;
    @Autowired
    private ISpglCommon ycISpglCommon;
    /**
     * 工改结果表实体对象
     */
    private SpglGcspxtjbxxbEdit dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglGcspxtjbxxbEdit> model;


    public void pageLoad() {
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<SpglGcspxtjbxxbEdit> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglGcspxtjbxxbEdit>() {

                @Override
                public List<SpglGcspxtjbxxbEdit> fetchData(int first, int pageSize, String sortField,
                                                           String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getJsdwmc())) {
                        sql.like("jsdwmc", dataBean.getJsdwmc());
                    }
                    if (StringUtil.isNotBlank(dataBean.getJsdwlxr())) {
                        sql.like("jsdwlxr", dataBean.getJsdwlxr());
                    }
                    PageData<SpglGcspxtjbxxbEdit> pageData = service.getDbListByPage(SpglGcspxtjbxxbEdit.class,
                            sql.getMap(), first, pageSize, sortField,
                            sortOrder);
                    for (SpglGcspxtjbxxbEdit spglGcspxtjbxxbEdit : pageData.getList()) {
                        if (ZwfwConstant.CONSTANT_STR_ZERO.equals(spglGcspxtjbxxbEdit.getSync())) {
                            spglGcspxtjbxxbEdit.setSync("未上报");
                        }
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(spglGcspxtjbxxbEdit.getSync())) {
                            spglGcspxtjbxxbEdit.setSync("已上报");
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    /**
     * 同步
     */
    public void SyncSpglGcspxtjbxxb(String rowguid) {
        // 事务控制
        String msg = "同步成功！";
        try {
            EpointFrameDsManager.begin(null);

            SpglGcspxtjbxxbEdit spglGcspxtjbxxbEdit = service.find(rowguid);
            if (spglGcspxtjbxxbEdit != null) {
                SpglGcspxtjbxxb spglGcspxtjbxxb = new SpglGcspxtjbxxb();
                BeanUtils.copyProperties(spglGcspxtjbxxbEdit, spglGcspxtjbxxb);
                spglGcspxtjbxxb.setDfsjzj(rowguid);
                spglGcspxtjbxxb.setRowguid(UUID.randomUUID().toString());
                spglGcspxtjbxxb.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                // 查询该辖区是否有同步过的数据
                SpglGcspxtjbxxb oldspglGcspxtjbxxb =
                        iSpglGcspxtjbxxbService.getDataByXq(spglGcspxtjbxxbEdit.getRowguid());
                if (oldspglGcspxtjbxxb != null) {
                    //若存在则说明【同步过】
                    ycISpglCommon.editToPushData(oldspglGcspxtjbxxb, spglGcspxtjbxxb);
                }
                else {
                    //不存在则说明【未同步过】则直接将数据插入至【工改系统信息上报表(SPGL_GCSPXTJBXXB)】
                    spglGcspxtjbxxb.setSync(ZwfwConstant.CONSTANT_STR_ZERO);
                    spglGcspxtjbxxb.setSjyxbs(ZwfwConstant.CONSTANT_STR_ONE);
                    spglGcspxtjbxxb.setSjsczt(ZwfwConstant.CONSTANT_STR_ZERO);
                    iSpglGcspxtjbxxbService.insert(spglGcspxtjbxxb);
                }
                spglGcspxtjbxxbEdit.setSync(ZwfwConstant.CONSTANT_STR_ONE);
                service.update(spglGcspxtjbxxbEdit);
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "同步失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
        }

    }


    public SpglGcspxtjbxxbEdit getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglGcspxtjbxxbEdit();
        }
        return dataBean;
    }

    public void setDataBean(SpglGcspxtjbxxbEdit dataBean) {
        this.dataBean = dataBean;
    }


}
