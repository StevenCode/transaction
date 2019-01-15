package com.steven.transaction.springcloud.feign;

import feign.Feign;
import feign.InvocationHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * RestTemplateConfiguration.
 *
 * @author steven
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new RestTemplateInterceptor())
                .invocationHandlerFactory(invocationHandlerFactory());
    }

    @Bean
    public InvocationHandlerFactory invocationHandlerFactory() {
        return ((target, dispatch) -> {
            FeignHandler feignHandler = new FeignHandler();
            feignHandler.setTarget(target);
            feignHandler.setHandlers(dispatch);
            return feignHandler;
        });
    }
}
