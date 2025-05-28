package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
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
@RestController("registerspglxmspsxblxxxxbaddv3action")
@Scope("request")
public class RegisterSpglXmspsxblxxxxbAddV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxblxxxxbV3 iSpglXmspsxblxxxxb;
    @Autowired
    private ISpglXmspsxblxxbV3 iSpglXmspsxblxxb;
    @Autowired
    private ISpglsplcjdsxxxb iSpglDfxmsplcjdsxxxb;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    // 下拉框组件Model
    private List<SelectItem> blztModel;
    private List<SelectItem> shifouModel;
    private List<SelectItem> spsxblModel;

    /**
     * 实体对象
     */
    private SpglXmspsxblxxxxbV3 dataBean = null;

    private String gcdm;
    private String xzqhdm;
    private String spsxslbm;

    public void pageLoad() {
        gcdm = getRequestParameter("gcdm");
        xzqhdm = getRequestParameter("xzqhdm");
        spsxslbm = getRequestParameter("spsxslbm");
        if (dataBean == null) {
            dataBean = new SpglXmspsxblxxxxbV3();
            dataBean.setGcdm(gcdm);
            dataBean.setXzqhdm(xzqhdm);
            dataBean.setSpsxslbm(spsxslbm);
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setDfsjzj(UUID.randomUUID().toString());

        dataBean.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        dataBean.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        dataBean.set("sync", "-1");

        iSpglXmspsxblxxxxb.insert(dataBean);

        addCallbackParam("msg", "新增成功！");
    }

    // 办理状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getBlztModel() {
        if (blztModel == null) {
            blztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_事项办理状态", null, false));
        }
        return this.blztModel;
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

    // 审批事项办理下拉
    public List<SelectItem> getSpsxblModel() {
        if (spsxblModel == null) {
            spsxblModel = new ArrayList<SelectItem>();
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("gcdm", dataBean.getGcdm());
            sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.eq("sync", "-1");
            List<SpglXmspsxblxxbV3> list = iSpglXmspsxblxxb.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(list)) {
                for (SpglXmspsxblxxbV3 info : list) {
                    spsxblModel.add(
                            new SelectItem(info.getSpsxslbm(), setSpsxmcSpsx(info) + "(" + info.getSpsxslbm() + ")"));
                }
            }
        }
        return this.spsxblModel;
    }

    private String setSpsxmcSpsx(Record info) {
        String spsxmc = null;
        SqlConditionUtil sqlC = new SqlConditionUtil();
        sqlC.eq("xzqhdm", info.get("xzqhdm"));
        //        sqlC.eq("splcbm", info.get("splcbm"));
        //        sqlC.eq("splcbbh", info.get("splcbbh") == null ? null : info.get("splcbbh").toString());
        sqlC.eq("spsxbm", info.get("spsxbm"));
        sqlC.eq("spsxbbh", info.get("spsxbbh") == null ? null : info.get("spsxbbh").toString());
        sqlC.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
        sqlC.nq("IFNULL(sync,0)", "-1");
        List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlC.getMap()).getResult();
        if (ValidateUtil.isNotBlankCollection(result)) {
            spsxmc = result.get(0).getSpsxmc();
        }
        return spsxmc;
    }

    public SpglXmspsxblxxxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxblxxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
