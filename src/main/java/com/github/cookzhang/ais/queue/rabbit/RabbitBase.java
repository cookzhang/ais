package com.github.cookzhang.ais.queue.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * User: zhangyi
 * Date: 5/4/14
 * Time: 15:55
 * Description:
 */
public class RabbitBase {

    protected Channel channel;
    protected ConnectionFactory factory;
    protected Connection connection;

    public RabbitBase(String host, int port) throws IOException {

        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        connection();
    }
    public void connection() throws IOException{
        connection = factory.newConnection();
        channel = connection.createChannel();
    }
    public final Channel getChannel() {
        return channel;
    }

    public void close() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
