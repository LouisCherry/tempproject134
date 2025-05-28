package com.epoint.zwfw.dj.service;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 好差评相关接口的详细实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public class JnDjService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;




    public JnDjService() {
        dao = CommonDao.getInstance();

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
        String sql =" select * from audit_task a join frame_ou_extendinfo b on a.ouguid = b.ouguid where a.taskname = ? and ifnull(a.is_history,0)=0 and a.is_editafterimport = 1 and a.is_enable = 1 And b.areacode = ? ";
        return dao.find(sql,AuditTask.class,taskName,areacode);
    }

    /**
     * 获取办件基本信息
     * @param item_id 事项编码
     * @return 返回事项基本信息
     */
    public AuditTask getAuditBasicInfo(String item_id){
        String sql =" select * from audit_task where item_id = ? and ifnull(is_history,0)=0 and is_editafterimport = 1 and is_enable = 1 ";
        return dao.find(sql,AuditTask.class,item_id);
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





}
