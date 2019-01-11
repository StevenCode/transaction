package com.steven.transaction.common.utils;

import com.steven.transaction.common.constant.CommonConstant;

/**
 * DbTypeUtils.
 *
 * @author steven
 */
public class DbTypeUtils {

    public static String buildByDriverClassName(final String driverClassName) {
        String dbType = "mysql";
        if (driverClassName.contains(CommonConstant.DB_MYSQL)) {
            dbType = "mysql";
        } else if (driverClassName.contains(CommonConstant.DB_SQLSERVER)) {
            dbType = "sqlserver";
        } else if (driverClassName.contains(CommonConstant.DB_ORACLE)) {
            dbType = "oracle";
        }
        return dbType;
    }
}
