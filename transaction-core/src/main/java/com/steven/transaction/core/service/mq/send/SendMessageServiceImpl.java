package com.steven.transaction.core.service.mq.send;

import com.steven.transaction.common.bean.entity.Participant;
import com.steven.transaction.common.bean.entity.Transaction;
import com.steven.transaction.common.bean.mq.MessageEntity;
import com.steven.transaction.common.serializer.ObjectSerializer;
import com.steven.transaction.core.helper.SpringBeanUtils;
import com.steven.transaction.core.service.MqSendService;
import com.steven.transaction.core.service.SendMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * SendMessageServiceImpl.
 *
 * @author steven
 */
@Service("mythSendMessageService")
public class SendMessageServiceImpl implements SendMessageService {

    private volatile ObjectSerializer serializer;

    private volatile MqSendService mqSendService;

    @Override
    public Boolean sendMessage(Transaction transaction) {
        if (Objects.isNull(transaction)) {
            return false;
        }
        final List<Participant> participants = transaction.getParticipants();
        /*
         * 这里的这个判断很重要，不为空，表示本地的方法执行成功，需要执行远端的rpc方法
         * 为什么呢，因为我会在切面的finally里面发送消息，意思是切面无论如何都需要发送mq消息
         * 那么考虑问题，如果本地执行成功，调用rpc的时候才需要发
         * 如果本地异常，则不需要发送mq ，此时mythParticipants为空
         */
        if (CollectionUtils.isNotEmpty(participants)) {
            for (Participant participant : participants) {
                MessageEntity messageEntity = new MessageEntity(participant.getTransId(), participant.getInvocation());
                try {
                    final byte[] message = getObjectSerializer().serialize(messageEntity);
                    getMqSendService().sendMessage(participant.getDestination(),participant.getPattern(),message);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.FALSE;
                }
            }
        }
        return null;
    }

    private synchronized MqSendService getMqSendService() {
        if (mqSendService == null) {
            synchronized (SendMessageServiceImpl.class) {
                if (mqSendService == null) {
                    mqSendService = SpringBeanUtils.getInstance().getBean(MqSendService.class);
                }
            }
        }
        return mqSendService;
    }

    private synchronized ObjectSerializer getObjectSerializer() {
        if (serializer == null) {
            synchronized (SendMessageServiceImpl.class) {
                if (serializer == null) {
                    serializer = SpringBeanUtils.getInstance().getBean(ObjectSerializer.class);
                }
            }
        }
        return serializer;
    }
}
