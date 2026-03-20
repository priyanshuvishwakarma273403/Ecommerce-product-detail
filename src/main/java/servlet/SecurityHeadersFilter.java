package servlet;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse resp =  (HttpServletResponse) response;

        // Prevent clickjacking
        resp.setHeader("X-Frame-Options", "DENY");

        resp.setHeader("X-XSS-Protection", "1; mode=block");

        resp.setHeader("X-Content-Type-Options", "nosniff");

        resp.setHeader("Strict-Transport-Security",
                "max-age=31536000; includeSubDomains");

        resp.setHeader("Content-Security-Policy",
                "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'");
// Prevent caching of sensitive data
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
