package com.epoint.auditsp.yqxm.action;

import com.epoint.auditsp.yqxm.api.ICrawlDataDY;
import com.epoint.auditsp.yqxm.api.entity.StSpglGcJdXxb;
import com.epoint.auditsp.yqxm.api.entity.StSpglXmSpsxblxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.dto.model.DataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 逾期项目后台
 *
 * @author yangyi
 * @version [版本号, 2022-05-30 14:58:35]
 */
@RestController("yuqiitemslistaction")
@Scope("request")
public class YuQiItemsListAction extends BaseController {

    @Autowired
    private ICrawlDataDY iCrawlDataDY;

    /**
     * 立项用地规划许可表格控件model
     */
    private DataGridModel<StSpglXmSpsxblxxb> firstModel;
    /**
     * 工程建设许可表格控件model
     */
    private DataGridModel<StSpglXmSpsxblxxb> secondModel;
    /**
     * 施工许可表格控件model
     */
    private DataGridModel<StSpglXmSpsxblxxb> thirdModel;
    /**
     * 竣工验收表格控件model
     */
    private DataGridModel<StSpglXmSpsxblxxb> fourthModel;
    /**
     * 并行推进表格控件model
     */
    private DataGridModel<StSpglXmSpsxblxxb> fifthModel;


    /**
     * 项目代码
     */
    private String xmdm;

    /**
     * 定义一个map存放各个阶段的数据
     */
    private Map<String, List<StSpglGcJdXxb>> map = new HashMap<>();

    @Override
    public void pageLoad() {
        xmdm = getRequestParameter("xmdm");
        //根据项目代码查询到ST_SPGL_GCJDXXB的数据
        List<StSpglGcJdXxb> list = iCrawlDataDY.getDataByXmdm(xmdm);
        if (ValidateUtil.isNotBlankCollection(list)) {
            map = list.stream().collect(Collectors.groupingBy(StSpglGcJdXxb::getSpjdmc));
        }
    }


