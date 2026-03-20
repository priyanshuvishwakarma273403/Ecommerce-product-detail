package servlet;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie [] cookies = req.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if("rememberedUser".equals(cookie.getName())){
                    req.setAttribute("rememberedUser",cookie.getValue());
                }
            }
        }

        req.getRequestDispatcher("/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        Optional<User> userOpt = userDAO.authenticate(username, password);

        if(userOpt.isPresent()){
            User user = userOpt.get();

            HttpSession session = req.getSession(true);
            session.setAttribute("user",user);
            session.setAttribute("role",user.getRole());
            session.setAttribute("loginTime", System.currentTimeMillis());
            session.setMaxInactiveInterval(30*60);


            if("on".equals(rememberMe)){
                Cookie cookie = new Cookie("rememberedUser",username);
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                resp.addCookie(cookie);
            }

            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if(redirectUrl!=null){
                session.removeAttribute("redirectAfterLogin");
                resp.sendRedirect(redirectUrl);
            } else{
                resp.sendRedirect(req.getContextPath()+ "/dashboard.jsp");
            }
        } else{
            req.setAttribute("error","Invalid username or password");
            req.setAttribute("username",username);
            req.getRequestDispatcher("/login.jsp").forward(req,resp);
        }
    }
}
