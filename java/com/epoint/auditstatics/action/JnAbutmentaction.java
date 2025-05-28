package com.epoint.auditstatics.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditstatics.api.IJnabutmentService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.inter.IAuditstatics;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController("jnabutmentaction")
@Scope("request")
public class JnAbutmentaction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditstatics auditstaticsService;
    @Autowired
    private IJnabutmentService jnabutmentService;
    @Autowired
    private ICodeItemsService codeItemsService;
    
    @Override
    public void pageLoad() {
        outaskStatics();
    }
    
    /**
     * 清单6大类统计
     */
    public void categoryStatics(){
    	String areacode=ZwfwUserSession.getInstance().getAreaCode();

        List<Record> datalist=jnabutmentService.categoryStatics(areacode);
        datalist.removeIf(record -> {
            if (StringUtil.isBlank(record.get("SHENPILB"))) {

                return true;
            }
            else {
                return false;
            }
        });
        if(datalist!=null&&datalist.size()>0){
            for (Record record : datalist) {
                if(StringUtil.isNotBlank(record.get("SHENPILB"))){
                    record.put("name", codeItemsService.getItemTextByCodeName("审批类别", record.get("SHENPILB")));
                    record.remove("SHENPILB");
                }
            }
        }
        addCallbackParam("categorylist", datalist);
    }
    
    public void taskSourceStatic(){
    	String areacode=ZwfwUserSession.getInstance().getAreaCode();
        List<Record> datalist=jnabutmentService.taskSourceStatics(areacode);
        datalist.removeIf(record -> {
            if (StringUtil.isBlank(record.get("tasksource"))) {

                return true;
            }
            else {
                return false;
            }
        });
        if(datalist!=null&&datalist.size()>0){
            for (Record record : datalist) {
                if(StringUtil.isNotBlank(record.get("tasksource"))){
                    if("2".equals(record.get("tasksource"))){
                        record.put("name","事项落地");
                    }else if("0".equals(record.get("tasksource"))){
                        record.put("name","中心自建");
                    }else if("1".equals(record.get("tasksource"))){
                        record.put("name","窗口自建");
                    }
                    record.remove("tasksource");
                }
            }
        }
        addCallbackParam("tasksourcelist", datalist); 
    }
    
    /**
     * 类型统计
     */
    public void typeStatics(){
    	String areacode=ZwfwUserSession.getInstance().getAreaCode();
        List<Record> datalist=jnabutmentService.typeStatics(areacode);
        datalist.removeIf(record->{
            if(StringUtil.isBlank(record.get("type"))){
                return true;
            }
            else{
                return false;
            }
        });
        if(datalist!=null&&datalist.size()>0){
            for (Record record : datalist) {
                if(StringUtil.isNotBlank(record.get("type"))){
                    record.put("name", jnabutmentService.getItemTextByCodeName(record.get("type").toString()));
                    record.remove("type");
                }
            }
        }
        addCallbackParam("typelist", datalist);
    }
    
    /**
     * 
     *  [部门事项统计]
     *  [功能详细描述]    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void outaskStatics(){
       // List<Record> datalist=auditstaticsService.outaskStatics().getResult();
    	String areacode=ZwfwUserSession.getInstance().getAreaCode();
        List<Record> datalist=jnabutmentService.outaskStatics(areacode);
        datalist.removeIf(record -> {
            if (StringUtil.isBlank(record.get("ouname"))||"".equals(record.get("ouname"))) {
                return true;
            }
            else{
                return false;
            }
        });
        addCallbackParam("outasklist", datalist);
    }

    
}
