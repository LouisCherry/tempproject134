package com.epoint.yjs.yjszn.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.yjs.yjszn.api.IYjsZnService;


/**
 * 一件事指南配置list页面对应的后台
 *
 * @author panshunxing
 * @version [版本号, 2024-10-08 19:07:22]
 */
@RestController("yjsznlistaction")
@Scope("request")
public class YjsZnListAction extends BaseController {
    @Autowired
    private IYjsZnService service;

    /**
     * 一件事指南配置实体对象
     */
    private YjsZn dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<YjsZn> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 类型单选按钮组model
     */
    private List<SelectItem> typeModel = null;
    /**
     * 辖区下拉列表model
     */
    private List<SelectItem> areacodeModel = null;


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
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<YjsZn> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<YjsZn>() {

                @Override
                public List<YjsZn> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    if(StringUtils.isNotBlank(dataBean.getType())){
                        conditionSql+=" and type=?";
                        conditionList.add(dataBean.getType());
                    }
                    if(StringUtils.isNotBlank(dataBean.getAreacode())){
                        conditionSql+=" and areacode=?";
                        conditionList.add(dataBean.getAreacode());
                    }
                    if(StringUtils.isNotBlank(dataBean.getTitle())){
                        conditionSql+=" and title like ?";
                        conditionList.add("%"+dataBean.getTitle()+"%");
                    }
                    //每个县区只能允许上传及看到他们自己县区的内容，模块要做县区限制，识别当前登陆人所属县区，然后进行判断展示。
                    //另外，市里的账号能看到所有内容
                    if(!"370800".equals(ZwfwUserSession.getInstance().getAreaCode())){
                        conditionSql+=" and areacode=?";
                        conditionList.add(ZwfwUserSession.getInstance().getAreaCode());
                    }
                    List<YjsZn> list = service.findList(
                            ListGenerator.generateSql("YJS_ZN", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countYjsZn(ListGenerator.generateSql("YJS_ZN", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public YjsZn getDataBean() {
        if (dataBean == null) {
            dataBean = new YjsZn();
        }
        return dataBean;
    }

    public void setDataBean(YjsZn dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "一件事指南类型", null, true));
        }
        return this.typeModel;
    }

    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
            areacodeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, true));
        }
        return this.areacodeModel;
    }

}
