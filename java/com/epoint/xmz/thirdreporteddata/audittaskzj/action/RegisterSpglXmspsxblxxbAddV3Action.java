package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 项目审批事项办理信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("registerspglxmspsxblxxbaddv3action")
@Scope("request")
public class RegisterSpglXmspsxblxxbAddV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxb;
    @Autowired
    private ISpglsplcxxb iSpglDfxmsplcxxb;
    @Autowired
    private ISpglsplcjdsxxxb iSpglDfxmsplcjdsxxxb;
    // 下拉框组件Model
    private List<SelectItem> gkfsMode;
    private List<SelectItem> slfsMode;
    private List<SelectItem> shifouModel;
    private List<SelectItem> splcbbhModel;
    private List<SelectItem> splcjdsxModel;

    /**
     * 实体对象
     */
    private SpglXmspsxblxxbV3 dataBean = null;

    private String gcdm;
    private String xzqhdm;
    private String splcbm;
    private String splcbbh;

    public void pageLoad() {
        gcdm = getRequestParameter("gcdm");
        xzqhdm = getRequestParameter("xzqhdm");
        splcbm = getRequestParameter("splcbm");
        splcbbh = getRequestParameter("splcbbh");
        if (dataBean == null) {
            dataBean = new SpglXmspsxblxxbV3();
            dataBean.setGcdm(gcdm);
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setSplcbm(splcbm);
            dataBean.setSplcbbh(Double.parseDouble(splcbbh));
            // 解决打开直接保存double被js转成int的问题
            if (dataBean.getSplcbbh() != null) {
                dataBean.set("splcbbhStr", dataBean.getSplcbbh().toString());
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        String msg = "保存成功！";
        /*try {
            EpointFrameDsManager.begin(null);
            
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }finally {
            EpointFrameDsManager.close();
        }*/
        // 先把splcbbhStr得值放入splcbbh
        dataBean.setSplcbbh(Double.parseDouble(dataBean.get("splcbbhStr")));

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setBlspslbm(UUID.randomUUID().toString());

        dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);// 默认为新增数据
        dataBean.set("sync", "-1");// 必为草稿（不同步）

        iSpglXmspsxblxxb.insert(dataBean);

        addCallbackParam("msg", msg);
    }

    // 受理方式
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSlfsModel() {
        if (slfsMode == null) {
            slfsMode = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_受理方式", null, false));
        }
        return this.slfsMode;
    }

    // 公开方式
    @SuppressWarnings("unchecked")
    public List<SelectItem> getGkfsModel() {
        if (gkfsMode == null) {
            gkfsMode = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_公开方式", null, false));
        }
        return this.gkfsMode;
    }

    // 是否
    @SuppressWarnings("unchecked")
    public List<SelectItem> getShifouModel() {
        if (shifouModel == null) {
            shifouModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.shifouModel;
    }

    // 审批流程版本号
    public List<SelectItem> getSplcbbhModel() {
        if (splcbbhModel == null) {
            splcbbhModel = new ArrayList<SelectItem>();
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", xzqhdm);
            sUtil.eq("splcbm", splcbm);
            sUtil.setOrderDesc("splcbbh");
            List<Spglsplcxxb> lcList = iSpglDfxmsplcxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(lcList)) {
                for (Spglsplcxxb lcxxb : lcList) {
                    splcbbhModel.add(new SelectItem(lcxxb.getSplcbbh().toString(), lcxxb.getSplcbbh().toString()));
                }
            }
        }
        return this.splcbbhModel;
    }

    public void getSpsxbbh(String spsxbm) {
        SqlConditionUtil sUtil = new SqlConditionUtil();
        sUtil.eq("xzqhdm", xzqhdm);
        sUtil.eq("splcbm", splcbm);
        sUtil.eq("spsxbm", spsxbm);
        sUtil.eq("splcbbh", dataBean.get("splcbbhStr"));
        sUtil.setOrderDesc("spsxbbh");
        List<Spglsplcjdsxxxb> list = iSpglDfxmsplcjdsxxxb.getListByCondition(sUtil.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(list)) {
            Spglsplcjdsxxxb info = list.get(0);
            if (info != null) {
                addCallbackParam("spsxbbh", info.getSpsxbbh());
            }
        }
    }

    public void getSpbmmc() {
        SqlConditionUtil sUtil = new SqlConditionUtil();
        sUtil.eq("xzqhdm", xzqhdm);
        sUtil.eq("splcbm", splcbm);
        sUtil.eq("splcbbh", dataBean.get("splcbbhStr"));
        sUtil.eq("spsxbm", dataBean.getSpsxbm());
        sUtil.eq("spsxbbh", dataBean.getSpsxbbh() == null ? "" : dataBean.getSpsxbbh().toString());
        List<Spglsplcjdsxxxb> list = iSpglDfxmsplcjdsxxxb.getListByCondition(sUtil.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(list)) {
            Spglsplcjdsxxxb splcjdsxxxb = list.get(0);
            if (splcjdsxxxb != null) {
                // addCallbackParam("spbmbm", splcjdsxxxb.getSpbmbm());
                // addCallbackParam("spbmmc", splcjdsxxxb.getSpbmmc());
            }
        }
    }

    public SpglXmspsxblxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxblxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
