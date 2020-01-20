
package com.feng.strom.web.controller;

import com.feng.strom.base.config.kafka.KafkaSendResultHandler;
import com.feng.strom.base.constants.KafkaConsts;
import com.feng.strom.base.util.DateUtil;
import com.feng.strom.web.config.systemLog.SystemControllerLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * @author
 * @version 1.0
 * @date 2020/1/16 9:32
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Resource
    private KafkaSendResultHandler producerListener;


    /**
     * 消息测试
     * @return
     */
    @GetMapping("/sendMsg")
    public String sendMsg(){
        String msg = "测试测试测试测试测试测试测试测试测试"+ DateUtil.nowTimeMillis();
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send(KafkaConsts.TOPIC_TEST,"test",msg);
        return "SUUCEES";
    }


    /**
     * 事务测试
     * @throws InterruptedException
     */
    @GetMapping("/msgTransactional")
    @Transactional(rollbackFor = Exception.class)
    @SystemControllerLog(description = "kafka事务测试")
    public void msgTransactional(){
        log.info("事务测试---开始");
        String msg = "事务测试  事务测试";
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send(KafkaConsts.TOPIC_TEST,"test",msg);
        throw new RuntimeException("fail");
    }

    @KafkaListener(topics = KafkaConsts.TOPIC_TEST,containerFactory = "ackContainerFactory")
    public void consumerBatch(String message, Acknowledgment acknowledgment){
        try {
            log.info("收到消息1: {}", message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info("手动提交 offset");
            acknowledgment.acknowledge();
        }
    }


}