    /**
     * 拿到第一阶段的数据
     *
     * @return
     */
    public DataGridModel<StSpglXmSpsxblxxb> getFirstDataGridData() {
        // 获得表格对象
        if (firstModel == null) {
            firstModel = new DataGridModel<StSpglXmSpsxblxxb>() {

                @Override
                public List<StSpglXmSpsxblxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<StSpglGcJdXxb> list = map.get("立项用地规划许可");
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        //拿到工程阶段信息表的rowguid 查询项目审批事项办理信息表
                        String JSRowguid = list.get(0).getRowguid();
                        Integer gcjdspys = list.get(0).getGcjdspys();
                        List<StSpglXmSpsxblxxb> resultList = iCrawlDataDY.getBlxxbListByRowguid(JSRowguid);
                        if (ValidateUtil.isNotBlankCollection(resultList)) {
                            for (StSpglXmSpsxblxxb stSpglXmSpsxblxxb : resultList) {
                                //防止阶段时限
                                stSpglXmSpsxblxxb.put("gcjdspys",gcjdspys);
                            }
                            this.setRowCount(resultList.size());
                            return resultList;
                        }
                    }
                    this.setRowCount(0);
                    return new ArrayList<>();
                }

            };
        }
        return firstModel;
    }

    /**
     * 拿到第二阶段的数据
     *
     * @return
     */
    public DataGridModel<StSpglXmSpsxblxxb> getSecondDataGridData() {
        // 获得表格对象
        if (secondModel == null) {
            secondModel = new DataGridModel<StSpglXmSpsxblxxb>() {

                @Override
                public List<StSpglXmSpsxblxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<StSpglGcJdXxb> list = map.get("工程建设许可");
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        //拿到工程阶段信息表的rowguid 查询项目审批事项办理信息表
                        String JSRowguid = list.get(0).getRowguid();
                        Integer gcjdspys = list.get(0).getGcjdspys();
                        List<StSpglXmSpsxblxxb> resultList = iCrawlDataDY.getBlxxbListByRowguid(JSRowguid);
                        if (ValidateUtil.isNotBlankCollection(resultList)) {
                            for (StSpglXmSpsxblxxb stSpglXmSpsxblxxb : resultList) {
                                //防止阶段时限
                                stSpglXmSpsxblxxb.put("gcjdspys",gcjdspys);
                            }
                            this.setRowCount(resultList.size());
                            return resultList;
                        }
                    }
                    this.setRowCount(0);
                    return new ArrayList<>();
                }

            };
        }
        return secondModel;
    }

    /**
     * 拿到第三阶段的数据
     *
     * @return
     */
    public DataGridModel<StSpglXmSpsxblxxb> getThirdDataGridData() {
        // 获得表格对象
        if (thirdModel == null) {
            thirdModel = new DataGridModel<StSpglXmSpsxblxxb>() {

                @Override
                public List<StSpglXmSpsxblxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<StSpglGcJdXxb> list = map.get("施工许可");
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        //拿到工程阶段信息表的rowguid 查询项目审批事项办理信息表
                        String JSRowguid = list.get(0).getRowguid();
                        Integer gcjdspys = list.get(0).getGcjdspys();
                        List<StSpglXmSpsxblxxb> resultList = iCrawlDataDY.getBlxxbListByRowguid(JSRowguid);
                        if (ValidateUtil.isNotBlankCollection(resultList)) {
                            for (StSpglXmSpsxblxxb stSpglXmSpsxblxxb : resultList) {
                                //防止阶段时限
                                stSpglXmSpsxblxxb.put("gcjdspys",gcjdspys);
                            }
                            this.setRowCount(resultList.size());
                            return resultList;
                        }
                    }
                    this.setRowCount(0);
                    return new ArrayList<>();
                }

            };
        }
        return thirdModel;
    }

    /**
     * 拿到第四阶段的数据
     *
     * @return
     */
    public DataGridModel<StSpglXmSpsxblxxb> getFourthDataGridData() {
        // 获得表格对象
        if (fourthModel == null) {
            fourthModel = new DataGridModel<StSpglXmSpsxblxxb>() {

                @Override
                public List<StSpglXmSpsxblxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<StSpglGcJdXxb> list = map.get("竣工验收");
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        //拿到工程阶段信息表的rowguid 查询项目审批事项办理信息表
                        String JSRowguid = list.get(0).getRowguid();
                        Integer gcjdspys = list.get(0).getGcjdspys();
                        String xmdm = list.get(0).getXmdm();
                        List<StSpglXmSpsxblxxb> resultList = iCrawlDataDY.getBlxxbListByRowguid(JSRowguid);
                        if (ValidateUtil.isNotBlankCollection(resultList)) {
                            for (StSpglXmSpsxblxxb stSpglXmSpsxblxxb : resultList) {
                                //防止阶段时限
                                stSpglXmSpsxblxxb.put("gcjdspys",gcjdspys);
                                stSpglXmSpsxblxxb.put("xmdm",xmdm);
                            }
                            this.setRowCount(resultList.size());
                            return resultList;
                        }
                    }
                    this.setRowCount(0);
                    return new ArrayList<>();
                }

            };
        }
        return fourthModel;
    }

    /**
     * 拿到第五阶段的数据
     *
     * @return
     */
    public DataGridModel<StSpglXmSpsxblxxb> getFifthDataGridData() {
        // 获得表格对象
        if (fifthModel == null) {
            fifthModel = new DataGridModel<StSpglXmSpsxblxxb>() {

                @Override
                public List<StSpglXmSpsxblxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<StSpglGcJdXxb> list = map.get("并行推进");
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        //拿到工程阶段信息表的rowguid 查询项目审批事项办理信息表
                        String JSRowguid = list.get(0).getRowguid();
                        Integer gcjdspys = list.get(0).getGcjdspys();
                        List<StSpglXmSpsxblxxb> resultList = iCrawlDataDY.getBlxxbListByRowguid(JSRowguid);
                        if (ValidateUtil.isNotBlankCollection(resultList)) {
                            for (StSpglXmSpsxblxxb stSpglXmSpsxblxxb : resultList) {
                                //防止阶段时限
                                stSpglXmSpsxblxxb.put("gcjdspys",gcjdspys);
                            }
                            this.setRowCount(resultList.size());
                            return resultList;
                        }
                    }
                    this.setRowCount(0);
                    return new ArrayList<>();
                }

            };
        }
        return fifthModel;
    }

}
