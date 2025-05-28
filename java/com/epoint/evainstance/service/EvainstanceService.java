package com.epoint.evainstance.service;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.Evainstance;
import java.util.List;
import java.util.Map;

public class EvainstanceService {
	protected ICommonDao dao;

	public EvainstanceService() {
		this.dao = CommonDao.getInstance();
	}

	 /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Evainstance record) {
        return dao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = dao.find(Evainstance.class, guid);
        return dao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Evainstance record) {
        return dao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public Evainstance find(Object primaryKey) {
        return dao.find(Evainstance.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Evainstance find(String sql,  Object... args) {
        return dao.find(sql, Evainstance.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Evainstance> findList(String sql, Object... args) {
        return dao.findList(sql, Evainstance.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Evainstance> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return dao.findList(sql, pageNumber, pageSize, Evainstance.class, args);
    }

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countEvainstance(String sql, Object... args){
        return dao.queryInt(sql, args);
    }
    
    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
            String keyword, String userGuid)  {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData();
        Entity en = baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (pjgs = '" + keyword + "'  or pjqd = '" + keyword + " ')");
            sb.append(" and (sfhfang = '" + keyword + "' or sfhf = '" + keyword + "    ')");
           // sb.append(" and ");
        }

        
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }

        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) dao.findList(sqlRecord, first, pageSize, baseClass, new Object[0]);
        int dataCount = dao.queryInt(sqlCount, new Object[0]);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }
	public List<Record> getEvalDetail(String grade) {
		String sql = "select itemtext evacontant,ITEMVALUE evacode from code_items i join code_main m on i.codeid = m.codeid where m.codename='好差评满意度' and SUBSTRING(ITEMVALUE,1,1)=?  ";
		return dao.findList(sql, Record.class, grade);
	}

	public int addEvainstance(Evainstance evainstance) {
		return dao.insert(evainstance);
	}

	public Record findProject(String projectNo, String areacode) {
		String sql = "select t.Taskcode,t.ITEM_ID taskHandleItem,t.TaskName,p.TASKTYPE,p.OUGUID,p.OUNAME,p.APPLYERNAME,p.APPLYERTYPE,p.CERTNUM,p.CERTTYPE,p.CONTACTPERSON,p.CONTACTCERTNUM,p.CONTACTMOBILE,p.ACCEPTUSERDATE from audit_project p join audit_task t on p.taskguid = t.rowguid  where flowsn ='"
				+ projectNo + "' and p.areacode = '" + areacode + "'";
		return dao.find(sql, Record.class);
	}

	public boolean isExistEvaluate(String projectNo, int servicenum) {
		String sql = "SELECT * FROM evainstance where projectno = ? and assessNumber =?";
		Record record = dao.find(sql, Evainstance.class, projectNo, servicenum);

		return record != null;
	}

	public void insertEvainstanceState(Record record) {
		this.dao.insert(record);
	}

	public void updateEvainstanceState(Record record) {
		this.dao.update(record);
	}

	public int getMaxServicenum(String flowsn) {
		String sql = "SELECT ifnull(MAX(servicenumber),0) num FROM evainstance_state WHERE projectno = ?";
		return dao.find(sql, Integer.class, flowsn);
	}

	public boolean findProService(String flowsn, String workItemGuid) {
		String sql = "SELECT count(1) FROM evainstance_state WHERE projectno = ? AND workitemguid = ?";
		int i = dao.find(sql, Integer.class, flowsn, workItemGuid);
		return i > 0;
	}

	public boolean isExistProService(String projectno, int servicenum) {
		String sql = "SELECT * FROM evainstance_state where projectno = ? and serviceNumber =?";
		Record record = dao.find(sql, Evainstance.class, projectno, servicenum);

		return record == null;
	}

	public Evainstance findEvaluate(String projectNo, int servicenum) {
		String sql = "SELECT * FROM evainstance where projectno = ? and assessNumber =?";
		return dao.find(sql, Evainstance.class, projectNo, servicenum);
	}

	public Record getServiceByProjectno(String projectno, int assessNumber) {
		String sql = "SELECT * FROM evaluateservice WHERE projectNo = ? AND serviceNumber = ?";
		return dao.find(sql, Record.class, projectno, assessNumber);
	}

    public Record findAuditProjectByFlown(String projectno) {
        String sql = "select * from audit_project where flowsn = ?";
        return dao.find(sql, Record.class, projectno);
    }

    public List<Record> getServiceByProjectno(String projectno) {
        String sql ="SELECT * FROM evaluateservice_ck WHERE projectNo = ?";
        return dao.findList(sql, Record.class, projectno);
    }

    public Record getZhibiao(String string) {
        String sql = "SELECT itemtext itemtext,ITEMVALUE ITEMVALUE FROM code_items WHERE codeid = '1016203' AND ITEMVALUE = ?";
        return dao.find(sql, Record.class, string);
    }

    public int findFcbmyTotal(String areacode) {
        String sql = "select count(satisfaction) from evainstance where satisfaction = '2' and sfeypj is null and areacode = ?";
        return dao.queryInt(sql, areacode);
    }

    public int findBmyTotal(String areacode) {
        String sql = "select count(satisfaction) from evainstance  where satisfaction = '1' and sfeypj is null and areacode = ?";
        return dao.queryInt(sql, areacode);
    }
    public List<Evainstance> getPageDate(Evainstance data) {
        String sql = "SELECT pf,rowguid,taskCode,taskName,projectNo,acceptDate,userProp,userName,serviceNumber,areacode,mobile,pjqd,winName,winUserName,sfhf,sfhfang,khyj,khouname,ldps,ldmc,evalDetail,satisfaction,zgjg,nbyj,hfjg,hfxx FROM evainstance WHERE 1=1  ";
        if (data != null) {
            if (StringUtil.isNotBlank(data.getStr("areacode"))) {
                sql += " and areacode = '" + data.getStr("areacode") + "' ";
            }
            if (StringUtil.isNotBlank(data.getStr("satisfaction"))) {
                sql += " and satisfaction = '" + data.getStr("satisfaction") + "' ";
            } else {
                sql += " and satisfaction in (1,2)  ";

            }
            if (StringUtil.isNotBlank(data.getStr("sfhf"))) {
                sql += " and sfhf = '" + data.getStr("sfhf") + "' ";
            }
            if (StringUtil.isNotBlank(data.getStr("sfhfang"))) {
                sql += " and sfhfang = '" + data.getStr("sfhfang") + "' ";
            }
            if (StringUtil.isNotBlank(data.getStr("startDate"))) {
                sql += " and assessTime >= '" + data.getStr("startDate") + "' ";
            }
            if (StringUtil.isNotBlank(data.getStr("endDate"))) {
                String endDate = EpointDateUtil.convertDate2String(data.getDate("endDate"), "yyyy-MM-dd") + " 59:59:59";
                sql += " and assessTime <= '" + endDate + "' ";
            }
            sql += " order by areacode desc ";

        }

        List<Evainstance> list = dao.findList(sql, Evainstance.class);
        dao.close();
        return list;

    }
}