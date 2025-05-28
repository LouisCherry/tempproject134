package com.epoint.statistics.action;

import com.epoint.basic.controller.BaseController;

import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.statistics.api.CqAuditProject;
import com.epoint.statistics.api.IOverdueAuditService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName OverdueAuditAction.java
 * @Description 统计超期办件action
 * @createTime 2022年01月05日 16:08:00
 */
@RestController("overdueauditaction")
@Scope("request")
public class OverdueAuditAction extends BaseController {


    /**
     * 组织机构的api
     */
    @Autowired
    private IOuServiceInternal ouService;
    /**
     * 查询办件的api
     */
    @Autowired
    private IOverdueAuditService iOverdueAuditService;

    /**
     * 懒加载树
     */
    private LazyTreeModal9 treeModel;

    /**
     * 办件来源下拉
     */
    private List<SelectItem> bjlyApplyWay = null;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    /**
     * 办件状态
     */
    private List<SelectItem> bjStatusWay = null;


    /**
     * 实体信息
     */
    private CqAuditProject dataBean;

    /**
     * 部门名称
     */
    private String ouName;
    /**
     * 部门简称
     */
    private String ouShortName;

    /**
     * 左侧选择节点
     */
    private String leftTreeNodeGuid;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    /**
     * 是否直属
     */
    private Boolean isDirect;

    /**
     * 辖区
     */
    private static final String AREA_CODE = "areacode";

    /**
     * 申请时间
     */
    private static final String APPLY_DATE = "applydate";
    /**
     * 办结时间
     */
    private static final String BANJIE_DATE = "banjiedate";

    /**
     * 申请来源
     */
    private static final String APPLY_WAY = "applyway";

    /**
     * 办件状态
     */
    private static final String STATUS = "status";

    /**
     * 超期
     */
    private static final String CQ = "cq";
    /**
     * 未办结
     */
    private static final String WBJ = "wbj";
    /**
     * 办结
     */
    private static final String YBJ = "ybj";
    /**
     * 办结率
     */
    private static final String BJL = "bjl";

    /**
     * @Author: yuchenglin
     * @Description: get方法
     * @Date: 2022/1/6 19:04
     * @return: java.lang.String
     **/
    public String getOuName() {
        return ouName;
    }

    /**
     * @param ouName: ouname
     * @Author: yuchenglin
     * @Description: set方法
     * @Date: 2022/1/6 19:04
     * @return: void
     **/
    public void setOuName(String ouName) {
        this.ouName = ouName;
    }

    /**
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:04
     * @return: java.lang.String
     **/
    public String getOuShortName() {
        return ouShortName;
    }

    /**
     * @param ouShortName: ouShortName
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:04
     * @return: void
     **/
    public void setOuShortName(String ouShortName) {
        this.ouShortName = ouShortName;
    }

    /**
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:04
     * @return: java.lang.Boolean
     **/
    public Boolean getIsDirect() {
        if (isDirect == null) {
            isDirect = false;
        }
        return isDirect;
    }


    /**
     * @Author: yuchenglin
     * @Description: get方法
     * @Date: 2022/1/6 12:59
     * @return: java.lang.String
     **/
    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    /**
     * @param leftTreeNodeGuid:
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 13:00
     * @return: void
     **/
    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    /**
     * @Author: yuchenglin
     * @Description:get方法
     * @Date: 2022/1/6 19:05
     * @return: com.epoint.statistics.api.CqAuditProject
     **/
    public CqAuditProject getDataBean() {
        return dataBean;
    }

