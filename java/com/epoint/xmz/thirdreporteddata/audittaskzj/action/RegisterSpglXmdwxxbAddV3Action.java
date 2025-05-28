package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZrztxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZrztxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 项目责任主体表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("registerspglxmdwxxbaddv3action")
@Scope("request")
public class RegisterSpglXmdwxxbAddV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglZrztxxbV3Service service;

    // 下拉框组件Model
    private List<SelectItem> dwlxModel;

    /**
     * 实体对象
     */
    private SpglZrztxxbV3 dataBean;

    private String xmdm;
    private String gcdm;
    private String xzqhdm;

    public void pageLoad() {
        xmdm = getRequestParameter("xmdm");
        gcdm = getRequestParameter("gcdm");
        xzqhdm = getRequestParameter("xzqhdm");
        if (dataBean == null) {
            dataBean = new SpglZrztxxbV3();
            dataBean.setXmdm(xmdm);
            dataBean.setGcdm(gcdm);
            dataBean.setXzqhdm(xzqhdm);
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        String msg = "新增成功！";
        // 如果存在建设单位，则不允许其他单位选择建设单位
        if (ZwfwConstant.CONSTANT_INT_ONE == dataBean.getDwlx()) {
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("gcdm", dataBean.getGcdm());
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("dwlx", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.nq("rowguid", dataBean.getRowguid());
            List<SpglZrztxxbV3> list = service.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(list)) {
                msg = "建设单位有且仅有一个，请选择其他单位类型!";
                addCallbackParam("msg", msg);
                addCallbackParam("flag", "false");
                return;
            }
        }

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        // 校验状态 本地校验失败为-1 校验没问题为0
        dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        // 新增时必为草稿（不同步）的新增数据 上报后为未同步的新增数据
        dataBean.set("sync", "-1");

        service.insert(dataBean);
        addCallbackParam("msg", msg);
    }

    // 项目单位类型
    @SuppressWarnings("unchecked")
    public List<SelectItem> getDwlxModel() {
        if (dwlxModel == null) {
            dwlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_项目单位类型", null, false));
        }
        return this.dwlxModel;
    }

    public SpglZrztxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglZrztxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
