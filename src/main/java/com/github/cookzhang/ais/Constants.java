package com.github.cookzhang.ais;

/**
 * User: zhangyi
 * Modify:lubh
 * Date: 4/5/14
 * Time: 19:59
 * Description:
 */
public interface Constants {

    public String GEARMAN_CONFIG_FILE = "gearman";
    public String SYSTEM_CONFIG_FILE = "ais";
    public String CACHE_CONFIG_FILE = "cache";
    public String QUEUE_CONFIG_FILE = "queue";
    public String JDBC_CONFIG_FILE = "jdbc";

    //offline相关参数
    public String OFFLINE_SWITCH = "offline.switch";
    public String OFFLINE_ERROR_THRESHOLD = "offline.error.threshold";
    public String OFFLINE_RESTORE_THRESHOLD = "offline.restore.threshold";

    public String RABBIT_QUEUE_NAME = "rabbitmq.queue.name";
    public String RABBIT_QUEUE_MONITOR_THRESHOLD = "rabbitmq.queue.monitor.threshold";

    public String INVOKER_THREAD_POOL_SIZE = "invoker.threadPool.size";
    public String FLUSH_THREAD_POOL_SIZE = "flush.threadPool.size";

    //排重保留样本量
    public String MERGE_SPECIMEN = "merge.specimen";

    //排重误报比率
    public String MERGE_REPORT_MISTAKE = "merge.report.mistake";

    //排重URL请求间隔
    public String MERGE_INTERVAL = "merge.interval";

    //redis config
    public String REDIS_HOST = "redis.host";
    public String REDIS_PORT = "redis.port";
    public String REDIS_MAXACTIVE = "redis.maxActive";
    public String REDIS_MAXIDLE = "redis.maxIdle";
    public String REDIS_MAXWAITMILLIS = "redis.maxWaitMillis";
    public String REDIS_TESTONBORROW = "redis.testOnBorrow";


    //ehCache相关
    public String CACHE_NAME = "ehCache.name";

    //queue config
    public String RABBIT_HOST = "rabbitmq.host";
    public String RABBIT_PORT = "rabbitmq.port";
    public String RABBIT_QUEUE_EXCHANGE = "rabbitmq.queue.exchange";
    public String RABBIT_QUEUE_ROUTINGKEY = "rabbitmq.queue.routingKey";

    //数据库相关参数
    public String DB_DRIVERCLASSNAME = "db.driverClassName";
    public String DB_URL = "db.url";
    public String DB_USERNAME = "db.username";
    public String DB_PASSWORD = "db.password";


    //Gearman相关参数
    public String GEARMAN_HOST = "gearman.host";
    public String GEARMAN_PORT = "gearman.port";

    //全局数据库连接池
    public String GLOBAL_FRONT_THREAD_POOL_SIZE = "global.front.threadPool.size";
    public String GLOBAL_BACKGROUND_THREAD_POOL_SIZE = "global.background.threadPool.size";

    //Handler 执行超时
    public String HANDLER_TIMEOUT = "handler.timeout";

    //Async 异步刷新缓存的执行超时时长
    public String ASYNC_FLUSH_TIMEOUT = "async.flush.timeout";

    //Async 异步发布消息队列的执行超时时长
    public String ASYNC_ENQUEUE_TIMEOUT = "async.enqueue.timeout";
}
