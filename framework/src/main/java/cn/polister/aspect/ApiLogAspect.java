package cn.polister.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    @Pointcut("execution(* cn.polister.controller..*.*(..))")
    void pt() {
    }

    @Around("pt()")
    public Object ApiLogNote(ProceedingJoinPoint pjp) throws Throwable {

        Object proceed = null;
        try {
            // 打印入口日志
            printInLog(pjp);
            proceed = pjp.proceed();
            return proceed;
        } finally {
            printOutLog(pjp);

        }
    }

    private void printOutLog(ProceedingJoinPoint pjp) {
        log.info("finish running");
    }

    private void printInLog(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        log.info("==========START=========");
        log.info("time: {}", new Date());
        log.info("from: {}", request.getRemoteHost());
        log.info("method: {}", request.getMethod());
        log.info("name: {}", signature.getMethod());
        log.info("===========END==========");

    }
}
