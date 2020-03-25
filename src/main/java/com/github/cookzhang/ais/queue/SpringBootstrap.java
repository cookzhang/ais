package com.github.cookzhang.ais.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: zhangyi
 * Date: 3/18/14
 * Time: 17:33
 * Description: Consumer启动入口
 */
public class SpringBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootstrap.class);

    public static void main(String[] args) {
        logger.info("消息队列消费者开始启动......");
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-*.xml");
        logger.info("spring 配置文件加载完毕！");
        MessageConsumer consumer = context.getBean(MessageConsumer.class);
        logger.info("从spring 获取队列消费者bean完成！");
        logger.info("开始消费队列消息......");
        consumer.receive();
    }
}
