package com.feng.strom.web.config;



import com.feng.strom.base.jackson.JacksonUtil;
import com.feng.strom.base.util.IpUtils;

import com.feng.strom.web.config.systemLog.ApiControllerLog;
import com.feng.strom.web.config.systemLog.SystemControllerLog;
import com.feng.strom.web.config.systemLog.SystemServiceLog;
import com.feng.strom.web.domain.entity.SystemLogDO;
import com.feng.strom.web.service.SystemLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Title: SystemControllerLog
 * @date 2020年1月19日
 * @version V1.0
 * Description: 切点类
 */
@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {
    //注入Service用于把日志保存数据库，实际项目入库采用队列做异步
    @Resource
    private SystemLogService systemLogService;

    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);


    //Controller层切点
    @Pointcut("@annotation(com.feng.strom.web.config.systemLog.SystemControllerLog)")
    public void controllerAspect(){
    }

    //Service层切点
    @Pointcut("@annotation(com.feng.strom.web.config.systemLog.SystemServiceLog)")
    public void serviceAspect(){
    }


    //apiController层切点
    @Pointcut("@annotation(com.feng.strom.web.config.systemLog.ApiControllerLog)")
    public void apiAspect(){
    }

    /**
     * @Description  前置通知  用于拦截Controller层记录方法调用
     * @date 2018年9月3日 10:38
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        SecurityProperties.User user = (SecurityProperties.User) session.getAttribute("user");

        String ip = IpUtils.getIpAddr(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params+= JacksonUtil.obj2String(joinPoint.getArgs()[i])+";";
            }
        }
        try {
            //*========控制台输出=========*//
            logger.info("==============前置通知开始==============");
            logger.info("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            logger.info("方法描述：" + getControllerMethodDescription(joinPoint));
            logger.info("请求人："+(null==user?null:user.getName()));
            logger.info("请求ip："+ip);
            logger.info("请求参数:" + params);
            //*========数据库日志=========*//
            SystemLogDO logDO = new SystemLogDO();
            saveLog(logDO);
        }catch (Exception e){
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}",e.getMessage());
        }
    }

    /**
     * 可获取返回值
     * @Description  后置通知  用于记录API调用
     * @date 2018年9月3日 10:38
     */

    @AfterReturning(value = "apiAspect()",returning ="returnValue")
    public void doAfter(JoinPoint joinPoint,Object returnValue){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        SecurityProperties.User user = (SecurityProperties.User) session.getAttribute("user");

        String ip = IpUtils.getIpAddr(request);

        try {
            //*========控制台输出=========*//
            logger.info("==============API后置通知开始==============");
            logger.info("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            logger.info("方法描述：" + getApiControllerMethodDescription(joinPoint));
            logger.info("请求人："+(null==user?null:user.getName()));//user.getUserName()
            logger.info("请求ip："+ip);
            logger.info("返回结果："+returnValue);

            //*========数据库日志=========*//
            SystemLogDO logDO = new SystemLogDO();
            saveLog(logDO);

        }catch (Exception e){
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}",e.getMessage());
        }
    }

    /**
     * @Description  异常通知 用于拦截service层记录异常日志
     * @date 2018年9月3日 下午5:43
     */
    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        SecurityProperties.User user = (SecurityProperties.User) session.getAttribute("user");
        //获取请求ip
        String ip = IpUtils.getIpAddr(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                //params+= JsonUtils.objectToJson(joinPoint.getArgs()[i])+";";
                params+= JacksonUtil.obj2String(joinPoint.getArgs()[i])+";";
            }
        }
        try{
            /*========控制台输出=========*/
            logger.info("=====异常通知开始=====");
            logger.info("异常代码:" + e.getClass().getName());
            logger.info("异常信息:" + e.getMessage());
            logger.info("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.info("方法描述:" + getServiceMethodDescription(joinPoint));
            logger.info("请求人:" + (null==user?null:user.getName()));
            logger.info("请求IP:" + ip);
            logger.info("请求参数:" + params);
            /*==========数据库日志=========*/
            SystemLogDO logDO = new SystemLogDO();
            saveLog(logDO);
        }catch (Exception ex){
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
    }

    /**
     * 异步保存日志
     * @param logDO
     */
    @Async
    public void saveLog(SystemLogDO logDO){
        //systemLogService.save(logDO);
        System.out.println("异步保存日志!");
    }


    /**
     * @Description  获取注解中对方法的描述信息 用于service层注解
     * @date 2018年9月3日 下午5:05
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint)throws Exception{
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }



    /**
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * API
     * @Description  获取注解中对方法的描述信息 用于API注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getApiControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(ApiControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}