package com.epoint.xmz.jnyjsevaluate.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;

/**
 * @Author Yally
 * @Description
 * @Date 2022/1/6 16:16
 * @Version 1.0
 **/
@RestController("jnyjsevaluatestataction")
@Scope("request")
public class JNYjsEvaluateStatAction extends BaseController {
    private static final long serialVersionUID = 1L;

    JnYjsEvaluate evaluate = new JnYjsEvaluate();
    
    
    @Autowired
    private IJnYjsEvaluateService iJnYjsEvaluateService;

    /**
     * 申请时间
     */
    private String applydateStart;
    private String applydateEnd;


    private DataGridModel<Record> modelall;


    private ExportModel exportModel;



    @Override
    public void pageLoad() {
        if (StringUtil.isBlank(applydateStart)) {
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            applydateStart = EpointDateUtil.convertDate2String(cale.getTime(), "YYYY-MM-dd HH:mm:ss");
        }
        if (StringUtil.isBlank(applydateEnd)) {
            applydateEnd = EpointDateUtil.convertDate2String(new Date(), "YYYY-MM-dd HH:mm:ss");
        }
    }

    public DataGridModel<Record> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<Record>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> endlist = new ArrayList<>();
                    int listNumCount = 0;
                    
                    endlist = iJnYjsEvaluateService.findEvaluateList(first, pageSize,applydateStart,applydateEnd);
                    
                    listNumCount = iJnYjsEvaluateService.countEvaluate(applydateStart,applydateEnd);
                    
                    
                    // 加上总量一行
                    this.setRowCount(listNumCount);
                    
                    return endlist;
                }
            };
        }
        return modelall;
    }



    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("businessname,satisfy5,satisfy4,satisfy3,satisfy2,satisfy1",
                    "主题名称,非常满意,满意,基本满意,不满意,非常不满意");
        }
        return exportModel;
    }



    public String getApplydateStart() {
        return applydateStart;
    }

    public void setApplydateStart(String applydateStart) {
        this.applydateStart = applydateStart;
    }

    public String getApplydateEnd() {
        return applydateEnd;
    }

    public void setApplydateEnd(String applydateEnd) {
        this.applydateEnd = applydateEnd;
    }

    
}
