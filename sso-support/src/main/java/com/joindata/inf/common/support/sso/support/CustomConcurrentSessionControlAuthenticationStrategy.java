package com.joindata.inf.common.support.sso.support;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

/**
 * 自定义并发Session Strateay
 * 
 * @author <a href="mailto:zhangkai@joindata.com">Zhang Kai</a>
 * @since 2016年12月14日
 */
public class CustomConcurrentSessionControlAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy
{
    private final SessionRegistry sessionRegistry;

    private boolean exceptionIfMaximumExceeded = false;

    public CustomConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry)
    {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
    {
        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(authentication.getPrincipal(), true);

        int sessionCount = sessions.size();
        int allowedSessions = getMaximumSessionsForThisUser(authentication);

        if(sessionCount < allowedSessions)
        {
            return;
        }

        if(allowedSessions == -1)
        {
            return;
        }
        HttpSession session;
        if(sessionCount == allowedSessions)
        {
            session = request.getSession(false);

            if(session != null)
            {
                for(SessionInformation si: sessions)
                {
                    if(si.getSessionId().equals(session.getId()))
                    {
                        return;
                    }
                }

            }

        }

        allowableSessionsExceeded(sessions, allowedSessions, this.sessionRegistry);
    }

    protected void allowableSessionsExceeded(List<SessionInformation> sessions, int allowableSessions, SessionRegistry registry) throws SessionAuthenticationException
    {
        if((this.exceptionIfMaximumExceeded) || (sessions == null))
        {
            throw new SessionAuthenticationException(this.messages.getMessage("ConcurrentSessionControlAuthenticationStrategy.exceededAllowed", new Object[]{Integer.valueOf(allowableSessions)}, "Maximum sessions of {0} for this principal exceeded"));
        }

        SessionInformation leastRecentlyUsed = null;

        for(SessionInformation session: sessions)
        {
            if((leastRecentlyUsed == null) || (session.getLastRequest().before(leastRecentlyUsed.getLastRequest())))
            {
                leastRecentlyUsed = session;
            }
        }

        leastRecentlyUsed.expireNow();
        // 如果存储在Redis或者其他第三方存储中，需要将 session information 给删除
        registry.removeSessionInformation(leastRecentlyUsed.getSessionId());
    }

    public void setExceptionIfMaximumExceeded(boolean exceptionIfMaximumExceeded)
    {
        super.setExceptionIfMaximumExceeded(exceptionIfMaximumExceeded);
        this.exceptionIfMaximumExceeded = exceptionIfMaximumExceeded;
    }

}
