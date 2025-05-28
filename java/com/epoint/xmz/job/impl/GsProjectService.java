package com.epoint.xmz.job.impl;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
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
public class GsProjectService
{
	private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "spglurl");
	private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "spglusername");
	private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "spglpassword");

	
	DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
	
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;
    protected ICommonDao qzkDao;
    
    
    /**
     * 前置库数据源
     */
    
    public GsProjectService() {
        dao = CommonDao.getInstance();
        qzkDao = CommonDao.getInstance(dataSourceConfig);
    }
   
    /**
	 * 
	 * 新增某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void insertQzk(Record record) {
		qzkDao.insert(record);
	}

	/**
	 * 修改某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void updateQzk(Record record) {
		qzkDao.update(record);
	}
	
	
	public List<AuditProject> getWaitEvaluateSbList() {
		String sql = "select * from audit_project where is_lczj = '2' AND STATUS = '90'  and (( ouname in ('济宁市医疗保障局','济宁市住房公积金管理中心') " +
				"and OperateUserName = '济宁市自建系统同步办件' and flowsn like '08%') or (task_id = '314aded6-2114-4fb8-aef8-593e98bebbec')) " +
				"and ACCEPTUSERNAME is not null and ACCEPTUSERNAME !=''and ACCEPTUSERDATE is not null and ifnull(zjsync,0) = 0 limit 50";
		return dao.findList(sql, AuditProject.class);
	}
	
	public List<AuditRsItemBaseinfo> getWaitItemList() {
		String sql = " select a.* from AUDIT_RS_ITEM_BASEINFO a join audit_sp_i_subapp b on a.rowguid = b.yewuguid ";
		sql += "join audit_sp_phase  c on b.phaseguid = c.rowguid where ifnull(a.gdjstatus,0) = 0 and  c.phaseid = '2' limit 100";
		return dao.findList(sql, AuditRsItemBaseinfo.class);
	}
	
	public List<AuditRsItemBaseinfo> getSwWaitItemList() {
		String sql = " select a.* from AUDIT_RS_ITEM_BASEINFO a join audit_sp_i_subapp b on a.rowguid = b.yewuguid ";
		sql += "join audit_sp_phase  c on b.phaseguid = c.rowguid where ifnull(a.swstatus,0) = 0 and  c.phaseid = '2' limit 100";
		return dao.findList(sql, AuditRsItemBaseinfo.class);
	}
	
	public List<Record> getSpglSgxkzList() {
		String sql = "SELECT "+ 
		"s.InvPropertyNum AS Inv_Property_Num,"+
		"s.RowGuid AS id,"+
		"s.XiangMuBianHao AS Prj_Num,"+
		"s.XiangMuMingChen AS Prj_Name,"+
		"(case when s.XiangMuFenLei='01' then '01' when s.XiangMuFenLei='02' then '02' else '99' end) AS Prj_Type_Num,"+
		"s.JianSheDanWeiMingChen AS Build_Corp_Name,"+
		"s.JianSheDanWeiTongYiSheHuiXinYo AS Project_Plan_Num,"+
		"s.XiangMuZongTouZiWanYuan AS All_Invest,"+
		"s.XiangMuZongJianZhuMianJiPingFa AS All_Area,"+
		"JianSheGuiMo AS Prj_Size,"+
		"'001' AS Prj_Property_Num,"+
		"HeTongKaiGongRiQi AS B_Date,"+
		"HeTongJunGongRiQi AS E_Date,"+
		"s.XiangMuDaiMa AS Prj_Code,"+
		"s.XiangMuSuoZaiShengFen AS Province_Num,"+
		"s.XiangMuSuoZaiChengShi AS City_Num,"+
		"(case when length(s.XiangMuSuoZaiQuXian)>2 then s.XiangMuSuoZaiQuXian else '370811' end) AS County_Num,"+
		"s.LiXiangPiFuShiJian AS Prj_Approval_Date,"+
		"s.ZiJinLaiYuan AS Fund_Source,"+
		"s.ZongChangDuMi1 AS All_Length,"+
		"'0' AS Is_Major,"+
		"p.ouname AS Check_Depart_Name,"+
		"p.BANJIEUSERNAME AS Check_Person_Name,"+
		"s.XiangMuDiDian AS Address,"+
		"p.OperateDate AS Create_Date,"+
		"s.JianSheDanWeiDiZhi AS Build_Corp_Add,"+
		"s.JianSheDanWeiSuoYouZhiXingShi AS Build_Corp_Owned,"+
		"s.JianSheDanWeiFaDingDaiBiaoRen AS Build_Corp_Legal_Person,"+
		"s.JianSheDanWeiDianHua AS Build_Corp_Phone,"+
		"s.JianSheDanWeiXiangMuFuZeRen AS Build_Corp_Prj_Person,"+
		"s.XiangMuDiShangJianZhuMianJiPin AS Floor_Area,"+
		"s.XiangMuDiShangChangDuMi AS Floor_Length,"+
		"s.XiangMuDiXiaJianZhuMianJiPingF AS Bottom_Area,"+
		"s.XiangMuDiXiaChangDuMi AS Bottom_Length,"+
		"s.XiangMuKuaDuMi AS All_Span,"+
		"s.HeTongJiaGeWanYuan AS Contract_Money,"+
		"s.GongChengZongChengBaoDanWei AS Contractor_Corp_Name,"+
		"s.gongchengzongchengbaodanweixia AS Contractor_Corp_Constructor,"+
		"s.ShiGongZongChengBaoQiYe AS Construction_Corp_Name,"+
		"s.ShiGongZongChengBaoQiYeXiangMu AS Construction_Corp_Person,"+
		"s.QuanGuoChengGongChengZiXunQiYe AS Advisory_Person,"+
		"s.KanChaDanWei AS Survey_Corp_Name,"+
		"s.KanChaDanWeiXiangMuFuZeRen AS Survey_Person,"+
		"s.SheJiDanWei AS Design_Corp_Name,"+
		"s.SheJiDanWeiXiangMuFuZeRen AS Design_Person,"+
		"s.JianLiDanWei AS Supervision_Corp_Name,"+
		"s.ZongJianLiGongChengShi AS Supervision_Person,"+
		"s.JianCeDanWei AS Detection_Corp_Name,"+
		"s.JianCeDanWeiFuZeRen AS Detection_Person,"+
		"s.ShenTuDanWei AS Review_Corp_Name,"+
		"s.ShenTuDanWeiXiangMuFuZeRen AS Review_Person,"+
		"s.jianshedanweitongyishehuixinyo AS Build_Corp_Code,"+
		"s.lixiangpifujiguan AS Prj_Approval_Depart,"+
		"s.gongchengyongtu AS Prj_Function_Num,"+
		"s.projectguid AS projectguid,"+
		"p.SUBAPPGUID AS SUBAPPGUID,"+
		"s.KanChaDanWeiCode AS Surrvey_Corp_Code,"+
		"s.SheJiDanWeiCode AS Design_Corp_Code,"+
		"s.gongchengzongchengbaodanweixia as Contractor_Corp_Constructor_Card_No,"+
		"s.ShenTuDanWeiCode as Review_Corp_Code,"+
		"s.ShiGongZongChengBaoQiYeTongYiS as Construction_Corp_Code"+
		" FROM audit_project_form_sgxkz s INNER JOIN audit_project p ON s.projectguid = p.RowGuid"+
		" WHERE p. STATUS = '90' and p.BANJIEUSERNAME is not null and s.JianSheDanWeiMingChen is not null"+
		" and s.XiangMuSuoZaiQuXian is not NULL and ifnull(s.sync,0) = 0 limit 100";
		return dao.findList(sql, Record.class);
	}
	
	
	public List<Record> getSpglSgxkzXkList() {
		String sql = "SELECT "+
		"s.bargaindays AS Bargain_Days,"+
		"s.RowGuid AS id,"+
		"c.certno AS Builder_Licence_Num,"+
		"s.XiangMuBianHao AS Prj_Num,"+
		"s.XiangMuMingChen AS Build_Prj_Name,"+
		"s.JianSheGongChengGuiHuaXuKeZhen AS Project_Plan_Num,"+
		"s.HeTongJiaGeWanYuan AS Contract_Money,"+
		"s.XiangMuZongJianZhuMianJiPingFa AS Area,"+
		"s.ZongChangDuMi1 AS Length,"+
		"s.XiangMuKuaDuMi AS Span,"+
		"s.JianSheGuiMo AS Prj_Size,"+
		"c.certno AS Release_Date,"+
		"p.OperateDate AS Create_Date,"+
		"p.ouname AS Check_Depart_Name,"+
		"p.BANJIEUSERNAME AS Check_Person_Name,"+
		"s.zhongbiaotongzhishubianhao AS Tender_Num,"+
		"s.shigongtushenchaxiangmubianhao AS Censor_Num,"+
		"s.projectguid AS projectguid"+
		" FROM audit_project_form_sgxkz s INNER JOIN audit_project p ON p.RowGuid = s.projectguid"+
		" INNER JOIN cert_info c ON p.CERTROWGUID = c.RowGuid WHERE p. STATUS = '90'"+
		" and p.BANJIEUSERNAME is not null and s.JianSheDanWeiMingChen is not null"+
		" and s.XiangMuSuoZaiQuXian is not NULL and length(s.bargaindays) >2 and ifnull(s.xmxksync,0) = 0 limit 100";
		return dao.findList(sql, Record.class);
	}
	
	public List<Record> getSpglSgxkzDantiList() {
		String sql = "SELECT "+
		"(case when d.ISSuperHightBuilding='1' then '1' else '0' end ) AS IS_Super_Hight_Building,"+
		"(case when d.SeismicIntensityScale='1'  then '003' when d.SeismicIntensityScale='2' then '002' else '001' end) AS Seismic_Intensity_Scale,"+
		"d.RowGuid AS id,"+
		"s.XiangMuBianHao AS Prj_Num,"+
		"d.DanTiJianGouZhuWuMingChen AS Sub_Prj_Name,"+
		"d.GongChengZaoJiaWanYuan AS Invest,"+
		"d.JianZhuMianJiPingFangMi AS Build_Area,"+
		"d.DiShangCengShu AS Floor_Count,"+
		"d.DiXiaCengShu AS Bottom_Floor_Count,"+
		"s.XiangMuDaiMa AS Prj_Code,"+
		"d.DiShangJianZhuMianJiPingFangMi AS Floor_Build_Area,"+
		"d.DiXiaJianZhuMianJiPingFangMi AS Bottom_Floor_Build_Area,"+
		"d.ChangDuMi AS Sub_Project_Length,"+
		"d.KuaDuMi AS Sub_Project_Span,"+
		"d.DiShangChangDuMi AS Floor_Build_Length,"+
		"d.DiXiaChangDuMi AS Bottom_Build_Length,"+
		"d.OperateDate AS Create_Date,"+
		"s.projectguid AS projectguid"+
		" FROM"+
		" audit_project_form_sgxkz_danti d"+
		" INNER JOIN audit_project_form_sgxkz s ON d.projectguid = s.projectguid"+
		" WHERE"+
		" d.zjdttb = '0'"+
		" and d.GongChengZaoJiaWanYuan >'1'"+
		" and d.GongChengZaoJiaWanYuan is not null and ifnull(d.sync,0) = 0 limit 100";
		return dao.findList(sql, Record.class);
	}
	
	
    public ICommonDao getCommonDaoTo() {
        return dao;
    }


	public List<Record> getHpDataList() {
		String sql="select a.xh,a.HJYXPJGLLB,b.SLRQ,b.SLR,b.PROJID,b.BLRLXSJ,b.BLR,b.QYZCDZ, b.SQRLX,b.SQRZJHM,b.SQRZJLX,a.ORGID,b.SQRZJHM  "
				+" from SOMYCN.t_xmsp_sp_jbxx a  INNER JOIN   SOMYCN.t_XMSP_ZWFW_JBXX b  on a.xh = b.xh  where a.ORGID like '3708%'  and a.cjsj > TO_DATE('2021-01-01 00:00:00', 'YYYY/MM/DD HH24:MI:SS') ";
		return qzkDao.findList(sql, Record.class);
	}


	public void insert(Record rec) {
		dao.insert(rec);
	}
	
	
	public Record getSpglSgxkIteminfoBySubappguid(String projectguid) {
		String sql = "SELECT "+
		"case when IFNULL(b.parentid,0) = 0 then b.rowguid else b.parentid end as ID,"+
		"b.itemCode as Prj_Code,"+
		"b.itemName as Prj_Name,"+
		"case when b.itemtype = '1' then '01' when b.itemtype = '2' then '02' when b.itemtype <> '1' and b.itemtype <> '2' then '99' else '99' end as Prj_Type_Num,"+
		"'370000' as Province_Num,"+
		"'370800' as City_Num,"+
		"case when c.AREACODE='370890' then '370899' when c.AREACODE='370891' then '370888' when c.AREACODE='370892' then '370877' else c.AREACODE end as Country_Num,"+
		"'山东省济宁市' as Address,"+
		"b.ITEMLEGALDEPT as Build_Corp_Name,"+
		"b.ITEMLEGALCERTNUM as Build_Corp_Code,"+
		"'100' as Inv_Property_Num,"+
		"b.TOTALINVEST as All_Invest,"+
		"case when b.CONSTRUCTIONPROPERTY = '1' then '001' when b.CONSTRUCTIONPROPERTY = '2' then '003' when b.CONSTRUCTIONPROPERTY = '3' then '005' when b.CONSTRUCTIONPROPERTY = '4' then '002' when b.CONSTRUCTIONPROPERTY = '5' then '099' end as Prj_Property_Num,"+
		"'999' as Prj_Function_Num,"+
		"'0' as Is_Major,"+
		"c.ouname as Check_Depart_Name,"+
		"c.BANJIEUSERNAME as Check_Person_Name,"+
		"'1' as Data_Source,"+
		"case when c.AREACODE='370800' then 'B' else 'C' end as Data_Level"+
		" from AUDIT_RS_ITEM_BASEINFO b "+
		" join audit_sp_i_subapp a on a.yewuguid = b.rowguid "+
		" inner join audit_project c on a.rowguid = c.subappguid "+
		" where c.rowguid = ?";
		return dao.find(sql, Record.class,projectguid);
	}
	
	public Record getSpglDantiInfoBySubappguid(String projectguid) {
		String sql = "SELECT "+
		"d.subappguid as id,"+
		"c.rowguid as project_id,"+
		"a.dantiname AS Sub_Prj_Name,"+
		"a.PRICE AS Invest,"+
		"case when IFNULL(a.ISLOWSHOCK,0) = 0 then '0' else '1' end as Is_Shockisolation_Building,"+
		"case when IFNULL(a.GREENBUILDINGNORM,0) = 0 then '0' else '1' end as Is_Green_Building,"+
		"case when a.KZLEVEL in ('4','5') then '004' when a.KZLEVEL = '1' then '002' when a.KZLEVEL in ('2','3') then '003' when a.KZLEVEL = '6' then '005' else '001' end as Seismic_Intensity_Scale,"+
		"'0' AS IS_Super_Hight_Building"+
		" FROM danti_sub_relation b "+
		" JOIN danti_info a ON b.DANTIGUID = a.rowguid "+
		" JOIN AUDIT_RS_ITEM_BASEINFO c ON c.rowguid = a.projectguid "+
		" inner join audit_project d on b.subappguid = d.subappguid "+
		" where d.rowguid = ?";
		return dao.find(sql, Record.class,projectguid);
	}
	

	public List<Record> getSpglParticipnListBySubappguid(String projectguid) {
		String sql = "SELECT "+
		"UUID() as id,"+
		"case when IFNULL(e.parentid,0) = 0 then e.rowguid else e.parentid end as project_id,"+
		"d.subappguid as unit_project_id,"+
		"case when a.CORPTYPE = '31' then '5' when a.CORPTYPE = '210' then '6' else  a.CORPTYPE end as Corp_Role_Num,"+
		"CORPNAME as Corp_Name,"+
		"CORPCODE as Corp_Code,"+
		"XMFZR as Person_Name,"+
		"'1' as ID_Card_Type_Num,"+
		"XMFZR_IDCARD as Person_ID_Card,"+
		"XMFZR_PHONE as Person_Phone"+
		" FROM PARTICIPANTS_INFO a "+
		" inner join audit_project d on a.subappguid = d.subappguid "+
		" join audit_sp_i_subapp c on a.subappguid = c.rowguid " +
		" JOIN AUDIT_RS_ITEM_BASEINFO e ON e.rowguid = c.yewuguid " +
		" WHERE CORPTYPE <> '999' and d.rowguid = ? ";
		return dao.findList(sql, Record.class,projectguid);
	}
	
	public Record getSpglSgxkInfoBySubappguid(String projectguid) {
		String sql = "SELECT "+
		"UUID() as id,"+
		"case when IFNULL(b.parentid,0) = 0 then b.rowguid else b.parentid end as project_id,"+
		"a.ITEMNAME as Bui_Prj_Name,"+
		"datediff(b.ITEMFINISHDATE,b.ITEMSTARTDATE) as Bargain_Days,"+
		"b.TOTALINVEST as Contract_Money,"+
		"DATE_FORMAT(d.banjiedate,'%Y-%m-%d') as Release_Date,"+
		"DATE_FORMAT(d.banjiedate,'%Y-%m-%d') as Create_Date,"+
		"d.ouname as Check_Depart_Name,"+
		"d.BANJIEUSERNAME as Check_Person_Name,"+
		"d.ouname as Check_Depart_Name,"+
		"'1' as Data_Source,"+
		"case when d.AREACODE='370800' then 'B' else 'C' end as Data_Level"+
		" FROM AUDIT_SP_SP_SGXK a join AUDIT_RS_ITEM_BASEINFO b on a.ITEMCODE = b.ITEMCODE "+
		" inner join audit_project d on a.subappguid = d.subappguid "+
		" WHERE d.rowguid = ? ";
		return dao.find(sql, Record.class,projectguid);
	}
	
	public Record getSpglJgYsInfoBySubappguid(String projectguid) {
		String sql = "SELECT "+
				"UUID() as id,"+
				"case when IFNULL(b.parentid,0) = 0 then b.rowguid else b.parentid end as project_id,"+
				"a.ITEMNAME as Fin_Prj_Name,"+
				"b.ITEMCODE as Prj_Code,"+
				"a.allmoney AS Fact_Cost,"+
				"case when IFNULL(a.areaused,0) = 0 then '0' else a.areaused end as Fact_Area,"+
				"'0' as Length,"+
				"'0' as Span,"+
				"b.CONSTRUCTIONSCALEANDDESC as Fact_Size,"+
				"DATE_FORMAT(b.ITEMSTARTDATE, '%Y-%m-%d') AS B_Date,"+
				"DATE_FORMAT(b.ITEMFINISHDATE, '%Y-%m-%d') AS E_Date,"+
				"d.ouname as Check_Depart_Name,"+
				"a.jzzh as Builder_Licence_Num,"+
				"d.BANJIEUSERNAME as Check_Person_Name,"+
				"d.ouname as Check_Depart_Name,"+
				"'1' as Data_Source,"+
				"case when d.AREACODE='370800' then 'B' else 'C' end as Data_Level"+
				" FROM AUDIT_SP_SP_JGYS a join AUDIT_RS_ITEM_BASEINFO b on a.ITEMCODE = b.ITEMCODE "+
				" inner join audit_project d on a.subappguid = d.subappguid "+
				" WHERE d.rowguid = ? ";
		return dao.find(sql, Record.class,projectguid);
	}
	
	
	public Record getProjectBasicInfoByRowguid( String id) {
    	String sql = "select * from TB_Project_Info where id = ?";
        return qzkDao.find(sql, Record.class, id);
    }
	
	public Record getProjectUnitInfoByRowguid( String id) {
		String sql = "select * from TB_Unit_Project_Info where id = ?";
		return qzkDao.find(sql, Record.class, id);
	}
	public Record getProjectCorpInfoByRowguid( String id) {
		String sql = "select * from TB_Project_Corp_Info where id = ?";
		return qzkDao.find(sql, Record.class, id);
	}
	public Record getProjectFinishByRowguid( String id) {
		String sql = "select * from TB_Project_Finish_Manage where id = ?";
		return qzkDao.find(sql, Record.class, id);
	}
	public Record getProjectLicenceByRowguid( String id) {
		String sql = "select * from TB_Builder_Licence_Manage where id = ?";
		return qzkDao.find(sql, Record.class, id);
	}
	
	public Record getSgxkzInfoByPorjectId( String id) {
    	String sql = "select * from TB_Builder_Licence_Manage where Project_Id = ?";
        return qzkDao.find(sql, Record.class, id);
    }
	
	public AuditSpSpSgxk getSpSgxkByRowguid(String rowguid) {
		String sql = "select * from AUDIT_SP_SP_SGXK where subappguid = ?";
        return dao.find(sql, AuditSpSpSgxk.class, rowguid);
	}
	    

}
