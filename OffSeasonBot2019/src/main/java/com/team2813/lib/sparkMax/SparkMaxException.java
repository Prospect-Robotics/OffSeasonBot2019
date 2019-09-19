package com.team2813.lib.sparkMax;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMaxLowLevel.ParameterStatus;

public class SparkMaxException extends Exception {

	CANError canError;
	ParameterStatus parameterStatus;
	String message;

	public SparkMaxException(CANError canError, String message) {
		super();
		this.canError = canError;
		this.message = message;
	}

	public SparkMaxException(CANError canError) {
		this(canError, canError.toString());
	}

	public SparkMaxException(ParameterStatus parameterStatus, String message) {
		super();
		this.parameterStatus = parameterStatus;
		this.message = message;
	}

	public SparkMaxException(ParameterStatus parameterStatus) {
		this(parameterStatus, parameterStatus.toString());
	}

	public SparkMaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public SparkMaxException(String message, SparkMaxException cause) {
		super(message, cause);
		canError = cause.canError;
		parameterStatus = cause.parameterStatus;
	}

	public static void throwIfNotOk(String message, CANError canError) throws SparkMaxException {
		if (canError != CANError.kOK) {
			throw new SparkMaxException(canError, message);
		}
	}

	public static void throwIfNotOk(CANError canError) throws SparkMaxException {
		if (canError != CANError.kOK) {
			throw new SparkMaxException(canError);
		}
	}

	public static void throwIfNotOk(String message, ParameterStatus parameterStatus) throws SparkMaxException {
		if (parameterStatus != ParameterStatus.kOK) {
			throw new SparkMaxException(parameterStatus, message);
		}
	}

	public static void throwIfNotOk(ParameterStatus parameterStatus) throws SparkMaxException {
		if (parameterStatus != ParameterStatus.kOK) {
			throw new SparkMaxException(parameterStatus);
		}
	}

}
