
package com.feng.strom.base.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * kafka消息发送结果回调
 * @author
 * @version 1.0
 * @date 2020/1/17 15:02
 */
@Slf4j
@Component
public class KafkaSendResultHandler implements ProducerListener {


    /**
     * 发送消息成功后调用
     */
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Message send success : " + producerRecord.toString());
    }

    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
        log.info("Message send error : " + producerRecord.toString());
    }
    /**
     * 是否开启发送监听
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean isInterestedInSuccess() {
        return true;
    }
}
