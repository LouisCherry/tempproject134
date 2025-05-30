package com.epoint.fmgl.tzxmmuqd.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.dataimport.DataImportModel9;
import com.epoint.basic.faces.dataimport.ImportExcelHandler;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 投资项目目录清单list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-26 12:07:56]
 */
@RestController("tzxmmuqdlistaction2")
@Scope("request")
public class TzxmmuqdListAction2  extends BaseController
{
	@Autowired
    private ITzxmmuqdService service;
	@Autowired
    private ICodeItemsService codeItemsService;
    
    /**
     * 投资项目目录清单实体对象
     */
    private Tzxmmuqd dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Tzxmmuqd> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    private DataImportModel9 dataImportModel;
    private List<String> struList = new ArrayList<String>();
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    String xzqhdm =null;
    String projecttype =null;
    

    public void pageLoad()
    {
        xzqhdm = getRequestParameter("xzqhdm");
        projecttype = getRequestParameter("projecttype");
    }

	

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<Tzxmmuqd> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Tzxmmuqd>()
            {

                @Override
                public List<Tzxmmuqd> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Tzxmmuqd> list=null;
                    int count=0;
                    if(StringUtil.isNotBlank(xzqhdm)&&StringUtil.isNotBlank(projecttype)) {
                        conditionSql+=" and xzqhbm='"+xzqhdm+"000000' and mllx='"+projecttype+"' order by mlhyfl";
                        list = service.findList(
                                ListGenerator.generateSql("tzxmmuqd", conditionSql, sortField, sortOrder), first, pageSize,
                                conditionList.toArray());
                        for (Tzxmmuqd tzxmmuqd : list) {
                            String mlhyfl = tzxmmuqd.getMlhyfl();
                            String mlhyflmc = codeItemsService.getItemTextByCodeName("省发改_投资项目行业分类", mlhyfl);
                            tzxmmuqd.put("mlhyflmc", mlhyflmc);
                        }
                        count = service.countTzxmmuqd(ListGenerator.generateSql("tzxmmuqd", conditionSql), conditionList.toArray());
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public Tzxmmuqd getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Tzxmmuqd();
    	}
        return dataBean;
    }

    public void setDataBean(Tzxmmuqd dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
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
                            }
                            list.add(map);
                        }
                    }
                    return message;
                }
                /**
                 * saveExcelData把数据都加入list后，这个方法动态加载第一行的数据作为grid表头
                 */
                @Override
                public void refreshTable() {
                    //存放未导入的企业
                    List<String> failed=new ArrayList<>();
                    for (Map<String, Object> datamap : list) {
                            //判断是否存在次企业信息
                        Tzxmmuqd tzxmmuqd=new Tzxmmuqd();
                        tzxmmuqd.setRowguid(UUID.randomUUID().toString());
                        tzxmmuqd.setOperatedate(new Date());
                        String mlbm = (String) datamap.get(struList.get(0));
                        String xzqhbm = (String) datamap.get(struList.get(1));
                        String xzqhmc = (String) datamap.get(struList.get(2));
                        String mlhyfl = (String) datamap.get(struList.get(3));
                        String mlmc = (String) datamap.get(struList.get(4));
                        String bmbm = (String) datamap.get(struList.get(5));
                        String bmmc = (String) datamap.get(struList.get(6));
                        String mllx = (String) datamap.get(struList.get(7));
                        tzxmmuqd.setMlbm(mlbm);
                        tzxmmuqd.setXzqhbm(xzqhbm);
                        tzxmmuqd.setXzqhmc(xzqhmc);
                        tzxmmuqd.setMlhyfl(mlhyfl);
                        tzxmmuqd.setMlmc(mlmc);
                        tzxmmuqd.setBmbm(bmbm);
                        tzxmmuqd.setBmmc(bmmc);
                        tzxmmuqd.setMllx(mllx);
                        service.insert(tzxmmuqd);
                    }
                }
            });
        }
        return dataImportModel;
    }
    
    public void gettzxmmlqd(String id) {
        Tzxmmuqd tzxmmuqd = service.find(id);
        if(StringUtil.isNotBlank(tzxmmuqd)) {
            String mlbm = tzxmmuqd.getMlbm();
            String mlhyfl = tzxmmuqd.getMlhyfl();
            String mlmc = tzxmmuqd.getMlmc();
            String mlhyflmc = codeItemsService.getItemTextByCodeName("省发改_投资项目行业分类", mlhyfl);
            addCallbackParam("mlbm", mlbm);
            addCallbackParam("mlhyfl", mlhyfl);
            addCallbackParam("mlmc", mlmc);
            addCallbackParam("mlhyflmc", mlhyflmc);
        }
       
       
    }

}
