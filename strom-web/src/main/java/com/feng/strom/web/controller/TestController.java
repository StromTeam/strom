

package com.feng.strom.web.controller;
/**
 * @ProjectName
 * @Description:
 * @Date 2020/1/15 11:47
 * @author
 * @version 1.0
 */

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feng.strom.base.util.RedisUtil;
import com.feng.strom.web.domain.entity.UserDO;
import com.feng.strom.web.service.ITestService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/test")
@DefaultProperties(defaultFallback = "defaultFail")

public class TestController {

    private RedisUtil redisUtil;

    private ITestService testService;
    @GetMapping("/redis")
    public String testRedis() {
        redisUtil.set("test:name", "test", 100);
        return String.valueOf(redisUtil.get("test:name"));
    }

    @HystrixCommand(fallbackMethod = "fail1")
    @GetMapping("/hystrix1")
    public String testHystrix1() {
        throw new RuntimeException();
    }

    private String fail1() {
        log.info("fail1");
        return "fail1";
    }

    @HystrixCommand(fallbackMethod = "fail2")
    @GetMapping("/hystrix2")
    public String testHystrix2() {
        throw new RuntimeException();
    }

    @HystrixCommand
    private String fail2() {
        log.info("fail2");
        throw new RuntimeException();
    }

    private String defaultFail() {
        log.info("defaultFail");
        return "defaultFail";
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody UserDO userDO) {
        testService.save(userDO);
    }

    @Cacheable(value = "test",key ="getMethodName()")
    @GetMapping("/user/{id}")
    public UserDO getUser(@PathVariable("id") Integer id) {
        return testService.getById(id);
    }

    /**
     * excel 导出测试
     * 文档地址:https://alibaba-easyexcel.github.io/index.html
     *
     * @author
     * @Date 2020/1/17 11:37
     */
    @GetMapping("/user/exportXls")
    public void exportXls(UserDO userDO, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<UserDO> query = new LambdaQueryWrapper();
        query.gt(UserDO::getId,userDO.getId());
        List<UserDO> list = testService.list(query);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserDO.class).sheet("模板").doWrite(list);
    }
}
