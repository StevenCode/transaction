package com.steven.transaction.springboot.starter.parent.config;

import com.steven.transaction.common.config.TransactionConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ConfigProperties.
 *
 * @author steven
 */
@Component
@ConfigurationProperties(prefix = "com.steven.transaction")
public class ConfigProperties extends TransactionConfig {
}
