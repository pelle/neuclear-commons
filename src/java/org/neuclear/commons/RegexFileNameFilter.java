package org.neuclear.commons;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Feb 21, 2003
 * Time: 11:00:18 AM
 * $Id: RegexFileNameFilter.java,v 1.2 2003/11/21 04:43:42 pelle Exp $
 * $Log: RegexFileNameFilter.java,v $
 * Revision 1.2  2003/11/21 04:43:42  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.1  2003/11/11 21:17:51  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/10/22 22:11:58  pelle
 * Replaced the dependency for the Apache Regex library with JDK1.4's Regex implementation.
 * Changed the valid format of NeuClear ID's to include neu://bob@hello/ formatted ids.
 * These ids are not identical to neu://hello/bob however in both cases neu://hello has to sign the Identity document.
 *
 * Revision 1.2  2003/10/21 22:30:33  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
 * <p/>
 * Revision 1.1  2003/02/21 22:48:12  pelle
 * New Test Infrastructure
 * Added test keys in src/testdata/keys
 * Modified tools to handle these keys
 */
public final class RegexFileNameFilter implements FilenameFilter {
    public RegexFileNameFilter(final String regex) {
        this.re = Pattern.compile(regex);
    }

    /**
     * Tests if a specified file should be included in a file list.
     * 
     * @param dir  the directory in which the file was found.
     * @param name the name of the file.
     * @return <code>true</code> if and only if the name should be
     *         included in the file list; <code>false</code> otherwise.
     */
    public final boolean accept(final File dir, final String name) {
        return (re.matcher(name).matches());
    }

    private final Pattern re;
}
