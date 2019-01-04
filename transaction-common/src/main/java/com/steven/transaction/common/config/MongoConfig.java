package com.steven.transaction.common.config;

import lombok.Data;

/**
 * MongoConfig.
 *
 * @author steven
 */
@Data
public class MongoConfig {

    /**
     * mongo数据库设置.
     */
    private String mongoDbName;

    /**
     * mongo数据库URL.
     */
    private String mongoDbUrl;

    /**
     * mongo数据库用户名.
     */
    private String mongoUserName;

    /**
     * mongo数据库密码
     */
    private String mongoUserPwd;
}
