package com.epoint.auditsp.yqxm.action;

import com.epoint.auditsp.yqxm.api.ICrawlDataDY;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmjbxxb;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 逾期率排名列表
 *
 * @author yangyi
 * @version [版本号, 2022-05-30 14:58:35]
 */
@RestController("yuqiratelistaction")
@Scope("request")
public class YuQiRateListAction extends BaseController {

    @Autowired
    private ICrawlDataDY iCrawlDataDY;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaArea> model;

    private DecimalFormat decimalFormat = new DecimalFormat("0.0%");

    public void pageLoad() {
    }


    public DataGridModel<AuditOrgaArea> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaArea>() {

                @Override
                public List<AuditOrgaArea> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //所有辖区的项目总数
                    Integer projectTotalCount = 0;
                    //所有辖区的逾期项目总数
                    Integer yuQiProjectTotalCount = 0;
                    //1. 查询每个区县st_spgl_xmjbxxb的逾期数据
                    List<StSpglXmjbxxb> list = iCrawlDataDY.getEachAreaData();
                    //查询总的数据
                    List<StSpglXmjbxxb> totalList = iCrawlDataDY.getTotalData();
                    //2.将查询到的list转换成map key为areacode
                    Map<String, StSpglXmjbxxb> map = new HashMap<>();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        list.removeIf(StSpglXmjbxxb -> StringUtil.isBlank(StSpglXmjbxxb.getAreacode()));
                        map = list.stream().collect(Collectors.toMap(StSpglXmjbxxb::getAreacode, Function.identity()));
                    }
                    if (ValidateUtil.isNotBlankCollection(totalList)) {
                        projectTotalCount = totalList.get(0).getInt("projectCount");
                        yuQiProjectTotalCount = totalList.get(0).getInt("yuQiProjectCount");
                    }
                    //3.循环查找audit_orga_area表
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.le("citylevel", "2");
                    PageData<AuditOrgaArea> pageData = iAuditOrgaArea.getAuditAreaPageData(sql.getMap(), first, pageSize, "", "").getResult();
                    if (ValidateUtil.isNotBlankCollection(pageData.getList())) {
                        List<AuditOrgaArea> areaList = pageData.getList();
                        for (AuditOrgaArea auditOrgaArea : areaList) {
                            String areaCode = auditOrgaArea.getXiaqucode();
                            //从map中获取项目总数和项目逾期数
                            Integer projectCount = map.get(areaCode) == null ? 0 : map.get(areaCode).getInt("projectCount");
                            Integer yuQiProjectCount = map.get(areaCode) == null ? 0 : map.get(areaCode).getInt("yuQiProjectCount");
                            String yuQiRate = "0.0%";
                            //计算逾期率
                            if (projectCount != null && projectCount != 0 && yuQiProjectCount != null) {
                                Double rate = yuQiProjectCount * 1.0 / projectCount;
                                yuQiRate = decimalFormat.format(rate);
                            }
                            auditOrgaArea.put("projectCount", projectCount);
                            auditOrgaArea.put("yuQiProjectCount", yuQiProjectCount);
                            auditOrgaArea.put("yuQiRate", yuQiRate);
                        }
                        //按逾期率倒叙排列
                        Collections.sort(areaList, new Comparator<Record>() {
                            @Override
                            public int compare(Record o1, Record o2) {
                                int compare = 0;

                                try {
                                    compare = o2.getStr("yuQiRate").compareTo(o1.getStr("yuQiRate"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return compare;
                            }
                        });
                        //在最后一行加入合计
                        AuditOrgaArea orgaArea = new AuditOrgaArea();
                        orgaArea.put("XiaQuName", "合计");
                        orgaArea.put("projectCount", projectTotalCount == null ? 0 : projectTotalCount);
                        orgaArea.put("yuQiProjectCount", yuQiProjectTotalCount == null ? 0 : yuQiProjectTotalCount);
                        //计算总逾期率
                        if (projectTotalCount != null && projectTotalCount != 0 && yuQiProjectTotalCount != null) {
                            Double rate = yuQiProjectTotalCount * 1.0 / projectTotalCount;
                            orgaArea.put("yuQiRate", decimalFormat.format(rate));
                        }
                        else{
                            orgaArea.put("yuQiRate", "0.0%");
                        }
                        //在最后一行加入总的数据
                        areaList.add(areaList.size(), orgaArea);
                        this.setRowCount(pageData.getRowCount());
                        return areaList;
                    } else {
                        this.setRowCount(0);
                        return new ArrayList<>();
                    }

                }

            };
        }
        return model;
    }


}
