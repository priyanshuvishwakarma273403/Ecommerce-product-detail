package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login", "/login.jsp", "/register", "/register.jsp",
            "/css/", "/js/", "/images/", "/error/"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getRequestURI().substring(request.getContextPath().length());
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);

        if(isPublic || "/".equals(path)) {
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if(isLoggedIn) {
            chain.doFilter(req, resp);
        } else {
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("redirectAfterLogin",
                    request.getRequestURI() +
                            (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
