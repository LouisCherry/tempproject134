package com.epoint.jnzwfw.sgxkzproject.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.jnzwfw.sgxkzproject.api.entity.jzgcsgxkz1;

/**
 * 车辆信息表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public class JzgcSgxkzService {
	private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkurl");
	private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkname");
	private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zzkqzkpassword");

	/**
	 * 前置库数据源
	 */
	private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

	/**
	 * 数据增删改查组件
	 */
	protected ICommonDao formbaseDao;

	public JzgcSgxkzService() {
		formbaseDao = CommonDao.getInstance(dataSourceConfig);
	}

	/**
	 * 插入数据
	 * 
	 * @param record
	 *            BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int insert(jzgcsgxkz1 record) {
		return formbaseDao.insert(record);
	}

	/**
	 * 更新数据
	 * 
	 * @param record
	 *            BaseEntity或Record对象 <必须继承Record>
	 * @return int
	 */
	public int update(jzgcsgxkz1 record) {
		return formbaseDao.update(record);
	}

	public jzgcsgxkz1 getRecordByRowguid(String projectguid){
        String sql = "select * from jzgcsgxkz_1 where id = ?";
        return formbaseDao.find(sql, jzgcsgxkz1.class, projectguid);
    }
	
}
