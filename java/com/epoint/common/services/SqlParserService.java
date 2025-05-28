package com.epoint.common.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.util.SqlConstant;

/**
 * 
 * SQL管理工具类
 * 
 * @version [版本号, 2016年8月22日]
 */
public class SqlParserService
{
    /**
     * 数据库操作dao
     */
    protected ICommonDao commonDao;

    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    public SqlParserService(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null){
            commonDao = CommonDao.getInstance(conf);
        }
        else{
            commonDao = CommonDao.getInstance();
        }
    }

    /**
     * 以下是4种构造函数重载。
     */
    public SqlParserService() {
        //配置指定数据源，先读指定数据源
        commonDao = CommonDao.getInstance("epointcert");
//        DataSource epointcert= dataSourceService.getDataSourceByName("epointcert");
//        if(epointcert!=null){
//            DataSourceConfig datasourceconfig=IDataSourceService.changeDsToDatasourceConfig(epointcert);
//            // 注入到容器
//            EpointFrameDsManager.inject(datasourceconfig);
//            commonDao = CommonDao.getInstance(datasourceconfig);
//        }else{       
//            commonDao = CommonDao.getInstance();        
//        }
    }

    public SqlParserService(String dataSourceName) {
        commonDao = CommonDao.getInstance(dataSourceService.getDataSourceByName(dataSourceName));
    }

    public SqlParserService(DataSourceConfig dataSourceConfig) {
        commonDao = CommonDao.getInstance(dataSourceConfig);
    }

    public SqlParserService(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    // 分页查询
    @SuppressWarnings("unchecked")
    public <T> PageData<T> getListByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        PageData<T> pageData = new PageData<T>();
        Entity en = baseClass.getAnnotation(Entity.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        // 查询字段
        String fields = "*";
        // 排序字段
        String sortCondition = "";
        
        String tableName = en.table();
        
        List<String> params = new ArrayList<>();
        sb.append(buildSql(conditionMap, params));
        if (conditionMap != null) {
            if (conditionMap.containsKey("#fields")) {
                fields = conditionMap.get("#fields");
                if (StringUtil.isBlank(fields)) {
                    fields = "*";
                }
            }

            // 若Map中不存在排序， 使用sortField排序
            if (StringUtil.isBlank(conditionMap.get("#sort")) && StringUtil.isNotBlank(sortField)) {
                sortCondition = " order by " + sortField + " " + sortOrder;
                if (commonDao.isOracle() || commonDao.isDM()) {
                    sortCondition += " nulls last ";
                }
            }
            
            if (StringUtil.isNotBlank(conditionMap.get("#join"))){
            	tableName = getTable(tableName + " a", conditionMap.get("#join"));
            }
        }
        else {
            if (StringUtil.isNotBlank(sortField)) {
                sortCondition = " order by " + sortField + " " + sortOrder;
                if (commonDao.isOracle() || commonDao.isDM()) {
                    sortCondition += " nulls last ";
                }
            }
        }

        String sqlRecord = "select " + fields + " from " + tableName + sb.toString() + sortCondition;
        String sqlCount = "select count(*) from " + tableName + sb.toString();
        Object[] paramsobject = params.toArray();
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass, paramsobject);
        int dataCount = commonDao.queryInt(sqlCount, paramsobject);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    private String getTable(String leftTablestr, String join){
    	String[] joinTables = join.split(";");
    	String joinType = "";
    	if(joinTables[0].startsWith("#left#")){
    		joinType = " left join ";
    	}else if(joinTables[0].startsWith("#right#")){
    		joinType = " right join ";
    	}else if(joinTables[0].startsWith("#inner#")){
    		joinType = " inner join ";
    	}
    	String[] field = joinTables[0].split("#");
    	String str = leftTablestr + joinType +  field[2] + " on " + field[3] + "=" + field[4];
    	if(joinTables.length>1){
    		return getTable(str, joinTables[1]);
    	}else{
    		return str;
    	}
    }
    
    // 根据conditionMap构造条件语句,兼容旧的写法
    public String buildSql(Map<String, String> conditionMap) {
        StringBuffer sb = new StringBuffer(" where 1=1 ");
        sb.append(buildPatchSql(conditionMap));
        return sb.toString();
    }

    public String buildSql(Map<String, String> conditionMap, List<String> params) {
        if (params == null) {
            System.err.println("params参数不能为null！");
            return "";
        }
        else {
            StringBuffer sb = new StringBuffer(" where 1=1 ");
            sb.append(buildPatchSql(conditionMap, params));
            return sb.toString();
        }
    }

    // 条件语句，不含where
    public String buildPatchSql(Map<String, String> conditionMap) {
        return buildSqlParams(conditionMap, false, null);
    }

    //参数化方式
    public String buildPatchSql(Map<String, String> conditionMap, List<String> params) {
        if (params == null) {
            System.err.println("params参数不能为null！");
            return "";
        }
        else {
            return buildSqlParams(conditionMap, true, params);
        }
    }

    /**
     * 
     * @param conditionMap
     * @param useParameterize 是否参数化
     * @param params 参数化时返回的参数列表
     * @return
     */
    private String buildSqlParams(Map<String, String> conditionMap, Boolean useParameterize, List<String> params) {
        StringBuffer sb = new StringBuffer();
        if (conditionMap != null) {
            if (!conditionMap.isEmpty()) {
                if (SqlConstant.STR_YES.equals(conditionMap.get("#isnew"))) {
                    for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                        String[] fieldAreaas = entry.getKey().trim().split(SqlConstant.SPLIT);
                        // 条件的情况
                        if (fieldAreaas.length < 3){
                            continue;
                        }
                        String fieldName = fieldAreaas[0];
                        String operateType = fieldAreaas[1];
                        String valueType = fieldAreaas[2];
                        String value = entry.getValue() == null ? "" : entry.getValue();
                        // 日期类型且非between
                        if ("D".equals(valueType) && !SqlConstant.SQL_OPERATOR_BETWEEN.equals(operateType)) {
                            if (StringUtil.isBlank(value)) {
                                value = EpointDateUtil.convertDate2String(new Date(0), EpointDateUtil.DATE_TIME_FORMAT);
                            }
                            if (useParameterize) {
                                if (commonDao.isOracle() || commonDao.isDM()) {
                                    params.add(value);
                                    value = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                                }
                                else {
                                    params.add(value);
                                    value = "?";
                                }
                            }
                            else {
                                if (commonDao.isOracle() || commonDao.isDM()) {
                                    value = "to_date('" + value + "','yyyy-MM-dd hh24:mi:ss')";
                                }
                                else {
                                    value = "'" + value + "'";
                                }
                            }
                        }
                        switch (operateType) {
                            case SqlConstant.SQL_OPERATOR_EQ: // 等于
                                if (useParameterize) {
                                    if (commonDao.isOracle() || commonDao.isDM() && fieldName.toLowerCase().contains("substring")) {
                                        sb.append(" and " + fieldName.toLowerCase().replace("substring", "substr")
                                                + "=?");
                                    }
                                    else {
                                        sb.append(" and " + fieldName + "=?");
                                    }
                                    params.add(value);
                                }
                                else {
                                    if (commonDao.isOracle() || commonDao.isDM() && fieldName.toLowerCase().contains("substring")) {
                                        sb.append(" and " + fieldName.toLowerCase().replace("substring", "substr")
                                                + "='" + value.replace("\\", "\\\\") + "'");
                                    }
                                    else {
                                        sb.append(" and " + fieldName + "='" + value.replace("\\", "\\\\") + "'");
                                    }
                                }

                                break;
                            case SqlConstant.SQL_OPERATOR_NQ: // 不等于
                                if (useParameterize) {
                                    sb.append(" and " + fieldName + "!=?");
                                    params.add(value);
                                }
                                else {
                                    sb.append(" and " + fieldName + "!='" + value + "'");
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_GT: // 大于
                                if (useParameterize && "S".equals(valueType)) {
                                    sb.append(" and " + fieldName + ">?");
                                    params.add(value);
                                }
                                else {
                                    sb.append(" and " + fieldName + ">" + value);
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_GE: // 大于等于
                                if (useParameterize && "S".equals(valueType)) {
                                    sb.append(" and " + fieldName + ">=?");
                                    params.add(value);
                                }
                                else {
                                    sb.append(" and " + fieldName + ">=" + value);
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_LT: // 小于
                                if (useParameterize && "S".equals(valueType)) {
                                    sb.append(" and " + fieldName + "<?");
                                    params.add(value);
                                }
                                else {
                                    sb.append(" and " + fieldName + "<" + value);
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_LE: // 小于等于
                                if (useParameterize && "S".equals(valueType)) {
                                    sb.append(" and " + fieldName + "<=?");
                                    params.add(value);
                                }
                                else {
                                    sb.append(" and " + fieldName + "<=" + value);
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_LIKE: // like
                                if (useParameterize) {
                                    sb.append(" and " + changeFieldName(fieldName) + " like ?");
                                    params.add("%" + value.replace("\\", "\\\\").replace("%", "\\%") + "%");
                                }
                                else {
                                    sb.append(" and " + changeFieldName(fieldName) + " like '%" + value.replace("\\", "\\\\").replace("%", "\\%") + "%'");
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_START_WITH: // startwith
                                if (useParameterize) {
                                    sb.append(" and " + changeFieldName(fieldName) + " like ?");
                                    params.add(value.replace("%", "\\%") + "%");
                                }
                                else {
                                    sb.append(" and " + changeFieldName(fieldName) + " like '" + value.replace("%", "\\%") + "%'");
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_END_WITH: // endwith
                                if (useParameterize) {
                                    sb.append(" and " + changeFieldName(fieldName) + " like ?");
                                    params.add("%" + value.replace("%", "\\%"));
                                }
                                else {
                                    sb.append(" and " + changeFieldName(fieldName) + " like '%" + value.replace("%", "\\%") + "'");
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_IN: // in
                                sb.append(" and " + fieldName + " in (" + value + ")");
                                break;
                            case SqlConstant.SQL_OPERATOR_NOTIN: // notin
                                sb.append(" and " + fieldName + " not in (" + value + ")");
                                break;
                            case SqlConstant.SQL_OPERATOR_BETWEEN: // between
                                String[] dates = value.split(SqlConstant.SPLIT, -1);
                                if (dates.length < 2){
                                    continue;
                                }
                                String fromDate = dates[0];
                                String endDate = dates[1];
                                if (StringUtil.isBlank(fromDate) && StringUtil.isBlank(endDate)){
                                    continue;
                                }
                                if (StringUtil.isBlank(fromDate)) {
                                    fromDate = EpointDateUtil.convertDate2String(new Date(0),
                                            EpointDateUtil.DATE_TIME_FORMAT);
                                }
                                if (StringUtil.isBlank(endDate)) {
                                    Calendar c = Calendar.getInstance();
                                    c.set(2900,0,1);
                                    endDate = EpointDateUtil.convertDate2String(c.getTime(),
                                            EpointDateUtil.DATE_TIME_FORMAT);
                                }
                                if (useParameterize) {
                                    if (commonDao.isOracle() || commonDao.isDM()) {
                                        params.add(fromDate);
                                        params.add(endDate);
                                        fromDate = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                                        endDate = "to_date(?,'yyyy-MM-dd hh24:mi:ss')";
                                    }
                                    else {
                                        params.add(fromDate);
                                        params.add(endDate);
                                        fromDate = "?";
                                        endDate = "?";
                                    }
                                    sb.append(" and " + fieldName + " between " + fromDate + " and " + endDate);
                                }
                                else {
                                    if (commonDao.isOracle() || commonDao.isDM()) {
                                        fromDate = "to_date('" + fromDate + "','yyyy-MM-dd hh24:mi:ss')";
                                        endDate = "to_date('" + endDate + "','yyyy-MM-dd hh24:mi:ss')";
                                    }
                                    else {
                                        fromDate = "'" + fromDate + "'";
                                        endDate = "'" + endDate + "'";
                                    }
                                    sb.append(" and " + fieldName + " between " + fromDate + " and " + endDate);
                                }

                                break;
                            case SqlConstant.SQL_OPERATOR_ISNULL: // 为空 或为某个值
                                if (StringUtil.isBlank(value)) {
                                    // 为空
                                    if (commonDao.isOracle() || commonDao.isDM()){
                                        sb.append(" and " + fieldName + " is null ");
                                    }
                                    else if (commonDao.isMySql()){
                                        sb.append(" and ifnull(" + fieldName + ",'')='' ");
                                    }
                                    else{
                                        sb.append(" isnull(" + fieldName + ",'')='' ");
                                    }

                                }
                                else {
                                    // 为空或为某值
                                    if (useParameterize) {
                                        if (commonDao.isOracle() || commonDao.isDM()){
                                            sb.append(" and nvl(" + fieldName + ",?)=?");
                                        }
                                        else if (commonDao.isMySql()){
                                            sb.append(" and ifnull(" + fieldName + ",?)=?");
                                        }
                                        else{
                                            sb.append(" and isnull(" + fieldName + ",?)=?");
                                        }
                                        params.add(value);
                                        params.add(value);
                                    }
                                    else {
                                        if (commonDao.isOracle() || commonDao.isDM()){
                                            sb.append(" and nvl(" + fieldName + ",'" + value + "')='" + value + "'");
                                        }
                                        else if (commonDao.isMySql()){
                                            sb.append(" and ifnull(" + fieldName + ",'" + value + "')='" + value + "'");
                                        }
                                        else{
                                            sb.append(" and isnull(" + fieldName + ",'" + value + "')='" + value + "'");
                                        }
                                    }
                                }
                                break;
                            case SqlConstant.SQL_OPERATOR_ISNOTNULL: // 不为空
                                if (commonDao.isOracle() || commonDao.isDM()){
                                    sb.append(" and " + fieldName + " is not null ");
                                }
                                else if (commonDao.isMySql()){
                                    sb.append(" and ifnull(" + fieldName + ",'')!='' ");
                                }
                                else{
                                    sb.append(" and isnull(" + fieldName + ",'')!='' ");
                                }
                                break;
                            default:
                                sb.append(" and " + fieldName + "='" + value + "'");
                                break;
                        }
                    }
                    // 设置排序
                    if (StringUtil.isNotBlank(conditionMap.get("#sort"))) {
                        sb.append(" order by " + conditionMap.get("#sort"));
                        if (commonDao.isOracle() || commonDao.isDM()) {
                            sb.append(" nulls last ");
                        }
                    }
                }
                else {
                    // 兼容旧模式
                    sb.append(buildSqlOld(conditionMap));
                }
            }
        }
        return sb.toString();
    }

    // 根据条件查询实体
    public <T> T getBeanByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        T t = null;
        List<T> dataList = getListByCondition(baseClass, conditionMap);
        if (dataList != null && dataList.size() > 0){
            t = dataList.get(0);
        }
        return t;
    }

    // 根据条件查询List，兼容
    @SuppressWarnings("unchecked")
    public <T> List<T> getListByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        List<T> dataList = new ArrayList<>();
        if(conditionMap == null || conditionMap.size() == 1){
            return dataList;
        }
        if (conditionMap != null && SqlConstant.STR_YES.equals(conditionMap.get("#isnew"))) {
            List<String> params = new ArrayList<>();
            String sql = buildSqlComoplete(baseClass, conditionMap, params);
            Object[] paramsobject = params.toArray();
            dataList = (List<T>) commonDao.findList(sql, baseClass, paramsobject);
        }
        else {
            // 旧模式
            Entity en = baseClass.getAnnotation(Entity.class);
            // 条件sql
            StringBuffer sb = new StringBuffer();
            sb.append(buildSqlOld(conditionMap));
            String sql = "select * from " + en.table() + " where 1=1 " + sb.toString();
            dataList = (List<T>) commonDao.findList(sql, baseClass);
        }

        return dataList;
    }

    // 构造一条完整sql语句
    public String buildSqlComoplete(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        StringBuffer sb = new StringBuffer();
        Entity en = baseClass.getAnnotation(Entity.class);
        sb.append(buildSql(conditionMap));
        String fields = "*";
        String count = "";
        String tableName = en.table();
        if (conditionMap != null) {
            if (conditionMap.containsKey("#fields")) {
                fields = conditionMap.get("#fields");
                if (StringUtil.isBlank(fields)) {
                    fields = "*";
                }
            }
            
            if (StringUtil.isNotBlank(conditionMap.get("#join"))){
            	tableName = getTable(tableName + " a", conditionMap.get("#join"));
            }

            // 查询数目
            if (StringUtil.isNotBlank(conditionMap.get("#count"))) {
                if (commonDao.isMySql()) {
                    sb.append(" limit " + conditionMap.get("#count"));
                }
                else if (commonDao.isSqlserver()) {
                    count = " top " + conditionMap.get("#count");
                }
                else if (commonDao.isOracle() || commonDao.isDM()) {
                    count = " where rownum<=" + conditionMap.get("#count");
                }
            }
        }

        String sqlRecord = "";
        // oracle单独处理
        if (StringUtil.isNotBlank(count) && commonDao.isOracle()) {
            sqlRecord = "select " + fields + " from " + tableName + sb.toString();
            sqlRecord = "select * from (" + sqlRecord + ") childQuery " + count;
        }
        else {
            sqlRecord = "select " + count + " " + fields + " from " + tableName + sb.toString();
        }
        return sqlRecord;
    }

    // 构造一条完整sql语句，参数化方式
    public String buildSqlComoplete(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            List<String> params) {
        if (params == null) {
            System.err.println("params参数不能为null！");
            return "";
        }
        else {
            StringBuffer sb = new StringBuffer();
            Entity en = baseClass.getAnnotation(Entity.class);
            sb.append(buildSql(conditionMap, params));
            String fields = "*";
            String count = "";
            String tableName = en.table();
            if (conditionMap != null) {
                if (conditionMap.containsKey("#fields")) {
                    fields = conditionMap.get("#fields");
                    if (StringUtil.isBlank(fields)) {
                        fields = "*";
                    }
                }
                
                if (StringUtil.isNotBlank(conditionMap.get("#join"))){
                	tableName = getTable(tableName + " a", conditionMap.get("#join"));
                }

                // 查询数目
                if (StringUtil.isNotBlank(conditionMap.get("#count"))) {
                    if (commonDao.isMySql()) {
                        sb.append(" limit " + conditionMap.get("#count"));
                    }
                    else if (commonDao.isSqlserver()) {
                        count = " top " + conditionMap.get("#count");
                    }
                    else if (commonDao.isOracle() || commonDao.isDM()) {
                        count = " where rownum<=" + conditionMap.get("#count");
                    }
                }
            }

            String sqlRecord = "";
            // oracle单独处理
            if (StringUtil.isNotBlank(count) && commonDao.isOracle()) {
                sqlRecord = "select " + fields + " from " + tableName + sb.toString();
                sqlRecord = "select * from (" + sqlRecord + ") childQuery " + count;
            }
            else {
                sqlRecord = "select " + count + " " + fields + " from " + tableName + sb.toString();
            }
            return sqlRecord;
        }
    }

    // 普通的conditionMap,暂时兼容
    private String buildSqlOld(Map<String, String> conditionMap) {
        // 条件sql
        StringBuffer sb = new StringBuffer();
        if (conditionMap != null && conditionMap.size() > 0) {
            for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                String fieldName = entry.getKey().trim().toUpperCase();
                // 如果是“like”开头
                if (fieldName.toUpperCase().endsWith("LIKE")) {
                    fieldName = fieldName.replaceAll("(?i)like$", "");
                    sb.append(" and " + fieldName + " like '%" + entry.getValue() + "%'");
                }
                // 如果是“IN”开头
                else if (fieldName.toUpperCase().endsWith("IN")) {
                    fieldName = fieldName.replaceAll("(?i)in$", "");
                    sb.append(" and " + fieldName + " in (" + entry.getValue() + ")");
                }
                // 如果是link结尾
                else if (fieldName.toUpperCase().endsWith("LINK")) {
                    fieldName = fieldName.replaceAll("(?i)link$", "");
                    sb.append(" and " + fieldName + " " + entry.getValue());
                }
                // 符号直接连接
                else {
                    if (commonDao.isOracle() || commonDao.isDM() && fieldName.endsWith("=") && StringUtil.isBlank(entry.getValue())) {
                        sb.append(" and " + fieldName.replaceAll("(?i)=$", "") + " is null");
                    }
                    else {
                        sb.append(" and " + fieldName + "'" + entry.getValue() + "'");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 
     * 获取列表个数
     * 
     * @param baseClass
     * @param conditionMap
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getListCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        Entity en = baseClass.getAnnotation(Entity.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(buildSql(conditionMap));
        String tableName = en.table();
        if (conditionMap != null) {
            if (StringUtil.isNotBlank(conditionMap.get("#join"))){
            	tableName = getTable(tableName + " a", conditionMap.get("#join"));
            }
        }
        String sql = "select count(*) from " + tableName + sb.toString();
        return commonDao.queryInt(sql);
    }

    /**
     * 处理参数名称
     *  
     *  @param fieldName
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String changeFieldName(String fieldName) {
        String newFieldName = fieldName;
        if (fieldName.contains("+") && commonDao.isOracle() || commonDao.isDM()) {
            newFieldName = fieldName.replace("+", "||");
        }
        else if (fieldName.contains("+") && commonDao.isMySql()) {
            newFieldName = "concat(";
            newFieldName += fieldName.replace("+", ",");
            newFieldName += ")";
        }
        return newFieldName;
    }
    
    public ICommonDao getDao(){
    	return commonDao;
    }
}
