package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebFilter("/*")
public class LoggingFilter implements Filter {




    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request =  (HttpServletRequest) req;

        long startTime = System.currentTimeMillis();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        chain.doFilter(req, resp);

        long duration = System.currentTimeMillis() - startTime;

        HttpServletResponse response = (HttpServletResponse) resp;
        System.out.printf("[%s] %s %s%s | IP: %s | Status: %d | Time: %dms%n", timestamp, method, uri,
                queryString != null ? "?" + queryString : "",
                clientIP, response.getStatus(), duration);

    }

    @Override
    public void destroy() {}
}
