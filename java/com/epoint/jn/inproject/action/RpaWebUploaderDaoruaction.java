package com.epoint.jn.inproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.rpagxbyxxb.api.IRpaGxbyxxbService;
import com.epoint.xmz.rpagxbyxxb.api.entity.RpaGxbyxxb;

@RestController("rpawebuploaderdaoruaction")
@Scope("request")
public class RpaWebUploaderDaoruaction extends BaseController
{

    private static final long serialVersionUID = 1L;
    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    
    private DataGridModel<RpaGxbyxxb> model = null;
    
    private Logger logger = Logger.getLogger(JNWebUploaderDaoruaction6.class);

    @Autowired
    private IRpaGxbyxxbService service;
    
    public void pageLoad() {
    }
    
    public DataImportModel9 getDataImportModel() {
        if (dataImportModel == null) {
            dataImportModel = new DataImportModel9(new ImportExcelHandler()
            {
                private static final long serialVersionUID = 1L;

                /**
                 * 此方法会将excel表格数据一行一行处理 每行数据都进入该方法，动态加载到list中
                 *
                 * @param filename
                 *            导入的excel名字
                 * @param sheetName
                 *            sheet名字
                 * @param sheet
                 *            第几个sheet
                 * @param curRow
                 *            第几行数据
                 * @param totalRows
                 *            总共多少行
                 * @param data
                 *            数据
                 * @return String 保存信息(如果失败,那么返回失败提示信息,否则返回null)
                 */
                @Override
                public String saveExcelData(String filename, String sheetName, int sheet, int curRow, int totalRows,
                        Object[] data) {
                    Map<String, Object> map = new HashMap<>();
                    String message = null;
                    try {
                    	EpointFrameDsManager.begin(null);
                    	RpaGxbyxxb gxbyxxb = new RpaGxbyxxb();
                    	gxbyxxb.setRowguid(UUID.randomUUID().toString());
                      
//                        Record total = service.getSizeInPorject();
//                    	
//                    	if(total != null) {
//                    		if("1".equals(total.getStr("status"))) {
//                    			return "已超过本日导入量，请明天再试!";
//                    		}
//                    	}
                    	
                    	 
                    	
                        // 默认导入sheet1的内容
                        if (sheet == 0) {
                            if (curRow == 0) {
                                // 默认excel的第一行为datagrid的列名
                                for (int i = 0; i < data.length; i++) {
                                    struList.add(data[i].toString());
                                }
                            }
                            if (curRow > 0) {
                                for (int i = 0; i < data.length; i++) {
                                    map.put(struList.get(i) + "", data[i].toString());
                                    String key = struList.get(i) + "";
                                    String value = data[i].toString();
                                    switch (key) {
                                        case "协议书编号":
                                        	gxbyxxb.setCertno(value);
                                            break;
                                        case "身份证号":
                                        	gxbyxxb.setCertnum(value);
                                            break;
                                        case "姓名":
                                        	gxbyxxb.setName(value);
                                            break;
                                        case "毕业年度":
                                        	gxbyxxb.setOveryear(value);
                                            break;
                                        case "学历":
                                        	gxbyxxb.setXl(value);
                                            break;
                                        case "专业":
                                        	gxbyxxb.setZy(value);
                                            break;
                                        case "毕业院校":
                                        	gxbyxxb.setByyx(value);
                                            break;
                                        case "单位名称":
                                        	gxbyxxb.setDwmc(value);
                                            break;
                                        case "单位性质":
                                        	gxbyxxb.setDwxz(value);
                                            break;
                                        case "单位隶属":
                                        	gxbyxxb.setDwls(value);
                                            break;
                                        case "单位注册所在地":
                                        	gxbyxxb.setDwzcszd(value);
                                            break;
                                        case "档案接收情况":
                                        	gxbyxxb.setDajjqk(value);
                                            break;
                                        case "档案接收单位":
                                        	gxbyxxb.setDajsdw(value);
                                            break;
                                        case "拟聘职位":
                                        	gxbyxxb.setNpdw(value);
                                        	break;
                                        case "签约日期":
                                        	if(StringUtil.isNotBlank(value)) {
                                        		Date qyrq = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                        		gxbyxxb.setQyrq(qyrq);
                                        	}
                                        	break;
                                        case "审核状态":
                                        	gxbyxxb.setShzt(value);
                                        	break;
                                        case "审核账号":
                                        	gxbyxxb.setShzh(value);
                                        	break;
                                        case "审核时间":
                                        	if(StringUtil.isNotBlank(value)) {
                                        		Date shsj = EpointDateUtil.convertString2Date(value, "yyyy-MM-dd");
                                        		gxbyxxb.setShsj(shsj);
                                        	}
                                        	break;
                                    }
                                }
                                
                                String sql = "select * from rpa_gxbyxxb where certnum = ?";
                                RpaGxbyxxb oldGxbyxxb = service.find(sql, gxbyxxb.getCertnum());
                                if (oldGxbyxxb == null) {
                                	service.insert(gxbyxxb);
                                }
                               
                            }
                        }
                        EpointFrameDsManager.commit();
                    } catch (Exception e) {
                    	EpointFrameDsManager.rollback();
                    	e.printStackTrace();
                        log.info("同步失败 =====" + e.getMessage());
                        return "导入失败！请联系管理员！";
                    }finally {
                        EpointFrameDsManager.close();
                    }
                    
                    return message;
                }

                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    getDataGridData();
                }
            });
        }
        return dataImportModel;
    }
   
    // 获得表格对象
    public DataGridModel<RpaGxbyxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RpaGxbyxxb>()
            {

                @Override
                public List<RpaGxbyxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<RpaGxbyxxb> list = service.findList(
                            ListGenerator.generateSql("rpa_gxbyxxb", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countRpaGxbyxxb(ListGenerator.generateSql("rpa_gxbyxxb", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    
    }
}
