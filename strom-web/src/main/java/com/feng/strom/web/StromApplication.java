package com.feng.strom.web;
/**
 * @ProjectName
 * @Description:
 * @Date 2020/1/15 11:28
 * @author
 * @version 1.0
 */

import com.feng.strom.base.util.SpringContextHolder;
import com.feng.strom.base.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication(scanBasePackages = StromApplication.ROOT_PACKAGE_NAME)
public class StromApplication {
    public static final String ROOT_PACKAGE_NAME = "com.feng.strom";

    public static void main(String[] args) {
        new SpringApplication(StromApplication.class).run(args);
        log.info("\n----------------------------------------------------------\n\t" +
                "Jimi ISD Web启动成功!\n\t" +
                "当前系统时区: \t\t" + DateUtil.getTimeZoneId() + "\n\t" +
                "profiles.active: \t" + SpringContextHolder.getActiveProfile() + "\n\t" +
                "----------------------------------------------------------");
    }
}
