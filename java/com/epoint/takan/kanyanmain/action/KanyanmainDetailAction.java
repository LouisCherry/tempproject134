package com.epoint.takan.kanyanmain.action;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 勘验主表详情页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RightRelation(KanyanmainListAction.class)
@RestController("kanyanmaindetailaction")
@Scope("request")
public class KanyanmainDetailAction  extends BaseController
{
	  @Autowired
      private IKanyanmainService service;

	@Autowired
	private IKanyanprojectService iKanyanprojectService;

	/**
	 * 表格控件model
	 */
	private DataGridModel<Kanyanproject> model;
    
    /**
     * 勘验主表实体对象
     */
  	private Kanyanmain dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
//		   SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
//		   sqlConditionUtil.eq("projctguid",projectguid);
	   		dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Kanyanmain();  
		  }
    }

	public DataGridModel<Kanyanproject> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<Kanyanproject>()
			{

				@Override
				public List<Kanyanproject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
					if(StringUtils.isNotBlank(dataBean.getRowguid())){
						conditionSql += " and kanyaguid=?";
						conditionList.add(dataBean.getRowguid());
					}
					List<Kanyanproject> list = iKanyanprojectService.findList(
							ListGenerator.generateSql("KANYANPROJECT", conditionSql, sortField, sortOrder), first, pageSize,
							conditionList.toArray());
					int count = iKanyanprojectService.countKanyanproject(ListGenerator.generateSql("KANYANPROJECT", conditionSql), conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}


	public Kanyanmain getDataBean()
	      {
	          return dataBean;
	      }
}