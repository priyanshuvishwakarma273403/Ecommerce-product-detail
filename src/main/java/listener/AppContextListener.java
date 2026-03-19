package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import util.DBUtil;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce){
        System.out.println("========================================");
        System.out.println(" Application Starting...");
        System.out.println(" Context Path: " + sce.getServletContext().getContextPath());
        System.out.println(" Server Info: " + sce.getServletContext().getServerInfo());
        System.out.println("========================================");

        sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
        sce.getServletContext().setAttribute("onlineUsers", 0);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce){
        System.out.println("Application Shutting Down...");
        DBUtil.close();
    }
}
