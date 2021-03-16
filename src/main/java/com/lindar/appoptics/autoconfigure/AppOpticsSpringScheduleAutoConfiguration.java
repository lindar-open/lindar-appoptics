package com.lindar.appoptics.autoconfigure;

import com.appoptics.api.ext.Trace;
import com.lindar.appoptics.aspects.ScheduleMonitoringAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@ConditionalOnClass({Trace.class, Scheduled.class})
public class AppOpticsSpringScheduleAutoConfiguration {

    @Bean
    public ScheduleMonitoringAspect scheduleMonitoringAspect(){
        return new ScheduleMonitoringAspect();
    }
}
