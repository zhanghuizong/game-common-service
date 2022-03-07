package com.bitgame.game.framework.config;

import com.base.job.core.executor.impl.JobSpringExecutor;
import com.base.job.core.handler.impl.HttpJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(HttpJobHandler.class)
public class JobConfig {
    @Value("${job.enabled:false}")
    private Boolean enabled;

    @Value("${job.admin.url:}")
    private String url;

    @Value("${job.service.name:}")
    private String appName;

    @Value("${job.executor.ip:}")
    private String ip;

    @Value("${job.executor.port}")
    private int port;

    @Value("${job.accessToken:}")
    private String accessToken;

    @Value("${job.executor.logpath}")
    private String logPath;

    @Value("${job.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public JobSpringExecutor jobExecutor() {
        if (enabled) {
            log.info(">>>>>>>>>>> job config init...");
            JobSpringExecutor jobSpringExecutor = new JobSpringExecutor();
            jobSpringExecutor.setAdminAddresses(url);
            jobSpringExecutor.setAppName(appName);
            jobSpringExecutor.setIp(ip);
            jobSpringExecutor.setPort(port);
            jobSpringExecutor.setAccessToken(accessToken);
            jobSpringExecutor.setLogPath(logPath);
            jobSpringExecutor.setLogRetentionDays(logRetentionDays);
            return jobSpringExecutor;
        }
        return null;
    }
}
