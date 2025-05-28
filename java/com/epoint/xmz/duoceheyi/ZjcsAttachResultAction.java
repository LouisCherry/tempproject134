package com.epoint.xmz.duoceheyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("zjcsattachresultaction")
@Scope("request")
public class ZjcsAttachResultAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    private String itemcode = "";


    @Override
    public void pageLoad() {
        itemcode = getRequestParameter("itemcode");
    }


    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 8339801819493001600L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = new ArrayList<Record>();
                    String itemcode = getRequestParameter("itemcode");
                    String apiUrl = configService.getFrameConfigValue("AS_DUOCEHEYI_API_URL");
                    DuoceheyiApiUtil util = new DuoceheyiApiUtil();
                    String token = util.getToken(apiUrl);
                    if (StringUtil.isBlank(token)) {
                        this.setRowCount(list.size());
                        return list;
                    }
                    String result = util.getResultsSharing(token, apiUrl, itemcode, first / pageSize + 1, pageSize);
                    if (StringUtil.isBlank(result)) {
                        log.info("多测合一接口返回结果：" + result);
                        this.setRowCount(list.size());
                        return list;
                    }
                    JSONObject json = JSON.parseObject(result);
                    String ob1 = json.getString("DATA");
                    if ("[]".equals(ob1) || StringUtil.isNotBlank(json.getString("DESC"))) {
                        log.info(result);
                        this.setRowCount(list.size());
                        return list;
                    }
                    JSONObject obj = (JSONObject) json.get("DATA");
                    JSONArray rowsList = obj.getJSONArray("rowsList");
                    for (Object object : rowsList) {
                        JSONObject rowsJson = (JSONObject) object;
                        Record r = new Record();
                        r.set("chjg", rowsJson.get("CHJG"));

                        JSONArray fileList = rowsJson.getJSONArray("FILE");
                        String file = "";
                        for (Object fileObject : fileList) {
                            JSONObject fileJson = (JSONObject) fileObject;
                            file += "</br>" + fileJson.get("FILENAME");
                        }
                        file = StringUtil.isBlank(file) ? "" : file.substring(5);
                        r.set("file", file);
                        r.set("fileList", fileList);
                        r.set("downUrl", apiUrl + "external/public/getSharedFile?token=" + token + "&fileRid=");
                        r.set("hjsj", rowsJson.get("HJSJ"));
                        list.add(r);
                    }
                    this.setRowCount(obj.getIntValue("total"));
                    return list;
                }

            };
        }
        return model;
    }


}
