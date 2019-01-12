package com.steven.transaction.core.bootstrap;

import com.steven.transaction.common.config.TransactionConfig;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.InitService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * TransactionBootstrap.
 *
 * @author steven
 */
public class TransactionBootstrap extends TransactionConfig implements ApplicationContextAware{

    private final InitService initService;

    @Autowired
    public TransactionBootstrap(final InitService initService) {
        this.initService = initService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.getInstance().setCfgContext((ConfigurableApplicationContext) applicationContext);
        start(this);
    }

    private void start(final TransactionConfig transactionConfig) {
        initService.initialization(transactionConfig);
    }
}
