package com.steven.transaction.springcloud.interceptor;

import com.steven.transaction.common.constant.CommonConstant;
import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.enums.RoleEnum;
import com.steven.transaction.common.utils.GsonUtils;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.interceptor.TransactionInterceptor;
import com.steven.transaction.core.service.TransactionAspectService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * SpringCloudTransactionInterceptor.
 *
 * @author steven
 */
@Component
public class SpringCloudTransactionInterceptor implements TransactionInterceptor {

    private final TransactionAspectService transactionAspectService;

    @Autowired
    public SpringCloudTransactionInterceptor(final TransactionAspectService transactionAspectService) {
        this.transactionAspectService = transactionAspectService;
    }

    @Override
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        TransactionContext transactionContext = TransactionContextLocal.getInstance().get();
        if (Objects.nonNull(transactionContext)
                && transactionContext.getRole() == RoleEnum.LOCAL.getCode()) {
            transactionContext = TransactionContextLocal.getInstance().get();
        }else {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String context = request.getHeader(CommonConstant.TRANSACTION_CONTEXT);
            if (StringUtils.isNoneBlank(context)) {
                transactionContext = GsonUtils.getInstance().fromJson(context, TransactionContext.class);
            }
        }
        return transactionAspectService.invoke(transactionContext, pjp);
    }
}
