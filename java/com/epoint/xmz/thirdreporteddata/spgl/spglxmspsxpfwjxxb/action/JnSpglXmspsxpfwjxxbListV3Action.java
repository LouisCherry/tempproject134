package com.epoint.xmz.thirdreporteddata.spgl.spglxmspsxpfwjxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxpfwjxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目审批事项办理详细信息表list页面对应的后台
 *
 * @author fwang
 * @version [版本号, 2010-07-05 15:59:26]
 */
@RestController("jnspglxmspsxpfwjxxblistv3action")
@Scope("request")
public class JnSpglXmspsxpfwjxxbListV3Action extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglXmspsxpfwjxxbV3 iSpglXmspsxpfwjxxbv3;

    /**
     * 实体对象
     */
    private SpglXmspsxpfwjxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmspsxpfwjxxbV3> model;

    private List<SelectItem> syncMoel;

    private String gcdm;
    private String spsxslbm;
    private String pfwjbt;
    private String sync;

    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new SpglXmspsxpfwjxxbV3();
        }
    }

    public DataGridModel<SpglXmspsxpfwjxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmspsxpfwjxxbV3>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<SpglXmspsxpfwjxxbV3> fetchData(int first, int pageSize, String sortField,
                                                           String sortOrder) {
                    SqlConditionUtil sUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(pfwjbt)) {
                        sUtil.like("pfwjbt", pfwjbt);
                    }
                    if (StringUtil.isNotBlank(gcdm)) {
                        sUtil.like("gcdm", gcdm);
                    }
                    if (StringUtil.isNotBlank(spsxslbm)) {
                        sUtil.like("spsxslbm", spsxslbm);
                    }
                    if (StringUtil.isNotBlank(sync)) {
                        sUtil.like("sync", sync);
                    }
                    // 数据有效
                    sUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    PageData<SpglXmspsxpfwjxxbV3> pageData = iSpglXmspsxpfwjxxbv3.getAllByPage(sUtil.getMap(), first,
                            pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<SpglXmspsxpfwjxxbV3> list = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        for (SpglXmspsxpfwjxxbV3 bean : list) {
                            // 本地校验失败
                            if ("0".equals(bean.getStr("sync"))) {
                                bean.put("sync", "新增");
                            } else if ("1".equals(bean.getStr("sync"))) {
                                bean.put("sync", "成功");
                            } else if ("-1".equals(bean.getStr("sync"))) {
                                bean.put("sync", "失败");
                            }
                        }
                    }
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iSpglXmspsxpfwjxxbv3.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    // 上报状态
    @SuppressWarnings("unchecked")
    public List<SelectItem> getsyncModel() {
        if (syncMoel == null) {
            List<SelectItem> result = new ArrayList<>();
            result.add(new SelectItem("", "请选择..."));
            result.add(new SelectItem("-2", "失败"));
            result.add(new SelectItem("0", "新增"));
            result.add(new SelectItem("1", "成功"));
            this.syncMoel = result;
        }
        return syncMoel;
    }

    public String getGcdm() {
        return gcdm;
    }

    public void setGcdm(String gcdm) {
        this.gcdm = gcdm;
    }

    public String getSpsxslbm() {
        return spsxslbm;
    }

    public void setSpsxslbm(String spsxslbm) {
        this.spsxslbm = spsxslbm;
    }

    public String getPfwjbt() {
        return pfwjbt;
    }

    public void setPfwjbt(String pfwjbt) {
        this.pfwjbt = pfwjbt;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public SpglXmspsxpfwjxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglXmspsxpfwjxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

}
