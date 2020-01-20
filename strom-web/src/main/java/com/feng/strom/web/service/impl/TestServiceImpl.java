package com.feng.strom.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.strom.web.domain.entity.UserDO;
import com.feng.strom.web.mapper.UserMapper;
import com.feng.strom.web.service.ITestService;
import org.springframework.stereotype.Service;

/**
 * @author
 * @Date 2020/1/16 14:17
 */
@Service
public class TestServiceImpl extends ServiceImpl<UserMapper, UserDO> implements ITestService {
}
