package com.steven.transaction.springcloud.feign;

import com.steven.transaction.common.constant.CommonConstant;
import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.utils.GsonUtils;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * RestTemplateInterceptor.
 *
 * @author steven
 */
public class RestTemplateInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        TransactionContext transactionContext = TransactionContextLocal.getInstance().get();
        requestTemplate.header(CommonConstant.TRANSACTION_CONTEXT,
                GsonUtils.getInstance().toJson(transactionContext));
    }
}
