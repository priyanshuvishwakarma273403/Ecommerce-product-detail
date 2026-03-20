package servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.Set;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    private static final Set<String> ADMIN_ROLES = Set.of("ADMIN", "MODERATOR");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        if(session != null){
            User user = (User) session.getAttribute("user");
            if (user != null && ADMIN_ROLES.contains(user.getRole())) {
                chain.doFilter(req, resp);
                return;
            }

            request.setAttribute("error", "Access denied. Admin privileges required.");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
