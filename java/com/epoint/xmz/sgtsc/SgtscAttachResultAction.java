package com.epoint.xmz.sgtsc;


import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.AttachUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.jnycsl.utils.HttpUtils;
import com.epoint.xmz.xmycslspinfo.api.IXmYcslSpinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController("sgtscattachresultaction")
@Scope("request")
public class SgtscAttachResultAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAttachService frameAttachInfoService;

    @Autowired
    private IXmYcslSpinfoService iXmYcslSpinfoService;


    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    private String itemguid = "";

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;


    @Override
    public void pageLoad() {
        itemguid = getRequestParameter("itemguid");
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
                    String sgtUrl = configService.getFrameConfigValue("Sgtsc_URL");
                    JSONObject params = new JSONObject();
                    JSONObject json = new JSONObject();
                    json.put("itemguid", itemguid);
                    params.put("params", json);
                    int count = 0;
                    try {
                        String result = HttpUtils.postHttp(sgtUrl, params.toJSONString());
                        JSONObject resultjson = JSONObject.parseObject(result);
                        String success = resultjson.getString("success");
                        if ("1".equals(success)) {
                            String filename = "合格证.pdf";
                            String itemname = resultjson.getString("itemname");
                            //base64字符串
                            String files = resultjson.getString("files");
                            String attachguid = "";
                            //加载过一次
                            List<FrameAttachInfo> attachInfos = frameAttachInfoService.getAttachInfoListByGuid(itemguid);
                            if (attachInfos != null && !attachInfos.isEmpty()) {
                                attachguid = attachInfos.get(0).getAttachGuid();
                            } else {
                                attachguid = UUID.randomUUID().toString();
                                ByteArrayInputStream stream = null;
                                BASE64Decoder decoder = new BASE64Decoder();
                                byte[] bytes1 = decoder.decodeBuffer(files);
                                stream = new ByteArrayInputStream(bytes1);
                                AttachUtil.saveFileInputStream(attachguid, itemguid, filename, ".pdf", "施工图审查", stream.available(), stream, "", "");
                            }
                            Record r = new Record();
                            r.set("filename", filename);
                            r.set("itemname", itemname);
                            r.set("attachGuid", attachguid);
                            list.add(r);
                            count = 1;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }
}
