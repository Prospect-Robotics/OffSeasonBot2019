package com.team2813.lib.sparkMax;

import com.revrobotics.CANError;

public class SparkMaxException extends Exception {

	CANError canError;
	String message;

	public SparkMaxException(CANError canError, String message) {
		super();
		this.canError = canError;
		this.message = message;
	}

	public SparkMaxException(CANError canError) {
		this(canError, canError.toString());
	}

	public SparkMaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public SparkMaxException(String message, SparkMaxException cause) {
		super(message, cause);
		canError = cause.canError;
	}

	public static void throwIfNotOk(String message, CANError canError) throws SparkMaxException {
		if (canError != CANError.kOk) {
			throw new SparkMaxException(canError, message);
		}
	}

	public static void throwIfNotOk(CANError canError) throws SparkMaxException {
		if (canError != CANError.kOk) {
			throw new SparkMaxException(canError);
		}
	}
}
