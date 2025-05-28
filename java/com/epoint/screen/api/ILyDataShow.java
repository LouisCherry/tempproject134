package com.epoint.screen.api;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Service
public interface ILyDataShow {

	/**
	 * 获取人流量数据
	 */
	public abstract List<Integer[]> getRenliuliangNum(String sql);
	/**
	 * 部门取号量
	 */
	public abstract AuditCommonResult<List<Record>> getQuhaoliangByOu(String sql, List<AuditOrgaDepartment> ouList);
	/**
	 *  窗口取号量
	 */
	public abstract AuditCommonResult<List<Record>> getQuhaoliangWindowByOuguid(String sql,String ouguid);
	
	public abstract AuditCommonResult<Integer> getQuhaoliangByHandleWindowguid(String sql,String ouguid);
	
	public abstract AuditCommonResult<Integer> getOuQuhaoliangByWindowguidList(String sql,String windowlist);
	
	/**
	 * 获取评价数据
	 */
	public abstract Integer getEvaluationNum(String satisfied);
	/**
     * 根据搜索时间获取评价数据
     */
    public abstract Integer getEvaluationNumByTime(String satisfied, String startdate, String enddate);
	/**
	 * 获取入驻中心的部门
	 */
	public abstract AuditCommonResult<List<AuditOrgaDepartment>> getCenterOuList(String centerguid);
	/**
	 * 获取年龄段数据
	 */
	public abstract List<Integer[]> getNianlingduanNum(String sql);
	/**
	 * 获取取号量
	 */
	public abstract AuditCommonResult<List<Integer[]>> getQuhaoNum(String sql, List<String> monthlist);
	/**
	 * 获取办件量
	 */
	public abstract AuditCommonResult<List<Integer[]>> getBanjianNum(String sql, List<String> monthlist);
	/**
	 * 获取办结量
	 */
	public abstract AuditCommonResult<List<Integer[]>> getBanjieNum(String sql, List<String> monthlist);
	/**
	 * 根据日期获取取号量
	 */
	public abstract AuditCommonResult<List<Integer[]>> getQuhaoNumByDate(String sql, List<String> dateList);
	
	   /**
     * 
     */
    public abstract AuditCommonResult<List<Integer[]>> getVisNumByDate(String sql, List<String> dateList);
	/**
	 * 性别数据
	 */
	public abstract AuditCommonResult<List<Integer[]>> getGenderNum(String sql);
	/**
	 * 获取窗口业务top10
	 */
	public abstract AuditCommonResult<List<Record>> getWindowTop10();
	/**
     * 根据查询时间获取窗口业务top10
     */
    public abstract AuditCommonResult<List<Record>> getWindowTop10ByTime(String startdate, String enddate,String isjd);
	/**
	 * 获取窗口业务top10
	 */
	public abstract AuditCommonResult<List<Record>> getOuTop10();
	/**
     * 根据搜索时间获取窗口业务top10
     */
    public abstract AuditCommonResult<List<Record>> getOuTop10ByTime(String startdate, String enddate,String isjd);
	/**
	 * 办件量
	 */
	public abstract AuditCommonResult<Integer> getBanjianSumByDate(String date);
	/**
     * 根据搜索时间获取办件量
     */
    public abstract AuditCommonResult<Integer> getBanjianSumByTime(String date, String startdate, String enddate);
	/**
	 * 办结量
	 */
	public abstract AuditCommonResult<Integer> getBanjieSumByDate(String date);
	/**
     * 根据搜索时间获取办结量
     */
    public abstract AuditCommonResult<Integer> getBanjieSumByTime(String date, String startdate, String enddate);
    /**
     * 根据搜索时间获取总取号人数
     */
    public AuditCommonResult<Integer> getQueueCountByTime(String startdate, String enddate);
	 /**
     * 获取当天取号人数
     */
    public AuditCommonResult<Integer> getQueueCountByDate();
    /**
     * 获取当天办件数
     */
    public AuditCommonResult<Integer> getProjectCountByDate();
    /**
     * 获取当天等待人数
     */
    public AuditCommonResult<Integer> getWaitNumByDate();
	/**
	 * 根据事项类型获取事项数量
	 */
    public AuditCommonResult<Integer> getTaskCountByShenpilb(String shenpilb1,String shenpilb2,String shenpilb3);
    /**
	 * 根据办件类型获取事项数量
	 */
    public AuditCommonResult<Integer> getTaskCountByType(String type);
    /**
     * 根据年月获取办件量
     */
    public AuditCommonResult<Integer> getBanjianCountByMonth(String month,String isjd);
	/**
	 * 热门事项top5
	 */
    public AuditCommonResult<List<AuditProject>> getHotTaskTop5();
    /**
     * 根据查询时间获取热门事项top5
     */
    public AuditCommonResult<List<AuditProject>> getHotTaskTop5ByTime(String startdate, String enddate,String isjd);
    /**
     * 根据企业类型获取企业数量
     */
    public AuditCommonResult<Integer> getCompanyCountByEnterprisetype(String enterprisetype);
    /**
     * 根据企业地址获取企业设立数量
     */
    public AuditCommonResult<Integer> getCompanyCountByTown(String town);
    /**
     * 根据企业地址获取企业设立总数
     */
    public AuditCommonResult<Integer> getCompanyCount();
    /**
     * 根据年龄段获取数量
     */
    public AuditCommonResult<Integer> getAgeGroupNum(String age,String gender);
    /**
     * 获取总注册资产
     */
    public AuditCommonResult<Double> getRegisteredcapitalSum();
    /**
     * 根据月份获取取号量
     */
	public AuditCommonResult<Integer> getQueueCountByMonth(String month);
	/**
	 * 户籍
	 */
	public  AuditCommonResult<Integer> getHomeCount(String status);
	
	public  AuditCommonResult<Integer> getJDHomeCount(String status);
	
	/**
	 * 户籍2 1.省内 2 省外 3北上广深
	 */
	public AuditCommonResult<Integer> isBelongTo(String status);
	
	
	/**
     * 根据线上办件总量
     */
    public AuditCommonResult<Integer> getXianxiaCount(String startdate, String enddate,String isjd);
    /**
     * 根据线下办件总量
     */
    public AuditCommonResult<Integer> getTotalCount(String startdate, String enddate,String isjd);
    
    public AuditCommonResult<Integer> getProjectHome(String startdate, String enddate,int type);
    
    public AuditCommonResult<Integer> getWindowTaskCount();
    //获取部门对应监察灯办件数
    public AuditCommonResult<List<Record>> getLightOuList(String startdate, String enddate,String isjd,int first,int pagesize);
    //获取部门对应监察灯办件数
    public AuditCommonResult<Integer> getLightOuCount(String startdate, String enddate,String isjd);
    
    public AuditCommonResult<Integer> getAppointTodayCount();
    
    public AuditCommonResult<List<Record>> getTodayAppointCountByHour();
    
    public AuditCommonResult<List<Record>> getBanjianTodayCountByHour();
    
    public AuditCommonResult<List<Record>> getQueueTodayCountByHour();
    
    public AuditCommonResult<List<Integer>> getRiskPointCompare();
    
    public AuditCommonResult<Map<String,List<String>>> getRiskUserCompare();
    
    public AuditCommonResult<List<Record>> getChildOuProjectCount(String parentouname,String starttime,String endtime);
    
    public AuditCommonResult<List<Record>> getChildOuHotTaskTop5ByTime(String startdate, String enddate,String ouguid);
    
}
