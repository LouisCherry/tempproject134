package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 区域评估信息表详情页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
@RestController("spglqypgxxbdetailaction")
@Scope("request")
public class SpglQypgxxbDetailAction extends BaseController {
    @Autowired
    private ISpglQypgxxbService service;
    @Autowired
    private ISpglQypgsxxxbService spglQypgsxxxbService;
    @Autowired
    private IAttachService iAttachService;

    /**
     * 区域评估信息表实体对象
     */
    private SpglQypgxxb dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglQypgsxxxb> model;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglQypgxxb();
        }
    }

    public DataGridModel<SpglQypgsxxxb> getQypgxsDatagrid() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglQypgsxxxb>() {

                @Override
                public List<SpglQypgsxxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("qypgguid", dataBean.getRowguid());
                    PageData<SpglQypgsxxxb> pageData = spglQypgsxxxbService.getAuditSpDanitemByPage(sql.getMap(), first,
                            pageSize, "operatedate", "DESC").getResult();
                    this.setRowCount(pageData.getRowCount());
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
                    return list;
                }

            };
        }
        return model;
    }

    public SpglQypgxxb getDataBean() {
        return dataBean;
    }
}
