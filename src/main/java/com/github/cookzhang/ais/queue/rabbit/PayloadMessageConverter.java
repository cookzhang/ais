package com.github.cookzhang.ais.queue.rabbit;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * User: zhangyi
 * Date: 4/7/14
 * Time: 20:44
 * Description:
 */
public class PayloadMessageConverter implements MessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(PayloadMessageConverter.class);

    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        HessianSerializerOutput hessianOutput = new HessianSerializerOutput(bout);
        try {
            hessianOutput.writeObject(object);
            return new Message(bout.toByteArray(), messageProperties);
        } catch (IOException e) {
            logger.error("message convert to message error:{}", e);
        }

        return new Message(new byte[0], messageProperties);
    }

    /**
     * Convert from a Message to a Java object.
     *
     * @param message the message to convert
     * @return the converted Java object
     * @throws org.springframework.amqp.support.converter.MessageConversionException in case of conversion failure
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        byte[] body = message.getBody();
        ByteArrayInputStream bin = new ByteArrayInputStream(body);
        HessianSerializerInput hessianInput = new HessianSerializerInput(bin);
        try {
            return hessianInput.readObject();
        } catch (IOException e) {
            logger.error("message convert from message error:{}", e);
        }

        return null;
    }
}
