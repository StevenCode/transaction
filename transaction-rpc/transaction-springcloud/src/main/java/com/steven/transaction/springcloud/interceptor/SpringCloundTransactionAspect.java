package com.steven.transaction.springcloud.interceptor;

import com.steven.transaction.core.interceptor.AbstractTransactionAspect;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * SpringCloundTransactionAspect.
 *
 * @author steven
 */
@Aspect
@Component
public class SpringCloundTransactionAspect extends AbstractTransactionAspect implements Ordered {


    @Autowired
    public SpringCloundTransactionAspect(final SpringCloundTransactionAspect springCloundTransactionAspect) {
        this.setTransactionInterceptor(springCloundTransactionAspect);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
