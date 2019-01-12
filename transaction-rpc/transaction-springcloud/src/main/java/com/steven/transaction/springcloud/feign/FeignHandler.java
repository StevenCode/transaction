package com.steven.transaction.springcloud.feign;

import com.steven.transaction.annotation.Transaction;
import com.steven.transaction.common.bean.entity.Invocation;
import com.steven.transaction.common.bean.entity.Participant;
import com.steven.transaction.common.context.TransactionContext;
import com.steven.transaction.common.utils.DefaultValueUtils;
import com.steven.transaction.core.concurrent.threadlocal.TransactionContextLocal;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.engine.TransactionEngine;
import feign.Target;

import java.lang.reflect.InvocationHandler;
import feign.InvocationHandlerFactory.MethodHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * FeignHandler.
 *
 * @author steven
 */
public class FeignHandler implements InvocationHandler {

    private Target<?> target;

    private Map<Method, MethodHandler> handlers;

    @Override
    public Object invoke(final Object proxy,final Method method,final Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }else {
            final Transaction transaction = method.getAnnotation(Transaction.class);
            if (Objects.isNull(transaction)) {
                return this.handlers.get(method).invoke(args);
            }
            try {
                TransactionEngine transactionEngine = SpringBeanUtils.getInstance().getBean(TransactionEngine.class);
                final Participant participant = buildParticipant(transaction, method, args);
                if (Objects.nonNull(participant)) {
                    transactionEngine.registerParticipant(participant);
                }
                return this.handlers.get(method).invoke(args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return DefaultValueUtils.getDefaultValue(method.getReturnType());
            }
        }
    }

    private Participant buildParticipant(final Transaction transaction, final Method method, final Object[] args) {
        TransactionContext transactionContext = TransactionContextLocal.getInstance().get();

        Participant participant;
        if (Objects.nonNull(transactionContext)) {
            final Class declaringClass = transaction.target();
            Invocation invocation = new Invocation(declaringClass, method.getName(), method.getParameterTypes(), args);
            final Integer pattern = transaction.pattern().getCode();
            //封装调用点
            participant = new Participant(transactionContext.getTransId(),
                    transaction.destination(),
                    pattern,
                    invocation);
            return participant;
        }
        return null;
    }


    public void setTarget(final Target<?> target) {
        this.target = target;
    }

    public void setHandlers(final Map<Method, MethodHandler> handlers) {
        this.handlers = handlers;
    }
}
