package com.epoint.rsj.impl;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 好差评相关接口的详细实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public class JnService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;
    //浪潮前置库链接
    protected ICommonDao lcqzkdao;
    private static String QZKJURL = ConfigUtil.getConfigValue("datasyncjdbc", "urlqy");
	private static String QZKNAME = ConfigUtil.getConfigValue("datasyncjdbc", "usernameqy");
	private static String QZKPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "passwordqy");
	private DataSourceConfig dataSourceConfig = new DataSourceConfig(QZKJURL, QZKNAME, QZKPASSWORD);
    
    /**
     * 前置库数据源
     */
    
    public JnService() {
        dao = CommonDao.getInstance();
        lcqzkdao = CommonDao.getInstance(dataSourceConfig);

    }
   


	public int insert(Record rec) {
		return dao.insert(rec);
	}

    /**
     * 获取办件基本信息
     * @param taskName 事项名称
     * @param areacode 辖区编码
     * @return 返回事项基本信息
     */
    public AuditTask getAuditBasicInfo(String taskName, String areacode){
        String sql =" select * from audit_task where taskname = ? and ifnull(is_history,0)=0 and is_editafterimport = 1 and is_enable = 1 And areacode = ? ";
        return dao.find(sql,AuditTask.class,taskName,areacode);
    }

    /**
     * 通过rowguid查询表中是已有否有对应数据
     * @param rowguid
     * @return
     */
    public Record getAuditProjectZjxtByRowguid(String rowguid){
        String sql = " select * from audit_project_zjxt where rowguid = ? ";
        return dao.find(sql,Record.class,rowguid);
    }

    /**
     * 通过rowguid查询表中是已有否有对应数据
     * @param rowguid
     * @return
     */
    public Record getAuditRsApplyZjxtByRowguid(String rowguid){
        String sql = " select * from AUDIT_RS_APPLY_PROCESS_ZJXT where rowguid = ? ";
        return dao.find(sql,Record.class,rowguid);
    }



    public String getAreacodeByareaname(String areaname) {
        String sql = " select ITEMVALUE from  code_items ci  where CODEID  ='1015788' and itemtext  = ? ";
        return dao.find(sql,String.class,areaname);
    }

	public List<Record> getGxbysjyProjectList() {
		/*String sql = "select  * from EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名') "
				+ " and  trunc(occurtime)=trunc(sysdate)";*/
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	
	public List<Record> getKjjProjectList() {
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	public List<Record> getZyjnjdProjectList() {
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	public List<Record> getZyjsryProjectList() {
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	
	public List<Record> getRskszxProjectList() {
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	public List<Record> getSjglProjectList() {
		String sql = "select  * from dhqzk.EA_JC_STEP_BASICINFO where ITEMNAME in ('山东省院士工作站备案','大型科学仪器设备资源共享','技术合同认定登记','就业报到证调整手续办理','就业报到证改派手续办理',"
				+ "  '省内院校非师范类大中专毕业生就业报到证遗失补发办理','网上发布招聘信息','网上发布求职信息','职业资格证书查询','录用未成年工登记备案',"
				+ " '企业执行最低工资标准备案','劳动用工备案','企业集体合同、工资集体协议审查备案','企业裁减人员方案备案','职称评审及核准备案','职称评审委员会备案',"
				+ " '正规全日制院校毕业生职称资格认定','外地调入人员职称确认','审计专业技术资格考试报名','山东省人事考试中心网上报名系统','一级造价工程师执业资格考试报名',"
				+ " '咨询工程师（投资）职业资格考试报名','勘察设计注册工程师执业资格考试报名','注册城乡规划师职业资格考试报名','注册设备监理师执业资格考试报名',"
				+ " '注册计量师资格考试报名','注册测绘师资格考试报名','注册安全工程师执业资格考试报名','一、二级注册建筑师资格考试报名','一级建造师资格考试报名',"
				+ " '一级注册消防工程师资格考试报名','助理社会工作师、社会工作师职业水平考试报名','全国监理工程师资格考试报名','环境影响评价工程师职业资格考试报名',"
				+ " '房地产估价师资格考试报名','出版专业技术人员职业资格考试报名','统计专业技术资格考试报名','审计专业技术资格考试报名','经济专业技术资格考试报名','翻译专业资格（水平）考试报名','特种设备检验检测机构核准','特种设备生产（包括设计、制造、安装、改造、修理）许可') "
				+ " and NVL(xdsync,'0') = '0' and REGION_ID = '370800000000' and rownum <= 50 ";
		return lcqzkdao.findList(sql, Record.class);
	}
	



	public List<Record> getHjProjectList(String oRGBUSNO) {
		String sql = "select * from dhqzk.EA_JC_STEP_PROC where ORGBUSNO='"+oRGBUSNO+"'";
		return lcqzkdao.findList(sql, Record.class);
	}



	public Record getIfSyncByProid(String str) {
		String sql = "select * from audit_project_zjxt where flowsn='"+str+"'";
		return dao.find(sql, Record.class);
	}



	public int updateByProid(String str,String status) {
		String sql = "update dhqzk.EA_JC_STEP_BASICINFO set xdsync='"+status+"' where ORGBUSNO='"+str+"'";
		return lcqzkdao.execute(sql);
	}
}
