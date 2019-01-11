package com.steven.transaction.common.bean.mq;

import com.steven.transaction.common.bean.entity.Invocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MessageEntity.
 *
 * @author steven
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    /**
     * 事务id.
     */
    private String transId;

    /**
     * 执行器.
     */
    private Invocation invocation;
}
