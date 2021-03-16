package com.lindar.appoptics.aspects;

import com.appoptics.api.ext.AgentChecker;
import com.appoptics.api.ext.Trace;
import com.appoptics.api.ext.TraceEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
public class ScheduleMonitoringAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        AgentChecker.waitUntilAgentReady(5, TimeUnit.SECONDS);

        String packageName = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        String transactionName = pjp.getSignature().getDeclaringTypeName() + "_" + pjp.getSignature().getName();
        log.debug("spring scheduled starting, {}", transactionName);
        TraceEvent startEvent = Trace.startTrace("spring-scheduled");
        startEvent.addInfo("Class", packageName);
        startEvent.addInfo("Method", methodName);
        startEvent.report();
        Trace.setTransactionName(transactionName);
        Object proceed;

        try {
            proceed = pjp.proceed();
        } catch (Exception e) {
            Trace.logException(e);
            throw e;
        } finally {
            Trace.endTrace("spring-scheduled");
        }
        log.debug("spring scheduled finished, {}", transactionName);
        return proceed;
    }
}
