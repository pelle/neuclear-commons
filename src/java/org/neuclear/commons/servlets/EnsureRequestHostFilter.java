package org.neuclear.commons.servlets;

import org.neuclear.commons.Utility;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter that only allows requests from the given ip address. Defaults to 127.0.0.1 (localhost).
 * This can be set in the init parameter <tt>allowip</tt>
 */
public class EnsureRequestHostFilter implements Filter{
    public void init(FilterConfig config) throws ServletException {
        allowed=Utility.denullString(config.getInitParameter("allowip"),"127.0.0.1");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request.getRemoteAddr().equals(allowed))
            chain.doFilter(request, response);
    }

    public void destroy() {

    }
    private String allowed;
}
