package com.healthMonitor.fall2020.orm;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL工具类
 *
 * @author L
 */
public abstract class SqlUtil {

    public static void main(String[] args) throws Exception {
//		System.out.println(StringUtils.containsIgnoreCase("select * from ", "SELECT "));
        System.out.println(trimOrderBy("(SELECT * FROM abc ORDER BY a) UNION (SELECT * FROM xyz ORDER BY x)"));
    }

    public static final String trimOrderBy(String sql) {
        if (StringUtils.contains(sql, "order by")) {
            String orderByRightStr = StringUtils.substringAfterLast(sql, "order by");
            if (!StringUtils.containsIgnoreCase(orderByRightStr, "SELECT ")
                    && !StringUtils.contains(orderByRightStr, ")")) {
                return StringUtils.substringBeforeLast(sql, "order by");
            }
        } else if (StringUtils.contains(sql, "ORDER BY")) {
            String orderByRightStr = StringUtils.substringAfterLast(sql, "ORDER BY");
            if (!StringUtils.containsIgnoreCase(orderByRightStr, "SELECT ")
                    && !StringUtils.contains(orderByRightStr, ")")) {
                return StringUtils.substringBeforeLast(sql, "ORDER BY");
            }
        }
        return sql;
    }

    /**
     * 拼接IN条件值
     *
     * @param strArr
     * @return
     */
    public static String getInCond(Object[] strArr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArr.length; i++) {
            if (i == strArr.length - 1) {
                sb.append("'" + strArr[i] + "'");
            } else {
                sb.append("'" + strArr[i] + "'" + ",");
            }
        }
        return sb.toString();
    }
}
