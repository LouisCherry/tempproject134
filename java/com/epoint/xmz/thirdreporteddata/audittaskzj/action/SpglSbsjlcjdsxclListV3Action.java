package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspjdxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController("spglsbsjlcjdsxcllistv3action")
@Scope("request")
public class SpglSbsjlcjdsxclListV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlcjdsxclListV3Action.class);

    /**
     * 流程信息实体对象
     */
    private Spglsplcjdsxxxb dataBean;

    @Autowired
    private ISpglspjdxxb iSpglspjdxxb;

    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    /**
     * 表格控件model
     */
    private DataGridModel<Spglsplcjdsxxxb> model;

    private List<SelectItem> sbztModel = null;

    private String splcbm = null;
    private String splcbbh = null;
    private String xzqhdm = null;
    private String spjdxh = null;

    @Override
    public void pageLoad() {
        splcbm = getRequestParameter("splcbm");
        splcbbh = getRequestParameter("splcbbh");
        xzqhdm = getRequestParameter("xzqhdm");
        spjdxh = getRequestParameter("spjdxh");
    }

    public DataGridModel<Spglsplcjdsxxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Spglsplcjdsxxxb>()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 5925727181973702200L;

                @Override
                public List<Spglsplcjdsxxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (dataBean.getSjsczt() != null) {
                        sql.eq("sjsczt", dataBean.getSjsczt().toString());
                    }
                    if (StringUtil.isNotBlank(splcbm)) {
                        sql.eq("splcbm", splcbm);
                    }
                    if (StringUtil.isNotBlank(splcbbh)) {
                        sql.eq("splcbbh", splcbbh);
                    }
                    if (StringUtil.isNotBlank(xzqhdm)) {
                        sql.eq("xzqhdm", xzqhdm);
                    }
                    if (StringUtil.isNotBlank(spjdxh)) {
                        sql.eq("spjdxh", spjdxh);
                    }
                    // 有效数据
                    sql.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                    // 去除草稿
                    sql.nq("IFNULL(sync,0)", "-1");
                    // 按照阶段序号升序
                    sql.setOrderDesc("spjdxh");
                    // 再按照同步时间排序
                    sql.setOrderDesc("operatedate");
                    PageData<Spglsplcjdsxxxb> pageData = iSpglsplcjdsxxxb.getAllByPage(sql.getMap(), first, pageSize,
                            sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    List<Spglsplcjdsxxxb> lists = pageData.getList();
                    if (ValidateUtil.isNotBlankCollection(lists)) {
                        for (Spglsplcjdsxxxb splcSpglsplcjdsxxxb : lists) {
                            // 本地校验失败
                            if (splcSpglsplcjdsxxxb.getSjsczt() == -1) {
                                splcSpglsplcjdsxxxb.put("sjscztText", "本地校验失败");
                            }
                            else {
                                splcSpglsplcjdsxxxb.put("sjscztText",
                                        codeItemsService.getItemTextByCodeName("国标_数据上传状态",
                                                splcSpglsplcjdsxxxb.getSjsczt().toString()));
                            }
                            // 阶段名称
                            int spjdxhInt = 0;
                            if (StringUtil.isBlank(spjdxh)) {
                                spjdxhInt = splcSpglsplcjdsxxxb.getSpjdxh();
                            }
                            else {
                                spjdxhInt = Integer.parseInt(spjdxh);
                            }
                            String phaseName = iSpglspjdxxb.getPhaseName(splcbm, Double.parseDouble(splcbbh), xzqhdm,
                                    spjdxhInt).getResult();
                            splcSpglsplcjdsxxxb.set("spjdmc", phaseName);
                            // 事项名称
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("xzqhdm", splcSpglsplcjdsxxxb.getXzqhdm());
                            sqlConditionUtil.eq("spsxbm", splcSpglsplcjdsxxxb.getSpsxbm());
                            sqlConditionUtil.eq("spsxbbh", String.valueOf(splcSpglsplcjdsxxxb.getSpsxbbh()));
                            List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlConditionUtil.getMap())
                                    .getResult();
                            if (EpointCollectionUtils.isNotEmpty(result)) {
                                splcSpglsplcjdsxxxb.set("spsxmc", result.get(0).getSpsxmc());
                            }
                        }
                    }
                    return lists;
                }
            };
        }
        return model;
    }

    public Spglsplcjdsxxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new Spglsplcjdsxxxb();
        }
        return dataBean;
    }

    public void setDataBean(Spglsplcjdsxxxb dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSbztModel() {
        if (sbztModel == null) {
            sbztModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
            sbztModel.add(new SelectItem("-1", "本地校验失败"));
        }
        return this.sbztModel;
    }
}
