package org.neuclear.commons.crypto.passphraseagents;

import javax.servlet.http.HttpServletRequest;

/**
 * PassPhraseAgent for use in server environments.
 * You set the passphrase first by passing it a servlet request. It fetches the passphrase from the paramater "passphrase"<p>
 * <b>WARNING</b> The sequence of events is very important for the safe usage of this. The following is an example within a
 * standard <tt>doPost()</tt> method.
 * <ol>
 * <li><tt>agent.setRequest(request); // Set the request</tt>
 * <li><tt>signer.sign(data);// Sign some data</tt>
 * <li><tt>agent.clear();// The moment we have used the passphrase we have to clear it</tt>
 * </ol>
 * Note, the above assumes that a <tt>Signer</tt> with the name signer was initialised in <tt>init()</tt> with a
 * <tt>ServletPassPhraseAgent</tt> called <tt>agent</tt>.
 * @see org.neuclear.commons.crypto.signers.Signer
 */
public class ServletPassPhraseAgent extends ThreadLocal implements InteractiveAgent {

    /**
     * Set the passphrase from the request object.
     * @param request
     */
    public void setRequest(HttpServletRequest request){
        set(request.getParameter("passphrase"));
    }
    /**
     * Gets the passphrase if set or null
     * @param name
     * @return
     */
    public char[] getPassPhrase(String name) {
        return (get()==null?null:((String)get()).toCharArray());
    }
    /**
     * Clears the passphrase. (Important, you have to manually call this at the end of the request code, or better yet
     * immediately after using your Signer).
     */
    public void clear(){
        set(null);
    }
}
