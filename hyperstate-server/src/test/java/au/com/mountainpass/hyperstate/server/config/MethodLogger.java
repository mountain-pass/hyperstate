package au.com.mountainpass.hyperstate.server.config;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MethodLogger {

    private static final String POINT_CUT = "execution(* au.com.mountainpass.hyperstate.server.InMemoryEntityRepository.*(..))";

    private static ForkJoinPool executer = new ForkJoinPool(1);

    @Before(POINT_CUT)
    public void logMethodAccessBefore(JoinPoint joinPoint) {
        CompletableFuture.runAsync(() -> {
            Logger LOGGER = getLogger(joinPoint);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("{}(..): @Before",
                        joinPoint.getSignature().getName());
                Object[] args = joinPoint.getArgs();
                for (int i = 0; i < args.length; ++i) {
                    LOGGER.debug("  arg[{}]: '{}'", i, args[i]);
                }
            }
        } , executer);
    }

    private Logger getLogger(JoinPoint joinPoint) {
        Logger LOGGER = LoggerFactory
                .getLogger(joinPoint.getTarget().getClass().getName());
        return LOGGER;
    }

    @AfterReturning(pointcut = POINT_CUT, returning = "rval")
    public void logMethodAccessAfter(JoinPoint joinPoint, Object rval) {
        CompletableFuture.runAsync(() -> {
            Logger LOGGER = getLogger(joinPoint);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("{}(..): @AfterReturning",
                        joinPoint.getSignature().getName());
                if (rval != null && CompletableFuture.class
                        .isAssignableFrom(rval.getClass())) {
                    CompletableFuture<?> future = (CompletableFuture<?>) rval;
                    future.thenAccept(completed -> {
                        LOGGER.debug("{}(..): @AfterReturning async",
                                joinPoint.getSignature().getName());
                        LOGGER.debug("  rval: {}", completed);
                    });
                } else {
                    LOGGER.debug("  rval: {}", rval);
                }
            }
        } , executer);
    }

    @AfterThrowing(pointcut = POINT_CUT, throwing = "ex")
    public void doRecoveryActions(JoinPoint joinPoint, Object ex) {
        CompletableFuture.runAsync(() -> {
            Logger LOGGER = getLogger(joinPoint);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("{}(..): @AfterThrowing",
                        joinPoint.getSignature().getName());
                LOGGER.debug("  exception: {}", ex);
            }
        } , executer);
    }
}
