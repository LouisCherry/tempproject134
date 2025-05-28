package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.basic.spgl.inter.ISpglCommon;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxjbxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglSpsxkzxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.Spglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglSpsxkzxxbService;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglspsxjbxxb;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;



@RestController("spglsbsjlckzsxctv3action")
@Scope("request")
public class SpglSbsjlckzsxctV3Action extends BaseController
{

    private static final long serialVersionUID = 8796953926146877276L;

    transient Logger log = LogUtil.getLog(SpglSbsjlckzsxctV3Action.class);

    /**
     * 流程信息实体对象
     */
    private SpglSpsxkzxxb dataBean;

    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;

    @Autowired
    private ISpglSpsxkzxxbService iSpglSpsxkzxxbService;

    @Autowired
    private ISpglCommon ispglcommon;

    @Autowired
    private ISpglspsxjbxxb iSpglspsxjbxxb;

    private List<SelectItem> sbztModel = null;
    private List<SelectItem> sfsxgzcnzModel = null;
    private List<SelectItem> sjyxbsModel = null;
    private List<SelectItem> bjlxModel = null;
    private List<SelectItem> sqdxModel = null;
    private List<SelectItem> sflcbsxModel = null;
    private List<SelectItem> bljgsdfsModel = null;

    private String rowguid;

    @Override
    public void pageLoad() {
        rowguid = getRequestParameter("rowguid");

        Spglsplcjdsxxxb find = iSpglsplcjdsxxxb.find(rowguid);
        if (find != null) {
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("xzqhdm", find.getXzqhdm());
            sqlConditionUtil.eq("spsxbm", find.getSpsxbm());
            sqlConditionUtil.eq("spsxbbh", String.valueOf(find.getSpsxbbh()));
            // 有效数据
            sqlConditionUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            // 去除草稿
            sqlConditionUtil.nq("IFNULL(sync,0)", "-1");
            List<SpglSpsxkzxxb> result = iSpglSpsxkzxxbService.getListByCondition(sqlConditionUtil.getMap())
                    .getResult();
            if (EpointCollectionUtils.isNotEmpty(result)) {
                dataBean = result.get(0);
            }
        }
        if (dataBean != null && find!= null) {
            addCallbackParam("sjsczt", dataBean.getSjsczt().toString());
            addCallbackParam("sbyy", StringUtil.isNotBlank(dataBean.getSbyy()) ? dataBean.getSbyy() : "无");
            addCallbackParam("sync", dataBean.getStr("sync"));
            // 根据国标_事项类型控制行政许可信息显隐
            // 查询基本事项信息表
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("xzqhdm", find.getXzqhdm());
            sqlConditionUtil.eq("spsxbm", find.getSpsxbm());
            sqlConditionUtil.eq("spsxbbh", String.valueOf(find.getSpsxbbh()));
            // 有效数据
            sqlConditionUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
            // 去除草稿
            sqlConditionUtil.nq("IFNULL(sync,0)", "-1");
            List<SpglSpsxjbxxb> result = iSpglspsxjbxxb.getListByCondition(sqlConditionUtil.getMap()).getResult();
            if (EpointCollectionUtils.isNotEmpty(result)) {
                if (("01").equals(result.get(0).getStr("SXLX"))) {
                    addCallbackParam("isTrue", "isTrue");
                }

                if ("1".equals(result.get(0).getStr("SFSF"))) {
                    addCallbackParam("isSf", "isSf");
                }
            }

        }
    }

    // 重新上报
    public void reSyncBusiness() {
        // 事务控制
        String msg = "上报成功！";
        try {
            EpointFrameDsManager.begin(null);

            SpglSpsxkzxxb oldDataBean = new SpglSpsxkzxxb();
            Spglsplcjdsxxxb find = iSpglsplcjdsxxxb.find(rowguid);
            if (find != null) {
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("xzqhdm", find.getXzqhdm());
                sqlConditionUtil.eq("spsxbm", find.getSpsxbm());
                sqlConditionUtil.eq("spsxbbh", String.valueOf(find.getSpsxbbh()));
                // 有效数据
                sqlConditionUtil.eq("sjyxbs", ZwfwConstant.CONSTANT_STR_ONE);
                // 去除草稿
                sqlConditionUtil.nq("IFNULL(sync,0)", "-1");
                List<SpglSpsxkzxxb> result = iSpglSpsxkzxxbService.getListByCondition(sqlConditionUtil.getMap())
                        .getResult();
                if (EpointCollectionUtils.isNotEmpty(result)) {
                    oldDataBean = result.get(0);
                }
            }
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

    public SpglSpsxkzxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglSpsxkzxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglSpsxkzxxb dataBean) {
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

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSfsxgzcnzModel() {
        if (sfsxgzcnzModel == null) {
            sfsxgzcnzModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_是否实行告知承诺制", null,
                            false));
        }
        return this.sfsxgzcnzModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSjyxbsModel() {
        if (sjyxbsModel == null) {
            sjyxbsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.sjyxbsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBjlxModel() {
        if (bjlxModel == null) {
            bjlxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_办件类型", null, false));
        }
        return this.bjlxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSqdxModel() {
        if (sqdxModel == null) {
            sqdxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_申请对象类型", null, false));
        }
        return this.sqdxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSflcbsxModel() {
        if (sflcbsxModel == null) {
            sflcbsxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_是否里程碑事项", null,
                            false));
        }
        return this.sflcbsxModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBljgsdfsModel() {
        if (bljgsdfsModel == null) {
            bljgsdfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_办理结果送达方式", null,
                            false));
        }
        return this.bljgsdfsModel;
    }
}
