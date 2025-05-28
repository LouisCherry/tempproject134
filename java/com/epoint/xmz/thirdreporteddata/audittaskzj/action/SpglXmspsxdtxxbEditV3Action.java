package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmdtxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmdtxxbV3Service;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjbxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目审批事项批复文件信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("spglxmspsxdtxxbeditv3action")
@Scope("request")
public class SpglXmspsxdtxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmdtxxbV3Service service;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private ISpglCommon ispglcommon;
    @Autowired
    private ISpglXmjbxxbV3 spglXmjbxxbV3;

    // 下拉框组件Model
    private List<SelectItem> shifouModel;
    private List<SelectItem> tbcxModel;
    private List<SelectItem> tbcxsxlxModel;

    /**
     * 工程类别下拉列表model
     */
    private List<SelectItem> gcytModel = null;

    /**
     * 实体对象
     */
    private SpglXmdtxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean != null) {
            addCallbackParam("sjsczt", dataBean.getSjsczt().toString());
            addCallbackParam("sbyy", StringUtil.isNotBlank(dataBean.getSbyy()) ? dataBean.getSbyy() : "无");
            addCallbackParam("sync", dataBean.getStr("sync"));
            int sjsczt = dataBean.getSjsczt();
            String sjscztText = "";
            if (sjsczt == -1) {
                sjscztText = "本地校验失败";
            }
            else {
                sjscztText = codeItemsService.getItemTextByCodeName("国标_数据上传状态", String.valueOf(sjsczt));
            }
            addCallbackParam("sjscztText", sjscztText);

            // 查询工程行业分类进行单体编码的显隐控制
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("gcdm", dataBean.getGcdm());
            sqlConditionUtil.eq("xzqhdm", dataBean.getXzqhdm());
            List<SpglXmjbxxbV3> result = spglXmjbxxbV3.getListByCondition(sqlConditionUtil.getMap()).getResult();
            if (EpointCollectionUtils.isNotEmpty(result)) {
                addCallbackParam("gcfl", result.get(0).getGchyfl());
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        // 事务控制
        String msg = "上报成功！";
        String rowguid = getRequestParameter("guid");
        try {
            EpointFrameDsManager.begin(null);

            SpglXmdtxxbV3 oldDataBean = service.find(rowguid);

            ispglcommon.editToPushData(oldDataBean, dataBean, true);

            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            msg = "上报失败！";
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            addCallbackParam("msg", msg);
            EpointFrameDsManager.close();
        }
    }

    // 数据有效标识
    @SuppressWarnings("unchecked")
    public List<SelectItem> getShifouModel() {
        if (shifouModel == null) {
            shifouModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.shifouModel;
    }

    public SpglXmdtxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmdtxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
