package org.neuclear.commons.crypto.passphraseagents;

import org.neuclear.commons.crypto.signers.BrowsableSigner;
import org.neuclear.commons.crypto.signers.SetPublicKeyCallBack;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * PassPhraseAgent for use in server environments.
 * You set the passphrase first by passing it a signers request. It fetches the passphrase from the paramater "passphrase"<p>
 * <b>WARNING</b> The sequence of events is very important for the safe usage of this. The following is an example within a
 * standard <tt>doPost()</tt> method.
 * <ol>
 * <li><tt>agent.setRequest(request); // Set the request</tt>
 * <li><tt>signer.sign(data);// Sign some data</tt>
 * <li><tt>agent.clear();// The moment we have used the passphrase we have to clear it</tt>
 * </ol>
 * Note, the above assumes that a <tt>Signer</tt> with the name signer was initialised in <tt>init()</tt> with a
 * <tt>ServletPassPhraseAgent</tt> called <tt>agent</tt>.
 *
 * @see org.neuclear.commons.crypto.signers.Signer
 */
public class ServletPassPhraseAgent extends ThreadLocal implements InteractiveAgent {

    /**
     * Set the passphrase from the request object.
     *
     * @param request
     */
    public void setRequest(HttpServletRequest request) {
        set(request.getParameter("passphrase"));
    }

    /**
     * Gets the passphrase if set or null
     *
     * @param name
     * @return
     */
    public char[] getPassPhrase(String name) {
        return (get() == null ? null : ((String) get()).toCharArray());
    }

    /**
     * Clears the passphrase. (Important, you have to manually call this at the end of the request code, or better yet
     * immediately after using your Signer).
     */
    public void clear() {
        set(null);
    }

    public char[] getPassPhrase(String name, boolean incorrect) throws UserCancellationException {
        return getPassPhrase(name);
    }

    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws UserCancellationException
     */
    public char[] getPassPhrase(BrowsableSigner signer) throws UserCancellationException {
        return new char[0];
    }

    /**
     * The User is asked to pick a name by the PassPhraseAgent. The PassPhraseAgent can query the given signer for
     * a list of included aliases or even create a new keypair.
     *
     * @return
     * @throws UserCancellationException
     */
    public byte[] sign(BrowsableSigner signer, byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        return new byte[0];
    }

    public File getSaveToFileName(String title, String def) throws UserCancellationException {
        return null;
    }

    public File getOpenFileName(String title, String def) throws UserCancellationException {
        return null;
    }

    public char[] getNewPassPhrase(String name) throws UserCancellationException {
        return new char[0];
    }
}
