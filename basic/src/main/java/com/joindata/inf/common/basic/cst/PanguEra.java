package com.joindata.inf.common.basic.cst;

import java.text.ParseException;
import java.util.Date;

/**
 * Pangu 纪元
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 1, 2017 7:17:07 PM
 */
public class PanguEra
{
    public static final String BASE_DATE_STR = "2017-4-1";

    public static final Date BASE_DATE = parseDate();

    private static Date parseDate()
    {
        try
        {
            return DateFormatterCst.DEFAULT_DATE_FORMATTER.parse(BASE_DATE_STR);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
