package com.steven.transaction.common.constant;

/**
 * CommonConstant.
 *
 * @author steven
 */
public interface CommonConstant {

    String DB_MYSQL = "mysql";

    String DB_SQLSERVER = "sqlserver";

    String DB_ORACLE = "oracle";

    String PATH_SUFFIX = "/transaction";

    String DB_SUFFIX = "transaction_";

    String RECOVER_REDIS_KEY_PRE = "transaction:%s";

    String TRANSACTION_CONTEXT = "TRANSACTION_CONTEXT";

    String TOPIC_TAG_SEPARATOR = ",";

    int SUCCESS = 1;

    int ERROR = 0;
}
