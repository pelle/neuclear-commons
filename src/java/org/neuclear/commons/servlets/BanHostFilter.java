package org.neuclear.commons.servlets;

import org.neuclear.commons.Utility;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter that only allows requests from the given ip address. Defaults to 127.0.0.1 (localhost).
 * This can be set in the init parameter <tt>allowip</tt>
 */
public class BanHostFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        bannedlist = new File(config.getServletContext().getRealPath(Utility.denullString(config.getInitParameter("banned"), "WEB-INF/banned.txt")));
        lastchanged = 0;
        System.out.println("Initializing BanHostFilter using: " + bannedlist.getAbsolutePath());
        reloadBannedList();

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        reloadBannedList();
        System.out.println("Filtering request from " + request.getRemoteHost());
        if (!banned.contains(request.getRemoteAddr()))
            chain.doFilter(request, response);
        else {
            ((HttpServletResponse) response).sendError(403, "Not Available");
//            response.getWriter().println("\n\n");
//            response.flushBuffer();

        }
    }

    public void destroy() {

    }

    public synchronized void reloadBannedList() {
        if (bannedlist.lastModified() > lastchanged) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(bannedlist));
                banned = new HashSet();
                String line = reader.readLine();
                while (!Utility.isEmpty(line)) {
                    banned.add(line);
                    line = reader.readLine();
                }
                lastchanged = bannedlist.lastModified();
                reader.close();
                System.out.println("RELOADED BANNED IP LIST");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (banned == null)
            banned = new HashSet();
    }

    private long lastchanged;
    private File bannedlist;
    private String allowed;
    private Set banned;
}
