package com.farrow.knmiddleware.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;

public class ExceptionUtils {
	
	private ExceptionUtils() {}


    public static String getStackTrace(Throwable t) {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        PrintStream tempStream = new PrintStream(byteOut);
        t.printStackTrace(tempStream);
        tempStream.flush();
        return StringUtils.substring(byteOut.toString(), 0, 31000);
    }
}
