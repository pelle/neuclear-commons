package org.neuclear.commons.time;

import java.util.Date;

/**
 * 
 * User: pelleb
 * Date: Aug 18, 2003
 * Time: 4:10:35 PM
 */
public class TimeTools {
    /** Please dont instantiate
     *
     */
    private TimeTools() {
        ;
    }

    /**
     * Really just a simple placeholder.
     * Eventually this should return UTC current time.
     * @return
     */
    public static Date now() {
        return new Date();
    }
}
