package com.steven.transaction.common.context;

import lombok.Data;

import java.io.Serializable;

/**
 * TransactionContext.
 *
 * @author steven
 */
@Data
public class TransactionContext implements Serializable {

    private static final long serialVersionUID = -5289080166922118073L;

    private String transId;

    /**
     * role. {@linkplain com.steven.transaction.common.enums.RoleEnum}
     */
    private int role;
}
