package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZrztxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZrztxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 项目单位信息表修改页面对应的后台
 *
 * @author 95453
 * @version [版本号, 2019-06-20 14:31:29]
 */
@RestController("registerspglxmdwxxbeditv3action")
@Scope("request")
public class RegisterSpglXmdwxxbEditV3Action extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private ISpglZrztxxbV3Service service;
    @Autowired
    private ICodeItemsService codeItemsService;

    // 下拉框组件Model
    private List<SelectItem> dwlxModel;

    /**
     * 实体对象
     */
    private SpglZrztxxbV3 dataBean;

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
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        // 如果存在建设单位，则不允许其他单位选择建设单位
        if (ZwfwConstant.CONSTANT_INT_ONE == dataBean.getDwlx()) {
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("gcdm", dataBean.getGcdm());
            sUtil.eq("xzqhdm", dataBean.getXzqhdm());
            sUtil.eq("dwlx", ZwfwConstant.CONSTANT_STR_ONE);
            sUtil.nq("rowguid", dataBean.getRowguid());
            List<SpglZrztxxbV3> list = service.getListByCondition(sUtil.getMap()).getResult();
            if (ValidateUtil.isNotBlankCollection(list)) {
                SpglZrztxxbV3 oldDatabean = service.find(dataBean.getRowguid());
                dataBean.setDwlx(oldDatabean.getDwlx());
                addCallbackParam("msg", "建设单位有且仅有一个，单位类型修改失败！");
                addCallbackParam("flag", "false");
                service.update(dataBean);
                return;
            }
        }
        service.update(dataBean);
        addCallbackParam("msg", "修改成功!");
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
