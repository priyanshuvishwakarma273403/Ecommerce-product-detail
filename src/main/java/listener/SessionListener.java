package listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;

@WebListener
public class SessionListener implements HttpSessionListener , HttpSessionAttributeListener {

    @Override
    public void sessionCreated(HttpSessionEvent se){
        System.out.println("Session Created..."+se.getSession().getId());
        incrementOnlineUsers(se, 1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se){
        System.out.println("Session Destroyed..."+se.getSession().getId());
        incrementOnlineUsers(se,-1);
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event){
        if("user".equals(event.getName())){
            System.out.println("User logged in: "+event.getValue());
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event){
        if("user".equals(event.getName())){
            System.out.println("User logged out: "+event.getValue());
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
      // Called when attribute value is replaced
    }

    private void incrementOnlineUsers(HttpSessionEvent se, int delta) {
        var ctx = se.getSession().getServletContext();
        Integer count = (Integer) ctx.getAttribute("onlineUsers");
        if (count == null) count = 0;
        ctx.setAttribute("onlineUsers", Math.max(0, count + delta));
    }



}
