package com.epoint.takan.kanyanmain.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 勘验主表详情页面对应的后台
 *
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RightRelation(KanyanmainListAction.class)
@RestController("kanyanmainurldetailaction")
@Scope("request")
public class KanyanmainURLDetailAction extends BaseController {
    @Autowired
    private IKanyanmainService service;

    @Autowired
    private IKanyanprojectService iKanyanprojectService;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;


    public void pageLoad() {
    }

//	public DataGridModel<Kanyanproject> getDataGridData() {
		// 获得表格对象
//		if (model == null) {
//			model = new DataGridModel<Kanyanproject>()
//			{
//
//				@Override
//				public List<Kanyanproject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
//					List<Object> conditionList = new ArrayList<Object>();
//					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
//					if(StringUtils.isNotBlank(dataBean.getRowguid())){
//						conditionSql += " and kanyaguid=?";
//						conditionList.add(dataBean.getRowguid());
//					}
//					List<Kanyanproject> list = iKanyanprojectService.findList(
//							ListGenerator.generateSql("KANYANPROJECT", conditionSql, sortField, sortOrder), first, pageSize,
//							conditionList.toArray());
//					int count = iKanyanprojectService.countKanyanproject(ListGenerator.generateSql("KANYANPROJECT", conditionSql), conditionList.toArray());
//					this.setRowCount(count);
//					return list;
//				}
//
//			};
//		}
//		return model;
//	}

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    //type = 1.Kanyanmain 里的 rowguid；2.Kanyanproject的rowguid
                    String guid = getRequestParameter("guid");
                    //1.Kanyanmain 里的 Videourls；2.Kanyanproject的 urls；3.Kanyanproject的 Videourls
                    String type = getRequestParameter("type");
                    List<Record>  urls = new ArrayList<>();
                    if (StringUtil.isNotBlank(type) && StringUtils.isNotBlank(guid)) {
                        switch (type) {
                            case "1":
                                Kanyanmain kanyanmain = service.find(guid);
                                String videos = kanyanmain.getVideourls();
                                if (StringUtils.isNotBlank(videos)) {
                                    String[] aurls = videos.split(";");
                                    for (String url : aurls) {
                                        Record record = new Record();
                                        record.set("url", url);
                                        urls.add(record);
                                    }
                                }
                                break;
                            case "2":
                                Kanyanproject kanyanproject = iKanyanprojectService.find(guid);
                                String purls = kanyanproject.getUrls();
                                if (StringUtils.isNotBlank(purls)) {
                                    String[]  aurls = purls.split(";");
                                    for (String url : aurls) {
                                        Record record = new Record();
                                        record.set("url", url);
                                        urls.add(record);
                                    }
                                }
                                break;
                            case "3":
                                Kanyanproject kanyanproject1 = iKanyanprojectService.find(guid);
                                String vurls = kanyanproject1.getVideourls();
                                if (StringUtils.isNotBlank(vurls)) {
                                    String[]  aurls = vurls.split(";");
                                    for (String url : aurls) {
                                        Record record = new Record();
                                        record.set("url", url);
                                        urls.add(record);
                                    }
                                }
                                break;
                        }

                    }
                    this.setRowCount(urls.size());
                    return urls;
                }

            };
        }
        return model;
    }
}