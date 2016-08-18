package com.andre.adapters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter class to handle JSON Date serialization and deserialization.
 * 
 * @author André Leite
 */
public class DateFormatterAdapter extends XmlAdapter<String, Date> {
	
	private final Logger logger = LoggerFactory.getLogger(DateFormatterAdapter.class);
	private final String dateFormat = "yyyy-MM-dd";
	
	@Override
	public Date unmarshal(final String dateString) throws Exception {
		// Validate provided date format...
		Date date = null;
		try {
		    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    date = sdf.parse(dateString);
		    if (!dateString.equals(sdf.format(date))) {
		    	logger.debug("Wrong date format : "+dateString);
		    	throw new Exception("Wrong Date Format");
		    }
		} catch (ParseException ex) {
			logger.debug("Wrong date format : "+ex);
			throw new Exception("Wrong Date Format" +ex);
		}
		return date;
	}

	@Override
	public String marshal(Date v) throws Exception {
		return new SimpleDateFormat(dateFormat).format(v);
	}
}