package com.steven.transaction.springboot.starter.parent.configuration;

import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.core.bootstrap.TransactionBootstrap;
import com.steven.transaction.core.service.InitService;
import com.steven.transaction.springboot.starter.parent.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * TransactionAutoConfiguration.
 *
 * @author steven
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@ComponentScan(basePackages = {"com.steven.transaction"})
public class TransactionAutoConfiguration {

    private final ConfigProperties configProperties;

    /**
     * Instantiates a new auto configuration.
     *
     * @param configProperties the config properties
     */
    @Autowired(required = false)
    public TransactionAutoConfiguration(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    /**
     * init TransactionBootstrap.
     *
     * @param initService {@linkplain InitService}
     * @return TransactionBootstrap {@linkplain TransactionBootstrap}
     */
    @Bean
    public TransactionBootstrap tccTransactionBootstrap(final InitService initService) {
        TransactionBootstrap bootstrap = new TransactionBootstrap(initService);
        bootstrap.builder(builder());
        return bootstrap;
    }

    /**
     * init bean of TransactionConfig
     *
     * @return {@linkplain TransactionConfig}
     */
    @Bean
    public TransactionConfig transactionConfig() {
        return builder().build();
    }

    private TransactionConfig.Builder builder() {
        return TransactionBootstrap.create()
                .setSerializer(configProperties.getSerializer())
                .setRepositorySuffix(configProperties.getRepositorySuffix())
                .setRepositorySupport(configProperties.getRepositorySupport())
                .setNeedRecover(configProperties.getNeedRecover())
                .setBufferSize(configProperties.getBufferSize())
                .setScheduledThreadMax(configProperties.getScheduledThreadMax())
                .setScheduledDelay(configProperties.getScheduledDelay())
                .setRetryMax(configProperties.getRetryMax())
                .setRecoverDelayTime(configProperties.getRecoverDelayTime())
                .setDbConfig(configProperties.getDbConfig())
                .setFileConfig(configProperties.getFileConfig())
                .setMongoConfig(configProperties.getMongoConfig())
                .setRedisConfig(configProperties.getRedisConfig())
                .setZookeeperConfig(configProperties.getZookeeperConfig());
    }
}
