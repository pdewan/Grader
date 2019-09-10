package grader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SakaiDateUtil {
	private static final String SAKAI_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
	private static final String SAKAI_DATE_FORMAT_OLD = "yyyyMMddhhmmssSSS";

	public static Date toDate(String timestamp) {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(SAKAI_DATE_FORMAT);
			date = dateFormat.parse(timestamp);
		} catch (ParseException e) {
		    try {
    			SimpleDateFormat dateFormat = new SimpleDateFormat(SAKAI_DATE_FORMAT_OLD);
    			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    			date = dateFormat.parse(timestamp);
		    } catch (ParseException e2) {
		    }
		}
		return date;
	}
}
