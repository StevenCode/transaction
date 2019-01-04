package com.steven.transaction.common.config;

import lombok.Data;

/**
 * FileConfig.
 *
 * @author steven
 */
@Data
public class FileConfig {

    /**
     * 文件保存路径
     */
    private String path;

    /**
     * 文件前缀
     */
    private String prefix;
}
