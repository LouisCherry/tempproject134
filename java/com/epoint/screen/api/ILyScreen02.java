package com.epoint.screen.api;

import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

public interface ILyScreen02
{
    
    public AuditCommonResult<List<Record>> getEnterpriseIndustrytype(String starttime ,String endtime ,String limit);
    
    public AuditCommonResult<List<Record>> getindividualType(String starttime ,String endtime ,String limit);
       
    public AuditCommonResult<Integer> getSetupCountByType(String starttime ,String endtime ,Integer type,String town);
    
    public AuditCommonResult<Integer> getAgeGroupNum(String starttime ,String endtime ,Integer sex,String agerange);
    
    public AuditCommonResult<Integer> getJDAgeGroupNum(String starttime ,String endtime ,Integer sex,String agerange);
    
    public AuditCommonResult<Double> getRegisteredcapital(String starttime ,String endtime,Integer type);
    
    public AuditCommonResult<Double> getRegisteredcapitalIsBelong(String starttime ,String endtime,Integer type);
    
    
    public AuditCommonResult<Integer> getSetupSumByType(String starttime ,String endtime ,Integer type);
    
    public AuditCommonResult<Integer> getSignCount(String starttime ,String endtime);
    
    public AuditCommonResult<Integer> getAllCountByType(String starttime ,String endtime ,Integer type);

    //节省工作日
    public List<Record> getCountDaysParams(String starttime,String endtime);
    //满意和不满意的办件数量
    public Map<String,Integer> getEvaluatProjectCount(String starttime, String endtime);
    //网上注册量
    public AuditCommonResult<Integer> getOlineRegister(String starttime, String endtime);
    //网上办件量
    public AuditCommonResult<Integer> getOlineProject(String starttime, String endtime);
    //网上预约量
    public AuditCommonResult<Integer> getOlineQueue(String starttime, String endtime);
    //网上咨询量
    public AuditCommonResult<Integer> getOlineConsult(String starttime, String endtime);
    //微信关注量
    public AuditCommonResult< Integer> getWeChartCount();
    //根据办件种类获取办件数
    public AuditCommonResult<Map<String, Integer>> getProjectCountByType(String starttime, String endtime);
    //根据办件种类获取办件的部门分类
    public AuditCommonResult<Map<String, List<Record>>> getTaskOuTypeByType();
    //根据标准获取事项数
    public AuditCommonResult<Integer> getTaskCountByWebLevel(int level); 
    //根据标准和部门类型获取事项数
    public AuditCommonResult<List<Record>> getTaskCountByWebLevelAndOUtype(int level);
    //法人库信息量
    public AuditCommonResult<Integer> getCompanyCount(); 
    //个人库信息量
    public AuditCommonResult<Integer> getIndividualCount(); 
    //根据事项类别获取事项数
    public AuditCommonResult< List<Record>> getTaskCountByType(String shenpilb1,String shenpilb2,String shenpilb3);
    //涉及部门
    public AuditCommonResult< List<Record>> getOuCount();
    //办事指南/事项清单/启用事项数
    public AuditCommonResult< Integer> getTaskCount();
    //岗位数
    public AuditCommonResult< Integer> getRiskPointCount();
    //表单数
    public AuditCommonResult< Integer> getMaterialCount();
    //本周线上线下数，时间
    public AuditCommonResult<Integer> getWeekProjectTimeAndCount(String time1,String time2,String applyway,String isjd);

}
