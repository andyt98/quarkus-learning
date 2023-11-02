package com.andy.interceptors;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.logging.Logger;

@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object logMethodInvocation(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        logger.info("Entering method: " + methodName);
        try {
            // Proceed with the original method invocation
            return context.proceed();
        } finally {
            logger.info("Exiting method: " + methodName);
        }
    }
}