package org.neuclear.commons.crypto.passphraseagents;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 14, 2003
 * Time: 2:06:29 PM
 * To change this template use Options | File Templates.
 */
public class MockPassphraseServlet extends HttpServlet implements PassPhraseAgent{
    public MockPassphraseServlet() {
        this.agent = new ServletPassPhraseAgent();
    }

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        agent.setRequest(httpServletRequest);
    }

    public char[] getPassPhrase(String name) {
        return agent.getPassPhrase(name);  //To change body of implemented methods use Options | File Templates.
    }

    public char[] getPassPhrase(String name, boolean incorrect) throws UserCancellationException {
        return getPassPhrase(name);
    }

    private ServletPassPhraseAgent agent;
}
