package com.sparta.eng68.traineetracker.security;

import org.hibernate.SessionFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public FailureHandler(String defaultFailureUrl) {
        super();
        setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        // TODO Auto-generated method stub

        String userName = request.getParameter("username");
        /*System.out.println("CustomFilter Begins");
        System.out.println("CustomeFilter.username :: " + userName);
        System.out.println("getMessage :: " + exception.getMessage());
        System.out.println("exception :: " + exception.getClass().getSimpleName());
        System.out.println("RemoteAddr :: " + request.getRemoteAddr());        */
        super.onAuthenticationFailure(request, response, exception);
    }
}
