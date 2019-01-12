package com.steven.transaction.common.bean.entity;

import com.google.common.collect.Lists;
import com.steven.transaction.common.utils.IdWorkerUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Transaction.
 *
 * @author steven
 */
@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = -6792063780987394917L;

    /**
     * 事务id.
     */
    private String transId;

    /**
     * 事务状态. {@linkplain com.steven.transaction.common.enums.StatusEnum}
     */
    private int status;

    /**
     * 事务类型. {@linkplain com.steven.transaction.common.enums.RoleEnum}
     */
    private int role;

    /**
     * 重试次数.
     */
    private volatile int retriedCount;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 更新时间.
     */
    private Date lastTime;

    /**
     * 版本号 乐观锁控制.
     */
    private Integer version = 1;

    /**
     * 调用接口名称.
     */
    private String targetClass;

    /**
     * 调用方法名称.
     */
    private String targetMethod;

    /**
     * 调用错误信息.
     */
    private String errorMsg;

    /**
     * 参与协调的方法合集.
     */
    private List<Participant> participants;

    public Transaction() {
        this.transId = IdWorkerUtils.getInstance().createUUID();
        this.createTime = new Date();
        this.lastTime = new Date();
        participants = Lists.newCopyOnWriteArrayList();
    }


    public Transaction(final String transId) {
        this.transId = transId;
        this.createTime = new Date();
        this.lastTime = new Date();
        participants = Lists.newCopyOnWriteArrayList();
    }

    /**
     * add participant.
     * @param participant
     */
    public void registerParticipant(final Participant participant) {
        participants.add(participant);
    }
}

