/*
 * $Id: TimeTools.java,v 1.9 2004/02/19 15:29:11 pelle Exp $
 * $Log: TimeTools.java,v $
 * Revision 1.9  2004/02/19 15:29:11  pelle
 * Various cleanups and corrections
 *
 * Revision 1.8  2004/01/03 20:36:33  pelle
 * Renamed HeldTransfer to Exchange
 * Dropped valuetime from the request objects.
 * Doesnt yet compile. New commit to follow soon.
 *
 * Revision 1.7  2003/12/31 00:39:29  pelle
 * Added Drivers for handling different Database dialects in the entity model.
 * Added Statement pattern to ledger, simplifying the statement writing process.
 *
 * Revision 1.6  2003/12/24 00:25:41  pelle
 * Created a kind of poor man's version of ofbiz.org's EntityEngine. It doesnt use xml to configure it, but code.
 * Should mainly be used to create tables. Can also insert rows, but hasnt been thoroughly tested.
 * At some point I will improve that part and add some kind of smart querying engine to it. Similar to EntityEngine. But I dont
 * need that myself right now.
 * SQLLedger now uses this to create its tables. It is not fully working yet, but will be shortly.
 *
 * Revision 1.5  2003/12/19 18:02:53  pelle
 * Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
 * - For most cases the main exception to worry about now is InvalidNamedObjectException.
 * - Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
 *   runtime exception.
 * - Source and Store patterns each now have their own exceptions that generalizes the various physical
 *   exceptions that can happen in that area.
 *
 * Revision 1.4  2003/12/19 00:31:16  pelle
 * Lots of usability changes through out all the passphrase agents and end user tools.
 *
 * Revision 1.3  2003/11/21 04:43:42  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.2  2003/11/11 21:17:49  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/10/21 22:31:14  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
 *
 * Revision 1.2  2003/09/22 19:24:02  pelle
 * More fixes throughout to problems caused by renaming.
 *
 * Revision 1.1.1.1  2003/09/19 14:41:15  pelle
 * First import into the neuclear project. This was originally under the SF neuclear
 * project. This marks a general major refactoring and renaming ahead.
 *
 * The new name for this code is NeuClear Identity and has the general package header of
 * org.neuclear.id
 * There are other areas within the current code which will be split out into other subprojects later on.
 * In particularly the signers will be completely seperated out as well as the contract types.
 *
 *
 * Revision 1.3  2003/02/10 22:30:22  pelle
 * Got rid of even further dependencies. In Particular OSCore
 *
 * Revision 1.2  2003/02/09 00:15:56  pelle
 * Fixed things so they now compile with r_0.7 of XMLSig
 *
 * Revision 1.1  2003/02/09 00:03:10  pelle
 * Clean ups. Got rid of things that have been moved elsewhere
 *
 * Revision 1.1  2003/01/18 18:12:30  pelle
 * First Independent commit of the Independent XML-Signature API for NeuDist.
 *
 * Revision 1.2  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *

*/
package org.neuclear.commons.time;

import org.neuclear.commons.Utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * @author
 * @version 1.0
 */

public final class TimeTools {
    private TimeTools() {
    }


    public static Date addDaysToDate(final Date d, final int iDays) {
        //create Calendar
        final Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        //add days
        cal.add(Calendar.DATE, iDays);

        //create Date
        return cal.getTime();
    }

    public static Date addMonthsToDate(final Date d, final int iMonths) {
        //create Calendar
        final Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        //add months
        cal.add(Calendar.MONTH, iMonths);

        //create Date
        return cal.getTime();
    }

    public static String createTimeStamp() {
        return formatTimeStamp(new Date());
    }

    public static Timestamp convertDateToTimestamp(final Date date) {
        if (date instanceof Timestamp)
            return (Timestamp) date;
        return new Timestamp(date.getTime());
    }

    public static Timestamp parseTimeStamp(final String ts) throws ParseException {
        if (Utility.isEmpty(ts))
            return null;
        return convertDateToTimestamp(getDateFormatter().parse(ts));

    }

    public static String formatTimeStamp(final Date time) {
        return getDateFormatter().format(time);
    }

    public static String formatTimeStampShort(final Date time) {
        return DFS.format(time);
    }

    private static SimpleDateFormat getDateFormatter() {
        return DF;
    }

    public static Date get2020() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Base ISO 8601 Date format yyyy-MM-ddTHH:mm:ss,SSSzzz i.e., 2002-12-25T14:32:12,333-500 for
     * the 25th day of December in the year 2002 at 2 pm 32 min 12 secs 333 mills -5 hours from GMT
     */
    private static final String ISO_EXPANDED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSSzzz";
    private static final SimpleDateFormat DF = new SimpleDateFormat(ISO_EXPANDED_DATE_TIME_FORMAT);

    {
        DF.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
//    private static final String SHORT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSSzzz";
    private static final SimpleDateFormat DFS = new SimpleDateFormat();

    {
//        DFS.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * Really just a simple placeholder.
     * Eventually this should return UTC current time.
     *
     * @return
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
