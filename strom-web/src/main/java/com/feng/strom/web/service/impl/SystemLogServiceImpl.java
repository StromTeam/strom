package com.feng.strom.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.strom.web.domain.entity.SystemLogDO;
import com.feng.strom.web.mapper.SystemLogMapper;
import com.feng.strom.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author
 * @version 1.0
 * @date 2020/1/19 11:44
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLogDO> implements SystemLogService {

    @Autowired
    SystemLogService systemLogService;

    /**
     * 保存系统操作日志
     * @param logDO
     */
    public void saveSystemLog(SystemLogDO logDO){
         //systemLogService.save(logDO);
    }

}
