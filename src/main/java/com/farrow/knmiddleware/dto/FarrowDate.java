package com.farrow.knmiddleware.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FarrowDate {

	public static Date parse(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = sdf.parse(dateString);
		return parsedDate;
	}
}
