package com.epoint.auditproject.guiji.utils;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 数据库连接工具类 
 * @author 刘雨雨
 * @time 2018年8月20日上午11:54:23
 */
public class DbUtils {

	public static ICommonDao getLocalCommonDao() {
		return CommonDao.getInstance();
	}

	/**
	 * 获取电子监察前置库（省前置库）
	 * @return
	 */
	public static ICommonDao getDzjcQzkCommonDao() {
		String fileName = "config.config";
		String url = ConfigUtil.getConfigValue(fileName, "dzjcsb-qzk-url");
		String username = ConfigUtil.getConfigValue(fileName, "dzjcsb-qzk-username");
		String password = ConfigUtil.getConfigValue(fileName, "dzjcsb-qzk-password");
		return DbUtils.getCommonDao(url, username, password);
	}

	public static ICommonDao getProjectQzkCommonDao() {
		String fileName = "config.config";
		String url = ConfigUtil.getConfigValue(fileName, "bjsb-qzk-url");
		String username = ConfigUtil.getConfigValue(fileName, "bjsb-qzk-username");
		String password = ConfigUtil.getConfigValue(fileName, "bjsb-qzk-password");
		return getCommonDao(url, username, password);
	}
	
	/**
	 * 获取电子监察前置库（新点新增的）
	 * @return
	 */
	public static ICommonDao getEpointDzjcQzkCommonDao() {
		String fileName = "config.config";
		String url = ConfigUtil.getConfigValue(fileName, "epoint-dzjcsb-qzk-url");
		String username = ConfigUtil.getConfigValue(fileName, "epoint-dzjcsb-qzk-username");
		String password = ConfigUtil.getConfigValue(fileName, "epoint-dzjcsb-qzk-password");
		return DbUtils.getCommonDao(url, username, password);
	}

	private static ICommonDao getCommonDao(String url, String username, String password) {
		DataSourceConfig dsc = new DataSourceConfig(url, username, password);
		return CommonDao.getInstance(dsc);
	}
	
	/**
	 * 关闭数据源
	 * @param commonDaos
	 */
	public static void closeDs(ICommonDao...commonDaos) {
		if (commonDaos != null) {
			for (ICommonDao commonDao : commonDaos) {
				if (commonDao != null) {
					commonDao.close();
				}
			}
		}
	}
	
}
