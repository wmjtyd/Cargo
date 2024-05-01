package main.config;

import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableScheduling
public class AppConfig {
    @Bean
    public SolverFactory vehicleRoutingSolverFactory() {
        return SolverFactory.createFromXmlResource("vehicleRoutingSolverConfig.xml");
    }
    @Bean
    public SolverFactory pathSolverFactory() {
        return SolverFactory.createFromXmlResource("path.xml");
    }

    @Bean
    public SolverFactory cargoSolverFactory() {
        return SolverFactory.createFromXmlResource("cargo.xml");
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
