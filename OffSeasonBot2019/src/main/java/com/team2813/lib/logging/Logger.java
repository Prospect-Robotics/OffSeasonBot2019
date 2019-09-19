package com.team2813.lib.logging;

import java.util.Arrays;

// import com.team2813.lib.util.Debug;

public class Logger {
	
	public static LogLevel logLevel = LogLevel.WARNING;

	private static boolean shouldLog(LogLevel level){
		return logLevel.canSee(level);
	}

	public static void logDelim(LogLevel level, String delimeter, Object first, Object...objects){
		if(!shouldLog(level)) return;

		StringBuilder sb = new StringBuilder();
		
		sb.append(String.valueOf(first));
		
		for(Object o : objects){
			sb.append(delimeter);
			sb.append(String.valueOf(o));
		}
		
		level.printStream.println(sb);
	}

	public static void log(LogLevel level, Object first, Object...objects){
		logDelim(level, " ", first, objects);
	}
	
	public static void log(LogLevel level, Throwable t){
		if(shouldLog(level)) t.printStackTrace(level.printStream);
	}

	public static void logf(LogLevel level, String format, Object...args){
		if(shouldLog(level)) level.printStream.printf(format, args);
	}

	/**
	 * prints the log along with the class, method, and line number of the caller
	 */
	public static void debugLog(LogLevel level, Object first, Object...objects){
		debugLog_(level, 2, first, objects);
	}
	
	//TODO better name
	/**
	 * @param offset target's offset from end of trace
	 */
	static void debugLog_(LogLevel level, int offset, Object first, Object...objects){
		if(!shouldLog(level)) return;
		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
		StackTraceElement targetTraceElement = traceElements[Math.min(offset+2, traceElements.length-1)];
		System.out.println(Arrays.toString(traceElements));


		StringBuilder sb = new StringBuilder();

		sb.append(targetTraceElement.getFileName());
		sb.append(':');
		sb.append(targetTraceElement.getLineNumber());
		sb.append(" - ");
		sb.append(targetTraceElement.getMethodName());
		sb.append(" - ");

		sb.append(String.valueOf(first));
		for(Object o : objects){
			sb.append(" ");
			sb.append(String.valueOf(o));
		}
		level.printStream.println(sb);
	}

}