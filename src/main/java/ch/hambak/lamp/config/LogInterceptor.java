package ch.hambak.lamp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String logId = UUID.randomUUID().toString();

        log.info("REQUEST[{}]: [{} {}]", logId, method, uri);

        request.setAttribute("logId", logId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String logId = (String) request.getAttribute("logId");

        log.info("RESPONSE[{}]: [{} {}]", logId, method, uri);

        if (ex != null) {
            log.error("ERROR", ex);
        }
    }
}
