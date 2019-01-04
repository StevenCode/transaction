package com.steven.transaction.common.config;

import lombok.Data;

/**
 * ZookeeperConfig.
 *
 * @author steven
 */
@Data
public class ZookeeperConfig {

    private String host;

    private int sessionTimeOut = 1000;

    private String rootPath = "/transaction";
}
