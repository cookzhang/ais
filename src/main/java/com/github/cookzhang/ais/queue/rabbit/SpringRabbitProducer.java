package com.github.cookzhang.ais.queue.rabbit;

import com.github.cookzhang.ais.Payload;
import com.github.cookzhang.ais.queue.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * User: zhangyi
 * Date: 3/18/14
 * Time: 17:19
 * Description:
 */
public class SpringRabbitProducer<T extends Payload> implements MessageProducer<T> {

    private RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SpringRabbitProducer.class);

    @Override
    public void send(T t) {

        Message message = new Message(t.serialize(), null);
        rabbitTemplate.send(message);

        logger.debug("发送消息成功！内容为：{}", t);
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

}
