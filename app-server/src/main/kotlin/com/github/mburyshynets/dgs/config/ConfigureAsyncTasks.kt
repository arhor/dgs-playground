package com.github.mburyshynets.dgs.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.slf4j.MDC
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor
import org.springframework.web.context.request.RequestContextHolder
import java.util.concurrent.Executor

@EnableAsync
@Configuration(proxyBeanMethods = false)
class ConfigureAsyncTasks : AsyncConfigurer {

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return SimpleAsyncUncaughtExceptionHandler()
    }

    @Bean(APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    override fun getAsyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()

        executor.initialize()
        executor.setTaskDecorator(::decorateUsingParentContext)

        return DelegatingSecurityContextAsyncTaskExecutor(executor)
    }

    @Bean
    fun applicationDispatcher(applicationTaskExecutor: Executor): CoroutineDispatcher {
        return applicationTaskExecutor.asCoroutineDispatcher()
    }

    private fun decorateUsingParentContext(task: Runnable): Runnable {
        val attributes = RequestContextHolder.getRequestAttributes()
        val contextMap = MDC.getCopyOfContextMap()

        return if (attributes == null && contextMap == null) task else Runnable {
            try {
                RequestContextHolder.setRequestAttributes(attributes)
                MDC.setContextMap(contextMap ?: emptyMap())
                task.run()
            } finally {
                MDC.clear()
                RequestContextHolder.resetRequestAttributes()
            }
        }
    }
}
