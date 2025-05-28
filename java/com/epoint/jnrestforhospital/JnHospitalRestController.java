package com.epoint.jnrestforhospital;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeMain;
import com.epoint.jnhospital.hospitalinfo.api.IHospitalinfoService;

@RestController
@RequestMapping("/jnhospital")
public class JnHospitalRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IHospitalinfoService hospitalinfoService;
    
    @Autowired
    private ICodeMainService codemainService;
    
    @Autowired
    private ICodeItemsService codeService;
    
    
    /**
     * 
     *  [接口]获取定点医院列表
     *  @param params
     * @return String[定点医院列表，定点医院列表总条数]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/gethospital", method = RequestMethod.POST)
    public String getHospital(@RequestBody String params) {
//        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject e = JSON.parseObject(params);
            JSONObject obj = (JSONObject) e.get("params");
            String hospital_name = obj.getString("hospital_name");
            int pageSize = Integer.parseInt(obj.getString("pagesize"));
            int pageIndex = Integer.parseInt(obj.getString("pageindex"));
            List<Record> hospitalList = new ArrayList<Record>();
            int total=0;
            //判断是否通过机构名称查询
            if (StringUtil.isNotBlank(hospital_name)) {
                hospitalList = hospitalinfoService.getHospitalListByName(hospital_name,pageIndex,pageSize);
                total = hospitalinfoService.getCountByName(hospital_name);
            }
            else {
                hospitalList = hospitalinfoService.getHospitalList(pageIndex,pageSize);
                total = hospitalinfoService.getCount();
            }
            //获取代码项数据
            CodeMain codeLevel=codemainService.getCodeMainByName("医院级别");
            CodeMain codeGrade=codemainService.getCodeMainByName("医院等级");
            CodeMain codeType=codemainService.getCodeMainByName("医院类型");
            String hospitalLevel,hospitalGrade,hospitalType="";
            for(Record record : hospitalList) {
                if(StringUtil.isNotBlank(record.getStr("hospital_level"))) {
                    hospitalLevel =codeService.getItemTextByCodeID(String.valueOf(codeLevel.getCodeID()), record.getStr("hospital_level")); 
                }else {
                    hospitalLevel=record.getStr("hospital_level");
                }
                if(StringUtil.isNotBlank(record.getStr("hospital_grade"))) {
                    hospitalGrade =codeService.getItemTextByCodeID(String.valueOf(codeGrade.getCodeID()), record.getStr("hospital_grade"));                    
                }else {
                    hospitalGrade =record.getStr("hospital_grade");
                }
                if(StringUtil.isNotBlank(record.getStr("hospital_type"))) {
                    hospitalType=codeService.getItemTextByCodeID(String.valueOf(codeType.getCodeID()), record.getStr("hospital_type"));      
                }else {
                    hospitalType=record.getStr("hospital_type");
                }
                record.put("hospitalLevel", hospitalLevel);
                record.put("hospitalGrade", hospitalGrade);
                record.put("hospitalType", hospitalType);
            }
            
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", hospitalList);
            dataJson.put("total", total);
            return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);

        }
        catch (Exception e) {
            log.info("【济宁定点医院信息结束调用getHospital异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", e.getMessage());
        }
    }
    
    
}