    /**
     * @param dataBean: dataBean
     * @Author: yuchenglin
     * @Description:set方法
     * @Date: 2022/1/6 19:05
     * @return: void
     **/
    public void setDataBean(CqAuditProject dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 是否以独立部门模式进入
     */
    private String isSub;

    /**
     * @Author: yuchenglin
     * @Description:页面加载得初始化方法
     * @Date: 2022/1/5 16:29
     * @return: void
     **/
    @Override
    public void pageLoad() {
        if (dataBean == null) {
            dataBean = new CqAuditProject();
        }
    }

    /**
     * @Author: yuchenglin
     * @Description: 构建树
     * @Date: 2022/1/6 12:41
     * @return: com.epoint.basic.faces.tree.LazyTreeModal9
     **/
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            int tableName = ConstValue9.FRAMEOU;
            if ("1".equals(isSub)) {
                tableName = ConstValue9.SUB_DEPARTMENT;
            }
            EpointTreeHandler9 epointTreeHandler9 = new EpointTreeHandler9(tableName);
            FrameOu ouByUserGuid = ouService.getOuByUserGuid(userSession.getUserGuid());
            treeModel = new LazyTreeModal9(epointTreeHandler9);
            if (!userSession.isAdmin()) {
                treeModel.setRootCode(ouByUserGuid.getOuguid());
                treeModel.setRootName(ouByUserGuid.getOuname());
            }
            else {
                treeModel.setRootName("所有部门");
            }
        }
        return treeModel;
    }

    /**
     * @Author: yuchenglin
     * @Description: 获取申请方式的下拉
     * @Date: 2022/1/6 12:55
     * @return: java.util.List<com.epoint.core.dto.model.SelectItem>
     **/
    public List<SelectItem> getApplyway() {
        if (bjlyApplyWay == null) {
            bjlyApplyWay = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请方式", null, false));
        }
        return this.bjlyApplyWay;
    }

    /**
     * @Author: yuchenglin
     * @Description: 获取办件状态的下拉
     * @Date: 2022/1/6 12:56
     * @return: java.util.List<com.epoint.core.dto.model.SelectItem>
     **/
    public List<SelectItem> getStatus() {
        if (bjStatusWay == null) {
            bjStatusWay = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, false));
        }
        return this.bjStatusWay;
    }

    /**
     * @Author: yuchenglin
     * @Description: 列表查询
     * @Date: 2022/1/6 13:01
     * @return: com.epoint.core.dto.model.DataGridModel<com.epoint.basic.auditproject.auditproject.domain.AuditProject>
     **/
    public DataGridModel<Record> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<Record>() {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //首次加载，显示当前登录用户所在的部门的统计数据
                    FrameOu frameOu = null;
                    if (StringUtil.isBlank(leftTreeNodeGuid)) {
                        frameOu = ouService.getOuByUserGuid(userSession.getUserGuid());
                    }
                    else {
                        //点击树节点出来的数据
                        frameOu = ouService.getOuByOuGuid(leftTreeNodeGuid);
                    }
                    FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(frameOu.getOuguid());
                    //获取下一层包含自己所以size是>=1
                    List<FrameOu> frameOus = ouService.listOUByGuid(frameOu.getOuguid(), 1);
                    //size==1可以确定是叶子节点
                    List<Record> listByOuguid = null;
                    if (frameOus.size() == 1) {
                        listByOuguid = iOverdueAuditService.getListByAOuguid(frameOuExtendInfo.getOuGuid(), getSearchSql(dataBean));
                    }
                    else {
                        List<String> chridenOuguid = getChridenOuguid(frameOu);
                        listByOuguid= iOverdueAuditService.getListByOuguids(chridenOuguid,getSearchSql(dataBean));
                       // listByOuguid = iOverdueAuditService.getListByAreacode(frameOuExtendInfo.get(AREA_CODE), getSearchSql(dataBean));
                    }
                    for (Record record : listByOuguid) {
                        record.set("ouguid", frameOu.getOuguid());
                        record.set("ouname", frameOu.getOuname());
                        if (Integer.valueOf(record.getStr(CQ)) == 0) {
                            record.set(BJL, record.get(YBJ));
                        }
                        else {
                            DecimalFormat df = new DecimalFormat("0.00");
                            int i = Integer.valueOf(record.getStr(YBJ)) / Integer.valueOf(record.getStr(CQ));
                            record.set(BJL, df.format(i));
                        }
                    }
                    this.setRowCount(listByOuguid.size());
                    return listByOuguid;
                }
            };
        }
        return model;
    }


    /***
      * @Author: yuchenglin
      * @Description:获取当前层的下级层
      * @Date: 2022/3/7 11:46
      * @param frameOu:
      * @return: java.util.List<java.lang.String>
      **/
    private List<String> getChridenOuguid(FrameOu frameOu) {
        //获取下面所有层包含自己
        List<FrameOu> frameOus = ouService.listOUByGuid(frameOu.getOuguid(), 4);
        return frameOus.stream().map(FrameOu::getOuguid).collect(Collectors.toList());
    }

    /***
     * @Author: yuchenglin
     * @Description:拼接查询语句的sql
     * @Date: 2022/1/6 19:19
     * @param cqAuditProject:dataBean
     * @return: java.lang.String
     **/
    private String getSearchSql(CqAuditProject cqAuditProject) {
        String sql = "";
        if (StringUtil.isNotBlank(cqAuditProject.getStr("promiseenddateStart"))) {
            String dtApplydate = EpointDateUtil.convertDate2String(
                    EpointDateUtil.convertString2DateAuto(cqAuditProject.getStr("promiseenddateStart")));
            sql += " and promiseenddate >= '" + dtApplydate + "'";
        }
        if (StringUtil.isNotBlank(cqAuditProject.get("promiseenddateEnd"))) {
            String dtApplydate = EpointDateUtil.convertDate2String(
                    EpointDateUtil.convertString2DateAuto(cqAuditProject.getStr("promiseenddateEnd")));
            sql += " and promiseenddate <= '" + dtApplydate + "'";
        }
        if (cqAuditProject.get(APPLY_WAY) != null) {
            sql += " and applyway = '" + cqAuditProject.get(APPLY_WAY) + "'";
        }
        if (cqAuditProject.get(STATUS) != null) {
            sql += " and  status = '" + cqAuditProject.get(STATUS) + "'";
        }
        return sql;
    }

    /**
     * @Author: yuchenglin
     * @Description:导出方法
     * @Date: 2022/1/7 14:02
     * @return: com.epoint.basic.faces.export.ExportModel
     **/
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("ouname,cq,wbj,ybj,bjl",
                    "部门名称,超期办件量数量,未办结数量,已办结数量,办结率");
        }
        return exportModel;
    }
}
