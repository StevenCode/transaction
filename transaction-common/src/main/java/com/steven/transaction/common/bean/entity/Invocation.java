package com.steven.transaction.common.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Invocation.
 *
 * @author steven
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invocation implements Serializable {

    private static final long serialVersionUID = -5108578223428529356L;

    @Getter
    private Class targetClass;

    @Getter
    private String methodName;

    @Getter
    private Class[] parameterTypes;

    @Getter
    private Object[] args;
}
