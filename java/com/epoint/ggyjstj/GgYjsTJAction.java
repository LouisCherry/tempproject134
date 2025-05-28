package com.epoint.ggyjstj;

import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.FormatUtil;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.ggyjstj.api.IgetSpDataByAreacode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("ggyjstjaction")
@Scope("request")
public class GgYjsTJAction extends BaseController {
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IgetSpDataByAreacode igetSpDataByAreacode;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    @Override
    public void pageLoad() {

    }
    /**
     * 获得证照基本信息列表
     *
     * @return
     */
    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 9179330200184695904L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list=new ArrayList<>();
                    //14个区县
                    List<CodeItems> codeItems = codeItemsService.listCodeItemsByCodeName("所属区县");
                    if (EpointCollectionUtils.isNotEmpty(codeItems)){
                        for (CodeItems co:codeItems) {
                            Record record=new Record();
                            record.set("XiaQuName",co.getItemText());
                            record.set("areacode",co.getItemValue());
                            Record data = igetSpDataByAreacode.getSpDataByAreacode(co.getItemValue());
                           if (data==null){
                               record.set("sb","0");
                               record.set("bj","0");
                           }
                           else {
                               record.set("sb",StringUtil.isBlank(data.getStr("sb"))?"0":data.getStr("sb"));
                               record.set("bj",StringUtil.isBlank(data.getStr("bj"))?"0":data.getStr("bj"));
                           }
                           list.add(record);
                        }
                    }
                    this.setRowCount(15);
                    return list;
                }
            };
        }
        return model;
    }

}
