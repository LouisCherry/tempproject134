package com.epoint.knowledge.oumanage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.oumanage.inter.ICnsKinfo;

import oracle.sql.CHAR;


/**
 * 
 *  知识库信息实现接口类
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoImpl extends CnsCommonImpl<CnsKinfo> implements ICnsKinfo
{

    @Override
    public List<CnsKinfo> getUneffectiveKinfoByTime(String date) {
        String sql = "select * from cns_kinfo where  date_format(EFFECTDATE,'%Y-%m-%d') <'" + date + "'AND ISENABLE=1";
        return commonDao.findList(sql, CnsKinfo.class);
    }

    @Override
    public List<CnsKinfo> getReadByCondition(Map<String, String> conditionMap, String date, Integer first,
            Integer pageSize) {
        String sql = "select * from CNS_KINFO where 1=1 and ISENABLE='1' and ISDEL='0' and KSTATUS='30' and (date_format(effectdate,'%Y-%m-%d %H:%i:%s')>='"
                + date + "' or effectdate is null)  ";
        StringBuffer sb = new StringBuffer("");
        if (conditionMap != null && conditionMap.size() > 0) {
            for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                String fieldName = entry.getKey().trim();
                if (!(fieldName.endsWith("=") || fieldName.endsWith(">") || fieldName.endsWith("<"))) {
                    fieldName = fieldName.toUpperCase();
                }
                // 如果是'='开头
                if (fieldName.endsWith("=") || fieldName.endsWith(">") || fieldName.endsWith("<")) {
                    if (entry.getValue().toLowerCase().startsWith("to_date")) {
                        sb.append(" and " + fieldName + " " + entry.getValue() + " ");
                    }
                    else {
                        sb.append(" and " + fieldName + "'" + entry.getValue() + "'");
                    }
                }
                // 如果是'like'结尾
                else if (fieldName.endsWith("LIKE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 4);
                    if (entry.getValue().startsWith("%")) {
                        sb.append(" and " + fieldName + " like '" + entry.getValue() + "'");
                    }
                    else {
                        sb.append(" and " + fieldName + " like '%" + entry.getValue() + "%'");
                    }
                }
                // 如果是'IN'结尾
                else if (fieldName.endsWith("IN")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + " in (" + entry.getValue() + ")");
                }
                // 如果是'GT'结尾，代表大于，即greater than
                else if (fieldName.endsWith("GT")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + "> '" + entry.getValue() + "'");
                }
                // 如果是'GTE'结尾，代表大于等于，即greater than Or equal to
                else if (fieldName.endsWith("GTE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 3);
                    sb.append(" and " + fieldName + ">= '" + entry.getValue() + "'");
                }
                // 如果是'LT'结尾，代表小于，即greater than
                else if (fieldName.endsWith("LT")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + "< '" + entry.getValue() + "'");
                }
                // 如果是'LTE'结尾，代表小于等于，即less than Or equal to
                else if (fieldName.endsWith("LTE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 3);
                    sb.append(" and " + fieldName + "<= '" + entry.getValue() + "'");
                }
                //自己定义sql语句,支持 
                else {
                    sb.append(" and " + fieldName + " " + entry.getValue());
                }

            }
        }
        sql = sql + sb.toString() + " order by CREATDATE desc";
        
        String str="and date_format(begindate,'%y-%m-%d')";
        
        String s= "and to_char(begindate,'yyyy-mm-dd')";
        
        String  a = "123456";
        //system.out.println(a.replace("3", "6")+"--a--"+a);
        //system.out.println(sql.indexOf(s));
        
        sql = sql .replaceAll("to_char", "date_format");
        sql=sql.replaceAll("yyyy-mm-dd", "%y-%m-%d");
        //system.out.println(sql+"---------sql");
        List<CnsKinfo> dataList = new ArrayList<>();
        //pagesize不为0，则进行分页
        if (pageSize != 0) {
            dataList = (List<CnsKinfo>) commonDao.findList(sql, first, pageSize, CnsKinfo.class);
        }
        else {
            dataList = (List<CnsKinfo>) commonDao.findList(sql, CnsKinfo.class);
        }
        return dataList;
    }

    @Override
    public Integer getReadCountByCondition(Map<String, String> conditionMap, String date) {
        String sql = "select count(1) from CNS_KINFO where 1=1 and ISENABLE='1' and ISDEL='0' and KSTATUS='30' and (date_format(effectdate,'%Y-%m-%d %h:%i:%s')>='"
                + date + "' or effectdate is null)  ";
        StringBuffer sb = new StringBuffer("");
        if (conditionMap != null && conditionMap.size() > 0) {
            for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                String fieldName = entry.getKey().trim();
                if (!(fieldName.endsWith("=") || fieldName.endsWith(">") || fieldName.endsWith("<"))) {
                    fieldName = fieldName.toUpperCase();
                }
                // 如果是'='开头
                if (fieldName.endsWith("=") || fieldName.endsWith(">") || fieldName.endsWith("<")) {
                    if (entry.getValue().toLowerCase().startsWith("to_date")) {
                        sb.append(" and " + fieldName + " " + entry.getValue() + " ");
                    }
                    else {
                        sb.append(" and " + fieldName + "'" + entry.getValue() + "'");
                    }
                }
                // 如果是'like'结尾
                else if (fieldName.endsWith("LIKE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 4);
                    if (entry.getValue().startsWith("%")) {
                        sb.append(" and " + fieldName + " like '" + entry.getValue() + "'");
                    }
                    else {
                        sb.append(" and " + fieldName + " like '%" + entry.getValue() + "%'");
                    }
                }
                // 如果是'IN'结尾
                else if (fieldName.endsWith("IN")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + " in (" + entry.getValue() + ")");
                }
                // 如果是'GT'结尾，代表大于，即greater than
                else if (fieldName.endsWith("GT")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + "> '" + entry.getValue() + "'");
                }
                // 如果是'GTE'结尾，代表大于等于，即greater than Or equal to
                else if (fieldName.endsWith("GTE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 3);
                    sb.append(" and " + fieldName + ">= '" + entry.getValue() + "'");
                }
                // 如果是'LT'结尾，代表小于，即greater than
                else if (fieldName.endsWith("LT")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 2);
                    sb.append(" and " + fieldName + "< '" + entry.getValue() + "'");
                }
                // 如果是'LTE'结尾，代表小于等于，即less than Or equal to
                else if (fieldName.endsWith("LTE")) {
                    fieldName = fieldName.substring(0, fieldName.length() - 3);
                    sb.append(" and " + fieldName + "<= '" + entry.getValue() + "'");
                }
                //自己定义sql语句,支持 
                else {
                    sb.append(" and " + fieldName + " " + entry.getValue());
                }

            }
        }
        sql = sql + sb.toString();
        sql = sql .replaceAll("to_char", "date_format");
        sql=sql.replaceAll("yyyy-mm-dd", "%y-%m-%d");
        //sql=sql.replaceAll("hh", "%y-%m-%d");
        return commonDao.find(sql, Integer.class);
    }
}
